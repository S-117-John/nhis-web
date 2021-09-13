package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl;

public class DataTable {
   private String patientId;
   private String patientName;
   private String billBatchCode;
   private String busNo;
   private String random;
   private String billNo;
   private String chargeDate;
   private String chargeTotal;
   private String printTimes;
   private String billUrl;

   public String getBillUrl() {
      return billUrl;
   }

   public void setBillUrl(String billUrl) {
      this.billUrl = billUrl;
   }

   public String getPatientId() {
      return patientId;
   }

   public void setPatientId(String patientId) {
      this.patientId = patientId;
   }

   public String getPatientName() {
      return patientName;
   }

   public void setPatientName(String patientName) {
      this.patientName = patientName;
   }

   public String getBillBatchCode() {
      return billBatchCode;
   }

   public void setBillBatchCode(String billBatchCode) {
      this.billBatchCode = billBatchCode;
   }

   public String getBusNo() {
      return busNo;
   }

   public void setBusNo(String busNo) {
      this.busNo = busNo;
   }

   public String getRandom() {
      return random;
   }

   public void setRandom(String random) {
      this.random = random;
   }

   public String getBillNo() {
      return billNo;
   }

   public void setBillNo(String billNo) {
      this.billNo = billNo;
   }

   public String getChargeDate() {
      return chargeDate;
   }

   public void setChargeDate(String chargeDate) {
      this.chargeDate = chargeDate;
   }

   public String getChargeTotal() {
      return chargeTotal;
   }

   public void setChargeTotal(String chargeTotal) {
      this.chargeTotal = chargeTotal;
   }

   public String getPrintTimes() {
      return printTimes;
   }

   public void setPrintTimes(String printTimes) {
      this.printTimes = printTimes;
   }
}
