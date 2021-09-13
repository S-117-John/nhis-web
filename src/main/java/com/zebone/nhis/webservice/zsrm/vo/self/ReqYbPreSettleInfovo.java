package com.zebone.nhis.webservice.zsrm.vo.self;

import java.math.BigDecimal;

public class ReqYbPreSettleInfovo {
    private BigDecimal amount;

    private BigDecimal amtJjzf;

    private BigDecimal amtGrzhzf;

    private BigDecimal amtGrzf;

    private BigDecimal amtGrzh;
    
    private BigDecimal cashAddFee;

    private String medType;

    private String mdtrtId;

    private String mdtrtCertType;
    
    private String grzhzfPayType;
    
    private String gzSerialNo;
    
    private String gzTradeNo;
    
    private String gzBankCardNo;
    
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmtJjzf() {
        return amtJjzf;
    }

    public void setAmtJjzf(BigDecimal amtJjzf) {
        this.amtJjzf = amtJjzf;
    }

    public BigDecimal getAmtGrzhzf() {
        return amtGrzhzf;
    }

    public void setAmtGrzhzf(BigDecimal amtGrzhzf) {
        this.amtGrzhzf = amtGrzhzf;
    }

    public BigDecimal getAmtGrzf() {
        return amtGrzf;
    }

    public void setAmtGrzf(BigDecimal amtGrzf) {
        this.amtGrzf = amtGrzf;
    }

    public BigDecimal getAmtGrzh() {
        return amtGrzh;
    }

    public void setAmtGrzh(BigDecimal amtGrzh) {
        this.amtGrzh = amtGrzh;
    }

    public String getMedType() {
        return medType;
    }

    public void setMedType(String medType) {
        this.medType = medType;
    }

    public String getMdtrtId() {
        return mdtrtId;
    }

    public void setMdtrtId(String mdtrtId) {
        this.mdtrtId = mdtrtId;
    }

    public String getMdtrtCertType() {
        return mdtrtCertType;
    }

    public void setMdtrtCertType(String mdtrtCertType) {
        this.mdtrtCertType = mdtrtCertType;
    }

	public String getGrzhzfPayType() {
		return grzhzfPayType;
	}

	public void setGrzhzfPayType(String grzhzfPayType) {
		this.grzhzfPayType = grzhzfPayType;
	}

	public String getGzSerialNo() {
		return gzSerialNo;
	}

	public String getGzTradeNo() {
		return gzTradeNo;
	}

	public void setGzSerialNo(String gzSerialNo) {
		this.gzSerialNo = gzSerialNo;
	}

	public void setGzTradeNo(String gzTradeNo) {
		this.gzTradeNo = gzTradeNo;
	}

	public String getGzBankCardNo() {
		return gzBankCardNo;
	}

	public void setGzBankCardNo(String gzBankCardNo) {
		this.gzBankCardNo = gzBankCardNo;
	}

	public BigDecimal getCashAddFee() {
		return cashAddFee;
	}

	public void setCashAddFee(BigDecimal cashAddFee) {
		this.cashAddFee = cashAddFee;
	}
	
    
}
