package com.zebone.nhis.common.module.sch.pub;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="SCH_RESOURCE_DT")
public class SchResourceDt extends BaseModule{
	
	
	/* 主键 */
	@PK
	@Field(value="PK_SCHRESDT",id=KeyId.UUID)
	private String pkSchresdt;
	
	/*关联资源 */
	@Field(value="PK_SCHRES")
	private String pkSchres;

	/*日期分组编码主键 */
	@Field(value="PK_DATESLOT")
	private String pkDateslot;
	
	

	/*指导号数*/
	@Field(value="CNT_STD")
	private int cntStd;
	/*日期分组名称*/
	private String nameDateslot;
	/*日期分组编码*/
	private String codeDateslot;
	
	public String getNameDateslot() {
		return nameDateslot;
	}

	public void setNameDateslot(String nameDateslot) {
		this.nameDateslot = nameDateslot;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public int getCntStd() {
		return cntStd;
	}

	public void setCntStd(int cntStd) {
		this.cntStd = cntStd;
	}

	public String getPkSchresdt() {
		return pkSchresdt;
	}

	public void setPkSchresdt(String pkSchresdt) {
		this.pkSchresdt = pkSchresdt;
	}

	public String getPkSchres() {
		return pkSchres;
	}

	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
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


	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getCodeDateslot() {
		return codeDateslot;
	}

	public void setCodeDateslot(String codeDateslot) {
		this.codeDateslot = codeDateslot;
	}
	
}
