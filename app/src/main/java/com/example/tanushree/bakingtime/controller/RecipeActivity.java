package com.example.tanushree.bakingtime.controller;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Recipe;

public class RecipeActivity extends AppCompatActivity implements StepsFragment.OnStepClickListener
{
    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(findViewById(R.id.llRecipeActivity) != null)
            mTwoPane = true;
        else
            mTwoPane = false;

        Intent intentThatStartedThisActivity = getIntent();

        if(intentThatStartedThisActivity!=null) {
            if (intentThatStartedThisActivity.hasExtra(MainActivity.KEY_RECIPE_DATA)) {
                mRecipe = intentThatStartedThisActivity
                        .getParcelableExtra(MainActivity.KEY_RECIPE_DATA);

                if(mRecipe.getName().isEmpty())
                    setTitle(R.string.app_name);
                else
                    setTitle(mRecipe.getName());

                // Only create new fragments when there is no previously saved state.

                if(savedInstanceState == null) {

                    StepsFragment stepsFragment = new StepsFragment();
                    stepsFragment.setRecipe(mRecipe);

                    // Use a FragmentManager and transaction to add the fragment to the screen.
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    if(!mTwoPane) {
                        fragmentManager.beginTransaction()
                                .add(R.id.flStepsContainer, stepsFragment)
                                .commit();
                    }
                    else
                    {
                        // Display the steps fragment.
                        fragmentManager.beginTransaction()
                                .add(R.id.flStepsFragmentContainer, stepsFragment)
                                .commit();

                        // Display the detail fragment for the 1st step.
                        DetailViewPagerFragment detailViewPagerFragment = new DetailViewPagerFragment();
                        detailViewPagerFragment.setStepList(mRecipe.getSteps(), 0, mTwoPane);

                        fragmentManager.beginTransaction()
                                .add(R.id.flDetailFragmentContainer, detailViewPagerFragment)
                                .commit();
                    }
                }
            }
        }
    }

    @Override
    public void onStepSelected(int stepIndex)
    {
        DetailViewPagerFragment detailViewPagerFragment = new DetailViewPagerFragment();
        detailViewPagerFragment.setStepList(mRecipe.getSteps(), stepIndex, mTwoPane);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if(!mTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.flStepsContainer, detailViewPagerFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else
        {
            fragmentManager.beginTransaction()
                    .replace(R.id.flDetailFragmentContainer, detailViewPagerFragment)
                    .commit();
        }
    }

    // This method is called when the Up button is pressed.
    @Override
    public boolean onSupportNavigateUp() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0)
        {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        else {
            return super.onSupportNavigateUp();
        }
    }
}