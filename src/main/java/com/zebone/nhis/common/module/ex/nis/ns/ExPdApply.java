package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PD_APPLY - ex_pd_apply 
 *
 * @since 2016-10-12 04:29:29
 */
@Table(value="EX_PD_APPLY")
public class ExPdApply extends BaseModule  {

	@Field(value="PK_PDAP")
    private String pkPdap;

    /** EU_DIRECT - 1 请领，-1 请退 */
	@Field(value="EU_DIRECT")
    private String euDirect;

    /** EU_APTYPE - 0 病区领药，1 科室领药，2 医技用药 */
	@Field(value="EU_APTYPE")
    private String euAptype;

    /** CODE_APPLY - 可用于请领单条形码 */
	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="PK_DEPT_AP")
    private String pkDeptAp;

	@Field(value="PK_EMP_AP")
    private String pkEmpAp;

	@Field(value="NAME_EMP_AP")
    private String nameEmpAp;

	@Field(value="DATE_AP")
    private Date dateAp;

	@Field(value="PK_ORG_DE")
    private String pkOrgDe;

	@Field(value="PK_DEPT_DE")
    private String pkDeptDe;

	@Field(value="DATE_DE")
    private Date dateDe;

	@Field(value="FLAG_CANCEL")
    private String flagCancel;

	@Field(value="PK_DEPT_CANCEL")
    private String pkDeptCancel;

	@Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;

	@Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;

	@Field(value="DATE_CANCEL")
    private Date dateCancel;

	@Field(value="FLAG_FINISH")
    private String flagFinish;

    /** EU_STATUS - 0 申请，1 发放，9 取消 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="EU_PRINT")
	private String euPrint;
	
    public String getEuPrint() {
		return euPrint;
	}
	public void setEuPrint(String euPrint) {
		this.euPrint = euPrint;
	}
	public String getPkPdap(){
        return this.pkPdap;
    }
    public void setPkPdap(String pkPdap){
        this.pkPdap = pkPdap;
    }

    public String getEuDirect(){
        return this.euDirect;
    }
    public void setEuDirect(String euDirect){
        this.euDirect = euDirect;
    }

    public String getEuAptype(){
        return this.euAptype;
    }
    public void setEuAptype(String euAptype){
        this.euAptype = euAptype;
    }

    public String getCodeApply(){
        return this.codeApply;
    }
    public void setCodeApply(String codeApply){
        this.codeApply = codeApply;
    }

    public String getPkDeptAp(){
        return this.pkDeptAp;
    }
    public void setPkDeptAp(String pkDeptAp){
        this.pkDeptAp = pkDeptAp;
    }

    public String getPkEmpAp(){
        return this.pkEmpAp;
    }
    public void setPkEmpAp(String pkEmpAp){
        this.pkEmpAp = pkEmpAp;
    }

    public String getNameEmpAp(){
        return this.nameEmpAp;
    }
    public void setNameEmpAp(String nameEmpAp){
        this.nameEmpAp = nameEmpAp;
    }

    public Date getDateAp(){
        return this.dateAp;
    }
    public void setDateAp(Date dateAp){
        this.dateAp = dateAp;
    }

    public String getPkOrgDe(){
        return this.pkOrgDe;
    }
    public void setPkOrgDe(String pkOrgDe){
        this.pkOrgDe = pkOrgDe;
    }

    public String getPkDeptDe(){
        return this.pkDeptDe;
    }
    public void setPkDeptDe(String pkDeptDe){
        this.pkDeptDe = pkDeptDe;
    }

    public Date getDateDe(){
        return this.dateDe;
    }
    public void setDateDe(Date dateDe){
        this.dateDe = dateDe;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public String getPkDeptCancel(){
        return this.pkDeptCancel;
    }
    public void setPkDeptCancel(String pkDeptCancel){
        this.pkDeptCancel = pkDeptCancel;
    }

    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
    }

    public Date getDateCancel(){
        return this.dateCancel;
    }
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }

    public String getFlagFinish(){
        return this.flagFinish;
    }
    public void setFlagFinish(String flagFinish){
        this.flagFinish = flagFinish;
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