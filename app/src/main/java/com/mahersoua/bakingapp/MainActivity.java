package com.mahersoua.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mahersoua.bakingapp.adapters.RecipeAdapter;
import com.mahersoua.bakingapp.fragment.RecipeDetailsFragment;
import com.mahersoua.bakingapp.idlingresource.SimpleIdlingResource;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private List<RecipeModel> mList;

    @Nullable
    private SimpleIdlingResource mIdlingResource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int stepId = -1;
        int recipeId = -1;
        if (intent.getExtras() != null) {
            stepId = intent.getExtras().getInt("step-id", 0);
            recipeId = intent.getExtras().getInt("selected-recipe", 0);
        }

        if (hasNoInternetAccess()) {
            findViewById(R.id.connectionErrorTv).setVisibility(View.VISIBLE);
            return;
        } else {
            findViewById(R.id.connectionErrorTv).setVisibility(View.INVISIBLE);
        }
        if (getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        RecyclerView.LayoutManager layoutManager;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if (isTablet) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }


        final RecipeAdapter recipeAdapter = new RecipeAdapter(this, null);
        RecyclerView recyclerView = findViewById(R.id.mainList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(layoutManager);

        RecipesViewModel model = ViewModelProviders.of(this).get(RecipesViewModel.class);

        int finalStepId = stepId;
        int finalRecipeId = recipeId;
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }
        model.getRecipes().observe(this, list -> {
            recipeAdapter.updateList(list);
            MainActivity.this.mList = list;

            if (finalStepId != -1) {
                displayRecipeStep(finalRecipeId);
            }

            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        });

        if (mList != null) {
            displayRecipeStep(finalRecipeId);
        }
    }

    public List<RecipeModel> getList() {
        return mList;
    }

    private void displayRecipeStep(int finalRecipeId) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        recipeDetailsFragment.setRecipeInfo(mList.get(finalRecipeId), this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, recipeDetailsFragment)
                .addToBackStack("Details")
                .commit();
    }

    private boolean hasNoInternetAccess() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return null == info || !info.isConnectedOrConnecting();
        }
        return false;
    }

    @Nullable
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        initActionBarTitle();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "OnDestroy");
    }

    private void initActionBarTitle() {
        int currentStep = getSupportFragmentManager().getBackStackEntryCount();

        if (currentStep == 1) {
            setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void onBackPressed() {
        initActionBarTitle();
        super.onBackPressed();
    }
}
