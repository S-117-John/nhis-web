package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.zebone.nhis.common.module.base.bd.res.BdDeptUnit;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqTriageSendHandlerMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageDeptVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageRegVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageReqBaseVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageRespVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.TriageUserVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 排队叫号对接
 */
@Service
public class PskqTriageSendHandler implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger("nhis.otherPlatLog");
    private String url="";
    public static final String AddState = "_ADD";
    public static final String UpdateState = "_UPDATE";
    public static final String DelState = "_DELETE";
    
    @Resource
    private PskqTriageSendHandlerMapper triageSendHandlerMapper;
    
    private RestTemplate restTemplate;

    /**
     * 发送科室信息
     * @param paramMap
     */
    public void sendBdOuDeptMsg(Map<String, Object> paramMap) {
        if(MapUtils.isEmpty(paramMap) || paramMap.get("dept")==null) {
            return;
        }
        //Map<String,Object> dept = BeanMap.create(paramMap.get("dept"));
        BdOuDept dept = (BdOuDept)paramMap.get("dept");
        List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
        String rleCode = MapUtils.getString(paramMap, "STATUS");
        if(StringUtils.indexOfAny(rleCode,AddState,UpdateState) >=0){
            TriageDeptVo deptVo = new TriageDeptVo();
            deptVo.setNameDept(dept.getNameDept());
            deptVo.setPinyinCode(dept.getPyCode());
            deptVo.setPkDept(dept.getPkDept());
            deptVo.setShortNameDept(dept.getShortname());
            String reqData = JsonUtil.writeValueAsString(new TriageReqBaseVo(deptVo));
            String responseBody = request(reqData, "syncDept");
            logger.info("同步科室信息PK：{},结果:{}",deptVo.getPkDept(),responseBody);
        }
        //第一次同步全部科室
		/*
		 * List<BdOuDept> schAppList =
		 * DataBaseHelper.queryForList("SELECT * FROM Bd_Ou_Dept ",BdOuDept.class); for
		 * (BdOuDept dept : schAppList) { TriageDeptVo deptVo = new TriageDeptVo();
		 * deptVo.setNameDept(dept.getNameDept());
		 * deptVo.setPinyinCode(dept.getPyCode()); deptVo.setPkDept(dept.getPkDept());
		 * deptVo.setShortNameDept(dept.getShortname()); String reqData =
		 * JsonUtil.writeValueAsString(new TriageReqBaseVo(deptVo)); String responseBody
		 * = request(reqData, "syncDept");
		 * logger.info("同步科室信息PK：{},结果:{}",deptVo.getPkDept(),responseBody); }
		 */
    }

    /**
     * 发送医生信息<br>
     *     顺带发送医生看诊科室信息
     * @param paramMap
     */
    public void sendUserMsg(Map<String,Object> paramMap){
        String rleCode = MapUtils.getString(paramMap, "STATUS");
        if(StringUtils.indexOfAny(rleCode,AddState,UpdateState) >=0){
            Map<String,Object> objectMap = (Map<String,Object>)MapUtils.getObject(paramMap, "user");
            String pkUser = MapUtils.getString(MapUtils.getMap(objectMap, "user"), "pkUser");
            if(StringUtils.isNotBlank(pkUser)){
                Map<String,Object> param = new HashMap<>();
                param.put("pkUser", pkUser);
                param.put("now", Application.isSqlServer()?"getdate()":"sysdate");
                List<TriageUserVo> userVoList = triageSendHandlerMapper.getUserInfo(param);
                if(!userVoList.isEmpty()){
                    TriageUserVo vo = userVoList.get(0);
                    String reqData = JsonUtil.writeValueAsString(new TriageReqBaseVo(vo));
                    String responseBody = request(reqData, "syncDoctor");
                    logger.info("同步医生信息PK：{},结果:{}",vo.getPkDoctor(),responseBody);

                    //1.4.同步医生看诊科室信息--用户组(包含共享用户组）--部门   {只有操作用户才出发，操作用户组目前没有接口触发}
                    List<Map<String, Object>> usrgList = triageSendHandlerMapper.getUserDept(pkUser);
                    if(!usrgList.isEmpty()){
                        StringBuilder sbl = new StringBuilder(JsonUtil.writeValueAsString(new TriageReqBaseVo(usrgList.get(0))));
                        //多个时，按照对方要求奇怪的姿势拼接多个？
                        usrgList.remove(0);
                        if(!usrgList.isEmpty()) {
                            sbl.replace(sbl.length()-1,sbl.length(),"");
                            String more = JsonUtil.writeValueAsString(usrgList);
                            sbl.append(",").append(more.substring(1, more.length()-1)).append("}");
                        }
                        responseBody = request(sbl.toString(), "syncDoctorDeptVisit");
                        logger.info("同步医生看诊信息PK：{},结果:{}",vo.getPkDoctor(),responseBody);
                    }
                }
            }
        }
    }

    /**
     * 发送医生信息<br>
     *     人员信息修改且有对应机构用户时处理，等于重新给同步一次数据。直接新增人员没有登录信息不会触发
     * @param paramMap
     */
    public void sendEmpMsg(Map<String,Object> paramMap){
        if(MapUtils.isNotEmpty(paramMap)){
            Map<String,Object> objectMap = (Map<String,Object>)MapUtils.getObject(paramMap, "emp");
            String codeEmp = MapUtils.getString(objectMap, "codeEmp");
            String nameEmp = MapUtils.getString(objectMap, "nameEmp");
            Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select ou.PK_USER from BD_OU_EMPLOYEE emp inner join bd_ou_user ou on emp.PK_EMP=ou.PK_EMP where emp.CODE_EMP=? and emp.NAME_EMP=? and emp.FLAG_ACTIVE='1'", new Object[]{codeEmp, nameEmp});
            if(queryForMap != null && !queryForMap.isEmpty()) {
                //不改变原始入参，重新构造入参
                Map<String,Object> user = new HashMap<>();
                user.put("user", queryForMap);
                Map<String,Object> param = new HashMap<>();
                param.putAll(paramMap);
                param.remove("emp");
                param.put("user", user);
                sendUserMsg(param);
            }
        }
        //第一次同步所有医生
		/*
		 * List<Map<String, Object>> schAppList
		 * =DataBaseHelper.queryForList("SELECT code_emp,name_emp FROM BD_OU_EMPLOYEE ",
		 * new Object[]{}); for (Map<String, Object> map : schAppList) { String codeEmp
		 * = MapUtils.getString(map, "codeEmp"); String nameEmp =
		 * MapUtils.getString(map, "nameEmp"); Map<String, Object> queryForMap =
		 * DataBaseHelper.
		 * queryForMap("select ou.PK_USER from BD_OU_EMPLOYEE emp inner join bd_ou_user ou on emp.PK_EMP=ou.PK_EMP where emp.CODE_EMP=? and emp.NAME_EMP=? and emp.FLAG_ACTIVE='1'"
		 * , new Object[]{codeEmp, nameEmp}); if(queryForMap != null &&
		 * !queryForMap.isEmpty()) { Map<String,Object> user = new HashMap<>();
		 * user.put("user", queryForMap); Map<String,Object> param = new HashMap<>();
		 * param.put("STATUS", "_UPDATE"); param.put("codeEmp",
		 * UserContext.getUser().getCodeEmp()); param.put("user", user);
		 * sendUserMsg(param); } }
		 */ 
    }

    public void sendReg(Map<String,Object> paramMap){
        String pkPv = null;
        if(MapUtils.isNotEmpty(paramMap) && StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
            List<TriageRegVo> regVoList = triageSendHandlerMapper.getRegInfo(pkPv);
            if(!regVoList.isEmpty()){
                String responseBody = request(JsonUtil.writeValueAsString(new TriageReqBaseVo(regVoList.get(0))), "syncRegistered");
                logger.info("发送挂号PK：{},结果:{}",pkPv,responseBody);
            }
        }
    }


    /**
     * 退号
     * @param paramMap
     */
    public void sendCancelReg(Map<String,Object> paramMap){
        String pkPv = null;
        if(MapUtils.isNotEmpty(paramMap) && StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
            Map<String,Object> map = new HashMap<>();
            map.put("pkRegistered", pkPv);
            String reqData = JsonUtil.writeValueAsString(new TriageReqBaseVo(map));
            String responseBody = request(reqData, "resign");
            logger.info("发送退号PK：{},结果:{}",pkPv,responseBody);
        }
    }

    /**
     * 发送诊室信息
     * @param paramMap
     */
    public void sendBdDeptUnitMsg(Map<String, Object> paramMap) {
        if(MapUtils.isEmpty(paramMap) || paramMap.get("pkDepts")==null) {
            return;
        }
        Set<String> pkDepts = (Set<String>)paramMap.get("pkDepts");
        String unitSql = "select pk_deptunit pk_clinic,name name_clinic,'1' type_clinic ,'' remark ,pk_dept from bd_dept_unit where pk_dept in (" + CommonUtils.convertSetToSqlInPart(pkDepts, "pk_dept") + ")";
		List<Map<String,Object>> bdDeptUnitList = DataBaseHelper.queryForList(unitSql);
        if(!bdDeptUnitList.isEmpty()){
        	for (Map<String,Object> bdDeptUnitMap : bdDeptUnitList) {
        		 String responseBody = request(JsonUtil.writeValueAsString(new TriageReqBaseVo(bdDeptUnitMap)), "sync/syncClinic");
                 logger.info("发送诊室信息：{},结果:{}",bdDeptUnitMap,responseBody);
			}
        }
    }
    
    private String request(String data,String remoteMethod){
        String rs="",status="SAVE",error="";
        try{
            logger.info("请求排队叫号：{}",data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
            rs = restTemplate.postForObject(url+"/"+remoteMethod,formEntity,String.class);
            if(StringUtils.isNotBlank(rs)){
                TriageRespVo<?> vo = JsonUtil.readValue(rs, new TypeReference<TriageRespVo>() {});
                if(StringUtils.equals(vo.getCode(), EnumerateParameter.ZERO)){
                    status = "ACK";
                } else {
                    error = vo.getMsg();
                }
            }
        } catch (Exception e){
            status="ERROR";
            error = e.getMessage();;
            logger.error("排队叫号接口{}调用异常：{}",remoteMethod,e);
        }
        //异常与否，写消息记录
        if(StringUtils.isNotBlank(data)){
//            ServiceLocator.getInstance().getBean(PskqTriageSendHandler.class).addMsg(remoteMethod,data,status,error);
        }
        return rs;
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void addMsg(String remoteMethod,String msg,String status,String error){
        SysMsgRec rec = new SysMsgRec();
        rec.setTransType("Send");
        rec.setMsgType(remoteMethod);
        rec.setMsgId(NHISUUID.getKeyId());
        rec.setTransDate(new Date());
        rec.setMsgContent(msg);
        rec.setSysCode("NHIS");
        rec.setMsgStatus(status);
        rec.setErrTxt(error);
        rec.setRemark("Triage");
        DataBaseHelper.insertBean(rec);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        url = ApplicationUtils.getPropertyValue("msg.triage.address", "");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }


}
