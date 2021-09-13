package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_ARCHIVE_REC 
 *
 * @since 2019-12-06 04:39:33
 */
@Table(value="EMR_ARCHIVE_REC")
public class EmrArchiveRec extends BaseModule  {

	@PK
	@Field(value="PK_ARCHIVE",id=KeyId.UUID)
    private String pkArchive;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PATREC")
    private String pkPatrec;

	@Field(value="PK_EMP_APP")
    private String pkEmpApp;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="DATE_DEAL")
    private Date dateDeal;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="DATE_ARVHIVE")
    private Date dateArvhive;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="REMARK")
    private String remark;

    private String status;
	
    public String getPkArchive(){
        return this.pkArchive;
    }
    public void setPkArchive(String pkArchive){
        this.pkArchive = pkArchive;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPatrec(){
        return this.pkPatrec;
    }
    public void setPkPatrec(String pkPatrec){
        this.pkPatrec = pkPatrec;
    }

    public String getPkEmpApp(){
        return this.pkEmpApp;
    }
    public void setPkEmpApp(String pkEmpApp){
        this.pkEmpApp = pkEmpApp;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public Date getDateDeal(){
        return this.dateDeal;
    }
    public void setDateDeal(Date dateDeal){
        this.dateDeal = dateDeal;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public Date getDateArvhive(){
        return this.dateArvhive;
    }
    public void setDateArvhive(Date dateArvhive){
        this.dateArvhive = dateArvhive;
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

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}