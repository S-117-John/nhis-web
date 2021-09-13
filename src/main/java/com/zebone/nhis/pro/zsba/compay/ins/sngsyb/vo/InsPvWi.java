package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 外部医保-省内工伤医保就诊登记
 * 
 * 1.入院登记成功后，取业务信息后生成记录，status=1；
 * 2.出院登记成功后，取业务信息后更新记录，status=5；
 * 3.出院结算成功后，取业务信息后更新记录，status=11；
 * 4.其他操作只更新status为对应的值。
 * 
 * @author lipz
 *
 */
@Table(value="ins_pv_wi")
public class InsPvWi extends BaseModule {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -590168137254012310L;

	@PK
	@Field(value="pk_inspvwi",id=KeyId.UUID)
	private String pkInspvwi;//	主键
	
	@Field(value="pk_org")
	private String pkOrg;//	所属机构

	@Field(value="pk_insu")
	private String pkInsu;//	PK_INSU - 医保主计划主键 
	

	@Field(value="pk_pi")
	private String pkPi;//		患者主键

	@Field(value="pk_pv")
	private String pkPv;//		就诊主键

	@Field(value="eu_pvtype")
	private String euPvtype;//	就诊类型: 1 门诊，2 急诊，3 住院，4 体检

	@Field(value="ins_type")
	private String insType;//	医保类型:3省内工伤医保

	@Field(value="aae140")
	private String aae140;//	险种编码,工伤：410

	@Field(value="aaz218")
	private String aaz218;//	就医登记号

	@Field(value="akb020")
	private String akb020;//	医疗机构编号
	
	@Field(value="akb021")
	private String akb021;//	定点医疗机构名称

	@Field(value="aka130")
	private String aka130;//	业务类型, 41：工伤门诊、42：工伤住院

	@Field(value="aaa027")
	private String aaa027;//	工伤保险中心编码

	@Field(value="aac001")
	private String aac001;//	电脑号

	@Field(value="aac003")
	private String aac003;//	患者姓名

	@Field(value="aac004")
	private String aac004;//	性别,1：男，2：女

	@Field(value="aac002")
	private String aac002;//	社会保障号码

	@Field(value="bka100")
	private String bka100;//	工伤保险卡号

	@Field(value="aac006")
	private String aac006;//	出生日期 ,格式：yyyyMMdd

	@Field(value="aae005")
	private String aae005;//	联系电话

	@Field(value="aab001")
	private String aab001;//	单位编码

	@Field(value="baa027")
	private String baa027;//	参保地统筹区

	@Field(value="bka008")
	private String bka008;//	单位名称

	@Field(value="bka004")
	private String bka004;//	人员类别

	@Field(value="bka005")
	private String bka005;//	行政职务级别

	@Field(value="bka006")
	private String bka006;//	待遇类别

	@Field(value="bka013")
	private String bka013;//	业务登记日期

	@Field(value="bka014")
	private String bka014;//	登记人工号
	
	@Field(value="bka015")
	private String bka015;//	登记人

	@Field(value="bka017")
	private String bka017;//	住院时间

	@Field(value="bka019")
	private String bka019;//	入院科室

	@Field(value="bka021")
	private String bka021;//	入院病区

	@Field(value="bka023")
	private String bka023;//	入院床位号

	@Field(value="bka025")
	private String bka025;//	住院号

	@Field(value="bka026")
	private String bka026;//	入院疾病诊断

	@Field(value="bka036")
	private String bka036;//	用卡标志

	@Field(value="bka042")
	private String bka042;//	工伤凭证号

	@Field(value="bka501")
	private String bka501;//	行政区域

	@Field(value="bka502")
	private String bka502;//	医院级别
	
	@Field(value="bka503")
	private String bka503;//	医师编码

	@Field(value="bka030")
	private String bka030;//	住院天数

	@Field(value="bka031")
	private String bka031;//	出院疾病诊断

	@Field(value="bka032")
	private String bka032;//	出院日期

	@Field(value="bkf002")
	private String bkf002;//	入院方式

	@Field(value="bkf003")
	private String bkf003;//	入院情况

	@Field(value="bkf004")
	private String bkf004;//	出院转归情况

	@Field(value="bka033")
	private String bka033;//	结束人工号

	@Field(value="bka034")
	private String bka034;//	结束人

	@Field(value="bka045")
	private String bka045;//	结算日期

	@Field(value="bka046")
	private String bka046;//	结算人工号

	@Field(value="bka047")
	private String bka047;//	结算人

	@Field(value="bka891")
	private String bka891;//	结算标识

	@Field(value="bka039")
	private String bka039;//	完成标志
	
	@Field(value="aac013")
	private String aac013;//	用工形式
	
	@Field(value="aae030")
	private String aae030;//	开始日期
	
	@Field(value="aae031")
	private String aae031;//	结束日期

	/*
	 * 状态标志
	 * 
	 *  1入院登记成功，2入院登记失败；3资料维护成功，4资料维护失败；
        5出院登记成功，6出院登记失败；7取消出院登记成功，
        8取消出院登记失败；9取消入院登记成功，
        10取消入院登记失败；11结算成功，
        12结算失败；13取消结算成功，14取消结算失败；15跨月取消结算成功，
        16跨月取消结算失败
	 */
	@Field(value="status")
	private String status;

	public String getPkInspvwi() {
		return pkInspvwi;
	}

	public void setPkInspvwi(String pkInspvwi) {
		this.pkInspvwi = pkInspvwi;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getInsType() {
		return insType;
	}

	public void setInsType(String insType) {
		this.insType = insType;
	}

	public String getAae140() {
		return aae140;
	}

	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}

	public String getAaz218() {
		return aaz218;
	}

	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}

	public String getAkb020() {
		return akb020;
	}

	public void setAkb020(String akb020) {
		this.akb020 = akb020;
	}

	public String getAkb021() {
		return akb021;
	}

	public void setAkb021(String akb021) {
		this.akb021 = akb021;
	}

	public String getAka130() {
		return aka130;
	}

	public void setAka130(String aka130) {
		this.aka130 = aka130;
	}

	public String getAaa027() {
		return aaa027;
	}

	public void setAaa027(String aaa027) {
		this.aaa027 = aaa027;
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

	public String getAac002() {
		return aac002;
	}

	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}

	public String getBka100() {
		return bka100;
	}

	public void setBka100(String bka100) {
		this.bka100 = bka100;
	}

	public String getAac006() {
		return aac006;
	}

	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}

	public String getAae005() {
		return aae005;
	}

	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}

	public String getAab001() {
		return aab001;
	}

	public void setAab001(String aab001) {
		this.aab001 = aab001;
	}

	public String getBaa027() {
		return baa027;
	}

	public void setBaa027(String baa027) {
		this.baa027 = baa027;
	}

	public String getBka008() {
		return bka008;
	}

	public void setBka008(String bka008) {
		this.bka008 = bka008;
	}

	public String getBka004() {
		return bka004;
	}

	public void setBka004(String bka004) {
		this.bka004 = bka004;
	}

	public String getBka005() {
		return bka005;
	}

	public void setBka005(String bka005) {
		this.bka005 = bka005;
	}

	public String getBka006() {
		return bka006;
	}

	public void setBka006(String bka006) {
		this.bka006 = bka006;
	}

	public String getBka013() {
		return bka013;
	}

	public void setBka013(String bka013) {
		this.bka013 = bka013;
	}

	public String getBka014() {
		return bka014;
	}

	public void setBka014(String bka014) {
		this.bka014 = bka014;
	}

	public String getBka015() {
		return bka015;
	}

	public void setBka015(String bka015) {
		this.bka015 = bka015;
	}

	public String getBka017() {
		return bka017;
	}

	public void setBka017(String bka017) {
		this.bka017 = bka017;
	}

	public String getBka019() {
		return bka019;
	}

	public void setBka019(String bka019) {
		this.bka019 = bka019;
	}

	public String getBka021() {
		return bka021;
	}

	public void setBka021(String bka021) {
		this.bka021 = bka021;
	}

	public String getBka023() {
		return bka023;
	}

	public void setBka023(String bka023) {
		this.bka023 = bka023;
	}

	public String getBka025() {
		return bka025;
	}

	public void setBka025(String bka025) {
		this.bka025 = bka025;
	}

	public String getBka026() {
		return bka026;
	}

	public void setBka026(String bka026) {
		this.bka026 = bka026;
	}

	public String getBka036() {
		return bka036;
	}

	public void setBka036(String bka036) {
		this.bka036 = bka036;
	}

	public String getBka042() {
		return bka042;
	}

	public void setBka042(String bka042) {
		this.bka042 = bka042;
	}

	public String getBka501() {
		return bka501;
	}

	public void setBka501(String bka501) {
		this.bka501 = bka501;
	}

	public String getBka502() {
		return bka502;
	}

	public void setBka502(String bka502) {
		this.bka502 = bka502;
	}

	public String getBka503() {
		return bka503;
	}

	public void setBka503(String bka503) {
		this.bka503 = bka503;
	}

	public String getBka030() {
		return bka030;
	}

	public void setBka030(String bka030) {
		this.bka030 = bka030;
	}

	public String getBka031() {
		return bka031;
	}

	public void setBka031(String bka031) {
		this.bka031 = bka031;
	}

	public String getBka032() {
		return bka032;
	}

	public void setBka032(String bka032) {
		this.bka032 = bka032;
	}

	public String getBkf002() {
		return bkf002;
	}

	public void setBkf002(String bkf002) {
		this.bkf002 = bkf002;
	}

	public String getBkf003() {
		return bkf003;
	}

	public void setBkf003(String bkf003) {
		this.bkf003 = bkf003;
	}

	public String getBkf004() {
		return bkf004;
	}

	public void setBkf004(String bkf004) {
		this.bkf004 = bkf004;
	}

	public String getBka033() {
		return bka033;
	}

	public void setBka033(String bka033) {
		this.bka033 = bka033;
	}

	public String getBka034() {
		return bka034;
	}

	public void setBka034(String bka034) {
		this.bka034 = bka034;
	}

	public String getBka045() {
		return bka045;
	}

	public void setBka045(String bka045) {
		this.bka045 = bka045;
	}

	public String getBka046() {
		return bka046;
	}

	public void setBka046(String bka046) {
		this.bka046 = bka046;
	}

	public String getBka047() {
		return bka047;
	}

	public void setBka047(String bka047) {
		this.bka047 = bka047;
	}

	public String getBka891() {
		return bka891;
	}

	public void setBka891(String bka891) {
		this.bka891 = bka891;
	}

	public String getBka039() {
		return bka039;
	}

	public void setBka039(String bka039) {
		this.bka039 = bka039;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAac013() {
		return aac013;
	}

	public void setAac013(String aac013) {
		this.aac013 = aac013;
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
	   
}
