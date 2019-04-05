package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.Semester;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CourseListFragment extends Fragment {

    private View root;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    private CourseRecyclerAdapter adapter;
    private ArrayList<Course> selectedCoursesList;
    private OnDoneClick mCallBack;

    public interface OnDoneClick {
        void doneSelecting(int degreeLength, ArrayList<Course> selectedCoursesList, String selectedSemester);
    }

    public CourseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnDoneClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnDoneClick interface.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        selectedCoursesList = new ArrayList<Course>();

        Bundle bundle = getArguments();
        String semester = null;
        int degreeLength = -1;
        try {
            semester = bundle.getString("semester");
            degreeLength = bundle.getInt("dropdownSelection");
        } catch (Exception err) {
            Log.d("TEST", "No bundle data");
        }

        TextView tvSelect = root.findViewById(R.id.tvSelectCourses);
        tvSelect.setText(getString(R.string.select_courses_for, semester));

        recyclerView = root.findViewById(R.id.recyclerView);
        adapter = new CourseRecyclerAdapter(new ArrayList<Course>(), getActivity(), semester);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(false);

        final List<Course> courseList = new ArrayList<Course>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // get courses from Firebase
        db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Course course = document.toObject(Course.class);
                        course.setId(document.getId());
                        courseList.add(course);
                    }
                    adapter.setCourses(courseList);
                } else {
                    Log.w("Error", "Error getting documents.", task.getException());
                }
            }
        });

        Button btnDone = root.findViewById(R.id.btnDone);
        final int finalDegreeLength = degreeLength;
        final String selectedSemester = semester;
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // degree length passed as parameter to show the correct semester amount when navigating back to NewPlanFragment
                mCallBack.doneSelecting(finalDegreeLength, selectedCoursesList, selectedSemester);
            }
        });

    }
    public void updateRecyclerItemButton(int courseListItemIndex, String semester, Course course){
        adapter.updateRecyclerItemButtonState(courseListItemIndex, semester, course);
    }

    public void updateSelectedCoursesList(Course course, String semester, Boolean action){
        // if action true -> add to list
        if(action){
            // only add course if it isn't yet added
            if(!selectedCoursesList.contains(course)){
                selectedCoursesList.add(course);
            }
        } else { // else, remove from list
            selectedCoursesList.remove(course);
        }
    }

}
