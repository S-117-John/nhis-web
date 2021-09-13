package com.zebone.nhis.common.module.base.bd.price;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_HP_FACTOR  - bd_hp_factor 
 *
 * @since 2017-08-25 02:38:47
 */
@Table(value="BD_HP_FACTOR")
public class BdHpFactor extends BaseModule  {

	@PK
	@Field(value="PK_HPFACTOR",id=KeyId.UUID)
    private String pkHpfactor;

	/** 医保计划主键 */
	@Field(value="PK_HP")
    private String pkHp;

    /** 就诊类别 - 1门诊，2急诊，3住院，4体检，5家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	/** 使用部门 */
	@Field(value="PK_DEPT")
    private String pkDept;

	/** 预交金系数 */
	@Field(value="FACTOR_PREP")
    private Double factorPrep;
	
	/** 允许欠费金额 */
	@Field(value="AMT_CRED")
    private Double amtCred;

	/** 担保金系数 */
	@Field(value="FACTOR_CRED")
    private Double factorCred;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkHpfactor(){
        return this.pkHpfactor;
    }
    public void setPkHpfactor(String pkHpfactor){
        this.pkHpfactor = pkHpfactor;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Double getFactorPrep(){
        return this.factorPrep;
    }
    public void setFactorPrep(Double factorPrep){
        this.factorPrep = factorPrep;
    }

    public Double getAmtCred() {
		return amtCred;
	}
	public void setAmtCred(Double amtCred) {
		this.amtCred = amtCred;
	}
	public Double getFactorCred(){
        return this.factorCred;
    }
    public void setFactorCred(Double factorCred){
        this.factorCred = factorCred;
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
}