package com.zhongyaogang.config;


import android.os.Environment;

public class Constants {

	// SDCard路径
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	// 图片存储路径
	public static final String BASE_PATH = SD_PATH + "/zhongyaogang/";
	// 缓存图片路径
	public static final String BASE_IMAGE_CACHE = BASE_PATH + "cache/images/";
	/** 域名*/
	public final static String JMZG = "http://yxt.jmzgo.com";
	//我供货添加
	public final static String WOGONGHUO_ADD=JMZG+"/api/services/app/yXTSupply/CreateYXTSupplyAsync";
	//我求货添加
	public final static String WOQIUHUO_ADD=JMZG+"/api/services/app/yXTDemand/CreateYXTDemandAsync";
	//图片上传
	public final static String PICTURE_UPLOADING=JMZG+"/api/services/app/yXTPng/CreateYXTPngForUrl";
	//市场查询
	public final static String BAZAAR_QUERY=JMZG+"/api/services/app/yXTMarket/GetYXTMarketsForStringAsync";
	//运费查询
	public final static String YUN_FEI_QUERY=JMZG+"/api/services/app/yXTFreightbase/GetListYXTFreightbaseByIdAsync";
	//运费修改
	public final static String YUN_FEI_XIU_GAI=JMZG+"/api/services/app/yXTFreightbase/UpdateYXTFreightbaseAsync";
	//运费添加
	public final static String YUN_FEI_ADD=JMZG+"/api/services/app/yXTFreightbase/CreateYXTFreightbaseAsync";
	//运费删除
	public final static String YUN_FEI_DELETE=JMZG+"/api/services/app/yXTFreightbase/DeleteYXTFreightbaseAsync";
	//修改密码
	public final static String CHANGEPASSWORD=JMZG+"/api/services/app/register/ChangePassword";
	/** 登陆*/
	public final static String ACCOUNT_LOGIN =JMZG+"/api/Account";
//				"/api/services/app/login/Authenticate"
	/**  注册*/
	public final static String ACCOUNT_REGISTER=JMZG+"/api/services/app/register/Register";
	/** 新增收货地址*/
	public final static String ADD_ADDRESS = JMZG+"/api/services/app/yXTReceivingaddress/CreateYXTReceivingaddressAsync";
	/**新增或者修改收货地址*/
	public final static String Updata_ADDRESS=JMZG+"/api/services/app/yXTReceivingaddress/CreateOrUpdateYXTReceivingaddressAsync";
	/** 修改收货地址*/
	public final static String EDIT_ADDRESS = JMZG+"/api/services/app/yXTReceivingaddress/UpdateYXTReceivingaddressAsync";
	// 地址查询
	public final static String QUERY_ADDRESS=JMZG+"/api/services/app/yXTReceivingaddress/GetYXTReceivingaddressByUserIdAsync";
	//地址删除
	public final static String DELETE_ADDRESS=JMZG+"/api/services/app/yXTReceivingaddress/DeleteYXTReceivingaddressAsync";
	//匹配药品名
	public static final String YAOPIN_QUERY=JMZG+"/api/services/app/yXTMerchandise/GetYXTMerchandisesAsync";
	//产源地索引查询
	public static final String ORIGIN_QUERY=JMZG+"/api/services/app/yXTOrigin/GetYXTOriginsAsync";
	//申请商户查询
	public static final String SHENGQINGSHOPPING_QUERY=JMZG+"/api/services/app/yXTReview/GetListYXTReviewByUserIdAsync";
	//申请商户添加
	public static final String SHENGQINGSHOPPING_ADD=JMZG+"/api/services/app/yXTReview/CreateYXTReviewAsync";
	//申请商户修改
	public static final String SHENGQINGSHOPPING_EDIT=JMZG+"/api/services/app/yXTReview/UpdateYXTReviewAsync";
	//申请商户删除
	public static final String SHENGQINGSHOPPING_DELETE=JMZG+"/api/services/app/yXTReview/DeleteYXTReviewAsync";
	//商品详情
	public static final String SHOPPINGDETAILS_QUERY=JMZG+"/api/services/app/yXTSupply/GetYXTSupplyByIdAsync";
	//热销产品
	public static final String HOT_QUERY=JMZG+"/api/services/app/yXTSupply/GetHotYXTSupplysAsync";
	//低价资源
	public static final String KEENPRICE_QUERY=JMZG+"/api/services/app/yXTSupply/GetKeenPriceYXTSupplysAsync";
	//最新上线
	public static final String NEW_QUERY=JMZG+"/api/services/app/yXTSupply/GetNewUpYXTSupplysAsync";
	//求购查询
	public static final String QIUGOU_QUERY=JMZG+"/api/services/app/yXTDemand/GetListYXTDemandForIndexAsync";
	//我的发布  供
	public static final String GONG_QUERY=JMZG+"/api/services/app/yXTSupply/GetYXTSupplyByUserIdAsync";
	//我的发布  供 修改
	public static final String GONG_EDIT=JMZG+"/api/services/app/yXTSupply/UpdateYXTSupplyAsync";
	//我的发布  供 删除
	public static final String GONG_DELETE=JMZG+"/api/services/app/yXTSupply/DeleteYXTSupply";
	//我的发布 求
	public static final String QIU_QUERY=JMZG+"/api/services/app/yXTDemand/GetYXTDemandByUserIdAsync";
	//我的发布 求 修改
	public static final String QIU_EDIT=JMZG+"/api/services/app/yXTDemand/UpdateYXTDemandAsync";
	//我的发布 求 删除
	public static final String QIU_DELETE=JMZG+"/api/services/app/yXTDemand/DeleteYXTDemandAsync";
	//急购特供查询
	public static final String URGENT_QUERY=JMZG+"/api/services/app/yXTSupply/GetUrgentYXTSupplysAsync";
	//求购的产品详情
	public static final String QIUGOU_XIANGQING_QUERY=JMZG+"/api/services/app/yXTDemand/GetYXTDemandByIdAsync";
	//购物车添加
	public static final String SHOPPING_ADD=JMZG+"/api/services/app/yXTCarts/CreateYXTCartsAsync";
	//购物车查询
	public static final String SHOPPING_QUERY=JMZG+"/api/services/app/yXTCarts/GetYXTCartsByUserIdAsync";
	//购物车单个删除
	public static final String SHOPPING_DELETE=JMZG+"/api/services/app/yXTCarts/DeleteYXTCartsAsync";
	//购物车全选删除
	public static final String BATCH_DELETE=JMZG+"/api/services/app/yXTCarts/BatchDeleteYXTCartsAsync";
	//供 搜索
	public static final String GONG_SEARCH=JMZG+"/api/services/app/yXTSupply/GetPagedYXTSupplysAsync";
	//求  搜索
	public static final String QIU_SEARCH=JMZG+"/api/services/app/yXTDemand/GetPagedYXTDemandsAsync";
	//购物车的提交订单
	public static  final String SUBMIT_ORDER_ADD=JMZG+"/api/services/app/yXTOrder/CreateYXTOrderForAppAsync";
	//立即购买的提交订单
	public static  final String AT_ONCE_ORDER_ADD=JMZG+"/api/services/app/yXTOrder/CreateYXTOrderForByNowAsync";
	//订单查询
	public  static  final  String ORDER_QUERY=JMZG+"/api/services/app/yXTOrder/GetYXTOrderByUserIdAsync";
	//确认订单查询
	public  static  final  String QUEREN_ORDER_QUERY=JMZG+"/api/services/app/yXTOrder/YXTOrderForAppAsync";


}
