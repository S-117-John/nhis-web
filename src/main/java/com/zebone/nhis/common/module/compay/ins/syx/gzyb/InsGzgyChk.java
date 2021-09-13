package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZGY_CHK - INS_GZGY_CHK 
 *
 * 
 */
@Table(value="INS_GZGY_CHK")
public class InsGzgyChk {
	@Field(value="PK_GZGYCHK",id=KeyId.UUID)
	private String pkGzgychk;
	
	@Field(value="EU_CHKTYPE")
	private String euChktype;
	
	@Field(value="PK_DEPT")
	private String pkDept;
	
	@Field(value="NAME_DEPT")
	private String nameDept;
	
	@Field(value="PK_EMP_APP")
	private String pkEmpApp;
	
	@Field(value="NAME_EMP_APP")
	private String nameEmpApp;
	
	@Field(value="DATE_APP")
	private Date dateApp;
	
	@Field(value="PK_PI")
	private String pkPi;
	
	@Field(value="NAME_PI")
	private String namePi;
	
	@Field(value="SEX")
	private String sex;
	
	@Field(value="BIRTH_DATE")
	private Date birthDate;
	
	@Field(value="IDNO")
	private String idno;
	
	@Field(value="INSUR_NO")
	private String insurNo;
	
	@Field(value="PK_PV")
	private String pkPv;
	
	@Field(value="EU_PVTYPE")
	private String euPvtype;
	
	@Field(value="PK_HP")
	private String pkHp;
	
	@Field(value="NAME_HP")
	private String nameHp;
	
	@Field(value="CODE_DIAG")
	private String codeDiag;
	
	@Field(value="NAME_DIAG")
	private String nameDiag;
	
	@Field(value="PK_CNORD")
	private String pkCnord;
	
	@Field(value="EU_ITEMTYPE")
	private String euItemtype;
	
	@Field(value="PK_ITEM")
	private String pkItem;
	
	@Field(value="NAME_ITEM")
	private String nameItem;
	
	@Field(value="PRICE")
	private Double price;
	
	@Field(value="RATIO_ORG")
	private Double ratioOrg;
	
	@Field(value="RATIO")
	private Double ratio;
	
	@Field(value="DATE_BEGIN")
	private Date dateBegin;
	
	@Field(value="DATE_END")
	private Date dateEnd;
	
	@Field(value="PK_EMP_CHK")
	private String pkEmpChk;
	
	@Field(value="NAME_EMP_CHK")
	private String nameEmpChk;
	
	@Field(value="DATE_CHK")
	private Date dateChk;
	
	@Field(value="FLAG_CHK")
	private String flagChk;
	
	@Field(value="EU_RESULT")
	private String euResult;
	
	@Field(value="FLAG_PRINT")
	private String flagPrint;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="DEL_FLAG")
	private String delFlag = "0";
	
	@Field(date=FieldType.ALL)
	private Date ts;
	
	private String codePi;	//患者编码
	
	private String nameEuResult;	//审批结果

	public String getNameEuResult() {
		return nameEuResult;
	}

	public void setNameEuResult(String nameEuResult) {
		this.nameEuResult = nameEuResult;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getPkGzgychk() {
		return pkGzgychk;
	}

	public void setPkGzgychk(String pkGzgychk) {
		this.pkGzgychk = pkGzgychk;
	}

	public String getEuChktype() {
		return euChktype;
	}

	public void setEuChktype(String euChktype) {
		this.euChktype = euChktype;
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

	public String getPkEmpApp() {
		return pkEmpApp;
	}

	public void setPkEmpApp(String pkEmpApp) {
		this.pkEmpApp = pkEmpApp;
	}

	public String getNameEmpApp() {
		return nameEmpApp;
	}

	public void setNameEmpApp(String nameEmpApp) {
		this.nameEmpApp = nameEmpApp;
	}

	public Date getDateApp() {
		return dateApp;
	}

	public void setDateApp(Date dateApp) {
		this.dateApp = dateApp;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getNameHp() {
		return nameHp;
	}

	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}

	public String getCodeDiag() {
		return codeDiag;
	}

	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}

	public String getNameDiag() {
		return nameDiag;
	}

	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getEuItemtype() {
		return euItemtype;
	}

	public void setEuItemtype(String euItemtype) {
		this.euItemtype = euItemtype;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getRatioOrg() {
		return ratioOrg;
	}

	public void setRatioOrg(Double ratioOrg) {
		this.ratioOrg = ratioOrg;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}

	public Date getDateBegin() {
		return dateBegin;
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

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getFlagChk() {
		return flagChk;
	}

	public void setFlagChk(String flagChk) {
		this.flagChk = flagChk;
	}

	public String getEuResult() {
		return euResult;
	}

	public void setEuResult(String euResult) {
		this.euResult = euResult;
	}

	public String getFlagPrint() {
		return flagPrint;
	}

	public void setFlagPrint(String flagPrint) {
		this.flagPrint = flagPrint;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}
	
	
}
