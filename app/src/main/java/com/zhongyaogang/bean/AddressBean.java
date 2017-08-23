package com.zhongyaogang.bean;


/**
 * 收货地址实体类
 *
 */
public class AddressBean  {
    /* "userID": 2,用户ID
     "telephone": 0,手机号码
     "street": "string",具体街道
     "consignee": "string",收件人
     "region": "string",大体地址
     "isDefault": true,默认
     "zipCode": 0,邮政
     "isEnable": true,
     "creationTime": "2017-05-25T17:33:41.243",
     "id": 1*/
    private String id;
    private String userID;
    private String consignee;//收货人
    private String telephone;//收货人电话
    private String street;//具体街道
    private String  region;
    private String isDefault;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getConsignee() {
        return consignee;
    }
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }
    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getIsDefault() {
        return isDefault;
    }
    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }





}

