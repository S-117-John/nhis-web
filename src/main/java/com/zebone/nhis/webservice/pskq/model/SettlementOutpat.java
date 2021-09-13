package com.zebone.nhis.webservice.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class SettlementOutpat {

    @JSONField(name = "SETTLEMENT_MASTER_OUTPAT",ordinal = 1)
    private SettlementMasterOutpat settlementMasterOutpat;

    @JSONField(name = "SETTLEMENT_DETAILS_OUTPAT",ordinal = 2)
    private List<SettlementDetailsOutpat> settlementDetailsOutpat;

    public SettlementMasterOutpat getSettlementMasterOutpat() {
        return settlementMasterOutpat;
    }

    public void setSettlementMasterOutpat(SettlementMasterOutpat settlementMasterOutpat) {
        this.settlementMasterOutpat = settlementMasterOutpat;
    }

    public List<SettlementDetailsOutpat> getSettlementDetailsOutpat() {
        return settlementDetailsOutpat;
    }

    public void setSettlementDetailsOutpat(List<SettlementDetailsOutpat> settlementDetailsOutpat) {
        this.settlementDetailsOutpat = settlementDetailsOutpat;
    }
}
