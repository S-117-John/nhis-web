package com.zebone.nhis.webservice.zsrm.support;

import com.zebone.nhis.common.support.DateUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;

public class JaxbDateAdapter extends XmlAdapter<String, Date> {
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    @Override
    public Date unmarshal(String v) throws Exception {
        return v==null?null: DateUtils.parseDate(v, DATE_FORMAT);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return v==null?null:DateUtils.dateToStr(DATE_FORMAT,v);
    }
}
