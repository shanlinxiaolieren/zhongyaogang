package com.zhongyaogang.bean;

public class ShoppingCarBean extends BaseInfo{
    private String id;
    private int quantity;
    private int price;
    private String creationTime;
    private String merchandiseName;
    private String pigUrl;
    private String stock;
    private int position;// 绝对位置，只在ListView构造的购物车中，在删除时有效


    public ShoppingCarBean( String pigUrl,String id,String stock, String merchandiseName,String creationTime, int price, int count)
    {

        this.pigUrl = pigUrl;
        this.merchandiseName = merchandiseName;
        this.creationTime = creationTime;
        this.price = price;
        this.stock = stock;
        this.id = id;
        this.quantity = count;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }
    public void setStock(String stock) {
        this.stock = stock;
    }
    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getFistPrice() {
        return price;
    }
    public void setFistPrice(int fistPrice) {
        this.price = fistPrice;
    }

    public String getMerchandiseName() {
        return merchandiseName;
    }


    public void setMerchandiseName(String merchandiseName) {
        this.merchandiseName = merchandiseName;
    }


    public String getImagesUrl() {
        return pigUrl;
    }
    public void setImagesUrl(String imagesUrl) {
        this.pigUrl = imagesUrl;
    }
    public int getPosition() {
        return position;
    }
    public void setPosition(int position) {
        this.position = position;
    }


}

