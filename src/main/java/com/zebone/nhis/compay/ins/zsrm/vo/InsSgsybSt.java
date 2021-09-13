package com.zebone.nhis.compay.ins.zsrm.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value = "INS_SGSYB_ST")
public class InsSgsybSt extends BaseModule {

	
	@PK
	@Field(value = "pk_Insst", id = KeyId.UUID)
	private String pkInsst;

	@Field("pk_visit")
	private String pkVisit;

	@Field("pk_hp")
	private String pkHp;
	@Field("pk_pv")
	private String pkPv;
	@Field("pk_pi")
	private String pkPi;
	@Field("pk_settle")
	private String pkSettle;
	@Field("pk_settle_cancel")
	private String pkSettleCancel;
	@Field("date_st")
	private Date dateSt;

	@Field("bka001")
	private String bka001;//费用序号，类似于结算id
	@Field("aaz213")
	private String aaz213;
	@Field("yb_pksettle")
	private String ybPksettle;
	@Field("yb_pksettle_cancel")
	private String ybPksettleCancel;
	@Field("pk_insst_cancel")
	private String pkInsstCancel;

	
	@Field("amount")
	private Double amount;//总金额
	@Field("bill_no")
	private String billNo;//发票号
	@Field("amt_grzhzf")
	private Double amtGrzhzf;//个账账户支付
	@Field("amt_grzf")
	private Double amtGrzf;//个人支付
	@Field("amt_grzh")
	private Double amtGrzh;//账户余额
	@Field("amt_jjzf")
	private Double amtJjzf;//基金支付
	
	@Field("akc264")
	private String akc264;//总金额
	@Field("bka832")
	private String bka832;//基金支付/工伤保险支付
	@Field("akb067")
	private String akb067;//个人现金
	@Field("akb066")
	private String akb066;//个人账户
	
	@Field("bka831")
	private String bka831;//个自付       bka831 = akb067 + akb066 +  bka839
	
	@Field("akb020")
	private String akb020;
	@Field("aaz218")
	private String aaz218;
	@Field("bka825")
	private String bka825;
	@Field("bka826")
	private String bka826;
	@Field("aka151")
	private String aka151;
	@Field("bka838")
	private String bka838;
	@Field("bka821")
	private String bka821;
	@Field("bka839")
	private String bka839;
	@Field("ake039")
	private String ake039;
	@Field("ake035")
	private String ake035;
	@Field("ake026")
	private String ake026;
	@Field("ake029")
	private String ake029;
	@Field("bka841")
	private String bka841;
	@Field("bka842")
	private String bka842;
	@Field("bka840")
	private String bka840;
	
	//费用总额
	private Double aggregateAmount;
	//个人负担
	private Double patientsPay;
	//医保支付
	private Double medicarePayments;
	
	private String yBPreIntoParam;
	private List<Map<String,Object>> YBPreReturn; 
	
	
	public List<Map<String, Object>> getYBPreReturn() {
		return YBPreReturn;
	}
	public void setYBPreReturn(List<Map<String, Object>> yBPreReturn) {
		YBPreReturn = yBPreReturn;
	}
	
	public String getyBPreIntoParam() {
		return yBPreIntoParam;
	}
	public void setyBPreIntoParam(String yBPreIntoParam) {
		this.yBPreIntoParam = yBPreIntoParam;
	}
	
	public Double getMedicarePayments() {
		return medicarePayments;
	}
	public void setMedicarePayments(Double medicarePayments) {
		this.medicarePayments = medicarePayments;
	}
	public String getPkInsst() {
		return pkInsst;
	}
	public void setPkInsst(String pkInsst) {
		this.pkInsst = pkInsst;
	}
	public String getPkVisit() {
		return pkVisit;
	}
	public void setPkVisit(String pkVisit) {
		this.pkVisit = pkVisit;
	}
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Double getAmtGrzhzf() {
		return amtGrzhzf;
	}
	public void setAmtGrzhzf(Double amtGrzhzf) {
		this.amtGrzhzf = amtGrzhzf;
	}
	public Double getAmtGrzf() {
		return amtGrzf;
	}
	public void setAmtGrzf(Double amtGrzf) {
		this.amtGrzf = amtGrzf;
	}
	public Double getAmtGrzh() {
		return amtGrzh;
	}
	public void setAmtGrzh(Double amtGrzh) {
		this.amtGrzh = amtGrzh;
	}
	public Double getAmtJjzf() {
		return amtJjzf;
	}
	public void setAmtJjzf(Double amtJjzf) {
		this.amtJjzf = amtJjzf;
	}
	public String getAkc264() {
		return akc264;
	}
	public void setAkc264(String akc264) {
		this.akc264 = akc264;
	}
	public String getBka832() {
		return bka832;
	}
	public void setBka832(String bka832) {
		this.bka832 = bka832;
	}
	public String getAkb067() {
		return akb067;
	}
	public void setAkb067(String akb067) {
		this.akb067 = akb067;
	}
	public String getAkb066() {
		return akb066;
	}
	public void setAkb066(String akb066) {
		this.akb066 = akb066;
	}
	public String getBka831() {
		return bka831;
	}
	public void setBka831(String bka831) {
		this.bka831 = bka831;
	}
	public String getAkb020() {
		return akb020;
	}
	public void setAkb020(String akb020) {
		this.akb020 = akb020;
	}
	public String getAaz218() {
		return aaz218;
	}
	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}
	public String getBka825() {
		return bka825;
	}
	public void setBka825(String bka825) {
		this.bka825 = bka825;
	}
	public String getBka826() {
		return bka826;
	}
	public void setBka826(String bka826) {
		this.bka826 = bka826;
	}
	public String getAka151() {
		return aka151;
	}
	public void setAka151(String aka151) {
		this.aka151 = aka151;
	}
	public String getBka838() {
		return bka838;
	}
	public void setBka838(String bka838) {
		this.bka838 = bka838;
	}
	public String getBka821() {
		return bka821;
	}
	public void setBka821(String bka821) {
		this.bka821 = bka821;
	}
	public String getBka839() {
		return bka839;
	}
	public void setBka839(String bka839) {
		this.bka839 = bka839;
	}
	public String getAke039() {
		return ake039;
	}
	public void setAke039(String ake039) {
		this.ake039 = ake039;
	}
	public String getAke035() {
		return ake035;
	}
	public void setAke035(String ake035) {
		this.ake035 = ake035;
	}
	public String getAke026() {
		return ake026;
	}
	public void setAke026(String ake026) {
		this.ake026 = ake026;
	}
	public String getAke029() {
		return ake029;
	}
	public void setAke029(String ake029) {
		this.ake029 = ake029;
	}
	public String getBka841() {
		return bka841;
	}
	public void setBka841(String bka841) {
		this.bka841 = bka841;
	}
	public String getBka842() {
		return bka842;
	}
	public void setBka842(String bka842) {
		this.bka842 = bka842;
	}
	public String getBka840() {
		return bka840;
	}
	public void setBka840(String bka840) {
		this.bka840 = bka840;
	}
	public String getAaz213() {
		return aaz213;
	}
	public void setAaz213(String aaz213) {
		this.aaz213 = aaz213;
	}
	public String getBka001() {
		return bka001;
	}
	public void setBka001(String bka001) {
		this.bka001 = bka001;
	}
	public String getPkSettleCancel() {
		return pkSettleCancel;
	}
	public void setPkSettleCancel(String pkSettleCancel) {
		this.pkSettleCancel = pkSettleCancel;
	}
	public String getYbPksettle() {
		return ybPksettle;
	}
	public void setYbPksettle(String ybPksettle) {
		this.ybPksettle = ybPksettle;
	}
	public String getYbPksettleCancel() {
		return ybPksettleCancel;
	}
	public void setYbPksettleCancel(String ybPksettleCancel) {
		this.ybPksettleCancel = ybPksettleCancel;
	}
	public String getPkInsstCancel() {
		return pkInsstCancel;
	}
	public void setPkInsstCancel(String pkInsstCancel) {
		this.pkInsstCancel = pkInsstCancel;
	}
	public Double getAggregateAmount() {
		return aggregateAmount;
	}
	public void setAggregateAmount(Double aggregateAmount) {
		this.aggregateAmount = aggregateAmount;
	}
	public Double getPatientsPay() {
		return patientsPay;
	}
	public void setPatientsPay(Double patientsPay) {
		this.patientsPay = patientsPay;
	}
	public Date getDateSt() {
		return dateSt;
	}
	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	
}
