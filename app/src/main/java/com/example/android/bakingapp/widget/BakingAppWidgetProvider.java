package com.example.android.bakingapp.widget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.ui.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        List<Ingredients> mListIngredients;
        String mRecipeName;
        SharedPreferences sharedPreferences
                = context.getSharedPreferences(context.getString(R.string.ingredients_shared_preference), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(context.getString(R.string.ingredients_list_key), null);
        Type type = new TypeToken<List<Ingredients>>() {}.getType();
        mListIngredients = gson.fromJson(json, type);
        mRecipeName = sharedPreferences.getString(context.getString(R.string.recipe_mane_key), null);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_update_label, mRecipeName);
        if (mListIngredients != null) {
            views.setTextViewText(R.id.appwidget_update, TextUtils.join("", mListIngredients));
        } else {
            views.setTextViewText(R.id.appwidget_update, (context.getString(R.string.select_recipe_first_message)));
        }

        //Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Set the click handler to launch pending intents
        views.setOnClickPendingIntent(R.id.appwidget_update_label, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
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

