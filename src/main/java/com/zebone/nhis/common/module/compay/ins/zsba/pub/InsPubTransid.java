package com.zebone.nhis.common.module.compay.ins.zsba.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_transid - 外部医保-费用明细录入交易流水号
 *
 * @since 2017-12-12 10:42:10
 */
@Table(value="INS_TRANSID")
public class InsPubTransid extends BaseModule  {

	@PK
	@Field(value="PK_INSTRANSID",id=KeyId.UUID)
    private String pkInstransid;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_INSPVOUT")
    private String pkInspvout;
	
	@Field(value="TRANSID0301")
	private String transid0301;

	@Field(value="TRANSID0302")
	private String transid0302;
	
	@Field(value="ERRORCODE")
	private int errorcode;
	
	@Field(value="ERRORMSG")
	private String errormsg;
	
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkInstransid() {
		return pkInstransid;
	}

	public void setPkInstransid(String pkInstransid) {
		this.pkInstransid = pkInstransid;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkInspvout() {
		return pkInspvout;
	}

	public void setPkInspvout(String pkInspvout) {
		this.pkInspvout = pkInspvout;
	}

	public String getTransid0301() {
		return transid0301;
	}

	public void setTransid0301(String transid0301) {
		this.transid0301 = transid0301;
	}

	public String getTransid0302() {
		return transid0302;
	}

	public void setTransid0302(String transid0302) {
		this.transid0302 = transid0302;
	}
	
	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}


}