package com.teamname.uniroll;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.teamname.uniroll.database.AppDatabase;
import com.teamname.uniroll.database.dao.SubjectDao;
import com.teamname.uniroll.database.entity.Subject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditSubjectActivity extends AppCompatActivity {

    private TextView tvFormTitle;
    private EditText etSubjectName, etSubjectCode, etCreditHours;
    private Button btnSave;

    private SubjectDao subjectDao;
    private ExecutorService executorService;

    private int subjectId = -1;
    private int lecturerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_subject);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences prefs = getSharedPreferences("UniRollSession", MODE_PRIVATE);
        lecturerId = prefs.getInt("USER_ID", -1);

        subjectDao = AppDatabase.getInstance(this).subjectDao();
        executorService = Executors.newSingleThreadExecutor();

        tvFormTitle = findViewById(R.id.tvFormTitle);
        etSubjectName = findViewById(R.id.etSubjectName);
        etSubjectCode = findViewById(R.id.etSubjectCode);
        etCreditHours = findViewById(R.id.etCreditHours);
        btnSave = findViewById(R.id.btnSave);

        subjectId = getIntent().getIntExtra("SUBJECT_ID", -1);
        if (subjectId != -1) {
            tvFormTitle.setText("Edit Subject");
            etSubjectName.setText(getIntent().getStringExtra("SUBJECT_NAME"));
            etSubjectCode.setText(getIntent().getStringExtra("SUBJECT_CODE"));
            etCreditHours.setText(String.valueOf(getIntent().getIntExtra("CREDIT_HOURS", 0)));
        }

        btnSave.setOnClickListener(v -> saveSubject());
    }

    private void saveSubject() {
        String name = etSubjectName.getText().toString().trim();
        String code = etSubjectCode.getText().toString().trim();
        String hoursStr = etCreditHours.getText().toString().trim();

        if (name.isEmpty() || code.isEmpty() || hoursStr.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int creditHours;
        try {
            creditHours = Integer.parseInt(hoursStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Credit hours must be a number", Toast.LENGTH_SHORT).show();
            return;
        }

        if (subjectId == -1) {
            String finalCode = code;
            executorService.execute(() -> {
                int exists = subjectDao.countByCode(finalCode);
                if (exists > 0) {
                    runOnUiThread(() -> Toast.makeText(this, "Subject code already exists", Toast.LENGTH_SHORT).show());
                    return;
                }
                Subject subject = new Subject(name, finalCode, creditHours, lecturerId);
                subjectDao.insert(subject);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Subject added", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        } else {
            int finalSubjectId = subjectId;
            int finalCreditHours = creditHours;
            executorService.execute(() -> {
                Subject subject = subjectDao.getSubjectById(finalSubjectId);
                subject.subjectName = name;
                subject.subjectCode = code;
                subject.creditHours = finalCreditHours;
                subjectDao.update(subject);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Subject updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            });
        }
    }
}
