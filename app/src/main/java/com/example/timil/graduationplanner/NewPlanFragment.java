package com.example.timil.graduationplanner;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timil.graduationplanner.db.AppDatabase;
import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;
import com.example.timil.graduationplanner.db.entities.Semester;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private int credits;
    //private ArrayList<Course> selectedCoursesList = new ArrayList<Course>();
    private ArrayList<Semester> semestersAndCoursesList = new ArrayList<Semester>();
    //private String selectedSemester;

    //list of items for the spinner (or drop down list).
    private String[] degreeLengths = new String[]{"1 year","2 years", "3 years", "4 years", "5 years", "6 years"};
    private ArrayList<String> semesterList = new ArrayList<String>(Arrays.asList("1st year, semester 1","1st year, semester 2", "2nd year, semester 1", "2nd year, semester 2",
            "3rd year, semester 1", "3rd year, semester 2", "4th year, semester 1", "4th year, semester 2", "5th year, semester 1", "5th year, semester 2",
            "6th year, semester 1", "6th year, semester 2"));

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

        TextView tvCurrentCreditHours = root.findViewById(R.id.tvCurrentCredits);
        credits=0;
        if(semestersAndCoursesList.size()>0){
            for (int i = 0; i<semestersAndCoursesList.size(); i++){
                for(int j = 0; j<semestersAndCoursesList.get(i).getCourseArrayList().size(); j++){
                    credits += semestersAndCoursesList.get(i).getCourseArrayList().get(j).getCredits();
                }
            }
        }
        tvCurrentCreditHours.setText(getString(R.string.selected_total_credits, credits));

        Bundle bundle = getArguments();
        try {
            dropdownListSelection = bundle.getInt("dropdownSelection");
        } catch (Exception err) {
            Log.d("TEST", "No bundle data");
        }

        semesterLV = root.findViewById(R.id.list);

        // add selected courses to semester list to keep track of already selected courses
        if(semestersAndCoursesList.size()>0){
            for (int i = 0; i < semestersAndCoursesList.size(); i++) {
                StringBuilder replacedSemesterName = new StringBuilder(semestersAndCoursesList.get(i).getName());
                ArrayList<Course> courseList = semestersAndCoursesList.get(i).getCourseArrayList();
                if(courseList.size() > 0) {
                    for (int j = 0; j < courseList.size(); j++){
                        if(j == 0){
                            replacedSemesterName.append(": \n");
                        }
                        replacedSemesterName.append("\n- ").append(courseList.get(j).getCourse_name());
                    }
                }
                semesterList.set(i, replacedSemesterName.toString());
            }
        }

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

                ArrayList<String> semesters = new ArrayList<String>();
                ArrayAdapter<String> semesterAdapter;
                switch (position) {
                    case 0:
                        // add only 2 first semester if user selects 1 year
                        semesters.addAll(semesterList.subList(0, 2));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 1:
                        // add only 4 first semester if user selects 2 years
                        semesters.addAll(semesterList.subList(0, 4));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 2:
                        semesters.addAll(semesterList.subList(0, 6));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 3:
                        semesters.addAll(semesterList.subList(0, 8));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 4:
                        semesters.addAll(semesterList.subList(0, 10));
                        semesterAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesters);
                        semesterLV.setAdapter(semesterAdapter);
                        break;
                    case 5:
                        semesters.addAll(semesterList.subList(0, 12));
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

                // make sure to take the original name only. E.g. if the user has already added courses before,
                // the name will be something like: 1st year, semester 1: CS 3270...
                // that is why we split the string with ":"
                semesterList.set(i, semesterList.get(i).split(":")[0]);
                mCallBack.showCourseList(semesterList.get(i), dropdownListSelection);
                dropdownListSelection = -1;
            }
        });

        final EditText editName = root.findViewById(R.id.editName);

        Button btnSave = root.findViewById(R.id.btnSave);
        final int finalCredits = credits;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Fill in the name field first.",Toast.LENGTH_SHORT).show();
                } else {
                    int recommendedCredits = Integer.valueOf(degreeLengths[dropdownListSelection].split(" ")[0]) * 30;
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Save plan");
                    alertDialog.setMessage("Are you sure you want to save this plan? \nNOTE! Your current credit amount is " + finalCredits + " (recommended is " + recommendedCredits + " credits).");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    GraduationPlan graduationPlan = new GraduationPlan();
                                    graduationPlan.setName(editName.getText().toString());
                                    graduationPlan.setSemestersArrayList(semestersAndCoursesList);

                                    setNewGraduationPlan setNewPlan = new setNewGraduationPlan();
                                    setNewPlan.execute(graduationPlan);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

    public void setSelectedCoursesList(ArrayList<Course> selectedCoursesList, String selectedSemester) {

        Semester semester = new Semester();
        semester.setName(selectedSemester);
        semester.setCourseArrayList(selectedCoursesList);

        boolean replaced=false;
        for (int i=0; i<semestersAndCoursesList.size(); i++) {
            // check that there is no semester with the same name, if there is, replace it with the new one
            if(semestersAndCoursesList.get(i).getName().matches(selectedSemester)){

                semestersAndCoursesList.set(i, semester);
                replaced = true;
            }
        }
        if(!replaced){
            semestersAndCoursesList.add(semester);
        }
    }

    /*private Semester[] parseJson(String json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Semester[] semesters = null;
        try {
            semesters = gson.fromJson(json, Semester[].class);
        } catch (Exception err) {
            Log.d("TEST", "JSON Parse Crashed");
        }
        return semesters;
    }*/

    public class setNewGraduationPlan extends AsyncTask<GraduationPlan, Integer, String> {

        @Override
        protected String doInBackground(GraduationPlan ... graduationPlan) {

            AppDatabase.getInstance(getContext()).planDAO().insertPlan(graduationPlan);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mCallBack.savePlan();
        }

    }

}
