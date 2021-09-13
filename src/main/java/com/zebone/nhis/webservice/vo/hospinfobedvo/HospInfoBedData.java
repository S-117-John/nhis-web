package com.zebone.nhis.webservice.vo.hospinfobedvo;

import javax.xml.bind.annotation.XmlElement;

public class HospInfoBedData {
   private HospInfoBedDataVo bedVo;
   
   @XmlElement(name = "hospInfoList")
   public HospInfoBedDataVo getBedVo() {
	   return bedVo;
   }

   public void setBedVo(HospInfoBedDataVo bedVo) {
	   this.bedVo = bedVo;
   }
   
   
}
