package com.zebone.nhis.pro.zsba.mz.datsett.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.InsuCountVo;
import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsBaBlCcPay;
import com.zebone.nhis.pro.zsba.mz.pub.vo.ZsbaBlCcDsMz;

/**
 * 门诊结账信息
 *  
 * @author wangpeng
 * @date 2016年10月21日
 *
 */
public class ZsbaOpBlCcVo {
	
	/** 结账信息 */
	private BlCc blCc;
	
	/** 结算收款和退款 */
	private List<ZsBaBlCcPay> blCcPayList;
	
	/** 账户收款和退款 */
	private List<ZsBaBlCcPay> blCcPayPiList;
	
	/** 支付信息 */
	private List<BlSettleDetail> blSettleDetailList;
	
	/** 挂号信息 */
	private List<InsuCountVo> insuCountVoList;
	
	/** 挂号人数 */
	private Integer cnt;
	
	/** 退号人数 */
	private Integer cancekCnt;
	
	private Double amountSt;
	
	public Double getAmountSt() {
		return amountSt;
	}


	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}

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
	
	//门诊日结
	private ZsbaBlCcDsMz zsbaBlCcDsZs;
	
	// 门诊日结 退票明细
	private List<ZsbaOpSettleBackInvDtl> settleBackInvDtls;
	
	
	public List<ZsbaOpSettleBackInvDtl> getSettleBackInvDtls() {
		return settleBackInvDtls;
	}


	public void setSettleBackInvDtls(List<ZsbaOpSettleBackInvDtl> settleBackInvDtls) {
		this.settleBackInvDtls = settleBackInvDtls;
	}


	public ZsbaBlCcDsMz getZsbaBlCcDsZs() {
		return zsbaBlCcDsZs;
	}
	

	public void setZsbaBlCcDsZs(ZsbaBlCcDsMz zsbaBlCcDsZs) {
		this.zsbaBlCcDsZs = zsbaBlCcDsZs;
	}

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

	public List<ZsBaBlCcPay> getBlCcPayList() {
		return blCcPayList;
	}

	public void setBlCcPayList(List<ZsBaBlCcPay> blCcPayList) {
		this.blCcPayList = blCcPayList;
	}

	public List<ZsBaBlCcPay> getBlCcPayPiList() {
		return blCcPayPiList;
	}

	public void setBlCcPayPiList(List<ZsBaBlCcPay> blCcPayPiList) {
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
