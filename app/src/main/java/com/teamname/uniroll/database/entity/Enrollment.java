package com.teamname.uniroll.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
        tableName = "enrollments",
        foreignKeys = {
                @ForeignKey(
                        entity = User.class,
                        parentColumns = "id",
                        childColumns = "student_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Subject.class,
                        parentColumns = "id",
                        childColumns = "subject_id",
                        onDelete = ForeignKey.CASCADE
                )
        }
)
public class Enrollment {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "student_id", index = true)
    public int studentId;

    @ColumnInfo(name = "subject_id", index = true)
    public int subjectId;

    public Enrollment(int studentId, int subjectId) {
        this.studentId = studentId;
        this.subjectId = subjectId;
    }
}