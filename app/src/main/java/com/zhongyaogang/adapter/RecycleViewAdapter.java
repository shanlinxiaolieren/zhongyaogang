package com.zhongyaogang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.activity.OrderConfirmActivity;
import com.zhongyaogang.activity.OrderGoodsActivity;
import com.zhongyaogang.bean.NewDate;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecycleViewAdapter_log";
    private List<NewDate> dataList = new ArrayList<>();
    private final int HEAD = 0x001;
    private final int ITEM = 0x002;
    private final int FOOT = 0x003;
    private Context context;
    private itemClickListeren listeren;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false));
            case ITEM:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
            case FOOT:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false));
            default:
                return null;
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (getItemViewType(position) == HEAD) {
            ((TextView) viewHolder.getView(R.id.tv_shopname)).setText(dataList.get(position).getShopName());
            ((TextView) viewHolder.getView(R.id.tv_orderstate)).setText(dataList.get(position).getOrderState());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.headItemClick(position);
                }
            });
        } else if (getItemViewType(position) == ITEM) {
            ((TextView) viewHolder.getView(R.id.tv_merchandisename)).setText(dataList.get(position).getMerchandiseName());
            ((TextView) viewHolder.getView(R.id.textview_price_lishi)).setText("￥" + dataList.get(position).getActualPrice());
            ((TextView) viewHolder.getView(R.id.tv_quantity)).setText(dataList.get(position).getQuantity());

            ImageLoader.getInstance().displayImage(dataList.get(position).getImagesUrl(), (ImageView) viewHolder.getView(R.id.imageView2_lishi));
//            Picasso.with(context).load(dataList.get(position).getImage()).into(((ImageView) viewHolder.getView(R.id.imageView2_lishi)));
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.itemClick(position);
                }
            });
        } else if (getItemViewType(position) == FOOT) {
            ((TextView) viewHolder.getView(R.id.tv_fareprice)).setText("￥" + dataList.get(position).getFarePrice());
            ((TextView) viewHolder.getView(R.id.textview_hejiprice_lishi)).setText("￥" + dataList.get(position).getPayable());
            if (dataList.get(position).getStateCode().equals("8")){
                ((Button) viewHolder.getView(R.id.button_fukuan_zhangsan)).setText("提醒发货");
            }else if (dataList.get(position).getStateCode().equals("16")) {
                ((Button) viewHolder.getView(R.id.button_fukuan_zhangsan)).setText("确认发货");
                ((Button) viewHolder.getView(R.id.button_fukuan_zhangsan)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,OrderGoodsActivity.class));
                    }
                });
            }else if (dataList.get(position).getStateCode().equals("64")) {
                ((Button) viewHolder.getView(R.id.button_fukuan_zhangsan)).setText("付款");
                ((Button) viewHolder.getView(R.id.button_fukuan_zhangsan)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,OrderConfirmActivity.class));
                    }
                });
                ((Button) viewHolder.getView(R.id.button_quxiao_order)).setVisibility(View.VISIBLE);

            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.footItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataList.get(position).getType()) {
            case 1:
                return HEAD;
            case 2:
                return ITEM;
            case 3:
                return FOOT;
            default:
                return 0;
        }
    }

    public RecycleViewAdapter(List<NewDate> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views = new SparseArray<>();

        public ViewHolder(View convertView) {
            super(convertView);
        }

        /**
         * 根据id获取view
         */
        public <T extends View> T getView(int viewId) {
            View view = views.get(viewId);
            if (null == view) {
                view = itemView.findViewById(viewId);
                views.put(viewId, view);
            }
            return (T) view;
        }
    }

    public interface itemClickListeren {
        void headItemClick(int position);

        void itemClick(int position);

        void footItemClick(int position);
    }

    public void setItemOnClick(itemClickListeren listeren) {
        this.listeren = listeren;
    }
}

