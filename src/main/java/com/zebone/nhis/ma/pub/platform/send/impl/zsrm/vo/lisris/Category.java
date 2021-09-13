package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;

import java.util.List;


public class Category {

  private List<Coding> coding;

  public List<Coding> getCoding() {
    return coding;
  }

  public void setCoding(List<Coding> coding) {
    this.coding = coding;
  }

  public Category() {
  }

  public Category(List<Coding> coding) {
    this.coding = coding;
  }
}
