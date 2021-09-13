package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

public class MzHpRegParam {
	
	  /**患者主键 */
    private String PkPi;
    /** 挂号科室编码 */
    private String GhksCode;

    /**  挂号科室名称*/
    private String GhksName;

    /** 挂号医生编码 */
    private String GhysCode;
	/** 挂号医生姓名 */
    private String GhysName;
    /** 挂号日期 */
    private String GhRQ;
	public String getPkPi() {
		return PkPi;
	}
	public void setPkPi(String pkPi) {
		PkPi = pkPi;
	}
	public String getGhksCode() {
		return GhksCode;
	}
	public void setGhksCode(String ghksCode) {
		GhksCode = ghksCode;
	}
	public String getGhksName() {
		return GhksName;
	}
	public void setGhksName(String ghksName) {
		GhksName = ghksName;
	}
	public String getGhysCode() {
		return GhysCode;
	}
	public void setGhysCode(String ghysCode) {
		GhysCode = ghysCode;
	}
	public String getGhysName() {
		return GhysName;
	}
	public void setGhysName(String ghysName) {
		GhysName = ghysName;
	}
	public String getGhRQ() {
		return GhRQ;
	}
	public void setGhRQ(String ghRQ) {
		GhRQ = ghRQ;
	}
    
    
    
}
