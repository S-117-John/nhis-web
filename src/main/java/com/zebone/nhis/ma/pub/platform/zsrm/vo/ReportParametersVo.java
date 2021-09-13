package com.zebone.nhis.ma.pub.platform.zsrm.vo;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ReportParametersVo extends PhResource {
    private Date occurrenceDateTime;
    private List<Parameter> parameter=new LinkedList<>();

    @Override
    public List<Parameter> getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public Date getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    public void setOccurrenceDateTime(Date occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }
}
