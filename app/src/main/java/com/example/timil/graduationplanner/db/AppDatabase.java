package com.example.timil.graduationplanner.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;
import com.example.timil.graduationplanner.db.entities.Semester;


@Database(entities = {Course.class, GraduationPlan.class, Semester.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {

        if (instance != null) return instance;

        return instance =
                Room.databaseBuilder(context, AppDatabase.class, "planner-database").build();
    }

    public abstract CourseDAO courseDAO();
    public abstract PlanDAO planDAO();
    public abstract SemesterDAO semesterDAO();

}
