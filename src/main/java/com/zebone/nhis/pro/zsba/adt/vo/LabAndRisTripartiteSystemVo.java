package com.zebone.nhis.pro.zsba.adt.vo;

import java.util.Date;

/**
 * @author 12370
 * @Classname LabAndRisTripartiteSystemVo
 * @Description 三方检验，检查实体类
 * @Date 2020-09-24 16:51
 * @Created by wuqiang
 */
public class LabAndRisTripartiteSystemVo {

 /**
  * 0 申请，1 提交，2 采集，3 核收，4 报告
  */
 private String euStatu;
 /**
  * 报告号
  */
 private String codeRpt;
 /**
  * 工号
  */
 private String codeEmp;
 /**
  * 条码号
  */
 private String sampNo;
 /**
  * 病人id号
  */
 private String patientId;
 /**
  * 条码下载日期
  */
 private Date bcDate;
 /**
  * 报告日期
  */
 private Date dateRpt;
 /**
  * 报告内容
  */
 private String descRpt;

 /**
  * 检查申请单号
  */
 private String codeApply;


 public String getEuStatu() {
  return euStatu;
 }

 public void setEuStatu(String euStatu) {
  this.euStatu = euStatu;
 }

 public String getCodeRpt() {
  return codeRpt;
 }

 public void setCodeRpt(String codeRpt) {
  this.codeRpt = codeRpt;
 }

 public String getCodeEmp() {
  return codeEmp;
 }

 public void setCodeEmp(String codeEmp) {
  this.codeEmp = codeEmp;
 }

 public String getSampNo() {
  return sampNo;
 }

 public void setSampNo(String sampNo) {
  this.sampNo = sampNo;
 }

 public String getPatientId() {
  return patientId;
 }

 public void setPatientId(String patientId) {
  this.patientId = patientId;
 }

 public Date getBcDate() {
  return bcDate;
 }

 public void setBcDate(Date bcDate) {
  this.bcDate = bcDate;
 }

 public Date getDateRpt() {
  return dateRpt;
 }

 public void setDateRpt(Date dateRpt) {
  this.dateRpt = dateRpt;
 }

 public String getDescRpt() {
  return descRpt;
 }

 public void setDescRpt(String descRpt) {
  this.descRpt = descRpt;
 }

 public String getCodeApply() {
  return codeApply;
 }

 public void setCodeApply(String codeApply) {
  this.codeApply = codeApply;
 }
}

