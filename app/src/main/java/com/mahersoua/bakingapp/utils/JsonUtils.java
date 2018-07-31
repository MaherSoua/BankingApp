package com.mahersoua.bakingapp.utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mahersoua.bakingapp.models.IngredientModel;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.models.StepModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonUtils {

    public static ArrayList<StepModel> getStepModel(JsonArray jsonArray){
        ArrayList<StepModel> stepModelList = new ArrayList<>();

        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            StepModel stepModel = new StepModel();
            stepModel.setId(jsonObject.get("id").getAsInt());
            stepModel.setDescription(jsonObject.get("description").getAsString());
            stepModel.setShortDescription(jsonObject.get("shortDescription").getAsString());
            stepModel.setThumbnailURL(jsonObject.get("thumbnailURL").getAsString());
            stepModel.setVideoURL(jsonObject.get("videoURL").getAsString());
            stepModelList.add(stepModel);
        }
        return stepModelList;
    }

    public static ArrayList<IngredientModel> getRecipeModel(JsonArray jsonArray){
        ArrayList<IngredientModel> recipeModels = new ArrayList<>();

        for(int i= 0; i < jsonArray.size(); i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            IngredientModel ingredientModel = new IngredientModel();
            ingredientModel.setIngredient(jsonObject.get("ingredient").getAsString());
            ingredientModel.setMeasure(jsonObject.get("measure").getAsString());
            ingredientModel.setQuantity(jsonObject.get("ingredient").getAsString());
            recipeModels.add(ingredientModel);
        }

        return recipeModels;
    }
}
