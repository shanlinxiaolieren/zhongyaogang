package com.zhongyaogang.activity;

        import android.util.AttributeSet;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.LinearLayout;
        import android.content.Context;

        import com.zhongyaogang.R;

public class SouYeActivity extends LinearLayout {

    public  SouYeActivity (Context context) {
        super(context);

        init(context);
    }


    public SouYeActivity (Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        LayoutInflater mInflater = LayoutInflater.from(context);

        View v = mInflater.inflate(R.layout.activity_sou_ye, null);

        addView(v,lp);

    }




}

