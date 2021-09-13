package com.zebone.nhis.ma.pub.syx.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: tFunctionRequestForPACS 
 *
 * @since 2018-12-12 05:07:31
 */
@Table(value="tFunctionRequestForPACS")
public class Tfunctionrequestforpacs   {

//	@PK
//	@Field(value="FunctionRequestForPACSID",id=KeyId.UUID)
    private Integer functionrequestforpacsid;

	@Field(value="PatientID")
    private String patientid;

	@Field(value="PatientName")
    private String patientname;

	@Field(value="PatientTypeListName")
    private String patienttypelistname;

	@Field(value="PatientSexFlag")
    private String patientsexflag;

	@Field(value="PatientBirthDay")
    private Date patientbirthday;

	@Field(value="InPatientID")
    private String inpatientid;

	@Field(value="IPSeqNoText")
    private String ipseqnotext;

	@Field(value="IPTimes")
    private String iptimes;

	@Field(value="SickBedNo")
    private String sickbedno;

	@Field(value="RegisterID")
    private String registerid;

	@Field(value="RegisterDate")
    private Date registerdate;

	@Field(value="SeqNo")
    private String seqno;

	@Field(value="Phone")
    private String phone;

	@Field(value="Address")
    private String address;

	@Field(value="PostalCode")
    private String postalcode;

	@Field(value="NativePlace")
    private String nativeplace;

	@Field(value="ManageNo")
    private String manageno;

	@Field(value="FunctionRequestID")
    private String functionrequestid;

	@Field(value="RequestDoctorEmployeeID")
    private String requestdoctoremployeeid;

	@Field(value="RequestDoctorEmployeeNo")
    private String requestdoctoremployeeno;

	@Field(value="RequestDoctorEmployeeName")
    private String requestdoctoremployeename;

	@Field(value="RequestDepartmentID")
    private String requestdepartmentid;

	@Field(value="RequestDepartmentNo")
    private String requestdepartmentno;

	@Field(value="RequestDepartmentName")
    private String requestdepartmentname;

	@Field(value="ExamineDepartmentID")
    private String examinedepartmentid;

	@Field(value="ExamineDepartmentNo")
    private String examinedepartmentno;

	@Field(value="ExamineDepartmentName")
    private String examinedepartmentname;

	@Field(value="ICDCode")
    private String icdcode;

	@Field(value="DiseaseName")
    private String diseasename;

	@Field(value="EmergencyFlag")
    private String emergencyflag;

	@Field(value="StatusFlag")
    private String statusflag;

	@Field(value="Flag")
    private String flag;

	@Field(value="ReceiveDateTime")
    private Date receivedatetime;

	@Field(value="ResultDateTime")
    private Date resultdatetime;

	@Field(value="SourceFlag")
    private String sourceflag;

	@Field(value="PacsFlag")
    private String pacsflag;

	@Field(value="TDRequestModelTypeDesc")
    private String tdrequestmodeltypedesc;

	@Field(value="RequestDateTime")
    private Date requestdatetime;

	@Field(value="PatientCardNo")
    private String patientcardno;

	@Field(value="PhysicalRegisterSeqno")
    private String physicalregisterseqno;

	@Field(value="RequestExecutiveDateTime")
    private Date requestexecutivedatetime;

	@Field(value="CollateDateTime")
    private Date collatedatetime;

	@Field(value="PlatformFlag")
    private String platformflag;

	@Field(value="tempflag")
    private String tempflag;


    public Integer getFunctionrequestforpacsid(){
        return this.functionrequestforpacsid;
    }
    public void setFunctionrequestforpacsid(Integer functionrequestforpacsid){
        this.functionrequestforpacsid = functionrequestforpacsid;
    }

    public String getPatientid(){
        return this.patientid;
    }
    public void setPatientid(String patientid){
        this.patientid = patientid;
    }

    public String getPatientname(){
        return this.patientname;
    }
    public void setPatientname(String patientname){
        this.patientname = patientname;
    }

    public String getPatienttypelistname(){
        return this.patienttypelistname;
    }
    public void setPatienttypelistname(String patienttypelistname){
        this.patienttypelistname = patienttypelistname;
    }

    public String getPatientsexflag(){
        return this.patientsexflag;
    }
    public void setPatientsexflag(String patientsexflag){
        this.patientsexflag = patientsexflag;
    }

    public Date getPatientbirthday(){
        return this.patientbirthday;
    }
    public void setPatientbirthday(Date patientbirthday){
        this.patientbirthday = patientbirthday;
    }

    public String getInpatientid(){
        return this.inpatientid;
    }
    public void setInpatientid(String inpatientid){
        this.inpatientid = inpatientid;
    }

    public String getIpseqnotext(){
        return this.ipseqnotext;
    }
    public void setIpseqnotext(String ipseqnotext){
        this.ipseqnotext = ipseqnotext;
    }

    public String getIptimes(){
        return this.iptimes;
    }
    public void setIptimes(String iptimes){
        this.iptimes = iptimes;
    }

    public String getSickbedno(){
        return this.sickbedno;
    }
    public void setSickbedno(String sickbedno){
        this.sickbedno = sickbedno;
    }

    public String getRegisterid(){
        return this.registerid;
    }
    public void setRegisterid(String registerid){
        this.registerid = registerid;
    }

    public Date getRegisterdate(){
        return this.registerdate;
    }
    public void setRegisterdate(Date registerdate){
        this.registerdate = registerdate;
    }

    public String getSeqno(){
        return this.seqno;
    }
    public void setSeqno(String seqno){
        this.seqno = seqno;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }

    public String getPostalcode(){
        return this.postalcode;
    }
    public void setPostalcode(String postalcode){
        this.postalcode = postalcode;
    }

    public String getNativeplace(){
        return this.nativeplace;
    }
    public void setNativeplace(String nativeplace){
        this.nativeplace = nativeplace;
    }

    public String getManageno(){
        return this.manageno;
    }
    public void setManageno(String manageno){
        this.manageno = manageno;
    }

    public String getFunctionrequestid(){
        return this.functionrequestid;
    }
    public void setFunctionrequestid(String functionrequestid){
        this.functionrequestid = functionrequestid;
    }

    public String getRequestdoctoremployeeid(){
        return this.requestdoctoremployeeid;
    }
    public void setRequestdoctoremployeeid(String requestdoctoremployeeid){
        this.requestdoctoremployeeid = requestdoctoremployeeid;
    }

    public String getRequestdoctoremployeeno(){
        return this.requestdoctoremployeeno;
    }
    public void setRequestdoctoremployeeno(String requestdoctoremployeeno){
        this.requestdoctoremployeeno = requestdoctoremployeeno;
    }

    public String getRequestdoctoremployeename(){
        return this.requestdoctoremployeename;
    }
    public void setRequestdoctoremployeename(String requestdoctoremployeename){
        this.requestdoctoremployeename = requestdoctoremployeename;
    }

    public String getRequestdepartmentid(){
        return this.requestdepartmentid;
    }
    public void setRequestdepartmentid(String requestdepartmentid){
        this.requestdepartmentid = requestdepartmentid;
    }

    public String getRequestdepartmentno(){
        return this.requestdepartmentno;
    }
    public void setRequestdepartmentno(String requestdepartmentno){
        this.requestdepartmentno = requestdepartmentno;
    }

    public String getRequestdepartmentname(){
        return this.requestdepartmentname;
    }
    public void setRequestdepartmentname(String requestdepartmentname){
        this.requestdepartmentname = requestdepartmentname;
    }

    public String getExaminedepartmentid(){
        return this.examinedepartmentid;
    }
    public void setExaminedepartmentid(String examinedepartmentid){
        this.examinedepartmentid = examinedepartmentid;
    }

    public String getExaminedepartmentno(){
        return this.examinedepartmentno;
    }
    public void setExaminedepartmentno(String examinedepartmentno){
        this.examinedepartmentno = examinedepartmentno;
    }

    public String getExaminedepartmentname(){
        return this.examinedepartmentname;
    }
    public void setExaminedepartmentname(String examinedepartmentname){
        this.examinedepartmentname = examinedepartmentname;
    }

    public String getIcdcode(){
        return this.icdcode;
    }
    public void setIcdcode(String icdcode){
        this.icdcode = icdcode;
    }

    public String getDiseasename(){
        return this.diseasename;
    }
    public void setDiseasename(String diseasename){
        this.diseasename = diseasename;
    }

    public String getEmergencyflag(){
        return this.emergencyflag;
    }
    public void setEmergencyflag(String emergencyflag){
        this.emergencyflag = emergencyflag;
    }

    public String getStatusflag(){
        return this.statusflag;
    }
    public void setStatusflag(String statusflag){
        this.statusflag = statusflag;
    }

    public String getFlag(){
        return this.flag;
    }
    public void setFlag(String flag){
        this.flag = flag;
    }

    public Date getReceivedatetime(){
        return this.receivedatetime;
    }
    public void setReceivedatetime(Date receivedatetime){
        this.receivedatetime = receivedatetime;
    }

    public Date getResultdatetime(){
        return this.resultdatetime;
    }
    public void setResultdatetime(Date resultdatetime){
        this.resultdatetime = resultdatetime;
    }

    public String getSourceflag(){
        return this.sourceflag;
    }
    public void setSourceflag(String sourceflag){
        this.sourceflag = sourceflag;
    }

    public String getPacsflag(){
        return this.pacsflag;
    }
    public void setPacsflag(String pacsflag){
        this.pacsflag = pacsflag;
    }

    public String getTdrequestmodeltypedesc(){
        return this.tdrequestmodeltypedesc;
    }
    public void setTdrequestmodeltypedesc(String tdrequestmodeltypedesc){
        this.tdrequestmodeltypedesc = tdrequestmodeltypedesc;
    }

    public Date getRequestdatetime(){
        return this.requestdatetime;
    }
    public void setRequestdatetime(Date requestdatetime){
        this.requestdatetime = requestdatetime;
    }

    public String getPatientcardno(){
        return this.patientcardno;
    }
    public void setPatientcardno(String patientcardno){
        this.patientcardno = patientcardno;
    }

    public String getPhysicalregisterseqno(){
        return this.physicalregisterseqno;
    }
    public void setPhysicalregisterseqno(String physicalregisterseqno){
        this.physicalregisterseqno = physicalregisterseqno;
    }

    public Date getRequestexecutivedatetime(){
        return this.requestexecutivedatetime;
    }
    public void setRequestexecutivedatetime(Date requestexecutivedatetime){
        this.requestexecutivedatetime = requestexecutivedatetime;
    }

    public Date getCollatedatetime(){
        return this.collatedatetime;
    }
    public void setCollatedatetime(Date collatedatetime){
        this.collatedatetime = collatedatetime;
    }

    public String getPlatformflag(){
        return this.platformflag;
    }
    public void setPlatformflag(String platformflag){
        this.platformflag = platformflag;
    }

    public String getTempflag(){
        return this.tempflag;
    }
    public void setTempflag(String tempflag){
        this.tempflag = tempflag;
    }
}