package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_BR")
public class EmrHomePageBr{
    /**
     * 
     */
    @PK
    @Field(value = "PK_BR", id = Field.KeyId.UUID)
    private String pkBr;
    /**
     * 
     */
    @Field(value = "PK_ORG")
    private String pkOrg;
    /**
     * 
     */
    @Field(value = "PK_PAGE")
    private String pkPage;
    /**
     * 
     */
    @Field(value = "SEQ_NO")
    private Integer seqNo;
    /**
     * 
     */
    @Field(value = "DT_SEX")
    private String dtSex;
    /**
     * 01-死产 02-活产 03-死胎  
     */
    @Field(value = "CODE_CB")
    private String codeCb;
    /**
     * 
     */
    @Field(value = "NAME_CB")
    private String nameCb;
    /**
     * 
     */
    @Field(value = "WEIGHT")
    private BigDecimal weight;
    /**
     * 01-死亡 02-转科 03-出院  
     */
    @Field(value = "CODE_OC")
    private String codeOc;
    /**
     * 
     */
    @Field(value = "NAME_OC")
    private String nameOc;
    /**
     * 01自然
02Ⅰ度窒息
03Ⅱ度窒息

     */
    @Field(value = "CODE_BREATH")
    private String codeBreath;
    /**
     * 
     */
    @Field(value = "NAME_BREATH")
    private String nameBreath;
    /**
     * 
     */
    @Field(value = "NUM_RES")
    private BigDecimal numRes;
    /**
     * 
     */
    @Field(value = "NUM_SUCC")
    private BigDecimal numSucc;
    /**
     * 
     */
    @Field(value = "DEL_FLAG")
    private String delFlag;
    /**
     * 
     */
    @Field(value = "REMARK")
    private String remark;
    /**
     * 
     */
    @Field(userfield = "pkEmp", userfieldscop = Field.FieldType.INSERT)
    private String creator;
    /**
     * 
     */
    @Field(value = "create_time", date = Field.FieldType.INSERT)
    private Date createTime;
    /**
     * 
     */
    @Field(value = "TS")
    private Date ts;
    
    
    private String Status;

    /**
     * 
     */
    public String getPkBr(){
        return this.pkBr;
    }

    /**
     * 
     */
    public void setPkBr(String pkBr){
        this.pkBr = pkBr;
    }    
    /**
     * 
     */
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    /**
     * 
     */
    public String getPkPage(){
        return this.pkPage;
    }

    /**
     * 
     */
    public void setPkPage(String pkPage){
        this.pkPage = pkPage;
    }    
    /**
     * 
     */
    public Integer getSeqNo(){
        return this.seqNo;
    }

    /**
     * 
     */
    public void setSeqNo(Integer seqNo){
        this.seqNo = seqNo;
    }    
    /**
     * 
     */
    public String getDtSex(){
        return this.dtSex;
    }

    /**
     * 
     */
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }    
    /**
     * 01-死产 02-活产 03-死胎  
     */
    public String getCodeCb(){
        return this.codeCb;
    }

    /**
     * 01-死产 02-活产 03-死胎  
     */
    public void setCodeCb(String codeCb){
        this.codeCb = codeCb;
    }    
    /**
     * 
     */
    public String getNameCb(){
        return this.nameCb;
    }

    /**
     * 
     */
    public void setNameCb(String nameCb){
        this.nameCb = nameCb;
    }    
    /**
     * 
     */
    public BigDecimal getWeight(){
        return this.weight;
    }

    /**
     * 
     */
    public void setWeight(BigDecimal weight){
        this.weight = weight;
    }    
    /**
     * 01-死亡 02-转科 03-出院  
     */
    public String getCodeOc(){
        return this.codeOc;
    }

    /**
     * 01-死亡 02-转科 03-出院  
     */
    public void setCodeOc(String codeOc){
        this.codeOc = codeOc;
    }    
    /**
     * 
     */
    public String getNameOc(){
        return this.nameOc;
    }

    /**
     * 
     */
    public void setNameOc(String nameOc){
        this.nameOc = nameOc;
    }    
    /**
     * 01自然
02Ⅰ度窒息
03Ⅱ度窒息

     */
    public String getCodeBreath(){
        return this.codeBreath;
    }

    /**
     * 01自然
02Ⅰ度窒息
03Ⅱ度窒息

     */
    public void setCodeBreath(String codeBreath){
        this.codeBreath = codeBreath;
    }    
    /**
     * 
     */
    public String getNameBreath(){
        return this.nameBreath;
    }

    /**
     * 
     */
    public void setNameBreath(String nameBreath){
        this.nameBreath = nameBreath;
    }    
    /**
     * 
     */
    public BigDecimal getNumRes(){
        return this.numRes;
    }

    /**
     * 
     */
    public void setNumRes(BigDecimal numRes){
        this.numRes = numRes;
    }    
    /**
     * 
     */
    public BigDecimal getNumSucc(){
        return this.numSucc;
    }

    /**
     * 
     */
    public void setNumSucc(BigDecimal numSucc){
        this.numSucc = numSucc;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
    
}