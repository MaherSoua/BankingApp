package com.mahersoua.bakingapp.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahersoua.bakingapp.fragment.RecipeDetailsFragment;
import com.mahersoua.bakingapp.fragment.StepDetailsFragment;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.utils.JsonUtils;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsDetailsActivity extends AppCompatActivity implements StepDetailsFragment.IStepDetails {

    private RecipeDetailsFragment recipeDetailsFragment;
    private StepDetailsFragment stepDetailsFragment;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);

        Bundle bundle = getIntent().getExtras().getBundle("recipe_args");

        if(bundle != null){
            ArrayList<RecipeModel> mList = bundle.getParcelableArrayList("recipe_list");
            currentPosition = bundle.getInt("selected_recipe", 0);
            if(mList != null){
                if(mList.get(currentPosition).getSteps() == null)
                    return;
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
//                stepDetailsFragment.setStepList(JsonUtils.getStepModel(mList.get(currentPosition).getSteps()));
                stepDetailsFragment.setListener(this);

                RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                recipeDetailsFragment.setRecipeInfo(mList.get(currentPosition), this);

                recipeDetailsFragment.setListener(stepDetailsFragment);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.stepsDetailsContainer, stepDetailsFragment)
                        .add(R.id.stepsFragmentContnainer, recipeDetailsFragment)
                        .commit();
            }
        }
    }

    @Override
    public void onViewChange(int position) {
        recipeDetailsFragment.onViewChange(position);
    }
}
