package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: QC_EHP_REC - 病案首页质控记录 
 *
 * @since 2020-06-16 09:43:41
 */
@Table(value="QC_EHP_REC")
public class QcEhpRec extends BaseModule  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_QCEHPREC - 主键 */
	@PK
	@Field(value="PK_QCEHPREC",id=KeyId.UUID)
    private String pkQcehprec;

    /** PK_QCEHP - 关联质控 */
	@Field(value="PK_QCEHP")
    private String pkQcehp;

    /** EU_QCTYPE - 质控类型 */
	@Field(value="EU_QCTYPE")
    private String euQctype;

    /** PK_DEPT - 质控科室 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** DATE_QC - 质控日期 */
	@Field(value="DATE_QC")
    private Date dateQc;

    /** PK_EMP_QC - 质控人 */
	@Field(value="PK_EMP_QC")
    private String pkEmpQc;

    /** NAME_EMP_QC - 质控人姓名 */
	@Field(value="NAME_EMP_QC")
    private String nameEmpQc;

    /** EU_RESULT - 质控结果 */
	@Field(value="EU_RESULT")
    private String euResult;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;
	
	private String pkPv;
	/**
	 * 0项目质控
	 * 1评分
	 */
	private String euType;


    public String getPkQcehprec(){
        return this.pkQcehprec;
    }
    public void setPkQcehprec(String pkQcehprec){
        this.pkQcehprec = pkQcehprec;
    }

    public String getPkQcehp(){
        return this.pkQcehp;
    }
    public void setPkQcehp(String pkQcehp){
        this.pkQcehp = pkQcehp;
    }

    public String getEuQctype(){
        return this.euQctype;
    }
    public void setEuQctype(String euQctype){
        this.euQctype = euQctype;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public Date getDateQc(){
        return this.dateQc;
    }
    public void setDateQc(Date dateQc){
        this.dateQc = dateQc;
    }

    public String getPkEmpQc(){
        return this.pkEmpQc;
    }
    public void setPkEmpQc(String pkEmpQc){
        this.pkEmpQc = pkEmpQc;
    }

    public String getNameEmpQc(){
        return this.nameEmpQc;
    }
    public void setNameEmpQc(String nameEmpQc){
        this.nameEmpQc = nameEmpQc;
    }

    public String getEuResult(){
        return this.euResult;
    }
    public void setEuResult(String euResult){
        this.euResult = euResult;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getEuType() {
		return euType;
	}
	public void setEuType(String euType) {
		this.euType = euType;
	}
}