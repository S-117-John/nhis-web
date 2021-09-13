package com.zebone.nhis.ma.pub.platform.pskq.service.impl;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.module.base.bd.mk.BdSupply;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.ma.pub.platform.pskq.model.OrderExecRecord;
import com.zebone.nhis.ma.pub.platform.pskq.model.listener.ResultListener;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.*;
import com.zebone.nhis.ma.pub.platform.pskq.utils.MessageBodyUtil;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 卡卡西
 */
@Service
public class NurseService {

    /**
     * 通用执行
     * @param paramMap
     * @param listener
     */
    public void universalExecution(Map<String, Object> paramMap, ResultListener listener){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OrderExecRecord orderExecRecord = new OrderExecRecord();
            List<Map<String,Object>> exlist = (List<Map<String, Object>>) MapUtils.getObject(paramMap,"exlist");
            Map<String,Object> param = exlist.stream().findFirst().orElse(null);
            String sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,MapUtils.getString(param,"pkPi"));
            sql = "select * from PV_ENCOUNTER where PK_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,MapUtils.getString(param,"pkPv"));

            sql = "select * from PV_IP where PK_PV = ?";
            List<PvIp> pvIpList = DataBaseHelper.queryForList(sql,PvIp.class,MapUtils.getString(param,"pkPv"));
            PvIp pvIp = pvIpList.stream().findFirst().orElse(null);
            orderExecRecord.setVisitNo(pvIp!=null?pvIp.getIpTimes()+"":"");
            //患者主键
            orderExecRecord.setPkPatient(piMaster.getCodePi());
            //就诊ID
            orderExecRecord.setEncounterId("10_3_2000_"+pvEncounter.getCodePv());
            //就诊流水号
            orderExecRecord.setVisitId(pvEncounter.getCodePv());
            //姓名
            orderExecRecord.setPatientName(piMaster.getNamePi());
            //就诊时间
            orderExecRecord.setVisitDateTime(simpleDateFormat.format(pvEncounter.getDateBegin()));
            //医嘱执行记录ID
            orderExecRecord.setExecId(MapUtils.getString(param,"pkExocc"));
            //医嘱ID
            orderExecRecord.setOrderId(MapUtils.getString(param,"pkCnord"));
            //医嘱号
            orderExecRecord.setPlacerOrderNo(MapUtils.getString(param,"ordsn"));




            String pkCnord1 = orderExecRecord.getOrderId();
            if(!StringUtils.isEmpty(pkCnord1)){

                sql = "select * from EX_ORDER_OCC where PK_CNORD = ? and EU_STATUS = '1' order by DATE_OCC desc ";

                ExOrderOcc exOrderOcc = DataBaseHelper.queryForBean(sql,ExOrderOcc.class,pkCnord1);

                sql = "select * from BD_OU_EMPLOYEE where PK_EMP = ?";
                if(!StringUtils.isEmpty(exOrderOcc.getPkEmpOcc())){
                    BdOuEmployee employee = DataBaseHelper.queryForBean(sql,BdOuEmployee.class,exOrderOcc.getPkEmpOcc());
                    if(employee!=null){
                        orderExecRecord.setExecOperaId(employee.getCodeEmp());
                    }
                }


                orderExecRecord.setScheduleDateTime(simpleDateFormat.format(exOrderOcc.getDatePlan()));
                orderExecRecord.setExecOperaName(exOrderOcc.getNameEmpOcc());

                orderExecRecord.setExecDateTime(simpleDateFormat.format(exOrderOcc.getDateOcc()));

                sql = "select * from CN_ORDER where PK_CNORD = ?";
                CnOrder cnOrder = DataBaseHelper.queryForBean(sql,CnOrder.class,pkCnord1);
                String euAlways = cnOrder.getEuAlways();
                //医嘱类型
                if("0".equals(euAlways)){
                    //长期
                    orderExecRecord.setOrderTypeCode("1");
                    orderExecRecord.setOrderTypeName("长期医嘱");
                }else if("1".equals(euAlways)){
                    //临时
                    orderExecRecord.setOrderTypeCode("0");
                    orderExecRecord.setOrderTypeName("临时医嘱");
                }
                orderExecRecord.setOrderItemCode(cnOrder.getCodeOrd());
                orderExecRecord.setOrderItemName(cnOrder.getNameOrd());
                orderExecRecord.setOrderGroupNo(cnOrder.getOrdsnParent()+"");
                orderExecRecord.setSkinTestFlag("0");
                String eu = cnOrder.getEuSt();
                if(!StringUtils.isEmpty(eu)){
                    orderExecRecord.setSkinTestFlag("1");
                    switch (eu){
                        case "2":
                            //皮试结果 0 不查，1待查，2阴性，3阳性
                            orderExecRecord.setSkinResult("1");
                            break;
                        case "3":
                            //皮试结果 0 不查，1待查，2阴性，3阳性
                            orderExecRecord.setSkinResult("2");
                            break;
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


            try{
                String datePlan = MapUtils.getString(param,"datePlan");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
                Date plan = simpleDateFormat1.parse(datePlan);
//                orderExecRecord.setScheduleDateTime(simpleDateFormat.format(plan));
            }catch (Exception e){

            }
            List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getBean(HttpRestTemplate.class);
            RequestBody requestBody = new RequestBody.Builder().service("S0036","医嘱信息更新服务").event("E003608","执行医嘱执行档案").sender().receiver().build();

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

            ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
            //消息成功
            if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                listener.success(requestString, responseString);
            } else {
                listener.error(paramVo.getAck().get("ackDetail").toString(), requestString, responseString);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),"","");
        }
    }

    /**
     * 取消执行
     * @param paramMap
     * @param listener
     */
    public void cancelExecution (Map<String, Object> paramMap, ResultListener listener){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            OrderExecRecord orderExecRecord = new OrderExecRecord();
            List<Map<String,Object>> exlist = (List<Map<String, Object>>) MapUtils.getObject(paramMap,"exlist");
            Map<String,Object> param = exlist.stream().findFirst().orElse(null);
            String sql = "select * from PI_MASTER where PK_PI = ?";
            PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,MapUtils.getString(param,"pkPi"));
            sql = "select * from PV_ENCOUNTER where PK_PV = ?";
            PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,MapUtils.getString(param,"pkPv"));
            //患者主键
            orderExecRecord.setPkPatient(piMaster.getCodePi());
            //就诊ID
            orderExecRecord.setEncounterId(pvEncounter.getCodePv());
            //就诊流水号
            orderExecRecord.setVisitId(pvEncounter.getCodePv());
            //姓名
            orderExecRecord.setPatientName(piMaster.getNamePi());
            //就诊时间
            orderExecRecord.setVisitDateTime(simpleDateFormat.format(pvEncounter.getDateBegin()));
            //医嘱执行记录ID
            orderExecRecord.setExecId(MapUtils.getString(param,"pkExocc"));
            //医嘱ID
            orderExecRecord.setOrderId(MapUtils.getString(param,"pkCnord"));
            //医嘱号
            orderExecRecord.setPlacerOrderNo(MapUtils.getString(param,"ordsn"));

            List<DataElement> dataElementList = MessageBodyUtil.dataElementsFactory(orderExecRecord);
            HttpRestTemplate httpRestTemplate = SpringContextHolder.getBean(HttpRestTemplate.class);
            RequestBody requestBody = new RequestBody.Builder().service("S0036","医嘱信息更新服务").event("E003609","撤销医嘱执行档").sender().receiver().build();

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

            ResponseBody paramVo = JsonUtil.readValue(responseString, ResponseBody.class);
            //消息成功
            if (paramVo != null && paramVo.getAck() != null && "AA".equals(paramVo.getAck().get("ackCode"))) {
                listener.success(requestString, responseString);
            } else {
                listener.error(paramVo.getAck().get("ackDetail").toString(), requestString, responseString);
            }
        }catch (Exception e){
            listener.exception(e.getMessage(),"","");
        }
    }


}
