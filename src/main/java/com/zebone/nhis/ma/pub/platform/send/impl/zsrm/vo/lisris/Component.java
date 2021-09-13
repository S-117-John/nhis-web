package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.List;


public class Component {

  private Code code;

  private List<Coding> valueCodeableConcept;

  private ValueQuantity valueQuantity;

  private TextElement referenceRange;

  private ValueQuantity valueString;

  public ValueQuantity getValueString() {
    return valueString;
  }

  public void setValueString(ValueQuantity valueString) {
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

  public List<Coding> getValueCodeableConcept() {
    return valueCodeableConcept;
  }

  public void setValueCodeableConcept(List<Coding> valueCodeableConcept) {
    this.valueCodeableConcept = valueCodeableConcept;
  }
}
