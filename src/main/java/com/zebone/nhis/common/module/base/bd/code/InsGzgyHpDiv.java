package com.zebone.nhis.common.module.base.bd.code;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table:INS_GZGY_HP_DIV
 * @author 
 *
 */
@Table(value="INS_GZGY_HP_DIV")
public class InsGzgyHpDiv {
	@PK
	@Field(value="PK_HPDIV",id=KeyId.UUID)
	private String pkHpdiv;
	
	@Field(value="PK_HP")
	private String pkHp;
	
	@Field(value="EU_DIVTYPE")
	private String euDivtype;
	
	@Field(value="PK_DIV")
	private String pkDiv;
	
	@Field(value="CREATE_TIME",date=FieldType.INSERT)
	private Date createTime;
	
	@Field(value="CREATOR",userfieldscop=FieldType.INSERT)
	private String creator;
	
	@Field(value="DEL_FLAG")
	private String delFlag = "0";
	
	@Field(date=FieldType.ALL)
	private Date ts;
	
	public String getPkHpdiv() {
		return pkHpdiv;
	}

	public void setPkHpdiv(String pkHpdiv) {
		this.pkHpdiv = pkHpdiv;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getEuDivtype() {
		return euDivtype;
	}

	public void setEuDivtype(String euDivtype) {
		this.euDivtype = euDivtype;
	}

	public String getPkDiv() {
		return pkDiv;
	}

	public void setPkDiv(String pkDiv) {
		this.pkDiv = pkDiv;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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
}
