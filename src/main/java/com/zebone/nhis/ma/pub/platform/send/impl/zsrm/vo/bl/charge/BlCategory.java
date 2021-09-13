package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;

import java.util.List;


public class BlCategory {

  private List<Coding> coding;

  public List<Coding> getCoding() {
    return coding;
  }

  public void setCoding(List<Coding> coding) {
    this.coding = coding;
  }

  public BlCategory() {
  }

  public BlCategory(List<Coding> coding) {
    this.coding = coding;
  }
}
