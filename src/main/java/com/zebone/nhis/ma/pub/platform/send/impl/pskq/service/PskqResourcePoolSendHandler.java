package com.zebone.nhis.ma.pub.platform.send.impl.pskq.service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao.PskqResourcePoolSendMapper;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.vo.ResourcePoolResVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.modules.core.spring.ServiceLocator;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 资源池订单上传对接
 */
@Service
public class PskqResourcePoolSendHandler implements InitializingBean {
	private Logger logger = LoggerFactory.getLogger("nhis.ResourcePoolLog");
    private String url="";
	// http地址
	private String tokenurl = "";
	//应用id
	private  String appid = "";
	//应用key
	private  String key = "";
	
    @Resource
    private PskqResourcePoolSendMapper pskqResourcePoolSendMapper;
    
    private RestTemplate restTemplate;
	/**
	 * 【资源池】挂号订单推送 PD001
	 * @param args
	 * @return
	 */
	public void sendReg(Map<String,Object> paramMap) {
		String pkPv = null;
		if(MapUtils.isNotEmpty(paramMap) && !CommonUtils.isEmptyString(url) 
				&& StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
			List<Map<String, Object>> patiOpRegInfo = pskqResourcePoolSendMapper.getPatiOpRegInfo(pkPv);
			if(patiOpRegInfo != null && patiOpRegInfo.size() > 0) {
				//将Data内容转换为json格式
				String dataJson = JsonUtil.writeValueAsString(patiOpRegInfo);
				//发送请求
				String responseBody = resourceRts(dataJson,"PD001");
		        logger.info("发送资源池挂号PK：{},结果:{}",pkPv,responseBody);
			}	
		}
		
	}
	
	 /**
     * 【资源池】退号订单推送 PD001
     * @param paramMap
     */
    public void sendCancelReg(Map<String,Object> paramMap){
    	String pkPv = null;
		if(MapUtils.isNotEmpty(paramMap) && !CommonUtils.isEmptyString(url) 
				&& StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
			List<Map<String, Object>> patiOpRegInfo = pskqResourcePoolSendMapper.getPatiOpRegInfo(pkPv);
			if(patiOpRegInfo != null && patiOpRegInfo.size() > 0) {
				//将Data内容转换为json格式
				String dataJson = JsonUtil.writeValueAsString(patiOpRegInfo);
				//发送请求
				String responseBody = resourceRts(dataJson,"PD001");
		        logger.info("发送资源池退号PK：{},结果:{}",pkPv,responseBody);
			}	
		}
    }
	
	/**
	 * 【资源池】入院推送 PD003
	 * @param args
	 * @return
	 */
	public void sendPvInMsg(Map<String,Object> paramMap) {
		String pkPv = null;
		if(MapUtils.isNotEmpty(paramMap) && !CommonUtils.isEmptyString(url) 
				&& StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
			//获取入院信息
			List<Map<String, Object>> patiAdtRegInfo = pskqResourcePoolSendMapper.getPatiAdtRegInfo(pkPv);
			if(patiAdtRegInfo != null && patiAdtRegInfo.size() > 0) {
				//将Data内容转换为json格式
				String dataJson = JsonUtil.writeValueAsString(patiAdtRegInfo);
				//发送请求
				String responseBody = resourceRts(dataJson,"PD003");
		        logger.info("发送入院登记PK：{},结果:{}",pkPv,responseBody);
			}	
		}
	}

	/**
	 * 【资源池】出院推送   PD004
	 * @param param
	 * @param user
	 * @return
	 */
	public void sendPvOutMsg(Map<String,Object> paramMap){
		String pkPv = null;
		if(MapUtils.isNotEmpty(paramMap) && !CommonUtils.isEmptyString(url) 
				&& StringUtils.isNotBlank(pkPv = MapUtils.getString(paramMap,"pkPv"))){
			//获取入院信息
			List<Map<String, Object>> patiHospInfo = pskqResourcePoolSendMapper.getPatiHospInfo(pkPv);
			if(patiHospInfo != null && patiHospInfo.size() > 0) {
				//将Data内容转换为json格式
				String dataJson = JsonUtil.writeValueAsString(patiHospInfo);
				//发送请求
				String responseBody = resourceRts(dataJson,"PD004");
		        logger.info("发送出院登记PK：{},结果:{}",pkPv,responseBody);
			}	
		}
	}
	

	/**
	 * 组织主参数，调用http服务
	 * @param dataJson  数据参数
	 * @param version	服务版本
	 * @param serviceName	服务名称	
	 */
	private String resourceRts(String dataJson,String serviceName){
		String rs="",status="SAVE",error="";
		try{
			/**调用服务接口*/
			//获取访问令牌
			logger.info("请求资源池：{}",dataJson);
			StringBuffer toKenUrlStr = new StringBuffer(String.format("%s", tokenurl));
			toKenUrlStr.append(String.format("a=%s", appid));//appid
			toKenUrlStr.append(String.format("&s=%s", key));//key
			HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            
            HttpEntity<String> formEntity = new HttpEntity<String>(dataJson, headers);
            String resTokenJson = restTemplate.postForObject(toKenUrlStr.toString(),formEntity,String.class); 
			Map<String, Object> toKenMap = JsonUtil.readValue(resTokenJson,new TypeReference<Map<String, Object>>() {});
			if(toKenMap == null || !"0".equals(CommonUtils.getPropValueStr(toKenMap,"code"))){
				error = CommonUtils.getPropValueStr(toKenMap,"msg");
			}else {
				String token = CommonUtils.getPropValueStr(toKenMap,"access_token");	
				//调用服务地址
				StringBuffer urlStr = new StringBuffer(String.format("%s", url));	
				urlStr.append(String.format("m=%s", serviceName));//功能
				urlStr.append(String.format("&token=%s", token));//令牌				
				formEntity = new HttpEntity<String>(dataJson, headers);
	            rs = restTemplate.postForObject(urlStr.toString(),formEntity,String.class);
			}
            if(StringUtils.isNotBlank(rs)){
            	ResourcePoolResVO<?> vo = JsonUtil.readValue(rs, new TypeReference<ResourcePoolResVO>() {});
                if(StringUtils.equals(vo.getCode(), EnumerateParameter.ZERO)){
                    status = "AA";
                    error = rs;
                } else {
                    error = vo.getMsg();
                }
            }
        } catch (Exception e){
            status="ERROR";
            error = e.getMessage();;
            logger.error("资源池接口{}调用异常：{}",serviceName,e);
        }
        //异常与否，写消息记录
        if(StringUtils.isNotBlank(dataJson)){
            ServiceLocator.getInstance().getBean(PskqTriageSendHandler.class).addMsg(serviceName,dataJson,status,error);
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
    	//发送订单url
        url = ApplicationUtils.getPropertyValue("resourcePool.url", "");
        //获取tokenurl
        tokenurl = ApplicationUtils.getPropertyValue("resourcePool.tokenUrl", "");
    	//应用id
    	appid = ApplicationUtils.getPropertyValue("resourcePool.appid", "");
    	//应用key
    	key = ApplicationUtils.getPropertyValue("resourcePool.key", "");

    	SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }


}
