package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

/**
 * @author Administrator
 *
 */
public class CnConsultApplyVO {
	private List<CnConsultResponseVO> resps;
	private CnOrder order;
    private String pkCons;
    private String pkCnord;
    private String pkDept;
    private String pkDeptNs;   
	private Date dateApply;
    private String euType;
    private String dtConlevel;
    private String euStatus;
    private String reason;
    private String illSummary;
    private String modifier;
    private Date modityTime;
    private String pkOrg;
    private Date dateStart;
    private String pkCprec;
    private String pkCpexp;
    private String expNote;
    private Date ts;
    private Date dateCons;
    private String examine;
    private String diagname;
    private String objective;
    
    public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getPkCons(){
        return this.pkCons;
    }
    public void setPkCons(String pkCons){
        this.pkCons = pkCons;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getDtConlevel(){
        return this.dtConlevel;
    }
    public void setDtConlevel(String dtConlevel){
        this.dtConlevel = dtConlevel;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getReason(){
        return this.reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }

    public String getIllSummary(){
        return this.illSummary;
    }
    public void setIllSummary(String illSummary){
        this.illSummary = illSummary;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    
    public CnOrder getOrder() {
		return order;
	}
	public void setOrder(CnOrder order) {
		this.order = order;
	}
	public List<CnConsultResponseVO> getResps() {
		return resps;
	}
	public void setResps(List<CnConsultResponseVO> resps) {
		this.resps = resps;
	}
	public String getPkCprec() {
		return pkCprec;
	}
	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
	}
	public String getPkCpexp() {
		return pkCpexp;
	}
	public void setPkCpexp(String pkCpexp) {
		this.pkCpexp = pkCpexp;
	}
	public String getExpNote() {
		return expNote;
	}
	public void setExpNote(String expNote) {
		this.expNote = expNote;
	}
	public Date getDateCons() {
		return dateCons;
	}
	public void setDateCons(Date dateCons) {
		this.dateCons = dateCons;
	}
	public String getExamine() {
		return examine;
	}
	public void setExamine(String examine) {
		this.examine = examine;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	
}
