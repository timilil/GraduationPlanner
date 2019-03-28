package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewPlanFragment extends Fragment {

    private View root;
    private OnSemesterClick mCallBack;
    private ListView semesterLV;
    private int dropdownListSelection = -1;
    private ArrayList<Course> selectedCoursesList = new ArrayList<Course>();
    private String selectedSemester;

    //list of items for the spinner (or drop down list).
    private String[] degreeLengths = new String[]{"1 year","2 years", "3 years", "4 years", "5 years", "6 years"};
    private String[] semesterList = new String[]{"1st year, semester 1","1st year, semester 2", "2nd year, semester 1", "2nd year, semester 2",
            "3rd year, semester 1", "3rd year, semester 2", "4th year, semester 1", "4th year, semester 2", "5th year, semester 1", "5th year, semester 2",
            "6th year, semester 1", "6th year, semester 2"};

    public interface OnSemesterClick {
        void showCourseList(String semester, int dropdownSelection);
        void savePlan();
    }

    public NewPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnSemesterClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnSemesterClick interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_new_plan, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        Bundle bundle = getArguments();
        try {
            dropdownListSelection = bundle.getInt("dropdownSelection");
        } catch (Exception err) {
            Log.d("TEST", "No bundle data");
        }

        semesterLV = root.findViewById(R.id.list);

        Spinner dropdown = root.findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, degreeLengths);
        dropdown.setAdapter(adapter);

        // if there are old selected degree length values, use that instead of the first value in the spinner/drop down list
        if (dropdownListSelection >= 0 ){
            dropdown.setSelection(dropdownListSelection);
        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                dropdownListSelection = position;

                // if user presses done button after he/she has selected courses,
                // add those courses to semester list to keep track of already selected courses
                if (selectedCoursesList != null && selectedSemester != null){
                    for (int i = 0; i<semesterList.length; i++){
                        if (selectedSemester.equals(semesterList[i])) {

                            for (int j = 0; j<selectedCoursesList.size(); j++){
                                if(j == 0){
                                    semesterList[i]=semesterList[i]+": ";
                                }
                                semesterList[i]=semesterList[i]+"\n   "+selectedCoursesList.get(j).getName();
                            }
                        }
                    }
                }

                ArrayList<String> semesters = new ArrayList<String>();
                ArrayAdapter<String> semesterAdapter;
                switch (position) {
                    case 0:
                        // add only 2 first semester if user selects 1 year
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 2));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 1:
                        // add only 4 first semester if user selects 2 years
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 4));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 2:
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 6));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 3:
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 8));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 4:
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 10));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 5:
                        semesters.addAll(Arrays.asList(semesterList).subList(0, 12));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semesterLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // remember to reset the data every time just in case there is old data
                selectedCoursesList = null;
                selectedSemester = null;

                // make sure to take the original name only. E.g. if the user has already added courses before,
                // the name will be something like: 1st year, semester 1: CS 3270...
                // that is why we split the string with ":"
                semesterList[i] = semesterList[i].split(":")[0];
                mCallBack.showCourseList(semesterList[i], dropdownListSelection);
                dropdownListSelection = -1;
            }
        });

        Button btnSave = root.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.savePlan();
            }
        });
    }

    public void updateCourseList(ArrayList<Course> courseList, String semester) {
        selectedCoursesList = courseList;
        selectedSemester = semester;
    }

}
