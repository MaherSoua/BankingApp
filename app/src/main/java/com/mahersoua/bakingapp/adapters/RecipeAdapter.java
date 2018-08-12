package com.mahersoua.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mahersoua.bakingapp.fragment.RecipeDetailsFragment;
import com.mahersoua.bakingapp.fragment.RecipeStepsDetailsFragment;
import com.mahersoua.bakingapp.fragment.StepDetailsFragment;
import com.mahersoua.bakingapp.models.RecipeModel;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.bakingapp.utils.JsonUtils;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private List<RecipeModel> mList;
    private Context mContext;
    private IRecipeAdapter mListener;

    public RecipeAdapter(Context context, ArrayList<RecipeModel> list) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        if (mList == null) return;
        holder.titleTv.setText(mList.get(position).getName());
        holder.recipeVideoBtn.setTag(position);
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
        private ImageButton recipeVideoBtn;

        public RecipeHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            titleTv = itemView.findViewById(R.id.titleTv);
            recipeVideoBtn = itemView.findViewById(R.id.recipeVideoBtn);
            recipeVideoBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("RecipeAdapter" , "onClick");
                    if (v.getId() == R.id.recipeVideoBtn) {
                        FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if (mContext.getResources().getBoolean(R.bool.isTablet)) {
                            RecipeStepsDetailsFragment recipeStepsDetailsFragment = new RecipeStepsDetailsFragment();
                            recipeStepsDetailsFragment.setData(mList, (int) v.getTag());
                            Log.d("RecipeAdapter" , ""+mList.size());

                            fragmentTransaction.replace(R.id.fragmentContainer, recipeStepsDetailsFragment)
                                    .addToBackStack("Details")
                                    .commit();
                        } else {
                            RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                            recipeDetailsFragment.setRecipeInfo(mList.get((int) v.getTag()), mContext);

                            fragmentTransaction.replace(R.id.fragmentContainer, recipeDetailsFragment)
                                    .addToBackStack("Details")
                                    .commit();
                        }
                    }
                }
            });
        }
    }

    public void setListener(IRecipeAdapter listener){
        mListener = listener;
    }

    public interface IRecipeAdapter {
        void onItemSelected();
    }
}
