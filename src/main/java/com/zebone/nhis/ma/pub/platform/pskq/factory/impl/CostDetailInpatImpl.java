package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpatList;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailsOutpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CostDetailInpatImpl implements Message {

    private CostDetailInpatList costDetailInpatList;
   
	public CostDetailInpatList getCostDetailInpatList() {
		return costDetailInpatList;
	}

	public void setCostDetailInpatList(CostDetailInpatList costDetailInpatList) {
		this.costDetailInpatList = costDetailInpatList;
	}

	public CostDetailInpatImpl(CostDetailInpatList costDetailInpatList) {
        this.costDetailInpatList = costDetailInpatList;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "S1503";
        String serviceName = "住院结算新增服务";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("eventCode")&&param.get("eventCode")!=null&& !StringUtils.isEmpty(param.get("eventCode").toString())){
            eventCode = (String) param.get("eventCode");
            eventName = (String) param.get("eventName");
        }
        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","医院信息管理系统"),
                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB","集成平台"),
                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
        );
        List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(costDetailInpatList);
        //费用明细信息
        List<CostDetailInpat> costDetailInpatListS =  costDetailInpatList.getCostDetailInpats();
        List<CostDetailInpatSend> costDetailInpatSendList = new ArrayList<>();
        for (int i=0;i <costDetailInpatListS.size();i++){
            List<DataElement> CostDetailOutpatList = MessageBodyUtil.dataElementsFactory(costDetailInpatListS.get(i));
            CostDetailInpatSend costDetailInpatSend = new CostDetailInpatSend();
            costDetailInpatSend.setSettlementDetailOutpat(CostDetailOutpatList);
            costDetailInpatSendList.add(costDetailInpatSend);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("COST_DETAIL_INPAT_LIST",costDetailInpatSendList);
        RequestBody requestBody = new RequestBody(
                serviceElement,
                eventElement,
                senderElement,
                receiverElement,
                map
        );
        return requestBody;
    }
}
