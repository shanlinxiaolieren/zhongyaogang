package com.zhongyaogang.activity;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import com.zhongyaogang.R;
import com.zhongyaogang.adapter.ShopcartExpandableListViewAdapter;
import com.zhongyaogang.adapter.ShopcartExpandableListViewAdapter.CheckInterface;
import com.zhongyaogang.adapter.ShopcartExpandableListViewAdapter.ModifyCountInterface;
import com.zhongyaogang.bean.NewShoppingCartOrderBean;
import com.zhongyaogang.config.Constants;
import com.zhongyaogang.bean.GroupInfo;
import com.zhongyaogang.bean.ShoppingCarBean;
import com.zhongyaogang.http.HttpUtils;
import com.zhongyaogang.utils.L;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
public class ShoppingCartActivity extends Activity implements CheckInterface, ModifyCountInterface, OnClickListener {


    private ExpandableListView exListView;
    private CheckBox cb_check_all;
    private TextView tv_total_price;
    private TextView tv_delete;
    private TextView tv_go_to_pay;
    private Context context;
    private ShoppingCartActivity act;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private int totalCountshoppingBatch;

    private ShopcartExpandableListViewAdapter selva;
    private ArrayList<NewShoppingCartOrderBean> neworderbeanlist=new ArrayList<>();
    private NewShoppingCartOrderBean  newshoppingcartorderbean;
    private List<GroupInfo> groups = new ArrayList<GroupInfo>();// 组元素数据列表
    private Map<String, List<ShoppingCarBean>> children = new HashMap<String, List<ShoppingCarBean>>();// 子元素数据列表

    private SharedPreferences sp;
    private String sharedusernameid;
    private String id;
    private String token;
    private String deleteList;
    private String shopId;
    private String stock;
    private String shopName;
    private String merchandiseName;
    private String supplyTitle;
    private String freightTitle;
    private String price;
    private String moq;
    private String units;
    private String pigUrl;
    private String cartsList;
    private String quantity;
    private StringBuilder str1=new StringBuilder();
    private StringBuilder str2=new StringBuilder();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    selva=new ShopcartExpandableListViewAdapter(groups, children, context);
                    selva.setCheckInterface(act);// 关键步骤1,设置复选框接口
                    selva.setModifyCountInterface(act);// 关键步骤2,设置数量增减接口
                    exListView.setAdapter(selva);
                    for (int i = 0; i < selva.getGroupCount(); i++)
                    {
                        exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                    break;
                case 2:
                    selva=new ShopcartExpandableListViewAdapter(groups, children, context);
                    selva.notifyDataSetChanged();
                    selva.setCheckInterface(act);// 关键步骤1,设置复选框接口
                    selva.setModifyCountInterface(act);// 关键步骤2,设置数量增减接口
                    exListView.setAdapter(selva);
                    for (int i = 0; i < selva.getGroupCount(); i++)
                    {
                        exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                    break;
                case 3:
                    selva=new ShopcartExpandableListViewAdapter(groups, children, context);
                    selva.notifyDataSetChanged();
                    selva.setCheckInterface(act);// 关键步骤1,设置复选框接口
                    selva.setModifyCountInterface(act);// 关键步骤2,设置数量增减接口
                    exListView.setAdapter(selva);
                    for (int i = 0; i < selva.getGroupCount(); i++)
                    {
                        exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopping_cart);
        EventBus.getDefault().register(this);
        act=this;
        initView();
        initEvents();
    }

    private void initView()
    {
        context = this;
        exListView = (ExpandableListView) findViewById(R.id.exListView);
        cb_check_all = (CheckBox) findViewById(R.id.all_chekbox);
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        tv_delete = (TextView) findViewById(R.id.tv_delete);
        tv_go_to_pay = (TextView) findViewById(R.id.tv_go_to_pay);
        sp = this.getSharedPreferences("config", 0);
        sharedusernameid = sp.getString("usernameid", "");
        token = sp.getString("token", "");
        shoppingCartQuery();
    }

    private void shoppingCartQuery() {
        // 数据应该提交给服务器 由服务器比较是否正确
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SHOPPING_QUERY;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", sharedusernameid);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：shoppingCartQueryresult="+strResult);
                    if (strResult.equals(401)){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                        JSONObject jo = new JSONObject(strResult);
                        JSONObject	body1 = jo.getJSONObject("result");
                        JSONArray items=body1.getJSONArray("items");
                        for(int i=0; i<items.length(); i++){
                            JSONObject yxtCartsList=items.getJSONObject(i);
                            L.e("返回结果：yxtCartsList="+yxtCartsList);
                            shopName=yxtCartsList.getString("shopName");
                            shopId=yxtCartsList.getString("shopId");
                            groups.add(new GroupInfo(i + "",shopName));
                            JSONArray yxtCarts=yxtCartsList.getJSONArray("yxtCartsListDto");
                            L.e("返回结果：yxtCartsListDto="+yxtCarts);
                            List<ShoppingCarBean> products = new ArrayList<ShoppingCarBean>();
                            StringBuilder str=new StringBuilder();
                            for (int j = 0; j <yxtCarts.length(); j++)
                            {
                                products.add(new ShoppingCarBean(yxtCarts.getJSONObject(j).getString("pigUrl"),yxtCarts.getJSONObject(j).getString("id"),yxtCarts.getJSONObject(j).getString("stock"),
                                        yxtCarts.getJSONObject(j).getString("merchandiseName"),yxtCarts.getJSONObject(j).getString("creationTime"),
                                        yxtCarts.getJSONObject(j).getInt("price"), yxtCarts.getJSONObject(j).getInt("quantity")));
                                L.e("返回结果：id="+yxtCarts.getJSONObject(j).getString("id"));
                                supplyTitle=yxtCarts.getJSONObject(j).getString("supplyTitle");
                                stock=yxtCarts.getJSONObject(j).getString("stock");
                                merchandiseName=yxtCarts.getJSONObject(j).getString("merchandiseName");
                                freightTitle=yxtCarts.getJSONObject(j).getString("freightTitle");
                                price=yxtCarts.getJSONObject(j).getString("price");
                                moq=yxtCarts.getJSONObject(j).getString("moq");
                                units=yxtCarts.getJSONObject(j).getString("units");
                                pigUrl=yxtCarts.getJSONObject(j).getString("pigUrl");
                                cartsList=yxtCarts.getJSONObject(j).getString("id");
                                quantity=yxtCarts.getJSONObject(j).getString("quantity");

                                if(j==yxtCarts.length()-1){
                                    str.append(yxtCarts.getJSONObject(j).getString("id"));
                                }else{
                                    str.append(yxtCarts.getJSONObject(j).getString("id"));
                                    str.append(",");
                                }
                            }
                            if(i==items.length()-1){
                                str1.append(str);
                            }else{
                                str1.append(str);
                                str1.append(",");
                            }
                            L.e("返回结果：str1="+str1);
                            EventBus.getDefault().post(str1);
                            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key

                        }
                        Message msg = new Message();
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "查询失败",
                            Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }
    private void initEvents()
    {
        cb_check_all.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_go_to_pay.setOnClickListener(this);
    }
    @Subscribe
    public void onEventMainThread(String Id){
        shoppingDelete(Id);
        if(str1==null){
            return;
        }else{
            deleteList=str1.toString();
            L.e("deleteList="+deleteList);
        }
        if(totalCount==0){
            return;
        }else{
            totalCountshoppingBatch=totalCount;
            L.e("totalCountBatch="+totalCountshoppingBatch);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    private void shoppingDelete(final String id){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.SHOPPING_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("id", id);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：deleteresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "删除失败", Toast.LENGTH_SHORT).show();
                }

            };
        }.start();
    }
    private void shoppingBatchDelete(){
        new Thread() {
            public void run() {
                try {
                    Looper.prepare();
                    String path = Constants.BATCH_DELETE;
                    Map<String,String> params = new HashMap<String,String>();
                    params.put("deleteList", deleteList);
                    String strResult= HttpUtils.submitPostDataToken(path,params, "utf-8",token);
                    L.e("返回结果：Batchdeleteresult"+strResult);
                    if (strResult.equals("401")){
                        Intent intent=new Intent(act,DengLuActivity.class);
                        startActivity(intent);
                        act.finish();
                    }
                        Message msg = new Message();
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(act, "删除失败", Toast.LENGTH_SHORT).show();
                }
            };
        }.start();
    }

    @Override
    public void onClick(View v)
    {
        AlertDialog alert;
        switch (v.getId())
        {
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0)
                {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                } else if(totalCount>0&&totalCount==1){
                    Intent intent=new Intent(act, DanGeQueRenDingDanActivity.class);
                    intent.putExtra("shopId", shopId);
                    intent.putExtra("supplyTitle", supplyTitle);
                    intent.putExtra("stock", stock);
                    intent.putExtra("merchandiseName", merchandiseName);
                    intent.putExtra("shopName", shopName);
                    intent.putExtra("freightTitle", freightTitle);
                    intent.putExtra("price", price);
                    intent.putExtra("moq", moq);
                    intent.putExtra("units", units);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("cartsList", cartsList);
                    intent.putExtra("pigUrl", pigUrl);
                    startActivity(intent);
                    act.finish();
                }else if(totalCount>0&&totalCount!=1){
//                    if (!isAllCheck()){
//                        Intent intent = new Intent(act, QueRenDingDanActivity.class);
//                        intent.putExtra("cartsList", cartsList);
//                        L.e("返回结果：cartsList=" + cartsList);
//                        startActivity(intent);
//                        act.finish();
//                    }else {
                        Intent intent = new Intent(act, QueRenDingDanActivity.class);
                        intent.putExtra("cartsList", str1.toString());
                        L.e("返回结果：QueRenDingDanActivitycartsList=" + str1.toString());
                        startActivity(intent);
                        act.finish();
//                    }
                }
//                alert = new AlertDialog.Builder(context).create();
//                alert.setTitle("操作提示");
//                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
//                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        return;
//                    }
//                });
//                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        return;
//                    }
//                });
//                alert.show();
                break;
            case R.id.tv_delete:
                if (totalCount == 0)
                {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        return;
                    }
                });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
//                        for (int j = 0; j <totalCount; j++) {
                            id = sp.getString("id", "");
//                            if(j==totalCount-1){
//                                str2.append(id);
//                            }else{
//                                str2.append(id);
//                                str2.append(",");
//                            }
//                        }
                        EventBus.getDefault().post(id);
//                        L.e("返回：str2=" + str2);
                        doDelete();
                        if(totalCountshoppingBatch>0&&totalCountshoppingBatch!=1){
                            L.e("返回：totalCountshoppingBatch=" + totalCountshoppingBatch);
                            shoppingBatchDelete();
                        }
                    }
                });
                alert.show();
                break;
        }
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete()
    {
        List<GroupInfo> toBeDeleteGroups = new ArrayList<GroupInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++)
        {
            GroupInfo group = groups.get(i);
            if (group.isChoosed())
            {

                toBeDeleteGroups.add(group);
            }
            List<ShoppingCarBean> toBeDeleteProducts = new ArrayList<ShoppingCarBean>();// 待删除的子元素列表
            List<ShoppingCarBean> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++)
            {
                if (childs.get(j).isChoosed())
                {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            childs.removeAll(toBeDeleteProducts);

        }

        groups.removeAll(toBeDeleteGroups);

        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked)
    {

        ShoppingCarBean product = (ShoppingCarBean) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getQuantity();
        currentCount++;
        product.setQuantity(currentCount);
        ((TextView) showCountView).setText(currentCount + "");

        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked)
    {

        ShoppingCarBean product = (ShoppingCarBean) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getQuantity();
        if (currentCount == 1)
            return;
        currentCount--;

        product.setQuantity(currentCount);
        ((TextView) showCountView).setText(currentCount + "");

        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked)
    {
        GroupInfo group = groups.get(groupPosition);
        List<ShoppingCarBean> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++)
        {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            cb_check_all.setChecked(true);
        else
            cb_check_all.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosiTion, boolean isChecked)
    {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        GroupInfo group = groups.get(groupPosition);
        List<ShoppingCarBean> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++)
        {
            if (childs.get(i).isChoosed() != isChecked)
            {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState)
        {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else
        {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck())
            cb_check_all.setChecked(true);
        else
            cb_check_all.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    private boolean isAllCheck()
    {

        for (GroupInfo group : groups)
        {
            if (!group.isChoosed())
                return false;

        }

        return true;
    }

    /** 全选与反选 */
    private void doCheckAll()
    {

        totalPrice = 0;
        if(groups!=null){

            int size = groups.size();
            if (size == 0)
            {
                return;
            }
            for (int i = 0; i < size; i++)
            {
                groups.get(i).setChoosed(cb_check_all.isChecked());
                GroupInfo group = groups.get(i);
                List<ShoppingCarBean> childs = children.get(group.getId());
                for (int j = 0; j < childs.size(); j++)
                {
                    childs.get(j).setChoosed(cb_check_all.isChecked());
                    totalPrice += childs.get(j).getQuantity() * childs.get(j).getFistPrice();
                }
            }
            selva.notifyDataSetChanged();
            calculate();
            L.e("返回：groupstotalPrice=" + totalPrice);
            L.e("返回：groupstotalCount=" + totalCount);

        }
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate()
    {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++)
        {
            GroupInfo group = groups.get(i);
            List<ShoppingCarBean> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++)
            {
                ShoppingCarBean product = childs.get(j);
                if (product.isChoosed())
                {
                    totalCount++;
                    totalPrice += product.getFistPrice() * product.getQuantity();
                }
            }
        }
        tv_total_price.setText("￥" + totalPrice);
        tv_go_to_pay.setText("去支付(" + totalCount + ")");
        L.e("返回：totalCount=" + totalCount);
        EventBus.getDefault().post(totalCount);

    }

}
