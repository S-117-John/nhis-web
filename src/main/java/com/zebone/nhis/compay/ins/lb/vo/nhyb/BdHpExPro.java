package com.zebone.nhis.compay.ins.lb.vo.nhyb;

import com.zebone.nhis.common.module.BaseModule;

public class BdHpExPro extends BaseModule  {

	//医保计划
    private String pkHp;
    //对应的第三方医保
    private String dtExthp; 
    //扩展属性
    private String valAttr;
    
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getDtExthp() {
		return dtExthp;
	}
	public void setDtExthp(String dtExthp) {
		this.dtExthp = dtExthp;
	}
	public String getValAttr() {
		return valAttr;
	}
	public void setValAttr(String valAttr) {
		this.valAttr = valAttr;
	}
}
