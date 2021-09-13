package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.CostDetailInpat;
import com.zebone.nhis.webservice.pskq.model.CostDetailOutpat;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.service.CostDetailInpatService;
import com.zebone.nhis.webservice.service.PskqPubForWsService;

public class CostDetailInpatServiceImpl implements CostDetailInpatService {

    /**
     *  COST_DETAIL_INPAT  住院划价记账
     * @param param
     * @param listener
     */
    @Override
    public void save(String param, ResultListener listener) {

        Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param,RequestBody.class);
        try {
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
            List<Map<String, Object>> list = (List<Map<String, Object>>) requestBody.getMessage().get("COST_DETAIL_INPAT_LIST");//
            for (Map<String, Object> map : list) {
            	 List<DataElement> lists =(List<DataElement>) map.get("COST_DETAIL_INPAT");
                 CostDetailInpat costDetailInpat = (CostDetailInpat) MessageFactory.deserialization(lists, new CostDetailInpat());
                 //处理手术计费方法
                 String ret = pskqPubForWsService.disposeIpBlMessage(costDetailInpat);
                 if("succ".equals(ret)){
                     listener.success("success");
                 }else {
                     listener.error(ret);
                     break;
                 }
			}
            
            //List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("COST_DETAIL_INPAT");//
            //手术计费信息
            
        }catch (Exception e){
           listener.exception(e.getMessage());
        }

    }



    @Override
    public void cancel(String param, ResultListener listener) {
       /* Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param,RequestBody.class);
        try {
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            String reqID = requestBody.getId();
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
            Map<String,Object> mapOutpat = (Map<String,Object>) requestBody.getMessage().get("SETTLEMENT_OUTPAT");
            List<DataElement> masterList = (List<DataElement>)mapOutpat.get("SETTLEMENT_MASTER_OUTPAT");
            List<Map<String,Object>> mapDetailsList = (List<Map<String,Object>>)mapOutpat.get("SETTLEMENT_DETAILS_OUTPAT");
            List<CostDetailOutpat>  mapCostList = new ArrayList<>();
            //获取手术计费信息
            for (int i = 0; i < mapDetailsList.size(); i++) {
                List<DataElement> settlementDetail = (List<DataElement>)mapDetailsList.get(i).get("SETTLEMENT_DETAIL_OUTPAT");
                List<Map<String,Object>> costDetailsList = (List<Map<String,Object>>)mapDetailsList.get(i).get("COST_DETAILS_OUTPAT");
                for (int j = 0; j < costDetailsList.size(); j++) {
                    List<DataElement> costDetail = (List<DataElement>)costDetailsList.get(j).get("COST_DETAIL_OUTPAT");
                    CostDetail cost = (CostDetail) MessageFactory.deserialization(costDetail, new CostDetail());
                    mapCostList.add(cost);
                }
            }
            //处理手术计费方法
            String ret = pskqPubForWsService.disposeOpReBlMessage(mapCostList);
            String ret = pskqPubForWsService.disposeIpReBlMessage(costDetailOutpat);
            if("succ".equals(ret)){
                listener.success("success");

            }else {
                listener.error(ret);
            }
        }catch (Exception e){
            listener.exception(e.getMessage());
        }*/
    }

    /**
     * 住院计费
     * @param
     * @param listener
     */
    @Override
    public void billing(String param, ResultListener listener) throws IllegalAccessException {
        Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param,RequestBody.class);
        List<Map<String, Object>> list = (List<Map<String, Object>>) requestBody.getMessage().get("COST_DETAIL_INPAT_LIST");
        for (Map<String, Object> map : list) {
            List<DataElement> lists =(List<DataElement>) map.get("COST_DETAIL_INPAT");
            CostDetailInpat costDetailInpat = (CostDetailInpat) MessageFactory.deserialization(lists, new CostDetailInpat());
            String pkPv = costDetailInpat.getEncounterId();
            PvEncounter pvEncounter = new PvEncounter();
            BlIpDt blIpDt = new BlIpDt();
            blIpDt.setPkOrg(pvEncounter.getPkOrg());
            blIpDt.setPkPi(pvEncounter.getPkPi());
            blIpDt.setPkPv(pvEncounter.getPkPv());

        }


    }
}
