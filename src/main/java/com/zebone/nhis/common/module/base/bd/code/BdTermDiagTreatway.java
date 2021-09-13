package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_TERM_DIAG_TREATWAY 
 *
 * @since 2017-12-27 05:12:08
 */
@Table(value="BD_TERM_DIAG_TREATWAY")
public class BdTermDiagTreatway {

	@PK
	@Field(value="PK_DIAGTREATWAY",id=KeyId.UUID)
   private String pkDiagtreatway;

	@Field(value="CODE_DIAG")
   private String codeDiag;

	@Field(value="NAME_DIAG")
   private String nameDiag;

	@Field(value="DT_TREATWAY")
   private String dtTreatway;

	@Field(value="VAL")
   private Double val;

	@Field(value="NOTE")
   private String note;

	@Field(value="MODIFIER")
   private String modifier;

	@Field(value="MODITY_TIME")
   private Date modityTime;
	
	@Field(value="CREATOR")
	private String creator;

	@Field(value="CREATE_TIME")
	private Date createTime;
	
	@Field(value="PK_ORG")
    private String pkOrg;
	
	 /** DEL_FLAG - 0:未删除 1:已删除 */
	@Field(value="DEL_FLAG")
	private String delFlag;

	@Field(date=FieldType.ALL)
	private Date ts;
	
	@Field(value="DT_HPPSNTYPE")
	private String dtHppsntype;		///--人员类别
	
	@Field(value="EU_TYPE")
	private String euType;         //--病种类型
	
	@Field(value="CODE_OP")
	private String codeOp;         //--诊治编码
	
	@Field(value="NAME_OP")
	private String nameOp;         //--诊治名称
	
	@Field(value="RATE")
	private String rate;            //--费率
	
	@Field(value="AMOUNT")
	private String amount;          //--预测费用
	
	public String getPkDiagtreatway(){
		return this.pkDiagtreatway;
	}
	public void setPkDiagtreatway(String pkDiagtreatway){
		this.pkDiagtreatway = pkDiagtreatway;
	}

    public String getCodeDiag(){
        return this.codeDiag;
    }
    public void setCodeDiag(String codeDiag){
        this.codeDiag = codeDiag;
    }

    public String getNameDiag(){
        return this.nameDiag;
    }
    public void setNameDiag(String nameDiag){
        this.nameDiag = nameDiag;
    }

    public String getDtTreatway(){
        return this.dtTreatway;
    }
    public void setDtTreatway(String dtTreatway){
        this.dtTreatway = dtTreatway;
    }

    public Double getVal(){
        return this.val;
    }
    public void setVal(Double val){
        this.val = val;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getDtHppsntype() {
		return dtHppsntype;
	}
	public void setDtHppsntype(String dtHppsntype) {
		this.dtHppsntype = dtHppsntype;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getNameOp() {
		return nameOp;
	}
	public void setNameOp(String nameOp) {
		this.nameOp = nameOp;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
  
}
