package com.example.timil.graduationplanner;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;
import com.example.timil.graduationplanner.db.entities.Semester;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlansFragment.OnPlanClick, NewPlanFragment.OnSemesterClick,
        CourseRecyclerAdapter.OnCourseClick, CourseListFragment.OnDoneClick, CourseInformationFragment.OnButtonClick,
        ViewPlanFragment.OnClick {

    private FragmentManager fm;
    private PlansFragment plansFragment;
    private ViewPlanFragment viewPlanFragment;
    private ViewSemesterFragment viewSemesterFragment;
    private NewPlanFragment newPlanFragment;
    private CourseListFragment courseListFragment;
    private CourseInformationFragment courseInformationFragment;

    private List<AuthUI.IdpConfig> providers;
    private FirebaseUser user;
    public static final int RC_SIGN_IN = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plansFragment = new PlansFragment();

        fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, plansFragment, "plansFragment")
                //.addToBackStack(null)
                .commit();

        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // https://firebase.google.com/docs/auth/android/firebaseui
        // Choose authentication providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
    }

    @Override
    protected void onResume() {
        super.onResume();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d("TESTTTT", "New user signed in"+user.getUid());
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:

                FirebaseAuth.getInstance().signOut();

                user = null;

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (courseInformationFragment != null){
            // this is a work around to navigate back to the saved state of courseListFragment
            // to not lose already selected course data
            if(courseInformationFragment.isAdded()){
                fm.popBackStack();
            }
        }

        super.onBackPressed();
    }

    @Override
    public void newPlan() {
        if (newPlanFragment == null){
            newPlanFragment = new NewPlanFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, newPlanFragment, "newPlanFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewPlan(GraduationPlan graduationPlan) {
        if (viewPlanFragment == null){
            viewPlanFragment = new ViewPlanFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, viewPlanFragment, "viewPlanFragment")
                .addToBackStack(null)
                .commit();

        viewPlanFragment.setGraduationPlan(graduationPlan);
    }

    @Override
    public void showCourseList(String semester, int dropdownSelection) {
        if (courseListFragment == null){
            courseListFragment = new CourseListFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putString("semester", semester);
        bundle.putInt("dropdownSelection", dropdownSelection);
        courseListFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.fragmentContainer, courseListFragment, "courseListFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void savePlan() {
        fm.popBackStack();
        // make sure to reset the fragment when saving because we don't want to have any old data stored when/if navigating back to make a new plan
        newPlanFragment = null;
    }

    @Override
    public void showCourse(Course course, String selectedSemester, int courseListItemIndex) {
        if (courseInformationFragment == null){
            courseInformationFragment = new CourseInformationFragment();
        }

        // when showing course, we need to make sure to keep the state of the previous fragment
        // to not lose already selected course(s) data (if any)
        fm.beginTransaction().hide(courseListFragment).addToBackStack(null).commit();
        fm.beginTransaction().add(R.id.fragmentContainer, courseInformationFragment, "courseInformationFragment").addToBackStack(null).commit();

        courseInformationFragment.setCourseInformation(course, selectedSemester, courseListItemIndex);
    }

    @Override
    public void updateCourseList(Course course, String semester) {

        if (courseListFragment == null) {
            courseListFragment = new CourseListFragment();
        }
        courseListFragment.updateSelectedCoursesList(course, semester);
    }

    @Override
    public void doneSelecting(int degreeLength, ArrayList<Course> selectedCoursesList, String selectedSemester) {
        fm.popBackStack();
        Snackbar.make(findViewById(android.R.id.content), "Successfully selected courses", Snackbar.LENGTH_LONG)
                .setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: Is this necessary? Or maybe display a toaster instead?
                    }
                })
                .show();
        if (newPlanFragment == null){
            newPlanFragment = new NewPlanFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("dropdownSelection", degreeLength);
        newPlanFragment.setArguments(bundle);
        newPlanFragment.setSelectedCoursesList(selectedCoursesList, selectedSemester);
    }

    @Override
    public void onAddButtonClick(Course course, String selectedSemester, int courseListItemIndex) {

        // pop back stack 2 times
        // get rid of the current fragment
        fm.popBackStack();
        // bring back the hidden courseListFragment in its previous state
        fm.popBackStack();

        courseListFragment.updateRecyclerItemButton(courseListItemIndex, selectedSemester, course);
    }

    @Override
    public void viewSemester(Semester semester) {
        if(viewSemesterFragment == null) {
            viewSemesterFragment = new ViewSemesterFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, viewSemesterFragment, "viewSemesterFragment")
                .addToBackStack(null)
                .commit();
        viewSemesterFragment.setSemester(semester);
    }

    @Override
    public void deletePlan() {
        fm.popBackStack();
    }
}
