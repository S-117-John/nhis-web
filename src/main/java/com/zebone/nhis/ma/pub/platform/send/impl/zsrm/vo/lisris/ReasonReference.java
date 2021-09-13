package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;

import java.util.List;

public class ReasonReference {

  private String resourceType;

  private Code code;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }
}
