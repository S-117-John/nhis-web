package com.zebone.nhis.ma.pub.platform.zsrm.model.message;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.BusinessBase;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;

import java.io.Serializable;
import java.util.List;

public class RequestBody extends BusinessBase implements Serializable {
    //查询时接受参数
    private List<Parameter> parameter;

    public List<Parameter> getParameter() {
        return parameter;
    }

    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }
}
