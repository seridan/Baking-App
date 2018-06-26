package com.example.android.bakingapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.IngredientFragmentAdapter;
import com.example.android.bakingapp.model.Ingredients;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    private List<Ingredients> mListIngredient;
    private static final String ARG_INGREDIENTS_LIST = "ingredientsList";

    @BindView(R.id.ingredient_rv)
    RecyclerView mRecyclerIngredient;

    @BindView(R.id.ingredient_label)
    TextView mIngredientLabel;

    @BindView(R.id.ingredient_toolbar)
    Toolbar mIngredientToolbar;

    public IngredientsFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);


        ButterKnife.bind(this, rootView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(mIngredientToolbar);
            Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mIngredientToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerIngredient.setLayoutManager(layoutManager);
        mRecyclerIngredient.setHasFixedSize(true);
        IngredientFragmentAdapter mIngredientFragmentAdapter= new IngredientFragmentAdapter(mListIngredient);
        mRecyclerIngredient.setAdapter(mIngredientFragmentAdapter);

        Bundle ingredientBundle = getArguments();

        if (ingredientBundle != null) {
            mListIngredient = ingredientBundle.getParcelableArrayList(ARG_INGREDIENTS_LIST);
            mIngredientFragmentAdapter.setIngredientList(mListIngredient);
        }

        return rootView;
    }
}
