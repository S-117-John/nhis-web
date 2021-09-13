package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_COMM - bd_ord_comm
多用于个人常用检验、检查项目 
 *
 * @since 2016-10-31 04:33:22
 */
@Table(value="BD_ORD_COMM")
public class BdOrdComm extends BaseModule  {

	@PK
	@Field(value="PK_ORDCOMM",id=KeyId.UUID)
    private String pkOrdcomm;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="PK_ORD")
    private String pkOrd;
	
	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_DEPT")
    private String pkDept;

    @Field(value="TIMES")
    private Long times;

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public String getPkOrdcomm(){
        return this.pkOrdcomm;
    }
    public void setPkOrdcomm(String pkOrdcomm){
        this.pkOrdcomm = pkOrdcomm;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
    
}