package com.zebone.nhis.common.module.bl;
import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BL_SETTLE_AR - bl_settle_ar 
 *
 * @since 2016-10-25 02:11:55
 */
@Table(value="BL_SETTLE_AR")
public class BlSettleAr extends BaseModule  {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		@PK
		@Field(value="PK_SETTLEAR",id=KeyId.UUID)
	    private String pkSettlear;

		@Field(value="PK_SETTLE")
	    private String pkSettle;

		@Field(value="AMT_AR")
	    private Double amtAr;

		@Field(value="AMT_PAY")
	    private Double amtPay;

		@Field(value="FLAG_CL")
	    private String flagCl;

		@Field(value="MODITY_TIME")
	    private Date modityTime;
		
		@Field(value="PK_EMP_PAY")
		private String pkEmpPay;
		
		@Field(value="NAME_EMP_PAY")
		private String nameEmpPay;
		
		@Field(value="FLAG_CC")
		private String flagCc;
		
		@Field(value="PK_CC")
		private String pkCc;
		
		@Field(value="DATE_PAY")
		private Date datePay;
		
		public Date getDatePay() {
			return datePay;
		}
		public void setDatePay(Date datePay) {
			this.datePay = datePay;
		}
		public String getPkEmpPay() {
			return pkEmpPay;
		}
		public void setPkEmpPay(String pkEmpPay) {
			this.pkEmpPay = pkEmpPay;
		}
		public String getNameEmpPay() {
			return nameEmpPay;
		}
		public void setNameEmpPay(String nameEmpPay) {
			this.nameEmpPay = nameEmpPay;
		}
		public String getFlagCc() {
			return flagCc;
		}
		public void setFlagCc(String flagCc) {
			this.flagCc = flagCc;
		}
		public String getPkCc() {
			return pkCc;
		}
		public void setPkCc(String pkCc) {
			this.pkCc = pkCc;
		}
		public String getPkSettlear(){
	        return this.pkSettlear;
	    }
	    public void setPkSettlear(String pkSettlear){
	        this.pkSettlear = pkSettlear;
	    }

	    public String getPkSettle(){
	        return this.pkSettle;
	    }
	    public void setPkSettle(String pkSettle){
	        this.pkSettle = pkSettle;
	    }

	    public Double getAmtAr(){
	        return this.amtAr;
	    }
	    public void setAmtAr(Double amtAr){
	        this.amtAr = amtAr;
	    }

	    public Double getAmtPay(){
	        return this.amtPay;
	    }
	    public void setAmtPay(Double amtPay){
	        this.amtPay = amtPay;
	    }

	    public String getFlagCl(){
	        return this.flagCl;
	    }
	    public void setFlagCl(String flagCl){
	        this.flagCl = flagCl;
	    }

	    public Date getModityTime(){
	        return this.modityTime;
	    }
	    public void setModityTime(Date modityTime){
	        this.modityTime = modityTime;
	    }
	}


