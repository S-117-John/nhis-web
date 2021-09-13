package com.zebone.nhis.sch.hd.vo;

import java.util.Date;

public class WeekVo {
		//排班PK
		private String pkSchhd;
		
		//排班日期
		private Date dateHd;
		
		//患者PK
		private String pkPi;
		
		//患者姓名
		private String namePi;

		public String getPkSchhd() {
			return pkSchhd;
		}

		public void setPkSchhd(String pkSchhd) {
			this.pkSchhd = pkSchhd;
		}

		public Date getDateHd() {
			return dateHd;
		}

		public void setDateHd(Date dateHd) {
			this.dateHd = dateHd;
		}

		public String getPkPi() {
			return pkPi;
		}

		public void setPkPi(String pkPi) {
			this.pkPi = pkPi;
		}

		public String getNamePi() {
			return namePi;
		}

		public void setNamePi(String namePi) {
			this.namePi = namePi;
		}
		
		
}
