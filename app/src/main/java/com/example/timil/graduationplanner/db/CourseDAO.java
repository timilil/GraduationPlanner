package com.example.timil.graduationplanner.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.timil.graduationplanner.db.entities.Course;

import java.util.List;

@Dao
public interface CourseDAO {

    //ellipse = ... means that you can provide 1 to many arguments
    @Insert
    void insertCourse(Course... courses);

    @Delete
    void deleteCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Query("Select * from course")
    LiveData<List<Course>> getAllCourses();

    @Query("Select * from course where _id = :id")
    List<Course> getAllById(int id);
    //Course getAllById(int id);

    @Query("DELETE FROM course WHERE id = :id")
    void deleteById(int id);

    @Query("Select * from course where course_name like :courseName")
    List<Course> findByName(String courseName);

    @Query("Select * from course where course_name = :courseName AND course_code = :courseCode")
    List<Course> findByCourseStartDate(String courseName, String courseCode);
}
