package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.bd.mk.BdTermFreq;
import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdOrg;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.module.sch.pub.SchSrvOrd;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.ma.pub.platform.pskq.model.ExamApply;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderExecRecord;
import com.zebone.nhis.ma.pub.platform.pskq.model.ReserveOutpatient;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.service.*;
import com.zebone.nhis.ma.pub.platform.pskq.service.impl.NurseService;
import com.zebone.nhis.ma.pub.platform.pskq.utils.*;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqPlatFormSendCnMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.base.bd.vo.BdItemVO;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.pskq.ViewResolver;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.param.OrderParam;
import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import com.zebone.nhis.ma.pub.platform.send.SendOtherPubHandler;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.BdOrdParam;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.OrderChargeItem;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.BdOrdVO;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.util.StringUtils;

/**
 * @author lijin
 */
@Service("PskqPlatFormSendService")
public class PskqPlatFormSendPubHandler extends PlatFormSendHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger("nhis.PskqWebServiceLog");

    /**
     * 新增状态
     */
    private final String ADD = "ADD";

    @Autowired
    private PskqPlatFormSendPatientHandler patientService;

    @Autowired
    private PskqPlatFormSendBdHandler baseDataService;

    @Autowired
    private PskqPlatFormSendBlHandler blService;

    @Autowired
    private PskqPlatFormSendCnHandler cnService;

    @Autowired
    private PskqPlatFormSendRegHandler pskqPlatFormSendRegHandler;

    /**
     * 患者信息
     * @param paramMap{codeEmp,pi【PiMaster(map)】}
     */
    @Override
    public void sendPiMasterMsg(Map<String, Object> paramMap) {

      patientService.getPatientInfo(paramMap, new ResultListener() {
          @Override
          public void success(String requestJson, String responseJson) {
        	  logger.info("发送患者信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
          }

          @Override
          public void error(String message, String requestJson, String responseJson) {
              logger.info("发送患者信息接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);

          }

          @Override
          public void exception(String message, String requestJson, String responseJson) {
              logger.error("发送收费项目异常，异常信息：" + "请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
          }
      });
    }

    /**
     * 发送收费项目
     * E001103
     */
    @Override
    public void sendBdItemMsg(Map<String, Object> paramMap) {
        baseDataService.getBdItemInfo(paramMap, new ResultListener() {
            @Override
            public void success( String requestJson, String responseJson) {
                logger.info("发送收费项目成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送收费项目接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送收费项目异常，异常信息：" + message +  "，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }


    /**
     * 主数据
     * E001101
     * @param paramMap{codeEmp,bdDefdoc:字典表集合【List<BdDefdoc>】含未变更内容,delPkDefdocs:删除的字典主键集合【List<String>】}
     */
    @Override
    public void sendBdDefDocMsg(Map<String, Object> paramMap) {

        baseDataService.sendBdDefDocMsg(paramMap,new ResultListener() {
            @Override
            public void success( String requestJson, String responseJson) {
                logger.info("发送主数据公共字典成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送主数据公共字典接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送主数据公共字典异常，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }

    /**
     * 发送医嘱项目
     * E001104
     */
    @Override
    public void sendBdOrdMsg(Map<String, Object> paramMap) throws IllegalAccessException, IOException {
        try{
            String param = JSON.toJSONString(paramMap);
            BdOrdParam bdOrdParam = JsonUtil.readValue(param,BdOrdParam.class);
            BdOrdVO bdOrdVO = baseDataService.findBdOrdById(bdOrdParam.getBdOrd().getPkOrd());
            RequestBody requestBody = new RequestBody.Builder()
                    .service("S0011","术语注册服务")
                    .event("E001105","新增医嘱项目与物价项目对照")
                    .sender().receiver().build();
            String sql = "select * from bd_ou_employee where code_emp = ? AND DEL_FLAG = '0' ";

            BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,new Object[]{bdOrdParam.getCodeEmp()});

            for (BdItemVO bdItemVO : bdOrdVO.getBdItemVOList()) {
                OrderChargeItem orderChargeItem = new OrderChargeItem();
                orderChargeItem.setOrderChargeItemId(bdItemVO.getPkOrdItem());
                orderChargeItem.setOrderItemId(bdOrdVO.getPkOrd());
                orderChargeItem.setChargeItemId(bdItemVO.getPkItem());
                orderChargeItem.setChargeQuantity(bdItemVO.getAmount() != null ? bdItemVO.getAmount().toString() : "");
                orderChargeItem.setEnterOperaId(bdOuEmployee.getIdMaster());
                orderChargeItem.setEnterOperaName(bdOuEmployee.getNameEmp());
                orderChargeItem.setEnterDateTime(new Date());
                orderChargeItem.setCancelFlag("0");
                List<DataElement> dataElements = ViewResolver.dataElementsFactory(orderChargeItem);
                Map<String, Object> message = new HashMap<>(16);
                message.put("ORDER_CHARGE_ITEM", dataElements);
                requestBody.setMessage(message);
                String requestString = JSON.toJSONString(requestBody);
                String result = RestTemplateUtil.postForString(requestString);
            }
        }catch (Exception e){

        }

        baseDataService.getBdOrdInfo(paramMap, new ResultListener() {
            @Override
            public void success( String requestJson, String responseJson) {
                logger.info("发送医嘱项目成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送医嘱项目接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送医嘱项目异常，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }

    /**
     *  发送换床
     * @param paramMap
     */

    @Override
    public void sendBedChange(Map<String, Object> paramMap) {
        paramMap.put("methodType", "sendBedChangeMsg");
        cnService.getEncounterIpAdtChangeById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送换床成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送换床出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送换床出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });

    }

    /**
     * 发送科室信息
     * E000502
     * @param paramMap{codeEmp,dept【BdOuDept】,STATUS:_ADD,_UPDATE,_DELETE}
     */
    @Override
    public void sendBdOuDeptMsg(Map<String, Object> paramMap) {
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendBdOuDeptMsg",paramMap);

    }

    /**
     * 发送人员信息
     * E000801
     * @param paramMap{codeEmp,emp【BdOuDept(map)】,empJobs【List<Map<String,Object>(BdOuEmpjob)>】STATUS:_ADD,_UPDATE,_DELETE}
     */
    @Override
    public void sendBdOuEmpMsg(Map<String, Object> paramMap) {
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendEmpMsg",paramMap);

    }

    /**
     * 发送用户信息
     * @param paramMap{codeEmp,user【BdOuUser(map)】,STATUS:_ADD,_UPDATE,_DELETE}
     */
    @Override
    public void sendBdOuUserMsg(Map<String, Object> paramMap) {
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendUserMsg",paramMap);
    }
    
    /**
     * 发送药品信息
     * E001109
     */
    @Override
    public void sendBdPdMsg(Map<String, Object> paramMap) {
    	baseDataService.getBdPdInfo(paramMap, new ResultListener() {
            @Override
            public void success( String requestJson, String responseJson) {
                logger.info("发送药品信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送药品信息接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送药品信息异常，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }
    /**
     * 发送物资信息
     * @param paramMap{pd:BdPd对象,STATUS:_ADD,_UPDATE,_DELETE}
     * E001108
     */
    @Override
    public void sendBdMaterMsg(Map<String, Object> paramMap) {
    	baseDataService.getBdMaterInfo(paramMap, new ResultListener() {
            @Override
            public void success( String requestJson, String responseJson) {
            	logger.info("发送物资项目成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
            	logger.info("发送物资项目接收方出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
            	logger.error("发送物资项目异常，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }
   

    /**
     * 入科
     */
    @Override
    public void sendDeptInMsg(Map<String, Object> paramMap) {
    	paramMap.put("methodType", "sendDeptInMsg");
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送入科成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送入科出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送入科出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }
    /**
     * 取消入科
     */
    @Override
    public void sendCancelDeptInMsg(Map<String, Object> paramMap) {
    	paramMap.put("methodType", "sendCancelDeptInMsg");
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送取消入科成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送取消入科出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送取消入科出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }

    /**
     * 入院登记
     */
    @Override
    public void sendPvInMsg(Map<String, Object> paramMap) {
    	paramMap.put("methodType", "sendPvInMsg");
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送入院登记成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送入院登记出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送入院登记息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    	String isPiAdd = CommonUtils.getPropValueStr(paramMap, "isPiAdd");
        if(!CommonUtils.isEmptyString(isPiAdd)) {
        	Map<String,Object> msgPiParam = new HashMap<String,Object>();
    		msgPiParam.put("pkPi", CommonUtils.getPropValueStr(paramMap, "pkPi"));
    		msgPiParam.put("isAdd",isPiAdd);		
            sendPiMasterMsg(msgPiParam);
        }
    	//发送入院登记信息到资源池平台
        SendOtherPubHandler.invokeOther(PskqResourcePoolSendHandler.class,"sendPvInMsg",paramMap);
    	
    }
    /**
     * 取消入院
     */
    @Override
    public void sendPvCancelInMsg(Map<String, Object> paramMap) {
    	paramMap.put("methodType", "sendPvCancelInMsg");
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送取消入院登记成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送取消入院登记出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送取消入院登记息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }
    /**
     * 发送出院
     */
    @Override
    public void sendPvOutMsg(Map<String, Object> paramMap) {
    	
    	paramMap.put("methodType", "sendPvOutMsg");
    	
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送出院登记成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送出院登记出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送出院登记息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    	
       	//发送出院信息到资源池平台
        SendOtherPubHandler.invokeOther(PskqResourcePoolSendHandler.class,"sendPvOutMsg",paramMap);
    	
    }
    /**
     * 取消出院
     */
    @Override
    public void sendPvCancelOutMsg(Map<String, Object> paramMap) {
    	paramMap.put("methodType", "sendPvCancelOutMsg");
    	cnService.getEncounterIpAdtById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送取消出院登记成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送取消出院登记出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送取消出院登记息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }


    /**
     * 挂号
     * E002301
     * @param paramMap
     */
    @Override
    public void sendPvOpRegMsg(Map<String, Object> paramMap) {
		
    	//发送挂号信息到分诊平台
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendReg",paramMap);

	 	//发送挂号信息到集成平台
        pskqPlatFormSendRegHandler.sendRegisterInfo(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送挂号信息成功，请求：{},响应结果:{}",requestJson,responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("挂号结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送挂号信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
        
        //发送患者信息
        String isPiAdd = CommonUtils.getPropValueStr(paramMap, "isPiAdd");
        if(!CommonUtils.isEmptyString(isPiAdd)) {
        	Map<String,Object> msgPiParam = new HashMap<String,Object>();
    		msgPiParam.put("pkPi", CommonUtils.getPropValueStr(paramMap, "pkPi"));
    		msgPiParam.put("isAdd",isPiAdd);		
            sendPiMasterMsg(msgPiParam);
        }
        
	 	//发送挂号信息到资源池平台
        SendOtherPubHandler.invokeOther(PskqResourcePoolSendHandler.class,"sendReg",paramMap);

    }

    @Override
    public void sendPvOpCancelRegMsg(Map<String, Object> paramMap) {
    	
    	//发送退号信息到分诊平台
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendCancelReg",paramMap);

       	 	//发送退号信息到集成平台
        pskqPlatFormSendRegHandler.sendRetreatInfo(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送退挂号信息成功，请求：{},响应结果:{}",requestJson,responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("退挂号结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送退挂号信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
        
        //发送退号信息到资源池平台
        SendOtherPubHandler.invokeOther(PskqResourcePoolSendHandler.class,"sendCancelReg",paramMap);
	 	
    }

    /**
     * 诊毕消息
     */
    @Override
    public void sendFinishClinicMsg(Map<String, Object> paramMap) {
    	//发送停诊信息到资源池
        SendOtherPubHandler.invokeOther(WePayService.class,"sendStayPayNotice",paramMap);
    }

    @Override
    public void sendOpO09Msg(Map<String, Object> paramMap) {
        List<Map<String,Object>> deletingObject =(List<Map<String,Object>> )paramMap.get("delMapList");
        if(deletingObject != null && deletingObject.size()>0){
            cnService.sendOpCnorderdelete(deletingObject, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱删除信息成功，请求：{},响应结果:{}",requestJson,responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱删除结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱删除信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }

        List<Object> addCnOrdList = (List<Object>)paramMap.get("addCnOrdList");
        List<Object> editCnOrdList = (List<Object>)paramMap.get("editCnOrdList");

        if(addCnOrdList != null && addCnOrdList.size()>0){
            cnService.sendOpCnorderNew(addCnOrdList,true, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱新增信息成功，请求：{},响应结果:{}",requestJson,responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱新增结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱新增信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }
        if(editCnOrdList != null && editCnOrdList.size()>0){
            cnService.sendOpCnorderUpdate(editCnOrdList,true, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱更新信息成功，请求：{},响应结果:{}",requestJson,responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱更新结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱更新信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }
    }

    /**
     * 发送门诊收费信息至平台
     * E150101
     * E150102
     */
    @Override
    public void sendBlOpSettleMsg(Map<String, Object> paramMap) {
        String Control =  CommonUtils.getPropValueStr(paramMap,"Control");
        //此状态控制HIS接收平台发的缴费消息，缴费后控制何时发送缴费成功消息（为空发送消息）
        String Source =  CommonUtils.getPropValueStr(paramMap,"Source");
        if(CommonUtils.isEmptyString(Source)) {
        	if("OK".equals(Control)){//记费
                SendOtherPubHandler.invokeOther(PskqPlatFormSendBlHandler.class,"wbSendStRisInfo",paramMap);

                blService.sendBlOpSettleIncomeMsg(paramMap, new ResultListener() {
                    @Override
                    public void success(String requestJson, String responseJson) {
                        logger.info("发送门急诊收费信息成功，请求是：{}，响应结果：{}",requestJson,responseJson);
                    }
                    @Override
                    public void error(String message, String requestJson, String responseJson) {
                        logger.info("发送门急诊收费出错信息{}，请求是：{},响应结果为：{}",message,requestJson,responseJson);
                    }

                    @Override
                    public void exception(String message, String requestJson, String responseJson) {
                        logger.error("发送门急诊收费信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                    }
                });

            }else if("CR".equals(Control)){//退费
              blService.sendOpRefundNews(paramMap, new ResultListener() {
                    @Override
                    public void success(String requestJson, String responseJson) {
                        logger.info("发送门急诊退费信息成功，请求是：{}，响应结果：{}",requestJson,responseJson);
                    }
                    @Override
                    public void error(String message, String requestJson, String responseJson) {
                        logger.info("发送门急诊退费出错信息{}，请求是：{},响应结果为：{}",message,requestJson,responseJson);
                    }

                    @Override
                    public void exception(String message, String requestJson, String responseJson) {
                        logger.error("发送门急诊退费信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                    }
                });
            }
        }
    }

    /**
     * 检验计费
     */
    @Override
    public void sendBlMedApplyMsg(Map<String, Object> paramMap) {
    	String Control =  CommonUtils.getPropValueStr(paramMap,"Control");
    	String type =  CommonUtils.getPropValueStr(paramMap,"type");
    	if("OK".equals(Control)){//记费
    		if("I".equals(type)){
    			cnService.getIpCostDetailInfo(paramMap, new ResultListener() {
	                @Override
	                public void success(String requestJson, String responseJson) {
	                    logger.info("发送住院检验检查收费信息成功，请求是：{}，响应结果：{}",requestJson,responseJson);
	                }
	                @Override
	                public void error(String message, String requestJson, String responseJson) {
	                    logger.info("发送住院检验检查收费信息出错{}，请求是：{},响应结果为：{}",message,requestJson,responseJson);
	                }
	
	                @Override
	                public void exception(String message, String requestJson, String responseJson) {
	                    logger.error("发送住院检验检查收费信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
	                }
	            });
    		}
        }else if("CR".equals(Control)){//退费
        	cnService.getIpCostDetailInfoRtn(paramMap, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送住院检查退费信息成功，请求是：{}，响应结果：{}",requestJson,responseJson);
                }
                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("发送住院检查退费信息出错{}，请求是：{},响应结果为：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送住院检查退费信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }
    }

    @Override
    public void sendCnDiagMsg(Map<String, Object> paramMap) {
        cnService.getDiagInfo(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送诊断信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送诊断信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送诊断信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });

    }
    /**
     * 发送检验检查申请
     * E003801
     */
    @Override
    public void sendCnMedApplyMsg(Map<String, Object> paramMap) {
    	
    	//检查
    	if("ris".equals(paramMap.get("type"))){
    		 cnService.getRisInfo(paramMap, new ResultListener() {
                 @Override
                 public void success(String requestJson, String responseJson) {
                     logger.info("发送检查成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                 }

                 @Override
                 public void error(String message, String requestJson, String responseJson) {
                     logger.info("发送检查信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
                 }

                 @Override
                 public void exception(String message, String requestJson, String responseJson) {
                     logger.error("发送检查信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                 }
             });
        //检验
    	}else if ("lis".equals(paramMap.get("type"))) {
    		 cnService.getIpLisMsg(paramMap, new ResultListener() {
                 @Override
                 public void success(String requestJson, String responseJson) {
                     logger.info("发送【住院检验】信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                 }

                 @Override
                 public void error(String message, String requestJson, String responseJson) {
                     logger.info("发送【住院检验】信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
                 }

                 @Override
                 public void exception(String message, String requestJson, String responseJson) {
                     logger.error("发送【住院检验】信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                 }
             });
		}else if("RisLis".equals(paramMap.get("type"))){
			cnService.getIpLisOrRisMsg(paramMap, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送【住院检验或检查】信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("发送【住院检验或检查】信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送【住院检验或检查】信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
		}else {
			//发送医嘱消息
            if(paramMap.containsKey("param")){
                String param = (String) paramMap.get("param");
                OrderParam list = JsonUtil.readValue(param,OrderParam.class);
                if(paramMap.containsKey("stop")){
                    list.setStop("1");
                }
                OrderInpatService orderInpatService = new OrderInpatService();
                orderInpatService.sendOrderInpat(list, new ResultListener() {
                    @Override
                    public void success(String requestJson, String responseJson) {
                        logger.info("发送【住院医嘱】信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                    }

                    @Override
                    public void error(String message, String requestJson, String responseJson) {
                        logger.info("发送【住院医嘱】信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
                    }

                    @Override
                    public void exception(String message, String requestJson, String responseJson) {
                        logger.error("发送【住院医嘱】信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                    }
                });
            }

		}

    }
    /**
     * 门诊检查 检验申请  -- 加医嘱消息
     */
    @Override
    public void sendCnOpAppMsg(Map<String, Object> paramMap) {
    	cnService.getOpLisAndRisMsg(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送门诊检查验申请，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送门诊检查验申请信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送门诊检查验申请信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });

        List<Object> addOrdList=(List<Object>) paramMap.get("addOrdList");
        List<Object> updateOrdList=(List<Object>) paramMap.get("updateOrdList");
        List<Map<String,Object>> delPSKQO09MapList=(List<Map<String,Object>>) paramMap.get("delPSKQO09MapList");
        if(addOrdList != null && addOrdList.size()>0) {
            cnService.sendOpCnorderNew(addOrdList, false, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱新增信息成功，请求：{},响应结果:{}", requestJson, responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱新增结果：{},请求信息：{},响应结果：{}", message, requestJson, responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱新增信息出错，异常信息：" + message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }
        if(updateOrdList != null && updateOrdList.size()>0){
            cnService.sendOpCnorderUpdate(updateOrdList,false, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱更新信息成功，请求：{},响应结果:{}",requestJson,responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱更新结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱更新信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }
        if(delPSKQO09MapList != null && delPSKQO09MapList.size()>0){
            cnService.sendOpCnorderdelete(delPSKQO09MapList, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                    logger.info("发送门诊医嘱删除信息成功，请求：{},响应结果:{}",requestJson,responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                    logger.info("门诊医嘱删除结果：{},请求信息：{},响应结果：{}",message,requestJson,responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                    logger.error("发送门诊医嘱删除信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }

    }

    /**
     * 发送住院预交金
     * @param paramMap
     */
    @Override
    public void sendDepositMsg(Map<String, Object> paramMap) {
        paramMap.put("status","OK");
        blService.getIpDepositMsg(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送住院预交金新增信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送住院预交金新增信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送住院预交金新增信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });

    }



    /**
     * 护士站取消执行、新增执行
     * paramMap.put("exlist", exlist);
		paramMap.put("typeStatus", "DEL");
     */
    @Override
    public void sendExConfirmMsg(Map<String, Object> paramMap) {
    	String ordStatus = MapUtils.getString(paramMap,"typeStatus","");

    	if(ADD.equals(ordStatus)){
            NurseService nurseService = SpringContextHolder.getBean(NurseService.class);
            nurseService.universalExecution(paramMap, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {

                }

                @Override
                public void error(String message, String requestJson, String responseJson) {

                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {

                }
            });
    	}else if ("DEL".equals(ordStatus)){//取消执行

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OrderExecRecord orderExecRecord = new OrderExecRecord();
            List<Map<String,Object>> exlist = (List<Map<String, Object>>) org.apache.commons.collections.MapUtils.getObject(paramMap,"exlist");
            Map<String,Object> param = exlist.stream().findFirst().orElse(null);

            String pkPv = org.apache.commons.collections.MapUtils.getString(param,"pkPv");
            String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class, org.apache.commons.collections.MapUtils.getString(param,"pkPv"));

            sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class, pvEncounter.getPkPi());


            sql = "select * from PV_IP where PK_PV = ?";
            List<PvIp> pvIpList = DataBaseHelper.queryForList(sql,PvIp.class, org.apache.commons.collections.MapUtils.getString(param,"pkPv"));
            PvIp pvIp = pvIpList.stream().findFirst().orElse(null);
            orderExecRecord.setVisitNo(pvIp!=null?pvIp.getIpTimes()+"":"");
            //患者主键
            orderExecRecord.setPkPatient(piMaster.getCodePi());
            //就诊ID
            orderExecRecord.setEncounterId("10_2000_"+pvEncounter.getCodePv());
            //就诊流水号
            orderExecRecord.setVisitId(pvEncounter.getCodePv());
            //姓名
            orderExecRecord.setPatientName(piMaster.getNamePi());
            //就诊时间
            orderExecRecord.setVisitDateTime(simpleDateFormat.format(pvEncounter.getDateBegin()));
            //医嘱执行记录ID
            orderExecRecord.setExecId(org.apache.commons.collections.MapUtils.getString(param,"pkExocc"));
            //医嘱ID
            orderExecRecord.setOrderId(org.apache.commons.collections.MapUtils.getString(param,"pkCnord"));
            //医嘱号
            orderExecRecord.setPlacerOrderNo(MapUtils.getString(param,"ordsn"));
            String sexCode = piMaster!=null?piMaster.getDtSex():"";
            orderExecRecord.setGenderName("02".equals(sexCode)?"男":"女");
            orderExecRecord.setWardId(MapUtils.getString(paramMap,"pkDeptNs"));
            orderExecRecord.setWardName(MapUtils.getString(paramMap,"nameDeptOcc"));
            orderExecRecord.setBedNo(MapUtils.getString(paramMap,"bedNo"));
            orderExecRecord.setOrderId(MapUtils.getString(paramMap,"pkCnord"));
            orderExecRecord.setPlacerOrderNo(MapUtils.getInteger(paramMap,"groupno")+"");
            orderExecRecord.setOrderTypeName(MapUtils.getString(paramMap,"ordtype"));
            orderExecRecord.setOrderItemName(MapUtils.getString(paramMap,"nameOrd"));
            orderExecRecord.setCancelOperaId(MapUtils.getString(paramMap,"pkEmpCanc"));
            orderExecRecord.setCancelOperaName(MapUtils.getString(paramMap,"nameEmpCanc"));
            try{
                String datePlan = org.apache.commons.collections.MapUtils.getString(param,"datePlan");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                Date plan = simpleDateFormat1.parse(datePlan);
//                orderExecRecord.setScheduleDateTime(simpleDateFormat.format(plan));
            }catch (Exception e){

            }
            List<DataElement> dataElementList = null;
            try {
                dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
            } catch (IllegalAccessException e) {

            }
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getBean(HttpRestTemplate.class);
            RequestBody requestBody = new RequestBody.Builder().service("S0036","医嘱信息更新服务").event("E003607","取消医嘱执行").sender().receiver().build();

            Map<String,Object> map = new HashMap<>(16);
            map.put("ORDER_EXEC_RECORD",dataElementList);
            requestBody.setMessage(map);
            SenderElement senderElement = new SenderElement("2000", new SoftwareNameElement("HIS","医院信息管理系统"), new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"), new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement("1200", new SoftwareNameElement("ESB","集成平台"), new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"), new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
            );
            requestBody.setSender(senderElement);
            requestBody.setReceiver(receiverElement);
            String requestString = JSON.toJSONString(requestBody);
            String responseString = httpRestTemplate.postForString(requestString);
            System.out.println(responseString);
    	}
    }

    @Autowired
    private PskqPlatFormSendCnMapper pskqPlatFormSendCnMapper;
    /**
     * 医嘱核对
     * E003603
     * 确认住院医嘱
     */
    @Override
    public void sendExOrderCheckMsg(Map<String, Object> paramMap) {
        //3:核对

        String ordStatus = MapUtils.getString(paramMap,"ordStatus","");
        OrderExecRecordService orderExecRecordService = SpringContextHolder.getApplicationContext().getBean(OrderExecRecordService.class);
        OrderInpatService orderInpatService = SpringContextHolder.getApplicationContext().getBean(OrderInpatService.class);
        List<Map<String,Object>> orderList = (List<Map<String, Object>>) MapUtils.getObject(paramMap, "ordlist",new ArrayList<CnOrder>());
        HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
        if("2".equalsIgnoreCase(ordStatus)){
            if("CA".equals(MapUtils.getString(paramMap,"control",""))){
                orderInpatService.cancel(orderList);
                return;
            }

            orderInpatService.creat(orderList, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                	
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                	
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                	 
                }
            });
        }

        if("3".equals(ordStatus)){
            String result = "";
            String responseBody="";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try{
                if(orderList.size()==0){

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
                        orderExecRecord.setOrderItemName(cnOrder!=null?cnOrder.getNameOrd():orderExecRecord.getOrderItemName());
                        if(StringUtils.isEmpty( orderExecRecord.getDrugFreqCode())){
                            orderExecRecord.setDrugFreqCode(cnOrder!=null?cnOrder.getCodeFreq():"");
                        }
                        if(StringUtils.isEmpty( orderExecRecord.getDrugFreqName())){
                            sql = "select * from BD_TERM_FREQ where CODE = ?";
                            if(!StringUtils.isEmpty(cnOrder.getCodeFreq())){
                                BdTermFreq freq = DataBaseHelper.queryForBean(sql,BdTermFreq.class,cnOrder.getCodeFreq());
                                orderExecRecord.setDrugFreqName(freq!=null? freq.getName():"");
                            }

                        }

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
                            sql = "select * from EX_ORDER_OCC where PK_PV = ? and PK_CNORD = ? order by DATE_PLAN asc";
                            List<ExOrderOcc> exOrderOccList = DataBaseHelper.queryForList(sql,ExOrderOcc.class,pkPv,pkCnord);
                            ExOrderOcc exOrderOcc = exOrderOccList.stream().findFirst().orElse(null);
                            Date planDate = exOrderOcc.getDatePlan();
                            orderExecRecord.setScheduleDateTime(planDate!=null?simpleDateFormat.format(planDate):"");
                        }

                    }


                    orderExecRecord.setEncounterId("10_3_2000_"+orderExecRecord.getEncounterId());
                    orderExecRecord.setVisitId(PskqMesUtils.pskqHisCode+orderExecRecord.getEncounterTypeCode()+"_"+PskqMesUtils.pskqSysCode+orderExecRecord.getVisitId());
                    //医嘱类型转换
                    orderExecRecord.setOrderCategoryCode(PskqDictionMapUtil.hisToPingOrdtypeCodeMap(orderExecRecord.getOrderCategoryCode()));
                    orderExecRecord.setOrderCategoryName(PskqDictionMapUtil.hisToPingOrdtypeNameMap(orderExecRecord.getOrderCategoryCode()));

                   try{
                       String sql = "select * from BD_ORD where CODE = ?";
                       String code = orderExecRecord.getOrderItemCode();
                       BdOrd bdOrd = DataBaseHelper.queryForBean(sql,BdOrd.class,code);
                       if(bdOrd!=null&&"06".equals(bdOrd.getDtOrdcate())){
                           orderExecRecord.setOrderCategoryName("心电图");
                       }
                   }catch (Exception e){

                   }





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

                    if("心电图".equals(orderExecRecord.getOrderCategoryName())){
                        //再次发送心电图核对
                        ExamApply examApply = new ExamApply();
                        examApply.setEmpiId(orderExecRecord.getEmpiId());
                        examApply.setPkPatient(orderExecRecord.getPkPatient());
                        examApply.setEncounterId(orderExecRecord.getEncounterId());
                        examApply.setOrgCode(orderExecRecord.getOrgCode());
                        examApply.setOrgName(orderExecRecord.getOrgName());
                        examApply.setEncounterTypeCode(orderExecRecord.getEncounterTypeCode());
                        examApply.setEncounterTypeName(orderExecRecord.getEncounterTypeName());
                        examApply.setVisitId(orderExecRecord.getVisitId());
                        examApply.setVisitNo(orderExecRecord.getVisitNo());
                        examApply.setPatientName(orderExecRecord.getPatientName());
                        examApply.setGenderCode(orderExecRecord.getGenderCode());
                        examApply.setGenderName(orderExecRecord.getGenderName());
                        examApply.setDateOfBirth(orderExecRecord.getDateOfBirth());
                        examApply.setAgeYear(orderExecRecord.getAgeYear());
                        examApply.setAgeMonth(orderExecRecord.getAgeMonth());
                        examApply.setAgeDay(orderExecRecord.getAgeDay());
                        examApply.setAgeHour(orderExecRecord.getAgeHour());
                        examApply.setVisitDateTime(orderExecRecord.getVisitDateTime());
                        examApply.setDeptId(orderExecRecord.getDeptId());
                        examApply.setDeptName(orderExecRecord.getDeptName());
                        examApply.setWardId(orderExecRecord.getWardId());
                        examApply.setWardName(orderExecRecord.getWardName());
                        examApply.setBedNo(orderExecRecord.getBedNo());
                        examApply.setExamApplyId("");
                        examApply.setExamApplyNo("");
                        examApply.setPlacerOrderNo(orderExecRecord.getPlacerOrderNo());
                        examApply.setApplyDateTime("");
                        examApply.setApplyDeptId(orderExecRecord.getApplyDeptId());
                        examApply.setApplyDeptName(orderExecRecord.getApplyDeptName());
                        examApply.setApplyDoctorId("");
                        examApply.setApplyDoctorName("");
                        examApply.setDiseaseDesc("");
                        examApply.setPresentHistoryDesc("");
                        examApply.setDiagDateTime("");
                        examApply.setDiagCode("");
                        examApply.setDiagName("");
                        examApply.setDiagDesc("");
                        examApply.setApplyPurposeDezsc("");
                        examApply.setExamCategoryCode("");
                        examApply.setExamCategoryName("");
                        examApply.setExamClassCode("");
                        examApply.setExamClassName("");
                        examApply.setUniversalServiceCode("");
                        examApply.setUniversalServiceName("");
                        examApply.setExamPartCode("");
                        examApply.setExamPartName("");
                        examApply.setApplyCmmt("");
                        examApply.setScheduleStartDateTime("");
                        examApply.setScheduleEndDateTime("");
                        examApply.setRegistrantDateTime("");
                        examApply.setRegistrantOperaId("");
                        examApply.setRegistrantOperaName("");
                        examApply.setCancelFlag("");
                        examApply.setCancelDateTime("");
                        examApply.setCancelReasonDesc("");
                        examApply.setCancelOperaId("");
                        examApply.setCancelOperaName("");
                        examApply.setExecOrgCode("");
                        examApply.setExecOrgName("");
                        examApply.setExecSystemCode("");
                        examApply.setExecSystemName("");
                        examApply.setExecDeptId("");
                        examApply.setExecDeptName("");
                        examApply.setEmerFlag("");
                        examApply.setGreenChannelFlag("");
                        examApply.setFeeAmount("");
                        examApply.setApplyStatusCode("");
                        examApply.setApplyStatusName("");
                        examApply.setGestationWeek("");
                        examApply.setGestationDay("");
                        examApply.setLastMenstrualPeriod("");
                        examApply.setExecDateTime("");
                        examApply.setExecOperaId("");
                        examApply.setExecOperaName("");
                        examApply.setChargeQuantity("");
                        RequestBody requestBody1 = new RequestBody.Builder()
                                .service("S0042","检查申请信息更新服务")
                                .event("E004210","确认住院电生理申请")
                                .sender()
                                .receiver()
                                .build();
                        Map<String,Object> map1 = new HashMap<>(16);
                        List<DataElement> dataElementList1 = MessageBodyUtil.dataElementsFactory(examApply);
                        map1.put("EXAM_APPLY",dataElementList1);
                        requestBody1.setMessage(map1);
                        SenderElement senderElement1 = new SenderElement("2000",
                                new SoftwareNameElement("HIS","医院信息管理系统"),
                                new SoftwareProviderElement("Zebone","江苏振邦智慧城市信息系统有限公司"),
                                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
                        );
                        ReceiverElement receiverElement1 = new ReceiverElement("1200",
                                new SoftwareNameElement("ESB","集成平台"),
                                new SoftwareProviderElement("Caradigm","恺恩泰（北京）科技有限公司"),
                                new OrganizationElement("10","南方医科大学深圳口腔医院（坪山）")
                        );
                        requestBody1.setSender(senderElement1);
                        requestBody1.setReceiver(receiverElement1);
                        String requestString1 = JSON.toJSONString(requestBody1);

                        String responseString1 = httpRestTemplate.postForString(requestString1);

                    }


                }
            }catch (Exception e){

            }
        }

        if("00".equalsIgnoreCase(ordStatus)){
        	orderExecRecordService.checkLisOrRis(orderList, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                	logger.info("发送住院检验检查确认信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                	logger.info("发送住院检验检查确认信息出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                	logger.error("发送住院检验检查确认信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        	cnService.sendIpCnorderCheck(orderList, new ResultListener() {//orderExecRecordService.check(orderList, new ResultListener() {
                @Override
                public void success(String requestJson, String responseJson) {
                	logger.info("发送住院医嘱确认信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void error(String message, String requestJson, String responseJson) {
                	logger.info("发送住院医嘱确认信息出错，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }

                @Override
                public void exception(String message, String requestJson, String responseJson) {
                	logger.error("发送住院医嘱确认信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
                }
            });
        }

    }

    /**
     * 发生手术信息
     * @param paramMap
     */
    @Override
    public void sendOpApplyMsg(Map<String, Object> paramMap) {
        cnService.getOpApplyInfo(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送手术信息成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送手术信息出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);

            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送手术信息出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
    }

    /**
     * 发送停诊信息到资源池
     * List<String> pkSchs=new ArrayList<String>();
	 * pkSchs.add(pkSch);
	 * platParam.put("pkSchs",pkSchs);
	 * platParam.put("isSendShortMsg","1");
     */
    @Override
    public void sendSchInfo(Map<String, Object> paramMap) {
    	//发送停诊信息到资源池
        SendOtherPubHandler.invokeOther(PskqResourcesSendHandler.class,"sendStopSchMsg",paramMap);
    }

    /**
     *发送转科
     */
	@Override
	public void sendDeptChangeMsg(Map<String, Object> paramMap) {
		paramMap.put("methodType", "sendDeptChangeMsg");
    	cnService.getEncounterIpAdtChangeById(paramMap, new ResultListener() {
            @Override
            public void success(String requestJson, String responseJson) {
                logger.info("发送转科成功，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void error(String message, String requestJson, String responseJson) {
                logger.info("发送转科出错，请求和响应结果分别为："+message+"---------"+requestJson+"--------------------"+responseJson);
            }

            @Override
            public void exception(String message, String requestJson, String responseJson) {
                logger.error("发送转科出错，异常信息："+message+"，请求和响应结果分别为："+requestJson+"--------------------"+responseJson);
            }
        });
		
	}

	/**
	 * 发送诊室信息
	 */
	@Override
	public void sendBdDeptUnitMsg(Map<String, Object> paramMap) {
        SendOtherPubHandler.invokeOther(PskqTriageSendHandler.class,"sendBdDeptUnitMsg",paramMap);
	}

    /**
     * 发送诊间预约取消信息
     */
    @Override
    public void sendSchApptReg(Map<String, Object> paramMap) {
        String note = org.apache.commons.collections.MapUtils.getString(paramMap,"note","");

        if(!StringUtils.isEmpty(note)&&"HIS-手动取消".equals(note)){
            String pkSchappt = org.apache.commons.collections.MapUtils.getString(paramMap,"pkSchappt","");
            if(StringUtils.isEmpty(pkSchappt)){
                return;
            }
            String sql = "select * from SCH_APPT where PK_SCHAPPT = ?";
            SchAppt schAppt = DataBaseHelper.queryForBean(sql,SchAppt.class,pkSchappt);
            if(schAppt == null){
                return;
            }
            ReserveOutpatient reserveOutpatient = new ReserveOutpatient();
            String pkPi = schAppt.getPkPi();
            String pkSch = schAppt.getPkSch();
            sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pkPi);
            sql = "select * from SCH_APPT where PK_PI = ? order by TS desc ";
            String ticketNo = schAppt.getTicketNo();
            sql = "select * from SCH_TICKET where PK_SCH = ? and TICKETNO = ?";
            SchTicket schTicket = DataBaseHelper.queryForBean(sql,SchTicket.class,new Object[]{pkSch,ticketNo});
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            reserveOutpatient.setEmpiId(piMaster.getMpi());
            reserveOutpatient.setPkPatient(piMaster.getCodePi());
            reserveOutpatient.setOrgCode(piMaster.getPkOrg());
            reserveOutpatient.setOrgName("南方医科大学深圳口腔医院（坪山）");
            reserveOutpatient.setPatientName(piMaster.getNamePi());
            reserveOutpatient.setGenderCode(piMaster.getDtSex());
            reserveOutpatient.setGenderName("02".equals(piMaster.getDtSex())?"男":"女");
            reserveOutpatient.setDateOfBirth(DateFormatUtils.format(piMaster.getBirthDate(),"yyyy-MM-dd"));
            reserveOutpatient.setIdTypeCode("01");
            reserveOutpatient.setIdTypeName("居民身份证");
            reserveOutpatient.setIdNo(piMaster!=null?piMaster.getIdNo():"");
            reserveOutpatient.setPhoneNo(piMaster.getMobile());
            //医保卡号
            reserveOutpatient.setMedInsuranceNo("");
            //是否医保
            if(!StringUtils.isEmpty(piMaster.getInsurNo())){
                reserveOutpatient.setIsInsurance("1");
            }else {
                reserveOutpatient.setIsInsurance("0");
            }

            //预约序号
            reserveOutpatient.setReserveId(schAppt!=null?schAppt.getCode():"");
            //取号密码
            reserveOutpatient.setHisTakeNo("");
            //预约渠道代码
            reserveOutpatient.setReserveCahannelCode("0");
            //预约渠道名称
            reserveOutpatient.setReserveCahannelName("诊间预约");
            //预约时间
            reserveOutpatient.setReserveDateTime(simpleDateFormat.format(new Date()));
            //排班表ID
            reserveOutpatient.setScheduleId(pkSch);
            //号源ID
            reserveOutpatient.setReserveSourceId(schTicket.getPkSchticket());
            //预约状态
            reserveOutpatient.setReserveState("2");
            //支付状态
            reserveOutpatient.setPayState("0");
            //支付方式代码
            reserveOutpatient.setPayMethodCode("");
            //支付方式名称
            reserveOutpatient.setPayMethodName("");
            //支付时间
            reserveOutpatient.setPayDateTime("");
            //支付总费用
            reserveOutpatient.setTotalPayment("");
            //医保费用
            reserveOutpatient.setMedicalInsuranceExpenses("");
            //自费费用
            reserveOutpatient.setPersonalExpenses("");
            //预约订单流水号
            reserveOutpatient.setReserveOrderNo(schAppt!=null?schAppt.getCode():"");
            //预约支付订单号
            reserveOutpatient.setPaymentOrderNo("");
            //预约支付交易流水号
            reserveOutpatient.setTransactionSerialNo("");
            //支付平台终端号
            reserveOutpatient.setPayTerminalNo("");
            //自助机终端编号
            reserveOutpatient.setMachineNo("");
            //电子健康卡号
            reserveOutpatient.setEhealthCardNo("");
            //HIS产生预约记录的唯一ID
            reserveOutpatient.setReserveOutpatientId(schAppt!=null?schAppt.getPkSchappt():"");
            //挂号发票明细ID
            reserveOutpatient.setRegisteredInvoiceId("");
            //挂号电子发票PDF路径
            reserveOutpatient.setRegisteredInvoicePdfUrl("");
            //原始挂号发票明细ID
            reserveOutpatient.setOldRegisteredInvoiceId("");
            //挂号电子发票状态
            reserveOutpatient.setRegisteredInvoicePdfStatus("");
            //医保类型编码
            reserveOutpatient.setMedicareTypeId("");
            //医保类型名称
            reserveOutpatient.setMedicareTypeName("");


            if(!StringUtils.isEmpty(schAppt.getPkSchres())){
                sql = "select * from SCH_RESOURCE where PK_SCHRES = ?";
                SchResource schResource = DataBaseHelper.queryForBean(sql,SchResource.class,schAppt.getPkSchres());
                if(schResource!=null){
                    //就诊科室代码
                    if(!StringUtils.isEmpty(schResource.getPkDeptBelong())){
                        sql = "select * from BD_OU_DEPT where PK_DEPT = ?";
                        BdOuDept dept = DataBaseHelper.queryForBean(sql,BdOuDept.class,schResource.getPkDeptBelong());
                        if(dept!=null){
                            reserveOutpatient.setVisitDeptId(dept.getCodeDept());
                            reserveOutpatient.setVisitDeptName(dept.getNameDept());
                        }
                    }

                    //就诊医师ID
                    if(!StringUtils.isEmpty(schResource.getPkEmp())){
                        sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
                        BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,schResource.getPkEmp());
                        if(bdOuEmployee!=null){
                            reserveOutpatient.setDoctorId(bdOuEmployee.getCodeEmp());
                            reserveOutpatient.setDoctorName(bdOuEmployee.getNameEmp());
                        }
                    }
                }
            }




            //就诊开始时间
            if(schAppt!=null&&schAppt.getDateAppt()!=null){

                reserveOutpatient.setVisitStartDateTime(simpleDateFormat.format(schAppt.getBeginTime()));
            }

            //门诊号
            reserveOutpatient.setOutpatientNo(piMaster.getCodeOp());


            List<DataElement> dataElementList = null;
            try {
                dataElementList = MessageBodyUtil.dataElementsFactory(reserveOutpatient);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = new RequestBody.Builder()
                    .service("S0054", "门诊预约状态信息更新服务")
                    .event("E005401", "更新门诊预约")
                    .sender()
                    .receiver()
                    .build();
            SenderElement senderElement = new SenderElement(
                    "2000",
                    new SoftwareNameElement("HIS", "医院信息管理系统"),
                    new SoftwareProviderElement("Zebone", "江苏振邦智慧城市信息系统有限公司"),
                    new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
            );
            ReceiverElement receiverElement = new ReceiverElement(
                    "1200",
                    new SoftwareNameElement("ESB", "集成平台"),
                    new SoftwareProviderElement("Caradigm", "恺恩泰（北京）科技有限公司"),
                    new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
            );
            Map<String, Object> map = new HashMap<>(16);
            map.put("RESERVE_OUTPATIENT", dataElementList);
            requestBody.setMessage(map);
            requestBody.setSender(senderElement);
            requestBody.setReceiver(receiverElement);
            String requestString = JSON.toJSONString(requestBody);
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
            String responseString = httpRestTemplate.postForString(requestString);
            ResponseBody responseBody = JsonUtil.readValue(responseString, ResponseBody.class);

        }
    }


    @Override
    public void sendDstributeCardMsg(Map<String, Object> paramMap) {

    }


    /**
     * 发送入院评估
     * @param paramMap
     */
    @Override
    public void sendAdmissionAssessment(Map<String, Object> paramMap) {
        AdmissionEvaluationSheetService service = SpringContextHolder.getBean(AdmissionEvaluationSheetService.class);
        String pkPv = org.apache.commons.collections.MapUtils.getString(paramMap,"pkPv","");
        if(StringUtils.isEmpty(pkPv)){
            return;
        }
        try {
            service.send(pkPv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送诊间预约
     * @param paramMap
     */
    @Override
    public void sendReserveOutpatient(Map<String, Object> paramMap) throws IllegalAccessException {
        ReserveOutpatient reserveOutpatient = new ReserveOutpatient();
        String pkPi = org.apache.commons.collections.MapUtils.getString(paramMap,"pkPi");
        String pkSch = org.apache.commons.collections.MapUtils.getString(paramMap,"pkSch");
        String sql = "select * from PI_MASTER where PK_PI = ?";
        PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,pkPi);
        sql = "select * from SCH_APPT where PK_PI = ? order by TS desc ";
        List<SchAppt> schApptList = DataBaseHelper.queryForList(sql,SchAppt.class,piMaster.getPkPi());
        SchAppt schAppt = schApptList.stream().findFirst().get();
        String ticketNo = schAppt.getTicketNo();
        sql = "select * from SCH_TICKET where PK_SCH = ? and TICKETNO = ?";
        SchTicket schTicket = DataBaseHelper.queryForBean(sql,SchTicket.class,new Object[]{pkSch,ticketNo});
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reserveOutpatient.setEmpiId(piMaster.getMpi());
        reserveOutpatient.setPkPatient(piMaster.getCodePi());
        reserveOutpatient.setOrgCode(piMaster.getPkOrg());
        reserveOutpatient.setOrgName("南方医科大学深圳口腔医院（坪山）");
        reserveOutpatient.setPatientName(piMaster.getNamePi());
        reserveOutpatient.setGenderCode(piMaster.getDtSex());
        reserveOutpatient.setGenderName("02".equals(piMaster.getDtSex())?"男":"女");
        reserveOutpatient.setDateOfBirth(DateFormatUtils.format(piMaster.getBirthDate(),"yyyy-MM-dd"));
        reserveOutpatient.setIdTypeCode("01");
        reserveOutpatient.setIdTypeName("居民身份证");
        reserveOutpatient.setIdNo(piMaster!=null?piMaster.getIdNo():"");
        reserveOutpatient.setPhoneNo(piMaster.getMobile());
        //医保卡号
        reserveOutpatient.setMedInsuranceNo("");
        //是否医保
        if(!StringUtils.isEmpty(piMaster.getInsurNo())){
            reserveOutpatient.setIsInsurance("1");
        }else {
            reserveOutpatient.setIsInsurance("0");
        }
        //挂号费用
        sql = "select * from SCH_SRV_ORD where PK_SCHSRV = ?";
        if(!StringUtils.isEmpty(schAppt.getPkSchsrv())){
            SchSrvOrd schSrvOrd = DataBaseHelper.queryForBean(sql,SchSrvOrd.class,schAppt.getPkSchsrv());
            if(schSrvOrd!=null&&!StringUtils.isEmpty(schSrvOrd.getPkOrd())){
                sql = "select * from BD_ORD_ORG where PK_ORD = ?";
                BdOrdOrg bdOrdOrg = DataBaseHelper.queryForBean(sql,BdOrdOrg.class,schSrvOrd.getPkOrd());
                if(bdOrdOrg==null){
                    reserveOutpatient.setTotalPayment("根据PkOrd【"+schSrvOrd.getPkOrd()+"】未查询到SchSrvOrd信息");
                }else {
                    reserveOutpatient.setTotalPayment(bdOrdOrg!=null?bdOrdOrg.getPrice()+"":"");
                }

            }else{
                reserveOutpatient.setTotalPayment("根据【"+schAppt.getPkSchsrv()+"】未查询到SchSrvOrd信息");
            }
        }else {
            reserveOutpatient.setTotalPayment("未获取到PK_SCHSRV");
        }
        //预约序号
        reserveOutpatient.setReserveId(schAppt!=null?schAppt.getCode():"");
        //取号密码
        reserveOutpatient.setHisTakeNo("");

        //预约渠道代码
        reserveOutpatient.setReserveCahannelCode("0");
        //预约渠道名称
        reserveOutpatient.setReserveCahannelName("诊间预约");
        //预约时间
        reserveOutpatient.setReserveDateTime(simpleDateFormat.format(new Date()));
        //排班表ID
        reserveOutpatient.setScheduleId(pkSch);
        //号源ID
        reserveOutpatient.setReserveSourceId(schTicket.getPkSchticket());
        //预约状态
        reserveOutpatient.setReserveState("1");
        //支付状态
        reserveOutpatient.setPayState("0");
        //支付方式代码
        reserveOutpatient.setPayMethodCode("");
        //支付方式名称
        reserveOutpatient.setPayMethodName("");
        //支付时间
        reserveOutpatient.setPayDateTime("");

        //医保费用
        reserveOutpatient.setMedicalInsuranceExpenses("");
        //自费费用
        reserveOutpatient.setPersonalExpenses(reserveOutpatient.getTotalPayment());
        //预约订单流水号
        reserveOutpatient.setReserveOrderNo(schAppt!=null?schAppt.getCode():"");
        //预约支付订单号
        reserveOutpatient.setPaymentOrderNo("");
        //预约支付交易流水号
        reserveOutpatient.setTransactionSerialNo("");
        //支付平台终端号
        reserveOutpatient.setPayTerminalNo("");
        //自助机终端编号
        reserveOutpatient.setMachineNo("");
        //电子健康卡号
        reserveOutpatient.setEhealthCardNo("");
        //HIS产生预约记录的唯一ID
        reserveOutpatient.setReserveOutpatientId(schAppt!=null?schAppt.getCode():"");
        //挂号发票明细ID
        reserveOutpatient.setRegisteredInvoiceId("");
        //挂号电子发票PDF路径
        reserveOutpatient.setRegisteredInvoicePdfUrl("");
        //原始挂号发票明细ID
        reserveOutpatient.setOldRegisteredInvoiceId("");
        //挂号电子发票状态
        reserveOutpatient.setRegisteredInvoicePdfStatus("");
        //医保类型编码
        reserveOutpatient.setMedicareTypeId("");
        //医保类型名称
        reserveOutpatient.setMedicareTypeName("");

        if(!StringUtils.isEmpty(schAppt.getPkSchres())){
            sql = "select * from SCH_RESOURCE where PK_SCHRES = ?";
            SchResource schResource = DataBaseHelper.queryForBean(sql,SchResource.class,schAppt.getPkSchres());
            if(schResource!=null){
                //就诊科室代码
                if(!StringUtils.isEmpty(schResource.getPkDeptBelong())){
                    sql = "select * from BD_OU_DEPT where PK_DEPT = ?";
                    BdOuDept dept = DataBaseHelper.queryForBean(sql,BdOuDept.class,schResource.getPkDeptBelong());
                    if(dept!=null){
                        reserveOutpatient.setVisitDeptId(dept.getCodeDept());
                        reserveOutpatient.setVisitDeptName(dept.getNameDept());
                    }
                }

                //就诊医师ID
                if(!StringUtils.isEmpty(schResource.getPkEmp())){
                    sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
                    BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,schResource.getPkEmp());
                    if(bdOuEmployee!=null){
                        reserveOutpatient.setDoctorId(bdOuEmployee.getCodeEmp());
                        reserveOutpatient.setDoctorName(bdOuEmployee.getNameEmp());
                    }
                }
            }
        }




        //就诊开始时间
        if(schAppt!=null&&schAppt.getDateAppt()!=null){

            reserveOutpatient.setVisitStartDateTime(simpleDateFormat.format(schAppt.getBeginTime()));
        }

        //门诊号
        reserveOutpatient.setOutpatientNo(piMaster.getCodeOp());



        List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(reserveOutpatient);
        RequestBody requestBody = new RequestBody.Builder()
                .service("S0053", "门诊预约状态信息新增服务")
                .event("E005301", "新增门诊预约")
                .sender()
                .receiver()
                .build();
        SenderElement senderElement = new SenderElement(
                "2000",
                new SoftwareNameElement("HIS", "医院信息管理系统"),
                new SoftwareProviderElement("Zebone", "江苏振邦智慧城市信息系统有限公司"),
                new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
        );
        ReceiverElement receiverElement = new ReceiverElement(
                "1200",
                new SoftwareNameElement("ESB", "集成平台"),
                new SoftwareProviderElement("Caradigm", "恺恩泰（北京）科技有限公司"),
                new OrganizationElement("10", "南方医科大学深圳口腔医院（坪山）")
        );
        Map<String, Object> map = new HashMap<>(16);
        map.put("RESERVE_OUTPATIENT", dataElementList);
        requestBody.setMessage(map);
        requestBody.setSender(senderElement);
        requestBody.setReceiver(receiverElement);
        String requestString = JSON.toJSONString(requestBody);
        HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
        String responseString = httpRestTemplate.postForString(requestString);
        ResponseBody responseBody = JsonUtil.readValue(responseString, ResponseBody.class);

    }



    /**
     * 护理记录单
     * @param paramMap
     */
    @Override
    public void sendNursingRecordSheet(Map<String, Object> paramMap) {
        NursingRecordSheetService service = SpringContextHolder.getBean(NursingRecordSheetService.class);
        try {
            service.add(paramMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生命体征
     * @param paramMap
     */
    @Override
    public void sendVitalSigns(Map<String, Object> paramMap) {
        VitalSignsService service = SpringContextHolder.getBean(VitalSignsService.class);
        HttpRestTemplate httpRestTemplate = SpringContextHolder.getApplicationContext().getBean(HttpRestTemplate.class);
        try {
            List<String> resultList = service.findVitalSigns(paramMap);
            for (String s : resultList) {
                String responseString = httpRestTemplate.postForString(s);
                ResponseBody responseBody = JsonUtil.readValue(responseString,ResponseBody.class);
                if (responseBody != null && responseBody.getAck() != null && "AA".equals(responseBody.getAck().get("ackCode"))) {

                } else {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
