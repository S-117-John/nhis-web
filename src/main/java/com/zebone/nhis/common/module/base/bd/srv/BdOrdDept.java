package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_ORD_DEPT  - bd_ord_dept 
 *
 * @since 2016-09-08 03:20:45
 */
@Table(value="BD_ORD_DEPT")
public class BdOrdDept extends BaseModule  {

	@PK
	@Field(value="PK_ORDDEPT",id=KeyId.UUID)
    private String pkOrddept;

	@Field(value="PK_ORDORG")
    private String pkOrdorg;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="PK_ORG_EXEC")
    private String pkOrgExec;

    /** PK_DEPT - 没有执行科室定义代表开立科室病区执行 */
	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="FLAG_DEF")
    private String flagDef;

	@Field(value = "PLACE_EXEC")
	private String placeExec;

    public String getPlaceExec() {
        return placeExec;
    }

    public void setPlaceExec(String placeExec) {
        this.placeExec = placeExec;
    }

    public String getPkOrddept(){
        return this.pkOrddept;
    }
    public void setPkOrddept(String pkOrddept){
        this.pkOrddept = pkOrddept;
    }

    public String getPkOrdorg(){
        return this.pkOrdorg;
    }
    public void setPkOrdorg(String pkOrdorg){
        this.pkOrdorg = pkOrdorg;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
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

    public String getFlagDef(){
        return this.flagDef;
    }
    public void setFlagDef(String flagDef){
        this.flagDef = flagDef;
    }

}