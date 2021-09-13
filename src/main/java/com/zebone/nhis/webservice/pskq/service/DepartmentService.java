package com.zebone.nhis.webservice.pskq.service;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.DepartmentDao;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.Department;
import com.zebone.nhis.webservice.pskq.model.entity.BdOuDept;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.*;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {


    public String findById(String param) {
        Map<String, Object> ack = new HashMap<>(16);
        ResponseBody responseBody = new ResponseBody();
        try {
            Map<String,Object> responseMap = new HashMap<>();
            String result = "";
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            AckElement ackElement = new AckElement();
            ackElement.setTargetMessageId(requestBody.getId());
            ack.put("targetMessageId", requestBody.getId());
            Map<String,Object> query = requestBody.getQuery();
            if(query.containsKey("DEPARTMENT")&&query.get("DEPARTMENT")!=null){
                List<DataElement> dataElement = (List<DataElement>) query.get("DEPARTMENT");
                Department department = (Department) MessageFactory.deserialization(dataElement, new Department());
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                DepartmentDao departmentDao = applicationContext.getBean("departmentDao", DepartmentDao.class);
                department = departmentDao.findDeptInfoById(department.getDeptId());
                if(department!=null){
                    ackElement.setAckCode("AA");
                    ackElement.setAckDetail("查询成功");
                    List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(department);
                    DepartmentResultResponse departmentResultResponse = new DepartmentResultResponse();
                    departmentResultResponse.setDepartment(dataElements);
                    ackElement.setDepartmentResultResponse(departmentResultResponse);
                    responseBody.setQueryAck(ackElement);
                    ack.put("ackCode", "AA");
                    ack.put("ackDetail", "消息处理成功");
                    responseBody.setAck(ack);
                    return JSON.toJSONString(responseBody);
                }else {
                    ack.put("ackCode", "AE");
                    ack.put("ackDetail", "消息处理失败：没有查询到科室信息");
                    responseBody.setAck(ack);
                    return JSON.toJSONString(responseBody);
                }
            }else {
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "消息处理失败：没有查询到科室信息");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
        }catch (Exception e){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败："+e.getMessage());
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
    }

    /**
     * 科室新增、同步
     * @param param
     * @param
     */

    @Transactional
    public String save(String param) {
        Map<String, Object> ack = new HashMap<>(16);

        Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
        RequestBody requestBody = gson.fromJson(param,RequestBody.class);
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
        ack.put("targetMessageId", requestBody.getId());
        EventElement event=requestBody.getEvent();
        List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("DEPARTMENT");
        Department department = null;
        try {
            department = (Department) MessageFactory.deserialization(list, new Department());
        } catch (IllegalAccessException e) {

        }
        BdOuDept bdOuDept= new BdOuDept();
        if(null==department){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：无法解析消息");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        bdOuDept.setFlagActive(department.getValidState());
        bdOuDept.setPyCode(department.getSpellCode());
        bdOuDept.setNameDept(department.getDeptName());
        bdOuDept.setCodeDept(department.getDeptId());
        bdOuDept.setPkOrg(department.getOrgCode());
        if(StringUtils.isEmpty(department.getEnterOperaId())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：enterOperaId为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        bdOuDept.setCreator(department.getEnterOperaId());
        if(StringUtils.isEmpty(department.getModifyOperaId())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：modifyOperaId为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        bdOuDept.setModifier(department.getModifyOperaId());
        if(StringUtils.isEmpty(department.getDeptParentId())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：deptParentId为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        bdOuDept.setPkFather(department.getDeptParentId());
        //校验非空
        if(CommonUtils.isEmptyString(bdOuDept.getFlagActive())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：validState为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        if(CommonUtils.isEmptyString(bdOuDept.getPyCode())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：spellCode为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        if(CommonUtils.isEmptyString(bdOuDept.getNameDept())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：deptName为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        if(CommonUtils.isEmptyString(bdOuDept.getCodeDept())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：deptId为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        if(CommonUtils.isEmptyString(bdOuDept.getPkOrg())){
            ack.put("ackCode", "AE");
            ack.put("ackDetail", "消息处理失败，原因：OrgCode为空");
            responseBody.setAck(ack);
            return JSON.toJSONString(responseBody);
        }
        Map<String,Object> orgDetils = DataBaseHelper.queryForMap(
                "select pk_org from bd_ou_org where del_flag='0' and pk_org is not null and pk_org<>'~'");
        bdOuDept.setPkOrg(MapUtils.getString(orgDetils,"pkOrg"));
        if(!CommonUtils.isEmptyString(bdOuDept.getCreator())){
            Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_employee where code_emp=? and del_flag='0'",bdOuDept.getCreator());
            bdOuDept.setCreator(empMap.get("pkEmp").toString());
        }
        if(!CommonUtils.isEmptyString(bdOuDept.getModifier())){
            Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_employee where code_emp=? and del_flag='0'",bdOuDept.getModifier());
            bdOuDept.setModifier(empMap.get("pkEmp").toString());
        }
        if(!CommonUtils.isEmptyString(bdOuDept.getPkFather())){
            Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_dept where code_dept=? and del_flag='0'",bdOuDept.getPkFather());
            bdOuDept.setPkFather(empMap.get("pkDept").toString());
        }
        bdOuDept.setDelFlag("0");
        if(event.getEventCode().equals("E000502")){//新增科室
            if(null!=getDeptByCode(bdOuDept.getCodeDept())){
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "消息处理失败，原因：科室编码重复");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            DataBaseHelper.insertBean(bdOuDept);
        }else{//修改科室
            Map<String, Object> m=getDeptByCode(bdOuDept.getCodeDept());
            if(null==m){
                ack.put("ackCode", "AE");
                ack.put("ackDetail", "消息处理失败，原因：科室编码不存在");
                responseBody.setAck(ack);
                return JSON.toJSONString(responseBody);
            }
            bdOuDept.setPkDept(m.get("pkDept").toString());
            DataBaseHelper.updateBeanByPk(bdOuDept,false);
        }
        ack.put("ackCode", "AA");
        ack.put("ackDetail", "消息处理成功");
        responseBody.setAck(ack);
        return JSON.toJSONString(responseBody);
    }

    private Map<String, Object> getOrgByCode(String code) {
        List<Map<String, Object>> orgDetils = DataBaseHelper.queryForList(
                "select pk_org,code_org,name_org,shortname from bd_ou_org where code_org = ?  and flag_active='1' and del_flag='0' ",
                code);
        if (orgDetils != null && orgDetils.size() > 0) {
            return orgDetils.get(0);
        }
        return null;
    }

    private Map<String, Object> getEmpByCode(String code) {
        Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_employee where code_emp=? and del_flag='0'",code);
        return empMap;
    }
    private Map<String, Object> getDeptByCode(String code) {
        Map<String, Object> empMap= DataBaseHelper.queryForMap("SELECT * FROM bd_ou_dept where code_dept=? and del_flag='0'",code);
        return empMap;
    }
}
