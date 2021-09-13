package com.zebone.nhis.common.module.compay.ins.shenzhen.xnh;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: INS_GZYB_ST_XNH - tabledesc
 * 
 * @since 2019-12-12 03:35:31
 */
@Table(value = "INS_GZYB_ST_XNH")
public class SzybStXnh extends BaseModule {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_INSSTXNH - 主键 */
	@PK
	@Field(value = "PK_INSSTXNH", id = KeyId.UUID)
	private String pkInsstxnh;

	/** PK_INSST - 关联医保结算 */
	@Field(value = "PK_INSST")
	private String pkInsst;

	/** DATE_REDEEM - 兑付日期 */
	@Field(value = "DATE_REDEEM")
	private Date dateRedeem;

	/** CODE_REDEEM_ORG - 补偿机构编码 */
	@Field(value = "CODE_REDEEM_ORG")
	private String codeRedeemOrg;

	/** CODE_PVTYPE - 就诊类型代码 */
	@Field(value = "CODE_PVTYPE")
	private String codePvtype;

	/** NAME_PVTYPE - 就诊类型名称 */
	@Field(value = "NAME_PVTYPE")
	private String namePvtype;

	/** CODE_DEPT_ADMIT - 入院科室代码 */
	@Field(value = "CODE_DEPT_ADMIT")
	private String codeDeptAdmit;

	/** NAME_DEPT_ADMIT - 入院科室名称 */
	@Field(value = "NAME_DEPT_ADMIT")
	private String nameDeptAdmit;

	/** CODE_DEPT_DIS - 出院科室代码 */
	@Field(value = "CODE_DEPT_DIS")
	private String codeDeptDis;

	/** NAME_DEPT_DIS - 出院科室名称 */
	@Field(value = "NAME_DEPT_DIS")
	private String nameDeptDis;

	/** CODE_STATUS_ADMIT - 入院状态代码 */
	@Field(value = "CODE_STATUS_ADMIT")
	private String codeStatusAdmit;

	/** NAME_STATUS_ADMIT - 入院状态名称 */
	@Field(value = "NAME_STATUS_ADMIT")
	private String nameStatusAdmit;

	/** CODE_STATUS_DIS - 出院状态代码 */
	@Field(value = "CODE_STATUS_DIS")
	private String codeStatusDis;

	/** NAME_STATUS_DIS - 出院状态名称 */
	@Field(value = "NAME_STATUS_DIS")
	private String nameStatusDis;

	/** STATUS_OUTP - 出院情况 */
	@Field(value = "STATUS_OUTP")
	private String statusOutp;

	/** CODE_COMP - 并发症代码 */
	@Field(value = "CODE_COMP")
	private String codeComp;

	/** NAME_COMP - 并发症名称 */
	@Field(value = "NAME_COMP")
	private String nameComp;

	/** HIC_NO - 居民健康卡号 */
	@Field(value = "HIC_NO")
	private String hicNo;

	/** CODE_MAIN_DIAG - 主要诊断代码 */
	@Field(value = "CODE_MAIN_DIAG")
	private String codeMainDiag;

	/** NAME_MAIN_DIAG - 主要诊断名称 */
	@Field(value = "NAME_MAIN_DIAG")
	private String nameMainDiag;

	/** CODE_OPPR - 手术名称代码 */
	@Field(value = "CODE_OPPR")
	private String codeOppr;

	/** NAME_OPPR - 手术名称 */
	@Field(value = "NAME_OPPR")
	private String nameOppr;

	/** CODE_XNHORG - 新农合经办机构代码 */
	@Field(value = "CODE_XNHORG")
	private String codeXnhorg;

	/** NAME_XNHORG - 新农合经办机构名称 */
	@Field(value = "NAME_XNHORG")
	private String nameXnhorg;

	/** AMOUNT - 费用总额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/** AMT_GRZF - 自付总额 */
	@Field(value = "AMT_GRZF")
	private Double amtGrzf;

	/** AMT_SJBX - 实际报销总额 */
	@Field(value = "AMT_SJBX")
	private Double amtSjbx;

	/** YEAR_BX - 报销（政策）年度 */
	@Field(value = "YEAR_BX")
	private String yearBx;

	/** AMT_KBX - 可报销总额 */
	@Field(value = "AMT_KBX")
	private Double amtKbx;

	/** AMT_DBZ - 单病种费用定额 */
	@Field(value = "AMT_DBZ")
	private Double amtDbz;

	/** AMT_DBBXKBC - 大病保险可补偿额 */
	@Field(value = "AMT_DBBXKBC")
	private Double amtDbbxkbc;

	/** AMT_DBBXSJBC - 大病保险实际补偿额 */
	@Field(value = "AMT_DBBXSJBC")
	private Double amtDbbxsjbc;

	/** AMT_MZQZBC - 民政救助补偿额 */
	@Field(value = "AMT_MZQZBC")
	private Double amtMzqzbc;

	/** PERC_ZTBX - 整体报销比例 */
	@Field(value = "PERC_ZTBX")
	private Double percZtbx;

	/** AMT_BNDLJBX - 本年度累计报销金额 */
	@Field(value = "AMT_BNDLJBX")
	private Double amtBndljbx;

	/** TIMES_BNDLJBX - 本年度累计报销次数 */
	@Field(value = "TIMES_BNDLJBX")
	private Double timesBndljbx;

	/** AMT_QFX - 起付线 */
	@Field(value = "AMT_QFX")
	private Double amtQfx;

	/** AMT_FDX - 封顶线 */
	@Field(value = "AMT_FDX")
	private Double amtFdx;

	/** NOTE - 备注信息 */
	@Field(value = "NOTE")
	private String note;

	/** CODE_JSDY - 结算单元编码 */
	@Field(value = "CODE_JSDY")
	private String codeJsdy;

	/** NAME_JSDY - 结算单元名称 */
	@Field(value = "NAME_JSDY")
	private String nameJsdy;

	/** AMT_KJ - 扣减总额 */
	@Field(value = "AMT_KJ")
	private Double amtKj;

	/** REASON_KJ - 扣减原因 */
	@Field(value = "REASON_KJ")
	private String reasonKj;

	/** AMT_DF - 垫付总额 */
	@Field(value = "AMT_DF")
	private Double amtDf;

	/** CODE_YJS - 预结算流水号 */
	@Field(value = "CODE_YJS")
	private String codeYjs;

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

	public String getPkInsstxnh() {
		return this.pkInsstxnh;
	}

	public void setPkInsstxnh(String pkInsstxnh) {
		this.pkInsstxnh = pkInsstxnh;
	}

	public String getPkInsst() {
		return this.pkInsst;
	}

	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}

	public Date getDateRedeem() {
		return this.dateRedeem;
	}

	public void setDateRedeem(Date dateRedeem) {
		this.dateRedeem = dateRedeem;
	}

	public String getCodeRedeemOrg() {
		return this.codeRedeemOrg;
	}

	public void setCodeRedeemOrg(String codeRedeemOrg) {
		this.codeRedeemOrg = codeRedeemOrg;
	}

	public String getCodePvtype() {
		return this.codePvtype;
	}

	public void setCodePvtype(String codePvtype) {
		this.codePvtype = codePvtype;
	}

	public String getNamePvtype() {
		return this.namePvtype;
	}

	public void setNamePvtype(String namePvtype) {
		this.namePvtype = namePvtype;
	}

	public String getCodeDeptAdmit() {
		return this.codeDeptAdmit;
	}

	public void setCodeDeptAdmit(String codeDeptAdmit) {
		this.codeDeptAdmit = codeDeptAdmit;
	}

	public String getNameDeptAdmit() {
		return this.nameDeptAdmit;
	}

	public void setNameDeptAdmit(String nameDeptAdmit) {
		this.nameDeptAdmit = nameDeptAdmit;
	}

	public String getCodeDeptDis() {
		return this.codeDeptDis;
	}

	public void setCodeDeptDis(String codeDeptDis) {
		this.codeDeptDis = codeDeptDis;
	}

	public String getNameDeptDis() {
		return this.nameDeptDis;
	}

	public void setNameDeptDis(String nameDeptDis) {
		this.nameDeptDis = nameDeptDis;
	}

	public String getCodeStatusAdmit() {
		return this.codeStatusAdmit;
	}

	public void setCodeStatusAdmit(String codeStatusAdmit) {
		this.codeStatusAdmit = codeStatusAdmit;
	}

	public String getNameStatusAdmit() {
		return this.nameStatusAdmit;
	}

	public void setNameStatusAdmit(String nameStatusAdmit) {
		this.nameStatusAdmit = nameStatusAdmit;
	}

	public String getCodeStatusDis() {
		return this.codeStatusDis;
	}

	public void setCodeStatusDis(String codeStatusDis) {
		this.codeStatusDis = codeStatusDis;
	}

	public String getNameStatusDis() {
		return this.nameStatusDis;
	}

	public void setNameStatusDis(String nameStatusDis) {
		this.nameStatusDis = nameStatusDis;
	}

	public String getStatusOutp() {
		return this.statusOutp;
	}

	public void setStatusOutp(String statusOutp) {
		this.statusOutp = statusOutp;
	}

	public String getCodeComp() {
		return this.codeComp;
	}

	public void setCodeComp(String codeComp) {
		this.codeComp = codeComp;
	}

	public String getNameComp() {
		return this.nameComp;
	}

	public void setNameComp(String nameComp) {
		this.nameComp = nameComp;
	}

	public String getHicNo() {
		return this.hicNo;
	}

	public void setHicNo(String hicNo) {
		this.hicNo = hicNo;
	}

	public String getCodeMainDiag() {
		return this.codeMainDiag;
	}

	public void setCodeMainDiag(String codeMainDiag) {
		this.codeMainDiag = codeMainDiag;
	}

	public String getNameMainDiag() {
		return this.nameMainDiag;
	}

	public void setNameMainDiag(String nameMainDiag) {
		this.nameMainDiag = nameMainDiag;
	}

	public String getCodeOppr() {
		return this.codeOppr;
	}

	public void setCodeOppr(String codeOppr) {
		this.codeOppr = codeOppr;
	}

	public String getNameOppr() {
		return this.nameOppr;
	}

	public void setNameOppr(String nameOppr) {
		this.nameOppr = nameOppr;
	}

	public String getCodeXnhorg() {
		return this.codeXnhorg;
	}

	public void setCodeXnhorg(String codeXnhorg) {
		this.codeXnhorg = codeXnhorg;
	}

	public String getNameXnhorg() {
		return this.nameXnhorg;
	}

	public void setNameXnhorg(String nameXnhorg) {
		this.nameXnhorg = nameXnhorg;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmtGrzf() {
		return this.amtGrzf;
	}

	public void setAmtGrzf(Double amtGrzf) {
		this.amtGrzf = amtGrzf;
	}

	public Double getAmtSjbx() {
		return this.amtSjbx;
	}

	public void setAmtSjbx(Double amtSjbx) {
		this.amtSjbx = amtSjbx;
	}

	public String getYearBx() {
		return this.yearBx;
	}

	public void setYearBx(String yearBx) {
		this.yearBx = yearBx;
	}

	public Double getAmtKbx() {
		return this.amtKbx;
	}

	public void setAmtKbx(Double amtKbx) {
		this.amtKbx = amtKbx;
	}

	public Double getAmtDbz() {
		return this.amtDbz;
	}

	public void setAmtDbz(Double amtDbz) {
		this.amtDbz = amtDbz;
	}

	public Double getAmtDbbxkbc() {
		return this.amtDbbxkbc;
	}

	public void setAmtDbbxkbc(Double amtDbbxkbc) {
		this.amtDbbxkbc = amtDbbxkbc;
	}

	public Double getAmtDbbxsjbc() {
		return this.amtDbbxsjbc;
	}

	public void setAmtDbbxsjbc(Double amtDbbxsjbc) {
		this.amtDbbxsjbc = amtDbbxsjbc;
	}

	public Double getAmtMzqzbc() {
		return this.amtMzqzbc;
	}

	public void setAmtMzqzbc(Double amtMzqzbc) {
		this.amtMzqzbc = amtMzqzbc;
	}

	public Double getPercZtbx() {
		return this.percZtbx;
	}

	public void setPercZtbx(Double percZtbx) {
		this.percZtbx = percZtbx;
	}

	public Double getAmtBndljbx() {
		return this.amtBndljbx;
	}

	public void setAmtBndljbx(Double amtBndljbx) {
		this.amtBndljbx = amtBndljbx;
	}

	public Double getTimesBndljbx() {
		return this.timesBndljbx;
	}

	public void setTimesBndljbx(Double timesBndljbx) {
		this.timesBndljbx = timesBndljbx;
	}

	public Double getAmtQfx() {
		return this.amtQfx;
	}

	public void setAmtQfx(Double amtQfx) {
		this.amtQfx = amtQfx;
	}

	public Double getAmtFdx() {
		return this.amtFdx;
	}

	public void setAmtFdx(Double amtFdx) {
		this.amtFdx = amtFdx;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getCodeJsdy() {
		return this.codeJsdy;
	}

	public void setCodeJsdy(String codeJsdy) {
		this.codeJsdy = codeJsdy;
	}

	public String getNameJsdy() {
		return this.nameJsdy;
	}

	public void setNameJsdy(String nameJsdy) {
		this.nameJsdy = nameJsdy;
	}

	public Double getAmtKj() {
		return this.amtKj;
	}

	public void setAmtKj(Double amtKj) {
		this.amtKj = amtKj;
	}

	public String getReasonKj() {
		return this.reasonKj;
	}

	public void setReasonKj(String reasonKj) {
		this.reasonKj = reasonKj;
	}

	public Double getAmtDf() {
		return this.amtDf;
	}

	public void setAmtDf(Double amtDf) {
		this.amtDf = amtDf;
	}

	public String getCodeYjs() {
		return this.codeYjs;
	}

	public void setCodeYjs(String codeYjs) {
		this.codeYjs = codeYjs;
	}
}
