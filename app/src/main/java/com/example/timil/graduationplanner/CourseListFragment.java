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
    private OnDoneClick mCallBack;

    public interface OnDoneClick {
        void doneSelecting(int degreeLength);
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

        //getCanvasCourses getCoursesTask = new getCanvasCourses();
        //getCoursesTask.execute();
        final List<Course> courseList = new ArrayList<Course>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // get courses from Firebase
        db.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Course course = (Course) document.getData();
                        Course course = document.toObject(Course.class);
                        course.setId(document.getId());
                        //Log.d("TESTTT", document.getId() + " => " + document.getData());
                        //Log.d("TESTTT", course.toString());
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
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // degree length passed as parameter to show the correct semesterName amount when navigating back to NewPlanFragment
                mCallBack.doneSelecting(finalDegreeLength);
            }
        });

    }

    /*public class getCanvasCourses extends AsyncTask<String, Integer, String>{

        private String rawJson;
        private Course[] courseList;
        @Override
        protected String doInBackground(String... strings) {


            try {


                /*URL url = new URL("https://weber.instructure.com/api/v1/courses");

                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                String auth_token = "14~V5tppZ0gfztBzDIReAXjkV3412f3UO176igUs8ySZpwxTm0LeW1LDywncmYcp9Wt";
                connection.setRequestProperty("Authorization", "Bearer "+auth_token);
                connection.connect();

                int status = connection.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(connection.getInputStream()));
                        rawJson = br.readLine();

                        courseList = parseeJson();
                }
            } catch (MalformedURLException e) {
                Log.d("TEST", "Wrong url");
                e.printStackTrace();
            } catch (IOException e) {
                Log.d("TEST", "I/O Exceptrion: "+e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            adapter.setCourses(Arrays.asList(courseList));
        }

        private Course[] parseeJson(){
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Course[] courses = null;
            try {
                courses = gson.fromJson(rawJson, Course[].class);
            } catch (Exception err) {
                Log.d("TEST", "JSON Parse Crashed");
            }
            return courses;
        }
    }*/
}
