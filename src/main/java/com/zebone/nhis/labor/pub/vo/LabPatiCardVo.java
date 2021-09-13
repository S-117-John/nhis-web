package com.zebone.nhis.labor.pub.vo;

import java.util.Date;

/**
 * 床位大卡
 */
public class LabPatiCardVo {

	private String pkBed; // 床位主键
	private String bedno;// 床号
	private String dtBedtype; // 床位类型

	private String pkPv; // pk_pv
	private String codePv;// 就诊编码
	private String codeIp;// 住院号
	private String pkPi;// 患者主键
	private String namePi;// 患者姓名
	private String dtSex;// 患者的性别编码
	private String age;// 患者年龄
	
	private String pkDeptNs;//就诊病区
	private String nameDeptNs;//就诊病区
	private String pkDept;//就诊科室
	private String nameDept;//就诊科室名称
	private String codeDept;//就诊科室名称
	private String nameEmpPhy;// 当前主管医生姓名
	private String nameEmpNs;// 当前责任护士名称
	private Date dateIn;// 入产房科日期
	private Date dateBegin;//就诊开始时间
	private String days;// 就诊或者住院天数
	private String descDiag;// 诊断名称
	private String pkPvlabor;//患者产房就诊主键
	private String pkPvlaborMother;//患者母亲产房就诊主键

	private String dtLevelNs;// 护理等级
	private String dtLevelDise;// 病情等级
	private String dtLevelNutr;// 营养等级
	private String levelNsHint;// 护理等级 - 名称
	private String levelDiseHint;// 病情等级 - 名称
	private String levelNutrHint;// 营养等级 - 名称

	private String hpname; // 主医保
	private String flagInfant;//新生儿标识
	private String flagMaj; // 主床标识
	private String euHold; // 主床标识

	/** 费用信息 */
	private Double price;// 床位费用
	private Double prefee;//预交金
	private Double accfee;//担保金
	private Double totalfee;//总费用
	private Double yuefee;// 余额
	private Double ztfee;// 在途
	private Double gdfee;//固定费用
	
	/** 床位状态 */
    private String name; // 获取或设置床位名称
    private String houseno; // 获取或设置所在房间   
    private String flagActive;// 获取或设置开放标志 
    private String flagTemp;// 获取或设置加床标志
    private String euStatus;// 获取或设置床位状态  
    private String flagOcupy;// 获取或设置占用标志
	private String pkItem;//婴儿占床标志
    
    /** 婴儿信息 */
    private String infantname;
    private String infantsex;
    private  Date birthDate;
    private int weekPreg;//宫内妊娠周数
    private String infantcode;
    private int sortNo;
    
    /** 产房显示信息 */
    private String numPreg; //孕次
    private Date DateLast;//末次月经
    
    
	public String getNumPreg() {
		return numPreg;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public void setNumPreg(String numPreg) {
		this.numPreg = numPreg;
	}
	public Date getDateLast() {
		return DateLast;
	}
	public void setDateLast(Date dateLast) {
		DateLast = dateLast;
	}
	public String getInfantcode() {
		return infantcode;
	}
	public String getDtBedtype() {
		return dtBedtype;
	}
	public void setDtBedtype(String dtBedtype) {
		this.dtBedtype = dtBedtype;
	}
	public void setInfantcode(String infantcode) {
		this.infantcode = infantcode;
	}
	public String getInfantname() {
		return infantname;
	}
	public void setInfantname(String infantname) {
		this.infantname = infantname;
	}
	public String getInfantsex() {
		return infantsex;
	}
	public void setInfantsex(String infantsex) {
		this.infantsex = infantsex;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getEuHold() {
		return euHold;
	}
	public void setEuHold(String euHold) {
		this.euHold = euHold;
	}
	public String getFlagMaj() {
		return flagMaj;
	}
	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}
	public String getLevelNsHint() {
		return levelNsHint;
	}
	public void setLevelNsHint(String levelNsHint) {
		this.levelNsHint = levelNsHint;
	}
	public String getLevelDiseHint() {
		return levelDiseHint;
	}
	public void setLevelDiseHint(String levelDiseHint) {
		this.levelDiseHint = levelDiseHint;
	}
	public String getLevelNutrHint() {
		return levelNutrHint;
	}
	public void setLevelNutrHint(String levelNutrHint) {
		this.levelNutrHint = levelNutrHint;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHouseno() {
		return houseno;
	}
	public void setHouseno(String houseno) {
		this.houseno = houseno;
	}
	public String getFlagActive() {
		return flagActive;
	}
	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	public String getFlagTemp() {
		return flagTemp;
	}
	public void setFlagTemp(String flagTemp) {
		this.flagTemp = flagTemp;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getFlagOcupy() {
		return flagOcupy;
	}
	public void setFlagOcupy(String flagOcupy) {
		this.flagOcupy = flagOcupy;
	}
	public String getPkBed() {
		return pkBed;
	}
	public void setPkBed(String pkBed) {
		this.pkBed = pkBed;
	}
	public String getBedno() {
		return bedno;
	}
	public void setBedno(String bedno) {
		this.bedno = bedno;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
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
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getNameDeptNs() {
		return nameDeptNs;
	}
	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getNameEmpNs() {
		return nameEmpNs;
	}
	public void setNameEmpNs(String nameEmpNs) {
		this.nameEmpNs = nameEmpNs;
	}
	public Date getDateIn() {
		return dateIn;
	}
	public void setDateIn(Date dateIn) {
		this.dateIn = dateIn;
	}
	public Date getDateBegin() {
		return dateBegin;
	}
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public String getDtLevelNs() {
		return dtLevelNs;
	}
	public void setDtLevelNs(String dtLevelNs) {
		this.dtLevelNs = dtLevelNs;
	}
	public String getDtLevelDise() {
		return dtLevelDise;
	}
	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}
	public String getDtLevelNutr() {
		return dtLevelNutr;
	}
	public void setDtLevelNutr(String dtLevelNutr) {
		this.dtLevelNutr = dtLevelNutr;
	}
	public String getHpname() {
		return hpname;
	}
	public void setHpname(String hpname) {
		this.hpname = hpname;
	}
	public String getFlagInfant() {
		return flagInfant;
	}
	public void setFlagInfant(String flagInfant) {
		this.flagInfant = flagInfant;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPrefee() {
		return prefee;
	}
	public void setPrefee(Double prefee) {
		this.prefee = prefee;
	}
	public Double getAccfee() {
		return accfee;
	}
	public void setAccfee(Double accfee) {
		this.accfee = accfee;
	}
	public Double getTotalfee() {
		return totalfee;
	}
	public void setTotalfee(Double totalfee) {
		this.totalfee = totalfee;
	}
	public Double getYuefee() {
		return yuefee;
	}
	public void setYuefee(Double yuefee) {
		this.yuefee = yuefee;
	}
	public Double getZtfee() {
		return ztfee;
	}
	public void setZtfee(Double ztfee) {
		this.ztfee = ztfee;
	}
	public Double getGdfee() {
		return gdfee;
	}
	public void setGdfee(Double gdfee) {
		this.gdfee = gdfee;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public int getWeekPreg() {
		return weekPreg;
	}

	public void setWeekPreg(int weekPreg) {
		this.weekPreg = weekPreg;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getPkItem() {return pkItem;}

	public void setPkItem(String pkItem) {this.pkItem = pkItem;}

	public String getPkPvlabor() {return pkPvlabor;}

	public void setPkPvlabor(String pkPvlabor) {this.pkPvlabor = pkPvlabor;}

	public String getPkPvlaborMother() {return pkPvlaborMother;}

	public void setPkPvlaborMother(String pkPvlaborMother) {this.pkPvlaborMother = pkPvlaborMother;}
}
