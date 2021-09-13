package com.zebone.nhis.ma.pub.platform.send.impl.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.ResponseBody;
import com.zebone.nhis.ma.pub.platform.pskq.service.VitalSignsService;
import com.zebone.nhis.ma.pub.platform.pskq.web.HttpRestTemplate;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.send.PlatFormSendHandlerAdapter;
import com.zebone.nhis.webservice.pskq.config.SpringContextHolder;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 振邦集成平台消息发送服务
 * @author yangxue
 *
 */
@Service("SDPlatFormSendService")
@SuppressWarnings("unchecked")
public class SDPlatFormSendPubHandler extends PlatFormSendHandlerAdapter {
	@Resource
	private SDPlatFormSendAdtHandler sDPlatFormSendAdtHandler;
	@Resource
    private SDPlatFormSendBdHandler sDPlatFormSendBdHandler;
	@Resource
    private SDPlatFormSendBlHandler sDPlatFormSendBlHandler;
	@Resource
    private SDPlatFormSendCnHandler sDPlatFormSendCnHandler;
	@Resource
    private SDPlatFormSendExHandler sDPlatFormSendExHandler;
	@Resource
    private SDPlatFormSendIpHandler sDPlatFormSendIpHandler;
	@Resource
	private SDPlatFormSendOpHandler sDPlatFormSendOpHandler;

	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Override
	public void sendBedChange(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendAdtHandler.sendBedChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBedChange ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdDefDocMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdDefDocMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdDefDocMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdTermDiagMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdTermDiagMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdTermDiagMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdTermFreqMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdTermFreqMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdTermFreqMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdSupplyMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdSupplyMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdSupplyMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdResBedMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdResBedMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdResBedMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdOrdMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendBdHandler.sendBdOrdMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdOrdMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdItemMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendBdHandler.sendBdItemMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdItemMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdItemSetMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdItemSetMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdItemSetMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdOuDeptMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendBdOuDeptMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdOuDeptMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdOuEmpMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendEmpMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdOuEmpMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBdOuUserMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBdHandler.sendUserMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBdOuUserMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBedChangeMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendBedChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBedChangeMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBedPackMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendBedPackMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBedPackMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBedRtnPackMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendBedRtnPackMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBedRtnPackMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendDeptInMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendAdtHandler.sendDeptInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendDeptInMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendCancelDeptInMsg(Map<String, Object> paramMap){
		try{
			sDPlatFormSendAdtHandler.sendCancelDeptInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendCancelDeptInMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendDoctorChangeMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendDoctorMainChangeMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendDoctorChangeMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 修改患者信息
	 */
	@Override
	public void sendPiMasterMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendPiMasterMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPiMasterMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvInfoMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendAdtHandler.sendPvInfoMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvInfoMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvInMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendAdtHandler.sendPvInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvInMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvCancelInMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendPvCancelInMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvCancelInMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvOutMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendAdtHandler.sendPvOutMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvOutMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvCancelOutMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendPvCancelOutMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvCancelOutMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvOpRegMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendAdtHandler.sendPvOpRegMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvOpRegMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvOpCancelRegMsg(Map<String, Object> paramMap){
		try{
			//退号消息
			sDPlatFormSendAdtHandler.sendPvOpRegCancelMsg(paramMap);
			//挂号退费消息
			//paramMap.put("CancelReg", "");
			//sDPlatFormSendBlHandler.sendRefundMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendPvOpCancelRegMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBlCancelSettleMsg(Map<String, Object> paramMap){
		try{
			sDPlatFormSendBlHandler.sendBlCancelSettleMsg(paramMap);
			 //2.深大发送发票信息
		     // Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		     paramMap.put("fpbz", "3");
		     sDPlatFormSendIpHandler.sendReceiptMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBlCancelSettleMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBlSettleMsg(Map<String, Object> paramMap){
		try{
			//结算消息
			sDPlatFormSendBlHandler.sendBlSettleMsg(paramMap);
			//2.新增发送发票信息（1为新增）
			paramMap.put("fpbz", "1");
			//paramMap.put("invoInfos", invoInfos);
			//paramMap.put("amountList", SDMsgMapper.queryZMRDataByPkPv(paramMap));
			//发票消息
			sDPlatFormSendIpHandler.sendReceiptMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBlSettleMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 门诊结算（收费退费）
	 */
	@Override
	public void sendBlOpSettleMsg(Map<String, Object> paramMap){
		try{
			sDPlatFormSendBlHandler.sendBlOpSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBlOpSettleMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendBlOpCancelSettleMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendBlHandler.sendBlOpCancelSettleMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBlOpCancelSettleMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检验检查消息ORL^O22  ORG^O20
	 */
	@Override
	public void sendBlMedApplyMsg(Map<String, Object> paramMap){
		try{
			if(paramMap.containsKey("SDNoSend")){
				paramMap.remove("SDNoSend");
			}else if(paramMap.containsKey("execAndCg")){
				//执行确认 不发送消息
			}else if(paramMap.containsKey("sureOrd")){
				//2、手术管理--医嘱确认触发点
				paramMap.put("control", "NW");//操作类型
				paramMap.put("ordStatus", "3");//医嘱状态
				paramMap.put("ordlist",paramMap.get("dtlist"));
				sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
				sDPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);
			}else{
				sDPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);
			}
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendBlMedApplyMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 发送诊断消息 ADT^A31
	 */
	@Override
	public void sendCnDiagMsg(Map<String, Object> paramMap)  {
		try{
			sDPlatFormSendCnHandler.sendCnDiagMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendCnDiagMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检查检验申请单（触发点：1、医嘱签署界面签署。2、未知）
	 */
	@Override
	public void sendCnMedApplyMsg(Map<String, Object> paramMap){
		try{
			//检查检验申请单
			sDPlatFormSendCnHandler.sendCnMedApply(paramMap);
			//判断触发点，整理查询接口数据
			String type  = SDMsgUtils.getPropValueStr(paramMap, "type");
			switch (type){
				case "RisLis":{//医嘱签署页面
					//查询接口参数
					List<String> pkCnords = new ArrayList<String>();
					List<CnOrder> orderList = (List<CnOrder>)paramMap.get("lisList");
					for(CnOrder order :orderList){
						pkCnords.add(order.getPkCnord());
					}
					paramMap.put("pkCnords", pkCnords);//参数
					//会诊应答消息
					sDPlatFormSendIpHandler.sendConsultMsg(paramMap);
				}
				default:break;
			}
		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendCnMedApplyMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendCnPresOpMsg(Map<String, Object> paramMap)
			 {
		try{
			sDPlatFormSendCnHandler.sendCnPresOpMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
			log.info("消息发送失败！错误方法：sendCnPresOpMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * 不启用
	 */
	@Override
	public void sendExConfirmMsg(Map<String, Object> paramMap){
		try{
			//sDPlatFormSendIpHandler.sendExConfirmMsg(paramMap);
			//sDPlatFormSendExHandler.sendExConfirmMsg(paramMap);

		} catch (Exception e) {
			//记录发送消息失败日志
		}
	}

	/**
	 * 医生站医嘱签署
	 * 护士站医嘱核对消息O09
	 */
	@Override
	public void sendExOrderCheckMsg(Map<String, Object> paramMap){
		try{
			//根据不同的触发点进行判断
			if(paramMap.containsKey("ns")){
				//1、护士站--护嘱 触发点
				//前台数据  医嘱主键集合以及ordlistvo
			   List<Map<String,Object>> ordlist =  (List<Map<String, Object>>) paramMap.get("ordlistvo");
			   if(ordlist != null && ordlist.size()>0){
				   paramMap.putAll(ordlist.get(0));//获取pkPv ，pkPi等数据
			   }
				sDPlatFormSendIpHandler.sendOrderInfoMsg(paramMap);
			}else if(paramMap.containsKey("checkOrd")){
				log.info("sendExOrderCheckMsg方法入口：线程名：{}",Thread.currentThread().getName());
				//2、护士站--医嘱核对触发点
				paramMap.put("control", "NW");//操作类型
				paramMap.put("ordStatus", "3");//医嘱状态
				sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
				log.info("sendExOrderCheckMsg方法入口：sendExOrderCheckMsg执行完成！线程名：{}",Thread.currentThread().getName());
				//发手术确认消息给平台（深大项目）
				paramMap.put("oplist", paramMap.get("ordlist"));
				paramMap.put("control", "OK");//操作类型OK
				sDPlatFormSendIpHandler.sendOpConfirmMsg(paramMap);
				log.info("sendExOrderCheckMsg方法入口：sendOpConfirmMsg执行完成！线程名：{}",Thread.currentThread().getName());
				
				//深大医嘱核对发消息
				List<OrderCheckVo> checkList = (List<OrderCheckVo>) paramMap.get("checkList");
				Map<String,Object> exMap = new HashMap<String,Object>();
				List<Map<String,Object>> exList = new ArrayList<Map<String,Object>>();
				for(int i=0;i<checkList.size();i++){
					OrderCheckVo o = checkList.get(i);
					//发送RAS^O17消息（医嘱执行）深大项目
					exMap = JsonUtil.readValue(ApplicationUtils.beanToJson(o), Map.class);
					exList.add(exMap);
					//exMap.put("exlist", exList);
					//exMap.put("typeStatus", "ADD");
				}
				//检查检验消息（ORL^O22）（ORG^O20）
				//msgMap.put("pkCnordS", exList);
				paramMap.put("dtlist", exList);
				paramMap.put("type", "I");
				paramMap.put("Control", "OK");
				sDPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);
				log.info("sendExOrderCheckMsg方法入口：sendBlMedApplyMsg执行完成！线程名：{}",Thread.currentThread().getName());
				
				//医嘱核对消息，移动护理（RAS_O17）(暂无消息)
				//sendExConfirmMsg(exMap);
			}else{
				//默认发送消息（非护嘱）
				sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
			}
		} catch (Exception e) {
			//log.info("消息发送失败！错误方法：sendExOrderCheckMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

   /**
    * 发送药品信息
    */
	@Override
	public void sendBdPdMsg(Map<String, Object> paramMap) {
		try{
			sDPlatFormSendBdHandler.sendBdPdMsg(paramMap);
		} catch (Exception e) {
			//记录发送消息失败日志
		}

	}

	@Override
	public void sendEmrMsg(Map<String, Object> paramMap) {
		//todo
	}

	@Override
	public void sendBdFactoryMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendBdOuOrgMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendMsgLisRis(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}
	@Override
	public void sendSchInfo(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendOpHandler.sendOpSchInfo(paramMap);
		} catch (HL7Exception e) {
			log.info("调用平台发送消息方法线程名："+Thread.currentThread().getName()+"【sendSchInfo】异常，详细信息："+e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void sendSchAppt(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Map<String,Object>> getEMRClinicalData(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendScmIpDeDrug(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendExHandler.sendScmIpDeDrug(paramMap);
		} catch (HL7Exception e) {
			log.info("线程名："+Thread.currentThread().getName()+",方法：sendScmIpDeDrug，异常原因："+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendPvOpNoMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

	}

	@Override
	public String sendCnOpCall(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub

		return null;
	}

	/**
	 * 诊疗级别（深圳）
	 */
	@Override
	public void sendRegLevelMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBdHandler.sendRegLevelMsg(paramMap);
		} catch (HL7Exception e) {
			log.info("消息发送失败！错误方法：sendRegLevelMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 单位合同（深圳）
	 */
	@Override
	public void sendPactMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBdHandler.sendPactMsg(paramMap);
		} catch (HL7Exception e) {
			log.info("消息发送失败！错误方法：sendPactMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 预交金（深圳）
	 */
	@Override
	public void sendDepositMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendIpHandler.sendDepositMsg(paramMap);
		} catch (HL7Exception e) {
			log.info("消息发送失败！错误方法：sendDepositMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 手术申请（深圳）
	 */
	@Override
	public void sendOpApplyMsg(Map<String, Object> paramMap) {
		try {
			if(paramMap.containsKey("cancelOpApply")){
				//1、撤销手术申请      发送手术申请撤销消息  ORM^O01
				paramMap.put("control", "CA");//操作类型
				sDPlatFormSendIpHandler.sendOpApplyMsg(paramMap);
				//发手术确认消息给平台  ORR^O02
				paramMap.put("control", "CR");//操作类型
				sDPlatFormSendIpHandler.sendOpConfirmMsg(paramMap);
				//发送医嘱O09 消息
				paramMap.put("control", "NW");//操作标志
				paramMap.put("ordStatus", "6");//医嘱状态
				sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
			}else if(paramMap.containsKey("cancelSignOpApply")){
				//2、取消签署手术医嘱
				paramMap.put("control", "CA");//操作类型
				sDPlatFormSendIpHandler.sendOpApplyMsg(paramMap);
			}else if(paramMap.containsKey("signOpApply2")){
				//3、住院 手术医嘱签署：signOpApply2     
				paramMap.put("control", "NW");//操作类型
				sDPlatFormSendIpHandler.sendOpApplyMsg(paramMap);
			}else if(paramMap.containsKey("saveOpApply")){
				//3、 门诊手术保存 saveOpApply
				if("".equals(SDMsgUtils.getPropValueStr(paramMap, "pkCnord"))){
					paramMap.put("control", "NW");//操作类型
					sDPlatFormSendOpHandler.sendOpApplyMsg(paramMap);
				}else{
					paramMap.put("control", "RU");//操作类型
					sDPlatFormSendOpHandler.sendOpApplyMsg(paramMap);
				}
			}else if(paramMap.containsKey("removeopapply")){
				//门诊删除
				paramMap.put("control", "CA");//操作类型
				sDPlatFormSendOpHandler.sendOpApplyMsg(paramMap);
			}else{
				//4、 默认 
				sDPlatFormSendIpHandler.sendOpApplyMsg(paramMap);
			}

		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendOpApplyMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 手术确认（深圳）
	 */
	@Override
	public void sendOpConfirmMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendIpHandler.sendOpConfirmMsg(paramMap);
		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendOpConfirmMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 发票消息
	 */
	@Override
	public void sendReceiptMsg(Map<String, Object> paramMap) {
		try {
			//旧发票号
			//BlInvPrint blInvPrint = (BlInvPrint) paramMap.get("oldBlInvoice");
			//List<BlInvoice> oldBlInvoice = blInvPrint.getBlInvoice();
			//paramMap.put("oldBlInvoice", oldBlInvoice);
			paramMap.put("fpbz", "2");
			sDPlatFormSendIpHandler.sendReceiptMsg(paramMap);
		} catch (HL7Exception e) {
			log.info("消息发送失败！错误方法：sendReceiptMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void sendCallPayMsg(Map<String, Object> paramMap) {
		try {
			List<Map<String,Object>> paramList = (List<Map<String, Object>>) paramMap.get("prepayList");
    		String tip = (String) paramMap.get("tip");
			if(paramList!=null && paramList.size()>0){
    			for(Map<String,Object> map : paramList){
    				map.put("tip", tip);
    				map.put("type", "ZYYJ");
    				sDPlatFormSendIpHandler.sendCallPayMsg(map);
        		}
    		}
		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendCallPayMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void sendQueryIpMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendIpHandler.sendQueryIpMsg(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendQueryPiMsg(Map<String, Object> paramMap) {
		sDPlatFormSendIpHandler.sendQueryPiMsg(paramMap);

	}

	/**
	 * 已弃用
	 */
	@Override
	public void sendBlWeiXinSQMZQ1Msg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBlHandler.sendBlWeiXinSQMZQ1Msg(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 已弃用
	 */
	@Override
	public void sendBlWeiXinQBPZZLMsgDetails(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBlHandler.sendBlWeiXinQBPZZLMsgDetails(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 已弃用
	 */
	@Override
	public void sendBlWeiXinQBPZZLMsgTheme(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBlHandler.sendBlWeiXinQBPZZLMsgTheme(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void sendTotalExpensesMsg(Map<String, Object> paramMap) {
		sDPlatFormSendIpHandler.sendTotalExpensesMsg(paramMap);

	}

	@Override
	public void sendFeedbackDepositMsg(Map<String, Object> paramMap) {
		sDPlatFormSendIpHandler.sendFeedbackDepositMsg(paramMap);

	}

	@Override
	public void sendOpCompleteMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try{
			sDPlatFormSendOpHandler.sendOpCompleteMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendOpCompleteMsg ：" +e.getClass()+ e.getMessage());
			e.printStackTrace();

		}
	}

	@Override
	public void sendDepositStatusMsg(Map<String, Object> paramMap) {
		sDPlatFormSendIpHandler.sendDepositStatusMsg(paramMap);

	}

	@Override
	public void sendAddExOrderOccMsg(Map<String, Object> paramMap) {
		System.out.println("PDA消息集成-新增");
		if(paramMap==null || paramMap.get("IsSendSD")==null ||(boolean)paramMap.get("IsSendSD")==false ||paramMap.get("occlist")==null)return;
		sDPlatFormSendExHandler.sendExorderCreateMsg(paramMap);
	}

	@Override
	public void sendDelExOrderOccMsg(Map<String, Object> paramMap) {
		System.out.println("PDA消息集成-删除");
		if(paramMap==null || paramMap.get("IsSendSD")==null ||(boolean)paramMap.get("IsSendSD")==false)return;
		sDPlatFormSendExHandler.sendExoderDelMsg(paramMap);

	}

	@Override
	public void sendUpDateExOrderOccMsg(Map<String, Object> paramMap) {
		System.out.println("PDA消息集成-更新");
		if(paramMap==null || paramMap.get("IsSendSD")==null ||(boolean)paramMap.get("IsSendSD")==false)return;
		sDPlatFormSendExHandler.sendExoderCancelMsg(paramMap);

	}

	 /*发送床位费消息
	 */
	@Override
	public void sendBedCgMsg(Map<String, Object> paramMap) {
		//sDPlatFormSendBlHandler.sendBedCgMsg(paramMap);

	}

	/**
	 * 发送费用短信提醒信息
	 * zsrm-门诊欠费短信提醒
	 * @param paramMap
	 */
	@Override
	public void sendOpFeeReminderMsg(Map<String, Object> paramMap) {

	}

	@Override
	public void sendAllExOrderOccMsg(Map<String, Object> paramMap) {
		System.out.println("PDA消息集成-首末次");
		if(paramMap==null || paramMap.get("IsSendSD")==null ||(boolean)paramMap.get("IsSendSD")==false)return;
			sDPlatFormSendExHandler.sendExorderAllMsg(paramMap);
	}

	@Override
	public void sendRelationshipMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendBdHandler.sendRelationshipMsg(paramMap);
		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendRelationshipMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 取消签署处方
	 */
	@Override
	public void sendCancelSignPresMsg(Map<String, Object> paramMap) {
		try{
			paramMap.put("control", "CA");//操作标志
			paramMap.put("ordStatus", "2");//医嘱状态
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendCancelSignPresMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 签署处方
	 */
	@Override
	public void sendSignPresMsg(Map<String, Object> paramMap) {
		try{
			paramMap.put("control", "NW");//操作标志
			paramMap.put("ordStatus", "2");//医嘱状态
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendSignPresMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 作废草药
	 */
	@Override
	public void sendCancelHerbOrderMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try{
			paramMap.put("control", "NW");//操作标志
			paramMap.put("ordStatus", "6");//医嘱状态
			paramMap.put("pkCnList", paramMap.get("pkCnList"));
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendCancelHerbOrderMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 取消草药签署
	 */
	@Override
	public void sendUpdateSignMsg(Map<String, Object> paramMap) {
		try{
			paramMap.put("control", "CA");//操作标志
			paramMap.put("ordStatus", "2");//医嘱状态
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendUpdateSignMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 草药签署
	 */
	@Override
	public void sendSignHerbOrder2Msg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		try{
			paramMap.put("control", "NW");//操作标志
			paramMap.put("ordStatus", "2");//医嘱状态
			//paramMap.put("order", JsonUtil.readValue(ApplicationUtils.objectToJson(paramMap.get("ordList")), new TypeReference<List<Map<String,Object>>>(){}));
			List<String> pkCnList = new ArrayList<String>();
			List<CnOrder> orderList = (List<CnOrder>) paramMap.get("ordList");
			for(CnOrder ord : orderList){
				pkCnList.add(ord.getPkCnord());
			}
			paramMap.put("pkCnList", pkCnList);
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendSignHerbOrder2Msg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 检验作废
	 */
	@Override
	public void sendCancleLisApplyListMsg(Map<String, Object> paramMap) {
		try{
			//发送检验申请作废平台消息  OMG^O21
			paramMap.put("Control","CA");
			paramMap.put("type", "lis");
			sDPlatFormSendCnHandler.sendCnMedApply(paramMap);

			//发送检验作废平台消息ORL^O22
			paramMap.put("Control","CR");
			paramMap.put("type", "lis");
			sDPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);

			//发送平台消息
			paramMap.put("control", "NW");//操作标志
			paramMap.put("ordStatus", "6");//医嘱状态
			//paramMap.put("pkCnList", pkCnOrds);
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendCancleLisApplyListMsg ：" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 检查作废
	 */
	@Override
	public void sendCancleRisApplyListMsg(Map<String, Object> paramMap) {
		try{
			//发送检查作废平台消息  OMG^O19
			paramMap.put("Control","CA");
			paramMap.put("type", "ris");
			sDPlatFormSendCnHandler.sendCnMedApply(paramMap);
			//发送检查作废平台消息 ORG^O20
			paramMap.put("Control","CR");
			//paramMap.put("type", "ris");
			sDPlatFormSendBlHandler.sendBlMedApplyMsg(paramMap);
			//发送平台消息
			paramMap.put("control", "NW");//操作标志
			paramMap.put("ordStatus", "6");//医嘱状态
			//m.put("pkCnList", pkCnOrds);
			sDPlatFormSendExHandler.sendExOrderCheckMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！错误方法：sendCancleRisApplyListMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 手术医嘱作废
	 */
	@Override
	public void sendOperaOrderCancelMsg(Map<String, Object> paramMap) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("type", "RisLis");
			map.put("lisList", paramMap.get("changeOrdList"));
			map.put("isAdd", "0");
			map.put("Control", "CA");
			sDPlatFormSendCnHandler.sendCnMedApply(map);

			map.put("control", "NW");//操作类型
			map.put("ordStatus", "6");//医嘱状态
			map.put("ordlist", paramMap.get("changeOrdList"));
			sDPlatFormSendExHandler.sendExOrderCheckMsg(map);
			//检验作废发送 ORL^O22消息
			map.put("Control", "CR");//操作类型
			map.put("type", "O");//操作类型
			map.put("dtlist", paramMap.get("changeOrdList"));
			sDPlatFormSendBlHandler.sendBlMedApplyMsg(map);
		} catch (Exception e) {
			log.info("消息发送失败！错误方法：sendOperaOrderCancelMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * 保存业务线()
	 */
	@Override
	public void sendSaveDeptBuAndBusesMsg(Map<String, Object> paramMap) {
		try{
			//操作标志 MAD:增加主文件记录
			paramMap.put("control", "MAD");
			//发送病区和科室关系消息
			sDPlatFormSendBdHandler.sendRelationshipMsg(paramMap);
		}catch(Exception e){
			log.info("消息发送失败！保存业务线，错误方法：sendSaveDeptBuAndBusesMsg ：" + e.getMessage());
			e.printStackTrace();
		}

	}
	/**
	 * 高值耗材计费接口
	 */
	@Override
	public void sendHighValueConSumIp(Map<String, Object> paramMap) {
		sDPlatFormSendBlHandler.sendHighValueConSumIp(paramMap);

	}

	/**
	 * 高值耗材退费接口
	 */
	@Override
	public void sendHighValueConSumIpBack(Map<String, Object> paramMap) {
		sDPlatFormSendBlHandler.sendHighValueConSumIpBack(paramMap);

	}

	@Override
	public void sendBdMaterMsg(Map<String, Object> paramMap) {

	}

	/**
	 * 发送门诊到诊消息接口
	 */
	@Override
	public void sendOpArriveMsg(Map<String, Object> paramMap) {
		try {
			paramMap.put("adtType", "门诊");
			paramMap.put("euPvtype", "1");
			sDPlatFormSendAdtHandler.sendDeptInMsg(paramMap);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送门诊转住院的消息
	 */
	@Override
	public void sendOpToIpMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendAdtHandler.sendopToIpMsg(paramMap);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送完成到诊消息
	 */
	@Override
	public void sendFinishClinicMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendAdtHandler.sendFinishClinicMsg(paramMap);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 发送取消就诊消息
	 */
	@Override
	public void sendCancelClinicMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendAdtHandler.sendCancelClinicMsg(paramMap);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改患者信息 (门诊使用)
	 */
	@Override
	public void sendUpPiInfoMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendAdtHandler.sendUpPiInfoMsg(paramMap);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 会诊申请
	 */
	@Override
	public void sendConsultMsg(Map<String, Object> paramMap) {
		try {
			//查询接口参数
			List<String> pkCnords = new ArrayList<String>();
			pkCnords.add(SDMsgUtils.getPropValueStr(paramMap, "pkCnord"));
			paramMap.put("pkCnords", pkCnords);//参数
			//签署
			if(paramMap.containsKey("signConsult")){
				paramMap.put("control", "NW");
				paramMap.put("ordStatus", "2");//签署
			//取消签署
			}else if(paramMap.containsKey("unCommitConsultInfo")){
				paramMap.put("control", "CA");
				paramMap.put("ordStatus", "2");//签署
			//作废
			}else if(paramMap.containsKey("updateConsultInfo")){
				paramMap.put("control", "CA");
				paramMap.put("ordStatus", "6");//作废
				sDPlatFormSendIpHandler.sendOrderInfoMsg(paramMap);
			}
			//会诊申请消息
			sDPlatFormSendIpHandler.sendConsultMsg(paramMap);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 会诊应答
	 * @param paramMap
	 */
	@Override
	public void sendConsultResponeMsg(Map<String, Object> paramMap){
		try{
			//查询接口参数
			List<String> pkCnords = new ArrayList<String>();
			pkCnords.add(SDMsgUtils.getPropValueStr(paramMap, "pkCnord"));
			paramMap.put("pkCnords", pkCnords);//参数
			//应答
			if(paramMap.containsKey("submitResponse")){
				paramMap.put("control", "NW");
			}
			//会诊应答消息
			sDPlatFormSendIpHandler.sendConsultResponeMsg(paramMap);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 发送门诊检查检验消息
	 * @param paramMap
	 */
	@Override
	public void sendCnOpAppMsg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendOpHandler.sendCnOpAppMsg(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 发送门诊处方消息
	 */
	@Override
	public void sendOpO09Msg(Map<String, Object> paramMap) {
		try {
			sDPlatFormSendCnHandler.sendOpO09Msg(paramMap);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendDeptChangeMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBdDeptUnitMsg(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendShortMsgPhoneChk(Map<String, Object> paramMap) {

	}

	@Override
	public void sendDstributeCardMsg(Map<String, Object> paramMap) {

	}
	
	/**
     * 生命体征
     * @param paramMap
     */
    @Override
    public void sendVitalSigns(Map<String, Object> paramMap) {
        int n = 0;
    }


}
