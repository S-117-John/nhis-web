package com.zebone.nhis.ma.pub.platform.sd.receive;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.ACK;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgParseUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


/**
 * hl7接收和解析
 * @author maijiaxing
 *
 */
@Service
public class SDMsgReceive {
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Autowired
	private SDMsgService sDMsgService;
	/**
	 * 接收消息不处理业务反馈
	 * @param msgRec
	 * @return
	 * @throws HL7Exception
	 */
	public String handleSysMsgRec(SysMsgRec msgRec) throws HL7Exception{
		MSH msh = null;
		String ackMsg =  "";
		Map<String, Object> mshMap = new HashMap<>(16);
		Parser parser = new GenericParser();
		try {
			String msg = msgRec.getMsgContent();
			Message hapiMsg = parser.parse(msg);
			if (hapiMsg == null) {
				throw new HL7Exception("receive a null message");
			}
			mshMap = MsgParseUtils.getMSH(hapiMsg);
			msh = (MSH) mshMap.get("MSH");
			String msgType = SDMsgUtils.getPropValueStr(mshMap, "msgType");
			// SIU^S12 ORR^O02 ORM^O01 ORL^O22 OUL^R21 OMG^O19 ORG^O20 ORU^R01 为灵璧版本
			switch (msgType) {
				// 手术安排
				case "SIU^S12":sDMsgService.receiveSIUS12(hapiMsg);break;
				// 手术室撤销手术(住院)
				case "ORM^O01":sDMsgService.receiveORMO01(hapiMsg);break;
				// 手术执行确认(住院)
				case "ORR^O02":sDMsgService.receiveORRO02(hapiMsg);break;
				// 发布检验结果(住院)
				case "OUL^R21":sDMsgService.receiveOULR21(hapiMsg);break;
				// 发布检查报告(住院)
				case "ORU^R01":sDMsgService.receiveORUR01(hapiMsg);break;
				// 检查退费(执行确认取消)和门诊体检检查申请
				case "OMG^O19":sDMsgService.receiveOMGO19(hapiMsg);break;
				// 检查收费(执行确认)
				case "ORG^O20":sDMsgService.receiveORGO20(hapiMsg);break;
				// 确认检验记费(住院)
				case "ORL^O22":sDMsgService.receiveORLO22(hapiMsg);break;
				// 住院用血计费 --包药袋计费
				case "RAS^O17":sDMsgService.receiveRASO17(hapiMsg);break;
				//自定义医嘱执行
				case "ZAS^O17":sDMsgService.receiveZASO17(hapiMsg);break;
				//case "ZAS^O17":sDMsgService.receiveZASO17New(hapiMsg);break;//自定义医嘱执行
				// 接收字典
				case "MFN^M01":sDMsgService.receiveMFNM01(hapiMsg);break;
				//住院病人护理评估
				case "ZPI^PSI":sDMsgService.receiveZPIPSI(hapiMsg);break;
				//诊断
				case "ADT^A31":sDMsgService.receiveADTA31(hapiMsg);break;
				//入院通知单
				case "ADT^A06":sDMsgService.receiveADTA06(hapiMsg);break;
				// case:"MFN^M01":sDMsgService.receiveMFN_M01(hapiMsg);break;//科室字典MFN^M01(Z01)
				// 人员字典 MFN^M01(Z02)人员简历字典 MFN^M01(Z19)物资字典 MFN^M01(ZE0)
				//门诊 //体检检验申请
				case "OML^O21":sDMsgService.receiveOMLO21(hapiMsg);break;
				//取消挂号
				case "ADT^A11":sDMsgService.receiveADTA11(hapiMsg);break;
				//毒麻药柜计费录入
				case "QRY^P04":sDMsgService.receiveQRYP04(hapiMsg);break;
				//case "OMG^O19":break;门诊体检检查申请住院合并
				default:log.info("接收的未作处理的消息类型 msgType:" + SDMsgUtils.getPropValueStr(mshMap, "msgType"));break;
			}
		} catch (Exception e) {
			String triggerEvent = msh.getMessageType().getTriggerEvent().getValue();
			ACK ack = new ACK();
			msh = ack.getMSH();
			String msgId = msgRec.getMsgId();
			SDMsgUtils.createMSHMsg(msh, msgId,"ACK", triggerEvent);
			msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(mshMap, "receive"));
			MSA msa=ack.getMSA();
			//接受
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(msgId);
			msa.getTextMessage().setValue(e.toString()+e.getLocalizedMessage());
			ackMsg = parser.encode(ack);
			return ackMsg;
		}
		return ackMsg;
	}

	/**
	 * 需要处理数据在发送消息
	 * @param msg
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException 
	 */
	public String sendFeedbackMsg(Message msg, String logPrefix) throws HL7Exception, ParseException{
		String msgMsg = null;
		Map<String, Object> mshMap = MsgParseUtils.getMSH(msg);
		String msgType = SDMsgUtils.getPropValueStr(mshMap, "msgType");
		switch (msgType){
			// 住院：查询住院信息列表(Theme)
			case "QBP^ZDI":msgMsg = sDMsgService.receiveQBPZDI(msg);break;
			// 住院：查询押金缴入状态 QBP^ZYJ
			case "QBP^ZYJ":msgMsg = sDMsgService.receiveQBPZYJ(msg);break;
			// 住院：查询患者信息
			case "QBP^Q21":msgMsg = sDMsgService.receiveQBPQ21(msg);break;
			// 住院：押金缴入(住院) 接收 DFT^P03
			case "DFT^P03":msgMsg = sDMsgService.receiveDFTP03(msg, logPrefix);break;
			// 住院费每日账单详情明细(Details) 住院费每日账单列表(Theme)
			case "QBP^ZZL":msgMsg = sDMsgService.receiveQBPZZL(msg);break;
			// 查询住院缴费记录
			case "SQM^ZQ1":msgMsg = sDMsgService.receiveSQMZQ1(msg);break;
			// 查询费用类别统计(TotalExpenses)QBP^ZPI 已缴费发票列表查询(挂号 APPR)已缴费发票列表查询(门诊 APPT)
			// 门诊
			case "QBP^ZPI":msgMsg = sDMsgService.receiveQBPZPI(msg);break;
			// 患者建档 +挂号
			case "SRM^S01":msgMsg = sDMsgService.receiveSRMS01(msg);break;
			// 已缴费记录明细查询
			case "QBP^ZDL":msgMsg = sDMsgService.receiveQBPZDL(msg);break;
			// 取消预约挂号
			case "SRM^S04":msgMsg = sDMsgService.receiveSRMS04(msg);break;
			// 查询号源
			case "ZHY^ZRN":msgMsg = sDMsgService.receiveZHYZRN(msg);break;
			// 待支付列表查询
			case "QRY^Q27":msgMsg = sDMsgService.receiveQRYQ27(msg);break;
			default:log.info("接收的未作处理的消息类型 msgType:" + SDMsgUtils.getPropValueStr(mshMap, "msgType"));break;
		}
		return msgMsg;
	}

}
