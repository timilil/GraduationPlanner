package com.example.timil.graduationplanner.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Course {

    @PrimaryKey(autoGenerate = true)
    public int _id;

    public String id;

    public String course_name;

    public String course_code;
    public Integer credits;
    public String description;
    public String pre_requisites;
    public Boolean required;

    public Boolean btnToggle = false;

    @Override
    public String toString() {
        return "{" +
                "_id:" + _id +
                ", id:'" + id + '\'' +
                ", course_name:'" + course_name + '\'' +
                ", course_code:'" + course_code + '\'' +
                ", credits:" + credits +
                ", description:'" + description + '\'' +
                ", pre_requisites:'" + pre_requisites + '\'' +
                ", required:" + required +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPre_requisites() {
        return pre_requisites;
    }

    public void setPre_requisites(String pre_requisites) {
        this.pre_requisites = pre_requisites;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getBtnToggle() {
        return btnToggle;
    }

    public void setBtnToggle() {
        this.btnToggle = !this.btnToggle;
    }

}
