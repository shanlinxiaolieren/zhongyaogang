package com.zhongyaogang.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.activity.MyApplication;
import com.zhongyaogang.activity.PromptlyShoppingOrderActivity;
import com.zhongyaogang.activity.ShangPinXiangQingActivity;
import com.zhongyaogang.bean.HotBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.view.MyGridView;
import com.zhongyaogang.view.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class StockBaby_F extends Fragment implements PullToRefreshView.OnFooterRefreshListener,PullToRefreshView.OnHeaderRefreshListener {
	private StockBaby_F act;// 最新上线
	private List<HotBean> datas;
	private MyGridView gv;
	private GridViewAdapter adapter;
	private PullToRefreshView pv;
	private int maxResultCount=10;
	private int skipCount=0;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					adapter = new GridViewAdapter(act, datas);
					gv.setAdapter(adapter);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.yuanchanggongyinghuadong,
				container, false);
		this.act = StockBaby_F.this;
		initView(view);
		return view;
	}

	private void initView(View view) {
		gv = (MyGridView) view
				.findViewById(R.id.gv);
	}

	@Override
	public void onStart() {
		super.onStart();
		intSearchHotQuary();
	}

	/**
	 * 处理fragment重复叠加的问题
	 * @param outState
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);//
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	private void intSearchHotQuary() {
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path =  Constants.NEW_QUERY;
					Map<String,String> params = new HashMap<String,String>();
					params.put("maxResultCount",maxResultCount+"");
					params.put("skipCount",skipCount*maxResultCount+"");
					String strResult= HttpUtils.submitPostData(path,params, "utf-8");
					JSONObject jo = new JSONObject(strResult);
					JSONObject	body1 = jo.getJSONObject("result");
					JSONArray items=body1.getJSONArray("items");
					Log.e("返回结果：StockBaby_F=",items.toString());
					Gson gson = new Gson();
					if(skipCount==0) {
						datas = gson.fromJson(items.toString(), new TypeToken<List<HotBean>>() {
						}.getType());
					}
					else
					{
						List<HotBean> data=gson.fromJson(items.toString(), new TypeToken<List<HotBean>>() {
						}.getType());
						for(int i=0;i<data.size();i++)
						{
							datas.add(data.get(i));
						}
					}
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

	public class GridViewAdapter extends BaseAdapter {
		private StockBaby_F act;
		private List<HotBean> datas;
		private HotBean hotbean;
		public GridViewAdapter(StockBaby_F parentContext, List<HotBean> childs) {
			datas = childs;
			act = parentContext;
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public HotBean getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final int dex=position;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.inflate_hot_item, parent, false);
				holder = new ViewHolder(convertView);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置图片内容
//			holder.llayitem.setTag(convertView);
//			holder.iv.setTag(position);
//			holder.update();
			ImageLoader.getInstance().displayImage(
					getItem(position).getPigUrl(), holder.iv);
			holder.yaomingcheng.setText(getItem(position).getMerchandiseName());
			holder.yaoprice.setText("¥"+getItem(position).getPrice());
			holder.iv.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					hotbean=datas.get(dex);
					Intent intent=new Intent(getActivity(), ShangPinXiangQingActivity.class);
					intent.putExtra("id", hotbean.getId());
					intent.putExtra("merchandiseName", hotbean.getMerchandiseName());
					startActivity(intent);
				}
			});
			holder.gouma.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					hotbean=datas.get(dex);
					Intent intent=new Intent(getActivity(), PromptlyShoppingOrderActivity.class);
					intent.putExtra("id", hotbean.getId());
					intent.putExtra("supplyTitle", hotbean.getSupplyTitle());
					intent.putExtra("supplyNo", hotbean.getSupplyNo());
					intent.putExtra("stock", hotbean.getStock());
					intent.putExtra("merchandiseName", hotbean.getMerchandiseName());
					intent.putExtra("shopName", hotbean.getShopName());
					intent.putExtra("freightTitle", hotbean.getFreightTitle());
					intent.putExtra("price", hotbean.getPrice());
					intent.putExtra("moq", hotbean.getMoq());
					intent.putExtra("units", hotbean.getUnits());
					intent.putExtra("quantity", hotbean.getQuantity());
					intent.putExtra("pigUrl", hotbean.getPigUrl());
					startActivity(intent);
				}
			});
			return convertView;
		}

		class ViewHolder {

			ImageView iv;
			TextView yaomingcheng;
			TextView yaoprice;
			Button gouma;
			LinearLayout llayitem;
			public ViewHolder(View view) {
				llayitem=(LinearLayout) view.findViewById(R.id.llayitem);
				iv = (ImageView) view.findViewById(R.id.yaomingchengpicture);
				yaomingcheng = (TextView) view.findViewById(R.id.yaomingcheng);
				yaoprice = (TextView) view.findViewById(R.id.yaoprice);
				gouma = (Button) view.findViewById(R.id.gouma);
				view.setTag(this);
			}
			public void update() {
				// 精确计算GridView的item高度
				llayitem.getViewTreeObserver().addOnGlobalLayoutListener(
						new ViewTreeObserver.OnGlobalLayoutListener() {
							public void onGlobalLayout() {
								int position = (Integer) iv.getTag();
								// 这里是保证同一行的item高度是相同的！！也就是同一行是齐整的 height相等
								if (position > 0 && position % 2 == 1) {
									View v = (View) llayitem.getTag();
									int height = v.getHeight();

									View view = gv.getChildAt(position - 1);
									int lastheight = view.getHeight();
									// 得到同一行的最后一个item和前一个item想比较，把谁的height大，就把两者中                                                                // height小的item的高度设定为height较大的item的高度一致，也就是保证同一                                                                 // 行高度相等即可
									if (height > lastheight) {
										view.setLayoutParams(new GridView.LayoutParams(
												GridView.LayoutParams.FILL_PARENT,
												height));
									} else if (height < lastheight) {
										v.setLayoutParams(new GridView.LayoutParams(
												GridView.LayoutParams.FILL_PARENT,
												lastheight));
									}
								}
							}
						});
			}
		}
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
				 skipCount=0;
				Message s=new Message();
				s.what=4;
				MyApplication.mHandler.sendMessage(s);
			} else {
				//上拉
				 skipCount+=1;
			}
			intSearchHotQuary();
		}
	}

}
