package com.zhongyaogang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongyaogang.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/18.
 */
public class OrderGoodsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String>data;
    public  OrderGoodsAdapter(Context context,ArrayList<String>data)
    {
        this.context=context;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_ordergoods, null);
            holder.imageView=(ImageView) convertView.findViewById(R.id.imageView);
            holder.tvcontent=(TextView) convertView.findViewById(R.id.tvcontent);
            holder.tvcolour=(TextView) convertView.findViewById(R.id.tvcolour);
            holder.tvnum=(TextView) convertView.findViewById(R.id.tvnum);
            holder.tvmoney=(TextView) convertView.findViewById(R.id.tvmoney);
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    private class ViewHolder {
        ImageView imageView;
        TextView tvcontent;
        TextView tvcolour;
        TextView tvmoney;
        TextView tvnum;
    }
}
