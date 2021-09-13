package com.zebone.nhis.bl.pub.vo;

public  class BillInfo {
	

    /// 获取或设置号码前缀
    private String invPrefix;
    
    private String prefix;

    
    /// 当前发票号值 前缀+补位数
    
    private  String curCodeInv;

    
    /// 获取或设置领用主键
    
    private  String pkEmpinv;

    
    /// 获取或设置票据分类主键
    
    private  String pkInvcate;

    
    /// 获取或设置当前票据号码
    
    private  int curNo;

  
    
    /// 获取或设置剩余张数
    
    private  int cntUse;


    
    /// 票据的固定长度
    
    private  int length;
    
    //客户端计算机名称
    private String nameMachine;


	public String getNameMachine() {
		return nameMachine;
	}



	public void setNameMachine(String nameMachine) {
		this.nameMachine = nameMachine;
	}



	public String getPrefix() {
		return prefix;
	}



	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}



	public String getInvPrefix() {
		return invPrefix;
	}



	public void setInvPrefix(String invPrefix) {
		this.invPrefix = invPrefix;
	}



	public String getCurCodeInv() {
		return curCodeInv;
	}



	public void setCurCodeInv(String curCodeInv) {
		this.curCodeInv = curCodeInv;
	}



	public String getPkEmpinv() {
		return pkEmpinv;
	}



	public void setPkEmpinv(String pkEmpinv) {
		this.pkEmpinv = pkEmpinv;
	}



	public String getPkInvcate() {
		return pkInvcate;
	}



	public void setPkInvcate(String pkInvcate) {
		this.pkInvcate = pkInvcate;
	}



	public int getCurNo() {
		return curNo;
	}



	public void setCurNo(int curNo) {
		this.curNo = curNo;
	}



	public int getCntUse() {
		return cntUse;
	}



	public void setCntUse(int cntUse) {
		this.cntUse = cntUse;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
	}

}
