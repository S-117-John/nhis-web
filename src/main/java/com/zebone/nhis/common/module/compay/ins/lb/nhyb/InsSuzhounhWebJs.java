package com.zebone.nhis.common.module.compay.ins.lb.nhyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_SUZHOUNH_WEB_JS
 * 
 * @since 2019-04-26 06:49:18
 */
@Table(value = "INS_SUZHOUNH_WEB_JS")
public class InsSuzhounhWebJs extends BaseModule {

	/** ID - 主键 */
	@PK
	@Field(value = "ID", id = KeyId.UUID)
	private String id;

	/** PKPV - PKPV-his就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** INPATIENTSN - INPATIENTSN-就诊号 */
	@Field(value = "INPATIENTSN")
	private String inpatientsn;

	/** REDEEMNO - REDEEMNO-补偿单号 */
	@Field(value = "REDEEMNO")
	private String redeemno;

	/** MEMBERNO - MEMBERNO-成员编码 */
	@Field(value = "MEMBERNO")
	private String memberno;

	/** NAME - NAME-成员姓名 */
	@Field(value = "NAME")
	private String name;

	/** BOOKNO - BOOKNO-医疗证、卡号 */
	@Field(value = "BOOKNO")
	private String bookno;

	/** SEXNAME - SEXNAME-性别名称 */
	@Field(value = "SEXNAME")
	private String sexname;

	/** BIRTHDAY - BIRTHDAY-出生年月日 */
	@Field(value = "BIRTHDAY")
	private String birthday;

	/** MASTERNAME - MASTERNAME-户主姓名 */
	@Field(value = "MASTERNAME")
	private String mastername;

	/** RELATIONNAME - RELATIONNAME-与户主关系名称 */
	@Field(value = "RELATIONNAME")
	private String relationname;

	/** IDENTITYNAME - IDENTITYNAME-个人身份属性名称 */
	@Field(value = "IDENTITYNAME")
	private String identityname;

	/** IDCARD - IDCARD-身份证号码 */
	@Field(value = "IDCARD")
	private String idcard;

	/** CURRYEARREDEEMCOUNT - CURRYEARREDEEMCOUNT-当前年度成员住院已补偿次数 */
	@Field(value = "CURRYEARREDEEMCOUNT")
	private String curryearredeemcount;

	/** CURRYEARTOTAL - CURRYEARTOTAL-当前年度成员住院已补偿总医疗费用 */
	@Field(value = "CURRYEARTOTAL")
	private String curryeartotal;

	/** CURRYEARENABLEMONEY - CURRYEARENABLEMONEY-当前年度成员住院已补偿总保内费用 */
	@Field(value = "CURRYEARENABLEMONEY")
	private String curryearenablemoney;

	/** CURRYEARREDDEMMONEY - CURRYEARREDDEMMONEY-当前年度成员住院已补偿金额 */
	@Field(value = "CURRYEARREDDEMMONEY")
	private String curryearreddemmoney;

	/** FAMILYNO - FAMILYNO-成员家庭编码 */
	@Field(value = "FAMILYNO")
	private String familyno;

	/** ADDRESS - ADDRESS-成员家庭住址 */
	@Field(value = "ADDRESS")
	private String address;

	/** DOORPROPNAME - DOORPROPNAME-户属性名称 */
	@Field(value = "DOORPROPNAME")
	private String doorpropname;

	/** JOINPROPNAME - JOINPROPNAME-参合属性名称 */
	@Field(value = "JOINPROPNAME")
	private String joinpropname;

	/** CURRFAMILYREDEEMCOUNT - CURRFAMILYREDEEMCOUNT-当前年度家庭住院已补偿次数 */
	@Field(value = "CURRFAMILYREDEEMCOUNT")
	private String currfamilyredeemcount;

	/** CURRFAMILYTOTAL - CURRFAMILYTOTAL-当前年度家庭住院已补偿总医疗费用 */
	@Field(value = "CURRFAMILYTOTAL")
	private String currfamilytotal;

	/** CURRFAMILYENABLEMONEY - CURRFAMILYENABLEMONEY-当前年度家庭住院已补偿保内费用 */
	@Field(value = "CURRFAMILYENABLEMONEY")
	private String currfamilyenablemoney;

	/** CURRFAMILYREDDEMMONEY - CURRFAMILYREDDEMMONEY-当前年度家庭住院已补偿金额 */
	@Field(value = "CURRFAMILYREDDEMMONEY")
	private String currfamilyreddemmoney;

	/** TOTALCOSTS - TOTALCOSTS-本次住院总医疗费用 */
	@Field(value = "TOTALCOSTS")
	private String totalcosts;

	/** ENABLEMONEY - ENABLEMONEY-本次住院保内费用 */
	@Field(value = "ENABLEMONEY")
	private String enablemoney;

	/**
	 * ESSENTIALMEDICINEMONEY -
	 * ESSENTIALMEDICINEMONEY-本次住院费用中国定基本药品费用(单位元,小数点后保留两位)
	 */
	@Field(value = "ESSENTIALMEDICINEMONEY")
	private String essentialmedicinemoney;

	/** PROVINCEMEDICINEMONEY - PROVINCEMEDICINEMONEY-本次住院费用省补基本药品(单位元,小数点后保留两位) */
	@Field(value = "PROVINCEMEDICINEMONEY")
	private String provincemedicinemoney;

	/** STARTMONEY - STARTMONEY-本次住院补偿扣除起伏线金额（单位元，小数点后保留两位） */
	@Field(value = "STARTMONEY")
	private String startmoney;

	/** REDEEMMONEY - REDEEMMONEY-本次住院补偿金额 */
	@Field(value = "REDEEMMONEY")
	private String redeemmoney;
	
	/** REDEEMTYPENAME - REDEEMTYPENAME-补偿类型编码 */
	@Field(value = "REDEEMTYPECODE")
	private String redeemtypecode;

	/** REDEEMTYPENAME - REDEEMTYPENAME-补偿类型名称 */
	@Field(value = "REDEEMTYPENAME")
	private String redeemtypename;

	/** REDEEMDATE - REDEEMDATE-补偿日期 */
	@Field(value = "REDEEMDATE")
	private String redeemdate;

	/** ISSPECIAL - ISSPECIAL-是否为单病种补偿 */
	@Field(value = "ISSPECIAL")
	private String isspecial;

	/** ISPAUL - ISPAUL-是否实行保底补偿 */
	@Field(value = "ISPAUL")
	private String ispaul;

	/** PHONECODE - PHONECODE-联系电话 */
	@Field(value = "PHONECODE")
	private String phonecode;

	/** ANLAGERNMONEY - ANLAGERNMONEY-追补金额，中药和国定基本药品提高补偿额部分（单位元，小数点后保留两位） */
	@Field(value = "ANLAGERNMONEY")
	private String anlagernmoney;

	/** FUNDPAYMONEY - FUNDPAYMONEY-单病种费用定额（单位元，小数点后保留两位） */
	@Field(value = "FUNDPAYMONEY")
	private String fundpaymoney;

	/** HOSPASSUMEMONEY - HOSPASSUMEMONEY-医疗机构承担费用(单位元，小数点后保留两位) */
	@Field(value = "HOSPASSUMEMONEY")
	private String hospassumemoney;

	/** PERSONALPAYMONEY - PERSONALPAYMONEY-个人自付费用（单位元，小数点后保留两位） */
	@Field(value = "PERSONALPAYMONEY")
	private String personalpaymoney;

	/** CIVILREDEEMMONEY - CIVILREDEEMMONEY-民政救助补偿金额（单位元，小数点后保留两位） */
	@Field(value = "CIVILREDEEMMONEY")
	private String civilredeemmoney;

	/** MATERIALMONEY - MATERIALMONEY-高额材料限价超额费用（单位元，小数点后保留两位） */
	@Field(value = "MATERIALMONEY")
	private String materialmoney;

	/** OBLIGATEONE - OBLIGATEONE-大病保险补偿金额 */
	@Field(value = "OBLIGATEONE")
	private String obligateone;

	/** OBLIGATETWO - OBLIGATETWO-财政兜底资金金额 */
	@Field(value = "OBLIGATETWO")
	private String obligatetwo;

	/** OBLIGATETHREE - OBLIGATETHREE-其他保障金额 */
	@Field(value = "OBLIGATETHREE")
	private String obligatethree;

	/** OBLIGATEFOUR - OBLIGATEFOUR-“180”补偿金额 */
	@Field(value = "OBLIGATEFOUR")
	private String obligatefour;

	/** OBLIGATEFIVE - OBLIGATEFIVE-预留字段5 */
	@Field(value = "OBLIGATEFIVE")
	private String obligatefive;

	/** USERNAME - 当前登录用户名 */
	@Field(value = "USERNAME")
	private String username;

	/** USERPWD - 当前登录用户密码 */
	@Field(value = "USERPWD")
	private String userpwd;

	/** HOSPCODE - 医疗机构编码 */
	@Field(value = "HOSPCODE")
	private String hospcode;

	/** LEAVEDATE - 出院日期(yyyy-mm-dd hh:mm:ss ) */
	@Field(value = "LEAVEDATE")
	private String leavedate;

	/** OUTOFFICEID - 出院科室编码 */
	@Field(value = "OUTOFFICEID")
	private String outofficeid;

	/** OUTHOSID - 出院状态 */
	@Field(value = "OUTHOSID")
	private String outhosid;

	/** ICDALLNO - 农合疾病出院诊断（icd编码） */
	@Field(value = "ICDALLNO")
	private String icdallno;

	/** TREATCODE - 治疗方式编码 */
	@Field(value = "TREATCODE")
	private String treatcode;

	/** TURNMODE - 转诊类型（0 正常住院 1 县外就医转诊 2 转院） */
	@Field(value = "TURNMODE")
	private String turnmode;

	/** TURNCODE - 转诊转院编码（县外就医转诊-该值为转诊申请单号；转院-该值为转院住院登记号） */
	@Field(value = "TURNCODE")
	private String turncode;

	/** TURNDATE - 转院日期( yyyy-mm-dd hh:mm:ss) */
	@Field(value = "TURNDATE")
	private String turndate;

	/** DOCTORNAME - 医生姓名 */
	@Field(value = "DOCTORNAME")
	private String doctorname;

	/** DOCTORNO - 医生编码 */
	@Field(value = "DOCTORNO")
	private String doctorno;

	/** MEDICINECOSTS - 总药品费用(单位元,小数点后保留两位) */
	@Field(value = "MEDICINECOSTS")
	private String medicinecosts;

	/** ENABLEMEDICINEMONEY - 保内药品费用(单位元,小数点后保留两位) */
	@Field(value = "ENABLEMEDICINEMONEY")
	private String enablemedicinemoney;

	/** CURECOSTS - 诊疗费用(单位元,小数点后保留两位) */
	@Field(value = "CURECOSTS")
	private String curecosts;

	/** OPERATIONCOSTS - 手术费用(单位元,小数点后保留两位) */
	@Field(value = "OPERATIONCOSTS")
	private String operationcosts;

	/** MATERIALCOSTS - 材料费用(单位元,小数点后保留两位) */
	@Field(value = "MATERIALCOSTS")
	private String materialcosts;

	/** MATERIALCOSTS - 本次住院补偿核算金额（单位元，小数点后保留两位） */
	@Field(value = "CHECKMONEY")
	private String checkmoney;

	/** MATERIALCOSTS - 核算日期 */
	@Field(value = "CHECKDATE")
	private String checkdate;

	/** MATERIALCOSTS - 是否需要审核 */
	@Field(value = "ISADUIT")
	private String isaduit;

	/** 本次住院补偿核算金额（单位元，小数点后保留两位） **/
	@Field(value = "CALCULATEMONEY")
	private String calculatemoney;

	/** MATERIALCOSTS - 减免金额 */
	@Field(value = "MINUSMONEY")
	private String minusmoney;

	/** MATERIALCOSTS - 领款人 */
	@Field(value = "ACCEPTMAN")
	private String acceptman;

	/** MATERIALCOSTS - 领款人电话 */
	@Field(value = "PHONE")
	private String phone;

	/** MODIFY_TIME - 最后操作时间 */
	@Field(value = "MODIFY_TIME")
	private Date modifyTime;

	private java.util.List<InsSuzhounhWebGrade> gradelist;

	private java.util.List<InsSuzhounhWebJsfee> feelist;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRedeemno() {
		return this.redeemno;
	}

	public void setRedeemno(String redeemno) {
		this.redeemno = redeemno;
	}

	public String getMemberno() {
		return this.memberno;
	}

	public void setMemberno(String memberno) {
		this.memberno = memberno;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBookno() {
		return this.bookno;
	}

	public void setBookno(String bookno) {
		this.bookno = bookno;
	}

	public String getSexname() {
		return this.sexname;
	}

	public void setSexname(String sexname) {
		this.sexname = sexname;
	}

	public String getBirthday() {
		return this.birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMastername() {
		return this.mastername;
	}

	public void setMastername(String mastername) {
		this.mastername = mastername;
	}

	public String getRelationname() {
		return this.relationname;
	}

	public void setRelationname(String relationname) {
		this.relationname = relationname;
	}

	public String getIdentityname() {
		return this.identityname;
	}

	public void setIdentityname(String identityname) {
		this.identityname = identityname;
	}

	public String getIdcard() {
		return this.idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getCurryearredeemcount() {
		return this.curryearredeemcount;
	}

	public void setCurryearredeemcount(String curryearredeemcount) {
		this.curryearredeemcount = curryearredeemcount;
	}

	public String getCurryeartotal() {
		return this.curryeartotal;
	}

	public void setCurryeartotal(String curryeartotal) {
		this.curryeartotal = curryeartotal;
	}

	public String getCurryearenablemoney() {
		return this.curryearenablemoney;
	}

	public void setCurryearenablemoney(String curryearenablemoney) {
		this.curryearenablemoney = curryearenablemoney;
	}

	public String getCurryearreddemmoney() {
		return this.curryearreddemmoney;
	}

	public void setCurryearreddemmoney(String curryearreddemmoney) {
		this.curryearreddemmoney = curryearreddemmoney;
	}

	public String getFamilyno() {
		return this.familyno;
	}

	public void setFamilyno(String familyno) {
		this.familyno = familyno;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDoorpropname() {
		return this.doorpropname;
	}

	public void setDoorpropname(String doorpropname) {
		this.doorpropname = doorpropname;
	}

	public String getJoinpropname() {
		return this.joinpropname;
	}

	public void setJoinpropname(String joinpropname) {
		this.joinpropname = joinpropname;
	}

	public String getCurrfamilyredeemcount() {
		return this.currfamilyredeemcount;
	}

	public void setCurrfamilyredeemcount(String currfamilyredeemcount) {
		this.currfamilyredeemcount = currfamilyredeemcount;
	}

	public String getCurrfamilytotal() {
		return this.currfamilytotal;
	}

	public void setCurrfamilytotal(String currfamilytotal) {
		this.currfamilytotal = currfamilytotal;
	}

	public String getCurrfamilyenablemoney() {
		return this.currfamilyenablemoney;
	}

	public void setCurrfamilyenablemoney(String currfamilyenablemoney) {
		this.currfamilyenablemoney = currfamilyenablemoney;
	}

	public String getCurrfamilyreddemmoney() {
		return this.currfamilyreddemmoney;
	}

	public void setCurrfamilyreddemmoney(String currfamilyreddemmoney) {
		this.currfamilyreddemmoney = currfamilyreddemmoney;
	}

	public String getTotalcosts() {
		return this.totalcosts;
	}

	public void setTotalcosts(String totalcosts) {
		this.totalcosts = totalcosts;
	}

	public String getEnablemoney() {
		return this.enablemoney;
	}

	public void setEnablemoney(String enablemoney) {
		this.enablemoney = enablemoney;
	}

	public String getEssentialmedicinemoney() {
		return this.essentialmedicinemoney;
	}

	public void setEssentialmedicinemoney(String essentialmedicinemoney) {
		this.essentialmedicinemoney = essentialmedicinemoney;
	}

	public String getProvincemedicinemoney() {
		return this.provincemedicinemoney;
	}

	public void setProvincemedicinemoney(String provincemedicinemoney) {
		this.provincemedicinemoney = provincemedicinemoney;
	}

	public String getStartmoney() {
		return this.startmoney;
	}

	public void setStartmoney(String startmoney) {
		this.startmoney = startmoney;
	}

	public String getRedeemmoney() {
		return this.redeemmoney;
	}

	public void setRedeemmoney(String redeemmoney) {
		this.redeemmoney = redeemmoney;
	}

	public String getRedeemtypename() {
		return this.redeemtypename;
	}

	public void setRedeemtypename(String redeemtypename) {
		this.redeemtypename = redeemtypename;
	}

	public String getRedeemdate() {
		return this.redeemdate;
	}

	public void setRedeemdate(String redeemdate) {
		this.redeemdate = redeemdate;
	}

	public String getIsspecial() {
		return this.isspecial;
	}

	public void setIsspecial(String isspecial) {
		this.isspecial = isspecial;
	}

	public String getIspaul() {
		return this.ispaul;
	}

	public void setIspaul(String ispaul) {
		this.ispaul = ispaul;
	}

	public String getPhonecode() {
		return this.phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}

	public String getAnlagernmoney() {
		return this.anlagernmoney;
	}

	public void setAnlagernmoney(String anlagernmoney) {
		this.anlagernmoney = anlagernmoney;
	}

	public String getFundpaymoney() {
		return this.fundpaymoney;
	}

	public void setFundpaymoney(String fundpaymoney) {
		this.fundpaymoney = fundpaymoney;
	}

	public String getHospassumemoney() {
		return this.hospassumemoney;
	}

	public void setHospassumemoney(String hospassumemoney) {
		this.hospassumemoney = hospassumemoney;
	}

	public String getPersonalpaymoney() {
		return this.personalpaymoney;
	}

	public void setPersonalpaymoney(String personalpaymoney) {
		this.personalpaymoney = personalpaymoney;
	}

	public String getCivilredeemmoney() {
		return this.civilredeemmoney;
	}

	public void setCivilredeemmoney(String civilredeemmoney) {
		this.civilredeemmoney = civilredeemmoney;
	}

	public String getMaterialmoney() {
		return this.materialmoney;
	}

	public void setMaterialmoney(String materialmoney) {
		this.materialmoney = materialmoney;
	}

	public String getObligateone() {
		return this.obligateone;
	}

	public void setObligateone(String obligateone) {
		this.obligateone = obligateone;
	}

	public String getObligatetwo() {
		return this.obligatetwo;
	}

	public void setObligatetwo(String obligatetwo) {
		this.obligatetwo = obligatetwo;
	}

	public String getObligatethree() {
		return this.obligatethree;
	}

	public void setObligatethree(String obligatethree) {
		this.obligatethree = obligatethree;
	}

	public String getObligatefour() {
		return this.obligatefour;
	}

	public void setObligatefour(String obligatefour) {
		this.obligatefour = obligatefour;
	}

	public String getObligatefive() {
		return this.obligatefive;
	}

	public void setObligatefive(String obligatefive) {
		this.obligatefive = obligatefive;
	}

	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getInpatientsn() {
		return inpatientsn;
	}

	public void setInpatientsn(String inpatientsn) {
		this.inpatientsn = inpatientsn;
	}

	public java.util.List<InsSuzhounhWebGrade> getGradelist() {
		return gradelist;
	}

	public void setGradelist(java.util.List<InsSuzhounhWebGrade> gradelist) {
		this.gradelist = gradelist;
	}

	public java.util.List<InsSuzhounhWebJsfee> getFeelist() {
		return feelist;
	}

	public void setFeelist(java.util.List<InsSuzhounhWebJsfee> feelist) {
		this.feelist = feelist;
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

	public String getHospcode() {
		return hospcode;
	}

	public void setHospcode(String hospcode) {
		this.hospcode = hospcode;
	}

	public String getLeavedate() {
		return leavedate;
	}

	public void setLeavedate(String leavedate) {
		this.leavedate = leavedate;
	}

	public String getOutofficeid() {
		return outofficeid;
	}

	public void setOutofficeid(String outofficeid) {
		this.outofficeid = outofficeid;
	}

	public String getOuthosid() {
		return outhosid;
	}

	public void setOuthosid(String outhosid) {
		this.outhosid = outhosid;
	}

	public String getIcdallno() {
		return icdallno;
	}

	public void setIcdallno(String icdallno) {
		this.icdallno = icdallno;
	}

	public String getTreatcode() {
		return treatcode;
	}

	public void setTreatcode(String treatcode) {
		this.treatcode = treatcode;
	}

	public String getTurnmode() {
		return turnmode;
	}

	public void setTurnmode(String turnmode) {
		this.turnmode = turnmode;
	}

	public String getTurncode() {
		return turncode;
	}

	public void setTurncode(String turncode) {
		this.turncode = turncode;
	}

	public String getTurndate() {
		return turndate;
	}

	public void setTurndate(String turndate) {
		this.turndate = turndate;
	}

	public String getDoctorname() {
		return doctorname;
	}

	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getDoctorno() {
		return doctorno;
	}

	public void setDoctorno(String doctorno) {
		this.doctorno = doctorno;
	}

	public String getMedicinecosts() {
		return medicinecosts;
	}

	public void setMedicinecosts(String medicinecosts) {
		this.medicinecosts = medicinecosts;
	}

	public String getEnablemedicinemoney() {
		return enablemedicinemoney;
	}

	public void setEnablemedicinemoney(String enablemedicinemoney) {
		this.enablemedicinemoney = enablemedicinemoney;
	}

	public String getCurecosts() {
		return curecosts;
	}

	public void setCurecosts(String curecosts) {
		this.curecosts = curecosts;
	}

	public String getOperationcosts() {
		return operationcosts;
	}

	public void setOperationcosts(String operationcosts) {
		this.operationcosts = operationcosts;
	}

	public String getMaterialcosts() {
		return materialcosts;
	}

	public void setMaterialcosts(String materialcosts) {
		this.materialcosts = materialcosts;
	}

	public String getCalculatemoney() {
		return calculatemoney;
	}

	public void setCalculatemoney(String calculatemoney) {
		this.calculatemoney = calculatemoney;
	}

	public String getMinusmoney() {
		return minusmoney;
	}

	public void setMinusmoney(String minusmoney) {
		this.minusmoney = minusmoney;
	}

	public String getAcceptman() {
		return acceptman;
	}

	public void setAcceptman(String acceptman) {
		this.acceptman = acceptman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCheckmoney() {
		return checkmoney;
	}

	public void setCheckmoney(String checkmoney) {
		this.checkmoney = checkmoney;
	}

	public String getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}

	public String getIsaduit() {
		return isaduit;
	}

	public void setIsaduit(String isaduit) {
		this.isaduit = isaduit;
	}

	public String getRedeemtypecode() {
		return redeemtypecode;
	}

	public void setRedeemtypecode(String redeemtypecode) {
		this.redeemtypecode = redeemtypecode;
	}

}