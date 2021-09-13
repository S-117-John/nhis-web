package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "INS_SGSYB_VISIT")
public class InsSgsybVisit extends BaseModule{

	@PK
	@Field(value = "PK_VISIT", id = KeyId.UUID)
	private String pkVisit;

	@Field("pk_hp")
	private String pkHp;
	@Field("pk_pv")
	private String pkPv;
	@Field("pk_pi")
	private String pkPi;
	
	@Field("aaz218")
	private String aaz218;//就医登记号

	@Field("aac001")
	private String aac001;
	@Field("aac003")
	private String aac003;
	@Field("aac004")
	private String aac004;
	@Field("bka004")
	private String bka004;
	@Field("aac013")
	private String aac013;
	@Field("bka005")
	private String bka005;
	@Field("aac002")
	private String aac002;
	@Field("aae005")
	private String aae005;
	@Field("aac006")
	private String aac006;
	@Field("baa027")
	private String baa027;
	@Field("aab001")
	private String aab001;
	@Field("bka008")
	private String bka008;
	@Field("aae140")
	private String aae140;
	@Field("aaa157")
	private String aaa157;
	@Field("aad006")
	private String aad006;
	@Field("aac031")
	private String aac031;
	@Field("bka006")
	private String bka006;
	@Field("bka345")
	private String bka345;
	@Field("aka130")
	private String aka130;
	
	@Field("bka026")
	private String bka026;
	@Field("aka121")
	private String aka121;
	@Field("aae030")
	private String aae030;
	@Field("aae031")
	private String aae031;
	@Field("bka042")
	private String bka042;
	public String getPkVisit() {
		return pkVisit;
	}
	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getAaz218() {
		return aaz218;
	}
	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}
	public String getAac001() {
		return aac001;
	}
	public void setAac001(String aac001) {
		this.aac001 = aac001;
	}
	public String getAac003() {
		return aac003;
	}
	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}
	public String getAac004() {
		return aac004;
	}
	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}
	public String getBka004() {
		return bka004;
	}
	public void setBka004(String bka004) {
		this.bka004 = bka004;
	}
	public String getAac013() {
		return aac013;
	}
	public void setAac013(String aac013) {
		this.aac013 = aac013;
	}
	public String getBka005() {
		return bka005;
	}
	public void setBka005(String bka005) {
		this.bka005 = bka005;
	}
	public String getAac002() {
		return aac002;
	}
	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}
	public String getAae005() {
		return aae005;
	}
	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}
	public String getAac006() {
		return aac006;
	}
	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}
	public String getBaa027() {
		return baa027;
	}
	public void setBaa027(String baa027) {
		this.baa027 = baa027;
	}
	public String getAab001() {
		return aab001;
	}
	public void setAab001(String aab001) {
		this.aab001 = aab001;
	}
	public String getBka008() {
		return bka008;
	}
	public void setBka008(String bka008) {
		this.bka008 = bka008;
	}
	public String getAae140() {
		return aae140;
	}
	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}
	public String getAaa157() {
		return aaa157;
	}
	public void setAaa157(String aaa157) {
		this.aaa157 = aaa157;
	}
	public String getAad006() {
		return aad006;
	}
	public void setAad006(String aad006) {
		this.aad006 = aad006;
	}
	public String getAac031() {
		return aac031;
	}
	public void setAac031(String aac031) {
		this.aac031 = aac031;
	}
	public String getBka006() {
		return bka006;
	}
	public void setBka006(String bka006) {
		this.bka006 = bka006;
	}
	public String getBka345() {
		return bka345;
	}
	public void setBka345(String bka345) {
		this.bka345 = bka345;
	}
	public String getAka130() {
		return aka130;
	}
	public void setAka130(String aka130) {
		this.aka130 = aka130;
	}
	public String getBka026() {
		return bka026;
	}
	public void setBka026(String bka026) {
		this.bka026 = bka026;
	}
	public String getAka121() {
		return aka121;
	}
	public void setAka121(String aka121) {
		this.aka121 = aka121;
	}
	public String getAae030() {
		return aae030;
	}
	public void setAae030(String aae030) {
		this.aae030 = aae030;
	}
	public String getAae031() {
		return aae031;
	}
	public void setAae031(String aae031) {
		this.aae031 = aae031;
	}
	public String getBka042() {
		return bka042;
	}
	public void setBka042(String bka042) {
		this.bka042 = bka042;
	}

	
	
	
}
