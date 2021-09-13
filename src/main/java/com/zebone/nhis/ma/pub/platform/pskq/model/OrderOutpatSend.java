package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElementmini;

import java.util.List;

public class OrderOutpatSend {
    @JSONField(name = "ORDER_OUTPAT")
    private List<DataElementmini> settlementDetailOutpat;

	public List<DataElementmini> getSettlementDetailOutpat() {
		return settlementDetailOutpat;
	}

	public void setSettlementDetailOutpat(List<DataElementmini> settlementDetailOutpat) {
		this.settlementDetailOutpat = settlementDetailOutpat;
	}

}
