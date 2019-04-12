package com.example.timil.graduationplanner;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.entities.Course;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseInformationFragment extends Fragment {

    private View root;
    private TextView tv2, tv4, tv6, tv8;
    private Course course;
    private String selectedSemester;
    private int courseListItemIndex;
    private OnButtonClick mCallBack;

    public interface OnButtonClick {
        void onAddButtonClick(Course course, String selectedSemester,int courseListItemIndex);
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

        tv2 = root.findViewById(R.id.tv2);
        tv4 = root.findViewById(R.id.tv4);
        tv6 = root.findViewById(R.id.tv6);
        tv8 = root.findViewById(R.id.tv8);
        //edit5 = root.findViewById(R.id.edit5);

        //edit1.setText(""+course.getId());
        tv2.setText(course.getCourse_name());
        tv4.setText(""+course.getCredits());
        tv6.setText(course.getPre_requisites());
        tv8.setText(course.getDescription());

        Button btnAddCourse = root.findViewById(R.id.btnAddCourse);
        if (course.getBtnToggle()) {
            btnAddCourse.setBackgroundColor(Color.RED);
            btnAddCourse.setText(R.string.delete_text);
        } else {
            btnAddCourse.setBackgroundColor(Color.GREEN);
            btnAddCourse.setText(R.string.add_text);
        }
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                course.setBtnToggle();
                mCallBack.onAddButtonClick(course, selectedSemester, courseListItemIndex);
            }
        });
    }

    public void setCourseInformation(Course course, String selectedSemester, int courseListItemIndex){
        this.course = course;
        this.courseListItemIndex = courseListItemIndex;
        this.selectedSemester = selectedSemester;
    }

}
