package com.zebone.nhis.ma.pub.platform.pskq.factory.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailsOutpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementDetailOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementDetailsOutpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementMasterOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementOutpatSend;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.EventElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.OrganizationElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ReceiverElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SenderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ServiceElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;


public class SettlementOutpatImpl implements Message {

    public void SettlemenOutpatImpl(){}

    public SettlementOutpat settlementOutpat;

    public SettlementOutpat getSettlementOutpat() {
        return settlementOutpat;
    }

    public void setSettlementOutpat(SettlementOutpat settlementOutpat) {
        this.settlementOutpat = settlementOutpat;
    }


    public SettlementOutpatImpl(SettlementOutpat settlementOutpat) {
        this.settlementOutpat = settlementOutpat;
    }

    @Override
    public RequestBody getRequestBody(Map<String, Object> param) throws IllegalAccessException {
        String serviceCode = "";
        String serviceName = "";
        String eventCode = "";
        String eventName = "";
        if(param.containsKey("Control")&&param.get("Control")!=null&& !StringUtils.isEmpty(param.get("Control").toString())){
            if("CR".equalsIgnoreCase(param.get("Control").toString())){
                //??????
                serviceCode = "S1501";
                serviceName = "???????????????????????????";
                eventCode = "E150102";
                eventName = "???????????????";
            }else if("OK".equalsIgnoreCase(param.get("Control").toString())){
                //??????
                serviceCode = "S1501";
                serviceName = "???????????????????????????";
                eventCode = "E150101";
                eventName = "???????????????";
            }
        }

        ServiceElement serviceElement  = new ServiceElement(serviceCode,serviceName);
        EventElement eventElement = new EventElement(eventCode,eventName);
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS","????????????????????????"),
                new SoftwareProviderElement("Zebone","????????????????????????????????????????????????"),
                new OrganizationElement("10","????????????????????????????????????????????????")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB","????????????"),
                new SoftwareProviderElement("Caradigm","???????????????????????????????????????"),
                new OrganizationElement("10","????????????????????????????????????????????????")
        );

        //???????????????
        SettlementMasterOutpat settlementMasterOutpat = new SettlementMasterOutpat();
        if(settlementOutpat.getSettlementMasterOutpat() != null || settlementOutpat.getSettlementMasterOutpat().size() >  0)  {
        	settlementMasterOutpat = settlementOutpat.getSettlementMasterOutpat().get(0);
        }
        List<DataElement> SettlementMasterOutpat = MessageBodyUtil.dataElementsFactory(settlementMasterOutpat);
        //????????????
        SettlementDetailOutpat detailOutpat = new SettlementDetailOutpat();
        if(settlementOutpat.getSettlementDetailOutpat() != null && settlementOutpat.getSettlementDetailOutpat().size() > 0) {
        	detailOutpat = settlementOutpat.getSettlementDetailOutpat().get(0);
        }

        List<DataElement> SettlementDetailOutpat = MessageBodyUtil.dataElementsFactory(detailOutpat);
        //??????????????????
        List<CostDetailOutpat> CostDetailOutpatlist =  settlementOutpat.getCostDetailOutpats();

        List<CostDetailsOutpatSend> CostDetailsOutpatSendList = new ArrayList<>();

        for (int i=0;i <CostDetailOutpatlist.size();i++){
            List<DataElement> CostDetailOutpatList = MessageBodyUtil.dataElementsFactory(CostDetailOutpatlist.get(i));
            CostDetailsOutpatSend costDetailsOutpatSend = new CostDetailsOutpatSend();
            costDetailsOutpatSend.setCostDetailOutpats(CostDetailOutpatList);
            CostDetailsOutpatSendList.add(costDetailsOutpatSend);
        }

        SettlementDetailsOutpatSend settlementDetailsOutpatSend = new SettlementDetailsOutpatSend();
        settlementDetailsOutpatSend.setSettlementDetailOutpat(SettlementDetailOutpat);
        settlementDetailsOutpatSend.setCostDetailsOutpatTest(CostDetailsOutpatSendList);

        List<SettlementDetailsOutpatSend> settlementDetailsOutpatSendList = new ArrayList<>();
        settlementDetailsOutpatSendList.add(settlementDetailsOutpatSend);

        SettlementOutpatSend settlementOutpatSend = new SettlementOutpatSend();
        settlementOutpatSend.setSettlementMasterOutpat(SettlementMasterOutpat);
        settlementOutpatSend.setSettlementDetailsOutpat(settlementDetailsOutpatSendList);


        Map<String,Object> map = new HashMap<>();
        map.put("SETTLEMENT_OUTPAT",settlementOutpatSend);
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
