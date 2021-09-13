package com.zebone.nhis.common.support;

public interface IOrdTypeCodeConst {
	/**
	 * 医嘱类型    
	 */
	public static final String DT_ORDTYPE_DRUG="01";//药品
	public static final String DT_ORDTYPE_DRUG_WEST="0101";//西药
	public static final String DT_ORDTYPE_DRUG_WEST_KFLYP="010101";//口服类药品
	public static final String DT_ORDTYPE_DRUG_WEST_ZSLYP="010102";//注射类药品
	public static final String DT_ORDTYPE_DRUG_IV="010102";//iv类药品
	public static final String DT_ORDTYPE_DRUG_CHINA="0102";//中成药
	public static final String DT_ORDTYPE_DRUG_HERB="0103";//草药
	public static final String DT_ORDTYPE_EXAMINE="02";//检查
	public static final String DT_ORDTYPE_TEST="03";//检验
	public static final String DT_ORDTYPE_SURGERY="04";//手术
	public static final String DT_ORDTYPE_TREAT="05";//治疗
	public static final String DT_ORDTYPE_NURSE="06";//护理
	public static final String DT_ORDTYPE_EISAI="07";//卫材
	public static final String DT_ORDTYPE_EXHORTATIONS="0801";//嘱托
	public static final String DT_ORDTYPE_CLINICSRV="0903";//会诊服务类型
	public static final String DT_ORDTYPE_DIET="13";//饮食
	public static final String DT_ORDTYPE_PATI_MANAGER="1201";//转科
	public static final String DT_ORDTYPE_CLINIC_EXPERT_CODE="090101";//专家
	public static final String DT_ORDTYPE_CLINIC_ORDINARY_CODE="090102";//普通
	public static final String DT_ORDTYPE_CLINIC_EMERG_CODE="090103";//急诊(原来的：090203)
	public static final String DT_ORDTYPE_CLINIC="09";//诊疗
	public static final String DT_ORDTYPE_PUBLIC_HEALTH="10";//公卫
	public static final String DT_ORDTYPE_OTHER="99";//其他
	public static final String DT_ORDTYPE_ANTITYPE="00";//抗菌分类编码（非抗）
	
	
}
