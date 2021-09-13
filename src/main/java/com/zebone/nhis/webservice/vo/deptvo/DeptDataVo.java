package com.zebone.nhis.webservice.vo.deptvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DeptDataVo {
   private List<ResDeptVo> resDeptVo;
   @XmlElement(name="dept")
   public List<ResDeptVo> getResDeptVo() {
	   return resDeptVo;
   }

   public void setResDeptVo(List<ResDeptVo> resDeptVo) {
	   this.resDeptVo = resDeptVo;
   }

   
   
}
