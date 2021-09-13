package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.zebone.nhis.bl.pub.vo.InvInfoVo;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgResend;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendIp;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;

import ca.uhn.hl7v2.HL7Exception;

/**
 * 住院发消息
 * @author maijiaxing
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDPlatFormSendIpHandler {

   @Resource
   private SDMsgSendIp sDMsgSendIp;
   @Resource
   private SDMsgResend sDMsgResend;
   @Resource
   private SDQueryUtils sDQueryUtils;
   @Resource
   private SDMsgMapper sDMsgMapper;
   private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

	   
   /**
    * 发送医嘱消息（O09）
    * @param paramMap :pkPi,pkPv,医嘱主键List<String>pkCnords 或者医嘱号List<String>ordsns
    * @throws HL7Exception 
    */
   public void sendOrderInfoMsg(Map<String, Object> paramMap) throws HL7Exception{
	   //通过医嘱主键查询相关信息  pkCnords：医嘱主键集合 ；或者ordsns医嘱号集合
	   List<Map<String, Object>> queryOrder = sDMsgMapper.queryOrder(paramMap);
	   paramMap.put("orderList", queryOrder);//医嘱信息 
	   //查询患者信息
	   if(queryOrder!=null && queryOrder.size()>0){
		   paramMap.putAll(queryOrder.get(0));
	   }
	   List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
	   sDMsgSendIp.sendOrderInfoMsg(paramMap);
   }


   /**
    * 催缴预交金(深大项目)
    * @param paramMap
    */
   public void sendCallPayMsg(Map<String, Object> paramMap){
	   sDMsgSendIp.sendCallPayMsg(paramMap);
   }

   /**
    * 发票（住院）（深圳项目）
    * @param paramMap 
    * @throws HL7Exception
    */
   public void sendReceiptMsg(Map<String, Object> paramMap) throws HL7Exception {
	   	//组装消息的数据集合
	   	Map<String,Object> map = new HashMap<String,Object>();
		//发票标志
		String fpbz = SDMsgUtils.getPropValueStr(paramMap, "fpbz");
		switch (fpbz){
			case "1":{
				//查询所需数据 
				List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
				if(null != queryPatList && queryPatList.size() > 0 ){
					map.putAll(queryPatList.get(0));
				}
				//结算记录查询 bl_deposit   bl_settle（查）
				Map<String, Object> queryBlSettleByPv = sDQueryUtils.queryBlSettleByPv(SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
				if(queryBlSettleByPv != null && queryBlSettleByPv.size()>0){
					map.putAll(queryBlSettleByPv);
				}
				//多张发票
				List<InvInfoVo> invoInfos = (List<InvInfoVo>) paramMap.get("invoInfos");
				if(invoInfos!=null && invoInfos.size()>0){
				   for(int i=0;i<invoInfos.size();i++){
					   String codeInv = invoInfos.get(i).getInv().getCurCodeInv();
						map.put("codeInv", codeInv);
						map.put("amountlist", sDQueryUtils.queryInvoiceDt("", codeInv));
						map.put("fpbz", fpbz);
						sDMsgSendIp.sendReceiptMsg(map);
				   }
				}else{
					log.info("线程名字："+Thread.currentThread().getName()+"发票消息发票号参数为空！");
				}
			}break;
			case "2":{
				//发票重打
				String pkSettle = SDMsgUtils.getPropValueStr(paramMap, "pkSettle");
				Map<String, Object> querySettleByPk = sDQueryUtils.querySettleByPk(pkSettle);
				if(querySettleByPk!=null && querySettleByPk.size()>0){
					paramMap.putAll(querySettleByPk);
				}else{
					log.info("结算信息不存在！");
				}
				//查询所需数据 
				List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
				if(null != queryPatList && queryPatList.size() > 0 ){
					map.putAll(queryPatList.get(0));
				}
				//新发票集合
				List<String> codeInvList = (List<String>) paramMap.get("codeInv");
				for(int i=0;i<codeInvList.size();i++){
					List<Map<String, Object>> queryInvoiceByPkSettle = sDQueryUtils.queryInvoiceByPkSettle(pkSettle,codeInvList.get(i),"and inv.flag_cancel='0'");
					map.putAll(queryInvoiceByPkSettle.get(0));
					//查询发发票明细
					map.put("amountlist", sDQueryUtils.queryInvoiceDt("", codeInvList.get(i)));
					//合并新旧发票号（如果金额相等，说明是对应的发票号 amount_inv）
					Map<String, Object> queryOldCodeInvByPkSettle = sDQueryUtils.queryOldCodeInvByPkSettle(pkSettle, SDMsgUtils.getPropValueStr(queryInvoiceByPkSettle.get(i), "amountInv"));
					map.put("oldCodeInv", SDMsgUtils.getPropValueStr(queryOldCodeInvByPkSettle, "codeInv"));
					map.put("fpbz", fpbz);
					sDMsgSendIp.sendReceiptMsg(map);
				}
			}break;
			case "3":{
				//查询所需数据 
				List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
				if(null != queryPatList && queryPatList.size() > 0 ){
					map.putAll(queryPatList.get(0));
				}
				//取消结算
				//根据结算主键查询发票信息
				String pkSettle = SDMsgUtils.getPropValueStr(paramMap, "pkSettle");
				List<Map<String, Object>> queryInvoiceByPkSettle = sDQueryUtils.queryInvoiceByPkSettle(pkSettle,"","order by inv.CODE_INV desc ");
				//Map<String, Object> queryBlSettle = sDQueryUtils.queryBlSettle(pkSettle);
				if(queryInvoiceByPkSettle!=null && queryInvoiceByPkSettle.size()>0){
					//for(int i=0;i<queryInvoiceByPkSettle.size();i++){
						String pkInvoice = SDMsgUtils.getPropValueStr(queryInvoiceByPkSettle.get(0), "pkInvoice");
						//发票明细
						map.putAll(queryInvoiceByPkSettle.get(0));
						map.put("amountlist", sDQueryUtils.queryInvoiceDt(pkInvoice, ""));
						map.put("fpbz", fpbz);
						sDMsgSendIp.sendReceiptMsg(map);
					//}
				}else{
					log.info("线程名字："+Thread.currentThread().getName()+"取消结算发票消息，未查到发票信息数据");
				}
			}break;
		}
	}

   /**
    * 预交金管理（深圳项目）
    * @param paramMap
    * @throws HL7Exception
    */
   public void sendDepositMsg(Map<String, Object> paramMap) throws HL7Exception {
	   sDMsgSendIp.sendDepositMsg(paramMap);
	}

   /**
	 * 发送手术申请信息（住院）
	 * @param paramMap  (pkCnList=医嘱主键集合)
	 * @throws HL7Exception
	 * @throws InterruptedException 
	 */
	public void sendOpApplyMsg(Map<String,Object> paramMap) throws HL7Exception, InterruptedException{
		if("CA".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			//删除标志
			paramMap.put("euStatusOrd","9" );
		}else{
			if(paramMap.containsKey("euStatusOrd")){
				paramMap.remove("euStatusOrd");
			}
		}
		//查询手术申请信息(control如果是CR，则不查询，因为数据已被删除，会报错)
		List<Map<String, Object>> queryOp = sDMsgMapper.queryOperation(paramMap);
		if(queryOp != null && queryOp.size()>0){
			paramMap.putAll(queryOp.get(0));
		}else{
			log.info(Thread.currentThread().getName()+":手术申请消息ORM^O01，查询结果为空！");
		}
		//查询患者信息所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		for(Map<String,Object> map : queryOp){
			map.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
			map.put("map", paramMap);
			sDMsgSendIp.sendOpApplyMsg(map);
		}
	}


	/**
	 * 发送手术医嘱确认（深圳）
	 * @param paramMap
	 * @throws HL7Exception
	 * @throws InterruptedException 
	 */
	public void sendOpConfirmMsg(Map<String, Object> paramMap) throws HL7Exception, InterruptedException{
		if("OK".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			List<Map<String,Object>> oplist = (List<Map<String, Object>>) paramMap.get("oplist");
			List<String> pkCnList = new ArrayList<String>();
			if(oplist!=null && oplist.size()>0){
				for(Map<String,Object> m : oplist){
					pkCnList.add(SDMsgUtils.getPropValueStr(m, "pkCnord"));
				}
			}
			paramMap.put("pkCnList", pkCnList);
		}
		if("CR".equals(SDMsgUtils.getPropValueStr(paramMap, "control"))){
			//删除标志
			paramMap.put("euStatusOrd","9" );
		}else{
			if(paramMap.containsKey("euStatusOrd")){
				paramMap.remove("euStatusOrd");
			}
		}
		//查询手术申请信息(control如果是CR，则不查询，因为数据已被删除，会报错)
		List<Map<String, Object>> queryOp = sDMsgMapper.queryOperation(paramMap);
		if(queryOp != null && queryOp.size()>0){
			paramMap.putAll(queryOp.get(0));
			for(Map<String,Object> map : queryOp){
				//查询患者信息所需数据
				List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(map);
				if(null != queryPatList && queryPatList.size() > 0 ){
					paramMap.putAll(queryPatList.get(0));
				}
				map.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));
				map.put("patmap", paramMap);
				sDMsgSendIp.sendOpConfirmMsg(map);
			}
		}else{
			log.info("手术消息ORR^O02;查询结果为空！sendOpConfirmMsg 线程名："+Thread.currentThread().getName());
		}
	}

	/**
	 * 发送检验医嘱消息（医嘱执行）
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void  sendExConfirmMsg(Map<String,Object> paramMap) throws HL7Exception{
		String typestatus = paramMap.get("typeStatus").toString();
		List<Map<String,Object>> listParamMap = (List<Map<String,Object>>)paramMap.get("exlist");
		List<String> pkExoccList = new ArrayList<String>();
		List<String> pkCnordList = new ArrayList<String>();
		//深大重写
		if(typestatus!=null&&typestatus.equals("DEL")){
			for(int i=0;i<listParamMap.size();i++){
				pkExoccList.add((String) listParamMap.get(i).get("pkExocc"));
			}
			//一条医嘱对应多条执行单（查询已删除）
			/**
			 
			List<Map<String, Object>> queryExoccByEx = sDMsgMapper.queryExoccByEx(pkExoccList);
			for(int i=0;i<queryExoccByEx.size();i++){
				Map<String, Object> map = queryExoccByEx.get(i);
				sDMsgSendIp.sendExConfirmMsg(map,typestatus);
			}
			* 
			 */
		}else if(typestatus!=null&&typestatus.equals("ADD")){
			for(int i=0;i<listParamMap.size();i++){
				pkCnordList.add((String)listParamMap.get(i).get("pkCnord"));
			}
			//一条医嘱对应多条执行单（查询已删除）
			/**
			List<Map<String, Object>> queryExoccByCn = sDMsgMapper.queryExoccByCn(pkCnordList);
			for(int i=0;i<queryExoccByCn.size();i++){
				Map<String, Object> map = queryExoccByCn.get(i);
				sDMsgSendIp.sendExConfirmMsg(map,typestatus);
			}
			* 
			 */
		}else{
			log.info("消息未发出发送检验医嘱消息（医嘱执行）操作标志为空！");
		}
	}

	/***************************微信模块*************************************************/

	/**
	 * 查询住院信息列表(Theme)
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendQueryIpMsg(Map<String, Object> paramMap) throws HL7Exception{
		sDMsgSendIp.sendQueryIpMsg(paramMap);
	}

	/**
	 * 查询患者信息(住院)
	 * @param paramMap
	 */
	public void sendQueryPiMsg(Map<String, Object> paramMap) {
		sDMsgSendIp.sendQueryPiMsg(paramMap);

	}
	/**
	 * 查询费用类别统计(TotalExpenses)
	 * @param paramMap
	 */
	public void sendTotalExpensesMsg(Map<String, Object> paramMap) {
		sDMsgSendIp.sendTotalExpensesMsg(paramMap);

	}
	/**
	 * 押金缴入(住院) 接收 DFT^P03  WeChat（深大项目）（深大项目）
	 * @param paramMap
	 */
	public void sendFeedbackDepositMsg(Map<String, Object> paramMap) {
		sDMsgSendIp.sendFeedbackDepositMsg(paramMap);

	}
	/**
	 * 查询押金缴入状态 接收 QBP^ZYJ WeChat（深大项目）
	 * @param paramMap
	 */
	public void sendDepositStatusMsg(Map<String, Object> paramMap) {
		sDMsgSendIp.sendDepositStatusMsg(paramMap);

	}
	
	/**
	 * 会诊申请消息
	 * @param paramMap
	 */
	public void sendConsultMsg(Map<String, Object> paramMap){
		//查询会诊申请单信息
		List<Map<String, Object>> consults = sDMsgMapper.queryConsultApply(paramMap);
		if(consults!=null && consults.size()>0){
			//查询患者信息参数pkPi，pkPv
			paramMap.putAll(consults.get(0));
			//查询患者信息
			List<Map<String, Object>> patList = sDMsgMapper.queryPatList(paramMap);
			if(patList!=null && patList.size()>0){
				//paramMap.put("patient", patList.get(0));
				paramMap.put("pati", patList.get(0));
			}
			
			for(Map<String, Object> map:consults){
				paramMap.putAll(map);
				//查询会诊应答信息
				List<Map<String, Object>> consultResponse = sDMsgMapper.queryConsultResponse(map);
				paramMap.put("consultResponse", consultResponse);
				//查询会诊应答信息
				sDMsgSendIp.sendConsultMsg(paramMap);
			}
		}else{
			log.info("会诊申请结果为空，查询患者信息参数不正确；查询失败！");
		}
		
		
	}
	/**
	 * 会诊应答消息
	 * @param paramMap
	 */
	public void sendConsultResponeMsg(Map<String, Object> paramMap){
		//查询会诊申请单信息
		List<Map<String, Object>> consults = sDMsgMapper.queryConsultApply(paramMap);
		if(consults!=null && consults.size()>0){
			//查询患者信息参数pkPi，pkPv
			paramMap.putAll(consults.get(0));
			//查询患者信息
			List<Map<String, Object>> patList = sDMsgMapper.queryPatList(paramMap);
			if(patList!=null && patList.size()>0){
				//paramMap.put("patient", patList.get(0));
				paramMap.put("pati", patList.get(0));
			}
		}else{
			log.info("会诊应答结果为空，查询患者信息参数不正确；查询失败！");
		}
		for(Map<String, Object> map:consults){
			paramMap.putAll(map);
			//查询会诊应答信息
			List<Map<String, Object>> consultResponse = sDMsgMapper.queryConsultResponse(map);
			paramMap.put("consultResponse", consultResponse);
			//查询会诊应答信息
			sDMsgSendIp.sendConsultResponeMsg(paramMap);
		}
	}
}
