package com.zebone.nhis.common.module.base.bd.wf;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_WF_ORD_ARGU_DEPT  - bd_wf_ord_argu_dept 
 *
 * @since 2016-08-30 01:10:43
 */
@Table(value="BD_WF_ORD_ARGU_DEPT")
public class BdWfOrdArguDept extends BaseModule  {

	@PK
	@Field(value="PK_WFARGUDEPT",id=KeyId.UUID)
    private String pkWfargudept;

	@Field(value="PK_WFARGU")
    private String pkWfargu;

	@Field(value="PK_ORG_EXEC")
    private String pkOrgExec;

	@Field(value="PK_DEPT")
    private String pkDept;

    /** FLAG_MAJ - 配置默认的执行科室 */
	@Field(value="FLAG_MAJ")
    private String flagMaj;


    public String getPkWfargudept(){
        return this.pkWfargudept;
    }
    public void setPkWfargudept(String pkWfargudept){
        this.pkWfargudept = pkWfargudept;
    }

    public String getPkWfargu(){
        return this.pkWfargu;
    }
    public void setPkWfargu(String pkWfargu){
        this.pkWfargu = pkWfargu;
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

    public String getFlagMaj(){
        return this.flagMaj;
    }
    public void setFlagMaj(String flagMaj){
        this.flagMaj = flagMaj;
    }
}