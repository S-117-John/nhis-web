package com.zebone.nhis.common.module.emr.rec.dict;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_PARAMETER 
 *
 * @since 2016-10-09 02:08:01
 */
@Table(value="EMR_PARAMETER")
public class EmrParameter extends BaseModule  {

	@PK
	@Field(value="PK_PARAM",id=KeyId.UUID)
    private String pkParam;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;

	@Field(value="VALUE")
    private String value;

	@Field(value="SHORT_NAME")
    private String shortName;

	@Field(value="P_VALUE")
    private String pValue;

	@Field(value="SORT_NUM")
    private Long sortNum;

	@Field(value="REMARK")
    private String remark;

    /** FLAG_SHOW - 当字典选择选择参数表时，是否可以作为待选项出现 */
	@Field(value="FLAG_SHOW")
    private String flagShow;

	private String status;

    public String getPkParam(){
        return this.pkParam;
    }
    public void setPkParam(String pkParam){
        this.pkParam = pkParam;
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

    public String getValue(){
        return this.value;
    }
    public void setValue(String value){
        this.value = value;
    }

    public String getShortName(){
        return this.shortName;
    }
    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getpValue(){
        return this.pValue;
    }
    public void setpValue(String pValue){
        this.pValue = pValue;
    }

    public Long getSortNum(){
        return this.sortNum;
    }
    public void setSortNum(Long sortNum){
        this.sortNum = sortNum;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public String getFlagShow(){
        return this.flagShow;
    }
    public void setFlagShow(String flagShow){
        this.flagShow = flagShow;
    }
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	} 
    
    
}