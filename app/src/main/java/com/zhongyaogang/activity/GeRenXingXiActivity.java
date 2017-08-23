package com.zhongyaogang.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongyaogang.R;
import com.zhongyaogang.utils.FileDirectory;
import com.zhongyaogang.utils.ImageProcessingUtil;

import java.io.File;

public class GeRenXingXiActivity extends Activity implements OnClickListener{
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private GeRenXingXiActivity act;
    private TextView index_search_yaocaiwang;
    private SharedPreferences sp;
    private String sharedusernameid;
    private String name;
    private String namephone;
    private RelativeLayout rlayimage;
    private ImageView imageview_touxiang;
    private RelativeLayout rlayusername;
    private TextView tvusername;
    private RelativeLayout rlaysex;
    private TextView tvsex;
    private RelativeLayout rlayname;
    private TextView tvname;
    private RelativeLayout rlayphone;
    private TextView tvphone;
    private RelativeLayout rlayaddres;
    private TextView tvaddress;
    private Bitmap bitmap;
    private String tuPian1 = null;
    private  PopupWindow  mpopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ge_ren_xing_xi);
        act=this;
        intView();
    }

    private void intView() {
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        index_search_yaocaiwang=(TextView)findViewById(R.id.index_search_yaocaiwang);
        index_search_yaocaiwang.setText("个人信息");
//		sp = this.getSharedPreferences("config", 0);
//		sharedusernameid=sp.getString("usernameid", "");
//		name=sp.getString("username", "");
//		namephone=sp.getString("usernamephone", "");
        rlayimage=(RelativeLayout) findViewById(R.id.rlayimage);
         imageview_touxiang=(ImageView) findViewById(R.id.imageview_touxiang);
         rlayusername=(RelativeLayout) findViewById(R.id.rlayusername);
          tvusername=(TextView) findViewById(R.id.tvusername);
         rlaysex=(RelativeLayout) findViewById(R.id.rlaysex);
         tvsex=(TextView) findViewById(R.id.tvsex);
         rlayname=(RelativeLayout) findViewById(R.id.rlayname);
         tvname=(TextView) findViewById(R.id.tvname);
        rlayphone=(RelativeLayout) findViewById(R.id.rlayphome);
         tvphone=(TextView) findViewById(R.id.tvphone);
         rlayaddres=(RelativeLayout) findViewById(R.id.rlayaddress);
         tvaddress=(TextView) findViewById(R.id.tvaddress);
        rlayimage.setOnClickListener(this);
        rlayaddres.setOnClickListener(this);
        rlayname.setOnClickListener(this);
        rlayphone.setOnClickListener(this);
        rlayusername.setOnClickListener(this);
        rlaysex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wodezhongxin_left:
                finish();
                break;
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case  R.id.rlayaddress:
                showDialog("请输入所在地",tvaddress);
                break;
            case  R.id.rlayname:
                showDialog("请输入真实姓名",tvname);
                break;
            case  R.id.rlayimage:
                showBottomPopupWindow(v,1);
                break;
            case  R.id.rlayphome:
                showDialog("请输入手机号码",tvphone);
                break;
            case  R.id.rlayusername:
                showDialog("请输入昵称",tvusername);
                break;
            case  R.id.rlaysex:
                showBottomPopupWindow(v,tvsex);
                break;
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
//					 intent.putExtra("usernameid", sharedusernameid);
//					 intent.putExtra("username", name);
//					 intent.putExtra("usernamephone", namephone);
                        startActivity(intent);
                        GeRenXingXiActivity.this.finish();
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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String sdStatus = Environment.getExternalStorageState();

        if (resultCode == Activity.RESULT_OK) {
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                Log.i("内存卡错误", "请检查您的内存卡");
            } else {
                switch (requestCode) {
                    case 1:
                        String tuPianurl=FileDirectory.pzls;
                        ImageProcessingUtil.save(tuPian1, 70);
                        bitmap=BitmapFactory
                                .decodeFile(tuPianurl);
                        imageview_touxiang.setImageBitmap(bitmap);
                        break;
                    case 4:
                        String tuPian = ImageProcessingUtil.selectImage(
                                this, data, 70);
                        bitmap=BitmapFactory
                                .decodeFile(tuPian);
                        imageview_touxiang.setImageBitmap(bitmap);
                        break;
                    default:
                        return;
                }
            }
        }
    }
    private void showDialog(String title,final TextView v)
    {
        View customView = getLayoutInflater().inflate(R.layout.item_input, null, false);
        final EditText et = (EditText)customView.findViewById(R.id.editText);
        et.setHint(title);
        new AlertDialog.Builder(this).setTitle(title)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(customView)
                .setPositiveButton("确定", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      String str=et.getText().toString();
                        v.setText(str);
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
    private void showBottomPopupWindow(View v,final TextView txt)
    {
        View customView = getLayoutInflater().inflate(R.layout.pop_sex, null, false);
        mpopupWindow = new PopupWindow(customView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT,true);
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
        RelativeLayout rlaymale=(RelativeLayout) customView.findViewById(R.id.rlaymale);
        RelativeLayout rlayfemale=(RelativeLayout) customView.findViewById(R.id.rlayfemale);
        rlaymale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("男");
                if (mpopupWindow != null && mpopupWindow.isShowing())
				{
					mpopupWindow.dismiss();
				}
            }
        });
        rlayfemale.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                txt.setText("女");
                if (mpopupWindow != null && mpopupWindow.isShowing())
                {
                    mpopupWindow.dismiss();
                }
            }
        });
        mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mpopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }
    private void  showBottomPopupWindow(View v,final int k)
    {
        View customView = getLayoutInflater().inflate(R.layout.alert_dialog, null, false);
        mpopupWindow = new PopupWindow(customView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT,true);
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
        Button bendiDelete = (Button) customView.findViewById(R.id.btn_take_photo);
        Button bendiAllSelect = (Button) customView.findViewById(R.id.btn_pick_photo);
        Button quxiao = (Button) customView.findViewById(R.id.btn_cancel);

        quxiao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mpopupWindow != null && mpopupWindow.isShowing())
                {
                    mpopupWindow.dismiss();
                }
            }
        });
        bendiDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定存放拍摄照片的位置
                File f = ImageProcessingUtil
                        .getAlbumDir(FileDirectory.YIJIANFANKUI);
                FileDirectory.pzls = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(f));
                startActivityForResult(takePictureIntent, k);
//				if (mpopupWindow != null && mpopupWindow.isShowing())
//				{
//					mpopupWindow.dismiss();
//				}
            }
        });
        bendiAllSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
				/* 调用本地 */
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2, k + 3);
//                if (mpopupWindow != null && mpopupWindow.isShowing())
//                {
//                    mpopupWindow.dismiss();
//                }
            }
        });
        mpopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mpopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }

}
