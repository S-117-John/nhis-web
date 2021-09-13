package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpInfect;

public class OpPressVo extends CnOpApply {
	/**
	 * 感染
	 */
	private List<CnOpInfect> infectOpList = new ArrayList<CnOpInfect>();
	/**
	 *手术级别--手术本来默认得级别，非医生选择得级别
	 */
	private String opLevel;

	private String codeIcd; //术前诊断编码

	private String diagCode; //手术编码

	private String nameEmpInput;//录入医生

	private String pkEmpInput;//录入医生编码

	public String getOpLevel() {
		return opLevel;
	}

	public void setOpLevel(String opLevel) {
		this.opLevel = opLevel;
	}

	public List<CnOpInfect> getInfectOpList() {
		return infectOpList;
	}
	public void setInfectOpList(List<CnOpInfect> infectOpList) {
		this.infectOpList = infectOpList;
	}

	public String getCodeIcd() {
		return codeIcd;
	}

	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getNameEmpInput() {
		return nameEmpInput;
	}

	public void setNameEmpInput(String nameEmpInput) {
		this.nameEmpInput = nameEmpInput;
	}

	public String getPkEmpInput() {
		return pkEmpInput;
	}

	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}
}
