package com.zhongyaogang.bean;

import java.io.Serializable;

public class ShengQingShoppingBean implements Serializable{

    private static final long serialVersionUID = 1L;
    private String id;
    private String certificate;
    private String idCard;
    private String idCardPng;
    private String proposer;
    private String proposerEimal;
    private String phone;
    private String contacts;
    private String contactsPhone;
    private String idCardStart;
    private String idCardEnd;
    private String certificateStart;
    private String certificateEnd;
    private String shopName;
    private String state;

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getContacts() {
        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
    public String getContactsPhone() {
        return contactsPhone;
    }
    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }
    public String getIdCardStart() {
        return idCardStart;
    }
    public void setIdCardStart(String idCardStart) {
        this.idCardStart = idCardStart;
    }
    public String getIdCardEnd() {
        return idCardEnd;
    }
    public void setIdCardEnd(String idCardEnd) {
        this.idCardEnd = idCardEnd;
    }
    public String getCertificateStart() {
        return certificateStart;
    }
    public void setCertificateStart(String certificateStart) {
        this.certificateStart = certificateStart;
    }
    public String getCertificateEnd() {
        return certificateEnd;
    }
    public void setCertificateEnd(String certificateEnd) {
        this.certificateEnd = certificateEnd;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCertificate() {
        return certificate;
    }
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
    public String getIdCard() {
        return idCard;
    }
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getIdCardPng() {
        return idCardPng;
    }
    public void setIdCardPng(String idCardPng) {
        this.idCardPng = idCardPng;
    }
    public String getProposer() {
        return proposer;
    }
    public void setProposer(String proposer) {
        this.proposer = proposer;
    }
    public String getProposerEimal() {
        return proposerEimal;
    }
    public void setProposerEimal(String proposerEimal) {
        this.proposerEimal = proposerEimal;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }


}
