package com.example.android.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.MainActivityAdapter;
import com.example.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

private RecyclerView.Adapter mMainActivityAdapter;
private List<Steps> mListSteps;
@BindView(R.id.tv_item_detail) RecyclerView mRecyclerDetail;

    public RecipeDetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_recipe, container, false);

        mListSteps = new ArrayList<>();
        ButterKnife.bind(this, rootView);
        mRecyclerDetail.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

}

