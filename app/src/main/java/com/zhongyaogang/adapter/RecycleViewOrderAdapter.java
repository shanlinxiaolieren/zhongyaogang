package com.zhongyaogang.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.NewShoppingCartOrderBean;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RecycleViewAdapter_log";
    private List<NewShoppingCartOrderBean> dataList = new ArrayList<>();
    private final int HEAD = 0x001;
    private final int ITEM = 0x002;
    private final int FOOT = 0x003;
    private Context context;
    private itemClickListeren listeren;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head_order, parent, false));
            case ITEM:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
            case FOOT:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot_order, parent, false));
            default:
                return null;
        }
    }
    public interface SaveEditListener{

        void SaveEdit(int position, String string);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (getItemViewType(position) == HEAD) {
            ((TextView) viewHolder.getView(R.id.tv_shopname)).setText(dataList.get(position).getShopName());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.headItemClick(position);
                }
            });
        } else if (getItemViewType(position) == ITEM) {
            ((TextView) viewHolder.getView(R.id.textview_zhelinali)).setText(dataList.get(position).getMerchandiseName()+
                    dataList.get(position).getMoq()+dataList.get(position).getUnits());
            ((TextView) viewHolder.getView(R.id.tv_stock)).setText(dataList.get(position).getStock());
            ((TextView) viewHolder.getView(R.id.textview_zhelinali_price)).setText("￥" + dataList.get(position).getPrice());
            ((TextView) viewHolder.getView(R.id.tv_quantity)).setText(dataList.get(position).getQuantity());

            ImageLoader.getInstance().displayImage(dataList.get(position).getPigUrl(), (ImageView) viewHolder.getView(R.id.imageview_zhelinali));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.itemClick(position);
                }
            });
        } else if (getItemViewType(position) == FOOT) {
            ((TextView) viewHolder.getView(R.id.tv_freightTitle)).setText( dataList.get(position).getFreightTitle());
            ((TextView) viewHolder.getView(R.id.textview_xiaojiprice)).setText( dataList.get(position).getTotalPrice());
            ((EditText) viewHolder.getView(R.id.editext_message)).setText("");
            //添加editText的监听事件
            ((EditText) viewHolder.getView(R.id.editext_message)).addTextChangedListener(new TextSwitcher(viewHolder));
            //通过设置tag，防止position紊乱
            ((EditText) viewHolder.getView(R.id.editext_message)).setTag(position);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listeren.footItemClick(position);
                }
            });
        }

    }

    //自定义EditText的监听类
    class TextSwitcher implements TextWatcher {

        private ViewHolder mHolder;

        public TextSwitcher(ViewHolder mHolder) {
           this.mHolder = mHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            //用户输入完毕后，处理输入数据，回调给主界面处理
            SaveEditListener listener= (SaveEditListener) context;
            if(s!=null){
                listener.SaveEdit(Integer.parseInt(mHolder.c_name_et.getTag().toString()),s.toString());
            }

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

    public RecycleViewOrderAdapter(List<NewShoppingCartOrderBean> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> views = new SparseArray<>();
        private EditText c_name_et;//填写项
        public ViewHolder(View convertView) {
            super(convertView);
            c_name_et= (EditText) convertView.findViewById(R.id.editext_message);
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

