package com.zebone.nhis.bl.pub.vo;

/**
 * 
 * 挂号按医保分类人数信息
 * 
 * @author wangpeng
 * @date 2016年10月25日
 *
 */
public class InsuCountVo {
	
	/** 医保主键 */
	private String pkInsu;
	
	/** 医保名称 */
	private String nameInsu;
	
	/** 挂号人数 */
	private Integer cnt;
	
	/** 退号人数 */
	private Integer cntCancel;
	
	/** 挂号金额 */
	private Double amount;

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getNameInsu() {
		return nameInsu;
	}

	public void setNameInsu(String nameInsu) {
		this.nameInsu = nameInsu;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getCntCancel() {
		return cntCancel;
	}

	public void setCntCancel(Integer cntCancel) {
		this.cntCancel = cntCancel;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

}
