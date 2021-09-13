package com.zebone.nhis.bl.pub.syx.vo;

import com.zebone.nhis.common.module.base.bd.price.BdHp;

public class HpVo extends BdHp{
    private String pkPicate;//pi_cate表主键
    
    private String codeAttr;//医保扩展属性
    
    private String valAttr;//医保扩展属性值
    
    private String faCode;//上级医保编码

	public String getFaCode() {
		return faCode;
	}

	public void setFaCode(String faCode) {
		this.faCode = faCode;
	}

	public String getPkPicate() {
		return pkPicate;
	}
	
	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}

	public String getCodeAttr() {
		return codeAttr;
	}

	public void setCodeAttr(String codeAttr) {
		this.codeAttr = codeAttr;
	}

	public String getValAttr() {
		return valAttr;
	}

	public void setValAttr(String valAttr) {
		this.valAttr = valAttr;
	}
  
  
}
