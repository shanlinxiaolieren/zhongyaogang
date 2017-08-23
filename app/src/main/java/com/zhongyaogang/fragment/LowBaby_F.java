package com.zhongyaogang.fragment;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.activity.DanGeQueRenDingDanActivity;
import com.zhongyaogang.R;
import com.zhongyaogang.activity.PromptlyShoppingOrderActivity;
import com.zhongyaogang.activity.ShangPinXiangQingActivity;
import com.zhongyaogang.bean.HotBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.utils.SystemUtil;
import com.zhongyaogang.view.NoScrollGridView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class LowBaby_F extends Fragment {
	private LowBaby_F act;// 低价资源
	private List<HotBean> datas;
	private ExpandableListView expandableListView;
	private ListViewAdapter treeViewAdapter;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					treeViewAdapter = new ListViewAdapter(act,ListViewAdapter.PaddingLeft >> 1,datas);
					treeViewAdapter.UpdateTreeNode(datas);
					expandableListView.setAdapter(treeViewAdapter);
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
		View view = inflater.inflate(R.layout.yuanchanggongyinghuadong,
				container, false);
		this.act = LowBaby_F.this;
		initView(view);
		return view;
	}

	private void initView(View view) {
		expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
		intSearchHotQuary();
	}

	private void intSearchHotQuary() {
		String url = Constants.KEENPRICE_QUERY;
		SystemUtil.getHttpUtils().send(HttpMethod.POST, url, callBack2);
	}

	RequestCallBack<String> callBack2 = new RequestCallBack<String>() {
		@Override
		public void onSuccess(ResponseInfo<String> arg0) {
			// 获得这个查询结果需要通过json解析后在此给数据源初始化和绑定adapter
			String content = arg0.result;
			JSONObject jo;
			try {
				jo = new JSONObject(content);
				JSONObject body1 = jo.getJSONObject("result");
				JSONArray items = body1.getJSONArray("items");
				L.e("返回结果：items=" + items);
				Gson gson = new Gson();
				datas = gson.fromJson(items.toString(),
						new TypeToken<List<HotBean>>() {
						}.getType());
				Message msg = new Message();
				msg.what = 1;
				mHandler.sendMessage(msg);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
	};

	public class ListViewAdapter extends BaseExpandableListAdapter implements
			OnItemClickListener {
		public static final int ItemHeight = 48;// 每项的高度
		public static final int PaddingLeft = 36;// 每项的高度
		private int myPaddingLeft = 0;

		private NoScrollGridView toolbarGrid;
		private GridViewAdapter gridviewadapter;
		private LowBaby_F act;
		private LayoutInflater layoutInflater;
		private List<HotBean> datas;

		public ListViewAdapter(LowBaby_F view, int myPaddingLeft,
							   List<HotBean> data) {
			act = view;
			this.myPaddingLeft = myPaddingLeft;
			datas = data;
		}

		public List<HotBean> GetTreeNode() {
			return datas;
		}

		public void UpdateTreeNode(List<HotBean> nodes) {
			datas = nodes;
		}

		public void RemoveAll() {
			datas.clear();
		}

		public Object getChild(int childPosition) {
			return datas.get(childPosition);
		}

		public int getChildrenCount(int groupPosition) {
			return
					// treeNodes.get(groupPosition).childs.size()
					1;
		}

		public TextView getTextView(Context context) {
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, ItemHeight);

			TextView textView = new TextView(context);
			textView.setLayoutParams(lp);
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			return textView;
		}

		/**
		 * 可自定义ExpandableListView
		 */
		public View getChildView(int groupPosition, int childPosition,
								 boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				layoutInflater = (LayoutInflater) getActivity()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = layoutInflater.inflate(R.layout.listview_item,
						null);
				toolbarGrid = (NoScrollGridView) convertView
						.findViewById(R.id.gridview);
				toolbarGrid.setNumColumns(2);// 设置每行列数
				toolbarGrid.setGravity(Gravity.CENTER);// 位置居中
				toolbarGrid.setHorizontalSpacing(10);// 水平间隔
				gridviewadapter = new GridViewAdapter(act, datas);
				toolbarGrid.setAdapter(gridviewadapter);
				toolbarGrid.setOnItemClickListener(this);
				gridviewadapter.notifyDataSetChanged();
			}
			return convertView;
		}

		/**
		 * 可自定义list
		 */
		public View getGroupView(int groupPosition, boolean isExpanded,
								 View convertView, ViewGroup parent) {
			TextView textView = getTextView(getActivity());
			textView.setText(getGroup(groupPosition).toString());
			textView.setPadding(myPaddingLeft + PaddingLeft, 0, 0, 0);
			for(int i = 0; i < treeViewAdapter.getGroupCount(); i++){
				expandableListView.expandGroup(i);
			}
			return textView;
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public Object getGroup(int groupPosition) {
			return "";
		}

		public int getGroupCount() {
			return 1;
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public boolean hasStableIds() {
			return true;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			Toast.makeText(getActivity(), "当前选中的是:" + position,
					Toast.LENGTH_SHORT).show();

		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return datas.get(childPosition);
		}
	}

	public class GridViewAdapter extends BaseAdapter {
		private LowBaby_F act;
		private List<HotBean> datas;
		private HotBean hotbean;

		public GridViewAdapter(LowBaby_F parentContext, List<HotBean> childs) {
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
			public ViewHolder(View view) {
				iv = (ImageView) view.findViewById(R.id.yaomingchengpicture);
				yaomingcheng = (TextView) view.findViewById(R.id.yaomingcheng);
				yaoprice = (TextView) view.findViewById(R.id.yaoprice);
				gouma = (Button) view.findViewById(R.id.gouma);
				view.setTag(this);
			}
		}
	}
}
