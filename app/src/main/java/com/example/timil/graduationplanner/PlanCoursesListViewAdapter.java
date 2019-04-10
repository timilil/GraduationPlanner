package com.example.timil.graduationplanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.entities.Course;

import java.util.ArrayList;

public class PlanCoursesListViewAdapter extends ArrayAdapter<Course> {

    public PlanCoursesListViewAdapter(Context context, ArrayList<Course> courses) {
        super(context, 0, courses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // course item for this position
        Course course = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.course_list_item, parent, false);
        }

        TextView tv1 = convertView.findViewById(R.id.tvListCourseName);
        TextView tv2 = convertView.findViewById(R.id.tvListCourseCredits);
        tv1.setText(course.getCourse_name());
        tv2.setText(""+course.getCredits());

        return convertView;
    }

}
