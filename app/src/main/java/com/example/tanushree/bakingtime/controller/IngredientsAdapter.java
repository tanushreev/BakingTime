package com.example.tanushree.bakingtime.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Ingredient;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>
{
    private List<Ingredient> mIngredientList;

    public IngredientsAdapter(List<Ingredient> ingredientList)
    {
        mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ingredients_list_item, viewGroup, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int i)
    {
        Ingredient ingredient = mIngredientList.get(i);
        ingredientsViewHolder.bindIngredient(ingredient);

    }

    @Override
    public int getItemCount() {
        if(mIngredientList!=null) {
            return mIngredientList.size();
        }
        return 0;
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder
    {
        private TextView mIngredientTv;

        public IngredientsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mIngredientTv = itemView.findViewById(R.id.tvIngredient);
        }

        public void bindIngredient(Ingredient ingredient)
        {
            if(ingredient.getName().isEmpty())
            {
                mIngredientTv.setText(R.string.data_not_available);
            }
            else
            {
                String string = ingredient.getQuantity() + " " + ingredient.getMeasure()
                        + " " + ingredient.getName();
                mIngredientTv.setText(string);
            }
        }
    }
}
