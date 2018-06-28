package com.example.android.bakingapp.ui;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.CommunicateFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CommunicateFragment {

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEPS_LIST = "stepsList";
    private static final String ARG_STEP_ID = "stepId";
    private static final String ARG_INGREDIENTS_LIST = "ingredientsList";
    private static final String ARG_RECIPE_NAME = "recipe_name";

    DetailFragment mDetailFragment;
    MediaFragment mMediaFragment;
    IngredientsFragment mIngredientsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeMainFragment mainFragment = new RecipeMainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_list_container, mainFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void sendRecipe(Recipe recipe) {

        mDetailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_RECIPE, recipe);
        mDetailFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, mDetailFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void sendFragmentStep(List<Step> stepList, int id) {
        mMediaFragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_STEPS_LIST, (ArrayList<? extends Parcelable>) stepList);
        bundle.putInt(ARG_STEP_ID, id);
        mMediaFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, mMediaFragment)
                .addToBackStack(null)
                .commit();
        }

    @Override
    public void sendFragmentIngredients(List<Ingredients> ingredientsList, String recipeName) {

        mIngredientsFragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_INGREDIENTS_LIST, (ArrayList<? extends Parcelable>) ingredientsList);
        bundle.putString(ARG_RECIPE_NAME, recipeName);
        mIngredientsFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, mIngredientsFragment)
                .addToBackStack(null)
                .commit();

    }
}
