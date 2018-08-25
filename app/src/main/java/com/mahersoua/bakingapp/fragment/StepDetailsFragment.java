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

import com.mahersoua.bakingapp.adapters.StepsAdapter.IStepAdapter;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;

public class StepDetailsFragment extends Fragment implements View.OnClickListener, IStepAdapter {

    private static final String TAG = "StepDetailsFragment";
    private ArrayList<StepModel> mStepList;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private Button nextStep;
    private Button previousStep;
    private int currentIndex = 0;
    private IStepDetails mListener;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if( mStepList== null && savedInstanceState != null){
            mStepList = savedInstanceState.getParcelableArrayList("step-list");
        }

        View mView = inflater.inflate(R.layout.fragment_step_details, container, false);
        mViewPager = mView.findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                if(mListener != null) {
                    mListener.onViewChange(position);
                }
                udpateButtonState();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerAdapter = new StepSlideAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        nextStep = mView.findViewById(R.id.nextStep);
        previousStep = mView.findViewById(R.id.previousStep);
        nextStep.setOnClickListener(this);
        previousStep.setOnClickListener(this);
        udpateButtonState();

        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListener = null;
        if(mStepList != null){
            outState.putParcelableArrayList("step-list", mStepList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewPager = null;
        mPagerAdapter = null;
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
        udpateButtonState();
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

    private void udpateButtonState(){
        nextStep.setEnabled(true);
        previousStep.setEnabled(true);

        if(currentIndex == (mStepList.size() - 1)){
            nextStep.setEnabled(false);
        }

        if(currentIndex == 0){
            previousStep.setEnabled(false);
        }
    }

    public void setListener(IStepDetails listener) {
        mListener = listener;
    }

    @Override
    public void onItemClicked(int index) {
        currentIndex = index;
        mViewPager.setCurrentItem(index);
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

    public interface IStepDetails {
        void onViewChange(int position);
    }
}
