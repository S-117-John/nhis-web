package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass;

public class Input1101Data {
	
	private String	mdtrt_cert_type;//	就诊凭证类型	
	private String	mdtrt_cert_no;//	就诊凭证编号	就诊凭证类型为“01”时填写电子凭证令牌，为“02”时填写身份证号，为“03”时填写社会保障卡卡号
	private String	card_sn;//	卡识别码	就诊凭证类型为“03”时必填
	private String	begntime;//	开始时间	获取历史参保信息时传入
	private String	psn_cert_type;//	人员证件类型	
	private String	certno;//	证件号码	
	private String	psn_name;//	人员姓名	
	public String getMdtrt_cert_type() {
		return mdtrt_cert_type;
	}
	public void setMdtrt_cert_type(String mdtrt_cert_type) {
		this.mdtrt_cert_type = mdtrt_cert_type;
	}
	public String getMdtrt_cert_no() {
		return mdtrt_cert_no;
	}
	public void setMdtrt_cert_no(String mdtrt_cert_no) {
		this.mdtrt_cert_no = mdtrt_cert_no;
	}
	public String getCard_sn() {
		return card_sn;
	}
	public void setCard_sn(String card_sn) {
		this.card_sn = card_sn;
	}
	public String getBegntime() {
		return begntime;
	}
	public void setBegntime(String begntime) {
		this.begntime = begntime;
	}
	public String getPsn_cert_type() {
		return psn_cert_type;
	}
	public void setPsn_cert_type(String psn_cert_type) {
		this.psn_cert_type = psn_cert_type;
	}
	public String getCertno() {
		return certno;
	}
	public void setCertno(String certno) {
		this.certno = certno;
	}
	public String getPsn_name() {
		return psn_name;
	}
	public void setPsn_name(String psn_name) {
		this.psn_name = psn_name;
	}
	
	

}
