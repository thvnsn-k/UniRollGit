package com.teamname.uniroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.teamname.uniroll.database.AppDatabase;
import com.teamname.uniroll.database.dao.SubjectDao;
import com.teamname.uniroll.database.entity.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LecturerDashboardActivity extends AppCompatActivity implements SubjectAdapter.OnSubjectActionListener {

    private RecyclerView rvSubjects;
    private TextView tvEmpty;
    private FloatingActionButton fabAddSubject;
    private Button btnLogout;

    private SubjectAdapter adapter;
    private SubjectDao subjectDao;
    private ExecutorService executorService;
    private int lecturerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecturer_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("UniRollSession", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);

        subjectDao = AppDatabase.getInstance(this).subjectDao();
        executorService = Executors.newSingleThreadExecutor();

        rvSubjects = findViewById(R.id.rvSubjects);
        tvEmpty = findViewById(R.id.tvEmpty);
        fabAddSubject = findViewById(R.id.fabAddSubject);
        btnLogout = findViewById(R.id.btnLogout);

        adapter = new SubjectAdapter(new ArrayList<>(), this);
        rvSubjects.setLayoutManager(new LinearLayoutManager(this));
        rvSubjects.setAdapter(adapter);

        fabAddSubject.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditSubjectActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubjects();
    }

    private void loadSubjects() {
        executorService.execute(() -> {
            List<Subject> subjects = subjectDao.getSubjectsByLecturer(lecturerId);
            runOnUiThread(() -> {
                adapter.updateList(subjects);
                tvEmpty.setVisibility(subjects.isEmpty() ? View.VISIBLE : View.GONE);
                rvSubjects.setVisibility(subjects.isEmpty() ? View.GONE : View.VISIBLE);
            });
        });
    }

    @Override
    public void onEdit(Subject subject) {
        Intent intent = new Intent(this, AddEditSubjectActivity.class);
        intent.putExtra("SUBJECT_ID", subject.id);
        intent.putExtra("SUBJECT_NAME", subject.subjectName);
        intent.putExtra("SUBJECT_CODE", subject.subjectCode);
        intent.putExtra("CREDIT_HOURS", subject.creditHours);
        startActivity(intent);
    }

    @Override
    public void onDelete(Subject subject) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete \"" + subject.subjectName + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    executorService.execute(() -> {
                        subjectDao.deleteById(subject.id);
                        runOnUiThread(this::loadSubjects);
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
