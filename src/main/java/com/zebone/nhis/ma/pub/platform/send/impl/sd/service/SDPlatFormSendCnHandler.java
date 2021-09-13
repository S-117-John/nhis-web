package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendCn;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendIp;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendOpCn;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.sd.dao.SDCnSendMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 发送CN领域消息
 * @author yangxue
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendCnHandler {
	@Resource
	private SDMsgSendCn sDMsgSendCn;
	@Resource
	private SDCnSendMapper sDCnSendMapper;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private SDMsgSendIp sDMsgSendIp;
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDMsgSendOpCn sDMsgSendOpCn;
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

   /**
    * 发送临床诊断信息
    * @param paramMap
 * @throws InterruptedException
    */
	public void sendCnDiagMsg(Map<String,Object> paramMap) throws InterruptedException{
		//新方法
		sDMsgSendCn.sendADT_A31("A31", paramMap);
	}
	/**
	 * 发送临床医技申请单信息
	 * @param paramMap
	 * @throws HL7Exception
	 */

	public void sendCnMedApply(Map<String,Object> paramMap) throws HL7Exception{
		//新增消息
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> saveList = new ArrayList<>();
		String type  = SDMsgUtils.getPropValueStr(paramMap, "type");
		String pkPv  = SDMsgUtils.getPropValueStr(paramMap, "pkPv");
		Map<String, Object> codeMap = new HashMap<String,Object>();
		//检验
		if ("lis".equals(type)) {
			if("CA".equals(SDMsgUtils.getPropValueStr(paramMap, "Control"))){
				List<String> pkCnords = (List<String>) paramMap.get("pkCnOrds");
				List<Map<String, Object>> queryRisLisUtil = sDQueryUtils.queryRisLisUtil(pkCnords);
				if(queryRisLisUtil!=null){
					for(Map<String,Object> map : queryRisLisUtil){
						map.put("control", "CA");
						map.put("fenlei", "lis");
						sDMsgSendCn.sendOmlMsg("O21", SDMsgUtils.getPropValueStr(map, "pkPv"), null, map,"lis");
					}
				}
			}else{
				//参数为医嘱对象，获取医嘱对象的医嘱主键
				List<CnOrder> orderList = (List<CnOrder>) paramMap.get("lisList");
				List<String> pkCnords = new ArrayList<>();
				for(CnOrder order : orderList){
					pkCnords.add(order.getPkCnord());
				}
				//查询对应医嘱主键的医嘱信息
				List<Map<String, Object>> queryRisLisUtil = sDQueryUtils.queryRisLisUtil(pkCnords);
				for(Map<String,Object> map : queryRisLisUtil){
					map.put("control", SDMsgUtils.getPropValueStr(paramMap, "Control"));
					map.put("fenlei", "lis");
					sDMsgSendCn.sendOmlMsg("O21", pkPv, null, map,SDMsgUtils.getPropValueStr(paramMap, "state"));
				}
			}
		}else if(("RisLis").equals(type)){
			//门诊医生签署，以及住院医生站签署
			String pkCnords="";
			List<Object> resList = (List<Object>)paramMap.get("lisList");
			saveList = SDMsgUtils.lisBToLisMap(resList);
			if(saveList.size()>0){
			   for (int i = 0; i < saveList.size(); i++) {
				   if(i==saveList.size()-1){
					  pkCnords+="'"+SDMsgUtils.getPropValueStr(saveList.get(i), "pkCnord")+"'";
				   }else{
					  pkCnords+="'"+SDMsgUtils.getPropValueStr(saveList.get(i), "pkCnord")+"',";
				   }
			   }
		   }
		   StringBuilder sql = new StringBuilder("SELECT distinct co.pk_cnord,co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,");
		   sql.append("co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.code_freq,co.flag_emer,");
		   sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st FROM cn_order co ");
		   sql.append("where co.pk_cnord in ("+pkCnords+")");
		   List<Map<String,Object>> mapList = DataBaseHelper.queryForList(sql.toString());
		   for (Map<String, Object> map : mapList) {
			   switch (SDMsgUtils.getPropValueStr(map, "codeOrdtype").substring(0,2)){
				   case "02":{
					   //遍历判断所需检查进行推送申请单
					   map.put("control", paramMap.get("Control"));
					   map.put("type","ris");
					   map.put("fenlei", "ris");
					   sDMsgSendCn.sendOmlMsg("O19", map.get("pkPv").toString(), null, map,"");
				   }break;
				   case "03":{
					   	//ORC-1申请控制NW:新增申请
						map.put("control", paramMap.get("Control"));
						sDMsgSendCn.sendOmlMsg("O21",  map.get("pkPv").toString(), null, map,"");
				   }break;
				   case "04":{
					   List<String> pkCnList = new ArrayList<String>();
					   pkCnList.add(SDMsgUtils.getPropValueStr(map, "pkCnord"));
					   //查询接口参数校验
					   if(pkCnList == null || pkCnList.size()<=0){
						   log.info("sendCnMedApply + 调用查询参数接口参数为空！线程名："+Thread.currentThread().getName());
						   return ;
					   }
					   map.put("pkCnList", pkCnList);
					   //查询患者信息所需数据
					   Map<String,Object> patMap = new HashMap<String,Object>();
					   patMap.put("pkPv", SDMsgUtils.getPropValueStr(map, "pkPv"));
					   List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(patMap);
						if(queryPatList!=null && queryPatList.size() > 0 ){
							patMap = queryPatList.get(0);
						}
					   //手术申请O01
					   List<Map<String, Object>> queryOp = sDMsgMapper.queryOperation(map);
					   for(Map<String,Object> m : queryOp){
							m.put("control", paramMap.get("Control"));
							m.put("map", patMap);
							sDMsgSendIp.sendOpApplyMsg(m);
						}
				   }break;
				   default:log.info(Thread.currentThread().getName()+"医嘱类型错误codeOrdtype:"+SDMsgUtils.getPropValueStr(map, "codeOrdtype"));
			   }
		   }
		}else{	//检查Ris
			if("CA".equals(SDMsgUtils.getPropValueStr(paramMap, "Control"))){
				List<String> pkCnords = (List<String>) paramMap.get("pkCnOrds");
				List<Map<String, Object>> queryRisLisUtil = sDQueryUtils.queryRisLisUtil(pkCnords);
				if(queryRisLisUtil != null){
					for(Map<String,Object> map : queryRisLisUtil){
						map.put("control", "CA");
						map.put("fenlei", SDMsgUtils.getPropValueStr(paramMap, "type"));
						sDMsgSendCn.sendOmlMsg("O19", SDMsgUtils.getPropValueStr(map, "pkPv"), null, map,"ris");
					}
				}else{
					log.info(Thread.currentThread().getName()+"检查取消申请消息，参数数据为空！");
				}
			}else{
				listMap = sDCnSendMapper.qryRisInfo(paramMap);
				saveList = (List<Map<String, Object>>)paramMap.get("risList");
				for (int i = 0; i < listMap.size(); i++) {
				    for (int j = 0; j < saveList.size(); j++) {
				    	codeMap=(Map<String, Object>)SDMsgUtils.beanToMap(saveList.get(j));
				    	String ordsn = codeMap.get("ordsn").toString();
						if((ordsn).equals(listMap.get(i).get("ordsn").toString())){
							listMap.get(i).put("control", SDMsgUtils.getPropValueStr(paramMap, "Control"));
							listMap.get(i).put("fenlei", "ris");
							sDMsgSendCn.sendOmlMsg("O19", pkPv, null, listMap.get(i),SDMsgUtils.getPropValueStr(paramMap, "state"));
						}
					}
				}
			}
		}
	}



	/**
	 * 门诊处方
	 * 根据医嘱号，查询医嘱信息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOpO09Msg(Map<String,Object> paramMap) throws HL7Exception{
		//处方医嘱
		if("prescription".equals(SDMsgUtils.getPropValueStr(paramMap, "OMPtype"))){
		  List<Map<String,Object>> addMapList = null ;

		  //新增处方 集合
		  String addPkCnords="";
		  List<CnOrdHerb> addHerbItemList = (List<CnOrdHerb>)paramMap.get("addHerbItemList");
		  
		  //修改处方集合
		  String upPkCnords="";
		  List<Map<String,Object>> upMapList = null ;
		  List<CnOrdHerb> updateHerbItemList = (List<CnOrdHerb>)paramMap.get("updateHerbItemList");
		  
		  //新增
		  if(addHerbItemList!=null && addHerbItemList.size()>0 && (updateHerbItemList==null || updateHerbItemList.size()<=0)){
			   String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,co.spec, "
					    +" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
					    +" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input,co.note_ord FROM cn_order co "
					    +" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
					    + "and co.pk_cnord  in ( " ;
			   int pkCnOrdSize = addHerbItemList.size();
			   for (int i = 0; i < pkCnOrdSize; i++) {
				   if(i==pkCnOrdSize-1){
					   if(StringUtils.isNotBlank(addHerbItemList.get(i).getPkCnord())){
						   addPkCnords+="'"+addHerbItemList.get(i).getPkCnord()+"')";
					   }
				   }else{
					   if(StringUtils.isNotBlank(addHerbItemList.get(i).getPkCnord())){
						   addPkCnords+="'"+addHerbItemList.get(i).getPkCnord()+"',";
					   }
				   }
			   }
			    addMapList=DataBaseHelper.queryForList(sql+addPkCnords);
				paramMap.put("addingList", addMapList);
				paramMap.put("control", "NW");
				sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
		  }

		  //修改
		  if(updateHerbItemList!=null&&updateHerbItemList.size()>0){
			   String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,co.spec, "
					    +" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
					    +" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input,co.note_ord FROM cn_order co "
					    +" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
					    + "and co.pk_cnord  in ( " ;
			   int pkCnOrdSize = updateHerbItemList.size();
			   for (int i = 0; i < pkCnOrdSize; i++) {
				   if(i==pkCnOrdSize-1){

					   if(StringUtils.isNotBlank(updateHerbItemList.get(i).getPkCnord())){
						   upPkCnords+="'"+updateHerbItemList.get(i).getPkCnord()+"')";
					   }
				   }else{
					   if(StringUtils.isNotBlank(updateHerbItemList.get(i).getPkCnord())){
						   upPkCnords+="'"+updateHerbItemList.get(i).getPkCnord()+"',";
					   }
				   }
			   }
			   upMapList=DataBaseHelper.queryForList(sql+upPkCnords);
				paramMap.put("addingList", upMapList);
				paramMap.put("control", "RU");
				sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
		  }


		  //删除处方
		  List<Map<String,Object>> delMapList =(List<Map<String,Object>> )paramMap.get("delMapList");
		  if(delMapList!=null&&delMapList.size()>0){
				paramMap.put("addingList", delMapList);
				paramMap.put("control", "CA");
				sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
		  }

		  //西药成药
		}else if ("WesternMedicine".equals(SDMsgUtils.getPropValueStr(paramMap, "OMPtype"))) {
			  List<CnOrder> addCnOrdList = (List<CnOrder>)paramMap.get("addCnOrdList");
			  List<CnOrder> editCnOrdList = (List<CnOrder>)paramMap.get("editCnOrdList");

			  //新增西药成药处方
			  List<Map<String,Object>> addMapList = null ;
			  String addPkCnords="";
			  if(addCnOrdList!=null&&addCnOrdList.size()>0){
				   String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,co.spec, "
						    +" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
						    +" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input,co.note_ord FROM cn_order co "
						    +" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
						    + "and co.pk_cnord  in ( " ;
				   int pkCnOrdSize = addCnOrdList.size();
				   for (int i = 0; i < pkCnOrdSize; i++) {
					   if(i==pkCnOrdSize-1){
						   if(StringUtils.isNotBlank(addCnOrdList.get(i).getPkCnord())){
							   addPkCnords+="'"+addCnOrdList.get(i).getPkCnord()+"')";
						   }
					   }else{
						   if(StringUtils.isNotBlank(addCnOrdList.get(i).getPkCnord())){
							   addPkCnords+="'"+addCnOrdList.get(i).getPkCnord()+"',";
						   }
					   }
				   }
				    addMapList=DataBaseHelper.queryForList(sql+addPkCnords);
					paramMap.put("addingList", addMapList);
					paramMap.put("control", "NW");
					sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
			  }

			  //修改西药成药处方
			  List<Map<String,Object>> upMapList = null ;
			  String upPkCnords="";
			  if(editCnOrdList!=null&&editCnOrdList.size()>0){
				   String sql="SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.price_cg,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.code_ord,co.eu_Always,co.spec, "
						    +" dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer, "
						    +" co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input,co.note_ord FROM cn_order co "
						    +" LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 "
						    + "and co.pk_cnord  in ( " ;
				   int pkCnOrdSize = editCnOrdList.size();
				   for (int i = 0; i < pkCnOrdSize; i++) {
					   if(i==pkCnOrdSize-1){
						   if(StringUtils.isNotBlank(editCnOrdList.get(i).getPkCnord())){
							   upPkCnords+="'"+editCnOrdList.get(i).getPkCnord()+"')";
						   }
					   }else{
						   if(StringUtils.isNotBlank(editCnOrdList.get(i).getPkCnord())){
							   upPkCnords+="'"+editCnOrdList.get(i).getPkCnord()+"',";
						   }
					   }
				   }
				    upMapList=DataBaseHelper.queryForList(sql+upPkCnords);
					paramMap.put("addingList", upMapList);
					paramMap.put("control", "RU");
					sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
			  }

			  List<Map<String,Object>> delMapList = (List<Map<String,Object>>)paramMap.get("delMapList");
			//删除西药成药处方
			  if(delMapList!=null&&delMapList.size()>0){
					paramMap.put("addingList", delMapList);
					paramMap.put("control", "CA");
					sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
			  }
		}
	}



	/**
	 * 门诊处方
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendCnPresOpMsg(Map<String,Object> paramMap){
		sDMsgSendOpCn.sendOMPMsg("O09", paramMap);
	}

}
