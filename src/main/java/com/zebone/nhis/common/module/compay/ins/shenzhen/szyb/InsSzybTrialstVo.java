package com.zebone.nhis.common.module.compay.ins.shenzhen.szyb;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value = "INS_GZYB_TRIALST")
public class InsSzybTrialstVo extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_TRIAL - 主键 */
	@PK
	@Field(value = "PK_TRIAL", id = KeyId.UUID)
	private String pkTrial;

	/** PK_PV - 住院患者主键 */
	@Field(value = "PK_PV")
	private String pkPv;

	/** NAME_PI - 患者姓名 */
	@Field(value = "NAME_PI")
	private String namePi;

	/** TYPE - 类型1住院2门诊 */
	@Field(value = "TYPE")
	private String type;

	/** PVCODE_INS - 就医登记号 */
	@Field(value = "PVCODE_INS")
	private String pvcodeIns;

	/** AMTST - 结算金额 */
	@Field(value = "AMTST")
	private String amtSt;

	/** AMTINSU - 医保支付金额 */
	@Field(value = "AMTINSU")
	private String amtInsu;

	/** ZHZFJE - 账户支付金额 */
	@Field(value = "ZHZFJE")
	private String zhzfje;

	/** BFXMZFJE - 部分项目自付金额 */
	@Field(value = "BFXMZFJE")
	private String bfxmzfje;

	/** QFJE - 个人起付金额 */
	@Field(value = "QFJE")
	private String qfje;

	/** GRZFJE - 个人自费项目金额 */
	@Field(value = "GRZFJE")
	private String grzfje;

	/** AMTPI - 患者自付金额 */
	@Field(value = "AMTPI")
	private String amtPi;

	/** GRZF - 个人自负金额 */
	@Field(value = "GRZF")
	private String grzf;

	/** CXZFJE - 超统筹支付限额 */
	@Field(value = "CXZFJE")
	private String cxzfje;

	/** YYFDJE - 医药机构分担金额 */
	@Field(value = "YYFDJE")
	private String yyfdje;

	/** FYPC - 费用批次 */
	@Field(value = "FYPC")
	private String fypc;

	/** ID_NO - 身份证号 */
	@Field(value = "ID_NO")
	private String idNo;

	/** CODE_IP - 住院号 */
	@Field(value = "CODE_IP")
	private String codeIp;

	/** DATE_INP - 入院日期 */
	@Field(value = "DATE_INP")
	private Date dateInp;

	/** DATE_OUT - 出院日期 */
	@Field(value = "DATE_OUT")
	private Date dateOut;

	/** DATE_ST - 结算日期 */
	@Field(value = "DATE_ST")
	private Date dateSt;

	/** PK_DEPT - 入院科室主键 */
	@Field(value = "PK_DEPT")
	private String pkDept;

	/** NAME_DEPT - 入院科室 */
	@Field(value = "NAME_DEPT")
	private String nameDept;

	/** PK_EMPPHY - 主管医生主键 */
	@Field(value = "PK_EMPPHY")
	private String pkEmpphy;

	/** NAME_EMPPHY - 主管医生姓名 */
	@Field(value = "NAME_EMPPHY")
	private String nameEmpphy;

	/** CREATOR - 创建者 */
	@Field(value = "CREATOR")
	private String creator;

	/** CREATE_TIME - 创建时间 */
	@Field(value = "CREATE_TIME")
	private Date createTime;

	/** DEL_FLAG - 删除标识 */
	@Field(value = "DEL_FLAG")
	private String delFlag;

	/** TS - 时间戳 */
	@Field(value = "TS")
	private Date ts;

	public String getPkTrial() {
		return this.pkTrial;
	}

	public void setPkTrial(String pkTrial) {
		this.pkTrial = pkTrial;
	}

	public String getPkPv() {
		return this.pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getNamePi() {
		return this.namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPvcodeIns() {
		return this.pvcodeIns;
	}

	public void setPvcodeIns(String pvcodeIns) {
		this.pvcodeIns = pvcodeIns;
	}

	public String getAmtSt() {
		return amtSt;
	}

	public void setAmtSt(String amtSt) {
		this.amtSt = amtSt;
	}

	public String getAmtInsu() {
		return amtInsu;
	}

	public void setAmtInsu(String amtInsu) {
		this.amtInsu = amtInsu;
	}

	public String getAmtPi() {
		return amtPi;
	}

	public void setAmtPi(String amtPi) {
		this.amtPi = amtPi;
	}

	public String getZhzfje() {
		return this.zhzfje;
	}

	public void setZhzfje(String zhzfje) {
		this.zhzfje = zhzfje;
	}

	public String getBfxmzfje() {
		return this.bfxmzfje;
	}

	public void setBfxmzfje(String bfxmzfje) {
		this.bfxmzfje = bfxmzfje;
	}

	public String getQfje() {
		return this.qfje;
	}

	public void setQfje(String qfje) {
		this.qfje = qfje;
	}

	public String getGrzfje() {
		return this.grzfje;
	}

	public void setGrzfje(String grzfje) {
		this.grzfje = grzfje;
	}

	public String getGrzf() {
		return this.grzf;
	}

	public void setGrzf(String grzf) {
		this.grzf = grzf;
	}

	public String getCxzfje() {
		return this.cxzfje;
	}

	public void setCxzfje(String cxzfje) {
		this.cxzfje = cxzfje;
	}

	public String getYyfdje() {
		return this.yyfdje;
	}

	public void setYyfdje(String yyfdje) {
		this.yyfdje = yyfdje;
	}

	public String getFypc() {
		return this.fypc;
	}

	public void setFypc(String fypc) {
		this.fypc = fypc;
	}

	public String getIdNo() {
		return this.idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getCodeIp() {
		return this.codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public Date getDateInp() {
		return this.dateInp;
	}

	public void setDateInp(Date dateInp) {
		this.dateInp = dateInp;
	}

	public Date getDateOut() {
		return this.dateOut;
	}

	public void setDateOut(Date dateOut) {
		this.dateOut = dateOut;
	}

	public Date getDateSt() {
		return this.dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getPkDept() {
		return this.pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNameDept() {
		return this.nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getPkEmpphy() {
		return this.pkEmpphy;
	}

	public void setPkEmpphy(String pkEmpphy) {
		this.pkEmpphy = pkEmpphy;
	}

	public String getNameEmpphy() {
		return this.nameEmpphy;
	}

	public void setNameEmpphy(String nameEmpphy) {
		this.nameEmpphy = nameEmpphy;
	}
}
