package com.teamname.uniroll.utils;

import android.content.Context;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;

public class ValidationUtils {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return !isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidCreditHours(int hours) {
        return hours >= 1 && hours <= 6;
    }

    public static boolean isValidSubjectCode(String code) {
        return !isEmpty(code) && code.matches("[A-Za-z]{2,4}[0-9]{2,4}");
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackbarError(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.setBackgroundTint(0xFFB00020);
        snackbar.show();
    }

    public static boolean validateLoginForm(Context context, String email, String password) {
        if (isEmpty(email)) {
            showToast(context, "Email cannot be empty");
            return false;
        }
        if (!isValidEmail(email)) {
            showToast(context, "Please enter a valid email");
            return false;
        }
        if (isEmpty(password)) {
            showToast(context, "Password cannot be empty");
            return false;
        }
        return true;
    }

    public static boolean validateRegisterForm(Context context, String name,
                                               String email, String password, String role) {
        if (isEmpty(name)) {
            showToast(context, "Name cannot be empty");
            return false;
        }
        if (!isValidEmail(email)) {
            showToast(context, "Please enter a valid email");
            return false;
        }
        if (!isValidPassword(password)) {
            showToast(context, "Password must be at least 6 characters");
            return false;
        }
        if (isEmpty(role)) {
            showToast(context, "Please select a role");
            return false;
        }
        return true;
    }

    public static boolean validateSubjectForm(Context context, String name,
                                              String code, String creditHoursStr) {
        if (isEmpty(name)) {
            showToast(context, "Subject name cannot be empty");
            return false;
        }
        if (!isValidSubjectCode(code)) {
            showToast(context, "Subject code must be like CS101 or BIT203");
            return false;
        }
        if (isEmpty(creditHoursStr)) {
            showToast(context, "Credit hours cannot be empty");
            return false;
        }
        try {
            int hours = Integer.parseInt(creditHoursStr);
            if (!isValidCreditHours(hours)) {
                showToast(context, "Credit hours must be between 1 and 6");
                return false;
            }
        } catch (NumberFormatException e) {
            showToast(context, "Credit hours must be a number");
            return false;
        }
        return true;
    }
}