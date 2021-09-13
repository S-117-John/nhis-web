package com.zebone.nhis.common.module.ma.dp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: DP_INV_RANGE - DP_INV_RANGE 
 *
 * @since 2016-11-09 01:36:26
 */
@Table(value="DP_INV_RANGE")
public class DpInvRange extends BaseModule  {

	@Field(value="PK_INVRANGE",id=KeyId.UUID)
    private String pkInvrange;

	@Field(value="PK_DPINV")
    private String pkDpinv;

    /** PK_TARGET - 患者主键pk_pi或员工主键pk_emp */
	@Field(value="PK_TARGET")
    private String pkTarget;

	@Field(value="MODITY_TIME")
    private Date modityTime;


    public String getPkInvrange(){
        return this.pkInvrange;
    }
    public void setPkInvrange(String pkInvrange){
        this.pkInvrange = pkInvrange;
    }

    public String getPkDpinv(){
        return this.pkDpinv;
    }
    public void setPkDpinv(String pkDpinv){
        this.pkDpinv = pkDpinv;
    }

    public String getPkTarget(){
        return this.pkTarget;
    }
    public void setPkTarget(String pkTarget){
        this.pkTarget = pkTarget;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}