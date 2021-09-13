package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;


import java.util.Date;

public class OpPatiInfo {

	 private String pkPi; //   --患者主键
     private String pkPv; //   --门诊就诊主键
     private String codePi; // --患者编码
	 private String codeOp;//门诊号
     private String namePi; // --姓名
     private String sexCode; //  --性别编码
	 private String addrCurDt;
	private String codeDeptOp;
	private String nameDeptOp;
	private String pkEmpOp;
	private Date ts;
	private String telRel;
	private String nameDiag;
	private String codeicd;
	private String nameRel;
	private String codeOrg;
	private  String agePv;
	private Date birthDate;
	private  String codeCate;
	private String mobile; //     --手机号码
	private String nameDept;
	private String codeDept;
	private String idNo; //      --证件号码
	private String dtLevelDise;//病情状况
	private Date dateAdmit;//计划入院日期
	private String pkEmpPhy; //   --门诊医生
	private String nameEmpPhy; // --门诊医生姓名
	private String dtWayIp;//入院方式
	private String note;//备注
	private String bedNo;
	private String codePv; //    --门诊就诊号码
	private String flagCovidCheck;
	private Date dateCovid;
	private String euResultCovid;
	private  String pkDiagMaj;
	private String flagIsolate;
	private String flagSpec;
	private String flagChgDept;
	private String flagIcu;
	private String dtPatCls;
	private String wardCode;
	private String wardName;

	public String getFlagIsolate() {
		return flagIsolate;
	}

	public void setFlagIsolate(String flagIsolate) {
		this.flagIsolate = flagIsolate;
	}

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
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

	public String getDtPatCls() {
		return dtPatCls;
	}

	public void setDtPatCls(String dtPatCls) {
		this.dtPatCls = dtPatCls;
	}

	public String getPkDiagMaj() {
		return pkDiagMaj;
	}

	public void setPkDiagMaj(String pkDiagMaj) {
		this.pkDiagMaj = pkDiagMaj;
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

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getCodeCate() {
		return codeCate;
	}

	public void setCodeCate(String codeCate) {
		this.codeCate = codeCate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}

	public String getCodeOrg() {
		return codeOrg;
	}

	public void setCodeOrg(String codeOrg) {
		this.codeOrg = codeOrg;
	}

	public String getNameRel() {
		return nameRel;
	}

	public void setNameRel(String nameRel) {
		this.nameRel = nameRel;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getCodeicd() {
		return codeicd;
	}

	public void setCodeicd(String codeicd) {
		this.codeicd = codeicd;
	}

	public String getTelRel() {
		return telRel;
	}

	public void setTelRel(String telRel) {
		this.telRel = telRel;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getPkEmpOp() {
		return pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {
		this.pkEmpOp = pkEmpOp;
	}

	public String getCodeDeptOp() {
		return codeDeptOp;
	}

	public void setCodeDeptOp(String codeDeptOp) {
		this.codeDeptOp = codeDeptOp;
	}

	public String getNameDeptOp() {
		return nameDeptOp;
	}

	public void setNameDeptOp(String nameDeptOp) {
		this.nameDeptOp = nameDeptOp;
	}

	public String getAddrCurDt() {
		return addrCurDt;
	}

	public void setAddrCurDt(String addrCurDt) {
		this.addrCurDt = addrCurDt;
	}

	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}


	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public String getDtLevelDise() {
		return dtLevelDise;
	}
	public void setDtLevelDise(String dtLevelDise) {
		this.dtLevelDise = dtLevelDise;
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

	public String getSexCode() {
		return sexCode;
	}

	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
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


	public String getDtWayIp() {
		return dtWayIp;
	}

	public void setDtWayIp(String dtWayIp) {
		this.dtWayIp = dtWayIp;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
}
