package com.zhongyaogang.fragment;

        import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
        import com.zhongyaogang.activity.BaiduMapActivity;
        import com.zhongyaogang.activity.FenBazaarActivity;
import com.zhongyaogang.bean.Bazaar;
import com.zhongyaogang.bean.BazaarBean;
import com.zhongyaogang.bean.BazaarItems;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
@SuppressLint({ "NewApi", "HandlerLeak" })
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class BazaarFragment extends Fragment implements OnClickListener{
    private TextView index_search_gongqiu;
    private BazaarFragment act;
    private BazaarAdapter adapter;
    private ListView lvAddress;
    private Bazaar bazaarbean;
    private BazaarItems a;
    private BazaarBean depts;
    private ImageView index_search_button;
    private List<Map<String, Object>> oList;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    adapter = new BazaarAdapter(act, oList);
                    lvAddress.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 处理fragment重复叠加的问题
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);//
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.bazaarfragment, container, false);
        this.act=BazaarFragment.this;
        initView(view);
        return view;
    }
//    private void showPopupMenu(View view) {
//        // View当前PopupMenu显示的相对View的位置
//        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
//        // menu布局
//        popupMenu.getMenuInflater().inflate(R.menu.main, popupMenu.getMenu());
//        // menu的item点击事件
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Toast.makeText(getActivity(), item.getTitle(), Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });
//        // PopupMenu关闭事件
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu menu) {
////		  Toast.makeText(getActivity(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
//            }
//        });
//        popupMenu.show();
//    }
    private void initView(View view) {
        lvAddress= (ListView) view.findViewById(R.id.listview_bazaar);
        index_search_gongqiu= (TextView) view.findViewById(R.id.index_search_gongqiu);
        index_search_gongqiu.setOnClickListener(this);
        index_search_button=(ImageView) view.findViewById(R.id.index_search_button);
        depts=new BazaarBean();
        a=new BazaarItems();
        bazaarbean=new Bazaar();
        bazaarQuary();
        index_search_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getContext(), BaiduMapActivity.class));
            }
        });
   }
    class BazaarAdapter extends BaseAdapter{
        private BazaarFragment act;
        private LayoutInflater layoutinflater;
        private List<Map<String, Object>> roots=new ArrayList<Map<String, Object>>();;

        public BazaarAdapter(BazaarFragment act, List<Map<String, Object>> roots) {
            super();
            this.act = act;
            this.roots = roots;
            layoutinflater=LayoutInflater.from(getActivity());
        }

        @Override
        public int getCount() {
            return roots.size();
        }

        @Override
        public Object getItem(int position) {
            return roots.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutinflater.inflate(R.layout.inflate_bazaar_item, null);
                holder.marketName = (TextView) convertView.findViewById(R.id.textview_marketName);
                holder.marketImage = (ImageView) convertView.findViewById(R.id.imageView1);
                holder.aboutMarket = (TextView) convertView.findViewById(R.id.textView1);
                holder.marketDescription = (TextView) convertView.findViewById(R.id.textView2);
                holder.marketLocation = (TextView) convertView.findViewById(R.id.textView3);
                holder.aboutShop = (TextView) convertView.findViewById(R.id.textView4);
                holder.bazaarfragment_button = (Button) convertView.findViewById(R.id.bazaarfragment_button);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map = roots.get(position);
            holder.marketName.setText(map.get("marketName")+"");
            ImageLoader.getInstance().displayImage(map.get("marketImage")+"",
                    holder.marketImage);
//			holder.marketImage.setText(map.get("marketImage")+"");
            holder.aboutMarket.setText(map.get("aboutMarket")+"");
            holder.marketDescription.setText(map.get("marketDescription")+"");
            holder.marketLocation.setText(map.get("marketLocation")+"");
            holder.aboutShop.setText(map.get("aboutShop")+"");
            holder.bazaarfragment_button.setTag(position);
            holder.bazaarfragment_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View V) {
                    startActivity(new Intent(getActivity(), FenBazaarActivity.class));
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView marketName;
            private ImageView marketImage;
            private TextView aboutMarket;
            private TextView marketDescription;
            private TextView marketLocation;
            private TextView aboutShop;
            private Button bazaarfragment_button;
        }

    }
    private void bazaarQuary(){
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.BAZAAR_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    String strResult= HttpUtils.submitPostData(path,params, "utf-8");
                        analysisData(strResult);
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    /*
     * 解析数据
     */
    public void analysisData(String data) {
        try{
            Gson gson = new Gson();
            depts =gson.fromJson(data, BazaarBean.class);
            Bazaar bazaar=depts.getResult();
            L.e("返回结果：bazaar="+bazaar);
            oList = new ArrayList<Map<String, Object>>();
            List<Bazaar> appinfos = new ArrayList<Bazaar>();
            appinfos.add(bazaar);
            for (int i = 0; i < appinfos.size(); i++) {
                bazaarbean=appinfos.get(i);
                List<BazaarItems> listA = new ArrayList<BazaarItems>();
                listA = bazaarbean.getItems();
                Map<String, Object> map = new HashMap<String, Object>();
                for (int j = 0; j < listA.size(); j++) {
                    a = listA.get(j);
                    map.put("marketName", a.getMarketName());
                    map.put("marketImage", a.getMarketImage());
                    map.put("aboutMarket", a.getAboutMarket());
                    map.put("marketDescription", a.getMarketDescription());
                    map.put("marketLocation", a.getMarketLocation());
                    map.put("aboutShop", a.getAboutShop());
                }
                oList.add(map);
                L.e("返回结果：oList="+oList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.index_search_gongqiu:
//                showPopupMenu(index_search_gongqiu);
                break;
//		case R.id.bazaarfragment_button:
//			startActivity(new Intent(getActivity(), FenBazaarActivity.class));
//			break;

        }
    }

}

