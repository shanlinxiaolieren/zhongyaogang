package com.zhongyaogang.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.bean.SearchQiu;

import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */
public class SearchQiuAdapter extends BaseAdapter {
    private Activity context;
    private List<SearchQiu> datas;
    private SearchQiu gongBean;

    public SearchQiuAdapter(Activity context, List<SearchQiu> datas)
    {
        this.context=context;
        this.datas=datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public SearchQiu getItem(int position) {
        return  datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_searchqiu,
                            parent, false);
            viewHolder = new ViewHolder();
            viewHolder.llayitem=(LinearLayout) convertView.findViewById(R.id.llayitem);
            viewHolder.specifiedMerchandise=(TextView) convertView.findViewById(R.id.specifiedMerchandise);
            viewHolder.details=(TextView) convertView.findViewById(R.id.details);
            viewHolder.jixutegong_number=(TextView) convertView.findViewById(R.id.jixutegong_number);
            viewHolder.tvCompany=(TextView) convertView.findViewById(R.id.tvCompany);
            viewHolder.tvbottom=(TextView) convertView.findViewById(R.id.tvbottom);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.details.setText(getItem(position).getDetails());
        viewHolder.jixutegong_number.setText(getItem(position).getWeight());
        viewHolder.tvCompany.setText(getItem(position).getUnits());
        viewHolder.tvbottom.setText(getItem(position).getSpecifiedMerchandise());
        viewHolder.llayitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "购买", Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
    class ViewHolder {
       LinearLayout llayitem;
        TextView specifiedMerchandise;
        TextView details;
        TextView jixutegong_number;
        TextView tvCompany;
        TextView tvbottom;
    }
}
