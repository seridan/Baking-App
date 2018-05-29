package com.example.android.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.CommunicateFragments;
import com.example.android.bakingapp.model.Recipe;

public class MainActivity extends AppCompatActivity implements CommunicateFragments {

    private static final String ARG_RECIPE = "recipe";
    DetailFragment detailFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecipeMainFragment mainFragment = new RecipeMainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_list_container, mainFragment)
                .commit();
    }


    @Override
    public void sendRecipe(Recipe recipe) {

        detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_RECIPE, recipe);
        detailFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, detailFragment)
                .addToBackStack(null)
                .commit();

    }
}
