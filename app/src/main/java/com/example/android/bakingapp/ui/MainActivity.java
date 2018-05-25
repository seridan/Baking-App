package com.example.android.bakingapp.ui;

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

public class MainActivity extends AppCompatActivity
        implements MainActivityAdapter.ListItemClickListener {

    JsonObjectRequest mJsonObjectRequest;
    JsonArrayRequest mJsonArrayRequest;
    RequestQueue mRequestQueue;
    MainActivityAdapter mMainActivityAdapter;

    @BindView(R.id.recipe_rv) RecyclerView mRecyclerRecipes;

    List<Recipe> mListRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListRecipe = new ArrayList<>();

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerRecipes.setLayoutManager(layoutManager);
        mRecyclerRecipes.setHasFixedSize(true);
        mMainActivityAdapter = new MainActivityAdapter(mListRecipe, this);
        mRecyclerRecipes.setAdapter(mMainActivityAdapter);

        mRequestQueue = Volley.newRequestQueue(this);

        loadJsonData();
    }

    private void loadJsonData() {
        String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json#";

        mJsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<Recipe>>(){}.getType();

                                mListRecipe = gson.fromJson(response.toString(), listType);

                                mMainActivityAdapter.setRecipeList(mListRecipe);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error en orResponse", error.toString());
                    }

    });
        mRequestQueue.add(mJsonArrayRequest);

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Toast.makeText(this, "se ha seleccionado la receta: " +
                mListRecipe.get(clickedItemIndex).getRecipeName(), Toast.LENGTH_LONG).show();

    }
}
