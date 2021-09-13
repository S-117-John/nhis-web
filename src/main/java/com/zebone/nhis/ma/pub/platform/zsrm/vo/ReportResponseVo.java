package com.zebone.nhis.ma.pub.platform.zsrm.vo;


import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;

import java.util.LinkedHashMap;
import java.util.List;

public class ReportResponseVo {
    private List<LinkedHashMap<String,Object>> extensions;

    public List<LinkedHashMap<String,Object>> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<LinkedHashMap<String,Object>> extensions) {
        this.extensions = extensions;
    }
}
