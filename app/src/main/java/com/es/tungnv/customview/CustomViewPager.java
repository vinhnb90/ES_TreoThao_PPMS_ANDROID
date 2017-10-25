package com.es.tungnv.customview;

/**
 * Allow enable, disable swiper tab layout
 * Created by VinhNB_Local on 12/6/2016.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class CustomViewPager extends android.support.v4.view.ViewPager {
    private boolean enabled;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return enabled ? super.onTouchEvent(event) : false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return enabled ? super.onInterceptTouchEvent(event) : false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isPagingEnabled() {
        return enabled;
    }

}