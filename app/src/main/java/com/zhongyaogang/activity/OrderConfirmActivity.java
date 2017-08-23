package com.zhongyaogang.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhongyaogang.R;
import com.zhongyaogang.adapter.OrderConfirmAdapter;

import java.util.ArrayList;

/**
 * 订单确认
 */
public class OrderConfirmActivity extends Activity implements View.OnClickListener{
    private RelativeLayout rlayaddress;
    private ImageView wodezhongxin_left;
    private TextView tvname;
    private TextView tvphone;
    private TextView tvaddres;
    private TextView tvnum;
    private TextView tvSubmit;
    private ListView gv;
    private OrderConfirmAdapter adapter;
    private ArrayList<String>data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirm);
        initview();
    }

    private void initview() {
        wodezhongxin_left=(ImageView) findViewById(R.id.wodezhongxin_left);
        rlayaddress=(RelativeLayout) findViewById(R.id.rlayaddress);
        tvname=(TextView) findViewById(R.id.tvname);
        tvphone=(TextView) findViewById(R.id.tvphone);
        tvaddres=(TextView) findViewById(R.id.tvaddress);
        tvnum=(TextView) findViewById(R.id.tvnum);
        tvSubmit=(TextView) findViewById(R.id.tvSubmit);
        gv=(ListView) findViewById(R.id.gv);
        wodezhongxin_left.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        rlayaddress.setOnClickListener(this);
        data=new ArrayList<String>();
        for(int i=0;i<5;i++)
        {
            data.add("");
        }
        adapter=new OrderConfirmAdapter(this,data,tvnum);
        gv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.wodezhongxin_left:
                this.finish();
                break;
            case R.id.tvSubmit:
                Toast.makeText(OrderConfirmActivity.this, "提交订单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rlayaddress:
                Intent i=new Intent(this,AddressActivity.class);
                i.putExtra("tag","1");
                startActivityForResult(i,101);
                break;
        }
    }
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101&&resultCode==102)
        {
            String id=data.getStringExtra("id");
            String consignee=data.getStringExtra("consignee");
            String telephone=data.getStringExtra("telephone");
            String street=data.getStringExtra("street");
            String region=data.getStringExtra("region");
            tvname.setText("收货人:"+consignee);
            tvphone.setText(telephone);
            tvaddres.setText("收货地址"+region+street);
        }
    }
}
