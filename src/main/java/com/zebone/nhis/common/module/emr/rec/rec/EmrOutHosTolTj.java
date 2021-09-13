package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * @author liH
 * @version 1.0
 * @date 2021/1/11 18:32
 * @description
 * @currentMinute zebone_CZ
 */
@Table(value="EMR_OUT_HOSTOTAL_TJ")
public class EmrOutHosTolTj  extends BaseModule {

	@PK
	@Field(value="PK_REC",id= KeyId.UUID)
	private String pkRec;

	@Field(value="PATIENT_ID")
	private String patientId;

	@Field(value="TIMES")
	private String times;

	@Field(value="HOS_SITUATION")
	private String hospitalizationSituation;

	@Field(value="DT_PROCESS")
	private String dtProcess;

	@Field(value="HOS_STATUS")
	private String leaveHospitalStatus;

	@Field(value="DOC_ADVICE")
	private String leaveDoctorAdvice;

	@Field(value="DOC_NAME")
	private String docName;

	public String getPkRec() {
		return pkRec;
	}

	public void setPkRec(String pkRec) {
		this.pkRec = pkRec;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
	}

	public String getHospitalizationSituation() {
		return hospitalizationSituation;
	}

	public void setHospitalizationSituation(String hospitalizationSituation) {
		this.hospitalizationSituation = hospitalizationSituation;
	}

	public String getDtProcess() {
		return dtProcess;
	}

	public void setDtProcess(String dtProcess) {
		this.dtProcess = dtProcess;
	}

	public String getLeaveHospitalStatus() {
		return leaveHospitalStatus;
	}

	public void setLeaveHospitalStatus(String leaveHospitalStatus) {
		this.leaveHospitalStatus = leaveHospitalStatus;
	}

	public String getLeaveDoctorAdvice() {
		return leaveDoctorAdvice;
	}

	public void setLeaveDoctorAdvice(String leaveDoctorAdvice) {
		this.leaveDoctorAdvice = leaveDoctorAdvice;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}
