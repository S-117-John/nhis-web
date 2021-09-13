package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class ValuePeriod {

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date start;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date end;

    public ValuePeriod(){}

    public ValuePeriod(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }
}