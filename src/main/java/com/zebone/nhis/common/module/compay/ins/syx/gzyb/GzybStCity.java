package com.zebone.nhis.common.module.compay.ins.syx.gzyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: INS_GZYB_ST_CITY - tabledesc
 * 
 * @since 2018-08-08 03:35:31
 */
@Table(value = "INS_GZYB_ST_CITY")
public class GzybStCity extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_INSSTCITY - 主键 */
	@PK
	@Field(value = "PK_INSSTCITY", id = KeyId.UUID)
	private String pkInsstcity;

	/** PK_INSST - 关联医保结算 */
	@Field(value = "PK_INSST")
	private String pkInsst;

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

	public String getPkInsstcity() {
		return this.pkInsstcity;
	}

	public void setPkInsstcity(String pkInsstcity) {
		this.pkInsstcity = pkInsstcity;
	}

	public String getPkInsst() {
		return this.pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public String getChargeBatch() {
		return this.chargeBatch;
	}

	public void setChargeBatch(String chargeBatch) {
		this.chargeBatch = chargeBatch;
	}

	public String getEuTreattype() {
		return this.euTreattype;
	}

	public void setEuTreattype(String euTreattype) {
		this.euTreattype = euTreattype;
	}

	public String getDiagcodeInp() {
		return this.diagcodeInp;
	}

	public void setDiagcodeInp(String diagcodeInp) {
		this.diagcodeInp = diagcodeInp;
	}

	public String getDiagnameInp() {
		return this.diagnameInp;
	}

	public void setDiagnameInp(String diagnameInp) {
		this.diagnameInp = diagnameInp;
	}

	public String getReasonOutp() {
		return this.reasonOutp;
	}

	public void setReasonOutp(String reasonOutp) {
		this.reasonOutp = reasonOutp;
	}

	public String getDiagcodeOutp() {
		return this.diagcodeOutp;
	}

	public void setDiagcodeOutp(String diagcodeOutp) {
		this.diagcodeOutp = diagcodeOutp;
	}

	public String getDiagnameOutp() {
		return this.diagnameOutp;
	}

	public void setDiagnameOutp(String diagnameOutp) {
		this.diagnameOutp = diagnameOutp;
	}

	public String getDiagcode2Outp() {
		return this.diagcode2Outp;
	}

	public void setDiagcode2Outp(String diagcode2Outp) {
		this.diagcode2Outp = diagcode2Outp;
	}

	public String getDiagname2Outp() {
		return this.diagname2Outp;
	}

	public void setDiagname2Outp(String diagname2Outp) {
		this.diagname2Outp = diagname2Outp;
	}

	public String getStatusOutp() {
		return this.statusOutp;
	}

	public void setStatusOutp(String statusOutp) {
		this.statusOutp = statusOutp;
	}

	public Double getAmtSbzf() {
		return this.amtSbzf;
	}

	public void setAmtSbzf(Double amtSbzf) {
		this.amtSbzf = amtSbzf;
	}

	public Double getAmtZhzf() {
		return this.amtZhzf;
	}

	public void setAmtZhzf(Double amtZhzf) {
		this.amtZhzf = amtZhzf;
	}

	public Double getAmtBfxmzf() {
		return this.amtBfxmzf;
	}

	public void setAmtBfxmzf(Double amtBfxmzf) {
		this.amtBfxmzf = amtBfxmzf;
	}

	public Double getAmtGrqf() {
		return this.amtGrqf;
	}

	public void setAmtGrqf(Double amtGrqf) {
		this.amtGrqf = amtGrqf;
	}

	public Double getAmtGrzfxm() {
		return this.amtGrzfxm;
	}

	public void setAmtGrzfxm(Double amtGrzfxm) {
		this.amtGrzfxm = amtGrzfxm;
	}

	public Double getAmtGrzf() {
		return this.amtGrzf;
	}

	public void setAmtGrzf(Double amtGrzf) {
		this.amtGrzf = amtGrzf;
	}

	public Double getAmtGrfd() {
		return this.amtGrfd;
	}

	public void setAmtGrfd(Double amtGrfd) {
		this.amtGrfd = amtGrfd;
	}

	public Double getAmtGrzfxmCash() {
		return this.amtGrzfxmCash;
	}

	public void setAmtGrzfxmCash(Double amtGrzfxmCash) {
		this.amtGrzfxmCash = amtGrzfxmCash;
	}

	public Double getAmtGrzfxmAcc() {
		return this.amtGrzfxmAcc;
	}

	public void setAmtGrzfxmAcc(Double amtGrzfxmAcc) {
		this.amtGrzfxmAcc = amtGrzfxmAcc;
	}

	public Double getAmtCtczfxegrzf() {
		return this.amtCtczfxegrzf;
	}

	public void setAmtCtczfxegrzf(Double amtCtczfxegrzf) {
		this.amtCtczfxegrzf = amtCtczfxegrzf;
	}

	public Double getAmtYyjgfd() {
		return this.amtYyjgfd;
	}

	public void setAmtYyjgfd(Double amtYyjgfd) {
		this.amtYyjgfd = amtYyjgfd;
	}

	public Double getAmtGrzfCash() {
		return this.amtGrzfCash;
	}

	public void setAmtGrzfCash(Double amtGrzfCash) {
		this.amtGrzfCash = amtGrzfCash;
	}

	public Double getAmtGrzfAcc() {
		return this.amtGrzfAcc;
	}

	public void setAmtGrzfAcc(Double amtGrzfAcc) {
		this.amtGrzfAcc = amtGrzfAcc;
	}
}
