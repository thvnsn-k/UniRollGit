package com.teamname.uniroll.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.Query;
import com.teamname.uniroll.database.entity.Enrollment;
import com.teamname.uniroll.database.entity.Subject;
import java.util.List;

@Dao
public interface EnrollmentDao {

    @Insert
    long insert(Enrollment enrollment);

    @Delete
    void delete(Enrollment enrollment);

    @Query("SELECT COUNT(*) FROM enrollments WHERE student_id = :studentId AND subject_id = :subjectId")
    int isEnrolled(int studentId, int subjectId);

    @Query("SELECT s.* FROM subjects s " +
            "INNER JOIN enrollments e ON s.id = e.subject_id " +
            "WHERE e.student_id = :studentId")
    List<Subject> getEnrolledSubjects(int studentId);

    @Query("DELETE FROM enrollments WHERE student_id = :studentId AND subject_id = :subjectId")
    void unenroll(int studentId, int subjectId);

    @Query("SELECT * FROM enrollments WHERE student_id = :studentId")
    List<Enrollment> getEnrollmentsByStudent(int studentId);
}