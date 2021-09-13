package com.zebone.nhis.webservice.zsrm.vo.self;

public class ResponseAppointNucVo {
	
	private String code;
	private String severity;
	private String diagnostics;
	private String codePv;
	private String applyNo;
	
	public String getDiagnostics() {
		return diagnostics;
	}
	public void setDiagnostics(String diagnostics) {
		this.diagnostics = diagnostics;
	}
	public String getCode() {
		return code;
	}
	public String getSeverity() {
		return severity;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	
}
