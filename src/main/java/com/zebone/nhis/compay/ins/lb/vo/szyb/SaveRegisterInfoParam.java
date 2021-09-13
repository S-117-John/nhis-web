package com.zebone.nhis.compay.ins.lb.vo.szyb;

public class SaveRegisterInfoParam  extends RegisterParam{
	
	public String id;

    public String pkPv;

    public String ywlx;
    
    private String fsflsh;

    /** 读卡方式 */
	private String disType;
	
	public String getFsflsh() {
		return fsflsh;
	}

	public void setFsflsh(String fsflsh) {
		this.fsflsh = fsflsh;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getYwlx() {
		return ywlx;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public String getDisType() {
		return disType;
	}

	public void setDisType(String disType) {
		this.disType = disType;
	}
	
}
