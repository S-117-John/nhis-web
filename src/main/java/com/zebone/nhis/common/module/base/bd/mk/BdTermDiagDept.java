package com.zebone.nhis.common.module.base.bd.mk;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * 
 * @author xujian
 * @date 2017-07-31 03:22:58
 */
@Table(value="BD_TERM_DIAG_DEPT")
public class BdTermDiagDept   {

	@PK
	@Field(value="PK_DIAGDEPT",id=KeyId.UUID)
    private String pkDiagdept;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="SORTNO")
    private BigDecimal sortno;

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

    public BigDecimal getSortno(){
        return this.sortno;
    }
    public void setSortno(BigDecimal sortno){
        this.sortno = sortno;
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
}