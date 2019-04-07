package com.example.timil.graduationplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.timil.graduationplanner.db.entities.Semester;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSemesterFragment extends ListFragment {

    private View root;
    private Semester semester;
    private ArrayAdapter<String> adapter;
    private List<String> courseArray;

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

        courseArray = new ArrayList<>();

        if (semester != null) {
            if (semester.getCourseArrayList().size() == 0) {
                courseArray.add("No course data.");
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_no_data, R.id.tvListNoData, courseArray);
            } else {
                for (int i = 0; i < semester.getCourseArrayList().size(); i++) {
                    courseArray.add(semester.getCourseArrayList().get(i).getCourse_name());
                }
                ListView listView = getListView();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //mCallBack.viewCourse(semester.getCourseArrayList().get(i));
                    }
                });
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, courseArray);
            }
            setListAdapter(adapter);
        }
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }
}
