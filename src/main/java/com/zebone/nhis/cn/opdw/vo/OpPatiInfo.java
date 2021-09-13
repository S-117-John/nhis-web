package com.zebone.nhis.cn.opdw.vo;


import com.zebone.platform.modules.dao.build.au.Field;

import java.util.Date;

public class OpPatiInfo {

	 private String pkPi; //   --患者主键
     private String pkPv; //   --门诊就诊主键
     private String codePi; // --患者编码
     private String namePi; // --姓名
     private String sex; //  --性别
     private String idtype; //--证件类型
     private String idNo; //      --证件号码
     private String mobile; //     --手机号码
     private String codePv; //    --门诊就诊号码
     private String nameDept; //--门诊就诊科室
     private String pkEmpPhy; //   --门诊医生
     private String nameEmpPhy; // --门诊医生姓名

     private String diagname; //--地区诊断名称 
     private String nameDiag; //--诊断名称
     private String descDiag;//诊断描述
     private String pkHp ;
     private String pkDiag ;
     private String pkDept ;
     
     private String pkInNotice;//入院通知单主键
     private String pkPvIp;//就诊编码_门诊
     private String pkPvOp;//就诊编码_住院
     private String euStatus;//通知单状态
     private String dateValid;//有效期
     private String pkDeptIp;//入院科室
     private String pkDeptNsIp;//入院病区
     private String pkDiagMaj;//入院诊断
     private String descDiagMaj;//入院诊断描述
     private String dtLevelDise;//病情状况
     private String dateAdmit;//计划入院日期
     private String amtDepo;//预计预交金额度
     private String note;//备注
	 private String dtWayIp;//入院方式
	 private String flagReadm;//十日内再入院
	 private String flagSygery;//日间手术
     

	private Integer isExists;//是否存在入院通知单
     
     private String bedNo;
     private String contactDept;

     private String flagCovidCheck;
     private Date dateCovid;
     private String euResultCovid;

	private String flagChgDept;// 允许跨科收治'

	private String flagIcu;// 收治ICU

	private String flagSpec;// 收治特诊

	private String flagIsolate;// 是否隔离

	private String nameRel;// 联系人

	private String telRel;// 联系电话

	private String dtPatCls;// 患者分类
     
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getContactDept() {
		return contactDept;
	}
	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}
	public Integer getIsExists() {
		return isExists;
	}
	public void setIsExists(Integer isExists) {
		this.isExists = isExists;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkDeptIp() {
		return pkDeptIp;
	}
	public void setPkDeptIp(String pkDeptIp) {
		this.pkDeptIp = pkDeptIp;
	}
	public String getPkDeptNsIp() {
		return pkDeptNsIp;
	}
	public void setPkDeptNsIp(String pkDeptNsIp) {
		this.pkDeptNsIp = pkDeptNsIp;
	}
	public String getPkDiagMaj() {
		return pkDiagMaj;
	}
	public void setPkDiagMaj(String pkDiagMaj) {
		this.pkDiagMaj = pkDiagMaj;
	}
	public String getDescDiagMaj() {
		return descDiagMaj;
	}
	public void setDescDiagMaj(String descDiagMaj) {
		this.descDiagMaj = descDiagMaj;
	}
	public String getDateAdmit() {
		return dateAdmit;
	}
	public String getDtLevelDise() {
		return dtLevelDise;
	}
	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
	}
	public void setDateAdmit(String dateAdmit) {
		this.dateAdmit = dateAdmit;
	}
	public String getAmtDepo() {
		return amtDepo;
	}
	public void setAmtDepo(String amtDepo) {
		this.amtDepo = amtDepo;
	}
	public String getPkInNotice() {
		return pkInNotice;
	}
	public void setPkInNotice(String pkInNotice) {
		this.pkInNotice = pkInNotice;
	}
	public String getPkPvIp() {
		return pkPvIp;
	}
	public void setPkPvIp(String pkPvIp) {
		this.pkPvIp = pkPvIp;
	}
	public String getPkPvOp() {
		return pkPvOp;
	}
	public void setPkPvOp(String pkPvOp) {
		this.pkPvOp = pkPvOp;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getDateValid() {
		return dateValid;
	}
	public void setDateValid(String dateValid) {
		this.dateValid = dateValid;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
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
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
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
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getDtWayIp() {
		return dtWayIp;
	}

	public void setDtWayIp(String dtWayIp) {
		this.dtWayIp = dtWayIp;
	}

	public String getFlagReadm() {
		return flagReadm;
	}

	public void setFlagReadm(String flagReadm) {
		this.flagReadm = flagReadm;
	}

	public String getFlagCovidCheck() {
		return flagCovidCheck;
	}

	public void setFlagCovidCheck(String flagCovidCheck) {
		this.flagCovidCheck = flagCovidCheck;
	}

	public Date getDateCovid() {
		return dateCovid;
	}

	public void setDateCovid(Date dateCovid) {
		this.dateCovid = dateCovid;
	}

	public String getEuResultCovid() {
		return euResultCovid;
	}

	public void setEuResultCovid(String euResultCovid) {
		this.euResultCovid = euResultCovid;
	}

	public String getFlagChgDept() {
		return flagChgDept;
	}

	public void setFlagChgDept(String flagChgDept) {
		this.flagChgDept = flagChgDept;
	}

	public String getFlagIcu() {
		return flagIcu;
	}

	public void setFlagIcu(String flagIcu) {
		this.flagIcu = flagIcu;
	}

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}

	public String getFlagIsolate() {
		return flagIsolate;
	}

	public void setFlagIsolate(String flagIsolate) {
		this.flagIsolate = flagIsolate;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getTelRel() {
		return telRel;
	}

	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}

	public String getDtPatCls() {
		return dtPatCls;
	}

	public void setDtPatCls(String dtPatCls) {
		this.dtPatCls = dtPatCls;
	}	

    public String getFlagSygery() {
		return flagSygery;
	}
	public void setFlagSygery(String flagSygery) {
		this.flagSygery = flagSygery;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
}
