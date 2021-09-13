package com.zebone.nhis.ma.pub.platform.sd.send;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreate;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgResendMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

/**
 * 未生成消息重新生成以及重发
 * @author JesusM
 *
 */
@Service
public class SDMsgResend {
	
	@Resource
	private MsgCreate msgCreate;
	@Resource
	private	SDMsgResendMapper sDMsgResendMapper;
	@Resource
	private	SDQueryUtils sDQueryUtils;
	
	/**
	 * 未生成消息重发（O09，O22，O21,O02）
	 * @param paramMap (医嘱号集合)
	 */
	public String msgResend(Map<String,Object> paramMap) {
		String msgId = SDMsgUtils.getMsgId();
		String message = "";
		//2.确认需要发送的消息类型 
		String messageType = SDMsgUtils.getPropValueStr(paramMap, "messageType");
		try{
			switch (messageType){
				case "ADT^A01":break;
				case "OMP^O09":message = reSendOMPO09(msgId,paramMap);break;//医嘱
				case "OMG^O19":message = reSendOMGO19(msgId,paramMap);break;//检查申请
				case "ORG^O20":message = reSendORGO20(msgId,paramMap);break;//检查确认
				case "OML^O21":message = reSendOMLO21(msgId,paramMap);break;//检验申请
				case "ORL^O22":message = reSendORLO22(msgId,paramMap);break;//检验确认
				case "ORM^O01":message = reSendORMO01(msgId,paramMap);break;//手术申请
				case "ORR^O02":message = reSendORRO02(msgId,paramMap);break;//手术确认
				case "RGV^O15":message = reSendRGVO15(msgId,paramMap);break;//
				case "RAS^O17":message = reSendRASO17(msgId,paramMap);break;//
				default:message = "消息类型错误！";break;
			}
		} catch(Exception e){
			message = "生成消息失败！"+e.getMessage();
		}
		return message;
		
	}
	
	/**
	 * 查询未发出消息
	 * @param paramMap
	 * @return
	 */
	public List<SysMsgRec> sendSaveMsg(Map<String,Object> paramMap){
		List<SysMsgRec> messageList = sDMsgResendMapper.queryMsgList(paramMap);
		return messageList;
	}
	
	
	
	/**
	 * 
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendRASO17(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendRGVO15(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 手术确认
	 * @param paramMap  申请单号 codeApply 申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendORRO02(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		try {
			//查询手术信息
			List<Map<String, Object>> opList = sDMsgResendMapper.queryOperationByOrdsn(paramMap);
			if(opList != null && opList.size()>0){
				paramMap.putAll(opList.get(0));
			}
			//查询患者信息
			List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
			if(patiList != null && patiList.size()>0){
				paramMap.put("patmap", patiList.get(0));
			}
			//组装消息
			message = msgCreate.createORR_O02Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 手术申请
	 * @param paramMap 申请单号 codeApply 申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendORMO01(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		try {
			//查询手术信息
			List<Map<String, Object>> opList = sDMsgResendMapper.queryOperationByOrdsn(paramMap);
			if(opList != null && opList.size()>0){
				paramMap.putAll(opList.get(0));
			}
			//查询患者信息
			List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
			if(patiList != null && patiList.size()>0){
				paramMap.put("map", patiList.get(0));
			}
			//组装消息
			message = msgCreate.createORM_O01Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 检验确认 
	 * @param paramMap 申请单号 codeApply 申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendORLO22(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		try {
			//查询检验信息
			List<Map<String, Object>> lisList = sDMsgResendMapper.queryLisByOrdsn(paramMap);
			if(lisList != null && lisList.size()>0){
				paramMap.putAll(lisList.get(0));
			}
			//查询患者信息
			List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
			if(patiList != null && patiList.size()>0){
				paramMap.putAll(patiList.get(0));
			}
			//组装消息
			message = msgCreate.createORL_O22Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 检验申请
	 * @param paramMap 申请单号 codeApply  申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendOMLO21(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		//查询检验信息
		List<Map<String, Object>> lisList = sDMsgResendMapper.queryLisByOrdsn(paramMap);
		if(lisList != null && lisList.size()>0){
			paramMap.putAll(lisList.get(0));
		}
		//查询患者信息
		List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
		if(patiList != null && patiList.size()>0){
			paramMap.putAll(patiList.get(0));
		}
		try {
			//组装消息
			message = msgCreate.createOML_O21Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 检查确认
	 * @param paramMap 申请单号 codeApply 申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendORGO20(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		//查询检验信息
		List<Map<String, Object>> risList = sDMsgResendMapper.queryRisByOrdsn(paramMap);
		if(risList != null && risList.size()>0){
			paramMap.putAll(risList.get(0));
		}
		//查询患者信息
		List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
		if(patiList != null && patiList.size()>0){
			paramMap.putAll(patiList.get(0));
		}
		try {
			//组装消息
			message = msgCreate.createORG_O20Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}
	/**
	 * 检查申请
	 * @param paramMap 申请单号 codeApply 申请状态control，
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendOMGO19(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		Message message = null;
		//验证参数
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			return "参数输入有误！";
		}
		//查询检验信息
		List<Map<String, Object>> risList = sDMsgResendMapper.queryRisByOrdsn(paramMap);
		if(risList != null && risList.size()>0){
			paramMap.putAll(risList.get(0));
		}
		//查询患者信息
		List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
		if(patiList != null && patiList.size()>0){
			paramMap.putAll(patiList.get(0));
		}
		try {
			//组装消息
			message = msgCreate.createOMG_O19Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}

	
	/**
	 * 医嘱消息
	 * @param 医嘱号ordsn，申请状态control，医嘱状态,ordStatus
	 * @return
	 * @throws HL7Exception 
	 */
	private String reSendOMPO09(String msgId,Map<String,Object> paramMap) throws HL7Exception {
		Message message = null;
		//判断参数是否够
		if("".equals(SDMsgUtils.getPropValueStr(paramMap, "ordsn")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "control")) || "".equals(SDMsgUtils.getPropValueStr(paramMap, "ordStatus"))){
			return "参数输入有误！";
		}
		try {
			//查询医嘱信息
			System.out.println("sadsd");
			List<Map<String, Object>> orderList = sDMsgResendMapper.queryOrderByOrdsn(paramMap);
			System.out.println("sadsd");
			if(orderList!=null && orderList.size()>0){
				paramMap.put("pkPv", SDMsgUtils.getPropValueStr(orderList.get(0), "pkPv"));
				paramMap.put("pkPi", SDMsgUtils.getPropValueStr(orderList.get(0), "pkPi"));
				paramMap.put("orderList", orderList);
			}
			//查询患者信息
			List<Map<String, Object>> patiList = sDMsgResendMapper.queryPatient(paramMap);
			if(patiList!=null && patiList.size()>0){
				paramMap.putAll(patiList.get(0));
			}
			//组装消息
			message = msgCreate.createOMP_O09Msg(msgId, paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SDMsgUtils.getParser().encode(message);
	}

}
