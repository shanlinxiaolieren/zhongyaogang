package com.zhongyaogang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.view.MyGridView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/17.
 */
public class OrderConfirmAdapter extends BaseAdapter {
    private ArrayList<String>data;
    private Context context;
    private TextView tvnum;
    public OrderConfirmAdapter(Context context, ArrayList<String>data, TextView tvnum)
    {
        this.context=context;
        this.data=data;
        this.tvnum=tvnum;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_orderconfirm, null);
            holder.tvshop=(TextView) convertView.findViewById(R.id.tvshop);
            holder.gv=(MyGridView) convertView.findViewById(R.id.gv);
            holder.rlaygive=(RelativeLayout) convertView.findViewById(R.id.rlaygive);
            holder.tvgive=(TextView) convertView.findViewById(R.id.tvgive);
            holder.edinput=(EditText) convertView.findViewById(R.id.edinput);
            holder.tvmoney=(TextView) convertView.findViewById(R.id.tvmoney);
            convertView.setTag(holder);
        }
        else
        {
          holder=(ViewHolder) convertView.getTag();
        }
        holder.rlaygive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "配送方式", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayList<String>list=new ArrayList<String>();
        for(int i=0;i<=position;i++)
        {
            list.add("");
        }
        SmallOrderAdapter adapter=new SmallOrderAdapter(context,list);
        holder.gv.setAdapter(adapter);
        return convertView;
    }
    private class ViewHolder {
        TextView tvshop;
        MyGridView gv;
        RelativeLayout rlaygive;
        TextView tvgive;
        EditText edinput;
        TextView tvmoney;
    }
}
