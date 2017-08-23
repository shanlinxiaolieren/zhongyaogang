package com.zhongyaogang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.ab.view.AbSlidingPlayView;
import com.zhongyaogang.bean.ShoppingXiangQingBean;

import java.util.ArrayList;

public class ShoppingXiangQingAdapter extends BaseAdapter{

    /** 存储产品首页轮播的界面 */
    private ArrayList<View> allListView;
    /** 产品首页轮播的界面的资源 */
    private ShoppingXiangQingBean datas;
    private Context context;

    public ShoppingXiangQingAdapter(Context context, ShoppingXiangQingBean datas) {
        super();
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        String [] resultPigurl = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.inflate_shangpinxiangqing_item, null);
            holder.merchandiseName = (TextView) convertView.findViewById(R.id.merchandiseName);
            holder.textview_yundanhaoma_price = (TextView) convertView.findViewById(R.id.textview_yundanhaoma_price);
            holder.textview_kucunprice = (TextView) convertView.findViewById(R.id.textview_kucunprice);
            holder.textview_yunfeiprice = (TextView) convertView.findViewById(R.id.textview_yunfeiprice);
            holder.sellnumber = (TextView) convertView.findViewById(R.id.sellnumber);
            holder.textview_guigenumber = (TextView) convertView.findViewById(R.id.textview_guigenumber);
            holder.textview_qishoushuliang_num = (TextView) convertView.findViewById(R.id.textview_qishoushuliang_num);
            holder.standard = (TextView) convertView.findViewById(R.id.standard);
            holder.isvoucher = (TextView) convertView.findViewById(R.id.isvoucher);
            holder.originname = (TextView) convertView.findViewById(R.id.originname);
            holder.warehouse = (TextView) convertView.findViewById(R.id.warehouse);
            holder.details = (TextView) convertView.findViewById(R.id.details);
            holder.textview_zhongxiaoliang_number = (TextView) convertView.findViewById(R.id.textview_zhongxiaoliang_number);
            holder.pageviews = (TextView) convertView.findViewById(R.id.pageviews);
            holder.imageview_bigimages = (ImageView) convertView.findViewById(R.id.imageview_bigimages);
            holder.viewPager = (AbSlidingPlayView) convertView.findViewById(R.id.viewPager_menu_chanpin);
convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.merchandiseName.setText(datas.getMerchandiseName());
        holder.textview_yundanhaoma_price.setText("¥"+datas.getPrice());
        holder.textview_kucunprice.setText(datas.getRepertory());
        holder.textview_yunfeiprice.setText(datas.getFreight());
        holder.sellnumber.setText(datas.getSell());
        holder.textview_guigenumber.setText(datas.getStock());
        holder.textview_qishoushuliang_num.setText(datas.getMoq());
        holder.standard.setText(datas.getStandard());
        if (datas.getIsVoucher().equals("true")){
            holder.isvoucher.setText("是");
        }else {
            holder.isvoucher.setText("否");
        }
        holder.originname.setText(datas.getOriginName());
        holder.warehouse.setText(datas.getWarehouse());
        holder.details.setText(datas.getDetails());
        holder.textview_zhongxiaoliang_number.setText(datas.getGross());
        holder.pageviews.setText(datas.getPageviews());
        ImageLoader.getInstance().displayImage(datas.getBigImages(), holder.imageview_bigimages);
        String result=datas.getPigUrl();
        resultPigurl=result.split(",");
        // 设置播放方式为顺序播放
        holder.viewPager.setPlayType(1);
        // 设置播放间隔时间
        holder.viewPager.setSleepTime(3000);
        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<View>();

        for (int i = 0; i < resultPigurl.length; i++) {
            // 导入ViewPager的布局
            View view = LayoutInflater.from(context).inflate(R.layout.pic_item,
                    null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            ImageLoader.getInstance().displayImage(resultPigurl[i], imageView);
//				imageView.setImageResource(resultPigurl[i]);
            allListView.add(view);
        }
        holder.viewPager.addViews(allListView);
        // 开始轮播
        holder.viewPager.startPlay();
        return convertView;
    }

    private class ViewHolder {
        TextView merchandiseName;// 药名
        TextView textview_yundanhaoma_price;//价格
        TextView textview_kucunprice;//库存
        TextView textview_yunfeiprice;//运费
        TextView sellnumber;//销量
        TextView textview_guigenumber;//规格
        TextView textview_qishoushuliang_num;//起售数量
        TextView standard;//质量标准
        TextView isvoucher;//可供票据
        TextView originname;//产地
        TextView warehouse;//产品所在地
        TextView details;//其他说明
        TextView textview_zhongxiaoliang_number;//总销量
        TextView pageviews;//浏览量
        ImageView imageview_bigimages;//详情图
        // 产品首页轮播
        AbSlidingPlayView viewPager;
    }

    @Override
    public int getCount() {

        return 1;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
}
