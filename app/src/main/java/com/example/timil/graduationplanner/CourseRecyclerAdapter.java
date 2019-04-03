package com.example.timil.graduationplanner;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.entities.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.CourseViewHolder> {

    private final List<Course> courses;
    private final String semester;
    private ArrayList<Course> selectedCoursesList;
    private OnCourseClick mCallback;

    public interface OnCourseClick{
        void showCourse(Course course);
        void updateCourseList(ArrayList<Course> courseList, String semester);
    }

    public CourseRecyclerAdapter(@NonNull List<Course> courses, Activity activity, String semester){
        this.courses = courses;
        this.semester = semester;

        try {
            this.mCallback = (OnCourseClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnCourseClick interface.");
        }
    }

    public void setCourses(List<Course> courses) {
        this.courses.clear();
        this.courses.addAll(courses);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseViewHolder courseViewHolder, int i) {
        selectedCoursesList = new ArrayList<Course>();
        final Course course = courses.get(i);
        if (course != null) {
            courseViewHolder.course = course;
            String courseName = course.getCourse_name();
            courseViewHolder.courseName.setText(courseName);
            courseViewHolder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.showCourse(course);
                    //Log.d("TESTTT", ""+course.toString());
                }
            });

            courseViewHolder.btnToggle.setTag(1);
            courseViewHolder.btnToggle.setBackgroundColor(Color.GREEN);
            courseViewHolder.btnToggle.setText(R.string.add_text);
            courseViewHolder.btnToggle.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick (View v) {
                    // do different actions depending on the button status
                    final int status =(Integer) v.getTag();
                    if(status == 1) {
                        courseViewHolder.btnToggle.setBackgroundColor(Color.RED);
                        courseViewHolder.btnToggle.setText(R.string.delete_text);
                        selectedCoursesList.add(course);
                        v.setTag(0);
                    } else {
                        courseViewHolder.btnToggle.setBackgroundColor(Color.GREEN);
                        courseViewHolder.btnToggle.setText(R.string.add_text);
                        selectedCoursesList.remove(course);
                        v.setTag(1);
                    }
                    mCallback.updateCourseList(selectedCoursesList, semester);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        public View itemRoot;
        public TextView courseName;
        public Button btnToggle;
        public Course course;

        public CourseViewHolder(@NonNull View itemView){
            super(itemView);

            itemRoot = itemView;
            courseName = itemRoot.findViewById(R.id.tvCourseName);
            btnToggle = itemRoot.findViewById(R.id.btnToggle);
        }
    }
}
