package com.zebone.nhis.webservice.cxf.impl;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExStOcc;
import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;

import com.zebone.nhis.ma.pub.platform.pskq.utils.MapUtils;


import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.webservice.pskq.dao.CostDetailInpatDao;
import com.zebone.nhis.webservice.pskq.dao.SurgicalAnesthesiaDao;
import com.zebone.nhis.webservice.pskq.model.*;
import com.zebone.nhis.webservice.pskq.service.*;
import com.zebone.nhis.webservice.pskq.service.impl.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.webservice.pskq.utils.PskqMesUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.webservice.cxf.INHISPSKQWebService;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.pskq.annotation.EventLog;
import com.zebone.nhis.webservice.pskq.annotation.EventSearch;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.ReceiverElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.model.message.SenderElement;
import com.zebone.nhis.webservice.service.BlPubForWsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;


/**
 * ????????????????????????-????????????webservice??????????????????-json
 *
 * @author zhangtao
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class NHISPSKQWebServiceImpl implements INHISPSKQWebService {

    @Resource
    private BlPubForWsService blPubForWsService;

    @Resource
    private BdPubForWsMapper bdPubForWsMapper;
    @Resource
    private SchPubForWsMapper schPubForWsMapper;

    @Autowired
    private SurgicalAnesthesiaDao surgicalAnesthesiaDao;

    private final Logger logger = LoggerFactory.getLogger("nhis.PskqWebServiceLog");

    ApplicationUtils apputil = new ApplicationUtils();

    /**
     * ??????????????????
     */
    @Override
    @EventLog
    public String messageSynchronize(String param) {
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        String result = "";
        try {
            Class<?> mClass = Class.forName("com.zebone.nhis.webservice.cxf.impl.NHISPSKQWebServiceImpl");
            Method[] declaredMethods = mClass.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                // ?????????????????????????????????  MethodInterface
                boolean annotationPresent = declaredMethod.isAnnotationPresent(EventSearch.class);
                if (annotationPresent) {
                    // ???????????????????????????
                    EventSearch methodAnno = declaredMethod.getAnnotation(EventSearch.class);
                    // ???????????????????????????
                    String eventCode = methodAnno.value();
                    String[] codes = eventCode.split(",");
                    String paramCode = requestBody.getEvent().getEventCode();
                    //?????????????????????????????????????????????????????????
                    for (int i = 0; i < codes.length; i++) {
                        if (codes[i].equals(paramCode)) {
                            NHISPSKQWebServiceImpl nhispskqWebService = SpringContextHolder.getApplicationContext().getBean(NHISPSKQWebServiceImpl.class);
                            result = (String) nhispskqWebService.getClass().getDeclaredMethod(declaredMethod.getName(), String.class).invoke(nhispskqWebService, param);
//							result = (String) declaredMethod.invoke(mClass.newInstance(), param);
                            return result;
                        }
                    }
                }
            }
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    @Override
    public String test(String param) {
        System.out.println(param);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><result><resultCode>0</resultCode><resultDesc/></result>";
    }


    /**
     * ??????????????????
     */
    @EventSearch("E000101")
    public String messagePatientInfoUpdate(String param) {
        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody.Builder()
                .service(requestBody.getService())
                .event(requestBody.getEvent())
                .sender(requestBody.getReceiver())
                .receiver(requestBody.getSender())
                .build();
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        PatientService patientService = new PskqWebServicePatientServiceImpl();
        patientService.save(requestBody, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                ack.put("PATIENT", object);
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000101?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000101?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000101?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E000301")
    public String messagePatientMerge(String param) {
        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody.Builder()
                .service(requestBody.getService())
                .event(requestBody.getEvent())
                .sender(requestBody.getReceiver())
                .receiver(requestBody.getSender())
                .build();
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        PatientService patientService = new PskqWebServicePatientServiceImpl();
        patientService.merge(requestBody, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ??????????????????
     * ???????????????
     */
    @EventSearch("E000502,E000602")
    public String messageDeptInfoSynchronize(String param) {
        DepartmentService departmentService = SpringContextHolder.getBean(DepartmentService.class);
        String result =  departmentService.save(param);
        return result;
    }


    /**
     * ??????????????????
     * ???????????????
     */
    @EventSearch("E000801,E000901")
    public String messageEmpInfoSynchronize(String param) {

        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        EmployeeService employeeService = new EmployeeServiceImpl();
        employeeService.save(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                objects[0] = JSON.toJSONString(responseBody);
                responseBody.setAck(ack);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });

        return (String) objects[0];
    }

    /**
     * ??????????????????
     */
    @EventSearch("E007701")
    public String messageUrgeryPlanAddSynchronize(String param) {
        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        SurgeryRecordService surgeryRecordService = new SurgeryRecordServiceImpl();
        surgeryRecordService.addExOpSch(requestBody, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
            }
        });
        return (String) objects[0];

    }


    /**
     * ??????????????????????????????
     * ????????????
     * ????????????
     * ????????????
     * ????????????
     * ????????????
     * ????????????
     */
    @EventSearch("E008001,E008002,E008003,E008004,E008005,E008006")
    public String messageSurgeryPlanSynchronize(String param) {
        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        SurgeryRecordService surgeryRecordService = new SurgeryRecordServiceImpl();
        surgeryRecordService.update(requestBody, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];

    }


    /**
     * ???????????????

     @EventSearch("E150102") public String messageReHospitalizedCostSynchronize(String param) {
     Object[] objects = new Object[1];
     Map<String,Object> responseMap = new HashMap<>();
     ResponseBody responseBody = new ResponseBody();
     Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
     RequestBody requestBody = gson.fromJson(param,RequestBody.class);
     responseBody.setService(requestBody.getService());
     responseBody.setEvent(requestBody.getEvent());
     responseBody.setId(requestBody.getId());
     responseBody.setCreationTime(requestBody.getCreationTime());
     responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
     requestBody.getReceiver().getSoftwareName(),
     requestBody.getReceiver().getSoftwareProvider(),
     requestBody.getReceiver().getOrganization()));
     responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
     requestBody.getSender().getSoftwareName(),
     requestBody.getSender().getSoftwareProvider(),
     requestBody.getSender().getOrganization()));

     SettlementOutpatService settlementOutpatService = new SettlementOutpatServiceImpl();

     settlementOutpatService.refund(param, new ResultListener() {
     @Override public void success(Object object) {
     responseMap.put("ackCode","AA");
     responseMap.put("targetMessageId",requestBody.getId());
     responseMap.put("ackDetail","??????????????????");
     responseBody.setAck(responseMap);
     objects[0] = JSON.toJSONString(responseBody);
     }

     @Override public void error(Object object) {
     responseMap.put("ackCode","AE");
     responseMap.put("targetMessageId",requestBody.getId());
     responseMap.put("ackDetail",object);
     responseBody.setAck(responseMap);
     objects[0] = JSON.toJSONString(responseBody);
     }

     @Override public void exception(Object object) {
     responseMap.put("ackCode","AE");
     responseMap.put("targetMessageId",requestBody.getId());
     responseMap.put("ackDetail",object);
     responseBody.setAck(responseMap);
     objects[0] = JSON.toJSONString(responseBody);
     }
     });


     return (String) objects[0];

     }*/

    /**
     * ?????????????????????   ?????????

     @EventSearch("E150103") public String messageHospitalizedCostSynchronize(String param) {
     String result = "";
     ResponseBody responseBody = new ResponseBody();
     Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
     RequestBody requestBody = gson.fromJson(param,RequestBody.class);
     responseBody.setService(requestBody.getService());
     responseBody.setEvent(requestBody.getEvent());
     responseBody.setId(requestBody.getId());
     responseBody.setCreationTime(requestBody.getCreationTime());
     responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
     requestBody.getReceiver().getSoftwareName(),
     requestBody.getReceiver().getSoftwareProvider(),
     requestBody.getReceiver().getOrganization()));
     responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
     requestBody.getSender().getSoftwareName(),
     requestBody.getSender().getSoftwareProvider(),
     requestBody.getSender().getOrganization()));
     String reqID = requestBody.getId();
     try {
     ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
     PskqPubForWsService pskqPubForWsService = applicationContext.getBean("pskqPubForWsService", PskqPubForWsService.class);
     List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("COST_DETAIL_OUTPAT");//
     //??????????????????
     CostDetail surgeryCost = (CostDetail) MessageFactory.deserialization(list, new CostDetail());
     //????????????????????????
     String ret = pskqPubForWsService.disposeOpBlMessage(surgeryCost);
     if("succ".equals(ret)){
     result = resSuccReturn(responseBody, reqID);
     }else {
     result = resFailReturn(responseBody,ret,reqID);
     }
     }catch (Exception e){
     try {return	resFailReturn(responseBody,e.getMessage(),reqID);} catch (IllegalAccessException e1) {e1.printStackTrace();}
     }
     return result;
     }
     */

    /**
     * ????????????????????????
     *
     * @param content ????????????
     * @param id      ??????????????????
     */
    private String resFailReturn(ResponseBody responseBody, String content, String id) throws IllegalAccessException {
        Map<String, Object> responseMap = Maps.newHashMap();
        responseMap.put("ackCode", "AE");
        responseMap.put("ackDetail", "???????????????????????????:" + content + "?????????????????????");
        responseMap.put("targetMessageId", id);
        responseBody.setAck(responseMap);
        logger.info("?????????:???" + responseBody.getService().getServiceName() + "?????????" + content);
        return JSON.toJSONString(responseBody);
    }


    /**
     * ???????????????????????????
     *
     * @param id ??????????????????
     */
    private String resSuccReturn(ResponseBody responseBody, String id) throws IllegalAccessException {

        Map<String, Object> map = new HashMap<>();
        map.put("ackCode", "AA");
        map.put("ackDetail", "?????????????????????");
        map.put("targetMessageId", id);
        responseBody.setAck(map);
        logger.info("?????????:???" + responseBody.getService().getServiceName() + "????????????????????????");
        return JSON.toJSONString(responseBody);
    }


    /**
     * ??????????????????
     */
    @EventSearch("E005301")
    public String messageReserveOutpatient(String param) {

        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody.Builder()
                .service(requestBody.getService())
                .event(requestBody.getEvent())
                .sender(requestBody.getReceiver())
                .receiver(requestBody.getSender())
                .build();
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
//        ReserveOutpatientService reserveOutpatientService = ReserveOutpatientServiceImpl.newInstance();
//        reserveOutpatientService.save(requestBody, new ResultListener() {
//            @Override
//            public void success(Object object) {
//                ack.put("ackCode", "AA");
//                ack.put("ackDetail", "??????????????????");
//                ack.put("RESERVE_OUTPATIENT", object);
//                responseBody.setAck(ack);
//                objects[0] = JSON.toJSONString(responseBody);
//                logger.info("???E005301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
//            }
//
//            @Override
//            public void error(Object object) {
//                ack.put("ackCode", "AE");
//                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
//                responseBody.setAck(ack);
//                objects[0] = JSON.toJSONString(responseBody);
//                logger.info("???E005301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
//            }
//
//            @Override
//            public void exception(Object object) {
//                ack.put("ackCode", "AE");
//                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
//                responseBody.setAck(ack);
//                objects[0] = JSON.toJSONString(responseBody);
//                logger.info("???E005301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
//            }
//        });

        try {


            PiMasterRegVo regvo = new PiMasterRegVo();
            Map<String, Object> message = requestBody.getMessage();
            if (!message.containsKey("RESERVE_OUTPATIENT") && message.get("RESERVE_OUTPATIENT") == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[????????????????????????]");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);

            }
            //?????????????????????
            User user = PskqMesUtils.getUserExt(requestBody.getSender().getId());
            if (user == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "??????his???????????????????????????his???????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }

            List<DataElement> dataElement = (List<DataElement>) message.get("RESERVE_OUTPATIENT");
            ReserveOutpatient reserveOutpatient = (ReserveOutpatient) MessageFactory.deserialization(dataElement, new ReserveOutpatient());

            String reserveDateTime = reserveOutpatient.getReserveDateTime();
            if (CommonUtils.isEmptyString(reserveDateTime)) {

                ack.put("ackCode", "AE");
                ack.put("ackDetail", "???????????????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            //????????????????????????????????????
            String regDate = com.zebone.nhis.common.support.DateUtils.dateToStr("yyyyMMdd", com.zebone.nhis.common.support.DateUtils.parseDate(reserveDateTime, "yyyy-MM-dd HH:mm:ss"));
            String type = "1";
            String dateToStr = com.zebone.nhis.common.support.DateUtils.dateToStr("yyyyMMdd", new Date());
            if (dateToStr.equals(regDate) && "zzj".equals(user.getCodeEmp())) {
                type = "0";
            }
            String codePi = reserveOutpatient.getPkPatient().split("_")[2];
            //??????????????????????????????regvo
            PiMaster piMaster = DataBaseHelper.queryForBean("select * from PI_MASTER where CODE_PI = ? AND DEL_FLAG='0' ",
                    PiMaster.class, codePi);
            if (piMaster == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "HIS?????????CODE_PI???" + codePi + "???????????????,???????????????????????????3?????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            List<PiLock> listLock = DataBaseHelper.queryForList("select * from pi_lock where pk_pi=?", PiLock.class, piMaster.getPkPi());
            if (null != listLock && listLock.size() > 0) {
                if ("3".equals(listLock.get(0).getEuLocktype())) {
                    String pkPv = DataBaseHelper.queryForScalar("SELECT pk_Pv pkPv from (SELECT PK_PV from BL_OP_DT WHERE FLAG_SETTLE='0' and DEL_FLAG='0' and PK_PI=?  GROUP BY PK_PV) a WHERE ROWNUM=1", String.class, piMaster.getPkPi());
                    if (!CommonUtils.isEmptyString(pkPv)) {
                        String amount = DataBaseHelper.queryForScalar("SELECT SUM(AMOUNT) amount from BL_OP_DT where PK_PV=? and FLAG_SETTLE='0' and DEL_FLAG='0'", String.class, pkPv);
                        String datestr = DataBaseHelper.queryForScalar("SELECT TO_CHAR(DATE_CLINIC,'yyyy-mm-dd') datestr from PV_ENCOUNTER WHERE PK_PV=?", String.class, pkPv);

                        ack.put("ackCode", "AE");
                        ack.put("ackDetail", "??????" + datestr + "????????????????????????????????????" + amount + "????????????????????????????????????????????????????????????????????????");
                        responseBody.setAck(ack);
                        return JSON.toJSONString(responseBody);
                    }
                } else {

                    ack.put("ackCode", "AE");
                    ack.put("ackDetail", "???????????????????????????????????????????????????");
                    responseBody.setAck(ack);
                    return JSON.toJSONString(responseBody);
                }
            }
            ApplicationUtils.copyProperties(regvo, piMaster);
            regvo.setTs(new Date());
            //???????????????
            String pkSch = reserveOutpatient.getScheduleId();
            SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
                    SchSch.class, pkSch);
            //1.???????????????????????????
            if (schSch == null || "1".equals(schSch.getFlagStop())) {

                ack.put("ackCode", "AE");
                ack.put("ackDetail", "????????????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }

            if (schSch.getCntTotal().intValue() <= schSch.getCntUsed().intValue()) {

                ack.put("ackCode", "AE");
                ack.put("ackDetail", "????????????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            //2.????????????????????????
            List<Map<String, Object>> schAppList = DataBaseHelper.queryForList("SELECT * FROM sch_appt WHERE eu_status < '9' and flag_cancel ='0' AND pk_sch=? and pk_pi=?", new Object[]{schSch.getPkSch(), piMaster.getPkPi()});
            if (schAppList.size() > 0) {


                ack.put("ackCode", "AE");
                ack.put("ackDetail", "??????????????????????????????????????????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }

            regvo.setPkSch(pkSch);//????????????
            regvo.setPkDateslotsec(reserveOutpatient.getReserveSourceId());//????????????
            regvo.setEuSchclass("0");//????????????
            regvo.setPkSchplan(schSch.getPkSchplan());
            regvo.setPkSchsrv(schSch.getPkSchsrv());
            SchSrv schSrv = DataBaseHelper.queryForBean("select * from sch_srv where eu_schclass = '0' and pk_schsrv = ?",
                    SchSrv.class, schSch.getPkSchsrv());
            if (schSrv != null && "9".equals(schSrv.getEuSrvtype())) {
                regvo.setEuPvtype("2");//????????????
            } else {
                regvo.setEuPvtype("1");//????????????
            }
            regvo.setPkSchres(schSch.getPkSchres());
            regvo.setPkDateslot(schSch.getPkDateslot());
            regvo.setDateAppt(com.zebone.nhis.common.support.DateUtils.parseDate(com.zebone.nhis.common.support.DateUtils.dateToStr("YYYY-MM-DD", schSch.getDateWork()), "YYYY-MM-DD"));//????????????
            //???????????????2500-????????????  2510-?????? ; 2520-??????160 ????????????????????????020100
            regvo.setOrderSource(reserveOutpatient.getReserveCahannelCode());
            //??????????????????????????????????????????-???????????????
            List<SchTicket> ticketlist = new ArrayList<SchTicket>();
            String sql = "select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and flag_stop='0' and FLAG_USED = '0' ";
            if ("0".equals(type)) {
                ticketlist = DataBaseHelper.queryForList(sql, SchTicket.class, pkSch);
                type = "1";
            } else {
                sql = sql + " and FLAG_APPT = ?";
                ticketlist = DataBaseHelper.queryForList(sql, SchTicket.class, pkSch, type);
            }

            if (ticketlist.size() <= 0) {

                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);

            }
            SchTicket ticket = new SchTicket();
            boolean haveTicketNo = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
                    Integer.class, new Object[]{schSch.getPkSch()}) > 0;
            //2.????????????
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            TicketPubService ticketPubService = applicationContext.getBean("ticketPubService", TicketPubService.class);
            RegSyxService regSyxService = applicationContext.getBean("regSyxService", RegSyxService.class);
            if (haveTicketNo) {
                // ????????????
                ticket = ticketPubService.getUnusedAppTicketExt(pkSch, regvo.getPkDateslotsec(), type);
            } else {
                ticket = ticketPubService.getUnusedAppTicketFromSchExt(pkSch, type);
            }
            //3.???????????????????????????????????????????????????
            DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
            DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
            TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
            try {
                transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
                regvo = regSyxService.saveApptSchRegInfo(regvo, schSch, ticket, haveTicketNo, user, false);
                reserveOutpatient.setReserveOutpatientId(regvo.getApptCode());
                reserveOutpatient.setOutpatientNo(piMaster.getCodeOp());
                List<DataElement> dataElementList = MessageBodyUtil.dataElementsReturnFactory(reserveOutpatient);
                dataSourceTransactionManager.commit(transStatus);

                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                ack.put("RESERVE_OUTPATIENT", dataElementList);
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            } catch (Exception e) {
                //????????????????????????
                if (haveTicketNo) {
                    ticketPubService.setTicketUnused(ticket);
                } else {
                    ticketPubService.setTicketUnusedFromSch(ticket);
                }
                dataSourceTransactionManager.rollback(transStatus);
                ack.put("ackCode", "AE");
                ack.put("ackDetail", e.getMessage());
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }

        } catch (Exception exception) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", exception.getMessage());
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
    }



    /**
     * ??????????????????
     * RESERVE_STATE?????????   PAY_STATE?????????
     */
    @EventSearch("E005401")
    public String messageCancelReservation(String param) {

        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        ReserveOutpatientServiceImpl reserveOutpatientService = new ReserveOutpatientServiceImpl();
        reserveOutpatientService.edit(requestBody, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                ack.put("RESERVE_OUTPATIENT", object);
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });

        return (String) objects[0];
    }


    /**
     * ??????????????????
     */
    @EventSearch("E005501")
    public String messageRecordReservation(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());
        //List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("RESERVE_OUTPATIENT");
        PskqSchPubForServiceImpl pskqSchPubForService = new PskqSchPubForServiceImpl();
        pskqSchPubForService.findOrderByPkPi(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ackElement.setAckCode("AA");
                ackElement.setAckDetail("??????????????????");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(object);
                logger.info("???E005501?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005501?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005501?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }

    /**
     * ???????????????
     */
    @EventSearch("E150203")
    public String messageQueryPaySettlement(String param) {
        String result = "";
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param, RequestBody.class);
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("RESERVE_OUTPATIENT");
            //????????????
            ReserveOutpatient reserveOutpatient = (ReserveOutpatient) MessageFactory.deserialization(list, new ReserveOutpatient());

        } catch (Exception e) {

        } finally {
            return result;
        }
    }

    /**
     * ???????????????
     */
    @EventSearch(value = "E150301")
    public String messageSavePayment(String param) throws IllegalAccessException {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HH24mmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());
        AdvancePaymentService advancePaymentService = new AdvancePaymentServiceImpl();
        advancePaymentService.save(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ackElement.setAckCode("AA");
                ackElement.setAckDetail("??????????????????");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150301?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });

        return (String) objects[0];
    }

    /**
     * ????????????
     * ??????????????????????????????????????????????????????????????????
     * ???????????????HIS???HIS??????????????????
     * ???????????????????????????????????????
     * ???????????????????????????????????????????????????
     * ???????????????????????????
     */
    @EventSearch("E150101")
    public String messageSettlementOutpatService(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        SettlementOutpatServiceImpl settlementOutpatService = new SettlementOutpatServiceImpl();
        //????????????????????? , ????????????????????????
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        DataSourceTransactionManager dataSourceTransactionManager = SpringContextHolder.getApplicationContext().getBean(DataSourceTransactionManager.class);
        TransactionStatus transStatus = dataSourceTransactionManager.getTransaction(transDefinition);
        String result = "";
        try {

            result = settlementOutpatService.save(requestBody);
            dataSourceTransactionManager.commit(transStatus);
        } catch (Exception e) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", e.getMessage());
            responseBody.setAck(ack);
            result = JSON.toJSONString(responseBody);
            dataSourceTransactionManager.rollback(transStatus);
        }
        return result;
    }

    /**
     * ??????????????????
     */
    @EventSearch("E000401")
    public String messageGetPatient(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());

        PskqWebServicePatientServiceImpl pskqPatientService = new PskqWebServicePatientServiceImpl();
        pskqPatientService.findByEmpiId(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E000401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000401?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ??????????????????
     */
    @EventSearch("E000701")
    public String messageQueryOrganization(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());

        PskqWebServiceOrganizationServiceImpl pskqWebServiceOrganizationService = new PskqWebServiceOrganizationServiceImpl();
        pskqWebServiceOrganizationService.findByOrgCode(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E000701?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000701?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E000701?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }

    /**
     * ??????????????????
     */
    @EventSearch("E000702")
    public String messageQueryDepartment(String param) {
        DepartmentService departmentService =SpringContextHolder.getBean(DepartmentService.class);
        String result =  departmentService.findById(param);
        return result;
    }

    /**
     * ??????????????????
     * ???????????????????????????
     */
    @EventSearch("E001001")
    public String messageQueryEmployee(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
        employeeService.findById(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E001001?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E001001?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E001001?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ????????????
     * ????????????
     */
    @EventSearch("E001002")
    public String messageQueryEmployeeList(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());

        EmployeeServiceImpl employeeService = new EmployeeServiceImpl();
        employeeService.findById(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E001002?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E001002?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E001002?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }

    /**
     * ?????????????????????????????????????????????
     */
    @EventSearch("E150307")
    public String messageReceiveCostDetailInpat(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        CostDetailInpatServiceImpl costDetailInpatService = new CostDetailInpatServiceImpl();
        costDetailInpatService.save(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150307?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150307?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150307?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];

    }


    /**
     * ???????????????????????????????????????
     */
    @EventSearch("E150308")
    public String messageCancelCostDetailInpat(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        CostDetailInpatServiceImpl costDetailInpatService = new CostDetailInpatServiceImpl();
        costDetailInpatService.cancel(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150308?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150308?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150308?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ???/??????????????????
     */
    @EventSearch("E150201")
    public String messageQueryCostPaymentOut(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody.Builder()
                .service(requestBody.getService())
                .event(requestBody.getEvent())
                .sender(requestBody.getReceiver())
                .receiver(requestBody.getSender())
                .build();
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());
        PskqOpSettleService pskqOpSettleService = PskqOpSettleServiceImpl.newInstance();
        pskqOpSettleService.findByPkPiTime(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E150201?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150201?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E150201?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ?????????????????????
     */
    @EventSearch("E005502")
    public String messageQueryReserveCost(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        AckElement ackElement = new AckElement();
        ackElement.setTargetMessageId(requestBody.getId());
        PskqSchPubForServiceImpl pskqSchPubForService = new PskqSchPubForServiceImpl();
        pskqSchPubForService.findOrderByPkPi(param, new ResultListener() {
            @Override
            public void success(Object object) {
                objects[0] = JSON.toJSONString(object);
                logger.info("???E005502?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005502?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ackElement.setAckCode("AE");
                ackElement.setAckDetail("?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setQueryAck(ackElement);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???E005502?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ????????????????????????
     */
    @EventSearch("E006301,E006302,E006303,E006306,E006307,E006309")
    public String messageEditLabApply(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        LabApplyServiceImpl labApplyService = new LabApplyServiceImpl();
        labApplyService.editLabApply(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }

    /**
     * ????????????????????????
     */
    @EventSearch("E007101,E007102,E007103,E007104,E007105")
    public String messageEditExamApply(String param) {
        Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));
        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        ExamApplyServiceImpl examApplyService = new ExamApplyServiceImpl();
        examApplyService.editExamApply(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });
        return (String) objects[0];
    }


    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E150303")
    @Transactional(rollbackForClassName = {"Exception", "RuntimeException"})
    public String messageCostDetailInpat(String param) throws Exception {
        Map<String, Object> ack = new HashMap<>(16);
        ResponseBody responseBody = new ResponseBody();
        try {
            CostDetailInpatDao dao = SpringContextHolder.getBean(CostDetailInpatDao.class);
            Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param, RequestBody.class);

            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                    requestBody.getReceiver().getSoftwareName(),
                    requestBody.getReceiver().getSoftwareProvider(),
                    requestBody.getReceiver().getOrganization()));
            responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                    requestBody.getSender().getSoftwareName(),
                    requestBody.getSender().getSoftwareProvider(),
                    requestBody.getSender().getOrganization()));

            ack.put("targetMessageId", requestBody.getId());
            List<Map<String, Object>> list = (List<Map<String, Object>>) requestBody.getMessage().get("COST_DETAIL_INPAT_LIST");
            for (Map<String, Object> map : list) {
                List<DataElement> lists = (List<DataElement>) map.get("COST_DETAIL_INPAT");
                CostDetailInpat costDetailInpat = (CostDetailInpat) MessageFactory.deserialization(lists, new CostDetailInpat());
                String pvCode = costDetailInpat.getEncounterId().split("_")[3];
                String sql = "select * from PV_ENCOUNTER where CODE_PV = ?";
                PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class, pvCode);
                Optional.ofNullable(pvEncounter).orElseThrow(() -> new Exception("????????????????????????"));
                sql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
                BdOuDept dept = DataBaseHelper.queryForBean(sql, BdOuDept.class, costDetailInpat.getApplyDeptId());
                BdOuDept deptNs = DataBaseHelper.queryForBean(sql, BdOuDept.class, costDetailInpat.getWardId());
                Optional.ofNullable(deptNs).orElseThrow(() -> new Exception("????????????????????????"));
                sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
                BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, costDetailInpat.getEnterDoctorId());

                BlIpDt blIpDt = new BlIpDt();
                blIpDt.setPkOrg(pvEncounter.getPkOrg());
                blIpDt.setPkPi(pvEncounter.getPkPi());
                blIpDt.setPkPv(pvEncounter.getPkPv());
                String flagPd = "";
                String pkPd = "";
                String pkItem = "";
                String pkItemCate = "";
                String blPkItem = "";
                String nameCg = "";
                String pkUnit = "";
                String spec = "";
                Double priceOrg = 0.0;
                if ("05".equals(costDetailInpat.getChargeItemClassCode()) ||
                        "04".equals(costDetailInpat.getChargeItemClassCode()) ||
                        "03".equals(costDetailInpat.getChargeItemClassCode())) {
                    flagPd = "1";
                    sql = "select * from BD_PD where CODE = ?";
                    BdPd bdPd = DataBaseHelper.queryForBean(sql, BdPd.class, costDetailInpat.getChargeItemCode());
                    Optional.ofNullable(bdPd).orElseThrow(() -> new Exception("????????????????????????"));
                    pkPd = bdPd.getPkPd();
                    pkItemCate = bdPd.getPkItemcate();
                    blPkItem = pkPd;
                    nameCg = bdPd.getName();
                    pkUnit = bdPd.getPkUnitMin();
                    spec = bdPd.getSpec();
                    priceOrg = bdPd.getPrice();
                } else {
                    flagPd = "0";
                    sql = "select * from BD_ITEM where CODE = ?";
                    BdItem bdItem = DataBaseHelper.queryForBean(sql, BdItem.class, costDetailInpat.getChargeItemCode());
                    pkItem = bdItem.getPkItem();
                    pkItemCate = bdItem.getPkItemcate();
                    blPkItem = pkItem;
                    nameCg = bdItem.getName();
                    pkUnit = bdItem.getPkUnit();
                    priceOrg = bdItem.getPrice();
                    spec = bdItem.getSpec();
                }
                blIpDt.setFlagPd(flagPd);
                Date now = new Date();
                Map<String, Object> paramMap = new HashMap<>(16);
                paramMap.put("pkOrg", pvEncounter.getPkOrg());
                paramMap.put("pkPd", pkPd);
                paramMap.put("pkItem", pkItem);
                List<Map<String, Object>> billList = dao.billCode(paramMap);
                Map<String, Object> billMap = billList.stream().filter(a -> "bill".equals(a.get("billtype"))).findFirst().orElse(null);
                String codeBill = MapUtils.getString(billMap, "codebill");
                Map<String, Object> accMap = billList.stream().filter(a -> "acc".equals(a.get("billtype"))).findFirst().orElse(null);
                String codeAudit = MapUtils.getString(accMap, "codebill");
                blIpDt.setCodeBill(codeBill);
                blIpDt.setCodeAudit(codeAudit);
                blIpDt.setPkItemcate(pkItemCate);
                blIpDt.setPkItem(blPkItem);
                blIpDt.setNameCg(nameCg);
                blIpDt.setPkUnit(pkUnit);
                priceOrg = Double.valueOf(costDetailInpat.getChargeItemPrice());
                blIpDt.setPriceOrg(priceOrg);
                //??????????????????
                blIpDt.setPrice(priceOrg);
                blIpDt.setQuan(Double.valueOf(costDetailInpat.getChargeQuantity()));
//                blIpDt.setAmount(priceOrg * Double.valueOf(costDetailInpat.getChargeQuantity()));
                blIpDt.setAmount(Double.valueOf(costDetailInpat.getChargeTotal()));
                blIpDt.setAmountPi(Double.valueOf(costDetailInpat.getChargeTotal()));
                blIpDt.setAmountAdd(Double.valueOf(costDetailInpat.getChargeTotal()) - Double.valueOf(costDetailInpat.getOrigChargeTotal()));
                blIpDt.setPkOrgApp(pvEncounter.getPkOrg());
                blIpDt.setPkDeptApp(dept.getPkDept());
                blIpDt.setPkDeptNsApp(deptNs.getPkDept());
                blIpDt.setPkEmpApp(bdOuEmployee.getPkEmp());
                blIpDt.setNameEmpApp(bdOuEmployee.getNameEmp());
                blIpDt.setPkOrgEx(pvEncounter.getPkOrg());
                //????????????
                sql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
                BdOuDept bdOuDeptEx = DataBaseHelper.queryForBean(sql, BdOuDept.class, costDetailInpat.getExecDeptId());
                Optional.ofNullable(bdOuDeptEx).orElseThrow(() -> new Exception("????????????????????????"));
                blIpDt.setPkDeptEx(bdOuDeptEx.getPkDept());
                blIpDt.setPkCnord("");
                blIpDt.setDateHap(now);
                blIpDt.setPkPd(pkPd);
                blIpDt.setBatchNo("");
                blIpDt.setPkUnitPd(pkUnit);
                blIpDt.setPackSize(1.0);
                blIpDt.setPriceCost(priceOrg);
                blIpDt.setFlagSettle("0");
                blIpDt.setPkSettle("");
                blIpDt.setInfantNo("0");
                blIpDt.setPkPres("");
                blIpDt.setFlagInsu("0");
                blIpDt.setPkCgipBack("");
                blIpDt.setCodeCg(ApplicationUtils.getCode("0602"));
                blIpDt.setDateCg(now);
                blIpDt.setPkDeptCg(dept.getPkDept());

                blIpDt.setDelFlag("0");
                blIpDt.setAmountHppi(priceOrg * Double.valueOf(costDetailInpat.getChargeQuantity()));
                blIpDt.setEuBltype("9");
                blIpDt.setSortno(1);
                blIpDt.setDateEntry(now);
                blIpDt.setSpec(spec);
                blIpDt.setRatioDisc(1.0);
                blIpDt.setRatioSelf(1.0);
                sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
                BdOuEmployee oper = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, costDetailInpat.getChargeOperaId());
                if (oper == null) {
                    throw new Exception("??????????????????id");
                }
                blIpDt.setNameEmpCg(oper.getNameEmp());
                blIpDt.setPkEmpCg(oper.getPkEmp());
                blIpDt.setPkEmpEx(oper.getPkEmp());
                blIpDt.setNameEmpEx(oper.getNameEmp());
                sql = "select * from BD_HP where CODE = '9901'";
                BdHp bdHp = DataBaseHelper.queryForBean(sql, BdHp.class);
                blIpDt.setPkDisc(bdHp != null ? bdHp.getPkHp() : "");
                blIpDt.setRatioAdd(0.0);

                DataBaseHelper.insertBean(blIpDt);

            }
            ack.put("ackCode", "AA");
            ack.put("ackDetail", "??????????????????");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        } catch (Exception e) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", e.getMessage());
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
    }

    /**
     * ??????????????????
     * ???????????????
     */
    @EventSearch("E001108,E001208")
    public String messagePdInfoSynchronize(String param) {
        final Object[] objects = new Object[1];
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());
        MaterialItemService materialItemService = new MaterialItemServiceImpl();
        materialItemService.save(param, new ResultListener() {
            @Override
            public void success(Object object) {
                ack.put("ackCode", "AA");
                ack.put("ackDetail", "??????????????????");
                objects[0] = JSON.toJSONString(responseBody);
                responseBody.setAck(ack);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void error(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }

            @Override
            public void exception(Object object) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????[" + String.valueOf(object) + "]");
                responseBody.setAck(ack);
                objects[0] = JSON.toJSONString(responseBody);
                logger.info("???" + requestBody.getEvent() != null ? requestBody.getEvent().getEventCode() : "" + "?????????????????????????????????????????????????????????" + param + "--------------------" + objects[0]);
            }
        });

        return (String) objects[0];
    }


    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E150304")
    @Transactional(rollbackForClassName = {"Exception", "RuntimeException"})
    public String SurgicalAnesthesiaRefund(String param) {
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        ResponseBody responseBody = new ResponseBody();
        responseBody.setService(requestBody.getService());
        responseBody.setEvent(requestBody.getEvent());
        responseBody.setId(requestBody.getId());
        responseBody.setCreationTime(requestBody.getCreationTime());
        responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                requestBody.getReceiver().getSoftwareName(),
                requestBody.getReceiver().getSoftwareProvider(),
                requestBody.getReceiver().getOrganization()));
        responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                requestBody.getSender().getSoftwareName(),
                requestBody.getSender().getSoftwareProvider(),
                requestBody.getSender().getOrganization()));

        Map<String, Object> ack = new HashMap<>(16);
        ack.put("targetMessageId", requestBody.getId());


        List<Map<String, Object>> list = (List<Map<String, Object>>) requestBody.getMessage().get("COST_DETAIL_INPAT_LIST");
        List<CostDetailInpat> costDetailInpatList = list.stream().map(a -> {
            List<DataElement> dataElementList = (List<DataElement>) a.get("COST_DETAIL_INPAT");
            CostDetailInpat costDetailInpat = null;
            try {
                costDetailInpat = (CostDetailInpat) MessageFactory.deserialization(dataElementList, new CostDetailInpat());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return costDetailInpat;
        }).collect(Collectors.toList());
        String codePv = costDetailInpatList.stream().findFirst().get().getEncounterId().split("_")[3];

        String sql = "select * from PV_ENCOUNTER where CODE_PV = ?";

        PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class, codePv);

        String pkPv = pvEncounter.getPkPv();
        String orgCode = costDetailInpatList.stream().findFirst().get().getOrgCode();
        String pkPi = costDetailInpatList.stream().findFirst().get().getPkPatient();
        Map<String, Object> paramMap = new HashMap<>(16);
        paramMap.put("pkPv", pkPv);
        //1????????????????????????
        List<Map<String, Object>> queryResult = surgicalAnesthesiaDao.refundableItems(paramMap);

        List<Map<String, Object>> ableRefund = costDetailInpatList.stream()
                .map(t -> queryResult.stream().filter(s ->
                        Objects.equals(MapUtils.getString(s, "code"), t.getChargeItemCode())
                                &&
                                Objects.equals(Double.valueOf(MapUtils.getString(s, "quan")).intValue(), Double.valueOf(t.getChargeQuantity()).intValue()))
                        .findAny().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (ableRefund.size() == 0) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "??????????????????");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        List<String> pkCgipList = ableRefund.stream().map(a -> MapUtils.getString(a, "pkCgip")).collect(Collectors.toList());
        List<BlIpDt> blIpDtList = surgicalAnesthesiaDao.findBlIpDt(pkCgipList);
        List<Map<String, Object>> blIpDtCountList = surgicalAnesthesiaDao.findBlIpDtCount(pkCgipList);
        List<Map<String, Object>> pvEncounterList = surgicalAnesthesiaDao.findPvEncounter(pkPv);
        String valAttr = surgicalAnesthesiaDao.findValAttr(pkPv);
        Date now = new Date();
        List<BlIpDt> refundBlIpDtList = blIpDtList.stream().map(a -> {
            //??????BL_IP_DT
            BlIpDt blIpDt = new BlIpDt();
            BeanUtils.copyProperties(a, blIpDt);
            blIpDt.setPkCgip(NHISUUID.getKeyId());
            blIpDt.setQuan(-a.getQuan());
            blIpDt.setAmount(-a.getAmount());
            blIpDt.setAmountPi(-a.getAmountPi());
            blIpDt.setPkCgipBack(a.getPkCgip());
            blIpDt.setDateCg(now);
            blIpDt.setCreateTime(now);
            blIpDt.setTs(now);
            blIpDt.setAmountHppi(-a.getAmountHppi());
            blIpDt.setDateEntry(now);
            blIpDt.setCodeCg(ApplicationUtils.getCode("0602"));
            return blIpDt;
        }).collect(Collectors.toList());

        DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlIpDt.class), refundBlIpDtList);
        return "????????????";
    }


    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E003608")
    @Transactional
    public String orderExecRecord(String param) {
        Map<String, Object> ack = new HashMap<>(16);
        ResponseBody responseBody = new ResponseBody();
        Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param, RequestBody.class);
        try {


            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(new SenderElement(requestBody.getReceiver().getId(),
                    requestBody.getReceiver().getSoftwareName(),
                    requestBody.getReceiver().getSoftwareProvider(),
                    requestBody.getReceiver().getOrganization()));
            responseBody.setReceiver(new ReceiverElement(requestBody.getSender().getId(),
                    requestBody.getSender().getSoftwareName(),
                    requestBody.getSender().getSoftwareProvider(),
                    requestBody.getSender().getOrganization()));

            ack.put("targetMessageId", requestBody.getId());

            List<DataElement> dataElementList = (List<DataElement>) requestBody.getMessage().get("ORDER_EXEC_RECORD");

            OrderExecRecord orderExecRecord = (OrderExecRecord) MessageFactory.deserialization(dataElementList, new OrderExecRecord());

            if (StringUtils.isEmpty(orderExecRecord.getExecDeptId())) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "?????????????????????ID");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }

            if (StringUtils.isEmpty(orderExecRecord.getScheduleDateTime())) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "???????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }


            List<ExlistPubVo> exList = new ArrayList<>();
            ExlistPubVo exlistPubVo = new ExlistPubVo();
            exlistPubVo.setEuStatus("0");
            String sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
            BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, orderExecRecord.getExecOperaId());
            if (bdOuEmployee == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "???????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            User u = new User();
            u.setPkOrg(bdOuEmployee.getPkOrg());
            u.setPkEmp(bdOuEmployee.getPkEmp());
            u.setNameEmp(bdOuEmployee.getNameEmp());
            exlistPubVo.setPkEmpOcc(bdOuEmployee.getPkEmp());
            exlistPubVo.setNameEmpOcc(bdOuEmployee.getNameEmp());
            exlistPubVo.setPkEmpEx(bdOuEmployee.getPkEmp());
            exlistPubVo.setNameEmpEx(bdOuEmployee.getNameEmp());
            if ("1".equals(orderExecRecord.getSkinResult())) {
                exlistPubVo.setIsskt("Y");
            } else {
                exlistPubVo.setIsskt("N");
            }

            if (StringUtils.isEmpty(orderExecRecord.getPlacerOrderNo())) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "???????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            sql = "select * from CN_ORDER where ORDSN = ?";
            CnOrder cnOrder = DataBaseHelper.queryForBean(sql, CnOrder.class, orderExecRecord.getPlacerOrderNo());

            if (cnOrder == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql, PiMaster.class, cnOrder.getPkPi());
            sql = "select * from PV_ENCOUNTER where PK_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql, PvEncounter.class, cnOrder.getPkPv());
            sql = "select * from BD_OU_DEPT where CODE_DEPT = ?";
            BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql, BdOuDept.class, orderExecRecord.getExecDeptId());
            String skinResult = cnOrder.getEuSt();
            //????????????1????????????2??????
            if (!StringUtils.isEmpty(orderExecRecord.getSkinResult())) {
                ExStOcc exStOcc = new ExStOcc();
                exStOcc.setPkOrg(cnOrder.getPkOrg());
                exStOcc.setPkPi(piMaster.getPkPi());
                exStOcc.setPkPv(pvEncounter.getPkPv());
                exStOcc.setPkCnord(cnOrder.getPkCnord());
                exStOcc.setPkOrgOcc(cnOrder.getPkOrg());
                exStOcc.setPkDeptOcc(bdOuDept.getPkDept());
                exStOcc.setPkEmpOcc(bdOuEmployee.getPkEmp());
                exStOcc.setNameEmpOcc(bdOuEmployee.getNameEmp());
                //????????????
                exStOcc.setDateOcc(new Date());
                //??????????????????
                exStOcc.setDateBeginSt(new Date());
                //????????????
                exStOcc.setDuartion(1);
                //????????????
                exStOcc.setEuStmode("0");
                //?????????????????????
                exStOcc.setQuan(1.0);
                //????????????
                exStOcc.setFlagCal("0");
                //??????????????????(??????)
                exStOcc.setLengthCal(0.0);
                //??????????????????(??????)
                exStOcc.setWidthCal(0.0);
                //??????????????????
                exStOcc.setEuSkin("0");
                //?????????????????????
                exStOcc.setQuanRight(0.0);
                exStOcc.setFlagCalRight("0");
                exStOcc.setLengthValRight(0.0);
                exStOcc.setWidthValRight(0.0);
                exStOcc.setEuSkinRight("0");
                exStOcc.setBatchNo(new Random().nextInt(1000) + "");
                exStOcc.setDateChk(new Date());
                exStOcc.setPkDeptAp(cnOrder.getPkDept());
                exStOcc.setPkDeptNsAp(cnOrder.getPkDeptNs());
                exStOcc.setFlagChk("1");
                exStOcc.setDateChk(new Date());
                exStOcc.setPkEmpChk(bdOuEmployee.getPkEmp());
                exStOcc.setNameEmpChk(bdOuEmployee.getNameEmp());
                exStOcc.setCreator(u.getPkEmp());
                Date date = new Date();
                exStOcc.setCreateTime(date);
                exStOcc.setModifier(u.getPkEmp());
                exStOcc.setModityTime(date);
                exStOcc.setDelFlag("0");
                exStOcc.setTs(date);
                switch (orderExecRecord.getSkinResult()) {
                    case "1":
                        skinResult = "2";


                        exStOcc.setResult("0");


//                        sql = "update EX_ST_OCC set RESULT = '0' where PK_CNORD = ?";
//                        DataBaseHelper.execute(sql,cnOrder.getPkCnord());

                        break;
                    case "2":
                        skinResult = "3";
//                        sql = "update EX_ST_OCC set RESULT = '1' where PK_CNORD = ?";
//                        DataBaseHelper.execute(sql,cnOrder.getPkCnord());
                        exStOcc.setResult("0");
                        break;
                }
                DataBaseHelper.insertBean(exStOcc);
                sql = "update CN_ORDER set EU_ST = ? where PK_CNORD = ?";
                DataBaseHelper.execute(sql, skinResult, cnOrder.getPkCnord());



            }


            exlistPubVo.setEuAlways(cnOrder.getEuAlways());
            exlistPubVo.setOrdsnParent(cnOrder.getOrdsnParent());


            u.setPkDept(bdOuDept.getPkDept());
            if (bdOuDept == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "??????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            sql = "select * from ex_order_occ where PK_CNORD = ? and DATE_PLAN = to_date(?,'''yyyy-mm-dd hh24:mi:ss')";

            ExOrderOcc exOrderOcc = DataBaseHelper.queryForBean(sql, ExOrderOcc.class, cnOrder.getPkCnord(), orderExecRecord.getScheduleDateTime());
            if (exOrderOcc == null) {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "??????????????????????????????");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            exlistPubVo.setPkExocc(exOrderOcc.getPkExocc());
            exlistPubVo.setCodeOrdtype(cnOrder.getCodeOrdtype());
            exlistPubVo.setFlagBl(cnOrder.getFlagBl());
            exlistPubVo.setFlagDurg(cnOrder.getFlagDurg());
            exlistPubVo.setFlagBase(cnOrder.getFlagBase());
            exlistPubVo.setEuPvtype(cnOrder.getEuPvtype());
            exlistPubVo.setPkPres(cnOrder.getPkPres());
            exlistPubVo.setPkPv(cnOrder.getPkPv());
            exlistPubVo.setPkCnord(cnOrder.getPkCnord());
            exlistPubVo.setFlagFit(cnOrder.getFlagFit());
            exlistPubVo.setDescFit(cnOrder.getDescFit());
            exlistPubVo.setNameEmpOrd(cnOrder.getNameEmpOrd());
            exlistPubVo.setPkOrg(cnOrder.getPkOrg());
            exlistPubVo.setPkEmpOrd(cnOrder.getPkEmpOrd());
            exlistPubVo.setPkDeptNs(cnOrder.getPkDeptNs());
            exlistPubVo.setDatePlan(exOrderOcc.getDatePlan());
            exlistPubVo.setPkDept(cnOrder.getPkDept());
            exlistPubVo.setPkDeptExec(cnOrder.getPkDeptExec());
            exlistPubVo.setOrdsn(cnOrder.getOrdsn());
            exlistPubVo.setCodeSupply(cnOrder.getCodeSupply());
            exlistPubVo.setPkOrgOcc(exOrderOcc.getPkOrg());
            exlistPubVo.setQuanOcc(exOrderOcc.getQuanOcc());

            exlistPubVo.setBedNo(pvEncounter != null ? pvEncounter.getBedNo() : "");
            exlistPubVo.setNamePi(piMaster.getNamePi());
            exlistPubVo.setPkPi(piMaster.getPkPi());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            exlistPubVo.setDateStart(cnOrder.getDatePlanEx());
            exlistPubVo.setEuAlwaysFreq(cnOrder.getEuAlways());
            exlistPubVo.setDateOcc(exOrderOcc.getDateOcc());
            sql = "select * from BD_UNIT where PK_UNIT = ?";
            if (!StringUtils.isEmpty(cnOrder.getPkUnit())) {
                BdUnit unit = DataBaseHelper.queryForBean(sql, BdUnit.class, cnOrder.getPkUnit());
                exlistPubVo.setNameUnit(unit.getName());
                exlistPubVo.setPkUnit(unit.getPkUnit());
            }
            exlistPubVo.setFlagAp("1");
            exlistPubVo.setFlagBack("0");
            exlistPubVo.setFlagDe("0");
            exlistPubVo.setFlagSelf(cnOrder.getFlagSelf());
            exlistPubVo.setNameOrd(cnOrder.getNameOrd());
            exlistPubVo.setEuAlways(cnOrder.getEuAlways());
            exlistPubVo.setFlagBl(cnOrder.getFlagBl());
            exlistPubVo.setInfantNo(cnOrder.getInfantNo() + "");
            if ("1".equals(orderExecRecord.getSkinResult())) {
                //??????
                exlistPubVo.setEuSt("2");
            }

            if ("2".equals(orderExecRecord.getSkinResult())) {
                //??????
                exlistPubVo.setEuSt("3");
            }

            exlistPubVo.setPkOrd(cnOrder.getPkOrd());
            exlistPubVo.setCodeSupply(cnOrder.getCodeSupply());
            exlistPubVo.setOrdsnParent(cnOrder.getOrdsnParent());
            exlistPubVo.setFlagSt("1");
            sql = "select * from BD_TERM_FREQ where CODE = ?";
            if (!StringUtils.isEmpty(cnOrder.getCodeFreq())) {
                BdTermFreq freq = DataBaseHelper.queryForBean(sql, BdTermFreq.class, cnOrder.getCodeFreq());
                exlistPubVo.setNameFreq(freq.getName());
            }
            exlistPubVo.setEuPvtype(cnOrder.getEuPvtype());
            exlistPubVo.setPkOrg(cnOrder.getPkOrg());
            exlistPubVo.setPkDeptPv(cnOrder.getPkDept());
            exlistPubVo.setPackSize(cnOrder.getPackSize().intValue());
            exlistPubVo.setFlagCheck("1");
            exlistPubVo.setCodeOrdtype(cnOrder.getCodeOrdtype());
            exlistPubVo.setPkEmpEx(cnOrder.getPkEmpEx());
            exlistPubVo.setNameEmpEx(cnOrder.getNameEmpEx());
            exlistPubVo.setDatePlanEx(cnOrder.getDatePlanEx());
            exlistPubVo.setCntFreq(1);
            exlistPubVo.setRisEuStatus("0");


            exList.add(exlistPubVo);


            BlCgExPubService blCgExPubService = SpringContextHolder.getBean(BlCgExPubService.class);
//		//??????????????????????????????
            List<BlPubParamVo> gclist = blCgExPubService.execAndCg(exList, u);
//		//orderExecListMapper.updateTransApply(exList);//????????????????????????????????????cn_trans_apply.eu_status = '3'
//		//?????????????????????1000??????????????????????????????
//		Set<String> ordSet = new HashSet<String>();
//		for(ExlistPubVo ex:exList){
//			ordSet.add(ex.getPkCnord());
//		}
//		DataBaseHelper.update("UPDATE CN_TRANS_APPLY SET eu_status='3'where eu_status < '3' and pk_cnord in ("+ CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")");
//		DataBaseHelper.update("UPDATE cn_lab_apply SET eu_status='3'where eu_status < '3' and pk_cnord in ("+CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")");
//		//???????????????????????????????????????????????????????????? -EX0077  0-????????? ????????????
//		String isUpApplyStatus = ApplicationUtils.getSysparam("EX0077", false);
//		//????????????null????????????????????????
//		if(!"0".equals(isUpApplyStatus)) {
//			DataBaseHelper.update("UPDATE cn_ris_apply SET eu_status='3'where eu_status < '3' and pk_cnord in ("+CommonUtils.convertSetToSqlInPart(ordSet, "pk_cnord")+")");
//		}

            sql = "update EX_ORDER_OCC set EU_STATUS = '1' where PK_EXOCC = ?";
            DataBaseHelper.execute(sql, exOrderOcc.getPkEmpOcc());
            ack.put("ackCode", "AC");
            ack.put("ackDetail", "????????????");
            responseBody.setAck(ack);
//            dataSourceTransactionManager.commit(status);

            String sql1 = "update EX_ST_OCC set RESULT = ? where PK_CNORD = ?";
            if ("1".equals(orderExecRecord.getSkinResult())) {
                //??????
                sql1 = "update EX_ST_OCC set RESULT = '-' where PK_CNORD = ?";
                DataBaseHelper.execute(sql, cnOrder.getPkCnord());
//                dataSourceTransactionManager.commit(status);
            }

            if ("2".equals(orderExecRecord.getSkinResult())) {
                //??????
                sql1 = "update EX_ST_OCC set RESULT = '+' where PK_CNORD = ?";
                DataBaseHelper.execute(sql, cnOrder.getPkCnord());
//                dataSourceTransactionManager.commit(status);
            }


            return JSON.toJSONString(responseBody);
        } catch (Exception e) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", e.getMessage());
            responseBody.setAck(ack);
            String result = JSON.toJSONString(responseBody);
            return result;
        }


    }


    /**
     * ????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E270102")
    public synchronized String vitalSigns(String param) {
        VitalSignsService vitalSignsService = SpringContextHolder.getBean(VitalSignsService.class);
        String result = vitalSignsService.save(param);
        return result;
    }


    /**
     * ??????????????????
     *
     * @param param
     * @return
     */
    @EventSearch("E006119,E006101")
    public String e006119(String param) {
        ExamAppService examAppService = SpringContextHolder.getBean(ExamAppService.class);
        String reslut = examAppService.update(param);
        return reslut;
    }

    public static void main(String[] args) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        System.out.println(calendar1.get(Calendar.HOUR));
    }


}
