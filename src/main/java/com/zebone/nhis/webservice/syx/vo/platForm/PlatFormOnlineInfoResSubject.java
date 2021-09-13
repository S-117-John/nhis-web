package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybTrialstVo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PlatFormOnlineInfoResSubject {

	@XmlElementWrapper(name = "res")
	@XmlElement(name = "item")
	private List<OnlineBlopdtVo> item;

	public List<OnlineBlopdtVo> getItem() {
		return item;
	}

	public void setItem(List<OnlineBlopdtVo> item) {
		this.item = item;
	}

	@XmlElement(name = "settlementvo")
	private OnlineSettlementVo settlementvo;

	public OnlineSettlementVo getSettlementvo() {
		return settlementvo;
	}

	public void setSettlementvo(OnlineSettlementVo settlementvo) {
		this.settlementvo = settlementvo;
	}
	@XmlElement(name = "trialettlementvo")
	private InsGzybTrialstVo trialettlementvo;

	public InsGzybTrialstVo getTrialettlementvo() {
		return trialettlementvo;
	}

	public void setTrialettlementvo(InsGzybTrialstVo trialettlementvo) {
		this.trialettlementvo = trialettlementvo;
	}

	@XmlElement(name = "stVoTemp")
	private BlSettle stVoTemp;

	public BlSettle getStVoTemp() {
		return stVoTemp;
	}

	public void setStVoTemp(BlSettle stVoTemp) {
		this.stVoTemp = stVoTemp;
	}

}
