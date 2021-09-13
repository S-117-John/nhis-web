package com.zebone.nhis.bl.pub.vo;

public class BlSettleDetailVo {
	 /// <summary>
    /// --医保类型
    /// </summary>
	private String hp ;
    
   /// <summary>
   ///  --支付方
   /// </summary>
   private String payer ;

   private double amount ;
   
   private double bedquota;

public String getHp() {
	return hp;
}

public void setHp(String hp) {
	this.hp = hp;
}

public String getPayer() {
	return payer;
}

public void setPayer(String payer) {
	this.payer = payer;
}

public double getAmount() {
	return amount;
}

public void setAmount(double amount) {
	this.amount = amount;
}

public double getBedquota() {
	return bedquota;
}

public void setBedquota(double bedquota) {
	this.bedquota = bedquota;
}
   
}
