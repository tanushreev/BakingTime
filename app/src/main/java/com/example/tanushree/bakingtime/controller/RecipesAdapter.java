package com.example.tanushree.bakingtime.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tanushree.bakingtime.IdlingResource.SimpleIdlingResource;
import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>
{
    private List<Recipe> mRecipeList;
    private final RecipesAdapterOnClickHandler mClickHandler;
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public void setRecipesData(List<Recipe> recipeList, SimpleIdlingResource idlingResource)
    {
        mRecipeList = recipeList;
        mIdlingResource = idlingResource;
        notifyDataSetChanged();
    }

    public RecipesAdapter(RecipesAdapterOnClickHandler clickHandler)
    {
        mClickHandler = clickHandler;
    }

    // Inner Interface
    public interface RecipesAdapterOnClickHandler
    {
        void onClick(Recipe recipe);
    }


    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.recipes_list_item, viewGroup, false);
        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder recipesViewHolder, int i)
    {
        Recipe recipe = mRecipeList.get(i);
        recipesViewHolder.bindRecipe(recipe);
    }

    @Override
    public int getItemCount() {
        if(mRecipeList!=null) {
            return mRecipeList.size();
        }
        return 0;
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        ImageView mRecipeImageIv;
        TextView mRecipeNameTv;

        public RecipesViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mRecipeImageIv = itemView.findViewById(R.id.ivRecipeImage);
            mRecipeNameTv = itemView.findViewById(R.id.tvRecipeName);
            itemView.setOnClickListener(this);
        }

        public void bindRecipe(Recipe recipe)
        {
            int imageResId;

            switch(recipe.getId())
            {
                case 1:
                    imageResId = R.drawable.nutella_pie;
                    break;
                case 2:
                    imageResId = R.drawable.brownies;
                    break;
                case 3:
                    imageResId = R.drawable.yellow_cake;
                    break;
                case 4:
                    imageResId = R.drawable.cheesecake;
                    break;
                default:
                    imageResId = R.drawable.baking;
            }

            if(recipe.getImagePath().isEmpty())
            {
                mRecipeImageIv.setImageResource(imageResId);
            }
            else
            {
                // For testing purpose.
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(false);
                }

                Picasso.get()
                        .load(recipe.getImagePath())
                        .error(R.drawable.baking)
                        .into(mRecipeImageIv);

                // For testing purpose.
                if (mIdlingResource != null) {
                    mIdlingResource.setIdleState(true);
                }
            }

            if(recipe.getName().isEmpty())
            {
                mRecipeNameTv.setText(R.string.data_not_available);
            }
            else {
                mRecipeNameTv.setText(recipe.getName());
            }
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }
}