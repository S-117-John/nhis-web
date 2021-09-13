package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_SUZHOUNH_WEB_REGINFO
 * 
 * @since 2019-04-22 04:57:56
 */
@Table(value = "INS_SUZHOUNH_WEB_REGINFO")
public class InsSuzhounhWebReginfo extends BaseModule {

	/** ID - 主键 */
	@PK
	@Field(value = "ID", id = KeyId.UUID)
	private String id;

	/** OPTYPE - OPTYPE-登记类型: */
	@Field(value = "OPTYPE")
	private String optype;

	/** INPATIENTSN - INPATIENTSN-就诊号 */
	@Field(value = "INPATIENTSN")
	private String inpatientsn;

	/** HOSORGNO - HOSORGNO-医疗机构编码 */
	@Field(value = "HOSORGNO")
	private String hosorgno;

	/** INPATIENTNO - INPATIENTNO-HIS住院号 */
	@Field(value = "INPATIENTNO")
	private String inpatientno;

	/** CENTERNO - CENTERNO-农合中心编码 */
	@Field(value = "CENTERNO")
	private String centerno;

	/** MEMBERNAME - MEMBERNAME-姓名(必须先通过GetPersonInfo得到病人信息中的姓名) */
	@Field(value = "MEMBERNAME")
	private String membername;

	/** FAMILYSYSNO - FAMILYSYSNO-家庭编号 */
	@Field(value = "FAMILYSYSNO")
	private String familysysno;

	/** MEMBERSYSNO - MEMBERSYSNO-个人编号 */
	@Field(value = "MEMBERSYSNO")
	private String membersysno;

	/** COUNTRYTEAMCODE - COUNTRYTEAMCODE-辖区编码 */
	@Field(value = "COUNTRYTEAMCODE")
	private String countryteamcode;

	/** SEXID - SEXID-性别代码 */
	@Field(value = "SEXID")
	private String sexid;

	/** IDCARDNO - IDCARDNO-身份证 */
	@Field(value = "IDCARDNO")
	private String idcardno;

	/** AGE - AGE-年龄 */
	@Field(value = "AGE")
	private String age;

	/** BOOKNO - BOOKNO-医疗证号 */
	@Field(value = "BOOKNO")
	private String bookno;

	/** CARDNO - CARDNO-医疗卡号 */
	@Field(value = "CARDNO")
	private String cardno;

	/** ICDALLNO - ICDALLNO-农合疾病诊断 */
	@Field(value = "ICDALLNO")
	private String icdallno;

	/** OPSID - OPSID-手术编号 */
	@Field(value = "OPSID")
	private String opsid;

	/** INOFFICEID - INOFFICEID-入院科室编码 */
	@Field(value = "INOFFICEID")
	private String inofficeid;

	/** OFFICEDATE - OFFICEDATE-住院日期 */
	@Field(value = "OFFICEDATE")
	private String officedate;

	/** CUREID - CUREID-就诊类型 */
	@Field(value = "CUREID")
	private String cureid;

	/** INHOSID - INHOSID-来院状态 */
	@Field(value = "INHOSID")
	private String inhosid;

	/** CUREDOCTOR - CUREDOCTOR-经治医生 */
	@Field(value = "CUREDOCTORNO")
	private String curedoctorno;
	
	/** CUREDOCTOR - CUREDOCTOR-经治医生 */
	@Field(value = "CUREDOCTORNAME")
	private String curedoctorname;

	/** BEDNO - BEDNO-床位号码 */
	@Field(value = "BEDNO")
	private String bedno;

	/** SECTIONNO - SECTIONNO-入院病区 */
	@Field(value = "SECTIONNO")
	private String sectionno;
	
	/** SECTIONNO - SECTIONNO-入院病区 */
	@Field(value = "SECTIONNAME")
	private String sectionname;

	/** LEAVEDATE - LEAVEDATE-出院日期 */
	@Field(value = "LEAVEDATE")
	private String leavedate;

	/** DAYS - DAYS-实际住院天数(要扣除中间不住院的天数) */
	@Field(value = "DAYS")
	private String days;

	/** OUTOFFICEID - OUTOFFICEID-出院科室编码 */
	@Field(value = "OUTOFFICEID")
	private String outofficeid;

	/** OUTHOSID - OUTHOSID-出院状态 */
	@Field(value = "OUTHOSID")
	private String outhosid;

	/** TURNOP - TURNOP-转诊类型(01 */
	@Field(value = "TURNOP")
	private String turnop;

	/** TURNORGNO - TURNORGNO-转入(出)医院(填写从哪个医院转入(出)的对应医疗机构的编号) */
	@Field(value = "TURNORGNO")
	private String turnorgno;

	/** TURNDATE - TURNDATE-转院日期 */
	@Field(value = "TURNDATE")
	private String turndate;

	/** TICKETNO - TICKETNO-医院住院收费收据号 */
	@Field(value = "TICKETNO")
	private String ticketno;

	/** MINISTERNOTICE - MINISTERNOTICE-民政通知书号 */
	@Field(value = "MINISTERNOTICE")
	private String ministernotice;

	/** PROCREATENOTICE - PROCREATENOTICE-生育证号 */
	@Field(value = "PROCREATENOTICE")
	private String procreatenotice;

	/** OPERATEEMPSYSNO - OPERATEEMPSYSNO-经办人农合登录用户名 */
	@Field(value = "OPERATEEMPSYSNO")
	private String operateempsysno;

	/** HOSLEVELID - HOSLEVELID-医疗机构级别ID */
	@Field(value = "HOSLEVELID")
	private String hoslevelid;

	/** FAMILYADDRESS - FAMILYADDRESS-家庭住址 */
	@Field(value = "FAMILYADDRESS")
	private String familyaddress;

	/** IDENAME - IDENAME-人员性质 */
	@Field(value = "IDENAME")
	private String idename;

	@Field(value = "PKPV")
	private String pkpv;
	
	/** USERNAME - USERNAME-登陆农合的用户名 */
	@Field(value = "USERNAME")
	private String username;

	/** USERPWD - USERPWD-登陆农合的密码 */
	@Field(value = "USERPWD")
	private String userpwd;

	/** USERPWD - USERPWD-第二疾病诊断*/
	@Field(value = "SECONDICDNO")
	private String secondicdno;
	
	/** TREATCODE - TREATCODE-治疗方式*/
	@Field(value = "TREATCODE")
	private String treatcode;

	/** IDENAME - IDENAME-并发症 */
	@Field(value = "COMPLICATION")
	private String complication;
	
	@Field(value = "OBLIGATEONE")
	private String obligateone;

	@Field(value = "OBLIGATETWO")
	private String obligatetwo;

	@Field(value = "OBLIGATETHREE")
	private String obligatethree;

	@Field(value = "OBLIGATEFOUR")
	private String obligatefour;

	@Field(value = "OBLIGATEFIVE")
	private String obligatefive;
	
	@Field(value = "INSTYPE")
	private String instype;
	
	@Field(value = "DRGS")
	private String drgs;

	/** MODIFY_TIME - 最后操作时间 */
	@Field(value = "MODIFY_TIME")
	private Date modifyTime;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOptype() {
		return this.optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public String getInpatientsn() {
		return this.inpatientsn;
	}

	public void setInpatientsn(String inpatientsn) {
		this.inpatientsn = inpatientsn;
	}

	public String getHosorgno() {
		return this.hosorgno;
	}

	public void setHosorgno(String hosorgno) {
		this.hosorgno = hosorgno;
	}

	public String getInpatientno() {
		return this.inpatientno;
	}

	public void setInpatientno(String inpatientno) {
		this.inpatientno = inpatientno;
	}

	public String getCenterno() {
		return this.centerno;
	}

	public void setCenterno(String centerno) {
		this.centerno = centerno;
	}

	public String getMembername() {
		return this.membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public String getFamilysysno() {
		return this.familysysno;
	}

	public void setFamilysysno(String familysysno) {
		this.familysysno = familysysno;
	}

	public String getMembersysno() {
		return this.membersysno;
	}

	public void setMembersysno(String membersysno) {
		this.membersysno = membersysno;
	}

	public String getCountryteamcode() {
		return this.countryteamcode;
	}

	public void setCountryteamcode(String countryteamcode) {
		this.countryteamcode = countryteamcode;
	}

	public String getSexid() {
		return this.sexid;
	}

	public void setSexid(String sexid) {
		this.sexid = sexid;
	}

	public String getIdcardno() {
		return this.idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBookno() {
		return this.bookno;
	}

	public void setBookno(String bookno) {
		this.bookno = bookno;
	}

	public String getCardno() {
		return this.cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getIcdallno() {
		return this.icdallno;
	}

	public void setIcdallno(String icdallno) {
		this.icdallno = icdallno;
	}

	public String getOpsid() {
		return this.opsid;
	}

	public void setOpsid(String opsid) {
		this.opsid = opsid;
	}

	public String getInofficeid() {
		return this.inofficeid;
	}

	public void setInofficeid(String inofficeid) {
		this.inofficeid = inofficeid;
	}

	public String getOfficedate() {
		return this.officedate;
	}

	public void setOfficedate(String officedate) {
		this.officedate = officedate;
	}

	public String getCureid() {
		return this.cureid;
	}

	public void setCureid(String cureid) {
		this.cureid = cureid;
	}

	public String getInhosid() {
		return this.inhosid;
	}

	public void setInhosid(String inhosid) {
		this.inhosid = inhosid;
	}

	public String getBedno() {
		return this.bedno;
	}

	public void setBedno(String bedno) {
		this.bedno = bedno;
	}

	public String getSectionno() {
		return this.sectionno;
	}

	public void setSectionno(String sectionno) {
		this.sectionno = sectionno;
	}

	public String getLeavedate() {
		return this.leavedate;
	}

	public void setLeavedate(String leavedate) {
		this.leavedate = leavedate;
	}

	public String getDays() {
		return this.days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getOutofficeid() {
		return this.outofficeid;
	}

	public void setOutofficeid(String outofficeid) {
		this.outofficeid = outofficeid;
	}

	public String getOuthosid() {
		return this.outhosid;
	}

	public void setOuthosid(String outhosid) {
		this.outhosid = outhosid;
	}

	public String getTurnop() {
		return this.turnop;
	}

	public void setTurnop(String turnop) {
		this.turnop = turnop;
	}

	public String getTurnorgno() {
		return this.turnorgno;
	}

	public void setTurnorgno(String turnorgno) {
		this.turnorgno = turnorgno;
	}

	public String getTurndate() {
		return this.turndate;
	}

	public void setTurndate(String turndate) {
		this.turndate = turndate;
	}

	public String getTicketno() {
		return this.ticketno;
	}

	public void setTicketno(String ticketno) {
		this.ticketno = ticketno;
	}

	public String getMinisternotice() {
		return this.ministernotice;
	}

	public void setMinisternotice(String ministernotice) {
		this.ministernotice = ministernotice;
	}

	public String getProcreatenotice() {
		return this.procreatenotice;
	}

	public void setProcreatenotice(String procreatenotice) {
		this.procreatenotice = procreatenotice;
	}

	public String getOperateempsysno() {
		return this.operateempsysno;
	}

	public void setOperateempsysno(String operateempsysno) {
		this.operateempsysno = operateempsysno;
	}

	public String getHoslevelid() {
		return this.hoslevelid;
	}

	public void setHoslevelid(String hoslevelid) {
		this.hoslevelid = hoslevelid;
	}

	public String getPkpv() {
		return this.pkpv;
	}

	public void setPkpv(String pkpv) {
		this.pkpv = pkpv;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCuredoctorno() {
		return curedoctorno;
	}

	public void setCuredoctorno(String curedoctorno) {
		this.curedoctorno = curedoctorno;
	}

	public String getCuredoctorname() {
		return curedoctorname;
	}

	public void setCuredoctorname(String curedoctorname) {
		this.curedoctorname = curedoctorname;
	}

	public String getSectionname() {
		return sectionname;
	}

	public void setSectionname(String sectionname) {
		this.sectionname = sectionname;
	}

	

	public String getComplication() {
		return complication;
	}

	public void setComplication(String complication) {
		this.complication = complication;
	}

	public String getFamilyaddress() {
		return familyaddress;
	}

	public void setFamilyaddress(String familyaddress) {
		this.familyaddress = familyaddress;
	}

	public String getIdename() {
		return idename;
	}

	public void setIdename(String idename) {
		this.idename = idename;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserpwd() {
		return userpwd;
	}

	public void setUserpwd(String userpwd) {
		this.userpwd = userpwd;
	}

	public String getSecondicdno() {
		return secondicdno;
	}

	public void setSecondicdno(String secondicdno) {
		this.secondicdno = secondicdno;
	}

	public String getTreatcode() {
		return treatcode;
	}

	public void setTreatcode(String treatcode) {
		this.treatcode = treatcode;
	}

	public String getObligateone() {
		return obligateone;
	}

	public void setObligateone(String obligateone) {
		this.obligateone = obligateone;
	}

	public String getObligatetwo() {
		return obligatetwo;
	}

	public void setObligatetwo(String obligatetwo) {
		this.obligatetwo = obligatetwo;
	}

	public String getObligatethree() {
		return obligatethree;
	}

	public void setObligatethree(String obligatethree) {
		this.obligatethree = obligatethree;
	}

	public String getObligatefour() {
		return obligatefour;
	}

	public void setObligatefour(String obligatefour) {
		this.obligatefour = obligatefour;
	}

	public String getObligatefive() {
		return obligatefive;
	}

	public void setObligatefive(String obligatefive) {
		this.obligatefive = obligatefive;
	}

	public String getInstype() {
		return instype;
	}

	public void setInstype(String instype) {
		this.instype = instype;
	}

	public String getDrgs() {
		return drgs;
	}

	public void setDrgs(String drgs) {
		this.drgs = drgs;
	}
}