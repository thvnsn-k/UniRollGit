package com.teamname.uniroll.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.Query;
import com.teamname.uniroll.database.entity.Subject;
import java.util.List;

@Dao
public interface SubjectDao {

    @Insert
    long insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    Subject getSubjectById(int subjectId);

    @Query("SELECT * FROM subjects WHERE lecturer_id = :lecturerId")
    List<Subject> getSubjectsByLecturer(int lecturerId);

    @Query("SELECT * FROM subjects")
    List<Subject> getAllSubjects();

    @Query("SELECT COUNT(*) FROM subjects WHERE subject_code = :code")
    int countByCode(String code);

    @Query("DELETE FROM subjects WHERE id = :subjectId")
    void deleteById(int subjectId);
}