package com.zebone.nhis.common.module.base.bd.wf;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WF_ORD_ARGU  - bd_wf_ord_argu 
 *
 * @since 2016-08-30 01:10:38
 */
@Table(value="BD_WF_ORD_ARGU")
public class BdWfOrdArgu   {

	@PK
	@Field(value="PK_WFARGU",id=KeyId.UUID)
    private String pkWfargu;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="PK_WF")
    private String pkWf;

	@Field(value="PK_ORG_EXEC")
    private String pkOrgExec;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="TIME_BEGIN")
    private String timeBegin;

	@Field(value="TIME_END")
    private String timeEnd;

    /** WEEKNOS - 周日用7表示. 用分号隔离: 例如 1;2;3;4;5 */
	@Field(value="WEEKNOS")
    private String weeknos;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	@Field(value="ORDER_TYPE")
    private String orderType;

	@Field(value="ORDRECUR")
    private String ordrecur;

	@Field(value="PK_SUPPLYCATE")
    private String pkSupplycate;


    public String getPkWfargu(){
        return this.pkWfargu;
    }
    public void setPkWfargu(String pkWfargu){
        this.pkWfargu = pkWfargu;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkWf(){
        return this.pkWf;
    }
    public void setPkWf(String pkWf){
        this.pkWf = pkWf;
    }

    public String getPkOrgExec(){
        return this.pkOrgExec;
    }
    public void setPkOrgExec(String pkOrgExec){
        this.pkOrgExec = pkOrgExec;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getTimeBegin(){
        return this.timeBegin;
    }
    public void setTimeBegin(String timeBegin){
        this.timeBegin = timeBegin;
    }

    public String getTimeEnd(){
        return this.timeEnd;
    }
    public void setTimeEnd(String timeEnd){
        this.timeEnd = timeEnd;
    }

    public String getWeeknos(){
        return this.weeknos;
    }
    public void setWeeknos(String weeknos){
        this.weeknos = weeknos;
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

    public String getOrderType(){
        return this.orderType;
    }
    public void setOrderType(String orderType){
        this.orderType = orderType;
    }

    public String getOrdrecur(){
        return this.ordrecur;
    }
    public void setOrdrecur(String ordrecur){
        this.ordrecur = ordrecur;
    }
	public String getPkSupplycate() {
		return pkSupplycate;
	}
	public void setPkSupplycate(String pkSupplycate) {
		this.pkSupplycate = pkSupplycate;
	}

    
}