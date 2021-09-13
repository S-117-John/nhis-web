package com.zebone.nhis.common.module.emr.rec.dict;

import java.util.Date;

public class EmrNurse {
    private String pkNurse;

    private String pkOrg;

    private String pkEmp;

    private String code;

    private String name;

    private String euQcLevel;

    private String signPwd;

    private String pyCode;

    private String dCode;

    private String pkDept;

    private String pkDeptNs;

    private String def1;

    private String def2;

    private String def3;

    private String def4;

    private String def5;

    private String delFlag;

    private String remark;

    private String creator;

    private Date createTime;

    private Date ts;

    private byte[] signImage;

    private String deptName;
    
    private String deptNsName;
    
    private String status;
    
    public String getPkNurse() {
        return pkNurse;
    }

    public void setPkNurse(String pkNurse) {
        this.pkNurse = pkNurse == null ? null : pkNurse.trim();
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg == null ? null : pkOrg.trim();
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp == null ? null : pkEmp.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getEuQcLevel() {
        return euQcLevel;
    }

    public void setEuQcLevel(String euQcLevel) {
        this.euQcLevel = euQcLevel == null ? null : euQcLevel.trim();
    }

    public String getSignPwd() {
        return signPwd;
    }

    public void setSignPwd(String signPwd) {
        this.signPwd = signPwd == null ? null : signPwd.trim();
    }

    public String getPyCode() {
        return pyCode;
    }

    public void setPyCode(String pyCode) {
        this.pyCode = pyCode == null ? null : pyCode.trim();
    }

    public String getdCode() {
        return dCode;
    }

    public void setdCode(String dCode) {
        this.dCode = dCode == null ? null : dCode.trim();
    }

    public String getPkDept() {
        return pkDept;
    }

    public void setPkDept(String pkDept) {
        this.pkDept = pkDept == null ? null : pkDept.trim();
    }

    public String getPkDeptNs() {
        return pkDeptNs;
    }

    public void setPkDeptNs(String pkDeptNs) {
        this.pkDeptNs = pkDeptNs == null ? null : pkDeptNs.trim();
    }

    public String getDef1() {
        return def1;
    }

    public void setDef1(String def1) {
        this.def1 = def1 == null ? null : def1.trim();
    }

    public String getDef2() {
        return def2;
    }

    public void setDef2(String def2) {
        this.def2 = def2 == null ? null : def2.trim();
    }

    public String getDef3() {
        return def3;
    }

    public void setDef3(String def3) {
        this.def3 = def3 == null ? null : def3.trim();
    }

    public String getDef4() {
        return def4;
    }

    public void setDef4(String def4) {
        this.def4 = def4 == null ? null : def4.trim();
    }

    public String getDef5() {
        return def5;
    }

    public void setDef5(String def5) {
        this.def5 = def5 == null ? null : def5.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public byte[] getSignImage() {
        return signImage;
    }

    public void setSignImage(byte[] signImage) {
        this.signImage = signImage;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptNsName() {
		return deptNsName;
	}

	public void setDeptNsName(String deptNsName) {
		this.deptNsName = deptNsName;
	}
 
}