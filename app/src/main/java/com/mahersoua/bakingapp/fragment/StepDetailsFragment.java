package com.mahersoua.bakingapp.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.bakingapp.viewmodels.SelectedRecipeModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;

public class StepDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "StepDetailsFragment";
    private ArrayList<StepModel> mStepList;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private SelectedRecipeModel selectedRecipeModel;
    private Button nextStep;
    private Button previousStep;
    private int currentIndex = 0;

    public void setStepList(ArrayList<StepModel> stepList){
        mStepList = stepList;
        if(mViewPager != null)
        {
            mViewPager.setCurrentItem(0);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedRecipeModel = ViewModelProviders.of(this).get(SelectedRecipeModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mStepList == null){
            mStepList = selectedRecipeModel.getStepList();
        } else {
            selectedRecipeModel.setStepList(mStepList);
        }
        View view = inflater.inflate(R.layout.step_details_fragment, container, false);
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                udpateVisibility();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerAdapter = new StepSlideAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        nextStep = view.findViewById(R.id.nextStep);
        previousStep = view.findViewById(R.id.previousStep);
        nextStep.setOnClickListener(this);
        previousStep.setOnClickListener(this);
        udpateVisibility();
        return view;
    }

    @Override
    public void onClick(View v) {
        currentIndex = mViewPager.getCurrentItem();
        switch (v.getId()){
            case R.id.nextStep:
                updateOnNext();
                mViewPager.setCurrentItem(currentIndex);
                break;

            case R.id.previousStep:
                updateOnPrevious();
                mViewPager.setCurrentItem(currentIndex);
                break;
        }
        udpateVisibility();
    }

    private void updateOnNext(){
        currentIndex++;
        if(currentIndex > (mStepList.size() - 1)){
            currentIndex = mStepList.size() - 1;
        }
    }

    private void updateOnPrevious(){
        currentIndex--;
        if(currentIndex < 0){
            currentIndex = 0;
        }
    }

    private void udpateVisibility(){
        nextStep.setEnabled(true);
        previousStep.setEnabled(true);

        if(currentIndex == (mStepList.size() - 1)){
            nextStep.setEnabled(false);
        }

        if(currentIndex == 0){
            previousStep.setEnabled(false);
        }
    }

    private class StepSlideAdapter extends FragmentStatePagerAdapter {

        public StepSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG , "getItem");
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
