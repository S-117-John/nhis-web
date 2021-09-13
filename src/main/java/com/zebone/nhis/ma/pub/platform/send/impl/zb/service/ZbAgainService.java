package com.zebone.nhis.ma.pub.platform.send.impl.zb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;

import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.zb.dao.CnSendMapper;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendBl;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendCn;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * Hl7重新推送
 *
 */
@Service
public class ZbAgainService {
	 @Resource
	 private MsgSendBl msgSendBl;
	 @Resource
	 private MsgSendCn msgSendCn;
	 @Resource
	 private CnSendMapper cnSendMapper;
	   
	 /*
		 * 门诊 医技申请Hl7消息重推
		 * @param triggerEvent
		 * @param paramMap 
		 * @throws HL7Exception
		 */
		public void sendCnMed(String param,IUser u){
			List<Map<String,Object>> paramList = (List<Map<String,Object>>)JsonUtil.readValue(param, List.class);
			String pkPv = MsgUtils.getPropValueStr(paramList.get(0), "pkPv");
			//查询版本1
			 /*
			 String pkCnords=""; 
			   if(paramList.size()>0){
				   for (int i = 0; i < paramList.size(); i++) {
					   if(i==paramList.size()-1){
						   pkCnords+="'"+MsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"'";
					   }else{
						   pkCnords+="'"+MsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"',"; 
					   }
				   }   
			   }
			   StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,");
			    sql.append("co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.code_freq,co.flag_emer,");
			    sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd.code_emp,co.name_emp_ord FROM cn_order co ");
			    sql.append("LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi LEFT JOIN BD_OU_EMPLOYEE bd on BD.pk_emp=co.pk_emp_ord where 1=1 ");
			    //sql.append(" and co.code_ordtype='0206' and co.date_sign > to_date(20200422000000,'YYYYMMDDHH24MISS')");
                sql.append("and co.pk_cnord  in ("+pkCnords+")");
			   List<Map<String,Object>> mapList=DataBaseHelper.queryForList(sql.toString());*/
			   
			  //查询版本2
				   List<String> pkCnords=new ArrayList<>(); 
				   if(paramList.size()>0){
					   for (int i = 0; i < paramList.size(); i++) {
						   String ce=String.valueOf(MsgUtils.getPropValueStr(paramList.get(i), "pkCnord"));
						   pkCnords.add(ce);
					   }   
				   }
			   Map<String, List<String>> paramMap=new HashMap<>();
			   paramMap.put("pkCnords", pkCnords);
			   List<Map<String,Object>> mapList=cnSendMapper.qryLisOrderRLInfo(paramMap);
			   List<Map<String,Object>> mapLists=new ArrayList<>();
			   mapLists.addAll(mapList);
			   
			   Map<String, Object> mapRis = new HashMap<>();
			   mapRis.put("pkPv", mapList.get(0).get("pkPv"));
			   mapRis.put("type","ris");
			   List<Map<String, Object>> listMap =cnSendMapper.qryRisInfo(mapRis);
			   mapRis.clear();
			   
			   try {
			   for (Map<String, Object> map : mapList) {
				   if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
					   //遍历判断所需检查进行推送申请单
					   for(Map<String, Object> risMap:listMap){
						   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
							   risMap.put("control", "NW");
							   risMap.put("fenlei", "ris");
								msgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(),risMap,"out");
							}
					   }
				}
				if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
					//ORC-1申请控制NW:新增申请
					map.put("control", "NW");
					msgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), map, "out");//门诊检验申请
				}
			   } 
			   
			   //延迟1分钟
			   Thread.sleep(10000);
			   
			   for (Map<String, Object> map : mapLists) {
				   map.put("control","OK");
					if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
							map.put("fenlei", "ris");
							msgSendBl.LbsendORLMsg("O20", map,"out");
					}
					if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
						msgSendBl.LbsendORLMsg("O22", map,"out");
						
					}
				}   
				} catch (Exception e) {
				}
		}
		
		/*
		 * 门诊医技记费Hl7消息重推
		 * @param triggerEvent
		 * @param paramMap 
		 * @throws HL7Exception
		 */
		public void sendBlMedApply(String param,IUser u)throws HL7Exception{
			List<Map<String,Object>> paramList = (List<Map<String,Object>>)JsonUtil.readValue(param, List.class);
			String pkPv = MsgUtils.getPropValueStr(paramList.get(0), "pkPv");
			List<String> pkCnords=new ArrayList<>(); 
			   if(paramList.size()>0){
				   for (int i = 0; i < paramList.size(); i++) {
					   String ce=String.valueOf(MsgUtils.getPropValueStr(paramList.get(i), "pkCnord"));
					   pkCnords.add(ce);
				   }   
			   }
			   
			 Map<String, List<String>> paramMap=new HashMap<>();
			 paramMap.put("pkCnords", pkCnords);
			 List<Map<String,Object>> mapList=cnSendMapper.qryLisOrderRLInfo(paramMap);
			 for (Map<String, Object> map : mapList) {
				map.put("control","OK");
				if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
					map.put("fenlei", "ris");
					msgSendBl.LbsendORLMsg("O20", map,"out");
				}
				if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
					msgSendBl.LbsendORLMsg("O22", map,"out");
				}
			}
		}
		
		/*
		 *  发送Hl7消息重推
		 * @param triggerEvent
		 * @param paramMap 
		 * @throws HL7Exception
		 */
		public void sendExOrderCheckMsg(String param,IUser u){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			List<Map<String,Object>> paramList = JsonUtil.readValue(param, List.class);
			paramMap.put("addingList", paramList);
			msgSendCn.sendOMPMsg("O09",paramMap);
		}
}
