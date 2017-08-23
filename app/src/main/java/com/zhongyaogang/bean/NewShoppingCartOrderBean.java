package com.zhongyaogang.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/7.
 */
public class NewShoppingCartOrderBean implements Parcelable{
    private int type;
    private  String shopId;
    private  String shopName;
    private  String freightTitle;
    private  String stock;
    private  String supplyTitle;
    private  String merchandiseName;
    private  String actualPrice;
    private  String totalPrice;
    private  String moq;
    private  String units;
    private  String imagesUrl;
    private  String quantity;
    private  String cartsList;

    public NewShoppingCartOrderBean() {
    }

    public NewShoppingCartOrderBean(Parcel in) {
        supplyTitle = in.readString();
        shopId = in.readString();
        stock = in.readString();
        merchandiseName = in.readString();
        shopName = in.readString();
        freightTitle = in.readString();
        actualPrice = in.readString();
        moq = in.readString();
        units = in.readString();
        imagesUrl = in.readString();
        quantity = in.readString();
        cartsList = in.readString();
        totalPrice = in.readString();
    }

    public NewShoppingCartOrderBean(int type,String shopName,String merchandiseName,  String moq, String units, String imagesUrl,String stock, String actualPrice, String quantity,   String freightTitle,String totalPrice) {
        this.type = type;
        this.stock = stock;
        this.merchandiseName = merchandiseName;
        this.shopName = shopName;
        this.freightTitle = freightTitle;
        this.actualPrice = actualPrice;
        this.moq = moq;
        this.units = units;
        this.imagesUrl = imagesUrl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }


    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static final Creator<NewShoppingCartOrderBean> CREATOR = new Creator<NewShoppingCartOrderBean>() {
        @Override
        public NewShoppingCartOrderBean createFromParcel(Parcel in) {
            return new NewShoppingCartOrderBean(in);
        }

        @Override
        public NewShoppingCartOrderBean[] newArray(int size) {
            return new NewShoppingCartOrderBean[size];
        }
    };

    public String getSupplyTitle() {
        return supplyTitle;
    }

    public void setSupplyTitle(String supplyTitle) {
        this.supplyTitle = supplyTitle;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFreightTitle() {
        return freightTitle;
    }

    public void setFreightTitle(String freightTitle) {
        this.freightTitle = freightTitle;
    }

    public String getPrice() {
        return actualPrice;
    }

    public void setPrice(String price) {
        this.actualPrice = actualPrice;
    }

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getPigUrl() {
        return imagesUrl;
    }

    public void setPigUrl(String pigUrl) {
        this.imagesUrl = pigUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCartsList() {
        return cartsList;
    }

    public void setCartsList(String cartsList) {
        this.cartsList = cartsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(supplyTitle);
          dest.writeString(shopId);
          dest.writeString(stock);
          dest.writeString(merchandiseName);
          dest.writeString(shopName);
          dest.writeString(freightTitle);
          dest.writeString(actualPrice);
          dest.writeString(moq);
          dest.writeString(units);
          dest.writeString(imagesUrl);
          dest.writeString(quantity);
          dest.writeString(cartsList);
          dest.writeString(totalPrice);

    }
}
