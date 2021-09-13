package com.zebone.nhis.compay.ins.lb.vo.szyb;

public class ParamLoginInSave {
	
	 /// <summary>
    /// 签到人，
    /// </summary>
    public String qdr;

    /// <summary>
    /// 医保类型，
    /// </summary>
    public String yblx;

    /// <summary>
    /// 签到时间，
    /// </summary>
    public String qdsj;

    /// <summary>
    /// 签到业务周期号
    /// </summary>
    public String qdywzqh;
    
    //签到状态
    private String qdzt;
    
    //签退时间
    private String qtsj;

	public String getQtsj() {
		return qtsj;
	}

	public void setQtsj(String qtsj) {
		this.qtsj = qtsj;
	}

	public String getQdzt() {
		return qdzt;
	}

	public void setQdzt(String qdzt) {
		this.qdzt = qdzt;
	}

	public String getQdr() {
		return qdr;
	}

	public void setQdr(String qdr) {
		this.qdr = qdr;
	}

	public String getYblx() {
		return yblx;
	}

	public void setYblx(String yblx) {
		this.yblx = yblx;
	}

	public String getQdsj() {
		return qdsj;
	}

	public void setQdsj(String qdsj) {
		this.qdsj = qdsj;
	}

	public String getQdywzqh() {
		return qdywzqh;
	}

	public void setQdywzqh(String qdywzqh) {
		this.qdywzqh = qdywzqh;
	}
}
