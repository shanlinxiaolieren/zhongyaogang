package com.zhongyaogang.bean;

public class BazaarItems {
	/*
	 * "marketName": "自由市场",
        "marketImage": "自由市场",
        "aboutMarket": "自由市场",
        "marketDescription": "自由市场",
        "marketLocation": "自由市场",
        "aboutShop": "自由市场",
        "creationTime": "2017-05-02T09:39:37.403",
        "id": 1
	 */
      private  String id;
      private  String marketName;
      private  String marketImage;
      private  String aboutMarket;
      private  String marketDescription;
      private  String marketLocation;
      private  String aboutShop;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarketName() {
		return marketName;
	}
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	public String getMarketImage() {
		return marketImage;
	}
	public void setMarketImage(String marketImage) {
		this.marketImage = marketImage;
	}
	public String getAboutMarket() {
		return aboutMarket;
	}
	public void setAboutMarket(String aboutMarket) {
		this.aboutMarket = aboutMarket;
	}
	public String getMarketDescription() {
		return marketDescription;
	}
	public void setMarketDescription(String marketDescription) {
		this.marketDescription = marketDescription;
	}
	public String getMarketLocation() {
		return marketLocation;
	}
	public void setMarketLocation(String marketLocation) {
		this.marketLocation = marketLocation;
	}
	public String getAboutShop() {
		return aboutShop;
	}
	public void setAboutShop(String aboutShop) {
		this.aboutShop = aboutShop;
	}
	@Override
	public String toString() {
		return "BazaarItems [id=" + id + ", marketName=" + marketName
				+ ", marketImage=" + marketImage + ", aboutMarket="
				+ aboutMarket + ", marketDescription=" + marketDescription
				+ ", marketLocation=" + marketLocation + ", aboutShop="
				+ aboutShop + "]";
	}
	
}
