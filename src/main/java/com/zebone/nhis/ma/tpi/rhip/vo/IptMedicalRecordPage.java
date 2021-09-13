package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.zebone.nhis.ma.tpi.rhip.support.StringAdapter;

/**
 * 住院病案首页
 * @author chengjia
 *
 */
@XmlRootElement(name = "Ipt_MedicalRecordPage")
@XmlAccessorType(XmlAccessType.FIELD)  
public class IptMedicalRecordPage {
    @XmlElement(name = "JZLSH", required = true)
    protected String jzlsh;
    @XmlElement(name = "HealthCardID")
    protected String healthCardID;
    @XmlElement(name = "HospizationID", required = true)
    protected String hospizationID;
    @XmlElement(name = "MedicalRecordID", required = true)
    protected String medicalRecordID;
    @XmlElement(name = "PathologyID")
    protected String pathologyID;
    @XmlElement(name = "KH", required = true)
    protected String kh;
    @XmlElement(name = "KLX", required = true)
    protected String klx;
    @XmlElement(name = "XM")
    protected String xm;
    @XmlElement(name = "Sex")
    protected String sex;
    @XmlElement(name = "HYZKDM")
    protected String hyzkdm;
    @XmlElement(name = "ZYLBDM")
    protected String zylbdm;
//    @XmlElement(name = "GJ")
//    protected String gj;
    @XmlElement(name = "CSRQ")
    protected String csrqsj;
    @XmlElement(name = "ABOXX", required = true)
    protected String aboxx;
    @XmlElement(name = "RHXX")
    protected String rhxx;
    @XmlElement(name = "OriginProvince")
    protected String originProvince;
    @XmlElement(name = "OriginCity")
    protected String originCity;
    @XmlElement(name = "PresentAddrProvince")
    protected String presentAddrProvince;
    @XmlElement(name = "PresentAddrCity")
    protected String presentAddrCity;
    @XmlElement(name = "PresentAddrCounty")
    protected String presentAddrCounty;
    @XmlElement(name = "PresentAddrOther",defaultValue = "")
    protected String presentAddrOther;
    @XmlElement(name = "PresentAddrPostalcode",defaultValue = "")
    protected String presentAddrPostalcode;
    @XmlElement(name = "MedicalPayment", required = true)
    protected String medicalPayment;
    @XmlElement(name = "OutsideReasonOfInjuryAndPoCode")
    protected String outsideReasonOfInjuryAndPoCode;
    @XmlElement(name = "MasterDiseaseCode")
    protected String masterDiseaseCode;
    @XmlElement(name = "MasterDiseaseName", required = true)
    protected String masterDiseaseName;
    @XmlElement(name = "MasterAdmissionCondition", required = true)
    protected String masterAdmissionCondition;
    @XmlElement(name = "CYZYZD_ZBMC")
    protected String cyzyzdzbmc;
    @XmlElement(name = "CYZYZD_ZBBM")
    protected String cyzyzdzbbm;
    @XmlElement(name = "CYZYZD_RYBQ")
    protected String cyzyzdrybq;
    @XmlElement(name = "CYZYZD_ZZMC")
    protected String cyzyzdzzmc;
    @XmlElement(name = "CYZYZD_ZZBM")
    protected String cyzyzdzzbm;
    @XmlElement(name = "CYZYZD_ZZ_RYBQ")
    protected String cyzyzdzzrybq;
    @XmlElement(name = "PathologyDiseaseCode")
    protected String pathologyDiseaseCode;
    @XmlElement(name = "PathologyDiseaseName")
    protected String pathologyDiseaseName;
    @XmlElement(name = "ClinicWesternDiagnosisCode")
    protected String clinicWesternDiagnosisCode;
    @XmlElement(name = "ClinicDiagnosis")
    protected String clinicDiagnosis;
    @XmlElement(name = "MJZZD_ZYZDMC")
    protected String mjzzdzyzdmc;
    @XmlElement(name = "MJZZD_ZYZDBM")
    protected String mjzzdzyzdbm;
    @XmlElement(name = "MJZZD_ZYZHMC")
    protected String mjzzdzyzhmc;
    @XmlElement(name = "MJZZD_ZYZHBM")
    protected String mjzzdzyzhbm;
    @XmlElement(name = "SYZYZJBZ")
    protected String syzyzjbz;
    @XmlElement(name = "SYZYSBBZ")
    protected String syzysbbz;
    @XmlElement(name = "SYZYJSBZ")
    protected String syzyjsbz;
    @XmlElement(name = "BZSHBZ")
    protected String bzshbz;
    @XmlElement(name = "MonthsAge")
    protected String monthsAge;
    @XmlElement(name = "NeonatalBirthWeight")
    protected String neonatalBirthWeight;
    @XmlElement(name = "NeonatalAdmissionWeight")
    protected String neonatalAdmissionWeight;
    @XmlElement(name = "Province")
    protected String province;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "County")
    protected String county;
    @XmlElement(name = "PatientPhone", required = true)
    protected String patientPhone;
    @XmlElement(name = "RegisteredPermanentResidencePr")
    protected String registeredPermanentResidencePr;
    @XmlElement(name = "RegisteredPermanentResidenceCi")
    protected String registeredPermanentResidenceCi;
    @XmlElement(name = "RegisteredPermanentResidenceTo")
    protected String registeredPermanentResidenceTo;
    @XmlElement(name = "RegisteredPermanentResidenceOt")
    protected String registeredPermanentResidenceOt;
    @XmlElement(name = "RegisteredPermanentResidencePo")
    protected String registeredPermanentResidencePo;
    @XmlElement(name = "WorkAddrPhone")
    protected String workAddrPhone;
    @XmlElement(name = "AdmissionWay", required = true)
    protected String admissionWay;
    @XmlElement(name = "DischargeDateTime", required = true)
    protected String dischargeDateTime;
    @XmlElement(name = "DischargeSickRoom", required = true)
    protected String dischargeSickRoom;
    @XmlElement(name = "ActualHospitalizationDays", required = true)
    protected String actualHospitalizationDays;
    @XmlElement(name = "MasterDischargePrognosisCondit")
    protected String masterDischargePrognosisCondit;
    @XmlElement(name = "OutsideReasonOfInjuryAndPoison")
    protected String outsideReasonOfInjuryAndPoison;
    @XmlElement(name = "DrugAllergy")
    protected String drugAllergy;
    @XmlElement(name = "AllergicDrug")
    protected String allergicDrug;
    @XmlElement(name = "AdmissionDateTime", required = true)
    protected String admissionDateTime;
    @XmlElement(name = "AdmissionSickroom", required = true)
    protected String admissionSickroom;
    @XmlElement(name = "AutopsySign", required = true)
    protected String autopsySign;
    @XmlElement(name = "MedicalRecordQuality")
    protected String medicalRecordQuality;
    @XmlElement(name = "QualityControlDate")
    protected String qualityControlDate;
    @XmlElement(name = "DischargeMethods", required = true)
    protected String dischargeMethods;
    @XmlElement(name = "PrepareAcceptHospitalName")
    protected String prepareAcceptHospitalName;
    @XmlElement(name = "Day31InpatientMk", required = true)
    protected String day31InpatientMk;
    @XmlElement(name = "Day31InpatientAim")
    protected String day31InpatientAim;
    @XmlElement(name = "StuporTimeBeforeAdmission")
    protected String stuporTimeBeforeAdmission;
    @XmlElement(name = "StuporTimeAfterAdmission")
    protected String stuporTimeAfterAdmission;
    @XmlElement(name = "DiagCoinOutpatientVsDischarge")
    protected String diagCoinOutpatientVsDischarge;
    @XmlElement(name = "DiagCoinAdmssionVsDischarge")
    protected String diagCoinAdmssionVsDischarge;//诊断符合情况-入院和出院//0	未做1符合2不符合3不确定
    @XmlElement(name = "DiagCoinPreoperativeVsPost")
    protected String diagCoinPreoperativeVsPost;
    @XmlElement(name = "DiagCoinClinicalVsPathologica")
    protected String diagCoinClinicalVsPathologica;
    @XmlElement(name = "DiagCoinRadiologyVspathology")
    protected String diagCoinRadiologyVspathology;
    @XmlElement(name = "SalvageConditionSalvageTimes")
    protected String salvageConditionSalvageTimes;
    @XmlElement(name = "SalvageConditionSuccessTimes")
    protected String salvageConditionSuccessTimes;
    @XmlElement(name = "ClinicalPathManagement", required = true)
    protected String clinicalPathManagement;
    @XmlElement(name = "AttendingDoctor", required = true)
    protected String attendingDoctor;
    @XmlElement(name = "HospizationDoctor", required = true)
    protected String hospizationDoctor;
    @XmlElement(name = "ResponsibilityNurse", required = true)
    protected String responsibilityNurse;
    @XmlElement(name = "RefresherDoctors", required = true)
    protected String refresherDoctors;
    @XmlElement(name = "InternDoctor", required = true)
    protected String internDoctor;
    @XmlElement(name = "MedicalRecordCoderSign", required = true)
    protected String medicalRecordCoderSign;
    @XmlElement(name = "QualityControlDoctor", required = true)
    protected String qualityControlDoctor;
    @XmlElement(name = "QualityControlSign", required = true)
    protected String qualityControlSign;
    @XmlElement(name = "TransferredDeptName")
    protected String transferredDeptName;
    @XmlElement(name = "DischargeDepartment", required = true)
    protected String dischargeDepartment;
    @XmlElement(name = "AdmissionDeptName", required = true)
    protected String admissionDeptName;
    @XmlElement(name = "GenerMediServCharge")
    protected String generMediServCharge;
    @XmlElement(name = "GenerMediServCharge_ZYBZLZF")
    protected String generMediServChargeZYBZLZF;
    @XmlElement(name = "GenerMediServCharge_ZYBZLZHZF")
    protected String generMediServChargeZYBZLZHZF;
    @XmlElement(name = "GenerTreatHandlingFee")
    protected String generTreatHandlingFee;
    @XmlElement(name = "Nurse")
    protected String nurse;
    @XmlElement(name = "GenerMediServChargeOther")
    protected String generMediServChargeOther;
    @XmlElement(name = "PathologicalFee")
    protected String pathologicalFee;
    @XmlElement(name = "LaboratoryFee")
    protected String laboratoryFee;
    @XmlElement(name = "ImagingFee")
    protected String imagingFee;
    @XmlElement(name = "ClinicalDiagnosisFee")
    protected String clinicalDiagnosisFee;
    @XmlElement(name = "NonoperativeTreatFee")
    protected String nonoperativeTreatFee;
    @XmlElement(name = "ClinicalPhysicalTreatment")
    protected String clinicalPhysicalTreatment;
    @XmlElement(name = "SurgicalTreatment")
    protected String surgicalTreatment;
    @XmlElement(name = "EstheticFee")
    protected String estheticFee;
    @XmlElement(name = "OperationFee")
    protected String operationFee;
    @XmlElement(name = "RehabilitationFee")
    protected String rehabilitationFee;
    @XmlElement(name = "ZYL_ZYZDF")
    protected String zylzyzdf;
    @XmlElement(name = "ZYZLFee")
    protected String zyzlFee;
    @XmlElement(name = "ZYZLF_ZYWZF")
    protected String zyzlfzywzf;
    @XmlElement(name = "ZYZLF_ZYGSF")
    protected String zyzlfzygsf;
    @XmlElement(name = "ZYZLF_ZYZJF")
    protected String zyzlfzyzjf;
    @XmlElement(name = "ZYZLF_ZYTNF")
    protected String zyzlfzytnf;
    @XmlElement(name = "ZYZLF_ZYGCZLF")
    protected String zyzlfzygczlf;
    @XmlElement(name = "ZYZLF_ZYTSZLF")
    protected String zyzlfzytszlf;
    @XmlElement(name = "ZYQTF")
    protected String zyqtf;
    @XmlElement(name = "ZYQTF_TSTPJGF")
    protected String zyqtftstpjgf;
    @XmlElement(name = "ZYQTF_BZSSF")
    protected String zyqtfbzssf;
    @XmlElement(name = "MedicineChina")
    protected String medicineChina;
    @XmlElement(name = "MedicineChina_ZYZJF")
    protected String medicineChinaZYZJF;
    @XmlElement(name = "XYFee")
    protected String xyFee;
    @XmlElement(name = "HerbalMedicineFee")
    protected String herbalMedicineFee;
    @XmlElement(name = "AntibacterialDrugExp")
    protected String antibacterialDrugExp;
    @XmlElement(name = "BloodFee")
    protected String bloodFee;
    @XmlElement(name = "ACPFee")
    protected String acpFee;
    @XmlElement(name = "GCPFee")
    protected String gcpFee;
    @XmlElement(name = "NXYZFee")
    protected String nxyzFee;
    @XmlElement(name = "XBYZFee")
    protected String xbyzFee;
    @XmlElement(name = "DeptManager", required = true)
    protected String deptManager;
    @XmlElement(name = "YCYYCXFee")
    protected String ycyycxFee;
    @XmlElement(name = "ChiefDoctorSign", required = true)
    protected String chiefDoctorSign;
    @XmlElement(name = "ZLYYCXFee")
    protected String zlyycxFee;
    @XmlElement(name = "SSYYCXZLFee")
    protected String ssyycxzlFee;
    @XmlElement(name = "OtherFee")
    protected String otherFee;
    @XmlElement(name = "HospizationTotalPersonalCost", required = true)
    protected String hospizationTotalPersonalCost;
    @XmlElement(name = "HospizationTotalCost", required = true)
    protected String hospizationTotalCost;
    @XmlElement(name = "MZ")
    protected String mz;
    @XmlElement(name = "ZYCS")
    protected String zycs;
    @XmlElement(name = "GZDWMC")
    protected String gzdwmc;
    @XmlElement(name = "GZDWDZ")
    protected String gzdwdz;
    @XmlElement(name = "GZDDYB")
    protected String gzddyb;
    @XmlElement(name = "LXRXM")
    protected String lxrxm;
    @XmlElement(name = "LXRYHZGX")
    protected String lxryhzgx;
    @XmlElement(name = "LXRDZ")
    protected String lxrdz;
    @XmlElement(name = "LXRDH")
    protected String lxrdh;
    @XmlElement(name = "GZDWDHHM")
    protected String gzdwdhhm;
    @XmlElement(name = "JCJYJGDM")
    protected String jcjyjgdm;
    @XmlElement(name = "JCJYDLJG")
    protected String jcjydljg;
    @XmlElement(name = "ZYZJBZTDM")
    protected String zyzjbztdm;
    @XmlElement(name = "QTYXCZ")
    protected String qtyxcz;
    @XmlElement(name = "JCJYLB")
    protected String jcjylb;
    @XmlElement(name = "YWLX")
    protected String ywlx;
    @XmlElement(name = "ZZMC")
    protected String zzmc;
    @XmlElement(name = "YLJGMC", required = true)
    protected String yljgmc;
    @XmlElement(name = "ZZJGDM")
    protected String zzjgdm;
    @XmlElement(name = "BLFX")
    protected String blfx;
//    @XmlElement(name = "ZLFQLX")
//    protected String zlfqlx;
    @XmlElement(name = "ZLFQ")
    protected String zlfq;
    @XmlElement(name = "ZLTFQ")
    protected String zltfq;
    @XmlElement(name = "ZLNFQ")
    protected String zlnfq;
    @XmlElement(name = "ZLMFQ")
    protected String zlmfq;
    @XmlElement(name = "ZLFLFSBM")
    protected String zlflfsbm;
    @XmlElement(name = "ZLFLFSMC")
    protected String zlflfsmc;
    @XmlElement(name = "ZLFLCSBM")
    protected String zlflcsbm;
    @XmlElement(name = "ZLFLCSMC")
    protected String zlflcsmc;
    @XmlElement(name = "ZLFLZZBM")
    protected String zlflzzbm;
    @XmlElement(name = "ZLFLZZMC")
    protected String zlflzzmc;
    @XmlElement(name = "YFZJL")
    protected String yfzjl;
    @XmlElement(name = "YFZCS")
    protected String yfzcs;
    @XmlElement(name = "YFZTS")
    protected String yfzts;
    @XmlElement(name = "YFZKSSJ")
    protected String yfzkssj;
    @XmlElement(name = "YFZJSSJ")
    protected String yfzjssj;
    @XmlElement(name = "QYLBJJL")
    protected String qylbjjl;
    @XmlElement(name = "QYLBJCS")
    protected String qylbjcs;
    @XmlElement(name = "QYLBJTS")
    protected String qylbjts;
    @XmlElement(name = "QYLBJKSSJ")
    protected String qylbjkssj;
    @XmlElement(name = "QYLBJJSSJ")
    protected String qylbjjssj;
    @XmlElement(name = "ZYZMC")
    protected String zyzmc;
    @XmlElement(name = "ZYZJL")
    protected String zyzjl;
    @XmlElement(name = "ZYZCS")
    protected String zyzcs;
    @XmlElement(name = "ZYZTS")
    protected String zyzts;
    @XmlElement(name = "ZYZKSSJ")
    protected String zyzkssj;
    @XmlElement(name = "ZYZJSSJ")
    protected String zyzjssj;
    @XmlElement(name = "HLFSBM")
    protected String hlfsbm;
    @XmlElement(name = "HLFSMC")
    protected String hlfsmc;
    @XmlElement(name = "HLFFBM")
    protected String hlffbm;
    @XmlElement(name = "HLFFMC")
    String hlffmc;
    /*
     * 01	居民身份证02	居民户口簿03	护照04	军官证05	驾驶证06	港澳居民往来内地通行证07	台湾居民往来内地通行证99	其他法定有效证件
     */
    @XmlElement(name = "SFBS_DM")	//身份标识类别代码CV02_01_101
    protected String sfbsDm;
    @XmlElement(name = "SFBSHM")	//SFBSHM	身份标识号码
    protected String sfbshm;
    @XmlElement(name = "HBSAGJCJG_DM")	//HBsAg检查结果代码
    protected String hbsagjcjgDm;
    @XmlElement(name = "HCVABJCJG_DM")	//HCV_Ab检查结果代码
    protected String hcvabjcjgDm;
    @XmlElement(name = "HIVABJCJG_DM")	//HIV_Ab检查结果代码
    protected String hivabjcjgDm;
    @XmlElement(name = "BAZLPD_DM")	//病案质量评定代码1甲2乙3丙
    protected String bazlpdDm;
    @XmlElement(name = "ZZYSGH")	//转诊医生工号
    protected String zzysgh;
    @XmlElement(name = "ZYYSGH")	//住院医生工号
    protected String zyysgh;
    @XmlElement(name = "JXYSGH")	//进修医师工号
    protected String jxysgh;
    @XmlElement(name = "ZRHSGH")	//责任护士工号
    protected String zrhsgh;
    @XmlElement(name = "SXYSGH")	//实习医师工号
    protected String sxysgh;
    @XmlElement(name = "BMYGH")		//编码员工号
    protected String bmygh;
    @XmlElement(name = "ZKYSGH")	//质控医师工号
    protected String zkysgh;
    @XmlElement(name = "ZKHSGH")	//质控护士工号
    protected String zzkhsbhkysgh;
    @XmlElement(name = "CYKBBM")	//出院科别编码
    protected String cykbbm;
    @XmlElement(name = "RYKBBM")	//入院科别编码
    protected String rykbbm;
    @XmlElement(name = "RYSQK_DM")	//入院时情况代码HIS18_01_014/1危重2急诊3一般4不适用5其他
    protected String rysqkdm;
    @XmlElement(name = "ZYHZRYBQ")	//住院患者入院病情
    protected String zyhzrybq;
    @XmlElement(name = "KZRGH")	//科主任工号
    protected String kzrgh;
    @XmlElement(name = "XZZ_DM")	//现住址-代码
    protected String xzzDm;
    @XmlElement(name = "ZRYSGH")	//主任（副主任）医师签名
    protected String zrysgh;
    @XmlElement(name = "YLFYJSFS_DM")	//医疗费用结算方式代码/HIS18_01_002/
    protected String ylfyjsfsDm;
    /*
     * 01现金02支票03汇款存款04内部转账05单位记账06	账户金07	统筹金08	银行卡09	微信支付10	支付宝支付11	健康中山APP支付99其他
     */
    @XmlElement(name = "XXB")
    protected String xxb;
    @XmlElement(name = "XJ")
    protected String xj;
    @XmlElement(name = "QX")
    protected String qx;
    @XmlElement(name = "QT")
    protected String qt;
    
    @XmlElement(name = "HJDZ_DM")
    protected String hjdzDm;

    
    @XmlElement(name = "CYJY")
    protected String cyjy;
    @XmlElement(name = "YYGRJG_DM")
    protected String yygrjgDm;
    @XmlElement(name = "GMY_MC")
    protected String gmyMc;
    @XmlElement(name = "ZYHZGMZ")
    protected String zyhzgmz;
    
    @XmlElement(name = "SFSZ_DM")
    protected String sfszDm;
    @XmlElement(name = "HZJBZDDZ_DM")
    protected String hzjbzddzDm;
    @XmlElement(name = "ZYHZZDFHQKXXMS")
    protected String zyhzzdfhqkxxms;
    @XmlElement(name = "ZYHZJBZDLXXXMS")
    protected String zyhzjbzdlxxxms;
    @XmlElement(name = "SZQX")
    protected String szqx;
    @XmlElement(name = "SZQXSJ_DW")
    protected String szqxsjDw;
    @XmlElement(name = "SSZLJCZDWBYDYL_DM")
    protected String sszljczdwbydylDm;
    @XmlElement(name = "RYHQZRQ")
    protected String ryhqzrq;
    @XmlElement(name = "YJSSXYSGH")
    protected String yjssxysgh;
    @XmlElement(name = "YJSSXYSMC")
    protected String yjssxysmc;
    @XmlElement(name = "ZKKBDM")
    protected String zkkbdm;
    @XmlElement(name = "CYXYZD_DM")
    protected String cyxyzdDm;
    @XmlElement(name = "CYXYZD_MC")
    protected String cyxyzdMc;
    @XmlElement(name = "ZLJG_DM")
    protected String zljgDm;
    @XmlElement(name = "ZYFYFL_DM")
    protected String zyfyflDm;
    @XmlElement(name = "XGYSJSMC")
    protected String xgysjsmc;
    @XmlElement(name = "XGYSXM")
    protected String xgysxm;
    @XmlElement(name = "ZLFQLX")
    protected String zlfqlx;
    @XmlElement(name = "DJSJ")
    protected String djsj;
    @XmlElement(name = "DJRYGH")
    protected String djrygh;
    @XmlElement(name = "ZHXGSJ")
    protected String zhxgsj;
    @XmlElement(name = "ZHXGRYGH")
    protected String zhxgrygh;
    @XmlElement(name = "HXB")
    protected String hxb;
    @XmlElement(name = "ZHXGRYMC")
    protected String zhxgrymc;
    @XmlElement(name = "BLQPH")
    protected String blqph;
    @XmlElement(name = "XXPH")
    protected String xxph;
    @XmlElement(name = "CYYY")
    protected String cyyy;
    @XmlElement(name = "SJBL_DM")
    protected String sjblDm;
    
    
    @XmlElementWrapper(name="IPT_DrugRecords")
    @XmlElement(name = "IPT_DrugRecord")  
    protected List<IPTDrugRecord> IPT_DrugRecords;

    @XmlElementWrapper(name="IPT_BabyRecords")
    @XmlElement(name = "IPT_BabyRecord")  
    protected List<IPTBabyRecord> IPT_BabyRecords;
    
    @XmlElementWrapper(name="Ipt_LeaveDiagnosis_news")
    @XmlElement(name = "Ipt_LeaveDiagnosis_new")
    protected List<IptLeaveDiagnosisNew> iptLeaveDiagnosisNews;
    
    @XmlElementWrapper(name="Ipt_Operation_news")
    @XmlElement(name = "Ipt_Operation_new")
    protected List<IptOperationNew> iptOperationNews;
    @XmlAttribute(name = "Name")
    protected String name;
    
	public String getSourceId() {
		return jzlsh;
	}

	public void setJzlsh(String jzlsh) {
		this.jzlsh = jzlsh;
	}

	public void setHealthCardID(String healthCardID) {
		this.healthCardID = healthCardID;
	}

	public void setHospizationID(String hospizationID) {
		this.hospizationID = hospizationID;
	}

	public void setMedicalRecordID(String medicalRecordID) {
		this.medicalRecordID = medicalRecordID;
	}

	public void setPathologyID(String pathologyID) {
		this.pathologyID = pathologyID;
	}

	public void setKh(String kh) {
		this.kh = kh;
	}

	public void setKlx(String klx) {
		this.klx = klx;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	public void setHyzkdm(String hyzkdm) {
		this.hyzkdm = hyzkdm;
	}

	public void setZylbdm(String zylbdm) {
		this.zylbdm = zylbdm;
	}

//	public void setGj(String gj) {
//		this.gj = gj;
//	}

	public void setCsrqsj(String csrqsj) {
		this.csrqsj = csrqsj;
	}

	public void setAboxx(String aboxx) {
		this.aboxx = aboxx;
	}

	public void setRhxx(String rhxx) {
		this.rhxx = rhxx;
	}

	public void setOriginProvince(String originProvince) {
		this.originProvince = originProvince;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public void setPresentAddrProvince(String presentAddrProvince) {
		this.presentAddrProvince = presentAddrProvince;
	}

	public void setPresentAddrCity(String presentAddrCity) {
		this.presentAddrCity = presentAddrCity;
	}

	public void setPresentAddrCounty(String presentAddrCounty) {
		this.presentAddrCounty = presentAddrCounty;
	}

	public void setPresentAddrOther(String presentAddrOther) {
		this.presentAddrOther = presentAddrOther;
	}

	public void setPresentAddrPostalcode(String presentAddrPostalcode) {
		this.presentAddrPostalcode = presentAddrPostalcode;
	}

	public void setMedicalPayment(String medicalPayment) {
		this.medicalPayment = medicalPayment;
	}

	public void setOutsideReasonOfInjuryAndPoCode(
			String outsideReasonOfInjuryAndPoCode) {
		this.outsideReasonOfInjuryAndPoCode = outsideReasonOfInjuryAndPoCode;
	}

	public void setMasterDiseaseCode(String masterDiseaseCode) {
		this.masterDiseaseCode = masterDiseaseCode;
	}

	public void setMasterDiseaseName(String masterDiseaseName) {
		this.masterDiseaseName = masterDiseaseName;
	}

	public void setMasterAdmissionCondition(String masterAdmissionCondition) {
		this.masterAdmissionCondition = masterAdmissionCondition;
	}

	public void setCyzyzdzbmc(String cyzyzdzbmc) {
		this.cyzyzdzbmc = cyzyzdzbmc;
	}

	public void setCyzyzdzbbm(String cyzyzdzbbm) {
		this.cyzyzdzbbm = cyzyzdzbbm;
	}

	public void setCyzyzdrybq(String cyzyzdrybq) {
		this.cyzyzdrybq = cyzyzdrybq;
	}

	public void setCyzyzdzzmc(String cyzyzdzzmc) {
		this.cyzyzdzzmc = cyzyzdzzmc;
	}

	public void setCyzyzdzzbm(String cyzyzdzzbm) {
		this.cyzyzdzzbm = cyzyzdzzbm;
	}

	public void setCyzyzdzzrybq(String cyzyzdzzrybq) {
		this.cyzyzdzzrybq = cyzyzdzzrybq;
	}

	public void setPathologyDiseaseCode(String pathologyDiseaseCode) {
		this.pathologyDiseaseCode = pathologyDiseaseCode;
	}

	public void setPathologyDiseaseName(String pathologyDiseaseName) {
		this.pathologyDiseaseName = pathologyDiseaseName;
	}

	public void setClinicWesternDiagnosisCode(String clinicWesternDiagnosisCode) {
		this.clinicWesternDiagnosisCode = clinicWesternDiagnosisCode;
	}

	public void setClinicDiagnosis(String clinicDiagnosis) {
		this.clinicDiagnosis = clinicDiagnosis;
	}

	public void setMjzzdzyzdmc(String mjzzdzyzdmc) {
		this.mjzzdzyzdmc = mjzzdzyzdmc;
	}

	public void setMjzzdzyzdbm(String mjzzdzyzdbm) {
		this.mjzzdzyzdbm = mjzzdzyzdbm;
	}

	public void setMjzzdzyzhmc(String mjzzdzyzhmc) {
		this.mjzzdzyzhmc = mjzzdzyzhmc;
	}

	public void setMjzzdzyzhbm(String mjzzdzyzhbm) {
		this.mjzzdzyzhbm = mjzzdzyzhbm;
	}

	public void setSyzyzjbz(String syzyzjbz) {
		this.syzyzjbz = syzyzjbz;
	}

	public void setSyzysbbz(String syzysbbz) {
		this.syzysbbz = syzysbbz;
	}

	public void setSyzyjsbz(String syzyjsbz) {
		this.syzyjsbz = syzyjsbz;
	}

	public void setBzshbz(String bzshbz) {
		this.bzshbz = bzshbz;
	}

	public void setMonthsAge(String monthsAge) {
		this.monthsAge = monthsAge;
	}

	public void setNeonatalBirthWeight(String neonatalBirthWeight) {
		this.neonatalBirthWeight = neonatalBirthWeight;
	}

	public void setNeonatalAdmissionWeight(String neonatalAdmissionWeight) {
		this.neonatalAdmissionWeight = neonatalAdmissionWeight;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public void setRegisteredPermanentResidencePr(
			String registeredPermanentResidencePr) {
		this.registeredPermanentResidencePr = registeredPermanentResidencePr;
	}

	public void setRegisteredPermanentResidenceCi(
			String registeredPermanentResidenceCi) {
		this.registeredPermanentResidenceCi = registeredPermanentResidenceCi;
	}

	public void setRegisteredPermanentResidenceTo(
			String registeredPermanentResidenceTo) {
		this.registeredPermanentResidenceTo = registeredPermanentResidenceTo;
	}

	public void setRegisteredPermanentResidenceOt(
			String registeredPermanentResidenceOt) {
		this.registeredPermanentResidenceOt = registeredPermanentResidenceOt;
	}

	public void setRegisteredPermanentResidencePo(
			String registeredPermanentResidencePo) {
		this.registeredPermanentResidencePo = registeredPermanentResidencePo;
	}

	public void setWorkAddrPhone(String workAddrPhone) {
		this.workAddrPhone = workAddrPhone;
	}

	public void setAdmissionWay(String admissionWay) {
		this.admissionWay = admissionWay;
	}

	public void setDischargeDateTime(String dischargeDateTime) {
		this.dischargeDateTime = dischargeDateTime;
	}

	public void setDischargeSickRoom(String dischargeSickRoom) {
		this.dischargeSickRoom = dischargeSickRoom;
	}

	public void setActualHospitalizationDays(String actualHospitalizationDays) {
		this.actualHospitalizationDays = actualHospitalizationDays;
	}

	public void setMasterDischargePrognosisCondit(
			String masterDischargePrognosisCondit) {
		this.masterDischargePrognosisCondit = masterDischargePrognosisCondit;
	}

	public void setOutsideReasonOfInjuryAndPoison(
			String outsideReasonOfInjuryAndPoison) {
		this.outsideReasonOfInjuryAndPoison = outsideReasonOfInjuryAndPoison;
	}

	public void setDrugAllergy(String drugAllergy) {
		this.drugAllergy = drugAllergy;
	}

	public void setAllergicDrug(String allergicDrug) {
		this.allergicDrug = allergicDrug;
	}

	public void setAdmissionDateTime(String admissionDateTime) {
		this.admissionDateTime = admissionDateTime;
	}

	public void setAdmissionSickroom(String admissionSickroom) {
		this.admissionSickroom = admissionSickroom;
	}

	public void setAutopsySign(String autopsySign) {
		this.autopsySign = autopsySign;
	}

	public void setMedicalRecordQuality(String medicalRecordQuality) {
		this.medicalRecordQuality = medicalRecordQuality;
	}

	public void setQualityControlDate(String qualityControlDate) {
		this.qualityControlDate = qualityControlDate;
	}

	public void setDischargeMethods(String dischargeMethods) {
		this.dischargeMethods = dischargeMethods;
	}

	public void setPrepareAcceptHospitalName(String prepareAcceptHospitalName) {
		this.prepareAcceptHospitalName = prepareAcceptHospitalName;
	}

	public void setDay31InpatientMk(String day31InpatientMk) {
		this.day31InpatientMk = day31InpatientMk;
	}

	public void setDay31InpatientAim(String day31InpatientAim) {
		this.day31InpatientAim = day31InpatientAim;
	}

	public void setStuporTimeBeforeAdmission(String stuporTimeBeforeAdmission) {
		this.stuporTimeBeforeAdmission = stuporTimeBeforeAdmission;
	}

	public void setStuporTimeAfterAdmission(String stuporTimeAfterAdmission) {
		this.stuporTimeAfterAdmission = stuporTimeAfterAdmission;
	}

	public void setDiagCoinOutpatientVsDischarge(
			String diagCoinOutpatientVsDischarge) {
		this.diagCoinOutpatientVsDischarge = diagCoinOutpatientVsDischarge;
	}

	public void setDiagCoinAdmssionVsDischarge(String diagCoinAdmssionVsDischarge) {
		this.diagCoinAdmssionVsDischarge = diagCoinAdmssionVsDischarge;
	}

	public void setDiagCoinPreoperativeVsPost(String diagCoinPreoperativeVsPost) {
		this.diagCoinPreoperativeVsPost = diagCoinPreoperativeVsPost;
	}

	public void setDiagCoinClinicalVsPathologica(
			String diagCoinClinicalVsPathologica) {
		this.diagCoinClinicalVsPathologica = diagCoinClinicalVsPathologica;
	}

	public void setDiagCoinRadiologyVspathology(String diagCoinRadiologyVspathology) {
		this.diagCoinRadiologyVspathology = diagCoinRadiologyVspathology;
	}

	public void setSalvageConditionSalvageTimes(String salvageConditionSalvageTimes) {
		this.salvageConditionSalvageTimes = salvageConditionSalvageTimes;
	}

	public void setSalvageConditionSuccessTimes(String salvageConditionSuccessTimes) {
		this.salvageConditionSuccessTimes = salvageConditionSuccessTimes;
	}

	public void setClinicalPathManagement(String clinicalPathManagement) {
		this.clinicalPathManagement = clinicalPathManagement;
	}

	public void setAttendingDoctor(String attendingDoctor) {
		this.attendingDoctor = attendingDoctor;
	}

	public void setHospizationDoctor(String hospizationDoctor) {
		this.hospizationDoctor = hospizationDoctor;
	}

	public void setResponsibilityNurse(String responsibilityNurse) {
		this.responsibilityNurse = responsibilityNurse;
	}

	public void setRefresherDoctors(String refresherDoctors) {
		this.refresherDoctors = refresherDoctors;
	}

	public void setInternDoctor(String internDoctor) {
		this.internDoctor = internDoctor;
	}

	public void setMedicalRecordCoderSign(String medicalRecordCoderSign) {
		this.medicalRecordCoderSign = medicalRecordCoderSign;
	}

	public void setQualityControlDoctor(String qualityControlDoctor) {
		this.qualityControlDoctor = qualityControlDoctor;
	}

	public void setQualityControlSign(String qualityControlSign) {
		this.qualityControlSign = qualityControlSign;
	}

	public void setTransferredDeptName(String transferredDeptName) {
		this.transferredDeptName = transferredDeptName;
	}

	public void setDischargeDepartment(String dischargeDepartment) {
		this.dischargeDepartment = dischargeDepartment;
	}

	public void setAdmissionDeptName(String admissionDeptName) {
		this.admissionDeptName = admissionDeptName;
	}

	public void setGenerMediServCharge(String generMediServCharge) {
		this.generMediServCharge = generMediServCharge;
	}

	public void setGenerMediServChargeZYBZLZF(String generMediServChargeZYBZLZF) {
		this.generMediServChargeZYBZLZF = generMediServChargeZYBZLZF;
	}

	public void setGenerMediServChargeZYBZLZHZF(String generMediServChargeZYBZLZHZF) {
		this.generMediServChargeZYBZLZHZF = generMediServChargeZYBZLZHZF;
	}

	public void setGenerTreatHandlingFee(String generTreatHandlingFee) {
		this.generTreatHandlingFee = generTreatHandlingFee;
	}

	public void setNurse(String nurse) {
		this.nurse = nurse;
	}

	public void setGenerMediServChargeOther(String generMediServChargeOther) {
		this.generMediServChargeOther = generMediServChargeOther;
	}

	public void setPathologicalFee(String pathologicalFee) {
		this.pathologicalFee = pathologicalFee;
	}

	public void setLaboratoryFee(String laboratoryFee) {
		this.laboratoryFee = laboratoryFee;
	}

	public void setImagingFee(String imagingFee) {
		this.imagingFee = imagingFee;
	}

	public void setClinicalDiagnosisFee(String clinicalDiagnosisFee) {
		this.clinicalDiagnosisFee = clinicalDiagnosisFee;
	}

	public void setNonoperativeTreatFee(String nonoperativeTreatFee) {
		this.nonoperativeTreatFee = nonoperativeTreatFee;
	}

	public void setClinicalPhysicalTreatment(String clinicalPhysicalTreatment) {
		this.clinicalPhysicalTreatment = clinicalPhysicalTreatment;
	}

	public void setSurgicalTreatment(String surgicalTreatment) {
		this.surgicalTreatment = surgicalTreatment;
	}

	public void setEstheticFee(String estheticFee) {
		this.estheticFee = estheticFee;
	}

	public void setOperationFee(String operationFee) {
		this.operationFee = operationFee;
	}

	public void setRehabilitationFee(String rehabilitationFee) {
		this.rehabilitationFee = rehabilitationFee;
	}

	public void setZylzyzdf(String zylzyzdf) {
		this.zylzyzdf = zylzyzdf;
	}

	public void setZyzlFee(String zyzlFee) {
		this.zyzlFee = zyzlFee;
	}

	public void setZyzlfzywzf(String zyzlfzywzf) {
		this.zyzlfzywzf = zyzlfzywzf;
	}

	public void setZyzlfzygsf(String zyzlfzygsf) {
		this.zyzlfzygsf = zyzlfzygsf;
	}

	public void setZyzlfzyzjf(String zyzlfzyzjf) {
		this.zyzlfzyzjf = zyzlfzyzjf;
	}

	public void setZyzlfzytnf(String zyzlfzytnf) {
		this.zyzlfzytnf = zyzlfzytnf;
	}

	public void setZyzlfzygczlf(String zyzlfzygczlf) {
		this.zyzlfzygczlf = zyzlfzygczlf;
	}

	public void setZyzlfzytszlf(String zyzlfzytszlf) {
		this.zyzlfzytszlf = zyzlfzytszlf;
	}

	public void setZyqtf(String zyqtf) {
		this.zyqtf = zyqtf;
	}

	public void setZyqtftstpjgf(String zyqtftstpjgf) {
		this.zyqtftstpjgf = zyqtftstpjgf;
	}

	public void setZyqtfbzssf(String zyqtfbzssf) {
		this.zyqtfbzssf = zyqtfbzssf;
	}

	public void setMedicineChina(String medicineChina) {
		this.medicineChina = medicineChina;
	}

	public void setMedicineChinaZYZJF(String medicineChinaZYZJF) {
		this.medicineChinaZYZJF = medicineChinaZYZJF;
	}

	public void setXyFee(String xyFee) {
		this.xyFee = xyFee;
	}

	public void setHerbalMedicineFee(String herbalMedicineFee) {
		this.herbalMedicineFee = herbalMedicineFee;
	}

	public void setAntibacterialDrugExp(String antibacterialDrugExp) {
		this.antibacterialDrugExp = antibacterialDrugExp;
	}

	public void setBloodFee(String bloodFee) {
		this.bloodFee = bloodFee;
	}

	public void setAcpFee(String acpFee) {
		this.acpFee = acpFee;
	}

	public void setGcpFee(String gcpFee) {
		this.gcpFee = gcpFee;
	}

	public void setNxyzFee(String nxyzFee) {
		this.nxyzFee = nxyzFee;
	}

	public void setXbyzFee(String xbyzFee) {
		this.xbyzFee = xbyzFee;
	}

	public void setDeptManager(String deptManager) {
		this.deptManager = deptManager;
	}

	public void setYcyycxFee(String ycyycxFee) {
		this.ycyycxFee = ycyycxFee;
	}

	public void setChiefDoctorSign(String chiefDoctorSign) {
		this.chiefDoctorSign = chiefDoctorSign;
	}

	public void setZlyycxFee(String zlyycxFee) {
		this.zlyycxFee = zlyycxFee;
	}

	public void setSsyycxzlFee(String ssyycxzlFee) {
		this.ssyycxzlFee = ssyycxzlFee;
	}

	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}

	public void setHospizationTotalPersonalCost(String hospizationTotalPersonalCost) {
		this.hospizationTotalPersonalCost = hospizationTotalPersonalCost;
	}

	public void setHospizationTotalCost(String hospizationTotalCost) {
		this.hospizationTotalCost = hospizationTotalCost;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public void setZycs(String zycs) {
		this.zycs = zycs;
	}

	public void setGzdwmc(String gzdwmc) {
		this.gzdwmc = gzdwmc;
	}

	public void setGzdwdz(String gzdwdz) {
		this.gzdwdz = gzdwdz;
	}

	public void setGzddyb(String gzddyb) {
		this.gzddyb = gzddyb;
	}

	public void setLxrxm(String lxrxm) {
		this.lxrxm = lxrxm;
	}

	public void setLxryhzgx(String lxryhzgx) {
		this.lxryhzgx = lxryhzgx;
	}

	public void setLxrdz(String lxrdz) {
		this.lxrdz = lxrdz;
	}

	public void setLxrdh(String lxrdh) {
		this.lxrdh = lxrdh;
	}

	public void setGzdwdhhm(String gzdwdhhm) {
		this.gzdwdhhm = gzdwdhhm;
	}

	public void setJcjyjgdm(String jcjyjgdm) {
		this.jcjyjgdm = jcjyjgdm;
	}

	public void setJcjydljg(String jcjydljg) {
		this.jcjydljg = jcjydljg;
	}

	public void setZyzjbztdm(String zyzjbztdm) {
		this.zyzjbztdm = zyzjbztdm;
	}

	public void setQtyxcz(String qtyxcz) {
		this.qtyxcz = qtyxcz;
	}

	public void setJcjylb(String jcjylb) {
		this.jcjylb = jcjylb;
	}

	public void setYwlx(String ywlx) {
		this.ywlx = ywlx;
	}

	public void setZzmc(String zzmc) {
		this.zzmc = zzmc;
	}

	public void setYljgmc(String yljgmc) {
		this.yljgmc = yljgmc;
	}

	public void setZzjgdm(String zzjgdm) {
		this.zzjgdm = zzjgdm;
	}

	public void setBlfx(String blfx) {
		this.blfx = blfx;
	}

	
	public void setZlfq(String zlfq) {
		this.zlfq = zlfq;
	}

	public void setZltfq(String zltfq) {
		this.zltfq = zltfq;
	}

	public void setZlnfq(String zlnfq) {
		this.zlnfq = zlnfq;
	}

	public void setZlmfq(String zlmfq) {
		this.zlmfq = zlmfq;
	}

	public void setZlflfsbm(String zlflfsbm) {
		this.zlflfsbm = zlflfsbm;
	}

	public void setZlflfsmc(String zlflfsmc) {
		this.zlflfsmc = zlflfsmc;
	}

	public void setZlflcsbm(String zlflcsbm) {
		this.zlflcsbm = zlflcsbm;
	}

	public void setZlflcsmc(String zlflcsmc) {
		this.zlflcsmc = zlflcsmc;
	}

	public void setZlflzzbm(String zlflzzbm) {
		this.zlflzzbm = zlflzzbm;
	}

	public void setZlflzzmc(String zlflzzmc) {
		this.zlflzzmc = zlflzzmc;
	}

	public void setYfzjl(String yfzjl) {
		this.yfzjl = yfzjl;
	}

	public void setYfzcs(String yfzcs) {
		this.yfzcs = yfzcs;
	}

	public void setYfzts(String yfzts) {
		this.yfzts = yfzts;
	}

	public void setYfzkssj(String yfzkssj) {
		this.yfzkssj = yfzkssj;
	}

	public void setYfzjssj(String yfzjssj) {
		this.yfzjssj = yfzjssj;
	}

	public void setQylbjjl(String qylbjjl) {
		this.qylbjjl = qylbjjl;
	}

	public void setQylbjcs(String qylbjcs) {
		this.qylbjcs = qylbjcs;
	}

	public void setQylbjts(String qylbjts) {
		this.qylbjts = qylbjts;
	}

	public void setQylbjkssj(String qylbjkssj) {
		this.qylbjkssj = qylbjkssj;
	}

	public void setQylbjjssj(String qylbjjssj) {
		this.qylbjjssj = qylbjjssj;
	}

	public void setZyzmc(String zyzmc) {
		this.zyzmc = zyzmc;
	}

	public void setZyzjl(String zyzjl) {
		this.zyzjl = zyzjl;
	}

	public void setZyzcs(String zyzcs) {
		this.zyzcs = zyzcs;
	}

	public void setZyzts(String zyzts) {
		this.zyzts = zyzts;
	}

	public void setZyzkssj(String zyzkssj) {
		this.zyzkssj = zyzkssj;
	}

	public void setZyzjssj(String zyzjssj) {
		this.zyzjssj = zyzjssj;
	}

	public void setHlfsbm(String hlfsbm) {
		this.hlfsbm = hlfsbm;
	}

	public void setHlfsmc(String hlfsmc) {
		this.hlfsmc = hlfsmc;
	}

	public void setHlffbm(String hlffbm) {
		this.hlffbm = hlffbm;
	}

	public void setHlffmc(String hlffmc) {
		this.hlffmc = hlffmc;
	}



	public List<IPTBabyRecord> getIPT_BabyRecords() {
		return IPT_BabyRecords;
	}

	public void setIPT_BabyRecords(List<IPTBabyRecord> iPT_BabyRecords) {
		IPT_BabyRecords = iPT_BabyRecords;
	}

	public void setIptLeaveDiagnosisNews(
			List<IptLeaveDiagnosisNew> iptLeaveDiagnosisNews) {
		this.iptLeaveDiagnosisNews = iptLeaveDiagnosisNews;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJzlsh() {
		return jzlsh;
	}

	public String getHealthCardID() {
		return healthCardID;
	}

	public String getHospizationID() {
		return hospizationID;
	}

	public String getMedicalRecordID() {
		return medicalRecordID;
	}

	public String getPathologyID() {
		return pathologyID;
	}

	public String getKh() {
		return kh;
	}

	public String getKlx() {
		return klx;
	}

	public String getXm() {
		return xm;
	}

	public String getSex() {
		return sex;
	}

	public String getHyzkdm() {
		return hyzkdm;
	}

	public String getZylbdm() {
		return zylbdm;
	}

	public String getCsrqsj() {
		return csrqsj;
	}

	public String getAboxx() {
		return aboxx;
	}

	public String getRhxx() {
		return rhxx;
	}

	public String getOriginProvince() {
		return originProvince;
	}

	public String getOriginCity() {
		return originCity;
	}

	public String getPresentAddrProvince() {
		return presentAddrProvince;
	}

	public String getPresentAddrCity() {
		return presentAddrCity;
	}

	public String getPresentAddrCounty() {
		return presentAddrCounty;
	}

	public String getPresentAddrOther() {
		return presentAddrOther;
	}

	public String getPresentAddrPostalcode() {
		return presentAddrPostalcode;
	}

	public String getMedicalPayment() {
		return medicalPayment;
	}

	public String getOutsideReasonOfInjuryAndPoCode() {
		return outsideReasonOfInjuryAndPoCode;
	}

	public String getMasterDiseaseCode() {
		return masterDiseaseCode;
	}

	public String getMasterDiseaseName() {
		return masterDiseaseName;
	}

	public String getMasterAdmissionCondition() {
		return masterAdmissionCondition;
	}

	public String getCyzyzdzbmc() {
		return cyzyzdzbmc;
	}

	public String getCyzyzdzbbm() {
		return cyzyzdzbbm;
	}

	public String getCyzyzdrybq() {
		return cyzyzdrybq;
	}

	public String getCyzyzdzzmc() {
		return cyzyzdzzmc;
	}

	public String getCyzyzdzzbm() {
		return cyzyzdzzbm;
	}

	public String getCyzyzdzzrybq() {
		return cyzyzdzzrybq;
	}

	public String getPathologyDiseaseCode() {
		return pathologyDiseaseCode;
	}

	public String getPathologyDiseaseName() {
		return pathologyDiseaseName;
	}

	public String getClinicWesternDiagnosisCode() {
		return clinicWesternDiagnosisCode;
	}

	public String getClinicDiagnosis() {
		return clinicDiagnosis;
	}

	public String getMjzzdzyzdmc() {
		return mjzzdzyzdmc;
	}

	public String getMjzzdzyzdbm() {
		return mjzzdzyzdbm;
	}

	public String getMjzzdzyzhmc() {
		return mjzzdzyzhmc;
	}

	public String getMjzzdzyzhbm() {
		return mjzzdzyzhbm;
	}

	public String getSyzyzjbz() {
		return syzyzjbz;
	}

	public String getSyzysbbz() {
		return syzysbbz;
	}

	public String getSyzyjsbz() {
		return syzyjsbz;
	}

	public String getBzshbz() {
		return bzshbz;
	}

	public String getMonthsAge() {
		return monthsAge;
	}

	public String getNeonatalBirthWeight() {
		return neonatalBirthWeight;
	}

	public String getNeonatalAdmissionWeight() {
		return neonatalAdmissionWeight;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getCounty() {
		return county;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public String getRegisteredPermanentResidencePr() {
		return registeredPermanentResidencePr;
	}

	public String getRegisteredPermanentResidenceCi() {
		return registeredPermanentResidenceCi;
	}

	public String getRegisteredPermanentResidenceTo() {
		return registeredPermanentResidenceTo;
	}

	public String getRegisteredPermanentResidenceOt() {
		return registeredPermanentResidenceOt;
	}

	public String getRegisteredPermanentResidencePo() {
		return registeredPermanentResidencePo;
	}

	public String getWorkAddrPhone() {
		return workAddrPhone;
	}

	public String getAdmissionWay() {
		return admissionWay;
	}

	public String getDischargeDateTime() {
		return dischargeDateTime;
	}

	public String getDischargeSickRoom() {
		return dischargeSickRoom;
	}

	public String getActualHospitalizationDays() {
		return actualHospitalizationDays;
	}

	public String getMasterDischargePrognosisCondit() {
		return masterDischargePrognosisCondit;
	}

	public String getOutsideReasonOfInjuryAndPoison() {
		return outsideReasonOfInjuryAndPoison;
	}

	public String getDrugAllergy() {
		return drugAllergy;
	}

	public String getAllergicDrug() {
		return allergicDrug;
	}

	public String getAdmissionDateTime() {
		return admissionDateTime;
	}

	public String getAdmissionSickroom() {
		return admissionSickroom;
	}

	public String getAutopsySign() {
		return autopsySign;
	}

	public String getMedicalRecordQuality() {
		return medicalRecordQuality;
	}

	public String getQualityControlDate() {
		return qualityControlDate;
	}

	public String getDischargeMethods() {
		return dischargeMethods;
	}

	public String getPrepareAcceptHospitalName() {
		return prepareAcceptHospitalName;
	}

	public String getDay31InpatientMk() {
		return day31InpatientMk;
	}

	public String getDay31InpatientAim() {
		return day31InpatientAim;
	}

	public String getStuporTimeBeforeAdmission() {
		return stuporTimeBeforeAdmission;
	}

	public String getStuporTimeAfterAdmission() {
		return stuporTimeAfterAdmission;
	}

	public String getDiagCoinOutpatientVsDischarge() {
		return diagCoinOutpatientVsDischarge;
	}

	public String getDiagCoinAdmssionVsDischarge() {
		return diagCoinAdmssionVsDischarge;
	}

	public String getDiagCoinPreoperativeVsPost() {
		return diagCoinPreoperativeVsPost;
	}

	public String getDiagCoinClinicalVsPathologica() {
		return diagCoinClinicalVsPathologica;
	}

	public String getDiagCoinRadiologyVspathology() {
		return diagCoinRadiologyVspathology;
	}

	public String getSalvageConditionSalvageTimes() {
		return salvageConditionSalvageTimes;
	}

	public String getSalvageConditionSuccessTimes() {
		return salvageConditionSuccessTimes;
	}

	public String getClinicalPathManagement() {
		return clinicalPathManagement;
	}

	public String getAttendingDoctor() {
		return attendingDoctor;
	}

	public String getHospizationDoctor() {
		return hospizationDoctor;
	}

	public String getResponsibilityNurse() {
		return responsibilityNurse;
	}

	public String getRefresherDoctors() {
		return refresherDoctors;
	}

	public String getInternDoctor() {
		return internDoctor;
	}

	public String getMedicalRecordCoderSign() {
		return medicalRecordCoderSign;
	}

	public String getQualityControlDoctor() {
		return qualityControlDoctor;
	}

	public String getQualityControlSign() {
		return qualityControlSign;
	}

	public String getTransferredDeptName() {
		return transferredDeptName;
	}

	public String getDischargeDepartment() {
		return dischargeDepartment;
	}

	public String getAdmissionDeptName() {
		return admissionDeptName;
	}

	public String getGenerMediServCharge() {
		return generMediServCharge;
	}

	public String getGenerMediServChargeZYBZLZF() {
		return generMediServChargeZYBZLZF;
	}

	public String getGenerMediServChargeZYBZLZHZF() {
		return generMediServChargeZYBZLZHZF;
	}

	public String getGenerTreatHandlingFee() {
		return generTreatHandlingFee;
	}

	public String getNurse() {
		return nurse;
	}

	public String getGenerMediServChargeOther() {
		return generMediServChargeOther;
	}

	public String getPathologicalFee() {
		return pathologicalFee;
	}

	public String getLaboratoryFee() {
		return laboratoryFee;
	}

	public String getImagingFee() {
		return imagingFee;
	}

	public String getClinicalDiagnosisFee() {
		return clinicalDiagnosisFee;
	}

	public String getNonoperativeTreatFee() {
		return nonoperativeTreatFee;
	}

	public String getClinicalPhysicalTreatment() {
		return clinicalPhysicalTreatment;
	}

	public String getSurgicalTreatment() {
		return surgicalTreatment;
	}

	public String getEstheticFee() {
		return estheticFee;
	}

	public String getOperationFee() {
		return operationFee;
	}

	public String getRehabilitationFee() {
		return rehabilitationFee;
	}

	public String getZylzyzdf() {
		return zylzyzdf;
	}

	public String getZyzlFee() {
		return zyzlFee;
	}

	public String getZyzlfzywzf() {
		return zyzlfzywzf;
	}

	public String getZyzlfzygsf() {
		return zyzlfzygsf;
	}

	public String getZyzlfzyzjf() {
		return zyzlfzyzjf;
	}

	public String getZyzlfzytnf() {
		return zyzlfzytnf;
	}

	public String getZyzlfzygczlf() {
		return zyzlfzygczlf;
	}

	public String getZyzlfzytszlf() {
		return zyzlfzytszlf;
	}

	public String getZyqtf() {
		return zyqtf;
	}

	public String getZyqtftstpjgf() {
		return zyqtftstpjgf;
	}

	public String getZyqtfbzssf() {
		return zyqtfbzssf;
	}

	public String getMedicineChina() {
		return medicineChina;
	}

	public String getMedicineChinaZYZJF() {
		return medicineChinaZYZJF;
	}

	public String getXyFee() {
		return xyFee;
	}

	public String getHerbalMedicineFee() {
		return herbalMedicineFee;
	}

	public String getAntibacterialDrugExp() {
		return antibacterialDrugExp;
	}

	public String getBloodFee() {
		return bloodFee;
	}

	public String getAcpFee() {
		return acpFee;
	}

	public String getGcpFee() {
		return gcpFee;
	}

	public String getNxyzFee() {
		return nxyzFee;
	}

	public String getXbyzFee() {
		return xbyzFee;
	}

	public String getDeptManager() {
		return deptManager;
	}

	public String getYcyycxFee() {
		return ycyycxFee;
	}

	public String getChiefDoctorSign() {
		return chiefDoctorSign;
	}

	public String getZlyycxFee() {
		return zlyycxFee;
	}

	public String getSsyycxzlFee() {
		return ssyycxzlFee;
	}

	public String getOtherFee() {
		return otherFee;
	}

	public String getHospizationTotalPersonalCost() {
		return hospizationTotalPersonalCost;
	}

	public String getHospizationTotalCost() {
		return hospizationTotalCost;
	}

	public String getMz() {
		return mz;
	}

	public String getZycs() {
		return zycs;
	}

	public String getGzdwmc() {
		return gzdwmc;
	}

	public String getGzdwdz() {
		return gzdwdz;
	}

	public String getGzddyb() {
		return gzddyb;
	}

	public String getLxrxm() {
		return lxrxm;
	}

	public String getLxryhzgx() {
		return lxryhzgx;
	}

	public String getLxrdz() {
		return lxrdz;
	}

	public String getLxrdh() {
		return lxrdh;
	}

	public String getGzdwdhhm() {
		return gzdwdhhm;
	}

	public String getJcjyjgdm() {
		return jcjyjgdm;
	}

	public String getJcjydljg() {
		return jcjydljg;
	}

	public String getZyzjbztdm() {
		return zyzjbztdm;
	}

	public String getQtyxcz() {
		return qtyxcz;
	}

	public String getJcjylb() {
		return jcjylb;
	}

	public String getYwlx() {
		return ywlx;
	}

	public String getZzmc() {
		return zzmc;
	}

	public String getYljgmc() {
		return yljgmc;
	}

	public String getZzjgdm() {
		return zzjgdm;
	}

	public String getBlfx() {
		return blfx;
	}

	public String getZlfq() {
		return zlfq;
	}

	public String getZltfq() {
		return zltfq;
	}

	public String getZlnfq() {
		return zlnfq;
	}

	public String getZlmfq() {
		return zlmfq;
	}

	public String getZlflfsbm() {
		return zlflfsbm;
	}

	public String getZlflfsmc() {
		return zlflfsmc;
	}

	public String getZlflcsbm() {
		return zlflcsbm;
	}

	public String getZlflcsmc() {
		return zlflcsmc;
	}

	public String getZlflzzbm() {
		return zlflzzbm;
	}

	public String getZlflzzmc() {
		return zlflzzmc;
	}

	public String getYfzjl() {
		return yfzjl;
	}

	public String getYfzcs() {
		return yfzcs;
	}

	public String getYfzts() {
		return yfzts;
	}

	public String getYfzkssj() {
		return yfzkssj;
	}

	public String getYfzjssj() {
		return yfzjssj;
	}

	public String getQylbjjl() {
		return qylbjjl;
	}

	public String getQylbjcs() {
		return qylbjcs;
	}

	public String getQylbjts() {
		return qylbjts;
	}

	public String getQylbjkssj() {
		return qylbjkssj;
	}

	public String getQylbjjssj() {
		return qylbjjssj;
	}

	public String getZyzmc() {
		return zyzmc;
	}

	public String getZyzjl() {
		return zyzjl;
	}

	public String getZyzcs() {
		return zyzcs;
	}

	public String getZyzts() {
		return zyzts;
	}

	public String getZyzkssj() {
		return zyzkssj;
	}

	public String getZyzjssj() {
		return zyzjssj;
	}

	public String getHlfsbm() {
		return hlfsbm;
	}

	public String getHlfsmc() {
		return hlfsmc;
	}

	public String getHlffbm() {
		return hlffbm;
	}

	public String getHlffmc() {
		return hlffmc;
	}


	public String getName() {
		return name;
	}

	public List<IPTDrugRecord> getIPT_DrugRecords() {
		return IPT_DrugRecords;
	}

	public void setIPT_DrugRecords(List<IPTDrugRecord> iPT_DrugRecords) {
		IPT_DrugRecords = iPT_DrugRecords;
	}

	public List<IptOperationNew> getIptOperationNews() {
		return iptOperationNews;
	}

	public void setIptOperationNews(List<IptOperationNew> iptOperationNews) {
		this.iptOperationNews = iptOperationNews;
	}

	public List<IptLeaveDiagnosisNew> getIptLeaveDiagnosisNews() {
		return iptLeaveDiagnosisNews;
	}

	public String getSfbsDm() {
		return sfbsDm;
	}

	public void setSfbsDm(String sfbsDm) {
		this.sfbsDm = sfbsDm;
	}

	public String getSfbshm() {
		return sfbshm;
	}

	public void setSfbshm(String sfbshm) {
		this.sfbshm = sfbshm;
	}

	public String getHbsagjcjgDm() {
		return hbsagjcjgDm;
	}

	public void setHbsagjcjgDm(String hbsagjcjgDm) {
		this.hbsagjcjgDm = hbsagjcjgDm;
	}

	public String getHcvabjcjgDm() {
		return hcvabjcjgDm;
	}

	public void setHcvabjcjgDm(String hcvabjcjgDm) {
		this.hcvabjcjgDm = hcvabjcjgDm;
	}

	public String getHivabjcjgDm() {
		return hivabjcjgDm;
	}

	public void setHivabjcjgDm(String hivabjcjgDm) {
		this.hivabjcjgDm = hivabjcjgDm;
	}

	public String getBazlpdDm() {
		return bazlpdDm;
	}

	public void setBazlpdDm(String bazlpdDm) {
		this.bazlpdDm = bazlpdDm;
	}

	public String getZzysgh() {
		return zzysgh;
	}

	public void setZzysgh(String zzysgh) {
		this.zzysgh = zzysgh;
	}

	public String getZyysgh() {
		return zyysgh;
	}

	public void setZyysgh(String zyysgh) {
		this.zyysgh = zyysgh;
	}

	public String getJxysgh() {
		return jxysgh;
	}

	public void setJxysgh(String jxysgh) {
		this.jxysgh = jxysgh;
	}

	

	public String getSxysgh() {
		return sxysgh;
	}

	public void setSxysgh(String sxysgh) {
		this.sxysgh = sxysgh;
	}

	public String getBmygh() {
		return bmygh;
	}

	public void setBmygh(String bmygh) {
		this.bmygh = bmygh;
	}

	public String getZkysgh() {
		return zkysgh;
	}

	public void setZkysgh(String zkysgh) {
		this.zkysgh = zkysgh;
	}

	public String getZzkhsbhkysgh() {
		return zzkhsbhkysgh;
	}

	public void setZzkhsbhkysgh(String zzkhsbhkysgh) {
		this.zzkhsbhkysgh = zzkhsbhkysgh;
	}

	public String getCykbbm() {
		return cykbbm;
	}

	public void setCykbbm(String cykbbm) {
		this.cykbbm = cykbbm;
	}

	public String getRykbbm() {
		return rykbbm;
	}

	public void setRykbbm(String rykbbm) {
		this.rykbbm = rykbbm;
	}

	public String getRysqkdm() {
		return rysqkdm;
	}

	public void setRysqkdm(String rysqkdm) {
		this.rysqkdm = rysqkdm;
	}

	public String getZyhzrybq() {
		return zyhzrybq;
	}

	public void setZyhzrybq(String zyhzrybq) {
		this.zyhzrybq = zyhzrybq;
	}

	public String getKzrgh() {
		return kzrgh;
	}

	public void setKzrgh(String kzrgh) {
		this.kzrgh = kzrgh;
	}

	public String getXzzDm() {
		return xzzDm;
	}

	public void setXzzDm(String xzzDm) {
		this.xzzDm = xzzDm;
	}

	public String getZrysgh() {
		return zrysgh;
	}

	public void setZrysgh(String zrysgh) {
		this.zrysgh = zrysgh;
	}

	public String getZrhsgh() {
		return zrhsgh;
	}

	public void setZrhsgh(String zrhsgh) {
		this.zrhsgh = zrhsgh;
	}

	public String getYlfyjsfsDm() {
		return ylfyjsfsDm;
	}

	public void setYlfyjsfsDm(String ylfyjsfsDm) {
		this.ylfyjsfsDm = ylfyjsfsDm;
	}

	public String getXxb() {
		return xxb;
	}

	public void setXxb(String xxb) {
		this.xxb = xxb;
	}

	public String getXj() {
		return xj;
	}

	public void setXj(String xj) {
		this.xj = xj;
	}

	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

	public String getQt() {
		return qt;
	}

	public void setQt(String qt) {
		this.qt = qt;
	}

	public String getHjdzDm() {
		return hjdzDm;
	}

	public void setHjdzDm(String hjdzDm) {
		this.hjdzDm = hjdzDm;
	}

	public String getCyjy() {
		return cyjy;
	}

	public void setCyjy(String cyjy) {
		this.cyjy = cyjy;
	}

	public String getYygrjgDm() {
		return yygrjgDm;
	}

	public void setYygrjgDm(String yygrjgDm) {
		this.yygrjgDm = yygrjgDm;
	}

	public String getGmyMc() {
		return gmyMc;
	}

	public void setGmyMc(String gmyMc) {
		this.gmyMc = gmyMc;
	}

	public String getZyhzgmz() {
		return zyhzgmz;
	}

	public void setZyhzgmz(String zyhzgmz) {
		this.zyhzgmz = zyhzgmz;
	}

	public String getSfszDm() {
		return sfszDm;
	}

	public void setSfszDm(String sfszDm) {
		this.sfszDm = sfszDm;
	}

	public String getHzjbzddzDm() {
		return hzjbzddzDm;
	}

	public void setHzjbzddzDm(String hzjbzddzDm) {
		this.hzjbzddzDm = hzjbzddzDm;
	}

	public String getZyhzzdfhqkxxms() {
		return zyhzzdfhqkxxms;
	}

	public void setZyhzzdfhqkxxms(String zyhzzdfhqkxxms) {
		this.zyhzzdfhqkxxms = zyhzzdfhqkxxms;
	}

	public String getZyhzjbzdlxxxms() {
		return zyhzjbzdlxxxms;
	}

	public void setZyhzjbzdlxxxms(String zyhzjbzdlxxxms) {
		this.zyhzjbzdlxxxms = zyhzjbzdlxxxms;
	}

	public String getSzqx() {
		return szqx;
	}

	public void setSzqx(String szqx) {
		this.szqx = szqx;
	}

	public String getSzqxsjDw() {
		return szqxsjDw;
	}

	public void setSzqxsjDw(String szqxsjDw) {
		this.szqxsjDw = szqxsjDw;
	}

	public String getSszljczdwbydylDm() {
		return sszljczdwbydylDm;
	}

	public void setSszljczdwbydylDm(String sszljczdwbydylDm) {
		this.sszljczdwbydylDm = sszljczdwbydylDm;
	}

	public String getRyhqzrq() {
		return ryhqzrq;
	}

	public void setRyhqzrq(String ryhqzrq) {
		this.ryhqzrq = ryhqzrq;
	}

	public String getYjssxysgh() {
		return yjssxysgh;
	}

	public void setYjssxysgh(String yjssxysgh) {
		this.yjssxysgh = yjssxysgh;
	}

	public String getYjssxysmc() {
		return yjssxysmc;
	}

	public void setYjssxysmc(String yjssxysmc) {
		this.yjssxysmc = yjssxysmc;
	}

	public String getZkkbdm() {
		return zkkbdm;
	}

	public void setZkkbdm(String zkkbdm) {
		this.zkkbdm = zkkbdm;
	}

	public String getCyxyzdDm() {
		return cyxyzdDm;
	}

	public void setCyxyzdDm(String cyxyzdDm) {
		this.cyxyzdDm = cyxyzdDm;
	}

	public String getCyxyzdMc() {
		return cyxyzdMc;
	}

	public void setCyxyzdMc(String cyxyzdMc) {
		this.cyxyzdMc = cyxyzdMc;
	}

	public String getZljgDm() {
		return zljgDm;
	}

	public void setZljgDm(String zljgDm) {
		this.zljgDm = zljgDm;
	}

	public String getZyfyflDm() {
		return zyfyflDm;
	}

	public void setZyfyflDm(String zyfyflDm) {
		this.zyfyflDm = zyfyflDm;
	}

	public String getXgysjsmc() {
		return xgysjsmc;
	}

	public void setXgysjsmc(String xgysjsmc) {
		this.xgysjsmc = xgysjsmc;
	}

	public String getXgysxm() {
		return xgysxm;
	}

	public void setXgysxm(String xgysxm) {
		this.xgysxm = xgysxm;
	}

	public String getZlfqlx() {
		return zlfqlx;
	}

	public void setZlfqlx(String zlfqlx) {
		this.zlfqlx = zlfqlx;
	}

	public String getDjsj() {
		return djsj;
	}

	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}

	public String getDjrygh() {
		return djrygh;
	}

	public void setDjrygh(String djrygh) {
		this.djrygh = djrygh;
	}

	public String getZhxgsj() {
		return zhxgsj;
	}

	public void setZhxgsj(String zhxgsj) {
		this.zhxgsj = zhxgsj;
	}

	public String getZhxgrygh() {
		return zhxgrygh;
	}

	public void setZhxgrygh(String zhxgrygh) {
		this.zhxgrygh = zhxgrygh;
	}

	public String getZhxgrymc() {
		return zhxgrymc;
	}

	public void setZhxgrymc(String zhxgrymc) {
		this.zhxgrymc = zhxgrymc;
	}

	public String getHxb() {
		return hxb;
	}

	public void setHxb(String hxb) {
		this.hxb = hxb;
	}

	public String getBlqph() {
		return blqph;
	}

	public void setBlqph(String blqph) {
		this.blqph = blqph;
	}

	public String getXxph() {
		return xxph;
	}

	public void setXxph(String xxph) {
		this.xxph = xxph;
	}

	public String getCyyy() {
		return cyyy;
	}

	public void setCyyy(String cyyy) {
		this.cyyy = cyyy;
	}

	public String getSjblDm() {
		return sjblDm;
	}

	public void setSjblDm(String sjblDm) {
		this.sjblDm = sjblDm;
	}
    

    
}
