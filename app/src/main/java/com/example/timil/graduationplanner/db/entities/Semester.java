package com.example.timil.graduationplanner.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class Semester {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String name;

    public String coursesJson;

    @Override
    public String toString() {
        return "Semester{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", courses=" + coursesJson +
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

    public String getCoursesJson() {
        return coursesJson;
    }

    public void setCoursesJson(String coursesJson) {
        this.coursesJson = coursesJson;
    }
}
