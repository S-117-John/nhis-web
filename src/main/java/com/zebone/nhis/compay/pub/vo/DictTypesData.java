package com.zebone.nhis.compay.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.compay.ins.zsba.zsyb.InsZsybDict;


/**
 * 保存下载医保字典
 */
public class DictTypesData {

	private String codeType; //"类别编码",
	private String nameType; //"类别名称",
	private String spcode; //"类别拼音码",
	private String dCode; //"类别自定义码",
	private List<InsZsybDict> dicts; //"code":"字典编码","name":"字典名称","spcode":"字典拼音码",	"dCode":"字典自定义码"
	
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getNameType() {
		return nameType;
	}
	public void setNameType(String nameType) {
		this.nameType = nameType;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getdCode() {
		return dCode;
	}
	public void setdCode(String dCode) {
		this.dCode = dCode;
	}
	public List<InsZsybDict> getDicts() {
		return dicts;
	}
	public void setDicts(List<InsZsybDict> dicts) {
		this.dicts = dicts;
	}
	
}
