package com.zebone.nhis.emr.rec.rec.vo;

public class PvDiagVo {
    
    private String pkDiag;
    private String diagcode;
    private String diagname;
    private String sum;
    private String imgIndex;
    private String flagMaj;
    private String dtDiagtype;
    private String euAdmcon;
    private String diagDesc;
    private String pkPvdiag;
    private String pkFather;
    private int sortNo;
    
    public String getDiagDesc() {
		return diagDesc;
	}
	public void setDiagDesc(String diagDesc) {
		this.diagDesc = diagDesc;
	}
	public String getEuAdmcon() {
		return euAdmcon;
	}
	public void setEuAdmcon(String euAdmcon) {
		this.euAdmcon = euAdmcon;
	}
	public String getPkDiag() {
        return pkDiag;
    }
    public void setPkDiag(String pkDiag) {
        this.pkDiag = pkDiag;
    }
    public String getDiagcode() {
        return diagcode;
    }
    public void setDiagcode(String diagcode) {
        this.diagcode = diagcode;
    }
    public String getDiagname() {
        return diagname;
    }
    public void setDiagname(String diagname) {
        this.diagname = diagname;
    }
    public String getSum() {
        return sum;
    }
    public void setSum(String sum) {
        this.sum = sum;
    }
    public String getImgIndex() {
        return imgIndex;
    }
    public void setImgIndex(String imgIndex) {
        this.imgIndex = imgIndex;
    }
	public String getFlagMaj() {
		return flagMaj;
	}
	public void setFlagMaj(String flagMaj) {
		this.flagMaj = flagMaj;
	}
	public String getDtDiagtype() {
		return dtDiagtype;
	}
	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}

	public int getSortNo() {
		return sortNo;
	}
	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}
	public String getPkPvdiag() {
		return pkPvdiag;
	}
	public void setPkPvdiag(String pkPvdiag) {
		this.pkPvdiag = pkPvdiag;
	}
	public String getPkFather() {
		return pkFather;
	}
	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

}
