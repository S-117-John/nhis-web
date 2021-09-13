package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDCOMPANY - reg_szyj_pdcompany
深圳药监管理系统 
 *
 * @since 2019-12-30 03:47:17
 */
@Table(value="REG_SZYJ_PDCOMPANY")
public class RegSzyjPdcompany   {

	@PK
	@Field(value="PK_PDCOMP",id=KeyId.UUID)
    private String pkPdcomp;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

    /** QYLX - 1 为生产厂家，2 为供应商，3 为配送商，4 为 GPO */
	@Field(value="QYLX")
    private String qylx;

	@Field(value="QYBM")
    private String qybm;

	@Field(value="QYMC")
    private String qymc;

	@Field(value="SFJY")
    private String sfjy;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkPdcomp(){
        return this.pkPdcomp;
    }
    public void setPkPdcomp(String pkPdcomp){
        this.pkPdcomp = pkPdcomp;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getQylx(){
        return this.qylx;
    }
    public void setQylx(String qylx){
        this.qylx = qylx;
    }

    public String getQybm(){
        return this.qybm;
    }
    public void setQybm(String qybm){
        this.qybm = qybm;
    }

    public String getQymc(){
        return this.qymc;
    }
    public void setQymc(String qymc){
        this.qymc = qymc;
    }

    public String getSfjy(){
        return this.sfjy;
    }
    public void setSfjy(String sfjy){
        this.sfjy = sfjy;
    }

    public String getCreator(){
        return this.creator;
    }
    public void setCreator(String creator){
        this.creator = creator;
    }

    public Date getCreateTime(){
        return this.createTime;
    }
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}