package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;

public class OpPatiOvervieVo {
	
    private String pkPv;//   --就诊主键
    private String problem; // --主诉
    private String present; // --现病史
    private String history; // --既往史
    private String allergy; // --过敏史
    private String height; //  --身高
    private String weight; //  --体重
    private String temperature; // --体温
    private String sbp; //     --伸缩压
    private String dbp; //     --舒张压
    private String examPhy; //--体格检查
    private String examAux; //--辅助检查
    private String note ;   // --备注
    private String descDiag;//诊断信息
    private List<ExLabOcc> labOccVos ;
    private List<ExRisOcc> risOccVos ;
    private List<CnOrder> ordVos ;
    private List<CnOrder> rptVos ;
    
    
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getProblem() {
		return problem;
	}
	public void setProblem(String problem) {
		this.problem = problem;
	}
	public String getPresent() {
		return present;
	}
	public void setPresent(String present) {
		this.present = present;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}
	public String getAllergy() {
		return allergy;
	}
	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getSbp() {
		return sbp;
	}
	public void setSbp(String sbp) {
		this.sbp = sbp;
	}
	public String getDbp() {
		return dbp;
	}
	public void setDbp(String dbp) {
		this.dbp = dbp;
	}
	public String getExamPhy() {
		return examPhy;
	}
	public void setExamPhy(String examPhy) {
		this.examPhy = examPhy;
	}
	public String getExamAux() {
		return examAux;
	}
	public void setExamAux(String examAux) {
		this.examAux = examAux;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<ExLabOcc> getLabOccVos() {
		return labOccVos;
	}
	public void setLabOccVos(List<ExLabOcc> labOccVos) {
		this.labOccVos = labOccVos;
	}
	public List<ExRisOcc> getRisOccVos() {
		return risOccVos;
	}
	public void setRisOccVos(List<ExRisOcc> risOccVos) {
		this.risOccVos = risOccVos;
	}
	public List<CnOrder> getOrdVos() {
		return ordVos;
	}
	public void setOrdVos(List<CnOrder> ordVos) {
		this.ordVos = ordVos;
	}
	public List<CnOrder> getRptVos() {
		return rptVos;
	}
	public void setRptVos(List<CnOrder> rptVos) {
		this.rptVos = rptVos;
	}

}
