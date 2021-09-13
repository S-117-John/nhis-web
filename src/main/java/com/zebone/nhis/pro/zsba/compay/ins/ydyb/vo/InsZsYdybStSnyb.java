package com.zebone.nhis.pro.zsba.compay.ins.ydyb.vo;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_st_snyb - ins_st_snyb
 *
 * @since 2017-12-13 10:42:10
 */
@Table(value="INS_ST_SNYB")
public class InsZsYdybStSnyb extends BaseModule  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1362972178249227803L;

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
	
	/** 结算原交易流水号 */
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
	 * 就诊结算日期
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
	 * 主要费用结算方式
	 */
	@Field(value="AKA105")
    private String aka105;
	
	/**
	 * 结算业务号
	 */
	@Field(value="YKC618")
    private String ykc618;
	
	/**
	 * 结算类型
	 */
	@Field(value="YKC675")
    private String ykc675;
	
	/**
	 * 医疗费总额
	 */
	@Field(value="AKC264")
    private BigDecimal akc264;

	/**
	 * 本次就诊政策范围外个人自费金额
	 */
	@Field(value="AKC253")
    private BigDecimal akc253;
	
	/**
	 * 本次就诊政策范围内个人自付金额
	 */
	@Field(value="AKC254")
    private BigDecimal akc254;
	
	/**
	 * 允许报销部分
	 */
	@Field(value="YKA319")
    private BigDecimal yka319;
	
	/**
	 * 个人支付金额
	 */
	@Field(value="YKC624")
    private BigDecimal ykc624;
	
	/**
	 * 本次就诊基本医疗保险统筹基金起付标准额
	 */
	@Field(value="AKA151")
    private BigDecimal aka151;
	
	/**
	 * 个人帐户支付金额合计
	 */
	@Field(value="AKB066")
    private BigDecimal akb066;
	
	/**
	 * 超报销限额自付金额
	 */
	@Field(value="YKC631")
    private BigDecimal ykc631;
	
	/**
	 * 统筹支付金额合计
	 */
	@Field(value="AKB068")
    private BigDecimal akb068;
	
	/**
	 * 基本医疗保险统筹基金支付金额
	 */
	@Field(value="AKE039")
    private BigDecimal ake039;
	
	/**
	 * 基本医疗统筹自付部分
	 */
	@Field(value="YKC627")
    private BigDecimal ykc627;
	
	/**
	 * 大病医疗统筹支付部分
	 */
	@Field(value="YKC630")
    private BigDecimal ykc630;
	
	/**
	 * 大病医疗统筹自付部分
	 */
	@Field(value="YKC629")
    private BigDecimal ykc629;
	
	/**
	 * 公务员医疗补助基金支付金额
	 */
	@Field(value="AKE035")
    private BigDecimal ake035;
	
	/**
	 * 公务员大病医疗补助部分
	 */
	@Field(value="YKC635")
    private BigDecimal ykc635;
	
	/**
	 * 公务员超限补助部分
	 */
	@Field(value="YKC636")
    private BigDecimal ykc636;
	
	/**
	 * 补充/补助保险等支付部分
	 */
	@Field(value="YKC637")
    private BigDecimal ykc637;
	
	/**
	 * 其他支付
	 */
	@Field(value="YKC639")
    private BigDecimal ykc639;
	
	/**
	 * 住院天数
	 */
	@Field(value="AKB063")
    private Integer akb063;
	
	/**
	 * 自费原因
	 */
	@Field(value="YKC666")
    private String ykc666;
	
    /** 本年度住院次数 */
	@Field(value="AKC200")
    private Integer akc200;

    /** 统筹累计已支付 */
	@Field(value="YKA430")
    private BigDecimal yka430;

    /** 补充/补助医疗累计已支付 */
	@Field(value="YKA431")
    private BigDecimal yka431;
	
    /** 公务员补助累计已支付 */
	@Field(value="YKA432")
    private BigDecimal yka432;

	/** 重大疾病/大病保险累计支付 */
	@Field(value="YKA433")
    private BigDecimal yka433;

	/** 其他累计支付 */
	@Field(value="YKA434")
    private BigDecimal yka434;

	/** 险种类型 */
	@Field(value="AAE140")
    private String aae140;

    /** 个人自负金额 */
	@Field(value="YZZ139")
    private BigDecimal yzz139;
	
    /** 公务员补助支付费用 */
	@Field(value="YKC640")
    private BigDecimal ykc640;

	/** 月度结算申报交易流水号 */
	@Field(value="TRANSID0521")
    private String transid0521;

	/** 月度结算申报回退原交易流水号 */
	@Field(value="TRANSID0522")
    private String transid0522;
	
	/** 结算申报业务号 */
	@Field(value="YZZ062")
    private String yzz062;

	/** 结算年度 */
	@Field(value="YZZ060")
    private String yzz060;

	/** 结算月份 */
	@Field(value="YZZ061")
    private String yzz061;

	/** 起始日期 */
	@Field(value="YZZ041")
    private Date yzz041;

	/** 截止日期 */
	@Field(value="YZZ042")
    private Date yzz042;

	/** 负责人 */
	@Field(value="YZZ134")
    private String yzz134;

    /** 复核人 */
	@Field(value="YZZ135")
    private String yzz135;

    /** 填表人 */
	@Field(value="YZZ136")
    private String yzz136;

	/**填报日期 */
	@Field(value="YZZ137")
    private Date yzz137;

    /** 联系电话 */
	@Field(value="YZZ138")
    private String yzz138;

	 /** 申报费用笔数 */
	@Field(value="YZZ063")
    private Integer yzz063;

	 /** 申报费用总额 */
	@Field(value="YZZ064")
    private BigDecimal yzz064;

	 /** 月度申报汇总表交易流水号 */
	@Field(value="TRANSID0534")
    private String transid0534;

    /** 月度申报汇总表回退交易流水号 */
	@Field(value="TRANSID0535")
    private String transid0535;
	
    /** 月度申报分险种汇总交易流水号 */
	@Field(value="TRANSID0536")
    private String transid0536;
	
    /** 合计参保地数量 */
	@Field(value="YZZ133")
    private Integer yzz133;
	
    /** 状态标志 */
	@Field(value="STATUS")
    private String status;
	
    /** 月度申报分险种汇总表回退交易流水号 */
	@Field(value="TRANSID0537")
    private String transid0537;
	
	/**
	 * 修改时间
	 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	/**
	 * 救助对象类型
	 */
	@Field(value="YKC751")
    private String ykc751;
	
	/**
	 * 医疗救助金额
	 */
	@Field(value="YKC641")
    private BigDecimal ykc641;
	
	/**
	 * 二次救助金额
	 */
	@Field(value="YKC642")
    private BigDecimal ykc642;
	
	/**
	 * 对账日期
	 */
	@Field(value="YKC706")
    private Date ykc706;
	
	/**
	 * 一至六级残疾军人等医疗补助
	 */
	@Field(value="YKC752")
    private BigDecimal ykc752;
	
	/**
	 * 本年度医疗救助累计金额
	 */
	@Field(value="YKC753")
    private BigDecimal ykc753;
	
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

	public String getAka105() {
		return aka105;
	}

	public void setAka105(String aka105) {
		this.aka105 = aka105;
	}

	public String getYkc618() {
		return ykc618;
	}

	public void setYkc618(String ykc618) {
		this.ykc618 = ykc618;
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

	public BigDecimal getAkc253() {
		return akc253;
	}

	public void setAkc253(BigDecimal akc253) {
		this.akc253 = akc253;
	}

	public BigDecimal getAkc254() {
		return akc254;
	}

	public void setAkc254(BigDecimal akc254) {
		this.akc254 = akc254;
	}

	public BigDecimal getYka319() {
		return yka319;
	}

	public void setYka319(BigDecimal yka319) {
		this.yka319 = yka319;
	}

	public BigDecimal getYkc624() {
		return ykc624;
	}

	public void setYkc624(BigDecimal ykc624) {
		this.ykc624 = ykc624;
	}

	public BigDecimal getAka151() {
		return aka151;
	}

	public void setAka151(BigDecimal aka151) {
		this.aka151 = aka151;
	}

	public BigDecimal getAkb066() {
		return akb066;
	}

	public void setAkb066(BigDecimal akb066) {
		this.akb066 = akb066;
	}

	public BigDecimal getYkc631() {
		return ykc631;
	}

	public void setYkc631(BigDecimal ykc631) {
		this.ykc631 = ykc631;
	}

	public BigDecimal getAkb068() {
		return akb068;
	}

	public void setAkb068(BigDecimal akb068) {
		this.akb068 = akb068;
	}

	public BigDecimal getAke039() {
		return ake039;
	}

	public void setAke039(BigDecimal ake039) {
		this.ake039 = ake039;
	}

	public BigDecimal getYkc627() {
		return ykc627;
	}

	public void setYkc627(BigDecimal ykc627) {
		this.ykc627 = ykc627;
	}

	public BigDecimal getYkc630() {
		return ykc630;
	}

	public void setYkc630(BigDecimal ykc630) {
		this.ykc630 = ykc630;
	}

	public BigDecimal getYkc629() {
		return ykc629;
	}

	public void setYkc629(BigDecimal ykc629) {
		this.ykc629 = ykc629;
	}

	public BigDecimal getAke035() {
		return ake035;
	}

	public void setAke035(BigDecimal ake035) {
		this.ake035 = ake035;
	}

	public BigDecimal getYkc635() {
		return ykc635;
	}

	public void setYkc635(BigDecimal ykc635) {
		this.ykc635 = ykc635;
	}

	public BigDecimal getYkc636() {
		return ykc636;
	}

	public void setYkc636(BigDecimal ykc636) {
		this.ykc636 = ykc636;
	}

	public BigDecimal getYkc637() {
		return ykc637;
	}

	public void setYkc637(BigDecimal ykc637) {
		this.ykc637 = ykc637;
	}

	public BigDecimal getYkc639() {
		return ykc639;
	}

	public void setYkc639(BigDecimal ykc639) {
		this.ykc639 = ykc639;
	}

	public Integer getAkb063() {
		return akb063;
	}

	public void setAkb063(Integer akb063) {
		this.akb063 = akb063;
	}

	public String getYkc666() {
		return ykc666;
	}

	public void setYkc666(String ykc666) {
		this.ykc666 = ykc666;
	}

	public Integer getAkc200() {
		return akc200;
	}

	public void setAkc200(Integer akc200) {
		this.akc200 = akc200;
	}

	public BigDecimal getYka430() {
		return yka430;
	}

	public void setYka430(BigDecimal yka430) {
		this.yka430 = yka430;
	}

	public BigDecimal getYka431() {
		return yka431;
	}

	public void setYka431(BigDecimal yka431) {
		this.yka431 = yka431;
	}

	public BigDecimal getYka432() {
		return yka432;
	}

	public void setYka432(BigDecimal yka432) {
		this.yka432 = yka432;
	}

	public BigDecimal getYka433() {
		return yka433;
	}

	public void setYka433(BigDecimal yka433) {
		this.yka433 = yka433;
	}

	public BigDecimal getYka434() {
		return yka434;
	}

	public void setYka434(BigDecimal yka434) {
		this.yka434 = yka434;
	}

	public String getAae140() {
		return aae140;
	}

	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}

	public BigDecimal getYzz139() {
		return yzz139;
	}

	public void setYzz139(BigDecimal yzz139) {
		this.yzz139 = yzz139;
	}

	public BigDecimal getYkc640() {
		return ykc640;
	}

	public void setYkc640(BigDecimal ykc640) {
		this.ykc640 = ykc640;
	}

	public String getTransid0521() {
		return transid0521;
	}

	public void setTransid0521(String transid0521) {
		this.transid0521 = transid0521;
	}

	public String getTransid0522() {
		return transid0522;
	}

	public void setTransid0522(String transid0522) {
		this.transid0522 = transid0522;
	}

	public String getYzz062() {
		return yzz062;
	}

	public void setYzz062(String yzz062) {
		this.yzz062 = yzz062;
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

	public Date getYzz041() {
		return yzz041;
	}

	public void setYzz041(Date yzz041) {
		this.yzz041 = yzz041;
	}

	public Date getYzz042() {
		return yzz042;
	}

	public void setYzz042(Date yzz042) {
		this.yzz042 = yzz042;
	}

	public String getYzz134() {
		return yzz134;
	}

	public void setYzz134(String yzz134) {
		this.yzz134 = yzz134;
	}

	public String getYzz135() {
		return yzz135;
	}

	public void setYzz135(String yzz135) {
		this.yzz135 = yzz135;
	}

	public String getYzz136() {
		return yzz136;
	}

	public void setYzz136(String yzz136) {
		this.yzz136 = yzz136;
	}

	public Date getYzz137() {
		return yzz137;
	}

	public void setYzz137(Date yzz137) {
		this.yzz137 = yzz137;
	}

	public String getYzz138() {
		return yzz138;
	}

	public void setYzz138(String yzz138) {
		this.yzz138 = yzz138;
	}

	public Integer getYzz063() {
		return yzz063;
	}

	public void setYzz063(Integer yzz063) {
		this.yzz063 = yzz063;
	}

	public BigDecimal getYzz064() {
		return yzz064;
	}

	public void setYzz064(BigDecimal yzz064) {
		this.yzz064 = yzz064;
	}

	public String getTransid0534() {
		return transid0534;
	}

	public void setTransid0534(String transid0534) {
		this.transid0534 = transid0534;
	}

	public String getTransid0535() {
		return transid0535;
	}

	public void setTransid0535(String transid0535) {
		this.transid0535 = transid0535;
	}

	public String getTransid0536() {
		return transid0536;
	}

	public void setTransid0536(String transid0536) {
		this.transid0536 = transid0536;
	}

	public Integer getYzz133() {
		return yzz133;
	}

	public void setYzz133(Integer yzz133) {
		this.yzz133 = yzz133;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTransid0537() {
		return transid0537;
	}

	public void setTransid0537(String transid0537) {
		this.transid0537 = transid0537;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}

	public String getYkc751() {
		return ykc751;
	}

	public void setYkc751(String ykc751) {
		this.ykc751 = ykc751;
	}

	public BigDecimal getYkc641() {
		return ykc641;
	}

	public void setYkc641(BigDecimal ykc641) {
		this.ykc641 = ykc641;
	}

	public BigDecimal getYkc642() {
		return ykc642;
	}

	public void setYkc642(BigDecimal ykc642) {
		this.ykc642 = ykc642;
	}

	public Date getYkc706() {
		return ykc706;
	}

	public void setYkc706(Date ykc706) {
		this.ykc706 = ykc706;
	}

	public BigDecimal getYkc752() {
		return ykc752;
	}

	public void setYkc752(BigDecimal ykc752) {
		this.ykc752 = ykc752;
	}

	public BigDecimal getYkc753() {
		return ykc753;
	}

	public void setYkc753(BigDecimal ykc753) {
		this.ykc753 = ykc753;
	}


	
}