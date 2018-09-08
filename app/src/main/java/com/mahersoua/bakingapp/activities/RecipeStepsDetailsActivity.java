package com.mahersoua.bakingapp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahersoua.bakingapp.fragment.RecipeDetailsFragment;
import com.mahersoua.bakingapp.fragment.StepDetailsFragment;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.R;

import java.util.ArrayList;

public class RecipeStepsDetailsActivity extends AppCompatActivity implements StepDetailsFragment.IStepDetails {

    public static final String APP_PREF = "recipe-app-preference";
    private RecipeDetailsFragment recipeDetailsFragment;
    private StepDetailsFragment stepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_details);
        if (getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        Intent intent = getIntent();
        if (intent != null && savedInstanceState == null) {
            int currentPosition = intent.getIntExtra("selected_recipe", 0);
            ArrayList<RecipeModel> mList = intent.getParcelableArrayListExtra("recipe_list");

            if (mList != null) {
                stepDetailsFragment = new StepDetailsFragment();
                stepDetailsFragment.setStepList(mList.get(currentPosition).getSteps());

                recipeDetailsFragment = new RecipeDetailsFragment();
                recipeDetailsFragment.setRecipeInfo(mList.get(currentPosition), this);

                recipeDetailsFragment.setListener(stepDetailsFragment);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.stepsDetailsContainer, stepDetailsFragment, "stepDetailsFragment")
                        .add(R.id.stepsFragmentContnainer, recipeDetailsFragment, "recipeDetailsFragment")
                        .commit();
            }
        } else {
            stepDetailsFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentByTag("stepDetailsFragment");
            recipeDetailsFragment = (RecipeDetailsFragment) getSupportFragmentManager().findFragmentByTag("recipeDetailsFragment");
        }

        if (stepDetailsFragment != null) {
            stepDetailsFragment.setListener(this);
            recipeDetailsFragment.setListener(stepDetailsFragment);
        }

    }

    @Override
    public void onViewChange(int position) {
        recipeDetailsFragment.onViewChange(position);
    }
}
