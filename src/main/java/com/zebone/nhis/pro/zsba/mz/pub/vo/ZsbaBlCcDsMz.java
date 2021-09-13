package com.zebone.nhis.pro.zsba.mz.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BL_CC_DS_MZ  - 门诊日结扩展-操作员结账 
 *
 * @since 2016-10-21 09:24:57
 */
@Table(value="BL_CC_DS_MZ")
public class ZsbaBlCcDsMz extends BaseModule  {

	private static long serialVersionUID = 1L;

	/** PK_CC_DS - 操作员结账主键 */
	@PK
	@Field(value="PK_CC_DS",id=KeyId.UUID)
    private String pKCcDs;

    /** AMT_XJ - 现金 */
	@Field(value="AMT_XJ")
    private Double amtXj=0.0;

    /** AMT_YHK - 银行卡 */
	@Field(value="AMT_YHK")
    private Double amtYhk =0.0;

    /** AMT_CWJZ - 财务记账 */
	@Field(value="AMT_CWJZ")
    private Double amtCwjz=0.0;

    public void setAmtCwjz(Double amtCwjz) {
		this.amtCwjz = amtCwjz;
	}

	/** AMT_GZ - 个账 */
	@Field(value="AMT_GZ")
    private Double amtGz=0.0;

    /** AMT_TC - 统筹 */
	@Field(value="AMT_TC")
    private Double amtTc=0.0;

    /** AMT_KQTGZ - 跨期退个账 */
	@Field(value="AMT_KQTGZ")
    private Double amtKqtgz=0.0;

    /** AMT_QTDW - 其他单位负担部分 */
	@Field(value="AMT_QTDW")
    private Double amtQtdw=0.0;

    /** AMT_DQCJ - 东区产检记账 */
	@Field(value="AMT_DQCJ")
    private Double amtDqcj=0.0;	

    /** AMT_WGSCJ - 五桂山产检记账*/
	@Field(value="AMT_WGSCJ")
    private Double amtWgscj=0.0;

    /** AMT_CSJZ - 产检记账 */
	@Field(value="AMT_CSJZ")
    private Double amtCsjz=0.0;
	
	/** AMT_YLJZ - 医疗救助 */
	@Field(value = "AMT_YLJZ")
	private Double amtYljz=0.0;

    /** AMT_KYJZ - 科研记账 */
	@Field(value="AMT_KYJZ")
    private Double amtKyjz=0.0; 

    /** AMT_GCP - GCP记账*/
	@Field(value="AMT_GCP")
    private Double amtGcp=0.0;

    /** AMT_DPCQZD - 地贫产检诊断记账 */
	@Field(value="AMT_DPCQZD")
    private Double amtDpcqzd=0.0;

    /** AMT_YYJS - 医院计生记账 */
	@Field(value="AMT_YYJS")
    private Double amtYyjs=0.0;

    /** AMT_ETFLY - 儿童福利院记账 */
	@Field(value="AMT_ETFLY")
    private Double amtEtfly=0.0;

    /** INV_INFO - 纸质发票起始 */
	@Field(value="INV_INFO")
    private String invInfo;

    /** INV_CNT - 开票数量*/
	@Field(value="INV_CNT")
    private Integer invCnt;

    /** INV_SCRAP - 废票*/
	@Field(value="INV_SCRAP")
    private Integer invScrap;

    /** INV_BACK - 退票 */
	@Field(value="INV_BACK")
    private Integer invBack;

    /** INV_BILL_INFO - 电子发票起始 */
	@Field(value="INV_BILL_INFO")
    private String invBillInfo;

    /** INV_BILL_CNT - 电子发票开票数量 */
	@Field(value="INV_BILL_CNT")
    private Integer invBillCnt;

    /** INV_BILL_BACK - 电子发票退票数量 */
	@Field(value="INV_BILL_BACK")
    private Integer invBillBack;

    /** AMT_XGYQ - 新冠疫情记账 */
	@Field(value="AMT_XGYQ")
    private Double amtXgyq=0.0;

    /** AMT_GR - 感染记账*/
	@Field(value="AMT_GR")
    private Double amtGr=0.0;

    /** AMT_RJSS - 日间手术*/
	@Field(value="AMT_RJSS")
    private Double amtRjss=0.0;

    /** AMT_Z95 - 95折 */
	@Field(value="AMT_Z95")
    private Double amtZ95=0.0;
	
	/** AMT_Z90 - 90折 */
	@Field(value = "AMT_Z90")
	private Double amtZ90=0.0;

	/** AMT_Z85 - 85折 */
	@Field(value = "AMT_Z85")
	private Double amtZ85=0.0;

	/** AMT_Z80 - 80折 */
	@Field(value = "AMT_Z80")
	private Double amtZ80=0.0;

    /** AMT_TZH - 特诊号费用 */
	@Field(value="AMT_TZH")
    private Double amtTzh=0.0;
	
	/** AMT_ETJS - 儿童加收费用 */
	@Field(value="AMT_ETJS")
	private Double amtEtjs=0.0;
		
	/** PK_CC - 操作员结账主键 */
	@Field(value="PK_CC")
    private String pkCc;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public String getpKCcDs() {
		return pKCcDs;
	}

	public void setpKCcDs(String pKCcDs) {
		this.pKCcDs = pKCcDs;
	}

	public Double getAmtXj() {
		return amtXj;
	}

	public void setAmtXj(Double amtXj) {
		this.amtXj = amtXj;
	}

	public Double getAmtYhk() {
		return amtYhk;
	}

	public void setAmtYhk(Double amtYhk) {
		this.amtYhk = amtYhk;
	}

	public Double getAmtCwjz() {
		return amtCwjz;
	}


	public Double getAmtGz() {
		return amtGz;
	}

	public void setAmtGz(Double amtGz) {
		this.amtGz = amtGz;
	}

	public Double getAmtTc() {
		return amtTc;
	}

	public void setAmtTc(Double amtTc) {
		this.amtTc = amtTc;
	}

	public Double getAmtKqtgz() {
		return amtKqtgz;
	}

	public void setAmtKqtgz(Double amtKqtgz) {
		this.amtKqtgz = amtKqtgz;
	}

	public Double getAmtQtdw() {
		return amtQtdw;
	}

	public void setAmtQtdw(Double amtQtdw) {
		this.amtQtdw = amtQtdw;
	}

	public Double getAmtDqcj() {
		return amtDqcj;
	}

	public void setAmtDqcj(Double amtDqcj) {
		this.amtDqcj = amtDqcj;
	}

	public Double getAmtWgscj() {
		return amtWgscj;
	}

	public void setAmtWgscj(Double amtWgscj) {
		this.amtWgscj = amtWgscj;
	}

	public Double getAmtCsjz() {
		return amtCsjz;
	}

	public void setAmtCsjz(Double amtCsjz) {
		this.amtCsjz = amtCsjz;
	}

	public Double getAmtYljz() {
		return amtYljz;
	}

	public void setAmtYljz(Double amtYljz) {
		this.amtYljz = amtYljz;
	}

	public Double getAmtKyjz() {
		return amtKyjz;
	}

	public void setAmtKyjz(Double amtKyjz) {
		this.amtKyjz = amtKyjz;
	}

	public Double getAmtGcp() {
		return amtGcp;
	}

	public void setAmtGcp(Double amtGcp) {
		this.amtGcp = amtGcp;
	}

	public Double getAmtDpcqzd() {
		return amtDpcqzd;
	}

	public void setAmtDpcqzd(Double amtDpcqzd) {
		this.amtDpcqzd = amtDpcqzd;
	}

	public Double getAmtYyjs() {
		return amtYyjs;
	}

	public void setAmtYyjs(Double amtYyjs) {
		this.amtYyjs = amtYyjs;
	}

	public Double getAmtEtfly() {
		return amtEtfly;
	}

	public void setAmtEtfly(Double amtEtfly) {
		this.amtEtfly = amtEtfly;
	}

	public String getInvInfo() {
		return invInfo;
	}

	public void setInvInfo(String invInfo) {
		this.invInfo = invInfo;
	}

	public Integer getInvCnt() {
		return invCnt;
	}

	public void setInvCnt(Integer invCnt) {
		this.invCnt = invCnt;
	}

	public Integer getInvScrap() {
		return invScrap;
	}

	public void setInvScrap(Integer invScrap) {
		this.invScrap = invScrap;
	}

	public Integer getInvBack() {
		return invBack;
	}

	public void setInvBack(Integer invBack) {
		this.invBack = invBack;
	}

	public String getInvBillInfo() {
		return invBillInfo;
	}

	public void setInvBillInfo(String invBillInfo) {
		this.invBillInfo = invBillInfo;
	}

	public Integer getInvBillCnt() {
		return invBillCnt;
	}

	public void setInvBillCnt(int invBillCnt) {
		this.invBillCnt = invBillCnt;
	}

	public Integer getInvBillBack() {
		return invBillBack;
	}

	public void setInvBillBack(Integer invBillBack) {
		this.invBillBack = invBillBack;
	}

	public Double getAmtXgyq() {
		return amtXgyq;
	}

	public void setAmtXgyq(Double amtXgyq) {
		this.amtXgyq = amtXgyq;
	}

	public Double getAmtGr() {
		return amtGr;
	}

	public void setAmtGr(Double amtGr) {
		this.amtGr = amtGr;
	}

	public Double getAmtRjss() {
		return amtRjss;
	}

	public void setAmtRjss(Double amtRjss) {
		this.amtRjss = amtRjss;
	}

	public Double getAmtZ95() {
		return amtZ95;
	}

	public void setAmtZ95(Double amtZ95) {
		this.amtZ95 = amtZ95;
	}

	public Double getAmtZ90() {
		return amtZ90;
	}

	public void setAmtZ90(Double amtZ90) {
		this.amtZ90 = amtZ90;
	}

	public Double getAmtZ85() {
		return amtZ85;
	}

	public void setAmtZ85(Double amtZ85) {
		this.amtZ85 = amtZ85;
	}

	public Double getAmtZ80() {
		return amtZ80;
	}

	public void setAmtZ80(Double amtZ80) {
		this.amtZ80 = amtZ80;
	}

	public Double getAmtTzh() {
		return amtTzh;
	}

	public void setAmtTzh(Double amtTzh) {
		this.amtTzh = amtTzh;
	}

	public Double getAmtEtjs() {
		return amtEtjs;
	}

	public void setAmtEtjs(Double amtEtjs) {
		this.amtEtjs = amtEtjs;
	}	

	public String getPkCc() {
		return pkCc;
	}

	public void setPkCc(String pkCc) {
		this.pkCc = pkCc;
	}

}