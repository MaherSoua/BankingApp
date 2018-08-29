package com.mahersoua.bakingapp;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mahersoua.bakingapp.adapters.RecipeAdapter;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;
import com.mahersoua.user.bakingapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(hasNoInternetAccess()){
            findViewById(R.id.connectionErrorTv).setVisibility(View.VISIBLE);
            return;
        } else {
            findViewById(R.id.connectionErrorTv).setVisibility(View.INVISIBLE);
        }
        if(getResources().getBoolean(R.bool.landscape_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        RecyclerView.LayoutManager layoutManager;
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if(isTablet){
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }


        final RecipeAdapter recipeAdapter = new RecipeAdapter(this, null);
        RecyclerView recyclerView = findViewById(R.id.mainList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(layoutManager);

        RecipesViewModel model = ViewModelProviders.of(this).get(RecipesViewModel.class);

        model.getRecipes().observe(this, recipeAdapter::updateList);
    }

    private boolean hasNoInternetAccess() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return null == info || !info.isConnectedOrConnecting();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        initActionBarTitle();
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG , "OnDestroy");
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
