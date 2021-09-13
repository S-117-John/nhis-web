package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;

import java.util.Date;
import java.util.List;

public class OpApply extends PhResource {

    private String status;
    private TextElement statusReason;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private Date performedDateTime;//手术排班日期
    private ValuePeriod performedPeriod;//手术时间

    public Date getPerformedDateTime() {
        return performedDateTime;
    }

    public void setPerformedDateTime(Date performedDateTime) {
        this.performedDateTime = performedDateTime;
    }

    public ValuePeriod getPerformedPeriod() {
        return performedPeriod;
    }

    public void setPerformedPeriod(ValuePeriod performedPeriod) {
        this.performedPeriod = performedPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TextElement getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(TextElement statusReason) {
        this.statusReason = statusReason;
    }
}
