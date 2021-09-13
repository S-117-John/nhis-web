package com.zebone.nhis.pro.zsba.compay.ins.ksyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_st_ksyb - ins_st_ksyb 
 *
 * @since 2017-12-13 10:42:10
 */
@Table(value="INS_ST_KSYB")
public class InsZsKsybStKsyb extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6321505174980616732L;

	/** 主键 */
	@PK
	@Field(value="PK_INSST",id=KeyId.UUID)
    private String pkInsst;

	/** 医院编号 */
	@Field(value="YYBH")
    private String yybh;
	
	/** 医保类别 */
	@Field(value="PK_HP")
    private String pkHp;
	
	/** 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;
	
	/** 就诊主键 */
	@Field(value="PK_PV")
    private String pkPv;
	
	/** 结算主键 */
	@Field(value="PK_SETTLE")
    private String pkSettle;
	
	/** 就诊类型 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/** 结算交易流水号 */
	@Field(value="TRANSID0304")
    private String transid0304;
	
	/** 结算回退交易流水号 */
	@Field(value="TRANSID0305")
    private String transid0305;

	/**
	 * 行政区划代码（就医地）
	 */
	@Field(value="AAB299")
    private String aab299;

	/**
	 * 就医地社保分支机构代码
	 */
	@Field(value="YAB600")
    private String yab600;
	
	/**
	 * 医疗机构执业许可证登记号
	 */
	@Field(value="AKB026")
    private String akb026;
	
	/**
	 * 医疗服务机构名称
	 */
	@Field(value="AKB021")
    private String akb021;
	
	/** 就诊登记号*/
	@Field(value="YKC700")
    private String ykc700;
	
	/** 行政区划代码（参保地）*/
	@Field(value="AAB301")
    private String aab301;

	/** 参保地社保分支机构代码*/
	@Field(value="YAB060")
    private String yab060;
	
	/**
	 * 社会保障号码
	 */
	@Field(value="AAC002")
    private String aac002;
	
	/**
	 * 证件类型
	 */
	@Field(value="AAC043")
    private String aac043;
	
	/**
	 * 证件号码
	 */
	@Field(value="AAC044")
    private String aac044;
    
	/**
	 * 结算类型
	 */
	@Field(value="ykc675")
    private String ykc675;
	
	/**
	 * 总费用
	 */
	@Field(value="AKC264")
    private BigDecimal akc264;
	
	/**
	 * 经办人
	 */
	@Field(value="AAE011")
    private String aae011;
	
	/**
	 * 经办时间
	 */
	@Field(value="AAE036")
    private Date aae036;
	
	/**
	 * 就诊结算时间
	 */
	@Field(value="AKC194")
    private Date akc194;
	
	/**
	 * 医院结算业务序列号
	 */
	@Field(value="YZZ021")
    private String yzz021;
	
	/**
	 * 备注
	 */
	@Field(value="AAE013")
    private String aae013;
	
	/**
	 * 错误代码
	 */
	@Field(value="ERRORCODE")
    private Integer errorcode;
	
	/**
	 * 错误信息
	 */
	@Field(value="ERRORMSG")
    private String errormsg;
	
	/**
	 * 结算流水号
	 */
	@Field(value="AAZ216")
    private String aaz216;
	
	/**
	 * 对账日期
	 */
	@Field(value="YKC706")
    private Date ykc706;
	
	/**
	 * 起付标准
	 */
	@Field(value="AKA151")
    private BigDecimal aka151;
	
	/**
	 * 经办机构支付总额
	 */
	@Field(value="AKE149")
    private BigDecimal ake149;
	
	/**
	 * 基本医疗保险统筹基金支付金额
	 */
	@Field(value="AKE039")
    private BigDecimal ake039;
	
	/**
	 * 公务员医疗补助基金
	 */
	@Field(value="AKE035")
    private BigDecimal ake035;
	
	/**
	 * 补充医疗保险基金
	 */
	@Field(value="AKE026")
    private BigDecimal ake026;
	
	/**
	 * 大病补充医疗保险基金（大额医疗补助基金）
	 */
	@Field(value="AAE045")
    private BigDecimal aae045;
	
    /** 伤残人员医疗保障基金 */
	@Field(value="AKE032")
    private BigDecimal ake032;
	
	/**
	 * 民政补助基金
	 */
	@Field(value="AKE181")
    private BigDecimal ake181;
	
	/**
	 * 其他基金支付
	 */
	@Field(value="AKE173")
    private BigDecimal ake173;
	
	/**
	 * 个人现金支付
	 */
	@Field(value="AKB067")
    private BigDecimal akb067;
	
	/**
	 * 个人账户支付
	 */
	@Field(value="AKE038")
    private BigDecimal ake038;
	
    /** 个人账户余额 */
	@Field(value="AAE240")
    private BigDecimal aae240;

    /** 姓名 */
	@Field(value="AAC003")
    private String aac003;
	
    /** 性别 */
	@Field(value="AAC004")
    private String aac004;

	/** 出生日期 */
	@Field(value="AAC006")
    private Date aac006;

	/** 单位编号 */
	@Field(value="AAB001")
    private String aab001;

	/** 单位名称 */
	@Field(value="AAB004")
    private String aab004;

    /** 单位类型 */
	@Field(value="AAB019")
    private String aab019;
	
    /** 经济类型 */
	@Field(value="AAB020")
    private String aab020;

    /** 隶属关系 */
	@Field(value="AAB021")
    private String aab021;

	/** 人员类别 */
	@Field(value="YKC021")
    private String ykc021;

	/** 险种类型 */
	@Field(value="AAE140")
    private String aae140;

	/** 全额垫付标志 */
	@Field(value="AKE105")
    private String ake105;
	
	/** 输出附加信息1 */
	@Field(value="AKC070")
    private String akc070;

	/** 输出附加信息2 */
	@Field(value="AKC071")
    private String akc071;

	/** 输出附加信息3 */
	@Field(value="AKC072")
    private String akc072;

	/** 统筹基金支付范围内费用 */
	@Field(value="AKE171")
    private BigDecimal ake171;

	/** 个人自费费用 */
	@Field(value="AKC253")
    private BigDecimal akc253;

	/** 月度结算清分明细交易流水号 */
	@Field(value="TRANSID6527")
    private String transid6527;

    /** 结算年度 */
	@Field(value="YZZ060")
    private String yzz060;

    /** 结算月份 */
	@Field(value="YZZ061")
    private String yzz061;

	/**开始行数 */
	@Field(value="STARTROW")
    private Integer startrow;

    /** 总行数 */
	@Field(value="TOTALROW")
    private Integer totalrow;

	 /** 跨省外来就医月度结算清分确认交易流水号 */
	@Field(value="TRANSID6521")
    private String transid6521;

	 /** 跨省外来月度结算清分确认结果回退交易流水号 */
	@Field(value="TRANSID6522")
    private String transid6522;

	 /** 状态标志 */
	@Field(value="STATUS")
    private String status;

    /** 月结确认标志 */
	@Field(value="YKC707")
    private String ykc707;
	
	/**
	 * 修改时间
	 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;
	
	public String getPkInsst() {
		return pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getYybh() {
		return yybh;
	}

	public void setYybh(String yybh) {
		this.yybh = yybh;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
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

	public String getTransid0304() {
		return transid0304;
	}

	public void setTransid0304(String transid0304) {
		this.transid0304 = transid0304;
	}

	public String getTransid0305() {
		return transid0305;
	}

	public void setTransid0305(String transid0305) {
		this.transid0305 = transid0305;
	}

	public String getAab299() {
		return aab299;
	}

	public void setAab299(String aab299) {
		this.aab299 = aab299;
	}

	public String getYab600() {
		return yab600;
	}

	public void setYab600(String yab600) {
		this.yab600 = yab600;
	}

	public String getAkb026() {
		return akb026;
	}

	public void setAkb026(String akb026) {
		this.akb026 = akb026;
	}

	public String getAkb021() {
		return akb021;
	}

	public void setAkb021(String akb021) {
		this.akb021 = akb021;
	}

	public String getYkc700() {
		return ykc700;
	}

	public void setYkc700(String ykc700) {
		this.ykc700 = ykc700;
	}

	public String getAab301() {
		return aab301;
	}

	public void setAab301(String aab301) {
		this.aab301 = aab301;
	}

	public String getYab060() {
		return yab060;
	}

	public void setYab060(String yab060) {
		this.yab060 = yab060;
	}

	public String getAac002() {
		return aac002;
	}

	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}

	public String getAac043() {
		return aac043;
	}

	public void setAac043(String aac043) {
		this.aac043 = aac043;
	}

	public String getAac044() {
		return aac044;
	}

	public void setAac044(String aac044) {
		this.aac044 = aac044;
	}

	public String getYkc675() {
		return ykc675;
	}

	public void setYkc675(String ykc675) {
		this.ykc675 = ykc675;
	}

	public BigDecimal getAkc264() {
		return akc264;
	}

	public void setAkc264(BigDecimal akc264) {
		this.akc264 = akc264;
	}

	public String getAae011() {
		return aae011;
	}

	public void setAae011(String aae011) {
		this.aae011 = aae011;
	}

	public Date getAae036() {
		return aae036;
	}

	public void setAae036(Date aae036) {
		this.aae036 = aae036;
	}

	public Date getAkc194() {
		return akc194;
	}

	public void setAkc194(Date akc194) {
		this.akc194 = akc194;
	}

	public String getYzz021() {
		return yzz021;
	}

	public void setYzz021(String yzz021) {
		this.yzz021 = yzz021;
	}

	public String getAae013() {
		return aae013;
	}

	public void setAae013(String aae013) {
		this.aae013 = aae013;
	}

	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getAaz216() {
		return aaz216;
	}

	public void setAaz216(String aaz216) {
		this.aaz216 = aaz216;
	}

	public Date getYkc706() {
		return ykc706;
	}

	public void setYkc706(Date ykc706) {
		this.ykc706 = ykc706;
	}

	public BigDecimal getAka151() {
		return aka151;
	}

	public void setAka151(BigDecimal aka151) {
		this.aka151 = aka151;
	}

	public BigDecimal getAke149() {
		return ake149;
	}

	public void setAke149(BigDecimal ake149) {
		this.ake149 = ake149;
	}

	public BigDecimal getAke039() {
		return ake039;
	}

	public void setAke039(BigDecimal ake039) {
		this.ake039 = ake039;
	}

	public BigDecimal getAke035() {
		return ake035;
	}

	public void setAke035(BigDecimal ake035) {
		this.ake035 = ake035;
	}

	public BigDecimal getAke026() {
		return ake026;
	}

	public void setAke026(BigDecimal ake026) {
		this.ake026 = ake026;
	}

	public BigDecimal getAae045() {
		return aae045;
	}

	public void setAae045(BigDecimal aae045) {
		this.aae045 = aae045;
	}

	public BigDecimal getAke032() {
		return ake032;
	}

	public void setAke032(BigDecimal ake032) {
		this.ake032 = ake032;
	}

	public BigDecimal getAke181() {
		return ake181;
	}

	public void setAke181(BigDecimal ake181) {
		this.ake181 = ake181;
	}

	public BigDecimal getAke173() {
		return ake173;
	}

	public void setAke173(BigDecimal ake173) {
		this.ake173 = ake173;
	}

	public BigDecimal getAkb067() {
		return akb067;
	}

	public void setAkb067(BigDecimal akb067) {
		this.akb067 = akb067;
	}

	public BigDecimal getAke038() {
		return ake038;
	}

	public void setAke038(BigDecimal ake038) {
		this.ake038 = ake038;
	}

	public BigDecimal getAae240() {
		return aae240;
	}

	public void setAae240(BigDecimal aae240) {
		this.aae240 = aae240;
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

	public Date getAac006() {
		return aac006;
	}

	public void setAac006(Date aac006) {
		this.aac006 = aac006;
	}

	public String getAab001() {
		return aab001;
	}

	public void setAab001(String aab001) {
		this.aab001 = aab001;
	}

	public String getAab004() {
		return aab004;
	}

	public void setAab004(String aab004) {
		this.aab004 = aab004;
	}

	public String getAab019() {
		return aab019;
	}

	public void setAab019(String aab019) {
		this.aab019 = aab019;
	}

	public String getAab020() {
		return aab020;
	}

	public void setAab020(String aab020) {
		this.aab020 = aab020;
	}

	public String getAab021() {
		return aab021;
	}

	public void setAab021(String aab021) {
		this.aab021 = aab021;
	}

	public String getYkc021() {
		return ykc021;
	}

	public void setYkc021(String ykc021) {
		this.ykc021 = ykc021;
	}

	public String getAae140() {
		return aae140;
	}

	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}

	public String getAke105() {
		return ake105;
	}

	public void setAke105(String ake105) {
		this.ake105 = ake105;
	}

	public String getAkc070() {
		return akc070;
	}

	public void setAkc070(String akc070) {
		this.akc070 = akc070;
	}

	public String getAkc071() {
		return akc071;
	}

	public void setAkc071(String akc071) {
		this.akc071 = akc071;
	}

	public String getAkc072() {
		return akc072;
	}

	public void setAkc072(String akc072) {
		this.akc072 = akc072;
	}

	public BigDecimal getAke171() {
		return ake171;
	}

	public void setAke171(BigDecimal ake171) {
		this.ake171 = ake171;
	}

	public BigDecimal getAkc253() {
		return akc253;
	}

	public void setAkc253(BigDecimal akc253) {
		this.akc253 = akc253;
	}

	public String getTransid6527() {
		return transid6527;
	}

	public void setTransid6527(String transid6527) {
		this.transid6527 = transid6527;
	}

	public String getYzz060() {
		return yzz060;
	}

	public void setYzz060(String yzz060) {
		this.yzz060 = yzz060;
	}

	public String getYzz061() {
		return yzz061;
	}

	public void setYzz061(String yzz061) {
		this.yzz061 = yzz061;
	}

	public Integer getStartrow() {
		return startrow;
	}

	public void setStartrow(Integer startrow) {
		this.startrow = startrow;
	}

	public Integer getTotalrow() {
		return totalrow;
	}

	public void setTotalrow(Integer totalrow) {
		this.totalrow = totalrow;
	}

	public String getTransid6521() {
		return transid6521;
	}

	public void setTransid6521(String transid6521) {
		this.transid6521 = transid6521;
	}

	public String getTransid6522() {
		return transid6522;
	}

	public void setTransid6522(String transid6522) {
		this.transid6522 = transid6522;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getYkc707() {
		return ykc707;
	}

	public void setYkc707(String ykc707) {
		this.ykc707 = ykc707;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
}