package com.zebone.nhis.pro.zsba.ex.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;

public class ExVtsOccVo extends ExVtsOcc{
	
	private String bedNo;  
	private String codePv;
	private String codeIp;
	private String namePi;
    private String pkPvAs;//pv表的pk_pv,以这个字段为准
    private int sortNo;
    private String changein;//新入院/转入病人
	private String critical;//普通病区危重病人
	private String operationexd;//次日手术病人
	private String operationtod;//今日手术病人
	private String operationedn;//手术后病人
	private String fever;//发热病人（37.5℃≤T≤38.4℃）
	private String heat;//高热病人（T≧38.5℃）
	private int ipDays;//住院天数
	private Date dateBegin;//就诊开始时间
	private Date dateEnd;//就诊结束时间
	private Date dateInDept;//入科时间
	private String agePv;//年龄
    
    private List<ExVtsoccDtVo> details;

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPkPvAs() {
		return pkPvAs;
	}

	public void setPkPvAs(String pkPvAs) {
		this.pkPvAs = pkPvAs;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}



	public List<ExVtsoccDtVo> getDetails() {
		return details;
	}

	public void setDetails(List<ExVtsoccDtVo> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "ExVtsOccVo [bedNo=" + bedNo + ", codePv=" + codePv
				+ ", codeIp=" + codeIp + ", namePi=" + namePi + ", pkPvAs="
				+ pkPvAs + ", sortNo=" + sortNo + ", details=" + details + "]";
	}

	public String getChangein() {return changein;}
	public void setChangein(String changein) {this.changein = changein;}

	public String getCritical() {return critical;}
	public void setCritical(String critical) {this.critical = critical;}

	public String getOperationexd() {return operationexd;}
	public void setOperationexd(String operationexd) {this.operationexd = operationexd;}

	public String getOperationtod() {return operationtod;}
	public void setOperationtod(String operationtod) {this.operationtod = operationtod;}

	public String getOperationedn() {return operationedn;}
	public void setOperationedn(String operationedn) {this.operationedn = operationedn;}

	public String getFever() {return fever;}
	public void setFever(String fever) {this.fever = fever;}

	public String getHeat() {return heat;}
	public void setHeat(String heat) {this.heat = heat;}

	public int getIpDays() { return ipDays; }

	public void setIpDays(int ipDays) { this.ipDays = ipDays; }

	public Date getDateBegin() {return dateBegin;}

	public void setDateBegin(Date dateBegin) {this.dateBegin = dateBegin;}

	public Date getDateEnd() {return dateEnd;}

	public void setDateEnd(Date dateEnd) {this.dateEnd = dateEnd;}

	public Date getDateInDept() {return dateInDept;}

	public void setDateInDept(Date dateInDept) {this.dateInDept = dateInDept;}

	public String getAgePv() {
		return agePv;
	}

	public void setAgePv(String agePv) {
		this.agePv = agePv;
	}
	
}
