package com.zhongyaogang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Administrator on 2017/8/17.
 */
public class MyGridView extends GridView {
    private boolean haveScrollbar = false;
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void setHaveScrollbar(boolean haveScrollbar) {
        this.haveScrollbar = haveScrollbar;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.haveScrollbar == false) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
