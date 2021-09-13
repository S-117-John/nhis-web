package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;


import java.text.SimpleDateFormat;
import java.util.*;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.cn.ipdw.ExOpOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MethodLog;
import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.nhis.common.module.base.bd.res.BdResOpt;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.MessageLog;
import com.zebone.nhis.ma.pub.platform.pskq.repository.BdResOptRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.CnOrderRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PiMasterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.ma.pub.platform.pskq.repository.PvIpRepository;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendPatientMapper;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.pskq.dao.SysMsgRecDao;
import com.zebone.nhis.ma.pub.platform.pskq.factory.Message;
import com.zebone.nhis.ma.pub.platform.pskq.factory.MessageFactory;
import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailInpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.ExamApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.EncounterInpatient;
import com.zebone.nhis.ma.pub.platform.pskq.model.LabApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrdDiagInfo;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderExecRecord;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SurgeryApplicationOrder;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.OrganizationElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ReceiverElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.RequestBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SenderElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareNameElement;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.SoftwareProviderElement;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqDictionMapUtil;
import com.zebone.nhis.ma.pub.platform.pskq.utils.PskqMesUtils;
import com.zebone.nhis.ma.pub.platform.pskq.utils.RestTemplateUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.util.StringUtils;

/**
 * 医生医嘱开立信息
 */
@Service
public class PskqPlatFormSendCnHandler {

    @Autowired
    private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;

    @Autowired
    private PskqPlatFormSendPatientMapper pskqPlatFormSendPatientMapper;


    @Autowired
    private HttpRestTemplate httpRestTemplate;

    /**
     * 开立手术申请
     * E005001
     *
     * @param paramMap
     * @param listener
     */
    public void getOpApplyInfo(Map<String, Object> paramMap, ResultListener listener){

        String result = "";
        String responseBody="";
        SurgeryApplicationOrder opApplyInfo = null;
        try {

            if(paramMap== null || !paramMap.containsKey("pkCnList")){
                //1、查询codeApply -> 2010000007
                if(!paramMap.containsKey("codeApply")|| StringUtils.isEmpty(paramMap.get("codeApply"))){
                    listener.error("未获取到查询手术信息的医嘱参数！","","");
                    return;
                }else {
                     if(paramMap.containsKey("saveOpApply")){
                         paramMap.put("control","NW");
                         opApplyInfo = pskqPlatFormSendCnMapper.getOpApplyInfoById(paramMap);
                         opApplyInfo.setEncounterId(PskqMesUtils.pskqHisCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterId());
                         opApplyInfo.setPlacerOrderNo(PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+opApplyInfo.getPlacerOrderNo());
                         opApplyInfo.setCancelFlag("0");
                         opApplyInfo.setSurgeryApplyId(PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+opApplyInfo.getSurgeryApplyNo());

                     }else {
                         paramMap.put("control","CA");
                         opApplyInfo = MapUtils.mapToObject(paramMap,SurgeryApplicationOrder.class);
                         opApplyInfo.setCancelFlag("1");
                         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         Date date = new Date();
                         opApplyInfo.setCancelDateTime(simpleDateFormat.format(date));
                     }

                }
            }else {
                opApplyInfo = pskqPlatFormSendCnMapper.getOpApplyInfoById(paramMap);
                opApplyInfo.setEncounterId(PskqMesUtils.pskqHisCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterId());
                opApplyInfo.setPlacerOrderNo(PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+opApplyInfo.getPlacerOrderNo());
                opApplyInfo.setCancelFlag("0");
                opApplyInfo.setSurgeryApplyId(PskqMesUtils.pskqSysCode+opApplyInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+opApplyInfo.getSurgeryApplyNo());
                opApplyInfo.setSurgeryDesc(org.apache.commons.collections.MapUtils.getString(paramMap,"note"));
            }


            if(opApplyInfo==null){
                listener.error("未获取到手术信息！","","");
                return;
            }


            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(opApplyInfo);

            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
            //消息成功
            if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                listener.success(result,responseBody);
                return;
            }else {
                listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
                return;
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
            return;
        }
    }


    /**
     * 发送住院门诊诊断  不含入院登记诊断
     * E005001
     *
     * @param paramMap
     * @param listener
     */
    public void getDiagInfo(Map<String, Object> paramMap, ResultListener listener){

        String result = "";
        String responseBody="";
        try {
            if(paramMap!= null && !paramMap.containsKey("paramList")){
                listener.error("未获取到诊断信息的医嘱参数！请检查触发消息方法","","");
                return;
            }
            List<Map<String, Object>> sourceList = (List<Map<String, Object>>)paramMap.get("source");//保存
            List<Map<String, Object>> paramList = (List<Map<String, Object>>)paramMap.get("paramList");//删除
            for (int i = 0; i < sourceList.size(); i ++){
                String pkPvdiagAdd = CommonUtils.getPropValueStr(sourceList.get(i),"pkPvdiag");
                for (int j = 0; j < paramList.size(); j ++){
                    if (CommonUtils.getPropValueStr(paramList.get(j),"pkPvdiag").equals(pkPvdiagAdd)){
                        paramList.remove(j);j--;
                        sourceList.remove(i);i--;
                    }
                }
            }
            if(sourceList.size()==0&&paramList.size()==0){
                listener.error("未获取诊断新增或删除信息","","");
                return;
            }
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            SysMsgRecDao sysMsgRecDao = applicationContext.getBean("sysMsgRecDao", SysMsgRecDao.class);
            OrdDiagInfo  diagInfo =  sysMsgRecDao.selectDiagInfo(CommonUtils.getPropValueStr(paramMap,"pkPv"));

            //发送诊断新增信息
            if(sourceList.size()>0){
                for (int i = 0 ; i < sourceList.size(); i++){
                    diagInfo.setEncounterId(PskqMesUtils.pskqHisCode+diagInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+diagInfo.getEncounterId());
                    diagInfo.setEncounterTypeName(PskqMesUtils.getPvTypeToPvTypeName(diagInfo.getEncounterTypeCode()));
                    diagInfo.setDiagCode(CommonUtils.getPropValueStr(sourceList.get(i),"codeIcd"));
                    diagInfo.setDiagName(CommonUtils.getPropValueStr(sourceList.get(i),"nameDiag"));
                    diagInfo.setDiagDesc(CommonUtils.getPropValueStr(sourceList.get(i),"descDiag"));
                    diagInfo.setPrimaryDiagFlag(CommonUtils.getPropValueStr(sourceList.get(i),"flagMaj"));
                    MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(diagInfo);
                    paramMap.put("Control","NW");
                    RequestBody requestBody = message.getRequestBody(paramMap);
                    result =  JSON.toJSONString(requestBody);
                    //发送消息
                    responseBody = httpRestTemplate.postForString(result);
                    ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                    //消息成功
                    if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
//                        PskqMesUtils.recordLogMessage(result,responseBody,"ACK");
                    }else {
//                        PskqMesUtils.recordLogMessage(result,responseBody,"ERROR");
                    }
                }
            }

            //发送诊断删除信息
            if(paramList.size()>0){
                for (int i = 0 ; i < paramList.size(); i++){
                    diagInfo.setEncounterId(PskqMesUtils.pskqHisCode+diagInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+diagInfo.getEncounterId());
                    diagInfo.setEncounterTypeName(PskqMesUtils.getPvTypeToPvTypeName(diagInfo.getEncounterTypeCode()));
                    diagInfo.setDiagCode(CommonUtils.getPropValueStr(paramList.get(i),"codeIcd"));
                    diagInfo.setDiagName(CommonUtils.getPropValueStr(paramList.get(i),"nameDiag"));
                    diagInfo.setDiagDesc(CommonUtils.getPropValueStr(paramList.get(i),"descDiag"));
                    diagInfo.setPrimaryDiagFlag(CommonUtils.getPropValueStr(paramList.get(i),"flagMaj"));
                    MessageFactory messageFactory = new MessageFactory();
                    Message message = messageFactory.getInstance(diagInfo);
                    paramMap.put("Control","UP");
                    RequestBody requestBody = message.getRequestBody(paramMap);
                    result =  JSON.toJSONString(requestBody);
                    //发送消息
                    responseBody = httpRestTemplate.postForString(result);
                    ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                    //消息成功
                    if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
//                        PskqMesUtils.recordLogMessage(result,responseBody,"ACK");
                    }else {
//                        PskqMesUtils.recordLogMessage(result,responseBody,"ERROR");
                    }
                }
            }
            listener.success("","");

        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
            return;
        }



    }



    /**
     * 住院检查申请
     * @param paramMap
     * @param listener
     */
    public void getIpRisMsg(Map<String, Object> paramMap, ResultListener listener){


    }

   /**
    * 住院 检验申请
    * @param paramMap
    * @param listener
    */
   public void getIpLisMsg(Map<String, Object> paramMap, ResultListener listener){

	   String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("lisList")==null){
               listener.error("未获取到查询检验或检查信息的医嘱参数！","","");
               return;
           }
           List<CnOrder> cnList=(List<CnOrder>) paramMap.get("lisList");
           List<String> qryAddLisList = Lists.newArrayList();
           for (int i = 0; i < cnList.size(); i++) {
        	   CnOrder cnOrder = CnOrderRepository.getOne(cnList.get(i).getPkCnord());
        	   if ("03".equals(cnOrder.getCodeOrdtype().substring(0, 2))) {
                   qryAddLisList.add(cnList.get(i).getPkCnord());
               }
           }
           sendIpLis(paramMap, qryAddLisList, listener);
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }

   }
   /**
    * 发送检查申请
    * @param paramMap
    * @param listener
    */
   @SuppressWarnings({"unchecked" })
	public void getRisInfo(Map<String, Object> paramMap, ResultListener listener){
	   String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("risList")==null){
               listener.error("未获取到查询检验或检查信息的医嘱参数！","","");
               return;
           }
           List<CnOrder> cnList=(List<CnOrder>) paramMap.get("risList");
           List<String> qryAddPascList = Lists.newArrayList();
           for (int i = 0; i < cnList.size(); i++) {
        	   CnOrder cnOrder = CnOrderRepository.getOne(cnList.get(i).getPkCnord());
               if ("02".equals(cnOrder.getCodeOrdtype().substring(0, 2))) {
                   qryAddPascList.add(cnList.get(i).getPkCnord());
               }
           }
           //发送【检查新增】申请消息
           sendIpRis(paramMap, qryAddPascList, listener);
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
   /**
    * 住院 检验申请和检查申请
    * @param paramMap
    * @param listener
    */
   public void getIpLisOrRisMsg(Map<String, Object> paramMap, ResultListener listener){

       String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("lisList")==null){
               listener.error("未获取到查询检验或检查信息的医嘱参数！","","");
               return;
           }
           List<CnOrder> cnList=(List<CnOrder>) paramMap.get("lisList");
           List<String> qryAddLisList = Lists.newArrayList();
           List<String> qryAddPascList = Lists.newArrayList();
           for (int i = 0; i < cnList.size(); i++) {
               if ("02".equals(cnList.get(i).getCodeOrdtype().substring(0, 2))) {
                   qryAddPascList.add(cnList.get(i).getPkCnord());
               }
               if ("03".equals(cnList.get(i).getCodeOrdtype().substring(0, 2))) {
                   qryAddLisList.add(cnList.get(i).getPkCnord());
               }
           }
           
           //发送【检验新增】申请消息
           sendIpLis(paramMap, qryAddLisList, listener);
           //发送【检查新增】申请消息
           sendIpRis(paramMap, qryAddPascList, listener);
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }

   }
   private void sendIpLis(Map<String, Object> paramMap,List<String> qryAddLisList, ResultListener listener){
	   String result = "";
       String responseBody="";
       try {
         //发送【检验新增】申请消息
           if(qryAddLisList.size()!=0) {
               boolean state = true;
               List<LabApply> lisAddList = pskqPlatFormSendCnMapper.queryLisApplyInfoById(qryAddLisList);
               for (LabApply labApply : lisAddList) {
                   if (labApply == null) {
                       listener.error("未获取到检验信息！", "", "");
                       return;
                   }
                   labApply.setLabApplyId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getLabApplyNo());
                   labApply.setEncounterId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getEncounterId());
                   labApply.setOrgCode("10");
                   labApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                   labApply.setExecOrgCode("10");
                   labApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                   labApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+labApply.getPlacerOrderNo());
                   MessageFactory messageFactory = new MessageFactory();
                   Message message = messageFactory.getInstance(labApply);
                   paramMap.put("Control", "NW");
                   RequestBody requestBody = message.getRequestBody(paramMap);
                   result = JSON.toJSONString(requestBody);
                   //发送消息
                   //responseBody = RestTemplateUtil.postForString(result);
                   responseBody = httpRestTemplate.postForString(result);
                   ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                   //消息成功
                   if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                       listener.success(result, responseBody);
                   } else {
                       listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                   }
               }
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
   private void sendIpRis(Map<String, Object> paramMap,List<String> qryAddPascList, ResultListener listener){
	   String result = "";
       String responseBody="";
       try {
    	 //发送【检查新增】申请消息
           if(qryAddPascList.size()!=0) {
               boolean state = true;
               List<ExamApply> pascAddList = pskqPlatFormSendCnMapper.queryPacsApplyInfoById(qryAddPascList);
               for (ExamApply examApply : pascAddList) {
                   if (examApply == null) {
                       listener.error("未获取到检验信息！", "", "");
                       return;
                   }

                   String ordCode = examApply.getUniversalServiceCode();
                   String sql = "select * from BD_ORD where code = ?";
                   BdOrd bdOrd = DataBaseHelper.queryForBean(sql,BdOrd.class,ordCode);
                   String dtCodeType = bdOrd.getDtOrdcate();//06心电
                   examApply.setExamCategoryCode(dtCodeType);
                   if("06".equals(dtCodeType)){
                       examApply.setExamCategoryName("心电图");
                   }
                   examApply.setExamApplyId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getExamApplyNo());
                   examApply.setEncounterId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getEncounterId());
                   examApply.setOrgCode("10");
                   examApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                   examApply.setExecOrgCode("10");
                   examApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                   examApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+examApply.getPlacerOrderNo());
//                   examApply.setPkPatient("10_2000_"+examApply.getPkPatient());
                   MessageFactory messageFactory = new MessageFactory();
                   Message message = messageFactory.getInstance(examApply);
                   paramMap.put("Control", "NW");
                   RequestBody requestBody = message.getRequestBody(paramMap);
                   result = JSON.toJSONString(requestBody);
                   //发送消息
                   responseBody = httpRestTemplate.postForString(result);
                   ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                   //消息成功
                   if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                       listener.success(result, responseBody);
                   } else {
                       listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                   }
               }
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
    /**
     *
     * 发送住院转科、换床消息
     * @param paramMap
     * @param listener
     */
   public void getEncounterIpAdtChangeById(Map<String, Object> paramMap, ResultListener listener){
       String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("pkPv")==null){
               listener.error("未获取到查询患者住院的参数！","","");
               return;
           }
           if(paramMap.get("methodType")==null){
               listener.error("未获取到调用来源的参数！","","");
               return;
           }
           String methodType=(String) paramMap.get("methodType");
           AdtChangeInfo adtChangeInfo = null;


           String serviceCode = "";
           String serviceName = "";
           String eventCode="";
           String eventName="";
           switch (methodType) {
                case "sendDeptChangeMsg":
                    serviceCode =  "S0030";
                    serviceName = "住院转科信息更新服务";
    				eventCode = "E003001";
    				eventName = "住院转科完成";
                    adtChangeInfo =pskqPlatFormSendCnMapper.getEncounterIpAdtChangeDeptById(paramMap);
                    if(adtChangeInfo ==null){
                        listener.error("未获取到患者住院信息！","","");
                        return;
                    }
                    adtChangeInfo.setAdtChangeTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
                    adtChangeInfo.setAdtChangeStatusCode("30");
                    adtChangeInfo.setAdtChangeStatusName("转科");
    				break;
    			case "sendBedChangeMsg"://换床
                    serviceCode =  "S0029";
                    serviceName = "住院转科信息新增服务";
    				eventCode = "E002902";
     				eventName = "住院换床";
                    adtChangeInfo =pskqPlatFormSendCnMapper.getEncounterIpAdtChangeBedById(paramMap);
                    if(adtChangeInfo ==null){
                        listener.error("未获取到患者住院信息！","","");
                        return;
                    }
                    adtChangeInfo.setTrunOutBedNo(PskqMesUtils.getPropValueStr(paramMap,"bedOld"));
                    adtChangeInfo.setAdtChangeStatusCode("29");
                    adtChangeInfo.setAdtChangeStatusName("换床");
    				break;
                default:
                   break;
           }
           adtChangeInfo.setAdtChangeInfoId(PskqMesUtils.pskqHisCode+adtChangeInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+adtChangeInfo.getAdtChangeInfoNo());
           adtChangeInfo.setEncounterId(PskqMesUtils.pskqHisCode+adtChangeInfo.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+adtChangeInfo.getEncounterId());
           adtChangeInfo.setOrgCode("10");
           adtChangeInfo.setOrgName("南方医科大学深圳口腔医院（坪山）");


           if("".equals(eventCode)){
               listener.error("未匹配对应的消息体！","","");
               return;
           }
           Map<String, Object> map=new HashMap<String, Object>();
           MessageFactory messageFactory = new MessageFactory();
           Message message = messageFactory.getInstance(adtChangeInfo);
           map.put("eventCode", eventCode);
           map.put("eventName", eventName);
           map.put("serviceCode", serviceCode);
           map.put("serviceName", serviceName);
           RequestBody requestBody = message.getRequestBody(map);
           result =  JSON.toJSONString(requestBody);
           //发送消息
           responseBody = httpRestTemplate.postForString(result);
           ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
           //消息成功
           if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
               listener.success(result,responseBody);
           }else {
               listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }


   }


   /**
    * 获取入院登记信息

    * @param paramMap
    * @param listener
    */
   @SuppressWarnings("unused")
   public void getEncounterIpAdtById(Map<String, Object> paramMap, ResultListener listener){
       String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("pkPv")==null){
               listener.error("未获取到查询患者住院的参数！","","");
               return;
           }
           if(paramMap.get("methodType")==null){
               listener.error("未获取到调用来源的参数！","","");
               return;
           }
           String methodType=(String) paramMap.get("methodType");
           EncounterInpatient encounterInpatient = pskqPlatFormSendCnMapper.getEncounterIpAdtById(paramMap);
           encounterInpatient.setVisitId(PskqMesUtils.pskqHisCode+encounterInpatient.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+encounterInpatient.getVisitId());
           if(encounterInpatient ==null){
               listener.error("未获取到患者住院信息！","","");
               return;
           }
           encounterInpatient.setOrgCode("10");
           encounterInpatient.setOrgName("南方医科大学深圳口腔医院（坪山）");
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           String now = simpleDateFormat.format(new Date());
           //入院时间
           encounterInpatient.setAdmissionDateTime(now);



           InEncounter inEncounter = new InEncounter();

           PvEncounter pvEncounter = PvEncounterRepository.getOne(paramMap.get("pkPv").toString());

           //入院诊断
           String sql = "select * from pv_ip where PK_PV = ?";
           List<PvIp> pvIpList = DataBaseHelper.queryForList(sql,PvIp.class,pvEncounter!=null?pvEncounter.getPkPv():"");
           PvIp pvIp = pvIpList.stream().findFirst().orElse(null);
           encounterInpatient.setAdmissionDiagCode(pvIp!=null?pvIp.getCodeDiag():"");
           encounterInpatient.setAdmissionDiagName(pvIp!=null?pvIp.getNameDiag():"");
           encounterInpatient.setVisitNo(pvIp.getIpTimes()+"");


           paramMap.put("pkPi", pvEncounter.getPkPi());

           String eventCode="";
           String eventName="";
           String serviceCode = "";
           String serviceName = "";
           switch (methodType) {
			case "sendPvInMsg":
                Patient  patient= pskqPlatFormSendPatientMapper.getPiMasterById(paramMap);
                if(patient ==null){
                    listener.error("未获取到患者信息！","","");
                    return;
                }
                patient.setOrgCode("10");
                patient.setOrgName("南方医科大学深圳口腔医院（坪山）");
                serviceCode = "S0026";
                serviceName = "住院就诊信息登记服务";
				eventCode = "E002601";
				eventName = "住院登记";
                encounterInpatient.setEncounterId("10_3_2000_"+encounterInpatient.getEncounterId());
                inEncounter.setPatient(patient);
				break;
			case "sendPvCancelInMsg":
                serviceCode = "S0027";
                serviceName = "住院就诊信息更新服务";
				eventCode = "E002701";
				eventName = "退院";
				break;
			case "sendDeptInMsg":
                serviceCode = "S0027";
                serviceName = "住院就诊信息更新服务";
				eventCode = "E002702";
				eventName = "入科";
				//床号
				break;
			case "sendCancelDeptInMsg":
                serviceCode = "S0027";
                serviceName = "住院就诊信息更新服务";
				eventCode = "E002703";
				eventName = "取消入科";
				break;
			case "sendPvOutMsg":
                serviceCode = "S0032";
                serviceName = "出院登记信息新增服务";
				eventCode = "E003201";
				eventName = "出院登记";
                encounterInpatient.setEncounterId("10_3_2000_"+encounterInpatient.getEncounterId());
				break;
			case "sendPvCancelOutMsg":
                serviceCode = "S0033";
                serviceName = "出院登记信息更新服务";
				eventCode = "E003301";
				eventName = "取消出院登记";
                encounterInpatient.setEncounterId("10_3_2000_"+encounterInpatient.getEncounterId());
				break;
			default:
				break;
			}
           if("".equals(eventCode)){
        	   listener.error("未匹配对应的消息体！","","");
               return;
           }
           Map<String, Object> map=new HashMap<String, Object>();
           MessageFactory messageFactory = new MessageFactory();
           Message message = messageFactory.getInstance(inEncounter);
           inEncounter.setEncounterInpatient(encounterInpatient);
           map.put("eventCode", eventCode);
           map.put("eventName", eventName);
           map.put("serviceCode", serviceCode);
           map.put("serviceName", serviceName);
           RequestBody requestBody = message.getRequestBody(map);
           result =  JSON.toJSONString(requestBody);
           //发送消息
           responseBody = httpRestTemplate.postForString(result);
           ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
           //消息成功
           if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
               listener.success(result,responseBody);
           }else {
               listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
   /**
    * 发送住院计费信息
    * @param paramMap
    * @param listener
    */
   @SuppressWarnings("unchecked")
   public void getIpCostDetailInfo(Map<String, Object> paramMap, ResultListener listener){

       String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("pkCnords")==null){
               listener.error("未获取到医嘱主键信息参数！","","");
               return;
           }
           List<String> l=(List<String>) paramMap.get("pkCnords");
           for (String string : l) {
        	   Map<String,Object> m=new HashMap<String, Object>();
        	   m.put("pkCnord", string);
        	   m.put("Control", CommonUtils.getPropValueStr(paramMap,"Control"));
        	   List<CostDetailInpat> listCode = pskqPlatFormSendCnMapper.queryIpCostDetailInfo(m);
        	   for (CostDetailInpat costDetailInpat : listCode) {
        		   if(costDetailInpat==null){
                       listener.error("未获取到医嘱信息！","","");
                       return;
                   }
            	   costDetailInpat.setOrgCode("10");
            	   costDetailInpat.setOrgName("南方医科大学深圳口腔医院（坪山）");
            	   costDetailInpat.setOrderId("10_"+costDetailInpat.getEncounterTypeCode()+"_"+costDetailInpat.getOrderId());
            	   costDetailInpat.setEncounterId(PskqMesUtils.pskqHisCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterId());
             	   costDetailInpat.setOrderId(PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+costDetailInpat.getOrderId());
             	   costDetailInpat.setChargeStatus("1");
        	   }
        	   CostDetailInpatList costDetailInpatList=new CostDetailInpatList();
        	   costDetailInpatList.setCostDetailInpats(listCode);
        	   MessageFactory messageFactory = new MessageFactory();
               Message message = messageFactory.getInstance(costDetailInpatList);
               Map<String, Object> map=new HashMap<String, Object>();
               map.put("eventCode", "E150303");map.put("eventName", "住院计费");
               RequestBody requestBody = message.getRequestBody(map);
               result =  JSON.toJSONString(requestBody);
               //发送消息
               //responseBody = RestTemplateUtil.postForString(result);
               responseBody = httpRestTemplate.postForString(result);
               ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
               //消息成功
               if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                   listener.success(result,responseBody);
               }else {
                   listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
               }
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
   /**
    * 发送住院检查退费信息
    * @param paramMap
    * @param listener
    */
   @SuppressWarnings("unchecked")
   public void getIpCostDetailInfoRtn(Map<String, Object> paramMap, ResultListener listener){

       String result = "";
       String responseBody="";
       try {
           if(paramMap== null || paramMap.get("pkCnords")==null){
               listener.error("未获取到医嘱主键信息参数！","","");
               return;
           }
           List<String> l=(List<String>) paramMap.get("pkCnords");
           for (String string : l) {
        	   Map<String,Object> m=new HashMap<String, Object>();
        	   m.put("pkCnord", string);
        	   m.put("Control", CommonUtils.getPropValueStr(paramMap,"Control"));
        	   List<CostDetailInpat> listCode = pskqPlatFormSendCnMapper.queryIpCostDetailInfo(m);
        	   for (CostDetailInpat costDetailInpat : listCode) {
        		   if(costDetailInpat==null){
                       listener.error("未获取到医嘱信息！","","");
                       return;
                   }

            	   costDetailInpat.setOrgCode("10");
            	   costDetailInpat.setOrgName("南方医科大学深圳口腔医院（坪山）");
            	   costDetailInpat.setOrderId("10_"+costDetailInpat.getEncounterTypeCode()+"_"+costDetailInpat.getOrderId());
            	   costDetailInpat.setEncounterId(PskqMesUtils.pskqHisCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterId());
             	   costDetailInpat.setOrderId(PskqMesUtils.pskqSysCode+costDetailInpat.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+costDetailInpat.getOrderId());
             	   costDetailInpat.setChargeStatus("2");
            	   
        	   }
        	   MessageFactory messageFactory = new MessageFactory();
        	   CostDetailInpatList costDetailInpatList=new CostDetailInpatList();
        	   costDetailInpatList.setCostDetailInpats(listCode);
               Message message = messageFactory.getInstance(costDetailInpatList);
               Map<String, Object> map=new HashMap<String, Object>();
               map.put("eventCode", "E150304");map.put("eventName", "住院退费");
               RequestBody requestBody = message.getRequestBody(map);
               result =  JSON.toJSONString(requestBody);
               //发送消息
               //responseBody = RestTemplateUtil.postForString(result);
               responseBody = httpRestTemplate.postForString(result);
               ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
               //消息成功
               if(paramVo !=null && paramVo.getAck()!=null && "AA".equals(paramVo.getAck().get("ackCode"))){
                   listener.success(result,responseBody);
               }else {
                   listener.error(paramVo.getAck().get("ackDetail").toString(),result,responseBody);
               }
           }
       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }
   }
   /**
    * 发送 门诊 检查和检验   申请,修改,撤销
    * @param paramMap
    * @param listener
    */
   @SuppressWarnings("unchecked")
   public void getOpLisAndRisMsg(Map<String, Object> paramMap, ResultListener listener){
	   
       List<CnOrder> addOrdList = new ArrayList<CnOrder>();
       List<CnOrder> updateOrdList = new ArrayList<CnOrder>();
       List<Map<String,Object>> delMapList = new ArrayList<>();
       Map<String,Object> qryLisMap = new HashMap<>();
       Map<String,Object> qryPacsMap = new HashMap<>();
       String result = "";
       String responseBody="";
       try {
    	   addOrdList=(List<CnOrder>) paramMap.get("addOrdList");
           updateOrdList=(List<CnOrder>) paramMap.get("updateOrdList");
           delMapList=(List<Map<String,Object>>) paramMap.get("delMapList");

           //发送检验检查【申请】消息
           if(addOrdList.size()!=0) {
               List<String> qryAddLisList = Lists.newArrayList();
               List<String> qryAddPascList = Lists.newArrayList();
               for (int i = 0; i < addOrdList.size(); i++) {
                   if ("02".equals(addOrdList.get(i).getCodeOrdtype().substring(0, 2))) {
                       qryAddPascList.add(addOrdList.get(i).getPkCnord());
                   }
                   if ("03".equals(addOrdList.get(i).getCodeOrdtype().substring(0, 2))) {
                       qryAddLisList.add(addOrdList.get(i).getPkCnord());
                   }
               }
               //发送【检查新增】申请消息
               if(qryAddPascList.size()!=0) {
                   boolean state = true;
                   List<ExamApply> pascAddList = pskqPlatFormSendCnMapper.queryPacsApplyInfoById(qryAddPascList);
                   for (ExamApply examApply : pascAddList) {
                       if (examApply == null) {
                           listener.error("未获取到检验信息！", "", "");
                           return;
                       }

                       String ordCode = examApply.getUniversalServiceCode();
                       String sql = "select * from BD_ORD where code = ?";
                       BdOrd bdOrd = DataBaseHelper.queryForBean(sql,BdOrd.class,ordCode);
                       String dtCodeType = bdOrd.getDtOrdcate();//06心电
                       examApply.setExamCategoryCode(dtCodeType);
                       examApply.setExamApplyId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getExamApplyNo());
                       examApply.setEncounterId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getEncounterId());
                       examApply.setOrgCode("10");
                       examApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setExecOrgCode("10");
                       examApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+examApply.getPlacerOrderNo());
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(examApply);
                       paramMap.put("Control", "NW");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }
                   }
               }
               //发送【检验新增】申请消息
               if(qryAddLisList.size()!=0) {
                   boolean state = true;

                   List<LabApply> lisAddList = pskqPlatFormSendCnMapper.queryLisApplyInfoById(qryAddLisList);
                   qryLisMap.clear();
                   for (LabApply labApply : lisAddList) {
                       if (labApply == null) {
                           listener.error("未获取到检验信息！", "", "");
                           return;
                       }
                       labApply.setLabApplyId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getLabApplyNo());
                       labApply.setEncounterId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getEncounterId());
                       labApply.setOrgCode("10");
                       labApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       labApply.setExecOrgCode("10");
                       labApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       labApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+labApply.getPlacerOrderNo());
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(labApply);
                       paramMap.put("Control", "NW");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       //responseBody = RestTemplateUtil.postForString(result);
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }
                   }
               }
           }

            //发送检查检验【修改】消息
           if(updateOrdList.size()!=0) {

               List<String> qryUpdatePacsList = Lists.newArrayList();
               List<String> qryUpdateLisList = Lists.newArrayList();
               for (int i = 0; i < updateOrdList.size(); i++) {
                   if ("02".equals(updateOrdList.get(i).getCodeOrdtype().substring(0, 2))) {
                       qryUpdatePacsList.add(updateOrdList.get(i).getPkCnord());
                   }
                   if ("03".equals(updateOrdList.get(i).getCodeOrdtype().substring(0, 2))) {
                       qryUpdateLisList.add(updateOrdList.get(i).getPkCnord());
                   }

               }
               //发送【检查修改】消息
               if(qryUpdatePacsList.size()!=0) {
                   boolean state = true;
                   List<ExamApply> pacsUpList =  pskqPlatFormSendCnMapper.queryPacsApplyInfoById(qryUpdatePacsList);
                   for (ExamApply examApply : pacsUpList) {
                       if (examApply == null) {
                           listener.error("未获取到检验信息！", "", "");
                           return;
                       }
                       examApply.setExamApplyId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getExamApplyNo());
                       examApply.setEncounterId(PskqMesUtils.pskqHisCode + examApply.getEncounterTypeCode() + "_" + PskqMesUtils.pskqSysCode + examApply.getEncounterId());
                       examApply.setOrgCode("10");
                       examApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setExecOrgCode("10");
                       examApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+examApply.getPlacerOrderNo());
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(examApply);
                       paramMap.put("Control", "RU");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       //responseBody = RestTemplateUtil.postForString(result);
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }
                   }
               }
               //发送【检验修改】消息
               if(qryUpdateLisList.size()!=0) {
                   boolean state = true;
                   List<LabApply> listUpLis = pskqPlatFormSendCnMapper.queryLisApplyInfoById(qryUpdateLisList);
                   for (LabApply labApply : listUpLis) {
                       if (labApply == null) {
                           listener.error("未获取到检验信息！", "", "");
                           return;
                       }
                       labApply.setLabApplyId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getLabApplyNo());
                       labApply.setEncounterId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getEncounterId());
                       labApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+labApply.getPlacerOrderNo());
                       labApply.setOrgCode("10");
                       labApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       labApply.setExecOrgCode("10");
                       labApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(labApply);
                       paramMap.put("Control", "RU");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       //responseBody = RestTemplateUtil.postForString(result);
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }

                   }
               }
           }


           //发送检查检验【删除】消息
           if(delMapList.size()!=0){
               //发送【检查删除】消息
               if(delMapList.get(0)!=null){
                   boolean state = true;
                   Map<String, Object> pacsMap = (Map<String, Object>)delMapList.get(0);
                   List<ExamApply> pacsUpList = (List<ExamApply>)pacsMap.get("pacsUpList");
                   for (ExamApply examApply : pacsUpList) {
                       if (examApply == null) {
                           listener.error("未获取到检查信息！", "", "");
                           return;
                       }
                       examApply.setExamApplyId(PskqMesUtils.pskqHisCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +examApply.getExamApplyNo());
                       examApply.setEncounterId(PskqMesUtils.pskqHisCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +examApply.getEncounterId());
                       examApply.setOrgCode("10");
                       examApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setExecOrgCode("10");
                       examApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       examApply.setCancelFlag("1");
                       examApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+examApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+examApply.getPlacerOrderNo());
                       examApply.setCancelDateTime(PskqMesUtils.simDatFor.format(new Date()));
                       /*examApply.setcanc*/
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(examApply);
                       paramMap.put("Control", "RU");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       //responseBody = RestTemplateUtil.postForString(result);
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }
                   }
               }
               if(delMapList.get(1)!=null){
                   boolean state = true;
                   Map<String, Object> pacsMap = (Map<String, Object>)delMapList.get(1);
                   List<LabApply> listUpLis = (List<LabApply>)pacsMap.get("lisUpList");
                   for (LabApply labApply : listUpLis) {
                       if (labApply == null) {
                           listener.error("未获取到检验信息！", "", "");
                           return;
                       }

                       labApply.setLabApplyId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getLabApplyNo());
                       labApply.setEncounterId(PskqMesUtils.pskqHisCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +labApply.getEncounterId());
                       labApply.setPlacerOrderNo(PskqMesUtils.pskqSysCode+labApply.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+labApply.getPlacerOrderNo());
                       labApply.setOrgCode("10");
                       labApply.setOrgName("南方医科大学深圳口腔医院（坪山）");
                       labApply.setExecOrgCode("10");
                       labApply.setExecOrgName("南方医科大学深圳口腔医院（坪山）");
                       labApply.setCancelFlag("1");
                       labApply.setCancelDateTime(PskqMesUtils.simDatFor.format(new Date()));
                       MessageFactory messageFactory = new MessageFactory();
                       Message message = messageFactory.getInstance(labApply);
                       paramMap.put("Control", "RU");
                       RequestBody requestBody = message.getRequestBody(paramMap);
                       result = JSON.toJSONString(requestBody);
                       //发送消息
                       //responseBody = RestTemplateUtil.postForString(result);
                       responseBody = httpRestTemplate.postForString(result);
                       ResponseBody paramVo = JsonUtil.readValue(responseBody, ResponseBody.class);
                       //消息成功
                       if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                           listener.success(result, responseBody);
                       } else {
                           listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                       }
                   }
               }

           }

       }catch (Exception e){
           listener.exception(e.getMessage(),result,responseBody);
       }



   }

    /**
     * 推送门诊开立医嘱信息
     * @param addCnOrdList
     * @param listener
     * @param flag 为ture则为医嘱开立，为false则为检查检验发送医嘱
     */
    @MethodLog
    public void sendOpCnorderNew(List<Object> addCnOrdList,Boolean flag, ResultListener listener){
        String result = "";
        String responseBody="";
        try{
            boolean state=true;
            List<String> pkCnords = new ArrayList<>();
            List<OrderOutpat> OrderOutpatList = new ArrayList<>();
            List<Map<String,Object>> addCnOrdListMap = PskqMesUtils.lisBToLisMap(addCnOrdList);
            for(int i=0;i<addCnOrdListMap.size();i++){
                String pkCnord = CommonUtils.getPropValueStr(addCnOrdListMap.get(i), "pkCnord");
                pkCnords.add(pkCnord);
            }
            OrderOutpatList = pskqPlatFormSendCnMapper.queryOpCnorderProject(pkCnords);
            if(flag) {
                OrderOutpatList.addAll(pskqPlatFormSendCnMapper.queryOpCnorderDrugs(pkCnords));
            }

            for (OrderOutpat order:OrderOutpatList){

                try{
                    //医嘱类型转换
                    order.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(order.getOrderCategoryCode()));
                    order.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(order.getOrderCategoryCode()));

                    //剂型转换
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(order.getDrugFormCode());
                    order.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    order.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    order.setOrderId(PskqMesUtils.pskqSysCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+order.getOrderId());
                    order.setEncounterId(PskqMesUtils.pskqHisCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +order.getEncounterId());
                    order.setOrgCode("10");
                    order.setOrgName("南方医科大学深圳口腔医院（坪山）");
                }catch (Exception e){

                }

            }
            OrderOutpatList orderOutpatLists=new OrderOutpatList();
            orderOutpatLists.setOrderOutpats(OrderOutpatList);
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(orderOutpatLists);
            Map<String,Object> paramMap= new HashMap<>();
            paramMap.put("serviceCode", "S0035");
            paramMap.put("serviceName", "医嘱信息新增服务");
            paramMap.put("eventCode", "E003501");
            paramMap.put("eventName", "开立门诊医嘱");
            
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
            //消息成功
            if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                listener.success(result, responseBody);
            } else {
                listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }


    /**
     * 推送门诊修改医嘱信息
     * @param editCnOrdList
     * @param listener
     */
    public void sendOpCnorderUpdate(List<Object> editCnOrdList,Boolean flag,  ResultListener listener){
        String result = "";
        String responseBody="";
        try{
            boolean state=true;
            List<Map<String,Object>> editCnOrdListMap = PskqMesUtils.lisBToLisMap(editCnOrdList);
            List<String> pkCnords = new ArrayList<>();
            for(int i=0;i<editCnOrdListMap.size();i++){
                String pkCnord = CommonUtils.getPropValueStr(editCnOrdListMap.get(i), "pkCnord");
                pkCnords.add(pkCnord);
                //OrderOutpat order = pskqPlatFormSendCnMapper.queryOpCnorder(pkCnord);
            }
            List<OrderOutpat> OrderOutpatList = pskqPlatFormSendCnMapper.queryOpCnorderProject(pkCnords);
            if(flag) {
                OrderOutpatList.addAll(pskqPlatFormSendCnMapper.queryOpCnorderDrugs(pkCnords));
            }
            for (OrderOutpat order:OrderOutpatList){
                //医嘱类型转换
                order.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(order.getOrderCategoryCode()));
                order.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(order.getOrderCategoryCode()));
                order.setOrderId(PskqMesUtils.pskqSysCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+order.getOrderId());
                //剂型转换
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(order.getDrugFormCode());
                order.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                order.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));

                order.setEncounterId(PskqMesUtils.pskqHisCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +order.getEncounterId());
                order.setOrgCode("10");
                order.setOrgName("南方医科大学深圳口腔医院（坪山）");
                
                
            }
            OrderOutpatList orderOutpatLists=new OrderOutpatList();
            orderOutpatLists.setOrderOutpats(OrderOutpatList);
            MessageFactory messageFactory = new MessageFactory();
            Message message = messageFactory.getInstance(orderOutpatLists);
            Map<String,Object> paramMap= new HashMap<>();
            paramMap.put("serviceCode", "S0036");
            paramMap.put("serviceName", "医嘱信息更新服务");
            paramMap.put("eventCode", "E003601");
            paramMap.put("eventName", "更新门诊医嘱");
            RequestBody requestBody = message.getRequestBody(paramMap);
            result =  JSON.toJSONString(requestBody);
            //发送消息
            responseBody = httpRestTemplate.postForString(result);
            ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);

            //消息成功
            if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                listener.success(result, responseBody);
            } else {
                listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
            }
            

        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }

    /**
     * 推送门诊删除医嘱信息
     * @param delMapList
     * @param listener
     */
    public void sendOpCnorderdelete(List<Map<String,Object>> delMapList, ResultListener listener){
        {
            String result = "";
            String responseBody="";
            try{
                boolean state=true;
                String dtListStr = JsonUtil.writeValueAsString(delMapList);
                List<OrderOutpat> OrderOutpatList = JsonUtil.readValue(dtListStr, new TypeReference<List<OrderOutpat>>() {});
                for (OrderOutpat order : OrderOutpatList) {
                	String genderCode=null;
                	String genderName=null;
                	PiMaster piMaster = DataBaseHelper.queryForBean("select * from PI_MASTER where mpi = ?",PiMaster.class,new Object[]{order.getEmpiId()});
                	if("02".equals(piMaster.getDtSex())){
                		genderCode="1";
                		genderName="男";
                	}else if("03".equals(piMaster.getDtSex())){
                		genderCode="2";
                		genderName="女";
                	}else{
                		genderCode="9";
                		genderName="未说明的性别";
                	}
                	order.setGenderCode(genderCode);
                	order.setGenderName(genderName);
                    //医嘱类型转换
                    order.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(order.getOrderCategoryCode()));
                    order.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(order.getOrderCategoryCode()));
                    order.setOrderId(PskqMesUtils.pskqSysCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+order.getOrderId());
                    //剂型转换
                    Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(order.getDrugFormCode());
                    order.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                    order.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                    
                    order.setEncounterId(PskqMesUtils.pskqHisCode+order.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode +order.getEncounterId());
                    order.setOrgCode("10");
                    order.setOrgName("南方医科大学深圳口腔医院（坪山）");
                    order.setCancelDateTime(PskqMesUtils.simDatFor.format(new Date()));
                    
                    
				}
                OrderOutpatList orderOutpatLists=new OrderOutpatList();
                orderOutpatLists.setOrderOutpats(OrderOutpatList);
                MessageFactory messageFactory = new MessageFactory();
                Message message = messageFactory.getInstance(orderOutpatLists);
                Map<String,Object> paramMap= new HashMap<>();
                paramMap.put("serviceCode", "S0036");
                paramMap.put("serviceName", "医嘱信息更新服务");
                paramMap.put("eventCode", "E003601");
                paramMap.put("eventName", "更新门诊医嘱");
                RequestBody requestBody = message.getRequestBody(paramMap);
                result =  JSON.toJSONString(requestBody);
                //发送消息
                responseBody = httpRestTemplate.postForString(result);
                ResponseBody paramVo = JsonUtil.readValue(responseBody,ResponseBody.class);
                
                //消息成功
                if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                    listener.success(result, responseBody);
                } else {
                    listener.error(paramVo.getAck().get("ackDetail").toString(), result, responseBody);
                }
            }catch (Exception e){
                listener.exception(e.getMessage(),result,responseBody);
            }
        }
    }
    /**
     * 推送住院医嘱核对信息
     *  E003603
     * @param listener
     */
    public void sendIpCnorderCheck(List<Map<String,Object>> orderList, ResultListener listener){
    	String result = "";
        String responseBody="";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            if(orderList.size()==0){
                listener.error("没有传入需要核对的医嘱信息","","");
                return;
            }
            List<String> pkCnords = new ArrayList<>();
            List<OrderExecRecord> orderExecRecords = new ArrayList<>();
            for(int i=0;i<orderList.size();i++){
                String pkCnord = CommonUtils.getPropValueStr(orderList.get(i), "pkCnord");
                pkCnords.add(pkCnord);
            }
            orderExecRecords = pskqPlatFormSendCnMapper.queryIpCnorderProject(pkCnords);
            orderExecRecords.addAll(pskqPlatFormSendCnMapper.queryIpCnorderDrugs(pkCnords));
            for (OrderExecRecord orderExecRecord : orderExecRecords) {

                String pkCnord1 = orderExecRecord.getOrderId();
                if(!StringUtils.isEmpty(pkCnord1)){
                    String sql = "select * from CN_ORDER where PK_CNORD = ?";
                    CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord1);
                    String supplyCode = cnOrder.getCodeSupply();
                    sql = "select * from BD_SUPPLY where CODE = ?";
                    if(!StringUtils.isEmpty(supplyCode)){
                        BdSupply bdSupply = DataBaseHelper.queryForBean(sql,BdSupply.class,supplyCode);
                        //医嘱类型转换
                        orderExecRecord.setOrderCardCode(bdSupply.getDtExcardtype());
                        switch (bdSupply.getDtExcardtype()){
                            case "1":
                                orderExecRecord.setOrderCardName("护理卡");
                                break;
                            case "2":
                                orderExecRecord.setOrderCardName("口服卡");
                                break;
                            case "3":
                                orderExecRecord.setOrderCardName("注射卡");
                                break;
                            case "4":
                                orderExecRecord.setOrderCardName("输液卡");
                                break;
                            case "0402":
                                orderExecRecord.setOrderCardName("输液瓶贴");
                                break;
                            case "0404":
                                orderExecRecord.setOrderCardName("执行单");
                                break;
                            case "5":
                                orderExecRecord.setOrderCardName("饮食卡");
                                break;
                            case "06":
                                orderExecRecord.setOrderCardName("检验");
                                break;
                            case "07":
                                orderExecRecord.setOrderCardName("检查");
                                break;
                            case "08":
                                orderExecRecord.setOrderCardName("检验");
                                break;
                            case "99":
                                orderExecRecord.setOrderCardName("其它卡");
                                break;
                        }

                    }
                }


                if(!StringUtils.isEmpty(orderExecRecord.getEncounterId())){
                    String sql = "select * from PV_ENCOUNTER where CODE_PV = ?";
                    List<PvEncounter> pvEncounterList = DataBaseHelper.queryForList(sql,PvEncounter.class,orderExecRecord.getEncounterId());
                    PvEncounter pvEncounter = pvEncounterList.stream().findFirst().orElse(null);
                    String pkPv = pvEncounter!=null?pvEncounter.getPkPv():"";
                    if(!StringUtils.isEmpty(pkPv)){
                        sql = "select * from PV_IP where PK_PV = ?";
                        List<PvIp> pvIpList = DataBaseHelper.queryForList(sql,PvIp.class,pkPv);
                        PvIp pvIp = pvIpList.stream().findFirst().orElse(null);
                        orderExecRecord.setVisitNo(pvIp!=null?pvIp.getIpTimes()+"":"");
                    }

                    String pkPi = pvEncounter!=null?pvEncounter.getPkPi():"";
                    if(!StringUtils.isEmpty(pkPi)){
                        sql = "select * from PI_MASTER where PK_PI = ?";
                        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pkPi);
                        orderExecRecord.setEmpiId(piMaster!=null?piMaster.getMpi():"");
                    }

                    String pkCnord = orderExecRecord!=null?orderExecRecord.getOrderId():"";
                    if(!StringUtils.isEmpty(pkCnord)&&!StringUtils.isEmpty(pkPv)){
                        sql = "select * from EX_ORDER_OCC where PK_PV = ? and PK_CNORD = ?";
                        List<ExOrderOcc> exOrderOccList = DataBaseHelper.queryForList(sql,ExOrderOcc.class,pkPv,pkCnord);
                        ExOrderOcc exOrderOcc = exOrderOccList.stream().findFirst().orElse(null);
                        Date planDate = exOrderOcc.getDatePlan();
                        orderExecRecord.setScheduleDateTime(planDate!=null?simpleDateFormat.format(planDate):"");
                    }

                }


            	orderExecRecord.setEncounterId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterId());
                orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());
                //医嘱类型转换
                orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));
                
                //剂型转换
                Map<String, Object> dosageFormMap = PskqDictionMapUtil.hisToPingDosageFormMap(orderExecRecord.getDrugFormCode());
                orderExecRecord.setDrugFormCode(CommonUtils.getPropValueStr(dosageFormMap, "code"));
                orderExecRecord.setDrugFormName(CommonUtils.getPropValueStr(dosageFormMap, "name"));
                orderExecRecord.setOrderId(PskqMesUtils.pskqSysCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqHisCode+orderExecRecord.getOrderId());
                orderExecRecord.setOrgCode("10");
                orderExecRecord.setOrgName("南方医科大学深圳口腔医院（坪山）");
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
		        SenderElement senderElement = new SenderElement("2000",
		                new SoftwareNameElement("HIS","医院信息管理系统"),
		                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
		                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
		        );
		        ReceiverElement receiverElement = new ReceiverElement("1200",
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
        }catch (Exception e){
            listener.exception(e.getMessage(),result,responseBody);
        }
    }
}
