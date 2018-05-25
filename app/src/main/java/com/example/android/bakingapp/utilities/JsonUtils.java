package com.example.android.bakingapp.utilities;

import com.example.android.bakingapp.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class JsonUtils {

    private static final String ID = "id";
    private static final String RECIPE_NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String INGREDIENT_QUANTITY = "quantity";
    private static final String INGREDIENT_MEASURE = "measure";
    private static final String INGREDIENT_DESCRIPTION = "ingredient";
    private static final String STEPS = "steps";
    private static final String STEPS_ID = "id";
    private static final String STEPS_SHORT_DESCRIPTION = "shortDescription";
    private static final String STEPS_VIDEO_URL = "videoURL";
    private static final String STEPS_THUMBNAIL_URL = "thumbnailURL";
    private static final String RECIPE_SERVINGS = "servings";
    private static final String RECIPE_IMAGE = "image";


    public static List<Recipe> getRecipesName(JSONArray json) throws JSONException {

        List<Recipe> parseRecipeList = new ArrayList<>();

        if (json != null) {
            for (int i = 0; i < json.length(); ++i){
                Recipe parseRecipe = new Recipe();
                JSONObject recipeObject = json.getJSONObject(i);
                parseRecipe.setRecipeName(recipeObject.optString(RECIPE_NAME));
                parseRecipe.setRecipeId(recipeObject.optString(ID));

                parseRecipeList.add(parseRecipe);
            }
        }
        Timber.d("recipe parse: " + parseRecipeList.toString());
        return parseRecipeList;
    }

    public static List<String> getRecipeSteps (JSONArray json, int id) {

        List<String> recipeSteps = new ArrayList<>();
        JSONObject stepJson = json.optJSONObject(id);

        if (stepJson != null) {
            JSONArray jsonArray = stepJson.optJSONArray(STEPS);
            for (int i = 0; i < jsonArray.length(); i++ ){
                recipeSteps.add(jsonArray.opt(i).toString());
            }
        }
        return recipeSteps;
    }
}