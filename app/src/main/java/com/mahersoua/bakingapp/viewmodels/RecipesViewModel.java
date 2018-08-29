package com.mahersoua.bakingapp.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.mahersoua.bakingapp.interfaces.Api;
import com.mahersoua.bakingapp.models.RecipeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesViewModel extends ViewModel {
    private MutableLiveData<ArrayList<RecipeModel>> recipeList;

    public LiveData<ArrayList<RecipeModel>> getRecipes(){
        if(recipeList == null){
            loadHeroes();
        }
        return recipeList;
    }

    private void loadHeroes(){
        recipeList = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<ArrayList<RecipeModel>> call = api.getRecipes();

        call.enqueue(new Callback<ArrayList<RecipeModel>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeModel>> call, Response<ArrayList<RecipeModel>> response) {
                recipeList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeModel>> call, Throwable t) {
                Log.d("RecipeViewModel" , t.getMessage());
            }
        });
    }
}
