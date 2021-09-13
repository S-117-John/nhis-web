package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;

public class RelevantHistory {

  private String resourceType;

  private List<TextElement> reason;

  public RelevantHistory(String resourceType, List<TextElement> reason) {
    this.resourceType = resourceType;
    this.reason = reason;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public List<TextElement> getReason() {
    return reason;
  }

  public void setReason(List<TextElement> reason) {
    this.reason = reason;
  }
}
