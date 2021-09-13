package com.zebone.nhis.webservice.zhongshan.vo;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.zebone.nhis.common.module.ex.nis.ns.ExLabOcc;

/**
 * @Classname ThiCnOrder
 * @Description 02号接口业务入参，进行医嘱录入
 * @Date 2021-04-07 20:35
 * @Created by wuqiang
 */
public class ThiCnOrder {
    //--02号开始
    /**
     *就诊编码
      */
    @NotBlank(message = "就诊主键")
    private String pkPv;
    /**
     *申请日期
     */
    @NotBlank(message = "申请日期不能为空")
    private String dateStart;
    /**
     *申请日期
     */
    @NotBlank(message = "申请日期不能为空")
    private String pkOrd;
    /**
     *执行科室编码
     */
    @NotBlank(message = "执行科室不能为空")
    private String pkDeptExec;
    /**
     *ABO血型,01 A型;02 B型；03 O型；04 AB型；05 不详；06 未查；90 已查未回；
     */
    @NotBlank(message = "ABO血型不能为空")
    private String dtBtAbo;
    /**
     *RhD血型,01 RhD阴性；02 RhD阳性；03 不详；04 未查；06 已查未回；
     */
    @NotBlank(message = "RhD血型不能为空")
    private String dtBtRh;	
    /**
     *输血性质，00 当前；01 紧急；02 手术备血；03  输血
     */
    @NotBlank(message = "输血性质不能为空")
    private String dtBttype;
    /**
     *计划输血日期，yyyy-mm-dd
     */
    @NotBlank(message = "计划输血日期不能为空")
    private String datePlan;
    /**
     *输血类型，1 红细胞；2 血小板；3 血浆；4 全血；5 其他；
     */
    @NotBlank(message = "输血类型不能为空")
    private String btContent;
    /**
     *输血量，数字，支持两位小数，例如：50.55
     */
    @NotBlank(message = "输血量不能为空")
    private String quanBt;
    /**
     *输血量单位，90 ml;48 U;361 治疗量
     */
    @NotBlank(message = "输血量单位不能为空")
    private String codeUnitBt;
    /**
     * 单位主键
     */
    private String pkUnitBt;
    /**
     *加急标志，1 加急；0 不加急；
     */
    @NotBlank(message = "加急标志不能为空")
    private String flagEmer;
    /**
     *需检验标志，1 需要；0不需要；
     */
    @NotBlank(message = "需检验标志不能为空")
    private String flagLab;
    /**
     *过敏史标志，1 过敏；0不过敏；
     */
    @NotBlank(message = "过敏史标志不能为空")
    private String flagAl;
    /**
     *适应症标志，1 是；0 否
     */
    @NotBlank(message = "适应症标志不能为空")
    private String flagFit;
    /**
     *怀孕史标志，1 是；0 否
     */
    @NotBlank(message = "怀孕史标志不能为空")
    private String flagPreg;
    /**
     *怀孕次数，整数；怀孕史标志为1时，需要传入；
     */
    private String cntPreg;
    /**
     *分娩次数，整数；怀孕史标志为1时，需要传入；
     */
    private String cntLabor;
    /**
     *输血史标志,1 是；0 否；
     */
    @NotBlank(message = "输血史标志不能为空")
    private String flagBthis;
    /**
     *预定输血血型, 1 A型RhD (+) 2 B型RhD (+) 3 O型RhD (+) 4 AB型RhD (+) 
     *5 A型RhD (-) 6 B型RhD (-) 7 O型RhD (-) 8 AB型RhD (-) 9 已查未回
     */
    @NotBlank(message = "预定输血血型不能为空")
    private String euPreAborh;
     /**
      *输血目的，文本
      */
     @NotBlank(message = "输血目的不能为空")
     private String note;
     /**
      *医嘱备注
      */
     private String noteOrd;
    /**
     *开立病区编码
     */
    private String pkDeptNs;
    /**
     *开立科室编码
     */
    @NotBlank(message = "开立科室编码不能为空")
    private String pkDept;
    /**
     *开立医生编码
     */
    @NotBlank(message = "开立医生编码不能为空")
    private String pkEmpOrd;

    private List<ExLabOcc> labOcc;
    
    
    public List<ExLabOcc> getLabOcc() {
		return labOcc;
	}

	public void setLabOcc(List<ExLabOcc> labOcc) {
		this.labOcc = labOcc;
	}

	public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }
    public String getPkDeptExec() {
        return pkDeptExec;
    }

    public void setPkDeptExec(String pkDeptExec) {
        this.pkDeptExec = pkDeptExec;
    }
    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }
    public String getPkDeptNs() {
        return pkDeptNs;
    }

    public void setPkDeptNs(String pkDeptNs) {
        this.pkDeptNs = pkDeptNs;
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept;
    }

    public String getPkEmpOrd() {
        return pkEmpOrd;
    }

    public void setPkEmpOrd(String pkEmpOrd) {
        this.pkEmpOrd = pkEmpOrd;
    }
    
    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public String getNoteOrd() {
        return noteOrd;
    }

    public void setNoteOrd(String noteOrd) {
        this.noteOrd = noteOrd;
    }

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getDtBtAbo() {
		return dtBtAbo;
	}

	public void setDtBtAbo(String dtBtAbo) {
		this.dtBtAbo = dtBtAbo;
	}

	public String getDtBtRh() {
		return dtBtRh;
	}

	public void setDtBtRh(String dtBtRh) {
		this.dtBtRh = dtBtRh;
	}

	public String getDtBttype() {
		return dtBttype;
	}

	public void setDtBttype(String dtBttype) {
		this.dtBttype = dtBttype;
	}

	public String getDatePlan() {
		return datePlan;
	}

	public void setDatePlan(String datePlan) {
		this.datePlan = datePlan;
	}

	public String getBtContent() {
		return btContent;
	}

	public void setBtContent(String btContent) {
		this.btContent = btContent;
	}

	public String getQuanBt() {
		return quanBt;
	}

	public void setQuanBt(String quanBt) {
		this.quanBt = quanBt;
	}

	public String getCodeUnitBt() {
		return codeUnitBt;
	}

	public void setCodeUnitBt(String codeUnitBt) {
		this.codeUnitBt = codeUnitBt;
	}

	public String getFlagLab() {
		return flagLab;
	}

	public void setFlagLab(String flagLab) {
		this.flagLab = flagLab;
	}

	public String getFlagAl() {
		return flagAl;
	}

	public void setFlagAl(String flagAl) {
		this.flagAl = flagAl;
	}

	public String getFlagFit() {
		return flagFit;
	}

	public void setFlagFit(String flagFit) {
		this.flagFit = flagFit;
	}

	public String getCntPreg() {
		return cntPreg;
	}

	public void setCntPreg(String cntPreg) {
		this.cntPreg = cntPreg;
	}

	public String getCntLabor() {
		return cntLabor;
	}

	public void setCntLabor(String cntLabor) {
		this.cntLabor = cntLabor;
	}

	public String getFlagBthis() {
		return flagBthis;
	}

	public void setFlagBthis(String flagBthis) {
		this.flagBthis = flagBthis;
	}

	public String getEuPreAborh() {
		return euPreAborh;
	}

	public void setEuPreAborh(String euPreAborh) {
		this.euPreAborh = euPreAborh;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagPreg() {
		return flagPreg;
	}

	public void setFlagPreg(String flagPreg) {
		this.flagPreg = flagPreg;
	}

	public String getPkUnitBt() {
		return pkUnitBt;
	}

	public void setPkUnitBt(String pkUnitBt) {
		this.pkUnitBt = pkUnitBt;
	}
    
}
