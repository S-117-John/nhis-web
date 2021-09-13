package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.emr.rec.rec.*;
import com.zebone.platform.modules.utils.JsonUtil;

import java.util.List;

public class EmrParam {

    private EmrHomePage emrHomePage;
    private List<EmrHomePageTrans> emrHomePageTrans;
    private List<EmrHomePageDiags> emrHomePageDiags;
    private List<EmrHomePageOps> emrHomePageOps;
    private List<EmrHomePageCharges> emrHomePageCharges;
    private List<EmrHomePageBr> emrHomePageBr;
    private EmrHomePageOr emrHomePageOr;
    private List<EmrHomePageOrDt> emrHomePageOrDt;
    //病情转归
  	private String dtOutcomes;
    //病情转归显示标志,1 显示; 0或其它 不显示
  	private String dtOutcomesShowFlag;

    public EmrHomePage getEmrHomePage() {
        return emrHomePage;
    }

    public void setEmrHomePage(EmrHomePage emrHomePage) {
        this.emrHomePage = emrHomePage;
    }

    public List<EmrHomePageTrans> getEmrHomePageTrans() {
        return emrHomePageTrans;
    }

    public void setEmrHomePageTrans(List<EmrHomePageTrans> emrHomePageTrans) {
        this.emrHomePageTrans = emrHomePageTrans;
    }

    public List<EmrHomePageDiags> getEmrHomePageDiags() {
        return emrHomePageDiags;
    }

    public void setEmrHomePageDiags(List<EmrHomePageDiags> emrHomePageDiags) {
        this.emrHomePageDiags = emrHomePageDiags;
    }

    public List<EmrHomePageOps> getEmrHomePageOps() {
        return emrHomePageOps;
    }

    public void setEmrHomePageOps(List<EmrHomePageOps> emrHomePageOps) {
        this.emrHomePageOps = emrHomePageOps;
    }

    public List<EmrHomePageCharges> getEmrHomePageCharges() {
        return emrHomePageCharges;
    }

    public void setEmrHomePageCharges(List<EmrHomePageCharges> emrHomePageCharges) {
        this.emrHomePageCharges = emrHomePageCharges;
    }

    public List<EmrHomePageBr> getEmrHomePageBr() {
        return emrHomePageBr;
    }

    public void setEmrHomePageBr(List<EmrHomePageBr> emrHomePageBr) {
        this.emrHomePageBr = emrHomePageBr;
    }

    public EmrHomePageOr getEmrHomePageOr() {
        return emrHomePageOr;
    }

    public void setEmrHomePageOr(EmrHomePageOr emrHomePageOr) {
        this.emrHomePageOr = emrHomePageOr;
    }

    public List<EmrHomePageOrDt> getEmrHomePageOrDt() {
        return emrHomePageOrDt;
    }

    public void setEmrHomePageOrDt(List<EmrHomePageOrDt> emrHomePageOrDt) {
        this.emrHomePageOrDt = emrHomePageOrDt;
    }

	public String getDtOutcomes() {
		return dtOutcomes;
	}

	public void setDtOutcomes(String dtOutcomes) {
		this.dtOutcomes = dtOutcomes;
	}

	public String getDtOutcomesShowFlag() {
		return dtOutcomesShowFlag;
	}

	public void setDtOutcomesShowFlag(String dtOutcomesShowFlag) {
		this.dtOutcomesShowFlag = dtOutcomesShowFlag;
	}
}
