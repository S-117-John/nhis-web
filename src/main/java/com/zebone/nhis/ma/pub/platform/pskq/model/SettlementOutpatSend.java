package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

public class SettlementOutpatSend {

    @JSONField(name = "SETTLEMENT_MASTER_OUTPAT",ordinal = 1)
    private List<DataElement> settlementMasterOutpat;

    @JSONField(name = "SETTLEMENT_DETAILS_OUTPAT",ordinal = 2)
    private List<SettlementDetailsOutpatSend> settlementDetailsOutpat;

    public List<DataElement> getSettlementMasterOutpat() {
        return settlementMasterOutpat;
    }

    public void setSettlementMasterOutpat(List<DataElement> settlementMasterOutpat) {
        this.settlementMasterOutpat = settlementMasterOutpat;
    }

    public List<SettlementDetailsOutpatSend> getSettlementDetailsOutpat() {
        return settlementDetailsOutpat;
    }

    public void setSettlementDetailsOutpat(List<SettlementDetailsOutpatSend> settlementDetailsOutpat) {
        this.settlementDetailsOutpat = settlementDetailsOutpat;
    }
}
