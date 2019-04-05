package com.example.timil.graduationplanner.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class Semester {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String name;

    public ArrayList<Course> courseArrayList;

    @Override
    public String toString() {
        return "{" +
                "_id:" + _id +
                ", name:'" + name + '\'' +
                ", courses:" + courseArrayList +
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

    public ArrayList<Course> getCourseArrayList() {
        return courseArrayList;
    }

    public void setCourseArrayList(ArrayList<Course> courseArrayList) {
        this.courseArrayList = courseArrayList;
    }
}
