package com.zebone.nhis.common.module.emr.mgr;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_SEAL_REC 
 *
 * @since 2017-12-11 09:55:04
 */
@Table(value="EMR_SEAL_REC")
public class EmrSealRec extends BaseModule  {

	@PK
	@Field(value="PK_SEALREC",id=KeyId.UUID)
    private String pkSealrec;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="DATE_SEAL")
    private Date dateSeal;

	@Field(value="PK_EMP_SEAL")
    private String pkEmpSeal;

    /** SEAL_PATI - 天 */
	@Field(value="SEAL_PATI")
    private String sealPati;

    /** PWD_PATI - 天 */
	@Field(value="PWD_PATI")
    private String pwdPati;

    /** SEAL_DEPT - 天 */
	@Field(value="SEAL_DEPT")
    private String sealDept;

    /** PWD_DEPT - 天 */
	@Field(value="PWD_DEPT")
    private String pwdDept;

    /** SEAL_HOS - 天 */
	@Field(value="SEAL_HOS")
    private String sealHos;

    /** PWD_HOS - 天 */
	@Field(value="PWD_HOS")
    private String pwdHos;

	@Field(value="DATE_RELEASE")
    private Date dateRelease;

	@Field(value="CONTENT_SEAL")
    private String contentSeal;

    /** EU_STATUS - 1-已封，2-解封 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="REMARK")
    private String remark;


    public String getPkSealrec(){
        return this.pkSealrec;
    }
    public void setPkSealrec(String pkSealrec){
        this.pkSealrec = pkSealrec;
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

    public Date getDateSeal(){
        return this.dateSeal;
    }
    public void setDateSeal(Date dateSeal){
        this.dateSeal = dateSeal;
    }

    public String getPkEmpSeal(){
        return this.pkEmpSeal;
    }
    public void setPkEmpSeal(String pkEmpSeal){
        this.pkEmpSeal = pkEmpSeal;
    }

    public String getSealPati(){
        return this.sealPati;
    }
    public void setSealPati(String sealPati){
        this.sealPati = sealPati;
    }

    public String getPwdPati(){
        return this.pwdPati;
    }
    public void setPwdPati(String pwdPati){
        this.pwdPati = pwdPati;
    }

    public String getSealDept(){
        return this.sealDept;
    }
    public void setSealDept(String sealDept){
        this.sealDept = sealDept;
    }

    public String getPwdDept(){
        return this.pwdDept;
    }
    public void setPwdDept(String pwdDept){
        this.pwdDept = pwdDept;
    }

    public String getSealHos(){
        return this.sealHos;
    }
    public void setSealHos(String sealHos){
        this.sealHos = sealHos;
    }

    public String getPwdHos(){
        return this.pwdHos;
    }
    public void setPwdHos(String pwdHos){
        this.pwdHos = pwdHos;
    }

    public Date getDateRelease(){
        return this.dateRelease;
    }
    public void setDateRelease(Date dateRelease){
        this.dateRelease = dateRelease;
    }

    public String getContentSeal(){
        return this.contentSeal;
    }
    public void setContentSeal(String contentSeal){
        this.contentSeal = contentSeal;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
}