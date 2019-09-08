package com.example.tanushree.bakingtime.controller;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

// Date: 6.6.19

class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public DividerItemDecoration(Drawable divider)
    {
        mDivider = divider;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state)
    {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildAdapterPosition(view) == 0)
        {
            return;
        }

        outRect.top = mDivider.getIntrinsicHeight();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, State state) {
        super.onDraw(c, parent, state);
        int dividerLeft = 32;
        int dividerRight = parent.getWidth() - 32;

        for(int i=0; i<parent.getChildCount(); i++)
        {
            if(i!=parent.getChildCount()-1)
            {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight,dividerBottom);
                mDivider.draw(c);
            }
        }
    }
}