package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragmentAdapter
        extends RecyclerView.Adapter<DetailFragmentAdapter.FragmentDetailViewHolder>
        implements View.OnClickListener{

    private List<Step> mStepList;
    private View.OnClickListener mListener;



    public DetailFragmentAdapter(List<Step> stepList) {
        mStepList = stepList;

    }

    @Override
    public FragmentDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.fragment_recipe_list_item_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        FragmentDetailViewHolder viewHolder = new FragmentDetailViewHolder(view);
        view.setOnClickListener(this);

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

    public void setOnclickListener(View.OnClickListener listener){
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }
    }

    public class FragmentDetailViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_detail) TextView listItemDetailTv;

        public FragmentDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

    public void setStepList(List<Step> recipeSteps) {
        mStepList = recipeSteps;
        notifyDataSetChanged();

    }
}
