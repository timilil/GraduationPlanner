package com.example.timil.graduationplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.Semester;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSemesterFragment extends ListFragment {

    private View root;
    private Semester semester;
    private String semesterName;
    private List<String> emptyArray;
    private ArrayList<Course> courseArray;

    public ViewSemesterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_view_semester, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (semester != null && semesterName != null) {
            TextView tvSemesterName = root.findViewById(R.id.tvSemesterInfo);
            tvSemesterName.setText(semesterName);
            if (semester.getCourseArrayList().size() == 0) {
                emptyArray = new ArrayList<>();
                emptyArray.add("No course data.");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_no_data, R.id.tvListNoData, emptyArray);
                setListAdapter(adapter);
            } else {

                courseArray = new ArrayList<Course>();
                courseArray.addAll(semester.getCourseArrayList());

                PlanCoursesListViewAdapter adapter = new PlanCoursesListViewAdapter(getContext(), courseArray);
                ListView listView = getListView();
                listView.setAdapter(adapter);
            }
        }
    }

    public void setSemester(Semester semester, String semesterName) {
        this.semester = semester;
        this.semesterName = semesterName;
    }
}
