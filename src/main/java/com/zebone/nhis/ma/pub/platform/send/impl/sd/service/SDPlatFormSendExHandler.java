package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendEx;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendIp;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.modules.utils.JsonUtil;

import ca.uhn.hl7v2.HL7Exception;

/**
 * 发送EX领域消息
 * @author yangxue
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendExHandler {
	@Resource
	private SDMsgSendEx sDMsgSendEx;
	@Resource
	private SDMsgSendIp sDMsgSendIp;
	@Resource
    private SDMsgMapper sDMsgMapper;

	/**
	 * 发送检验医嘱消息（医嘱执行）(暂不发消息)（入口已经注释）
	 * @param paramMap
	 * @throws HL7Exception
	 */
//	public void  sendExConfirmMsg(Map<String,Object> paramMap) throws HL7Exception{
//		List<Map<String,Object>> listMap = (List<Map<String,Object>>)paramMap.get("exlist");
//		String typeStatus = paramMap.get("typeStatus").toString();
//		sDMsgSendEx.sendRASMsg("O17", listMap,typeStatus);
//	}
	/**
	 * 发送医嘱核对信息
	 * @param paramMap
	 * @throws HL7Exception
	 * @throws InterruptedException
	 */
	public void sendExOrderCheckMsg(Map<String,Object> paramMap) throws HL7Exception, InterruptedException{
		List<Map<String,Object>> listMap = Collections.synchronizedList(new ArrayList<Map<String,Object>>());
		//new CopyOnWriteArrayList<Map<String,Object>>();
		if(paramMap.get("ordlist")!=null){
			if(paramMap.get("ordlist") instanceof Map ){
				listMap = (List<Map<String,Object>>)paramMap.get("ordlist");
			}else{
				listMap = JsonUtil.readValue(ApplicationUtils.objectToJson(paramMap.get("ordlist")), new TypeReference<List<Map<String,Object>>>(){});
			}
		}else if(paramMap.get("ordlistvo")!=null){
			if(paramMap.get("ordlistvo") instanceof Map){
				listMap = (List<Map<String,Object>>)paramMap.get("ordlistvo");
			}else{
				String param = ApplicationUtils.objectToJson(paramMap.get("ordlistvo"));
				listMap = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>(){});
				//listMap = JsonUtil.readValue(, new TypeReference<List<Map<String, Object>>>(){});
			}
		}else if(paramMap.get("pkCnList")!=null){
			listMap = sDMsgMapper.queryOrderByPkCnord((List<String>)paramMap.get("pkCnList"));
		}else if(paramMap.get("pkCnOrds")!=null){
			listMap = (List<Map<String, Object>>) paramMap.get("pkCnOrds");
		}

		String control = SDMsgUtils.getPropValueStr(paramMap, "control");
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "ordStatus"))){
			control = SDMsgUtils.getPropValueStr(paramMap, "control")+SDMsgUtils.getPropValueStr(paramMap, "ordStatus");
		}else {
			control = SDMsgUtils.getPropValueStr(paramMap, "control");
		}
		sDMsgSendEx.sendOMPMsg("O09",listMap,control);
	}

	/**
	 * 住院药房发药
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendScmIpDeDrug(Map<String,Object> paramMap) throws HL7Exception{
		if(paramMap!=null &&paramMap.get("IsSendSDEx")!=null &&(boolean)paramMap.get("IsSendSDEx")==true){
			sDMsgSendEx.sendDeExorderMsg(paramMap);
		}
		sDMsgSendEx.sendRGVMsg("O15", paramMap);

	}

	/**
	 * 发送执行单生成消息
	 * @param paramMap
	 * @throws HL7Exception
	 */

    public void sendExorderCreateMsg(Map<String,Object> paramMap){
		createExOrderMsg(paramMap,"occlist","ADD" );
    }

	/**
	 * 发送取消医嘱执行单消息
	 * @param paramMap
	 */
	public void sendExoderCancelMsg(Map<String,Object> paramMap){
		createExOrderMsg(paramMap,"exlist","DEL" );
	}

	/**
	 * 发送删除医嘱执行单消息(暂时与取消医嘱执行单可公用)
	 * @param paramMap
	 */

	public void sendExoderDelMsg(Map<String,Object> paramMap){
		createExOrderMsg(paramMap,"exlist","DEL" );
	}

	/**
	 * 发送首末次医嘱执行记录
	 * @param paramMap
	 */

	public void sendExorderAllMsg(Map<String,Object> paramMap){
		createExOrderMsg(paramMap, "addList","ADD");
		createExOrderMsg(paramMap, "deleteList","DEL");
	}

	/**
	 * 构建取消执行医嘱消息
	 * @param paramMap
	 * @param key
	 */
	private void createExOrderMsg(Map<String,Object> paramMap,String key,String typeStatus){
		List<ExOrderOcc> delExordList = (List<ExOrderOcc>)paramMap.get(key);
		if(delExordList!=null && delExordList.size()>0){
			List<Map<String,Object>> tempList=new ArrayList<Map<String,Object>>();

			List<String> delPkcnords = new ArrayList<String>();
			for(int i=0;i<delExordList.size();i++){
				delPkcnords.add(delExordList.get(i).getPkCnord());
				ExOrderOcc orderocc=delExordList.get(i);

				Map<String,Object> tempMap=(Map<String, Object>) SDMsgUtils.beanToMap(orderocc);
				if(orderocc.getDateCanc()!=null){
					tempMap.put("dateCanc",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getDateCanc()));
				}
				if(orderocc.getDateOcc()!=null){
					tempMap.put("dateOcc",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getDateCanc()));
				}
				if(orderocc.getDatePlan()!=null){
					tempMap.put("datePlan",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getDatePlan()));
				}
				if(orderocc.getCreateTime()!=null){
					tempMap.put("createTime",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getCreateTime()));
				}
				if(orderocc.getModityTime()!=null){
					tempMap.put("modityTime",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getModityTime()));
				}
				if(orderocc.getTs()!=null){
					tempMap.put("ts",DateUtils.dateToStr("yyyyMMddHHmmss", orderocc.getTs()));
				}
				tempList.add(tempMap);
			}
			List<Map<String, Object>> queryDelExoccByEx = sDMsgMapper.queryOrderByPkCnord(delPkcnords);

			for (int i = 0; i < tempList.size(); i++) {
				Map<String,Object> map=tempList.get(i);
				for(int j=0;j<queryDelExoccByEx.size();j++){
					if(tempList.get(i).get("pkCnord").equals(queryDelExoccByEx.get(j).get("pkCnord"))){
						Map<String,Object> sendMap=new HashMap<String,Object>();
						sendMap.putAll(queryDelExoccByEx.get(j));
						sendMap.putAll(map);
						sDMsgSendIp.sendExConfirmMsg(sendMap,typeStatus);
					}
				}
			}
		}
	}
}
