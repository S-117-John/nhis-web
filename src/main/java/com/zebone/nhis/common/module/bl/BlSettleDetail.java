package com.zebone.nhis.common.module.bl;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BL_SETTLE_DETAIL  - 收费结算-结算明细 
 *
 * @since 2016-09-27 09:40:48
 */
@Table(value="BL_SETTLE_DETAIL")
public class BlSettleDetail   {

    /** PK_STDT - 明细主键 */
	@PK
	@Field(value="PK_STDT",id=KeyId.UUID)
    private String pkStdt;

    /** PK_SETTLE - 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;

    /** PK_PAYER - 付款方 */
	@Field(value="PK_PAYER")
    private String pkPayer;

    /**  PK_INSURANCE - 收费项目分类 */
	@Field(value="PK_INSURANCE")
    private String pkInsurance;

    /** AMOUNT - 金额 */
	@Field(value="AMOUNT")
    private Double amount;

    /** CREATOR - 创建人 */
	@Field(value="CREATOR",userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

    /** CREATE_TIME - 创建时间 */
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

    /** MODIFIER - 修改人 */
	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

    /** DEL_FLAG - 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** TS - 时间戳 */
	@Field(value="TS",date=FieldType.ALL)
    private Date ts;
	
	/** 收费项目分类名称 */
	private String temcateName;


    public String getPkStdt(){
        return this.pkStdt;
    }
    public void setPkStdt(String pkStdt){
        this.pkStdt = pkStdt;
    }

    public String getPkSettle(){
        return this.pkSettle;
    }
    public void setPkSettle(String pkSettle){
        this.pkSettle = pkSettle;
    }

    public String getPkPayer(){
        return this.pkPayer;
    }
    public void setPkPayer(String pkPayer){
        this.pkPayer = pkPayer;
    }

    public String getPkInsurance(){
        return this.pkInsurance;
    }
    public void setPkInsurance(String pkInsurance){
        this.pkInsurance = pkInsurance;
    }

    public Double getAmount(){
        return this.amount;
    }
    public void setAmount(Double amount){
        this.amount = amount;
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

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
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
    
	public String getTemcateName() {
		return temcateName;
	}
	
	public void setTemcateName(String temcateName) {
		this.temcateName = temcateName;
	}
}