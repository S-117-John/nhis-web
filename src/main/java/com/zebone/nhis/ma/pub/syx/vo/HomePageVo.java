package com.zebone.nhis.ma.pub.syx.vo;

import java.util.List;

public class HomePageVo {

	//首页pk
	public String pkPage;
	
	public String pkPv;
	
	public String bedNo;
	// 证件类型aac043
	public String IdType;

	// 证件号码aac044

	public String IdNo;

	// 病案号yzy001

	public String PatNo;

	// 住院次数yzy002

	public String Times;

	// ICD版本yzy003
	public String DiagVersion;

	// 住院流水号yzy004

	public String CodePv;

	// 年龄akc023

	public String age;

	// 患者姓名aac003

	public String Name;

	// 性别编号aac004

	public String DtSex;

	// 性别名称yzy008

	public String SexName;

	// 出生日期aac006

	public String BirthDate;

	// 出生地yzy010

	public String BirthAddr;

	// 身份证号yzy011

	public String PageIdNo;

	// 国籍编码aac161

	public String CountryCode;

	// 国籍名称yzy013

	public String CountryName;

	// 民族编号aac005

	public String NationCode;

	// 民族名称yzy015

	public String NationName;

	// 职业yzy016

	public String OccupName;

	// 婚姻状况编号aac017

	public String MarryCode;

	// 婚姻状况名称yzy018

	public String MarryName;

	// 单位名称aab004

	public String UnitWork;

	// 单位地址yzy020

	public String PostcodeWork;

	// 单位电话yzy021

	public String TelUnit;

	// 单位邮编yzy022

	public String WorkUnitZipCode;

	// 户口地址aac010

	public String ResideAddr;

	// 户口邮编yzy024

	public String ResideZipCode;

	// 联系人aae004

	public String ContactName;

	// 联系人关系yzy026

	public String ContactRelatName;

	// 联系人地址yzy027

	public String ContactAddr;

	// 联系人电话yzy028

	public String ContactPhone;

	// 健康卡号yzy029

	public String HealthCardNo;

	// 入院日期ykc701

	public String AdmitTime;

	// 入院统一科号yzy032

	public String AdmitCodeDept;

	// 入院科别yzy033

	public String AdmitDeptName;

	// 入院病室yzy034

	public String AdmitWardName;

	// 出院时间ykc702

	public String DisTime;

	// 出院统一科号yzy037

	public String DisCodeDept;

	// 出院科室yzy038

	public String DisDeptName;

	// 出院病室yzy039

	public String DisWardName;

	// 实际住院天数akb063

	public String InHosDays;

	// 门急诊诊断编码akc193

	public String DiagCodeClinic;

	// 门急诊诊断名称akc050

	public String DiagNameClinic;

	// 门急诊医生编号yzy043

	public String PkEmpClinic;

	// 门急诊医生名称ake022

	public String ClinicName;

	// 病理诊断yzy045

	public String DiagNamePatho;

	// 过敏药物yzy046

	public String AllergicDrug;

	// 抢救次数yzy047

	public String NumRes;

	// 抢救成功次数yzy048

	public String NumSucc;

	// 科主任编号yzy049

	public String PkEmpChief;

	// 科主任yzy050

	public String ChiefName;

	// 主任医生编号yzy051

	public String PkEmpDirector;

	// 主任医生yzy052

	public String DirectorName;

	// 主治医生编号yzy053

	public String PkEmpConsult;

	// 主治医生yzy054

	public String ConsultName;

	// 住院医生编号yzy055

	public String PkEmpRefer;

	// 住院医生yzy056

	public String ReferName;

	// 进修医生编号yzy057

	public String PkEmpLearn;

	// 进修医生yzy058

	public String LearnName;

	// 实习医生编号yzy059

	public String PkEmpIntern;

	// 实习医生yzy060

	public String InternName;

	// 编码员编号yzy061

	public String PkEmpCoder;

	// 编码员yzy062

	public String CoderName;

	// 病案质量编号yzy063

	public String QualityCode;

	// 病案质量yzy064

	public String QualityName;

	// 质控医师编号yzy065

	public String PkEmpQcDoc;

	// 质控医师yzy066

	public String QcDocName;

	// 质控护士编号yzy067

	public String PkEmpQcNurse;

	// 质控护士yzy068

	public String QcNurseName;

	// 质控日期yzy069

	public String QcDate;

	// 总费用akc264

	public String Sum;

	// 西药费ake047

	public String Xyf;

	// 中药费yzy072

	public String Zyf;

	// 中成药费ake050

	public String Zchyf;

	// 中草药费ake049

	public String Zcaoyf;

	// 其他费ake044

	public String Qtf;

	// 是否尸检编号yzy076

	public String FlagAutopsy;

	// 是否尸检yzy077

	public String AutopsyName;

	// 血型编号yzy078

	public String BloodCodeAbo;

	// 血型yzy079

	public String BloodNameAbo;

	// RH编号yzy080

	public String BloodNodeRh;

	// RH yzy081

	public String BloodNameRh;

	// 首次转科统一科号yzy082

	public String FirstTranCodeDept;

	// 首次转科科别yzy083

	public String FirstTranDept;

	// 首次转科日期yzy084

	public String FirstTranDate;

	// 首次转科时间yzy085

	public String FirstTranTime;

	// 疾病分型编号yzy086

	public String MedRecTypeCode;

	// 疾病分型yzy087

	public String MedRecTypeName;

	// 籍贯yzy088

	public String OriginAddr;

	// 现住址yzy089

	public String CurrAddr;

	// 现电话yzy090

	public String CurrPhone;

	// 现邮编yzy091

	public String CurrZipCode;

	// 职业编号aca111

	public String OccupCode;

	// 新生儿出生体重yzy093

	public String NewbornWeight;

	// 新生儿入院体重yzy094

	public String NewbornInWeight;

	// 入院途径编号yzy095

	public String AdmitPathCode;

	// 入院途径yzy096

	public String AdmitPathName;

	// 临床路径病例标志
	public String FlagCp;
	
	// 临床路径病例编号yzy097

	public String FlagCpCode;

	// 临床路径病例yzy098

	public String FlagCpName;

	// 病理疾病编码yzy099

	public String DiagCodePatho;

	// 病理号yzy100

	public String PathoNo;

	//是否药物过敏标志
	public String FlagDrugAllergy;
	
	// 是否药物过敏编号yzy101

	public String FlagDrugAllergyCode;

	// 是否药物过敏yzy102

	public String FlagDrugAllergyName;

	// 责任护士编号yzy103

	public String PkEmpNurse;

	// 责任护士yzy104

	public String NurseName;

	// 离院方式编号yzy105

	public String LeaveHosCode;

	// 离院方式yzy106

	public String LeaveHosName;

	// 离院方式为医嘱转院,拟接收医疗机构名称yzy107

	public String ReceiveOrgNameTwo;

	// 离院方式为转社区卫生服务器机构/乡镇卫生院，拟接收医疗机构名称yzy108

	public String ReceiveOrgNameThree;

	// 是否有出院31天内再住院计划标志
	public String FlagReadmit;
	
	// 是否有出院31天内再住院计划编号 yzy109

	public String FlagReadmitCode;

	// 是否有出院31天内再住院计划 yzy110

	public String FlagReadmitName;

	// 再住院目的 yzy111

	public String ReadmitPurp;

	// 颅脑损伤患者昏迷时间：入院前天 yzy112

	public String ComaDayBef;

	// 颅脑损伤患者昏迷时间：入院前小时 yzy113

	public String ComaHourBef;

	// 颅脑损伤患者昏迷时间：入院前分钟 yzy114

	public String ComaMinBef;

	// 入院前昏迷总分钟 yzy115

	public String ZongMinBef;

	// 颅脑损伤患者昏迷时间：入院后天yzy116

	public String ComaDayAfter;

	// 颅脑损伤患者昏迷时间：入院后小时 yzy117

	public String ComaHourAfter;

	// 颅脑损伤患者昏迷时间：入院后 分钟 yzy118

	public String ComaMinAfter;

	// 入院后昏迷总分钟 yzy119

	public String ZongMinAfter;

	//付款方式
	public String MedPayMode;
	
	// yzy120 付款方式编号

	public String MedPayModeCode;

	// yzy121 付款方式

	public String MedPayModeName;

	// yzy122 住院总费用：自费金额

	public String Zfje;

	// yzy123 综合医疗服务类：（1）一般医疗服务费

	public String Ybfwf;

	// yzy124 综合医疗服务类：（2）一般治疗操作费

	public String Ybzlczf;

	// yzy125 综合医疗服务类：（3）护理费

	public String Hlf;

	// yzy126 综合医疗服务类：（4）其他费用

	public String FuQtf;

	// yzy127 诊断类：(5) 病理诊断费

	public String Blzdf;

	// yzy128 诊断类：(6) 实验室诊断费

	public String Syzdf;

	// yzy129 诊断类：(7) 影像学诊断费

	public String Yxzdf;

	// yzy130 诊断类：(8) 临床诊断项目费

	public String Lczdf;

	// yzy131 治疗类：(9) 非手术治疗项目费

	public String Fsszlf;

	// yzy132 治疗类：非手术治疗项目费 其中临床物理治疗费

	public String Lcwlf;

	// yzy133 治疗类：(10) 手术治疗费

	public String Ssf;

	// yzy134 治疗类：手术治疗费 其中麻醉费

	public String Mzf;

	// yzy135 治疗类：手术治疗费 其中手术费

	public String Ssqzf;

	// yzy136 康复类：(11) 康复费

	public String Kff;

	// yzy137 中医类：中医治疗类

	public String Zyzll;

	// yzy138 西药类： 西药费 其中抗菌药物费用

	public String Kjywf;

	// yzy139 血液和血液制品类： 血费

	public String Xf;

	// yzy140 血液和血液制品类： 白蛋白类制品费

	public String Bdbf;

	// yzy141 血液和血液制品类： 球蛋白制品费

	public String Qdbf;

	// yzy142 血液和血液制品类：凝血因子类制品费

	public String Yxyzf;

	// yzy143 血液和血液制品类： 细胞因子类费

	public String Xbyzf;

	// yzy144 耗材类：检查用一次性医用材料费

	public String Ycxyyf;

	// yzy145 耗材类：治疗用一次性医用材料费

	public String Zlclf;

	// yzy146 耗材类：手术用一次性医用材料费

	public String Ssclf;
	//诊断列表
	public List<HomePageDiagVo> diagList;
	//手术列表
	public List<HomePageOpVo> opsList;
	//妇婴
	public List<HomePageBrVo> brList;
	//肿瘤
	public List<HomePageOrVo> orList;
	//出院记录
	public List<HomeLhVo> lhList;
	
	public String hpName;

	
	public String getHpName() {
		return hpName;
	}

	public void setHpName(String hpName) {
		this.hpName = hpName;
	}

	public List<HomePageDiagVo> getDiagList() {
		return diagList;
	}

	public void setDiagList(List<HomePageDiagVo> diagList) {
		this.diagList = diagList;
	}

	public List<HomePageOpVo> getOpsList() {
		return opsList;
	}

	public void setOpsList(List<HomePageOpVo> opsList) {
		this.opsList = opsList;
	}

	public List<HomePageBrVo> getBrList() {
		return brList;
	}

	public void setBrList(List<HomePageBrVo> brList) {
		this.brList = brList;
	}

	public List<HomePageOrVo> getOrList() {
		return orList;
	}

	public void setOrList(List<HomePageOrVo> orList) {
		this.orList = orList;
	}

	public List<HomeLhVo> getLhList() {
		return lhList;
	}

	public void setLhList(List<HomeLhVo> lhList) {
		this.lhList = lhList;
	}

	public String getIdType() {
		return IdType;
	}

	public void setIdType(String idType) {
		IdType = idType;
	}

	public String getIdNo() {
		return IdNo;
	}

	public void setIdNo(String idNo) {
		IdNo = idNo;
	}

	public String getPatNo() {
		return PatNo;
	}

	public void setPatNo(String patNo) {
		PatNo = patNo;
	}

	public String getTimes() {
		return Times;
	}

	public void setTimes(String times) {
		Times = times;
	}

	public String getDiagVersion() {
		return DiagVersion;
	}

	public void setDiagVersion(String diagVersion) {
		DiagVersion = diagVersion;
	}

	public String getCodePv() {
		return CodePv;
	}

	public void setCodePv(String codePv) {
		CodePv = codePv;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDtSex() {
		return DtSex;
	}

	public void setDtSex(String dtSex) {
		DtSex = dtSex;
	}

	public String getSexName() {
		return SexName;
	}

	public void setSexName(String sexName) {
		SexName = sexName;
	}

	public String getBirthDate() {
		return BirthDate;
	}

	public void setBirthDate(String birthDate) {
		BirthDate = birthDate;
	}

	public String getBirthAddr() {
		return BirthAddr;
	}

	public void setBirthAddr(String birthAddr) {
		BirthAddr = birthAddr;
	}

	public String getPageIdNo() {
		return PageIdNo;
	}

	public void setPageIdNo(String pageIdNo) {
		PageIdNo = pageIdNo;
	}

	public String getCountryCode() {
		return CountryCode;
	}

	public void setCountryCode(String countryCode) {
		CountryCode = countryCode;
	}

	public String getCountryName() {
		return CountryName;
	}

	public void setCountryName(String countryName) {
		CountryName = countryName;
	}

	public String getNationCode() {
		return NationCode;
	}

	public void setNationCode(String nationCode) {
		NationCode = nationCode;
	}

	public String getNationName() {
		return NationName;
	}

	public void setNationName(String nationName) {
		NationName = nationName;
	}

	public String getOccupName() {
		return OccupName;
	}

	public void setOccupName(String occupName) {
		OccupName = occupName;
	}

	public String getMarryCode() {
		return MarryCode;
	}

	public void setMarryCode(String marryCode) {
		MarryCode = marryCode;
	}

	public String getMarryName() {
		return MarryName;
	}

	public void setMarryName(String marryName) {
		MarryName = marryName;
	}

	public String getUnitWork() {
		return UnitWork;
	}

	public void setUnitWork(String unitWork) {
		UnitWork = unitWork;
	}

	public String getPostcodeWork() {
		return PostcodeWork;
	}

	public void setPostcodeWork(String postcodeWork) {
		PostcodeWork = postcodeWork;
	}

	public String getTelUnit() {
		return TelUnit;
	}

	public void setTelUnit(String telUnit) {
		TelUnit = telUnit;
	}

	public String getWorkUnitZipCode() {
		return WorkUnitZipCode;
	}

	public void setWorkUnitZipCode(String workUnitZipCode) {
		WorkUnitZipCode = workUnitZipCode;
	}

	public String getResideAddr() {
		return ResideAddr;
	}

	public void setResideAddr(String resideAddr) {
		ResideAddr = resideAddr;
	}

	public String getResideZipCode() {
		return ResideZipCode;
	}

	public void setResideZipCode(String resideZipCode) {
		ResideZipCode = resideZipCode;
	}

	public String getContactName() {
		return ContactName;
	}

	public void setContactName(String contactName) {
		ContactName = contactName;
	}

	public String getContactRelatName() {
		return ContactRelatName;
	}

	public void setContactRelatName(String contactRelatName) {
		ContactRelatName = contactRelatName;
	}

	public String getContactAddr() {
		return ContactAddr;
	}

	public void setContactAddr(String contactAddr) {
		ContactAddr = contactAddr;
	}

	public String getContactPhone() {
		return ContactPhone;
	}

	public void setContactPhone(String contactPhone) {
		ContactPhone = contactPhone;
	}

	public String getHealthCardNo() {
		return HealthCardNo;
	}

	public void setHealthCardNo(String healthCardNo) {
		HealthCardNo = healthCardNo;
	}

	public String getAdmitTime() {
		return AdmitTime;
	}

	public void setAdmitTime(String admitTime) {
		AdmitTime = admitTime;
	}

	public String getAdmitCodeDept() {
		return AdmitCodeDept;
	}

	public void setAdmitCodeDept(String admitCodeDept) {
		AdmitCodeDept = admitCodeDept;
	}

	public String getAdmitDeptName() {
		return AdmitDeptName;
	}

	public void setAdmitDeptName(String admitDeptName) {
		AdmitDeptName = admitDeptName;
	}

	public String getAdmitWardName() {
		return AdmitWardName;
	}

	public void setAdmitWardName(String admitWardName) {
		AdmitWardName = admitWardName;
	}


	public String getDisTime() {
		return DisTime;
	}

	public void setDisTime(String disTime) {
		DisTime = disTime;
	}

	public String getDisDeptName() {
		return DisDeptName;
	}

	public void setDisDeptName(String disDeptName) {
		DisDeptName = disDeptName;
	}

	public String getDisWardName() {
		return DisWardName;
	}

	public void setDisWardName(String disWardName) {
		DisWardName = disWardName;
	}

	public String getDiagNameClinic() {
		return DiagNameClinic;
	}

	public void setDiagNameClinic(String diagNameClinic) {
		DiagNameClinic = diagNameClinic;
	}

	public String getDisCodeDept() {
		return DisCodeDept;
	}

	public void setDisCodeDept(String disCodeDept) {
		DisCodeDept = disCodeDept;
	}


	public String getInHosDays() {
		return InHosDays;
	}

	public void setInHosDays(String inHosDays) {
		InHosDays = inHosDays;
	}

	public String getDiagCodeClinic() {
		return DiagCodeClinic;
	}

	public void setDiagCodeClinic(String diagCodeClinic) {
		DiagCodeClinic = diagCodeClinic;
	}


	public String getPkEmpClinic() {
		return PkEmpClinic;
	}

	public void setPkEmpClinic(String pkEmpClinic) {
		PkEmpClinic = pkEmpClinic;
	}

	public String getClinicName() {
		return ClinicName;
	}

	public void setClinicName(String clinicName) {
		ClinicName = clinicName;
	}

	public String getDiagNamePatho() {
		return DiagNamePatho;
	}

	public void setDiagNamePatho(String diagNamePatho) {
		DiagNamePatho = diagNamePatho;
	}

	public String getAllergicDrug() {
		return AllergicDrug;
	}

	public void setAllergicDrug(String allergicDrug) {
		AllergicDrug = allergicDrug;
	}

	public String getNumRes() {
		return NumRes;
	}

	public void setNumRes(String numRes) {
		NumRes = numRes;
	}

	public String getNumSucc() {
		return NumSucc;
	}

	public void setNumSucc(String numSucc) {
		NumSucc = numSucc;
	}

	public String getPkEmpChief() {
		return PkEmpChief;
	}

	public void setPkEmpChief(String pkEmpChief) {
		PkEmpChief = pkEmpChief;
	}

	public String getChiefName() {
		return ChiefName;
	}

	public void setChiefName(String chiefName) {
		ChiefName = chiefName;
	}

	public String getPkEmpDirector() {
		return PkEmpDirector;
	}

	public void setPkEmpDirector(String pkEmpDirector) {
		PkEmpDirector = pkEmpDirector;
	}

	public String getDirectorName() {
		return DirectorName;
	}

	public void setDirectorName(String directorName) {
		DirectorName = directorName;
	}

	public String getPkEmpConsult() {
		return PkEmpConsult;
	}

	public void setPkEmpConsult(String pkEmpConsult) {
		PkEmpConsult = pkEmpConsult;
	}

	public String getConsultName() {
		return ConsultName;
	}

	public void setConsultName(String consultName) {
		ConsultName = consultName;
	}

	public String getPkEmpRefer() {
		return PkEmpRefer;
	}

	public void setPkEmpRefer(String pkEmpRefer) {
		PkEmpRefer = pkEmpRefer;
	}

	public String getReferName() {
		return ReferName;
	}

	public void setReferName(String referName) {
		ReferName = referName;
	}

	public String getPkEmpLearn() {
		return PkEmpLearn;
	}

	public void setPkEmpLearn(String pkEmpLearn) {
		PkEmpLearn = pkEmpLearn;
	}

	public String getLearnName() {
		return LearnName;
	}

	public void setLearnName(String learnName) {
		LearnName = learnName;
	}

	public String getPkEmpIntern() {
		return PkEmpIntern;
	}

	public void setPkEmpIntern(String pkEmpIntern) {
		PkEmpIntern = pkEmpIntern;
	}

	public String getInternName() {
		return InternName;
	}

	public void setInternName(String internName) {
		InternName = internName;
	}

	public String getPkEmpCoder() {
		return PkEmpCoder;
	}

	public void setPkEmpCoder(String pkEmpCoder) {
		PkEmpCoder = pkEmpCoder;
	}

	public String getCoderName() {
		return CoderName;
	}

	public void setCoderName(String coderName) {
		CoderName = coderName;
	}

	public String getQualityCode() {
		return QualityCode;
	}

	public void setQualityCode(String qualityCode) {
		QualityCode = qualityCode;
	}

	public String getQualityName() {
		return QualityName;
	}

	public void setQualityName(String qualityName) {
		QualityName = qualityName;
	}

	public String getPkEmpQcDoc() {
		return PkEmpQcDoc;
	}

	public void setPkEmpQcDoc(String pkEmpQcDoc) {
		PkEmpQcDoc = pkEmpQcDoc;
	}

	public String getQcDocName() {
		return QcDocName;
	}

	public void setQcDocName(String qcDocName) {
		QcDocName = qcDocName;
	}

	public String getPkEmpQcNurse() {
		return PkEmpQcNurse;
	}

	public void setPkEmpQcNurse(String pkEmpQcNurse) {
		PkEmpQcNurse = pkEmpQcNurse;
	}

	public String getQcNurseName() {
		return QcNurseName;
	}

	public void setQcNurseName(String qcNurseName) {
		QcNurseName = qcNurseName;
	}

	public String getQcDate() {
		return QcDate;
	}

	public void setQcDate(String qcDate) {
		QcDate = qcDate;
	}

	public String getSum() {
		return Sum;
	}

	public void setSum(String sum) {
		Sum = sum;
	}

	public String getXyf() {
		return Xyf;
	}

	public void setXyf(String xyf) {
		Xyf = xyf;
	}

	public String getZyf() {
		return Zyf;
	}

	public void setZyf(String zyf) {
		Zyf = zyf;
	}

	public String getZchyf() {
		return Zchyf;
	}

	public void setZchyf(String zchyf) {
		Zchyf = zchyf;
	}

	public String getZcaoyf() {
		return Zcaoyf;
	}

	public void setZcaoyf(String zcaoyf) {
		Zcaoyf = zcaoyf;
	}

	public String getQtf() {
		return Qtf;
	}

	public void setQtf(String qtf) {
		Qtf = qtf;
	}

	public String getFlagAutopsy() {
		return FlagAutopsy;
	}

	public void setFlagAutopsy(String flagAutopsy) {
		FlagAutopsy = flagAutopsy;
	}

	public String getAutopsyName() {
		return AutopsyName;
	}

	public void setAutopsyName(String autopsyName) {
		AutopsyName = autopsyName;
	}

	public String getBloodCodeAbo() {
		return BloodCodeAbo;
	}

	public void setBloodCodeAbo(String bloodCodeAbo) {
		BloodCodeAbo = bloodCodeAbo;
	}

	public String getBloodNameAbo() {
		return BloodNameAbo;
	}

	public void setBloodNameAbo(String bloodNameAbo) {
		BloodNameAbo = bloodNameAbo;
	}

	public String getBloodNodeRh() {
		return BloodNodeRh;
	}

	public void setBloodNodeRh(String bloodNodeRh) {
		BloodNodeRh = bloodNodeRh;
	}

	public String getBloodNameRh() {
		return BloodNameRh;
	}

	public void setBloodNameRh(String bloodNameRh) {
		BloodNameRh = bloodNameRh;
	}

	public String getFirstTranCodeDept() {
		return FirstTranCodeDept;
	}

	public void setFirstTranCodeDept(String firstTranCodeDept) {
		FirstTranCodeDept = firstTranCodeDept;
	}

	public String getFirstTranDept() {
		return FirstTranDept;
	}

	public void setFirstTranDept(String firstTranDept) {
		FirstTranDept = firstTranDept;
	}

	public String getFirstTranDate() {
		return FirstTranDate;
	}

	public void setFirstTranDate(String firstTranDate) {
		FirstTranDate = firstTranDate;
	}

	public String getFirstTranTime() {
		return FirstTranTime;
	}

	public void setFirstTranTime(String firstTranTime) {
		FirstTranTime = firstTranTime;
	}

	public String getMedRecTypeCode() {
		return MedRecTypeCode;
	}

	public void setMedRecTypeCode(String medRecTypeCode) {
		MedRecTypeCode = medRecTypeCode;
	}

	public String getMedRecTypeName() {
		return MedRecTypeName;
	}

	public void setMedRecTypeName(String medRecTypeName) {
		MedRecTypeName = medRecTypeName;
	}

	public String getOriginAddr() {
		return OriginAddr;
	}

	public void setOriginAddr(String originAddr) {
		OriginAddr = originAddr;
	}

	public String getCurrAddr() {
		return CurrAddr;
	}

	public void setCurrAddr(String currAddr) {
		CurrAddr = currAddr;
	}

	public String getCurrPhone() {
		return CurrPhone;
	}

	public void setCurrPhone(String currPhone) {
		CurrPhone = currPhone;
	}

	public String getCurrZipCode() {
		return CurrZipCode;
	}

	public void setCurrZipCode(String currZipCode) {
		CurrZipCode = currZipCode;
	}

	public String getOccupCode() {
		return OccupCode;
	}

	public void setOccupCode(String occupCode) {
		OccupCode = occupCode;
	}

	public String getNewbornWeight() {
		return NewbornWeight;
	}

	public void setNewbornWeight(String newbornWeight) {
		NewbornWeight = newbornWeight;
	}

	public String getNewbornInWeight() {
		return NewbornInWeight;
	}

	public void setNewbornInWeight(String newbornInWeight) {
		NewbornInWeight = newbornInWeight;
	}

	public String getAdmitPathCode() {
		return AdmitPathCode;
	}

	public void setAdmitPathCode(String admitPathCode) {
		AdmitPathCode = admitPathCode;
	}

	public String getAdmitPathName() {
		return AdmitPathName;
	}

	public void setAdmitPathName(String admitPathName) {
		AdmitPathName = admitPathName;
	}

	public String getFlagCpCode() {
		return FlagCpCode;
	}

	public void setFlagCpCode(String flagCpCode) {
		FlagCpCode = flagCpCode;
	}

	public String getFlagCpName() {
		return FlagCpName;
	}

	public void setFlagCpName(String flagCpName) {
		FlagCpName = flagCpName;
	}

	public String getDiagCodePatho() {
		return DiagCodePatho;
	}

	public void setDiagCodePatho(String diagCodePatho) {
		DiagCodePatho = diagCodePatho;
	}

	public String getPathoNo() {
		return PathoNo;
	}

	public void setPathoNo(String pathoNo) {
		PathoNo = pathoNo;
	}

	public String getFlagDrugAllergyCode() {
		return FlagDrugAllergyCode;
	}

	public void setFlagDrugAllergyCode(String flagDrugAllergyCode) {
		FlagDrugAllergyCode = flagDrugAllergyCode;
	}

	public String getFlagDrugAllergyName() {
		return FlagDrugAllergyName;
	}

	public void setFlagDrugAllergyName(String flagDrugAllergyName) {
		FlagDrugAllergyName = flagDrugAllergyName;
	}

	public String getPkEmpNurse() {
		return PkEmpNurse;
	}

	public void setPkEmpNurse(String pkEmpNurse) {
		PkEmpNurse = pkEmpNurse;
	}

	public String getNurseName() {
		return NurseName;
	}

	public void setNurseName(String nurseName) {
		NurseName = nurseName;
	}

	public String getLeaveHosCode() {
		return LeaveHosCode;
	}

	public void setLeaveHosCode(String leaveHosCode) {
		LeaveHosCode = leaveHosCode;
	}

	public String getLeaveHosName() {
		return LeaveHosName;
	}

	public void setLeaveHosName(String leaveHosName) {
		LeaveHosName = leaveHosName;
	}

	public String getReceiveOrgNameTwo() {
		return ReceiveOrgNameTwo;
	}

	public void setReceiveOrgNameTwo(String receiveOrgNameTwo) {
		ReceiveOrgNameTwo = receiveOrgNameTwo;
	}

	public String getReceiveOrgNameThree() {
		return ReceiveOrgNameThree;
	}

	public void setReceiveOrgNameThree(String receiveOrgNameThree) {
		ReceiveOrgNameThree = receiveOrgNameThree;
	}

	public String getFlagReadmitCode() {
		return FlagReadmitCode;
	}

	public void setFlagReadmitCode(String flagReadmitCode) {
		FlagReadmitCode = flagReadmitCode;
	}

	public String getFlagReadmitName() {
		return FlagReadmitName;
	}

	public void setFlagReadmitName(String flagReadmitName) {
		FlagReadmitName = flagReadmitName;
	}

	public String getReadmitPurp() {
		return ReadmitPurp;
	}

	public void setReadmitPurp(String readmitPurp) {
		ReadmitPurp = readmitPurp;
	}

	public String getComaDayBef() {
		return ComaDayBef;
	}

	public void setComaDayBef(String comaDayBef) {
		ComaDayBef = comaDayBef;
	}

	public String getComaHourBef() {
		return ComaHourBef;
	}

	public void setComaHourBef(String comaHourBef) {
		ComaHourBef = comaHourBef;
	}

	public String getComaMinBef() {
		return ComaMinBef;
	}

	public void setComaMinBef(String comaMinBef) {
		ComaMinBef = comaMinBef;
	}

	public String getZongMinBef() {
		return ZongMinBef;
	}

	public void setZongMinBef(String zongMinBef) {
		ZongMinBef = zongMinBef;
	}

	public String getComaDayAfter() {
		return ComaDayAfter;
	}

	public void setComaDayAfter(String comaDayAfter) {
		ComaDayAfter = comaDayAfter;
	}

	public String getComaHourAfter() {
		return ComaHourAfter;
	}

	public void setComaHourAfter(String comaHourAfter) {
		ComaHourAfter = comaHourAfter;
	}

	public String getComaMinAfter() {
		return ComaMinAfter;
	}

	public void setComaMinAfter(String comaMinAfter) {
		ComaMinAfter = comaMinAfter;
	}

	public String getZongMinAfter() {
		return ZongMinAfter;
	}

	public void setZongMinAfter(String zongMinAfter) {
		ZongMinAfter = zongMinAfter;
	}

	public String getMedPayModeCode() {
		return MedPayModeCode;
	}

	public void setMedPayModeCode(String medPayModeCode) {
		MedPayModeCode = medPayModeCode;
	}

	public String getMedPayModeName() {
		return MedPayModeName;
	}

	public void setMedPayModeName(String medPayModeName) {
		MedPayModeName = medPayModeName;
	}

	public String getZfje() {
		return Zfje;
	}

	public void setZfje(String zfje) {
		Zfje = zfje;
	}

	public String getYbfwf() {
		return Ybfwf;
	}

	public void setYbfwf(String ybfwf) {
		Ybfwf = ybfwf;
	}

	public String getYbzlczf() {
		return Ybzlczf;
	}

	public void setYbzlczf(String ybzlczf) {
		Ybzlczf = ybzlczf;
	}

	public String getHlf() {
		return Hlf;
	}

	public void setHlf(String hlf) {
		Hlf = hlf;
	}

	public String getFuQtf() {
		return FuQtf;
	}

	public void setFuQtf(String fuQtf) {
		FuQtf = fuQtf;
	}

	public String getBlzdf() {
		return Blzdf;
	}

	public void setBlzdf(String blzdf) {
		Blzdf = blzdf;
	}

	public String getSyzdf() {
		return Syzdf;
	}

	public void setSyzdf(String syzdf) {
		Syzdf = syzdf;
	}

	public String getYxzdf() {
		return Yxzdf;
	}

	public void setYxzdf(String yxzdf) {
		Yxzdf = yxzdf;
	}

	public String getLczdf() {
		return Lczdf;
	}

	public void setLczdf(String lczdf) {
		Lczdf = lczdf;
	}

	public String getFsszlf() {
		return Fsszlf;
	}

	public void setFsszlf(String fsszlf) {
		Fsszlf = fsszlf;
	}

	public String getLcwlf() {
		return Lcwlf;
	}

	public void setLcwlf(String lcwlf) {
		Lcwlf = lcwlf;
	}

	public String getSsf() {
		return Ssf;
	}

	public void setSsf(String ssf) {
		Ssf = ssf;
	}

	public String getMzf() {
		return Mzf;
	}

	public void setMzf(String mzf) {
		Mzf = mzf;
	}

	public String getSsqzf() {
		return Ssqzf;
	}

	public void setSsqzf(String ssqzf) {
		Ssqzf = ssqzf;
	}

	public String getKff() {
		return Kff;
	}

	public void setKff(String kff) {
		Kff = kff;
	}

	public String getZyzll() {
		return Zyzll;
	}

	public void setZyzll(String zyzll) {
		Zyzll = zyzll;
	}

	public String getKjywf() {
		return Kjywf;
	}

	public void setKjywf(String kjywf) {
		Kjywf = kjywf;
	}

	public String getXf() {
		return Xf;
	}

	public void setXf(String xf) {
		Xf = xf;
	}

	public String getBdbf() {
		return Bdbf;
	}

	public void setBdbf(String bdbf) {
		Bdbf = bdbf;
	}

	public String getQdbf() {
		return Qdbf;
	}

	public void setQdbf(String qdbf) {
		Qdbf = qdbf;
	}

	public String getYxyzf() {
		return Yxyzf;
	}

	public void setYxyzf(String yxyzf) {
		Yxyzf = yxyzf;
	}

	public String getXbyzf() {
		return Xbyzf;
	}

	public void setXbyzf(String xbyzf) {
		Xbyzf = xbyzf;
	}

	public String getYcxyyf() {
		return Ycxyyf;
	}

	public void setYcxyyf(String ycxyyf) {
		Ycxyyf = ycxyyf;
	}

	public String getZlclf() {
		return Zlclf;
	}

	public void setZlclf(String zlclf) {
		Zlclf = zlclf;
	}

	public String getSsclf() {
		return Ssclf;
	}

	public void setSsclf(String ssclf) {
		Ssclf = ssclf;
	}

	public String getPkPage() {
		return pkPage;
	}

	public void setPkPage(String pkPage) {
		this.pkPage = pkPage;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getFlagDrugAllergy() {
		return FlagDrugAllergy;
	}

	public void setFlagDrugAllergy(String flagDrugAllergy) {
		FlagDrugAllergy = flagDrugAllergy;
	}

	public String getFlagCp() {
		return FlagCp;
	}

	public void setFlagCp(String flagCp) {
		FlagCp = flagCp;
	}

	public String getFlagReadmit() {
		return FlagReadmit;
	}

	public void setFlagReadmit(String flagReadmit) {
		FlagReadmit = flagReadmit;
	}

	public String getMedPayMode() {
		return MedPayMode;
	}

	public void setMedPayMode(String medPayMode) {
		MedPayMode = medPayMode;
	}
}
