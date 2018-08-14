package com.mahersoua.bakingapp.interfaces;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mahersoua.bakingapp.models.RecipeModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<RecipeModel>> getRecipes();
}
