package com.zebone.nhis.common.module.emr.qc;

import java.util.Date;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;

public class EmrAmendRec {
    private String pkAmend;

    private String pkOrg;

    private String pkPv;

    private String pkGradeitem;

    private String pkRec;

    private String euType;

    private String content;

    private String pkEmp;
    
    private String pkDept;

    private Date dateReceive;
    
    private Date dateFinish;

    private String euStatus;

    private String pkEmpCreate;

    private Date dateCreate;

    private String remark;

    private String delFlag;

    private String creator;

    private Date createTime;

    private Date ts;

    private String status;
    
    private String empNameQc; 
    
    private String empName;
    
    private EmrAmendDoc doc;
    
    private EmrPatList patList;
    
    private EmrGradeStandard std;
    
    private EmrMedRec rec;
    
    private EmrDocType docType;
    
    private Date dateBack;
    
    private String noteBack;
    
    private String pkEmpQc;
    
    private Date dateQc;
    
    private String typeName;
    
    private String codeIp;
    
    private String namePi;
    
    private String ipTimes;
    
    
    public EmrAmendDoc getDoc() {
		return doc;
	}

	public void setDoc(EmrAmendDoc doc) {
		this.doc = doc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPkAmend() {
        return pkAmend;
    }

    public void setPkAmend(String pkAmend) {
        this.pkAmend = pkAmend == null ? null : pkAmend.trim();
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg == null ? null : pkOrg.trim();
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv == null ? null : pkPv.trim();
    }

    public String getPkGradeitem() {
        return pkGradeitem;
    }

    public void setPkGradeitem(String pkGradeitem) {
        this.pkGradeitem = pkGradeitem == null ? null : pkGradeitem.trim();
    }

    public String getPkRec() {
        return pkRec;
    }

    public void setPkRec(String pkRec) {
        this.pkRec = pkRec == null ? null : pkRec.trim();
    }

    public String getEuType() {
        return euType;
    }

    public void setEuType(String euType) {
        this.euType = euType == null ? null : euType.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getPkEmp() {
        return pkEmp;
    }

    public void setPkEmp(String pkEmp) {
        this.pkEmp = pkEmp == null ? null : pkEmp.trim();
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus == null ? null : euStatus.trim();
    }

    public String getPkEmpCreate() {
        return pkEmpCreate;
    }

    public void setPkEmpCreate(String pkEmpCreate) {
        this.pkEmpCreate = pkEmpCreate == null ? null : pkEmpCreate.trim();
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag == null ? null : delFlag.trim();
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

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}


	public EmrPatList getPatList() {
		return patList;
	}

	public void setPatList(EmrPatList patList) {
		this.patList = patList;
	}

	public EmrGradeStandard getStd() {
		return std;
	}

	public void setStd(EmrGradeStandard std) {
		this.std = std;
	}

	public String getEmpNameQc() {
		return empNameQc;
	}

	public void setEmpNameQc(String empNameQc) {
		this.empNameQc = empNameQc;
	}

	public EmrMedRec getRec() {
		return rec;
	}

	public void setRec(EmrMedRec rec) {
		this.rec = rec;
	}

	public Date getDateReceive() {
		return dateReceive;
	}

	public void setDateReceive(Date dateReceive) {
		this.dateReceive = dateReceive;
	}

	public EmrDocType getDocType() {
		return docType;
	}

	public void setDocType(EmrDocType docType) {
		this.docType = docType;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Date getDateBack() {
		return dateBack;
	}

	public void setDateBack(Date dateBack) {
		this.dateBack = dateBack;
	}

	public String getNoteBack() {
		return noteBack;
	}

	public void setNoteBack(String noteBack) {
		this.noteBack = noteBack;
	}

	public String getPkEmpQc() {
		return pkEmpQc;
	}

	public void setPkEmpQc(String pkEmpQc) {
		this.pkEmpQc = pkEmpQc;
	}

	public Date getDateQc() {
		return dateQc;
	}

	public void setDateQc(Date dateQc) {
		this.dateQc = dateQc;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	
    
}