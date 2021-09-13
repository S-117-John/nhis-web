package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;


public class ValueQuantity {

  private String value;

  private String unit;

  private String comparator;

  public String getComparator() {
    return comparator;
  }

  public void setComparator(String comparator) {
    this.comparator = comparator;
  }

  public ValueQuantity(String value, String unit) {
    this.value = value;
    this.unit = unit;
  }

  public ValueQuantity() {
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }
}
