package com.mahersoua.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

public class StepItemFragment extends Fragment {

    StepModel mStepModel;

    public void setStepModel(StepModel stepModel){
        mStepModel = stepModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.step_item_fragment , container, false);
        TextView descriptionTv = view.findViewById(R.id.stepDescription);
        descriptionTv.setText(mStepModel.getDescription());
        return view;
    }
}
