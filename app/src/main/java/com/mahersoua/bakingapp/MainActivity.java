package com.mahersoua.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.JsonArray;
import com.mahersoua.bakingapp.adapters.RecipeAdapter;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;
import com.mahersoua.user.bakingapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


        final RecipeAdapter recipeAdapter = new RecipeAdapter(this, null);
        RecyclerView recyclerView = findViewById(R.id.mainList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        RecipesViewModel model = ViewModelProviders.of(this).get(RecipesViewModel.class);

        model.getRecipes().observe(this, new Observer<List<RecipeModel>>() {
            @Override
            public void onChanged(@Nullable List<RecipeModel> recipeModels) {
                recipeAdapter.updateList(recipeModels);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MainActivity", ">>>> "+getSupportFragmentManager().getBackStackEntryCount());
        initActionBarTitle();
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    private void initActionBarTitle(){
        int currentStep = getSupportFragmentManager().getBackStackEntryCount();

        if(currentStep == 1) {
            setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void onBackPressed() {
        initActionBarTitle();
        super.onBackPressed();
    }
}
