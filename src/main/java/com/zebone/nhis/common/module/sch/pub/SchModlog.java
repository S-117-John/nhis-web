package com.zebone.nhis.common.module.sch.pub;

import java.io.Serializable;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_MODLOG 
 *
 * @since 2020-10-11 09:33:21
 * 排班修改记录
 */
@Table(value="SCH_MODLOG")
public class SchModlog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PK
	@Field(value="PK_SCHLOG",id=KeyId.UUID)
    private String pkSchlog;

	@Field(value="PK_SCH")
    private String pkSch;
	/**
	 * 修改类型1 停诊，2 加号，3 减号
	 */
	@Field(value="EU_TYPE")
    private String euType;
	/**
	 * 修改原因
	 */
	@Field(value="REASONS")
    private String reasons;

	/**
     * 机构主键
     */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    public String pkOrg;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;
    
	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	public Date ts;

	public String getPkSchlog() {
		return pkSchlog;
	}

	public void setPkSchlog(String pkSchlog) {
		this.pkSchlog = pkSchlog;
	}

	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
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