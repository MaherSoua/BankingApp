package com.mahersoua.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahersoua.bakingapp.adapters.IngredientAdpater;
import com.mahersoua.bakingapp.adapters.StepsAdapter;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.utils.JsonUtils;
import com.mahersoua.user.bakingapp.R;

public class RecipeDetailsFragment extends Fragment {

    private RecipeModel mRecipeModel;
    private StepsAdapter mStepsAdapter;

    public RecipeDetailsFragment() {

    }

    public void setRecipeInfo(RecipeModel recipeModel , Context context) {
        mRecipeModel = recipeModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipe_details_fragment, container, false);
        RecyclerView ingredientRecylerView = view.findViewById(R.id.ingredientsContainer);
        ingredientRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientRecylerView.setAdapter(new IngredientAdpater(getContext(),
                JsonUtils.getRecipeModel(mRecipeModel.getIngredients())));

        RecyclerView stepsRecylerView = view.findViewById(R.id.stepsContainer);
        stepsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsAdapter = new StepsAdapter(getContext(),  JsonUtils.getStepModel(mRecipeModel.getSteps()));
        stepsRecylerView.setAdapter(mStepsAdapter);

        getActivity().setTitle(mRecipeModel.getName());

        TextView stepListHeader = view.findViewById(R.id.stepsLabel);
        stepListHeader.setOnClickListener(new View.OnClickListener (){

            @Override
            public void onClick(View v) {
                mStepsAdapter.toggle();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(getClass().getSimpleName().toString(), "");
        super.onAttach(context);
    }
}
