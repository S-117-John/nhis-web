package com.zebone.nhis.pi.pub.vo;

import java.util.Date;

public class PiBaseLaborVo extends PibaseVo {
	
	/** 孕次 */
    private String numPreg;
    
    /** 产次 */
    private String numProduct;
    
    /** 产房 - 床号 */
    private String CodeBed;
    
    /** 末次月经 */
    private Date DateLast;
    
    /** 入产房时间 */
    private Date DateIn;
    
    /** 孕产就诊主键 */
    private String PkPvlabor;

	public String getNumPreg() {
		return numPreg;
	}

	public void setNumPreg(String numPreg) {
		this.numPreg = numPreg;
	}

	public String getNumProduct() {
		return numProduct;
	}

	public void setNumProduct(String numProduct) {
		this.numProduct = numProduct;
	}

	public String getCodeBed() {
		return CodeBed;
	}

	public void setCodeBed(String codeBed) {
		CodeBed = codeBed;
	}

	public Date getDateLast() {
		return DateLast;
	}

	public void setDateLast(Date dateLast) {
		DateLast = dateLast;
	}

	public Date getDateIn() {
		return DateIn;
	}

	public void setDateIn(Date dateIn) {
		DateIn = dateIn;
	}

	public String getPkPvlabor() {
		return PkPvlabor;
	}

	public void setPkPvlabor(String pkPvlabor) {
		PkPvlabor = pkPvlabor;
	}
    
}
