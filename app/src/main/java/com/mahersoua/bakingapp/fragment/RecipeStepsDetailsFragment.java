package com.mahersoua.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.utils.JsonUtils;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsDetailsFragment extends Fragment implements StepDetailsFragment.IStepDetails {

    private List<RecipeModel> mList;
    private RecipeDetailsFragment recipeDetailsFragment;
    private int currentPosition = 0;

    public void setData(List<RecipeModel> list, int index){
        mList = list;
        currentPosition = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_steps_details_fragment, container, false);

        if(savedInstanceState != null){
            mList = savedInstanceState.getParcelableArrayList("recipe_list");
            currentPosition = savedInstanceState.getInt("selected_recipe");
        }

        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        stepDetailsFragment.setStepList(JsonUtils.getStepModel(mList.get(currentPosition).getSteps()));
        stepDetailsFragment.setListener(this);

        recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setRecipeInfo(mList.get(currentPosition), getContext());

        recipeDetailsFragment.setListener(stepDetailsFragment);
        FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.stepsDetailsContainer, stepDetailsFragment)
                .add(R.id.stepsFragmentCnotnainer, recipeDetailsFragment)
                .commit();
        return view;
    }

    @Override
    public void onViewChange(int position) {
         recipeDetailsFragment.onViewChange(position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mList != null){
            outState.putParcelableArrayList("recipe_list", (ArrayList<RecipeModel>) mList);
            outState.putInt("selected_recipe", currentPosition);
        }
    }
}
