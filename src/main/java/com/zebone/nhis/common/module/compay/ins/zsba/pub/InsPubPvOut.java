package com.zebone.nhis.common.module.compay.ins.zsba.pub;

import java.util.Date;
import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: ins_pv_out - ins_pv_out 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_PV_OUT")
public class InsPubPvOut extends BaseModule  {

	@PK
	@Field(value="PK_INSPVOUT",id=KeyId.UUID)
    private String pkInspvout;

	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/**1省内医保，2跨省医保*/
	@Field(value="INS_TYPE")
    private String insType;
	
	@Field(value="TRANSID0212")
    private String transid0212;
	
	@Field(value="TRANSID0214")
    private String transid0214;
	
	@Field(value="TRANSID0215")
    private String transid0215;
	
	@Field(value="TRANSID0216")
    private String transid0216;

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
	
	@Field(value="AAB301")
    private String aab301;

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
	 * 医疗类别
	 */
	@Field(value="AKA130")
    private String aka130;
	
	/**
	 * 病历号
	 */
	@Field(value="YKC009")
    private String ykc009;
	
	/**
	 * 住院号(门诊号)
	 */
	@Field(value="AKC190")
    private String akc190;
	
	/**
	 * 入院科室
	 */
	@Field(value="AKF001")
    private String akf001;
	
	/**
	 * 入院病区号
	 */
	@Field(value="YZZ018")
    private String yzz018;
	
	/**
	 * 入院病区名称
	 */
	@Field(value="YZZ019")
    private String yzz019;

	/**
	 * 入院床位
	 */
	@Field(value="YKC012")
    private String ykc012;
	
	/**
	 * 入院疾病诊断名称
	 */
	@Field(value="AKC050")
    private String akc050;
	
	/**
	 * 住院原因
	 */
	@Field(value="YKC679")
    private String ykc679;
	
	/**
	 * 补助类型字段
	 */
	@Field(value="YKC680")
    private String ykc680;
	
	/**
	 * 入院诊断疾病编码1_icd10
	 */
	@Field(value="AKC193")
    private String akc193;
	
	/**
	 * 入院诊断编码2_icd10
	 */
	@Field(value="YKC601")
    private String ykc601;
	
	/**
	 * 入院诊断编码3_icd10
	 */
	@Field(value="YKC602")
    private String ykc602;
	
	/**
	 * 医师执业证编码
	 */
	@Field(value="AKC056")
    private String akc056;
	
	/**
	 * 入院诊断医生
	 */
	@Field(value="AKE022")
    private String ake022;
	
	/**
	 * 入院日期
	 */
	@Field(value="YKC701")
    private Date ykc701;
	
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
	 * 联系人
	 */
	@Field(value="AAE004")
    private String aae004;
	
	/**
	 * 联系电话
	 */
	@Field(value="AAE005")
    private String aae005;
	
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
	 * 就诊登记号
	 */
	@Field(value="YKC700")
    private String ykc700;
	
	/**
	 * 姓名
	 */
	@Field(value="AAC003")
    private String aac003;
	
	/**
	 * 性别
	 */
	@Field(value="AAC004")
    private String aac004;
	
	/**
	 * 民族
	 */
	@Field(value="AAC005")
    private String aac005;
	
	/**
	 * 出生日期
	 */
	@Field(value="AAC006")
    private Date aac006;
	
    /** YKC021 - 字典ykc021 */
	@Field(value="YKC021")
    private String ykc021;

    /** YKC300 - 字典ykc300 */
	@Field(value="YKC300")
    private String ykc300;

    /** 险种类型 */
	@Field(value="AAE140")
    private String aae140;
	
    /** AKC026 - 0 非公务员，1 公务员 */
	@Field(value="AKC026")
    private String akc026;

	@Field(value="AKC023")
    private Integer akc023;

	@Field(value="AAB001")
    private Integer aab001;

	@Field(value="AAB003")
    private String aab003;

	@Field(value="AAB004")
    private String aab004;

    /** AAB020 - 字典aab020 */
	@Field(value="AAB020")
    private String aab020;

    /** AAB019 - 字典aab019 */
	@Field(value="AAB019")
    private String aab019;

    /** AAB021 - 字典aab021 */
	@Field(value="AAB021")
    private String aab021;

	@Field(value="AAE379")
    private Integer aae379;

	@Field(value="AKC252")
    private BigDecimal akc252;

	@Field(value="YKA116")
    private BigDecimal yka116;

	@Field(value="YKA119")
    private BigDecimal yka119;

	@Field(value="YKA121")
    private BigDecimal yka121;

	@Field(value="YKA123")
    private BigDecimal yka123;

	@Field(value="AKE092")
    private BigDecimal ake092;

	@Field(value="YKA437")
    private BigDecimal yka437;

	@Field(value="AKC200")
    private Integer akc200;

    /** YKC023 - 字典ykc023 */
	@Field(value="YKC023")
    private String ykc023;

    /** YKC667 - 0 不是二次返院，1 二次返院未审批，2 二次返院已审批 */
	@Field(value="YKC667")
    private String ykc667;

	@Field(value="YZZ014")
    private String yzz014;

    /** AKE132 - 字典ake132 */
	@Field(value="AKE132")
    private String ake132;

	@Field(value="YKC669")
    private String ykc669;

	@Field(value="YKC678")
    private String ykc678;

	@Field(value="YKC670")
    private String ykc670;

    /** YKC682 - 字典ykc682 */
	@Field(value="YKC682")
    private String ykc682;

	@Field(value="AKE014")
    private Date ake014;

	@Field(value="YKC672")
    private String ykc672;

    /** YKC673 - 1 未审批，2 已审批，9 审批无效 */
	@Field(value="YKC673")
    private String ykc673;

	@Field(value="YKC674")
    private String ykc674;

	/**
	 * 出院科室
	 */
	@Field(value="AKF002")
    private String akf002;
	
	/**
	 * 出院病区号
	 */
	@Field(value="yzz088")
    private String yzz088;
	
	/**
	 * 出院病区名称
	 */
	@Field(value="yzz089")
    private String yzz089;
	
	/**
	 * 出院床位
	 */
	@Field(value="ykc016")
    private String ykc016;
	
	/**
	 * 出院疾病诊断名称
	 */
	@Field(value="akc185")
    private String akc185;
	
	/**
	 * 出院诊断疾病编码1_icd10
	 */
	@Field(value="akc196")
    private String akc196;
	
	/**
	 * 出院诊断编码2_icd10
	 */
	@Field(value="akc188")
    private String akc188;
	
	/**
	 * 出院诊断编码3_icd10
	 */
	@Field(value="akc189")
    private String akc189;
	
	/**
	 * 出院诊断医师
	 */
	@Field(value="ake021")
    private String ake021;
	
	/**
	 * 出院原因
	 */
	@Field(value="ykc195")
    private String ykc195;
	
	/**
	 * 手术名称
	 */
	@Field(value="ykc683")
    private String ykc683;

	/**
	 * 出院日期
	 */
	@Field(value="ykc702")
    private Date ykc702;

	/**
	 * 住院天数
	 */
	@Field(value="akb063")
    private Integer akb063;

	/**
	 * 出院带药天数
	 */
	@Field(value="yzz020")
    private Integer yzz020;

	/**
	 * 出院经办人
	 */
	@Field(value="aae011_cy")
    private String aae011Cy;

	/**
	 * 出院经办时间
	 */
	@Field(value="ykc018")
    private Date ykc018;

	/**
	 * 状态标志
	 */
	@Field(value="status")
    private String status;

	/**
	 * 修改时间
	 */
	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkInspvout() {
		return pkInspvout;
	}

	public void setPkInspvout(String pkInspvout) {
		this.pkInspvout = pkInspvout;
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

	public String getTransid0212() {
		return transid0212;
	}

	public void setTransid0212(String transid0212) {
		this.transid0212 = transid0212;
	}

	public String getTransid0214() {
		return transid0214;
	}

	public void setTransid0214(String transid0214) {
		this.transid0214 = transid0214;
	}

	public String getTransid0215() {
		return transid0215;
	}

	public void setTransid0215(String transid0215) {
		this.transid0215 = transid0215;
	}

	public String getTransid0216() {
		return transid0216;
	}

	public void setTransid0216(String transid0216) {
		this.transid0216 = transid0216;
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

	public String getAka130() {
		return aka130;
	}

	public void setAka130(String aka130) {
		this.aka130 = aka130;
	}

	public String getYkc009() {
		return ykc009;
	}

	public void setYkc009(String ykc009) {
		this.ykc009 = ykc009;
	}

	public String getAkc190() {
		return akc190;
	}

	public void setAkc190(String akc190) {
		this.akc190 = akc190;
	}

	public String getAkf001() {
		return akf001;
	}

	public void setAkf001(String akf001) {
		this.akf001 = akf001;
	}

	public String getYzz018() {
		return yzz018;
	}

	public void setYzz018(String yzz018) {
		this.yzz018 = yzz018;
	}

	public String getYzz019() {
		return yzz019;
	}

	public void setYzz019(String yzz019) {
		this.yzz019 = yzz019;
	}

	public String getYkc012() {
		return ykc012;
	}

	public void setYkc012(String ykc012) {
		this.ykc012 = ykc012;
	}

	public String getAkc050() {
		return akc050;
	}

	public void setAkc050(String akc050) {
		this.akc050 = akc050;
	}

	public String getYkc679() {
		return ykc679;
	}

	public void setYkc679(String ykc679) {
		this.ykc679 = ykc679;
	}

	public String getYkc680() {
		return ykc680;
	}

	public void setYkc680(String ykc680) {
		this.ykc680 = ykc680;
	}

	public String getAkc193() {
		return akc193;
	}

	public void setAkc193(String akc193) {
		this.akc193 = akc193;
	}

	public String getYkc601() {
		return ykc601;
	}

	public void setYkc601(String ykc601) {
		this.ykc601 = ykc601;
	}

	public String getYkc602() {
		return ykc602;
	}

	public void setYkc602(String ykc602) {
		this.ykc602 = ykc602;
	}

	public String getAkc056() {
		return akc056;
	}

	public void setAkc056(String akc056) {
		this.akc056 = akc056;
	}

	public String getAke022() {
		return ake022;
	}

	public void setAke022(String ake022) {
		this.ake022 = ake022;
	}

	public Date getYkc701() {
		return ykc701;
	}

	public void setYkc701(Date ykc701) {
		this.ykc701 = ykc701;
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

	public String getAae004() {
		return aae004;
	}

	public void setAae004(String aae004) {
		this.aae004 = aae004;
	}

	public String getAae005() {
		return aae005;
	}

	public void setAae005(String aae005) {
		this.aae005 = aae005;
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

	public String getYkc700() {
		return ykc700;
	}

	public void setYkc700(String ykc700) {
		this.ykc700 = ykc700;
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

	public String getAac005() {
		return aac005;
	}

	public void setAac005(String aac005) {
		this.aac005 = aac005;
	}

	public Date getAac006() {
		return aac006;
	}

	public void setAac006(Date aac006) {
		this.aac006 = aac006;
	}

	public String getYkc021() {
		return ykc021;
	}

	public void setYkc021(String ykc021) {
		this.ykc021 = ykc021;
	}

	public String getYkc300() {
		return ykc300;
	}

	public void setYkc300(String ykc300) {
		this.ykc300 = ykc300;
	}

	public String getAae140() {
		return aae140;
	}

	public void setAae140(String aae140) {
		this.aae140 = aae140;
	}

	public String getAkc026() {
		return akc026;
	}

	public void setAkc026(String akc026) {
		this.akc026 = akc026;
	}

	public Integer getAkc023() {
		return akc023;
	}

	public void setAkc023(Integer akc023) {
		this.akc023 = akc023;
	}

	public Integer getAab001() {
		return aab001;
	}

	public void setAab001(Integer aab001) {
		this.aab001 = aab001;
	}

	public String getAab003() {
		return aab003;
	}

	public void setAab003(String aab003) {
		this.aab003 = aab003;
	}

	public String getAab004() {
		return aab004;
	}

	public void setAab004(String aab004) {
		this.aab004 = aab004;
	}

	public String getAab020() {
		return aab020;
	}

	public void setAab020(String aab020) {
		this.aab020 = aab020;
	}

	public String getAab019() {
		return aab019;
	}

	public void setAab019(String aab019) {
		this.aab019 = aab019;
	}

	public String getAab021() {
		return aab021;
	}

	public void setAab021(String aab021) {
		this.aab021 = aab021;
	}

	public Integer getAae379() {
		return aae379;
	}

	public void setAae379(Integer aae379) {
		this.aae379 = aae379;
	}

	public BigDecimal getAkc252() {
		return akc252;
	}

	public void setAkc252(BigDecimal akc252) {
		this.akc252 = akc252;
	}

	public BigDecimal getYka116() {
		return yka116;
	}

	public void setYka116(BigDecimal yka116) {
		this.yka116 = yka116;
	}

	public BigDecimal getYka119() {
		return yka119;
	}

	public void setYka119(BigDecimal yka119) {
		this.yka119 = yka119;
	}

	public BigDecimal getYka121() {
		return yka121;
	}

	public void setYka121(BigDecimal yka121) {
		this.yka121 = yka121;
	}

	public BigDecimal getYka123() {
		return yka123;
	}

	public void setYka123(BigDecimal yka123) {
		this.yka123 = yka123;
	}

	public BigDecimal getAke092() {
		return ake092;
	}

	public void setAke092(BigDecimal ake092) {
		this.ake092 = ake092;
	}

	public BigDecimal getYka437() {
		return yka437;
	}

	public void setYka437(BigDecimal yka437) {
		this.yka437 = yka437;
	}

	public Integer getAkc200() {
		return akc200;
	}

	public void setAkc200(Integer akc200) {
		this.akc200 = akc200;
	}

	public String getYkc023() {
		return ykc023;
	}

	public void setYkc023(String ykc023) {
		this.ykc023 = ykc023;
	}

	public String getYkc667() {
		return ykc667;
	}

	public void setYkc667(String ykc667) {
		this.ykc667 = ykc667;
	}

	public String getYzz014() {
		return yzz014;
	}

	public void setYzz014(String yzz014) {
		this.yzz014 = yzz014;
	}

	public String getAke132() {
		return ake132;
	}

	public void setAke132(String ake132) {
		this.ake132 = ake132;
	}

	public String getYkc669() {
		return ykc669;
	}

	public void setYkc669(String ykc669) {
		this.ykc669 = ykc669;
	}

	public String getYkc678() {
		return ykc678;
	}

	public void setYkc678(String ykc678) {
		this.ykc678 = ykc678;
	}

	public String getYkc670() {
		return ykc670;
	}

	public void setYkc670(String ykc670) {
		this.ykc670 = ykc670;
	}

	public String getYkc682() {
		return ykc682;
	}

	public void setYkc682(String ykc682) {
		this.ykc682 = ykc682;
	}

	public Date getAke014() {
		return ake014;
	}

	public void setAke014(Date ake014) {
		this.ake014 = ake014;
	}

	public String getYkc672() {
		return ykc672;
	}

	public void setYkc672(String ykc672) {
		this.ykc672 = ykc672;
	}

	public String getYkc673() {
		return ykc673;
	}

	public void setYkc673(String ykc673) {
		this.ykc673 = ykc673;
	}

	public String getYkc674() {
		return ykc674;
	}

	public void setYkc674(String ykc674) {
		this.ykc674 = ykc674;
	}

	public String getAkf002() {
		return akf002;
	}

	public void setAkf002(String akf002) {
		this.akf002 = akf002;
	}

	public String getYzz088() {
		return yzz088;
	}

	public void setYzz088(String yzz088) {
		this.yzz088 = yzz088;
	}

	public String getYzz089() {
		return yzz089;
	}

	public void setYzz089(String yzz089) {
		this.yzz089 = yzz089;
	}

	public String getYkc016() {
		return ykc016;
	}

	public void setYkc016(String ykc016) {
		this.ykc016 = ykc016;
	}

	public String getAkc185() {
		return akc185;
	}

	public void setAkc185(String akc185) {
		this.akc185 = akc185;
	}

	public String getAkc196() {
		return akc196;
	}

	public void setAkc196(String akc196) {
		this.akc196 = akc196;
	}

	public String getAkc188() {
		return akc188;
	}

	public void setAkc188(String akc188) {
		this.akc188 = akc188;
	}

	public String getAkc189() {
		return akc189;
	}

	public void setAkc189(String akc189) {
		this.akc189 = akc189;
	}

	public String getAke021() {
		return ake021;
	}

	public void setAke021(String ake021) {
		this.ake021 = ake021;
	}

	public String getYkc195() {
		return ykc195;
	}

	public void setYkc195(String ykc195) {
		this.ykc195 = ykc195;
	}

	public String getYkc683() {
		return ykc683;
	}

	public void setYkc683(String ykc683) {
		this.ykc683 = ykc683;
	}

	public Date getYkc702() {
		return ykc702;
	}

	public void setYkc702(Date ykc702) {
		this.ykc702 = ykc702;
	}

	public Integer getAkb063() {
		return akb063;
	}

	public void setAkb063(Integer akb063) {
		this.akb063 = akb063;
	}

	public Integer getYzz020() {
		return yzz020;
	}

	public void setYzz020(Integer yzz020) {
		this.yzz020 = yzz020;
	}

	public String getAae011Cy() {
		return aae011Cy;
	}

	public void setAae011Cy(String aae011Cy) {
		this.aae011Cy = aae011Cy;
	}

	public Date getYkc018() {
		return ykc018;
	}

	public void setYkc018(Date ykc018) {
		this.ykc018 = ykc018;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}


  
}