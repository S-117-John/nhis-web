package com.zebone.nhis.webservice.zhongshan.vo;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

public class ZsbaOutLabVo {
	/**
	 * 操作类型：
	 * 0,条码打印；1-标本采集；2-标本送出；3-标本接收；4-报告；5-取消标本送出；
	 * 6-取消采集；7-标本退回；8,取消条码打印；9-上机；10-报告召回。
	 */
	@NotBlank(message = "操作类型不能为空")
	private String opeType;
	/**
	 * 操作人编码
	 */
	@NotBlank(message = "操作人编码不能为空")
	private String codeEmp;
	/**
	 * 操作时间,yyyy-mm-dd hh24:mi:ss
	 */
	@NotBlank(message = "操作时间不能为空")
	private String timeOpe;
	/**
	 * 条码编号
	 */
	private String codeBar;	
	/**
	 * 医嘱序号
	 */
	private List<String> ordlist;
    /**
     * 执行类型：
     * 0，不执行；1，医技执行；6，撤销执行；9，取消医技执行。
     */
	private String execType;
	/**
	 * 收费项目编码
	 */
	private String codeItem;
	/**
	 * 收费项目的数量
	 */
	private String quan;
	public String getOpeType() {
		return opeType;
	}
	public void setOpeType(String opeType) {
		this.opeType = opeType;
	}
	public String getCodeEmp() {
		return codeEmp;
	}
	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}
	public String getTimeOpe() {
		return timeOpe;
	}
	public void setTimeOpe(String timeOpe) {
		this.timeOpe = timeOpe;
	}
	public String getCodeBar() {
		return codeBar;
	}
	public void setCodeBar(String codeBar) {
		this.codeBar = codeBar;
	}
	public List<String> getOrdlist() {
		return ordlist;
	}
	public void setOrdlist(List<String> ordlist) {
		this.ordlist = ordlist;
	}
	public String getExecType() {
		return execType;
	}
	public void setExecType(String execType) {
		this.execType = execType;
	}
	public String getCodeItem() {
		return codeItem;
	}
	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}
	public String getQuan() {
		return quan;
	}
	public void setQuan(String quan) {
		this.quan = quan;
	}
}
