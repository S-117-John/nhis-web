package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.scm;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Parameter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class QueCallResource extends PhResource {

    /** 基类定义了parameter，但是我们用的是queCallVos，所以这里忽略他*/
    @Deprecated
    @JsonIgnore
    private List<Parameter> parameter;

    @JsonProperty("parameter")
    private List<QueCallVo> queCallVos;

    @JsonDeserialize(using = ZsphJsonDateDeserializer.class)
    private Date occurrenceDateTime;

    @Override
    public List<Parameter> getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(List<Parameter> parameter) {
        this.parameter = parameter;
    }

    public List<QueCallVo> getQueCallVos() {
        return queCallVos;
    }

    public void setQueCallVos(List<QueCallVo> queCallVos) {
        this.queCallVos = queCallVos;
    }

    public Date getOccurrenceDateTime() {
        return occurrenceDateTime;
    }

    public void setOccurrenceDateTime(Date occurrenceDateTime) {
        this.occurrenceDateTime = occurrenceDateTime;
    }

}
