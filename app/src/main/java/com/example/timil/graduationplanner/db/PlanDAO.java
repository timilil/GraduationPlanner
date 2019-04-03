package com.example.timil.graduationplanner.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.timil.graduationplanner.db.entities.GraduationPlan;

import java.util.List;

@Dao
public interface PlanDAO {

    //ellipse = ... means that you can provide 1 to many arguments
    @Insert
    void insertPlan(GraduationPlan... plans);

    @Delete
    void deletePlan(GraduationPlan graduationPlan);

    @Update
    void updatePlan(GraduationPlan graduationPlan);

    @Query("Select * from GraduationPlan")
    LiveData<List<GraduationPlan>> getAllPlans();

    @Query("Select * from GraduationPlan where _id = :id")
    List<GraduationPlan> getAllById(int id);

    @Query("DELETE FROM GraduationPlan WHERE _id = :id")
    void deleteById(int id);

    @Query("Select * from GraduationPlan where name like :name")
    List<GraduationPlan> findByName(String name);
}
