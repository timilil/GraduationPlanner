package com.example.timil.graduationplanner;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timil.graduationplanner.db.AppDatabase;
import com.example.timil.graduationplanner.db.entities.Course;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;
import com.example.timil.graduationplanner.db.entities.Semester;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPlanFragment extends ListFragment {

    public View root;
    private OnClick mCallBack;
    private GraduationPlan graduationPlan;
    private ArrayAdapter<String> adapter;
    private List<String> semesterArray;

    public interface OnClick {
        void viewSemester(Semester semester);
        void deletePlan();
    }

    public ViewPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_view_plan, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnClick interface.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        semesterArray = new ArrayList<>();

        if (graduationPlan != null) {
            TextView tvPlanName = root.findViewById(R.id.tvPlanName);
            tvPlanName.setText(graduationPlan.getName());
            int totalCredits = 0;
            for (int i = 0; i<graduationPlan.getSemestersArrayList().size(); i++) {
                Semester semester = graduationPlan.getSemestersArrayList().get(i);
                for (int j = 0; j<semester.getCourseArrayList().size(); j++){
                    Course course = semester.getCourseArrayList().get(j);
                    totalCredits += course.getCredits();
                }
            }
            TextView tvPlanCredits = root.findViewById(R.id.tvPlanTotalCredits);
            tvPlanCredits.setText(""+totalCredits);

            if(graduationPlan.getSemestersArrayList().size()==0){
                semesterArray.add("No semester data.");
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_no_data, R.id.tvListNoData, semesterArray);
            } else {
                for (int i = 0; i < graduationPlan.getSemestersArrayList().size(); i++) {
                    semesterArray.add(graduationPlan.getSemestersArrayList().get(i).getName());
                }
                ListView listView = getListView();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mCallBack.viewSemester(graduationPlan.getSemestersArrayList().get(i));
                    }
                });
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, semesterArray);
            }
            setListAdapter(adapter);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.deletePlan).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletePlan:

                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Delete plan");
                alertDialog.setMessage("Are you sure you want to delete this plan?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase.getInstance(getContext())
                                                .planDAO()
                                                .deleteById(graduationPlan._id);
                                    }
                                }).start();
                                dialog.dismiss();
                                mCallBack.deletePlan();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                });
                alertDialog.show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setGraduationPlan(final GraduationPlan graduationPlan) {
        this.graduationPlan = graduationPlan;
    }
}
