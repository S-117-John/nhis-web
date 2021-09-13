package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 门诊结算发票明细
 * SETTLEMENT_DETAILS_OUTPAT
 */
public class SettlementDetail {

    @MetadataDescribe(id= "LHDE0048501",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0048502",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0048503",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0048504",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0048505",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0048506",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0048507",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0048508",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0048509",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0048510",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0048511",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;


    @MetadataDescribe(id= "LHDE0048512",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0048513",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0048514",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0048515",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;

    @MetadataDescribe(id= "LHDE0048516",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;

    @MetadataDescribe(id= "LHDE0048517",name = "结算发票明细ID",eName = "SETTLEMENT_DETAIL_ID")
    private String settlementDetailId;

    @MetadataDescribe(id= "LHDE0048518",name = "结算主记录ID",eName = "SETTLEMENT_MASTER_ID")
    private String settlementMasterId;

    @MetadataDescribe(id= "LHDE0048519",name = "结算次数",eName = "SETTLEMENT_TIMES")
    private String settlementTimes;

    @MetadataDescribe(id= "LHDE0048520",name = "结算类型代码",eName = "ACCOUNT_TYPE_CODE")
    private String accountTypeCode;

    @MetadataDescribe(id= "LHDE0048521",name = "结算类型名称",eName = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;

    @MetadataDescribe(id= "LHDE0048522",name = "结算日期时间",eName = "SETTLEMENT_DATE_TIME")
    private String settlementDateTime;

    @MetadataDescribe(id= "LHDE0048523",name = "结算人ID",eName = "SETTLEMENT_OPERA_ID")
    private String settlementOperaId;

    @MetadataDescribe(id= "LHDE0048524",name = "结算人姓名",eName = "SETTLEMENT_OPERA_NAME")
    private String settlementOperaName;

    @MetadataDescribe(id= "LHDE0048525",name = "结算窗口号",eName = "SETTLEMENT_WIN_NO")
    private String settlementWinNo;

    @MetadataDescribe(id= "LHDE0048526",name = "发票分类代码",eName = "RCPT_CLASS_CODE")
    private String rcptClassCode;

    @MetadataDescribe(id= "LHDE0048527",name = "发票分类名称",eName = "RCPT_CLASS_NAME")
    private String rcptClassName;

    @MetadataDescribe(id= "LHDE0048528",name = "结算总费用",eName = "TOTAL_FEE")
    private String totalFee;

    @MetadataDescribe(id= "LHDE0048529",name = "费用状态",eName = "CHARGE_STATUS")
    private String chargeStatus;

    @MetadataDescribe(id= "LHDE0048530",name = "子系统",eName = "SUB_SYSTEM_CODE")
    private String subSystemCode;

    @MetadataDescribe(id= "LHDE0048531",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;




}
