package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;


public class ComponentBact {

  private Code code;

  private Code valueCodeableConcept;

  private ValueQuantity valueQuantity;

  private TextElement referenceRange;

  private String valueString;

  private String valueBoolean;

  public String getValueBoolean() {
    return valueBoolean;
  }

  public void setValueBoolean(String valueBoolean) {
    this.valueBoolean = valueBoolean;
  }

  public String getValueString() {
    return valueString;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }

  public ValueQuantity getValueQuantity() {
    return valueQuantity;
  }

  public void setValueQuantity(ValueQuantity valueQuantity) {
    this.valueQuantity = valueQuantity;
  }

  public TextElement getReferenceRange() {
    return referenceRange;
  }

  public void setReferenceRange(TextElement referenceRange) {
    this.referenceRange = referenceRange;
  }

  public Code getValueCodeableConcept() {
    return valueCodeableConcept;
  }

  public void setValueCodeableConcept(Code valueCodeableConcept) {
    this.valueCodeableConcept = valueCodeableConcept;
  }
}
