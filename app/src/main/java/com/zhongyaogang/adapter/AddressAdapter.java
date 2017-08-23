package com.zhongyaogang.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.AddressAddActivity;
import com.zhongyaogang.bean.AddressBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private boolean isSetDefault=false;
    private List<AddressBean> datas;
    private AddressBean addressbean;
    private Activity context;
    private boolean istrue=false;
    public AddressAdapter(Activity context, List<AddressBean> datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    public void Settrue(boolean istrue)
    {
        this.istrue=istrue;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int dex=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.inflate_address_item, null);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            holder.tvConsigneeName = (TextView) convertView.findViewById(R.id.tvConsigneeName);
            holder.textview_moren = (TextView) convertView.findViewById(R.id.textview_moren);
            holder.cbEdit = (CheckBox) convertView.findViewById(R.id.cbEdit);
            holder.cbDel = (CheckBox) convertView.findViewById(R.id.cbDel);
            holder.checkbox_morenadress = (CheckBox) convertView.findViewById(R.id.checkbox_morenadress);
            holder.lladdress=(LinearLayout) convertView.findViewById(R.id.lladdress);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvAddress.setText(datas.get(position).getRegion()+datas.get(position).getStreet());
        holder.tvPhone.setText(datas.get(position).getTelephone());
        holder.tvConsigneeName.setText(datas.get(position).getConsignee());
        if(datas.get(position).getIsDefault().equals("true")){
            holder.textview_moren.setText("默认地址");
        }else{
            holder.textview_moren.setText(R.string.settingmoren);
        }
        holder.checkbox_morenadress.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isSetDefault) {
                    isSetDefault = false;
                    holder.textview_moren.setText(R.string.settingmoren);
                }else{
                    isSetDefault = true;
                    holder.textview_moren.setText("默认地址");
                }
            }
        });
        holder.cbEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                addressbean=datas.get(dex);
                Intent intent=new Intent(context, AddressAddActivity.class);
                intent.putExtra("id", addressbean.getId());
                intent.putExtra("consignee", addressbean.getConsignee());
                intent.putExtra("telephone", addressbean.getTelephone());
                intent.putExtra("street", addressbean.getStreet());
                intent.putExtra("region", addressbean.getRegion());
                intent.putExtra("tag","1");
                context.startActivity(intent);
            }
        });
        holder.cbDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("是否删除该收货地址")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EventBus.getDefault().post(datas.get(dex).getId());
                                datas.remove(dex);
                            }
                        })
                        .show();
            }
        });
        holder.lladdress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(istrue) {
                    addressbean = datas.get(dex);
                    Intent inetent = new Intent();
                    inetent.putExtra("id", addressbean.getId() + "");
                    inetent.putExtra("consignee", addressbean.getConsignee());
                    inetent.putExtra("telephone", addressbean.getTelephone());
                    inetent.putExtra("street", addressbean.getStreet());
                    inetent.putExtra("region", addressbean.getRegion());
                    context.setResult(102, inetent);
                    context.finish();//此处一定要调用finish()方法
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        LinearLayout lladdress;
        TextView tvConsigneeName;// 收货人姓名
        TextView tvPhone;
        TextView tvAddress;
        TextView textview_moren;
        CheckBox cbEdit;
        CheckBox cbDel;
        CheckBox checkbox_morenadress;
    }

    @Override
    public int getCount() {

        return datas.size();
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

