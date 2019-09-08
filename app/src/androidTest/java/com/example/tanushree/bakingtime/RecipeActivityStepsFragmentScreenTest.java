package com.example.tanushree.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.tanushree.bakingtime.utils.JsonUtils;
import com.example.tanushree.bakingtime.controller.MainActivity;
import com.example.tanushree.bakingtime.controller.RecipeActivity;
import com.example.tanushree.bakingtime.model.Recipe;

import org.json.JSONException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

// Date: 4.6.19

@RunWith(AndroidJUnit4.class)
public class RecipeActivityStepsFragmentScreenTest
{
    private Context mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private Recipe getRecipe()
    {
        String json_data = mContext.getSharedPreferences(MainActivity.MY_PREFERENCES, Context.MODE_PRIVATE)
                .getString(MainActivity.KEY_JSON_DATA, null);

        try
        {
            List<Recipe> recipeList = JsonUtils.getRecipeListFromJson(json_data);
            return recipeList.get(1);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<RecipeActivity>(RecipeActivity.class) {
        @Override
        protected Intent getActivityIntent()
        {
            return new Intent(mContext, RecipeActivity.class)
                    .putExtra(MainActivity.KEY_RECIPE_DATA, getRecipe());
        }
    };

    @Test
    public void clickRecyclerViewItem_OpensDetailFragment()
    {
        onView(withId(R.id.rvSteps)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        onView(withId(R.id.vpDetailViewPager)).check(matches(isDisplayed()));
    }
}
