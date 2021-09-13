package com.zebone.nhis.webservice.pskq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zebone.nhis.common.module.base.ou.BdOuOrg;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.nhis.webservice.pskq.dao.EmployeeDao;
import com.zebone.nhis.webservice.pskq.factory.MessageFactory;
import com.zebone.nhis.webservice.pskq.model.Employee;
import com.zebone.nhis.webservice.pskq.model.entity.BdOuEmployee;
import com.zebone.nhis.webservice.pskq.model.listener.ResultListener;
import com.zebone.nhis.webservice.pskq.model.message.AckElement;
import com.zebone.nhis.webservice.pskq.model.message.DataElement;
import com.zebone.nhis.webservice.pskq.model.message.EmployeeResultResponse;
import com.zebone.nhis.webservice.pskq.model.message.EventElement;
import com.zebone.nhis.webservice.pskq.model.message.RequestBody;
import com.zebone.nhis.webservice.pskq.model.message.ResponseBody;
import com.zebone.nhis.webservice.pskq.service.EmployeeService;
import com.zebone.nhis.webservice.pskq.utils.MessageBodyUtil;
import com.zebone.platform.modules.dao.DataBaseHelper;

import net.sf.json.JSONObject;

public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public void findById(String param, ResultListener listener) {
        try {
            Map<String,Object> responseMap = new HashMap<>();
            String result = "";
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            AckElement ackElement = new AckElement();
            ackElement.setTargetMessageId(requestBody.getId());
            Map<String,Object> query = requestBody.getQuery();
            if(query.containsKey("EMPLOYEE")&&query.get("EMPLOYEE")!=null){
                List<DataElement> dataElement = (List<DataElement>) query.get("EMPLOYEE");
                Employee employee = (Employee) MessageFactory.deserialization(dataElement, new Employee());
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                EmployeeDao employeeDao = applicationContext.getBean("employeeDao", EmployeeDao.class);
                employee = employeeDao.findEmpInfoById(employee.getPkEmployee());//无员工主索引，改为员工主键
                if(employee!=null){
                    ackElement.setAckCode("AA");
                    ackElement.setAckDetail("查询成功");
                    List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(employee);
                    EmployeeResultResponse employeeResultResponse = new EmployeeResultResponse();
                    employeeResultResponse.setEmployee(dataElements);
                    ackElement.setEmployeeResultResponse(employeeResultResponse);
                    responseBody.setQueryAck(ackElement);
                    listener.success(responseBody);
                }else {
                    listener.error("没有查询到人员信息");
                    return;
                }
            }else {
                listener.error("没有查询到人员信息");
                return;
            }
        }catch (Exception e){
            listener.error(e.getMessage());
            return;
        }
    }

    @Override
    public void findByOrgCode(String param, ResultListener listener) {
        try {
            Map<String,Object> responseMap = new HashMap<>();
            String result = "";
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            ResponseBody responseBody = new ResponseBody();
            responseBody.setService(requestBody.getService());
            responseBody.setEvent(requestBody.getEvent());
            responseBody.setId(requestBody.getId());
            responseBody.setCreationTime(requestBody.getCreationTime());
            responseBody.setSender(requestBody.getSender());
            responseBody.setReceiver(requestBody.getReceiver());
            AckElement ackElement = new AckElement();
            ackElement.setTargetMessageId(requestBody.getId());
            Map<String,Object> query = requestBody.getQuery();
            if(query.containsKey("EMPLOYEE")&&query.get("EMPLOYEE")!=null){
                List<DataElement> dataElement = (List<DataElement>) query.get("EMPLOYEE");
                Employee employee = (Employee) MessageFactory.deserialization(dataElement, new Employee());
                ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
                EmployeeDao employeeDao = applicationContext.getBean("employeeDao", EmployeeDao.class);

                //通过机构编码查询机构主键
                BdOuOrg bdOuOrg = DataBaseHelper.queryForBean("select * from BD_OU_ORG where CODE_ORG = ?",BdOuOrg.class,employee.getOrgCode());

                if(bdOuOrg==null){
                    listener.error("没有查询到机构信息");
                    return;
                }
                //无员工主索引，改为员工主键
                List<Employee> employeeList = employeeDao.findByOrgCode(bdOuOrg.getPyCode());


                if(employee!=null){
                    ackElement.setAckCode("AA");
                    ackElement.setAckDetail("查询成功");
                    List<DataElement> dataElements = MessageBodyUtil.dataElementsFactory(employee);
                    EmployeeResultResponse employeeResultResponse = new EmployeeResultResponse();
                    employeeResultResponse.setEmployee(dataElements);
                    ackElement.setEmployeeResultResponse(employeeResultResponse);
                    responseBody.setQueryAck(ackElement);
                    listener.success(responseBody);
                }else {
                    listener.error("没有查询到人员信息");
                    return;
                }
            }else {
                listener.error("没有查询到人员信息");
                return;
            }
        }catch (Exception e){
            listener.error(e.getMessage());
            return;
        }
    }

    @Override
    public void save(String param, ResultListener listener) {
        try {
            Gson gson =  new GsonBuilder().setDateFormat("yyyyMMdd'T'HHmmss").create();
            RequestBody requestBody = gson.fromJson(param,RequestBody.class);
            EventElement event=requestBody.getEvent();
            List<DataElement> list = (List<DataElement>) requestBody.getMessage().get("EMPLOYEE");
            Employee department = (Employee) MessageFactory.deserialization(list, new Employee());
            JSONObject json1 =JSONObject.fromObject(department);
            String text = json1.toString();
            BdOuEmployee bdOuEmployee= JSON.parseObject(text, BdOuEmployee.class);
            if(null==bdOuEmployee){
                listener.error("消息处理失败，原因：反序列化失败");
                return;
            }
            //校验非空
            if (CommonUtils.isEmptyString(bdOuEmployee.getPkOrg()) || CommonUtils.isEmptyString(bdOuEmployee.getCodeEmp())
                    || CommonUtils.isEmptyString(bdOuEmployee.getNameEmp())|| CommonUtils.isEmptyString(bdOuEmployee.getDtIdentype())
                    || CommonUtils.isEmptyString(bdOuEmployee.getDtSex())) {
                listener.error("消息处理失败，原因：必填项为空，请检查！");
                return;
            }
            if(!CommonUtils.isEmptyString(bdOuEmployee.getPkOrg())){
                bdOuEmployee.setPkOrg(getOrgByCode(bdOuEmployee.getPkOrg()).get("pkOrg").toString());
            }
            if(!CommonUtils.isEmptyString(bdOuEmployee.getCreator())){
                bdOuEmployee.setCreator(getEmpByCode(bdOuEmployee.getCreator()).get("pkEmp").toString());
            }
            if(!CommonUtils.isEmptyString(bdOuEmployee.getModifier())){
                bdOuEmployee.setModifier(getEmpByCode(bdOuEmployee.getModifier()).get("pkEmp").toString());
            }
            bdOuEmployee.setDelFlag("0");
            if(event.getEventCode().equals("E000801")){//新增
                if(null!=getEmpByCode(bdOuEmployee.getCodeEmp())){
                    listener.error("消息处理失败，原因：人员编码重复，请检查！");
                    return;
                }
                ApplicationUtils.setDefaultValue(bdOuEmployee, true);
                DataBaseHelper.insertBean(bdOuEmployee);
            }else{//修改科室
                Map<String, Object> m=getEmpByCode(bdOuEmployee.getCodeEmp());
                if(null==m){
                    listener.error("消息处理失败，原因：人员编码不存在，请检查！");
                    return;
                }
                bdOuEmployee.setPkEmp(m.get("pkEmp").toString());
                DataBaseHelper.updateBeanByPk(bdOuEmployee,false);
            }
           listener.success("success");
            return;
        }catch (Exception e){
            listener.exception(e.getMessage());
            return;
        }
    }

    private Map<String, Object> getOrgByCode(String code) {
        List<Map<String, Object>> orgDetils = DataBaseHelper.queryForList(
                "select pk_org,code_org,name_org,shortname from bd_ou_org where code_org = ?  and flag_active='1' and del_flag='0' ",
                code);
        if (orgDetils != null && orgDetils.size() > 0)
            return orgDetils.get(0);
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
