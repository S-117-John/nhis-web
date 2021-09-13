package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Specimen;

import java.util.Date;


public class CollectionRis {

  private SpecimenRis collector;

  @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
  private Date collectedDateTime;

  public CollectionRis() {
  }

  public CollectionRis(SpecimenRis collector, Date collectedDateTime) {
    this.collector = collector;
    this.collectedDateTime = collectedDateTime;
  }

  public SpecimenRis getCollector() {
    return collector;
  }

  public void setCollector(SpecimenRis collector) {
    this.collector = collector;
  }

  public Date getCollectedDateTime() {
    return collectedDateTime;
  }

  public void setCollectedDateTime(Date collectedDateTime) {
    this.collectedDateTime = collectedDateTime;
  }
}
