package com.teamname.uniroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.teamname.uniroll.database.AppDatabase;
import com.teamname.uniroll.database.entity.Enrollment;
import com.teamname.uniroll.database.entity.Subject;

import java.util.List;

public class StudentDashboardActivity extends AppCompatActivity {

    private LinearLayout subjectsContainer;
    private Button btnMySubjects, btnLogout;
    private AppDatabase db;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        subjectsContainer = findViewById(R.id.subjectsContainer);
        btnMySubjects = findViewById(R.id.btnMySubjects);
        btnLogout = findViewById(R.id.btnLogout);

        db = AppDatabase.getInstance(this);
        studentId = getLoggedInStudentId();

        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StudentDashboardActivity.this, LoginActivity.class));
            finish();
            return;
        }

        loadAllSubjects();

        btnMySubjects.setOnClickListener(v -> {
            Intent intent = new Intent(StudentDashboardActivity.this, MySubjectsActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private int getLoggedInStudentId() {
        SharedPreferences session = getSharedPreferences("UniRollSession", MODE_PRIVATE);

        int id = session.getInt("USER_ID", -1);

        if (id == -1) {
            id = session.getInt("id", -1);
        }

        if (id == -1) {
            id = session.getInt("studentId", -1);
        }

        return id;
    }

    private void loadAllSubjects() {
        new Thread(() -> {
            List<Subject> subjects = db.subjectDao().getAllSubjects();

            runOnUiThread(() -> {
                subjectsContainer.removeAllViews();

                if (subjects == null || subjects.isEmpty()) {
                    TextView emptyText = new TextView(this);
                    emptyText.setText("No subjects available yet.");
                    emptyText.setTextSize(16);
                    emptyText.setTextColor(0xFF777777);
                    emptyText.setPadding(10, 20, 10, 20);
                    subjectsContainer.addView(emptyText);
                    return;
                }

                for (Subject subject : subjects) {
                    addSubjectCard(subject);
                }
            });
        }).start();
    }

    private void addSubjectCard(Subject subject) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(0xFFFFFFFF);
        card.setPadding(30, 25, 30, 25);

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 20);
        card.setLayoutParams(cardParams);

        TextView subjectName = new TextView(this);
        subjectName.setText(subject.subjectName);
        subjectName.setTextSize(18);
        subjectName.setTextColor(0xFF333333);
        subjectName.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView subjectDetails = new TextView(this);
        subjectDetails.setText(subject.subjectCode + "\n" + subject.creditHours + " credit hours");
        subjectDetails.setTextSize(14);
        subjectDetails.setTextColor(0xFF777777);
        subjectDetails.setPadding(0, 8, 0, 8);

        Button enrollButton = new Button(this);
        enrollButton.setText("ENROLL");
        enrollButton.setOnClickListener(v -> enrollSubject(subject.id));

        card.addView(subjectName);
        card.addView(subjectDetails);
        card.addView(enrollButton);

        subjectsContainer.addView(card);
    }

    private void enrollSubject(int subjectId) {
        new Thread(() -> {
            int alreadyEnrolled = db.enrollmentDao().isEnrolled(studentId, subjectId);

            if (alreadyEnrolled > 0) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Already enrolled in this subject", Toast.LENGTH_SHORT).show()
                );
            } else {
                db.enrollmentDao().insert(new Enrollment(studentId, subjectId));

                runOnUiThread(() ->
                        Toast.makeText(this, "Enrolled successfully", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void logoutUser() {
        SharedPreferences session = getSharedPreferences("UniRollSession", MODE_PRIVATE);
        session.edit().clear().apply();

        Intent intent = new Intent(StudentDashboardActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}