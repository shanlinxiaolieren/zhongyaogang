package com.zhongyaogang.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.AddressActivity;
import com.zhongyaogang.activity.ChangePasswordActivity;
import com.zhongyaogang.activity.GeRenXingXiActivity;
import com.zhongyaogang.activity.OrderActivity;
import com.zhongyaogang.activity.ShengQingShoppingActivity;
import com.zhongyaogang.activity.ShoppingCartActivity;
import com.zhongyaogang.activity.WoDeFaBuActivity;
import com.zhongyaogang.activity.WoDeXiaoXiActivity;

public class CentreFragment extends Fragment implements OnClickListener{
	private RelativeLayout relativelayout_denglu;
	private RelativeLayout relativelayout_address;
	private RelativeLayout relativelayout_shopping;
	private RelativeLayout relativelayout_wodefabu;
	private RelativeLayout relativelayout_changepassword;
	private RelativeLayout relativelayout_shengqingshopping;
	private LinearLayout linearlayout_quanbu;
	private LinearLayout linearlayout_daifukuan;
	private LinearLayout linearlayout_daifahua;
	private LinearLayout linearlayout_shouhuo;
	private ImageView wodezhongxin_xingxi;
	private TextView textview_usernamephone;
	private TextView textview_username;
	private String username;
	private String usernamephone;
	private SharedPreferences sp;
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
		 View view =inflater.inflate(R.layout.wodezhongxin, container, false);
    	 initView(view);
	 	return view;
	 }

	private void initView(View view) {
		textview_username=(TextView) view.findViewById(R.id.textView_username);
		textview_usernamephone=(TextView) view.findViewById(R.id.textview_usernamephone);
		relativelayout_denglu=(RelativeLayout) view.findViewById(R.id.relativelayout_denglu);
		relativelayout_address=(RelativeLayout) view.findViewById(R.id.relativelayout_address);
		relativelayout_shopping=(RelativeLayout) view.findViewById(R.id.relativelayout_shopping);
		relativelayout_wodefabu=(RelativeLayout) view.findViewById(R.id.relativelayout_wodefabu);
		relativelayout_changepassword=(RelativeLayout) view.findViewById(R.id.relativelayout_changepassword);
		relativelayout_shengqingshopping=(RelativeLayout) view.findViewById(R.id.relativelayout_shengqingshopping);
		wodezhongxin_xingxi=(ImageView) view.findViewById(R.id.wodezhongxin_xingxi);
		linearlayout_quanbu=(LinearLayout) view.findViewById(R.id.linearlayout_quanbu);
		linearlayout_daifukuan=(LinearLayout) view.findViewById(R.id.linearlayout_daifukuan);
		linearlayout_daifahua=(LinearLayout) view.findViewById(R.id.linearlayout_daifahua);
		linearlayout_shouhuo=(LinearLayout) view.findViewById(R.id.linearlayout_shouhuo);
		relativelayout_denglu.setOnClickListener(this);
		linearlayout_quanbu.setOnClickListener(this);
		linearlayout_daifukuan.setOnClickListener(this);
		linearlayout_daifahua.setOnClickListener(this);
		linearlayout_shouhuo.setOnClickListener(this);
		relativelayout_address.setOnClickListener(this);
		relativelayout_wodefabu.setOnClickListener(this);
		relativelayout_shopping.setOnClickListener(this);
		relativelayout_changepassword.setOnClickListener(this);
		relativelayout_shengqingshopping.setOnClickListener(this);
		wodezhongxin_xingxi.setOnClickListener(this);
		 sp = getActivity().getSharedPreferences("config", 0);
//		 username=getActivity().getIntent().getStringExtra("username");
//		 usernamephone=getActivity().getIntent().getStringExtra("usernamephone");
		 username=sp.getString("username", "");
		usernamephone=sp.getString("usernamephone", "");
		textview_username.setText(username);
		textview_usernamephone.setText(usernamephone);
	}
	 @SuppressWarnings("unused")
	private void startOrderActivity(Class<?> clazz, int currentTab) {
		 Intent intent = new Intent(getActivity(), clazz);
	            intent.putExtra("data", currentTab);
	            startActivity(intent);
	    }

    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.relativelayout_shopping:
			startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
			break;
		case R.id.relativelayout_address:
			Intent i=new Intent(getActivity(),AddressActivity.class);
			i.putExtra("tag","0");
			startActivity(i);
			break;
		case R.id.wodezhongxin_xingxi:
			startActivity(new Intent(getActivity(), WoDeXiaoXiActivity.class));
			break;
		case R.id.relativelayout_wodefabu:
			startActivity(new Intent(getActivity(), WoDeFaBuActivity.class));
			break;
		case R.id.relativelayout_changepassword:
			Intent intent=new Intent(getActivity(),ChangePasswordActivity.class); 
			 intent.putExtra("username", username); 
			 intent.putExtra("usernamephone", usernamephone); 
			 startActivity(intent);
			break;
		case R.id.relativelayout_denglu:
			startActivity(new Intent(getActivity(), GeRenXingXiActivity.class));
			break;
		case R.id.relativelayout_shengqingshopping:
			startActivity(new Intent(getActivity(), ShengQingShoppingActivity.class));
			break;
		case R.id.linearlayout_quanbu:
			startOrderActivity(OrderActivity.class, 0);
			break;
		case R.id.linearlayout_daifukuan:
			startOrderActivity(OrderActivity.class, 1);
			break;
		case R.id.linearlayout_daifahua:
			startOrderActivity(OrderActivity.class, 2);
			break;
		case R.id.linearlayout_shouhuo:
			startOrderActivity(OrderActivity.class, 3);
			break;
		}
	}

	
}
