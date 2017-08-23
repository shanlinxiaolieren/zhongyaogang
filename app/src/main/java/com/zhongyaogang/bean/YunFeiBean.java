package com.zhongyaogang.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class YunFeiBean implements Serializable{
    private static final long serialVersionUID = 1L;
    //		public ArrayList<YunFeiItems> items;
//		public static  class YunFeiItems{
    private String id;
    private String title;
    private String figure;// 初始价格
    private String logisticsType;// 快递方式
    private String upN;// 个数
    private String upMoney;// 金额



    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFigure() {
        return figure;
    }
    public void setFigure(String figure) {
        this.figure = figure;
    }
    public String getLogisticsType() {
        return logisticsType;
    }
    public void setLogisticsType(String logisticsType) {
        this.logisticsType = logisticsType;
    }
    public String getUpN() {
        return upN;
    }
    public void setUpN(String upN) {
        this.upN = upN;
    }
    public String getUpMoney() {
        return upMoney;
    }
    public void setUpMoney(String upMoney) {
        this.upMoney = upMoney;
    }
    @Override
    public String toString() {
        return "YunFeiBean [id=" + id + ", title=" + title
                + ", figure=" + figure + ", logisticsType="
                + logisticsType + ", upN=" + upN + ", upMoney="
                + upMoney + "]";
    }



//		}
}
