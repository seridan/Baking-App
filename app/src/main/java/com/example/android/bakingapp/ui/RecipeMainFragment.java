package com.example.android.bakingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.MainActivityAdapter;
import com.example.android.bakingapp.interfaces.CommunicateFragment;
import com.example.android.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeMainFragment extends Fragment {

    private JsonArrayRequest mJsonArrayRequest;
    private RequestQueue mRequestQueue;
    private MainActivityAdapter mMainActivityAdapter;
    private List<Recipe> mListRecipe;
    private boolean mTwoPane;

    private static final String URL_JSON
            = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json#";
    private static final String ARG_TWO_PANE = "isTwoPane";
    @BindView(R.id.recipe_rv) RecyclerView mRecyclerRecipes;

    Activity mActivity;
    CommunicateFragment communicateFragment;

    public RecipeMainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main_recipe, container, false);

        mListRecipe = new ArrayList<>();

        ButterKnife.bind(this, rootView);
        Timber.plant(new Timber.DebugTree());

        final Bundle stepBundle = getArguments();

        if (stepBundle != null) {

            mTwoPane = stepBundle.getBoolean(ARG_TWO_PANE);
        }


        if (mTwoPane) {
            mRecyclerRecipes.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            mRecyclerRecipes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
        mRecyclerRecipes.setHasFixedSize(true);
        mMainActivityAdapter = new MainActivityAdapter(mListRecipe);
        mRecyclerRecipes.setAdapter(mMainActivityAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mRequestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        }

        loadJsonData();

        mMainActivityAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communicateFragment.sendRecipe
                        (mListRecipe.get(mRecyclerRecipes.getChildAdapterPosition(view)));
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (Activity) context;
            communicateFragment = (CommunicateFragment) mActivity;
        }
    }

    private void loadJsonData() {

        mJsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET,
                        URL_JSON,
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

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), R.string.error_connection_message, Toast.LENGTH_LONG).show();
                        }
                    }

                });
        mRequestQueue.add(mJsonArrayRequest);

    }

}

