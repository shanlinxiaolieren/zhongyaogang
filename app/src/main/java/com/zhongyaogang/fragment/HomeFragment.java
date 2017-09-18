package com.zhongyaogang.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.ab.view.AbSlidingPlayView;
import com.zhongyaogang.ab.view.MyGallery;
import com.zhongyaogang.activity.MyApplication;
import com.zhongyaogang.activity.SearchActivity;
import com.zhongyaogang.activity.ShangPinXiangQingActivity;
import com.zhongyaogang.bean.Banner;
import com.zhongyaogang.bean.DataYaoCai;
import com.zhongyaogang.bean.GongBean;
import com.zhongyaogang.bean.QiuGouXiangQingBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.image.ImageLoaderConfig;
import com.zhongyaogang.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "NewApi", "HandlerLeak" })
public class HomeFragment extends Fragment implements OnClickListener  {
	private TextView index_search_yaocaiwang;
	//private TextView gongqiu;
	private PopupWindow popWindow;
	private MyGallery mStormGallery = null;
	//	private GalleryFlow mStormGallery;
	private IndexGalleryAdapter mStormAdapter = null;//有问题
	private HomeFragment act;
	private List<GongBean> mStormListData ;
	private ImageView index_search_button;
	private TextView bt_cart_all, bt_cart_low, bt_cart_stock;
	private AllBaby_F allBaby_F;
	private LowBaby_F lowBaby_F;
	private StockBaby_F stockBaby_F;
	// 首页轮播
	private AbSlidingPlayView viewPager;
	private ArrayList<Banner>bannerlist;
	/** 存储首页轮播的界面 */
	private ArrayList<View> allListView;
	/** 首页轮播的界面的资源 */

	private ListView mListView;// 列表
	private ListView urgentxiangqing;// 求购详情页
	private QiuGouXiangQingAdapter qiugouxiangqing;
	private QiuGouXiangQingBean xiangqingbean;
	private ListAdapter mListAdapter;// adapter
	private List<DataYaoCai> datas;// 数据
	private SharedPreferences sp;
	private  PopupWindow mpopupWindow ;
	private String token;
	private Spinner spinner;
	private List<String> list;
	private String[] items = {"供求","供货"};
	private ArrayAdapter<String> adapter;
	//	private String id;
	private EditText search_et_input;
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					mListAdapter=new ListAdapter(datas, act);
					mListView.setAdapter(mListAdapter);
					mListAdapter.notifyDataSetChanged();
					break;
				case 2:
					initView();
					break;
				case 3:
					qiugouxiangqing=new QiuGouXiangQingAdapter(xiangqingbean, act);
					urgentxiangqing.setAdapter(qiugouxiangqing);
					qiugouxiangqing.notifyDataSetChanged();
					break;
				case 4://上拉
					qiuGouQuery();
					urgentQuery();
					CarouselQuery();
					break;
				case 5:
					initViewPager();
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
		View view = inflater.inflate(R.layout.homefragment, container, false);
		if (!ImageLoader.getInstance().isInited()) {
			ImageLoaderConfig.initImageLoader(getActivity(),
					Constants.BASE_IMAGE_CACHE);
		}
		this.act = HomeFragment.this;
		initView(view);
		return view;
	}

	public void qiuGouXiangQingQuery(final String id) {
		// 数据应该提交给服务器 由服务器比较是否正确
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.QIUGOU_XIANGQING_QUERY;
					Map<String,String> params = new HashMap<String,String>();
					params.put("id", id);
					String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
					L.e("返回结果：qiuGouXiangQingresult" + strResult);
						JSONObject jo = new JSONObject(strResult);
						JSONObject	body1 = jo.getJSONObject("result");
						L.e("返回结果：body1="+body1);
						Gson gson = new Gson();
						xiangqingbean=gson.fromJson(body1.toString(),new  TypeToken<QiuGouXiangQingBean>(){}.getType());
						Message msg = new Message();
						msg.what = 3;
						mHandler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "查询失败",
							Toast.LENGTH_SHORT).show();
				}
			};
		}.start();
	}
	public void qiuGouQuery(){
		// 数据应该提交给服务器 由服务器比较是否正确
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.QIUGOU_QUERY;
					Map<String,String> params = new HashMap<String,String>();
					String strResult= HttpUtils.submitPostData(path,params, "utf-8");
					L.e("返回结果：qiuGouresult"+strResult);
						JSONObject jo = new JSONObject(strResult);
						JSONObject	body1 = jo.getJSONObject("result");
						JSONArray items=body1.getJSONArray("items");
						L.e("返回结果：qiuGouitems="+items);
						Gson gson = new Gson();
						datas=gson.fromJson(items.toString(),new  TypeToken<List<DataYaoCai>>(){}.getType());
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

	@Override
	public void onStart() {
		super.onStart();
		qiuGouQuery();
		urgentQuery();
		CarouselQuery();
	}

	//急购特供查询
	private void urgentQuery(){
		// 数据应该提交给服务器 由服务器比较是否正确
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.URGENT_QUERY;
					Map<String,String> params = new HashMap<String,String>();
					params.put("maxResultCount","10");
					params.put("skipCount","0");
					String strResult= HttpUtils.submitPostData(path,params, "utf-8");
					JSONObject jo = new JSONObject(strResult);
						JSONObject	body1 = jo.getJSONObject("result");
						JSONArray items=body1.getJSONArray("items");
						L.e("返回结果：mStormListDataitems="+items);
						Gson gson = new Gson();
						mStormListData=gson.fromJson(items.toString(),new  TypeToken<List<GongBean>>(){}.getType());
						Message msg = new Message();
						msg.what = 2;
						mHandler.sendMessage(msg);
						L.e("返回结果：mStormListData="+mStormListData);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
				}

			};
		}.start();
	}

	//轮播图
	private void CarouselQuery(){
		// 数据应该提交给服务器 由服务器比较是否正确
		new Thread() {
			public void run() {
				try {
					Looper.prepare();
					String path = Constants.Carousel;
					Map<String,String> params = new HashMap<String,String>();
					String strResult= HttpUtils.submitPostData(path,params, "utf-8");
					JSONObject jo = new JSONObject(strResult);
					JSONObject	body1 = jo.getJSONObject("result");
					JSONArray items=body1.getJSONArray("items");
					L.e("返回结果：bannerlist="+items);
					Gson gson = new Gson();
					bannerlist=gson.fromJson(items.toString(),new  TypeToken<List<Banner>>(){}.getType());
					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
					L.e("返回结果：mStormListData="+mStormListData);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
				}

			};
		}.start();
	}

	private void initView(View view) {
		MyApplication.mHandler=mHandler;
		index_search_button = (ImageView) view
				.findViewById(R.id.index_search_button);
		spinner=(Spinner) view.findViewById(R.id.sp);
		index_search_yaocaiwang = (TextView) view
				.findViewById(R.id.index_search_yaocaiwang);
//		gongqiu = (TextView) view.findViewById(R.id.index_search_gongqiu);
		mStormGallery = (MyGallery) view.findViewById(R.id.index_jingqiu_gallery);
		bt_cart_all = (TextView) view.findViewById(R.id.id_chat_tv);
		bt_cart_low = (TextView) view.findViewById(R.id.id_friend_tv);
		bt_cart_stock = (TextView) view.findViewById(R.id.id_contacts_tv);
		mListView = (ListView) view.findViewById(R.id.listview_qiugouxingxi);
		search_et_input=(EditText) view.findViewById(R.id.search_et_input);
		mListView.setSelector(R.drawable.list_selector);
		bt_cart_all.setOnClickListener(this);
		bt_cart_low.setOnClickListener(this);
		bt_cart_stock.setOnClickListener(this);
		//gongqiu.setOnClickListener(this);
		index_search_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//showBottomPopupWindow(v);
				if(search_et_input.getText().toString().length()!=0) {
					Intent i = new Intent(getContext(), SearchActivity.class);
					i.putExtra("tag","gong");
					i.putExtra("search",search_et_input.getText().toString().trim());
					startActivity(i);
				}
				else
				{
					Toast.makeText(getContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
				}
			}
		});

		allBaby_F = new AllBaby_F();

		addFragment(allBaby_F);
		showFragment(allBaby_F);
		viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
		// 设置播放方式为顺序播放
		viewPager.setPlayType(1);
		// 设置播放间隔时间
		viewPager.setSleepTime(5000);
//		initViewPager();
		sp = getActivity().getSharedPreferences("config", 0);
		token = sp.getString("token", "");

		list = new ArrayList<String>();
		for(int i = 0; i < items.length; i++)
		{
			list.add(items[i]);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}


	private void initView() {
		mStormAdapter = new IndexGalleryAdapter(act,mStormListData);
		mStormGallery.setAdapter(mStormAdapter);
		//mStormAdapter.notifyDataSetChanged();
		mStormGallery.setSelection(1);
		// 相应的点击事件
//		mStormGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> adapterView,
//							View view, int i, long l) {
//						 Toast.makeText(getActivity(), "您点击的是" +i,
//						 Toast.LENGTH_LONG).show();
//					}
//				});
	}

	private void initViewPager() {

		if (allListView != null) {
			allListView.clear();
			allListView = null;
		}
		allListView = new ArrayList<View>();

		for (int i = 0; i < bannerlist.size(); i++) {
			// 导入ViewPager的布局
			View view = LayoutInflater.from(getActivity()).inflate(
					R.layout.pic_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
			//imageView.setImageResource(bannerlist.get(i).get);
			ImageLoader.getInstance().displayImage(bannerlist.get(i).getPinUrl(), imageView);
			allListView.add(view);
		}

		viewPager.addViews(allListView);
		// 开始轮播
		viewPager.startPlay();
		// viewPager.setOnItemClickListener(new AbOnItemClickListener() {
		// @Override
		// public void onClick(int position) {
		// //跳转到详情界面
		// Intent intent = new Intent(getActivity(), BabyActivity.class);
		// startActivity(intent);
		// }
		// });
	}


	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		ft.add(R.id.show_cart_view, fragment);
		ft.commitAllowingStateLoss();
	}

	/** 删除Fragment **/
	public void removeFragment(Fragment fragment) {
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();
	}

	/** 显示Fragment **/
	public void showFragment(Fragment fragment) {
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		if (allBaby_F != null) {
			ft.hide(allBaby_F);
		}
		if (lowBaby_F != null) {
			ft.hide(lowBaby_F);
		}
		if (stockBaby_F != null) {
			ft.hide(stockBaby_F);
		}

		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}


	class IndexGalleryAdapter extends BaseAdapter {

		HomeFragment context;
		List<GongBean> listData;
		GongBean gongbean;

		public IndexGalleryAdapter(HomeFragment context,
								   List<GongBean> listData) {
			this.context = context;
			this.listData = listData;
		}

		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public Object getItem(int position) {
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				convertView = inflater.inflate(R.layout.gallery_item_image, parent, false);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.index_gallery_item_image);
				viewHolder.yaoming = (TextView) convertView.findViewById(R.id.index_gallery_item_text);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.index_gallery_item_text2);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
				//resetViewHolder(viewHolder);
			}
			gongbean=listData.get(position);
			ImageLoader.getInstance().displayImage(
					gongbean.getPigUrl(), viewHolder.imageView);
			viewHolder.yaoming.setText(gongbean.getMerchandiseName());
			viewHolder.textView.setText("¥"+gongbean.getPrice());
			viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			viewHolder.yaoming.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			viewHolder.imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(), ShangPinXiangQingActivity.class);
					intent.putExtra("id", gongbean.getId());
					intent.putExtra("merchandiseName", gongbean.getMerchandiseName());
					startActivity(intent);
				}
			});
			return convertView;
		}

		class ViewHolder {
			ImageView imageView;
			TextView textView;
			TextView yaoming;
		}

		protected void resetViewHolder(ViewHolder viewHolder) {
			viewHolder.imageView.setImageBitmap(null);
			viewHolder.textView.setText("");
			viewHolder.yaoming.setText("");
		}
	}

	/**
	 * 底部弹窗
	 * @param v
	 */
	private void  showBottomPopupWindow(View v)
	{
		View customView = getActivity().getLayoutInflater().inflate(R.layout.bottom_pop, null, false);
		mpopupWindow = new PopupWindow(customView,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,true);
		mpopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 这两句是键盘弹出来也不会挡住pop
		mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		mpopupWindow.setFocusable(true); // 点击空白的时候关闭pop
		mpopupWindow.setTouchable(true);
		mpopupWindow.setOutsideTouchable(true);
		customView.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					if (mpopupWindow != null && mpopupWindow.isShowing())
					{
						mpopupWindow.dismiss();
						return true;
					}
				}
				return false;
			}
		});
		TextView tvtitle=(TextView) customView.findViewById(R.id.tvtitle);
		TextView tvSpecifications=(TextView) customView.findViewById(R.id.tvSpecifications);
		TextView tvNumber=(TextView) customView.findViewById(R.id.tvNumber);
		TextView tvstandard=(TextView) customView.findViewById(R.id.tvstandard);
		TextView tvbill=(TextView) customView.findViewById(R.id.tvbill);
		TextView tvname=(TextView) customView.findViewById(R.id.tvname);
		TextView tvphone=(TextView) customView.findViewById(R.id.tvphone);
		RelativeLayout rlayclose=(RelativeLayout) customView.findViewById(R.id.rlayclose);
		RelativeLayout rlayBuyers=(RelativeLayout)customView.findViewById(R.id.rlayBuyers);
		rlayclose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "关闭详情", Toast.LENGTH_SHORT).show();
				if(mpopupWindow.isShowing())
				{
					mpopupWindow.dismiss();
				}
			}
		});
		rlayBuyers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "联系买家", Toast.LENGTH_SHORT).show();
				if(mpopupWindow.isShowing())
				{
					mpopupWindow.dismiss();
				}
			}
		});
		mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mpopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//			case R.id.index_search_gongqiu:
////			showPopupMenu(gongqiu);
//				break;
			case R.id.id_chat_tv:
				if (allBaby_F == null) {
					allBaby_F = new AllBaby_F();
					addFragment(allBaby_F);
					showFragment(allBaby_F);
				} else {
					showFragment(allBaby_F);
				}
				bt_cart_all.setBackgroundColor(getResources().getColor(
						R.color.bg_Black));
				bt_cart_low.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_stock.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_all.setTextColor(0xffffffff);
				bt_cart_stock.setTextColor(Color.DKGRAY);
				bt_cart_low.setTextColor(Color.DKGRAY);

				break;
			case R.id.id_friend_tv:
				if (lowBaby_F == null) {
					lowBaby_F = new LowBaby_F();
					addFragment(lowBaby_F);
					showFragment(lowBaby_F);
				} else {
					showFragment(lowBaby_F);
				}
				bt_cart_low.setBackgroundColor(getResources().getColor(
						R.color.bg_Black));
				bt_cart_all.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_stock.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_low.setTextColor(0xffffffff);
				bt_cart_all.setTextColor(Color.DKGRAY);
				bt_cart_stock.setTextColor(Color.DKGRAY);
				break;
			case R.id.id_contacts_tv:
				if (stockBaby_F == null) {
					stockBaby_F = new StockBaby_F();
					addFragment(stockBaby_F);
					showFragment(stockBaby_F);
				} else {
					showFragment(stockBaby_F);
				}
				bt_cart_stock.setBackgroundColor(getResources().getColor(
						R.color.bg_Black));
				bt_cart_all.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_low.setBackgroundColor(getResources().getColor(
						R.color.qianghuangshe));
				bt_cart_stock.setTextColor(0xffffffff);
				bt_cart_low.setTextColor(Color.DKGRAY);
				bt_cart_all.setTextColor(Color.DKGRAY);
				break;

			default:
				break;
		}
	}

	private void showPopwindow(String id) {
		View parent = (mStormGallery).getChildAt(0);
		View popView = View.inflate(getActivity(),
				R.layout.customxiangqing, null);
		urgentxiangqing = (ListView) popView
				.findViewById(R.id.urgentxiangqing);
//		button_guangbixiangqing = (Button) popView
//				.findViewById(R.id.button_guangbixiangqing);
//		linearlayout_lianximaojia = (LinearLayout) popView
//				.findViewById(R.id.linearlayout_lianximaojia);
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		popWindow = new PopupWindow(popView,
				width, height);
		popWindow.setAnimationStyle(R.style.AnimBottom);
		popWindow.setFocusable(true);
		popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
//		OnClickListener listener = new OnClickListener() {
//			public void onClick(View v) {
//				switch (v.getId()) {
//				case R.id.button_guangbixiangqing:
//					break;
//				case R.id.linearlayout_lianximaojia:
//					break;
//				}
//				popWindow.dismiss();
//			}
//		};

//		button_guangbixiangqing.setOnClickListener(listener);
//		linearlayout_lianximaojia.setOnClickListener(listener);

		qiuGouXiangQingQuery(id);
		ColorDrawable dw = new ColorDrawable(0x30000000);
		popWindow.setBackgroundDrawable(dw);
		// 设置好参数之后再show
		popWindow.showAsDropDown(parent);
	}
	class ListAdapter extends BaseAdapter {
		private List<DataYaoCai> mListData;
		private HomeFragment act;

		public ListAdapter(List<DataYaoCai> mListData, HomeFragment act) {
			super();
			this.mListData = mListData;
			this.act = act;
		}

		@Override
		public int getCount() {
			return mListData.size();
		}

		@Override
		public Object getItem(int position) {
			return mListData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			final int dex=position;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.qiugouxingxifenzi, null);
				holder = new ViewHolder();
				holder.jixutegong_yaopeng = (TextView) convertView.findViewById(R.id.jixutegong_yaopeng);
				holder.jixutegong_huo = (TextView) convertView
						.findViewById(R.id.jixutegong_huo);
				holder.jixutegong_number = (TextView) convertView
						.findViewById(R.id.jixutegong_number);
				holder.jixutegong_button = (TextView) convertView
						.findViewById(R.id.jixutegong_button);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.jixutegong_yaopeng.setText(mListData.get(position)
					.getSpecifiedMerchandise());
			holder.jixutegong_huo.setText(mListData.get(position)
					.getDetails());
			holder.jixutegong_number.setText(mListData.get(position)
					.getWeight());
			holder.jixutegong_button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String id=String.valueOf(mListData.get(dex).getId());
					showPopwindow(id);
				}
			});

			return convertView;
		}

		class ViewHolder {
			TextView jixutegong_yaopeng;
			TextView jixutegong_huo;
			TextView jixutegong_number;
			TextView jixutegong_button;

		}

	}
	class QiuGouXiangQingAdapter extends BaseAdapter {
		private QiuGouXiangQingBean mData;
		private HomeFragment act;

		public QiuGouXiangQingAdapter(QiuGouXiangQingBean mListData, HomeFragment act) {
			super();
			this.mData = mListData;
			this.act = act;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.qiugou_item_listview, null);
				holder = new ViewHolder();
				holder.specifiedMerchandise = (TextView) convertView.findViewById(R.id.textview_yuzhu);
				holder.stock = (TextView) convertView.findViewById(R.id.stock);
				holder.weight = (TextView) convertView.findViewById(R.id.weight);
				holder.details = (TextView) convertView.findViewById(R.id.details);
				holder.isVoucher = (TextView) convertView.findViewById(R.id.isVoucher);
				holder.units = (TextView) convertView.findViewById(R.id.units);
				holder.contacts = (TextView) convertView.findViewById(R.id.contacts);
				holder.phone = (TextView) convertView.findViewById(R.id.phone);
				holder.button_guangbixiangqing=(Button) convertView.findViewById(R.id.button_guangbixiangqing);
				holder.linearlayout_lianximaojia=(LinearLayout) convertView.findViewById(R.id.linearlayout_lianximaojia);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.specifiedMerchandise.setText(mData.getSpecifiedMerchandise());
			holder.stock.setText(mData.getStock());
			holder.weight.setText(mData.getWeight());
			holder.details.setText(mData.getDetails());
			holder.isVoucher.setText(mData.getIsVoucher());
			holder.units.setText(mData.getUnits());
			holder.contacts.setText(mData.getContacts());
			holder.phone.setText(mData.getPhone());
			holder.button_guangbixiangqing.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
if(popWindow.isShowing()&&popWindow!=null)
{
	popWindow.dismiss();
}
				}
			});
			holder.linearlayout_lianximaojia.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getContext(), "联系买家", Toast.LENGTH_SHORT).show();
					if(popWindow.isShowing()&&popWindow!=null)
					{
						popWindow.dismiss();
					}
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView specifiedMerchandise;
			TextView stock;
			TextView weight;
			TextView details;
			TextView isVoucher;
			TextView contacts;
			TextView phone;
			TextView units;
			Button button_guangbixiangqing;
			LinearLayout linearlayout_lianximaojia;
		}
	}

}
