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

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PlansFragment.OnPlanClick, NewPlanFragment.OnSemesterClick, CourseRecyclerAdapter.OnCourseClick, CourseListFragment.OnDoneClick {

    private FragmentManager fm;
    private PlansFragment plansFragment;
    private ViewFragment viewFragment;
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
                Log.d("TESTTTT", "logout pressed");
                FirebaseAuth.getInstance().signOut();

                user = null;

                //if (user == null) {
                    // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
                //}

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void newPlanClick() {
        if (newPlanFragment == null){
            newPlanFragment = new NewPlanFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, newPlanFragment, "newPlanFragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void viewPlanClick() {
        if (viewFragment == null){
            viewFragment = new ViewFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, viewFragment, "viewFragment")
                .addToBackStack(null)
                .commit();
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
    public void showCourse(Course course) {
        if (courseInformationFragment == null){
            courseInformationFragment = new CourseInformationFragment();
        }
        fm.beginTransaction()
                .replace(R.id.fragmentContainer, courseInformationFragment, "courseInformationFragment")
                .addToBackStack(null)
                .commit();

        courseInformationFragment.setCourse(course);
    }

    @Override
    public void updateCourseList(ArrayList<Course> courseList, String semester) {
        if (newPlanFragment == null) {
            newPlanFragment = new NewPlanFragment();
        }
        newPlanFragment.updateCourseList(courseList, semester);
    }

    @Override
    public void doneSelecting(int degreeLength) {
        fm.popBackStack();
        Snackbar.make(findViewById(android.R.id.content), "Successfully selected courses", Snackbar.LENGTH_LONG)
                .setAction("OKAY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: maybe give undo option here?
                    }
                })
                .show();
        if (newPlanFragment == null){
            newPlanFragment = new NewPlanFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("dropdownSelection", degreeLength);
        newPlanFragment.setArguments(bundle);
    }
}
