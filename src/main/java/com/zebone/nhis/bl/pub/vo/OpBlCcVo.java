package com.zebone.nhis.bl.pub.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettleDetail;

/**
 * 门诊结账信息
 *  
 * @author wangpeng
 * @date 2016年10月21日
 *
 */
public class OpBlCcVo {
	
	/** 结账信息 */
	private BlCc blCc;
	
	/** 结算收款和退款 */
	private List<BlCcPay> blCcPayList;
	
	/** 账户收款和退款 */
	private List<BlCcPay> blCcPayPiList;
	
	/** 支付信息 */
	private List<BlSettleDetail> blSettleDetailList;
	
	/** 挂号信息 */
	private List<InsuCountVo> insuCountVoList;
	
	/** 挂号人数 */
	private Integer cnt;
	
	/** 退号人数 */
	private Integer cancekCnt;
	
	/** 发票信息 */
	private List<BlInvoice> blInvoiceList;
	
	/** 发票作废信息 */
	private List<BlInvoice> blInvoiceCancelList;
	
	//扩展参数
	private Map<String,Object> extParam;
	
	//扩展参数集合
	private List<Map<String,Object>> extListParam;
	
	//支付列表信息(深大使用)
	private List<BlCcPay> blCcPayListSd;
	
	
	public List<BlCcPay> getBlCcPayListSd() {
		return blCcPayListSd;
	}

	public void setBlCcPayListSd(List<BlCcPay> blCcPayListSd) {
		this.blCcPayListSd = blCcPayListSd;
	}

	public Map<String, Object> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, Object> extParam) {
		this.extParam = extParam;
	}

	public List<Map<String, Object>> getExtListParam() {
		return extListParam;
	}

	public void setExtListParam(List<Map<String, Object>> extListParam) {
		this.extListParam = extListParam;
	}

	public BlCc getBlCc() {
		return blCc;
	}

	public void setBlCc(BlCc blCc) {
		this.blCc = blCc;
	}

	public List<BlCcPay> getBlCcPayList() {
		return blCcPayList;
	}

	public void setBlCcPayList(List<BlCcPay> blCcPayList) {
		this.blCcPayList = blCcPayList;
	}

	public List<BlCcPay> getBlCcPayPiList() {
		return blCcPayPiList;
	}

	public void setBlCcPayPiList(List<BlCcPay> blCcPayPiList) {
		this.blCcPayPiList = blCcPayPiList;
	}

	public List<BlSettleDetail> getBlSettleDetailList() {
		return blSettleDetailList;
	}

	public void setBlSettleDetailList(List<BlSettleDetail> blSettleDetailList) {
		this.blSettleDetailList = blSettleDetailList;
	}

	public List<InsuCountVo> getInsuCountVoList() {
		return insuCountVoList;
	}

	public void setInsuCountVoList(List<InsuCountVo> insuCountVoList) {
		this.insuCountVoList = insuCountVoList;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getCancekCnt() {
		return cancekCnt;
	}

	public void setCancekCnt(Integer cancekCnt) {
		this.cancekCnt = cancekCnt;
	}

	public List<BlInvoice> getBlInvoiceList() {
		return blInvoiceList;
	}

	public void setBlInvoiceList(List<BlInvoice> blInvoiceList) {
		this.blInvoiceList = blInvoiceList;
	}

	public List<BlInvoice> getBlInvoiceCancelList() {
		return blInvoiceCancelList;
	}

	public void setBlInvoiceCancelList(List<BlInvoice> blInvoiceCancelList) {
		this.blInvoiceCancelList = blInvoiceCancelList;
	}

}
