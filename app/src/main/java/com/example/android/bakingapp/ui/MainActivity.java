package com.example.android.bakingapp.ui;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.CommunicateFragmentRecipe;
import com.example.android.bakingapp.interfaces.ComunicateFragmentStep;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CommunicateFragmentRecipe,
        ComunicateFragmentStep {

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEPS_LIST = "stepsList";
    private static final String ARG_STEP_ID = "stepId";
    DetailFragment detailFragment;
    MediaFragment mediaFragment;

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

    @Override
    public void sendStep(List<Step> stepList, int id) {
        mediaFragment = new MediaFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_STEPS_LIST, (ArrayList<? extends Parcelable>) stepList);
        bundle.putInt(ARG_STEP_ID, id);
        mediaFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_list_container, mediaFragment)
                .addToBackStack(null)
                .commit();
    }
}
