package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.ipdw.ExOpOcc;

public class ExOpOccVo extends ExOpOcc {

    private String pkOrdop;

    private String nameOp;

    private String namePi;

    private String codeIp;

    private String codeApply;

    private List<CnOpSubjoin> subOpList = new ArrayList<CnOpSubjoin>();

    private String pkDept;

    private String dtSex;

    private String agePv;

    private String bedNo;

    private String deptNsName;

    private String nameEmpOrd;
    
    private Date dateApply;

    private String euStatusOrd;
    //医嘱序号
    private String ordsn;
    
	public String getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}

	public Date getDateApply() {
		return dateApply;
	}

	public void setDateApply(Date dateApply) {
		this.dateApply = dateApply;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getDtSex() {return dtSex;}

    public void setDtSex(String dtSex) {this.dtSex = dtSex;}

    public String getAgePv() {return agePv;}

    public void setAgePv(String agePv) {this.agePv = agePv;}

    public String getBedNo() {return bedNo;}

    public void setBedNo(String bedNo) {this.bedNo = bedNo;}

    public String getDeptNsName() {return deptNsName;}

    public void setDeptNsName(String deptNsName) {this.deptNsName = deptNsName;}

    public String getNameOp() {
        return nameOp;
    }

    public void setNameOp(String nameOp) {
        this.nameOp = nameOp;
    }

    public String getNamePi() {
        return namePi;
    }

    public void setNamePi(String namePi) {
        this.namePi = namePi;
    }

    public String getCodeIp() {
        return codeIp;
    }

    public void setCodeIp(String codeIp) {
        this.codeIp = codeIp;
    }

    public String getCodeApply() {
        return codeApply;
    }

    public void setCodeApply(String codeApply) {
        this.codeApply = codeApply;
    }

    public List<CnOpSubjoin> getSubOpList() { return subOpList; }

    public void setSubOpList(List<CnOpSubjoin> subOpList) { this.subOpList = subOpList; }

    public String getPkOrdop() {
        return pkOrdop;
    }

    public void setPkOrdop(String pkOrdop) {
        this.pkOrdop = pkOrdop;
    }

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

    public String getEuStatusOrd() {return euStatusOrd;}

    public void setEuStatusOrd(String euStatusOrd) {this.euStatusOrd = euStatusOrd;}
}
