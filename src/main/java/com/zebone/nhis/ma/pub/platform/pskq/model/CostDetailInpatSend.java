package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

public class CostDetailInpatSend {
    @JSONField(name = "COST_DETAIL_INPAT")
    private List<DataElement> settlementDetailOutpat;

	public List<DataElement> getSettlementDetailOutpat() {
		return settlementDetailOutpat;
	}

	public void setSettlementDetailOutpat(List<DataElement> settlementDetailOutpat) {
		this.settlementDetailOutpat = settlementDetailOutpat;
	}

}
