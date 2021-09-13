package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;

import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 外部医保-省内工伤医保住院结算单
 * @author Administrator
 *
 */
@Table(value="ins_st_wi_list")
public class InsStWiList extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7939651593337140822L;

	@PK
	@Field(value="pk_insstwilist",id=KeyId.UUID)
	private String pkInsstwilist;//主键
	
	@Field(value="pk_org")
	private String pkOrg;//所属机构
	
	@Field(value="pk_insu")
	private String pkInsu;//医保主计划主键
	
	@Field(value="pk_pi")
	private String pkPi ;//患者主键
	
	@Field(value="pk_pv")
	private String pkPv ;//就诊主键
	
	@Field(value="pk_settle")
	private String pkSettle;//结算主键
	
	@Field(value="eu_pvtype")
	private String euPvtype;//就诊类型：1 门诊，2 急诊，3 住院，4 体检
	
	@Field(value="akb021")
	private String akb021;//医疗机构名称
	
	@Field(value="bkc110")
	private String bkc110;//医院等级
	
	@Field(value="aaz218")
	private String aaz218;//工伤保险就医登记号
	
	@Field(value="bkc119")
	private String bkc119;//是否政府办基层医疗机构
	
	@Field(value="bka989")
	private String bka989;//先行支付
	
	@Field(value="aac003")
	private String aac003;//姓名
	
	@Field(value="bka004")
	private String bka004;//人员类别
	
	@Field(value="aac004")
	private String aac004;//性别
	
	@Field(value="aac001")
	private String aac001;//个人电脑号
	
	@Field(value="bka100")
	private String bka100;//社保卡号
	
	@Field(value="bka006")
	private String bka006;//待遇类别
	
	@Field(value="aka130")
	private String aka130;//业务类别
	
	@Field(value="bka045")
	private String bka045;//结算时间
	
	@Field(value="bka020")
	private String bka020;//科别
	
	@Field(value="bka025")
	private String bka025;//住院号
	
	@Field(value="bka017")
	private String bka017;//住院时间
	
	@Field(value="bka032")
	private String bka032;//出院时间
	
	@Field(value="bka030")
	private String bka030;//住院天数
	
	@Field(value="bka979")
	private String bka979;//总费用
	
	@Field(value="bka811")
	private String bka811;//工伤保险支付金额
	
	@Field(value="bka812")
	private String bka812;//个人支付金额
	
	@Field(value="bka814")
	private String bka814;//基本工伤保险统筹已支付
	
	@Field(value="bka815")
	private String bka815;//补充保险自付累计
	
	@Field(value="bka816")
	private String bka816;//补充保险已支付
	
	@Field(value="aae011")
	private String aae011;//制单人
	
	private FundList120206 fundInfo;

	public String getPkInsstwilist() {
		return pkInsstwilist;
	}

	public void setPkInsstwilist(String pkInsstwilist) {
		this.pkInsstwilist = pkInsstwilist;
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

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getAkb021() {
		return akb021;
	}

	public void setAkb021(String akb021) {
		this.akb021 = akb021;
	}

	public String getBkc110() {
		return bkc110;
	}

	public void setBkc110(String bkc110) {
		this.bkc110 = bkc110;
	}

	public String getAaz218() {
		return aaz218;
	}

	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}

	public String getBkc119() {
		return bkc119;
	}

	public void setBkc119(String bkc119) {
		this.bkc119 = bkc119;
	}

	public String getBka989() {
		return bka989;
	}

	public void setBka989(String bka989) {
		this.bka989 = bka989;
	}

	public String getAac003() {
		return aac003;
	}

	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}

	public String getBka004() {
		return bka004;
	}

	public void setBka004(String bka004) {
		this.bka004 = bka004;
	}

	public String getAac004() {
		return aac004;
	}

	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}

	public String getAac001() {
		return aac001;
	}

	public void setAac001(String aac001) {
		this.aac001 = aac001;
	}

	public String getBka100() {
		return bka100;
	}

	public void setBka100(String bka100) {
		this.bka100 = bka100;
	}

	public String getBka006() {
		return bka006;
	}

	public void setBka006(String bka006) {
		this.bka006 = bka006;
	}

	public String getAka130() {
		return aka130;
	}

	public void setAka130(String aka130) {
		this.aka130 = aka130;
	}

	public String getBka045() {
		return bka045;
	}

	public void setBka045(String bka045) {
		this.bka045 = bka045;
	}

	public String getBka020() {
		return bka020;
	}

	public void setBka020(String bka020) {
		this.bka020 = bka020;
	}

	public String getBka025() {
		return bka025;
	}

	public void setBka025(String bka025) {
		this.bka025 = bka025;
	}

	public String getBka017() {
		return bka017;
	}

	public void setBka017(String bka017) {
		this.bka017 = bka017;
	}

	public String getBka032() {
		return bka032;
	}

	public void setBka032(String bka032) {
		this.bka032 = bka032;
	}

	public String getBka030() {
		return bka030;
	}

	public void setBka030(String bka030) {
		this.bka030 = bka030;
	}

	public String getBka979() {
		return bka979;
	}

	public void setBka979(String bka979) {
		this.bka979 = bka979;
	}

	public String getBka811() {
		return bka811;
	}

	public void setBka811(String bka811) {
		this.bka811 = bka811;
	}

	public String getBka812() {
		return bka812;
	}

	public void setBka812(String bka812) {
		this.bka812 = bka812;
	}

	public String getBka814() {
		return bka814;
	}

	public void setBka814(String bka814) {
		this.bka814 = bka814;
	}

	public String getBka815() {
		return bka815;
	}

	public void setBka815(String bka815) {
		this.bka815 = bka815;
	}

	public String getBka816() {
		return bka816;
	}

	public void setBka816(String bka816) {
		this.bka816 = bka816;
	}

	public String getAae011() {
		return aae011;
	}

	public void setAae011(String aae011) {
		this.aae011 = aae011;
	}

	public FundList120206 getFundInfo() {
		return fundInfo;
	}

	public void setFundInfo(FundList120206 fundInfo) {
		this.fundInfo = fundInfo;
	}
}
