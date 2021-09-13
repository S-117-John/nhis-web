package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

public class SettlementDetailsOutpatSend {
    @JSONField(name = "SETTLEMENT_DETAIL_OUTPAT",ordinal = 1)
    private List<DataElement> settlementDetailOutpat;

    @JSONField(name = "COST_DETAILS_OUTPAT",ordinal = 2)
    private List<CostDetailsOutpatSend> costDetailsOutpatTest;

    public List<DataElement> getSettlementDetailOutpat() {
        return settlementDetailOutpat;
    }

    public void setSettlementDetailOutpat(List<DataElement> settlementDetailOutpat) {
        this.settlementDetailOutpat = settlementDetailOutpat;
    }

    public List<CostDetailsOutpatSend> getCostDetailsOutpatTest() {
        return costDetailsOutpatTest;
    }

    public void setCostDetailsOutpatTest(List<CostDetailsOutpatSend> costDetailsOutpatTest) {
        this.costDetailsOutpatTest = costDetailsOutpatTest;
    }
}
