package com.zhongyaogang.activity;

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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhongyaogang.R;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.DateTimePickDialog;
import com.zhongyaogang.utils.FileDirectory;
import com.zhongyaogang.utils.ImageProcessingUtil;
import com.zhongyaogang.utils.L;
import com.zhongyaogang.utils.DateTimePickDialog.OnDateTimeSetListener;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressWarnings("deprecation")
public class GongHuoActivity extends Activity implements OnClickListener{

    private TextView tv_origin;
    private TextView tv_drug;
    private LinearLayout imageview_yunfeisetting;
    private EditText edittext_chuanpinshangjia;
    private EditText edittext_chuanpinxiajia;
    private EditText edittext_supplyTitle;
    private EditText edittext_stock;
    private EditText edittext_moq;
    private EditText edittext_details;
    private EditText edittext_standard;
    private Spinner spinner_units;
    private EditText edittext_repertory;
    private EditText edittext_price;
    private EditText edittext_warehouse;
    private Button button_fabu;
    private String tuPian1 = null;
    private String tuPian2 = null;
    private String tuPian3 = null;
    private String  urltuPian1;
    private String  urltuPian2;
    private String  urltuPian3;
    private ImageView img_paiZhao1;
    private ImageView img_paiZhao2;
    private ImageView img_paiZhao3;
    // 调用拍照1
    private static final int pz1 = 1;
    // 调用拍照2
    private static final int pz2 = 2;
    // 调用拍照3
    private static final int pz3 = 3;
    // 图片目录容器
    private List<String> tuPianList = new ArrayList<String>();
    private String muLu = Environment.getExternalStorageDirectory().getPath()
            + "/jwt/yjfk/";
    // 图片文件夹
    private String tuPian = muLu + "tupian/";
    DatePickerDialog dialog;
    private File file = new File(tuPian);
    private SharedPreferences sp;
    private String sharedusernameid;
    private String token;
    private String name;
    private String origin;
    private GongHuoActivity act;
    private String supplyTitle;
    private String price;
    private String stock;//规格
    private String pigUrl;
    //	 private String supplyUID;
//	 private String supplyUserName;
    private String details;//详细信息
    private String types="1";//订单类型
    private String repertory;//库存
    private String merchandiseName;//自定义药品
    private String originName;//产源地
    private String warehouse;//仓库所在地
    private String units;
    private String figureId;//邮费
    private String isVoucher;
    private String standard;
    private String moq;
    private String upTime;//上架时间
    private String downTime;//下架时间
    private String urlResult;
    private RadioGroup rgisvoucher;
    private RadioButton rbshi;
    private RadioButton rbfou;
    private String shi;
    private String fou;
    private TextView text_units;
    private String spinner;
    //	 private String str;
    private String gonghuoId;
    private String [] resultPigurl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gong_huo);
        this.act=GongHuoActivity.this;
        EventBus.getDefault().register(act);
        initView();
    }

    @Subscribe
    public void onEventMainThread(String result){
        tv_origin.setText(result);
        tv_drug.setText(result);
        if(urltuPian1==null||urltuPian2==null||urltuPian3==null){
            return;
        }else{
            pigUrl=urltuPian1+","+urltuPian2+","+urltuPian3;
            L.e("返回结果：图片pigUrl=" +pigUrl);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void woGongHuoAdd() {
        supplyTitle=edittext_supplyTitle.getText().toString().trim();
        stock=edittext_stock.getText().toString().trim();
        spinner=(String) spinner_units.getSelectedItem();
//		 spinner_units.setVisibility(View.GONE);
//	     	text_units.setVisibility(View.VISIBLE);
//			 str=text_units.getText().toString();
        if(spinner.equals("吨")){
            units="1";
        }else if(spinner.equals("千克")){
            units="2";
        }else if(spinner.equals("克")){
            units="3";
        }else if(spinner.equals("毫克")){
            units="4";
        }
        moq=edittext_moq.getText().toString().trim();
        details=edittext_details.getText().toString().trim();
        standard=edittext_standard.getText().toString().trim();
        repertory=edittext_repertory.getText().toString().trim();
        price=edittext_price.getText().toString().trim();
        upTime=edittext_chuanpinshangjia.getText().toString().trim();
        downTime=edittext_chuanpinxiajia.getText().toString().trim();
        originName=tv_origin.getText().toString().trim();
        merchandiseName=tv_drug.getText().toString().trim();
        warehouse=edittext_warehouse.getText().toString().trim();
        if(shi.equals("是")){
            isVoucher="1";
        }else if(fou.equals("否")){
            isVoucher="0";
        }
        L.e("返回结果：isVoucher=" + isVoucher);
        if (TextUtils.isEmpty(supplyTitle) || TextUtils.isEmpty(stock)||
                TextUtils.isEmpty(repertory)||TextUtils.isEmpty(price)||
                TextUtils.isEmpty(upTime)||TextUtils.isEmpty(downTime)||
                TextUtils.isEmpty(originName)||TextUtils.isEmpty(merchandiseName)||
                TextUtils.isEmpty(warehouse)||TextUtils.isEmpty(standard)
                ||TextUtils.isEmpty(units)||TextUtils.isEmpty(moq)
                ||TextUtils.isEmpty(details)||TextUtils.isEmpty(isVoucher)
                ) {
            Toast.makeText(act, "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {

            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.GONG_EDIT;
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("id", gonghuoId);
                        params.put("supplyUID",sharedusernameid);
                        params.put("supplyUserName",name);
                        params.put("supplyTitle", supplyTitle);
                        params.put("price", price);
                        params.put("stock",stock);
                        params.put("pigUrl",pigUrl);
                        params.put("details",details);
                        params.put("types", types);
                        params.put("repertory", repertory);
                        params.put("merchandiseName", merchandiseName);
                        params.put("originName", originName);
                        params.put("warehouse", warehouse);
                        params.put("units", units);
                        params.put("figureId", figureId);
                        params.put("isVoucher", isVoucher);
                        params.put("upTime", upTime);
                        params.put("moq", moq);
                        params.put("standard", standard);
                        params.put("downTime", downTime);
                        String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                        if (strResult.equals(401)){
                            Intent intent=new Intent(act,DengLuActivity.class);
                            startActivity(intent);
                            act.finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(act, "供货上传失败", Toast.LENGTH_SHORT).show();
                    }

                };
            }.start();

        }
    }

    private void woGongHuoAddPicture(final String tuPian) {
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
                        L.e("返回结果：图片result" + strResult);
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
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "图片上传失败", Toast.LENGTH_SHORT)
                            .show();
                }

            };
        }.start();
    }

    //	 //清空数据
//	 private void qingkong() {
//	 tuPianList.clear();
//	 img_paiZhao1.setImageResource(R.drawable.xq_feedback);
//	 img_paiZhao2.setImageResource(R.drawable.xq_feedback);
//	 img_paiZhao3.setImageResource(R.drawable.xq_feedback);
//	 img_paiZhao2.setVisibility(View.INVISIBLE);
//	 img_paiZhao3.setVisibility(View.INVISIBLE);
//	 tuPian1 = null;
//	 tuPian2 = null;
//	 tuPian3 = null;
//	 }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();

        if (resultCode == Activity.RESULT_OK) {
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.i("内存卡错误", "请检查您的内存卡");
            } else {
                switch (requestCode) {
                    case pz1:
                        tuPian1 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian1, 70);
                        img_paiZhao1.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian1));
                        tuPianList.add(tuPian1);
                        img_paiZhao2.setVisibility(View.VISIBLE);
                        break;
                    case pz2:
                        tuPian2 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian2, 70);
                        tuPianList.add(tuPian2);
                        img_paiZhao2.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian2));
                        img_paiZhao3.setVisibility(View.VISIBLE);

                        break;
                    case pz3:
                        tuPian3 = FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian3, 70);
                        tuPianList.add(tuPian3);
                        img_paiZhao3.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian3));
                        break;
                    case pz1 + 3:
                        String tupianPath4 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian1 = tupianPath4;
                        img_paiZhao1.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian1));
                        tuPianList.add(tuPian1);
                        img_paiZhao2.setVisibility(View.VISIBLE);
                        woGongHuoAddPicture(tuPian1);
                        break;
                    case pz2 + 3:
                        String tupianPath5 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian2 = tupianPath5;
                        img_paiZhao2.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian2));
                        tuPianList.add(tuPian2);
                        img_paiZhao3.setVisibility(View.VISIBLE);
                        woGongHuoAddPicture(tuPian2);
                        break;
                    case pz3 + 3:
                        String tupianPath6 = ImageProcessingUtil.selectImage(
                                act, data, 70);
                        tuPian3 = tupianPath6;
                        tuPianList.add(tuPian3);
                        img_paiZhao3.setImageBitmap(BitmapFactory
                                .decodeFile(tuPian3));
                        woGongHuoAddPicture(tuPian3);
                        break;
                    default:
                        return;
                }
            }
        }
    }

    private void xiuGaiView(){
        rbshi = (RadioButton)findViewById(R.id.rbshi);
        rbfou = (RadioButton)findViewById(R.id.rbfou);
        // 修改传过来的
        gonghuoId = getIntent().getStringExtra("id");
        sharedusernameid=getIntent().getStringExtra("supplyUID");
        supplyTitle=getIntent().getStringExtra("supplyTitle");
        price=getIntent().getStringExtra("price");
        upTime=getIntent().getStringExtra("upTime");
        downTime=getIntent().getStringExtra("downTime");
        stock=getIntent().getStringExtra("stock");
        pigUrl=getIntent().getStringExtra("pigUrl");
        name=getIntent().getStringExtra("supplyUserName");
        details=getIntent().getStringExtra("details");
        types=getIntent().getStringExtra("types");
        repertory=getIntent().getStringExtra("repertory");
        merchandiseName=getIntent().getStringExtra("merchandiseName");
        originName=getIntent().getStringExtra("originName");
        warehouse=getIntent().getStringExtra("warehouse");
        units=getIntent().getStringExtra("units");
        figureId=getIntent().getStringExtra("figureId");
        isVoucher=getIntent().getStringExtra("isVoucher");
        standard=getIntent().getStringExtra("standard");
        moq=getIntent().getStringExtra("moq");

        edittext_supplyTitle.setText(supplyTitle);
        edittext_stock.setText(stock);
        edittext_repertory.setText(repertory);
        edittext_price.setText(price);
        edittext_moq.setText(moq);
        if(units.equals("1")){
            spinner_units.setSelection(0, true);
        }else if(units.equals("2")){
            spinner_units.setSelection(1, true);;
        }else if(units.equals("3")){
            spinner_units.setSelection(2, true);;
        }else if(units.equals("4")){
            spinner_units.setSelection(3, true);;
        }
        edittext_standard.setText(standard);
        if(isVoucher.equals("1")){
            rbshi.setChecked(true);
        }else if(isVoucher.equals("0")){
            rbfou.setChecked(true);
        }
        tv_origin.setText(originName);
        edittext_warehouse.setText(warehouse);
        tv_drug.setText(merchandiseName);
        edittext_chuanpinshangjia.setText(upTime);
        edittext_chuanpinxiajia.setText(downTime);
        edittext_details.setText(details);
        resultPigurl=pigUrl.split(",");
//					 L.e("返回结果：resultPigurl[0]=" + resultPigurl[0]);
        ImageLoader.getInstance().displayImage(resultPigurl[0], img_paiZhao1);
        ImageLoader.getInstance().displayImage(resultPigurl[1], img_paiZhao2);
        ImageLoader.getInstance().displayImage(resultPigurl[2], img_paiZhao3);
    }
    private void initView() {
//		text_units = (TextView) findViewById(R.id.text_units);
        rgisvoucher = (RadioGroup) findViewById(R.id.rgisvoucher);
        imageview_yunfeisetting = (LinearLayout)
                findViewById(R.id.imageview_yunfeisetting);
        tv_origin = (TextView) findViewById(R.id.tv_origin);
        tv_drug = (TextView) findViewById(R.id.tv_drug);
        edittext_chuanpinshangjia = (EditText) findViewById(R.id.edittext_chuanpinshangjia);
        edittext_chuanpinxiajia = (EditText) findViewById(R.id.edittext_chuanpinxiajia);
        edittext_supplyTitle = (EditText) findViewById(R.id.edittext_supplyTitle);
        edittext_stock = (EditText) findViewById(R.id.edittext_stock);
        spinner_units = (Spinner) findViewById(R.id.spinner_units);
        edittext_standard = (EditText) findViewById(R.id.edittext_standard);
        edittext_moq = (EditText) findViewById(R.id.edittext_moq);
        edittext_details = (EditText) findViewById(R.id.edittext_details);
        edittext_repertory = (EditText) findViewById(R.id.edittext_repertory);
        edittext_price = (EditText) findViewById(R.id.edittext_price);
        edittext_warehouse = (EditText) findViewById(R.id.edittext_warehouse);
        button_fabu = (Button) findViewById(R.id.button_fabu);
        button_fabu.setOnClickListener(this);
        edittext_chuanpinshangjia.setOnClickListener(this);
        edittext_chuanpinxiajia.setOnClickListener(this);
        imageview_yunfeisetting.setOnClickListener(this);
        tv_origin.setOnClickListener(this);
        tv_drug.setOnClickListener(this);
        img_paiZhao1 = (ImageView) findViewById(R.id.img_feedback_tu1);
        img_paiZhao1.setOnClickListener(this);
        img_paiZhao2 = (ImageView) findViewById(R.id.img_feedback_tu2);
        img_paiZhao2.setOnClickListener(this);
        img_paiZhao3 = (ImageView)findViewById(R.id.img_feedback_tu3);
        img_paiZhao3.setOnClickListener(this);
        sp = act.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        name = sp.getString("username", "");
        token=sp.getString("token", "");

        xiuGaiView();
        if (!file.exists()) { // 检查图片存放的文件夹是否存在
            file.mkdirs(); // 不存在的话 创建文件夹
        }
        rgisvoucher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbshi = (RadioButton)findViewById(rgisvoucher.getCheckedRadioButtonId());
                shi = rbshi.getText().toString();
                rbfou = (RadioButton) findViewById(rgisvoucher.getCheckedRadioButtonId());
                fou = rbfou.getText().toString();

            }
        });
//		 spinner = (String) spinner_units.getSelectedItem();
//        //把该值传给 TextView
//    	text_units.setText(spinner);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.button_fabu:
                woGongHuoAdd();
                Toast.makeText(act, "发布成功",
                        Toast.LENGTH_LONG).show();
                break;
            case R.id.img_feedback_tu1:
                if (tuPianList.size() == 0) {
                    // startActivityForResult(intentpz, pz1);
                    dlog(pz1);
                }

                break;
            case R.id.img_feedback_tu2:
                if (tuPianList.size() == 1) {
                    dlog(pz2);
                }
                break;
            case R.id.img_feedback_tu3:
                if (tuPianList.size() == 2) {
                    dlog(pz3);
                }
                break;
            case R.id.imageview_yunfeisetting:
                startActivity(new Intent(act, YunFeiSettingActivity.class));
                break;
            case R.id.tv_origin:
                startActivity(new Intent(act, OriginSearchActivity.class));
                break;
            case R.id.tv_drug:
                startActivity(new Intent(act, DrugSearchActivity.class));
                break;
            case R.id.edittext_chuanpinshangjia:
                new DateTimePickDialog(act, c)
                        .setOnDateTimeSetListener(new OnDateTimeSetListener() {// 给定Calendar
                            // c,就能将日期和时间进行初始化
                            @Override
                            public void onDateTimeSet(DatePicker dp, TimePicker tp,
                                                      int year, int monthOfYear, int dayOfMonth,
                                                      int hourOfDay, int minute) {
                                // 保存选择后时间
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                edittext_chuanpinshangjia.setText(DateFormat
                                        .format("yyy-MM-dd-HH:mm", c));
                            }
                        });
                break;
            case R.id.edittext_chuanpinxiajia:
                new DateTimePickDialog(act, c)
                        .setOnDateTimeSetListener(new OnDateTimeSetListener() {// 给定Calendar
                            // c,就能将日期和时间进行初始化
                            @Override
                            public void onDateTimeSet(DatePicker dp, TimePicker tp,
                                                      int year, int monthOfYear, int dayOfMonth,
                                                      int hourOfDay, int minute) {
                                // 保存选择后时间
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                c.set(Calendar.MINUTE, minute);
                                edittext_chuanpinxiajia.setText(DateFormat.format(
                                        "yyy-MM-dd-HH:mm", c));
                            }
                        });
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
                // TODO 自动生成的方法存根
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
                startActivityForResult(intent2, k + 3);
            }
        });
        d.show();
    }



}
