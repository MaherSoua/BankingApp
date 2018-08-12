package com.mahersoua.bakingapp.viewmodels;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.models.StepModel;

import java.util.ArrayList;

public class SelectedRecipeModel extends ViewModel {

    RecipeModel mRecipeModel;

    public void setRecipeModel(RecipeModel recipeModel){
        mRecipeModel = recipeModel;
    }

    public RecipeModel getRecipeModel(){
        return mRecipeModel;
    }
}
