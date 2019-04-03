package com.example.timil.graduationplanner.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Arrays;

@Entity
public class GraduationPlan {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String name;

    public String semestersJson;

    @Override
    public String toString() {
        return "GraduationPlan{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", plans=" + semestersJson +
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

    public String getSemestersJson() {
        return semestersJson;
    }

    public void setSemestersJson(String semestersJson) {
        this.semestersJson = semestersJson;
    }
}
