package com.zebone.nhis.ma.pub.platform.zsrm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.HttpClientUtil;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.support.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.zsrm.dao.ZsrmPhSettleServiceMapper;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.ReportBusinessBase;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.ReportParametersVo;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.ReportResponseVo;
import com.zebone.nhis.ma.pub.platform.zsrm.vo.RrportEntry;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;


@Service
public class ZsrmPhSettleService {
    private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
    @Resource
    ZsrmPhSettleServiceMapper zsrmPhSettleServiceMapper;
    @Resource
    RequestTemplate requestTemplate;

    /**
     * 门诊结算后补充指引单打印逻辑
     */
    public ReportResponseVo getReportParameters(String param, IUser user) {
        ReportResponseVo responseDate = null;
        try {
            //解析参数
            String pkPi = JsonUtil.getFieldValue(param, "pkPi");
            JsonNode jsonNode = JsonUtil.getJsonNode(param, "pkOrdes");
            if (StringUtils.isBlank(pkPi) || jsonNode == null) {
                throw new BusException("入参不能为空");
            }
            //获取调用接口所需的入参
            String codeOp = DataBaseHelper.queryForScalar("select CODE_OP from PI_MASTER where PK_PI=?", String.class, pkPi);
            String pkOrde = "";
            for (int i = 0; i < jsonNode.size(); i++) {
                String replace = jsonNode.get(i).toString().replace("\"", "");
                if (i == jsonNode.size() - 1) {
                    pkOrde += "'" + replace + "'";
                } else {
                    pkOrde += "'" + replace + "',";
                }
            }
            List<Map<String, Object>> stringObjectMap = DataBaseHelper.queryForList("select CODE_APPLY as APPLY from CN_ORDER where CODE_APPLY is not null and PK_CNORD in (" + pkOrde + ")  ");
            if (stringObjectMap == null || stringObjectMap.size() < 1) {
                throw new BusException("根据医嘱主键未能获取到医嘱单号");
            }
            //拼接pkOrde
            pkOrde = "";
            for (int i = 0; i < stringObjectMap.size(); i++) {
                if (i == stringObjectMap.size() - 1) {
                    pkOrde += stringObjectMap.get(i).get("apply").toString();
                } else {
                    pkOrde += stringObjectMap.get(i).get("apply").toString() + ",";
                }

            }
            //开始调用接口
            String responseParameters = getRequestParameters(codeOp, pkOrde);
            //处理返回的参数
            LinkedHashMap linkedHashMap = responseParameters(responseParameters);
            if ("success".equals(linkedHashMap.get("state"))) {
                responseDate = (ReportResponseVo) linkedHashMap.get("responseDate");
            } else {
                throw new BusException("调用接口失败");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return responseDate;
    }

    /**
     * 门诊结算查询  打印指引单
     */
    public ReportResponseVo aginPrintReport(String param, IUser user) {
        ReportResponseVo responseDate = null;
        try {
            //处理入参
            String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
            if (StringUtils.isBlank(pkSettle)) {
                throw new BusException("入参不能为空");
            }
            List<Map<String, Object>> applyOpByPKSettle = zsrmPhSettleServiceMapper.getApplyOpByPKSettle(pkSettle);
            if (applyOpByPKSettle.size() < 1) {
                return null;
            }
            //拼接需要的参数
            String pkOrde = "";
            String codeOp = "";
            for (int i = 0; i < applyOpByPKSettle.size(); i++) {
                if (i == applyOpByPKSettle.size() - 1) {
                    pkOrde += applyOpByPKSettle.get(i).get("APPLY").toString();
                } else {
                    pkOrde += applyOpByPKSettle.get(i).get("APPLY").toString() + ",";
                }
                codeOp = applyOpByPKSettle.get(i).get("CODEOP").toString();
            }
            //执行请求并解析返回的数据
            LinkedHashMap linkedHashMap = responseParameters(getRequestParameters(codeOp, pkOrde));
            if ("success".equals(linkedHashMap.get("state"))) {
                responseDate = (ReportResponseVo) linkedHashMap.get("responseDate");
            } else {
                throw new BusException("调用接口失败");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return responseDate;
    }
    private  String getRequestParameters(String codeOp,String pkOrde){
        //拼接入参
        String id = NHISUUID.getKeyId();
        BusinessBase businessBase = new BusinessBase();
        businessBase.setResourceType("Bundle");
        businessBase.setType("message");
        businessBase.setId(id);
        businessBase.setTimestamp(new Date());
        List<Entry> entrys = new ArrayList<>();
        Entry entry=new Entry();
        PhResource resource=new PhResource();
        resource.setResourceType("MessageHeader");
        resource.setId(NHISUUID.getKeyId());
        resource.setSource(new Destination("NHIS"));
        List<Destination> destination=new LinkedList<>();
        destination.add(new Destination("ABS"));
        resource.setDestination(destination);
        entry.setResource(resource);
        entrys.add(entry);
        //第二个entry
        entry=new Entry();
        ReportParametersVo reportParametersVo=new ReportParametersVo();
        reportParametersVo.setResourceType("ServiceRequest");
        reportParametersVo.setImplicitRules("JCYYCX");
        reportParametersVo.setOccurrenceDateTime(new Timestamp(new Date().getTime()));
        List<Parameter> getParameter=new LinkedList<>();
        Parameter parameter=new Parameter();
        parameter.setName("patientNo");
        parameter.setValueString(codeOp);
        getParameter.add(parameter);
        parameter = new Parameter();
        parameter.setName("applyNo");
        parameter.setValueString(pkOrde);
        getParameter.add(parameter);
        reportParametersVo.setParameter(getParameter);
        entry.setResource(reportParametersVo);
        entrys.add(entry);
        businessBase.setEntry(entrys);
        //转json调用接口
        RequestData requestData = RequestData.newBuilder()
                .id(id)
                .data(JSON.toJSONString(businessBase))
                .msgIndexData(MsgIndexData.newBuilder().codeOp(codeOp).build())
                .remoteMethod("ServiceRequest").build();
        log.info("检查申请构造完成id:{},data:{},remoteMethod:{}"
                , requestData.getId(), requestData.getData(), requestData.getRemoteMethod());
        /**
         * 返回json
         */
        log.info("------请求json-------");
        log.info(requestData.getData());
        /**
         * 因返回的Issue与send中封装的Issue结构不一致，故采用requestTemplate.post方法调用
         */
        String responseData = requestTemplate.post(requestData).getData();
        saveSendMsg(requestData.getId(), requestData.getData(), responseData, requestData.getRemoteMethod(), businessBase.getTimestamp());
        return responseData;
    }
    private LinkedHashMap responseParameters(String param) {
        /**
         * 返回json
         */
        log.info("------返回json-------");
        log.info(param);
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap();
        try {
            ReportResponseVo reportResponseVo = new ReportResponseVo();
            List<LinkedHashMap<String, Object>> extension = new LinkedList<>();
            //处理返回数据格式问题
            ReportBusinessBase businessBase = JsonUtil.readValue(param, ReportBusinessBase.class);
            List<RrportEntry> entrys = businessBase.getEntry();
            for (RrportEntry entry : entrys) {
                if ("AA".equals(entry.getResponse().getStaus())) {
                    List<Map<String, Object>> issues = (List<Map<String, Object>>) entry.getResponse().getOutcome().get("issue");
                    List<LinkedHashMap<String, Object>> extensions = (List<LinkedHashMap<String, Object>>) issues.get(0).get("extension");
                    for (LinkedHashMap<String, Object> parameter : extensions) {
                        if ("GuideInfo".equals(parameter.get("url"))) {
                            extension.add(parameter);
                        }
                    }
                }
            }
            reportResponseVo.setExtensions(extension);
            linkedHashMap.put("state", "success");
            linkedHashMap.put("responseDate", reportResponseVo);
            return linkedHashMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusException("从返回信息中获取数据时发生异常");
        }
    }

    /**
     * 发送消息后保存记录
     */
    public void saveSendMsg(String id, String requestData, String responseData, String msgType, Date date) {
        SysMsgRec rec = new SysMsgRec();
        ApplicationUtils.setDefaultValue(rec, true);
        rec.setMsgId(id);
        rec.setMsgType(msgType);
        rec.setTransType("send");
        rec.setTransDate(date);
        rec.setSysCode("NHIS");
        rec.setMsgStatus("ACK");
        rec.setErrTxt(requestData);
        rec.setMsgContent(responseData);
        DataBaseHelper.insertBean(rec);
    }
}
