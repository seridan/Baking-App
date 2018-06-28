package com.example.android.bakingapp.interfaces;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public interface CommunicateFragment {

    void sendRecipe(Recipe recipe);

    void sendFragmentStep(List<Step> stepList, int id);

    void sendFragmentIngredients(List<Ingredients> ingredientsList, String recipeName);

}
