package com.mahersoua.bakingapp.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahersoua.bakingapp.adapters.RecipeAdapter;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.viewmodels.RecipesViewModel;
import com.mahersoua.user.bakingapp.R;

import java.util.List;

public class RecipeListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        RecyclerView.LayoutManager layoutManager;

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);

        if(isTablet){
            layoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }

        final RecipeAdapter recipeAdapter = new RecipeAdapter(getContext(), null);
        RecyclerView recyclerView = view.findViewById(R.id.mainList);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setLayoutManager(layoutManager);

        RecipesViewModel model = ViewModelProviders.of(this).get(RecipesViewModel.class);

        model.getRecipes().observe(getActivity(), new Observer<List<RecipeModel>>() {
            @Override
            public void onChanged(@Nullable List<RecipeModel> recipeModels) {
                recipeAdapter.updateList(recipeModels);
            }
        });

        return view;
    }
}
