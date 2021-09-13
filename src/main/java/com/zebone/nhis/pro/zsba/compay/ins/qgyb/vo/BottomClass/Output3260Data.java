package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Output3260Data {
	
	private String	seqno;//	Y	顺序号	
	private String	mdtrtarea;//	Y	就医地医保区划	地市编码
	private String	medins_no;//	Y	医疗机构编号	按照省统一编码
	private String	certno;//	Y	证件号码	
	private String	mdtrt_id;//	Y	就诊登记号	就诊业务标识’S’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位）
	private String	mdtrt_setl_time;//	Y	就诊结算时间	格式：yyyy-MM-dd HH:mm:ss
	private String	setl_sn;//	Y	结算流水号	费用结算交易的返回的结算业务号
	private String	fulamt_advpay_flag;//	Y	全额垫付标志	结算业务标识’A’+参保地统筹地市区编 号(6 位)＋ 日期（6 位YYMMDD）＋流水号 （7 位）
	private String	medfee_sumamt;//	Y	总费用	0-医院报销，1-零星报销
	private String	optins_pay_sumamt;//	Y	经办机构支付总额	
	public String getSeqno() {
		return seqno;
	}
	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}
	public String getMdtrtarea() {
		return mdtrtarea;
	}
	public void setMdtrtarea(String mdtrtarea) {
		this.mdtrtarea = mdtrtarea;
	}
	public String getMedins_no() {
		return medins_no;
	}
	public void setMedins_no(String medins_no) {
		this.medins_no = medins_no;
	}
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
	public String getFulamt_advpay_flag() {
		return fulamt_advpay_flag;
	}
	public void setFulamt_advpay_flag(String fulamt_advpay_flag) {
		this.fulamt_advpay_flag = fulamt_advpay_flag;
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


	
}
