package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendBl;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendCn;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendOpBl;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendOpCn;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.sd.dao.SDCnSendMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 发送BL领域消息
 * @author yangxue
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendBlHandler {

   @Resource
   private SDMsgSendBl sDMsgSendBl;
   @Resource
   private SDMsgSendOpBl sDMsgSendOpBl;
   @Resource
   private SDMsgSendCn sDMsgSendCn;
   @Resource
   private SDCnSendMapper sDCnSendMapper;
   @Resource
   private SDQueryUtils sDQueryUtils;
   @Resource
   private SDMsgSendOpCn sDMsgSendOpCn;


   private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

   public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");



   /**
    * 发送给微信--住院相关的缴费信息
    * @param paramMap
    * @throws HL7Exception
    */

   public void sendBlWeiXinSQMZQ1Msg(Map<String,Object> paramMap) throws HL7Exception{
		//sDMsgSendBl.sendSQRZQ1Msg("ZQ1", paramMap);
   }

   /**
    * 发送给微信--住院费每日账单详情
    * @param paramMap
    * @throws HL7Exception
    */

   public void sendBlWeiXinQBPZZLMsgDetails(Map<String,Object> paramMap) throws HL7Exception{
		//sDMsgSendBl.sendQBPZZLMsgDetails("ZZL", paramMap);
   }

   /**
    * 发送给微信--住院费每日账单列表
    * @param paramMap
    * @throws HL7Exception
    */

   public void sendBlWeiXinQBPZZLMsgTheme(Map<String,Object> paramMap) throws HL7Exception{
		//sDMsgSendBl.sendQBPZZLMsgTheme("ZZL", paramMap);
   }

   /**
    * 发送取消结算信息
    * @param paramMap
 * @throws HL7Exception
    */

   public void sendBlCancelSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
			paramMap.put("doCode",paramMap.get("codeEmp"));
			paramMap.put("doName",paramMap.get("nameEmp"));
			sDMsgSendBl.sendADTA13Msg("A13", paramMap);
   }
   /**
    * 发送住院结算信息
    * @param paramMap{"doCode":"操作员编码","doName":"操作员名称","pkPi":"患者编码","pkPv":"就诊编码","totalAmount":"结算总金额","selfAmount":"自费金额"}
 * @throws HL7Exception
    */

   public void sendBlSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
	   if("0".equals(SDMsgUtils.getPropValueStr(paramMap, "euSttype"))){
		   paramMap.put("doCode", paramMap.get("codeEmp"));
			paramMap.put("doName", paramMap.get("nameEmp"));
			Map<String,Object> paramMapS = new HashMap<String, Object>();
			paramMapS.putAll(paramMap);
			sDMsgSendBl.sendADTA03Msg("A03", paramMapS);//发送结算信息
			//sDMsgSendBl.sendZMR_ZH1Msg("ZH1", paramMap);//发送病案首页信息
	   }

   }
   /**
    * 门诊收费时，发送检查、检验、医嘱、处方消息
    * @param paramMap
    * @throws Exception
    */
   public void sendBlOpSettleMsg(Map<String,Object> paramMap) throws Exception{
	   String Control =  SDMsgUtils.getPropValueStr(paramMap,"Control");
	   List<Map<String,Object>> opDtList = SDMsgUtils.lisBToLisMap((List<Object>)paramMap.get("blOpDts"));
	   String pkCnords="";
	   String pkCgops="";
	   if(opDtList.size()>0){
		   for (int i = 0; i < opDtList.size(); i++) {
			   if(i==opDtList.size()-1){
				   if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop"))){
					   pkCgops+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop")+"'";
				   }else if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord"))){
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord")+"'";
				   }

			   }else{
				   if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop"))){
					   pkCgops+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCgop")+"',";
				   }else if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord"))){
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(opDtList.get(i), "pkCnord")+"',";
				   }

			   }
		   }
	   }

	   StringBuilder sql=new StringBuilder("SELECT distinct(co.pk_cnord),co.code_supply,co.pk_dept_exec,co.pk_pv,co.code_apply,co.eu_pvtype,co.ordsn,co.desc_ord,co.eu_Always,ord.code code_ord,ord.DT_ORDCATE,");
	    sql.append("dt.PK_SETTLE,co.code_ordtype,co.name_ord,co.pk_pres,co.ordsn_parent,co.dosage,co.pk_unit_dos,co.pk_unit_cg,co.quan_cg,pre.pres_no,co.code_freq,co.flag_emer,");
	    sql.append("co.days,co.date_start,co.pk_dept,co.flag_durg,co.quan,co.ords,co.eu_st,pm.code_pi,bd1.code_emp,co.name_emp_ord,bd2.code_emp code_emp_input,co.name_emp_input, ");
	    sql.append("(select sum(AMOUNT) from BL_OP_DT dt where co.PK_CNORD=dt.PK_CNORD group by co.PK_CNORD) price_cg, ");
	    sql.append("(select dt_medicaltype from BD_OU_DEPT dept where dept.PK_DEPT=co.pk_dept) dt_medicaltype  ");
	    sql.append("FROM cn_order co INNER JOIN pi_master pm ON pm.pk_pi = co.pk_pi INNER JOIN BL_OP_DT dt ON dt.pk_cnord = co.pk_cnord inner join BD_ORD ord on ord.PK_ORD=co.PK_ORD ");
	    sql.append("LEFT JOIN BD_OU_EMPLOYEE bd1 on BD1.pk_emp=co.pk_emp_ord LEFT JOIN BD_OU_EMPLOYEE bd2 on BD2.pk_emp = co.pk_emp_input left join cn_prescription pre on pre.pk_pres = co.pk_pres where 1=1 ");
	   if(!pkCnords.equals("")){
		   sql.append("and co.pk_cnord  in ("+pkCnords+")");
	   }
	   if(!pkCgops.equals("")){
		   sql.append("and dt.pk_cgop  in ("+pkCgops+")");
	   }

	   List<Map<String,Object>> mapList=DataBaseHelper.queryForList(sql.toString());
	   Map<String, Object> mapRis = new HashMap<>();
	   mapRis.put("pkPv", mapList.get(0).get("pkPv"));
	   mapRis.put("type","ris");
	   List<Map<String, Object>> listMap =sDCnSendMapper.qryRisInfo(mapRis);
	   mapRis.clear();
	   if(Control.equals("OK")){
		   //门诊收费发送发票信息
		   sDMsgSendOpBl.sendBlInvioceDict(paramMap);
	   }else if(Control.equals("CR")){
		   //退费消息
		  // paramMap.put("mapList", mapList);
		  // paramMap.put("listMap", listMap);
		  //sendRefundMsg(paramMap);
	   }else{
		   log.info("HL7消息发送失败！门诊收费时无Control值");
		   throw new BusException("HL7消息发送失败！门诊收费时无Control值");
	   }

	   //发送检查检验确认消息
   	   sendOpApplyEnsureMsg(mapList,listMap,Control);
	   //发送处方消息
	   sendOpRecipeMsg(opDtList,paramMap,Control);

   }
   /**
    * 发送取消结算信息
    * @param paramMap
    * @throws HL7Exception
    *
    * 2020年5月25日未见深大启用该方法
    *
    */
	   public void sendBlOpCancelSettleMsg(Map<String,Object> paramMap) throws HL7Exception{
			/*	List<Map<String, Object>> opDtList = (List<Map<String, Object>>)paramMap.get("blOpDts");
				sDMsgSendBl.sendORPMsg("O10", opDtList, "CR");*/
		   try {
			new BusException("有方法调用门诊取消结算");
			} catch (Exception e) {
				// TODO: handle exception
			}
	   }


   /**
    *检验检查消息ORL^O22  ORG^O20
    * @param paramMap
    * @throws HL7Exception
    */

   public void sendBlMedApplyMsg(Map<String,Object> paramMap) throws HL7Exception{
	   List<Map<String, Object>> paramList = (List<Map<String, Object>>)paramMap.get("dtlist");
	   String pkCnords="";
	 //未知触发点位置数据处理
	   if( null != paramMap.get("pkCnordS") && !("").equals(paramMap.get("pkCnordS"))){
		   pkCnords= paramMap.get("pkCnordS").toString();
	   }else if("CR".equals(SDMsgUtils.getPropValueStr(paramMap, "Control"))){
		   List<String> pkCnordList = (List<String>)paramMap.get("pkCnOrds");
		   //科室医生站触发点位置数据处理
		   if(pkCnordList!=null){
			   for(int i=0;i<pkCnordList.size();i++){
				   if(i==pkCnordList.size()-1 ){
					   pkCnords += "'"+pkCnordList.get(i)+"'";
				   }else {
					   pkCnords += "'"+pkCnordList.get(i)+"',";
				   }
			   }
		   }else if(paramList!=null){
			   //手术医嘱作废触发点处理
			   for(int i=0;i<paramList.size();i++){
				   if(i==paramList.size()-1 ){
					   pkCnords += "'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"'";
				   }else {
					   pkCnords += "'"+SDMsgUtils.getPropValueStr(paramList.get(i), "pkCnord")+"',";
				   }
			   }
		   }
	   }else{
		 //未知触发点位置数据处理
		   for (int i = 0; i < paramList.size(); i++) {
			   Map<String, Object>  exOrder = new HashMap<>();
			   if(i==paramList.size()-1){
				   if(null != paramList.get(i).get("pkCnord")){
					   pkCnords+="'"+paramList.get(i).get("pkCnord")+"'";
				   }else if(null != paramList.get(i).get("exOrderOcc")){
					   exOrder.putAll((Map<String, Object>)paramList.get(i).get("exOrderOcc"));
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(exOrder,"pkCnord")+"'";
				   }
			   }else{
				   if(null != paramList.get(i).get("exOrderOcc")){
					   exOrder.putAll((Map<String, Object>)paramList.get(i).get("exOrderOcc"));
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(exOrder,"pkCnord")+"',";
				   }else{
					   pkCnords+="'"+SDMsgUtils.getPropValueStr(paramList.get(i),"pkCnord")+"',";
				   }
			   }
		   }
	   }
	   //查询所需数据
	    StringBuilder sqls=new StringBuilder("SELECT co.pk_dept_exec, co.pk_pv, co.price_cg, co.code_apply, co.eu_pvtype, co.ordsn, co.desc_ord, co.code_ord,");
	    sqls.append("co.code_ordtype,co.name_ord, pm.code_pi FROM cn_order co LEFT JOIN pi_master pm ON pm.pk_pi = co.pk_pi");
		sqls.append(" where co.pk_cnord  in ("+pkCnords+")");
		List<Map<String,Object>> mapLists =DataBaseHelper.queryForList(sqls.toString());
		if(mapLists!=null){
			for (Map<String, Object> maps : mapLists){
				if("03".equals(maps.get("codeOrdtype").toString().substring(0, 2))){
					//List<Map<String,Object>> orlList=new ArrayList<Map<String,Object>>();//ORLO22消息(检验收费/退费确认)
					maps.put("control", SDMsgUtils.getPropValueStr(paramMap,"Control"));
					//orlList.add(maps);
					//msgSendBl.sendORLMsg("O22", orlList,paramMap.get("type").toString());
					sDMsgSendBl.LbsendORLMsg("O22", maps,paramMap.get("type").toString());
					//orgList.clear();
				}
				if("02".equals(maps.get("codeOrdtype").toString().substring(0,2))){
					maps.put("control",SDMsgUtils.getPropValueStr(paramMap,"Control"));
					maps.put("fenlei", "ris");
					//orgList.add(map);
					sDMsgSendBl.sendOGRGMsg("O20", maps,paramMap.get("type").toString());
					//orgList.clear();
				}
			}
		}
   }

	/**
	 * 门诊医技确费
	 * @param mapList
	 * @param listMap
	 * @param Control
	 * @throws Exception
	 */
   public void sendOpApplyEnsureMsg (List<Map<String,Object>> mapList,List<Map<String, Object>> listMap,String Control){
	   for (Map<String, Object> map : mapList) {
			if("02".equals(map.get("codeOrdtype").toString().substring(0,2))){
				for(Map<String, Object> risMap:listMap){
					   if((map.get("ordsn")).equals(risMap.get("ordsn"))){
						   map.put("control", Control);
						   map.put("fenlei", "ris");
						   sDMsgSendOpBl.sendOGRGMsgOut("O20", map,"O");
						}
				   }
			}
			if("03".equals(map.get("codeOrdtype").toString().substring(0, 2))){
				map.put("control", Control);
				sDMsgSendOpBl.sendORLMsgOut("O22", map,"O");
			}
		}
   }







   public void sendOpRecipeMsg(List<Map<String,Object>> opDtList,Map<String,Object> paramMap,String Control) throws Exception{
		String stringBean = JsonUtil.writeValueAsString(paramMap.get("invoiceInfo"));// 发票列表
		List<Map<String, Object>> invoices = JsonUtil.readValue(stringBean, List.class);
		if(null != invoices){
			for (int i = 0; i < opDtList.size(); i++) {
			     for (int j = 0; j < invoices.size(); j++) {
			    	 List<Map<String,Object>> blinDtsLis = (List<Map<String,Object>>)invoices.get(j).get("blInDts");// blinDtsLis   发票明细       opDtList   住院明细
					if (blinDtsLis.get(0).get("codeBill").equals(opDtList.get(i).get("codeBill"))) {
						opDtList.get(i).put("codeInv", invoices.get(j).get("codeInv"));
						break;
					}
				}
			}
		}else if(null != paramMap.get("inVoiceNo") && !("").equals(paramMap.get("inVoiceNo"))){   // 发票号   inVoiceNo
			for (int i = 0; i < opDtList.size(); i++) {
				opDtList.get(i).put("codeInv", paramMap.get("inVoiceNo"));
			}
		}
		sDMsgSendOpBl.sendORPMsg("O10", opDtList, Control);
		opDtList.clear();
   }
   /**
    * 发送住院床位费用
    * @param paramMap
    */

   public void sendBedCgMsg(Map<String,Object> paramMap){
	   sDMsgSendBl.sendBedCgInfo("RAS_O17",paramMap);
   }

   /**
    * 发送高值耗材计费消息
    * @param paramMap
    */

   public void sendHighValueConSumIp(Map<String,Object> paramMap){
	   sDMsgSendBl.sendHighValueConSumIp("NW", paramMap);
   }

   /**
    * 发送高值耗材退费信息
    * @param paramMap
    */

   public void sendHighValueConSumIpBack(Map<String,Object> paramMap){
	   sDMsgSendBl.sendHighValueConSumIp("CA", paramMap);
   }

}
