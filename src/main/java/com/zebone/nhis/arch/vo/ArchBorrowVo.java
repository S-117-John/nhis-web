package com.zebone.nhis.arch.vo;

import java.util.Date;

public class ArchBorrowVo {
	
	 private String codePv;//     --就诊号码
     private String namePi;//     --病人姓名
     private String codePi;//     --患者编码
     private int ipTimes;//    --住院次数
     private String nameDept;// --科室
     private String nameEmpPhy;//--医生
     private String descDiag;// --诊断
     private Date dateEnd;//    --出院日期
     private String pkBorrow;//   --借阅主键
     private Date dateAp;//     --申请日期
     private Date dateExpire;//    --失效日期
     private int days;//        --借阅天数
     private Date dateChk;//    --审核日期
     private String nameEmpChk;//--审核人
     private String euStatus;//   --状态
     private String pkArchive;// --归档主键
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public int getIpTimes() {
		return ipTimes;
	}
	public void setIpTimes(int ipTimes) {
		this.ipTimes = ipTimes;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameEmpPhy() {
		return nameEmpPhy;
	}
	public void setNameEmpPhy(String nameEmpPhy) {
		this.nameEmpPhy = nameEmpPhy;
	}
	public String getDescDiag() {
		return descDiag;
	}
	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getPkBorrow() {
		return pkBorrow;
	}
	public void setPkBorrow(String pkBorrow) {
		this.pkBorrow = pkBorrow;
	}
	public Date getDateAp() {
		return dateAp;
	}
	public void setDateAp(Date dateAp) {
		this.dateAp = dateAp;
	}
	public Date getDateExpire() {
		return dateExpire;
	}
	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	public Date getDateChk() {
		return dateChk;
	}
	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	public String getNameEmpChk() {
		return nameEmpChk;
	}
	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getPkArchive() {
		return pkArchive;
	}
	public void setPkArchive(String pkArchive) {
		this.pkArchive = pkArchive;
	}
     
     
     

}
