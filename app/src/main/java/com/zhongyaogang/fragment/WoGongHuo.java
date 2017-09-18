package com.zhongyaogang.fragment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.activity.DengLuActivity;
import com.zhongyaogang.activity.DrugSearchActivity;
import com.zhongyaogang.activity.YunFeiSettingActivity;
import com.zhongyaogang.bean.YunFeiBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.DateTimePickDialog;
import com.zhongyaogang.utils.DateTimePickDialog.OnDateTimeSetListener;
import com.zhongyaogang.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "deprecation"})
public class WoGongHuo extends Fragment implements OnClickListener {
    // 调用拍照1
    private static final int pz1 = 1;
    // 调用拍照2
    private static final int pz2 = 2;
    // 调用拍照3
    private static final int pz3 = 3;
    DatePickerDialog dialog;
    private EditText     tv_origin;
    private EditText     tv_drug;
    private LinearLayout imageview_yunfeisetting;
    private TextView     tvfreight;
    private EditText     edittext_chuanpinshangjia;
    private EditText     edittext_chuanpinxiajia;
    private EditText     edittext_supplyTitle;
    private EditText     edittext_stock;
    private EditText     edittext_moq;
    private EditText     edittext_details;
    private EditText     edittext_standard;
    private Spinner      spinner_units;
    private EditText     edittext_repertory;
    private EditText     edittext_price;
    private EditText     edittext_warehouse;
    private Button       button_fabu;
    //	private String tuPian1 = null;
//	private String tuPian2 = null;
//	private String tuPian3 = null;
    private String       urltuPian1;
    private String       urltuPian2;
    private String       urltuPian3;
    private ImageView    img_paiZhao1;
    private ImageView    img_paiZhao2;
    private ImageView    img_paiZhao3;
    // 图片目录容器
    private int        tag=0;
    // 图片文件夹
    private YunFeiBean yunfeibean;
    private SharedPreferences sp;
    private String            sharedusernameid;
    private String            token;
    private String            name;
    private String            pwd;
    private String            origin;
    private WoGongHuo         act;
    private Context           context;
    private String            supplyTitle;
    private String            price;
    private String            stock;//规格
    private String            pigUrl;
    //	 private String supplyUID;
//	 private String supplyUserName;
    private String            details;//详细信息
    private String types = "1";//订单类型
    private String      repertory;//库存
    private String      merchandiseName;//自定义药品
    private String      originName;//产源地
    private String      warehouse;//仓库所在地
    private String      units;
    private String      figureId;//邮费
    private String      isVoucher;
    private String      standard;
    private String      moq;
    private String      upTime;//上架时间
    private String      downTime;//下架时间
    private String      urlResult;
    private RadioGroup  rgisvoucher;
    private RadioButton rbshi;
    private RadioButton rbfou;
    private String shi = "是";
    private String fou = "";
    private TextView    text_units;
    private String      spinner;
    private PopupWindow mpopupWindow;
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    L.e("到这了");
                    Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wogonghuo, container, false);
        this.act = WoGongHuo.this;
        EventBus.getDefault().register(act);
        initView(view);
        return view;
    }

    @Subscribe
    public void onEventMainThread(String result) {

    }

    /**
     * 处理fragment重复叠加的问题
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);//
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void woGongHuoAdd() {
        supplyTitle = edittext_supplyTitle.getText().toString().trim();
        stock = edittext_stock.getText().toString().trim();
        spinner = (String) spinner_units.getSelectedItem();
        if (spinner.equals("吨")) {
            units = "1";
        } else if (spinner.equals("千克")) {
            units = "2";
        } else if (spinner.equals("克")) {
            units = "3";
        } else if (spinner.equals("毫克")) {
            units = "4";
        }
//			 L.e("返回结果：str=" + str);
        moq = edittext_moq.getText().toString().trim();
        details = edittext_details.getText().toString().trim();
        standard = edittext_standard.getText().toString().trim();
        repertory = edittext_repertory.getText().toString().trim();
        price = edittext_price.getText().toString().trim();
        upTime = edittext_chuanpinshangjia.getText().toString().trim();
        downTime = edittext_chuanpinxiajia.getText().toString().trim();
        originName = tv_origin.getText().toString().trim();
        merchandiseName = tv_drug.getText().toString().trim();
        warehouse = edittext_warehouse.getText().toString().trim();
        if (shi.equals("是")) {
            isVoucher = "1";
        } else if (fou.equals("否")) {
            isVoucher = "0";
        }
        L.e("返回结果：isVoucher=" + isVoucher);
        if (TextUtils.isEmpty(supplyTitle) || TextUtils.isEmpty(stock) ||
                TextUtils.isEmpty(repertory) || TextUtils.isEmpty(price) ||
                TextUtils.isEmpty(upTime) || TextUtils.isEmpty(downTime) ||
                TextUtils.isEmpty(originName) || TextUtils.isEmpty(merchandiseName) ||
                TextUtils.isEmpty(warehouse) || TextUtils.isEmpty(standard)
                || TextUtils.isEmpty(units) || TextUtils.isEmpty(moq)
                || TextUtils.isEmpty(details) || TextUtils.isEmpty(isVoucher)|| TextUtils.isEmpty(urltuPian1)
                || TextUtils.isEmpty(urltuPian2)|| TextUtils.isEmpty(urltuPian3)) {
            Toast.makeText(getActivity(), "不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else if (yunfeibean == null) {
            Toast.makeText(getActivity(), "请选择运费", Toast.LENGTH_SHORT).show();
        } else {
            // 数据应该提交给服务器 由服务器比较是否正确
            new Thread() {
                public void run() {
                    try {
                        Looper.prepare();
                        String path = Constants.WOGONGHUO_ADD;
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", "0");
                        params.put("supplyUID", sharedusernameid);
                        params.put("supplyUserName", name);
                        params.put("supplyTitle", supplyTitle);
                        params.put("price", price);
                        params.put("stock", stock);
                        params.put("pigUrl", urltuPian1+","+urltuPian2+","+urltuPian3);
                        params.put("details", details);
                        params.put("types", types);
                        params.put("repertory", repertory);
                        params.put("merchandiseName", merchandiseName);
                        params.put("originName", originName);
                        params.put("warehouse", warehouse);
                        params.put("units", units);
                        params.put("figureId", yunfeibean.getId());
                        params.put("isVoucher", isVoucher);
                        params.put("upTime", upTime);
                        params.put("moq", moq);
                        params.put("standard", standard);
                        params.put("downTime", downTime);
                        params.put("shopId", "0");//店铺id
                        params.put("originId", "");//原产地ID
                        params.put("merchandiseId", "");//药品名
                        L.e("params" + params);
                        String strResult = HttpUtils.submitPostDataToken(path, params, "utf-8", token);
                        Log.e("strResult", strResult);//{"result":{"id":1311,"supplyTitle":"测试测试 测试","price":111.0,"upTime":"2017-09-06T11:19:00+08:00","downTime":"2017-09-06T11:19:00+08:00","stock":"1111","pigUrl":"http://yxt.jmzgo.com/Upload/Accounts/a37f5b14-a502-4b76-ace5-4834c4816535.png,http://yxt.jmzgo.com/Upload/Accounts/796ea6a6-5267-41b0-8a88-ddc040d2418a.png,http://yxt.jmzgo.com/Upload/Accounts/e3285d68-508c-467d-a177-b8142ccb12ec.png","details":"11111","types":1,"repertory":1111,"merchandiseName":"1111","originName":"请输入产111源地","warehouse":"1111","units":4,"figureId":1,"isVoucher":1,"standard":"1111","moq":1111,"shopId":361,"originId":1,"merchandiseId":1},"targetUrl":null,"success":true,"error":null,"unAuthorizedRequest":false,"__abp":true}

                        if (strResult.equals("401")) {
                            Intent intent = new Intent(getActivity(), DengLuActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        JSONObject obj = new JSONObject(strResult);
                        String success = obj.getString("success");
                        String error = obj.getString("error");
                        if (success.equals("true") && error.equals("null")) {
                            Message s = new Message();
                            s.what = 100;
                            mhandler.sendMessage(s);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "供货上传失败", Toast.LENGTH_SHORT)
                                .show();
                    }
                }

            }.start();

        }
    }

    private Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private void woGongHuoAddPicture(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bao);
        byte[] ba = bao.toByteArray();
        final String ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.PICTURE_UPLOADING;
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userID", sharedusernameid);
                    params.put("types", "1");
                    params.put("isEnable", "1");
                    params.put("base64", ba1);
                    String strResult = HttpUtils.submitPostDataToken(path, params, "utf-8", token);
                    if (strResult.equals("401")) {
                        Intent intent = new Intent(getActivity(), DengLuActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    JSONObject jo = new JSONObject(strResult);
                    JSONObject body1 = jo.getJSONObject("result");
                    urlResult = body1.getString("url");
                    L.e("urlResult:"+urlResult);
                    if (tag == pz1) {
                        urltuPian1 = urlResult;
                        Log.e("urltuPian1:", urltuPian1);
                    } else if (tag == pz2) {
                        urltuPian2 = urlResult;
                        Log.e("urltuPian2:", urltuPian2);
                    }
                    else if (tag == pz3) {
                        urltuPian3 = urlResult;
                        Log.e("urltuPian2:", urltuPian3);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "图片上传失败", Toast.LENGTH_SHORT)
                            .show();
                }

            }

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
        ContentResolver resolver = getActivity().getContentResolver();
        if (requestCode == 109 && resultCode == 110) {
            yunfeibean = (YunFeiBean) data.getSerializableExtra("data");
            tvfreight.setText(yunfeibean.getFigure() + " " + yunfeibean.getLogisticsType());
            //Toast.makeText(getActivity(), "拿到运费了", Toast.LENGTH_SHORT).show();
        }
        if (resultCode == Activity.RESULT_OK) {
            Bitmap b = null;
            switch (requestCode) {
                case pz1:
                    tag = pz1;
                    b = getbitmap(data);
                    img_paiZhao1.setImageBitmap(b);
                    img_paiZhao2.setVisibility(View.VISIBLE);
                    woGongHuoAddPicture(b);
                    break;
                case pz2:
                    tag = pz2;
                    b = getbitmap(data);
                    img_paiZhao2.setImageBitmap(b);
                    img_paiZhao3.setVisibility(View.VISIBLE);
                    woGongHuoAddPicture(b);
                    break;
                case pz3:
                    tag = pz3;
                    b = getbitmap(data);
                    img_paiZhao3.setImageBitmap(b);
                    woGongHuoAddPicture(b);
                    break;
                case pz1 + 3:
                    tag = pz1;
                    b=GetBitmap(data);
                    img_paiZhao1.setImageBitmap(b);
                    img_paiZhao2.setVisibility(View.VISIBLE);
                    woGongHuoAddPicture(b);
                    break;
                case pz2 + 3:
                    tag = pz2;
                    b=GetBitmap(data);
                    img_paiZhao2.setImageBitmap(b);
                    img_paiZhao3.setVisibility(View.VISIBLE);
                    woGongHuoAddPicture(b);
                    break;
                case pz3 + 3:
                    tag = pz3;
                    b=GetBitmap(data);
                    img_paiZhao3.setImageBitmap(b);
//                    img_paiZhao2.setVisibility(View.VISIBLE);
                    woGongHuoAddPicture(b);
                    break;
                default:
                    return;
            }
        }
    }
    private Bitmap GetBitmap(Intent data)
    {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        return compressBitmap(picturePath, 100, 100);
    }
    private Bitmap getbitmap(Intent data)
    {
        Bitmap b= (Bitmap) data.getExtras().get("data");
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
                b.getHeight(), matrix, true);
        return  b;
    }

    private void initView(final View view) {
//		text_units = (TextView) view.findViewById(R.id.text_units);
        tvfreight = (TextView) view.findViewById(R.id.tvfreight);
        rgisvoucher = (RadioGroup) view.findViewById(R.id.rgisvoucher);
        imageview_yunfeisetting = (LinearLayout) view
                .findViewById(R.id.imageview_yunfeisetting);
        tv_origin = (EditText) view.findViewById(R.id.tv_origin);
        tv_drug = (EditText) view.findViewById(R.id.tv_drug);
        edittext_chuanpinshangjia = (EditText) view.findViewById(R.id.edittext_chuanpinshangjia);
        edittext_chuanpinxiajia = (EditText) view.findViewById(R.id.edittext_chuanpinxiajia);
        edittext_supplyTitle = (EditText) view.findViewById(R.id.edittext_supplyTitle);
        edittext_stock = (EditText) view.findViewById(R.id.edittext_stock);
        spinner_units = (Spinner) view.findViewById(R.id.spinner_units);
        edittext_standard = (EditText) view.findViewById(R.id.edittext_standard);
        edittext_moq = (EditText) view.findViewById(R.id.edittext_moq);
        edittext_details = (EditText) view.findViewById(R.id.edittext_details);
        edittext_repertory = (EditText) view.findViewById(R.id.edittext_repertory);
        edittext_price = (EditText) view.findViewById(R.id.edittext_price);
        edittext_warehouse = (EditText) view.findViewById(R.id.edittext_warehouse);
        button_fabu = (Button) view.findViewById(R.id.button_fabu);
        button_fabu.setOnClickListener(this);
        edittext_chuanpinshangjia.setOnClickListener(this);
        edittext_chuanpinxiajia.setOnClickListener(this);
        imageview_yunfeisetting.setOnClickListener(this);
        //.setOnClickListener(this);
        //tv_drug.setOnClickListener(this);
        img_paiZhao1 = (ImageView) view.findViewById(R.id.img_feedback_tu1);
        img_paiZhao1.setOnClickListener(this);
        img_paiZhao2 = (ImageView) view.findViewById(R.id.img_feedback_tu2);
        img_paiZhao2.setOnClickListener(this);
        img_paiZhao3 = (ImageView) view.findViewById(R.id.img_feedback_tu3);
        img_paiZhao3.setOnClickListener(this);
        sp = getActivity().getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        name = sp.getString("username", "");
        pwd = sp.getString("password", "");
        token = sp.getString("token", "");
        originName = getActivity().getIntent().getStringExtra("origin");
        if (originName == null || originName == "") {
            //tv_origin.setText("请输入产源地");
        } else {
            tv_origin.setText(originName);
        }

        rgisvoucher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                rbshi = (RadioButton) view.findViewById(rgisvoucher.getCheckedRadioButtonId());
                shi = rbshi.getText().toString();
                rbfou = (RadioButton) view.findViewById(rgisvoucher.getCheckedRadioButtonId());
                fou = rbfou.getText().toString();

            }
        });

    }

    private Intent getIntent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.button_fabu:
                woGongHuoAdd();
                break;
            case R.id.img_feedback_tu1:
//                if (tag == 0) {
                    // startActivityForResult(intentpz, pz1);
                    showBottomPopupWindow(v, pz1);
              //  }

                break;
            case R.id.img_feedback_tu2:
               // if (tag == 1) {
                    showBottomPopupWindow(v, pz2);
               // }
                break;
            case R.id.img_feedback_tu3:
               // if (tag == 2) {
                    showBottomPopupWindow(v, pz3);
               // }
                break;
            case R.id.imageview_yunfeisetting://跳过去没值回传？
                //startActivity(new Intent(getActivity(), YunFeiSettingActivity.class));
                startActivityForResult(new Intent(getActivity(), YunFeiSettingActivity.class), 109);
                break;
            case R.id.tv_origin://跳过去没值回传？
                //startActivity(new Intent(getActivity(), OriginSearchActivity.class));
                break;
            case R.id.tv_drug://跳过去没值回传？
                startActivity(new Intent(getActivity(), DrugSearchActivity.class));
                break;
            case R.id.edittext_chuanpinshangjia:
                new DateTimePickDialog(getActivity(), c)
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
                                        .format("yyy-MM-dd-hh:mm", c));
                            }
                        });
                break;
            case R.id.edittext_chuanpinxiajia:
                new DateTimePickDialog(getActivity(), c)
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
                                        "yyy-MM-dd-hh:mm", c));
                            }
                        });
                break;
        }
    }

    private void showBottomPopupWindow(View v, final int k) {
        View customView = getActivity().getLayoutInflater().inflate(R.layout.alert_dialog, null, false);
        mpopupWindow = new PopupWindow(customView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mpopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED); // 这两句是键盘弹出来也不会挡住pop
        mpopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mpopupWindow.setFocusable(true); // 点击空白的时候关闭pop
        mpopupWindow.setTouchable(true);
        mpopupWindow.setOutsideTouchable(true);
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (mpopupWindow != null && mpopupWindow.isShowing()) {
                        mpopupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
        Button bendiDelete = (Button) customView.findViewById(R.id.btn_take_photo);
        Button bendiAllSelect = (Button) customView.findViewById(R.id.btn_pick_photo);
        Button quxiao = (Button) customView.findViewById(R.id.btn_cancel);

        quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mpopupWindow != null && mpopupWindow.isShowing()) {
                    mpopupWindow.dismiss();
                }
            }
        });
        bendiDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mpopupWindow != null && mpopupWindow.isShowing()) {
                    mpopupWindow.dismiss();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, k);
            }
        });
        bendiAllSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (mpopupWindow != null && mpopupWindow.isShowing()) {
                    mpopupWindow.dismiss();
                }
                /* 调用本地 */
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, k + 3);
            }
        });
        mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mpopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
}
