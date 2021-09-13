package com.zebone.nhis.common.module.emr.rec.rec;

public class EmrHanDoverDuty {
	
	 /**
     *  患者主键
     */
    private String pkPi;
    
    /**
     * 体温
     * */
    private String tw;
    
    
    /**
     * 呼吸
     * */
    private String hx;
    
    /**
     * 
     * 心率
     * */
    private String xl;
    
    /**
     * spo2
     * */
    private String spo;
    
    /**
     * cvp(血压)
     * */
    private String cvp;

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getTw() {
		return tw;
	}

	public void setTw(String tw) {
		this.tw = tw;
	}

	public String getHx() {
		return hx;
	}

	public void setHx(String hx) {
		this.hx = hx;
	}

	public String getXl() {
		return xl;
	}

	public void setXl(String xl) {
		this.xl = xl;
	}

	public String getSpo() {
		return spo;
	}

	public void setSpo(String spo) {
		this.spo = spo;
	}

	public String getCvp() {
		return cvp;
	}

	public void setCvp(String cvp) {
		this.cvp = cvp;
	}
    
    
    
}
