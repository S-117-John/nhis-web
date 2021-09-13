package com.zebone.nhis.webservice.syx.vo.emr;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.emr.rec.rec.vo.PvDiagVo;

/**
 *
 * @author 
 */
@JsonRootName("home_page")
public class EmrAfHomePage{
    /**
     * 
     */
	@JsonIgnore
    private String pkPage;
    /**
     * 
     */
	@JsonIgnore
    private String pkOrg;
    /**
     * 
     */
	@JsonIgnore
    private String pkPi;
    
    /**
     * 
     */
    @JsonIgnore
    private String pkPv;
    /**
     * 
     */
    @JsonProperty("hospital_id")
    private String medOrgCode;
    /**
     * 
     */
    @JsonProperty("hospital_name")
    private String medOrgName;
    /**
     * 
     */
    @JsonProperty("medical_card_num")
    private String insurNo;
    
    @JsonProperty("health_card_num")
    private String healthCardNo;
    /**
     * 
     */
    @JsonProperty("pay_type")
    private String medPayMode;
    
    @JsonProperty("admission_num")
    private Integer times;
    
    /**
     * 
     */
    @JsonProperty("inpatient_id")
    private String patNo;
    /**
     * 
     */
    @JsonProperty("inpatient_serial_num")
    private String codePv;
    
    @JsonProperty("name")
    private String name;
    /**
     * 
     */
    
    @JsonProperty("sex")
    private String dtSex;
    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonProperty("birthday")
    private Date birthDate;
    /**
     * 
     */
  
    /**
     * 
     */
    @JsonProperty("age")
    private String ageTxt;
    
    /**
     * 
     */
    @JsonProperty("marital_status")
    private String marryCode;
    /**
     * 
     */
    @JsonIgnore
    private String marryName;
    
    /**
     * 
     */
    @JsonProperty("profession")
    private String occupCode;
    /**
     * 
     */
    @JsonIgnore
    private String occupName;
    
   

    @JsonProperty("birthplace")
    private String birthAddr;
    /**
     * 
     */
    @JsonProperty("birth_province")
    private String birthAddrProv;
    /**
     * 
     */
    @JsonProperty("birth_city")
    private String birthAddrCity;
    /**
     * 
     */
    @JsonProperty("birth_county")
    private String birthAddrCounty;
    
    /**
     * 
     */
    @JsonIgnore
    private String nationCode;
    /**
     * 
     */
    @JsonProperty("nationality")
    private String nationName;
    
    /**
     * 
     */
    @JsonIgnore
    private String countryCode;
    /**
     * 
     */
    @JsonProperty("country")
    private String countryName;
    
    /**
     * 
     */
    @JsonIgnore
    private String idType;
    /**
     * 
     */
    @JsonProperty("identity_card_num")
    private String idNo;


    /**
     * 
     */
    @JsonIgnore
    private String currAddrProv;
    /**
     * 
     */
    @JsonIgnore
    private String currAddrCity;
    /**
     * 
     */
    @JsonIgnore
    private String currAddrCounty;


    
    @JsonProperty("present_addr")
    private String currAddr;
    
    /**
     * 
     */
    @JsonProperty("home_telephone")
    private String currPhone;
    
    @JsonIgnore
    private String currAddrDt;
    
    /**
     * 
     */
    @JsonProperty("post_code")
    private String currZipCode;
    
    @JsonProperty("mobile")
    private String mobile;
    

    
    /**
     * 
     */
    @JsonProperty("work_unit_addr")
    private String workUnit;
    
    /**
     * 
     */
    @JsonProperty("work_unit_post_code")
    private String workUnitZipCode;
    
    /**
     * 
     */
    @JsonIgnore
    private String workUnitPhone;
    
    
    /**
     * 
     */
    @JsonIgnore
    private String resideAddrProv;
    /**
     * 
     */
    @JsonIgnore
    private String resideAddrCity;
    /**
     * 
     */
    @JsonIgnore
    private String resideAddrCounty;
    /**
     * 
     */

    
    @JsonProperty("registered_addr")
    private String resideAddr;
    
    @JsonProperty("registered_post_code")
    private String resideZipCode;
    
    @JsonIgnore
    private String resideAddrDt;
    
    /**
     * 
     */
    @JsonIgnore
    private String originAddrProv;
    /**
     * 
     */
    @JsonIgnore
    private String originAddrCity;
    
    @JsonIgnore
    private String originAddr;
    
    
    /**
     * 
     */
    @JsonProperty("contact_name")
    private String contactName;
    /**
     * 
     */
    @JsonProperty("relationship")
    private String contactRelatCode;
    /**
     * 
     */
    @JsonIgnore
    private String contactRelatName;
    /**
     * 
     */
    @JsonProperty("contact_addr")
    private String contactAddr;
    /**
     * 
     */
    @JsonProperty("contact_mobile")
    private String contactPhone;
    /**
     * 
     */
    @JsonProperty("admission_type")
    private String admitPathCode;
    /**
     * 
     */
    @JsonIgnore
    private String admitPathName;
    /**
     * 
     */
    @JsonProperty("admission_date")
    private Date admitTime;
    /**
     * 
     */
    @JsonIgnore
    private String pkAdmitDept;
    
    @JsonProperty("admission_dep")
    private String admitDeptCode;
    
    /**
     * 
     */
    @JsonProperty("admission_ward")
    private String admitDeptName;
    /**
     * 
     */
    @JsonIgnore
    private String admitWardName;
    /**
     * 患者住院期间转科的转入科室名称，如果超过一次以上的转科，用“→”转接表示
     */
    @JsonProperty("transfer_dep")
    private String transDeptCode;
    
    @JsonIgnore
    private String transDeptNames;
    /**
     * 
     */
    @JsonProperty("discharge_date")
    private Date disTime;
    
    @JsonProperty("discharge_dep")
    private String disDeptCode;
    
    /**
     * 
     */
    @JsonIgnore
    private String pkDeptDis;
    /**
     * 
     */
    @JsonProperty("discharge_ward")
    private String disDeptName;
    /**
     * 
     */
    @JsonIgnore
    private String pkWardDis;
    /**
     * 
     */
    @JsonIgnore
    private String disWardName;
    /**
     * 
     */
    @JsonIgnore
    private String pkDiagClinic;
    /**
     * 
     */
    @JsonProperty("hospital_stay")
    private BigDecimal inHosDays;
    /**
     * 
     */
    @JsonProperty("clinic_diagnosis_code")
    private String diagCodeClinic;
    /**
     * 
     */
    @JsonIgnore
    private String pkDiagDis;
    /**
     * 
     */
    @JsonProperty("clinic_diagnosis_desc")
    private String diagNameClinic;
    
    @JsonProperty("admission_status")
    private String admitCondCode;
    
    @JsonIgnore
    private String admitCondName;
    

    
    @JsonIgnore
    private String pkEmpClinic;
    
    @JsonIgnore
    private String clinicName;
    
    @JsonProperty("admission_diagnosis_code")
    private String diagCodeAdmit;
    
    @JsonProperty("admission_diagnosis_desc")
    private String diagNameAdmit;
    
    @JsonProperty("admission_diagnosed_date")
    private Date diagDate;//确诊日期
    
    /**
     * 
     */
    @JsonProperty("major_diagnosis_code")
    private String diagCodeDis;

    @JsonIgnore
    private String pkDiagExtcIp;

    @JsonProperty("major_diagnosis_desc")
    private String diagNameDis;
    
    
    @JsonProperty("major_diagnosis_admission")
    private String admitCondCode0;

    @JsonProperty("major_diagnosis_discharge")
    private String disCondCode;

    @JsonProperty("other_diagnosis_code_1")
    private String diagCodeDis1;
    
    @JsonProperty("other_diagnosis_desc_1")
    private String diagNameDis1;
    @JsonProperty("other_diagnosis_admission_1")
    private String admitCondCode1;
    
    @JsonProperty("other_diagnosis_admission_2")
    private String admitCondCode2;

    @JsonProperty("other_diagnosis_discharge_2")
    private String disCondCode2;
    
    @JsonProperty("other_diagnosis_code_2")
    private String diagCodeDis2;
    
    @JsonProperty("other_diagnosis_desc_2")
    private String diagNameDis2;
    
    
    @JsonProperty("other_diagnosis_admission_3")
    private String admitCondCode3;

    @JsonProperty("other_diagnosis_discharge_3")
    private String disCondCode3;
    
    @JsonProperty("other_diagnosis_code_3")
    private String diagCodeDis3;
    
    @JsonProperty("other_diagnosis_desc_3")
    private String diagNameDis3;
    
    @JsonProperty("other_diagnosis_admission_4")
    private String admitCondCode4;

    @JsonProperty("other_diagnosis_discharge_4")
    private String disCondCode4;
    
    @JsonProperty("other_diagnosis_code_4")
    private String diagCodeDis4;
    
    @JsonProperty("other_diagnosis_desc_4")
    private String diagNameDis4;
    
    
    @JsonProperty("other_diagnosis_admission_5")
    private String admitCondCode5;

    @JsonProperty("other_diagnosis_discharge_5")
    private String disCondCode5;
    
    @JsonProperty("other_diagnosis_code_5")
    private String diagCodeDis5;
    
    @JsonProperty("other_diagnosis_desc_5")
    private String diagNameDis5;
    
    
    @JsonProperty("other_diagnosis_admission_6")
    private String admitCondCode6;

    @JsonProperty("other_diagnosis_discharge_6")
    private String disCondCode6;
    
    @JsonProperty("other_diagnosis_code_6")
    private String diagCodeDis6;
    
    @JsonProperty("other_diagnosis_desc_6")
    private String diagNameDis6;
    
    
    @JsonProperty("other_diagnosis_admission_7")
    private String admitCondCode7;

    @JsonProperty("other_diagnosis_discharge_7")
    private String disCondCode7;
    
    @JsonProperty("other_diagnosis_code_7")
    private String diagCodeDis7;
    
    @JsonProperty("other_diagnosis_desc_7")
    private String diagNameDis7;
    
    
    @JsonProperty("other_diagnosis_admission_8")
    private String admitCondCode8;

    @JsonProperty("other_diagnosis_discharge_8")
    private String disCondCode8;
    
    @JsonProperty("other_diagnosis_code_8")
    private String diagCodeDis8;
    
    @JsonProperty("other_diagnosis_desc_8")
    private String diagNameDis8;
    
    
    @JsonProperty("other_diagnosis_admission_9")
    private String admitCondCode9;

    @JsonProperty("other_diagnosis_discharge_9")
    private String disCondCode9;
    
    @JsonProperty("other_diagnosis_code_9")
    private String diagCodeDis9;
    
    @JsonProperty("other_diagnosis_desc_9")
    private String diagNameDis9;
    
    
    @JsonProperty("other_diagnosis_admission_10")
    private String admitCondCode10;

    @JsonProperty("other_diagnosis_discharge_10")
    private String disCondCode10;
    
    @JsonProperty("other_diagnosis_code_10")
    private String diagCodeDis10;
    
    @JsonProperty("other_diagnosis_desc_10")
    private String diagNameDis10;
    
    @JsonProperty("infection_counts")
    private Integer numInfect;
    
    @JsonProperty("pathological_diagnosis_code_1")
    private String diagCodePatho;
    /**
     * 
     */
    @JsonProperty("pathological_diagnosis_name_1")
    private String diagNamePatho;
    /**
     * 
     */
    @JsonProperty("pathological_number_1")
    private String pathoNo;
    
    @JsonProperty("pathological_diagnosis_code_2")
    private String diagCodePatho2;
    /**
     * 
     */
    @JsonProperty("pathological_diagnosis_name_2")
    private String diagNamePatho2;
    /**
     * 
     */
    @JsonProperty("pathological_number_2")
    private String pathoNo2;
    
    @JsonProperty("pathological_diagnosis_code_3")
    private String diagCodePatho3;
    /**
     * 
     */
    @JsonProperty("pathological_diagnosis_name_3")
    private String diagNamePatho3;
    /**
     * 
     */
    @JsonProperty("pathological_number_3")
    private String pathoNo3;
    
    /**
     * 
     */
    @JsonProperty("injure_poison_code_1")
    private String diagCodeExtcIp;
    /**
     * 
     */
    @JsonIgnore
    private String pkDiagPatho;
    /**
     * 
     */
    @JsonProperty("injure_poison_name_1")
    private String diagNameExtcIp;
    
    @JsonProperty("injure_poison_code_2")
    private String diagCodeExtcIp2;

    @JsonProperty("injure_poison_name_2")
    private String diagNameExtcIp2;
    
    
    @JsonProperty("injure_poison_code_3")
    private String diagCodeExtcIp3;

    @JsonProperty("injure_poison_name_3")
    private String diagNameExtcIp3;
    
    @JsonIgnore
    private String flagDrugAllergy;
    
    @JsonProperty("allergen")
    private String allergen;
    
    @JsonProperty("allergy_drugs")
    private String allergicDrug;
    
    @JsonProperty("outpatient_discharge_match")
   	private String diagFitCodeOi;

    @JsonProperty("admission_discharge_match")
   	private String diagFitCodeIo;
    
    @JsonProperty("operation_before_after_match")
   	private String diagFitCodeBAOp;
    
    @JsonProperty("clinic_pathology_match")
   	private String diagFitCodeCp;
   	
    @JsonProperty("irradiation_pathology_match")
   	private String diagFitCodeRc;
    
    @JsonIgnore
    private String medRecType;
    @JsonIgnore
    private String flagCp;
    @JsonProperty("salvage_counts")
    private Integer numRes;
    @JsonProperty("salvage_success_counts")
    private Integer numSucc;

    /**
     * 
     */
    

    @JsonIgnore
    private String pkEmpChief;
    
    @JsonProperty("section_director")
    private String chiefName;
    
    @JsonIgnore
    private String pkEmpDirector;
    /**
     * 
     */
    @JsonProperty("chief_physician")
    private String directorName;
    @JsonIgnore
    private String pkEmpConsult;

    @JsonProperty("attending_physician")
    private String consultName;

    @JsonIgnore
    private String pkEmpRefer;

    @JsonProperty("resident")
    private String referName;
    @JsonIgnore
    private String pkEmpNurse;
    @JsonProperty("primary_nurse")
    private String nurseName;
    @JsonIgnore
    private String pkEmpLearn;
    @JsonProperty("continuing_education_physician")
    private String learnName;
    /**
     * 
     */
    @JsonIgnore
    private String pkEmpIntern;
    /**
     * 
     */
    @JsonProperty("intern_physician")
    private String internName;
    @JsonIgnore
    private String pkEmpCoder;
    @JsonProperty("coder")
    private String coderName;

    @JsonProperty("medical_record_quality")
    private String qualityCode;
    /**
     * 
     */
    @JsonIgnore
    private String qualityName;
    /**
     * 
     */
    @JsonIgnore
    private String pkEmpQcDoc;
    /**
     * 
     */
    @JsonProperty("quality_control_physician")
    private String qcDocName;
    /**
     * 
     */
    @JsonIgnore
    private String pkEmpQcNurse;
    /**
     * 
     */
    @JsonProperty("quality_control_senior_nurse")
    private String qcNurseName;
    /**
     * 
     */
    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonProperty("quality_control_date")
    private Date qcDate;
    
    @JsonProperty("operation_code_1")
    private String opCode1;
    
    @JsonProperty("operation_date_1")
    private Date opDate1;
    
    @JsonProperty("operation_level_1")
    private String gradeCode1;
    
    @JsonProperty("operation_name_1")
    private String opName1;
    
    @JsonProperty("operation_spot_1")
    private String opSite1;
    
    @JsonProperty("operation_last_hours_1")
    private String opDuration1;
    
    @JsonProperty("operator_1")
    private String opDocName1;
    
    @JsonProperty("first_assistant_1")
    private String opiName1;
    
    @JsonProperty("second_assistant_1")
    private String opiiName1;
    
    @JsonProperty("anesthesia_method_1")
    private String anesTypeCode1;
    
    @JsonProperty("anesthesia_level_1")
    private String anesLevelCode1;
    
    @JsonProperty("wound_healing_level_1")
    private String incisionTypeCode1;
    
    @JsonProperty("anesthesia_physician_1")
    private String anesDocName1;
    
    @JsonProperty("operation_code_2")
    private String opCode2;
    
    @JsonProperty("operation_date_2")
    private Date opDate2;
    
    @JsonProperty("operation_level_2")
    private String gradeCode2;
    
    @JsonProperty("operation_name_2")
    private String opName2;
    
    @JsonProperty("operation_spot_2")
    private String opSite2;
    
    @JsonProperty("operation_last_hours_2")
    private String opDuration2;
    
    @JsonProperty("operator_2")
    private String opDocName2;
    
    @JsonProperty("first_assistant_2")
    private String opiName2;
    
    @JsonProperty("second_assistant_2")
    private String opiiName2;
    
    @JsonProperty("anesthesia_method_2")
    private String anesTypeCode2;
    
    @JsonProperty("anesthesia_level_2")
    private String anesLevelCode2;
    
    @JsonProperty("wound_healing_level_2")
    private String incisionTypeCode2;
    
    @JsonProperty("anesthesia_physician_2")
    private String anesDocName2;
    
    @JsonProperty("operation_code_3")
    private String opCode3;
    
    @JsonProperty("operation_date_3")
    private Date opDate3;
    
    @JsonProperty("operation_level_3")
    private String gradeCode3;
    
    @JsonProperty("operation_name_3")
    private String opName3;
    
    @JsonProperty("operation_spot_3")
    private String opSite3;
    
    @JsonProperty("operation_last_hours_3")
    private String opDuration3;
    
    @JsonProperty("operator_3")
    private String opDocName3;
    
    @JsonProperty("first_assistant_3")
    private String opiName3;
    
    @JsonProperty("second_assistant_3")
    private String opiiName3;
    
    @JsonProperty("anesthesia_method_3")
    private String anesTypeCode3;
    
    @JsonProperty("anesthesia_level_3")
    private String anesLevelCode3;
    
    @JsonProperty("wound_healing_level_3")
    private String incisionTypeCode3;
    
    @JsonProperty("anesthesia_physician_3")
    private String anesDocName3;
    
    @JsonProperty("operation_code_4")
    private String opCode4;
    
    @JsonProperty("operation_date_4")
    private Date opDate4;
    
    @JsonProperty("operation_level_4")
    private String gradeCode4;
    
    @JsonProperty("operation_name_4")
    private String opName4;
    
    @JsonProperty("operation_spot_4")
    private String opSite4;
    
    @JsonProperty("operation_last_hours_4")
    private String opDuration4;
    
    @JsonProperty("operator_4")
    private String opDocName4;
    
    @JsonProperty("first_assistant_4")
    private String opiName4;
    
    @JsonProperty("second_assistant_4")
    private String opiiName4;
    
    @JsonProperty("anesthesia_method_4")
    private String anesTypeCode4;
    
    @JsonProperty("anesthesia_level_4")
    private String anesLevelCode4;
    
    @JsonProperty("wound_healing_level_4")
    private String incisionTypeCode4;
    
    @JsonProperty("anesthesia_physician_4")
    private String anesDocName4;
    
    @JsonProperty("operation_code_5")
    private String opCode5;
    
    @JsonProperty("operation_date_5")
    private Date opDate5;
    
    @JsonProperty("operation_level_5")
    private String gradeCode5;
    
    @JsonProperty("operation_name_5")
    private String opName5;
    
    @JsonProperty("operation_spot_5")
    private String opSite5;
    
    @JsonProperty("operation_last_hours_5")
    private String opDuration5;
    
    @JsonProperty("operator_5")
    private String opDocName5;
    
    @JsonProperty("first_assistant_5")
    private String opiName5;
    
    @JsonProperty("second_assistant_5")
    private String opiiName5;
    
    @JsonProperty("anesthesia_method_5")
    private String anesTypeCode5;
    
    @JsonProperty("anesthesia_level_5")
    private String anesLevelCode5;
    
    @JsonProperty("wound_healing_level_5")
    private String incisionTypeCode5;
    
    @JsonProperty("anesthesia_physician_5")
    private String anesDocName5;
    
    @JsonProperty("operation_code_6")
    private String opCode6;
    
    @JsonProperty("operation_date_6")
    private Date opDate6;
    
    @JsonProperty("operation_level_6")
    private String gradeCode6;
    
    @JsonProperty("operation_name_6")
    private String opName6;
    
    @JsonProperty("operation_spot_6")
    private String opSite6;
    
    @JsonProperty("operation_last_hours_6")
    private String opDuration6;
    
    @JsonProperty("operator_6")
    private String opDocName6;
    
    @JsonProperty("first_assistant_6")
    private String opiName6;
    
    @JsonProperty("second_assistant_6")
    private String opiiName6;
    
    @JsonProperty("anesthesia_method_6")
    private String anesTypeCode6;
    
    @JsonProperty("anesthesia_level_6")
    private String anesLevelCode6;
    
    @JsonProperty("wound_healing_level_6")
    private String incisionTypeCode6;
    
    @JsonProperty("anesthesia_physician_6")
    private String anesDocName6;
    
    @JsonProperty("operation_code_7")
    private String opCode7;
    
    @JsonProperty("operation_date_7")
    private Date opDate7;
    
    @JsonProperty("operation_level_7")
    private String gradeCode7;
    
    @JsonProperty("operation_name_7")
    private String opName7;
    
    @JsonProperty("operation_spot_7")
    private String opSite7;
    
    @JsonProperty("operation_last_hours_7")
    private String opDuration7;
    
    @JsonProperty("operator_7")
    private String opDocName7;
    
    @JsonProperty("first_assistant_7")
    private String opiName7;
    
    @JsonProperty("second_assistant_7")
    private String opiiName7;
    
    @JsonProperty("anesthesia_method_7")
    private String anesTypeCode7;
    
    @JsonProperty("anesthesia_level_7")
    private String anesLevelCode7;
    
    @JsonProperty("wound_healing_level_7")
    private String incisionTypeCode7;
    
    @JsonProperty("anesthesia_physician_7")
    private String anesDocName7;
    
    @JsonProperty("operation_code_8")
    private String opCode8;
    
    @JsonProperty("operation_date_8")
    private Date opDate8;
    
    @JsonProperty("operation_level_8")
    private String gradeCode8;
    
    @JsonProperty("operation_name_8")
    private String opName8;
    
    @JsonProperty("operation_spot_8")
    private String opSite8;
    
    @JsonProperty("operation_last_hours_8")
    private String opDuration8;
    
    @JsonProperty("operator_8")
    private String opDocName8;
    
    @JsonProperty("first_assistant_8")
    private String opiName8;
    
    @JsonProperty("second_assistant_8")
    private String opiiName8;
    
    @JsonProperty("anesthesia_method_8")
    private String anesTypeCode8;
    
    @JsonProperty("anesthesia_level_8")
    private String anesLevelCode8;
    
    @JsonProperty("wound_healing_level_8")
    private String incisionTypeCode8;
    
    @JsonProperty("anesthesia_physician_8")
    private String anesDocName8;
    
    @JsonProperty("operation_code_9")
    private String opCode9;
    
    @JsonProperty("operation_date_9")
    private Date opDate9;
    
    @JsonProperty("operation_level_9")
    private String gradeCode9;
    
    @JsonProperty("operation_name_9")
    private String opName9;
    
    @JsonProperty("operation_spot_9")
    private String opSite9;
    
    @JsonProperty("operation_last_hours_9")
    private String opDuration9;
    
    @JsonProperty("operator_9")
    private String opDocName9;
    
    @JsonProperty("first_assistant_9")
    private String opiName9;
    
    @JsonProperty("second_assistant_9")
    private String opiiName9;
    
    @JsonProperty("anesthesia_method_9")
    private String anesTypeCode9;
    
    @JsonProperty("anesthesia_level_9")
    private String anesLevelCode9;
    
    @JsonProperty("wound_healing_level_9")
    private String incisionTypeCode9;
    
    @JsonProperty("anesthesia_physician_9")
    private String anesDocName9;
    
    @JsonProperty("operation_code_10")
    private String opCode10;
    
    @JsonProperty("operation_date_10")
    private Date opDate10;
    
    @JsonProperty("operation_level_10")
    private String gradeCode10;
    
    @JsonProperty("operation_name_10")
    private String opName10;
    
    @JsonProperty("operation_spot_10")
    private String opSite10;
    
    @JsonProperty("operation_last_hours_10")
    private String opDuration10;
    
    @JsonProperty("operator_10")
    private String opDocName10;
    
    @JsonProperty("first_assistant_10")
    private String opiName10;
    
    @JsonProperty("second_assistant_10")
    private String opiiName10;
    
    @JsonProperty("anesthesia_method_10")
    private String anesTypeCode10;
    
    @JsonProperty("anesthesia_level_10")
    private String anesLevelCode10;
    
    @JsonProperty("wound_healing_level_10")
    private String incisionTypeCode10;
    
    @JsonProperty("anesthesia_physician_10")
    private String anesDocName10;
    
    @JsonProperty("death_patient_autopsy")
    private String flagAutopsy;
    
    @JsonProperty("ABO_blood_group")
    private String bloodCodeAbo;
    /**
     * 
     */
    @JsonIgnore
    private String bloodNameAbo;
    /**
     * 
     */
    @JsonProperty("Rh_blood_group")
    private String bloodCodeRh;
    /**
     * 
     */
    @JsonIgnore
    private String bloodNameRh;
    
    @JsonIgnore
    private BigDecimal ageYear;
    /**
     * 
     */
    @JsonProperty("month_age")
    private String ageMonth;
    
    /**
     * g
     */
    @JsonProperty("newborn_birth_weight")
    private BigDecimal newbornWeight;
    /**
     * g
     */
    @JsonProperty("newborn_admission_weight")
    private BigDecimal newbornInWeight;
    
    @JsonIgnore
    private Integer comaDayBef;

    @JsonProperty("coma_hours_before_admission")
    private Integer comaHourBef;
    /**
     * 
     */
    @JsonProperty("coma_minutes_before_admission")
    private Integer comaMinBef;
    /**
     * 
     */
    @JsonIgnore
    private Integer comaDayAfter;
    /**
     * 
     */
    @JsonIgnore
    private Integer comaHourAfter;
    /**
     * 
     */
    @JsonProperty("coma_minutes_after_admission")
    private Integer comaMinAfter;
    
    /**
     * 出院31天内再住院计划标志
     */
    @JsonProperty("readmission")
    private String flagReadmit;
    /**
     * 出院31天内再住院计划目的
     */
    @JsonProperty("readmission_purpose")
    private String readmitPurp;
    
    @JsonProperty("discharge_type")
    private String leaveHosCode;
    /**
     * 
     */
    @JsonIgnore
    private String leaveHosName;
    
    @JsonIgnore
    private String receiveOrgCode;
    /**
     * 
     */
    @JsonProperty("to_hospital_name")
    private String receiveOrgName;
    
    /**
     * 
     */
    @JsonProperty("total_inpatient_cost")
    private BigDecimal totalCost;
    /**
     * 
     */
    @JsonProperty("self_payment")
    private BigDecimal selfCost;
   
    @JsonProperty("medical_service_cost")
    private BigDecimal cost1;
    @JsonProperty("treatment_cost")
    private BigDecimal cost2;
    @JsonProperty("nursing_cost")
    private BigDecimal cost3;
    @JsonProperty("comprehensive_other_cost")
    private BigDecimal cost4;
    @JsonProperty("pathologic_diagnosis_cost")
    private BigDecimal cost5;
    @JsonProperty("laboratory_cost")
    private BigDecimal cost6;
    @JsonProperty("imaging_diagnosis_cost")
    private BigDecimal cost7;
    @JsonProperty("clinical_diagnosis_cost")
    private BigDecimal cost8;
    @JsonProperty("non_operation_cost")
    private BigDecimal cost9;
    @JsonProperty("clinical_physical_therapy_cost")
    private BigDecimal cost0901;
    @JsonProperty("operation_therapy_cost")
    private BigDecimal cost10;
    @JsonProperty("anesthetic_cost")
    private BigDecimal cost1001;
    @JsonProperty("operation_cost")
    private BigDecimal cost1002;
    @JsonProperty("rehabilitation_cost")
    private BigDecimal cost11;
    @JsonProperty("TCM_treatment_cost")
    private BigDecimal cost12;
    @JsonProperty("western_medicine_cost")
    private BigDecimal cost13;
    @JsonProperty("antibiotics_cost")
    private BigDecimal cost1301;
    @JsonProperty("chinese_patent_medicine_cost")
    private BigDecimal cost14;
    @JsonProperty("chinese_herbal_medicine_cost")
    private BigDecimal cost15;
    @JsonProperty("blood_cost")
    private BigDecimal cost16;
    @JsonProperty("albumin_products_cost")
    private BigDecimal cost17;
    @JsonProperty("globulin_products_cost")
    private BigDecimal cost18;
    @JsonProperty("coagulation_factor_products_cost")
    private BigDecimal cost19;
    @JsonProperty("cytokine_products_cost")
    private BigDecimal cost20;
    @JsonProperty("check_material_cost")
    private BigDecimal cost21;
    @JsonProperty("treatment_material_cost")
    private BigDecimal cost22;
    @JsonProperty("operation_material_cost")
    private BigDecimal cost23;
    @JsonProperty("other_cost")
    private BigDecimal cost24;
    
    @JsonIgnore
	private String diagCodeClinicIcd;

    @JsonIgnore
	private String diagNameClinicIcd;

    @JsonIgnore
	private String diagCodeExtcIpIcd;

    @JsonIgnore
	private String diagNameExtcIpIcd;

    @JsonIgnore
	private String diagCodePathoIcd;

    @JsonIgnore
	private String diagNamePathoIcd;

    @JsonIgnore
	private String pathoNoIcd;
    @JsonIgnore
    private String flagDrugAllergyIcd;
    @JsonIgnore
    private String allergicDrugIcd;
    @JsonIgnore
    private String partDisease;
    @JsonIgnore
    private String patSource;
    @JsonIgnore
    private String birthAddrCode; //出生地-区域编码
    @JsonIgnore
    private String originAddrCode;	//籍贯-区域编码
    @JsonIgnore
    private String currAddrCode;	//现住址-区域编码
    @JsonIgnore
    private String resideAddrCode;	//户口住址-区域编码
    

	public String getFlagDrugAllergyIcd() {
		return flagDrugAllergyIcd;
	}

	public void setFlagDrugAllergyIcd(String flagDrugAllergyIcd) {
		this.flagDrugAllergyIcd = flagDrugAllergyIcd;
	}

	public String getAllergicDrugIcd() {
		return allergicDrugIcd;
	}

	public void setAllergicDrugIcd(String allergicDrugIcd) {
		this.allergicDrugIcd = allergicDrugIcd;
	}

	public String getPartDisease() {
		return partDisease;
	}

	public void setPartDisease(String partDisease) {
		this.partDisease = partDisease;
	}

	public String getPatSource() {
		return patSource;
	}

	public void setPatSource(String patSource) {
		this.patSource = patSource;
	}

	/**
     * 
     */
	@JsonIgnore
    private String delFlag;
    /**
     * 
     */
	@JsonIgnore
    private String remark;
    /**
     * 
     */
	@JsonIgnore
    private String creator;
    /**
     * 
     */
	@JsonIgnore
    private Date createTime;
    /**
     * 
     */
	@JsonIgnore
    private Date ts;
	@JsonIgnore
    private String status;
	@JsonIgnore
    private String  pageType;
    
	@JsonIgnore
    public String codePi;
    
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonIgnore
    private String pkAdmitWard;

	@JsonIgnore
    public String getPkPage(){
        return this.pkPage;
    }

    /**
     * 
     */
    public void setPkPage(String pkPage){
        this.pkPage = pkPage;
    }    
    @JsonIgnore
    public String getPkOrg(){
        return this.pkOrg;
    }

    /**
     * 
     */
    public void setPkOrg(String pkOrg){
        this.pkOrg = pkOrg;
    }    
    @JsonIgnore
    public String getPkPi(){
        return this.pkPi;
    }

    /**
     * 
     */
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }    
    /**
     * 
     */
    public Integer getTimes(){
        return this.times;
    }

    /**
     * 
     */
    public void setTimes(Integer times){
        this.times = times;
    }    
    /**
     * 
     */
    public String getPkPv(){
        return this.pkPv;
    }

    /**
     * 
     */
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }    
    /**
     * 
     */
    public String getMedOrgCode(){
        return this.medOrgCode;
    }

    /**
     * 
     */
    public void setMedOrgCode(String medOrgCode){
        this.medOrgCode = medOrgCode;
    }    
    /**
     * 
     */
    public String getMedOrgName(){
        return this.medOrgName;
    }

    /**
     * 
     */
    public void setMedOrgName(String medOrgName){
        this.medOrgName = medOrgName;
    }    
    /**
     * 
     */
    public String getHealthCardNo(){
        return this.healthCardNo;
    }

    /**
     * 
     */
    public void setHealthCardNo(String healthCardNo){
        this.healthCardNo = healthCardNo;
    }    
    /**
     * 
     */
    public String getMedPayMode(){
        return this.medPayMode;
    }

    /**
     * 
     */
    public void setMedPayMode(String medPayMode){
        this.medPayMode = medPayMode;
    }    
    /**
     * 
     */
    public String getPatNo(){
        return this.patNo;
    }

    /**
     * 
     */
    public void setPatNo(String patNo){
        this.patNo = patNo;
    }    
    /**
     * 
     */
    public String getName(){
        return this.name;
    }

    /**
     * 
     */
    public void setName(String name){
        this.name = name;
    }    
    /**
     * 
     */
    public String getDtSex(){
        return this.dtSex;
    }

    /**
     * 
     */
    public void setDtSex(String dtSex){
        this.dtSex = dtSex;
    }    
    /**
     * 
     */
    public Date getBirthDate(){
        return this.birthDate;
    }

    /**
     * 
     */
    public void setBirthDate(Date birthDate){
        this.birthDate = birthDate;
    }    
    /**
     * 
     */
    public BigDecimal getAgeYear(){
        return this.ageYear;
    }

    /**
     * 
     */
    public void setAgeYear(BigDecimal ageYear){
        this.ageYear = ageYear;
    }    
    /**
     * 
     */
    public String getAgeMonth(){
        return this.ageMonth;
    }

    /**
     * 
     */
    public void setAgeMonth(String ageMonth){
        this.ageMonth = ageMonth;
    }    
    /**
     * 
     */
    public String getAgeTxt(){
        return this.ageTxt;
    }

    /**
     * 
     */
    public void setAgeTxt(String ageTxt){
        this.ageTxt = ageTxt;
    }    
    /**
     * g
     */
    public BigDecimal getNewbornWeight(){
        return this.newbornWeight;
    }

    /**
     * g
     */
    public void setNewbornWeight(BigDecimal newbornWeight){
        this.newbornWeight = newbornWeight;
    }    
    /**
     * g
     */
    public BigDecimal getNewbornInWeight(){
        return this.newbornInWeight;
    }

    /**
     * g
     */
    public void setNewbornInWeight(BigDecimal newbornInWeight){
        this.newbornInWeight = newbornInWeight;
    }    
    /**
     * 
     */
    public String getBirthAddrProv(){
        return this.birthAddrProv;
    }

    /**
     * 
     */
    public void setBirthAddrProv(String birthAddrProv){
        this.birthAddrProv = birthAddrProv;
    }    
    /**
     * 
     */
    public String getBirthAddrCity(){
        return this.birthAddrCity;
    }

    /**
     * 
     */
    public void setBirthAddrCity(String birthAddrCity){
        this.birthAddrCity = birthAddrCity;
    }    
    /**
     * 
     */
    public String getBirthAddrCounty(){
        return this.birthAddrCounty;
    }

    /**
     * 
     */
    public void setBirthAddrCounty(String birthAddrCounty){
        this.birthAddrCounty = birthAddrCounty;
    }    
    /**
     * 
     */
    public String getOriginAddrProv(){
        return this.originAddrProv;
    }

    /**
     * 
     */
    public void setOriginAddrProv(String originAddrProv){
        this.originAddrProv = originAddrProv;
    }    
    /**
     * 
     */
    public String getOriginAddrCity(){
        return this.originAddrCity;
    }

    /**
     * 
     */
    public void setOriginAddrCity(String originAddrCity){
        this.originAddrCity = originAddrCity;
    }    
    /**
     * 
     */
    public String getCountryCode(){
        return this.countryCode;
    }

    /**
     * 
     */
    public void setCountryCode(String countryCode){
        this.countryCode = countryCode;
    }    
    /**
     * 
     */
    public String getCountryName(){
        return this.countryName;
    }

    /**
     * 
     */
    public void setCountryName(String countryName){
        this.countryName = countryName;
    }    
    /**
     * 
     */
    public String getNationCode(){
        return this.nationCode;
    }

    /**
     * 
     */
    public void setNationCode(String nationCode){
        this.nationCode = nationCode;
    }    
    /**
     * 
     */
    public String getNationName(){
        return this.nationName;
    }

    /**
     * 
     */
    public void setNationName(String nationName){
        this.nationName = nationName;
    }    
    /**
     * 
     */
    public String getIdType(){
        return this.idType;
    }

    /**
     * 
     */
    public void setIdType(String idType){
        this.idType = idType;
    }    
    /**
     * 
     */
    public String getIdNo(){
        return this.idNo;
    }

    /**
     * 
     */
    public void setIdNo(String idNo){
        this.idNo = idNo;
    }    
    /**
     * 
     */
    public String getOccupCode(){
        return this.occupCode;
    }

    /**
     * 
     */
    public void setOccupCode(String occupCode){
        this.occupCode = occupCode;
    }    
    /**
     * 
     */
    public String getOccupName(){
        return this.occupName;
    }

    /**
     * 
     */
    public void setOccupName(String occupName){
        this.occupName = occupName;
    }    
    /**
     * 
     */
    public String getMarryCode(){
        return this.marryCode;
    }

    /**
     * 
     */
    public void setMarryCode(String marryCode){
        this.marryCode = marryCode;
    }    
    /**
     * 
     */
    public String getMarryName(){
        return this.marryName;
    }

    /**
     * 
     */
    public void setMarryName(String marryName){
        this.marryName = marryName;
    }    
    /**
     * 
     */
    public String getCurrAddrProv(){
        return this.currAddrProv;
    }

    /**
     * 
     */
    public void setCurrAddrProv(String currAddrProv){
        this.currAddrProv = currAddrProv;
    }    
    /**
     * 
     */
    public String getCurrAddrCity(){
        return this.currAddrCity;
    }

    /**
     * 
     */
    public void setCurrAddrCity(String currAddrCity){
        this.currAddrCity = currAddrCity;
    }    
    /**
     * 
     */
    public String getCurrAddrCounty(){
        return this.currAddrCounty;
    }

    /**
     * 
     */
    public void setCurrAddrCounty(String currAddrCounty){
        this.currAddrCounty = currAddrCounty;
    }    
    /**
     * 
     */
    public String getCurrPhone(){
        return this.currPhone;
    }

    /**
     * 
     */
    public void setCurrPhone(String currPhone){
        this.currPhone = currPhone;
    }    
    /**
     * 
     */
    public String getCurrZipCode(){
        return this.currZipCode;
    }

    /**
     * 
     */
    public void setCurrZipCode(String currZipCode){
        this.currZipCode = currZipCode;
    }    
    /**
     * 
     */
    public String getWorkUnit(){
        return this.workUnit;
    }

    /**
     * 
     */
    public void setWorkUnit(String workUnit){
        this.workUnit = workUnit;
    }    
    /**
     * 
     */
    public String getWorkUnitPhone(){
        return this.workUnitPhone;
    }

    /**
     * 
     */
    public void setWorkUnitPhone(String workUnitPhone){
        this.workUnitPhone = workUnitPhone;
    }    
    /**
     * 
     */
    public String getResideAddrProv(){
        return this.resideAddrProv;
    }

    /**
     * 
     */
    public void setResideAddrProv(String resideAddrProv){
        this.resideAddrProv = resideAddrProv;
    }    
    /**
     * 
     */
    public String getResideAddrCity(){
        return this.resideAddrCity;
    }

    /**
     * 
     */
    public void setResideAddrCity(String resideAddrCity){
        this.resideAddrCity = resideAddrCity;
    }    
    /**
     * 
     */
    public String getResideAddrCounty(){
        return this.resideAddrCounty;
    }

    /**
     * 
     */
    public void setResideAddrCounty(String resideAddrCounty){
        this.resideAddrCounty = resideAddrCounty;
    }    
    /**
     * 
     */
    public String getResideZipCode(){
        return this.resideZipCode;
    }

    /**
     * 
     */
    public void setResideZipCode(String resideZipCode){
        this.resideZipCode = resideZipCode;
    }    
    /**
     * 
     */
    public String getWorkUnitZipCode(){
        return this.workUnitZipCode;
    }

    /**
     * 
     */
    public void setWorkUnitZipCode(String workUnitZipCode){
        this.workUnitZipCode = workUnitZipCode;
    }    
    /**
     * 
     */
    public String getContactName(){
        return this.contactName;
    }

    /**
     * 
     */
    public void setContactName(String contactName){
        this.contactName = contactName;
    }    
    /**
     * 
     */
    public String getContactRelatCode(){
        return this.contactRelatCode;
    }

    /**
     * 
     */
    public void setContactRelatCode(String contactRelatCode){
        this.contactRelatCode = contactRelatCode;
    }    
    /**
     * 
     */
    public String getContactRelatName(){
        return this.contactRelatName;
    }

    /**
     * 
     */
    public void setContactRelatName(String contactRelatName){
        this.contactRelatName = contactRelatName;
    }    
    /**
     * 
     */
    public String getContactAddr(){
        return this.contactAddr;
    }

    /**
     * 
     */
    public void setContactAddr(String contactAddr){
        this.contactAddr = contactAddr;
    }    
    /**
     * 
     */
    public String getContactPhone(){
        return this.contactPhone;
    }

    /**
     * 
     */
    public void setContactPhone(String contactPhone){
        this.contactPhone = contactPhone;
    }    
    /**
     * 
     */
    public String getAdmitPathCode(){
        return this.admitPathCode;
    }

    /**
     * 
     */
    public void setAdmitPathCode(String admitPathCode){
        this.admitPathCode = admitPathCode;
    }    
    /**
     * 
     */
    public String getAdmitPathName(){
        return this.admitPathName;
    }

    /**
     * 
     */
    public void setAdmitPathName(String admitPathName){
        this.admitPathName = admitPathName;
    }    
    /**
     * 
     */
    public Date getAdmitTime(){
        return this.admitTime;
    }

    /**
     * 
     */
    public void setAdmitTime(Date admitTime){
        this.admitTime = admitTime;
    }    
    /**
     * 
     */
    public String getPkAdmitDept(){
        return this.pkAdmitDept;
    }

    /**
     * 
     */
    public void setPkAdmitDept(String pkAdmitDept){
        this.pkAdmitDept = pkAdmitDept;
    }    
    /**
     * 
     */
    public String getAdmitDeptName(){
        return this.admitDeptName;
    }

    /**
     * 
     */
    public void setAdmitDeptName(String admitDeptName){
        this.admitDeptName = admitDeptName;
    }    
    /**
     * 
     */
    public String getAdmitWardName(){
        return this.admitWardName;
    }

    /**
     * 
     */
    public void setAdmitWardName(String admitWardName){
        this.admitWardName = admitWardName;
    }    
    /**
     * 患者住院期间转科的转入科室名称，如果超过一次以上的转科，用“→”转接表示
     */
    public String getTransDeptNames(){
        return this.transDeptNames;
    }

    /**
     * 患者住院期间转科的转入科室名称，如果超过一次以上的转科，用“→”转接表示
     */
    public void setTransDeptNames(String transDeptNames){
        this.transDeptNames = transDeptNames;
    }    
    /**
     * 
     */
    public Date getDisTime(){
        return this.disTime;
    }

    /**
     * 
     */
    public void setDisTime(Date disTime){
        this.disTime = disTime;
    }    
    /**
     * 
     */
    public String getPkDeptDis(){
        return this.pkDeptDis;
    }

    /**
     * 
     */
    public void setPkDeptDis(String pkDeptDis){
        this.pkDeptDis = pkDeptDis;
    }    
    /**
     * 
     */
    public String getDisDeptName(){
        return this.disDeptName;
    }

    /**
     * 
     */
    public void setDisDeptName(String disDeptName){
        this.disDeptName = disDeptName;
    }    
    /**
     * 
     */
    public String getPkWardDis(){
        return this.pkWardDis;
    }

    /**
     * 
     */
    public void setPkWardDis(String pkWardDis){
        this.pkWardDis = pkWardDis;
    }    
    /**
     * 
     */
    public String getDisWardName(){
        return this.disWardName;
    }

    /**
     * 
     */
    public void setDisWardName(String disWardName){
        this.disWardName = disWardName;
    }    
    /**
     * 
     */
    public String getPkDiagClinic(){
        return this.pkDiagClinic;
    }

    /**
     * 
     */
    public void setPkDiagClinic(String pkDiagClinic){
        this.pkDiagClinic = pkDiagClinic;
    }    
    /**
     * 
     */
    public BigDecimal getInHosDays(){
        return this.inHosDays;
    }

    /**
     * 
     */
    public void setInHosDays(BigDecimal inHosDays){
        this.inHosDays = inHosDays;
    }    
    /**
     * 
     */
    public String getDiagCodeClinic(){
        return this.diagCodeClinic;
    }

    /**
     * 
     */
    public void setDiagCodeClinic(String diagCodeClinic){
        this.diagCodeClinic = diagCodeClinic;
    }    
    /**
     * 
     */
    public String getPkDiagDis(){
        return this.pkDiagDis;
    }

    /**
     * 
     */
    public void setPkDiagDis(String pkDiagDis){
        this.pkDiagDis = pkDiagDis;
    }    
    /**
     * 
     */
    public String getDiagNameClinic(){
        return this.diagNameClinic;
    }

    /**
     * 
     */
    public void setDiagNameClinic(String diagNameClinic){
        this.diagNameClinic = diagNameClinic;
    }    
    /**
     * 
     */
    public String getDiagCodeDis(){
        return this.diagCodeDis;
    }

    /**
     * 
     */
    public void setDiagCodeDis(String diagCodeDis){
        this.diagCodeDis = diagCodeDis;
    }    
    /**
     * 
     */
    public String getPkDiagExtcIp(){
        return this.pkDiagExtcIp;
    }

    /**
     * 
     */
    public void setPkDiagExtcIp(String pkDiagExtcIp){
        this.pkDiagExtcIp = pkDiagExtcIp;
    }    
    /**
     * 
     */
    public String getDiagNameDis(){
        return this.diagNameDis;
    }

    /**
     * 
     */
    public void setDiagNameDis(String diagNameDis){
        this.diagNameDis = diagNameDis;
    }    
    /**
     * 
     */
    public String getDiagCodeExtcIp(){
        return this.diagCodeExtcIp;
    }

    /**
     * 
     */
    public void setDiagCodeExtcIp(String diagCodeExtcIp){
        this.diagCodeExtcIp = diagCodeExtcIp;
    }    
    /**
     * 
     */
    public String getPkDiagPatho(){
        return this.pkDiagPatho;
    }

    /**
     * 
     */
    public void setPkDiagPatho(String pkDiagPatho){
        this.pkDiagPatho = pkDiagPatho;
    }    
    /**
     * 
     */
    public String getDiagNameExtcIp(){
        return this.diagNameExtcIp;
    }

    /**
     * 
     */
    public void setDiagNameExtcIp(String diagNameExtcIp){
        this.diagNameExtcIp = diagNameExtcIp;
    }    
    /**
     * 
     */
    public String getDiagCodePatho(){
        return this.diagCodePatho;
    }

    /**
     * 
     */
    public void setDiagCodePatho(String diagCodePatho){
        this.diagCodePatho = diagCodePatho;
    }    
    /**
     * 
     */
    public String getDiagNamePatho(){
        return this.diagNamePatho;
    }

    /**
     * 
     */
    public void setDiagNamePatho(String diagNamePatho){
        this.diagNamePatho = diagNamePatho;
    }    
    /**
     * 
     */
    public String getPathoNo(){
        return this.pathoNo;
    }

    /**
     * 
     */
    public void setPathoNo(String pathoNo){
        this.pathoNo = pathoNo;
    }    
    /**
     * 
     */
    public String getFlagDrugAllergy(){
        return this.flagDrugAllergy;
    }

    /**
     * 
     */
    public void setFlagDrugAllergy(String flagDrugAllergy){
        this.flagDrugAllergy = flagDrugAllergy;
    }    
    /**
     * 
     */
    public String getAllergicDrug(){
        return this.allergicDrug;
    }

    /**
     * 
     */
    public void setAllergicDrug(String allergicDrug){
        this.allergicDrug = allergicDrug;
    }    
    /**
     * 
     */
    public String getFlagAutopsy(){
        return this.flagAutopsy;
    }

    /**
     * 
     */
    public void setFlagAutopsy(String flagAutopsy){
        this.flagAutopsy = flagAutopsy;
    }    
    /**
     * 
     */
    public String getBloodCodeAbo(){
        return this.bloodCodeAbo;
    }

    /**
     * 
     */
    public void setBloodCodeAbo(String bloodCodeAbo){
        this.bloodCodeAbo = bloodCodeAbo;
    }    
    /**
     * 
     */
    public String getBloodNameAbo(){
        return this.bloodNameAbo;
    }

    /**
     * 
     */
    public void setBloodNameAbo(String bloodNameAbo){
        this.bloodNameAbo = bloodNameAbo;
    }    
    /**
     * 
     */
    public String getBloodCodeRh(){
        return this.bloodCodeRh;
    }

    /**
     * 
     */
    public void setBloodCodeRh(String bloodCodeRh){
        this.bloodCodeRh = bloodCodeRh;
    }    
    /**
     * 
     */
    public String getBloodNameRh(){
        return this.bloodNameRh;
    }

    /**
     * 
     */
    public void setBloodNameRh(String bloodNameRh){
        this.bloodNameRh = bloodNameRh;
    }    
    /**
     * 
     */
    public String getPkEmpDirector(){
        return this.pkEmpDirector;
    }

    /**
     * 
     */
    public void setPkEmpDirector(String pkEmpDirector){
        this.pkEmpDirector = pkEmpDirector;
    }    
    /**
     * 
     */
    public String getDirectorName(){
        return this.directorName;
    }

    /**
     * 
     */
    public void setDirectorName(String directorName){
        this.directorName = directorName;
    }    
    /**
     * 
     */
    public String getPkEmpConsult(){
        return this.pkEmpConsult;
    }

    /**
     * 
     */
    public void setPkEmpConsult(String pkEmpConsult){
        this.pkEmpConsult = pkEmpConsult;
    }    
    /**
     * 
     */
    public String getConsultName(){
        return this.consultName;
    }

    /**
     * 
     */
    public void setConsultName(String consultName){
        this.consultName = consultName;
    }    

 
    public String getPkEmpChief() {
		return pkEmpChief;
	}

	public void setPkEmpChief(String pkEmpChief) {
		this.pkEmpChief = pkEmpChief;
	}

	public String getChiefName() {
		return chiefName;
	}

	public void setChiefName(String chiefName) {
		this.chiefName = chiefName;
	}

	/**
     * 
     */
    public String getPkEmpRefer(){
        return this.pkEmpRefer;
    }

    /**
     * 
     */
    public void setPkEmpRefer(String pkEmpRefer){
        this.pkEmpRefer = pkEmpRefer;
    }    
    /**
     * 
     */
    public String getReferName(){
        return this.referName;
    }

    /**
     * 
     */
    public void setReferName(String referName){
        this.referName = referName;
    }    
    /**
     * 
     */
    public String getPkEmpNurse(){
        return this.pkEmpNurse;
    }

    /**
     * 
     */
    public void setPkEmpNurse(String pkEmpNurse){
        this.pkEmpNurse = pkEmpNurse;
    }    
    /**
     * 
     */
    public String getNurseName(){
        return this.nurseName;
    }

    /**
     * 
     */
    public void setNurseName(String nurseName){
        this.nurseName = nurseName;
    }    
    /**
     * 
     */
    public String getPkEmpLearn(){
        return this.pkEmpLearn;
    }

    /**
     * 
     */
    public void setPkEmpLearn(String pkEmpLearn){
        this.pkEmpLearn = pkEmpLearn;
    }    
    /**
     * 
     */
    public String getLearnName(){
        return this.learnName;
    }

    /**
     * 
     */
    public void setLearnName(String learnName){
        this.learnName = learnName;
    }    
    /**
     * 
     */
    public String getPkEmpIntern(){
        return this.pkEmpIntern;
    }

    /**
     * 
     */
    public void setPkEmpIntern(String pkEmpIntern){
        this.pkEmpIntern = pkEmpIntern;
    }    
    /**
     * 
     */
    public String getInternName(){
        return this.internName;
    }

    /**
     * 
     */
    public void setInternName(String internName){
        this.internName = internName;
    }    
    /**
     * 
     */
    public String getPkEmpCoder(){
        return this.pkEmpCoder;
    }

    /**
     * 
     */
    public void setPkEmpCoder(String pkEmpCoder){
        this.pkEmpCoder = pkEmpCoder;
    }    
    /**
     * 
     */
    public String getCoderName(){
        return this.coderName;
    }

    /**
     * 
     */
    public void setCoderName(String coderName){
        this.coderName = coderName;
    }    
    /**
     * 
     */
    public String getQualityCode(){
        return this.qualityCode;
    }

    /**
     * 
     */
    public void setQualityCode(String qualityCode){
        this.qualityCode = qualityCode;
    }    
    /**
     * 
     */
    public String getQualityName(){
        return this.qualityName;
    }

    /**
     * 
     */
    public void setQualityName(String qualityName){
        this.qualityName = qualityName;
    }    
    /**
     * 
     */
    public String getPkEmpQcDoc(){
        return this.pkEmpQcDoc;
    }

    /**
     * 
     */
    public void setPkEmpQcDoc(String pkEmpQcDoc){
        this.pkEmpQcDoc = pkEmpQcDoc;
    }    
    /**
     * 
     */
    public String getQcDocName(){
        return this.qcDocName;
    }

    /**
     * 
     */
    public void setQcDocName(String qcDocName){
        this.qcDocName = qcDocName;
    }    
    /**
     * 
     */
    public String getPkEmpQcNurse(){
        return this.pkEmpQcNurse;
    }

    /**
     * 
     */
    public void setPkEmpQcNurse(String pkEmpQcNurse){
        this.pkEmpQcNurse = pkEmpQcNurse;
    }    
    /**
     * 
     */
    public String getQcNurseName(){
        return this.qcNurseName;
    }

    /**
     * 
     */
    public void setQcNurseName(String qcNurseName){
        this.qcNurseName = qcNurseName;
    }    
    /**
     * 
     */
    public Date getQcDate(){
        return this.qcDate;
    }

    /**
     * 
     */
    public void setQcDate(Date qcDate){
        this.qcDate = qcDate;
    }    
    /**
     * 
     */
    public String getLeaveHosCode(){
        return this.leaveHosCode;
    }

    /**
     * 
     */
    public void setLeaveHosCode(String leaveHosCode){
        this.leaveHosCode = leaveHosCode;
    }    
    /**
     * 
     */
    public String getLeaveHosName(){
        return this.leaveHosName;
    }

    /**
     * 
     */
    public void setLeaveHosName(String leaveHosName){
        this.leaveHosName = leaveHosName;
    }    
    /**
     * 
     */
    public String getReceiveOrgCode(){
        return this.receiveOrgCode;
    }

    /**
     * 
     */
    public void setReceiveOrgCode(String receiveOrgCode){
        this.receiveOrgCode = receiveOrgCode;
    }    
    /**
     * 
     */
    public String getReceiveOrgName(){
        return this.receiveOrgName;
    }

    /**
     * 
     */
    public void setReceiveOrgName(String receiveOrgName){
        this.receiveOrgName = receiveOrgName;
    }    
    /**
     * 出院31天内再住院计划标志
     */
    public String getFlagReadmit(){
        return this.flagReadmit;
    }

    /**
     * 出院31天内再住院计划标志
     */
    public void setFlagReadmit(String flagReadmit){
        this.flagReadmit = flagReadmit;
    }    
    /**
     * 出院31天内再住院计划目的
     */
    public String getReadmitPurp(){
        return this.readmitPurp;
    }

    /**
     * 出院31天内再住院计划目的
     */
    public void setReadmitPurp(String readmitPurp){
        this.readmitPurp = readmitPurp;
    }    
    /**
     * 
     */
    public Integer getComaDayBef(){
        return this.comaDayBef;
    }

    /**
     * 
     */
    public void setComaDayBef(Integer comaDayBef){
        this.comaDayBef = comaDayBef;
    }    
    /**
     * 
     */
    public Integer getComaHourBef(){
        return this.comaHourBef;
    }

    /**
     * 
     */
    public void setComaHourBef(Integer comaHourBef){
        this.comaHourBef = comaHourBef;
    }    
    /**
     * 
     */
    public Integer getComaMinBef(){
        return this.comaMinBef;
    }

    /**
     * 
     */
    public void setComaMinBef(Integer comaMinBef){
        this.comaMinBef = comaMinBef;
    }    
    /**
     * 
     */
    public Integer getComaDayAfter(){
        return this.comaDayAfter;
    }

    /**
     * 
     */
    public void setComaDayAfter(Integer comaDayAfter){
        this.comaDayAfter = comaDayAfter;
    }    
    /**
     * 
     */
    public Integer getComaHourAfter(){
        return this.comaHourAfter;
    }

    /**
     * 
     */
    public void setComaHourAfter(Integer comaHourAfter){
        this.comaHourAfter = comaHourAfter;
    }    
    /**
     * 
     */
    public Integer getComaMinAfter(){
        return this.comaMinAfter;
    }

    /**
     * 
     */
    public void setComaMinAfter(Integer comaMinAfter){
        this.comaMinAfter = comaMinAfter;
    }    
    /**
     * 
     */
    public BigDecimal getTotalCost(){
        return this.totalCost;
    }

    /**
     * 
     */
    public void setTotalCost(BigDecimal totalCost){
        this.totalCost = totalCost;
    }    
    /**
     * 
     */
    public BigDecimal getSelfCost(){
        return this.selfCost;
    }

    /**
     * 
     */
    public void setSelfCost(BigDecimal selfCost){
        this.selfCost = selfCost;
    }    
    /**
     * 
     */
    public String getDelFlag(){
        return this.delFlag;
    }

    /**
     * 
     */
    public void setDelFlag(String delFlag){
        this.delFlag = delFlag;
    }    
    /**
     * 
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getCreator(){
        return this.creator;
    }

    /**
     * 
     */
    public void setCreator(String creator){
        this.creator = creator;
    }    
    /**
     * 
     */
    public Date getCreateTime(){
        return this.createTime;
    }

    /**
     * 
     */
    public void setCreateTime(Date createTime){
        this.createTime = createTime;
    }    
    /**
     * 
     */
    public Date getTs(){
        return this.ts;
    }

    /**
     * 
     */
    public void setTs(Date ts){
        this.ts = ts;
    }    
    /**
     * 
     */
    public String getPkAdmitWard(){
        return this.pkAdmitWard;
    }

    /**
     * 
     */
    public void setPkAdmitWard(String pkAdmitWard){
        this.pkAdmitWard = pkAdmitWard;
    }

//	public List<EmrHomePageDiags> getDiags() {
//		return diags;
//	}
//
//	public void setDiags(List<EmrHomePageDiags> diags) {
//		this.diags = diags;
//	}
//
//	public List<EmrHomePageCharges> getCharges() {
//		return charges;
//	}
//
//	public void setCharges(List<EmrHomePageCharges> charges) {
//		this.charges = charges;
//	}
//
//	public List<EmrHomePageOps> getOps() {
//		return ops;
//	}
//
//	public void setOps(List<EmrHomePageOps> ops) {
//		this.ops = ops;
//	}

	public String getMedRecType() {
		return medRecType;
	}

	public void setMedRecType(String medRecType) {
		this.medRecType = medRecType;
	}

	public String getFlagCp() {
		return flagCp;
	}

	public void setFlagCp(String flagCp) {
		this.flagCp = flagCp;
	}

	public Integer getNumRes() {
		return numRes;
	}

	public void setNumRes(Integer numRes) {
		this.numRes = numRes;
	}

	public Integer getNumSucc() {
		return numSucc;
	}

	public void setNumSucc(Integer numSucc) {
		this.numSucc = numSucc;
	}

   
	public String getPkEmpClinic() {
		return pkEmpClinic;
	}

	public void setPkEmpClinic(String pkEmpClinic) {
		this.pkEmpClinic = pkEmpClinic;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}


	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public String getCurrAddr() {
		return currAddr;
	}

	public void setCurrAddr(String currAddr) {
		this.currAddr = currAddr;
	}

	public String getResideAddr() {
		return resideAddr;
	}

	public void setResideAddr(String resideAddr) {
		this.resideAddr = resideAddr;
	}

	public String getAdmitCondCode() {
		return admitCondCode;
	}

	public void setAdmitCondCode(String admitCondCode) {
		this.admitCondCode = admitCondCode;
	}

	public String getAdmitCondName() {
		return admitCondName;
	}

	public void setAdmitCondName(String admitCondName) {
		this.admitCondName = admitCondName;
	}

	public String getBirthAddr() {
		return birthAddr;
	}

	public void setBirthAddr(String birthAddr) {
		this.birthAddr = birthAddr;
	}

	public String getOriginAddr() {
		return originAddr;
	}

	public void setOriginAddr(String originAddr) {
		this.originAddr = originAddr;
	}

	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}

	public String getCurrAddrDt() {
		return currAddrDt;
	}

	public void setCurrAddrDt(String currAddrDt) {
		this.currAddrDt = currAddrDt;
	}

	public String getResideAddrDt() {
		return resideAddrDt;
	}

	public void setResideAddrDt(String resideAddrDt) {
		this.resideAddrDt = resideAddrDt;
	}
	public String getDiagFitCodeOi() {
		return diagFitCodeOi;
	}

	public void setDiagFitCodeOi(String diagFitCodeOi) {
		this.diagFitCodeOi = diagFitCodeOi;
	}

	public String getDiagFitCodeCp() {
		return diagFitCodeCp;
	}

	public void setDiagFitCodeCp(String diagFitCodeCp) {
		this.diagFitCodeCp = diagFitCodeCp;
	}

	public String getDiagCodeClinicIcd() {
		return diagCodeClinicIcd;
	}

	public void setDiagCodeClinicIcd(String diagCodeClinicIcd) {
		this.diagCodeClinicIcd = diagCodeClinicIcd;
	}

	public String getDiagNameClinicIcd() {
		return diagNameClinicIcd;
	}

	public void setDiagNameClinicIcd(String diagNameClinicIcd) {
		this.diagNameClinicIcd = diagNameClinicIcd;
	}

	public String getDiagCodeExtcIpIcd() {
		return diagCodeExtcIpIcd;
	}

	public void setDiagCodeExtcIpIcd(String diagCodeExtcIpIcd) {
		this.diagCodeExtcIpIcd = diagCodeExtcIpIcd;
	}

	public String getDiagNameExtcIpIcd() {
		return diagNameExtcIpIcd;
	}

	public void setDiagNameExtcIpIcd(String diagNameExtcIpIcd) {
		this.diagNameExtcIpIcd = diagNameExtcIpIcd;
	}

	public String getDiagCodePathoIcd() {
		return diagCodePathoIcd;
	}

	public void setDiagCodePathoIcd(String diagCodePathoIcd) {
		this.diagCodePathoIcd = diagCodePathoIcd;
	}

	public String getDiagNamePathoIcd() {
		return diagNamePathoIcd;
	}

	public void setDiagNamePathoIcd(String diagNamePathoIcd) {
		this.diagNamePathoIcd = diagNamePathoIcd;
	}

	public String getPathoNoIcd() {
		return pathoNoIcd;
	}

	public void setPathoNoIcd(String pathoNoIcd) {
		this.pathoNoIcd = pathoNoIcd;
	}

	public String getBirthAddrCode() {
		return birthAddrCode;
	}

	public void setBirthAddrCode(String birthAddrCode) {
		this.birthAddrCode = birthAddrCode;
	}

	public String getOriginAddrCode() {
		return originAddrCode;
	}

	public void setOriginAddrCode(String originAddrCode) {
		this.originAddrCode = originAddrCode;
	}

	public String getCurrAddrCode() {
		return currAddrCode;
	}

	public void setCurrAddrCode(String currAddrCode) {
		this.currAddrCode = currAddrCode;
	}

	public String getResideAddrCode() {
		return resideAddrCode;
	}

	public void setResideAddrCode(String resideAddrCode) {
		this.resideAddrCode = resideAddrCode;
	}

	public String getInsurNo() {
		return insurNo;
	}

	public void setInsurNo(String insurNo) {
		this.insurNo = insurNo;
	}

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAdmitDeptCode() {
		return admitDeptCode;
	}

	public void setAdmitDeptCode(String admitDeptCode) {
		this.admitDeptCode = admitDeptCode;
	}

	public String getTransDeptCode() {
		return transDeptCode;
	}

	public void setTransDeptCode(String transDeptCode) {
		this.transDeptCode = transDeptCode;
	}

	public String getDisDeptCode() {
		return disDeptCode;
	}

	public void setDisDeptCode(String disDeptCode) {
		this.disDeptCode = disDeptCode;
	}

	public String getDiagCodeAdmit() {
		return diagCodeAdmit;
	}

	public void setDiagCodeAdmit(String diagCodeAdmit) {
		this.diagCodeAdmit = diagCodeAdmit;
	}

	public String getDiagNameAdmit() {
		return diagNameAdmit;
	}

	public void setDiagNameAdmit(String diagNameAdmit) {
		this.diagNameAdmit = diagNameAdmit;
	}

	public Date getDiagDate() {
		return diagDate;
	}

	public void setDiagDate(Date diagDate) {
		this.diagDate = diagDate;
	}

	public String getDisCondCode() {
		return disCondCode;
	}

	public void setDisCondCode(String disCondCode) {
		this.disCondCode = disCondCode;
	}

	public String getAdmitCondCode0() {
		return admitCondCode0;
	}

	public void setAdmitCondCode0(String admitCondCode0) {
		this.admitCondCode0 = admitCondCode0;
	}

	public String getDiagCodeDis1() {
		return diagCodeDis1;
	}

	public void setDiagCodeDis1(String diagCodeDis1) {
		this.diagCodeDis1 = diagCodeDis1;
	}

	public String getDiagNameDis1() {
		return diagNameDis1;
	}

	public void setDiagNameDis1(String diagNameDis1) {
		this.diagNameDis1 = diagNameDis1;
	}

	public String getAdmitCondCode2() {
		return admitCondCode2;
	}

	public void setAdmitCondCode2(String admitCondCode2) {
		this.admitCondCode2 = admitCondCode2;
	}

	public String getDisCondCode2() {
		return disCondCode2;
	}

	public void setDisCondCode2(String disCondCode2) {
		this.disCondCode2 = disCondCode2;
	}

	public String getDiagCodeDis2() {
		return diagCodeDis2;
	}

	public void setDiagCodeDis2(String diagCodeDis2) {
		this.diagCodeDis2 = diagCodeDis2;
	}

	public String getDiagNameDis2() {
		return diagNameDis2;
	}

	public void setDiagNameDis2(String diagNameDis2) {
		this.diagNameDis2 = diagNameDis2;
	}

	public String getAdmitCondCode3() {
		return admitCondCode3;
	}

	public void setAdmitCondCode3(String admitCondCode3) {
		this.admitCondCode3 = admitCondCode3;
	}

	public String getDisCondCode3() {
		return disCondCode3;
	}

	public void setDisCondCode3(String disCondCode3) {
		this.disCondCode3 = disCondCode3;
	}

	public String getDiagCodeDis3() {
		return diagCodeDis3;
	}

	public void setDiagCodeDis3(String diagCodeDis3) {
		this.diagCodeDis3 = diagCodeDis3;
	}

	public String getDiagNameDis3() {
		return diagNameDis3;
	}

	public void setDiagNameDis3(String diagNameDis3) {
		this.diagNameDis3 = diagNameDis3;
	}

	public String getAdmitCondCode4() {
		return admitCondCode4;
	}

	public void setAdmitCondCode4(String admitCondCode4) {
		this.admitCondCode4 = admitCondCode4;
	}

	public String getDisCondCode4() {
		return disCondCode4;
	}

	public void setDisCondCode4(String disCondCode4) {
		this.disCondCode4 = disCondCode4;
	}

	public String getDiagCodeDis4() {
		return diagCodeDis4;
	}

	public void setDiagCodeDis4(String diagCodeDis4) {
		this.diagCodeDis4 = diagCodeDis4;
	}

	public String getDiagNameDis4() {
		return diagNameDis4;
	}

	public void setDiagNameDis4(String diagNameDis4) {
		this.diagNameDis4 = diagNameDis4;
	}

	public String getAdmitCondCode5() {
		return admitCondCode5;
	}

	public void setAdmitCondCode5(String admitCondCode5) {
		this.admitCondCode5 = admitCondCode5;
	}

	public String getDisCondCode5() {
		return disCondCode5;
	}

	public void setDisCondCode5(String disCondCode5) {
		this.disCondCode5 = disCondCode5;
	}

	public String getDiagCodeDis5() {
		return diagCodeDis5;
	}

	public void setDiagCodeDis5(String diagCodeDis5) {
		this.diagCodeDis5 = diagCodeDis5;
	}

	public String getDiagNameDis5() {
		return diagNameDis5;
	}

	public void setDiagNameDis5(String diagNameDis5) {
		this.diagNameDis5 = diagNameDis5;
	}

	public String getAdmitCondCode6() {
		return admitCondCode6;
	}

	public void setAdmitCondCode6(String admitCondCode6) {
		this.admitCondCode6 = admitCondCode6;
	}

	public String getDisCondCode6() {
		return disCondCode6;
	}

	public void setDisCondCode6(String disCondCode6) {
		this.disCondCode6 = disCondCode6;
	}

	public String getDiagCodeDis6() {
		return diagCodeDis6;
	}

	public void setDiagCodeDis6(String diagCodeDis6) {
		this.diagCodeDis6 = diagCodeDis6;
	}

	public String getDiagNameDis6() {
		return diagNameDis6;
	}

	public void setDiagNameDis6(String diagNameDis6) {
		this.diagNameDis6 = diagNameDis6;
	}

	public String getAdmitCondCode7() {
		return admitCondCode7;
	}

	public void setAdmitCondCode7(String admitCondCode7) {
		this.admitCondCode7 = admitCondCode7;
	}

	public String getDisCondCode7() {
		return disCondCode7;
	}

	public void setDisCondCode7(String disCondCode7) {
		this.disCondCode7 = disCondCode7;
	}

	public String getDiagCodeDis7() {
		return diagCodeDis7;
	}

	public void setDiagCodeDis7(String diagCodeDis7) {
		this.diagCodeDis7 = diagCodeDis7;
	}

	public String getDiagNameDis7() {
		return diagNameDis7;
	}

	public void setDiagNameDis7(String diagNameDis7) {
		this.diagNameDis7 = diagNameDis7;
	}

	public String getAdmitCondCode8() {
		return admitCondCode8;
	}

	public void setAdmitCondCode8(String admitCondCode8) {
		this.admitCondCode8 = admitCondCode8;
	}

	public String getDisCondCode8() {
		return disCondCode8;
	}

	public void setDisCondCode8(String disCondCode8) {
		this.disCondCode8 = disCondCode8;
	}

	public String getDiagCodeDis8() {
		return diagCodeDis8;
	}

	public void setDiagCodeDis8(String diagCodeDis8) {
		this.diagCodeDis8 = diagCodeDis8;
	}

	public String getDiagNameDis8() {
		return diagNameDis8;
	}

	public void setDiagNameDis8(String diagNameDis8) {
		this.diagNameDis8 = diagNameDis8;
	}

	public String getAdmitCondCode9() {
		return admitCondCode9;
	}

	public void setAdmitCondCode9(String admitCondCode9) {
		this.admitCondCode9 = admitCondCode9;
	}

	public String getDisCondCode9() {
		return disCondCode9;
	}

	public void setDisCondCode9(String disCondCode9) {
		this.disCondCode9 = disCondCode9;
	}

	public String getDiagCodeDis9() {
		return diagCodeDis9;
	}

	public void setDiagCodeDis9(String diagCodeDis9) {
		this.diagCodeDis9 = diagCodeDis9;
	}

	public String getDiagNameDis9() {
		return diagNameDis9;
	}

	public void setDiagNameDis9(String diagNameDis9) {
		this.diagNameDis9 = diagNameDis9;
	}

	public String getAdmitCondCode10() {
		return admitCondCode10;
	}

	public void setAdmitCondCode10(String admitCondCode10) {
		this.admitCondCode10 = admitCondCode10;
	}

	public String getDisCondCode10() {
		return disCondCode10;
	}

	public void setDisCondCode10(String disCondCode10) {
		this.disCondCode10 = disCondCode10;
	}

	public String getDiagCodeDis10() {
		return diagCodeDis10;
	}

	public void setDiagCodeDis10(String diagCodeDis10) {
		this.diagCodeDis10 = diagCodeDis10;
	}

	public String getDiagNameDis10() {
		return diagNameDis10;
	}

	public void setDiagNameDis10(String diagNameDis10) {
		this.diagNameDis10 = diagNameDis10;
	}

	public Integer getNumInfect() {
		return numInfect;
	}

	public void setNumInfect(Integer numInfect) {
		this.numInfect = numInfect;
	}

	public String getDiagCodePatho2() {
		return diagCodePatho2;
	}

	public void setDiagCodePatho2(String diagCodePatho2) {
		this.diagCodePatho2 = diagCodePatho2;
	}

	public String getDiagNamePatho2() {
		return diagNamePatho2;
	}

	public void setDiagNamePatho2(String diagNamePatho2) {
		this.diagNamePatho2 = diagNamePatho2;
	}

	public String getPathoNo2() {
		return pathoNo2;
	}

	public void setPathoNo2(String pathoNo2) {
		this.pathoNo2 = pathoNo2;
	}

	public String getDiagCodePatho3() {
		return diagCodePatho3;
	}

	public void setDiagCodePatho3(String diagCodePatho3) {
		this.diagCodePatho3 = diagCodePatho3;
	}

	public String getDiagNamePatho3() {
		return diagNamePatho3;
	}

	public void setDiagNamePatho3(String diagNamePatho3) {
		this.diagNamePatho3 = diagNamePatho3;
	}

	public String getPathoNo3() {
		return pathoNo3;
	}

	public void setPathoNo3(String pathoNo3) {
		this.pathoNo3 = pathoNo3;
	}

	public String getDiagCodeExtcIp2() {
		return diagCodeExtcIp2;
	}

	public void setDiagCodeExtcIp2(String diagCodeExtcIp2) {
		this.diagCodeExtcIp2 = diagCodeExtcIp2;
	}

	public String getDiagNameExtcIp2() {
		return diagNameExtcIp2;
	}

	public void setDiagNameExtcIp2(String diagNameExtcIp2) {
		this.diagNameExtcIp2 = diagNameExtcIp2;
	}

	public String getDiagCodeExtcIp3() {
		return diagCodeExtcIp3;
	}

	public void setDiagCodeExtcIp3(String diagCodeExtcIp3) {
		this.diagCodeExtcIp3 = diagCodeExtcIp3;
	}

	public String getDiagNameExtcIp3() {
		return diagNameExtcIp3;
	}

	public void setDiagNameExtcIp3(String diagNameExtcIp3) {
		this.diagNameExtcIp3 = diagNameExtcIp3;
	}

	public String getAllergen() {
		return allergen;
	}

	public void setAllergen(String allergen) {
		this.allergen = allergen;
	}

	public String getDiagFitCodeIo() {
		return diagFitCodeIo;
	}

	public void setDiagFitCodeIo(String diagFitCodeIo) {
		this.diagFitCodeIo = diagFitCodeIo;
	}

	public String getDiagFitCodeBAOp() {
		return diagFitCodeBAOp;
	}

	public void setDiagFitCodeBAOp(String diagFitCodeBAOp) {
		this.diagFitCodeBAOp = diagFitCodeBAOp;
	}

	public String getDiagFitCodeRc() {
		return diagFitCodeRc;
	}

	public void setDiagFitCodeRc(String diagFitCodeRc) {
		this.diagFitCodeRc = diagFitCodeRc;
	}

	public String getOpCode1() {
		return opCode1;
	}

	public void setOpCode1(String opCode1) {
		this.opCode1 = opCode1;
	}

	public Date getOpDate1() {
		return opDate1;
	}

	public void setOpDate1(Date opDate1) {
		this.opDate1 = opDate1;
	}

	public String getGradeCode1() {
		return gradeCode1;
	}

	public void setGradeCode1(String gradeCode1) {
		this.gradeCode1 = gradeCode1;
	}

	public String getOpName1() {
		return opName1;
	}

	public void setOpName1(String opName1) {
		this.opName1 = opName1;
	}

	public String getOpSite1() {
		return opSite1;
	}

	public void setOpSite1(String opSite1) {
		this.opSite1 = opSite1;
	}

	public String getOpDuration1() {
		return opDuration1;
	}

	public void setOpDuration1(String opDuration1) {
		this.opDuration1 = opDuration1;
	}

	public String getOpDocName1() {
		return opDocName1;
	}

	public void setOpDocName1(String opDocName1) {
		this.opDocName1 = opDocName1;
	}

	public String getOpiName1() {
		return opiName1;
	}

	public void setOpiName1(String opiName1) {
		this.opiName1 = opiName1;
	}

	public String getOpiiName1() {
		return opiiName1;
	}

	public void setOpiiName1(String opiiName1) {
		this.opiiName1 = opiiName1;
	}

	public String getAnesTypeCode1() {
		return anesTypeCode1;
	}

	public void setAnesTypeCode1(String anesTypeCode1) {
		this.anesTypeCode1 = anesTypeCode1;
	}

	public String getAnesLevelCode1() {
		return anesLevelCode1;
	}

	public void setAnesLevelCode1(String anesLevelCode1) {
		this.anesLevelCode1 = anesLevelCode1;
	}

	public String getIncisionTypeCode1() {
		return incisionTypeCode1;
	}

	public void setIncisionTypeCode1(String incisionTypeCode1) {
		this.incisionTypeCode1 = incisionTypeCode1;
	}

	public String getAnesDocName1() {
		return anesDocName1;
	}

	public void setAnesDocName1(String anesDocName1) {
		this.anesDocName1 = anesDocName1;
	}

	public String getOpCode2() {
		return opCode2;
	}

	public void setOpCode2(String opCode2) {
		this.opCode2 = opCode2;
	}

	public Date getOpDate2() {
		return opDate2;
	}

	public void setOpDate2(Date opDate2) {
		this.opDate2 = opDate2;
	}

	public String getGradeCode2() {
		return gradeCode2;
	}

	public void setGradeCode2(String gradeCode2) {
		this.gradeCode2 = gradeCode2;
	}

	public String getOpName2() {
		return opName2;
	}

	public void setOpName2(String opName2) {
		this.opName2 = opName2;
	}

	public String getOpSite2() {
		return opSite2;
	}

	public void setOpSite2(String opSite2) {
		this.opSite2 = opSite2;
	}

	public String getOpDuration2() {
		return opDuration2;
	}

	public void setOpDuration2(String opDuration2) {
		this.opDuration2 = opDuration2;
	}

	public String getOpDocName2() {
		return opDocName2;
	}

	public void setOpDocName2(String opDocName2) {
		this.opDocName2 = opDocName2;
	}

	public String getOpiName2() {
		return opiName2;
	}

	public void setOpiName2(String opiName2) {
		this.opiName2 = opiName2;
	}

	public String getOpiiName2() {
		return opiiName2;
	}

	public void setOpiiName2(String opiiName2) {
		this.opiiName2 = opiiName2;
	}

	public String getAnesTypeCode2() {
		return anesTypeCode2;
	}

	public void setAnesTypeCode2(String anesTypeCode2) {
		this.anesTypeCode2 = anesTypeCode2;
	}

	public String getAnesLevelCode2() {
		return anesLevelCode2;
	}

	public void setAnesLevelCode2(String anesLevelCode2) {
		this.anesLevelCode2 = anesLevelCode2;
	}

	public String getIncisionTypeCode2() {
		return incisionTypeCode2;
	}

	public void setIncisionTypeCode2(String incisionTypeCode2) {
		this.incisionTypeCode2 = incisionTypeCode2;
	}

	public String getAnesDocName2() {
		return anesDocName2;
	}

	public void setAnesDocName2(String anesDocName2) {
		this.anesDocName2 = anesDocName2;
	}

	public String getOpCode3() {
		return opCode3;
	}

	public void setOpCode3(String opCode3) {
		this.opCode3 = opCode3;
	}

	public Date getOpDate3() {
		return opDate3;
	}

	public void setOpDate3(Date opDate3) {
		this.opDate3 = opDate3;
	}

	public String getGradeCode3() {
		return gradeCode3;
	}

	public void setGradeCode3(String gradeCode3) {
		this.gradeCode3 = gradeCode3;
	}

	public String getOpName3() {
		return opName3;
	}

	public void setOpName3(String opName3) {
		this.opName3 = opName3;
	}

	public String getOpSite3() {
		return opSite3;
	}

	public void setOpSite3(String opSite3) {
		this.opSite3 = opSite3;
	}

	public String getOpDuration3() {
		return opDuration3;
	}

	public void setOpDuration3(String opDuration3) {
		this.opDuration3 = opDuration3;
	}

	public String getOpDocName3() {
		return opDocName3;
	}

	public void setOpDocName3(String opDocName3) {
		this.opDocName3 = opDocName3;
	}

	public String getOpiName3() {
		return opiName3;
	}

	public void setOpiName3(String opiName3) {
		this.opiName3 = opiName3;
	}

	public String getOpiiName3() {
		return opiiName3;
	}

	public void setOpiiName3(String opiiName3) {
		this.opiiName3 = opiiName3;
	}

	public String getAnesTypeCode3() {
		return anesTypeCode3;
	}

	public void setAnesTypeCode3(String anesTypeCode3) {
		this.anesTypeCode3 = anesTypeCode3;
	}

	public String getAnesLevelCode3() {
		return anesLevelCode3;
	}

	public void setAnesLevelCode3(String anesLevelCode3) {
		this.anesLevelCode3 = anesLevelCode3;
	}

	public String getIncisionTypeCode3() {
		return incisionTypeCode3;
	}

	public void setIncisionTypeCode3(String incisionTypeCode3) {
		this.incisionTypeCode3 = incisionTypeCode3;
	}

	public String getAnesDocName3() {
		return anesDocName3;
	}

	public void setAnesDocName3(String anesDocName3) {
		this.anesDocName3 = anesDocName3;
	}

	public String getOpCode4() {
		return opCode4;
	}

	public void setOpCode4(String opCode4) {
		this.opCode4 = opCode4;
	}

	public Date getOpDate4() {
		return opDate4;
	}

	public void setOpDate4(Date opDate4) {
		this.opDate4 = opDate4;
	}

	public String getGradeCode4() {
		return gradeCode4;
	}

	public void setGradeCode4(String gradeCode4) {
		this.gradeCode4 = gradeCode4;
	}

	public String getOpName4() {
		return opName4;
	}

	public void setOpName4(String opName4) {
		this.opName4 = opName4;
	}

	public String getOpSite4() {
		return opSite4;
	}

	public void setOpSite4(String opSite4) {
		this.opSite4 = opSite4;
	}

	public String getOpDuration4() {
		return opDuration4;
	}

	public void setOpDuration4(String opDuration4) {
		this.opDuration4 = opDuration4;
	}

	public String getOpDocName4() {
		return opDocName4;
	}

	public void setOpDocName4(String opDocName4) {
		this.opDocName4 = opDocName4;
	}

	public String getOpiName4() {
		return opiName4;
	}

	public void setOpiName4(String opiName4) {
		this.opiName4 = opiName4;
	}

	public String getOpiiName4() {
		return opiiName4;
	}

	public void setOpiiName4(String opiiName4) {
		this.opiiName4 = opiiName4;
	}

	public String getAnesTypeCode4() {
		return anesTypeCode4;
	}

	public void setAnesTypeCode4(String anesTypeCode4) {
		this.anesTypeCode4 = anesTypeCode4;
	}

	public String getAnesLevelCode4() {
		return anesLevelCode4;
	}

	public void setAnesLevelCode4(String anesLevelCode4) {
		this.anesLevelCode4 = anesLevelCode4;
	}

	public String getIncisionTypeCode4() {
		return incisionTypeCode4;
	}

	public void setIncisionTypeCode4(String incisionTypeCode4) {
		this.incisionTypeCode4 = incisionTypeCode4;
	}

	public String getAnesDocName4() {
		return anesDocName4;
	}

	public void setAnesDocName4(String anesDocName4) {
		this.anesDocName4 = anesDocName4;
	}

	public String getOpCode5() {
		return opCode5;
	}

	public void setOpCode5(String opCode5) {
		this.opCode5 = opCode5;
	}

	public Date getOpDate5() {
		return opDate5;
	}

	public void setOpDate5(Date opDate5) {
		this.opDate5 = opDate5;
	}

	public String getGradeCode5() {
		return gradeCode5;
	}

	public void setGradeCode5(String gradeCode5) {
		this.gradeCode5 = gradeCode5;
	}

	public String getOpName5() {
		return opName5;
	}

	public void setOpName5(String opName5) {
		this.opName5 = opName5;
	}

	public String getOpSite5() {
		return opSite5;
	}

	public void setOpSite5(String opSite5) {
		this.opSite5 = opSite5;
	}

	public String getOpDuration5() {
		return opDuration5;
	}

	public void setOpDuration5(String opDuration5) {
		this.opDuration5 = opDuration5;
	}

	public String getOpDocName5() {
		return opDocName5;
	}

	public void setOpDocName5(String opDocName5) {
		this.opDocName5 = opDocName5;
	}

	public String getOpiName5() {
		return opiName5;
	}

	public void setOpiName5(String opiName5) {
		this.opiName5 = opiName5;
	}

	public String getOpiiName5() {
		return opiiName5;
	}

	public void setOpiiName5(String opiiName5) {
		this.opiiName5 = opiiName5;
	}

	public String getAnesTypeCode5() {
		return anesTypeCode5;
	}

	public void setAnesTypeCode5(String anesTypeCode5) {
		this.anesTypeCode5 = anesTypeCode5;
	}

	public String getAnesLevelCode5() {
		return anesLevelCode5;
	}

	public void setAnesLevelCode5(String anesLevelCode5) {
		this.anesLevelCode5 = anesLevelCode5;
	}

	public String getIncisionTypeCode5() {
		return incisionTypeCode5;
	}

	public void setIncisionTypeCode5(String incisionTypeCode5) {
		this.incisionTypeCode5 = incisionTypeCode5;
	}

	public String getAnesDocName5() {
		return anesDocName5;
	}

	public void setAnesDocName5(String anesDocName5) {
		this.anesDocName5 = anesDocName5;
	}

	public String getOpCode6() {
		return opCode6;
	}

	public void setOpCode6(String opCode6) {
		this.opCode6 = opCode6;
	}

	public Date getOpDate6() {
		return opDate6;
	}

	public void setOpDate6(Date opDate6) {
		this.opDate6 = opDate6;
	}

	public String getGradeCode6() {
		return gradeCode6;
	}

	public void setGradeCode6(String gradeCode6) {
		this.gradeCode6 = gradeCode6;
	}

	public String getOpName6() {
		return opName6;
	}

	public void setOpName6(String opName6) {
		this.opName6 = opName6;
	}

	public String getOpSite6() {
		return opSite6;
	}

	public void setOpSite6(String opSite6) {
		this.opSite6 = opSite6;
	}

	public String getOpDuration6() {
		return opDuration6;
	}

	public void setOpDuration6(String opDuration6) {
		this.opDuration6 = opDuration6;
	}

	public String getOpDocName6() {
		return opDocName6;
	}

	public void setOpDocName6(String opDocName6) {
		this.opDocName6 = opDocName6;
	}

	public String getOpiName6() {
		return opiName6;
	}

	public void setOpiName6(String opiName6) {
		this.opiName6 = opiName6;
	}

	public String getOpiiName6() {
		return opiiName6;
	}

	public void setOpiiName6(String opiiName6) {
		this.opiiName6 = opiiName6;
	}

	public String getAnesTypeCode6() {
		return anesTypeCode6;
	}

	public void setAnesTypeCode6(String anesTypeCode6) {
		this.anesTypeCode6 = anesTypeCode6;
	}

	public String getAnesLevelCode6() {
		return anesLevelCode6;
	}

	public void setAnesLevelCode6(String anesLevelCode6) {
		this.anesLevelCode6 = anesLevelCode6;
	}

	public String getIncisionTypeCode6() {
		return incisionTypeCode6;
	}

	public void setIncisionTypeCode6(String incisionTypeCode6) {
		this.incisionTypeCode6 = incisionTypeCode6;
	}

	public String getAnesDocName6() {
		return anesDocName6;
	}

	public void setAnesDocName6(String anesDocName6) {
		this.anesDocName6 = anesDocName6;
	}

	public String getOpCode7() {
		return opCode7;
	}

	public void setOpCode7(String opCode7) {
		this.opCode7 = opCode7;
	}

	public Date getOpDate7() {
		return opDate7;
	}

	public void setOpDate7(Date opDate7) {
		this.opDate7 = opDate7;
	}

	public String getGradeCode7() {
		return gradeCode7;
	}

	public void setGradeCode7(String gradeCode7) {
		this.gradeCode7 = gradeCode7;
	}

	public String getOpName7() {
		return opName7;
	}

	public void setOpName7(String opName7) {
		this.opName7 = opName7;
	}

	public String getOpSite7() {
		return opSite7;
	}

	public void setOpSite7(String opSite7) {
		this.opSite7 = opSite7;
	}

	public String getOpDuration7() {
		return opDuration7;
	}

	public void setOpDuration7(String opDuration7) {
		this.opDuration7 = opDuration7;
	}

	public String getOpDocName7() {
		return opDocName7;
	}

	public void setOpDocName7(String opDocName7) {
		this.opDocName7 = opDocName7;
	}

	public String getOpiName7() {
		return opiName7;
	}

	public void setOpiName7(String opiName7) {
		this.opiName7 = opiName7;
	}

	public String getOpiiName7() {
		return opiiName7;
	}

	public void setOpiiName7(String opiiName7) {
		this.opiiName7 = opiiName7;
	}

	public String getAnesTypeCode7() {
		return anesTypeCode7;
	}

	public void setAnesTypeCode7(String anesTypeCode7) {
		this.anesTypeCode7 = anesTypeCode7;
	}

	public String getAnesLevelCode7() {
		return anesLevelCode7;
	}

	public void setAnesLevelCode7(String anesLevelCode7) {
		this.anesLevelCode7 = anesLevelCode7;
	}

	public String getIncisionTypeCode7() {
		return incisionTypeCode7;
	}

	public void setIncisionTypeCode7(String incisionTypeCode7) {
		this.incisionTypeCode7 = incisionTypeCode7;
	}

	public String getAnesDocName7() {
		return anesDocName7;
	}

	public void setAnesDocName7(String anesDocName7) {
		this.anesDocName7 = anesDocName7;
	}

	public String getOpCode8() {
		return opCode8;
	}

	public void setOpCode8(String opCode8) {
		this.opCode8 = opCode8;
	}

	public Date getOpDate8() {
		return opDate8;
	}

	public void setOpDate8(Date opDate8) {
		this.opDate8 = opDate8;
	}

	public String getGradeCode8() {
		return gradeCode8;
	}

	public void setGradeCode8(String gradeCode8) {
		this.gradeCode8 = gradeCode8;
	}

	public String getOpName8() {
		return opName8;
	}

	public void setOpName8(String opName8) {
		this.opName8 = opName8;
	}

	public String getOpSite8() {
		return opSite8;
	}

	public void setOpSite8(String opSite8) {
		this.opSite8 = opSite8;
	}

	public String getOpDuration8() {
		return opDuration8;
	}

	public void setOpDuration8(String opDuration8) {
		this.opDuration8 = opDuration8;
	}

	public String getOpDocName8() {
		return opDocName8;
	}

	public void setOpDocName8(String opDocName8) {
		this.opDocName8 = opDocName8;
	}

	public String getOpiName8() {
		return opiName8;
	}

	public void setOpiName8(String opiName8) {
		this.opiName8 = opiName8;
	}

	public String getOpiiName8() {
		return opiiName8;
	}

	public void setOpiiName8(String opiiName8) {
		this.opiiName8 = opiiName8;
	}

	public String getAnesTypeCode8() {
		return anesTypeCode8;
	}

	public void setAnesTypeCode8(String anesTypeCode8) {
		this.anesTypeCode8 = anesTypeCode8;
	}

	public String getAnesLevelCode8() {
		return anesLevelCode8;
	}

	public void setAnesLevelCode8(String anesLevelCode8) {
		this.anesLevelCode8 = anesLevelCode8;
	}

	public String getIncisionTypeCode8() {
		return incisionTypeCode8;
	}

	public void setIncisionTypeCode8(String incisionTypeCode8) {
		this.incisionTypeCode8 = incisionTypeCode8;
	}

	public String getAnesDocName8() {
		return anesDocName8;
	}

	public void setAnesDocName8(String anesDocName8) {
		this.anesDocName8 = anesDocName8;
	}

	public String getOpCode9() {
		return opCode9;
	}

	public void setOpCode9(String opCode9) {
		this.opCode9 = opCode9;
	}

	public Date getOpDate9() {
		return opDate9;
	}

	public void setOpDate9(Date opDate9) {
		this.opDate9 = opDate9;
	}

	public String getGradeCode9() {
		return gradeCode9;
	}

	public void setGradeCode9(String gradeCode9) {
		this.gradeCode9 = gradeCode9;
	}

	public String getOpName9() {
		return opName9;
	}

	public void setOpName9(String opName9) {
		this.opName9 = opName9;
	}

	public String getOpSite9() {
		return opSite9;
	}

	public void setOpSite9(String opSite9) {
		this.opSite9 = opSite9;
	}

	public String getOpDuration9() {
		return opDuration9;
	}

	public void setOpDuration9(String opDuration9) {
		this.opDuration9 = opDuration9;
	}

	public String getOpDocName9() {
		return opDocName9;
	}

	public void setOpDocName9(String opDocName9) {
		this.opDocName9 = opDocName9;
	}

	public String getOpiName9() {
		return opiName9;
	}

	public void setOpiName9(String opiName9) {
		this.opiName9 = opiName9;
	}

	public String getOpiiName9() {
		return opiiName9;
	}

	public void setOpiiName9(String opiiName9) {
		this.opiiName9 = opiiName9;
	}

	public String getAnesTypeCode9() {
		return anesTypeCode9;
	}

	public void setAnesTypeCode9(String anesTypeCode9) {
		this.anesTypeCode9 = anesTypeCode9;
	}

	public String getAnesLevelCode9() {
		return anesLevelCode9;
	}

	public void setAnesLevelCode9(String anesLevelCode9) {
		this.anesLevelCode9 = anesLevelCode9;
	}

	public String getIncisionTypeCode9() {
		return incisionTypeCode9;
	}

	public void setIncisionTypeCode9(String incisionTypeCode9) {
		this.incisionTypeCode9 = incisionTypeCode9;
	}

	public String getAnesDocName9() {
		return anesDocName9;
	}

	public void setAnesDocName9(String anesDocName9) {
		this.anesDocName9 = anesDocName9;
	}

	public String getOpCode10() {
		return opCode10;
	}

	public void setOpCode10(String opCode10) {
		this.opCode10 = opCode10;
	}

	public Date getOpDate10() {
		return opDate10;
	}

	public void setOpDate10(Date opDate10) {
		this.opDate10 = opDate10;
	}

	public String getGradeCode10() {
		return gradeCode10;
	}

	public void setGradeCode10(String gradeCode10) {
		this.gradeCode10 = gradeCode10;
	}

	public String getOpName10() {
		return opName10;
	}

	public void setOpName10(String opName10) {
		this.opName10 = opName10;
	}

	public String getOpSite10() {
		return opSite10;
	}

	public void setOpSite10(String opSite10) {
		this.opSite10 = opSite10;
	}

	public String getOpDuration10() {
		return opDuration10;
	}

	public void setOpDuration10(String opDuration10) {
		this.opDuration10 = opDuration10;
	}

	public String getOpDocName10() {
		return opDocName10;
	}

	public void setOpDocName10(String opDocName10) {
		this.opDocName10 = opDocName10;
	}

	public String getOpiName10() {
		return opiName10;
	}

	public void setOpiName10(String opiName10) {
		this.opiName10 = opiName10;
	}

	public String getOpiiName10() {
		return opiiName10;
	}

	public void setOpiiName10(String opiiName10) {
		this.opiiName10 = opiiName10;
	}

	public String getAnesTypeCode10() {
		return anesTypeCode10;
	}

	public void setAnesTypeCode10(String anesTypeCode10) {
		this.anesTypeCode10 = anesTypeCode10;
	}

	public String getAnesLevelCode10() {
		return anesLevelCode10;
	}

	public void setAnesLevelCode10(String anesLevelCode10) {
		this.anesLevelCode10 = anesLevelCode10;
	}

	public String getIncisionTypeCode10() {
		return incisionTypeCode10;
	}

	public void setIncisionTypeCode10(String incisionTypeCode10) {
		this.incisionTypeCode10 = incisionTypeCode10;
	}

	public String getAnesDocName10() {
		return anesDocName10;
	}

	public void setAnesDocName10(String anesDocName10) {
		this.anesDocName10 = anesDocName10;
	}

	

	public BigDecimal getCost1() {
		return cost1;
	}

	public void setCost1(BigDecimal cost1) {
		this.cost1 = cost1;
	}

	public BigDecimal getCost2() {
		return cost2;
	}

	public void setCost2(BigDecimal cost2) {
		this.cost2 = cost2;
	}

	public BigDecimal getCost3() {
		return cost3;
	}

	public void setCost3(BigDecimal cost3) {
		this.cost3 = cost3;
	}

	public BigDecimal getCost4() {
		return cost4;
	}

	public void setCost4(BigDecimal cost4) {
		this.cost4 = cost4;
	}

	public BigDecimal getCost5() {
		return cost5;
	}

	public void setCost5(BigDecimal cost5) {
		this.cost5 = cost5;
	}

	public BigDecimal getCost6() {
		return cost6;
	}

	public void setCost6(BigDecimal cost6) {
		this.cost6 = cost6;
	}

	public BigDecimal getCost7() {
		return cost7;
	}

	public void setCost7(BigDecimal cost7) {
		this.cost7 = cost7;
	}

	public BigDecimal getCost8() {
		return cost8;
	}

	public void setCost8(BigDecimal cost8) {
		this.cost8 = cost8;
	}

	public BigDecimal getCost9() {
		return cost9;
	}

	public void setCost9(BigDecimal cost9) {
		this.cost9 = cost9;
	}

	public BigDecimal getCost0901() {
		return cost0901;
	}

	public void setCost0901(BigDecimal cost0901) {
		this.cost0901 = cost0901;
	}

	public BigDecimal getCost10() {
		return cost10;
	}

	public void setCost10(BigDecimal cost10) {
		this.cost10 = cost10;
	}

	public BigDecimal getCost1001() {
		return cost1001;
	}

	public void setCost1001(BigDecimal cost1001) {
		this.cost1001 = cost1001;
	}

	public BigDecimal getCost1002() {
		return cost1002;
	}

	public void setCost1002(BigDecimal cost1002) {
		this.cost1002 = cost1002;
	}

	public BigDecimal getCost11() {
		return cost11;
	}

	public void setCost11(BigDecimal cost11) {
		this.cost11 = cost11;
	}

	public BigDecimal getCost12() {
		return cost12;
	}

	public void setCost12(BigDecimal cost12) {
		this.cost12 = cost12;
	}

	public BigDecimal getCost13() {
		return cost13;
	}

	public void setCost13(BigDecimal cost13) {
		this.cost13 = cost13;
	}

	public BigDecimal getCost1301() {
		return cost1301;
	}

	public void setCost1301(BigDecimal cost1301) {
		this.cost1301 = cost1301;
	}

	public BigDecimal getCost14() {
		return cost14;
	}

	public void setCost14(BigDecimal cost14) {
		this.cost14 = cost14;
	}

	public BigDecimal getCost15() {
		return cost15;
	}

	public void setCost15(BigDecimal cost15) {
		this.cost15 = cost15;
	}

	public BigDecimal getCost16() {
		return cost16;
	}

	public void setCost16(BigDecimal cost16) {
		this.cost16 = cost16;
	}

	public BigDecimal getCost17() {
		return cost17;
	}

	public void setCost17(BigDecimal cost17) {
		this.cost17 = cost17;
	}

	public BigDecimal getCost18() {
		return cost18;
	}

	public void setCost18(BigDecimal cost18) {
		this.cost18 = cost18;
	}

	public BigDecimal getCost19() {
		return cost19;
	}

	public void setCost19(BigDecimal cost19) {
		this.cost19 = cost19;
	}

	public BigDecimal getCost20() {
		return cost20;
	}

	public void setCost20(BigDecimal cost20) {
		this.cost20 = cost20;
	}

	public BigDecimal getCost21() {
		return cost21;
	}

	public void setCost21(BigDecimal cost21) {
		this.cost21 = cost21;
	}

	public BigDecimal getCost22() {
		return cost22;
	}

	public void setCost22(BigDecimal cost22) {
		this.cost22 = cost22;
	}

	public BigDecimal getCost23() {
		return cost23;
	}

	public void setCost23(BigDecimal cost23) {
		this.cost23 = cost23;
	}

	public BigDecimal getCost24() {
		return cost24;
	}

	public void setCost24(BigDecimal cost24) {
		this.cost24 = cost24;
	}

	public String getAdmitCondCode1() {
		return admitCondCode1;
	}

	public void setAdmitCondCode1(String admitCondCode1) {
		this.admitCondCode1 = admitCondCode1;
	}
	
	
}