package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientFragmentAdapter
        extends RecyclerView.Adapter<IngredientFragmentAdapter.FragmentIngredientViewHolder>{

    private List<Ingredients> mIngredientList;

    public IngredientFragmentAdapter(List<Ingredients> ingredientList) {
        mIngredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientFragmentAdapter.FragmentIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.fragment_ingredient_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        FragmentIngredientViewHolder viewHolder = new FragmentIngredientViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientFragmentAdapter.FragmentIngredientViewHolder holder, int position) {

        float quantity = mIngredientList.get(position).getQuantity();
        String measure = mIngredientList.get(position).getMeasure();
        String formattedText = quantity + ": " + measure;

        holder.mMeasureQuatityLabel.setText(formattedText);
        holder.mIngredientTv.setText(mIngredientList.get(position).getIngredient());

    }

    @Override
    public int getItemCount() {
        if (null == mIngredientList) return 0;
        return mIngredientList.size();
    }

    public class FragmentIngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.measure_quantity_label)
        TextView mMeasureQuatityLabel;

        @BindView(R.id.ingredient_tv)
        TextView mIngredientTv;

        public FragmentIngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setIngredientList(List<Ingredients> ingredientList){
        mIngredientList = ingredientList;
        notifyDataSetChanged();
    }
}
