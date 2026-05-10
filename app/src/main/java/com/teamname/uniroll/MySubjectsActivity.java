package com.teamname.uniroll;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.teamname.uniroll.database.AppDatabase;
import com.teamname.uniroll.database.entity.Subject;

import java.util.List;

public class MySubjectsActivity extends AppCompatActivity {

    private LinearLayout subjectsContainer;
    private Button btnBack;
    private AppDatabase db;
    private int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subjects);

        subjectsContainer = findViewById(R.id.subjectsContainer);
        btnBack = findViewById(R.id.btnBack);

        db = AppDatabase.getInstance(this);
        studentId = getLoggedInStudentId();

        if (studentId == -1) {
            Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadEnrolledSubjects();

        btnBack.setOnClickListener(v -> finish());
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

    private void loadEnrolledSubjects() {
        new Thread(() -> {
            List<Subject> subjects = db.enrollmentDao().getEnrolledSubjects(studentId);

            runOnUiThread(() -> {
                subjectsContainer.removeAllViews();

                if (subjects == null || subjects.isEmpty()) {
                    TextView emptyText = new TextView(this);
                    emptyText.setText("No subjects enrolled yet.");
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

        Button dropButton = new Button(this);
        dropButton.setText("DROP");
        dropButton.setOnClickListener(v -> dropSubject(subject.id));

        card.addView(subjectName);
        card.addView(subjectDetails);
        card.addView(dropButton);

        subjectsContainer.addView(card);
    }

    private void dropSubject(int subjectId) {
        new Thread(() -> {
            db.enrollmentDao().unenroll(studentId, subjectId);

            runOnUiThread(() -> {
                Toast.makeText(this, "Subject dropped", Toast.LENGTH_SHORT).show();
                loadEnrolledSubjects();
            });
        }).start();
    }
}