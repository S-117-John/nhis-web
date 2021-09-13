package com.zebone.nhis.webservice.syx.support;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class XmlDataAdapter extends XmlAdapter<Object, Object> {

    @Override
    public Object unmarshal(Object o) throws Exception {
        return o;
    }

    @Override
    public Object marshal(Object o) throws Exception {
        if (o == null ) {
            o = "";
        }
        return o;
    }
}
