package com.example.tanushree.bakingtime.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Step;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>
{
    private List<Step> mStepList;
    private StepsFragment.OnStepClickListener mListener;

    public StepsAdapter(List<Step> stepList, StepsFragment.OnStepClickListener listener)
    {
        mStepList = stepList;
        mListener = listener;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.steps_list_item, viewGroup, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder stepsViewHolder, int i) {
        Step step = mStepList.get(i);
        stepsViewHolder.bindStep(step, i);
    }

    @Override
    public int getItemCount() {
        if(mStepList!=null) {
            return mStepList.size();
        }
        return 0;
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView mStepTv;
        private int mStepIndex;

        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);
            mStepTv = itemView.findViewById(R.id.tvStep);
            itemView.setOnClickListener(this);
        }

        public void bindStep(Step step, int index)
        {
            if(step.getName().isEmpty())
            {
                mStepTv.setText(R.string.data_not_available);
            }
            else
            {
                mStepTv.setText(step.getName());
            }
            mStepIndex = index;
        }

        @Override
        public void onClick(View v)
        {
            // Here, mListener is pointing to the activity instance.
            mListener.onStepSelected(mStepIndex);
        }
    }
}
