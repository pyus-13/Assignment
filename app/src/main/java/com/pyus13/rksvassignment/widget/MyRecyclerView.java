package com.pyus13.rksvassignment.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 *
 * This class is written just to overcome bug in Recycler View
 * Created by pyus_13 on 1/2/2015.
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void stopScroll()
    {
        try
        {
            super.stopScroll();
        }
        catch( NullPointerException exception )
        {
            /**
             *  The mLayout has been disposed of before the
             *  RecyclerView and this stops the application
             *  from crashing.
             */
        }
    }
}
