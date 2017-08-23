package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.bean.AddressBean;
import com.zhongyaogang.bean.City;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressAddActivity extends Activity implements OnClickListener {

    private ImageView wodezhongxin_left_address;
    private TextView tv_city1;
    private TextView rlSaveAddress;
    private TextView index_search_yaocaiwang_address;
    private EditText etUserName;
    private EditText etAddressDetails;
    private EditText etPhoneNumber;
    private String name;
    private String address;
    private String phone;
    private String street;
    private RelativeLayout rlRootLayout;
    private AddressBean addressBean;
    private String addressId;
    private City city;
    private ArrayList<City> toCitys;
    private ImageView wodezhongxin_right_address;
    private AddressAddActivity act;
    private HashMap<String, String> saveMap;
    private String url = Constants.ADD_ADDRESS;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String username;
    private String namephone;
    private String token;
    private String yunfeiId = "0";
    private String tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_address_add);
        act = this;
        intView();
    }

    private void intView() {
        index_search_yaocaiwang_address=(TextView) findViewById(R.id.index_search_yaocaiwang_address);
        wodezhongxin_right_address = (ImageView) findViewById(R.id.wodezhongxin_right_address);
        wodezhongxin_left_address = (ImageView) findViewById(R.id.wodezhongxin_left_address);
        etAddressDetails = (EditText) findViewById(R.id.etAddressDetails);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPhoneNumber = (EditText) findViewById(R.id.edittext_adresshaoma);
        wodezhongxin_left_address.setOnClickListener(this);
        wodezhongxin_right_address.setOnClickListener(this);
        tv_city1 = (TextView) findViewById(R.id.tv_city);
        tv_city1.setOnClickListener(this);
        city = new City();
        toCitys = new ArrayList<City>();
        rlSaveAddress = (TextView) findViewById(R.id.rl_save_address);
        rlRootLayout = (RelativeLayout) findViewById(R.id.rl_root_layout);
        rlRootLayout.setOnClickListener(this);
        rlSaveAddress.setOnClickListener(this);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token = sp.getString("token", "");
        // 修改传过来的
        yunfeiId = getIntent().getStringExtra("id");
        if (yunfeiId == null || yunfeiId == "0") {
            yunfeiId = "0";
        }
        name = getIntent().getStringExtra("consignee");
        phone = getIntent().getStringExtra("telephone");
        street = getIntent().getStringExtra("street");
        address = getIntent().getStringExtra("region");
        tag=getIntent().getStringExtra("tag");
        if(tag.equals("1"))
        {
            index_search_yaocaiwang_address.setText("修改收货人");
        }
        etUserName.setText(name);
        etPhoneNumber.setText(phone);
        etAddressDetails.setText(street);
        tv_city1.setText(address);
    }

    private void addressAdd() {
        name = etUserName.getText().toString().trim();
        phone = etPhoneNumber.getText().toString().trim();
        street = etAddressDetails.getText().toString().trim();
        address = tv_city1.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(address) || TextUtils.isEmpty(street)) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                @SuppressWarnings("deprecation")
                public void run() {
                    try {

                        Looper.prepare();
                        String path = Constants.ADD_ADDRESS;
                        String pathXiuGai = Constants.EDIT_ADDRESS;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id", yunfeiId);
                        params.put("userID",sharedusernameid);
                        params.put("consignee", name);
                        params.put("telephone", phone);
                        params.put("region", address);
                        params.put("street", street);
                        params.put("isDefault", "0");
                        params.put("zipCode", "0");
                        String strResult= HttpUtils.submitPostDataToken(yunfeiId == "0" ? path:pathXiuGai,params, "utf-8",token);
                        L.e("返回结果：addressAddresult" + strResult);
//                            Intent intent = new Intent(act,AddressActivity.class);
//                            startActivity(intent);
                            act.finish();
//                        // 1.打开浏览器
//                        HttpClient client = new DefaultHttpClient();
//                        // 2.设置数据
//                        HttpPost httpPost = new HttpPost(yunfeiId == "0" ? path
//                                : pathXiuGai);
//                        httpPost.addHeader("Authorization", "Bearer" + " "
//                                + token);
//                        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
//                        parameters.add(new BasicNameValuePair("id", yunfeiId));
//                        parameters.add(new BasicNameValuePair("userID",sharedusernameid));
//                        parameters
//                                .add(new BasicNameValuePair("consignee", name));
//                        parameters.add(new BasicNameValuePair("telephone",
//                                phone));
//                        parameters
//                                .add(new BasicNameValuePair("region", address));
//                        parameters
//                                .add(new BasicNameValuePair("street", street));
//                        parameters
//                                .add(new BasicNameValuePair("isDefault", "0"));
//                        parameters.add(new BasicNameValuePair("zipCode", "0"));
//                        httpPost.setEntity(new UrlEncodedFormEntity(parameters,
//                                "UTF-8"));
//                        L.e("返回：parameters=" + parameters);
//                        // 3.发送请求
//                        HttpResponse response = client.execute(httpPost);
//                        int code = response.getStatusLine().getStatusCode();
//                        L.e("返回结果：code=" + code);
//                        if (code == 200) {
//                            InputStream is = response.getEntity().getContent();
//                            final String result = SystemUtil.readStream(is);
//                            L.e("返回结果：addressAddresult" + result);
//                            Toast.makeText(getApplicationContext(), "添加成功",
//                                    Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(act,
//                                    AddressActivity.class);
//                            startActivity(intent);
//                            act.finish();
//                        }else if(code==401){
//                            Intent intent=new Intent(act,DengLuActivity.class);
//                            startActivity(intent);
//                            act.finish();
//                        } else {
//                            Toast.makeText(getApplicationContext(), "添加失败",
//                                    Toast.LENGTH_SHORT).show();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "添加失败",
                                Toast.LENGTH_SHORT).show();
                    }

                };
            }.start();
        }
    }

    // /**
    // * 对用户的输入进行简单的验证判断
    // * @return
    // */
    // private boolean isLegal(){
    // name=etUserName.getText().toString().trim();
    // phone=etPhoneNumber.getText().toString().trim();
    // address=etAddressDetails.getText().toString().trim();
    // street=tv_city1.getText().toString().trim();
    // if (TextUtils.isEmpty(name)) {
    // Toast.makeText(this, "请填写您的姓名", 1).show();
    // return false;
    // } else if (TextUtils.isEmpty(phone) ||
    // !CommonUtils.matcher(phone,"^1[3|4|5|7|8][0-9]\\d{8}$")) {
    // Toast.makeText(this,
    // getResources().getString(R.string.register_input_auth_phone_hint),
    // 1).show();
    // return false;
    // }else if (TextUtils.isEmpty(address)) {
    // Toast.makeText(this, "请填写详细地址", 1).show();
    // return false;
    // }
    // else if (street==null) {
    // Toast.makeText(this, "请选择区域", 1).show();
    // return false;
    // }
    // return true;
    // }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8) {
            if (requestCode == 1) {
                city = data.getParcelableExtra("city");
                tv_city1.setText(city.getProvince() + city.getCity()
                        + city.getDistrict());
            } else if (requestCode == 2) {
                toCitys = data.getParcelableArrayListExtra("toCitys");
                StringBuffer ab = new StringBuffer();
                for (int i = 0; i < toCitys.size(); i++) {
                    if (i == toCitys.size() - 1) {// 如果是最后一个城市就不需要逗号
                        ab.append(toCitys.get(i).getCity());
                    } else {
                        ab.append(toCitys.get(i).getCity() + "， ");// 如果不是最后一个城市就需要逗号
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_city:
                Intent in = new Intent(this, CitySelect1Activity.class);
                in.putExtra("city", city);
                startActivityForResult(in, 1);
                break;
            case R.id.wodezhongxin_left_address:
                finish();
                break;
            case R.id.wodezhongxin_right_address:
                showPopwindowMenu(v);
                break;
            case R.id.rl_save_address://也有可能是修改啊
                addressAdd();
                break;
        }
    }

    private void showPopwindowMenu(View parent) {
        View popView = View.inflate(act, R.layout.right_pop_menu, null);
        Button btnCamera = (Button) popView
                .findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView
                .findViewById(R.id.btn_camera_pop_album);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView, width, height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        Intent intent = new Intent(act, MainActivity.class);
                        intent.putExtra("contentid", 1);
                        startActivity(intent);
                        AddressAddActivity.this.finish();
                        break;
                    case R.id.btn_camera_pop_album:
                        startActivity(new Intent(act, WoDeXiaoXiActivity.class));
                        break;
                }
                popWindow.dismiss();
            }
        };
        btnCamera.setOnClickListener(listener);
        btnAlbum.setOnClickListener(listener);

        ColorDrawable dw = new ColorDrawable(0x30000000);
        popWindow.setBackgroundDrawable(dw);
        // 设置好参数之后再show
        popWindow.showAsDropDown(parent, 10, 20);
        // popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
