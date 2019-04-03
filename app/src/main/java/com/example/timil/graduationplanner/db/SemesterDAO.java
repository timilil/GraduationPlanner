package com.example.timil.graduationplanner.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.timil.graduationplanner.db.entities.Semester;

import java.util.List;

@Dao
public interface SemesterDAO {
    //ellipse = ... means that you can provide 1 to many arguments
    @Insert
    void insertSemester(Semester... semesters);

    @Delete
    void deleteSemester(Semester semester);

    @Update
    void updateSemester(Semester semester);

    @Query("Select * from semester")
    LiveData<List<Semester>> getAllSemesters();

    @Query("Select * from semester where _id = :id")
    List<Semester> getAllById(int id);

    @Query("DELETE FROM semester WHERE _id = :id")
    void deleteById(int id);

    @Query("Select * from semester where name like :name")
    List<Semester> findByName(String name);

}
