package com.zebone.nhis.bl.pub.vo;

import java.util.Date;
import java.util.List;

/**
 * 门诊汇总结账信息
 *  
 * @author wangpeng
 * @date 2016年10月27日
 *
 */
public class OpBlCcLeaderVo {
	
	/** 结账付款信息 */
	private List<OpBlCcPayAndPiVo> operList;
	
	/** 付款方式 */
	private List<OpBlCcPayAndPiVo> paymodeList;
	
	/** 开始时间 */
	private Date dateBegin;
	
	/** 截止时间 */
	private Date dateEnd;
	
	/** 汇总人 */
	private String nameEmpLeader;

	public List<OpBlCcPayAndPiVo> getOperList() {
		return operList;
	}

	public void setOperList(List<OpBlCcPayAndPiVo> operList) {
		this.operList = operList;
	}

	public List<OpBlCcPayAndPiVo> getPaymodeList() {
		return paymodeList;
	}

	public void setPaymodeList(List<OpBlCcPayAndPiVo> paymodeList) {
		this.paymodeList = paymodeList;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public String getNameEmpLeader() {
		return nameEmpLeader;
	}

	public void setNameEmpLeader(String nameEmpLeader) {
		this.nameEmpLeader = nameEmpLeader;
	}
	
}
