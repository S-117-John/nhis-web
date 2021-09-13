package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 用户组科室关系
 * @author Xulj
 *
 */
@Table(value="bd_ou_usrgrp_dept")
public class BdOuUsrgrpDept {

	/**
	 * 主键
	 */
	@PK
	@Field(value="pk_usrgrpdept",id=KeyId.UUID)
	private String pkUsrgrpdept;
	
	/**
	 * 科室主键
	 */
	@Field(value="pk_dept")
	private String pkDept;
	
	/**
	 * 默认标识
	 */
	@Field(value="isdefualt")
	private String isdefualt;
	
	/**
	 * 用户组主键
	 */
	@Field(value="pk_usrgrp")
	private String pkUsrgrp;
	
	/**
	 * 序号
	 */
	@Field(value="seqno")
	private int seqno;

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
	
	public String getPkUsrgrpdept() {
		return pkUsrgrpdept;
	}

	public void setPkUsrgrpdept(String pkUsrgrpdept) {
		this.pkUsrgrpdept = pkUsrgrpdept;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getIsdefualt() {
		return isdefualt;
	}

	public void setIsdefualt(String isdefualt) {
		this.isdefualt = isdefualt;
	}

	public String getPkUsrgrp() {
		return pkUsrgrp;
	}

	public void setPkUsrgrp(String pkUsrgrp) {
		this.pkUsrgrp = pkUsrgrp;
	}

	public int getSeqno() {
		return seqno;
	}

	public void setSeqno(int seqno) {
		this.seqno = seqno;
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
