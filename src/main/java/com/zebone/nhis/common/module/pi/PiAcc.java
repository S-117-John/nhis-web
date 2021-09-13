package com.zebone.nhis.common.module.pi;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PI_ACC  - 患者信息-账户 
 *
 * @since 2016-09-26 08:37:19
 */
@Table(value="PI_ACC")
public class PiAcc   {

    /** PK_PIACC - 账户主键 */
	@PK
	@Field(value="PK_PIACC",id=KeyId.UUID)
    private String pkPiacc;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** CODE_ACC - 账户编码 */
	@Field(value="CODE_ACC")
    private String codeAcc;

    /** AMT_ACC - 账户余额 */
	@Field(value="AMT_ACC")
    private BigDecimal amtAcc;

    /** CREDIT_ACC - 信用额度 */
	@Field(value="CREDIT_ACC")
    private BigDecimal creditAcc;

    /** EU_STATUS - 账户状态:1有效 2冻结9作废 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;

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


    public String getPkPiacc(){
        return this.pkPiacc;
    }
    public void setPkPiacc(String pkPiacc){
        this.pkPiacc = pkPiacc;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getCodeAcc(){
        return this.codeAcc;
    }
    public void setCodeAcc(String codeAcc){
        this.codeAcc = codeAcc;
    }

    public BigDecimal getAmtAcc() {
		return amtAcc;
	}
	public void setAmtAcc(BigDecimal amtAcc) {
		this.amtAcc = amtAcc;
	}
	public BigDecimal getCreditAcc() {
		return creditAcc;
	}
	public void setCreditAcc(BigDecimal creditAcc) {
		this.creditAcc = creditAcc;
	}
	public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
}