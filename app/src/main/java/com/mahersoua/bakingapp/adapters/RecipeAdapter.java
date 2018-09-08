package com.mahersoua.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mahersoua.bakingapp.activities.RecipeStepsDetailsActivity;
import com.mahersoua.bakingapp.fragment.RecipeDetailsFragment;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.R;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.bakingapp.utils.StringUtils;
import com.mahersoua.bakingapp.widget.BankingWidgetProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private List<RecipeModel> mList;
    private Context mContext;

    public RecipeAdapter(Context context, ArrayList<RecipeModel> list) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_recipe_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        if (mList == null) return;
        holder.titleTv.setText(mList.get(position).getName());
        holder.recipeImageBtn.setTag(position);
        Picasso.get().load(mList.get(position).getName()).error(R.drawable.ic_broken_image_black_24dp).into(holder.recipeImageBtn);
    }

    public void updateList(List<RecipeModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class RecipeHolder extends RecyclerView.ViewHolder {
        private TextView titleTv;
        private View itemView;
        private ImageButton recipeImageBtn;

        private RecipeHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleTv = itemView.findViewById(R.id.titleTv);
            recipeImageBtn = itemView.findViewById(R.id.recipeVideoBtn);
            recipeImageBtn.setOnClickListener(v -> {
                if (v.getId() == R.id.recipeVideoBtn) {
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    if (mContext.getResources().getBoolean(R.bool.isTablet)) {
                        Intent intent = new Intent(mContext, RecipeStepsDetailsActivity.class);
                        intent.putParcelableArrayListExtra("recipe_list", (ArrayList<RecipeModel>) mList);
                        mContext.startActivity(intent);
                    } else {
                        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                        recipeDetailsFragment.setRecipeInfo(mList.get((int) v.getTag()), mContext);

                        fragmentTransaction.replace(R.id.fragmentContainer, recipeDetailsFragment)
                                .addToBackStack("Details")
                                .commit();
                    }

                    int selectedRecipe = (int) v.getTag();
                    List<StepModel> stepsModel = mList.get(selectedRecipe).getSteps();
                    String[] stepList = new String[stepsModel.size()];
                    for (int i = 0; i < stepsModel.size(); i++) {
                        stepList[i] = stepsModel.get(i).getShortDescription().trim();
                    }

                    SharedPreferences.Editor editor = mContext
                            .getSharedPreferences(RecipeStepsDetailsActivity.APP_PREF, Context.MODE_PRIVATE).edit();
                    editor.putString("step-list", StringUtils.join(StringUtils.DELIMETER, stepList));
                    editor.putInt("selected-recipe", selectedRecipe);
                    editor.commit();

                    BankingWidgetProvider.updateWidgetGridView(stepList, mContext);
                }
            });
        }
    }
}
