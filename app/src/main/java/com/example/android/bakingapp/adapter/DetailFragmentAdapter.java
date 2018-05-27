package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Steps;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragmentAdapter
        extends RecyclerView.Adapter<DetailFragmentAdapter.FragmentDetailViewHolder> {

    private List<Steps> mStepList;
    final private ListItemClickListener mOnClickListener;

    /**
     * Handling the onClick items in the recycler view
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);

    }

    public DetailFragmentAdapter(List<Steps> stepList, ListItemClickListener listener) {
        mStepList = stepList;
        mOnClickListener = listener;
    }

    @Override
    public FragmentDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_recipe_list_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        FragmentDetailViewHolder viewHolder = new FragmentDetailViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FragmentDetailViewHolder holder, int position) {

        holder.listItemDetailTv.setText(mStepList.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (null == mStepList) return 0;
        return mStepList.size();
    }

    public class FragmentDetailViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.tv_item_detail) TextView listItemDetailTv;

        public FragmentDetailViewHolder(View itemView) {
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
}
