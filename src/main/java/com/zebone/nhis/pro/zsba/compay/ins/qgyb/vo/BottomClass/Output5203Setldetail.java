package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 结算信息查询   结算基金分项信息
 * @author Administrator
 *
 */
public class Output5203Setldetail {
	
	private String	fund_pay_type;//		基金支付类型
	private String	inscp_scp_amt;//		符合政策范围金额
	private String	crt_payb_lmt_amt;//		本次可支付限额金额
	private String	fund_payamt;//		基金支付金额
	private String	fund_pay_type_name;//		基金支付类型名称
	private String	setl_proc_info;//		结算过程信息
	public String getFund_pay_type() {
		return fund_pay_type;
	}
	public void setFund_pay_type(String fund_pay_type) {
		this.fund_pay_type = fund_pay_type;
	}
	public String getInscp_scp_amt() {
		return inscp_scp_amt;
	}
	public void setInscp_scp_amt(String inscp_scp_amt) {
		this.inscp_scp_amt = inscp_scp_amt;
	}
	public String getCrt_payb_lmt_amt() {
		return crt_payb_lmt_amt;
	}
	public void setCrt_payb_lmt_amt(String crt_payb_lmt_amt) {
		this.crt_payb_lmt_amt = crt_payb_lmt_amt;
	}
	public String getFund_payamt() {
		return fund_payamt;
	}
	public void setFund_payamt(String fund_payamt) {
		this.fund_payamt = fund_payamt;
	}
	public String getFund_pay_type_name() {
		return fund_pay_type_name;
	}
	public void setFund_pay_type_name(String fund_pay_type_name) {
		this.fund_pay_type_name = fund_pay_type_name;
	}
	public String getSetl_proc_info() {
		return setl_proc_info;
	}
	public void setSetl_proc_info(String setl_proc_info) {
		this.setl_proc_info = setl_proc_info;
	}

	
}
