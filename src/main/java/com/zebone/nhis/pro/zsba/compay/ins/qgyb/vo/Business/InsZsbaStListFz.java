package com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 3202 医药机构费用结算对明细账表
 * @author Administrator
 *
 */
@Table(value="INS_QGYB_STLIST_FZ")
public class InsZsbaStListFz extends BaseModule{
	
	@PK
	@Field(value="PK_STLISTFZ",id=KeyId.UUID)
	private String pkStlistfz;//
	
	@Field(value="DIAG_CODE")
	private String	diagCode;//	主要诊断编码	字符型	40	
	@Field(value="DIAG_NAME")
	private String	diagName;//	主要诊断名称	字符型	255		
	@Field(value="POOLAREA_NO")
	private String	poolareaNo;//	统筹区	字符型	10		Y	
	@Field(value="DISE_SCO")
	private BigDecimal	diseSco;//	对照病种分值	数值型	10,2	
	@Field(value="ACT_DISE_SCO")
	private BigDecimal	actDiseSco;//	实际分组病种分值	数值型	10,2		
	@Field(value="ACT_SETL_SCO")
	private BigDecimal	actSetlSco;//	实际结算分值	数值型	10,2		
	@Field(value="TRT_WAY")
	private String	trtWay;//	治疗方式	字符型	10	Y		
	@Field(value="TRT_WAY_NAME")
	private String	trtWayName;//	治疗方式名称	字符型	10		
	@Field(value="FIXMEDINS_CODE")
	private String	fixmedinsCode;//	定点医药机构编号	字符型	30		Y	
	@Field(value="FIXMEDINS_NAME")
	private String	fixmedinsName;//	定点医药机构名称	字符型	200		
	@Field(value="PSN_NO")
	private String	psnNo;//	人员编号	字符型	32			
	@Field(value="PSN_NAME")
	private String	psnName;//	人员姓名	字符型	10			
	@Field(value="INSU_ADMDVS")
	private String	insuAdmdvs;//	参保所属医保区划	字符型	100			
	@Field(value="INSUTYPE")
	private String	insutype;//	险种类型	字符型	6	Y		
	@Field(value="ACT_IPT_DAYS")
	private int	actIptDays;//	住院天数	数值型	10,0			
	@Field(value="FUND_PAYAMT")
	private BigDecimal	fundPayamt;//	统筹基金支付金额	数值型	16,2			
	@Field(value="PSN_SELFPAY_AMT")
	private BigDecimal	psnSelfpayAmt;//	个人自付金额	数值型	16,2		Y	
	@Field(value="TOTAL_PAYAMT")
	private BigDecimal	totalPayamt;//	住院总费用	数值型	16,2			
	@Field(value="INSCP_AMT")
	private BigDecimal	inscpAmt;//	符合范围金额	数值型	16,2			
	@Field(value="FUND_PAY_SUMAMT")
	private BigDecimal	fundPaySumamt;//	基金支付总额	数值型	16,2			
	@Field(value="ADM_TIME")
	private Date	admTime;//	入院时间	日期时间型				yyyy-MM-dd HH:mm:ss
	@Field(value="DSCG_TIME")
	private Date	dscgTime;//	出院时间	日期时间型				yyyy-MM-dd HH:mm:ss
	@Field(value="SETL_END_DATE")
	private Date	setlEndDate;//	结算结束时间	日期时间型				yyyy-MM-dd HH:mm:ss
	@Field(value="NORM_FLAG")
	private String	normFlag;//	数据标识	字符型	6	Y		
	@Field(value="PAYB_SETL_AMT")
	private BigDecimal	paybSetlAmt;//	应付结算金额	数值型	16,2			
	@Field(value="ACT_SETL_AMT")
	private BigDecimal	actSetlAmt;//	实际结算金额	数值型	16,2			
	@Field(value="DIAG_GRP")
	private String	diagGrp;//	诊断亚目分组	字符型	10			
	@Field(value="REMARK")
	private String	remark;//	备注	字符型	100			
	public String getPkStlistfz() {
		return pkStlistfz;
	}
	public void setPkStlistfz(String pkStlistfz) {
		this.pkStlistfz = pkStlistfz;
	}
	public String getDiagCode() {
		return diagCode;
	}
	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}
	public String getDiagName() {
		return diagName;
	}
	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}
	public String getPoolareaNo() {
		return poolareaNo;
	}
	public void setPoolareaNo(String poolareaNo) {
		this.poolareaNo = poolareaNo;
	}
	public BigDecimal getDiseSco() {
		return diseSco;
	}
	public void setDiseSco(BigDecimal diseSco) {
		this.diseSco = diseSco;
	}
	public BigDecimal getActDiseSco() {
		return actDiseSco;
	}
	public void setActDiseSco(BigDecimal actDiseSco) {
		this.actDiseSco = actDiseSco;
	}
	public BigDecimal getActSetlSco() {
		return actSetlSco;
	}
	public void setActSetlSco(BigDecimal actSetlSco) {
		this.actSetlSco = actSetlSco;
	}
	public String getTrtWay() {
		return trtWay;
	}
	public void setTrtWay(String trtWay) {
		this.trtWay = trtWay;
	}
	public String getTrtWayName() {
		return trtWayName;
	}
	public void setTrtWayName(String trtWayName) {
		this.trtWayName = trtWayName;
	}
	public String getFixmedinsCode() {
		return fixmedinsCode;
	}
	public void setFixmedinsCode(String fixmedinsCode) {
		this.fixmedinsCode = fixmedinsCode;
	}
	public String getFixmedinsName() {
		return fixmedinsName;
	}
	public void setFixmedinsName(String fixmedinsName) {
		this.fixmedinsName = fixmedinsName;
	}
	public String getPsnNo() {
		return psnNo;
	}
	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}
	public String getPsnName() {
		return psnName;
	}
	public void setPsnName(String psnName) {
		this.psnName = psnName;
	}
	public String getInsuAdmdvs() {
		return insuAdmdvs;
	}
	public void setInsuAdmdvs(String insuAdmdvs) {
		this.insuAdmdvs = insuAdmdvs;
	}
	public String getInsutype() {
		return insutype;
	}
	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}
	public int getActIptDays() {
		return actIptDays;
	}
	public void setActIptDays(int actIptDays) {
		this.actIptDays = actIptDays;
	}
	public BigDecimal getFundPayamt() {
		return fundPayamt;
	}
	public void setFundPayamt(BigDecimal fundPayamt) {
		this.fundPayamt = fundPayamt;
	}
	public BigDecimal getPsnSelfpayAmt() {
		return psnSelfpayAmt;
	}
	public void setPsnSelfpayAmt(BigDecimal psnSelfpayAmt) {
		this.psnSelfpayAmt = psnSelfpayAmt;
	}
	public BigDecimal getTotalPayamt() {
		return totalPayamt;
	}
	public void setTotalPayamt(BigDecimal totalPayamt) {
		this.totalPayamt = totalPayamt;
	}
	public BigDecimal getInscpAmt() {
		return inscpAmt;
	}
	public void setInscpAmt(BigDecimal inscpAmt) {
		this.inscpAmt = inscpAmt;
	}
	public BigDecimal getFundPaySumamt() {
		return fundPaySumamt;
	}
	public void setFundPaySumamt(BigDecimal fundPaySumamt) {
		this.fundPaySumamt = fundPaySumamt;
	}
	public Date getAdmTime() {
		return admTime;
	}
	public void setAdmTime(Date admTime) {
		this.admTime = admTime;
	}
	public Date getDscgTime() {
		return dscgTime;
	}
	public void setDscgTime(Date dscgTime) {
		this.dscgTime = dscgTime;
	}
	public Date getSetlEndDate() {
		return setlEndDate;
	}
	public void setSetlEndDate(Date setlEndDate) {
		this.setlEndDate = setlEndDate;
	}
	public String getNormFlag() {
		return normFlag;
	}
	public void setNormFlag(String normFlag) {
		this.normFlag = normFlag;
	}
	public BigDecimal getPaybSetlAmt() {
		return paybSetlAmt;
	}
	public void setPaybSetlAmt(BigDecimal paybSetlAmt) {
		this.paybSetlAmt = paybSetlAmt;
	}
	public BigDecimal getActSetlAmt() {
		return actSetlAmt;
	}
	public void setActSetlAmt(BigDecimal actSetlAmt) {
		this.actSetlAmt = actSetlAmt;
	}
	public String getDiagGrp() {
		return diagGrp;
	}
	public void setDiagGrp(String diagGrp) {
		this.diagGrp = diagGrp;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
}
