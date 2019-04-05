package com.example.timil.graduationplanner;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.example.timil.graduationplanner.db.AppDatabase;
import com.example.timil.graduationplanner.db.entities.GraduationPlan;

import java.util.List;

public class GraduationPlanViewModel extends ViewModel {

    private MutableLiveData<GraduationPlan> graduationPlan = new MutableLiveData<>();
    private LiveData<List<GraduationPlan>> graduationPlanList;

    public MutableLiveData<GraduationPlan> getGraduationPlan() {
        return graduationPlan;
    }

    public LiveData<List<GraduationPlan>> getGraduationPlanList(Context context) {
        if (graduationPlanList == null){
            graduationPlanList = AppDatabase.getInstance(context).planDAO().getAllPlans();
        }
        return graduationPlanList;
    }

    public void setGraduationPlan(GraduationPlan graduationPlan) {
        this.graduationPlan.postValue(graduationPlan);
    }


}
