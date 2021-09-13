package com.zebone.nhis.common.module.nd.record;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ND_RECORD_ROW 
 *
 * @since 2017-06-08 11:29:30
 */
@Table(value="ND_RECORD_ROW")
public class NdRecordRow extends BaseModule  {
	@PK
	@Field(value="PK_RECORDROW",id=KeyId.UUID)
    private String pkRecordrow;

	@Field(value="PK_RECORD")
    private String pkRecord;

	@Field(value="ROWNO")
    private Integer rowno;
	
	@Field(value="ROWNO_PARENT")
	private Integer rownoParent;

	@Field(value="PAGE_NO")
    private BigDecimal pageNo;

	@Field(value="DATE_SIGN")
    private Date dateSign;

	@Field(value="FLAG_SIGN")
    private String flagSign;

	@Field(value="PK_EMP")
    private String pkEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="FLAG_PRT")
    private String flagPrt;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="DATE_ENTRY")
    private Date dateEntry;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="FLAG_NOTE")
	private String flagNote;
	
//	@Field(value="FLAG_CHECK")
//	private String flagCheck;

//    public String getFlagCheck() {
//		return flagCheck;
//	}
//	public void setFlagCheck(String flagCheck) {
//		this.flagCheck = flagCheck;
//	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFlagNote() {
		return flagNote;
	}
	public void setFlagNote(String flagNote) {
		this.flagNote = flagNote;
	}
	public Date getDateEntry() {
		return dateEntry;
	}
	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}
	public Integer getRownoParent() {
		return rownoParent;
	}
	public void setRownoParent(Integer rownoParent) {
		this.rownoParent = rownoParent;
	}
	public String getPkRecordrow(){
        return this.pkRecordrow;
    }
    public void setPkRecordrow(String pkRecordrow){
        this.pkRecordrow = pkRecordrow;
    }

    public String getPkRecord(){
        return this.pkRecord;
    }
    public void setPkRecord(String pkRecord){
        this.pkRecord = pkRecord;
    }

    public Integer getRowno(){
        return this.rowno;
    }
    public void setRowno(Integer rowno){
        this.rowno = rowno;
    }

    public BigDecimal getPageNo(){
        return this.pageNo;
    }
    public void setPageNo(BigDecimal pageNo){
        this.pageNo = pageNo;
    }

    public Date getDateSign(){
        return this.dateSign;
    }
    public void setDateSign(Date dateSign){
        this.dateSign = dateSign;
    }

    public String getFlagSign(){
        return this.flagSign;
    }
    public void setFlagSign(String flagSign){
        this.flagSign = flagSign;
    }

    public String getPkEmp(){
        return this.pkEmp;
    }
    public void setPkEmp(String pkEmp){
        this.pkEmp = pkEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
}