package com.zebone.nhis.webservice.pskq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.bl.pub.dao.BlMedicalExe2Mapper;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.ExamApply;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamAppService {

    @Transactional
    public String update(String param){
        Map<String, Object> ack = new HashMap<>(16);
        ResponseBody responseBody = new ResponseBody();
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
        Map<String, Object> query = requestBody.getMessage();
        List<DataElement> dataElement = new ArrayList<DataElement>();

        if (query.containsKey("EXAM_RESULT") && query.get("EXAM_RESULT") != null) {
            dataElement = (List<DataElement>) query.get("EXAM_RESULT");
        }
        if (query.containsKey("EXAM_APPLY") && query.get("EXAM_APPLY") != null) {
            dataElement = (List<DataElement>) query.get("EXAM_APPLY");
        }
        if (null == dataElement || dataElement.size() == 0) {

        }
        ExamApply examApply = null;
        try {
            examApply = (ExamApply) MessageFactory.deserialization(dataElement, new ExamApply());
        } catch (IllegalAccessException e) {
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "参数解析失败");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        BlMedicalExe2Mapper blMedicalExe2Mapper = SpringContextHolder.getBean(BlMedicalExe2Mapper.class);
        List<CnOrder> cnOrderList = DataBaseHelper.queryForList("select * from cn_order where CODE_APPLY = ? and del_flag = '0' and  code_ordtype like '02%' ",
                CnOrder.class, examApply.getExamApplyNo());
        if (cnOrderList==null||cnOrderList.size() == 0) {
            throw new BusException("根据申请单号未查询到医嘱信息");
        }

        JSONArray jsonArray = new JSONArray();
        for (CnOrder cnOrder : cnOrderList) {
            String sql = "select * from EX_ASSIST_OCC where PK_PV = ? and PK_CNORD = ?";
            ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean(sql, ExAssistOcc.class, cnOrder.getPkPv(), cnOrder.getPkCnord());
            if (exAssistOcc == null) {
                throw new BusException("未查询到医技执行信息");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("pkAssocc", exAssistOcc.getPkAssocc());
            jsonObject.put("codeOcc", exAssistOcc.getCodeOcc());
            jsonObject.put("quanOcc", exAssistOcc.getQuanOcc().intValue() + "");
            jsonObject.put("dateAppt", exAssistOcc.getDateAppt());
            jsonObject.put("dateOcc", exAssistOcc.getDateOcc());
            jsonObject.put("pre", null);

            sql = "select * from BD_OU_EMPLOYEE where CODE_EMP = ?";
            BdOuEmployee employee = DataBaseHelper.queryForBean(sql, BdOuEmployee.class, examApply.getRegistrantOperaId());
            if (employee == null) {
                throw new BusException("未查询到执行人信息");
            }
            jsonObject.put("pkEmpOcc", employee.getPkEmp());
            jsonObject.put("nameEmpOcc", employee.getNameEmp());
            jsonObject.put("pkMsp", null);
            jsonObject.put("note", null);
            jsonObject.put("flagOcc", "0");
            jsonObject.put("flagPrt", "0");
            jsonObject.put("isChecked", "0");
            jsonObject.put("flagYue", "1");
            jsonObject.put("yue", "约");
            jsonObject.put("euPvtype", "3");
            jsonObject.put("pkCnord", exAssistOcc.getPkCnord());
            jsonObject.put("euStatusName", "未执行");
            jsonArray.add(jsonObject);

        }

        String exeParam = JSONObject.toJSONString(jsonArray);
        List<Map<String, Object>> paramMapList = JsonUtil.readValue(exeParam, new TypeReference<List<Map<String, Object>>>() {
        });
        for (Map<String, Object> paramMap : paramMapList) {
            paramMap.put("dateOcc", DateUtils.getDateTime());
            paramMap.put("pkEmpOcc", org.apache.commons.collections.MapUtils.getString(paramMap,"pkEmpOcc"));
            paramMap.put("nameEmpOcc", org.apache.commons.collections.MapUtils.getString(paramMap,"nameEmpOcc"));
            paramMap.put("pkOrgOcc", org.apache.commons.collections.MapUtils.getString(paramMap,"pkOrgOcc"));
            paramMap.put("pkDeptOcc", org.apache.commons.collections.MapUtils.getString(paramMap,"pkDeptOcc"));
            // 数据校验
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 1) {
                // 门急诊：
                int count = blMedicalExe2Mapper.opDataCheck(paramMap.get("pkCnord").toString());
                if (count > 0) {
                    throw new BusException("有结算费用，不能执行!");
                }
            }
            if (Integer.parseInt(paramMap.get("euPvtype").toString()) == 3) {
                // 住院：
                List<Map<String, Object>> ipData = blMedicalExe2Mapper.ipDataCheck(paramMap.get("pkCnord").toString());
                for (Map<String, Object> map : ipData) {
                    if (map.get("pkCgip") == null) {
                        throw new BusException("有未记费项目，不能执行！");
                    }
                }
            }
            // 医技执行
            try {
                blMedicalExe2Mapper.medExeOcc(paramMap);
            } catch (Exception e) {
                throw new BusException("执行失败！");
            }
            // 如果该执行单关联了预约信息，同时更新预约记录状态为“到达”
            List<Map<String, Object>> occlist = blMedicalExe2Mapper.queryExAssistOccList(paramMap);
            for (Map<String, Object> map : occlist) {
                if (map.get("pkMsp") != null) {
                    map.put("pkMsp", map.get("pkMsp").toString().trim());
                }
                if (map.get("dateAppt") != null) {
                    // 如果该执行单关联了预约信息(预约时间不为空)，同时更新预约记录状态为“到达”
                    DataBaseHelper.execute("update sch_appt  set eu_status='1' where pk_schappt in(select pk_schappt from sch_appt_ord where pk_assocc=?) and eu_status='0'", map.get("pkAssocc"));
                    // 更新医嘱预约记录的执行标志；
                    DataBaseHelper.execute("update sch_appt_ord set flag_exec='1' where pk_assocc=? and flag_exec='0'", map.get("pkAssocc"));
                }
            }
            // 更新医嘱状态为3（检查）
            DataBaseHelper.execute("update cn_ris_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            DataBaseHelper.execute("update cn_lab_apply set eu_status='3' where pk_cnord=? and eu_status in ('1','2')", new Object[]{paramMap.get("pkCnord").toString()});
            //发送检查检验记费信息至平台
//                Map<String, Object> paramListMap = new HashMap<String, Object>();
//                paramListMap.put("dtlist", paramMapList);
//                paramListMap.put("type", "I");
//                paramListMap.put("Control", "OK");
//                PlatFormSendUtils.sendBlMedApplyMsg(paramListMap);
//                paramListMap = null;
            // 执行成功之后，讲结果返回前台
//                return occlist;
        }




        ack.put("ackCode", "AA");
        ack.put("ackDetail", "消息处理成功");
        responseBody.setAck(ack);
        return JSON.toJSONString(responseBody);
    }
}
