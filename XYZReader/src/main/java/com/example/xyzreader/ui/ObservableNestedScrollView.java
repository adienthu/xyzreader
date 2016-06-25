package com.example.xyzreader.ui;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ObservableNestedScrollView extends NestedScrollView{

    private static final String LOG_TAG = ObservableNestedScrollView.class.getSimpleName();

    private OnScrollListener mListener;

    public ObservableNestedScrollView(Context context) {
        super(context);
        Log.d(LOG_TAG,"C1");
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(LOG_TAG, "C2");
    }

    public ObservableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(LOG_TAG, "C3");
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Log.d(LOG_TAG, "Scrolled by (" + dxConsumed + ", " + dyConsumed + ")");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        super.onNestedPreScroll(target, dx, dy, consumed);
        Log.d(LOG_TAG,"onNestedPreScroll");
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        boolean result = super.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
//        if (result) {
//
//        }else {
//            Log.d(LOG_TAG,"dispatchNestedScroll false");
//        }
        Log.d(LOG_TAG,"dispatchNestedScroll: (" + dxConsumed + ", " + dyConsumed + ")");
        mListener.onScroll(dxConsumed, dyConsumed);
        return result;
    }

    public OnScrollListener getOnScrollListener() {
        return mListener;
    }

    public void setOnScrollListener(OnScrollListener mListener) {
        this.mListener = mListener;
    }

    interface OnScrollListener {
        void onScroll(int dx, int dy);
    }
}
