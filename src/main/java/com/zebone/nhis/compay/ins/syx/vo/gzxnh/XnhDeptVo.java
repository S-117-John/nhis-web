package com.zebone.nhis.compay.ins.syx.vo.gzxnh;

import com.zebone.platform.modules.dao.build.au.Field;

public class XnhDeptVo {

	@Field(value = "CODE_INSDEPT")
    private String codeinsdept ;

	@Field(value = "NAME_INSDEPT")
    private String nameinsdept ;

	public String getCodeinsdept() {
		return codeinsdept;
	}

	public void setCodeinsdept(String codeinsdept) {
		this.codeinsdept = codeinsdept;
	}

	public String getNameinsdept() {
		return nameinsdept;
	}

	public void setNameinsdept(String nameinsdept) {
		this.nameinsdept = nameinsdept;
	}
	
	
	
	}
