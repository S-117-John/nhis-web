package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;


public class ExtensionLis {

  private String url;

  private Coding valueCoding;

  public ExtensionLis(String url, Coding valueCoding) {
    this.url = url;
    this.valueCoding = valueCoding;
  }

  public ExtensionLis() {
  }

  public ExtensionLis(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Coding getValueCoding() {
    return valueCoding;
  }

  public void setValueCoding(Coding valueCoding) {
    this.valueCoding = valueCoding;
  }
}
