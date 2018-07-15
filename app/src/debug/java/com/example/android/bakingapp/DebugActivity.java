package com.example.android.bakingapp;

import android.support.v4.app.FragmentActivity;

import com.example.android.bakingapp.interfaces.CommunicateFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class DebugActivity extends FragmentActivity implements CommunicateFragment {


    @Override
    public void sendRecipe(Recipe recipe) {

    }

    @Override
    public void sendFragmentStep(List<Step> stepList, int id) {

    }

    @Override
    public void sendFragmentIngredients(List<Ingredients> ingredientsList, String recipeName) {

    }
}
