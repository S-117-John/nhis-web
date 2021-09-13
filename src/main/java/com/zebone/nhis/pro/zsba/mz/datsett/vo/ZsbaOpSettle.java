package com.zebone.nhis.pro.zsba.mz.datsett.vo;

public class ZsbaOpSettle {
	/*现金*/
	private Double cash;
	
	/*银行卡*/
	private Double bankCard;
	
	/*财务记账*/
	private Double cwjz;
	
	/**医院优惠**/
	private Double yyyh;	

	/**跨期退医保个账**/
	private Double kqtybgz;
	
	/*医保个账*/
	private Double ybgz;
		
	/*医保统筹*/
	private Double ybtc;	
	
	/*其他单位*/
	private Double qtdw;
	
	/*结算总金额*/
	private Double amountSt;
	
	/*东区产检记账*/
	private Double dqcjjz;
	
	/*五桂山产检记账*/
	private Double wgscjjz;
	
	/*产筛记账*/
	private Double csjz;
	
	/*医疗救助*/
	private Double yljz;
	
	/*科研记账*/
	private Double kyjz;
	
	/* 地贫产前诊断记账 */
	private Double dpcqjz;
	
	/*医院计生记账*/
	private Double yyjsjz;
	
	/*GCP临床试验记账*/
	private Double gcplcsyjz;
	
	/*儿童福利院记账*/
	private Double etflyjz;
	
	/*单位（项目）负担部分合计*/
	private Double AmountDw;
	
	/*纸质票据开始号*/
	private String invCodeMin;
	
	/*纸质票据截止号*/
	private String invCodeMax;
	
	/*开票数量*/
	private String  invCount;
	
	/*废票数量*/
	private String  invCancelCount;
	
	/*退票数量*/
	private String  invBackCount;
	
	/*电子票据开始号*/
	private String invEbillCodeMin;
	
	/*电子票据截止号*/
	private String invEbillCodeMax;
	
	/*电子开票数量*/
	private String  invEbillCount;
		
	/*电子退票数量*/
	private String  invEbillBackCount;
	
	public Double getYbgz() {
		return ybgz;
	}

	public void setYbgz(Double ybgz) {
		this.ybgz = ybgz;
	}

	
	public String getInvCodeMin() {
		return invCodeMin;
	}

	public void setInvCodeMin(String invCodeMin) {
		this.invCodeMin = invCodeMin;
	}

	public String getInvCodeMax() {
		return invCodeMax;
	}

	public void setInvCodeMax(String invCodeMax) {
		this.invCodeMax = invCodeMax;
	}

	public String getInvCount() {
		return invCount;
	}

	public void setInvCount(String invCount) {
		this.invCount = invCount;
	}

	public String getInvCancelCount() {
		return invCancelCount;
	}

	public void setInvCancelCount(String invCancelCount) {
		this.invCancelCount = invCancelCount;
	}

	public String getInvBackCount() {
		return invBackCount;
	}

	public void setInvBackCount(String invBackCount) {
		this.invBackCount = invBackCount;
	}

	public String getInvEbillCodeMin() {
		return invEbillCodeMin;
	}

	public void setInvEbillCodeMin(String invEbillCodeMin) {
		this.invEbillCodeMin = invEbillCodeMin;
	}

	public String getInvEbillCodeMax() {
		return invEbillCodeMax;
	}

	public void setInvEbillCodeMax(String invEbillCodeMax) {
		this.invEbillCodeMax = invEbillCodeMax;
	}

	public String getInvEbillCount() {
		return invEbillCount;
	}

	public void setInvEbillCount(String invEbillCount) {
		this.invEbillCount = invEbillCount;
	}

	public String getInvEbillBackCount() {
		return invEbillBackCount;
	}

	public void setInvEbillBackCount(String invEbillBackCount) {
		this.invEbillBackCount = invEbillBackCount;
	}

	public Double getCash() {
		return cash;
	}

	public void setCash(Double cash) {
		this.cash = cash;
	}

	public Double getBankCard() {
		return bankCard;
	}

	public void setBankCard(Double bankCard) {
		this.bankCard = bankCard;
	}

	public Double getCwjz() {
		return cwjz;
	}

	public void setCwjz(Double cwjz) {
		this.cwjz = cwjz;
	}

	public Double getQtdw() {
		return qtdw;
	}

	public void setQtdw(Double qtdw) {
		this.qtdw = qtdw;
	}

	public Double getAmountSt() {
		return amountSt;
	}

	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}

	public Double getDqcjjz() {
		return dqcjjz;
	}

	public void setDqcjjz(Double dqcjjz) {
		this.dqcjjz = dqcjjz;
	}

	public Double getWgscjjz() {
		return wgscjjz;
	}

	public void setWgscjjz(Double wgscjjz) {
		this.wgscjjz = wgscjjz;
	}

	public Double getCsjz() {
		return csjz;
	}

	public void setCsjz(Double csjz) {
		this.csjz = csjz;
	}

	public Double getYljz() {
		return yljz;
	}

	public void setYljz(Double yljz) {
		this.yljz = yljz;
	}

	public Double getKyjz() {
		return kyjz;
	}

	public void setKyjz(Double kyjz) {
		this.kyjz = kyjz;
	}

	public Double getGcplcsyjz() {
		return gcplcsyjz;
	}

	public void setGcplcsyjz(Double gcplcsyjz) {
		this.gcplcsyjz = gcplcsyjz;
	}

	public Double getDpcqjz() {
		return dpcqjz;
	}

	public void setDpcqjz(Double dpcqjz) {
		this.dpcqjz = dpcqjz;
	}

	public Double getYyjsjz() {
		return yyjsjz;
	}

	public void setYyjsjz(Double yyjsjz) {
		this.yyjsjz = yyjsjz;
	}

	public Double getEtflyjz() {
		return etflyjz;
	}

	public void setEtflyjz(Double etflyjz) {
		this.etflyjz = etflyjz;
	}

	public Double getAmountDw() {
		return AmountDw;
	}

	public void setAmountDw(Double amountDw) {
		AmountDw = amountDw;
	}
	
	public Double getYbtc() {
		return ybtc;
	}

	public void setYbtc(Double ybtc) {
		this.ybtc = ybtc;
	}
	public Double getYyyh() {
		return yyyh;
	}

	public void setYyyh(Double yyyh) {
		this.yyyh = yyyh;
	}

	public Double getKqtybgz() {
		return kqtybgz;
	}

	public void setKqtybgz(Double kqtybgz) {
		this.kqtybgz = kqtybgz;
	}
}
