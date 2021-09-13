package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.pv.PvInfant;

public class PvInfantVo extends PvInfant{
	
	private String preWeeks; //孕周
	
    private String posFo;//胎方位
    
    private String dtOutMode;//胎儿娩出方式
    
    private String opReason;//剖宫产手术原因
    
    private String weekPreg;//宫内妊娠周数
    
    private String euInf;//胎数
    
    private Double timeEarly;//胎膜早破时间
               
	private String dtExcep;//妊娠异常情况           

    private String euHb;//乙肝性状                
    
    private String plaAbout;//胎盘完整性
    
    private int plaDefectLen;//胎盘缺损长
    
    private int plaDefectWid;//胎盘缺损宽
    
    private int plaRoughLen;//胎盘粗糙长
    
    private int plaRoughWid;//胎盘粗糙宽

    private int plaLen;//胎盘长

    private int plaWid;//胎盘宽

    private int plaHigh;//胎盘高

    private int plaWight;//胎盘重量

    private String plaOthers;//胎盘特殊描述

    private String caulAbout;//胎膜完整性
    
    private String special;//分娩特殊情况

    private int caulDefectLen;//胎膜缺损长

    private int caulDefectWid;//胎膜缺损宽

    private String caulOthers;//胎膜特殊描述

    private int umbLen;//脐带长度

    private String umbAbout;//脐带情况

    private Double umbCrossCycle;//缠绕周数

    private Double umbTurnCycle;//脐带扭转周数

    private String umbPoistion;//脐带假结位置

    private String umbOthers;//脐带其他情况

    private String umbAdh;//脐带附着情况

    private String amFluidAfter;//后羊水性状
    
    private String amFluidOther;//后羊水其他性状

    private int amFluidAnum;//后其他性状出水量

    private int amFluidAll;//总羊水量

    private Date dateBeginSx;//早吸吮开始时间

    private Date dateEndSx;//早吸吮结束时间

    private String outAbout;//出生时情况

    //private String dtBreath;//呼吸

    private String dtAsp;//新生儿窒息

    private String otherFlag;//畸形和特殊标志

    private String isPm;//产瘤/血肿

    private String pmPart;//产瘤/血肿位置

    private int pmLen;//产瘤/血肿长

    private int pmWid;//产瘤/血肿宽

    private int pmHigh;//产瘤/血肿高
    
    private String pkEmpHy; // 婴护者
    
    private String pkPiCate;//患者分类
    
    private BabyBedVO babyBed;//床位信息
    
    private Date dateBegin;//入院日期
        
    private String hpcode;//医保类型

	private String flagDept;//母亲是否在产科

	private String isSavePvLabor;//婴儿信息是否保存pv_labor表   1：是；其他：否
    
    /////////////以下是婴儿母亲的就诊信息/////////////////////
    
    public String getSpecial(){
        return this.special;
    }
    public void setSpecial(String special){
        this.special = special;
    }

	public String getWeekPreg() {
		return weekPreg;
	}

	public String getOpReason() {
		return opReason;
	}

	public void setOpReason(String opReason) {
		this.opReason = opReason;
	}

	public String getEuHb() {
		return euHb;
	}

	public void setEuHb(String euHb) {
		this.euHb = euHb;
	}

	public void setWeekPreg(String weekPreg) {
		this.weekPreg = weekPreg;
	}

	public String getEuInf() {
		return euInf;
	}

	public void setEuInf(String euInf) {
		this.euInf = euInf;
	}

	public Double getTimeEarly() {
		return timeEarly;
	}

	public void setTimeEarly(Double timeEarly) {
		this.timeEarly = timeEarly;
	}

	public String getDtExcep() {
		return dtExcep;
	}

	public void setDtExcep(String dtExcep) {
		this.dtExcep = dtExcep;
	}

	public String getAmFluidOther() {
		return amFluidOther;
	}

	public String getPkPiCate() {
		return pkPiCate;
	}

	public void setPkPiCate(String pkPiCate) {
		this.pkPiCate = pkPiCate;
	}

	public void setAmFluidOther(String amFluidOther) {
		this.amFluidOther = amFluidOther;
	}

	public String getPreWeeks() {
		return preWeeks;
	}

	public void setPreWeeks(String preWeeks) {
		this.preWeeks = preWeeks;
	}

	public String getPkEmpHy() {
		return pkEmpHy;
	}

	public void setPkEmpHy(String pkEmpHy) {
		this.pkEmpHy = pkEmpHy;
	}

	public String getPosFo() {
		return posFo;
	}

	public void setPosFo(String posFo) {
		this.posFo = posFo;
	}

	public String getPlaAbout() {
		return plaAbout;
	}

	public void setPlaAbout(String plaAbout) {
		this.plaAbout = plaAbout;
	}

	public int getPlaDefectLen() {
		return plaDefectLen;
	}

	public void setPlaDefectLen(int plaDefectLen) {
		this.plaDefectLen = plaDefectLen;
	}

	public int getPlaDefectWid() {
		return plaDefectWid;
	}

	public void setPlaDefectWid(int plaDefectWid) {
		this.plaDefectWid = plaDefectWid;
	}

	public int getPlaRoughLen() {
		return plaRoughLen;
	}

	public void setPlaRoughLen(int plaRoughLen) {
		this.plaRoughLen = plaRoughLen;
	}

	public int getPlaRoughWid() {
		return plaRoughWid;
	}

	public void setPlaRoughWid(int plaRoughWid) {
		this.plaRoughWid = plaRoughWid;
	}

	public int getPlaLen() {
		return plaLen;
	}

	public void setPlaLen(int plaLen) {
		this.plaLen = plaLen;
	}

	public int getPlaWid() {
		return plaWid;
	}

	public void setPlaWid(int plaWid) {
		this.plaWid = plaWid;
	}

	public int getPlaHigh() {
		return plaHigh;
	}

	public void setPlaHigh(int plaHigh) {
		this.plaHigh = plaHigh;
	}

	public int getPlaWight() {
		return plaWight;
	}

	public void setPlaWight(int plaWight) {
		this.plaWight = plaWight;
	}

	public String getPlaOthers() {
		return plaOthers;
	}

	public void setPlaOthers(String plaOthers) {
		this.plaOthers = plaOthers;
	}

	public String getCaulAbout() {
		return caulAbout;
	}

	public void setCaulAbout(String caulAbout) {
		this.caulAbout = caulAbout;
	}

	public int getCaulDefectLen() {
		return caulDefectLen;
	}

	public void setCaulDefectLen(int caulDefectLen) {
		this.caulDefectLen = caulDefectLen;
	}

	public int getCaulDefectWid() {
		return caulDefectWid;
	}

	public void setCaulDefectWid(int caulDefectWid) {
		this.caulDefectWid = caulDefectWid;
	}

	public String getCaulOthers() {
		return caulOthers;
	}

	public void setCaulOthers(String caulOthers) {
		this.caulOthers = caulOthers;
	}

	public int getUmbLen() {
		return umbLen;
	}

	public void setUmbLen(int umbLen) {
		this.umbLen = umbLen;
	}

	public String getUmbAbout() {
		return umbAbout;
	}

	public void setUmbAbout(String umbAbout) {
		this.umbAbout = umbAbout;
	}

	public Double getUmbCrossCycle() {
		return umbCrossCycle;
	}

	public void setUmbCrossCycle(Double umbCrossCycle) {
		this.umbCrossCycle = umbCrossCycle;
	}

	public Double getUmbTurnCycle() {
		return umbTurnCycle;
	}

	public void setUmbTurnCycle(Double umbTurnCycle) {
		this.umbTurnCycle = umbTurnCycle;
	}

	public String getUmbPoistion() {
		return umbPoistion;
	}

	public void setUmbPoistion(String umbPoistion) {
		this.umbPoistion = umbPoistion;
	}

	public String getUmbOthers() {
		return umbOthers;
	}

	public void setUmbOthers(String umbOthers) {
		this.umbOthers = umbOthers;
	}

	public String getUmbAdh() {
		return umbAdh;
	}

	public void setUmbAdh(String umbAdh) {
		this.umbAdh = umbAdh;
	}

	public String getAmFluidAfter() {
		return amFluidAfter;
	}

	public void setAmFluidAfter(String amFluidAfter) {
		this.amFluidAfter = amFluidAfter;
	}

	public int getAmFluidAnum() {
		return amFluidAnum;
	}

	public void setAmFluidAnum(int amFluidAnum) {
		this.amFluidAnum = amFluidAnum;
	}

	public int getAmFluidAll() {
		return amFluidAll;
	}

	public void setAmFluidAll(int amFluidAll) {
		this.amFluidAll = amFluidAll;
	}

	public String getDtOutMode() {
		return dtOutMode;
	}

	public void setDtOutMode(String dtOutMode) {
		this.dtOutMode = dtOutMode;
	}

	public Date getDateBeginSx() {
		return dateBeginSx;
	}

	public void setDateBeginSx(Date dateBeginSx) {
		this.dateBeginSx = dateBeginSx;
	}

	public Date getDateEndSx() {
		return dateEndSx;
	}

	public void setDateEndSx(Date dateEndSx) {
		this.dateEndSx = dateEndSx;
	}

	public String getOutAbout() {
		return outAbout;
	}

	public void setOutAbout(String outAbout) {
		this.outAbout = outAbout;
	}

	public String getDtAsp() {
		return dtAsp;
	}

	public void setDtAsp(String dtAsp) {
		this.dtAsp = dtAsp;
	}

	public String getOtherFlag() {
		return otherFlag;
	}

	public void setOtherFlag(String otherFlag) {
		this.otherFlag = otherFlag;
	}

	public String getIsPm() {
		return isPm;
	}

	public void setIsPm(String isPm) {
		this.isPm = isPm;
	}

	public String getPmPart() {
		return pmPart;
	}

	public void setPmPart(String pmPart) {
		this.pmPart = pmPart;
	}

	public int getPmLen() {
		return pmLen;
	}

	public void setPmLen(int pmLen) {
		this.pmLen = pmLen;
	}

	public int getPmWid() {
		return pmWid;
	}

	public void setPmWid(int pmWid) {
		this.pmWid = pmWid;
	}

	public int getPmHigh() {
		return pmHigh;
	}

	public void setPmHigh(int pmHigh) {
		this.pmHigh = pmHigh;
	}
	public BabyBedVO getBabyBed() {
		return babyBed;
	}
	public void setBabyBed(BabyBedVO babyBed) {
		this.babyBed = babyBed;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getHpcode() {
		return hpcode;
	}
	public void setHpcode(String hpcode) {
		this.hpcode = hpcode;
	}

	public String getFlagDept() { return flagDept; }
	public void setFlagDept(String flagDept) { this.flagDept = flagDept; }

	public String getIsSavePvLabor() {return isSavePvLabor;}
	public void setIsSavePvLabor(String isSavePvLabor) {this.isSavePvLabor = isSavePvLabor;}
}
