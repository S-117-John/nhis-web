package com.zebone.nhis.common.module.pi.acc;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_ACC_DETAIL  - PI_ACC_DETAIL 
 *
 * @since 2016-09-27 01:45:34
 */
@Table(value="PI_ACC_DETAIL")
public class PiAccDetail extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_ACCDT",id=KeyId.UUID)
    private String pkAccdt;

	@Field(value="PK_PIACC")
    private String pkPiacc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DATE_HAP")
    private Date dateHap;

    /** EU_OPTYPE - 1充值 2账户消费 3信用消费 9退费
信用额度的增加和减少从pi_acc_credit中获取
退费时金额不能超过账户余额，并且不能超过同类型累计充值总额 */
	@Field(value="EU_OPTYPE")
    private String euOptype;

    /** EU_DIRECT - 1增加，-1减少 */
	@Field(value="EU_DIRECT")
    private String euDirect;

	@Field(value="AMOUNT")
    private BigDecimal amount;

	@Field(value="NOTE")
    private String note;

	@Field(value="AMT_BALANCE")
    private BigDecimal amtBalance;

    /** PK_DEPOPI - 非医院人员操作则不填 */
	@Field(value="PK_DEPOPI")
    private String pkDepopi;

    /** PK_EMP_OPERA - 非医院人员操作则不填 */
	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

	@Field(value="ATM_NO")
    private String atmNo;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    public String getPkAccdt(){
        return this.pkAccdt;
    }
    public void setPkAccdt(String pkAccdt){
        this.pkAccdt = pkAccdt;
    }

    public String getPkPiacc(){
        return this.pkPiacc;
    }
    public void setPkPiacc(String pkPiacc){
        this.pkPiacc = pkPiacc;
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

    public Date getDateHap(){
        return this.dateHap;
    }
    public void setDateHap(Date dateHap){
        this.dateHap = dateHap;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
    public BigDecimal getAmtBalance() {
		return amtBalance;
	}
	public void setAmtBalance(BigDecimal amtBalance) {
		this.amtBalance = amtBalance;
	}
	public String getPkDepopi(){
        return this.pkDepopi;
    }
    public void setPkDepopi(String pkDepopi){
        this.pkDepopi = pkDepopi;
    }

    public String getPkEmpOpera(){
        return this.pkEmpOpera;
    }
    public void setPkEmpOpera(String pkEmpOpera){
        this.pkEmpOpera = pkEmpOpera;
    }

    public String getNameEmpOpera(){
        return this.nameEmpOpera;
    }
    public void setNameEmpOpera(String nameEmpOpera){
        this.nameEmpOpera = nameEmpOpera;
    }

    public String getAtmNo(){
        return this.atmNo;
    }
    public void setAtmNo(String atmNo){
        this.atmNo = atmNo;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

}