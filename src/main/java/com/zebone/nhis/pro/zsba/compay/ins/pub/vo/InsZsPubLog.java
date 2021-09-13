package com.zebone.nhis.pro.zsba.compay.ins.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_log
 *
 * @zrj 2020-011-32 10:42:10
 */
@Table(value="INS_LOG")
public class InsZsPubLog extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4453394141463354442L;

	@PK
	@Field(value="PK_INSLOG",id=KeyId.UUID)
    private String pkInslog;

	//0 就诊登记，1 取消登记，2 中途结算，3 出院结算，4 取消结算
	@Field(value="EU_TYPE")
    private String euType;
	
	@Field(value="DATE_HAP")
    private Date dateHap;
	
	/**患者编码*/
	@Field(value="CODE_PI")
    private String codePi;
	
	@Field(value="NAME_PI")
    private String namePi;
	
	/**
	 * 备注
	 */
	@Field(value="MEMO")
    private String memo;

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkInslog() {
		return pkInslog;
	}

	public void setPkInslog(String pkInslog) {
		this.pkInslog = pkInslog;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public Date getDateHap() {
		return dateHap;
	}

	public void setDateHap(Date dateHap) {
		this.dateHap = dateHap;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
	
}