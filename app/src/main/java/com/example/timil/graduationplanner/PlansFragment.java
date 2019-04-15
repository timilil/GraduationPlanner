package com.example.timil.graduationplanner;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.timil.graduationplanner.db.entities.GraduationPlan;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends ListFragment {

    private View root;
    private OnPlanClick mCallBack;
    private ArrayAdapter<String> adapter;
    private List<String> plansArray;
    private List<GraduationPlan> graduationPlans;

    public interface OnPlanClick {
        void newPlan();
        void viewPlan(GraduationPlan graduationPlan);
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

        plansArray = new ArrayList<>();
        graduationPlans = new ArrayList<>();

        ViewModelProviders.of(getActivity())
                .get(GraduationPlanViewModel.class)
                .getGraduationPlanList(getContext())
                .observe(this, new Observer<List<GraduationPlan>>() {
                    @Override
                    public void onChanged(@Nullable List<GraduationPlan> plans) {
                        if(plans != null) {
                            graduationPlans = plans;
                            plansArray.clear();
                            LinearLayout infoLinear = root.findViewById(R.id.infoLinear);
                            if (plans.size() == 0) {
                                infoLinear.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < plans.size(); i++) {
                                    plansArray.add(plans.get(i).name);
                                }
                                infoLinear.setVisibility(View.GONE);
                                ListView listView = getListView();
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                        GraduationPlan graduationPlan = graduationPlans.get(i);
                                        mCallBack.viewPlan(graduationPlan);
                                    }
                                });
                                adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.tvList, plansArray);
                                setListAdapter(adapter);
                            }
                        }
                    }
                });

        FloatingActionButton fabNewPlan = root.findViewById(R.id.fabNew);
        fabNewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallBack.newPlan();
            }
        });
    }
}
