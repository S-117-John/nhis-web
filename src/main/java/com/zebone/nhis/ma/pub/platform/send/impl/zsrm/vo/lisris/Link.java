package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;

public class Link {

  private String resourceType;

  private Code bodySite;

  private String deviceName;

  private ExtensionLis content;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public Code getBodySite() {
    return bodySite;
  }

  public void setBodySite(Code bodySite) {
    this.bodySite = bodySite;
  }

  public String getDeviceName() {
    return deviceName;
  }

  public void setDeviceName(String deviceName) {
    this.deviceName = deviceName;
  }

  public ExtensionLis getContent() {
    return content;
  }

  public void setContent(ExtensionLis content) {
    this.content = content;
  }
}
