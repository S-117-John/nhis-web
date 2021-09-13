package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;

import java.math.BigDecimal;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 外部医保-省内工伤医保住院结算
 * 
 * 1.结算成功时，将医保结算返回的数据生成记录；
 * 2.取消结算成功时，将aaz218下的数据做假删处理；
 * 
 * @author lipz
 *
 */
@Table(value="ins_st_wi")
public class InsStWi extends BaseModule implements Cloneable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7189010960424508998L;

	public Object clone() {  
		InsStWi o = null;  
        try {  
            o = (InsStWi) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
    }  
	
	@PK
	@Field(value="pk_insstwi",id=KeyId.UUID)
	private String pkInsstwi;//主键	
	
	@Field(value="pk_org")
	private String pkOrg;//		所属机构

	@Field(value="PK_INSU")
	private String pkInsu;//	PK_INSU - 医保主计划主键 

	@Field(value="pk_pi")
	private String pkPi;//		患者主键

	@Field(value="pk_pv")
	private String pkPv;//		就诊主键
	
	@Field(value="pk_settle")
	private String pkSettle;//		就诊主键

	@Field(value="eu_pvtype")
	private String euPvtype;//	就诊类型: 1 门诊，2 急诊，3 住院，4 体检
	
	@Field(value="aaz218")
	private String aaz218;//	就医登记号	

	@Field(value="akb020")
	private String akb020;//	医疗机构编号	
	
	@Field(value="bka031")
	private String bka031;//	出院诊断
	
	@Field(value="bkf004")
	private String bkf004;//	出院转归情况

	@Field(value="akc264")
	private BigDecimal akc264;//	医疗总费用akc264 = bka831 + bka832

	@Field(value="bka831")
	private BigDecimal bka831;//	个人自付bka831 = akb067 + akb066 +  bka839


	@Field(value="bka832")
	private BigDecimal bka832;//	工伤保险支付bka832 = ake039 + ake035 + ake026 + ake029 + bka841 + bka842 + bka840 + bka821


	@Field(value="bka825")
	private BigDecimal bka825;//	全自费费用	

	@Field(value="bka826")
	private BigDecimal bka826;//	部分自费费用	

	@Field(value="aka151")
	private BigDecimal aka151;//	起付线费用	

	@Field(value="bka838")
	private BigDecimal bka838;//	超共付段费用个人自付	

	@Field(value="akb067")
	private BigDecimal akb067;//	个人现金支付	

	@Field(value="akb066")
	private BigDecimal akb066;//	个人账户支付	

	@Field(value="bka821")
	private BigDecimal bka821;//	民政救助金支付	

	@Field(value="bka839")
	private BigDecimal bka839;//	其他支付	

	@Field(value="ake039")
	private BigDecimal ake039;//	工伤保险统筹基金支付	

	@Field(value="ake035")
	private BigDecimal ake035;//	公务员医疗补助基金支付	

	@Field(value="ake026")
	private BigDecimal ake026;//	企业补充工伤保险基金支付	

	@Field(value="ake029")
	private BigDecimal ake029;//	大额医疗费用补助基金支付	

	@Field(value="bka841")
	private BigDecimal bka841;//	单位支付	

	@Field(value="bka842")
	private BigDecimal bka842;//	医院垫付	

	@Field(value="bka840")
	private BigDecimal bka840;//	其他基金支付	

	@Field(value="pk_insstwi_can")
	private String pkInsstwiCan;//被取消结算记录的主键
	
	public String getPkInsstwi() {
		return pkInsstwi;
	}

	public void setPkInsstwi(String pkInsstwi) {
		this.pkInsstwi = pkInsstwi;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
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

	public String getAaz218() {
		return aaz218;
	}

	public void setAaz218(String aaz218) {
		this.aaz218 = aaz218;
	}

	public String getAkb020() {
		return akb020;
	}

	public void setAkb020(String akb020) {
		this.akb020 = akb020;
	}

	public BigDecimal getAkc264() {
		return akc264;
	}

	public void setAkc264(BigDecimal akc264) {
		this.akc264 = akc264;
	}

	public BigDecimal getBka831() {
		return bka831;
	}

	public void setBka831(BigDecimal bka831) {
		this.bka831 = bka831;
	}

	public BigDecimal getBka832() {
		return bka832;
	}

	public void setBka832(BigDecimal bka832) {
		this.bka832 = bka832;
	}

	public BigDecimal getBka825() {
		return bka825;
	}

	public void setBka825(BigDecimal bka825) {
		this.bka825 = bka825;
	}

	public BigDecimal getBka826() {
		return bka826;
	}

	public void setBka826(BigDecimal bka826) {
		this.bka826 = bka826;
	}

	public BigDecimal getAka151() {
		return aka151;
	}

	public void setAka151(BigDecimal aka151) {
		this.aka151 = aka151;
	}

	public BigDecimal getBka838() {
		return bka838;
	}

	public void setBka838(BigDecimal bka838) {
		this.bka838 = bka838;
	}

	public BigDecimal getAkb067() {
		return akb067;
	}

	public void setAkb067(BigDecimal akb067) {
		this.akb067 = akb067;
	}

	public BigDecimal getAkb066() {
		return akb066;
	}

	public void setAkb066(BigDecimal akb066) {
		this.akb066 = akb066;
	}

	public BigDecimal getBka821() {
		return bka821;
	}

	public void setBka821(BigDecimal bka821) {
		this.bka821 = bka821;
	}

	public BigDecimal getBka839() {
		return bka839;
	}

	public void setBka839(BigDecimal bka839) {
		this.bka839 = bka839;
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

	public BigDecimal getAke029() {
		return ake029;
	}

	public void setAke029(BigDecimal ake029) {
		this.ake029 = ake029;
	}

	public BigDecimal getBka841() {
		return bka841;
	}

	public void setBka841(BigDecimal bka841) {
		this.bka841 = bka841;
	}

	public BigDecimal getBka842() {
		return bka842;
	}

	public void setBka842(BigDecimal bka842) {
		this.bka842 = bka842;
	}

	public BigDecimal getBka840() {
		return bka840;
	}

	public void setBka840(BigDecimal bka840) {
		this.bka840 = bka840;
	}

	public String getBka031() {
		return bka031;
	}

	public void setBka031(String bka031) {
		this.bka031 = bka031;
	}

	public String getBkf004() {
		return bkf004;
	}

	public void setBkf004(String bkf004) {
		this.bkf004 = bkf004;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPkInsstwiCan() {
		return pkInsstwiCan;
	}

	public void setPkInsstwiCan(String pkInsstwiCan) {
		this.pkInsstwiCan = pkInsstwiCan;
	}

	
	

}
