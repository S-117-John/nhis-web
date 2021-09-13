package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;

import java.util.Date;
import java.util.List;

public class CnOpApplyVo extends CnOpApply {

    private String namePi;

    private String codeIp;

    private String dtSex;

    private String agePv;

    private String bedNo;

    private String deptNsName;
    
    private String nameEmpOrd;

    private Date dateEnd;
    
    private String dsaNo;
    
    private String nameEmpTech;
    
    private String pkEmpTech;
    
    private String pkDiagAf;
    
    private String descDiagAf;
    
    private String attrDesc;
    
    private String flagFinishAnae;
    
    private String dtOpmode;//手术方式
    
    private String ordsn;//医嘱序号
    
    public String getOrdsn() {
		return ordsn;
	}

	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}

	public String getDtOpmode() {
		return dtOpmode;
	}

	public void setDtOpmode(String dtOpmode) {
		this.dtOpmode = dtOpmode;
	}

	public String getDtSex() {return dtSex;}

    public void setDtSex(String dtSex) {this.dtSex = dtSex;}

    public String getAgePv() {return agePv;}

    public void setAgePv(String agePv) {this.agePv = agePv;}

    public String getBedNo() {return bedNo;}

    public void setBedNo(String bedNo) {this.bedNo = bedNo;}

    public String getDeptNsName() {return deptNsName;}

    public void setDeptNsName(String deptNsName) {this.deptNsName = deptNsName;}

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

	public String getNameEmpOrd() {
		return nameEmpOrd;
	}

	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getDsaNo() {
		return dsaNo;
	}

	public void setDsaNo(String dsaNo) {
		this.dsaNo = dsaNo;
	}

	public String getNameEmpTech() {
		return nameEmpTech;
	}

	public void setNameEmpTech(String nameEmpTech) {
		this.nameEmpTech = nameEmpTech;
	}

	public String getPkEmpTech() {
		return pkEmpTech;
	}

	public void setPkEmpTech(String pkEmpTech) {
		this.pkEmpTech = pkEmpTech;
	}

	public String getPkDiagAf() {
		return pkDiagAf;
	}

	public void setPkDiagAf(String pkDiagAf) {
		this.pkDiagAf = pkDiagAf;
	}

	public String getDescDiagAf() {
		return descDiagAf;
	}

	public void setDescDiagAf(String descDiagAf) {
		this.descDiagAf = descDiagAf;
	}

	public String getAttrDesc() {
		return attrDesc;
	}

	public void setAttrDesc(String attrDesc) {
		this.attrDesc = attrDesc;
	}

	public String getFlagFinishAnae() {
		return flagFinishAnae;
	}

	public void setFlagFinishAnae(String flagFinishAnae) {
		this.flagFinishAnae = flagFinishAnae;
	}
	
}
