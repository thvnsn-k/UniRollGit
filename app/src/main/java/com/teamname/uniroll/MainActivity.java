package com.teamname.uniroll;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.content.SharedPreferences sharedPreferences = getSharedPreferences("UniRollSession", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);
        String userRole = sharedPreferences.getString("USER_ROLE", null);

        if (userId != -1 && userRole != null) {
            // User is logged in, redirect to correct dashboard
            android.content.Intent intent;
            if ("LECTURER".equals(userRole)) {
                intent = new android.content.Intent(MainActivity.this, LecturerDashboardActivity.class);
            } else {
                intent = new android.content.Intent(MainActivity.this, StudentDashboardActivity.class);
            }
            startActivity(intent);
            finish();
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btnGoToLogin).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}