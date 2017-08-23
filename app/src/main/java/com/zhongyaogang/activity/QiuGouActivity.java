package com.zhongyaogang.activity;

//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;

import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
        import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("deprecation")
public class QiuGouActivity extends Activity implements OnClickListener{
    private QiuGouActivity act;
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
    private String yunfeiId ;
    private String  spinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qiu_gou);
        act=this;
        initView();

    }
    private void initView() {

        rbshi = (RadioButton)findViewById(R.id.rbshi);
        rbfou = (RadioButton)findViewById(R.id.rbfou);
        button_qiuhuo = (Button) findViewById(R.id.button_qiuhuo);
        rgisvoucher = (RadioGroup) findViewById(R.id.rgisvoucher);
        button_qiuhuo = (Button) findViewById(R.id.button_qiuhuo);
        edittext_stock = (EditText) findViewById(R.id.edittext_stock);
        spinner_units = (Spinner) findViewById(R.id.spinner_units);
        edittext_details = (EditText) findViewById(R.id.edittext_details);
        edittext_weight = (EditText) findViewById(R.id.edittext_weight);
        editext_dianhua = (EditText) findViewById(R.id.editext_dianhua);
        edittext_contacts = (EditText)findViewById(R.id.edittext_contacts);
        edittext_specifiedMerchandise = (EditText) findViewById(R.id.specifiedMerchandise);
        button_qiuhuo.setOnClickListener(this);
        // 修改传过来的
        yunfeiId = getIntent().getStringExtra("id");
        demandUserID = getIntent().getStringExtra("demandUserID");
        demandUserName = getIntent().getStringExtra("demandUserName");
        stock = getIntent().getStringExtra("stock");
        details = getIntent().getStringExtra("details");
        weight = getIntent().getStringExtra("weight");
        phone = getIntent().getStringExtra("phone");
        contacts = getIntent().getStringExtra("contacts");
        specifiedMerchandise = getIntent().getStringExtra("specifiedMerchandise");
        units = getIntent().getStringExtra("units");
        isVoucher = getIntent().getStringExtra("isVoucher");

        edittext_specifiedMerchandise.setText(specifiedMerchandise);
        edittext_stock.setText(stock);
        edittext_weight.setText(weight);
        if(units.equals("1")){
            spinner_units.setSelection(0, true);
        }else if(units.equals("2")){
            spinner_units.setSelection(1, true);;
        }else if(units.equals("3")){
            spinner_units.setSelection(2, true);;
        }else if(units.equals("4")){
            spinner_units.setSelection(3, true);;
        }
        editext_dianhua.setText(phone);
        edittext_contacts.setText(contacts);
        edittext_details.setText(details);
        edittext_weight.setText(weight);
        if(isVoucher.equals("1")){
            rbshi.setChecked(true);
        }else if(isVoucher.equals("0")){
            rbfou.setChecked(true);
        }

        sp = act.getSharedPreferences("config", 0);
        demandUserID = sp.getString("usernameid", "");
        demandUserName = sp.getString("username", "");
        token=sp.getString("token", "");
        button_qiuhuo.setOnClickListener(this);
        rgisvoucher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbshi = (RadioButton)findViewById(rgisvoucher.getCheckedRadioButtonId());
                shi = rbshi.getText().toString();
                rbfou = (RadioButton) findViewById(rgisvoucher.getCheckedRadioButtonId());
                fou = rbfou.getText().toString();

            }
        });
    }

    private void woQiuHuoAdd() {
        specifiedMerchandise=edittext_specifiedMerchandise.getText().toString().trim();
        stock=edittext_stock.getText().toString().trim();
        spinner=(String) spinner_units.getSelectedItem();
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
            Toast.makeText(act, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {

            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.WOQIUHUO_ADD;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id", yunfeiId);
                        params.put("demandUserID", demandUserID);
                        params.put("demandUserName", demandUserName);
                        params.put("stock", stock);
                        params.put("details", details);
                        params.put("weight", weight);
                        params.put("phone", phone);
                        params.put("contacts", contacts);
                        params.put("specifiedMerchandise", specifiedMerchandise);
                        params.put("units", units);
                        params.put("isVoucher", isVoucher);
                        String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                        if (strResult.equals(401)){
                            Intent intent=new Intent(act,DengLuActivity.class);
                            startActivity(intent);
                            act.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(act, "求货上传失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                };
            }.start();

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_qiuhuo:
                woQiuHuoAdd();
                break;
        }
    }

}
