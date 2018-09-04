package com.mahersoua.bakingapp.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.mahersoua.bakingapp.MainActivity;
import com.mahersoua.bakingapp.R;
import com.mahersoua.bakingapp.activities.RecipeStepsDetailsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BankingWidgetProvider extends AppWidgetProvider {

    private static boolean isSmall;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViews());
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void updateWidgetGridView(String[] stepList, Context context) {
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_OPTIONS_CHANGED);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViews());
        remoteViews.setRemoteAdapter(R.id.widget_grid_view, intent);
        ComponentName thisWidget = new ComponentName(context, BankingWidgetProvider.class);
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(thisWidget);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);



        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViews());
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_grid_view, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        // Obtain appropriate widget and update it.
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), getRemoteViews());
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    private static int getRemoteViews() {
        int columns = getCellsForSize(300);

        switch (columns) {
            case 1:  return R.layout.banking_widget_provider_small;
            case 2:  return R.layout.banking_widget_provider;
            default: return R.layout.banking_widget_provider_small;
        }
    }

    private static int getCellsForSize(int size) {
        int n = 2;
        while (70 * n - 30 < size) {
            ++n;
        }
        return n - 1;
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

