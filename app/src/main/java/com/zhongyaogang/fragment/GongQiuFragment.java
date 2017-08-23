package com.zhongyaogang.fragment;


import com.zhongyaogang.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GongQiuFragment extends Fragment implements OnClickListener{
	private GongQiuFragment act;
	private WoGongHuo wogonghuo;
	private WoQiuHuo woqiuhuo;
	private TextView button_wogonghuo;
	private TextView button_woqiuhuo;

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
		View view =inflater.inflate(R.layout.gongqiufabu, container, false);
		act=this;
		initView(view);
		return view;
	}

	private void initView(View view) {
		button_wogonghuo=(TextView)view.findViewById(R.id.button_wogonghuo);
		button_woqiuhuo=(TextView)view.findViewById(R.id.button_woqiuhuo);
		button_wogonghuo.setOnClickListener(this);
		button_woqiuhuo.setOnClickListener(this);
		wogonghuo = new WoGongHuo();
		addFragment(wogonghuo);
		showFragment(wogonghuo);
	}

	/** 添加Fragment **/
	public void addFragment(Fragment fragment) {
		FragmentTransaction ft = this.getFragmentManager().beginTransaction();
		ft.add(R.id.show_cart_wogongqiu, fragment);
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
		if (wogonghuo != null) {
			ft.hide(wogonghuo);
		}
		if (woqiuhuo != null) {
			ft.hide(woqiuhuo);
		}
		ft.show(fragment);
		ft.commitAllowingStateLoss();

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.button_wogonghuo:
				if (wogonghuo == null) {
					wogonghuo = new WoGongHuo();
					addFragment(wogonghuo);
					showFragment(wogonghuo);
				} else {
					showFragment(wogonghuo);
				}
				button_wogonghuo.setBackgroundColor(getResources().getColor(R.color.blue));
				button_woqiuhuo.setBackgroundColor(getResources().getColor(R.color.woqiugou));
				button_wogonghuo.setTextColor(Color.DKGRAY);
				button_woqiuhuo.setTextColor(Color.WHITE);

				break;
			case R.id.button_woqiuhuo:
				if (woqiuhuo == null) {
					woqiuhuo = new WoQiuHuo();
					addFragment(woqiuhuo);
					showFragment(woqiuhuo);
				} else {
					showFragment(woqiuhuo);
				}
				button_woqiuhuo.setBackgroundColor(getResources().getColor(R.color.blue));
				button_wogonghuo.setBackgroundColor(getResources().getColor(R.color.woqiugou));
				button_woqiuhuo.setTextColor(Color.DKGRAY);
				button_wogonghuo.setTextColor(Color.WHITE);
				break;
		}
	}
}
