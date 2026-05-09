package com.teamname.uniroll.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.ForeignKey;

@Entity(
        tableName = "subjects",
        foreignKeys = @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "lecturer_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Subject {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "subject_name")
    public String subjectName;

    @ColumnInfo(name = "subject_code")
    public String subjectCode;

    @ColumnInfo(name = "credit_hours")
    public int creditHours;

    @ColumnInfo(name = "lecturer_id", index = true)
    public int lecturerId;

    public Subject(String subjectName, String subjectCode, int creditHours, int lecturerId) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.creditHours = creditHours;
        this.lecturerId = lecturerId;
    }
}