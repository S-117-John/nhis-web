package com.zebone.nhis.pro.zsba.compay.ins.sngsyb.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="ins_st_wi_details")
public class InsStWiDetails extends BaseModule {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1525965955297142417L;
	
	@PK
	@Field(value="pk_insstwidetail",id=KeyId.UUID)
	private String pkInsstwidetail;//主键

	
	@Field(value="pk_insstwilist")
	private String pkInsstwilist;//结算单主键

	@Field(value="bka800")
	private String bka800;//名称
	
	@Field(value="bka801_1")
	private String bka801_1;//个人现金

	
	@Field(value="bka801_2")
	private String bka801_2;//基本医疗保障

	
	@Field(value="bka801_3")
	private String bka801_3;//补充保险支付

	
	@Field(value="bka801_4")
	private String bka801_4;//工伤保险统筹基金基本药物补助

	
	@Field(value="bka801_5")
	private String bka801_5;//重大疾病补助

	
	@Field(value="bka801_6")
	private String bka801_6;//其他补助

	
	@Field(value="bka801_7")
	private String bka801_7;//合计

	public String getPkInsstwidetail() {
		return pkInsstwidetail;
	}


	public void setPkInsstwidetail(String pkInsstwidetail) {
		this.pkInsstwidetail = pkInsstwidetail;
	}


	public String getPkInsstwilist() {
		return pkInsstwilist;
	}


	public void setPkInsstwilist(String pkInsstwilist) {
		this.pkInsstwilist = pkInsstwilist;
	}


	public String getBka800() {
		return bka800;
	}


	public void setBka800(String bka800) {
		this.bka800 = bka800;
	}


	public String getBka801_1() {
		return bka801_1;
	}


	public void setBka801_1(String bka801_1) {
		this.bka801_1 = bka801_1;
	}


	public String getBka801_2() {
		return bka801_2;
	}


	public void setBka801_2(String bka801_2) {
		this.bka801_2 = bka801_2;
	}


	public String getBka801_3() {
		return bka801_3;
	}


	public void setBka801_3(String bka801_3) {
		this.bka801_3 = bka801_3;
	}


	public String getBka801_4() {
		return bka801_4;
	}


	public void setBka801_4(String bka801_4) {
		this.bka801_4 = bka801_4;
	}


	public String getBka801_5() {
		return bka801_5;
	}


	public void setBka801_5(String bka801_5) {
		this.bka801_5 = bka801_5;
	}


	public String getBka801_6() {
		return bka801_6;
	}


	public void setBka801_6(String bka801_6) {
		this.bka801_6 = bka801_6;
	}


	public String getBka801_7() {
		return bka801_7;
	}


	public void setBka801_7(String bka801_7) {
		this.bka801_7 = bka801_7;
	}

}
