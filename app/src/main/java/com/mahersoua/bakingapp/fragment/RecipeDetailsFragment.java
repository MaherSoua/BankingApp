package com.mahersoua.bakingapp.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mahersoua.bakingapp.adapters.StepsAdapter.IStepAdapter;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.utils.JsonUtils;
import com.mahersoua.bakingapp.viewmodels.SelectedRecipeModel;
import com.mahersoua.user.bakingapp.R;

public class RecipeDetailsFragment extends Fragment {

    private static final String TAG = "RecipeDetailsFragment";
    private RecipeModel mRecipeModel;
    private StepsAdapter mStepsAdapter;
    private IStepAdapter mListener;
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

    public void setRecipeInfo(RecipeModel recipeModel , Context context) {
        mRecipeModel = recipeModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState != null){
            mRecipeModel = savedInstanceState.getParcelable("recipe_model");
        }

        View view = inflater.inflate(R.layout.recipe_details_fragment, container, false);
        RecyclerView ingredientRecylerView = view.findViewById(R.id.ingredientsContainer);
        ingredientRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ingredientRecylerView.setAdapter(new IngredientAdpater(getContext(),
                JsonUtils.getRecipeModel(mRecipeModel.getIngredients())));

        RecyclerView stepsRecylerView = view.findViewById(R.id.stepsContainer);
        stepsRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsAdapter = new StepsAdapter(getContext(),  JsonUtils.getStepModel(mRecipeModel.getSteps()));
        stepsRecylerView.setAdapter(mStepsAdapter);
        mStepsAdapter.setListener(mListener);
        int size = mRecipeModel.getSteps().size();
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

    public void onViewChange(int position) {
        mStepsAdapter.onPageViewChange(position);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe_model", mRecipeModel);
    }
}
