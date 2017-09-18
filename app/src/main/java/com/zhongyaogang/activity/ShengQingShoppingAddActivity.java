package com.zhongyaogang.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.bean.Market;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.FileDirectory;
import com.zhongyaogang.utils.ImageProcessingUtil;
import com.zhongyaogang.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
public class ShengQingShoppingAddActivity extends Activity implements OnClickListener{
    private TextView index_search_yaocaiwang;
    private TextView textview_shengqingrenxingxin;
    private TextView textview_zizixingxishangchuan;
    private TextView rl_save_address;
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private ImageView imageview_identity_card_front;
    private ImageView imageview_identity_card_contrary;
    //营业执照
    private ImageView imageview_business_license;
    //开户许可证
    private ImageView imageview_licence;
    private ShengQingShoppingAddActivity act;
    private EditText edittext_yingyenjieshudata;
    private EditText edittext_yingyenqishidata;
    private EditText edittext_shengfenjieshudata;
    private EditText edittext_shengfenqishidata;
    private DatePickerDialog dialog;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private String tuPian1 = null;
    private String tuPian2 = null;
    private String tuPian3 = null;
    private String tuPian4 = null;
    // 调用拍照1
    private static final int pz1 = 1;
    // 调用拍照2
    private static final int pz2 = 2;
    // 调用拍照3
    private static final int pz3 = 3;
    // 调用拍照4
    private static final int pz4 = 4;
    // 图片目录容器
    private List<String> tuPianList = new ArrayList<String>();
    private String muLu = Environment.getExternalStorageDirectory().getPath()
            + "/jwt/shengqingshopping/";
    // 图片文件夹
    private String tuPian = muLu + "tupian/";
    private File file = new File(tuPian);
    private EditText edittext_shopName;
    private EditText edittext_proposer;
    private EditText edittext_youxiang;
    private EditText edittext_shoujihaoma;
    private EditText edittext_idCard;
    private EditText edittext_contacts;
    private EditText edittext_contactshaoma;
    private String result;
    private String shopName;
    private String proposer;
    private String proposerEimal;
    private String phone;
    private String idCard;//身份证号码
    private String idCardPng;//身份证图片
    private String idCardStart;
    private String idCardEnd;
    private String contacts;
    private String contactsPhone;
    private String certificate;//营业执照
    private String certificateStart;
    private String certificateEnd;
    private  String urlResult;
    private String  urltuPian1;
    private String  urltuPian2;
    private String  urltuPian3;
    private String  urltuPian4;
    private String yunfeiId = "0";
    private String Alipay;
    private String market;
    private EditText edAlipay;
    private TextView tvmarket;
    private LinearLayout rlaymarket;
    private Market mMarket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除掉当前activity头title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sheng_qing_shopping_add);
        act=this;
        EventBus.getDefault().register(act);
        intView();
    }
    @Subscribe
    public void onEventMainThread(String url){
        if(urltuPian1==null||urltuPian2==null||urltuPian3==null||urltuPian4==null){
            return;
        }else{
            idCardPng=urltuPian1+","+urltuPian2;
            certificate=urltuPian3+","+urltuPian4;
            L.e("返回结果：图片idCardPng=" +idCardPng);
            L.e("返回结果：图片certificate=" +certificate);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void shengQingShoppingAdd(){
        shopName=edittext_shopName.getText().toString().trim();
        Alipay=edAlipay.getText().toString().trim();
        market=mMarket.getId();
        proposer=edittext_proposer.getText().toString().trim();
        proposerEimal=edittext_youxiang.getText().toString().trim();
        phone=edittext_shoujihaoma.getText().toString().trim();
        idCard=edittext_idCard.getText().toString().trim();
        contacts=edittext_contacts.getText().toString().trim();
        contactsPhone=edittext_contactshaoma.getText().toString().trim();
        idCardStart=edittext_shengfenqishidata.getText().toString().trim();
        idCardEnd=edittext_shengfenjieshudata.getText().toString().trim();
        certificateStart=edittext_yingyenqishidata.getText().toString().trim();
        certificateEnd=edittext_yingyenjieshudata.getText().toString().trim();
        if (TextUtils.isEmpty(proposer) || TextUtils.isEmpty(phone)||
                TextUtils.isEmpty(proposerEimal)||TextUtils.isEmpty(idCard)||
                TextUtils.isEmpty(contacts)||TextUtils.isEmpty(contactsPhone)||
                TextUtils.isEmpty(idCardStart)||TextUtils.isEmpty(certificateEnd)||
                TextUtils.isEmpty(certificateStart)||TextUtils.isEmpty(idCardEnd)||
                TextUtils.isEmpty(idCardPng)||TextUtils.isEmpty(certificate)
               ||TextUtils.isEmpty(shopName)||TextUtils.isEmpty(Alipay)||TextUtils.isEmpty(market)) {
            Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.SHENGQINGSHOPPING_ADD;
                        String pathXiuGai=Constants.SHENGQINGSHOPPING_EDIT;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id", yunfeiId);
                        params.put("userID",sharedusernameid);
                        params.put("shopName", shopName);
                        params.put("phone", phone);
                        params.put("proposer", proposer);
                        params.put("proposerEimal", proposerEimal);
                        params.put("idCard", idCard);
                        params.put("idCardPng", idCardPng);
                        params.put("idCardStart", idCardStart);
                        params.put("idCardEnd", idCardEnd);
                        params.put("certificate", certificate);
                        params.put("certificateStart",certificateStart);
                        params.put("certificateEnd", certificateEnd);
                        params.put("contacts", contacts);
                        params.put("contactsPhone", contactsPhone);
                        params.put("marketId", "0");
                        params.put("alipay ",Alipay);
                        String strResult= HttpUtils.submitPostDataToken(yunfeiId == "0" ? path:pathXiuGai,params, "utf-8",token);
                        if (strResult.equals("401")){
                            Intent intent=new Intent(act,DengLuActivity.class);
                            startActivity(intent);
                            act.finish();
                        }
                        Intent intent=new Intent(act,ShengQingShoppingActivity.class);
                            startActivity(intent);
                            act.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                    }

                };
            }.start();
        }
    }
    private void shengQingShoppingAddPicture(final String tuPian) {
        Bitmap bitmapOrg = BitmapFactory.decodeFile(tuPian);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.PNG, 90, bao);
        byte[] ba = bao.toByteArray();
        final String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.PICTURE_UPLOADING;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("userID",sharedusernameid);
                    params.put("types", "1");
                    params.put("isEnable","1");
                    params.put("base64",ba1);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：shengQingShoppingAddPicture="+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        urlResult=body1.getString("url");
                        if(tuPian==tuPian1){
                            urltuPian1=urlResult;
                            EventBus.getDefault().post(urltuPian1);
                            L.e("返回结果：图片urltuPian1=" +urltuPian1);
                        }else if(tuPian==tuPian2){
                            urltuPian2=urlResult;
                            EventBus.getDefault().post(urltuPian2);
                        }else if(tuPian==tuPian3){
                            urltuPian3=urlResult;
                            EventBus.getDefault().post(urltuPian3);
                        }else if(tuPian==tuPian4){
                            urltuPian4=urlResult;
                            EventBus.getDefault().post(urltuPian4);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "图片上传失败", Toast.LENGTH_SHORT)
                            .show();
                }

            };
        }.start();
    }

    private void intView() {
        rlaymarket=(LinearLayout)findViewById(R.id.rlaymarket);
        rlaymarket.setOnClickListener(this);
        edAlipay=(EditText) findViewById(R.id.edAlipay);
        tvmarket=(TextView) findViewById(R.id.tvmarket);
        edittext_shopName=(EditText)findViewById(R.id.edittext_shopName);
        edittext_proposer=(EditText)findViewById(R.id.edittext_proposer);
        edittext_youxiang=(EditText)findViewById(R.id.edittext_youxiang);
        edittext_shoujihaoma=(EditText)findViewById(R.id.edittext_shoujihaoma);
        edittext_contacts=(EditText)findViewById(R.id.edittext_contacts);
        edittext_contactshaoma=(EditText)findViewById(R.id.edittext_contactshaoma);
        edittext_idCard=(EditText)findViewById(R.id.edittext_idCard);
        edittext_yingyenjieshudata=(EditText)findViewById(R.id.edittext_yingyenjieshudata);
        edittext_yingyenqishidata=(EditText)findViewById(R.id.edittext_yingyenqishidata);
        edittext_shengfenjieshudata=(EditText)findViewById(R.id.edittext_shengfenjieshudata);
        edittext_shengfenqishidata=(EditText)findViewById(R.id.edittext_shengfenqishidata);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right);
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        imageview_licence=(ImageView)findViewById(R.id.imageview_licence);
        imageview_business_license=(ImageView)findViewById(R.id.imageview_business_license);
        imageview_identity_card_contrary=(ImageView)findViewById(R.id.imageview_identity_card_contrary);
        imageview_identity_card_front=(ImageView)findViewById(R.id.imageview_identity_card_front);
        index_search_yaocaiwang=(TextView) findViewById(R.id.index_search_yaocaiwang);
        rl_save_address=(TextView) findViewById(R.id.rl_save_address);
        textview_shengqingrenxingxin=(TextView) findViewById(R.id.textview_shengqingrenxingxin);
        textview_zizixingxishangchuan=(TextView) findViewById(R.id.textview_zizixingxishangchuan);
        index_search_yaocaiwang.setText("申请商户");
        String str="<font color='#940000'><small>●</small></font>申请人基本信息";
        textview_shengqingrenxingxin.setTextSize(22);
        textview_shengqingrenxingxin.setText(Html.fromHtml(str));
        String str2="<font color='#940000'><small>●</small></font>资质信息上传";
        textview_zizixingxishangchuan.setTextSize(22);
        textview_zizixingxishangchuan.setText(Html.fromHtml(str2));
        rl_save_address.setOnClickListener(this);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        imageview_identity_card_front.setOnClickListener(this);
        imageview_identity_card_contrary.setOnClickListener(this);
        imageview_business_license.setOnClickListener(this);
        imageview_licence.setOnClickListener(this);
        edittext_yingyenjieshudata.setOnClickListener(this);
        edittext_yingyenqishidata.setOnClickListener(this);
        edittext_shengfenjieshudata.setOnClickListener(this);
        edittext_shengfenqishidata.setOnClickListener(this);
        sp = this.getSharedPreferences("config", MODE_PRIVATE);
        sharedusernameid = sp.getString("usernameid", "");
        token=sp.getString("token", "");
        //修改传过来的
        yunfeiId=getIntent().getStringExtra("id");
        if(yunfeiId==null||yunfeiId=="0"){
            yunfeiId="0";
        }
        shopName=getIntent().getStringExtra("shopName");
        proposer=getIntent().getStringExtra("proposer");
        proposerEimal=getIntent().getStringExtra("proposerEimal");
        phone=getIntent().getStringExtra("phone");
        idCard=getIntent().getStringExtra("idCard");
        idCardStart=getIntent().getStringExtra("idCardStart");
        idCardEnd=getIntent().getStringExtra("idCardEnd");
        contacts=getIntent().getStringExtra("contacts");
        contactsPhone=getIntent().getStringExtra("contactsPhone");
        certificateStart=getIntent().getStringExtra("certificateStart");
        certificateEnd=getIntent().getStringExtra("certificateEnd");
        urltuPian3=getIntent().getStringExtra("certificate1");
        urltuPian4=getIntent().getStringExtra("certificate2");
        urltuPian1=getIntent().getStringExtra("idCardPng1");
        urltuPian2=getIntent().getStringExtra("idCardPng2");

        edittext_proposer.setText(proposer);
        edittext_youxiang.setText(proposerEimal);
        edittext_shoujihaoma.setText(phone);
        edittext_idCard.setText(idCard);
        edittext_contacts.setText(contacts);
        edittext_contactshaoma.setText(contactsPhone);
        edittext_shengfenqishidata.setText(idCardStart);
        edittext_shengfenjieshudata.setText(idCardEnd);
        edittext_yingyenqishidata.setText(certificateStart);
        edittext_yingyenjieshudata.setText(certificateEnd);
        edittext_youxiang.setText(proposerEimal);
        ImageLoader.getInstance().displayImage(urltuPian1, imageview_identity_card_front);
        ImageLoader.getInstance().displayImage(urltuPian2, imageview_identity_card_contrary);
        ImageLoader.getInstance().displayImage(urltuPian3, imageview_business_license);
        ImageLoader.getInstance().displayImage(urltuPian4, imageview_licence);
        if(urltuPian1==null||urltuPian2==null||urltuPian3==null||urltuPian4==null){
            return;
        }else{
            idCardPng=urltuPian1+","+urltuPian2;
            certificate=urltuPian3+","+urltuPian4;
            L.e("返回结果：idCardPng=" +idCardPng);
            L.e("返回结果：certificate=" +certificate);
        }
        if (!file.exists()) { // 检查图片存放的文件夹是否存在
            file.mkdirs(); // 不存在的话 创建文件夹
        }

    }

    @Override
    public void onClick(View v) {
        final Calendar c= Calendar.getInstance();
        switch (v.getId()) {
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case R.id.rlaymarket:
                startActivityForResult(new Intent(this,MarketSelectActivity.class),109);
                break;
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.rl_save_address:
                shengQingShoppingAdd();
                break;
            case R.id.imageview_licence:
                if (tuPianList.size() == 3) {
                    dlog(pz4);
                }
                break;
            case R.id.imageview_business_license:
                if (tuPianList.size() == 2) {
                    dlog(pz3);
                }
                break;
            case R.id.imageview_identity_card_front:
                if (tuPianList.size() == 0) {
                    dlog(pz1);
                }
                break;
            case R.id.imageview_identity_card_contrary:
                if (tuPianList.size() == 1) {
                    dlog(pz2);
                }
                break;
            case R.id.edittext_shengfenqishidata:
                dialog = new DatePickerDialog(ShengQingShoppingAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        edittext_shengfenqishidata.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.edittext_shengfenjieshudata:
                dialog = new DatePickerDialog(ShengQingShoppingAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        edittext_shengfenjieshudata.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.edittext_yingyenqishidata:
                dialog = new DatePickerDialog(ShengQingShoppingAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        edittext_yingyenqishidata.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.edittext_yingyenjieshudata:
                dialog = new DatePickerDialog(ShengQingShoppingAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        c.set(year, monthOfYear, dayOfMonth);
                        edittext_yingyenjieshudata.setText(DateFormat.format("yyy-MM-dd", c));
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
        }
    }
    /** 点击拍照从底部弹窗选择 */
    private void dlog(final int k) {
        final Dialog d = new Dialog(act, R.style.myDialog);
        d.setContentView(R.layout.alert_dialog);
        d.setTitle(null);
        Button bendiDelete = (Button) d.findViewById(R.id.btn_take_photo);
        Button bendiAllSelect = (Button) d.findViewById(R.id.btn_pick_photo);
        Button quxiao = (Button) d.findViewById(R.id.btn_cancel);
        quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //  自动生成的方法存根
                d.dismiss();
            }
        });
        bendiDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                d.dismiss();
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存放拍摄照片的位置
                File f = ImageProcessingUtil
                        .getAlbumDir(FileDirectory.YIJIANFANKUI);
                FileDirectory.pzls = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(f));
                startActivityForResult(takePictureIntent, k);
            }
        });
        bendiAllSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                d.dismiss();
				/* 调用本地 */
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2, k + 4);
            }
        });
        d.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();

        if(requestCode==109||resultCode==110)
        {
            mMarket=(Market) data.getSerializableExtra("mMarket");
            Log.e("mMarket",mMarket.getId());
            tvmarket.setText(mMarket.getMarketName());
        }
        if (resultCode == Activity.RESULT_OK) {
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.i("内存卡错误", "请检查您的内存卡");
            } else {
                switch (requestCode) {
                    case pz1:
                        tuPian1 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian1, 70);
                        imageview_identity_card_front.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian1));
                        tuPianList.add(tuPian1);
                        break;
                    case pz2:
                        tuPian2 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian2, 70);
                        tuPianList.add(tuPian2);
                        imageview_identity_card_contrary.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian2));
                        break;
                    case pz3:
                        tuPian3 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian3, 70);
                        tuPianList.add(tuPian3);
                        imageview_business_license.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian3));
                        break;
                    case pz4:
                        tuPian4 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian4, 70);
                        tuPianList.add(tuPian4);
                        imageview_licence.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian4));
                        break;
                    case pz1 + 4:
                        String tupianPath5 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian1 = tupianPath5;
                        imageview_identity_card_front.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian1));
                        tuPianList.add(tuPian1);
                        shengQingShoppingAddPicture(tuPian1);
                        break;
                    case pz2 + 4:
                        String tupianPath6 = ImageProcessingUtil.selectImage(
                                act, data,70);
                        tuPian2 = tupianPath6;
                        imageview_identity_card_contrary.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian2));
                        tuPianList.add(tuPian2);
                        shengQingShoppingAddPicture(tuPian2);
                        break;
                    case pz3 + 4:
                        String tupianPath7 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian3 = tupianPath7;
                        tuPianList.add(tuPian3);
                        imageview_business_license.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian3));
                        shengQingShoppingAddPicture(tuPian3);
                        break;
                    case pz4 + 4:
                        String tupianPath8 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian4 = tupianPath8;
                        tuPianList.add(tuPian4);
                        imageview_licence.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian4));
                        shengQingShoppingAddPicture(tuPian4);
                        break;
                    default:
                        return;
                }
            }
        }
    }
    private void showPopwindowMenu(View parent) {
        View popView = View.inflate(act, R.layout.right_pop_menu, null);
        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView,width,height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        OnClickListener listener = new OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        Intent intent=new Intent(act,MainActivity.class);
                        intent.putExtra("contentid", 1);
                        startActivity(intent);
                        ShengQingShoppingAddActivity.this.finish();
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
        popWindow.showAsDropDown(parent,10,20);
//		popWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }

}
