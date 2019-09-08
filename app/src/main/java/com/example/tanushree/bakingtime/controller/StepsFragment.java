package com.example.tanushree.bakingtime.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Recipe;

public class StepsFragment extends Fragment
{
    private static final String KEY_RECIPE = "recipe";

    private Recipe mRecipe;
    private RecyclerView mIngredientsRv;
    private IngredientsAdapter mIngredientsAdapter;
    private RecyclerView mStepsRv;
    private StepsAdapter mStepsAdapter;
    private OnStepClickListener mCallback;

    //Mandatory constructor for instantiating the fragment.
    public StepsFragment()
    {
    }

    public void setRecipe(Recipe recipe)
    {
        mRecipe = recipe;
    }

    public interface OnStepClickListener
    {
        void onStepSelected(int step_index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //This makes sure that the host activity has implemented the callback interface.
        //If not, it throws an exception.

        try
        {
            mCallback = (OnStepClickListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() + " must implement onImageClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            mRecipe = savedInstanceState.getParcelable(KEY_RECIPE);
        }

        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        mIngredientsRv = rootView.findViewById(R.id.rvIngredients);

        LinearLayoutManager layoutManager = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        mIngredientsRv.setLayoutManager(layoutManager);

        mIngredientsAdapter = new IngredientsAdapter(mRecipe.getIngredients());
        mIngredientsRv.setAdapter(mIngredientsAdapter);

        mIngredientsRv.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

        mStepsRv = rootView.findViewById(R.id.rvSteps);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager
                (getActivity(), LinearLayoutManager.VERTICAL, false);
        mStepsRv.setLayoutManager(layoutManager1);

        mStepsAdapter = new StepsAdapter(mRecipe.getSteps(), mCallback);
        mStepsRv.setAdapter(mStepsAdapter);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        currentState.putParcelable(KEY_RECIPE, mRecipe);
    }
}
