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
 * Table: ins_cert_out - ins_cert_out 
 *
 * @since 2017-09-06 10:42:10
 */
@Table(value="INS_CERT_OUT")
public class InsPubCertOut extends BaseModule  {

	@PK
	@Field(value="PK_CERTOUT",id=KeyId.UUID)
    private String pkCertout;

	@Field(value="PK_PI")
    private String pkPi;
	
	@Field(value="PK_PV")
    private String pkPv;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;
	
	/**1省内医保，2跨省医保*/
	@Field(value="INS_TYPE")
    private String insType;
	
	@Field(value="TRANSID0211")
    private String transid0211;
	
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
	
	@Field(value="AAB301")
    private String aab301;
	
	/**
	 * 参保地社保分支机构代码
	 */
	@Field(value="YAB060")
    private String yab060;
	
	@Field(value="AAC002")
    private String aac002;

    /** AAC043 - 字典aac043 */
	@Field(value="AAC043")
    private String aac043;

	@Field(value="AAC044")
    private String aac044;

    /** AKE007 - 发生日期 */
	@Field(value="AKE007")
    private Date ake007;
	
    /** ERRORCODE - 错误代码 */
	@Field(value="ERRORCODE")
    private int errorcode;
	
    /** ERRORMSG - 错误信息 */
	@Field(value="ERRORMSG")
    private String errormsg;
	
	@Field(value="AAC003")
    private String aac003;

    /** AAC004 - 1 男，2 女，9 未说明 */
	@Field(value="AAC004")
    private String aac004;

	@Field(value="AAC005")
    private String aac005;

	@Field(value="AAC006")
    private Date aac006;

    /** YKC021 - 字典ykc021 */
	@Field(value="YKC021")
    private String ykc021;

    /** YKC300 - 字典ykc300 */
	@Field(value="YKC300")
    private String ykc300;

    /** AKC026 - 0 非公务员，1 公务员 */
	@Field(value="AKC026")
    private String akc026;

	@Field(value="AKC023")
    private BigDecimal akc023;

	@Field(value="AAE379")
    private BigDecimal aae379;

	@Field(value="AKC252")
    private BigDecimal akc252;

	@Field(value="AAB001")
    private String aab001;

	@Field(value="AAB003")
    private String aab003;

	@Field(value="AAB004")
    private String aab004;

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
    private BigDecimal akc200;

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

    /** AKA130 - 字典aka130 */
	@Field(value="AKA130")
    private String aka130;

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

	@Field(value="MODITY_TIME",date=FieldType.UPDATE)
    private Date modityTime;

	public String getPkCertout() {
		return pkCertout;
	}

	public void setPkCertout(String pkCertout) {
		this.pkCertout = pkCertout;
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

	public String getTransid0211() {
		return transid0211;
	}

	public void setTransid0211(String transid0211) {
		this.transid0211 = transid0211;
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

	public Date getAke007() {
		return ake007;
	}

	public void setAke007(Date ake007) {
		this.ake007 = ake007;
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

	public String getAkc026() {
		return akc026;
	}

	public void setAkc026(String akc026) {
		this.akc026 = akc026;
	}

	public BigDecimal getAkc023() {
		return akc023;
	}

	public void setAkc023(BigDecimal akc023) {
		this.akc023 = akc023;
	}

	public BigDecimal getAae379() {
		return aae379;
	}

	public void setAae379(BigDecimal aae379) {
		this.aae379 = aae379;
	}

	public BigDecimal getAkc252() {
		return akc252;
	}

	public void setAkc252(BigDecimal akc252) {
		this.akc252 = akc252;
	}

	public String getAab001() {
		return aab001;
	}

	public void setAab001(String aab001) {
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

	public BigDecimal getAkc200() {
		return akc200;
	}

	public void setAkc200(BigDecimal akc200) {
		this.akc200 = akc200;
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

	public String getAka130() {
		return aka130;
	}

	public void setAka130(String aka130) {
		this.aka130 = aka130;
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

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}


 
}