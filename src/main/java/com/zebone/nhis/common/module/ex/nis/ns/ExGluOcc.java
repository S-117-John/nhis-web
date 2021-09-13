package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;
import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_GLU_OCC 
 *
 * @since 2017-9-16 10:49:42
 */
@Table(value="EX_GLU_OCC")
public class ExGluOcc extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_GLUOCC",id=KeyId.UUID)
    private String pkGluocc;

	@Field(value="PK_ORG")
	private String pkOrg;
	
	@Override
	public String getPkOrg() {
		return pkOrg;
	}
	@Override
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_EXOCC")
    private String pkExocc;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;

	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="DT_GLUPOINT")
    private String dtGlupoint;

	@Field(value="DT_GLUTYPE")
    private String dtGlutype;

	@Field(value="VAL")
    private double val;

	@Field(value="EU_REASON")
    private String euReason;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkGluocc(){
        return this.pkGluocc;
    }
    public void setPkGluocc(String pkGluocc){
        this.pkGluocc = pkGluocc;
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

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getPkOrgOcc(){
        return this.pkOrgOcc;
    }
    public void setPkOrgOcc(String pkOrgOcc){
        this.pkOrgOcc = pkOrgOcc;
    }

    public String getPkDeptOcc(){
        return this.pkDeptOcc;
    }
    public void setPkDeptOcc(String pkDeptOcc){
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getPkEmpOcc(){
        return this.pkEmpOcc;
    }
    public void setPkEmpOcc(String pkEmpOcc){
        this.pkEmpOcc = pkEmpOcc;
    }

    public String getNameEmpOcc(){
        return this.nameEmpOcc;
    }
    public void setNameEmpOcc(String nameEmpOcc){
        this.nameEmpOcc = nameEmpOcc;
    }

    public Date getDateOcc(){
        return this.dateOcc;
    }
    public void setDateOcc(Date dateOcc){
        this.dateOcc = dateOcc;
    }

    public String getDtGlupoint(){
        return this.dtGlupoint;
    }
    public void setDtGlupoint(String dtGlupoint){
        this.dtGlupoint = dtGlupoint;
    }

    public String getDtGlutype(){
        return this.dtGlutype;
    }
    public void setDtGlutype(String dtGlutype){
        this.dtGlutype = dtGlutype;
    }

    public double getVal(){
        return this.val;
    }
    public void setVal(double val){
        this.val = val;
    }

    public String getEuReason(){
        return this.euReason;
    }
    public void setEuReason(String euReason){
        this.euReason = euReason;
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