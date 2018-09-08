package com.mahersoua.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahersoua.bakingapp.models.IngredientModel;
import com.mahersoua.bakingapp.R;

import java.util.ArrayList;

public class IngredientAdpater extends RecyclerView.Adapter<IngredientAdpater.IngredientViewHolder> {

    private Context mContext;
    private ArrayList<IngredientModel> mList;

    public IngredientAdpater(Context context, ArrayList<IngredientModel> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        IngredientModel ingredientModel = mList.get(position);
        String ingredient = ingredientModel.getIngredient() + " " +
                ingredientModel.getQuantity() + " " + ingredientModel.getMeasure();
        holder.ingredientTv.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientTv;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ingredientTv = itemView.findViewById(R.id.ingredientTv);
        }
    }
}
