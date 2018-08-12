package com.mahersoua.bakingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.JsonArray;
import com.mahersoua.bakingapp.adapters.RecipeAdapter;
import com.mahersoua.bakingapp.fragment.RecipeListFragment;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;
import com.mahersoua.user.bakingapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.IRecipeAdapter{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        if(savedInstanceState == null){
            RecipeListFragment recipeListFragment = new RecipeListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer , recipeListFragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onItemSelected() {
    }
}
