package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;

public class ResultBact {

  private String resourceType;

  private String implicitRules;

  private Device device;

  private TextElement method;

  private List<ComponentBact> component;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public String getImplicitRules() {
    return implicitRules;
  }

  public void setImplicitRules(String implicitRules) {
    this.implicitRules = implicitRules;
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

  public List<ComponentBact> getComponent() {
    return component;
  }

  public void setComponent(List<ComponentBact> component) {
    this.component = component;
  }
}
