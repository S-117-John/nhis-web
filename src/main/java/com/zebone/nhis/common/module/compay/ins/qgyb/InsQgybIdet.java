package com.zebone.nhis.common.module.compay.ins.qgyb;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "Ins_Qgyb_Idet")
public class InsQgybIdet extends BaseModule{
	
	@PK
	@Field(value = "pk_idet", id = KeyId.UUID)
	private String pkIdet;
	
	//医保患者主键
	@Field("pk_insupi")
	@JsonProperty("pk_insupi")
	private String pkInsupi;
    // 人员身份类别
	@Field("psn_idet_type")
	@JsonProperty("psn_idet_type")
	private String psnIdetType ;
    // 人员类别等级
	@Field("psn_type_lv")
	@JsonProperty("psn_type_lv")
	private String psnTypeLv ;
    // 备注
	@Field("memo")
	private String memo ;
	@Field("begntime")
	private Date begntime ;
	@Field("endtime")
	private Date endtime ;
	public String getPsnIdetType() {
		return psnIdetType;
	}
	public void setPsnIdetType(String psnIdetType) {
		this.psnIdetType = psnIdetType;
	}
	public String getPsnTypeLv() {
		return psnTypeLv;
	}
	public void setPsnTypeLv(String psnTypeLv) {
		this.psnTypeLv = psnTypeLv;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPkIdet() {
		return pkIdet;
	}
	public void setPkIdet(String pkIdet) {
		this.pkIdet = pkIdet;
	}
	public Date getBegntime() {
		return begntime;
	}
	public void setBegntime(Date begntime) {
		this.begntime = begntime;
	}
	public Date getEndtime() {
		return endtime;
	}
	public void setEndtime(Date endtime) {
		this.endtime = endtime;
	}
	public String getPkInsupi() {
		return pkInsupi;
	}
	public void setPkInsupi(String pkInsupi) {
		this.pkInsupi = pkInsupi;
	}
    
}
