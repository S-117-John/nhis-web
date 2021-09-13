package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;


public class EncounterBact {

  private String resourceType;

  @JSONField(name = "class")
  private Coding clas;

  private List<Identifier> identifier;

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public Coding getClas() {
    return clas;
  }

  public void setClas(Coding clas) {
    this.clas = clas;
  }

  public List<Identifier> getIdentifier() {
    return identifier;
  }

  public void setIdentifier(List<Identifier> identifier) {
    this.identifier = identifier;
  }
}
