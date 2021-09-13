package com.zebone.nhis.pro.zsba.compay.other.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 投票参数表
 * @author 85102
 *
 */
@Table(value="DRUG_OPT_CONFIG")
public class DrugOptConfig {
	
	/**  投票结果主键 */
	@PK
	@Field(value="PK_CONFIG",id=KeyId.UUID)
    private String pkConfig;
	 
    /** 编码 */
	@Field(value="CODE")
    private String code; 
	
    /** 名称 */
	@Field(value="NAME")
    private String name;

    /** 值 */
	@Field(value="VALUE")
    private String value;
	
	/** 创建时间 */
	@Field(value="CREATE_TIME")
    private Date createTime;

    /** 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

	public String getPkConfig() {
		return pkConfig;
	}

	public void setPkConfig(String pkConfig) {
		this.pkConfig = pkConfig;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
