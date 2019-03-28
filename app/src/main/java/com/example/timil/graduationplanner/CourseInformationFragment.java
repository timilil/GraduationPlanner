package com.example.timil.graduationplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseInformationFragment extends Fragment {

    private View root;
    private EditText edit1, edit2, edit3, edit4, edit5;
    private Course course;

    public CourseInformationFragment() {
        // Required empty public constructor
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
        edit5 = root.findViewById(R.id.edit5);

        edit1.setText(""+course.getId());
        edit2.setText(course.getName());
        edit3.setText(course.getCourse_code());
        edit4.setText(course.getStart_at());
        edit5.setText(course.getEnd_at());
    }

    public void setCourse(Course course){
        this.course = course;

    }

}
