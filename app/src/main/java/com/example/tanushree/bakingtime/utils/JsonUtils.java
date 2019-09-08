package com.example.tanushree.bakingtime.utils;

import com.example.tanushree.bakingtime.model.Ingredient;
import com.example.tanushree.bakingtime.model.Recipe;
import com.example.tanushree.bakingtime.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils
{
    private static final String TAG = JsonUtils.class.getSimpleName();
    private static final String KEY_ID = "id";
    private static final String KEY_RECIPE_NAME = "name";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_RECIPE_IMAGE = "image";
    private static final String KEY_INGREDIENT_NAME = "ingredient";
    private static final String KEY_INGREDIENT_QUANTITY = "quantity";
    private static final String KEY_INGREDIENT_MEASURE = "measure";
    private static final String KEY_STEP_NAME = "shortDescription";
    private static final String KEY_STEP_DESCRIPTION = "description";
    private static final String KEY_STEP_VIDEO = "videoURL";
    private static final String KEY_STEP_IMAGE = "thumbnailURL";

    // Suppress default constructor for noninstantiability.
    private JsonUtils()
    {
        throw new AssertionError();
    }

    public static List<Recipe> getRecipeListFromJson(String recipeJsonString) throws JSONException
    {
        List<Recipe> recipeList = new ArrayList<Recipe>();

        JSONArray recipes = new JSONArray(recipeJsonString);

        JSONObject recipeData;
        JSONArray ingredients;
        JSONArray steps;
        JSONObject ingredientData;
        JSONObject stepData;

        int recipe_id, step_id;
        String recipe_name, recipe_imagePath, ingredient_name, ingredient_measure, step_name,
                step_description, step_videoUrl, step_imageUrl;
        double ingredient_quantity;
        List<Ingredient> recipe_ingredientList;
        List<Step> recipe_stepList;
        Recipe recipe;
        Ingredient ingredient;
        Step step;

        for(int i=0; i<recipes.length(); i++)
        {
            recipeData = recipes.getJSONObject(i);

            recipe_id = recipeData.getInt(KEY_ID);
            recipe_name = recipeData.getString(KEY_RECIPE_NAME);
            ingredients = recipeData.getJSONArray(KEY_INGREDIENTS);
            steps = recipeData.getJSONArray(KEY_STEPS);
            recipe_imagePath = recipeData.getString(KEY_RECIPE_IMAGE);

            recipe_ingredientList = new ArrayList<Ingredient>();
            for(int j=0; j<ingredients.length();j++)
            {
                ingredientData = ingredients.getJSONObject(j);
                ingredient_name = ingredientData.getString(KEY_INGREDIENT_NAME);
                ingredient_quantity = ingredientData.getDouble(KEY_INGREDIENT_QUANTITY);
                ingredient_measure = ingredientData.getString(KEY_INGREDIENT_MEASURE);

                //Log.i(TAG, ingredient_name);
                //Log.i(TAG, ingredient_quantity+"");
                //Log.i(TAG, ingredient_measure);

                ingredient = new Ingredient();
                ingredient.setName(ingredient_name);
                ingredient.setQuantity(ingredient_quantity);
                ingredient.setMeasure(ingredient_measure);

                recipe_ingredientList.add(ingredient);
            }
            //Log.d(TAG, "Size in JsonUtils is " + recipe_ingredientList.size());

            recipe_stepList = new ArrayList<Step>();
            for(int k=0; k<steps.length(); k++)
            {
                stepData = steps.getJSONObject(k);
                step_id = stepData.getInt(KEY_ID);
                step_name = stepData.getString(KEY_STEP_NAME);
                step_description = stepData.getString(KEY_STEP_DESCRIPTION);
                step_videoUrl = stepData.getString(KEY_STEP_VIDEO);
                step_imageUrl = stepData.getString(KEY_STEP_IMAGE);

                //Log.i(TAG, step_id+"");
                //Log.i(TAG, step_name);
                //Log.i(TAG, step_description);
                //Log.i(TAG, step_videoUrl);

                step = new Step();
                step.setId(step_id);
                step.setName(step_name);
                step.setDescription(step_description);
                step.setVideoUrl(step_videoUrl);
                step.setImageUrl(step_imageUrl);

                recipe_stepList.add(step);
            }

            //Log.i(TAG, id+"");
            //Log.i(TAG, name);

            recipe = new Recipe();
            recipe.setId(recipe_id);
            recipe.setName(recipe_name);
            recipe.setIngredients(recipe_ingredientList);
            recipe.setSteps(recipe_stepList);
            recipe.setImagePath(recipe_imagePath);

            recipeList.add(recipe);
        }

        return recipeList;
    }
}
