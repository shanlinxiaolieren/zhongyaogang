package com.zhongyaogang.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.activity.PromptlyShoppingOrderActivity;
import com.zhongyaogang.activity.ShangPinXiangQingActivity;
import com.zhongyaogang.bean.GongBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/25.
 */
public class SearchAdapter extends BaseAdapter {
    private  Activity context;
    private List<GongBean> datas;
    private GongBean gongBean;

    public SearchAdapter(Activity context, List<GongBean> datas)
    {
        this.context=context;
        this.datas=datas;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public GongBean getItem(int position) {
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
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.item_search,
                            parent, false);
            viewHolder = new ViewHolder();
            viewHolder.yaomingchengpicture=(ImageView) convertView.findViewById(R.id.yaomingchengpicture);
            viewHolder.yaomingcheng=(TextView) convertView.findViewById(R.id.yaomingcheng);
            viewHolder.yaoprice=(TextView) convertView.findViewById(R.id.yaoprice);
            viewHolder.gouma=(Button) convertView.findViewById(R.id.gouma);
            convertView.setTag(viewHolder);
        }
        else
        {
         viewHolder=(ViewHolder)convertView.getTag();
        }
        //设置图片内容
        ImageLoader.getInstance().displayImage(getItem(position).getPigUrl(),
                viewHolder.yaomingchengpicture);
        viewHolder.yaomingcheng.setText(getItem(position).getMerchandiseName());
        viewHolder.yaoprice.setText("¥"+getItem(position).getPrice());
        viewHolder.yaomingchengpicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gongBean=datas.get(position);
                Intent intent=new Intent(context,ShangPinXiangQingActivity.class);
                intent.putExtra("id", gongBean.getId());
                intent.putExtra("merchandiseName", gongBean.getMerchandiseName());
                context.startActivity(intent);
            }
        });
        viewHolder.gouma.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gongBean=datas.get(position);
                Intent intent=new Intent(context, PromptlyShoppingOrderActivity.class);
                intent.putExtra("id", gongBean.getId());
                intent.putExtra("supplyTitle", gongBean.getFreightTitle());
                intent.putExtra("supplyNo", gongBean.getSupplyNo());
                intent.putExtra("stock", gongBean.getStock());
                intent.putExtra("merchandiseName", gongBean.getMerchandiseName());
                intent.putExtra("shopName", gongBean.getShopName());
                intent.putExtra("freightTitle", gongBean.getFreightTitle());
                intent.putExtra("price", gongBean.getPrice());
                intent.putExtra("moq", gongBean.getMoq());
                intent.putExtra("units", gongBean.getUnits());
                intent.putExtra("quantity", gongBean.getQuantity());
                intent.putExtra("pigUrl", gongBean.getPigUrl());
                context.startActivity(intent);

            }
        });
        return convertView;
    }
    class ViewHolder {
        ImageView yaomingchengpicture;
        TextView yaomingcheng;
        TextView yaoprice;
        Button gouma;
    }
}
