package com.zebone.nhis.common.module.compay.ins.shenzhen.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZYB_VISIT_XNH 医保登记_新农合
 * 
 * @since 2019-12-12 10:45:53
 */
@Table(value = "INS_GZYB_VISIT_XNH")
public class SzybVisitXnh extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_VISITXNH - 主键 */
	@PK
	@Field(value = "PK_VISITXNH", id = KeyId.UUID)
	private String pkVisitXnh;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** PK_VISIT-关联就诊登记 */
	@Field(value = "PK_VISIT")
	private String pkVisit;

	/** CODE_HZCHDQ-患者参合地区编码 */
	@Field(value = "CODE_HZCHDQ")
	private String codeHzchdq;

	/** CODE_PV-住院登记流水号 */
	@Field(value = "CODE_PV")
	private String codePv;

	/** CODE_PVORG-就医机构代码 */
	@Field(value = "CODE_PVORG")
	private String codePvOrg;

	/** NAME_PVORG-就医机构名称 */
	@Field(value = "NAME_PVORG")
	private String namePvOrg;

	/** ADDRESS-患者通讯地址 */
	@Field(value = "ADDRESS")
	private String address;

	/** CODE_CHS-参合省代码 */
	@Field(value = "CODE_CHS")
	private String codeChs;

	/** NAME_CHS-参合省名称 */
	@Field(value = "NAME_CHS")
	private String nameChs;

	/** CODE_CHSH-参合市代码 */
	@Field(value = "CODE_CHSH")
	private String codeChsh;

	/** NAME_CHSH-参合市名称 */
	@Field(value = "NAME_CHSH")
	private String nameChsh;

	/** CODE_CHQ-参合区代码 */
	@Field(value = "CODE_CHQ")
	private String codeChq;

	/** NAME_CHQ-参合区名称 */
	@Field(value = "NAME_CHQ")
	private String nameChq;

	/** CODE_FAMI-家庭编码 */
	@Field(value = "CODE_FAMI")
	private String codeFami;

	/** PERSONNO-个人编码 */
	@Field(value = "PERSONNO")
	private String personno;

	/** STATURE-身高 */
	@Field(value = "STATURE")
	private String stature;

	/** WEIGHT-体重 */
	@Field(value = "WEIGHT")
	private String weight;

	/** CODE_DISE-住院疾病诊断代码 */
	@Field(value = "CODE_DISE")
	private String codeDise;

	/** NAME_DISE-住院疾病诊断名称 */
	@Field(value = "NAME_DISE")
	private String nameDise;

	/** CODE_SECOND_DISE-第二疾病诊断代码 */
	@Field(value = "CODE_SECOND_DISE")
	private String codeSecondDise;

	/** NAME_SECOND_DISE-第二疾病诊断名称 */
	@Field(value = "NAME_SECOND_DISE")
	private String nameSecondDise;

	/** CODE_OPPR-手术编码 */
	@Field(value = "CODE_OPPR")
	private String codeOppr;

	/** NAME_OPPR-手术名称 */
	@Field(value = "NAME_OPPR")
	private String nameOppr;

	/** CODE_TREA-治疗编码 */
	@Field(value = "CODE_TREA")
	private String codeTrea;

	/** NAME_TREA-治疗名称 */
	@Field(value = "NAME_TREA")
	private String nameTrea;

	/** CODE_DEPT_ADMIT-入院科室代码 */
	@Field(value = "CODE_DEPT_ADMIT")
	private String codeDeptAdmit;

	/** NAME_DEPT_ADMIT-入院科室名称 */
	@Field(value = "NAME_DEPT_ADMIT")
	private String nameDeptAdmit;

	/** CODE_PVTYPE-就诊类型代码 */
	@Field(value = "CODE_PVTYPE")
	private String codePvType;

	/** NAME_PVTYPE-就诊类型名称 */
	@Field(value = "NAME_PVTYPE")
	private String namePvType;

	/** CODE_COMP-并发症编码 */
	@Field(value = "CODE_COMP")
	private String codeComp;

	/** NAME_COMP-并发症名称 */
	@Field(value = "NAME_COMP")
	private String nameComp;

	/** CODE_STATUS_ADMIT-来院状态代码 */
	@Field(value = "CODE_STATUS_ADMIT")
	private String codeStatusAdmit;

	/** NAME_STATUS_ADMI-来院状态名称 */
	@Field(value = "NAME_STATUS_ADMI")
	private String nameStatusAdmi;

	/** NAME_REL-联系人姓名 */
	@Field(value = "NAME_REL")
	private String nameRel;

	/** TEL_REL-电话号码 */
	@Field(value = "TEL_REL")
	private String telRel;

	/** NAME_DOCT-医生姓名 */
	@Field(value = "NAME_DOCT")
	private String nameDoct;

	/** DATE_ADMIT-入院日期 */
	@Field(value = "DATE_ADMIT")
	private Date dateAdmit;

	/** CODE_IP-住院号 */
	@Field(value = "CODE_IP")
	private String codeIp;

	/** CODE_BED-床位号 */
	@Field(value = "CODE_BED")
	private String codeBed;

	/** NAME_NS_ADMIT-入院病区 */
	@Field(value = "NAME_NS_ADMIT")
	private String nameNsAdmit;

	/** CODE_ZZLX-转诊类型代码 */
	@Field(value = "CODE_ZZLX")
	private String codeZzlx;

	/** NAME_ZZLX-转诊类型名称 */
	@Field(value = "NAME_ZZLX")
	private String nameZzlx;

	/** CODE_DOC-转诊单号 */
	@Field(value = "CODE_DOC")
	private String codeDoc;

	/** DATE_ZY-转院日期 */
	@Field(value = "DATE_ZY")
	private Date dateZy;

	/** RECEIPT_NO-医院住院收费收据号 */
	@Field(value = "RECEIPT_NO")
	private String receiptNo;

	/** CODE_MZTZS-民政通知书号 */
	@Field(value = "CODE_MZTZS")
	private String codeMztzs;

	/** CODE_SYZ-生育证号 */
	@Field(value = "CODE_SYZ")
	private String codeSyz;

	/** CODE_YLZ-医疗证/卡号 */
	@Field(value = "CODE_YLZ")
	private String codeYlz;

	/** BIRTHDAY-出生日期 */
	@Field(value = "BIRTHDAY")
	private Date birthday;

	/** DATE_CREATE-创建时间 */
	@Field(value = "DATE_CREATE")
	private Date dateCreate;

	/** DATE_UPDATE-更新时间 */
	@Field(value = "DATE_UPDATE")
	private Date dateUpdate;

	/** CODE_USER-医院信息系统操作者代码 */
	@Field(value = "CODE_USER")
	private String codeUser;

	/** NAME_USER-医院信息系统操作者姓名 */
	@Field(value = "NAME_USER")
	private String nameUser;

	/** AMT_BNDLJBX-本年度累计报销金额 */
	@Field(value = "AMT_BNDLJBX")
	private Double amtBndljbx;

	/** TIMES_BNDLJ-本年度累计报销次数 */
	@Field(value = "TIMES_BNDLJ")
	private String timesBndlj;

	/** AMT_LJTBTC-累计大病统筹金额 */
	@Field(value = "AMT_LJTBTC")
	private Double amtLjtbtc;

	/** AMT_LJDBBXYBC-累计大病保险已补偿金额 */
	@Field(value = "AMT_LJDBBXYBC")
	private Double amtLjdbbxybc;

	/** AMT_YKJDBBXQFX-已扣减大病保险起付线金额 */
	@Field(value = "AMT_YKJDBBXQFX")
	private Double amtYkjdbbxqfx;

	/** FLAG_JZFPDX-是否精准扶贫对象 */
	@Field(value = "FLAG_JZFPDX")
	private String flagJzfpdx;

	/** CREATOR-创建人 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME-创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG-删除标志 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS-时间戳 */
	@Field(value = "TS")
	private String ts;

	public String getPkVisitXnh() {
		return pkVisitXnh;
	}

	public void setPkVisitXnh(String pkVisitXnh) {
		this.pkVisitXnh = pkVisitXnh;
	}

	public String getPkVisit() {
		return pkVisit;
	}

	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
	}

	public String getCodeHzchdq() {
		return codeHzchdq;
	}

	public void setCodeHzchdq(String codeHzchdq) {
		this.codeHzchdq = codeHzchdq;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodePvOrg() {
		return codePvOrg;
	}

	public void setCodePvOrg(String codePvOrg) {
		this.codePvOrg = codePvOrg;
	}

	public String getNamePvOrg() {
		return namePvOrg;
	}

	public void setNamePvOrg(String namePvOrg) {
		this.namePvOrg = namePvOrg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCodeChs() {
		return codeChs;
	}

	public void setCodeChs(String codeChs) {
		this.codeChs = codeChs;
	}

	public String getNameChs() {
		return nameChs;
	}

	public void setNameChs(String nameChs) {
		this.nameChs = nameChs;
	}

	public String getCodeChsh() {
		return codeChsh;
	}

	public void setCodeChsh(String codeChsh) {
		this.codeChsh = codeChsh;
	}

	public String getNameChsh() {
		return nameChsh;
	}

	public void setNameChsh(String nameChsh) {
		this.nameChsh = nameChsh;
	}

	public String getCodeChq() {
		return codeChq;
	}

	public void setCodeChq(String codeChq) {
		this.codeChq = codeChq;
	}

	public String getNameChq() {
		return nameChq;
	}

	public void setNameChq(String nameChq) {
		this.nameChq = nameChq;
	}

	public String getCodeFami() {
		return codeFami;
	}

	public void setCodeFami(String codeFami) {
		this.codeFami = codeFami;
	}

	public String getPersonno() {
		return personno;
	}

	public void setPersonno(String personno) {
		this.personno = personno;
	}

	public String getStature() {
		return stature;
	}

	public void setStature(String stature) {
		this.stature = stature;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getCodeDise() {
		return codeDise;
	}

	public void setCodeDise(String codeDise) {
		this.codeDise = codeDise;
	}

	public String getNameDise() {
		return nameDise;
	}

	public void setNameDise(String nameDise) {
		this.nameDise = nameDise;
	}

	public String getCodeSecondDise() {
		return codeSecondDise;
	}

	public void setCodeSecondDise(String codeSecondDise) {
		this.codeSecondDise = codeSecondDise;
	}

	public String getNameSecondDise() {
		return nameSecondDise;
	}

	public void setNameSecondDise(String nameSecondDise) {
		this.nameSecondDise = nameSecondDise;
	}

	public String getCodeOppr() {
		return codeOppr;
	}

	public void setCodeOppr(String codeOppr) {
		this.codeOppr = codeOppr;
	}

	public String getNameOppr() {
		return nameOppr;
	}

	public void setNameOppr(String nameOppr) {
		this.nameOppr = nameOppr;
	}

	public String getCodeTrea() {
		return codeTrea;
	}

	public void setCodeTrea(String codeTrea) {
		this.codeTrea = codeTrea;
	}

	public String getNameTrea() {
		return nameTrea;
	}

	public void setNameTrea(String nameTrea) {
		this.nameTrea = nameTrea;
	}

	public String getCodeDeptAdmit() {
		return codeDeptAdmit;
	}

	public void setCodeDeptAdmit(String codeDeptAdmit) {
		this.codeDeptAdmit = codeDeptAdmit;
	}

	public String getNameDeptAdmit() {
		return nameDeptAdmit;
	}

	public void setNameDeptAdmit(String nameDeptAdmit) {
		this.nameDeptAdmit = nameDeptAdmit;
	}

	public String getCodePvType() {
		return codePvType;
	}

	public void setCodePvType(String codePvType) {
		this.codePvType = codePvType;
	}

	public String getNamePvType() {
		return namePvType;
	}

	public void setNamePvType(String namePvType) {
		this.namePvType = namePvType;
	}

	public String getCodeComp() {
		return codeComp;
	}

	public void setCodeComp(String codeComp) {
		this.codeComp = codeComp;
	}

	public String getNameComp() {
		return nameComp;
	}

	public void setNameComp(String nameComp) {
		this.nameComp = nameComp;
	}

	public String getCodeStatusAdmit() {
		return codeStatusAdmit;
	}

	public void setCodeStatusAdmit(String codeStatusAdmit) {
		this.codeStatusAdmit = codeStatusAdmit;
	}

	public String getNameStatusAdmi() {
		return nameStatusAdmi;
	}

	public void setNameStatusAdmi(String nameStatusAdmi) {
		this.nameStatusAdmi = nameStatusAdmi;
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

	public String getNameDoct() {
		return nameDoct;
	}

	public void setNameDoct(String nameDoct) {
		this.nameDoct = nameDoct;
	}

	public Date getDateAdmit() {
		return dateAdmit;
	}

	public void setDateAdmit(Date dateAdmit) {
		this.dateAdmit = dateAdmit;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getCodeBed() {
		return codeBed;
	}

	public void setCodeBed(String codeBed) {
		this.codeBed = codeBed;
	}

	public String getNameNsAdmit() {
		return nameNsAdmit;
	}

	public void setNameNsAdmit(String nameNsAdmit) {
		this.nameNsAdmit = nameNsAdmit;
	}

	public String getCodeZzlx() {
		return codeZzlx;
	}

	public void setCodeZzlx(String codeZzlx) {
		this.codeZzlx = codeZzlx;
	}

	public String getNameZzlx() {
		return nameZzlx;
	}

	public void setNameZzlx(String nameZzlx) {
		this.nameZzlx = nameZzlx;
	}

	public String getCodeDoc() {
		return codeDoc;
	}

	public void setCodeDoc(String codeDoc) {
		this.codeDoc = codeDoc;
	}

	public Date getDateZy() {
		return dateZy;
	}

	public void setDateZy(Date dateZy) {
		this.dateZy = dateZy;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getCodeMztzs() {
		return codeMztzs;
	}

	public void setCodeMztzs(String codeMztzs) {
		this.codeMztzs = codeMztzs;
	}

	public String getCodeSyz() {
		return codeSyz;
	}

	public void setCodeSyz(String codeSyz) {
		this.codeSyz = codeSyz;
	}

	public String getCodeYlz() {
		return codeYlz;
	}

	public void setCodeYlz(String codeYlz) {
		this.codeYlz = codeYlz;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Date getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(Date dateCreate) {
		this.dateCreate = dateCreate;
	}

	public Date getDateUpdate() {
		return dateUpdate;
	}

	public void setDateUpdate(Date dateUpdate) {
		this.dateUpdate = dateUpdate;
	}

	public String getCodeUser() {
		return codeUser;
	}

	public void setCodeUser(String codeUser) {
		this.codeUser = codeUser;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public Double getAmtBndljbx() {
		return amtBndljbx;
	}

	public void setAmtBndljbx(Double amtBndljbx) {
		this.amtBndljbx = amtBndljbx;
	}

	public String getTimesBndlj() {
		return timesBndlj;
	}

	public void setTimesBndlj(String timesBndlj) {
		this.timesBndlj = timesBndlj;
	}

	public Double getAmtLjtbtc() {
		return amtLjtbtc;
	}

	public void setAmtLjtbtc(Double amtLjtbtc) {
		this.amtLjtbtc = amtLjtbtc;
	}

	public Double getAmtLjdbbxybc() {
		return amtLjdbbxybc;
	}

	public void setAmtLjdbbxybc(Double amtLjdbbxybc) {
		this.amtLjdbbxybc = amtLjdbbxybc;
	}

	public Double getAmtYkjdbbxqfx() {
		return amtYkjdbbxqfx;
	}

	public void setAmtYkjdbbxqfx(Double amtYkjdbbxqfx) {
		this.amtYkjdbbxqfx = amtYkjdbbxqfx;
	}

	public String getFlagJzfpdx() {
		return flagJzfpdx;
	}

	public void setFlagJzfpdx(String flagJzfpdx) {
		this.flagJzfpdx = flagJzfpdx;
	}
	
	
}
