package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button btnDone;
    private CourseRecyclerAdapter adapter;
    private ArrayList<Course> selectedCoursesList;
    private OnDoneClick mCallBack;
    private int credits;
    private boolean searchBarToggled;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        searchBarToggled = false;
        selectedCoursesList = new ArrayList<Course>();
        credits=0;
        TextView tvSelectedCreditHours = root.findViewById(R.id.tvSelectedCredits);
        tvSelectedCreditHours.setText(getString(R.string.selected_total_credits, credits));

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

        btnDone = root.findViewById(R.id.btnDone);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.search).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                searchBarToggled = !searchBarToggled;
                final SearchView searchView = root.findViewById(R.id.searchView);
                searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        // hide the button if the keyboard is shown because it saves some space
                        // and might distract the user
                        if (b) {
                            btnDone.setVisibility(View.GONE);
                        } else {
                            btnDone.setVisibility(View.VISIBLE);
                        }
                    }
                });

                searchView.setIconified(false);
                searchView.setFocusable(true);
                if(searchBarToggled) {
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            adapter.filterCourses(query);
                            searchView.clearFocus();
                            return true;
                        }
                        @Override
                        public boolean onQueryTextChange(String changedText) {
                            adapter.filterCourses(changedText);
                            return true;
                        }
                    });
                } else {
                    searchView.setVisibility(View.GONE);
                }
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateRecyclerItemButton(int courseListItemIndex, String semester, Course course){
        adapter.updateRecyclerItemButtonState(courseListItemIndex, semester, course);
    }

    public void updateSelectedCoursesList(Course course, String semester, int courseListItemIndex){

        int currentCredits=0;
        for(int i = 0; i < selectedCoursesList.size(); i++){
            currentCredits += selectedCoursesList.get(i).getCredits();
        }

        // if btnToggle is false -> remove from list
        if(!course.getBtnToggle()) {
            selectedCoursesList.remove(course);
            credits -= course.getCredits();
        }
        else {
            boolean isOverMaxCredits = false;
            if((currentCredits + course.getCredits()) > 20){
                isOverMaxCredits = true;
                course.setBtnToggle();
                Toast.makeText(getContext(),"Can't select more than 20 credits!",Toast.LENGTH_SHORT).show();
            }
            // only add course if it isn't yet added and it isn't over max credits
            if(!selectedCoursesList.contains(course) && !isOverMaxCredits){
                selectedCoursesList.add(course);
                credits += course.getCredits();
            }
        }

        adapter.notifyItemChanged(courseListItemIndex);

        TextView tvSelectedCreditHours = root.findViewById(R.id.tvSelectedCredits);
        tvSelectedCreditHours.setText(getString(R.string.selected_total_credits, credits));
    }

}
