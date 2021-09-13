package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TERM_DIAG_DEPT 
 *
 * @since 2018-12-30 02:07:11
 */
@Table(value="BD_TERM_DIAG_DEPT")
public class BdTermDiagDeptVo   {

	@PK
	@Field(value="PK_DIAGDEPT",id=KeyId.UUID)
    private String pkDiagdept;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="SORTNO")
    private Long sortno;

	@Field(value="NAME_DIAG")
    private String nameDiag;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="CREATER")
    private String creater;

	@Field(value="CREATE_TIME")
    private Date createTime;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="TS")
    private Date ts;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;
	
	private String dtCndiagtype;

	
    public String getPkDiagdept(){
        return this.pkDiagdept;
    }
    public void setPkDiagdept(String pkDiagdept){
        this.pkDiagdept = pkDiagdept;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Long getSortno(){
        return this.sortno;
    }
    public void setSortno(Long sortno){
        this.sortno = sortno+1;
    }

    public String getNameDiag(){
        return this.nameDiag;
    }
    public void setNameDiag(String nameDiag){
        this.nameDiag = nameDiag;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getCreater(){
        return this.creater;
    }
    public void setCreater(String creater){
        this.creater = creater;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
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

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }
	public String getDtCndiagtype() {
		return dtCndiagtype;
	}
	public void setDtCndiagtype(String dtCndiagtype) {
		this.dtCndiagtype = dtCndiagtype;
	}
    
    
    
}