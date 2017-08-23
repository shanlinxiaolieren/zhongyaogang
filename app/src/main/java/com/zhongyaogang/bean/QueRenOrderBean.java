package com.zhongyaogang.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/8.
 */
public class QueRenOrderBean {
    private  String shopName;
    private  String freightTitle;
    private  String totalPrice;
    private  String shopId;
    private List<NewShoppingCartOrderBean> orderItem;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public List<NewShoppingCartOrderBean> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<NewShoppingCartOrderBean> orderItem) {
        this.orderItem = orderItem;
    }

    public  class  OrderItem{
        private  String stock;
        private  String supplyTitle;
        private  String merchandiseName;
        private  String price;
        private  String moq;
        private  String units;
        private  String pigUrl;
        private  String quantity;
        private  String cartsList;

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getSupplyTitle() {
            return supplyTitle;
        }

        public void setSupplyTitle(String supplyTitle) {
            this.supplyTitle = supplyTitle;
        }

        public String getMerchandiseName() {
            return merchandiseName;
        }

        public void setMerchandiseName(String merchandiseName) {
            this.merchandiseName = merchandiseName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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
            return pigUrl;
        }

        public void setPigUrl(String pigUrl) {
            this.pigUrl = pigUrl;
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
    }

}
