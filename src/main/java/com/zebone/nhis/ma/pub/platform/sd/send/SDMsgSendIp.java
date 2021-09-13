package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.RAS_O17;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreate;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.RSP_ZZL;
import com.zebone.nhis.ma.pub.platform.sd.vo.SQR_ZQ1;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZDL;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZIV;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 住院消息发送处理(深圳项目新加)
 * @author maijiaxing
 *
 */
@Service
public class SDMsgSendIp {

	@Resource
	private MsgCreate msgCreate;
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDMsgSendAdt sDMsgSendAdt;

	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.split.num", "10"));

	/**
	 * 医嘱核对消息发送（自定义医嘱执行）
	 * @throws HL7Exception
	 */
	public void sendExConfirmMsg(Map<String,Object> paramMap,String typeStatus){
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRAS_O17Msg(msgId, paramMap,typeStatus);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (HL7Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * （发要启用，其他不启用）
	 * 医嘱核对消息发送（自定义医嘱执行）-多条发送
	 * @throws HL7Exception
	 */
	public void sendExConfirmMsg(List<Map<String,Object>> resList,String typeStatus) throws HL7Exception{
		String msgId=SDMsgUtils.getMsgId();
		RAS_O17 ras = new RAS_O17();
		MSH msh = ras.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");
		PID pid = ras.getPATIENT().getPID();
		PV1 pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
		
		String pkPv=SDMsgUtils.getPropValueStr(resList.get(0), "pkPv");
		String pkPi=SDMsgUtils.getPropValueStr(resList.get(0), "pkPi");
		Map<String,Object> param=new HashMap<>(16);
		param.put("pkPv", pkPv);
		param.put("pkPi", pkPi);
		sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
		
		int index=0;
		String msg;
		for (Map<String, Object> paramMap : resList) {
			//如果患者不相同重新构建MSH,PV1,PID以及msgId,并将之前构建数据先发送
			if (!pkPv.equals(paramMap.get("pkPv"))) {
				msg = MsgUtils.getParser().encode(ras);
				sDHl7MsgHander.sendMsg(msgId, msg);

				msgId = SDMsgUtils.getMsgId();
				ras = new RAS_O17();
				msh = ras.getMSH();
				SDMsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");
				pid = ras.getPATIENT().getPID();
				pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
				param = new HashMap<>(16);
				pkPv = SDMsgUtils.getPropValueStr(paramMap, "pkPv");
				pkPi = SDMsgUtils.getPropValueStr(paramMap, "pkPi");
				param.put("pkPv", pkPv);
				param.put("pkPi", pkPi);
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
				index = 0;
			}
			paramMap.put("typestatus", typeStatus);
			switch (typeStatus) {
				case "DEL":
					paramMap.put("control", "CA");
					break;
				case "UPDATE":
					paramMap.put("control", "RU");
					break;
				//默认新增
				default:
					paramMap.put("control", "NW");
					break;
			}
			ORC orc = ras.getORDER(index).getORC();
			msgCreate.createORCMsg(orc, paramMap);
			RXO rxo = ras.getORDER(index).getORDER_DETAIL().getRXO();
			msgCreate.createRXOMsg(rxo, paramMap);
			NTE nte = ras.getORDER(index).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
			msgCreate.createNTEMsg(nte, paramMap);
			RXR rxr = ras.getORDER(index).getRXR();
			msgCreate.createRXRMsg(rxr, paramMap);
			RXA rxa = ras.getORDER(index).getRXA();
			msgCreate.createRXAMsg(rxa, paramMap);

			if (index != 0 && (index + 1) % splitNum == 0) {
				msg = MsgUtils.getParser().encode(ras);
				sDHl7MsgHander.sendMsg(msgId, msg);

				msgId = MsgUtils.getMsgId();
				ras = new RAS_O17();
				msh = ras.getMSH();
				SDMsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");
				pid = ras.getPATIENT().getPID();
				pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();

				param = new HashMap<>();
				pkPv = SDMsgUtils.getPropValueStr(paramMap, "pkPv");
				pkPi = SDMsgUtils.getPropValueStr(paramMap, "pkPi");
				param.put("pkPv", pkPv);
				param.put("pkPi", pkPi);
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
				index = 0;
			} else {
				index++;
			}
		}
		if (index>=0 &&index<=splitNum) {
			msg = MsgUtils.getParser().encode(ras);
			sDHl7MsgHander.sendMsg(msgId, msg);
		}
			
	}

	/**
	 * 挂号级别消息发送
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendRegLevelMsg(Map<String,Object> paramMap) throws HL7Exception{
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createDFT_P03Msg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HL7Exception("发送入院预交金消息发送！");
		}
	}


	/**
	 * 入院预交金消息发送
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendDepositMsg(Map<String,Object> paramMap) throws HL7Exception{
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createDFT_P03Msg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		} catch (Exception e) {
			e.printStackTrace();
			throw new HL7Exception("发送入院预交金消息发送！");
		}
	}

	/**
	 * 手术申请消息发送
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOpApplyMsg(Map<String,Object> paramMap) {
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createORM_O01Msg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("发送手术申请消息发送失败  ！"+e);
		}
	}

	/**
	 * 手术医嘱确认发送
	 * @param paramMap
	 */
	public void sendOpConfirmMsg(Map<String, Object> paramMap) {
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createORR_O02Msg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 发票（住院）（深圳项目）
	 * @param paramMap
	 */
	public void sendReceiptMsg(Map<String, Object> paramMap) {
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createZPI_PMIMsg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	* 催缴预交金(深大项目)
	* @param paramMap
	*/
	public void sendCallPayMsg(Map<String, Object> paramMap){
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createZDL_TXLMsg(msgId, paramMap);
			String msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*******************************微信模块****************************************/
	
	
	/**
	 * 门诊已缴费记录明细查询
	 * @param paramMap
	 * @return
	 */
	public String sendQueryBlMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRSP_ZDLMsg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 查询住院信息列表(Theme)
	 * @param paramMap
	 */
	public String sendQueryIpMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRSP_ZDIMsg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 查询患者信息(住院)QBP^Q21
	 * @param paramMap
	 */
	public String sendQueryPiMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createQBP_Q21Msg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 查询费用类别统计(TotalExpenses)
	 * @param paramMap
	 */
	public String sendTotalExpensesMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRSP_ZPIMsg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 押金缴入(住院ACK_P03) 接收 DFT^P03  WeChat（深大项目）（深大项目）
	 * @param paramMap
	 */
	public String sendFeedbackDepositMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createACK_P03Msg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 查询押金缴入状态(RSP_ZYJ) 接收 QBP^ZYJ WeChat（深大项目）
	 * @param paramMap
	 */
	public String sendDepositStatusMsg(Map<String, Object> paramMap) {
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRSP_ZYJMsg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			//sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 发送给微信住院相关的缴费信息(深大) SQR^ZQ1
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public String sendSQRZQ1Msg(Map<String,Object> paramMap) throws HL7Exception{
		String sql = "select  case when depo.EU_DPTYPE = '9' then '住院预交金' " +
				" when depo.EU_DPTYPE = '4' then '取消结算' " +
				" when depo.EU_DPTYPE = '3' then '欠费补缴' " +
				" when depo.EU_DPTYPE = '2' then '结算冲账' " +
				" when depo.EU_DPTYPE = '1' then '中途结算' " +
				" when depo.EU_DPTYPE = '0' then '就诊结算' " +
				"  end EU_DPTYPE , doc.NAME,depo.AMOUNT,depo.EU_DIRECT, to_char(depo.date_pay,'yyyyMMddHH24mmss')  date_pay  from BL_DEPOSIT depo " +
				" left join bd_defdoc doc on doc.code = depo.dt_paymode  left join pv_encounter pv on pv.pk_pv = depo.pk_pv " +
				" where pv.code_pv = ?  and doc.CODE_DEFDOCLIST = '110100' and doc.del_flag = '0'  and depo.del_flag = '0' order by DATE_PAY";
		List<Map<String, Object>> patiBl = DataBaseHelper.queryForList(sql,paramMap.get("ipCode"));
		SQR_ZQ1 sqr = new SQR_ZQ1();
		String msgId=SDMsgUtils.getMsgId();
		MSH msh = sqr.getMSH();
		MSA msa = sqr.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "SQR", "ZQ1");
		String msgOldId=SDMsgUtils.getPropValueStr(paramMap,"msgOldId");
		if (patiBl.size()<1) {
				msa.getAcknowledgementCode().setValue("AE");
				msa.getMessageControlID().setValue(msgOldId);
				msa.getTextMessage().setValue("失败");
				return SDMsgUtils.getParser().encode(sqr);
		}else{
			msa.getAcknowledgementCode().setValue("AA");
			msa.getMessageControlID().setValue(msgOldId);
			msa.getTextMessage().setValue("成功");

			int blLend = patiBl.size();
			for (int i = 0; i < blLend; i++) {
				sqr.getARQ(i).getRequestEventReason().getText().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i), "euDptype"));
				String ss = SDMsgUtils.getPropValueStr(patiBl.get(i), "datePay");
				sqr.getARQ(i).getRequestedStartDateTimeRange(0).getRangeStartDateTime().getTimeOfAnEvent().setValue(ss);
			}
			for (int i = 0; i < blLend; i++) {
				sqr.getZAI(i).getPayMethod().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i), "name"));
				sqr.getZAI(i).getZfzje().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i), "amount"));
			}
		}

		return SDMsgUtils.getParser().encode(sqr);
	}

	/**
	 * 发送给微信--住院费每日账单详情(深大) RSP^ZZL
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public String sendQBPZZLMsgDetails(Map<String,Object> paramMap) throws DataTypeException{
		String msg = "";
		String sql = " select PI.code_pi,PI.code_ip,PI.name_pi,DT.PK_CGIP,DT.Code_Cg,dt.name_cg,TO_CHAR(dt.date_cg,'yyyyMMddHH24miss') date_cg,dt.price,dt.quan,dt.amount,un.name," +
				" case PI.dt_sex when '02' then 'M' when '03' then 'F' else 'O' END SEX, case when  PI.dt_idtype= '01'then PI.Id_No end as idno ," +
				" CASE WHEN (select CODE from bd_pd where PK_PD = dt.pk_pd ) IS NULL THEN (select CODE from BD_ITEM where pk_item = dt.pk_item ) ELSE (select CODE from bd_pd where PK_PD = dt.pk_pd ) END as itemcode ," +
				" CASE WHEN (select name from bd_pd where PK_PD = dt.pk_pd ) IS NULL THEN (select name from BD_ITEM where pk_item = dt.pk_item ) ELSE (select name from bd_pd where PK_PD = dt.pk_pd ) END as itemname " +
				" from BL_IP_DT DT  LEFT JOIN PI_MASTER PI ON pi.PK_PI = DT.PK_pi left join pv_encounter pv on pv.pk_pv = dt.pk_pv   left join bd_unit un on un.pk_unit = dt.pk_unit " +
				" WHERE to_date('" + paramMap.get("time") + "000000', 'yyyyMMddHH24miss') < dt.date_cg and to_date('" +paramMap.get("time") + "235959', 'yyyyMMddHH24miss') > dt.date_cg  and pv.code_pv = '" +  paramMap.get("ipCode") +
				"' and DT.del_flag = '0' and PI.del_flag = '0' and pv.del_flag = '0' ";
		List<Map<String, Object>> patiBl = DataBaseHelper.queryForList(sql);
		RSP_ZZL zzl = new RSP_ZZL();
		String msgId = SDMsgUtils.getMsgId();
		MSH msh = zzl.getMSH();
		MSA msa = zzl.getMSA();
		PID pid = zzl.getPID();
		PV1 pv1 = zzl.getPV1();
		ZIV ziv ;
		ZDL zdl ;
		SDMsgUtils.createMSHMsg(msh, msgId, "RSP", "ZZL");
		//消息接收方
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap, "receive"))) {
			msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(paramMap, "receive"));
		}
		try {
			String msgCntrlID=SDMsgUtils.getPropValueStr(paramMap,"msgCntrlID");
			if (patiBl.size()<1) {
				msa.getAcknowledgementCode().setValue("AE");
				msa.getMessageControlID().setValue(msgCntrlID);
				msa.getTextMessage().setValue("失败");
			}else{
				msa.getAcknowledgementCode().setValue("AA");
				msa.getMessageControlID().setValue(msgCntrlID);
				msa.getTextMessage().setValue("成功");
				Map<String, Object> pati = patiBl.get(0);
				int blLen = patiBl.size();
				//PID_2 ID
				pid.getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(pati,"codePi"));
				//PID_3 卡号 codePi
				//pid.getPatientIdentifierList(1).getID().setValue(SDMsgUtils.getPropValueStr(pati,"cardno"));
				pid.getPatientIdentifierList(0).getID().setValue(SDMsgUtils.getPropValueStr(pati,"codeIp"));
				pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IDCard");
				//PID_3 住院号
				pid.getPatientIdentifierList(1).getID().setValue(SDMsgUtils.getPropValueStr(pati,"codePi"));
				pid.getPatientIdentifierList(1).getIdentifierTypeCode().setValue("PatientNO");
				//PID_3 身份证号
				pid.getPatientIdentifierList(2).getID().setValue(SDMsgUtils.getPropValueStr(pati,"idno"));
				pid.getPatientIdentifierList(2).getIdentifierTypeCode().setValue("IdentifyNO");
				//PID_5 姓名
				pid.getPatientName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(pati,"namePi"));
				//PID_8 性别
				pid.getAdministrativeSex().setValue(SDMsgUtils.getPropValueStr(pati,"sex"));
				//【PV1-19】 就诊号码 his visit no
				pv1.getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"ipCode"));
				Double count = 0.0;
				for (int i = 0; i < blLen; i++) {
					count = MathUtils.add(count, Double.valueOf(SDMsgUtils.getPropValueStr(patiBl.get(i),"amount")));
					ziv = zzl.getZIV(i);
					//ziv-2  记账ID
					ziv.getId().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"pkCgip"));
					//ziv-5 费用
					ziv.getGrjf().getQuantity().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"amount"));
					//ziv-8 费用产生时间
					ziv.getGhrq().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"dateCg"));
					//ziv-9 项目执行时间
					ziv.getFprq().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"dateCg"));
				}
				//【PV1-47】 总费用
				pv1.getTotalCharges().setValue(count.toString());
				for (int i = 0; i < blLen; i++) {
					zdl = zzl.getZDL(i);
					//zdl-3 单位
					zdl.getDw().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"name"));
					//zdl-4 数量
					zdl.getSl().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"quan"));
					//zdl-5单价
					zdl.getDj().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"price"));
					//zdl-6 费用
					zdl.getHjje().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"amount"));
					//zdl-1.1 费用编码
					zdl.getDrug().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"itemcode"));
					//zdl-1.2 费用名称
					zdl.getDrug().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(patiBl.get(i),"itemname"));
				}
			}
			msg =  SDMsgUtils.getParser().encode(zzl);
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("ZZL--住院费每日账单详情(深大) 组装错误"+e);
		}
		return msg;
	}

	/**
	 * 发送给微信--住院费每日账单列表(深大) RSP^ZZL
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public String sendQBPZZLMsgTheme(Map<String,Object> paramMap) throws DataTypeException{
		String msg = "";
		String sql = "select PI.code_pi, PI.code_ip, PI.name_pi, pv.code_pv,case PI.dt_sex when '02' then 'M' when '03' then 'F' else 'O' END SEX,case when PI.dt_idtype = '01' then PI.Id_No end as idno " +
				"from  pv_encounter pv left join PI_MASTER PI on PI.pk_pi = pv.pk_pi WHERE pv.code_pv = '" + paramMap.get("ipCode") + "' and  PI.del_flag = '0' and pv.del_flag = '0' ";
		List<Map<String, Object>> patiBl = DataBaseHelper.queryForList(sql);

		RSP_ZZL zzl = new RSP_ZZL();
		String msgId = SDMsgUtils.getMsgId();
		MSH msh = zzl.getMSH();
		MSA msa = zzl.getMSA();
		PID pid = zzl.getPID();
		PV1 pv1 = zzl.getPV1();
		ZIV ziv ;
		SDMsgUtils.createMSHMsg(msh, msgId, "RSP", "ZZL");
		//消息接收方
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap, "receive"))) {
			msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(paramMap, "receive"));
		}
		
		try {
			String MsgCntrlID = SDMsgUtils.getPropValueStr(paramMap, "msgCntrlID");
			if (patiBl.size()<1) {
				msa.getAcknowledgementCode().setValue("AE");
				msa.getMessageControlID().setValue(MsgCntrlID);
				msa.getTextMessage().setValue("失败");
			}else{
				msa.getAcknowledgementCode().setValue("AA");
				msa.getMessageControlID().setValue(MsgCntrlID);
				msa.getTextMessage().setValue("成功");
				Map<String, Object> pati = patiBl.get(0);
				//PID_2 ID
				pid.getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(pati,"codePi"));
				//PID_3 卡号 codePi
				//pid.getPatientIdentifierList(1).getID().setValue(SDMsgUtils.getPropValueStr(pati,"cardno"));
				pid.getPatientIdentifierList(0).getID().setValue(SDMsgUtils.getPropValueStr(pati,"codeIp"));
				pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IDCard");
				//PID_3 住院号
				pid.getPatientIdentifierList(1).getID().setValue(SDMsgUtils.getPropValueStr(pati,"codePi"));
				pid.getPatientIdentifierList(1).getIdentifierTypeCode().setValue("PatientNO");
				//PID_3 身份证号
				pid.getPatientIdentifierList(2).getID().setValue(SDMsgUtils.getPropValueStr(pati,"idno"));
				pid.getPatientIdentifierList(2).getIdentifierTypeCode().setValue("IdentifyNO");
				//PID_5 姓名
				pid.getPatientName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(pati,"namePi"));
				//PID_8 性别
				pid.getAdministrativeSex().setValue(SDMsgUtils.getPropValueStr(pati,"sex"));
				//【PV1-19】 就诊号码 his visit no
				pv1.getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(paramMap,"ipCode"));
				String sql2 ="select sum(dt.AMOUNT) as price ,TO_CHAR(dt.date_cg, 'yyyyMMdd') date_cg  from  BL_IP_DT DT left join pv_encounter pv on pv.pk_pv = dt.pk_pv  where pv.code_pv = '"+paramMap.get("ipCode")+"' and DT.del_flag = '0' and pv.del_flag = '0'  group by TO_CHAR(dt.date_cg, 'yyyyMMdd') order by date_cg desc ";
				List<Map<String, Object>> blTime = DataBaseHelper.queryForList(sql2);
				int blTiLen = blTime.size();
				for (int i = 0; i < blTiLen; i++) {
					ziv = zzl.getZIV(i);
					ziv.getGrjf().getQuantity().setValue(SDMsgUtils.getPropValueStr(blTime.get(i),"price"));
					ziv.getGhrq().setValue(SDMsgUtils.getPropValueStr(blTime.get(i),"dateCg"));
				}
			}
			msg = SDMsgUtils.getParser().encode(zzl);
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("ZZL--住院费每日账单详情(深大) 组装错误"+e);
		}
		return msg;
	}

	/**
	 * 医嘱消息OMP_O09(护嘱,会诊医嘱)
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public String  sendOrderInfoMsg(Map<String,Object> paramMap) {
		String msg = "";
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createOMP_O09Msg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 会诊申请消息发送（REF_I12）
	 * @param paramMap
	 * @return
	 */
	public String sendConsultMsg(Map<String,Object> paramMap){
		String msg = "";
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createREF_I12Msg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	/**
	 * 会诊应答消息发送（RRI_I12）
	 * @param paramMap
	 * @return
	 */
	public String sendConsultResponeMsg(Map<String,Object> paramMap){
		String msg = "";
		try {
			String msgId=SDMsgUtils.getMsgId();
			Message message = msgCreate.createRRI_I12Msg(msgId, paramMap);
			msg = SDMsgUtils.getParser().encode(message);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (HL7Exception e) {
			e.printStackTrace();
		}
		return msg;
	}
	
}
