package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.scm.pub.BdPdStorePack;

public class BdPdStorePackVo extends BdPdStorePack {
  private String spec;
  private String pkUnitPack;//替换原来的pk_pdconvert字段值
 

public String getPkUnitPack() {
	return pkUnitPack;
}

public void setPkUnitPack(String pkUnitPack) {
	this.pkUnitPack = pkUnitPack;
}

public String getSpec() {
	return spec;
}

public void setSpec(String spec) {
	this.spec = spec;
}
  
}
