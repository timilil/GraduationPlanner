package com.example.timil.graduationplanner.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class GraduationPlan {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String name;

    public ArrayList<Semester> semestersArrayList;

    @Override
    public String toString() {
        return "{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", plans=" + semestersArrayList +
                '}';
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Semester> getSemestersArrayList() {
        return semestersArrayList;
    }

    public void setSemestersArrayList(ArrayList<Semester> semestersArrayList) {
        this.semestersArrayList = semestersArrayList;
    }
}
