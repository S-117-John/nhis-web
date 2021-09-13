package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;


/**
 * 修改状态接口返回值
 * @author zrj
 *
 */
public class UpdateStatusReturnData {
	
	/*
	 * 取消结算时，返回取消结算那条负记录的主键
	 */
	private String pkInsstwi;//	省内工伤医保结算主键

	public String getPkInsstwi() {
		return pkInsstwi;
	}

	public void setPkInsstwi(String pkInsstwi) {
		this.pkInsstwi = pkInsstwi;
	}
}
