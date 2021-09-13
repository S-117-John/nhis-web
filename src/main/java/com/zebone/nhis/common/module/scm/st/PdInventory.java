package com.zebone.nhis.common.module.scm.st;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_INVENTORY - pd_inventory 
 *
 * @since 2016-11-09 09:29:51
 */
@Table(value="PD_INVENTORY")
public class PdInventory extends BaseModule  {

	@PK
	@Field(value="PK_PDINV",id=KeyId.UUID)
    private String pkPdinv;

    /** DT_INVTYPE - 01月盘点 02临时盘点 03期初盘点 */
	@Field(value="DT_INVTYPE")
    private String dtInvtype;

	@Field(value="CODE_INV")
    private String codeInv;

	@Field(value="NAME_INV")
    private String nameInv;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

	@Field(value="DATE_INV")
    private Date dateInv;

	@Field(value="PK_EMP_INV")
    private String pkEmpInv;

	@Field(value="NAME_EMP_INV")
    private String nameEmpInv;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

    /** EU_STATUS - 0制单，1审核 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkPdinv(){
        return this.pkPdinv;
    }
    public void setPkPdinv(String pkPdinv){
        this.pkPdinv = pkPdinv;
    }

    public String getDtInvtype(){
        return this.dtInvtype;
    }
    public void setDtInvtype(String dtInvtype){
        this.dtInvtype = dtInvtype;
    }

    public String getCodeInv(){
        return this.codeInv;
    }
    public void setCodeInv(String codeInv){
        this.codeInv = codeInv;
    }

    public String getNameInv(){
        return this.nameInv;
    }
    public void setNameInv(String nameInv){
        this.nameInv = nameInv;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public Date getDateInv(){
        return this.dateInv;
    }
    public void setDateInv(Date dateInv){
        this.dateInv = dateInv;
    }

    public String getPkEmpInv(){
        return this.pkEmpInv;
    }
    public void setPkEmpInv(String pkEmpInv){
        this.pkEmpInv = pkEmpInv;
    }

    public String getNameEmpInv(){
        return this.nameEmpInv;
    }
    public void setNameEmpInv(String nameEmpInv){
        this.nameEmpInv = nameEmpInv;
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

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}