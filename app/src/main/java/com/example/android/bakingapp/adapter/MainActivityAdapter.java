package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Recipe;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityAdapter
        extends RecyclerView.Adapter<MainActivityAdapter.BakingAppViewHolder>
        implements View.OnClickListener{

    private List<Recipe> mRecipeList;
    private View.OnClickListener listener;


    public MainActivityAdapter(List<Recipe> recipeList) {
        mRecipeList = recipeList;

    }

    @Override
    public MainActivityAdapter.BakingAppViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_recipe_list_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        BakingAppViewHolder viewHolder = new BakingAppViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainActivityAdapter.BakingAppViewHolder holder, int position) {

        holder.listItemRecipeTv.setText(mRecipeList.get(position).getRecipeName());

    }

    @Override
    public int getItemCount() {
       if (null == mRecipeList) return 0;
       return mRecipeList.size();
    }

    public void setOnclickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }

    }

    public class BakingAppViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_detail) TextView listItemRecipeTv;

        public BakingAppViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }
}
