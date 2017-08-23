package com.zhongyaogang.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.DanGeQueRenDingDanActivity;
import com.zhongyaogang.bean.AddressBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class QueRenDingDanAddressAdapter extends BaseAdapter {
    private List<AddressBean> datas;
    private Context context;
     private DanGeQueRenDingDanActivity act;
    public QueRenDingDanAddressAdapter(Context act, List<AddressBean> datas) {
        super();
        this.context = act;
        this.datas = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.inflate_dangequerendingdan_address_item, null);
            holder.shouhuoaddresssuoming= (TextView) convertView.findViewById(R.id.shouhuoaddresssuoming);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.textview_addressphone);
            holder.tvConsigneeName = (TextView) convertView.findViewById(R.id.tvConsigneeName);
            holder.rlayitem=(RelativeLayout) convertView.findViewById(R.id.rlayitem);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.shouhuoaddresssuoming.setText(datas.get(position).getRegion()+datas.get(position).getStreet());
        holder.tvPhone.setText(datas.get(position).getTelephone());
        holder.tvConsigneeName.setText(datas.get(position).getConsignee());
//        holder.rlayitem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(act, AddressActivity.class);
//                i.putExtra("tag","1");
//                act.startActivityForResult(i,101);
//            }
//        });
        EventBus.getDefault().post(datas.get(position).getId());
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout rlayitem;
        TextView tvConsigneeName;// 收货人姓名
        TextView tvPhone;
        TextView shouhuoaddresssuoming;
    }

    @Override
    public int getCount() {

        return 1;
    }

    @Override
    public Object getItem(int position) {

        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

}

