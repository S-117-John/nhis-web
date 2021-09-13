package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.Date;
import java.util.List;

/**
 * 收费项目
 */
public class ChargeItem  extends Outcome {
    private String status;
    private CodeableConcept code;
    private ValuePeriod occurrencePeriod;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SS")
    private Date enteredDate;
    private List<BdExtension> extension;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CodeableConcept getCode() {
        return code;
    }

    public void setCode(CodeableConcept code) {
        this.code = code;
    }

    public ValuePeriod getOccurrencePeriod() {
        return occurrencePeriod;
    }

    public void setOccurrencePeriod(ValuePeriod occurrencePeriod) {
        this.occurrencePeriod = occurrencePeriod;
    }

    public Date getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(Date enteredDate) {
        this.enteredDate = enteredDate;
    }

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }
}
