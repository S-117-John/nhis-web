package com.zebone.nhis.bl.pub.vo;

public class InvalidStInv {
	//2.3作废发票 list
			private  String pkInvcate;//作废票据分类主键
			private String pkEmpinvoice;//票据领用主键
			private String codeInv;//作废发票号
			public String getPkInvcate() {
				return pkInvcate;
			}
			public void setPkInvcate(String pkInvcate) {
				this.pkInvcate = pkInvcate;
			}
			public String getPkEmpinvoice() {
				return pkEmpinvoice;
			}
			public void setPkEmpinvoice(String pkEmpinvoice) {
				this.pkEmpinvoice = pkEmpinvoice;
			}
			public String getCodeInv() {
				return codeInv;
			}
			public void setCodeInv(String codeInv) {
				this.codeInv = codeInv;
			}
}
