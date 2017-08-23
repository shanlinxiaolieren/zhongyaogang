package com.zhongyaogang.fragment;

import java.util.HashMap;
import java.util.Map;

import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class WoQiuHuo extends Fragment implements OnClickListener{
	private String demandUserName;
	private String weight;//求购数量
	private String specifiedMerchandise;//名称
	private String details;//质量需求
	private String contacts;//联系人
	private String phone;
	private String isVoucher;//票据
	private String demandUserID;
	private String units;//单位
	private String stock;//规格
	private SharedPreferences sp;
	private String token;
	private EditText edittext_stock;
	private Spinner spinner_units;
	private EditText edittext_details;
	private EditText edittext_weight;
	private EditText editext_dianhua;
	private EditText edittext_contacts;
	private EditText edittext_specifiedMerchandise;
	private Button button_qiuhuo;
	private RadioGroup rgisvoucher;
	private RadioButton rbshi;
	private RadioButton rbfou;
	private String shi;
	private String fou;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view =inflater.inflate(R.layout.woqiuhuo, container, false);
		initView(view);
		return view;
	}

	private void initView(final View view) {
		rgisvoucher = (RadioGroup) view.findViewById(R.id.rgisvoucher);
		button_qiuhuo = (Button) view.findViewById(R.id.button_qiuhuo);
		edittext_stock = (EditText) view.findViewById(R.id.edittext_stock);
		spinner_units = (Spinner) view.findViewById(R.id.spinner_units);
		edittext_details = (EditText) view.findViewById(R.id.edittext_details);
		edittext_weight = (EditText) view.findViewById(R.id.edittext_weight);
		editext_dianhua = (EditText) view.findViewById(R.id.editext_dianhua);
		edittext_contacts = (EditText) view.findViewById(R.id.edittext_contacts);
		edittext_specifiedMerchandise = (EditText) view.findViewById(R.id.specifiedMerchandise);
		sp = getActivity().getSharedPreferences("config", 0);
		demandUserID = sp.getString("usernameid", "");
		demandUserName = sp.getString("username", "");
		token=sp.getString("token", "");
		button_qiuhuo.setOnClickListener(this);
		rgisvoucher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				rbshi = (RadioButton)view.findViewById(rgisvoucher.getCheckedRadioButtonId());
				shi = rbshi.getText().toString();
				rbfou = (RadioButton) view.findViewById(rgisvoucher.getCheckedRadioButtonId());
				fou = rbfou.getText().toString();

			}
		});
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_qiuhuo:
				woQiuHuoAdd();
				Toast.makeText(getActivity(), "发布成功",
						Toast.LENGTH_LONG).show();
				break;
		}
	}

	private void woQiuHuoAdd() {
		specifiedMerchandise=edittext_specifiedMerchandise.getText().toString().trim();
		stock=edittext_stock.getText().toString().trim();
		String spinner=(String) spinner_units.getSelectedItem();
		if(spinner.equals("吨")){
			units="1";
		}else if(spinner.equals("千克")){
			units="2";
		}else if(spinner.equals("克")){
			units="3";
		}else if(spinner.equals("毫克")){
			units="4";
		}

		if(shi.equals("需要")){
			isVoucher="1";
		}else if(fou.equals("不需要")){
			isVoucher="0";
		}
		L.e("返回结果：isVoucher=" + isVoucher);

		weight=edittext_weight.getText().toString().trim();
		details=edittext_details.getText().toString().trim();
		phone=editext_dianhua.getText().toString().trim();
		contacts=edittext_contacts.getText().toString().trim();
		if (TextUtils.isEmpty(specifiedMerchandise) || TextUtils.isEmpty(stock)||
				TextUtils.isEmpty(phone)||TextUtils.isEmpty(contacts)
				||TextUtils.isEmpty(units)||TextUtils.isEmpty(details)
				||TextUtils.isEmpty(isVoucher)
				) {
			Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
			return;
		} else {

			// 数据应该提交给服务器 由服务器比较是否正确
			new Thread() {
				public void run() {
					try {
						Looper.prepare();
						String path = Constants.WOQIUHUO_ADD;
						Map<String,String> params = new HashMap<String,String>();
						params.put("id", "0");
						params.put("demandUserID",demandUserID);
						params.put("demandUserName",demandUserName);
						params.put("stock",stock);
						params.put("details",details);
						params.put("weight", weight);
						params.put("phone", phone);
						params.put("contacts", contacts);
						params.put("specifiedMerchandise", specifiedMerchandise);
						params.put("units", units);
						params.put("isVoucher", isVoucher);
						String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
						if (strResult.equals("401")){
							Intent intent=new Intent(getActivity(),DengLuActivity.class);
							startActivity(intent);
							getActivity().finish();
						}
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(getActivity(), "求货上传失败", Toast.LENGTH_SHORT)
								.show();
					}
				};
			}.start();

		}
	}

}
