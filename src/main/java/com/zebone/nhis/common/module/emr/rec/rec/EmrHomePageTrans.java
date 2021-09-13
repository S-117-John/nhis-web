package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

/**
 *
 * @author 
 */
@Table(value = "EMR_HOME_PAGE_TRANS")
public class EmrHomePageTrans{
    /**
     * 
     */
    @PK
    @Field(value = "PK_TRANS", id = Field.KeyId.UUID)
    private String pkTrans;
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
    @Field(value = "PK_DEPT")
    private String pkDept;
    /**
     * 
     */
    @Field(value = "DEPT_CODE")
    private String deptCode;
    /**
     * 
     */
    @Field(value = "DEPT_NAME")
    private String deptName;
    /**
     * 
     */
    @Field(value = "TRANS_DATE")
    private Date transDate;
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

    private String status;

    /**
     *
     */
    @Field(value = "FLAG_ICU")
    private int flagIcu;

    /**
     *
     */
    @Field(value = "CODE_ICUTYPE")
    private String codeIcutype;

    /**
     *
     */
    @Field(value = "NAME_ICUTYPE")
    private String nameIcutype;

    /**
     *
     */
    @Field(value = "DATE_TRANSOUT")
    private Date dateTransout;
    
    /**
     * 
     */
    public String getPkTrans(){
        return this.pkTrans;
    }

    /**
     * 
     */
    public void setPkTrans(String pkTrans){
        this.pkTrans = pkTrans;
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
    public String getPkDept(){
        return this.pkDept;
    }

    /**
     * 
     */
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }    
    /**
     * 
     */
    public String getDeptCode(){
        return this.deptCode;
    }

    /**
     * 
     */
    public void setDeptCode(String deptCode){
        this.deptCode = deptCode;
    }    
    /**
     * 
     */
    public String getDeptName(){
        return this.deptName;
    }

    /**
     * 
     */
    public void setDeptName(String deptName){
        this.deptName = deptName;
    }    
    /**
     * 
     */
    public Date getTransDate(){
        return this.transDate;
    }

    /**
     * 
     */
    public void setTransDate(Date transDate){
        this.transDate = transDate;
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
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public int getFlagIcu() {
        return flagIcu;
    }

    public void setFlagIcu(int flagIcu) {
        this.flagIcu = flagIcu;
    }

    public String getCodeIcutype() {
        return codeIcutype;
    }

    public void setCodeIcutype(String codeIcutype) {
        this.codeIcutype = codeIcutype;
    }

    public String getNameIcutype() {
        return nameIcutype;
    }

    public void setNameIcutype(String nameIcutype) {
        this.nameIcutype = nameIcutype;
    }

    public Date getDateTransout() {
        return dateTransout;
    }

    public void setDateTransout(Date dateTransout) {
        this.dateTransout = dateTransout;
    }
}