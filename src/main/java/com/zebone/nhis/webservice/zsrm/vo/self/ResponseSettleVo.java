package com.zebone.nhis.webservice.zsrm.vo.self;

public class ResponseSettleVo {

	private String diagnostics;
	
    private String codePi;

    private String codeOp;
    
    private String codePv;

    private String codeInv;

    private String winnoConf;

    private String winnoConfNote;

    private String codeSt;

    private String applyNo;

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo;
    }

    public String getCodePi() {
        return codePi;
    }

    public void setCodePi(String codePi) {
        this.codePi = codePi;
    }

    public String getCodeOp() {
        return codeOp;
    }

    public void setCodeOp(String codeOp) {
        this.codeOp = codeOp;
    }

    public String getCodeInv() {
        return codeInv;
    }

    public void setCodeInv(String codeInv) {
        this.codeInv = codeInv;
    }

    public String getWinnoConf() {
        return winnoConf;
    }

    public void setWinnoConf(String winnoConf) {
        this.winnoConf = winnoConf;
    }

    public String getWinnoConfNote() {
        return winnoConfNote;
    }

    public void setWinnoConfNote(String winnoConfNote) {
        this.winnoConfNote = winnoConfNote;
    }

    public String getCodeSt() {
        return codeSt;
    }

    public void setCodeSt(String codeSt) {
        this.codeSt = codeSt;
    }

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getDiagnostics() {
		return diagnostics;
	}

	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}
    
}
