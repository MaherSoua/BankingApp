package com.mahersoua.bakingapp.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.models.StepModel;

import java.util.ArrayList;

public class SelectedRecipeModel extends ViewModel {

    ArrayList<StepModel> mStepList;
    RecipeModel mRecipeModel;
    StepModel mStepModel;

    public SelectedRecipeModel(){
        Log.d("SelectedRecipeModel" , "Create instance");
    }


    public StepModel getStepModel() {
        return mStepModel;
    }

    public void setStepModel(StepModel mStepModel) {
        this.mStepModel = mStepModel;
    }

    public void setStepList(ArrayList<StepModel> stepList){
        mStepList = stepList;
    }

    public ArrayList<StepModel> getStepList(){
        return mStepList;
    }

    public void setRecipeModel(RecipeModel recipeModel){
        mRecipeModel = recipeModel;
    }

    public RecipeModel getRecipeModel(){
        return mRecipeModel;
    }
}
