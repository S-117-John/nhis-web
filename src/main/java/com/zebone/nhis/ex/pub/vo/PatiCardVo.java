package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

/**
 * 床位大卡
 * 
 * @author yangxue
 * 
 */
public class PatiCardVo {

	private String pkBed; // 床位主键
	private String bedno;// 床号
	private String dtBedtype; // 床位类型
	private String pkPv; // pk_pv
	private String codePi;// 患者编码
	private String codePv;// 就诊编码
	private String codeIp;// 住院号
	private String pkPi;// 患者主键
	private String namePi;// 患者姓名
	private String dtSex;// 患者的性别编码
	private String age;// 患者年龄
	private String idNo;//证件号码 
	private String weight;//体重
	private String height;//身高
	private String pkDeptNs;//就诊病区
//	private String nameDeptNs;//就诊病区
	private String pkDept;//就诊科室
	private String nameDept;//就诊科室名称
	private String codeDept;//就诊科室名称
	private String nameEmpPhy;// 当前主管医生姓名
	private String codeEmpPhy;// 当前主管医生编号
	private String nameEmpNs;// 当前责任护士名称
	private String pkEmpNs;// 当前责任护士主键
	private Date dateAdmit;// 入院日期
	private Date dateBegin;//就诊开始时间
	private String days;// 就诊或者住院天数
	private String descDiag;// 诊断名称
	private Date inDeptTime;//入科时间
	private String dtLevelNs;// 护理等级
	private String dtLevelDise;// 病情等级
	private String dtLevelNutr;// 营养等级
	private String dtDietary;//饮食情况
	private String patientMemo;
	//过敏史
	private String nameAl;
	//电话
	private String mobile;
	
	private String levelNsHInteger;
	private String LevelNsHint;
	/**
	 * 病情等级 - 名称
	 */
	private String levelDiseHInteger;
	private String levelDiseHint;
	/**
	 * 营养等级 - 名称
	 */
	private String levelNutrHint;

	private String isAllergic;//是否过敏

	private String pkPicate;//患者分类 - 主键
	private String picate;//患者分类 - 名称
	private String pkPicateNew;//患者分类 - 修改后的患者分类名称
	private int ipTimes;  //住院次数

	private String hpname; // 主医保
	private String euHptype;//医保类型
	private String flagInfant;//新生儿标识
	private String flagMaj; // 主床标识
	private String euHold; // 主床标识
	private String euPvmode;//就诊模式

	/** 床位费用 */
	private Double price;// 床位费用
	
	private Double prefee;//预交金
	private Double accfee;//担保金
	private Double totalfee;//总费用
	private Double yuefee;// 余额
	private Double ztfee;// 在途
	private Double gdfee;//固定费用
	/// 获取或设置床位名称
    private String name;
    /// 获取或设置所在房间         
    private String houseno;
    /// 获取或设置开放标志         
    private String flagActive;
    /// 获取或设置加床标志       
    private String flagTemp;
    /// 获取或设置床位状态        
    private String euStatus;
    /// 获取或设置占用标志        
    private String flagOcupy;
    
    //婴儿信息
    private String infantname;
    private String pkPvInfant;
    private String infantsex;
    private  Date dateBirth;
    private String infantcode;
    private int sortNo;
    private String pkPvlabor;//孕产记录主键
    
    private Date dateSt;	//结算时间
    
    private int euStatusInfant; //是否在产房
    /**
     * 手术标识
     */
	private String isOperation;
	 /**
     * 引流管标识
     */
	private String isDrain;
//	/**
//	 * 科室-专业类型
//	 */
//	private String dtMedicaltype;

	private String ageFormat;

	private String cadreLevel;

	public String getCadreLevel() {
		return cadreLevel;
	}

	public void setCadreLevel(String cadreLevel) {
		this.cadreLevel = cadreLevel;
	}

//	public String getDtMedicaltype() {
//		return dtMedicaltype;
//	}
//
//	public void setDtMedicaltype(String dtMedicaltype) {
//		this.dtMedicaltype = dtMedicaltype;
//	}

	public String getIsOperation() {
		return isOperation;
	}

	public void setIsOperation(String isOperation) {
		this.isOperation = isOperation;
	}

	public String getIsDrain() {
		return isDrain;
	}

	public void setIsDrain(String isDrain) {
		this.isDrain = isDrain;
	}

	public String getIsAllergic() {
		return isAllergic;
	}

	public void setIsAllergic(String isAllergic) {
		this.isAllergic = isAllergic;
	}

	public Date getDateSt() {
		return dateSt;
	}
	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}
	public int getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(int ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getPkPvlabor() {
		return pkPvlabor;
	}
	public String getPkPvInfant() {
		return pkPvInfant;
	}
	public void setPkPvInfant(String pkPvInfant) {
		this.pkPvInfant = pkPvInfant;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public void setPkPvlabor(String pkPvlabor) {
		this.pkPvlabor = pkPvlabor;
	}
	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
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
	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
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
	public Date getDateBirth() {
		return dateBirth;
	}
	public void setDate_birth(Date birth_date) {
		this.dateBirth = birth_date;
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
	public String getLevelDiseHint() {
		return levelDiseHint;
	}
	public void setLevelDiseHint(String levelDiseHint) {
		this.levelDiseHint = levelDiseHint;
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
//	public String getNameDeptNs() {
//		return nameDeptNs;
//	}
//	public void setNameDeptNs(String nameDeptNs) {
//		this.nameDeptNs = nameDeptNs;
//	}
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
	public void setCodeEmpPhy(String codeEmpPhy) {
		this.codeEmpPhy = codeEmpPhy;
	}
	public String getCodeEmpPhy() {
		return codeEmpPhy;
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
	public Date getDateAdmit() {
		return dateAdmit;
	}
	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
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
	public String getEuPvmode() {
		return euPvmode;
	}
	public void setEuPvmode(String euPvmode) {
		this.euPvmode = euPvmode;
	}
	public String getEuHptype() {
		return euHptype;
	}
	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getPkPicate() {
		return pkPicate;
	}
	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}
	public String getPicate() {
		return picate;
	}
	public void setPicate(String picate) {
		this.picate = picate;
	}
	public String getPkPicateNew() {
		return pkPicateNew;
	}
	public void setPkPicateNew(String pkPicateNew) {
		this.pkPicateNew = pkPicateNew;
	}
	public String getPkEmpNs() {
		return pkEmpNs;
	}
	public void setPkEmpNs(String pkEmpNs) {
		this.pkEmpNs = pkEmpNs;
	}
	public int getEuStatusInfant() {
		return euStatusInfant;
	}
	public void setEuStatusInfant(int euStatusInfant) {
		this.euStatusInfant = euStatusInfant;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getLevelNsHInteger() {
		return levelNsHInteger;
	}
	public void setLevelNsHInteger(String levelNsHInteger) {
		this.levelNsHInteger = levelNsHInteger;
	}
	public String getLevelDiseHInteger() {
		return levelDiseHInteger;
	}
	public void setLevelDiseHInteger(String levelDiseHInteger) {
		this.levelDiseHInteger = levelDiseHInteger;
	}
	
	public String getLevelNutrHint() {
		return levelNutrHint;
	}
	public void setLevelNutrHint(String levelNutrHint) {
		this.levelNutrHint = levelNutrHint;
	}
	public String getLevelNsHint() {
		return LevelNsHint;
	}
	public void setLevelNsHint(String levelNsHint) {
		LevelNsHint = levelNsHint;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getNameAl() {
		return nameAl;
	}
	public void setNameAl(String nameAl) {
		this.nameAl = nameAl;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDtDietary() {
		return dtDietary;
	}

	public void setDtDietary(String dtDietary) {
		this.dtDietary = dtDietary;
	}

	public String getAgeFormat() {
		return ageFormat;
	}

	public void setAgeFormat(String ageFormat) {
		this.ageFormat = ageFormat;
	}

	public Date getInDeptTime() {return inDeptTime;}
	public void setInDeptTime(Date inDeptTime) {this.inDeptTime = inDeptTime;}

	public String getPatientMemo() {
		return patientMemo;
	}

	public void setPatientMemo(String patientMemo) {
		this.patientMemo = patientMemo;
	}
}
