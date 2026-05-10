package com.teamname.uniroll.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.teamname.uniroll.database.dao.EnrollmentDao;
import com.teamname.uniroll.database.dao.SubjectDao;
import com.teamname.uniroll.database.dao.UserDao;
import com.teamname.uniroll.database.entity.Enrollment;
import com.teamname.uniroll.database.entity.Subject;
import com.teamname.uniroll.database.entity.User;

@Database(
        entities = {User.class, Subject.class, Enrollment.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract SubjectDao subjectDao();
    public abstract EnrollmentDao enrollmentDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "UniRoll_db"
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}