package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;

public class ResultRis {

  private String resourceType;

  private Device device;

  private TextElement method;

  private List<Parameter> component;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public Device getDevice() {
    return device;
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public TextElement getMethod() {
    return method;
  }

  public void setMethod(TextElement method) {
    this.method = method;
  }

  public List<Parameter> getComponent() {
    return component;
  }

  public void setComponent(List<Parameter> component) {
    this.component = component;
  }
}
