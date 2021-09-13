package com.zebone.nhis.webservice.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

public class SettlementDetailsOutpat {


    @JSONField(name = "SETTLEMENT_DETAIL_OUTPAT",ordinal = 1)
    private SettlementDetailOutpat settlementDetailOutpat;

    @JSONField(name = "COST_DETAILS_OUTPAT",ordinal = 2)
    private CostDetailsOutpat costDetailsOutpat;

    public SettlementDetailOutpat getSettlementDetailOutpat() {
        return settlementDetailOutpat;
    }

    public void setSettlementDetailOutpat(SettlementDetailOutpat settlementDetailOutpat) {
        this.settlementDetailOutpat = settlementDetailOutpat;
    }

    public CostDetailsOutpat getCostDetailsOutpat() {
        return costDetailsOutpat;
    }

    public void setCostDetailsOutpat(CostDetailsOutpat costDetailsOutpat) {
        this.costDetailsOutpat = costDetailsOutpat;
    }
}
