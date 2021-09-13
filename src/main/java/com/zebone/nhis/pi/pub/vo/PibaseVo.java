package com.zebone.nhis.pi.pub.vo;

import com.zebone.platform.modules.dao.build.au.Field;

import java.util.Date;

/**
 * 患者及就诊信息
 * 
 * @author wangpeng
 * @date 2016年9月10日
 *
 */
	public class PibaseVo {
	
	/** 就诊类型 */
	private String euPvtype;
	
	/** 就诊模式：0普通，1临床路径，2单病种限价，3门诊慢病，4门诊特殊病，11急诊留观 */
	private String euPvmode;
	
	/** 患者主键 */
	private String pkPi;
	
	/** 就诊主键 */
	private String pkPv;
	
	/** 患者编码 */
    private String codePi;
    
    /** 住院号 */
    private String codeIp;
    
    /** 住院号 */
    private String codeOp;
    
    /** 患者姓名 */
    private String namePi;
    
    /** 性别 */
    private String dtSex;
    
    /** 出生日期 */
    private Date birthDate;
    
    /** 证件类型 */
    private String dtIdtype;
    
    /** 证件号码 */
    private String idNo;
     
    /** 手机号码 */
    private String mobile;
    
    /** 医保卡号 */
    private String insurNo;
    
    /**医保主键*/
    private String pkInsu;
    
    /**入院备注*/
    private String note; 
    
    /**医保附加属性0307 : 是否为广州市医保*/
    private String flagGzHp; 
    
    /**婴儿标志*/
    private String flagInfant; 
    
    /**
     * 锁定状态
     */
    private String euLocked;
    
    /**
     * 床位限额
     */
    private Double bedquota;
    
   

	/** 患者分类 */
    private String namePicate;
    
    /** 过敏源  多笔过敏信息之间以逗号分隔*/
    private String nameAl;
    
    /** 医保名称 */
    private String nameInsu;
    
    /** 诊断 */
    private String nameDiag;
    
    /** 在院状态 */
    private String flagIn;
    
    /** 就诊编码 */
    private String codePv;
    
    /** 主治医生 */
    private String pkEmpPhy;

	/** 当前主管医生姓名 */
    private String nameEmpPhy;

    /**责任护士 */
    private String pkEmpNs;

	/** 当前责任护士姓名 */
    private String nameEmpNs;
    
    /** BED_NO - 门诊慢性病，急诊观察时，住院床位。调床是实时更新 */
    private String bedNo;
    
    /** 科室 */
    private String pkDept;
    
    /** 科室名称 */
    private String nameDept;
    
    /** 科室代码 */
    private String codeDept;
    
    /** 享受配偶生育保险1-是 0-不是 */
    private String flagMi;
	/** 配偶姓名 */
    private String nameSpouse;
	/** 配偶身份证号 */
    private String idnoSpouse;
	/** 联系人 */
    private String nameRel;
    
    /** 床位类型 */
    private String DtBedtype;

    private String adtType;

    private String ipTimes;
    
    /** 输出格式化年龄*/
    private String ageFormat;

	private Double weight;

	private String sbp;
	private String dbp;

	/** 未结算金额*/
	private Double notSettle;
	
	/**结算总金额*/
	private Double amountSt;
	
	/**结算主键*/
	private String pkSettle;
	
	/**结算号*/
	private String codeSt;
	
	/**发票号*/
	private String codeInv;

	private String pkContraception;

	private String dtJurisdiction;
	
	/**弹窗特殊处理标志, 1 特殊处理*/
	private String popUpWindowsSpecialFlag;
	
	/**弹窗特殊处理里符合条件的就诊记录*/
	private String matchPkPvs;


	private String unitWork;

	/**
	 * 结算时间
	 */
	private Date dateSettle;

	/**
	 * 人医项目-门诊慢病名称
	 */
	private String chDiseName;

	/**
	 * 本院职工
	 */
	private String pkemp;

	public String getPkEmp() {
		return pkemp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkemp = pkEmp;
	}

	/**
	 * 人医项目-门诊慢性病编码
	 */
	private String chDiseCode;

	public String getDbp() {
		return dbp;
	}

	public void setDbp(String dbp) {
		this.dbp = dbp;
	}

	public String getSbp() {
		return sbp;
	}

	public void setSbp(String sbp) {
		this.sbp = sbp;
	}

	public String getChDiseCode() {
		return chDiseCode;
	}

	public void setChDiseCode(String chDiseCode) {
		this.chDiseCode = chDiseCode;
	}

	public String getChDiseName() {
		return chDiseName;
	}

	public void setChDiseName(String chDiseName) {
		this.chDiseName = chDiseName;
	}

	public Date getDateSettle() {
		return dateSettle;
	}

	public void setDateSettle(Date dateSettle) {
		this.dateSettle = dateSettle;
	}

	public String getUnitWork() {
		return unitWork;
	}

	public void setUnitWork(String unitWork) {
		this.unitWork = unitWork;
	}

	public String getAgeFormat() {
		return ageFormat;
	}

	public void setAgeFormat(String ageFormat) {
		this.ageFormat = ageFormat;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public Double getBedquota() {
		return bedquota;
	}

	public void setBedquota(Double bedquota) {
		this.bedquota = bedquota;
	}

	public String getEuLocked() {
		return euLocked;
	}

	public void setEuLocked(String euLocked) {
		this.euLocked = euLocked;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}
    
    public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	/** 病区 */
	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;
	
	/** 产房床号 */
	private String bedNoLab;
	
	/** EU_STATUS - 0 登记，1 就诊，2 结束，3 结算，9 退诊 */
    private String euStatus;
    
    /** 就诊开始时间（入院时间） */
    private Date dateBegin;
	/** 入科时间 */
	private Date dateAdmit;
	/** 就诊结束时间(出院时间) */
    private Date dateEnd;
    
    /** 看诊时间 */
    private Date dateClinic;
    
    /** 预交金金额 */
    private Double prepayAmount;
    
    /** 费用总额 */
    private Double totalAmount;
    
    /** 住院天数 */
    private int ipDays;
    
    /** 主诊断*/
	private String pkDiag;

	/** 主诊断编码*/
	private String diagCode;
    
    private String pkPicate;
    
    /**自费总额*/
    private Double amountPi;
    
    /**挂号医生*/
    private String pkEmpPv;
    
    /**挂号医生姓名*/
    private String nameEmpPv;

    /**挂号医生医疗项目权限*/
    private String dtEmpsrvtype;

    /**流行病史信息*/
    private String descEpid;

	/**患者就诊诊区*/
	private String pkDeptArea;

	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}

	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public Double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(Double amountPi) {
		this.amountPi = amountPi;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	/**患者所属门诊医生*/
    private String empName;
    
    
    
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getPkPicate() {
		return pkPicate;
	}

	public String getBedNoLab() {
		return bedNoLab;
	}

	public void setBedNoLab(String bedNoLab) {
		this.bedNoLab = bedNoLab;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getDtSex() {
		return dtSex;
	}

	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getNamePicate() {
		return namePicate;
	}

	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}

	public String getNameAl() {
		return nameAl;
	}

	public void setNameAl(String nameAl) {
		this.nameAl = nameAl;
	}

	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getFlagIn() {
		return flagIn;
	}

	public void setFlagIn(String flagIn) {
		this.flagIn = flagIn;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getPkEmpPhy() {
		return pkEmpPhy;
	}

	public void setPkEmpPhy(String pkEmpPhy) {
		this.pkEmpPhy = pkEmpPhy;
	}

	public String getNameEmpPhy() {
		return nameEmpPhy;
	}

	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}

	public String getPkEmpNs() {
		return pkEmpNs;
	}

	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}

	public String getNameEmpNs() {
		return nameEmpNs;
	}

	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}



	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Date getDateClinic() {
		return dateClinic;
	}

	public void setDateClinic(Date dateClinic) {
		this.dateClinic = dateClinic;
	}

	public Double getPrepayAmount() {
		return prepayAmount;
	}

	public void setPrepayAmount(Double prepayAmount) {
		this.prepayAmount = prepayAmount;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getIpDays() {
		return ipDays;
	}

	public void setIpDays(int ipDays) {
		this.ipDays = ipDays;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	   /** 患者地址现住址 */
	 private String address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	 
	/** 特诊标志*/

	private String	flagSpec;

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getFlagGzHp() {
		return flagGzHp;
	}

	public void setFlagGzHp(String flagGzHp) {
		this.flagGzHp = flagGzHp;
	}

	public String getEuPvmode() {
		return euPvmode;
	}

	public void setEuPvmode(String euPvmode) {
		this.euPvmode = euPvmode;
	}

	public String getPkEmpPv() {
		return pkEmpPv;
	}

	public void setPkEmpPv(String pkEmpPv) {
		this.pkEmpPv = pkEmpPv;
	}

	public String getNameEmpPv() {
		return nameEmpPv;
	}

	public void setNameEmpPv(String nameEmpPv) {
		this.nameEmpPv = nameEmpPv;
	}

	public String getFlagInfant() {
		return flagInfant;
	}

	public void setFlagInfant(String flagInfant) {
		this.flagInfant = flagInfant;
	}

	public String getFlagMi() {
		return flagMi;
	}

	public void setFlagMi(String flagMi) {
		this.flagMi = flagMi;
	}

	public String getNameSpouse() {
		return nameSpouse;
	}

	public void setNameSpouse(String nameSpouse) {
		this.nameSpouse = nameSpouse;
	}

	public String getIdnoSpouse() {
		return idnoSpouse;
	}

	public void setIdnoSpouse(String idnoSpouse) {
		this.idnoSpouse = idnoSpouse;
	}
	/** 医疗证号*/
	private String mcno;

	public String getMcno() {
		return mcno;
	}

	public void setMcno(String mcno) {
		this.mcno = mcno;
	}

	public String getDtBedtype() {
		return DtBedtype;
	}

	public void setDtBedtype(String dtBedtype) {
		DtBedtype = dtBedtype;
	}

	public String getAdtType() {
		return adtType;
	}

	public void setAdtType(String adtType) {
		this.adtType = adtType;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public String getDescEpid() {
		return descEpid;
	}

	public void setDescEpid(String descEpid) {
		this.descEpid = descEpid;
	}

	public Double getNotSettle() {
		return notSettle;
	}

	public void setNotSettle(Double notSettle) {
		this.notSettle = notSettle;
	}

	public String getPkDeptArea() {
		return pkDeptArea;
	}

	public void setPkDeptArea(String pkDeptArea) {
		this.pkDeptArea = pkDeptArea;
	}

	public Double getAmountSt() {
		return amountSt;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getCodeInv() {
		return codeInv;
	}

	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPkContraception() {
		return pkContraception;
	}

	public void setPkContraception(String pkContraception) {
		this.pkContraception = pkContraception;
	}

	public String getDtJurisdiction() {
		return dtJurisdiction;
	}

	public void setDtJurisdiction(String dtJurisdiction) {
		this.dtJurisdiction = dtJurisdiction;
	}

	public String getPopUpWindowsSpecialFlag() {
		return popUpWindowsSpecialFlag;
	}

	public void setPopUpWindowsSpecialFlag(String popUpWindowsSpecialFlag) {
		this.popUpWindowsSpecialFlag = popUpWindowsSpecialFlag;
	}

	public String getMatchPkPvs() {
		return matchPkPvs;
	}

	public void setMatchPkPvs(String matchPkPvs) {
		this.matchPkPvs = matchPkPvs;
	}
}
