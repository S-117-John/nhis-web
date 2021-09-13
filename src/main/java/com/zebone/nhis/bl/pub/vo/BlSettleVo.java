package com.zebone.nhis.bl.pub.vo;

public class BlSettleVo {
	/// <summary>
    /// --结算PK
    /// </summary>
    private String pkSettle ; 
    
    /// <summary>
    ///  --结算日期
    /// </summary>
    private String dateSt ;  

     /// <summary>
     /// --结算类型
     /// </summary>
    private String dtSttype ; 

     /// <summary>
     /// --结算编码
     /// </summary>
    private String codeSt ;  

     /// <summary>
     /// --开始日期
     /// </summary>
    private String dateBegin ;

     /// <summary>
     /// --结束日期
     /// </summary>
    private String dateEnd ;  

     /// <summary>
     /// --预交金
     /// </summary>
    private double amountPrep;

     /// <summary>
     /// --总费用
     /// </summary>
    private double amountSt;

     /// <summary>
     ///  --患者自付
     /// </summary>
    private double amountPi;

     /// <summary>
     /// --医保支付
     /// </summary>
    private double amountInsu ;

     /// <summary>
     /// --优惠金额
     /// </summary>
    private double amountDisc;

     /// <summary>
    /// --操作员
     /// </summary>
    private String nameEmpSt;

    private String reasonCanc;
    
    private String dtPaymode;
    
    private String pkSettleCanc;
    
    //自费总额
    private Double totalAmtPi;
    
    
	public Double getTotalAmtPi() {
		return totalAmtPi;
	}

	public void setTotalAmtPi(Double totalAmtPi) {
		this.totalAmtPi = totalAmtPi;
	}

	public String getPkSettleCanc() {
		return pkSettleCanc;
	}

	public void setPkSettleCanc(String pkSettleCanc) {
		this.pkSettleCanc = pkSettleCanc;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getDateSt() {
		return dateSt;
	}

	public void setDateSt(String dateSt) {
		this.dateSt = dateSt;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(String dateBegin) {
		this.dateBegin = dateBegin;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public double getAmountPrep() {
		return amountPrep;
	}

	public void setAmountPrep(double amountPrep) {
		this.amountPrep = amountPrep;
	}

	public double getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(double amountSt) {
		this.amountSt = amountSt;
	}

	public double getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(double amountPi) {
		this.amountPi = amountPi;
	}

	public double getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(double amountInsu) {
		this.amountInsu = amountInsu;
	}

	public double getAmountDisc() {
		return amountDisc;
	}

	public void setAmountDisc(double amountDisc) {
		this.amountDisc = amountDisc;
	}

	public String getNameEmpSt() {
		return nameEmpSt;
	}

	public void setNameEmpSt(String nameEmpSt) {
		this.nameEmpSt = nameEmpSt;
	}

	public String getReasonCanc() {
		return reasonCanc;
	}

	public void setReasonCanc(String reasonCanc) {
		this.reasonCanc = reasonCanc;
	}

	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
    
    
}
