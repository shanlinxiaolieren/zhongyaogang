package com.zhongyaogang.adapter;

import java.util.List;

import org.greenrobot.eventbus.EventBus;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.activity.ShengQingShoppingAddActivity;
import com.zhongyaogang.bean.ShengQingShoppingBean;
import com.zhongyaogang.utils.L;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
            holder.imageview_picture1 = (ImageView) convertView.findViewById(R.id.imageview_picture1);
            holder.imageview_picture2 = (ImageView) convertView.findViewById(R.id.imageview_picture2);
            holder.imageview_picture3 = (ImageView) convertView.findViewById(R.id.imageview_picture3);
            holder.imageview_picture4 = (ImageView) convertView.findViewById(R.id.imageview_picture4);
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
        ImageLoader.getInstance().displayImage(resultidCardPng[0], holder.imageview_picture1);
        ImageLoader.getInstance().displayImage(resultidCardPng[1], holder.imageview_picture2);
        ImageLoader.getInstance().displayImage(resultCertificate[0], holder.imageview_picture3);
        ImageLoader.getInstance().displayImage(resultCertificate[1], holder.imageview_picture4);
        holder.imageview_shengqingshoppingquanbu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isActive) {
                    isActive = false;
                    holder.imageview_picture1.setVisibility(View.VISIBLE);
                    holder.imageview_picture2.setVisibility(View.VISIBLE);
                    holder.imageview_picture3.setVisibility(View.VISIBLE);
                    holder.imageview_picture4.setVisibility(View.VISIBLE);
                }else{
                    isActive = true;
                    holder.imageview_picture1.setVisibility(View.GONE);
                    holder.imageview_picture2.setVisibility(View.GONE);
                    holder.imageview_picture3.setVisibility(View.GONE);
                    holder.imageview_picture4.setVisibility(View.GONE);
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
                EventBus.getDefault().post(data.get(dex).getId());
                data.remove(dex);

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
        private ImageView imageview_picture1;
        private ImageView imageview_picture2;
        private ImageView imageview_picture3;
        private ImageView imageview_picture4;
    }
}
