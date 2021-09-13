package com.zebone.nhis.common.module.labor.nis;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: PV_LABOR_REC_DT 
 *
 * @since 2017-09-15 07:25:34
 */
@Table(value="PV_LABOR_REC_DT")
public class PvLaborRecDt extends BaseModule  {

	@PK
	@Field(value="PK_LABORRECDT",id=KeyId.UUID)
    private String pkLaborrecdt;

	@Field(value="PK_LABORREC")
    private String pkLaborrec;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DATE_OUT")
    private Date dateOut;

	@Field(value="DT_OUT_MODE")
    private String dtOutMode;
	
	@Field(value="OP_REASON")
    private String opReason;

	@Field(value="SPECIAL")
    private String special;

	@Field(value="POS_FO")
    private String posFo;

	@Field(value="PLA_ABOUT")
    private String plaAbout;

	@Field(value="PLA_DEFECT_LEN")
    private Integer plaDefectLen;

	@Field(value="PLA_DEFECT_WID")
    private Integer plaDefectWid;

	@Field(value="PLA_ROUGH_LEN")
    private Integer plaRoughLen;

	@Field(value="PLA_ROUGH_WID")
    private Integer plaRoughWid;

	@Field(value = "PLA_LEN")
	private Double plaLen;

	@Field(value = "PLA_WID")
	private Double plaWid;

	@Field(value = "PLA_HIGH")
	private Double plaHigh;

	@Field(value="PLA_WIGHT")
    private Integer plaWight;

	@Field(value="PLA_OTHERS")
    private String plaOthers;

	@Field(value="PLA_EMP")
    private String plaEmp;

	@Field(value="TIME_EARLY")
    private Double timeEarly;

	@Field(value="CAUL_ABOUT")
    private String caulAbout;

	@Field(value="CAUL_DEFECT_LEN")
    private Integer caulDefectLen;

	@Field(value="CAUL_DEFECT_WID")
    private Integer caulDefectWid;

	@Field(value="CAUL_OTHERS")
    private String caulOthers;

	@Field(value="UMB_LEN")
    private Integer umbLen;

	@Field(value="UMB_ABOUT")
    private String umbAbout;

	@Field(value="UMB_CROSS_CYCLE")
    private Double umbCrossCycle;

	@Field(value="UMB_TURN_CYCLE")
    private Double umbTurnCycle;

	@Field(value="UMB_POISTION")
    private String umbPoistion;

	@Field(value="UMB_OTHERS")
    private String umbOthers;

	@Field(value="UMB_ADH")
    private String umbAdh;

	@Field(value="AM_FLUID_AFTER")
    private String amFluidAfter;

	@Field(value="AM_FLUID_OTHER")
    private String amFluidOther;

	@Field(value="AM_FLUID_ANUM")
    private Integer amFluidAnum;

	@Field(value="AM_FLUID_ALL")
    private Integer amFluidAll;

	@Field(value="SORT_NO")
    private Integer sortNo;

	@Field(value="DT_SEX_INF")
    private String dtSexInf;

	@Field(value="DATE_BEGIN_SX")
    private Date dateBeginSx;

	@Field(value="DATE_END_SX")
    private Date dateEndSx;

	@Field(value="OUT_ABOUT")
    private String outAbout;

	@Field(value="DT_BREATH")
    private String dtBreath;

	@Field(value="DT_ASP")
    private String dtAsp;

	@Field(value="INF_WIGHT")
    private Double infWight;

	@Field(value="INF_LEN")
    private Double infLen;

	@Field(value="OTHER_FLAG")
    private String otherFlag;

	@Field(value="IS_PM")
    private String isPm;

	@Field(value="PM_PART")
    private String pmPart;

	@Field(value = "PM_LEN")
	private Double pmLen;

	@Field(value = "PM_WID")
	private Double pmWid;

	@Field(value = "PM_HIGH")
	private Double pmHigh;

	@Field(value="WEEK_PREG")
    private String weekPreg;

	@Field(value="EU_INF")
    private String euInf;

	@Field(value="MODIFY_TIME")
    private Date modifyTime;

	@Field(value="FLAG_AM_FLUID_ANUM")
	private String flagAmFluidAnum;

	@Field(value="FLAG_AM_FLUID_ALL")
	private String flagAmFluidAll;
	
	@Field(value="BREAK_MODE")
	private String breakMode;
	public String getPkLaborrecdt() {
		return pkLaborrecdt;
	}

	public void setPkLaborrecdt(String pkLaborrecdt) {
		this.pkLaborrecdt = pkLaborrecdt;
	}

	public String getPkLaborrec() {
		return pkLaborrec;
	}

	public void setPkLaborrec(String pkLaborrec) {
		this.pkLaborrec = pkLaborrec;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public Date getDateOut() {
		return dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public String getDtOutMode() {
		return dtOutMode;
	}

	public void setDtOutMode(String dtOutMode) {
		this.dtOutMode = dtOutMode;
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

	public Integer getPlaDefectLen() {
		return plaDefectLen;
	}

	public void setPlaDefectLen(Integer plaDefectLen) {
		this.plaDefectLen = plaDefectLen;
	}

	public Integer getPlaDefectWid() {
		return plaDefectWid;
	}

	public void setPlaDefectWid(Integer plaDefectWid) {
		this.plaDefectWid = plaDefectWid;
	}

	public Integer getPlaRoughLen() {
		return plaRoughLen;
	}

	public void setPlaRoughLen(Integer plaRoughLen) {
		this.plaRoughLen = plaRoughLen;
	}

	public Integer getPlaRoughWid() {
		return plaRoughWid;
	}

	public void setPlaRoughWid(Integer plaRoughWid) {
		this.plaRoughWid = plaRoughWid;
	}




	public Integer getPlaWight() {
		return plaWight;
	}

	public void setPlaWight(Integer plaWight) {
		this.plaWight = plaWight;
	}

	public String getPlaOthers() {
		return plaOthers;
	}

	public void setPlaOthers(String plaOthers) {
		this.plaOthers = plaOthers;
	}
	public String getOpReason(){
        return this.opReason;
    }
    public void setOpReason(String opReason){
        this.opReason = opReason;
    }

    public String getSpecial(){
        return this.special;
    }
    public void setSpecial(String special){
        this.special = special;
    }

	public String getPlaEmp() {
		return plaEmp;
	}

	public void setPlaEmp(String plaEmp) {
		this.plaEmp = plaEmp;
	}

	public Double getTimeEarly() {
		return timeEarly;
	}

	public void setTimeEarly(Double timeEarly) {
		this.timeEarly = timeEarly;
	}

	public String getCaulAbout() {
		return caulAbout;
	}

	public void setCaulAbout(String caulAbout) {
		this.caulAbout = caulAbout;
	}

	public Integer getCaulDefectLen() {
		return caulDefectLen;
	}

	public void setCaulDefectLen(Integer caulDefectLen) {
		this.caulDefectLen = caulDefectLen;
	}

	public Integer getCaulDefectWid() {
		return caulDefectWid;
	}

	public void setCaulDefectWid(Integer caulDefectWid) {
		this.caulDefectWid = caulDefectWid;
	}

	public String getCaulOthers() {
		return caulOthers;
	}

	public void setCaulOthers(String caulOthers) {
		this.caulOthers = caulOthers;
	}

	public Integer getUmbLen() {
		return umbLen;
	}

	public void setUmbLen(Integer umbLen) {
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

	public String getAmFluidOther() {
		return amFluidOther;
	}

	public void setAmFluidOther(String amFluidOther) {
		this.amFluidOther = amFluidOther;
	}

	public Integer getAmFluidAnum() {
		return amFluidAnum;
	}

	public void setAmFluidAnum(Integer amFluidAnum) {
		this.amFluidAnum = amFluidAnum;
	}

	public Integer getAmFluidAll() {
		return amFluidAll;
	}

	public void setAmFluidAll(Integer amFluidAll) {
		this.amFluidAll = amFluidAll;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getDtSexInf() {
		return dtSexInf;
	}

	public void setDtSexInf(String dtSexInf) {
		this.dtSexInf = dtSexInf;
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

	public String getDtBreath() {
		return dtBreath;
	}

	public void setDtBreath(String dtBreath) {
		this.dtBreath = dtBreath;
	}

	public String getDtAsp() {
		return dtAsp;
	}

	public void setDtAsp(String dtAsp) {
		this.dtAsp = dtAsp;
	}

	public Double getInfWight() {
		return infWight;
	}

	public void setInfWight(Double infWight) {
		this.infWight = infWight;
	}

	public Double getInfLen() {
		return infLen;
	}

	public void setInfLen(Double infLen) {
		this.infLen = infLen;
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



	public String getWeekPreg() {
		return weekPreg;
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

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Double getPlaLen() {
		return plaLen;
	}

	public void setPlaLen(Double plaLen) {
		this.plaLen = plaLen;
	}

	public Double getPlaWid() {
		return plaWid;
	}

	public void setPlaWid(Double plaWid) {
		this.plaWid = plaWid;
	}

	public Double getPlaHigh() {
		return plaHigh;
	}

	public void setPlaHigh(Double plaHigh) {
		this.plaHigh = plaHigh;
	}

	public Double getPmLen() {
		return pmLen;
	}

	public void setPmLen(Double pmLen) {
		this.pmLen = pmLen;
	}

	public Double getPmWid() {
		return pmWid;
	}

	public void setPmWid(Double pmWid) {
		this.pmWid = pmWid;
	}

	public Double getPmHigh() {
		return pmHigh;
	}

	public void setPmHigh(Double pmHigh) {
		this.pmHigh = pmHigh;
	}

	public String getBreakMode() {
		return breakMode;
	}

	public void setBreakMode(String breakMode) {
		this.breakMode = breakMode;
	}

	public String getFlagAmFluidAnum() {return flagAmFluidAnum;}

	public void setFlagAmFluidAnum(String flagAmFluidAnum) {this.flagAmFluidAnum = flagAmFluidAnum;}

	public String getFlagAmFluidAll() {return flagAmFluidAll;}

	public void setFlagAmFluidAll(String flagAmFluidAll) {this.flagAmFluidAll = flagAmFluidAll;}
}