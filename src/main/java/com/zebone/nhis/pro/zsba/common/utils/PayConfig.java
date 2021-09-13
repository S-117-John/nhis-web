package com.zebone.nhis.pro.zsba.common.utils;

public class PayConfig {
	
	/* 微信-中转服务接口地址 */
	public static String wechat_qrcode_url 		= "/itf/nhis/pay/getWechatQrcode";		//微信预下单
	public static String wechat_barcode_url		= "/itf/nhis/pay/wechatBarCodePay";		//微信条码支付
	public static String wechat_refund_url		= "/itf/nhis/pay/wechatRefund";			//微信退款
	public static String wechat_order_query		= "/itf/nhis/pay/wechatOrderQuery";		//微信订单主动查询
	
	/* 支付宝-中转服务接口地址 */
	public static String ali_qrcode_url			= "/itf/nhis/pay/getAliQrcode";			//支付宝预下单
	public static String ali_barcode_url		= "/itf/nhis/pay/aliBarCodePay";		//支付宝条码支付
	public static String ali_refund_url			= "/itf/nhis/pay/aliRefund";			//支付宝退款
	public static String ali_order_query		= "/itf/nhis/pay/aliOrderQuery";		//支付宝订单主动查询
	public static String ali_applets_order_query		= "/itf/nhis/pay/aliAppletsOrderQuery";		//支付宝小程序订单主动查询
	public static String ali_applets_refund_url			= "/itf/nhis/pay/aliAppletsRefund";			//支付宝小程序退款

	/* 非医疗费用-中转服务接口地址 */
	public static String wx_push_pay_url		= "/itf/nhis/nmpay/sendPushWx";			//发送通知患者付款
	public static String wx_push_notify_url		= "/itf/nhis/nmpay/payNotify";			//中转通知回调服务方法
	
	
	
	public static int timeout_express			= 5; 									//订单超时时间，单位分钟
	public static int queryNum 					= 50;									//主动查询次数 6秒一次、10次一分钟
	
	public static String system_module_nm		= "nhis-nm";
	
	public static String PAY_METHOD_WECHAT 		= "W";
	public static String PAY_METHOD_ALI 		= "A";
	
	private static String wxCode 				= ",10,11,12,13,14,15,";
	private static String aliCode 				= ",25,26,27,28,29,30,";
	
	public static String getPayMethod(String authCode){
		String method = "";
		String prefix = ","+authCode.substring(0,2)+",";
		if(wxCode.indexOf(prefix)>-1){
			method = PAY_METHOD_WECHAT;
		}else if(aliCode.indexOf(prefix)>-1){
			method = PAY_METHOD_ALI;
		}
		return method;
	}

}
