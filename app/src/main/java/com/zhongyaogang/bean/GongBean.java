package com.zhongyaogang.bean;

import java.io.Serializable;

public class GongBean  implements Serializable {
//    private String id;
//    private String merchandiseName;
//    private String price;
//    private String pigUrl;
//    private String originalPrice;
//    private String supplyTitle;
//    private String upTime;
//    private String downTime;
//    private String stock;
//    private String supplyUID;
//    private String supplyUserName;
//    private String details;
//    private String types;
//    private String repertory;
//    private String originName;
//    private String warehouse;
//    private String units;
//    private String figureId;
//    private String isVoucher;
//    private String standard;
//    private String moq;
//
//    public String getOriginalPrice() {
//        return originalPrice;
//    }
//    public void setOriginalPrice(String originalPrice) {
//        this.originalPrice = originalPrice;
//    }
//    public String getSupplyTitle() {
//        return supplyTitle;
//    }
//    public void setSupplyTitle(String supplyTitle) {
//        this.supplyTitle = supplyTitle;
//    }
//    public String getUpTime() {
//        return upTime;
//    }
//    public void setUpTime(String upTime) {
//        this.upTime = upTime;
//    }
//    public String getDownTime() {
//        return downTime;
//    }
//    public void setDownTime(String downTime) {
//        this.downTime = downTime;
//    }
//    public String getStock() {
//        return stock;
//    }
//    public void setStock(String stock) {
//        this.stock = stock;
//    }
//    public String getSupplyUID() {
//        return supplyUID;
//    }
//    public void setSupplyUID(String supplyUID) {
//        this.supplyUID = supplyUID;
//    }
//    public String getSupplyUserName() {
//        return supplyUserName;
//    }
//    public void setSupplyUserName(String supplyUserName) {
//        this.supplyUserName = supplyUserName;
//    }
//    public String getDetails() {
//        return details;
//    }
//    public void setDetails(String details) {
//        this.details = details;
//    }
//    public String getTypes() {
//        return types;
//    }
//    public void setTypes(String types) {
//        this.types = types;
//    }
//    public String getRepertory() {
//        return repertory;
//    }
//    public void setRepertory(String repertory) {
//        this.repertory = repertory;
//    }
//    public String getOriginName() {
//        return originName;
//    }
//    public void setOriginName(String originName) {
//        this.originName = originName;
//    }
//    public String getWarehouse() {
//        return warehouse;
//    }
//    public void setWarehouse(String warehouse) {
//        this.warehouse = warehouse;
//    }
//    public String getUnits() {
//        return units;
//    }
//    public void setUnits(String units) {
//        this.units = units;
//    }
//    public String getFigureId() {
//        return figureId;
//    }
//    public void setFigureId(String figureId) {
//        this.figureId = figureId;
//    }
//    public String getIsVoucher() {
//        return isVoucher;
//    }
//    public void setIsVoucher(String isVoucher) {
//        this.isVoucher = isVoucher;
//    }
//    public String getStandard() {
//        return standard;
//    }
//    public void setStandard(String standard) {
//        this.standard = standard;
//    }
//    public String getMoq() {
//        return moq;
//    }
//    public void setMoq(String moq) {
//        this.moq = moq;
//    }
//    public String getId() {
//        return id;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }
//    public String getMerchandiseName() {
//        return merchandiseName;
//    }
//    public void setMerchandiseName(String merchandiseName) {
//        this.merchandiseName = merchandiseName;
//    }
//    public String getPrice() {
//        return price;
//    }
//    public void setPrice(String price) {
//        this.price = price;
//    }
//    public String getPigUrl() {
//        return pigUrl;
//    }
//    public void setPigUrl(String pigUrl) {
//        this.pigUrl = pigUrl;
//    }
//    "id": 22,
//            "supplyNo": "s1708211530340001",
//            "pigUrl": "http://yxt.jmzgo.com/Upload/Supply/d1602337-ac12-4f9c-9967-ad05c55077da.jpg",
//            "price": 250,
//            "stock": "500克",
//            "merchandiseName": "田七",
//            "shopName": null,
//            "shopId": 3,
//            "quantity": 1,
//            "moq": 1,
//            "freightTitle": "包邮",
//            "units": "千克"
    private  String id;
    private  String supplyNo;
    private  String pigUrl;
    private  String price;
    private  String stock;
    private  String merchandiseName;
    private  String shopName;
    private  String shopId;
    private  String quantity;
    private  String moq;
    private  String freightTitle;
    private  String units;

    public void setId(String id) {
        this.id = id;
    }

    public void setSupplyNo(String supplyNo) {
        this.supplyNo = supplyNo;
    }

    public void setPigUrl(String pigUrl) {
        this.pigUrl = pigUrl;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public void setFreightTitle(String freightTitle) {
        this.freightTitle = freightTitle;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getSupplyNo() {
        return supplyNo;
    }

    public String getId() {
        return id;
    }

    public String getPigUrl() {
        return pigUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getShopName() {
        return shopName;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }

    public String getShopId() {
        return shopId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMoq() {
        return moq;
    }

    public String getFreightTitle() {
        return freightTitle;
    }

    public String getUnits() {
        return units;
    }
}
