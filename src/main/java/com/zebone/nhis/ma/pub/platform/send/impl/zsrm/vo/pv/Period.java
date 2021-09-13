package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Period {
    public Period(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date start;
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date end;

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getStart() {
        return start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getEnd() {
        return end;
    }

}