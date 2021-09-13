package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.sch;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Period;

import java.util.List;

public class DataJson{

   private String businessType;

   private String patientId;

   private String dataFirst;

   private String dataRemark;

   private String redirectUri;

   private KeyWords keyWords;

   public String getRedirectUri() {
      return redirectUri;
   }

   public void setRedirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
   }

   public String getBusinessType() {
      return businessType;
   }

   public void setBusinessType(String businessType) {
      this.businessType = businessType;
   }

   public String getPatientId() {
      return patientId;
   }

   public void setPatientId(String patientId) {
      this.patientId = patientId;
   }

   public String getDataFirst() {
      return dataFirst;
   }

   public void setDataFirst(String dataFirst) {
      this.dataFirst = dataFirst;
   }

   public String getDataRemark() {
      return dataRemark;
   }

   public void setDataRemark(String dataRemark) {
      this.dataRemark = dataRemark;
   }

   public KeyWords getKeyWords() {
      return keyWords;
   }

   public void setKeyWords(KeyWords keyWords) {
      this.keyWords = keyWords;
   }
}
