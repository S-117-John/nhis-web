package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 报告信息（简化）
 * @author wwx
 *
 */
public class ReportInfo {
	
	private String operationType;//获取报告列表成功失败标识
	private String reportOrgName;//机构名称
	private String reportDate;//报告时间
	private String reportTitle;// 报告信息
	private String errorCode;//错误代码
	private String errorName;//错误信息
	
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getReportOrgName() {
		return reportOrgName;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorName() {
		return errorName;
	}
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}
	public void setReportOrgName(String reportOrgName) {
		this.reportOrgName = reportOrgName;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
}
