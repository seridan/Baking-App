package com.example.android.bakingapp.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.MainActivityAdapter;
import com.example.android.bakingapp.interfaces.CommunicateFragments;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utilities.JsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

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
                .add(R.id.recipe_list_fragment, mainFragment)
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
                .replace(R.id.recipe_list_fragment, detailFragment)
                .addToBackStack(null)
                .commit();

    }
}
