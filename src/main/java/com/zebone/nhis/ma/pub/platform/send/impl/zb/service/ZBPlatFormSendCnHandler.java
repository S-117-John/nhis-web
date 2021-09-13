package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.ma.pub.platform.send.impl.zb.dao.CnSendMapper;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendCn;
import com.zebone.nhis.ma.pub.platform.zb.utils.MapUtils;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
/**
 * 发送CN领域消息
 * @author yangxue
 *
 */
@Service
public class ZBPlatFormSendCnHandler {
	@Resource
	private MsgSendCn msgSendCn;
	@Resource
	private CnSendMapper cnSendMapper;
   /**
    * 发送临床诊断信息
    * @param paramMap
    */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendCnDiagMsg(Map<String,Object> paramMap){
		//LB-住院，门诊-诊断传递数据key不一样。
		if(null != paramMap.get("pvDiagList") && !("").equals(paramMap.get("pvDiagList"))){
			//门诊
			msgSendCn.sendADT_A31("A31", (List<Map<String,Object>>)paramMap.get("pvDiagList"), "out", MsgUtils.getPropValueStr(paramMap, "pkPv"));
		}else if(null != paramMap.get("source") && !("").equals(paramMap.get("source"))){
	        //住院
			msgSendCn.sendADT_A31("A31", (List<Map<String,Object>>)paramMap.get("source"), null, MsgUtils.getPropValueStr(paramMap, "pkPv"));
		}
	}
	/**
	 * 发送临床医技申请单信息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendCnMedApply(Map<String,Object> paramMap) throws HL7Exception{
			List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> saveList = new ArrayList<>();
			if(null !=paramMap){
			String type  = paramMap.get("type").toString();
			String pkPv  = MsgUtils.getPropValueStr(paramMap, "pkPv");
			Map<String, Object> codeMap = null;
			//检验
			if (type == "lis") {
				listMap = cnSendMapper.qryLisInfo(paramMap);
				saveList = (List<Map<String, Object>>)paramMap.get("lisList");
				for (int i = 0; i < listMap.size(); i++) {
				    for (int j = 0; j < saveList.size(); j++) {
				    	codeMap = (Map<String, Object>)MapUtils.beanToMap(saveList.get(j)); 	
						if((listMap.get(i).get("codeApply").toString()).equals(codeMap.get("codeApply"))){
							listMap.get(i).put("Control", MsgUtils.getPropValueStr(paramMap, "Control"));
							msgSendCn.sendOmlMsg("O21", pkPv, listMap.get(i),MsgUtils.getPropValueStr(paramMap, "state"));
						}
						codeMap = null;
					}
				}
			}else if(("RisLis").equals(type)){
				List<String> pkCnords=new ArrayList<>();
				List resList = (List)paramMap.get("lisList");
				saveList = MapUtils.lisBToLisMap(resList);
				if(saveList.size()>0){
					   for (int i = 0; i < saveList.size(); i++) {
						   pkCnords.add(String.valueOf(MsgUtils.getPropValueStr(saveList.get(i), "pkCnord")));
					   }   
				   }
				   
				   Map<String, List<String>> parMap=new HashMap<>();
				   parMap.put("pkCnords", pkCnords);
				   List<Map<String,Object>> mapList=cnSendMapper.qryLisOrderRLInfo(parMap);
					
				   Map<String, Object> mapRis = new HashMap<>();
				   mapRis.put("pkPv", mapList.get(0).get("pkPv"));
				   mapRis.put("type","ris");
				   List<Map<String, Object>> RistMap =cnSendMapper.qryRisInfo(mapRis);
				   
				   for (Map<String, Object> map : mapList) {
					   if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
						   //遍历判断所需检查进行推送申请单
						   for(Map<String, Object> risMap:RistMap){
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
						msgSendCn.sendOmlMsg("O21", map.get("pkPv").toString(), map, "out");//门诊检验申请
					}
				   }
			}else{	//检查
				listMap =cnSendMapper.qryRisInfo(paramMap);
				saveList = (List<Map<String, Object>>)paramMap.get("risList");
				for (int i = 0; i < listMap.size(); i++) {
				    for (int j = 0; j < saveList.size(); j++) {
				    	codeMap=(Map<String, Object>)MapUtils.beanToMap(saveList.get(j));
				    	String ordsn = codeMap.get("ordsn").toString();
						if((ordsn).equals(listMap.get(i).get("ordsn").toString())){
							listMap.get(i).put("control", MsgUtils.getPropValueStr(paramMap, "Control"));
							listMap.get(i).put("fenlei", "ris");
							msgSendCn.sendOmlMsg("O19", pkPv, listMap.get(i),MsgUtils.getPropValueStr(paramMap, "state"));
						}	
						codeMap = null;
					}
				}
			}
			saveList=null;
			listMap=null;
			}
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void sendCnPresOpMsg(Map<String,Object> paramMap) throws HL7Exception{
		//msgSendCn.sendOMPMsg("O09", paramMap);
	}
}
