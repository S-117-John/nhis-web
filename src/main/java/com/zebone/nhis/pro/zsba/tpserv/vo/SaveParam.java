package com.zebone.nhis.pro.zsba.tpserv.vo;


/**
 * 项目出租信息&&银联交易记录表<br>
 * <pre>
 * 1 项目出租信息
 * 2 银联交易记录
 * </pre>
 *  
 * @author zhengrj
 * @date 2017年7月14日
 *
 */
public class SaveParam {
	
	/** 项目出租信息 */
	private TpServItemRent servItemRent;
	
	/** 项目出租结算信息 */
	private TpServItemRent rentSettlement;
	
	/** 银联交易记录 */
	private TpServUnionpayTrading servUnionpayTrading;
	
	/** 支付方式 */
	private String payMethod;
	
	

	public TpServItemRent getServItemRent() {
		return servItemRent;
	}

	public void setServItemRent(TpServItemRent servItemRent) {
		this.servItemRent = servItemRent;
	}
	
	public TpServItemRent getRentSettlement() {
		return rentSettlement;
	}

	public void setRentSettlement(TpServItemRent rentSettlement) {
		this.rentSettlement = rentSettlement;
	}

	public TpServUnionpayTrading getServUnionpayTrading() {
		return servUnionpayTrading;
	}

	public void setServUnionpayTrading(TpServUnionpayTrading servUnionpayTrading) {
		this.servUnionpayTrading = servUnionpayTrading;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}
	
	

}
