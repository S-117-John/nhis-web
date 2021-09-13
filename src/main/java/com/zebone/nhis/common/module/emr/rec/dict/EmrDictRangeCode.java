package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_DICT_RANGE_CODE 
 *
 * @since 2016-10-11 05:06:51
 */
@Table(value="EMR_DICT_RANGE_CODE")
public class EmrDictRangeCode   {

	@PK
	@Field(value="PK_RANGE_CODE",id=KeyId.UUID)
    private String pkRangeCode;

	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    private String pkOrg;

	@Field(value="PK_RANGE")
    private String pkRange;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="PY_CODE")
    private String pyCode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="GB_CODE")
    private String gbCode;

	@Field(value="SORT_NUM")
    private Integer sortNum;

	@Field(value="DEL_FLAG")
    private String delFlag;

	@Field(value="REMARK")
    private String remark;

	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    private String creator;

	@Field(value="CREATE_TIME",date=FieldType.INSERT)
    private Date createTime;

	@Field(date=FieldType.ALL)
    private Date ts;

	private String status;

    public String getPkRangeCode(){
        return this.pkRangeCode;
    }
    public void setPkRangeCode(String pkRangeCode){
        this.pkRangeCode = pkRangeCode;
    }

    public String getPkOrg(){
        return this.pkOrg;
    }
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }

    public String getPkRange(){
        return this.pkRange;
    }
    public void setPkRange(String pkRange){
        this.pkRange = pkRange;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPyCode(){
        return this.pyCode;
    }
    public void setPyCode(String pyCode){
        this.pyCode = pyCode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getGbCode(){
        return this.gbCode;
    }
    public void setGbCode(String gbCode){
        this.gbCode = gbCode;
    }

    public Integer getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Integer sortNum){
        this.sortNum = sortNum;
    }

    public String getDelFlag(){
        return this.delFlag;
    }
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
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

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
}