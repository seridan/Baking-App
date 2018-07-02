package com.example.android.bakingapp.ui;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.interfaces.CommunicateFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements CommunicateFragment {

    private static final String ARG_RECIPE = "recipe";
    private static final String ARG_STEPS_LIST = "stepsList";
    private static final String ARG_STEP_ID = "stepId";
    private static final String ARG_INGREDIENTS_LIST = "ingredientsList";
    private static final String ARG_RECIPE_NAME = "recipe_name";
    private static final String ARG_TWO_PANE = "isTwoPane";
    private boolean mTwoPane;

    DetailFragment mDetailFragment;
    MediaFragment mMediaFragment;
    IngredientsFragment mIngredientsFragment;
    RecipeMainFragment mainFragment;

    @BindView(R.id.detail_toolbar)
    Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recreate();
            }
        });



        if(findViewById(R.id.fragment_tablet_linear_layout) != null){
            mTwoPane = true;
        } else {
            mTwoPane = false;
            if (savedInstanceState != null){
                return;
            }
            }

        RecipeMainFragment mainFragment = new RecipeMainFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_TWO_PANE, mTwoPane);
        mainFragment.setArguments(bundle);
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

        if (!mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list_container, mDetailFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            findViewById(R.id.fragment_list_container).setVisibility(View.GONE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.master_list_fragment, mDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void sendFragmentStep(List<Step> stepList, int id) {

       mMediaFragment = new MediaFragment();
       Bundle bundle = new Bundle();
       bundle.putParcelableArrayList(ARG_STEPS_LIST, (ArrayList<? extends Parcelable>) stepList);
       bundle.putInt(ARG_STEP_ID, id);
       bundle.putBoolean(ARG_TWO_PANE, mTwoPane);
       mMediaFragment.setArguments(bundle);

        if (!mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_list_container, mMediaFragment)
                    .addToBackStack(null)
                    .commit();
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.master_detail_fragment, mMediaFragment)
                    .commit();
        }
    }

    @Override
    public void sendFragmentIngredients(List<Ingredients> ingredientsList, String recipeName) {

        mIngredientsFragment = new IngredientsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARG_INGREDIENTS_LIST, (ArrayList<? extends Parcelable>) ingredientsList);
        bundle.putString(ARG_RECIPE_NAME, recipeName);
        mIngredientsFragment.setArguments(bundle);

        if (!mTwoPane) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.master_detail_fragment, mIngredientsFragment)
                    .addToBackStack(null)
                    .commit();
        } else {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.master_detail_fragment, mIngredientsFragment)
                    .commit();
        }
    }
}
