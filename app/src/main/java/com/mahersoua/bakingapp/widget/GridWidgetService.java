package com.mahersoua.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mahersoua.bakingapp.R;
import com.mahersoua.bakingapp.activities.RecipeStepsDetailsActivity;
import com.mahersoua.bakingapp.utils.StringUtils;

public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(getApplicationContext(), intent);
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    Intent mIntent;
    int mSelectedRecipe;
    String[] stepList;

    public GridRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
        stepList = intent.getStringArrayExtra("step-name");
    }

    @Override
    public void onCreate() {
        Log.d("GridWidgetService", "///");
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences editor = mContext
                .getSharedPreferences(RecipeStepsDetailsActivity.APP_PREF, Context.MODE_PRIVATE);
        String savedData = editor.getString("step-list", "");
        mSelectedRecipe = editor.getInt("selected-recipe", 0);
        stepList = savedData.split(StringUtils.DELIMETER);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return stepList.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_gridview_item);
        remoteViews.setTextViewText(R.id.widget_step_recipe_tv, position+" - "+ stepList[position]);

        Intent fillinIntent = new Intent();
        fillinIntent.setData(Uri.parse(fillinIntent.toUri(Intent.URI_INTENT_SCHEME)));
        fillinIntent.putExtra("step-id", position);
        fillinIntent.putExtra("selected-recipe", mSelectedRecipe);

        remoteViews.setOnClickFillInIntent(R.id.widget_step_recipe_tv, fillinIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

