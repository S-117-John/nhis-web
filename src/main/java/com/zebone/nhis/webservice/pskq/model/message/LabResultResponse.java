package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class LabResultResponse {

    @JSONField(name = "LAB_MASTER")
    private List<DataElement> labMaster;

    public List<DataElement> getLabMaster() {
        return labMaster;
    }

    public void setLabMaster(List<DataElement> labMaster) {
        this.labMaster = labMaster;
    }
}
