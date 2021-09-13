package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;

import java.util.List;


public class Device {

  private String resourceType;

  private List<Parameter> deviceName;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public List<Parameter> getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(List<Parameter> deviceName) {
    this.deviceName = deviceName;
  }
}
