package com.example.tanushree.bakingtime.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tanushree.bakingtime.R;
import com.example.tanushree.bakingtime.model.Step;

import java.util.ArrayList;
import java.util.List;

public class DetailViewPagerFragment extends Fragment
{
    private static final String KEY_STEP_LIST = "step_list";

    private List<Step> mStepList;
    private int mStepIndex;
    private boolean mTwoPane;

    public void setStepList(List<Step> stepList, int index, boolean twoPane)
    {
        mStepList = stepList;
        mStepIndex = index;
        mTwoPane = twoPane;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        if(savedInstanceState!=null)
        {
            mStepList = savedInstanceState.getParcelableArrayList(KEY_STEP_LIST);
        }
        View view = inflater.inflate(R.layout.fragment_viewpager_detail, container, false);

        ViewPager viewPager = view.findViewById(R.id.vpDetailViewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                Step step = mStepList.get(i);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setStep(step, mTwoPane);
                return detailFragment;
            }

            @Override
            public int getCount() {
                return mStepList.size();
            }
        });

        viewPager.setCurrentItem(mStepIndex);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState)
    {
        currentState.putParcelableArrayList(KEY_STEP_LIST, (ArrayList<Step>) mStepList);
    }
}