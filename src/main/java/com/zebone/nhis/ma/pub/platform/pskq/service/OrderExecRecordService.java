package com.zebone.nhis.ma.pub.platform.pskq.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderExecRecord;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.OrganizationElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ReceiverElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SenderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.ma.pub.platform.pskq.repository.CnOrderRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PiMasterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvIpRepository;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqMesUtils;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderExecRecordService {

    public void check(List<Map<String,Object>> orderList, ResultListener listener){

       try{
           HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
           if(httpRestTemplate==null){
               listener.error("没有获取到HttpRestTemplate对象","","");
               return;
           }
           if(orderList.size()==0){
               listener.error("没有传入需要核对的医嘱信息","","");
               return;
           }

           for (Map<String,Object> cnOrdMap : orderList) {
               OrderExecRecord orderExecRecord = new OrderExecRecord();
               CnOrder cnOrder = CnOrderRepository.getOne(MapUtils.getString(cnOrdMap,"pkCnord",""));
               PiMaster piMaster = PiMasterRepository.getOne(cnOrder.getPkPi());
               PvEncounter pvEncounter = PvEncounterRepository.getOne(cnOrder.getPkPv());
               PvIp pvip=PvIpRepository.getOne(pvEncounter.getPkPv());
               orderExecRecord.setEmpiId(piMaster.getMpi());
               orderExecRecord.setEncounterTypeCode(pvEncounter.getEuPvtype());
               orderExecRecord.setEncounterTypeName("住院");
               orderExecRecord.setPkPatient(piMaster.getCodePi());
               orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+pvEncounter.getCodePv());
               orderExecRecord.setOrderId(cnOrder.getPkCnord());
               orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+pvEncounter.getCodePv());
               orderExecRecord.setVisitNo(String.valueOf(pvip.getIpTimes()));
               orderExecRecord.setPlacerOrderNo(cnOrder.getOrdsn()!=null?cnOrder.getOrdsn().toString():"");
               orderExecRecord.setPatientName(pvEncounter.getNamePi());
               String sql = "select * from EX_ORDER_OCC where PK_CNORD  = ?";
               List<ExOrderOcc> exOrderOccList = DataBaseHelper.queryForList(sql,ExOrderOcc.class,cnOrder.getPkCnord());
               ExOrderOcc exOrderOcc = exOrderOccList.stream().findFirst().orElse(new ExOrderOcc());
               orderExecRecord.setExecId(exOrderOcc.getPkExocc());
               sql = "select * from EX_ST_OCC where PK_CNORD = ?";
               List<ExStOcc> exStOccList = DataBaseHelper.queryForList(sql,ExStOcc.class,cnOrder.getPkCnord());
               ExStOcc exStOcc = exStOccList.stream().findFirst().get();
               if(exStOcc!=null){
                   String resulEx = exStOcc.getResult();
                   if(resulEx.contains("+")){
                       orderExecRecord.setSkinResult("2");
                   }else {
                       orderExecRecord.setSkinResult("1");
                   }
               }


               String sexCode=null;
               String sexName=null;
               if ("02".equals(piMaster.getDtSex())) {
            	   sexCode="1";sexName="男";
               }else if ("03".equals(piMaster.getDtSex())) {
            	   sexCode="2";sexName="女";
               }else if ("04".equals(piMaster.getDtSex())) {
            	   sexCode="0";sexName="不明";
               }
               orderExecRecord.setGenderCode(sexCode);
               orderExecRecord.setGenderName(sexName);
               orderExecRecord.setDateOfBirth(DateUtils.formatDate(piMaster.getBirthDate(),"yyyy-MM-dd HH:mm:ss"));
               orderExecRecord.setAgeYear(pvEncounter.getAgePv());
               orderExecRecord.setVisitDateTime(DateUtils.formatDate(pvEncounter.getDateBegin(),"yyyy-MM-dd HH:mm:ss"));
               orderExecRecord.setConfirmDateTime(DateUtils.formatDate(cnOrder.getDateChk(),"yyyy-MM-dd HH:mm:ss"));
               orderExecRecord.setConfirmNurseId(cnOrder.getPkEmpChk());
               orderExecRecord.setConfirmNurseName(cnOrder.getNameEmpChk());
               RequestBody requestBody = new RequestBody.Builder()
                       .service("S0036","医嘱信息更新服务")
                       .event("E003603","确认住院医嘱")
                       .sender()
                       .receiver()
                       .build();
               Map<String,Object> map = new HashMap<>(16);
               List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
               map.put("ORDER_EXEC_RECORD",dataElementList);
               requestBody.setMessage(map);
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
               requestBody.setSender(senderElement);
               requestBody.setReceiver(receiverElement);
               String requestString = JSON.toJSONString(requestBody);
               String responseString = httpRestTemplate.postForString(requestString);
               
               ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
               //消息成功
               if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                   listener.success(requestString, responseString);
               } else {
                   listener.error(paramVo.getAck().get("ackDetail").toString(), requestString, responseString);
               }
           }
           //listener.success("发送医嘱核对信息成功","");
       }catch (Exception e){
           //listener.error("发送医嘱核对信息失败","","");
       }

    }
    public void checkLisOrRis(List<Map<String,Object>> orderList, ResultListener listener){

        try{
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            if(httpRestTemplate==null){
                listener.error("没有获取到HttpRestTemplate对象","","");
                return;
            }
            if(orderList.size()==0){
                listener.error("没有传入需要核对的医嘱信息","","");
                return;
            }
            List<Map<String,Object>> qryLisList = Lists.newArrayList();
            List<Map<String,Object>> qryPascList = Lists.newArrayList();
            for (int i = 0; i < orderList.size(); i++) {
                if ("02".equals(orderList.get(i).get("codeOrdtype").toString().substring(0, 2)))
                	qryPascList.add(orderList.get(i));
                if ("03".equals(orderList.get(i).get("codeOrdtype").toString().substring(0, 2)))
                	qryLisList.add(orderList.get(i));
            }
            //发送检验核对
            if(qryLisList.size()!=0) {
            	for (Map<String,Object> cnOrdMap : qryLisList) {
                    OrderExecRecord orderExecRecord = new OrderExecRecord();
                    CnOrder cnOrder = CnOrderRepository.getOne(MapUtils.getString(cnOrdMap,"pkCnord",""));
                    PiMaster piMaster = PiMasterRepository.getOne(cnOrder.getPkPi());
                    PvEncounter pvEncounter = PvEncounterRepository.getOne(cnOrder.getPkPv());
                    orderExecRecord.setEmpiId(piMaster.getMpi());
                    orderExecRecord.setPkPatient(piMaster.getCodePi());
                    orderExecRecord.setEncounterId(pvEncounter.getCodePv());
                    orderExecRecord.setOrderId(cnOrder.getPkCnord());
                    orderExecRecord.setPlacerOrderNo(cnOrder.getOrdsn()!=null?cnOrder.getOrdsn().toString():"");
                    orderExecRecord.setConfirmDateTime(DateUtils.formatDate(cnOrder.getDateChk(),"yyyy-MM-dd HH:mm:ss"));
                    orderExecRecord.setConfirmNurseId(cnOrder.getPkEmpChk());
                    orderExecRecord.setConfirmNurseName(cnOrder.getNameEmpChk());
                    RequestBody requestBody = new RequestBody.Builder()
                            .service("S0039","检验申请信息更新服务")
                            .event("E003902","确认住院检验申请")
                            .sender()
                            .receiver()
                            .build();
                    Map<String,Object> map = new HashMap<>(16);
                    List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
                    map.put("ORDER_EXEC_RECORD",dataElementList);
                    requestBody.setMessage(map);
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
                    requestBody.setSender(senderElement);
                    requestBody.setReceiver(receiverElement);
                    String requestString = JSON.toJSONString(requestBody);
                    String responseString = httpRestTemplate.postForString(requestString);
                    
                    ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
                    //消息成功
                    if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                        listener.success(requestString, responseString);
                    } else {
                        listener.error(paramVo.getAck().get("ackDetail").toString(), requestString, responseString);
                    }
                }
            }
            String serviceCode = "";
            String serviceName = "";
            String eventCode = "";
            String eventName = "";
            //发送检查核对
            if(qryPascList.size()!=0) {
            	for (Map<String,Object> cnOrdMap : qryPascList) {
                    OrderExecRecord orderExecRecord = new OrderExecRecord();
                    CnOrder cnOrder = CnOrderRepository.getOne(MapUtils.getString(cnOrdMap,"pkCnord",""));
                    PiMaster piMaster = PiMasterRepository.getOne(cnOrder.getPkPi());
                    PvEncounter pvEncounter = PvEncounterRepository.getOne(cnOrder.getPkPv());
                    orderExecRecord.setEmpiId(piMaster.getMpi());
                    orderExecRecord.setPkPatient(piMaster.getCodePi());
                    orderExecRecord.setEncounterId(pvEncounter.getCodePv());
                    orderExecRecord.setOrderId(cnOrder.getPkCnord());
                    orderExecRecord.setPlacerOrderNo(cnOrder.getOrdsn()!=null?cnOrder.getOrdsn().toString():"");
                    orderExecRecord.setConfirmDateTime(DateUtils.formatDate(cnOrder.getDateChk(),"yyyy-MM-dd HH:mm:ss"));
                    orderExecRecord.setConfirmNurseId(cnOrder.getPkEmpChk());
                    orderExecRecord.setConfirmNurseName(cnOrder.getNameEmpChk());
                    String ordType=cnOrdMap.get("codeOrdtype").toString();
                    serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004206";eventName = "更新放射申请";
                    if("0201".equals(ordType)||"0202".equals(ordType)||"0219".equals(ordType)){
                        serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004206";eventName = "更新放射申请";
                    }
                    if("0207".equals(ordType)||"0208".equals(ordType)){
                        serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004307";eventName = "更新超声申请";
                    }
                    if("0209".equals(ordType)){
                        serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004210";eventName = "更新电生理申请";
                    }
                    if("0213".equals(ordType)||"0217".equals(ordType)){
                        serviceCode = "S0042";serviceName = "检查申请信息更新服务";eventCode = "E004208";eventName = "更新内镜申请";
                    }
                    RequestBody requestBody = new RequestBody.Builder()
                            .service(serviceCode,serviceName)
                            .event(eventCode,eventName)
                            .sender()
                            .receiver()
                            .build();
                    Map<String,Object> map = new HashMap<>(16);
                    List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
                    map.put("ORDER_EXEC_RECORD",dataElementList);
                    requestBody.setMessage(map);
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
                    requestBody.setSender(senderElement);
                    requestBody.setReceiver(receiverElement);
                    String requestString = JSON.toJSONString(requestBody);
                    String responseString = httpRestTemplate.postForString(requestString);
                    
                    ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
                    //消息成功
                    if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                        listener.success(requestString, responseString);
                    } else {
                        listener.error(paramVo.getAck().get("ackDetail").toString(), requestString, responseString);
                    }
                }
            }
            
            //listener.success("发送医嘱核对信息成功","");
        }catch (Exception e){
            //listener.error("发送医嘱核对信息失败","","");
        }

     }
}
