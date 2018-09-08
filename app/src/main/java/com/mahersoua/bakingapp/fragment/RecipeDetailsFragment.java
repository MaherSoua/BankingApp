package com.mahersoua.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahersoua.bakingapp.adapters.StepsAdapter;
import com.mahersoua.bakingapp.adapters.StepsAdapter.IStepAdapter;
import com.mahersoua.bakingapp.models.IngredientModel;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.R;

public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    private RecipeModel mRecipeModel;
    private StepsAdapter mStepsAdapter;
    private IStepAdapter mListener;
    private int selectedPosition;
    private StepDetailsFragment.IStepDetails mPageListener;

    public RecipeDetailsFragment() {

    }

    public void setListener(IStepAdapter listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setRecipeInfo(RecipeModel recipeModel, Context context) {
        mRecipeModel = recipeModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (mRecipeModel == null) {
                mRecipeModel = savedInstanceState.getParcelable("recipe_model");
            }
            selectedPosition = savedInstanceState.getInt("selected_position");
        }

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        TextView ingredientTv = view.findViewById(R.id.ingredientsContainer);

        StringBuilder recipeList = new StringBuilder();

        for (int i = 0; i < mRecipeModel.getIngredients().size(); i++) {
            IngredientModel ingredientModel = mRecipeModel.getIngredients().get(i);
            recipeList.append(ingredientModel.getQuantity()).append(" ").append(ingredientModel.getMeasure()).append(" ").append(ingredientModel.getIngredient());
            if (i < mRecipeModel.getIngredients().size() - 1) {
                recipeList.append(" / ");
            }
        }

        ingredientTv.setText(recipeList.toString());


        RecyclerView stepsRecylerView = view.findViewById(R.id.stepsContainer);
        stepsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsAdapter = new StepsAdapter(getContext(), mRecipeModel.getSteps());
        stepsRecylerView.setAdapter(mStepsAdapter);
        mStepsAdapter.setListener(mListener);
        getActivity().setTitle(mRecipeModel.getName());
        mStepsAdapter.onPageViewChange(selectedPosition);

        TextView stepListHeader = view.findViewById(R.id.stepsLabel);
        stepListHeader.setOnClickListener(v -> mStepsAdapter.toggle());
        return view;
    }

    public void onViewChange(int position) {
        if (mStepsAdapter != null) {
            mStepsAdapter.onPageViewChange(position);
        }
        selectedPosition = position;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe_model", mRecipeModel);
        outState.putInt("selected_position", selectedPosition);
    }
}
