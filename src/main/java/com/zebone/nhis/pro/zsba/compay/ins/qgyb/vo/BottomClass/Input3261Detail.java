package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

/**
 * 5.1.1.2异地清分结果确认3261 结算详情
 * @author Administrator
 *
 */
public class Input3261Detail {
	
	private String	certno;//	Y	证件号码	
	private String	mdtrt_id;//	Y	就诊登记号	就诊业务标识’S’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位）
	private String	mdtrt_setl_time;//	Y	就诊结算时间	格式：yyyy-MM-dd HH:mm:ss
	private String	setl_sn;//	Y	结算流水号	费用结算交易的返回的结算业务号结算业务标识’A’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位）
	private String	medfee_sumamt;//	Y	总费用	
	private String	optins_pay_sumamt;//	Y	经办机构支付总额	该笔结算业务的全部医保支付金额，与结算时返回的一致
	private String	cnfm_flag;//		确认标志	0-不确认，本次不纳入清分；1-确认，纳入本次清分
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
	public String getMdtrt_id() {
		return mdtrt_id;
	}
	public void setMdtrt_id(String mdtrt_id) {
		this.mdtrt_id = mdtrt_id;
	}
	public String getMdtrt_setl_time() {
		return mdtrt_setl_time;
	}
	public void setMdtrt_setl_time(String mdtrt_setl_time) {
		this.mdtrt_setl_time = mdtrt_setl_time;
	}
	public String getSetl_sn() {
		return setl_sn;
	}
	public void setSetl_sn(String setl_sn) {
		this.setl_sn = setl_sn;
	}
	public String getMedfee_sumamt() {
		return medfee_sumamt;
	}
	public void setMedfee_sumamt(String medfee_sumamt) {
		this.medfee_sumamt = medfee_sumamt;
	}
	public String getOptins_pay_sumamt() {
		return optins_pay_sumamt;
	}
	public void setOptins_pay_sumamt(String optins_pay_sumamt) {
		this.optins_pay_sumamt = optins_pay_sumamt;
	}
	public String getCnfm_flag() {
		return cnfm_flag;
	}
	public void setCnfm_flag(String cnfm_flag) {
		this.cnfm_flag = cnfm_flag;
	}


}
