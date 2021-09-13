package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_SETTLE  - 收费结算-结算记录 
 *
 * @since 2016-09-27 09:39:04
 */
@Table(value="BL_SETTLE")
public class ZsbaBlSettle extends BaseModule implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	public Object clone() {  
		ZsbaBlSettle o = null;  
        try {  
            o = (ZsbaBlSettle) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  

	/** PK_SETTLE - 结算主键 */
	@PK
	@Field(value="PK_SETTLE",id=KeyId.UUID)
    private String pkSettle;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** PK_PV - 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;

    /** EU_PVTYPE - 就诊类型:1 门诊，2 急诊，3 住院，4 体检，5 家庭病床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

    /** PK_INSURANCE - 患者主医保计划 */
	@Field(value="PK_INSURANCE")
    private String pkInsurance;

    /** DT_STTYPE - 结算类型:
     * 	0X: 门诊流程： 00 门诊收费结算，01 门诊挂号结算 ；
	 *	1X 住院流程：10 出院结算，11中途结算；
	 *	2X 反流程结算：20 冲账，21 取消结算 
	 */
	@Field(value="DT_STTYPE")
    private String dtSttype;

    /** EU_STRESULT - 结算结果分类:0 正常结算，1 欠款结算，2 存款结算 */
	@Field(value="EU_STRESULT")
    private String euStresult;

    /** DATE_BEGIN - 结算涉及的住院开始时间 */
	@Field(value="DATE_BEGIN")
    private Date dateBegin;

    /** DATE_END - 结算涉及的住院截止时间 */
	@Field(value="DATE_END")
    private Date dateEnd;

    /** AMOUNT_PREP - 结算涉及的预交金合计 */
	@Field(value="AMOUNT_PREP")
    private BigDecimal amountPrep;

    /** AMOUNT_ST - 结算总金额 */
	@Field(value="AMOUNT_ST")
    private BigDecimal amountSt;

    /** AMOUNT_PI - 患者自费金额 */
	@Field(value="AMOUNT_PI")
    private BigDecimal amountPi;

    /** AMOUNT_INSU - 第三方医保金额 */
	@Field(value="AMOUNT_INSU")
    private BigDecimal amountInsu;

    /** DATE_ST - 结算日期 */
	@Field(value="DATE_ST")
    private Date dateSt;

    /** PK_ORG_ST - 结算机构 */
	@Field(value="PK_ORG_ST")
    private String pkOrgSt;

    /** PK_DEPT_ST - 结算部门 */
	@Field(value="PK_DEPT_ST")
    private String pkDeptSt;

    /** PK_EMP_ST - 结算人员 */
	@Field(value="PK_EMP_ST")
    private String pkEmpSt;

    /** NAME_EMP_ST - 结算人员姓名 */
	@Field(value="NAME_EMP_ST")
    private String nameEmpSt;

    /** PK_CC - 操作员结账主键 */
	@Field(value="PK_CC")
    private String pkCc;

    /** FLAG_CC - 操作员结账标志 */
	@Field(value="FLAG_CC")
    private String flagCc;

    /** FLAG_CANC - 取消标志 */
	@Field(value="FLAG_CANC")
    private String flagCanc;

    /** REASON_CANC - 取消原因描述 */
	@Field(value="REASON_CANC")
    private String reasonCanc;

    /** PK_SETTLE_CANC - 取消关联结算主键 */
	@Field(value="PK_SETTLE_CANC")
    private String pkSettleCanc;

    /** FLAG_ARCLARE - 欠费结清标志 */
	@Field(value="FLAG_ARCLARE")
    private String flagArclare;
    
    /** PK_SETTLE_REV - 关联结算主键 */
	@Field(value="PK_SETTLE_REV")
    private String pkSettleRev;
	
	/**加收金额 */
	@Field(value="AMOUNT_ADD")
    private Double amountAdd;
	
	/**结算编码*/
	@Field(value="CODE_ST")
	private String codeSt;
	
	/**优惠金额*/
	@Field(value="AMOUNT_DISC")
	private Double amountDisc;
	
	/**備註*/
	@Field(value="NOTE")
	private String note;
	
	@Field(value="RECEIPT_NO")
	private String receiptNo;
	
	@Field(value="PK_EMP_RECEIPT")
	private String pkEmpReceipt;
	
	@Field(value="NAME_EMP_RECEIPT")
	private String nameEmpReceipt;
	
	@Field(value="DATE_RECEIPT")
	private Date dateReceipt;
	
	@Field(value="EU_PREST")
	private String euPrest;
	
	@Field(value="PK_EMP_FINISH")
	private String pkEmpFinish;
	
	@Field(value="NAME_EMP_FINISH")
	private String nameEmpFinish;
	
	@Field(value="DATE_FINISH")
	private Date dateFinish;
	
	/**舍入金额*/
	@Field(value="AMOUNT_ROUND")
	private BigDecimal amountRound;
	
	/**计生项目编码*/
	@Field(value="DT_FAYPLGITEM")
	private String dtFayplgitem;
	
	/**所在镇区：计划生育结算时需要填写这个*/
	@Field(value="ADDR_CUR_TOWN")
	private String addrCurTown;
	
    public BigDecimal getAmountRound() {
		return amountRound;
	}
	public void setAmountRound(BigDecimal amountRound) {
		this.amountRound = amountRound;
	}
	public String getEuPrest() {
		return euPrest;
	}
	public void setEuPrest(String euPrest) {
		this.euPrest = euPrest;
	}
	public String getPkEmpFinish() {
		return pkEmpFinish;
	}
	public void setPkEmpFinish(String pkEmpFinish) {
		this.pkEmpFinish = pkEmpFinish;
	}
	public String getNameEmpFinish() {
		return nameEmpFinish;
	}
	public void setNameEmpFinish(String nameEmpFinish) {
		this.nameEmpFinish = nameEmpFinish;
	}
	public Date getDateFinish() {
		return dateFinish;
	}
	public void setDateFinish(Date dateFinish) {
		this.dateFinish = dateFinish;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getPkEmpReceipt() {
		return pkEmpReceipt;
	}
	public void setPkEmpReceipt(String pkEmpReceipt) {
		this.pkEmpReceipt = pkEmpReceipt;
	}
	public String getNameEmpReceipt() {
		return nameEmpReceipt;
	}
	public void setNameEmpReceipt(String nameEmpReceipt) {
		this.nameEmpReceipt = nameEmpReceipt;
	}
	public Date getDateReceipt() {
		return dateReceipt;
	}
	public void setDateReceipt(Date dateReceipt) {
		this.dateReceipt = dateReceipt;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Double getAmountDisc() {
		return amountDisc;
	}
	public void setAmountDisc(Double amountDisc) {
		this.amountDisc = amountDisc;
	}
	public String getCodeSt() {
		return codeSt;
	}
	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}
	public Double getAmountAdd() {
		return amountAdd;
	}
	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}
	public String getPkSettleRev() {
		return pkSettleRev;
	}
	public void setPkSettleRev(String pkSettleRev) {
		this.pkSettleRev = pkSettleRev;
	}
	public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkInsurance(){
        return this.pkInsurance;
    }
    public void setPkInsurance(String pkInsurance){
        this.pkInsurance = pkInsurance;
    }

    public String getDtSttype(){
        return this.dtSttype;
    }
    public void setDtSttype(String dtSttype){
        this.dtSttype = dtSttype;
    }

    public String getEuStresult(){
        return this.euStresult;
    }
    public void setEuStresult(String euStresult){
        this.euStresult = euStresult;
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

    public BigDecimal getAmountPrep() {
		return amountPrep;
	}
	public void setAmountPrep(BigDecimal amountPrep) {
		this.amountPrep = amountPrep;
	}
	public BigDecimal getAmountSt() {
		return amountSt;
	}
	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public BigDecimal getAmountInsu() {
		return amountInsu;
	}
	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}
	public Date getDateSt(){
        return this.dateSt;
    }
    public void setDateSt(Date dateSt){
        this.dateSt = dateSt;
    }

    public String getPkOrgSt(){
        return this.pkOrgSt;
    }
    public void setPkOrgSt(String pkOrgSt){
        this.pkOrgSt = pkOrgSt;
    }

    public String getPkDeptSt(){
        return this.pkDeptSt;
    }
    public void setPkDeptSt(String pkDeptSt){
        this.pkDeptSt = pkDeptSt;
    }

    public String getPkEmpSt(){
        return this.pkEmpSt;
    }
    public void setPkEmpSt(String pkEmpSt){
        this.pkEmpSt = pkEmpSt;
    }

    public String getNameEmpSt(){
        return this.nameEmpSt;
    }
    public void setNameEmpSt(String nameEmpSt){
        this.nameEmpSt = nameEmpSt;
    }

    public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
    }

    public String getFlagCc(){
        return this.flagCc;
    }
    public void setFlagCc(String flagCc){
        this.flagCc = flagCc;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public String getReasonCanc(){
        return this.reasonCanc;
    }
    public void setReasonCanc(String reasonCanc){
        this.reasonCanc = reasonCanc;
    }

    public String getPkSettleCanc(){
        return this.pkSettleCanc;
    }
    public void setPkSettleCanc(String pkSettleCanc){
        this.pkSettleCanc = pkSettleCanc;
    }

    public String getFlagArclare(){
        return this.flagArclare;
    }
    public void setFlagArclare(String flagArclare){
        this.flagArclare = flagArclare;
    }
	public String getDtFayplgitem() {
		return dtFayplgitem;
	}
	public void setDtFayplgitem(String dtFayplgitem) {
		this.dtFayplgitem = dtFayplgitem;
	}
	public String getAddrCurTown() {
		return addrCurTown;
	}
	public void setAddrCurTown(String addrCurTown) {
		this.addrCurTown = addrCurTown;
	}


}