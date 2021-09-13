package com.zebone.nhis.common.module.emr.oth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.pv.PvDiag;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_DIAG - cn_diag 
 *
 * @since 2016-11-08 10:40:19
 */
@Table(value="CN_DIAG")
public class CnDiag extends BaseModule  {

	@PK
	@Field(value="PK_CNDIAG",id=KeyId.UUID)
    private String pkCndiag;

    /** EU_PVTYPE - 1门诊，2急诊，3住院，4体检，5家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="DESC_DIAGS")
    private String descDiags;

	@Field(value="DATE_DIAG")
    private Date dateDiag;

	@Field(value="PK_EMP_DIAG")
    private String pkEmpDiag;

	@Field(value="NAME_EMP_DIAG")
    private String nameEmpDiag;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_CNNET")
    private String pkCnnet;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCndiag(){
        return this.pkCndiag;
    }
    public void setPkCndiag(String pkCndiag){
        this.pkCndiag = pkCndiag;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getDescDiags(){
        return this.descDiags;
    }
    public void setDescDiags(String descDiags){
        this.descDiags = descDiags;
    }

    public Date getDateDiag(){
        return this.dateDiag;
    }
    public void setDateDiag(Date dateDiag){
        this.dateDiag = dateDiag;
    }

    public String getPkEmpDiag(){
        return this.pkEmpDiag;
    }
    public void setPkEmpDiag(String pkEmpDiag){
        this.pkEmpDiag = pkEmpDiag;
    }

    public String getNameEmpDiag(){
        return this.nameEmpDiag;
    }
    public void setNameEmpDiag(String nameEmpDiag){
        this.nameEmpDiag = nameEmpDiag;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkCnnet(){
        return this.pkCnnet;
    }
    public void setPkCnnet(String pkCnnet){
        this.pkCnnet = pkCnnet;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    private List<PvDiag> pVDiagList = new ArrayList<PvDiag>();

	public List<PvDiag> getpVDiagList() {
		return pVDiagList;
	}
	public void setpVDiagList(List<PvDiag> pVDiagList) {
		this.pVDiagList = pVDiagList;
	}
    
}