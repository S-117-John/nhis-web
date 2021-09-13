package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Location;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Specimen;

import java.util.Date;
import java.util.List;


public class Collection  {

  private Specimen collector;

  @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
  private Date collectedDateTime;

  public Collection() {
  }

  public Collection(Specimen collector, Date collectedDateTime) {
    this.collector = collector;
    this.collectedDateTime = collectedDateTime;
  }

  public Specimen getCollector() {
    return collector;
  }

  public void setCollector(Specimen collector) {
    this.collector = collector;
  }

  public Date getCollectedDateTime() {
    return collectedDateTime;
  }

  public void setCollectedDateTime(Date collectedDateTime) {
    this.collectedDateTime = collectedDateTime;
  }
}
