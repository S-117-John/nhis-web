package com.zebone.nhis.webservice.vo.wechatvo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "res")
@XmlAccessorType(XmlAccessType.FIELD)
public class HisMerchantSumResVO {
	
   /**
    * 结果代码 0- 成功, 1-失败
    */
   @XmlElement(name="resultCode")
   private String resultCode;
   
    /**
     * 结果描述 成功为空，失败时返回描述
     */
    @XmlElement(name="resultMsg")
    private String resultMsg;
    
    /**
     * 每日账单业务对象
     */
    @XmlElement(name="sumItems")
    private List<SumItemsVO> sumItems;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<SumItemsVO> getSumItems() {
		return sumItems;
	}

	public void setSumItems(List<SumItemsVO> sumItems) {
		this.sumItems = sumItems;
	}

       
}
