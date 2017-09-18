package com.zhongyaogang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.ShengQingShoppingAddActivity;
import com.zhongyaogang.bean.ShengQingShoppingBean;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.view.MyGridView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ShengQingShoppingAdapter extends BaseAdapter{
    private List<ShengQingShoppingBean> data;
    private ShengQingShoppingBean  shengqingbean;
    private Context mcontext;
    private boolean isActive=false;
    public ShengQingShoppingAdapter(Context mcontext, List<ShengQingShoppingBean> data) {
        super();
        this.mcontext = mcontext;
        this.data = data;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int dex=position;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.inflate_shengqingshopping_item, null);
            holder.textView_shopName = (TextView) convertView.findViewById(R.id.textView_shopName);
            holder.textView_proposer = (TextView) convertView.findViewById(R.id.textView_proposer);
            holder.textView_email = (TextView) convertView.findViewById(R.id.textView_email);
            holder.textView_phone = (TextView) convertView.findViewById(R.id.textView_phone);
            holder.textview_shengfenzhenghaoma = (TextView) convertView.findViewById(R.id.textview_shengfenzhenghaoma);
            holder.textView_xiugai = (TextView) convertView.findViewById(R.id.textView_xiugai);
            holder.textView_shanchu = (TextView) convertView.findViewById(R.id.textView_shanchu);
            holder.imageview_shengqingshoppingquanbu = (ImageView) convertView.findViewById(R.id.imageview_shengqingshoppingquanbu);
            holder.gv = (MyGridView) convertView.findViewById(R.id.gv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView_shopName.setText(data.get(position).getShopName());
        holder.textView_proposer.setText(data.get(position).getProposer());
        holder.textView_email.setText(data.get(position).getProposerEimal());
        holder.textView_phone.setText(data.get(position).getPhone());
        holder.textview_shengfenzhenghaoma.setText(data.get(position).getIdCard());
        String result=data.get(position).getCertificate();
        final String [] resultCertificate=result.split(",");
        L.e("返回结果：resultCertificate[0]="+resultCertificate[0]);
        final String [] resultidCardPng=data.get(position).getIdCardPng().split(",");
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<resultidCardPng.length;i++)
        {
            list.add(resultidCardPng[i]);
        }
        for(int i=0;i<resultCertificate.length;i++)
        {
            list.add(resultCertificate[i]);
        }
        GridviewAdapter adapter=new GridviewAdapter(mcontext,list);
        holder.gv.setAdapter(adapter);
        holder.imageview_shengqingshoppingquanbu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActive) {
                    isActive = false;
                    holder.gv.setVisibility(View.VISIBLE);
        ;
                }else{
                    isActive = true;
                }
            }
        });
        holder.textView_xiugai.setTag(position);
        holder.textView_xiugai.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                shengqingbean=data.get(dex);
                Intent intent=new Intent(mcontext, ShengQingShoppingAddActivity.class);
                intent.putExtra("id", shengqingbean.getId());
                intent.putExtra("idCard", shengqingbean.getIdCard());
                intent.putExtra("shopName", shengqingbean.getShopName());
                intent.putExtra("proposer", shengqingbean.getProposer());
                intent.putExtra("proposerEimal", shengqingbean.getProposerEimal());
                intent.putExtra("phone", shengqingbean.getPhone());
                intent.putExtra("contacts", shengqingbean.getContacts());
                intent.putExtra("contactsPhone", shengqingbean.getContactsPhone());
                intent.putExtra("idCardStart", shengqingbean.getIdCardStart());
                intent.putExtra("idCardEnd", shengqingbean.getIdCardEnd());
                intent.putExtra("certificateStart", shengqingbean.getCertificateStart());
                intent.putExtra("certificateEnd", shengqingbean.getCertificateEnd());
                intent.putExtra("idCardPng1", resultidCardPng[0]);
                intent.putExtra("idCardPng2", resultidCardPng[1]);
                intent.putExtra("certificate1", resultCertificate[0]);
                intent.putExtra("certificate2", resultCertificate[1]);
                mcontext.startActivity(intent);
            }
        });
        holder.textView_shanchu.setTag(position);
        holder.textView_shanchu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View V) {
                shengqingbean=data.get(dex);
                if(Integer.parseInt(shengqingbean.getState())>=2) {
                    EventBus.getDefault().post(data.get(dex).getId());
                    data.remove(dex);
                }
                else
                {

                }

            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView textView_shopName;
        private TextView textView_proposer;
        private TextView textView_email;
        private TextView textView_phone;
        private TextView textview_shengfenzhenghaoma;
        private TextView textView_xiugai;
        private TextView textView_shanchu;
        private ImageView imageview_shengqingshoppingquanbu;
        private MyGridView gv;
    }
}
