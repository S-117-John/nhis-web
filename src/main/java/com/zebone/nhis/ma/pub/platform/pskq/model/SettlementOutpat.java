package com.zebone.nhis.ma.pub.platform.pskq.model;

import java.util.List;

public class SettlementOutpat {

    private List<SettlementMasterOutpat> settlementMasterOutpat;
    private List<SettlementDetailOutpat> settlementDetailOutpat;
    private List<CostDetailOutpat> costDetailOutpats;

    public List<SettlementMasterOutpat> getSettlementMasterOutpat() {
		return settlementMasterOutpat;
	}

	public void setSettlementMasterOutpat(List<SettlementMasterOutpat> settlementMasterOutpat) {
		this.settlementMasterOutpat = settlementMasterOutpat;
	}

	public List<SettlementDetailOutpat> getSettlementDetailOutpat() {
		return settlementDetailOutpat;
	}

	public void setSettlementDetailOutpat(List<SettlementDetailOutpat> settlementDetailOutpat) {
		this.settlementDetailOutpat = settlementDetailOutpat;
	}

	public List<CostDetailOutpat> getCostDetailOutpats() {
        return costDetailOutpats;
    }

    public void setCostDetailOutpats(List<CostDetailOutpat> costDetailOutpats) {
        this.costDetailOutpats = costDetailOutpats;
    }
}
