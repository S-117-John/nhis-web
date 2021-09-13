package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_CATEORD_DT  - bd_cp_cateord_dt 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="BD_CP_CATEORD_DT")
public class BdCpCateordDt extends BaseModule  {

	@PK
	@Field(value="PK_CATEORDDT",id=KeyId.UUID)
    private String pkCateorddt;

	@Field(value="PK_CATEORD")
    private String pkCateord;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	private String nameord;
	private String codeord;

    public String getPkCateorddt(){
        return this.pkCateorddt;
    }
    public void setPkCateorddt(String pkCateorddt){
        this.pkCateorddt = pkCateorddt;
    }

    public String getPkCateord(){
        return this.pkCateord;
    }
    public void setPkCateord(String pkCateord){
        this.pkCateord = pkCateord;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
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
    
    
	private String rowStatus;
	private String spec;

	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getNameord() {
		return nameord;
	}
	public void setNameord(String nameord) {
		this.nameord = nameord;
	}
	public String getCodeord() {
		return codeord;
	}
	public void setCodeord(String codeord) {
		this.codeord = codeord;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
}