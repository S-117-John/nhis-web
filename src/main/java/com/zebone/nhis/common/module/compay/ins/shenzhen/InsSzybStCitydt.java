package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: INS_SZYB_ST_CITYDT - ins_szyb_st_citydt 
 *
 * @since 2020-03-05 05:05:34
 */
@Table(value="INS_SZYB_ST_CITYDT")
public class InsSzybStCitydt   {

    /** PK_INSSTCITYDT - 主键 */
	@PK
	@Field(value="PK_INSSTCITYDT",id=KeyId.UUID)
    private String pkInsstcitydt;

    /** PK_INSSTCITY - 主键 */
	@Field(value="PK_INSSTCITY")
    private String pkInsstcity;

    /** TYPE_OUTPUT - 大类代码 AKA111，支付项目代码 AAA036，个人医保累计信息类别 AKA037 */
	@Field(value="TYPE_OUTPUT")
    private String typeOutput;

	@Field(value="CATEGORY")
    private String category;

	@Field(value="AMT_FEE")
    private Double amtFee;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(date=FieldType.ALL)
    private Date ts;


    public String getPkInsstcitydt(){
        return this.pkInsstcitydt;
    }
    public void setPkInsstcitydt(String pkInsstcitydt){
        this.pkInsstcitydt = pkInsstcitydt;
    }

    public String getPkInsstcity(){
        return this.pkInsstcity;
    }
    public void setPkInsstcity(String pkInsstcity){
        this.pkInsstcity = pkInsstcity;
    }

    public String getTypeOutput(){
        return this.typeOutput;
    }
    public void setTypeOutput(String typeOutput){
        this.typeOutput = typeOutput;
    }

    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public Double getAmtFee(){
        return this.amtFee;
    }
    public void setAmtFee(Double amtFee){
        this.amtFee = amtFee;
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

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
}