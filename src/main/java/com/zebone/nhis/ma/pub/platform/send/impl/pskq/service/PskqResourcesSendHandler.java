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
 * 资源池对接
 */
@Service
public class PskqResourcesSendHandler implements InitializingBean {
    private Logger logger = LoggerFactory.getLogger("nhis.otherPlatLog");
    private String url="";
    public static final String AddState = "_ADD";
    public static final String UpdateState = "_UPDATE";
    public static final String DelState = "_DELETE";
    
    @Resource
    private PskqTriageSendHandlerMapper triageSendHandlerMapper;
    
    private RestTemplate restTemplate;

    /**
     * 发送停诊信息
     * @param paramMap
     */
    @SuppressWarnings("unchecked")
	public void sendStopSchMsg(Map<String, Object> paramMap) {
    	if (paramMap == null) return;
        List<String> pkSchList = (List<String>) paramMap.get("pkSchs");
        if(null==pkSchList || pkSchList.size()<1)return;
        String pkSchs=CommonUtils.convertListToSqlInPart(pkSchList);
        String sql = "SELECT '"+ApplicationUtils.getPropertyValue("health.orgid", "")+"' org_Uuid,bod.CODE_DEPT org_Dept_Uuid,emp.CODE_EMP org_Dept_Emp_Uuid,TO_CHAR(sch.DATE_WORK,'yyyy-mm-dd') sdate,"
        		+ "case when TO_CHAR(sch.DATE_WORK,'yyyy-mm-dd')=TO_CHAR(SYSDATE) then '99' else '1' end schedule_Type,"
        		+ "case when slot.code_dateslot='0101' then '1' else '2' end time_Part_Type,slot.name_dateslot,'2' schedule_Status,TO_CHAR(SYSDATE,'yyyy-MM-dd HH:mm:ss')  schedule_Time "
        		+ "from SCH_SCH sch "
        		+ "left join BD_OU_DEPT bod on sch.PK_DEPT=bod.PK_DEPT "
        		+ "INNER JOIN sch_resource res ON sch.pk_schres = res.pk_schres AND res.eu_schclass = '0' "
        		+ "LEFT JOIN bd_ou_employee emp ON emp.pk_emp = res.pk_emp "
        		+ "LEFT JOIN bd_code_dateslot slot ON sch.pk_dateslot = slot.pk_dateslot "
        		+ "where sch.FLAG_STOP='1' and PK_SCH in ("+pkSchs+") ";
        List<Map<String,Object>> bdDeptUnitList = DataBaseHelper.queryForList(sql);  
        if(null==bdDeptUnitList || bdDeptUnitList.size()<1)return;
        String req = JsonUtil.writeValueAsString(bdDeptUnitList);
        String responseBody = request(req);
        logger.info("发送资源池停诊接口：{},结果:{}",req,responseBody);
    }
    private String request(String data){
        String rs="",status="SAVE",error="";
        try{
            logger.info("请求资源池接口：{}",data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> formEntity = new HttpEntity<String>(data, headers);
            rs = restTemplate.postForObject(url+"PC001",formEntity,String.class);
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
            logger.error("资源池接口{}调用异常：{}","PC001",e);
        }
        //异常与否，写消息记录
        if(StringUtils.isNotBlank(data)){
            ServiceLocator.getInstance().getBean(PskqResourcesSendHandler.class).addMsg("PC001",data,status,error);
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
        url = ApplicationUtils.getPropertyValue("msg.resources.address", "");
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }


}
