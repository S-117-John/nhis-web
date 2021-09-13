package com.zebone.nhis.common.module.base.bd.srv;


import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TEMP_PRT_DEPT - 执行单使用科室 
 *
 * @since 2020-07-28 03:06:40
 */
@Table(value="BD_TEMP_PRT_DEPT")
public class BdTempPrtDept extends BaseModule  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -430784691592397470L;

	/** PK_TEMPPRTDEPT - 主键 */
	@PK
	@Field(value="PK_TEMPPRTDEPT",id=KeyId.UUID)
    private String pkTempprtdept;

    /** PK_TEMPPRT - 关联打印模板 */
	@Field(value="PK_TEMPPRT")
    private String pkTempprt;

    /** DT_ORDEXTYPE - 病区医嘱执行单类型 */
	@Field(value="DT_ORDEXTYPE")
    private String dtOrdextype;

    /** PK_DEPT - 使用科室 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** MODIFY_TIME - 修改时间 */
	@Field(value="MODIFY_TIME")
    private Date modifyTime;


    public String getPkTempprtdept(){
        return this.pkTempprtdept;
    }
    public void setPkTempprtdept(String pkTempprtdept){
        this.pkTempprtdept = pkTempprtdept;
    }

    public String getPkTempprt(){
        return this.pkTempprt;
    }
    public void setPkTempprt(String pkTempprt){
        this.pkTempprt = pkTempprt;
    }

    public String getDtOrdextype(){
        return this.dtOrdextype;
    }
    public void setDtOrdextype(String dtOrdextype){
        this.dtOrdextype = dtOrdextype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getModifyTime(){
        return this.modifyTime;
    }
    public void setModifyTime(Date modifyTime){
        this.modifyTime = modifyTime;
    }
}