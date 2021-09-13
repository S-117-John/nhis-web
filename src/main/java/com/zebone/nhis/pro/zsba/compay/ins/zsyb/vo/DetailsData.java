package com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo;

import java.util.List;

/**
 * 医保预结算所需数据
 * @author zrj
 *
 */
public class DetailsData {

	private String codeIp; //住院号
	
	private String jzjlh; //就诊记录号
	
	//2078入参
	private String jzrq;// 就诊日期
	
	private String totalCost;//总费用
	
	private String grsxh;//参保号
	private String ybzhye;//个人账户余额
	private String nd; //年度
	private String rylb; //人员类别
	private String kh; //社保卡号
	private String jym;//校验码
	private String psam;//psam卡号
	
	private List<ChargeDetailsData> chargeDetailsDataList ;//住院收费明细
	
	private String errorMsg; //错误信息，不为null就是有问题

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getJzjlh() {
		return jzjlh;
	}

	public void setJzjlh(String jzjlh) {
		this.jzjlh = jzjlh;
	}

	public String getJzrq() {
		return jzrq;
	}

	public void setJzrq(String jzrq) {
		this.jzrq = jzrq;
	}

	public String getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(String totalCost) {
		this.totalCost = totalCost;
	}

	public String getGrsxh() {
		return grsxh;
	}

	public void setGrsxh(String grsxh) {
		this.grsxh = grsxh;
	}

	public String getYbzhye() {
		return ybzhye;
	}

	public void setYbzhye(String ybzhye) {
		this.ybzhye = ybzhye;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String getRylb() {
		return rylb;
	}

	public void setRylb(String rylb) {
		this.rylb = rylb;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getJym() {
		return jym;
	}

	public void setJym(String jym) {
		this.jym = jym;
	}

	public String getPsam() {
		return psam;
	}

	public void setPsam(String psam) {
		this.psam = psam;
	}

	public List<ChargeDetailsData> getChargeDetailsDataList() {
		return chargeDetailsDataList;
	}

	public void setChargeDetailsDataList(
			List<ChargeDetailsData> chargeDetailsDataList) {
		this.chargeDetailsDataList = chargeDetailsDataList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}


}
