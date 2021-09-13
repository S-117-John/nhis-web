package com.zebone.nhis.ex.pivas.vo;

/***
 * 领药、静配参数
 * 
 * @author wangpeng
 * @date 2016年12月16日
 *
 */
public class PivasAndApplyParam {
	
	/** 领药明细主键 */
	private String[] pkPdapdts;
	
	/** 静配记录明细主键 */
	private String[] pkPdpivass;
	
	/** 停发原因 */
	private String reasonStop;

	public String[] getPkPdapdts() {
		return pkPdapdts;
	}

	public void setPkPdapdts(String[] pkPdapdts) {
		this.pkPdapdts = pkPdapdts;
	}

	public String[] getPkPdpivass() {
		return pkPdpivass;
	}

	public void setPkPdpivass(String[] pkPdpivass) {
		this.pkPdpivass = pkPdpivass;
	}

	public String getReasonStop() {
		return reasonStop;
	}

	public void setReasonStop(String reasonStop) {
		this.reasonStop = reasonStop;
	}

}
