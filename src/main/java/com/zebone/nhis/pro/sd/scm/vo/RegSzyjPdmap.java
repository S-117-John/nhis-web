package com.zebone.nhis.pro.sd.scm.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: REG_SZYJ_PDMAP - reg_szyj_pdmap 
 *
 * @since 2019-12-31 10:29:16
 */
@Table(value="REG_SZYJ_PDMAP")
public class RegSzyjPdmap   {

	@PK
	@Field(value="PK_REGPDMAP",id=KeyId.UUID)
    private String pkRegpdmap;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_PD")
    private String pkPd;

    /** CODE - bd_pd.code */
	@Field(value="CODE")
    private String code;

    /** NAME_GEN - bd_pd.name_gen */
	@Field(value="NAME_GEN")
    private String nameGen;

    /** YPBM - 市药品交易监管系统发布的对药品的统编代码 */
	@Field(value="YPBM")
    private String ypbm;

	@Field(value="TYM")
    private String tym;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkRegpdmap(){
        return this.pkRegpdmap;
    }
    public void setPkRegpdmap(String pkRegpdmap){
        this.pkRegpdmap = pkRegpdmap;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkPd(){
        return this.pkPd;
    }
    public void setPkPd(String pkPd){
        this.pkPd = pkPd;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getNameGen(){
        return this.nameGen;
    }
    public void setNameGen(String nameGen){
        this.nameGen = nameGen;
    }

    public String getYpbm(){
        return this.ypbm;
    }
    public void setYpbm(String ypbm){
        this.ypbm = ypbm;
    }

    public String getTym(){
        return this.tym;
    }
    public void setTym(String tym){
        this.tym = tym;
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