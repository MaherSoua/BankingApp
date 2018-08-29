package com.mahersoua.bakingapp.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.mahersoua.bakingapp.models.RecipeModel;

public class SelectedRecipeModel extends ViewModel {

    RecipeModel mRecipeModel;

    public void setRecipeModel(RecipeModel recipeModel){
        mRecipeModel = recipeModel;
    }

    public RecipeModel getRecipeModel(){
        return mRecipeModel;
    }
}
