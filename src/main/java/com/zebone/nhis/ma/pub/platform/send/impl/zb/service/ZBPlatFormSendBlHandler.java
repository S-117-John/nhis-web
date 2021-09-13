package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;


import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.ma.pub.platform.send.impl.zb.dao.CnSendMapper;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendBl;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendCn;
import com.zebone.nhis.ma.pub.platform.zb.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 发送BL领域消息
 * @author yangxue
 *
 */
@Service
public class ZBPlatFormSendBlHandler {
   @Resource
   private MsgSendBl msgSendBl;
   @Resource
   private MsgSendCn msgSendCn;
   @Resource
	private CnSendMapper cnSendMapper;
   
   /**
    * 预交金消息发送
    * @param paramMap
 * @throws HL7Exception 
    */
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void sendDepositMsg(Map<String,Object> paramMap) throws HL7Exception{
			msgSendBl.sendBlMsgs("P03", paramMap);
   }
   
   
   /**
    * 发送取消结算信息
    * @param paramMap
 * @throws HL7Exception 
    */
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void sendBlCancelSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
			paramMap.put("doCode",paramMap.get("codeEmp"));
			paramMap.put("doName",paramMap.get("nameEmp"));
			msgSendBl.sendBlMsgs("A13", paramMap);
   }
   /**
    * 发送住院结算信息
    * @param paramMap{"doCode":"操作员编码","doName":"操作员名称","pkPi":"患者编码","pkPv":"就诊编码","totalAmount":"结算总金额","selfAmount":"自费金额"}
 * @throws HL7Exception 
    */
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void sendBlSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
		paramMap.put("doCode", paramMap.get("codeEmp"));
		paramMap.put("doName", paramMap.get("nameEmp"));
		Map<String,Object> paramMapS = new HashMap<String, Object>();
		paramMapS.putAll(paramMap);
		msgSendBl.sendBlMsgs("A03", paramMapS);//发送结算信息
		msgSendBl.sendBlMsgs("ZH1", paramMap);//发送病案首页信息
   }
 
   
   public void sendBlOpSettleMsg(Map<String,Object> paramMap) throws Exception{
	   String Control =  MsgUtils.getPropValueStr(paramMap,"Control");
	   
	   @SuppressWarnings("unchecked")
	List<Map<String,Object>> opDtList = MapUtils.lisBToLisMap((List<Object>)paramMap.get("blOpDts"));
	   
	   List<String> pkCnords=new ArrayList<>();
	   List<String> pkCgops=new ArrayList<>(); 
	   if(opDtList.size()>0){
		   for (int i = 0; i < opDtList.size(); i++) {
				   if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(opDtList.get(i), "pkCgop"))){
					   pkCgops.add(String.valueOf(MsgUtils.getPropValueStr(opDtList.get(i), "pkCgop")));
				   }else if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(opDtList.get(i), "pkCnord"))){
					   pkCnords.add(String.valueOf(MsgUtils.getPropValueStr(opDtList.get(i), "pkCnord")));
				   }
		   }   
	   }
	   if(!("").equals(pkCnords) ||!("").equals(pkCgops)){

	   Map<String, List<String>> parMap=new HashMap<>();
	   if(pkCnords.size()>0){
		   parMap.put("pkCnords", pkCnords);  
	   }
	   if(pkCgops.size()>0){
		   parMap.put("pkCgops", pkCgops);
	   }
	   if(!parMap.isEmpty()){
	   List<Map<String,Object>> mapList=cnSendMapper.qryLisOrderRLInfo(parMap);
	   Map<String, Object> mapRis = new HashMap<>();
	   mapRis.put("pkPv", mapList.get(0).get("pkPv"));
	   mapRis.put("type","ris");
	   List<Map<String, Object>> listMap =cnSendMapper.qryRisInfo(mapRis);
	   mapRis.clear();
	   if(Control.equals("OK")){
		   for (Map<String, Object> map : mapList) {
			   if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				   //遍历判断所需检查进行推送申请单
				   for(Map<String, Object> risMap:listMap){
					   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
						   risMap.put("control", "NW");
						   risMap.put("fenlei", "ris");
							msgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(), risMap,"out");
						}
				   }
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				//ORC-1申请控制NW:新增申请
				map.put("control", "NW");
				msgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), map, "out");//门诊检验申请
			}
		   } 
		   Map<String,Object> cnOrdListMap = new HashMap<>();
		   cnOrdListMap.put("addingList", mapList);
		   msgSendCn.sendOMPMsg("O09", cnOrdListMap);
		   //延迟1分钟执行下步操作
		   Thread.sleep(10000);
	   }
	   for (Map<String, Object> map : mapList) {
		   map.put("control",Control);
			if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				for(Map<String, Object> risMap:listMap){
					   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
						   map.put("fenlei", "ris");
							msgSendBl.LbsendORLMsg("O20", map,"out");
						}
				   }
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				msgSendBl.LbsendORLMsg("O22", map,"out");
			}
		}    
	   if(Control.equals("CR")){
		 //延迟1分钟执行下步操作
		   Thread.sleep(10000);
		   for (Map<String, Object> map : mapList) {
			   if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				   //遍历判断所需检查进行推送申请单
				   for(Map<String, Object> risMap:listMap){
					   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
						   risMap.put("control", "OC");
						   risMap.put("fenlei", "ris");
							msgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(),risMap,"out");
						}
				   }
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				//ORC-1申请控制NW:新增申请
				map.put("control", "OC");
				msgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), map, "out");//门诊检验申请
			}
		   } 
		   Map<String,Object> cnOrdListMap = new HashMap<>();
		   cnOrdListMap.put("addingList", mapList);
		   msgSendCn.sendOMPMsg("O09", cnOrdListMap);
		   cnOrdListMap.clear();
	   }
			String stringBean = JsonUtil.writeValueAsString(paramMap.get("invoiceInfo"));
			List<Map<String, Object>> invoices = (List<Map<String, Object>>)JsonUtil.readValue(stringBean, List.class);
			if(null != invoices){
			for (int i = 0; i < opDtList.size(); i++) {
				for (int j = 0; j < invoices.size(); j++) {
					List<Map<String,Object>> blinDtsLis = (List<Map<String,Object>>)invoices.get(j).get("blInDts");
					if (blinDtsLis.get(0).get("codeBill").equals(opDtList.get(i).get("codeBill"))) {
						opDtList.get(i).put("codeInv", invoices.get(j).get("codeInv"));
						break;
					}
				}
			}
			}else if(null != paramMap.get("inVoiceNo") && !("").equals(paramMap.get("inVoiceNo"))){
				for (int i = 0; i < opDtList.size(); i++) {
					opDtList.get(i).put("codeInv", paramMap.get("inVoiceNo"));
			}
			}
			msgSendBl.sendORPMsg("O10", opDtList, MsgUtils.getPropValueStr(paramMap,"Control"));	
			opDtList.clear();
			mapList.clear();
	   }	
	   }
   }
   /**
    * 发送取消结算信息
    * @param paramMap
    * @throws HL7Exception 
    */
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void sendBlOpCancelSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
			List<Map<String, Object>> opDtList = (List<Map<String, Object>>)paramMap.get("blOpDts");
			msgSendBl.sendORPMsg("O10", opDtList, "CR");
   }
   
   
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void sendBlMedApplyMsg(Map<String,Object> paramMap) throws HL7Exception{
	   List<Map<String, Object>> paramList= (List<Map<String, Object>>)paramMap.get("dtlist");	
	   List<String> pkCnords=new ArrayList<>();
	   if( null != paramMap.get("pkCnordS") && !("").equals(paramMap.get("pkCnordS"))){
		   String recip[] = paramMap.get("pkCnordS").toString().split("'");
			for (String recipeid : recip) {
				 pkCnords.add(String.valueOf(recipeid));
			}
	   }else{
		   for (int i = 0; i < paramList.size(); i++) {
			   Map<String, Object>  exOrder = new HashMap<>();

				   if(null != paramList.get(i).get("pkCnord")){
					   pkCnords.add(String.valueOf(paramList.get(i).get("pkCnord")));
				   }else if(null != paramList.get(i).get("exOrderOcc")){
					   exOrder.putAll((Map<String, Object>)paramList.get(i).get("exOrderOcc"));
					   pkCnords.add(String.valueOf(MsgUtils.getPropValueStr(exOrder,"pkCnord")));
				   }
		   } 
	    }
	   Map<String, List<String>> parMap=new HashMap<>();
	   if(pkCnords.size()>0){
		   parMap.put("pkCnords", pkCnords); 
	   }
	   if(!parMap.isEmpty()){
		   List<Map<String,Object>> mapLists=cnSendMapper.qryLisOrderIpInfo(parMap);
		   for (Map<String, Object> maps : mapLists){
			 if("03".equals(maps.get("codeOrdtype").toString().substring(0, 2))){
				maps.put("control", MsgUtils.getPropValueStr(paramMap,"Control"));
				msgSendBl.LbsendORLMsg("O22", maps,null);
			 }
			 if("02".equals(maps.get("codeOrdtype").toString().substring(0,2))){
				maps.put("control",MsgUtils.getPropValueStr(paramMap,"Control"));
				maps.put("fenlei", "ris");
				msgSendBl.LbsendORLMsg("O20", maps,null);
			 }
		   }
	   }
   }
}
