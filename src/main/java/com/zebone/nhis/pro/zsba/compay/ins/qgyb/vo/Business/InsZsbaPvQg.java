package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: ins_pv_qg
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_PV_QG")
public class InsZsbaPvQg extends BaseModule{

	@PK
	@Field(value="PK_INSPVQG",id=KeyId.UUID)
    private String pkInspvqg;
	
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	@Field(value="PK_INSU")
    private String pkInsu;
	
	@Field(value="MDTRT_ID")
    private String mdtrtId;
	
	@Field(value="PSN_NO")
    private String psnNo;
	
	@Field(value="INSUTYPE")
    private String insutype;
	
	@Field(value="CONER_NAME")
    private String conerName;
	
	@Field(value="TEL")
    private String tel;
	
	@Field(value="BEGNTIME")
    private String begntime;
	
	@Field(value="MDTRT_CERT_TYPE")
    private String mdtrtCertType;
	
	@Field(value="MDTRT_CERT_NO")
    private String mdtrtCertNo;
	
	@Field(value="MED_TYPE")
    private String medType;
	
	@Field(value="IPT_NO")
    private String iptNo;
	
	@Field(value="MEDRCDNO")
    private String medrcdno;
	
	@Field(value="ATDDR_NO")
    private String atddrNo;
	
	@Field(value="CHFPDR_NAME")
    private String chfpdrName;
	
	@Field(value="ADM_DIAG_DSCR")
    private String admDiagDscr;
	
	@Field(value="ADM_DEPT_CODG")
    private String admDeptCodg;
	
	@Field(value="ADM_DEPT_NAME")
    private String admDeptName;
	
	@Field(value="ADM_BED")
    private String admBed;
	
	@Field(value="DSCG_MAINDIAG_CODE")
    private String dscgMaindiagCode;
	
	@Field(value="DSCG_MAINDIAG_NAME")
    private String dscgMaindiagName;
	
	@Field(value="MAIN_COND_DSCR")
    private String mainCondDscr;
	
	@Field(value="DISE_CODG")
    private String diseCodg;
	
	@Field(value="DISE_NAME")
    private String diseName;
	
	@Field(value="OPRN_OPRT_CODE")
    private String oprnOprtCode;
	
	@Field(value="OPRN_OPRT_NAME")
    private String oprnOprtName;
	
	@Field(value="FPSC_NO")
    private String fpscNo;
	
	@Field(value="MATN_TYPE")
    private String matnType;
	
	@Field(value="BIRCTRL_TYPE")
    private String birctrlType;
	
	@Field(value="LATECHB_FLAG")
    private String latechbFlag;
	
	@Field(value="GESO_VAL")
    private String gesoVal;
	
	@Field(value="FETTS")
    private String fetts;
	
	@Field(value="FETUS_CNT")
    private String fetusCnt;
	
	@Field(value="PRET_FLAG")
    private String pretFlag;
	
	@Field(value="BIRCTRL_MATN_DATE")
    private String birctrlMatnDate;
	
	@Field(value="ENDTIME")
    private String endtime;
	
	@Field(value="COP_FLAG")
    private String copFlag;
	
	@Field(value="DSCG_DEPT_CODG")
    private String dscgDeptCodg;
	
	@Field(value="DSCG_DEPT_NAME")
    private String dscgDeptName;
	
	@Field(value="DSCG_BED")
    private String dscgBed;
	
	@Field(value="DSCG_WAY")
    private String dscgWay;
	
	@Field(value="DIE_DATE")
    private String dieDate;
	
	@Field(value="INSUPLC_ADMDVS")
	private String insuplcAdmdvs;
	
	@Field(value="OPTER_TYPE")
	private String opterType;
	
	@Field(value="OPTER")
	private String opter;
	
	@Field(value="OPTER_NAME")
	private String opterName;
	
	@Field(value="STATUS")
	private String status;
	/**
	 * 单位名称  1101获取的
	 */
	@Field(value="EMP_NAME")
	private String empName;
	
	@Field(value="RJZD")
	private String rjzd;
	
	/**
	 * 入院登记的报文id
	 */
	@Field(value="MSGID2401")
	private String msgid2401;
	
	/**
	 * 0:未审核 1：审核通过 2：审核不通过
	 */
	@Field(value="EXAMINE_STATUS")
	private String examineStatus;
	
	/**
	 * 用于存储审核失败的原因
	 */
	@Field(value="EXAMINE_MSG")
	private String examineMsg;
	
	/**
	 * 出院登记的报文id
	 */
	@Field(value="MSGID2402")
	private String msgid2402;
	
    /**
     * 卡识别码  1101就诊凭证类型为“03”时必填
     */
    @Field(value="CARD_SN")
    private String cardSn;
    
    /**
     * 人员身份类别,目前只存重点保健对象
     */
    @Field(value="PSN_IDET_TYPE")
    private String psnIdetType;
    
    /**
     * 身份信息开始时间,目前只存重点保健对象
     */
    @Field(value="IDET_BEGNTIME")
    private String idetBegntime;
    
    /**
     * 身份信息结束时间,目前只存重点保健对象
     */
    @Field(value="IDET_ENDTIME")
    private String idetEndtime;
	
	/**
	 * 名称
	 */
	private String psnName;
	
    private String code;
	
    private String msg;
	
    private String ip;
    
    private String mac;
    
    private String certno;
    
    private String dtIdtype;
    
    private String hpCode;
    

    
	public String getPkInspvqg() {
		return pkInspvqg;
	}

	public void setPkInspvqg(String pkInspvqg) {
		this.pkInspvqg = pkInspvqg;
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

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}

	public String getConerName() {
		return conerName;
	}

	public void setConerName(String conerName) {
		this.conerName = conerName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getBegntime() {
		return begntime;
	}

	public void setBegntime(String begntime) {
		this.begntime = begntime;
	}

	public String getMdtrtCertType() {
		return mdtrtCertType;
	}

	public void setMdtrtCertType(String mdtrtCertType) {
		this.mdtrtCertType = mdtrtCertType;
	}

	public String getMdtrtCertNo() {
		return mdtrtCertNo;
	}

	public void setMdtrtCertNo(String mdtrtCertNo) {
		this.mdtrtCertNo = mdtrtCertNo;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}

	public String getIptNo() {
		return iptNo;
	}

	public void setIptNo(String iptNo) {
		this.iptNo = iptNo;
	}

	public String getMedrcdno() {
		return medrcdno;
	}

	public void setMedrcdno(String medrcdno) {
		this.medrcdno = medrcdno;
	}

	public String getAtddrNo() {
		return atddrNo;
	}

	public void setAtddrNo(String atddrNo) {
		this.atddrNo = atddrNo;
	}

	public String getChfpdrName() {
		return chfpdrName;
	}

	public void setChfpdrName(String chfpdrName) {
		this.chfpdrName = chfpdrName;
	}

	public String getAdmDiagDscr() {
		return admDiagDscr;
	}

	public void setAdmDiagDscr(String admDiagDscr) {
		this.admDiagDscr = admDiagDscr;
	}

	public String getAdmDeptCodg() {
		return admDeptCodg;
	}

	public void setAdmDeptCodg(String admDeptCodg) {
		this.admDeptCodg = admDeptCodg;
	}

	public String getAdmDeptName() {
		return admDeptName;
	}

	public void setAdmDeptName(String admDeptName) {
		this.admDeptName = admDeptName;
	}

	public String getAdmBed() {
		return admBed;
	}

	public void setAdmBed(String admBed) {
		this.admBed = admBed;
	}

	public String getDscgMaindiagCode() {
		return dscgMaindiagCode;
	}

	public void setDscgMaindiagCode(String dscgMaindiagCode) {
		this.dscgMaindiagCode = dscgMaindiagCode;
	}

	public String getDscgMaindiagName() {
		return dscgMaindiagName;
	}

	public void setDscgMaindiagName(String dscgMaindiagName) {
		this.dscgMaindiagName = dscgMaindiagName;
	}

	public String getMainCondDscr() {
		return mainCondDscr;
	}

	public void setMainCondDscr(String mainCondDscr) {
		this.mainCondDscr = mainCondDscr;
	}

	public String getDiseCodg() {
		return diseCodg;
	}

	public void setDiseCodg(String diseCodg) {
		this.diseCodg = diseCodg;
	}

	public String getDiseName() {
		return diseName;
	}

	public void setDiseName(String diseName) {
		this.diseName = diseName;
	}

	public String getOprnOprtCode() {
		return oprnOprtCode;
	}

	public void setOprnOprtCode(String oprnOprtCode) {
		this.oprnOprtCode = oprnOprtCode;
	}

	public String getOprnOprtName() {
		return oprnOprtName;
	}

	public void setOprnOprtName(String oprnOprtName) {
		this.oprnOprtName = oprnOprtName;
	}

	public String getFpscNo() {
		return fpscNo;
	}

	public void setFpscNo(String fpscNo) {
		this.fpscNo = fpscNo;
	}

	public String getMatnType() {
		return matnType;
	}

	public void setMatnType(String matnType) {
		this.matnType = matnType;
	}

	public String getBirctrlType() {
		return birctrlType;
	}

	public void setBirctrlType(String birctrlType) {
		this.birctrlType = birctrlType;
	}

	public String getLatechbFlag() {
		return latechbFlag;
	}

	public void setLatechbFlag(String latechbFlag) {
		this.latechbFlag = latechbFlag;
	}

	public String getGesoVal() {
		return gesoVal;
	}

	public void setGesoVal(String gesoVal) {
		this.gesoVal = gesoVal;
	}

	public String getFetts() {
		return fetts;
	}

	public void setFetts(String fetts) {
		this.fetts = fetts;
	}

	public String getFetusCnt() {
		return fetusCnt;
	}

	public void setFetusCnt(String fetusCnt) {
		this.fetusCnt = fetusCnt;
	}

	public String getPretFlag() {
		return pretFlag;
	}

	public void setPretFlag(String pretFlag) {
		this.pretFlag = pretFlag;
	}

	public String getBirctrlMatnDate() {
		return birctrlMatnDate;
	}

	public void setBirctrlMatnDate(String birctrlMatnDate) {
		this.birctrlMatnDate = birctrlMatnDate;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCopFlag() {
		return copFlag;
	}

	public void setCopFlag(String copFlag) {
		this.copFlag = copFlag;
	}

	public String getDscgDeptCodg() {
		return dscgDeptCodg;
	}

	public void setDscgDeptCodg(String dscgDeptCodg) {
		this.dscgDeptCodg = dscgDeptCodg;
	}

	public String getDscgDeptName() {
		return dscgDeptName;
	}

	public void setDscgDeptName(String dscgDeptName) {
		this.dscgDeptName = dscgDeptName;
	}

	public String getDscgBed() {
		return dscgBed;
	}

	public void setDscgBed(String dscgBed) {
		this.dscgBed = dscgBed;
	}

	public String getDscgWay() {
		return dscgWay;
	}

	public void setDscgWay(String dscgWay) {
		this.dscgWay = dscgWay;
	}

	public String getDieDate() {
		return dieDate;
	}

	public void setDieDate(String dieDate) {
		this.dieDate = dieDate;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getInsuplcAdmdvs() {
		return insuplcAdmdvs;
	}

	public void setInsuplcAdmdvs(String insuplcAdmdvs) {
		this.insuplcAdmdvs = insuplcAdmdvs;
	}

	public String getOpterType() {
		return opterType;
	}

	public void setOpterType(String opterType) {
		this.opterType = opterType;
	}

	public String getOpter() {
		return opter;
	}

	public void setOpter(String opter) {
		this.opter = opter;
	}

	public String getOpterName() {
		return opterName;
	}

	public void setOpterName(String opterName) {
		this.opterName = opterName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getRjzd() {
		return rjzd;
	}

	public void setRjzd(String rjzd) {
		this.rjzd = rjzd;
	}

	public String getMsgid2401() {
		return msgid2401;
	}

	public void setMsgid2401(String msgid2401) {
		this.msgid2401 = msgid2401;
	}

	public String getMsgid2402() {
		return msgid2402;
	}

	public void setMsgid2402(String msgid2402) {
		this.msgid2402 = msgid2402;
	}

	public String getExamineStatus() {
		return examineStatus;
	}

	public void setExamineStatus(String examineStatus) {
		this.examineStatus = examineStatus;
	}

	public String getExamineMsg() {
		return examineMsg;
	}

	public void setExamineMsg(String examineMsg) {
		this.examineMsg = examineMsg;
	}

	public String getCardSn() {
		return cardSn;
	}

	public void setCardSn(String cardSn) {
		this.cardSn = cardSn;
	}

	public String getPsnName() {
		return psnName;
	}

	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}

	public String getCertno() {
		return certno;
	}

	public void setCertno(String certno) {
		this.certno = certno;
	}

	public String getDtIdtype() {
		return dtIdtype;
	}

	public void setDtIdtype(String dtIdtype) {
		this.dtIdtype = dtIdtype;
	}

	public String getHpCode() {
		return hpCode;
	}

	public void setHpCode(String hpCode) {
		this.hpCode = hpCode;
	}

	public String getPsnIdetType() {
		return psnIdetType;
	}

	public void setPsnIdetType(String psnIdetType) {
		this.psnIdetType = psnIdetType;
	}

	public String getIdetBegntime() {
		return idetBegntime;
	}

	public void setIdetBegntime(String idetBegntime) {
		this.idetBegntime = idetBegntime;
	}

	public String getIdetEndtime() {
		return idetEndtime;
	}

	public void setIdetEndtime(String idetEndtime) {
		this.idetEndtime = idetEndtime;
	}
	
}
