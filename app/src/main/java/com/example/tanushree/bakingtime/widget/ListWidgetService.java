package com.example.tanushree.bakingtime.widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

// Date 26.5.19

public class ListWidgetService extends RemoteViewsService
{
    //private static final String TAG = ListWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        //Log.d(TAG, "onGetViewFactory() in RemoteViewsService called");
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
