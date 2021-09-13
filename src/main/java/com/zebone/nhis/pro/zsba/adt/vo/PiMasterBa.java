package com.zebone.nhis.pro.zsba.adt.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_MASTER  - 患者信息-基本 
 *
 * @since 2016-09-09 03:59:16
 */
@Table(value="PI_MASTER")
public class PiMasterBa extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** 患者主键 */
	@PK
	@Field(value="PK_PI",id=KeyId.UUID)
    private String pkPi;

	/** 患者编码 */
	@Field(value="CODE_PI")
    private String codePi;

	/** 门诊号 */
	@Field(value="CODE_OP")
    private String codeOp;

	/** 住院号 */
	@Field(value="CODE_IP")
    private String codeIp;

	/** 急诊号 */
	@Field(value="CODE_ER")
    private String codeEr;
	
	/** 条形码 */
	@Field(value="BARCODE")
    private String barcode;

	/** 患者分类 */
	@Field(value="PK_PICATE")
    private String pkPicate;

	/** 患者姓名 */
	@Field(value="NAME_PI")
    private String namePi;

	/** 患者照片 */
	@Field(value="PHOTO_PI")
    private byte[] photoPi;

	/** 证件类型 */
	@Field(value="DT_IDTYPE")
    private String dtIdtype;

	/** 证件号码 */
	@Field(value="ID_NO")
    private String idNo;

	/** 健康卡号 */
	@Field(value="HIC_NO")
    private String hicNo;

	/** 医保卡号 */
	@Field(value="INSUR_NO")
    private String insurNo;

	/** 区域主索引 */
	@Field(value="MPI")
    private String mpi;

	/** 健康档案建立标志 */
	@Field(value="FLAG_EHR")
    private String flagEhr;

	/** 性别编码 */
	@Field(value="DT_SEX")
    private String dtSex;

	/** 出生日期 */
	@Field(value="BIRTH_DATE")
    private Date birthDate;

	/** 出生地 */
	@Field(value="PLACE_BIRTH")
    private String placeBirth;

	/** 婚姻编码 */
	@Field(value="DT_MARRY")
    private String dtMarry;

	/** 职业编码 */
	@Field(value="DT_OCCU")
    private String dtOccu;

	/** 学历编码 */
	@Field(value="DT_EDU")
    private String dtEdu;

	/** 国籍编码 */
	@Field(value="DT_COUNTRY")
    private String dtCountry;

	/** 民族编码 */
	@Field(value="DT_NATION")
    private String dtNation;

	/** 电话号码 */
	@Field(value="TEL_NO")
    private String telNo;

	/** 手机号码 */
	@Field(value="MOBILE")
    private String mobile;

	/** 微信号码 */
	@Field(value="WECHAT_NO")
    private String wechatNo;

	/** 邮箱地址 */
	@Field(value="EMAIL")
    private String email;

	/** 工作单位 */
	@Field(value="UNIT_WORK")
    private String unitWork;

	/** 工作单位电话 */
	@Field(value="TEL_WORK")
    private String telWork;

	/** 患者地址 */
	@Field(value="ADDRESS")
    private String address;

	/** 联系人 */
	@Field(value="NAME_REL")
    private String nameRel;

	/** 联系人电话 */
	@Field(value="TEL_REL")
    private String telRel;
	
	/** 联系人证件类型*/
	@Field(value="DT_IDTYPE_REL")
	private String	dtIdtypeRel;
	
	/** 联系人证件号*/
	@Field(value="IDNO_REL")
	private String	idnoRel;

	/** ABO血型 */
	@Field(value="DT_BLOOD_ABO")
    private String dtBloodAbo;

	/** RH血型 */
	@Field(value="DT_BLOOD_RH")
    private String dtBloodRh;
	
	/** 关系 */
	@Field(value="DT_RALATION")
    private String dtRalation;

	/** 患者分类(名称) */
	private String namePicate;
	
	private String cardNo;
	
	/** 出生地区域编码*/
	@Field(value="ADDRCODE_BIRTH")
	private String addrcodeBirth;
	
	/** 出生地描述*/
	@Field(value="ADDR_BIRTH")
	private String addrBirth;
	
	/** 籍贯区域编码*/
	@Field(value="ADDRCODE_ORIGIN")
	private String addrcodeOrigin;
	
	/** 籍贯描述*/
	@Field(value="ADDR_ORIGIN")
	private String addrOrigin;
	
	/** 户口地址区域编码*/
	@Field(value="ADDRCODE_REGI")
	private String 	addrcodeRegi;
	
	/** 户口地址描述*/
	@Field(value="ADDR_REGI")
	private String 	addrRegi;
	
	/** 户口详细地址*/
	@Field(value="ADDR_REGI_DT")
	private String 	addrRegiDt;
	
	/** 户口邮编*/
	@Field(value="POSTCODE_REGI")
	private String 	postcodeRegi;
	
	/** 现住址区域编码*/
	@Field(value="ADDRCODE_CUR")
	private String 	addrcodeCur;
	
	/** 现住址描述*/
	@Field(value="ADDR_CUR")
	private String 	addrCur;
	
	/** 现住址详细地址*/
	@Field(value="ADDR_CUR_DT")
	private String 	addrCurDt;
	
	/** 现住址邮编*/
	@Field(value="POSTCODE_CUR")
	private String 	postcodeCur;
	
	/** 工作单位地址邮编*/
	@Field(value="POSTCODE_WORK")
	private String	postcodeWork;
	
	/** 联系人地址*/
	@Field(value="ADDR_REL")
	private String	addrRel;
	/////以下字段yangxue 2019-05-19 添加///
	/** 实名认证标志*/
	@Field(value="FLAG_REALNAME")
	private String flagRealname;
	/** 关联本院职工*/
	@Field(value="PK_EMP")
	private String pkEmp;
	/** 优抚证号*/
	@Field(value="SPCA_NO")
	private String spcaNo;
	/** 老人证号*/
	@Field(value="SEN_NO")
	private String senNo;
	/** 医疗证号*/
	@Field(value="MCNO")
	private String mcno;
	/** 市民卡号*/
	@Field(value="CITIZEN_NO")
	private String citizenNo;
	/** 特约单位*/
	@Field(value="DT_SPECUNIT")
	private String dtSpecunit;
	/** 患者来源*/
	@Field(value="DT_SOURCE")
	private String dtSource;
	/** 备注信息*/
	@Field(value="NOTE")
	private String note;
	/** 手机认证标志*/
	@Field(value="FLAG_REALMOBILE")
	private String flagRealmobile;
	////////2019-05-19 添加结束////////
	/**
	 * 平台外部推展使用
	 */
	private List<Map<String,Object>> piList;
	
	/**
	 * 是否是透析患者 1-是 0不是
	 */
	private String dialysisFlag;
	
	/**
	 * 床位类型
	 */
	private String dtBedtype;

	/** 健康卡号*/
	@Field(value="ic_card_id")
	private String icCardId;

	/** 虚拟卡号*/
	@Field(value="health_card_id")
	private String healthCardId;

	/** 电子健康码*/
	@Field(value="ele_hearth_card")
	private String eleHearthCard;

	/** 诊疗卡*/
	@Field(value="p_car_code")
	private String pCarCode;

	/** 三代医保卡*/
	@Field(value="third_social_no")
	private String thirdSocialNo;

	/** 妇幼保健号*/
	@Field(value="fy_pno")
	private String fyPno;

	public String getFlagRealmobile() {
		return flagRealmobile;
	}

	public void setFlagRealmobile(String flagRealmobile) {
		this.flagRealmobile = flagRealmobile;
	}

	public String getFlagRealname() {
		return flagRealname;
	}
	public void setFlagRealname(String flagRealname) {
		this.flagRealname = flagRealname;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getSpcaNo() {
		return spcaNo;
	}
	public void setSpcaNo(String spcaNo) {
		this.spcaNo = spcaNo;
	}
	public String getSenNo() {
		return senNo;
	}
	public void setSenNo(String senNo) {
		this.senNo = senNo;
	}
	public String getMcno() {
		return mcno;
	}
	public void setMcno(String mcno) {
		this.mcno = mcno;
	}
	public String getCitizenNo() {
		return citizenNo;
	}
	public void setCitizenNo(String citizenNo) {
		this.citizenNo = citizenNo;
	}
	public String getDtSpecunit() {
		return dtSpecunit;
	}
	public void setDtSpecunit(String dtSpecunit) {
		this.dtSpecunit = dtSpecunit;
	}
	public String getDtSource() {
		return dtSource;
	}
	public void setDtSource(String dtSource) {
		this.dtSource = dtSource;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDtIdtypeRel() {
		return dtIdtypeRel;
	}
	public void setDtIdtypeRel(String dtIdtypeRel) {
		this.dtIdtypeRel = dtIdtypeRel;
	}
	public String getIdnoRel() {
		return idnoRel;
	}
	public void setIdnoRel(String idnoRel) {
		this.idnoRel = idnoRel;
	}
	public String getAddrcodeBirth() {
		return addrcodeBirth;
	}
	public void setAddrcodeBirth(String addrcodeBirth) {
		this.addrcodeBirth = addrcodeBirth;
	}
	public String getAddrBirth() {
		return addrBirth;
	}
	public void setAddrBirth(String addrBirth) {
		this.addrBirth = addrBirth;
	}
	public String getAddrcodeOrigin() {
		return addrcodeOrigin;
	}
	public void setAddrcodeOrigin(String addrcodeOrigin) {
		this.addrcodeOrigin = addrcodeOrigin;
	}
	public String getAddrOrigin() {
		return addrOrigin;
	}
	public void setAddrOrigin(String addrOrigin) {
		this.addrOrigin = addrOrigin;
	}
	public String getAddrcodeRegi() {
		return addrcodeRegi;
	}
	public void setAddrcodeRegi(String addrcodeRegi) {
		this.addrcodeRegi = addrcodeRegi;
	}
	public String getAddrRegi() {
		return addrRegi;
	}
	public void setAddrRegi(String addrRegi) {
		this.addrRegi = addrRegi;
	}
	public String getAddrRegiDt() {
		return addrRegiDt;
	}
	public void setAddrRegiDt(String addrRegiDt) {
		this.addrRegiDt = addrRegiDt;
	}
	public String getPostcodeRegi() {
		return postcodeRegi;
	}
	public void setPostcodeRegi(String postcodeRegi) {
		this.postcodeRegi = postcodeRegi;
	}
	public String getAddrcodeCur() {
		return addrcodeCur;
	}
	public void setAddrcodeCur(String addrcodeCur) {
		this.addrcodeCur = addrcodeCur;
	}
	public String getAddrCur() {
		return addrCur;
	}
	public void setAddrCur(String addrCur) {
		this.addrCur = addrCur;
	}
	public String getAddrCurDt() {
		return addrCurDt;
	}
	public void setAddrCurDt(String addrCurDt) {
		this.addrCurDt = addrCurDt;
	}
	public String getPostcodeCur() {
		return postcodeCur;
	}
	public void setPostcodeCur(String postcodeCur) {
		this.postcodeCur = postcodeCur;
	}
	public String getPostcodeWork() {
		return postcodeWork;
	}
	public void setPostcodeWork(String postcodeWork) {
		this.postcodeWork = postcodeWork;
	}
	public String getAddrRel() {
		return addrRel;
	}
	public void setAddrRel(String addrRel) {
		this.addrRel = addrRel;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodePi(){
        return this.codePi;
    }
    public void setCodePi(String codePi){
        this.codePi = codePi;
    }

    public String getCodeOp(){
        return this.codeOp;
    }
    public void setCodeOp(String codeOp){
        this.codeOp = codeOp;
    }

    public String getCodeIp(){
        return this.codeIp;
    }
    public void setCodeIp(String codeIp){
        this.codeIp = codeIp;
    }

    public String getBarcode(){
        return this.barcode;
    }
    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String getPkPicate(){
        return this.pkPicate;
    }
    public void setPkPicate(String pkPicate){
        this.pkPicate = pkPicate;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public byte[] getPhotoPi(){
        return this.photoPi;
    }
    public void setPhotoPi(byte[] photoPi){
        this.photoPi = photoPi;
    }

    public String getDtIdtype(){
        return this.dtIdtype;
    }
    public void setDtIdtype(String dtIdtype){
        this.dtIdtype = dtIdtype;
    }

    public String getIdNo(){
        return this.idNo;
    }
    public void setIdNo(String idNo){
        this.idNo = idNo;
    }

    public String getHicNo(){
        return this.hicNo;
    }
    public void setHicNo(String hicNo){
        this.hicNo = hicNo;
    }

    public String getInsurNo(){
        return this.insurNo;
    }
    public void setInsurNo(String insurNo){
        this.insurNo = insurNo;
    }

    public String getMpi(){
        return this.mpi;
    }
    public void setMpi(String mpi){
        this.mpi = mpi;
    }

    public String getFlagEhr(){
        return this.flagEhr;
    }
    public void setFlagEhr(String flagEhr){
        this.flagEhr = flagEhr;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public Date getBirthDate(){
        return this.birthDate;
    }
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }

    public String getPlaceBirth(){
        return this.placeBirth;
    }
    public void setPlaceBirth(String placeBirth){
        this.placeBirth = placeBirth;
    }

    public String getDtMarry(){
        return this.dtMarry;
    }
    public void setDtMarry(String dtMarry){
        this.dtMarry = dtMarry;
    }

    public String getDtOccu(){
        return this.dtOccu;
    }
    public void setDtOccu(String dtOccu){
        this.dtOccu = dtOccu;
    }

    public String getDtEdu(){
        return this.dtEdu;
    }
    public void setDtEdu(String dtEdu){
        this.dtEdu = dtEdu;
    }

    public String getDtCountry(){
        return this.dtCountry;
    }
    public void setDtCountry(String dtCountry){
        this.dtCountry = dtCountry;
    }

    public String getDtNation(){
        return this.dtNation;
    }
    public void setDtNation(String dtNation){
        this.dtNation = dtNation;
    }

    public String getTelNo(){
        return this.telNo;
    }
    public void setTelNo(String telNo){
        this.telNo = telNo;
    }

    public String getMobile(){
        return this.mobile;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getWechatNo(){
        return this.wechatNo;
    }
    public void setWechatNo(String wechatNo){
        this.wechatNo = wechatNo;
    }

    public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUnitWork(){
        return this.unitWork;
    }
    public void setUnitWork(String unitWork){
        this.unitWork = unitWork;
    }

    public String getTelWork(){
        return this.telWork;
    }
    public void setTelWork(String telWork){
        this.telWork = telWork;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getNameRel(){
        return this.nameRel;
    }
    public void setNameRel(String nameRel){
        this.nameRel = nameRel;
    }

    public String getTelRel(){
        return this.telRel;
    }
    public void setTelRel(String telRel){
        this.telRel = telRel;
    }

    public String getDtBloodAbo(){
        return this.dtBloodAbo;
    }
    public void setDtBloodAbo(String dtBloodAbo){
        this.dtBloodAbo = dtBloodAbo;
    }

    public String getDtBloodRh(){
        return this.dtBloodRh;
    }
    public void setDtBloodRh(String dtBloodRh){
        this.dtBloodRh = dtBloodRh;
    }

	public String getNamePicate() {
		return namePicate;
	}
	public void setNamePicate(String namePicate) {
		this.namePicate = namePicate;
	}
	public String getDtRalation() {
		return dtRalation;
	}
	public void setDtRalation(String dtRalation) {
		this.dtRalation = dtRalation;
	}
	public String getCodeEr() {
		return codeEr;
	}
	public void setCodeEr(String codeEr) {
		this.codeEr = codeEr;
	}
	public List<Map<String, Object>> getPiList() {
		return piList;
	}
	public void setPiList(List<Map<String, Object>> piList) {
		this.piList = piList;
	}
	public String getDialysisFlag() {
		return dialysisFlag;
	}
	public void setDialysisFlag(String dialysisFlag) {
		this.dialysisFlag = dialysisFlag;
	}
	public String getDtBedtype() {
		return dtBedtype;
	}
	public void setDtBedtype(String dtBedtype) {
		this.dtBedtype = dtBedtype;
	}

	public String getIcCardId() {
		return icCardId;
	}

	public void setIcCardId(String icCardId) {
		this.icCardId = icCardId;
	}

	public String getHealthCardId() {
		return healthCardId;
	}

	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}

	public String getEleHearthCard() {
		return eleHearthCard;
	}

	public void setEleHearthCard(String eleHearthCard) {
		this.eleHearthCard = eleHearthCard;
	}

	public String getpCarCode() {
		return pCarCode;
	}

	public void setpCarCode(String pCarCode) {
		this.pCarCode = pCarCode;
	}

	public String getThirdSocialNo() {
		return thirdSocialNo;
	}

	public void setThirdSocialNo(String thirdSocialNo) {
		this.thirdSocialNo = thirdSocialNo;
	}

	public String getFyPno() {
		return fyPno;
	}

	public void setFyPno(String fyPno) {
		this.fyPno = fyPno;
	}

}