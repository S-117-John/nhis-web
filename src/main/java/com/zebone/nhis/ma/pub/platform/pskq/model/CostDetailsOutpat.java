package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class CostDetailsOutpat {

    @JSONField(name = "COST_DETAIL_OUTPAT")
    private List<CostDetailOutpat> costDetailOutpats;

    public List<CostDetailOutpat> getCostDetailOutpats() {
        return costDetailOutpats;
    }

    public void setCostDetailOutpats(List<CostDetailOutpat> costDetailOutpats) {
        this.costDetailOutpats = costDetailOutpats;
    }
}
