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
import com.mahersoua.bakingapp.dataproviders.DataProvider;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.bakingapp.R;

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

    public void setStepList(ArrayList<StepModel> stepList) {
        mStepList = stepList;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(currentIndex);
        }
    }

    public void setCurrentPage(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mStepList == null && savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList("step-list");
            currentIndex = savedInstanceState.getInt("current-index");
        }

        View mView = inflater.inflate(R.layout.fragment_step_details, container, false);
        mViewPager = mView.findViewById(R.id.pager);

        mPagerAdapter = new StepSlideAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentIndex);
        DataProvider.setCurrentStep(currentIndex);

        nextStep = mView.findViewById(R.id.nextStep);
        previousStep = mView.findViewById(R.id.previousStep);
        if (nextStep != null && previousStep != null) {
            nextStep.setOnClickListener(this);
            previousStep.setOnClickListener(this);
            udpateButtonState();
        }
        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListener = null;
        if (mStepList != null) {
            outState.putParcelableArrayList("step-list", mStepList);
            outState.putInt("current-index", currentIndex);
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
        switch (v.getId()) {
            case R.id.nextStep:
                updateOnNext();
                break;

            case R.id.previousStep:
                updateOnPrevious();
                break;
        }
        DataProvider.setCurrentStep(currentIndex);
        mViewPager.setCurrentItem(currentIndex);
        if (mListener != null) {
            mListener.onViewChange(currentIndex);
        }

        String[] stepList = new String[mStepList.size()];
        for (int i = 0; i < mStepList.size(); i++) {
            stepList[i] = mStepList.get(i).getShortDescription();
        }
        udpateButtonState();
    }

    private void updateOnNext() {
        currentIndex++;
        if (currentIndex > (mStepList.size() - 1)) {
            currentIndex = mStepList.size() - 1;
        }
    }

    private void updateOnPrevious() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = 0;
        }
    }

    private void udpateButtonState() {
        nextStep.setEnabled(true);
        previousStep.setEnabled(true);

        if (currentIndex == (mStepList.size() - 1)) {
            nextStep.setEnabled(false);
        }

        if (currentIndex == 0) {
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

    public interface IStepDetails {
        void onViewChange(int position);
    }

    private class StepSlideAdapter extends FragmentStatePagerAdapter {
        public StepSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return StepItemFragment
                    .newInstance(mStepList.get(position), position, mViewPager.getCurrentItem());
        }

        @Override
        public int getCount() {
            return mStepList.size();
        }
    }
}
