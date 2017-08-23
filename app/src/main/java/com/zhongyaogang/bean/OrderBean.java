package com.zhongyaogang.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */
public class OrderBean {
     private String shopName;
     private String orderState;
     private String farePrice;
     private String payable;
     private String stateCode;
     private List<OrderItem> orderItem;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
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

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public List<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    public  class  OrderItem{
        private String imagesUrl;//商品图片
        private String merchandiseName;//商品名
        private String actualPrice;//单价
        private String quantity;//数量

        public String getImagesUrl() {
            return imagesUrl;
        }

        public void setImagesUrl(String imagesUrl) {
            this.imagesUrl = imagesUrl;
        }

        public String getMerchandiseName() {
            return merchandiseName;
        }

        public void setMerchandiseName(String merchandiseName) {
            this.merchandiseName = merchandiseName;
        }

        public String getActualPrice() {
            return actualPrice;
        }

        public void setActualPrice(String actualPrice) {
            this.actualPrice = actualPrice;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }
    }
}
