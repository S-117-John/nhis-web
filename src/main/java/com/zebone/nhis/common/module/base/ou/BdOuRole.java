package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 角色
 * @author Xulj
 *
 */
@Table(value="bd_ou_role")
public class BdOuRole {

	/**
	 * 角色主键
	 */
	@PK
	@Field(value="pk_role",id=KeyId.UUID)
	private String pkRole;
	
	/**
	 * 角色编码
	 */
	@Field(value="code_role")
	private String codeRole;
	
	/**
	 * 角色名称
	 */
	@Field(value="name_role")
	private String nameRole;
	
	/**
     * 拼音码
     */
    @Field(value = "spcode")
    private String spcode;
    
    /**
     * 自定义码
     */
    @Field(value = "d_code")
    private String dCode;
	
	/**
	 * 角色类型
	 */
	@Field(value="eu_roletype")
	private String euRoletype;
	
	/**
	 * 角色备注
	 */
	@Field(value="note")
	private String note;

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
	
	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getPkRole() {
		return pkRole;
	}

	public void setPkRole(String pkRole) {
		this.pkRole = pkRole;
	}

	public String getCodeRole() {
		return codeRole;
	}

	public void setCodeRole(String codeRole) {
		this.codeRole = codeRole;
	}

	public String getNameRole() {
		return nameRole;
	}

	public void setNameRole(String nameRole) {
		this.nameRole = nameRole;
	}

	public String getEuRoletype() {
		return euRoletype;
	}

	public void setEuRoletype(String euRoletype) {
		this.euRoletype = euRoletype;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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
