package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.timil.graduationplanner.db.AppDatabase;
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
    }

    public ViewPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_view, container, false);
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

    public void setGraduationPlan(final GraduationPlan graduationPlan) {
        //Log.d("TESTTT", String.valueOf(graduationPlan));
        this.graduationPlan = graduationPlan;
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                List<GraduationPlan> graduationPlanList = AppDatabase.getInstance(getContext())
                        .planDAO()
                        .getAllById(graduationPlan._id);
                //Log.d("TESTTT", String.valueOf(graduationPlanList)+" size "+graduationPlanList.size());

            }
        }).start();*/
    }
}
