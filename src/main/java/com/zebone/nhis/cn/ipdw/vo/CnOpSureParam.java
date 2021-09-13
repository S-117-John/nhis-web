package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;

public class CnOpSureParam extends BlPubParamVo{

	private static final long serialVersionUID = 1L;
    private String codeFreq;
    private Double quan;
    private Integer days;
    private String pkEmpInput;
    private String nameEmpInput;
    
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getCodeFreq() {
		return codeFreq;
	}
	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public String getPkEmpInput() {
		return pkEmpInput;
	}
	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}
	public String getNameEmpInput() {
		return nameEmpInput;
	}
	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
	}
	
	
}
