package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CP_TEMP_DIAG 
 *
 * @since 2016-12-12 02:43:26
 */
@Table(value="CP_TEMP_DIAG")
public class CpTempDiag extends BaseModule  {

	@PK
	@Field(value="PK_CPDIAG",id=KeyId.UUID)
    private String pkCpdiag;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkCpdiag(){
        return this.pkCpdiag;
    }
    public void setPkCpdiag(String pkCpdiag){
        this.pkCpdiag = pkCpdiag;
    }

    public String getPkCptemp(){
        return this.pkCptemp;
    }
    public void setPkCptemp(String pkCptemp){
        this.pkCptemp = pkCptemp;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
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
	/**
	 * 数据更新状态
	 */
	private String rowStatus;
	private String nameDiag;
	private String codeDiag;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getCodeDiag() {
		return codeDiag;
	}
	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}
	
}