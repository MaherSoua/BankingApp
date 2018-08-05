package com.mahersoua.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mahersoua.bakingapp.fragment.StepDetailsFragment;
import com.mahersoua.bakingapp.models.StepModel;
import com.mahersoua.user.bakingapp.R;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepHolder> {

    private static final String TAG = "StepsAdapter";
    private ArrayList<StepModel> mList;
    private Context mContext;
    private boolean isCollapsed = true;

    public StepsAdapter(Context context, ArrayList<StepModel> list){
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.step_item_adapter, parent, false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        holder.recipeName.setText(mList.get(position).getDescription());
        holder.recipeName.setTag(position);
    }

    @Override
    public int getItemCount() {
        if(mList != null){
            return isCollapsed ? mList.size() : 0;
        }
        return 0;
    }

    public void toggle(){
        isCollapsed = !isCollapsed;
        notifyDataSetChanged();
    }

    class StepHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        public StepHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.stepName);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                    stepDetailsFragment.setStepList(mList);
                    ((AppCompatActivity )mContext).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, stepDetailsFragment)
                            .addToBackStack("StepDetails")
                            .commit();
                }
            });
        }
    }
}
