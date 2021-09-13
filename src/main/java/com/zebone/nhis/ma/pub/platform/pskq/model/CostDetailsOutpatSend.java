package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

public class CostDetailsOutpatSend {
    @JSONField(name = "COST_DETAIL_OUTPAT")
    private List<DataElement> costDetailOutpats;

    public List<DataElement> getCostDetailOutpats() {
        return costDetailOutpats;
    }

    public void setCostDetailOutpats(List<DataElement> costDetailOutpats) {
        this.costDetailOutpats = costDetailOutpats;
    }
}
