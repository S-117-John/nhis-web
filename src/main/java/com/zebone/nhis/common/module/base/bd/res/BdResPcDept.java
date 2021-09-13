package com.zebone.nhis.common.module.base.bd.res;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="BD_RES_PC_DEPT")
public class BdResPcDept extends BaseModule{

	@PK
	@Field(value="PK_PCDEPT",id=KeyId.UUID)
    private String pkPcdept;

	@Field(value="PK_PC")
    private String pkPc;
	
	@Field(value="PK_QCPLATFORM")
    private String pkQcplatform;
	
	@Field(value="PK_DEPT")
    private String pkDept;

	public String getPkPcdept() {
		return pkPcdept;
	}

	public void setPkPcdept(String pkPcdept) {
		this.pkPcdept = pkPcdept;
	}

	public String getPkPc() {
		return pkPc;
	}

	public void setPkPc(String pkPc) {
		this.pkPc = pkPc;
	}

	public String getPkQcplatform() {
		return pkQcplatform;
	}

	public void setPkQcplatform(String pkQcplatform) {
		this.pkQcplatform = pkQcplatform;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	
	
}
