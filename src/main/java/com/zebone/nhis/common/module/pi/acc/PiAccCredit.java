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
 * Table: PI_ACC_CREDIT  - 患者信息-信用操作记录 
 *
 * @since 2016-09-27 09:49:52
 */
@Table(value="PI_ACC_CREDIT")
public class PiAccCredit extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_ACC_CREDIT",id=KeyId.UUID)
    private String pkAccCredit;

	@Field(value="PK_PIACC")
    private String pkPiacc;

	@Field(value="PK_PI")
    private String pkPi;

    /** EU_DIRECT - 信用度额度维护：1 增加；-1 减少； */
	@Field(value="EU_DIRECT")
    private String euDirect;

    /** AMT_CREDIT - 增加或减少的金额 */
	@Field(value="AMT_CREDIT")
    private BigDecimal amtCredit;

	@Field(value="PK_EMP_OPERA")
    private String pkEmpOpera;

	@Field(value="NAME_EMP_OPERA")
    private String nameEmpOpera;

	@Field(value="DATE_OPERA")
    private Date dateOpera;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="CHK_INFO")
    private String chkInfo;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="NOTE")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkAccCredit(){
        return this.pkAccCredit;
    }
    public void setPkAccCredit(String pkAccCredit){
        this.pkAccCredit = pkAccCredit;
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

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public BigDecimal getAmtCredit() {
		return amtCredit;
	}
	public void setAmtCredit(BigDecimal amtCredit) {
		this.amtCredit = amtCredit;
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

    public Date getDateOpera(){
        return this.dateOpera;
    }
    public void setDateOpera(Date dateOpera){
        this.dateOpera = dateOpera;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public String getChkInfo(){
        return this.chkInfo;
    }
    public void setChkInfo(String chkInfo){
        this.chkInfo = chkInfo;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
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
}