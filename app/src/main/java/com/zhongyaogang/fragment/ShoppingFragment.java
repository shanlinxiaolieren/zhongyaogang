package com.zhongyaogang.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.ab.view.MyListView;
import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.activity.MyApplication;
import com.zhongyaogang.adapter.ShopcartAdapter;
import com.zhongyaogang.bean.ShopCart;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.view.PullToRefreshView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("deprecation")
public class ShoppingFragment extends Fragment implements PullToRefreshView.OnFooterRefreshListener,PullToRefreshView.OnHeaderRefreshListener {
	private PullToRefreshView pv;
	private MyListView gv;
	private Context context;
	private ShoppingFragment act;
	private ShopcartAdapter adapter;
    private ArrayList<ShopCart>data;
	private SharedPreferences sp;
	private String sharedusernameid;
	private String id;
	private String token;
	private String deleteList;
	private StringBuilder str1 = new StringBuilder();
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					adapter=new ShopcartAdapter(getContext(),data);
					gv.setAdapter(adapter);
					break;
				case 2:
					shoppingCartQuery();
					break;
				case 3:
					//selva = new ShopcartExpandableListViewAdapter(groups, children, context);

					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);//
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_shopping_cart, container, false);
		this.act = ShoppingFragment.this;
		EventBus.getDefault().register(this);
		initView(view);
		return view;
	}


	private void initView(View view) {
		MyApplication.mHandler=mHandler;
		context = getActivity();
		pv = (PullToRefreshView) view.findViewById(R.id.pv);
		gv = (MyListView) view.findViewById(R.id.gv);
		pv.setOnFooterRefreshListener(this);
		pv.setOnHeaderRefreshListener(this);
		sp = getActivity().getSharedPreferences("config", 0);
		sharedusernameid = sp.getString("usernameid", "");
		token = sp.getString("token", "");
		Log.e("usernameid",sharedusernameid);

	}

	@Override
	public void onStart() {
		super.onStart();
		shoppingCartQuery();
	}

	private void shoppingCartQuery() {
		// 数据应该提交给服务器 由服务器比较是否正确
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.SHOPPING_QUERY;
					Map<String, String> params = new HashMap<String, String>();
					params.put("id", sharedusernameid);
					String strResult = HttpUtils.submitPostDataToken(path, params, "utf-8", token);
					L.e("返回结果：shoppingCartQueryresult=" + strResult);
					if (strResult.equals("401")) {
						Intent intent = new Intent(getActivity(), DengLuActivity.class);
						startActivity(intent);
					}

					JSONObject jo = new JSONObject(strResult);
					JSONObject body1 = jo.getJSONObject("result");
					JSONArray items = body1.getJSONArray("items");
					data=new ArrayList<ShopCart>();
					for(int i=0;i<items.length();i++)
					{
						jo=items.getJSONObject(i);
						ShopCart s=new ShopCart();
						s.shopId=jo.getString("shopId");
						s.shopName=jo.getString("shopName");
						s.yxtCartsListDto=jo.getString("yxtCartsListDto");
						data.add(s);
					}
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "查询失败",
							Toast.LENGTH_SHORT).show();
				}
			}

			;
		}.start();
	}

	@Subscribe
	public void onEventMainThread(String Id) {
//		shoppingDelete(Id);
//		if(str1==null){
//			return;
//		}else{
//			deleteList=str1.toString();
//			L.e("deleteList="+deleteList);
//		}
//		if(totalCount==0){
//			return;
//		}else{
//			totalCountshoppingBatch=totalCount;
//			L.e("totalCountBatch="+totalCountshoppingBatch);
//		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void shoppingDelete(final String id) {
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.SHOPPING_DELETE;
					Map<String, String> params = new HashMap<String, String>();
					params.put("id", id);
					String strResult = HttpUtils.submitPostDataToken(path, params, "utf-8", token);
					L.e("返回结果：deleteresult" + strResult);
					if (strResult.equals("401")) {
						Intent intent = new Intent(getActivity(), DengLuActivity.class);
						startActivity(intent);
					}
					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
				}

			}

			;
		}.start();
	}

	private void shoppingBatchDelete() {
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.BATCH_DELETE;
					Map<String, String> params = new HashMap<String, String>();
					params.put("deleteList", deleteList);
					String strResult = HttpUtils.submitPostDataToken(path, params, "utf-8", token);
					L.e("返回结果：shoppingCartQueryresult" + strResult);
					if (strResult.equals("401")) {
						Intent intent = new Intent(getActivity(), DengLuActivity.class);
						startActivity(intent);
					}
					L.e("返回结果：Batchdeleteresult" + strResult);
					Message msg = new Message();
					msg.what = 3;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
				}

			}

			;
		}.start();
	}

	// ------------------------------上滑发送的东西------------------------------------//
	@Override
	// 底部刷新
	public void onFooterRefresh(PullToRefreshView view) {
		pv.postDelayed(new Runnable() {
			@Override
			public void run() {
				MyHandler mh = new MyHandler();
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putBoolean("key", false);
				msg.setData(b);
				mh.sendMessage(msg);
			}
		}, 1);
	}
	@Override
	// 头部刷新
	public void onHeaderRefresh(PullToRefreshView view) {
		pv.postDelayed(new Runnable() {
			@Override
			public void run() {
				// 用线程更新数据与监听刷新的动态
				MyHandler mh = new MyHandler();
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putBoolean("key", true);
				msg.setData(b);
				mh.sendMessage(msg);
			}
		}, 1);
	}

	// 继承Handler类时，必须重写handleMessage方法
	// 利用Handler更新数据
	public class MyHandler extends Handler {

		public MyHandler() {
		}


		public MyHandler(Looper l) {
			super(l);
		}

		// 执行接收到的通知，此时执行的顺序是按照队列进行，即先进先出
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle b = msg.getData();
			pv.onHeaderRefreshComplete();
			pv.onFooterRefreshComplete();
			// 得到线程里面数据
			if (b.getBoolean("key")) {
				//下拉
				//skipCount=0;
			} else {
				//上拉
				//skipCount+=1;
			}
			//intSearchHotQuary();
		}
	}

}
