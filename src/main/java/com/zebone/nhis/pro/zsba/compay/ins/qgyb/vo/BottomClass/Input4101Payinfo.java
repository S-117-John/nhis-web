package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 医疗保障基金结算清单信息上传入参——基金支付信息
 * @author Administrator
 *
 */
public class Input4101Payinfo {
	
	private String	fund_pay_type;//	Y	基金支付类型
	private String	fund_payamt;//	Y	基金支付金额
	public String getFund_pay_type() {
		return fund_pay_type;
	}
	public void setFund_pay_type(String fund_pay_type) {
		this.fund_pay_type = fund_pay_type;
	}
	public String getFund_payamt() {
		return fund_payamt;
	}
	public void setFund_payamt(String fund_payamt) {
		this.fund_payamt = fund_payamt;
	}

	
}
