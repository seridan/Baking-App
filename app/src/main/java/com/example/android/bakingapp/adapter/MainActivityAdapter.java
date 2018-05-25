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
        extends RecyclerView.Adapter<MainActivityAdapter.BakingAppViewHolder>{

    private List<Recipe> mRecipeList;
    final private ListItemClickListener mOnClickListener;

    /**
     * Handling the onClick items in the recycler view
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }

    public MainActivityAdapter(List<Recipe> recipeList, ListItemClickListener listener) {
        mRecipeList = recipeList;
        mOnClickListener = listener;
    }

    @Override
    public MainActivityAdapter.BakingAppViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        BakingAppViewHolder viewHolder = new BakingAppViewHolder(view);

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

    public class BakingAppViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_item_recipe) TextView listItemRecipeTv;

        public BakingAppViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void setRecipeList(List<Recipe> recipeList) {
        mRecipeList = recipeList;
        notifyDataSetChanged();
    }
}
