package com.zebone.nhis.common.module.emr.rec.rec;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_Follow 
 *
 * @since 2020-06-10 09:56:56
 */
@Table(value="EMR_Follow")
public class EmrFollow{

	
	/**
     * 主键
     */
	@Field(value="Pk_Rec")
    private String pkRec;

	
	/**
     * pkpv
     */
	@Field(value="Pk_PV")
    private String pkPv;

	
	/**
     * 记录时间
     */
	@Field(value="Rec_Date")
    private Date recDate;

	
	/**
     * 表单名称
     */
	@Field(value="Pk_Template")
    private String pkTemplate;

	@Field(value="Type_Code")
	private String typeCode;

	@Field(value="Pk_Report_Rec")
	private String pkReportRec;
	/**
     * 
     */
	@Field(value="Category")
    private String category;

	
	/**
     * 
     */
	@Field(value="Item")
    private String item;

	
	/**
     * 键
     */
	@Field(value="Code")
    private String code;

	
	/**
     * 值
     */
	@Field(value="Value")
    private String value;

	
	/**
     * 备注
     */
	@Field(value="Remark")
    private String remark;

	/**
     * 创建人
     */
	@Field(value="Creator")
    public String creator;

	/**
     * 创建时间
     */
	@Field(value="createTime")
	public Date createTime;
    

	/**
     * 时间戳
     */
	@Field(value="Ts")
	public Date ts;

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getPkTemplate() {
		return pkTemplate;
	}

	public void setPkTemplate(String pkTemplate) {
		this.pkTemplate = pkTemplate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getPkReportRec() {
		return pkReportRec;
	}

	public void setPkReportRec(String pkReportRec) {
		this.pkReportRec = pkReportRec;
	}

	public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Date getRecDate(){
        return this.recDate;
    }
    public void setRecDate(Date recDate){
        this.recDate = recDate;
    }

	public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }

    public String getItem(){
        return this.item;
    }
    public void setItem(String item){
        this.item = item;
    }

	public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
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
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}

}