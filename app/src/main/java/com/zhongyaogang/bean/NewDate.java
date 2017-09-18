package com.zhongyaogang.bean;

/**
 * Created by Administrator on 2017/7/5.
 */
public class NewDate {
    private int type;
    private String shopName;//订单店铺名
    private String orderState;//订单状态
    private String imagesUrl;//商品图片
    private String merchandiseName;//商品名
    private String actualPrice;//单价
    private String farePrice;//运费总价
    private String payable;//应付价格：单价x数量+运费总价
    private String quantity;//数量
    private String stateCode;//交易状态码
    private String orderId;
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public NewDate(int type, String shopName, String orderState, String imagesUrl, String merchandiseName, String actualPrice, String farePrice, String payable, String quantity, String stateCode) {
        this.type = type;
        this.shopName = shopName;
        this.orderState = orderState;
        this.imagesUrl = imagesUrl;
        this.merchandiseName = merchandiseName;
        this.actualPrice = actualPrice;
        this.farePrice = farePrice;
        this.payable = payable;
        this.quantity = quantity;
        this.stateCode = stateCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public String getFarePrice() {
        return farePrice;
    }

    public void setFarePrice(String farePrice) {
        this.farePrice = farePrice;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }
}
