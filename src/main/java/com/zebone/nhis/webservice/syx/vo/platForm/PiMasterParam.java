package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import com.zebone.nhis.common.module.pi.PiAddress;
import com.zebone.nhis.common.module.pi.PiAllergic;
import com.zebone.nhis.common.module.pi.PiCard;
import com.zebone.nhis.common.module.pi.PiDise;
import com.zebone.nhis.common.module.pi.PiFamily;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;

public class PiMasterParam {
	/** 基本信息 */
	private PiMaster master;
	
	/** 医保计划 */
	private List<PiInsurance> insuranceList;
	
	/** 家庭关系*/
	private List<PiFamily> familyList;
	
	/** 患者地址*/
	private List<PiAddress> addressList;
	
	/** 过敏史 */
	private List<PiAllergic> allergicList;
	
	/** 疾病史*/
	private List<PiDise> diseList;
	
	/** 患者卡信息*/
	private List<PiCard> cardList;
	
	/** 住院次数*/
	private int ipTimes;

	public PiMaster getMaster() {
		return master;
	}

	public void setMaster(PiMaster master) {
		this.master = master;
	}

	public List<PiInsurance> getInsuranceList() {
		return insuranceList;
	}

	public void setInsuranceList(List<PiInsurance> insuranceList) {
		this.insuranceList = insuranceList;
	}

	public List<PiFamily> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<PiFamily> familyList) {
		this.familyList = familyList;
	}

	public List<PiAddress> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<PiAddress> addressList) {
		this.addressList = addressList;
	}

	public List<PiAllergic> getAllergicList() {
		return allergicList;
	}

	public void setAllergicList(List<PiAllergic> allergicList) {
		this.allergicList = allergicList;
	}

	public List<PiDise> getDiseList() {
		return diseList;
	}

	public void setDiseList(List<PiDise> diseList) {
		this.diseList = diseList;
	}

	public List<PiCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<PiCard> cardList) {
		this.cardList = cardList;
	}

	public int getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(int ipTimes) {
		this.ipTimes = ipTimes;
	}
	
	
}
