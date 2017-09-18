package com.zhongyaogang.bean;

/**
 * Created by Administrator on 2017/8/26.
 */
public class SearchQiu {
//    "id": 17,
//            "specifiedMerchandise": "甘草",
//            "details": "啊打发打发",
//            "weight": 123123,
//            "units": "克"
    private String id;
    private String specifiedMerchandise;
    private String details;
    private String weight;
    private String units;

    public String getId() {
        return id;
    }

    public String getSpecifiedMerchandise() {
        return specifiedMerchandise;
    }

    public String getDetails() {
        return details;
    }

    public String getUnits() {
        return units;
    }

    public String getWeight() {
        return weight;
    }

    public void setSpecifiedMerchandise(String specifiedMerchandise) {
        this.specifiedMerchandise = specifiedMerchandise;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setId(String id) {
        this.id = id;
    }
}
