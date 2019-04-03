package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.timil.graduationplanner.db.entities.Course;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseInformationFragment extends Fragment {

    private View root;
    private EditText edit1, edit2, edit3, edit4, edit5;
    private Course course;
    private OnButtonClick mCallBack;

    public interface OnButtonClick {
        void onAddButtonClick(Course course);
    }

    public CourseInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnButtonClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnButtonClick interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_course_information, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        edit1 = root.findViewById(R.id.edit1);
        edit2 = root.findViewById(R.id.edit2);
        edit3 = root.findViewById(R.id.edit3);
        edit4 = root.findViewById(R.id.edit4);
        //edit5 = root.findViewById(R.id.edit5);

        //edit1.setText(""+course.getId());
        edit1.setText(course.getCourse_name());
        edit2.setText(""+course.getCredits());
        edit3.setText(course.getPre_requisites());
        edit4.setText(course.getDescription());

        Button btnAddCourse = root.findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.onAddButtonClick(course);
            }
        });
    }

    public void setCourse(Course course){
        this.course = course;

    }

}
