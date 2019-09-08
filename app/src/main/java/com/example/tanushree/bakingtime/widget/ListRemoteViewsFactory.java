package com.example.tanushree.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.utils.JsonUtils;
import com.example.tanushree.bakingtime.controller.MainActivity;
import com.example.tanushree.bakingtime.model.Ingredient;
import com.example.tanushree.bakingtime.model.Recipe;

import org.json.JSONException;

import java.util.List;

// Date: 25.5.19

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory
{
    //private static final String TAG = ListRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private List<Recipe> mRecipeList;
    private List<Ingredient> mIngredientList;
    private int mRecipeIndex;

    public ListRemoteViewsFactory(Context applicationContext, Intent intent)
    {
        mContext = applicationContext;
        if(intent!=null)
        {
            if(intent.hasExtra(IngredientsWidgetProvider.KEY_RECIPE_INDEX));
            {
                mRecipeIndex = intent.getIntExtra(IngredientsWidgetProvider.KEY_RECIPE_INDEX, 0);
                //Log.d(TAG, "Recipe Index: " + mRecipeIndex);
            }
            //Log.d(TAG, "ListRemoteViewsFactory constructor called");
        }


    }

    @Override
    public void onCreate() {

    }

    // Called once the RemoteViewsFactory is created as well as when notifyAppWidgetViewDataChanged is called.
    @Override
    public void onDataSetChanged()
    {
        //Log.d(TAG, "onDataSetChanged() Called");

        mSharedPreferences = mContext.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE);
        String json_data = mSharedPreferences.getString(MainActivity.KEY_JSON_DATA, null);
        try {
            mRecipeList = JsonUtils.getRecipeListFromJson(json_data);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        mIngredientList = mRecipeList.get(mRecipeIndex).getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount()
    {
        //Log.d(TAG, "getCount() Called");
        if (mIngredientList == null)
            return 0;
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position)
    {
        //Log.d(TAG, "getViewAt() Called");
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients_list_item);
        Ingredient ingredient = mIngredientList.get(position);
        if(ingredient.getName().isEmpty()) {
            rv.setTextViewText(R.id.tvWidgetIngredient, mContext.getString(R.string.data_not_available));
        }
        else {
            String string = ingredient.getQuantity() + " " + ingredient.getMeasure() + " " + ingredient.getName();
            rv.setTextViewText(R.id.tvWidgetIngredient, string);
        }
        return rv;
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
