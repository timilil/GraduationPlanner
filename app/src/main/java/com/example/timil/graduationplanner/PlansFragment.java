package com.example.timil.graduationplanner;


import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends Fragment {

    private View root;
    private OnPlanClick mCallBack;

    public interface OnPlanClick {
        void newPlanClick();
        void viewPlanClick();
    }

    public PlansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return root = inflater.inflate(R.layout.fragment_plans, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallBack = (OnPlanClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+" must implement OnPlanClick interface.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Button viewBtn = root.findViewById(R.id.btnView);
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.viewPlanClick();
            }
        });

        /*Button addBtn = root.findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.newPlanClick();
            }
        });*/

        FloatingActionButton fabNewPlan = root.findViewById(R.id.fabNew);
        fabNewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.newPlanClick();
            }
        });
    }
}
