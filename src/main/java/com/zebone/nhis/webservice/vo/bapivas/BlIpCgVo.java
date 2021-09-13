package com.zebone.nhis.webservice.vo.bapivas;

import java.util.List;

public class BlIpCgVo {
	private List<BlPubParamVo> blCgPubParamVos ;
	private boolean isAllowQF;
	public List<BlPubParamVo> getBlCgPubParamVos() {
		return blCgPubParamVos;
	}
	public void setBlCgPubParamVos(List<BlPubParamVo> blCgPubParamVos) {
		this.blCgPubParamVos = blCgPubParamVos;
	}
	public boolean isAllowQF() {
		return isAllowQF;
	}
	public void setAllowQF(boolean isAllowQF) {
		this.isAllowQF = isAllowQF;
	}
	
	
}
