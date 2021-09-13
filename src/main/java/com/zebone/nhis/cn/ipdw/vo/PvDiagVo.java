package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.pv.PvDiag;

public class PvDiagVo extends PvDiag{
    
    private String pkDiag;
    private String diagcode;
    private String diagname;
    private String sum;
    private String imgIndex;
    private String empCode;
    private String diagtypeName;
    private String codePv;
    private String codeIp;
    private Integer sn;
    
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
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getDiagtypeName() {
		return diagtypeName;
	}
	public void setDiagtypeName(String diagtypeName) {
		this.diagtypeName = diagtypeName;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	
 }
