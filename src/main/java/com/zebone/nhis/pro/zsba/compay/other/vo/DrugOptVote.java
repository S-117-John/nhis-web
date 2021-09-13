package com.zebone.nhis.pro.zsba.compay.other.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 投票明细表
 * @author 85102
 *
 */
@Table(value="DRUG_OPT_VOTE")
public class DrugOptVote {
	
	/**  选票主键 */
	@PK
	@Field(value="PK_VOTE",id=KeyId.UUID)
    private String pkVote;

    /**  选中标志*/
	@Field(value="FLAG_PASS")
    public String flagPass;

    /**  药品主键 */
	@Field(value="PK_PD")
    private String pkPd;
	
	/** 人员主键 */
	@Field(value="PK_EMP")
    private String pkEmp ;

    /** 人员编码 */
	@Field(value="CODE_EMP")
    private String codeEmp ;

    /** 人员名称 */
	@Field(value="NAME_EMP")
    private String nameEmp ;

    /** 电话 */
	@Field(value="TEL_NO")
    private String telNo ;

    /** 打印标志 */
	@Field(value="FLAG_PRINT")
    private String FlagPrint ;

	/** 创建时间 */
	@Field(value="CREATE_TIME")
    private Date createTime;

    /** 删除标志 */
	@Field(value="DEL_FLAG")
    private String delFlag;

    /** 二次投票标志 */
	@Field(value="FLAG_SECOND")
    private String flagSecond;
	
	public String getPkVote() {
		return pkVote;
	}

	public void setPkVote(String pkVote) {
		this.pkVote = pkVote;
	}

	public String getFlagPass() {
		return flagPass;
	}

	public void setFlagPass(String flagPass) {
		this.flagPass = flagPass;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getFlagPrint() {
		return FlagPrint;
	}

	public void setFlagPrint(String flagPrint) {
		FlagPrint = flagPrint;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getFlagSecond() {
		return flagSecond;
	}

	public void setFlagSecond(String flagSecond) {
		this.flagSecond = flagSecond;
	}

	
}
