package com.example.timil.graduationplanner.db;

import android.arch.persistence.room.TypeConverter;

import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.Semester;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static ArrayList<Course> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Course>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Course> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

    @TypeConverter
    public static ArrayList<Semester> fromSemesterString(String value) {
        Type listType = new TypeToken<ArrayList<Semester>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromSemesterArrayList(ArrayList<Semester> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}