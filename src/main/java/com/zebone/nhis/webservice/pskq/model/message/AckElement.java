package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.webservice.pskq.model.ReserveOutpatient;

import java.util.List;

public class AckElement {

    private String ackCode;

    private String targetMessageId;

    private String ackDetail;

    @JSONField(name = "LAB_RESULT")
    private LabResultResponse labResult;

    @JSONField(name = "PATIENTS")
    private PatientResultResponse patientResult;

    @JSONField(name = "ORGANIZATIONS")
    private OrganizationResultResponse organizationResultResponse;

    @JSONField(name = "DEPARTMENTS")
    private DepartmentResultResponse departmentResultResponse;

    @JSONField(name = "EMPLOYEES")
    private EmployeeResultResponse employeeResultResponse;
    
    @JSONField(name = "RESERVE_OUTPATIENT")
    private List<DataElement> reserveList;

    public EmployeeResultResponse getEmployeeResultResponse() {
        return employeeResultResponse;
    }

    public void setEmployeeResultResponse(EmployeeResultResponse employeeResultResponse) {
        this.employeeResultResponse = employeeResultResponse;
    }

    public DepartmentResultResponse getDepartmentResultResponse() {
        return departmentResultResponse;
    }

    public void setDepartmentResultResponse(DepartmentResultResponse departmentResultResponse) {
        this.departmentResultResponse = departmentResultResponse;
    }

    public OrganizationResultResponse getOrganizationResultResponse() {
        return organizationResultResponse;
    }

    public void setOrganizationResultResponse(OrganizationResultResponse organizationResultResponse) {
        this.organizationResultResponse = organizationResultResponse;
    }

    public PatientResultResponse getPatientResult() {
        return patientResult;
    }

    public void setPatientResult(PatientResultResponse patientResult) {
        this.patientResult = patientResult;
    }

    public LabResultResponse getLabResult() {
        return labResult;
    }

    public void setLabResult(LabResultResponse labResult) {
        this.labResult = labResult;
    }

    public String getAckCode() {
        return ackCode;
    }

    public void setAckCode(String ackCode) {
        this.ackCode = ackCode;
    }

    public String getTargetMessageId() {
        return targetMessageId;
    }

    public void setTargetMessageId(String targetMessageId) {
        this.targetMessageId = targetMessageId;
    }

    public String getAckDetail() {
        return ackDetail;
    }

    public void setAckDetail(String ackDetail) {
        this.ackDetail = ackDetail;
    }

	public List<DataElement> getReserveList() {
		return reserveList;
	}

	public void setReserveList(List<DataElement> reserveList) {
		this.reserveList = reserveList;
	}

	
}
