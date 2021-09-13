package com.zebone.nhis.common.module.base.bd.srv;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: BD_AUDIT  - bd_audit 
 *
 * @since 2016-09-09 01:59:54
 */
@Table(value="BD_ORD_ATD")
public class BdOrdAtd extends BaseModule  {

	@PK
	@Field(value="PK_ORDATD",id=KeyId.UUID)
    private String pkOrdatd;

	@Field(value="PK_ORG")
    private String pkOrg;

	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="PK_ORGAREA")
    private String pkOrgarea;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_ORD")
    private String pkOrd;

	public String getPkOrdatd() {
		return pkOrdatd;
	}

	public void setPkOrdatd(String pkOrdatd) {
		this.pkOrdatd = pkOrdatd;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getPkOrgarea() {
		return pkOrgarea;
	}

	public void setPkOrgarea(String pkOrgarea) {
		this.pkOrgarea = pkOrgarea;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

}