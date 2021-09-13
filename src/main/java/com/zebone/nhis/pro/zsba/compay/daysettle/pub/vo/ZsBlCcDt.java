package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.Collection;
import java.util.List;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;

public class ZsBlCcDt {
	
	private ZsbaBlCc blCc;
	private ZsbaBlCcDs blCcDs;
	
	private Collection<BlCcPay> blCcPayList;
	
	private List<ZsInvInfo> InvInfo;
	private List<ZsInvalidStInv> InvalidList;
	private List<ZsPayerData> payerList;
	
	private String voidInvFp;//作废的发票串
	private int voidFpCnt;//作废发票数量
	private String backInvFp;//退费的发票串
	private int backFpCnt;//退费发票数量
	
	private String voidInvPre;//作废的预交金票据串
	private int voidPreCnt;//作废预交金票据数量
	private String backInvPre;//退费的预交金票据串
	private int backPreCnt;//退费预交金票据数量
	
	private String cntRetreatFp;//退款发票数量
	private String cntScrapFp;//作废发票数量
	
	private String cntRetreat;//退款收据号数量
	private String cntScrap;//作废收据号数量
	
	private ZsbaPayTypeAmount pta; //各支付类型对应的的金额
	
	//欠款金额
	private Double amtAr;

	private Double amtKy;//科研费用
	
	private List<ZsbaBackInvInfo> backInvInfoList;//退票信息
	
	private List<ZsbaBackDepoInfo> backDepoInfoList;//退预交金票据信息
	
	private List<ZsbaBackDepoInfo> nbzzDepoInfoList;//内部转账信息
	
	public Double getAmtAr() {
		return amtAr;
	}

	public void setAmtAr(Double amtAr) {
		this.amtAr = amtAr;
	}

	public List<ZsInvInfo> getInvInfo() {
		return InvInfo;
	}

	public void setInvInfo(List<ZsInvInfo> invInfo) {
		InvInfo = invInfo;
	}

	public List<ZsInvalidStInv> getInvalidList() {
		return InvalidList;
	}

	public void setInvalidList(List<ZsInvalidStInv> invalidList) {
		InvalidList = invalidList;
	}
	
	private Double amtGet;
	public Double getAmtGet() {
		return amtGet;
	}

	public void setAmtGet(Double amtGet) {
		this.amtGet = amtGet;
	}

	public ZsbaBlCc getBlCc() {
		return blCc;
	}

	public void setBlCc(ZsbaBlCc blCc) {
		this.blCc = blCc;
	}

	public Collection<BlCcPay> getBlCcPayList() {
		return blCcPayList;
	}

	public void setBlCcPayList(Collection<BlCcPay> blCcPayList) {
		this.blCcPayList = blCcPayList;
	}

	public List<ZsPayerData> getPayerList() {
		return payerList;
	}

	public void setPayerList(List<ZsPayerData> payerList) {
		this.payerList = payerList;
	}

	public String getCntRetreat() {
		return cntRetreat;
	}

	public void setCntRetreat(String cntRetreat) {
		this.cntRetreat = cntRetreat;
	}

	public String getCntScrap() {
		return cntScrap;
	}

	public void setCntScrap(String cntScrap) {
		this.cntScrap = cntScrap;
	}

	public String getCntRetreatFp() {
		return cntRetreatFp;
	}

	public void setCntRetreatFp(String cntRetreatFp) {
		this.cntRetreatFp = cntRetreatFp;
	}

	public String getCntScrapFp() {
		return cntScrapFp;
	}

	public void setCntScrapFp(String cntScrapFp) {
		this.cntScrapFp = cntScrapFp;
	}

	public ZsbaPayTypeAmount getPta() {
		return pta;
	}

	public void setPta(ZsbaPayTypeAmount pta) {
		this.pta = pta;
	}

	public Double getAmtKy() {
		return amtKy;
	}

	public void setAmtKy(Double amtKy) {
		this.amtKy = amtKy;
	}

	public String getVoidInvFp() {
		return voidInvFp;
	}

	public void setVoidInvFp(String voidInvFp) {
		this.voidInvFp = voidInvFp;
	}

	public int getVoidFpCnt() {
		return voidFpCnt;
	}

	public void setVoidFpCnt(int voidFpCnt) {
		this.voidFpCnt = voidFpCnt;
	}

	public String getBackInvFp() {
		return backInvFp;
	}

	public void setBackInvFp(String backInvFp) {
		this.backInvFp = backInvFp;
	}

	public int getBackFpCnt() {
		return backFpCnt;
	}

	public void setBackFpCnt(int backFpCnt) {
		this.backFpCnt = backFpCnt;
	}

	public String getVoidInvPre() {
		return voidInvPre;
	}

	public void setVoidInvPre(String voidInvPre) {
		this.voidInvPre = voidInvPre;
	}

	public int getVoidPreCnt() {
		return voidPreCnt;
	}

	public void setVoidPreCnt(int voidPreCnt) {
		this.voidPreCnt = voidPreCnt;
	}

	public String getBackInvPre() {
		return backInvPre;
	}

	public void setBackInvPre(String backInvPre) {
		this.backInvPre = backInvPre;
	}

	public int getBackPreCnt() {
		return backPreCnt;
	}

	public void setBackPreCnt(int backPreCnt) {
		this.backPreCnt = backPreCnt;
	}

	public ZsbaBlCcDs getBlCcDs() {
		return blCcDs;
	}

	public void setBlCcDs(ZsbaBlCcDs blCcDs) {
		this.blCcDs = blCcDs;
	}

	public List<ZsbaBackInvInfo> getBackInvInfoList() {
		return backInvInfoList;
	}

	public void setBackInvInfoList(List<ZsbaBackInvInfo> backInvInfoList) {
		this.backInvInfoList = backInvInfoList;
	}

	public List<ZsbaBackDepoInfo> getBackDepoInfoList() {
		return backDepoInfoList;
	}

	public void setBackDepoInfoList(List<ZsbaBackDepoInfo> backDepoInfoList) {
		this.backDepoInfoList = backDepoInfoList;
	}

	public List<ZsbaBackDepoInfo> getNbzzDepoInfoList() {
		return nbzzDepoInfoList;
	}

	public void setNbzzDepoInfoList(List<ZsbaBackDepoInfo> nbzzDepoInfoList) {
		this.nbzzDepoInfoList = nbzzDepoInfoList;
	}

	
}
