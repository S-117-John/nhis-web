package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

import com.zebone.nhis.common.module.cn.ipdw.CnTransApply;

public class InpatientVo {

	private String pkIprep; 		//主键；
	private String pkOrg; 	 		//当前机构；
	private String pkPv;			//患者就诊主键；
	private String pkDiag;			//患者当前主诊断；
	private String flagHp; 		//医患纠纷标识；
	private String flagPat; 		//患者纠纷标识；
	private String flagDise;		//病情正常标识；
	private String flagCompDise;	//疾病并发症标识；
	private String flagRej;		//拒绝出院标识；
	private String flagCompMed;	//医疗并发症标识；
	private String flagInf;		//院内感染标识；
	private String flagDelay;		//服务流程延迟标识；
	private String flagOver;		//过度医疗标识；
	private String flagOth;		//其他标识；
	private String descOth;		//其他因素描述；
	private String descSoc;		//社会因素描述；
	private String descAdv;		//后续治疗方案描述；
	private String pkEmpRep;		//上报人主键；
	private String nameEmpRep;	//上报人姓名；
	private String pkDept;			//上报科室；
	private Date dateRep;			//上报日期。
	private CnTransApply InpatienApply;
	
	
	
	public CnTransApply getInpatienApply() {
		return InpatienApply;
	}
	public void setInpatienApply(CnTransApply inpatienApply) {
		InpatienApply = inpatienApply;
	}
	public String getPkIprep() {
		return pkIprep;
	}
	public void setPkIprep(String pkIprep) {
		this.pkIprep = pkIprep;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getFlagHp() {
		return flagHp;
	}
	public void setFlagHp(String flagHp) {
		this.flagHp = flagHp;
	}
	public String getFlagPat() {
		return flagPat;
	}
	public void setFlagPat(String flagPat) {
		this.flagPat = flagPat;
	}
	public String getFlagDise() {
		return flagDise;
	}
	public void setFlagDise(String flagDise) {
		this.flagDise = flagDise;
	}
	public String getFlagCompDise() {
		return flagCompDise;
	}
	public void setFlagCompDise(String flagCompDise) {
		this.flagCompDise = flagCompDise;
	}
	public String getFlagRej() {
		return flagRej;
	}
	public void setFlagRej(String flagRej) {
		this.flagRej = flagRej;
	}
	public String getFlagCompMed() {
		return flagCompMed;
	}
	public void setFlagCompMed(String flagCompMed) {
		this.flagCompMed = flagCompMed;
	}
	public String getFlagInf() {
		return flagInf;
	}
	public void setFlagInf(String flagInf) {
		this.flagInf = flagInf;
	}
	public String getFlagDelay() {
		return flagDelay;
	}
	public void setFlagDelay(String flagDelay) {
		this.flagDelay = flagDelay;
	}
	public String getFlagOver() {
		return flagOver;
	}
	public void setFlagOver(String flagOver) {
		this.flagOver = flagOver;
	}
	public String getFlagOth() {
		return flagOth;
	}
	public void setFlagOth(String flagOth) {
		this.flagOth = flagOth;
	}
	public String getDescOth() {
		return descOth;
	}
	public void setDescOth(String descOth) {
		this.descOth = descOth;
	}
	public String getDescSoc() {
		return descSoc;
	}
	public void setDescSoc(String descSoc) {
		this.descSoc = descSoc;
	}
	public String getDescAdv() {
		return descAdv;
	}
	public void setDescAdv(String descAdv) {
		this.descAdv = descAdv;
	}
	public String getPkEmpRep() {
		return pkEmpRep;
	}
	public void setPkEmpRep(String pkEmpRep) {
		this.pkEmpRep = pkEmpRep;
	}
	public String getNameEmpRep() {
		return nameEmpRep;
	}
	public void setNameEmpRep(String nameEmpRep) {
		this.nameEmpRep = nameEmpRep;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public Date getDateRep() {
		return dateRep;
	}
	public void setDateRep(Date dateRep) {
		this.dateRep = dateRep;
	}
	public Object get(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
}
