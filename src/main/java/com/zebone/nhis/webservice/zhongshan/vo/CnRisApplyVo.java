package com.zebone.nhis.webservice.zhongshan.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;

public class CnRisApplyVo extends CnRisApply {

	/**申请单号*/
	private String codeApp;
	
	/**医嘱号*/
	private String ordsnNo;
	
	/**父医嘱号*/
	private String ordsnParentNo;
	
	/**医嘱状态*/
	private String euStatusOrd;
	
	/**医嘱名称*/
	private String ordName;
	
	/**患者编码*/
	private String codePi;
	
	/**住院号/门诊号*/
	private String code;
	
	/**住院次数/门诊次数*/
	private String times;
	
	/**住院号*/
	private String codeIp;
	
	/**门诊号*/
	private String codeOp;
	
	/**住院次数*/
	private String ipTimes;

	/**门诊次数*/
	private String opTimes;
	
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getOpTimes() {
		return opTimes;
	}

	public void setOpTimes(String opTimes) {
		this.opTimes = opTimes;
	}

	public String getCodeApp() {
		return codeApp;
	}

	public void setCodeApp(String codeApp) {
		this.codeApp = codeApp;
	}

	public String getOrdsnNo() {
		return ordsnNo;
	}

	public void setOrdsnNo(String ordsnNo) {
		this.ordsnNo = ordsnNo;
	}

	public String getOrdsnParentNo() {
		return ordsnParentNo;
	}

	public void setOrdsnParentNo(String ordsnParentNo) {
		this.ordsnParentNo = ordsnParentNo;
	}

	public String getEuStatusOrd() {
		return euStatusOrd;
	}

	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}

	public String getOrdName() {
		return ordName;
	}

	public void setOrdName(String ordName) {
		this.ordName = ordName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

}
