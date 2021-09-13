package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: ins_st_qg
 *
 * @since 2020-10-26 10:42:10
 */
@Table(value="INS_ST_QG")
public class InsZsbaStQg extends BaseModule implements Cloneable{

	@PK
	@Field(value="PK_INSSTQG",id=KeyId.UUID)
    private String pkInsstqg;
	
	@PK
	@Field(value="PK_INSPVQG")
    private String pkInspvqg;
	
	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="PK_SETTLE")
    private String pkSettle;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	@Field(value="MDTRT_CERT_TYPE")
    private String mdtrtCertType;
	
	@Field(value="MDTRT_CERT_NO")
    private String mdtrtCertNo;
	
	@Field(value="PSN_SETLWAY")
    private String psnSetlway;
	
	@Field(value="MDTRT_ID")
    private String mdtrtId;
	
	@Field(value="ACCT_USED_FLAG")
    private String acctUsedFlag;
	
	@Field(value="INVONO")
    private String invono;
	
	@Field(value="SETL_ID")
    private String setlId;
	
	@Field(value="PSN_NO")
    private String psnNo;
	
	@Field(value="PSN_NAME")
    private String psnName;
	
	@Field(value="PSN_CERT_TYPE")
    private String psnCertType;
	
	@Field(value="CERTNO")
    private String certno;
	
	@Field(value="GEND")
    private String gend;
	
	@Field(value="NATY")
    private String naty;
	
	@Field(value="BRDY")
    private String brdy;
	
	@Field(value="AGE")
    private String age;
	
	@Field(value="INSUTYPE")
    private String insutype;
	
	@Field(value="PSN_TYPE")
    private String psnType;
	
	@Field(value="CVLSERV_FLAG")
    private String cvlservFlag;
	
	@Field(value="SETL_TIME")
    private Date setlTime;
	
	@Field(value="MED_TYPE")
    private String medType;
	
	@Field(value="MEDFEE_SUMAMT")
    private BigDecimal medfeeSumamt;
	
	@Field(value="FULAMT_OWNPAY_AMT")
    private BigDecimal fulamtOwnpayAmt;
	
	@Field(value="OVERLMT_SELFPAY")
    private BigDecimal overlmtSelfpay;
	
	@Field(value="PRESELFPAY_AMT")
    private BigDecimal preselfpayAmt;
	
	@Field(value="INSCP_SCP_AMT")
    private BigDecimal inscpScpAmt;
	
	@Field(value="ACT_PAY_DEDC")
    private BigDecimal actPayDedc;
	
	@Field(value="HIFP_PAY")
    private BigDecimal hifpPay;
	
	@Field(value="POOL_PROP_SELFPAY")
    private BigDecimal poolPropSelfpay;
	
	@Field(value="CVLSERV_PAY")
    private BigDecimal cvlservPay;
	
	@Field(value="HIFES_PAY")
    private BigDecimal hifesPay;
	
	@Field(value="HIFMI_PAY")
    private BigDecimal hifmiPay;
	
	@Field(value="HIFOB_PAY")
    private BigDecimal hifobPay;
	
	@Field(value="MAF_PAY")
    private BigDecimal mafPay;
	
	@Field(value="HOSP_PART_AMT")
    private BigDecimal hospPartAmt;
	
	@Field(value="OTH_PAY")
    private BigDecimal othPay;
	
	@Field(value="FUND_PAY_SUMAMT")
    private BigDecimal fundPaySumamt;
	
	@Field(value="PSN_PART_AMT")
    private BigDecimal psnPartAmt;
	
	@Field(value="ACCT_PAY")
    private BigDecimal acctPay;
	
	@Field(value="PSN_CASH_PAY")
    private BigDecimal psnCashPay;
	
	@Field(value="BALC")
    private BigDecimal balc;
	
	@Field(value="ACCT_MULAID_PAY")
    private BigDecimal acctMulaidPay;
	
	@Field(value="MEDINS_SETL_ID")
	private String medinsSetlId;
	
	@Field(value="CLR_OPTINS")
	private String clrOptins;
	
	@Field(value="CLR_WAY")
	private String clrWay;
	
	@Field(value="CLR_TYPE")
	private String clrType;
	
	@Field(value="STATUS")
	private String status;
	
	@Field(value="POS_SN")
	private String posSn;
	
	@Field(value="YBZHYE")
	private BigDecimal ybzhye;
	
	@Field(value="ND")
	private String nd;
	
	@Field(value="RYLB")
	private String rylb;
	
	@Field(value="KH")
	private String kh;
	
	@Field(value="JYM")
	private String jym;
	
	@Field(value="PSAM")
	private String psam;
	
	@Field(value="CBH")
	private String cbh;
	
	@Field(value="NDK")
	private String ndk;
	
	@Field(value="MSGID2304")
	private String msgid2304;
	
	@Field(value="SETLISTID")
	private String setListId;//这是个啥字段
	
	@Field(value="TRT_YEAR")
	private String trtYear; //结算年度
	
	@Field(value="TRT_MONTH")
	private String trtMonth; // 结算月份
	
	@Field(value="CNFM_FLAG")
	private String cnfmflag; // 确认标志 0-不确认，本次不纳入清分；1-确认，纳入本次清分
	
	@Field(value="MSGID3261")
	private String msgid3261; // 5.1.1.2异地清分结果确认
	
	@Field(value="MSGID3262")
	private String msgid3262; // 5.1.1.3异地清分结果确认回退
	
	@Field(value="FLAG_REC")
	private String flagRec; // 是否对账  1是 0否
	 
	@Field(value="TRT_YEAR_MONTH")
	private String trtYearMonth; // 对账年月,用于异地(未上线全国医保的参保地)清分
	 
	@Field(value="HIS_TC")
	private String hisTc; //经过his运算后的统筹金额(因单价小数点的问题，最终进行his报销的金额跟医保的不一定一样)
	
	@Field(value="HIS_GZ")
	private String hisGz; //本地结算使用的个账金额
	
	@Field(value="HIS_ZF")
	private String hisZf; //经过his运算后的支付金额
	
	@Field(value="MID_SETL_FLAG")
	private String mid_setl_flag; //中途结算标志
	
    private String code;
	
    private String msg;
    
    @Field(value="ZDBJDX_AMT")
    private String zdbjdxAmt;// 重点保健对象记账金额

	public Object clone() {  
		InsZsbaStQg o = null;  
        try {  
            o = (InsZsbaStQg) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
    
	public String getPkInsstqg() {
		return pkInsstqg;
	}

	public void setPkInsstqg(String pkInsstqg) {
		this.pkInsstqg = pkInsstqg;
	}

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

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
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

	public String getPsnSetlway() {
		return psnSetlway;
	}

	public void setPsnSetlway(String psnSetlway) {
		this.psnSetlway = psnSetlway;
	}

	public String getMdtrtId() {
		return mdtrtId;
	}

	public void setMdtrtId(String mdtrtId) {
		this.mdtrtId = mdtrtId;
	}

	public String getAcctUsedFlag() {
		return acctUsedFlag;
	}

	public void setAcctUsedFlag(String acctUsedFlag) {
		this.acctUsedFlag = acctUsedFlag;
	}

	public String getInvono() {
		return invono;
	}

	public void setInvono(String invono) {
		this.invono = invono;
	}

	public String getSetlId() {
		return setlId;
	}

	public void setSetlId(String setlId) {
		this.setlId = setlId;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getPsnName() {
		return psnName;
	}

	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}

	public String getPsnCertType() {
		return psnCertType;
	}

	public void setPsnCertType(String psnCertType) {
		this.psnCertType = psnCertType;
	}

	public String getCertno() {
		return certno;
	}

	public void setCertno(String certno) {
		this.certno = certno;
	}

	public String getGend() {
		return gend;
	}

	public void setGend(String gend) {
		this.gend = gend;
	}

	public String getNaty() {
		return naty;
	}

	public void setNaty(String naty) {
		this.naty = naty;
	}

	public String getBrdy() {
		return brdy;
	}

	public void setBrdy(String brdy) {
		this.brdy = brdy;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}

	public String getPsnType() {
		return psnType;
	}

	public void setPsnType(String psnType) {
		this.psnType = psnType;
	}

	public String getCvlservFlag() {
		return cvlservFlag;
	}

	public void setCvlservFlag(String cvlservFlag) {
		this.cvlservFlag = cvlservFlag;
	}

	public Date getSetlTime() {
		return setlTime;
	}

	public void setSetlTime(Date setlTime) {
		this.setlTime = setlTime;
	}

	public String getMedType() {
		return medType;
	}

	public void setMedType(String medType) {
		this.medType = medType;
	}

	public BigDecimal getMedfeeSumamt() {
		return medfeeSumamt;
	}

	public void setMedfeeSumamt(BigDecimal medfeeSumamt) {
		this.medfeeSumamt = medfeeSumamt;
	}

	public BigDecimal getFulamtOwnpayAmt() {
		return fulamtOwnpayAmt;
	}

	public void setFulamtOwnpayAmt(BigDecimal fulamtOwnpayAmt) {
		this.fulamtOwnpayAmt = fulamtOwnpayAmt;
	}

	public BigDecimal getOverlmtSelfpay() {
		return overlmtSelfpay;
	}

	public void setOverlmtSelfpay(BigDecimal overlmtSelfpay) {
		this.overlmtSelfpay = overlmtSelfpay;
	}

	public BigDecimal getPreselfpayAmt() {
		return preselfpayAmt;
	}

	public void setPreselfpayAmt(BigDecimal preselfpayAmt) {
		this.preselfpayAmt = preselfpayAmt;
	}

	public BigDecimal getInscpScpAmt() {
		return inscpScpAmt;
	}

	public void setInscpScpAmt(BigDecimal inscpScpAmt) {
		this.inscpScpAmt = inscpScpAmt;
	}

	public BigDecimal getActPayDedc() {
		return actPayDedc;
	}

	public void setActPayDedc(BigDecimal actPayDedc) {
		this.actPayDedc = actPayDedc;
	}

	public BigDecimal getHifpPay() {
		return hifpPay;
	}

	public void setHifpPay(BigDecimal hifpPay) {
		this.hifpPay = hifpPay;
	}

	public BigDecimal getPoolPropSelfpay() {
		return poolPropSelfpay;
	}

	public void setPoolPropSelfpay(BigDecimal poolPropSelfpay) {
		this.poolPropSelfpay = poolPropSelfpay;
	}

	public BigDecimal getCvlservPay() {
		return cvlservPay;
	}

	public void setCvlservPay(BigDecimal cvlservPay) {
		this.cvlservPay = cvlservPay;
	}

	public BigDecimal getHifesPay() {
		return hifesPay;
	}

	public void setHifesPay(BigDecimal hifesPay) {
		this.hifesPay = hifesPay;
	}

	public BigDecimal getHifmiPay() {
		return hifmiPay;
	}

	public void setHifmiPay(BigDecimal hifmiPay) {
		this.hifmiPay = hifmiPay;
	}

	public BigDecimal getHifobPay() {
		return hifobPay;
	}

	public void setHifobPay(BigDecimal hifobPay) {
		this.hifobPay = hifobPay;
	}

	public BigDecimal getMafPay() {
		return mafPay;
	}

	public void setMafPay(BigDecimal mafPay) {
		this.mafPay = mafPay;
	}

	public BigDecimal getHospPartAmt() {
		return hospPartAmt;
	}

	public void setHospPartAmt(BigDecimal hospPartAmt) {
		this.hospPartAmt = hospPartAmt;
	}

	public BigDecimal getOthPay() {
		return othPay;
	}

	public void setOthPay(BigDecimal othPay) {
		this.othPay = othPay;
	}

	public BigDecimal getFundPaySumamt() {
		return fundPaySumamt;
	}

	public void setFundPaySumamt(BigDecimal fundPaySumamt) {
		this.fundPaySumamt = fundPaySumamt;
	}

	public BigDecimal getPsnPartAmt() {
		return psnPartAmt;
	}

	public void setPsnPartAmt(BigDecimal psnPartAmt) {
		this.psnPartAmt = psnPartAmt;
	}

	public BigDecimal getAcctPay() {
		return acctPay;
	}

	public void setAcctPay(BigDecimal acctPay) {
		this.acctPay = acctPay;
	}

	public BigDecimal getPsnCashPay() {
		return psnCashPay;
	}

	public void setPsnCashPay(BigDecimal psnCashPay) {
		this.psnCashPay = psnCashPay;
	}

	public BigDecimal getBalc() {
		return balc;
	}

	public void setBalc(BigDecimal balc) {
		this.balc = balc;
	}

	public BigDecimal getAcctMulaidPay() {
		return acctMulaidPay;
	}

	public void setAcctMulaidPay(BigDecimal acctMulaidPay) {
		this.acctMulaidPay = acctMulaidPay;
	}

	public String getMedinsSetlId() {
		return medinsSetlId;
	}

	public void setMedinsSetlId(String medinsSetlId) {
		this.medinsSetlId = medinsSetlId;
	}

	public String getClrOptins() {
		return clrOptins;
	}

	public void setClrOptins(String clrOptins) {
		this.clrOptins = clrOptins;
	}

	public String getClrWay() {
		return clrWay;
	}

	public void setClrWay(String clrWay) {
		this.clrWay = clrWay;
	}

	public String getClrType() {
		return clrType;
	}

	public void setClrType(String clrType) {
		this.clrType = clrType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPosSn() {
		return posSn;
	}

	public void setPosSn(String posSn) {
		this.posSn = posSn;
	}

	public BigDecimal getYbzhye() {
		return ybzhye;
	}

	public void setYbzhye(BigDecimal ybzhye) {
		this.ybzhye = ybzhye;
	}

	public String getNd() {
		return nd;
	}

	public void setNd(String nd) {
		this.nd = nd;
	}

	public String getRylb() {
		return rylb;
	}

	public void setRylb(String rylb) {
		this.rylb = rylb;
	}

	public String getKh() {
		return kh;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public String getJym() {
		return jym;
	}

	public void setJym(String jym) {
		this.jym = jym;
	}

	public String getPsam() {
		return psam;
	}

	public void setPsam(String psam) {
		this.psam = psam;
	}

	public String getCbh() {
		return cbh;
	}

	public void setCbh(String cbh) {
		this.cbh = cbh;
	}

	public String getNdk() {
		return ndk;
	}

	public void setNdk(String ndk) {
		this.ndk = ndk;
	}

	public String getMsgid2304() {
		return msgid2304;
	}

	public void setMsgid2304(String msgid2304) {
		this.msgid2304 = msgid2304;
	}

	public String getSetListId() {
		return setListId;
	}

	public void setSetListId(String setListId) {
		this.setListId = setListId;
	}

	public String getTrtYear() {
		return trtYear;
	}

	public void setTrtYear(String trtYear) {
		this.trtYear = trtYear;
	}

	public String getTrtMonth() {
		return trtMonth;
	}

	public void setTrtMonth(String trtMonth) {
		this.trtMonth = trtMonth;
	}

	public String getCnfmflag() {
		return cnfmflag;
	}

	public void setCnfmflag(String cnfmflag) {
		this.cnfmflag = cnfmflag;
	}

	public String getMsgid3261() {
		return msgid3261;
	}

	public void setMsgid3261(String msgid3261) {
		this.msgid3261 = msgid3261;
	}

	public String getMsgid3262() {
		return msgid3262;
	}

	public void setMsgid3262(String msgid3262) {
		this.msgid3262 = msgid3262;
	}

	public String getFlagRec() {
		return flagRec;
	}

	public void setFlagRec(String flagRec) {
		this.flagRec = flagRec;
	}

	public String getTrtYearMonth() {
		return trtYearMonth;
	}

	public void setTrtYearMonth(String trtYearMonth) {
		this.trtYearMonth = trtYearMonth;
	}

	public String getHisTc() {
		return hisTc;
	}

	public void setHisTc(String hisTc) {
		this.hisTc = hisTc;
	}

	public String getHisGz() {
		return hisGz;
	}

	public void setHisGz(String hisGz) {
		this.hisGz = hisGz;
	}

	public String getHisZf() {
		return hisZf;
	}

	public void setHisZf(String hisZf) {
		this.hisZf = hisZf;
	}

	public String getMid_setl_flag() {
		return mid_setl_flag;
	}

	public void setMid_setl_flag(String mid_setl_flag) {
		this.mid_setl_flag = mid_setl_flag;
	}

	public String getZdbjdxAmt() {
		return zdbjdxAmt;
	}

	public void setZdbjdxAmt(String zdbjdxAmt) {
		this.zdbjdxAmt = zdbjdxAmt;
	}
	
}
