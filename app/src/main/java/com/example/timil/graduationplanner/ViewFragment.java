package com.example.timil.graduationplanner;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timil.graduationplanner.db.AppDatabase;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFragment extends Fragment {

    public View root;

    public ViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_view, container, false);
    }

    public void setGraduationPlan(final GraduationPlan graduationPlan) {
        Log.d("TESTTT", String.valueOf(graduationPlan));

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
