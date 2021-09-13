package com.zebone.nhis.common.module.bl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_DEPOSIT_PI  - BL_DEPOSIT_PI 
 *
 * @since 2016-09-27 10:36:56
 */
@Table(value="BL_DEPOSIT_PI")
public class BlDepositPi extends BaseModule  {

	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_DEPOPI",id=KeyId.UUID)
    private String pkDepopi;

    /** EU_DIRECT - 1收 -1退 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="AMOUNT")
    private BigDecimal amount;

    /** DT_PAYMODE - 例如：现金；支票；银行卡；账户 */
	@Field(value="DT_PAYMODE")
    private String dtPaymode;

    /** DT_BANK - 银行卡或支票支付时对应基础数据的银行档案 */
	@Field(value="DT_BANK")
    private String dtBank;

    /** BANK_NO - 银行卡支付时，对应的银行卡号 */
	@Field(value="BANK_NO")
    private String bankNo;

    /** PAY_INFO - 对应支票号，银行交易号码等 */
	@Field(value="PAY_INFO")
    private String payInfo;

	@Field(value="DATE_PAY")
    private Date datePay;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP_PAY")
    private String pkEmpPay;

	@Field(value="NAME_EMP_PAY")
    private String nameEmpPay;

	@Field(value="FLAG_CC")
    private String flagCc;

	@Field(value="PK_CC")
    private String pkCc;

	@Field(value="REPT_NO")
    private String reptNo;

    /** PK_SETTLE - 转存患者账户对应的结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	/** 收付款方式名称 */
	private String paymodeName;
	
	private Date bankTime; // 交易日期
	
	private List<BlExtPayBankVo> blPayTrdVO; // 支付方式为第三方支付方式，退费时修改bl_ext_pay表
	
	/** 交易金额 **/
	private BigDecimal amountThird;
	
	/** pos机编码**/
	private String outTradeNo;
	
	/** 结算标志**/
	private String payResult;
	
	/** 银行卡流水号**/
	private String serialNo;
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public BigDecimal getAmountThird() {
		return amountThird;
	}
	public void setAmountThird(BigDecimal amountThird) {
		this.amountThird = amountThird;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	public Date getBankTime() {
		return bankTime;
	}
	public void setBankTime(Date bankTime) {
		this.bankTime = bankTime;
	}
	
	public List<BlExtPayBankVo> getBlPayTrdVO() {
		return blPayTrdVO;
	}
	public void setBlPayTrdVO(List<BlExtPayBankVo> blPayTrdVO) {
		this.blPayTrdVO = blPayTrdVO;
	}
	public String getPkDepopi(){
        return this.pkDepopi;
    }
    public void setPkDepopi(String pkDepopi){
        this.pkDepopi = pkDepopi;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDtPaymode(){
        return this.dtPaymode;
    }
    public void setDtPaymode(String dtPaymode){
        this.dtPaymode = dtPaymode;
    }

    public String getDtBank(){
        return this.dtBank;
    }
    public void setDtBank(String dtBank){
        this.dtBank = dtBank;
    }

    public String getBankNo(){
        return this.bankNo;
    }
    public void setBankNo(String bankNo){
        this.bankNo = bankNo;
    }

    public String getPayInfo(){
        return this.payInfo;
    }
    public void setPayInfo(String payInfo){
        this.payInfo = payInfo;
    }

    public Date getDatePay(){
        return this.datePay;
    }
    public void setDatePay(Date datePay){
        this.datePay = datePay;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmpPay(){
        return this.pkEmpPay;
    }
    public void setPkEmpPay(String pkEmpPay){
        this.pkEmpPay = pkEmpPay;
    }

    public String getNameEmpPay(){
        return this.nameEmpPay;
    }
    public void setNameEmpPay(String nameEmpPay){
        this.nameEmpPay = nameEmpPay;
    }

    public String getFlagCc(){
        return this.flagCc;
    }
    public void setFlagCc(String flagCc){
        this.flagCc = flagCc;
    }

    public String getPkCc(){
        return this.pkCc;
    }
    public void setPkCc(String pkCc){
        this.pkCc = pkCc;
    }

    public String getReptNo(){
        return this.reptNo;
    }
    public void setReptNo(String reptNo){
        this.reptNo = reptNo;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getPaymodeName() {
		return paymodeName;
	}
	public void setPaymodeName(String paymodeName) {
		this.paymodeName = paymodeName;
	}
}