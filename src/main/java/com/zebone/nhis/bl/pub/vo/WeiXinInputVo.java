package com.zebone.nhis.bl.pub.vo;

/**
 * 微信输入
 * @author Administrator
 *
 */
public class WeiXinInputVo extends PayInputVo{

	private String detail;          //商品详情          非必填
	private String attach;          //附加数据          非必填
	private  int totalFee;	         //订单金额           必填   单位是分，只能是整数
	private String feeType;         //货币类型          非必填 CNY
	private String spbillCreateIp;  //终端ip    必填
	private String goodsTag;        //订单优惠标记  非必填
	private String limitPay;        //指定支付方式  非必填
	private String sceneInfo;       //场景信息          非必填
	private String deviceInfo;      //设备号               必填
	private int refundFee;          //微信退款         退款时必填
	private String outRefundNo;     //退款单号          退款时必填
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getGoodsTag() {
		return goodsTag;
	}
	public void setGoodsTag(String goodsTag) {
		this.goodsTag = goodsTag;
	}
	public String getLimitPay() {
		return limitPay;
	}
	public void setLimitPay(String limitPay) {
		this.limitPay = limitPay;
	}
	public String getSceneInfo() {
		return sceneInfo;
	}
	public void setSceneInfo(String sceneInfo) {
		this.sceneInfo = sceneInfo;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public int getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(int refundFee) {
		this.refundFee = refundFee;
	}
	public String getOutRefundNo() {
		return outRefundNo;
	}
	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}
}
