package com.example.tanushree.bakingtime.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.tanushree.bakingtime.IdlingResource.SimpleIdlingResource;
import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.controller.RecipesAdapter.RecipesAdapterOnClickHandler;
import com.example.tanushree.bakingtime.model.Recipe;
import com.example.tanushree.bakingtime.utils.JsonUtils;
import com.example.tanushree.bakingtime.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>, RecipesAdapterOnClickHandler
{

    //private static String TAG = MainActivity.class.getSimpleName();

    private static final int RECIPES_LOADER = 108;

    private static final String EXTRA_URL = "url";

    //public, so that we can access from another class.
    public static final String KEY_RECIPE_DATA = "RECIPE_DATA";

    public static final String MY_PREFERENCES = "MyPrefs";

    public static final String KEY_JSON_DATA = "json_data";

    private RecyclerView mRecipesRv;
    private RecipesAdapter mRecipesAdapter;
    private SharedPreferences mSharedPreferences;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource()
    {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecipesRv = findViewById(R.id.rv_recipes);

        Configuration configuration = getResources().getConfiguration();

        boolean tablet = getResources().getBoolean(R.bool.isTablet);

        int gridColumns;

        if(tablet || (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE))
            gridColumns = 2;
        else
            gridColumns = 1;

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, gridColumns, GridLayoutManager.VERTICAL, false);
        mRecipesRv.setLayoutManager(layoutManager);

        mRecipesAdapter = new RecipesAdapter(this);
        mRecipesRv.setAdapter(mRecipesAdapter);

        getIdlingResource();

        mSharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        URL url = null;

        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();

        bundle.putString(EXTRA_URL, url.toString());

        LoaderManager loaderManager = LoaderManager.getInstance(this);
        Loader<String> recipesLoader = loaderManager.getLoader(RECIPES_LOADER);

        if(isNetworkAvailable()) {
            if (recipesLoader == null)
                loaderManager.initLoader(RECIPES_LOADER, bundle, this);
            else
                loaderManager.restartLoader(RECIPES_LOADER, bundle, this);
        }
        else {
            Toast.makeText
                    (this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo!=null && activeNetworkInfo.isConnected());
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, @Nullable final Bundle bundle)
    {
        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(bundle == null) {
                    //Log.d(TAG, "Bundle is null");
                    return;
                }
                // For testing purpose.
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }
                forceLoad();
            }

            //This method is like doInBackground().
            @Nullable
            @Override
            public String loadInBackground()
            {
                String urlString = bundle.getString(EXTRA_URL);
                if (urlString == null)
                    return null;

                try {
                    URL url = new URL(urlString);
                    String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                    //Log.v(TAG, jsonResponse);
                    return jsonResponse;
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s)
    {
        if(s!=null)
        {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(KEY_JSON_DATA, s);
            editor.apply();
            try
            {
                List<Recipe> recipeList = JsonUtils.getRecipeListFromJson(s);
                mRecipesAdapter.setRecipesData(recipeList, mIdlingResource);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Toast.makeText(MainActivity.this, R.string.data_not_available,
                    Toast.LENGTH_LONG).show();
        }

        // For testing purpose.
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(KEY_RECIPE_DATA, recipe);
        startActivity(intent);
    }
}
