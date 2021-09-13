package com.zebone.nhis.common.module.base.bd.price;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_DAYCG_SET_SRV  - bd_daycg_set_srv 
 *
 * @since 2016-08-26 02:55:37
 */
@Table(value="BD_DAYCG_SET_SRV")
public class BdDaycgSetSrv extends BaseModule  {

	@PK
	@Field(value="PK_DAYCGSETSRV",id=KeyId.UUID)
    private String pkDaycgsetsrv;

	@Field(value="PK_DAYCGSET")
    private String pkDaycgset;

	@Field(value="PK_ITEM")
    private String pkItem;

	@Field(value="MONTH_BEGIN")
    private BigDecimal monthBegin;

	@Field(value="MONTH_END")
    private BigDecimal monthEnd;

	@Field(value="DAY_END")
    private BigDecimal dayEnd;

	@Field(value="DAY_BEGIN")
    private BigDecimal dayBegin;

	@Field(value="DESCRIBE")
    private String describe;

    /** EU_CGMODE - 0按人收费1按床收费 */
	@Field(value="EU_CGMODE")
    private String euCgmode;


    public String getPkDaycgsetsrv(){
        return this.pkDaycgsetsrv;
    }
    public void setPkDaycgsetsrv(String pkDaycgsetsrv){
        this.pkDaycgsetsrv = pkDaycgsetsrv;
    }

    public String getPkDaycgset(){
        return this.pkDaycgset;
    }
    public void setPkDaycgset(String pkDaycgset){
        this.pkDaycgset = pkDaycgset;
    }

    public String getPkItem(){
        return this.pkItem;
    }
    public void setPkItem(String pkItem){
        this.pkItem = pkItem;
    }

    public BigDecimal getMonthBegin(){
        return this.monthBegin;
    }
    public void setMonthBegin(BigDecimal monthBegin){
        this.monthBegin = monthBegin;
    }

    public BigDecimal getMonthEnd(){
        return this.monthEnd;
    }
    public void setMonthEnd(BigDecimal monthEnd){
        this.monthEnd = monthEnd;
    }

    public BigDecimal getDayEnd(){
        return this.dayEnd;
    }
    public void setDayEnd(BigDecimal dayEnd){
        this.dayEnd = dayEnd;
    }

    public BigDecimal getDayBegin(){
        return this.dayBegin;
    }
    public void setDayBegin(BigDecimal dayBegin){
        this.dayBegin = dayBegin;
    }

    public String getDescribe(){
        return this.describe;
    }
    public void setDescribe(String describe){
        this.describe = describe;
    }

    public String getEuCgmode(){
        return this.euCgmode;
    }
    public void setEuCgmode(String euCgmode){
        this.euCgmode = euCgmode;
    }
}