package com.zebone.nhis.common.module.ex.nis.fee;

import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PV_IP_ACC  - 患者就诊-住院担保记录 
 *
 * @since 2016-09-19 12:48:02
 */
@Table(value="PV_IP_ACC")
public class PvIpAcc extends BaseModule  {

	@PK
	@Field(value="PK_IPACC",id=KeyId.UUID)
    private String pkIpacc;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="AMT_CREDIT")
    private BigDecimal amtCredit;

	@Field(value="PK_EMP_WAR")
    private String pkEmpWar;

	@Field(value="NAME_EMP_WAR")
    private String nameEmpWar;

	@Field(value="DATE_WAR")
    private Date dateWar;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;
	
	@Field(value="Note")
    private String note;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkIpacc(){
        return this.pkIpacc;
    }
    public void setPkIpacc(String pkIpacc){
        this.pkIpacc = pkIpacc;
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

    public BigDecimal getAmtCredit(){
        return this.amtCredit;
    }
    public void setAmtCredit(BigDecimal amtCredit){
        this.amtCredit = amtCredit;
    }

    public String getPkEmpWar(){
        return this.pkEmpWar;
    }
    public void setPkEmpWar(String pkEmpWar){
        this.pkEmpWar = pkEmpWar;
    }

    public String getNameEmpWar(){
        return this.nameEmpWar;
    }
    public void setNameEmpWar(String nameEmpWar){
        this.nameEmpWar = nameEmpWar;
    }

    public Date getDateWar(){
        return this.dateWar;
    }
    public void setDateWar(Date dateWar){
        this.dateWar = dateWar;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}
