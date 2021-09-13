package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: BD_ORD_LAB_COL 
 *
 * @since 2019-08-21 09:09:46
 */
@Table(value="BD_ORD_LAB_COL")
public class BdOrdLabCol extends BaseModule  {

	@PK
	@Field(value="PK_ORDLABCOL",id=KeyId.UUID)
    private String pkOrdlabcol;

	@Field(value="PK_ORGAREA")
    private String pkOrgarea;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="PK_DEPT_COL")
    private String pkDeptCol;

	@Field(value="COLLPLACE")
    private String collplace;

	@Field(value="FLAG_DEF")
    private String flagDef;

    @Field(value="eu_pvtype")
    private String euPvtype;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

    @Field(value="dt_building")
    private String dtBuilding;

    public String getPkOrdlabcol(){
        return this.pkOrdlabcol;
    }
    public void setPkOrdlabcol(String pkOrdlabcol){
        this.pkOrdlabcol = pkOrdlabcol;
    }

    public String getPkOrgarea(){
        return this.pkOrgarea;
    }
    public void setPkOrgarea(String pkOrgarea){
        this.pkOrgarea = pkOrgarea;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getPkDeptCol(){
        return this.pkDeptCol;
    }
    public void setPkDeptCol(String pkDeptCol){
        this.pkDeptCol = pkDeptCol;
    }

    public String getCollplace(){
        return this.collplace;
    }
    public void setCollplace(String collplace){
        this.collplace = collplace;
    }

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
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

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }
	public String getDtBuilding() {
		return dtBuilding;
	}
	public void setDtBuilding(String dtBuilding) {
		this.dtBuilding = dtBuilding;
	}

}