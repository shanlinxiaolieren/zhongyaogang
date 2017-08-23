package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;
import com.zhongyaogang.R;
import com.zhongyaogang.adapter.OrderGoodsAdapter;
import com.zhongyaogang.view.MyGridView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/18.
 */
public class OrderGoodsActivity extends Activity implements View.OnClickListener {
    private ImageView wodezhongxin_left;
    private ImageView wodezhongxin_right;
    private RelativeLayout rlayaddress;
    private TextView tvSurplustime;
    private TextView tvcollect;
    private TextView tvname;
    private TextView tvphone;
    private TextView tvaddress;

    private TextView tvcompany;
    private TextView tvselectnum;
    private TextView tvcopy;
    private MyGridView gv;
    private TextView tvordernum;
    private TextView tvtime;
    private TextView tvShould;
    private TextView tvfreight;
    private TextView tvTotal;
    private RelativeLayout rlayservice;
    private RelativeLayout rlayrefund;
    private TextView tvmoeny;
    private TextView tvconfirm;
    private OrderGoodsAdapter adapter;
    private ArrayList<String>data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordergoods);
        initview();
    }

    private void initview() {
        tvname=(TextView) findViewById(R.id.tvname);
         tvphone=(TextView) findViewById(R.id.tvphone);
       tvaddress=(TextView) findViewById(R.id.tvaddress);
        wodezhongxin_left=(ImageView)findViewById(R.id.wodezhongxin_left);
        wodezhongxin_right=(ImageView)findViewById(R.id.wodezhongxin_right);
        tvSurplustime=(TextView) findViewById(R.id.tvSurplustime);
        tvcollect=(TextView)findViewById(R.id.tvcollect);
        tvcompany=(TextView)findViewById(R.id.tvcompany);
        tvselectnum=(TextView)findViewById(R.id.tvselectnum);
        tvcopy=(TextView)findViewById(R.id.tvcopy);
        gv=(MyGridView) findViewById(R.id.gv);
        tvordernum=(TextView)findViewById(R.id.tvordernum);
        tvtime=(TextView)findViewById(R.id.tvtime);
        tvShould=(TextView)findViewById(R.id.tvShould);
        tvfreight=(TextView)findViewById(R.id.tvfreight);
        tvTotal=(TextView)findViewById(R.id.tvTotal);
        rlayservice=(RelativeLayout) findViewById(R.id.rlayservice);
        rlayrefund=(RelativeLayout)findViewById(R.id.rlayrefund);
        tvmoeny=(TextView)findViewById(R.id.tvmoeny);
        tvconfirm=(TextView)findViewById(R.id.tvconfirm);
        rlayaddress=(RelativeLayout) findViewById(R.id.rlayaddress);
        wodezhongxin_left.setOnClickListener(this);
        wodezhongxin_right.setOnClickListener(this);
        rlayaddress.setOnClickListener(this);
        tvcopy.setOnClickListener(this);
        rlayservice.setOnClickListener(this);
        rlayrefund.setOnClickListener(this);
        tvconfirm.setOnClickListener(this);
        data=new ArrayList<>();
        for(int i=0;i<3;i++)
        {
            data.add("");
        }
        adapter=new OrderGoodsAdapter(this,data);
        gv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.wodezhongxin_left:
                this.finish();
                break;
            case R.id.wodezhongxin_right:
                showPopwindowMenu(v);
                break;
            case R.id.rlayaddress:
//                Intent i=new Intent(this,AddressActivity.class);
//                i.putExtra("tag","0");
//                startActivityForResult(i,101);
                break;
            case  R.id.tvcopy:
                //Toast.makeText(OrderGoodsActivity.this, "复制订单号", Toast.LENGTH_SHORT).show();
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("667373760445");
                Toast.makeText(this,"复制成功",Toast.LENGTH_LONG).show();
                break;
            case R.id.rlayservice:
                Toast.makeText(OrderGoodsActivity.this, "联系客服", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.rlayrefund:
                Toast.makeText(OrderGoodsActivity.this, "退款", Toast.LENGTH_SHORT).show();
                break;
            case  R.id.tvconfirm:
                Toast.makeText(OrderGoodsActivity.this, "确认发货", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void showPopwindowMenu(View parent) {
        View popView = View.inflate(this, R.layout.right_pop_menu, null);
        Button btnCamera = (Button) popView.findViewById(R.id.btn_camera_pop_camera);
        Button btnAlbum = (Button) popView.findViewById(R.id.btn_camera_pop_album);
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        final PopupWindow popWindow = new PopupWindow(popView,width,height);
        popWindow.setAnimationStyle(R.style.AnimBottom);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_camera_pop_camera:
                        Intent intent=new Intent(getApplication(),MainActivity.class);
                        intent.putExtra("contentid", 1);
                        startActivity(intent);
                        OrderGoodsActivity.this.finish();
                        break;
                    case R.id.btn_camera_pop_album:
                        startActivity(new Intent(getApplication(), WoDeXiaoXiActivity.class));
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
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode==101&&resultCode==102)
//        {
//            String id=data.getStringExtra("id");
//            String consignee=data.getStringExtra("consignee");
//            String telephone=data.getStringExtra("telephone");
//            String street=data.getStringExtra("street");
//            String region=data.getStringExtra("region");
//            tvname.setText("收货人:"+consignee);
//            tvphone.setText(telephone);
//            tvaddress.setText("收货地址"+region+street);
//        }
//    }

}
