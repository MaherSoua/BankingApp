package com.mahersoua.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;

public class StepDetailsFragment extends Fragment implements View.OnClickListener {

    ArrayList<StepModel> mStepList;
    ViewPager mViewPager;
    PagerAdapter mPagerAdapter;

    public void setStepList(ArrayList<StepModel> stepList){
        mStepList = stepList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.step_details_fragment, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mPagerAdapter = new StepSlideAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        Button nextStep = view.findViewById(R.id.nextStep);
        Button previousStep = view.findViewById(R.id.previousStep);
        nextStep.setOnClickListener(this);
        previousStep.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int curretIndex = mViewPager.getCurrentItem();
        switch (v.getId()){
            case R.id.nextStep:
                curretIndex++;
                if(curretIndex > 3){
                    curretIndex = 3;
                }
                mViewPager.setCurrentItem(curretIndex);
                break;

            case R.id.previousStep:
                curretIndex--;
                if(curretIndex < 0){
                    curretIndex = 0;
                }
                mViewPager.setCurrentItem(curretIndex);
                break;
        }
    }

    private class StepSlideAdapter extends FragmentStatePagerAdapter {

        public StepSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StepItemFragment stepItemFragment = new StepItemFragment();
            stepItemFragment.setStepModel(mStepList.get(position));
            return stepItemFragment;
        }

        @Override
        public int getCount() {
            return mStepList.size();
        }
    }
}
