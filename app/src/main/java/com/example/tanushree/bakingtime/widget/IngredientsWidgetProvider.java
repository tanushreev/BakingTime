package com.example.tanushree.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.utils.JsonUtils;
import com.example.tanushree.bakingtime.controller.MainActivity;
import com.example.tanushree.bakingtime.model.Recipe;

import org.json.JSONException;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 * Date: 25.5.19
 */

public class IngredientsWidgetProvider extends AppWidgetProvider
{
    //private static final String TAG = IngredientsWidgetProvider.class.getSimpleName();
    private static final String WIDGET_RECIPE_PREFERENCES = "WidgetRecipePrefs";
    public static final String KEY_RECIPE_INDEX = "recipe_index";
    private static SharedPreferences mSharedPreferences;
    private static List<Recipe> mRecipeList = null;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId)
    {
        //Log.d(TAG, "updateAppWidget() Called");

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_provider);

        populateRecipeList(context);

        // Get the recipe index from the shared preferences.
        mSharedPreferences = context.getSharedPreferences(WIDGET_RECIPE_PREFERENCES, Context.MODE_PRIVATE);
        int recipe_index = mSharedPreferences.getInt(KEY_RECIPE_INDEX, 0);
        //Log.d(TAG, "Recipe Index: " + recipe_index);

        // Populate the Recipe Name Textview.
        String recipe_name = mRecipeList.get(recipe_index).getName();

        if(recipe_name.isEmpty())
            views.setTextViewText(R.id.tvWidgetRecipeName, context.getString(R.string.data_not_available));
        else
            views.setTextViewText(R.id.tvWidgetRecipeName, recipe_name);

        // Populates the Ingredients ListView
        Intent intentListWidgetService = new Intent(context, ListWidgetService.class);
        intentListWidgetService.putExtra(KEY_RECIPE_INDEX, recipe_index);
        intentListWidgetService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intentListWidgetService.setData(Uri.parse(intentListWidgetService.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.lvWidgetIngredients, intentListWidgetService);

        if(recipe_index < mRecipeList.size()-1)
           recipe_index++;
        else
           recipe_index = 0;

        // Updates the recipe_index in the Shared Preferences.
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(KEY_RECIPE_INDEX, recipe_index) ;
        editor.apply();

        // Create an Intent to launch Mainactivity.
        Intent intentMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntentMainActivity = PendingIntent
               .getActivity(context, 0, intentMainActivity, 0);
        views.setOnClickPendingIntent(R.id.tvWidgetRecipeName, pendingIntentMainActivity);

        // Update the Widget on Click of the Next ImageButton.
        Intent intentUpdate = new Intent(context, IngredientsWidgetProvider.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent pendingIntentUpdate = PendingIntent
                .getBroadcast(context, 0, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ibWidgetNext, pendingIntentUpdate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        //Log.d(TAG, "onUpdate() Called");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        //Log.d(TAG, "onRecieve() Called");
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void populateRecipeList(Context context)
    {
        String json_data = context.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
                .getString(MainActivity.KEY_JSON_DATA, null);
        try
        {
            mRecipeList = JsonUtils.getRecipeListFromJson(json_data);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}