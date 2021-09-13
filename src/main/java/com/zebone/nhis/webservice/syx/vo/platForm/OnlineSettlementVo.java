package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;

public class OnlineSettlementVo {

	/** PK_INSST - 主键 */
	@PK
	@Field(value = "PK_INSST")
	private String pkInsst;

	@Field(value = "PK_VISIT")
	private String pkvisit;

	/** PK_HP - 医保类别 */
	@Field(value = "PK_HP")
	private String pkHp;

	/** PK_PV - 病人就诊主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** PK_PI - 患者主键 */
	@Field(value = "PK_PI")
	private String pkPi;

	/** DATE_INP - 入院日期 */
	@Field(value = "DATE_INP")
	private Date dateInp;

	/** DATE_OUTP - 出院日期 */
	@Field(value = "DATE_OUTP")
	private Date dateOutp;

	/** DAYS - 实际住院天数 */
	@Field(value = "DAYS")
	private Integer days;

	/** PK_SETTLE - 结算主键 */
	@Field(value = "PK_SETTLE")
	private String pkSettle;

	/** PVCODE_INS - 就医登记号 */
	@Field(value = "PVCODE_INS")
	private String pvcodeIns;

	/** DATE_ST - 结算日期 */
	@Field(value = "DATE_ST")
	private Date dateSt;

	/** AMOUNT - 总金额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/** CODE_CENTER - 中心编码 */
	@Field(value = "CODE_CENTER")
	private String codeCenter;

	/** CODE_ORG - 医院编号 */
	@Field(value = "CODE_ORG")
	private String codeOrg;

	/** NAME_ORG - 医院名称 */
	@Field(value = "NAME_ORG")
	private String nameOrg;

	/** PK_ORG-所属机构 */
	@Field(value = "PK_ORG")
	private String pkOrg;

	/** CREATOR-创建人 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME-创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG-删除标志 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS-时间戳 */
	@Field(value = "TS")
	private String ts;

	/** PK_INSSTCITY - 主键 */
	@PK
	@Field(value = "PK_INSSTCITY")
	private String pkInsstcity;

	/** CHARGE_BATCH - 费用批次 */
	@Field(value = "CHARGE_BATCH")
	private String chargeBatch;

	/** EU_TREATTYPE - 交易类型 */
	@Field(value = "EU_TREATTYPE")
	private String euTreattype;

	/** DIAGCODE_INP - 入院诊断疾病编号 */
	@Field(value = "DIAGCODE_INP")
	private String diagcodeInp;

	/** DIAGNAME_INP - 入院诊断疾病名称 */
	@Field(value = "DIAGNAME_INP")
	private String diagnameInp;

	/** REASON_OUTP - 出院原因 */
	@Field(value = "REASON_OUTP")
	private String reasonOutp;

	/** DIAGCODE_OUTP - 出院诊断疾病编号 */
	@Field(value = "DIAGCODE_OUTP")
	private String diagcodeOutp;

	/** DIAGNAME_OUTP - 出院诊断疾病名称 */
	@Field(value = "DIAGNAME_OUTP")
	private String diagnameOutp;

	/** DIAGCODE2_OUTP - 出院诊断疾病编号2 */
	@Field(value = "DIAGCODE2_OUTP")
	private String diagcode2Outp;

	/** DIAGNAME2_OUTP - 出院诊断疾病名称2 */
	@Field(value = "DIAGNAME2_OUTP")
	private String diagname2Outp;

	/** STATUS_OUTP - 出院情况 */
	@Field(value = "STATUS_OUTP")
	private String statusOutp;

	/** AMT_SBZF - 社保支付金额 */
	@Field(value = "AMT_SBZF")
	private Double amtSbzf;

	/** AMT_ZHZF - 帐户支付金额 */
	@Field(value = "AMT_ZHZF")
	private Double amtZhzf;

	/** AMT_BFXMZF - 部分项目自付金额 */
	@Field(value = "AMT_BFXMZF")
	private Double amtBfxmzf;

	/** AMT_GRQF - 个人起付金额 */
	@Field(value = "AMT_GRQF")
	private Double amtGrqf;

	/** AMT_GRZFXM - 个人自费项目金额 */
	@Field(value = "AMT_GRZFXM")
	private Double amtGrzfxm;

	/** AMT_GRZF - 个人自付金额 */
	@Field(value = "AMT_GRZF")
	private Double amtGrzf;

	/** AMT_GRFD - 个人自负金额 */
	@Field(value = "AMT_GRFD")
	private Double amtGrfd;

	/** AMT_GRZFXM_CASH - 个人自费现金部分 */
	@Field(value = "AMT_GRZFXM_CASH")
	private Double amtGrzfxmCash;

	/** AMT_GRZFXM_ACC - 个人自费个人帐户部分 */
	@Field(value = "AMT_GRZFXM_ACC")
	private Double amtGrzfxmAcc;

	/** AMT_CTCZFXEGRZF - 超统筹支付限额个人自付金额 */
	@Field(value = "AMT_CTCZFXEGRZF")
	private Double amtCtczfxegrzf;

	/** AMT_YYJGFD - 医药机构分担金额 */
	@Field(value = "AMT_YYJGFD")
	private Double amtYyjgfd;

	/** AMT_GRZF_CASH - 个人自付现金部分 */
	@Field(value = "AMT_GRZF_CASH")
	private Double amtGrzfCash;

	/** AMT_GRZF_ACC - 个人自付个人帐户部分 */
	@Field(value = "AMT_GRZF_ACC")
	private Double amtGrzfAcc;

}
