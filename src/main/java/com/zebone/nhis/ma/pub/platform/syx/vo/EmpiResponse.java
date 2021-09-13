/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlRootElement
public class EmpiResponse {
	@XmlElement(name = "HIP_EMPI")
    private HipEmpi hipEmpi;
    public void setHIP_EMPI(HipEmpi hipEmpi) {
         this.hipEmpi = hipEmpi;
     }
     public HipEmpi getHIP_EMPI() {
         return hipEmpi;
     }

}