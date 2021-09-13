package com.zebone.nhis.common.module.pv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Table: PV_ENCOUNTER  - 患者就诊-就诊记录 
 *
 * @since 2016-09-09 04:08:55
 */
@Table(value="PV_ENCOUNTER")
public class PvEncounter extends BaseModule  {

	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_PV",id=KeyId.UUID)
    private String pkPv;

	/** 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** CODE_PV - 门诊号或门诊流水号； 住院号或住院流水号 */
	@Field(value="CODE_PV")
    private String codePv;

    /** EU_PVTYPE - 1门诊，2急诊，3住院，4体检，5家庭病床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/** 就诊开始时间 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	/** 就诊结束时间 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** EU_STATUS - 0 登记，1 就诊，2 结束，3 结算，9 退诊 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** FLAG_IN - 仅对住院流程，急诊观察有效 */
	@Field(value="FLAG_IN")
    private String flagIn;

    /** FLAG_SETTLE - 0表示未出院结算 1表示出院结算 */
	@Field(value="FLAG_SETTLE")
    private String flagSettle;

	/** 患者姓名 */
	@Field(value="NAME_PI")
    private String namePi;

	/** 性别 */
	@Field(value="DT_SEX")
    private String dtSex;

	/** 年龄 */
	@Field(value="AGE_PV")
    private String agePv;

	/** 现住址 */
	@Field(value="ADDRESS")
    private String address;

	/** 婚姻编码 */
	@Field(value="DT_MARRY")
    private String dtMarry;
	
	/** 身高 */
	@Field(value="HEIGHT")
	private Double height;
	
	/** 体重 */
	@Field(value="WEIGHT")
	private Double weight;

	/** 伸缩压 */
	private String Sbp;

	/** 舒张压 */
	private String Dbp;

    /** PK_DEPT - 门诊：医生接受时写入本次临床科室；住院：病区接受时写入本次对应的临床科室 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** PK_DEPT_NS - 住院：病区接受时写入本次对应的部门 */
	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

    /** DATE_CLINIC - 门诊医生看诊时间 */
	@Field(value="DATE_CLINIC")
    private Date dateClinic;

    /** DATE_ADMIT - 病区接收入科时间 */
	@Field(value="DATE_ADMIT")
    private Date dateAdmit;

    /** PK_WG - 在医疗组管理模式下的对应医疗组主键，可为空 */
	@Field(value="PK_WG")
    private String pkWg;

	@Field(value="PK_WG_ORG")
	private String pkWgOrg;

	@Field(value = "PK_WG_Ex")
	private String pkWgEx;

    /** BED_NO - 门诊慢性病，急诊观察时，住院床位。调床是实时更新 */
	@Field(value="BED_NO")
    private String bedNo;

    /** PK_EMP_TRE - 入院登记时，根据入院通知单填入收治医生，若无入院通知单则操作员手工录入 */
	@Field(value="PK_EMP_TRE")
    private String pkEmpTre;

	/** 收治医生姓名 */
	@Field(value="NAME_EMP_TRE")
    private String nameEmpTre;

    /** PK_EMP_PHY - 
     * 门诊：医生接受时写入接诊医生编码；
     * 住院：病区接受时写入本次对应的责任医生，以后执行患者医生调整时写入调整后的医生
     */
	@Field(value="PK_EMP_PHY")
    private String pkEmpPhy;

	/** 当前主管医生姓名 */
	@Field(value="NAME_EMP_PHY")
    private String nameEmpPhy;

    /** PK_EMP_NS - 住院：病区接受时写入本次对应的责任护士，以后执行患者护士调整时写入调整后的护士 */
	@Field(value="PK_EMP_NS")
    private String pkEmpNs;

	/** 当前责任护士姓名 */
	@Field(value="NAME_EMP_NS")
    private String nameEmpNs;

	/** 医保主计划 */
	@Field(value="PK_INSU")
    private String pkInsu;
	
	/** PK_PICATE - 居民、军人、老人等 2017-10-16默认使用以下字段 */
	@Field(value="PK_PICATE")
    private String pkPicate;
	
	/** PK_PICATE - 患者来源 */
	@Field(value="DT_PVSOURCE")
    private String dtPvsource;
	
	/** EU_PVMODE - 就诊模式 */
	@Field(value="EU_PVMODE")
	private String euPvmode;

	/** 登记人主键 */
	@Field(value="PK_EMP_REG")
    private String pkEmpReg;

	/** 登记人姓名 */
	@Field(value="NAME_EMP_REG")
    private String nameEmpReg;

	/** 登记日期 */
	@Field(value="DATE_REG")
    private Date dateReg;

    /** FLAG_CANCEL - 门诊：退号；住院：当日退院 */
	@Field(value="FLAG_CANCEL")
    private String flagCancel;

	/** 退诊人主键 */
	@Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;

	/** 退诊人姓名 */
	@Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;

	/** 退诊日期时间 */
	@Field(value="DATE_CANCEL")
    private Date dateCancel;

    /** EU_STATUS_FP - 0和null  未产生随访，1 生成随访计划，2 随访过程，3 随访结束，9 随访终止 */
	@Field(value="EU_STATUS_FP")
    private String euStatusFp;

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
	
	/** 工作单位 */
	@Field(value="UNIT_WORK")
    private String unitWork;

	/** 工作单位电话 */
	@Field(value="TEL_WORK")
    private String telWork;
	
	/** 工作单位地址邮编*/
	@Field(value="POSTCODE_WORK")
	private String	postcodeWork;
	
	/** 联系人 */
	@Field(value="NAME_REL")
    private String nameRel;

	/** 联系人电话 */
	@Field(value="TEL_REL")
    private String telRel;
	
	/** 联系人关系 */
	@Field(value="DT_RALATION")
    private String dtRalation;
	
	/** 联系人地址*/
	@Field(value="ADDR_REL")
	private String	addrRel;
	
	/** 联系人证件类型*/
	@Field(value="DT_IDTYPE_REL")
	private String	dtIdtypeRel;
	
	/** 联系人地址*/
	@Field(value="IDNO_REL")
	private String	idnoRel;
	
	/** 特诊标志*/
	@Field(value="FLAG_SPEC")
	private String	flagSpec;
	
	 /**
     * 锁定状态
     */
	@Field(value="EU_LOCKED")
    private String euLocked;
	
	/** 备注*/
	@Field(value="NOTE")
	private String	note;
	
	/** 疾病类型*/
	@Field(value="EU_DISETYPE")
	private String euDisetype;

	@Field(value = "NAME_AGENT")
	private String nameAgent;

	@Field(value = "IDNO_AGENT")
	private String idnoAgent;

	@Field(value = "ADDR_AGENT")
	private String addrAgent;

	@Field(value = "TEL_AGENT")
	private String telAgent;

	@Field(value = "FLAG_CARD_CHK")
	private String flagCardChk;

	@Field(value = "DT_DISEASE")
	private String dtDisease;

	@Field(value = "PK_DEPT_AREA")
	private String pkDeptArea;

	private int dayFromLast;//距离上次就诊天数

	/** 患者编码 */
	private String codePi;
	
	/** 卡编码 */
	private String cardNo;
	
	/** 证件号码 */
	private String idNo;
	
	/** 健康卡号 */
	private String hicNo;
	
	/** 医保卡号 */
	private String insurNo;
	
	/** 住院号 */
	private String codeIp;
	
	/** 住院号 */
	private String codeOp;
	
	/** 就诊状态编码组 */
	private String[] euStatuss;
	
	/** 就诊类型编码组 */
	private String[] euPvtypes;
	
	/** 0-不判断就诊状态，查全部；1-判断就诊状态，查0、1、3，再根据参数euStatus过滤；默认1 */
	private String flagStatus = "1";
	
	/**就诊状态只查询结束状态标志（急诊除外）*/
	private String flagStatusEnd;
	
	/**就诊过滤退诊状态标志*/
	private String flagStatusCancel;
	
	/** 医保计划数组 */
	private String[] pkHps;
	
	/**患者所属医生姓名*/
	private String empName;
	
	 /**挂号医生*/
    private String pkEmpPv;
    
    /**挂号医生姓名*/
    private String nameEmpPv;

	/** 医保实名状态 */
	@Field(value="DT_HPREALTYPE")
    private String dtHprealtype;
	
	/**是否是透析患者  1-是 0-不是*/
	private String dialysisFlag;
	/** 享受配偶生育保险1-是 0-不是 */
	@Field(value="FLAG_MI")
    private String flagMi;
	/** 配偶姓名 */
	@Field(value="NAME_SPOUSE")
    private String nameSpouse;
	/** 配偶身份证号 */
	@Field(value="IDNO_SPOUSE")
    private String idnoSpouse;
	
	private String adtType;
	
	/** 特殊人群 */
	@Field(value="DT_SPCDTYPE")
	private String dtSpcdtype;
	
	/**婴儿占床主键*/
	private String pkBedAn;
	
	/**是否退费模块*/
	private String flagRefundModular;
	
	/**
	 * 平台外部推展使用
	 */
	private List<Map<String,Object>> piList;

	/** 流行病史信息 */
	@Field(value="DESC_EPID")
	private String descEpid;

	/**避孕服务主键*/
	@Field(value="PK_CONTRACEPTION")
	private String pkContraception;

	/**所属辖区-000207字典编码*/
	@Field(value="DT_JURISDICTION")
	private String dtJurisdiction;

	/**
	 * 是否查询结算时间
	 */
	private String flagSettleDate;

	/**
	 * 人医项目-是否查询患者慢病信息
	 */
	private String flagChDise;
	
	//ba项目-干部级别
	@Field(value="CADRE_LEVEL")
	private String cadreLevel;

	public String getSbp() {
		return Sbp;
	}

	public void setSbp(String sbp) {
		Sbp = sbp;
	}

	public String getDbp() {
		return Dbp;
	}

	public void setDbp(String dbp) {
		Dbp = dbp;
	}

	public String getCadreLevel() {
		return cadreLevel;
	}

	public void setCadreLevel(String cadreLevel) {
		this.cadreLevel = cadreLevel;
	}

	public String getFlagChDise() {
		return flagChDise;
	}

	public void setFlagChDise(String flagChDise) {
		this.flagChDise = flagChDise;
	}

	public String getEuDisetype() {
		return euDisetype;
	}
	public void setEuDisetype(String euDisetype) {
		this.euDisetype = euDisetype;
	}
	public String getEuLocked() {
		return euLocked;
	}
	public void setEuLocked(String euLocked) {
		this.euLocked = euLocked;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getInsurNo() {
		return insurNo;
	}
	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}
	public String[] getEuPvtypes() {
		return euPvtypes;
	}
	public void setEuPvtypes(String[] euPvtypes) {
		this.euPvtypes = euPvtypes;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	/** 退号时若是银行卡支付类型 要传的参数 **/
	private List<BlExtPayBankVo> blExtPayBank;

	public List<BlExtPayBankVo> getBlExtPayBank() {
		return blExtPayBank;
	}
	public String getDtPvsource() {
		return dtPvsource;
	}
	public void setDtPvsource(String dtPvsource) {
		this.dtPvsource = dtPvsource;
	}
	public void setBlExtPayBank(List<BlExtPayBankVo> blExtPayBank) {
		this.blExtPayBank = blExtPayBank;
	}
	public String getPkPicate() {
		return pkPicate;
	}
	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
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
	public int getDayFromLast() {
		return dayFromLast;
	}
	public void setDayFromLast(int dayFromLast) {
		this.dayFromLast = dayFromLast;
	}
	public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodePv(){
        return this.codePv;
    }
    public void setCodePv(String codePv){
        this.codePv = codePv;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd(){
        return this.dateEnd;
    }
    public void setDateEnd(Date dateEnd){
        this.dateEnd = dateEnd;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagIn(){
        return this.flagIn;
    }
    public void setFlagIn(String flagIn){
        this.flagIn = flagIn;
    }

    public String getFlagSettle(){
        return this.flagSettle;
    }
    public void setFlagSettle(String flagSettle){
        this.flagSettle = flagSettle;
    }

    public String getNamePi(){
        return this.namePi;
    }
    public void setNamePi(String namePi){
        this.namePi = namePi;
    }

    public String getDtSex(){
        return this.dtSex;
    }
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }

    public String getAgePv(){
        return this.agePv;
    }
    public void setAgePv(String agePv){
        this.agePv = agePv;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getDtMarry(){
        return this.dtMarry;
    }
    public void setDtMarry(String dtMarry){
        this.dtMarry = dtMarry;
    }

    public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public Date getDateClinic(){
        return this.dateClinic;
    }
    public void setDateClinic(Date dateClinic){
        this.dateClinic = dateClinic;
    }

    public Date getDateAdmit(){
        return this.dateAdmit;
    }
    public void setDateAdmit(Date dateAdmit){
        this.dateAdmit = dateAdmit;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getBedNo(){
        return this.bedNo;
    }
    public void setBedNo(String bedNo){
        this.bedNo = bedNo;
    }

    public String getPkEmpTre(){
        return this.pkEmpTre;
    }
    public void setPkEmpTre(String pkEmpTre){
        this.pkEmpTre = pkEmpTre;
    }

    public String getNameEmpTre(){
        return this.nameEmpTre;
    }
    public void setNameEmpTre(String nameEmpTre){
        this.nameEmpTre = nameEmpTre;
    }

    public String getPkEmpPhy(){
        return this.pkEmpPhy;
    }
    public void setPkEmpPhy(String pkEmpPhy){
        this.pkEmpPhy = pkEmpPhy;
    }

    public String getNameEmpPhy(){
        return this.nameEmpPhy;
    }
    public void setNameEmpPhy(String nameEmpPhy){
        this.nameEmpPhy = nameEmpPhy;
    }

    public String getPkEmpNs(){
        return this.pkEmpNs;
    }
    public void setPkEmpNs(String pkEmpNs){
        this.pkEmpNs = pkEmpNs;
    }

    public String getNameEmpNs(){
        return this.nameEmpNs;
    }
    public void setNameEmpNs(String nameEmpNs){
        this.nameEmpNs = nameEmpNs;
    }

    public String getPkInsu(){
        return this.pkInsu;
    }
    public void setPkInsu(String pkInsu){
        this.pkInsu = pkInsu;
    }
	public String getPkEmpReg(){
        return this.pkEmpReg;
    }
    public void setPkEmpReg(String pkEmpReg){
        this.pkEmpReg = pkEmpReg;
    }

    public String getNameEmpReg(){
        return this.nameEmpReg;
    }
    public void setNameEmpReg(String nameEmpReg){
        this.nameEmpReg = nameEmpReg;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
    }

    public Date getDateCancel(){
        return this.dateCancel;
    }
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }

    public String getEuStatusFp(){
        return this.euStatusFp;
    }
    public void setEuStatusFp(String euStatusFp){
        this.euStatusFp = euStatusFp;
    }
    
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String[] getEuStatuss() {
		return euStatuss;
	}
	public void setEuStatuss(String[] euStatuss) {
		this.euStatuss = euStatuss;
	}
	public String getFlagStatus() {
		return flagStatus;
	}
	public void setFlagStatus(String flagStatus) {
		this.flagStatus = flagStatus;
	}
	public String[] getPkHps() {
		return pkHps;
	}
	public void setPkHps(String[] pkHps) {
		this.pkHps = pkHps;
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
	public String getUnitWork() {
		return unitWork;
	}
	public void setUnitWork(String unitWork) {
		this.unitWork = unitWork;
	}
	public String getTelWork() {
		return telWork;
	}
	public void setTelWork(String telWork) {
		this.telWork = telWork;
	}
	public String getPostcodeWork() {
		return postcodeWork;
	}
	public void setPostcodeWork(String postcodeWork) {
		this.postcodeWork = postcodeWork;
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
	public String getDtRalation() {
		return dtRalation;
	}
	public void setDtRalation(String dtRalation) {
		this.dtRalation = dtRalation;
	}
	public String getAddrRel() {
		return addrRel;
	}
	public void setAddrRel(String addrRel) {
		this.addrRel = addrRel;
	}
	public String getEuPvmode() {
		return euPvmode;
	}
	public void setEuPvmode(String euPvmode) {
		this.euPvmode = euPvmode;
	}
	public String getDtHprealtype() {
		return dtHprealtype;
	}
	public void setDtHprealtype(String dtHprealtype) {
		this.dtHprealtype = dtHprealtype;
	}
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
	public List<Map<String, Object>> getPiList() {
		return piList;
	}
	public void setPiList(List<Map<String, Object>> piList) {
		this.piList = piList;
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
	public String getDialysisFlag() {
		return dialysisFlag;
	}
	public void setDialysisFlag(String dialysisFlag) {
		this.dialysisFlag = dialysisFlag;
	}
    public String getFlagMi(){
        return this.flagMi;
    }
    public void setFlagMi(String flagMi){
        this.flagMi = flagMi;
    }

    public String getNameSpouse(){
        return this.nameSpouse;
    }
    public void setNameSpouse(String nameSpouse){
        this.nameSpouse = nameSpouse;
    }

    public String getIdnoSpouse(){
        return this.idnoSpouse;
    }
    public void setIdnoSpouse(String idnoSpouse){
        this.idnoSpouse = idnoSpouse;
    }
	public String getDtSpcdtype() {
		return dtSpcdtype;
	}
	public void setDtSpcdtype(String dtSpcdtype) {
		this.dtSpcdtype = dtSpcdtype;
	}
	public String getAdtType() {
		return adtType;
	}
	public void setAdtType(String adtType) {
		this.adtType = adtType;
	}
	public String getHicNo() {
		return hicNo;
	}
	public void setHicNo(String hicNo) {
		this.hicNo = hicNo;
	}

	public String getPkBedAn() {
		return pkBedAn;
	}

	public void setPkBedAn(String pkBedAn) {
		this.pkBedAn = pkBedAn;
	}

	public String getNameAgent() {
		return nameAgent;
	}

	public void setNameAgent(String nameAgent) {
		this.nameAgent = nameAgent;
	}

	public String getIdnoAgent() {
		return idnoAgent;
	}

	public void setIdnoAgent(String idnoAgent) {
		this.idnoAgent = idnoAgent;
	}

	public String getAddrAgent() {
		return addrAgent;
	}

	public void setAddrAgent(String addrAgent) {
		this.addrAgent = addrAgent;
	}

	public String getTelAgent() {
		return telAgent;
	}

	public void setTelAgent(String telAgent) {
		this.telAgent = telAgent;
	}

	public String getFlagCardChk() {
		return flagCardChk;
	}

	public void setFlagCardChk(String flagCardChk) {
		this.flagCardChk = flagCardChk;
	}

	public String getDescEpid() {
		return descEpid;
	}

	public void setDescEpid(String descEpid) {
		this.descEpid = descEpid;
	}

	public String getDtDisease() {
		return dtDisease;
	}

	public void setDtDisease(String dtDisease) {
		this.dtDisease = dtDisease;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPkDeptArea() {
		return pkDeptArea;
	}

	public void setPkDeptArea(String pkDeptArea) {
		this.pkDeptArea = pkDeptArea;
	}
	public String getFlagStatusEnd() {
		return flagStatusEnd;
	}
	public void setFlagStatusEnd(String flagStatusEnd) {
		this.flagStatusEnd = flagStatusEnd;
	}
	public String getFlagStatusCancel() {
		return flagStatusCancel;
	}
	public void setFlagStatusCancel(String flagStatusCancel) {
		this.flagStatusCancel = flagStatusCancel;
	}
	public String getFlagRefundModular() {
		return flagRefundModular;
	}
	public void setFlagRefundModular(String flagRefundModular) {
		this.flagRefundModular = flagRefundModular;
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

	public String getFlagSettleDate() {
		return flagSettleDate;
	}

	public void setFlagSettleDate(String flagSettleDate) {
		this.flagSettleDate = flagSettleDate;
	}

	public String getPkWgOrg() {
		return pkWgOrg;
	}

	public void setPkWgOrg(String pkWgOrg) {
		this.pkWgOrg = pkWgOrg;
	}

	public String getPkWgEx() {
		return pkWgEx;
	}

	public void setPkWgEx(String pkWgEx) {
		this.pkWgEx = pkWgEx;
	}
}