package com.zebone.nhis.ma.pub.syx.vo;


import java.util.Date;
import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: tOrders 
 *
 * @since 2018-12-19 10:15:04
 */
@Table(value="tOrders")
public class Torders   {

	@Field(value="InPatientid")
    private Integer inpatientid;

	@Field(value="IPSeqnotext")
    private String ipseqnotext;

	@Field(value="PatientName")
    private String patientname;

	@Field(value="SexFlag")
    private Integer sexflag;

	@Field(value="Age")
    private String age;

	@Field(value="Birthday")
    private Date birthday;

	@Field(value="IPtimes")
    private Integer iptimes;

	@Field(value="SickbedNo")
    private String sickbedno;

	@Field(value="ExecDAID")
    private Integer execdaid;

	@Field(value="Sourcetype")
    private Integer sourcetype;

	@Field(value="Sourceid")
    private Integer sourceid;

	@Field(value="NO")
    private String no;

	@Field(value="Name")
    private String name;

	@Field(value="Spec")
    private String spec;

	@Field(value="DosetypeName")
    private String dosetypename;

	@Field(value="Dosage")
    private BigDecimal dosage;

	@Field(value="DosageUnit")
    private String dosageunit;

	@Field(value="PackageUnit")
    private String packageunit;

	@Field(value="MedicineUsingMethodName")
    private String medicineusingmethodname;

	@Field(value="Exectimes")
    private Integer exectimes;

	@Field(value="DosagePerTime")
    private BigDecimal dosagepertime;

	@Field(value="EmployeeID")
    private Integer employeeid;

	@Field(value="EmployeeNO")
    private String employeeno;

	@Field(value="EmployeeName")
    private String employeename;

	@Field(value="DepartMentID")
    private Integer departmentid;

	@Field(value="DepartmentNO")
    private String departmentno;

	@Field(value="DepartmentName")
    private String departmentname;

	@Field(value="SendDatetime")
    private Date senddatetime;

	@Field(value="DieaseName")
    private String dieasename;

	@Field(value="DaStartDateTime")
    private Date dastartdatetime;

	@Field(value="DaStopDatetime")
    private Date dastopdatetime;

	@Field(value="TakingMedicineTimeDesc")
    private String takingmedicinetimedesc;

	@Field(value="TakingMedicineTime")
    private String takingmedicinetime;

	@Field(value="DASeqno")
    private String daseqno;

	@Field(value="ExecDAListID")
    private String execdalistid;

	@Field(value="UnitPerTime")
    private BigDecimal unitpertime;

	@Field(value="ExecDate")
    private Date execdate;

	@Field(value="Description")
    private String description;

	@Field(value="DAType")
    private Integer datype;

	@Field(value="ZBFlag")
    private Integer zbflag;

	@Field(value="CancelFlag")
    private Integer cancelflag;

	@Field(value="CancelTime")
    private Date canceltime;

	@Field(value="CNSMFlag")
    private Integer cnsmflag;

	@Field(value="CNSMTime")
    private Date cnsmtime;

	@Field(value="DAID")
    private String daid;


    public Integer getInpatientid(){
        return this.inpatientid;
    }
    public void setInpatientid(Integer inpatientid){
        this.inpatientid = inpatientid;
    }

    public String getIpseqnotext(){
        return this.ipseqnotext;
    }
    public void setIpseqnotext(String ipseqnotext){
        this.ipseqnotext = ipseqnotext;
    }

    public String getPatientname(){
        return this.patientname;
    }
    public void setPatientname(String patientname){
        this.patientname = patientname;
    }

    public Integer getSexflag(){
        return this.sexflag;
    }
    public void setSexflag(Integer sexflag){
        this.sexflag = sexflag;
    }

    public String getAge(){
        return this.age;
    }
    public void setAge(String age){
        this.age = age;
    }

    public Date getBirthday(){
        return this.birthday;
    }
    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    public Integer getIptimes(){
        return this.iptimes;
    }
    public void setIptimes(Integer iptimes){
        this.iptimes = iptimes;
    }

    public String getSickbedno(){
        return this.sickbedno;
    }
    public void setSickbedno(String sickbedno){
        this.sickbedno = sickbedno;
    }

    public Integer getExecdaid(){
        return this.execdaid;
    }
    public void setExecdaid(Integer execdaid){
        this.execdaid = execdaid;
    }

    public Integer getSourcetype(){
        return this.sourcetype;
    }
    public void setSourcetype(Integer sourcetype){
        this.sourcetype = sourcetype;
    }

    public Integer getSourceid(){
        return this.sourceid;
    }
    public void setSourceid(Integer sourceid){
        this.sourceid = sourceid;
    }

    public String getNo(){
        return this.no;
    }
    public void setNo(String no){
        this.no = no;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public String getDosetypename(){
        return this.dosetypename;
    }
    public void setDosetypename(String dosetypename){
        this.dosetypename = dosetypename;
    }

    public BigDecimal getDosage(){
        return this.dosage;
    }
    public void setDosage(BigDecimal dosage){
        this.dosage = dosage;
    }

    public String getDosageunit(){
        return this.dosageunit;
    }
    public void setDosageunit(String dosageunit){
        this.dosageunit = dosageunit;
    }

    public String getPackageunit(){
        return this.packageunit;
    }
    public void setPackageunit(String packageunit){
        this.packageunit = packageunit;
    }

    public String getMedicineusingmethodname(){
        return this.medicineusingmethodname;
    }
    public void setMedicineusingmethodname(String medicineusingmethodname){
        this.medicineusingmethodname = medicineusingmethodname;
    }

    public Integer getExectimes(){
        return this.exectimes;
    }
    public void setExectimes(Integer exectimes){
        this.exectimes = exectimes;
    }

    public BigDecimal getDosagepertime(){
        return this.dosagepertime;
    }
    public void setDosagepertime(BigDecimal dosagepertime){
        this.dosagepertime = dosagepertime;
    }

    public Integer getEmployeeid(){
        return this.employeeid;
    }
    public void setEmployeeid(Integer employeeid){
        this.employeeid = employeeid;
    }

    public String getEmployeeno(){
        return this.employeeno;
    }
    public void setEmployeeno(String employeeno){
        this.employeeno = employeeno;
    }

    public String getEmployeename(){
        return this.employeename;
    }
    public void setEmployeename(String employeename){
        this.employeename = employeename;
    }

    public Integer getDepartmentid(){
        return this.departmentid;
    }
    public void setDepartmentid(Integer departmentid){
        this.departmentid = departmentid;
    }

    public String getDepartmentno(){
        return this.departmentno;
    }
    public void setDepartmentno(String departmentno){
        this.departmentno = departmentno;
    }

    public String getDepartmentname(){
        return this.departmentname;
    }
    public void setDepartmentname(String departmentname){
        this.departmentname = departmentname;
    }

    public Date getSenddatetime(){
        return this.senddatetime;
    }
    public void setSenddatetime(Date senddatetime){
        this.senddatetime = senddatetime;
    }

    public String getDieasename(){
        return this.dieasename;
    }
    public void setDieasename(String dieasename){
        this.dieasename = dieasename;
    }

    public Date getDastartdatetime(){
        return this.dastartdatetime;
    }
    public void setDastartdatetime(Date dastartdatetime){
        this.dastartdatetime = dastartdatetime;
    }

    public Date getDastopdatetime(){
        return this.dastopdatetime;
    }
    public void setDastopdatetime(Date dastopdatetime){
        this.dastopdatetime = dastopdatetime;
    }

    public String getTakingmedicinetimedesc(){
        return this.takingmedicinetimedesc;
    }
    public void setTakingmedicinetimedesc(String takingmedicinetimedesc){
        this.takingmedicinetimedesc = takingmedicinetimedesc;
    }

    public String getTakingmedicinetime(){
        return this.takingmedicinetime;
    }
    public void setTakingmedicinetime(String takingmedicinetime){
        this.takingmedicinetime = takingmedicinetime;
    }

    public String getDaseqno(){
        return this.daseqno;
    }
    public void setDaseqno(String daseqno){
        this.daseqno = daseqno;
    }

    public String getExecdalistid(){
        return this.execdalistid;
    }
    public void setExecdalistid(String execdalistid){
        this.execdalistid = execdalistid;
    }

    public BigDecimal getUnitpertime(){
        return this.unitpertime;
    }
    public void setUnitpertime(BigDecimal unitpertime){
        this.unitpertime = unitpertime;
    }

    public Date getExecdate(){
        return this.execdate;
    }
    public void setExecdate(Date execdate){
        this.execdate = execdate;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public Integer getDatype(){
        return this.datype;
    }
    public void setDatype(Integer datype){
        this.datype = datype;
    }

    public Integer getZbflag(){
        return this.zbflag;
    }
    public void setZbflag(Integer zbflag){
        this.zbflag = zbflag;
    }

    public Integer getCancelflag(){
        return this.cancelflag;
    }
    public void setCancelflag(Integer cancelflag){
        this.cancelflag = cancelflag;
    }

    public Date getCanceltime(){
        return this.canceltime;
    }
    public void setCanceltime(Date canceltime){
        this.canceltime = canceltime;
    }

    public Integer getCnsmflag(){
        return this.cnsmflag;
    }
    public void setCnsmflag(Integer cnsmflag){
        this.cnsmflag = cnsmflag;
    }

    public Date getCnsmtime(){
        return this.cnsmtime;
    }
    public void setCnsmtime(Date cnsmtime){
        this.cnsmtime = cnsmtime;
    }

    public String getDaid(){
        return this.daid;
    }
    public void setDaid(String daid){
        this.daid = daid;
    }
}