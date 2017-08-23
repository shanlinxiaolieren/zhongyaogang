package com.zhongyaogang.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView{
	 public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {  
	        super(context, attrs, defStyle);  
	        // TODO Auto-generated constructor stub  
	    }  
	  
	  
	    public CustomScrollView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	        // TODO Auto-generated constructor stub  
	    }  
	  
	    public CustomScrollView(Context context) {  
	        super(context);  
	        // TODO Auto-generated constructor stub  
	    }  
	      
	      
	    private OnScrollListener listener;  
	    public interface OnScrollListener{  
	        void onScrollListener(int scrollY);  
	    }  
	    public void setOnScrollListener(OnScrollListener listener){  
	        this.listener = listener;  
	    }  
	      
	      
	    private int lastScrollY;  
	    private Handler mHandler = new Handler(){  
	        public void handleMessage(android.os.Message msg) {  
	            int scrollY = getScrollY();  
	            if(lastScrollY != scrollY){  
	                lastScrollY = scrollY;  
	                mHandler.sendMessageDelayed(mHandler.obtainMessage(), 10);  
	            }  
	        };  
	    };  
	      
	    @Override  
	    public boolean onTouchEvent(MotionEvent ev) {
	        if(null != listener ){  
	            listener.onScrollListener(lastScrollY = getScrollY());  
	        }  
	        switch (ev.getAction()) {  
	        case MotionEvent.ACTION_UP:  
	            mHandler.sendMessageDelayed(mHandler.obtainMessage(), 10);  
	            break;  
	        }  
	        return super.onTouchEvent(ev);  
	    };  
	      
	      
	     @Override  
	    protected void onScrollChanged(int l, int t, int oldl, int oldt) {  
	        // TODO Auto-generated method stub  
	        super.onScrollChanged(l, t, oldl, oldt);  
	        if(null != listener){  
	            listener.onScrollListener(t);  
	            mHandler.sendMessageDelayed(mHandler.obtainMessage(), 10);  
	        }  
	     }
}
