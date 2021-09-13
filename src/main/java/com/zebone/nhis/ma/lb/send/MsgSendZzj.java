package com.zebone.nhis.ma.lb.send;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

























































import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.lb.vo.PvOpVo;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.model.ACK_P03;
import com.zebone.nhis.ma.pub.platform.zb.model.SRR_S04;
import com.zebone.nhis.ma.pub.platform.zb.model.ZPO;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendEx;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pv.pub.vo.PvOpAndSettleVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.web.support.ResponseJson;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.RAR_RAR;
import ca.uhn.hl7v2.model.v24.message.RSP_K21;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.MSA;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.QRD;
import ca.uhn.hl7v2.model.v24.segment.RXA;
import ca.uhn.hl7v2.model.v24.segment.SCH;
/**
 * 自助机信息处理
 * @author Administrator
 *
 */
@Service
public class MsgSendZzj {
	private static Logger log = org.slf4j.LoggerFactory.getLogger(MsgSendEx.class);
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Resource
	private	Hl7MsgHander msgHander;
	
	//数组转字符串的接受变量
	private String zstr;
	/**
	 * 患者首次建档反馈消息
	 * @param triggerEvent
	 * @param paramMap
	 * @param type
	 * @throws HL7Exception
	 */
	public String sendPati(String triggerEvent, Map<String, Object> paramMap,String type ,boolean success)throws HL7Exception{
		if (triggerEvent.equals("S01")) {
			SRR_S01 srr=new SRR_S01();
			String msgId=MsgUtils.getMsgId();
			MSH msh=srr.getMSH();		
			MsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
			msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
			MSA msa=srr.getMSA();
			MsgUtils.createMSAMsg(msa, msgId);
			if(success==false){
				msa.getAcknowledgementCode().setValue("AR");
				msa.getTextMessage().setValue("首次建档失败");
			}else{
				
				SCH sch=srr.getSCHEDULE().getSCH();
				sch.getFillerStatusCode().getIdentifier().setValue("0");
				
				NTE nte=srr.getSCHEDULE().getNTE(0);
				
				PID pid=srr.getSCHEDULE().getPATIENT(0).getPID();
				pid.getPatientID().getID().setValue(MsgUtils.getPropValueStr(paramMap, "codeOp"));
				pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(paramMap, "namePi"));
				
				if(MsgUtils.getPropValueStr(paramMap, "dtIdtype").equals("01")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IdentifyNO");
				}else if(MsgUtils.getPropValueStr(paramMap, "dtIdtype").equals("21")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
				}
				else if(MsgUtils.getPropValueStr(paramMap, "dtIdtype").equals("25")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("HealthCardNO");
				}
				pid.getPatientIdentifierList(0).getID().setValue(MsgUtils.getPropValueStr(paramMap, "idNo"));
				
				PV1 pv1=srr.getSCHEDULE().getPATIENT(0).getPV1();
				pv1.getPatientClass().setValue(type);
			}
			String msg = MsgUtils.getParser().encode(srr);
			return msg;
		}
		return "";
	}

	/*
	 * 挂号反馈消息
	 */
	public String sendRegister(String triggerEvent, List<Map<String, Object>> param, List<PvOpAndSettleVo> list, PiMasterParam pimast, String pkSch, boolean success) throws HL7Exception {
		if (triggerEvent.equals("S01")) {
			SRR_S01 srr=new SRR_S01();
			String msgId=MsgUtils.getMsgId();
			MSH msh=srr.getMSH();
			MsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
			msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
			MSA msa=srr.getMSA();
			MsgUtils.createMSAMsg(msa, msgId);
			if(success==false || list.get(0).getCodePv()==null){
				msa.getAcknowledgementCode().setValue("AR");
				msa.getTextMessage().setValue("挂号失败");
				
			}else{
				msa.getExpectedSequenceNumber().setValue("100");
				msa.getDelayedAcknowledgmentType().setValue("F");
				SCH sch=srr.getSCHEDULE().getSCH();
				sch.getFillerStatusCode().getIdentifier().setValue("0");
				NTE nte1=srr.getSCHEDULE().getNTE(0);
				
				nte1.getComment(0).setValue(String.valueOf(param.get(0).get("patientsPay")));
				nte1.getCommentType().getText().setValue("Zfzje");

				NTE nte2=srr.getSCHEDULE().getNTE(1);
				nte2.getComment(0).setValue(String.valueOf(param.get(0).get("medicarePayments")));
				nte2.getCommentType().getText().setValue("Sbzje");
				
				PID pid=srr.getSCHEDULE().getPATIENT(0).getPID();
				
				pid.getPatientID().getID().setValue(pimast.getMaster().getCodePi());
				pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(pimast.getMaster().getNamePi());
				if(pimast.getMaster().getDtIdtype().equals("01")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IdentifyNO");
				}
				if(pimast.getMaster().getDtIdtype().equals("21")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
				}
				if(pimast.getMaster().getDtIdtype().equals("25")){
					pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("HealthCardNO");
				}
				
				pid.getPatientIdentifierList(0).getID().setValue(pimast.getMaster().getIdNo());
				pid.getProductionClassCode().getIdentifier().setValue("1");
				pid.getProductionClassCode().getText().setValue("自费");
				
				//根据排班主键，查询科室以及科室代码
				Map<String, Object> deptParam = DataBaseHelper.queryForMap("select sr.code,sr.name from sch_resource sr left join sch_sch ss on ss.pk_schres=sr.pk_schres where ss.pk_sch=?", new Object[]{pkSch});
				PV1 pv1=srr.getSCHEDULE().getPATIENT(0).getPV1();
				pv1.getPatientClass().setValue("O");
				pv1.getVisitIndicator().setValue("01");
				pv1.getOtherHealthcareProvider(0).getIDNumber().setValue(list.get(0).getCodePv());
				if(deptParam != null){
					pv1.getAssignedPatientLocation().getLocationStatus().setValue(deptParam.get("name").toString());
					pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(deptParam.get("code").toString());
				}
			}
			String msg = MsgUtils.getParser().encode(srr);
			return msg;
		}
		return "";
	}
	
	/**
	 * 患者信息查询
	 * @param string
	 * @param piMasterList
	 * @return
	 */
	public String sendPatientInfo(String triggerEvent, List<Map<String, Object>> piMasterList) throws HL7Exception{
		if(piMasterList == null || piMasterList.size() == 0){
			RSP_K21 rsp = new RSP_K21();
			MSH msh = rsp.getMSH();
			String msgId=MsgUtils.getMsgId();
			MsgUtils.createMSHMsg(msh, msgId, "RSP", "K21");
			msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
			MSA msa = rsp.getMSA();
			MsgUtils.createMSAMsg(msa, msgId);
			msa.getAcknowledgementCode().setValue("AR");
			msa.getTextMessage().setValue("失败");
			
			String msg = MsgUtils.getParser().encode(rsp);
			return msg;
		}
		if (triggerEvent.equals("K21")) {
			String msg = null;
			try {
				RSP_K21 rsp = new RSP_K21();
				//MSH
				MSH msh = rsp.getMSH();
				String msgId=MsgUtils.getMsgId();
				MsgUtils.createMSHMsg(msh, msgId, "RSP", "K21");
				msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
				//MSA
				MSA msa = rsp.getMSA();
				MsgUtils.createMSAMsg(msa, msgId);
				msa.getTextMessage().setValue("成功");
				//QPD
				//QPD qpd = rsp.getQPD();
				String[] strs = new String[3];
				String dtIdtype = "";
				//消息查询名称
				strs[0] = "";
				//查询标识符
				strs[1] = "";
				//患者标识   ID号^^^^ID类型
				if(piMasterList.get(0).get("dtIdtype").equals("01")){
					dtIdtype="IdentifyNO";
				}
				String a =(String) piMasterList.get(0).get("idNo") + "^^^^" + dtIdtype;
				strs[2] = a;
				//qpd.parse(makeZxxString(strs,zstr));
				
				//PID
				PID pid=rsp.getQUERY_RESPONSE().getPID();
				
				MsgUtils.createPIDMsg(pid,piMasterList.get(0));
				
				msg = MsgUtils.getParser().encode(rsp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return msg;
		}
		return "";
	}
	
	/**
	 * 挂号支付反馈
	 * @param je 
	 * @param string
	 * @param hashMap
	 * @return
	 * @throws HL7Exception 
	 */
	public String sendRegisterSettle(String triggerEvent, String je, List<PibaseVo> opPiMaster) throws HL7Exception {
		ACK_P03 ack = new ACK_P03();
		//MSH
		MSH msh = ack.getMSH();
		String msgId=MsgUtils.getMsgId();
		MsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		//MSA
		MSA msa = ack.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		String msg = null;
		if (opPiMaster == null || opPiMaster.size()==0 || !triggerEvent.equals("P03")) {
			msa.getAcknowledgementCode().setValue("AR");
			msa.getTextMessage().setValue("支付失败");
			msg = MsgUtils.getParser().encode(ack);
			return msg;
		}else if(triggerEvent.equals("P03")){
			msa.getTextMessage().setValue("成功");
			//PID
			PID pid = ack.getPID();
		    pid.getPatientID().getID().setValue(opPiMaster.get(0).getCodePi());
		    if(opPiMaster.get(0).getDtIdtype().equals("01")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IdentifyNO");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("21")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("25")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("HealthCardNO");
		    }
			pid.getPatientIdentifierList(0).getID().setValue(opPiMaster.get(0).getIdNo());
			pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(opPiMaster.get(0).getNamePi());
			//PV1
			PV1 pv1 = ack.getPV1();
			pv1.getPatientClass().setValue("O");
			pv1.getAssignedPatientLocation().getLocationStatus().setValue(opPiMaster.get(0).getNameDept());
			pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(opPiMaster.get(0).getCodeDept());
			pv1.getVisitNumber().getID().setValue(opPiMaster.get(0).getCodePv());
			//NTE
			NTE nte1 = ack.getNTE(0);			
			nte1.getComment(0).setValue(je);
			nte1.getCommentType().getText().setValue("Qye");

			NTE nte2 = ack.getNTE(1);
			nte2.getComment(0).setValue("0");
			nte2.getCommentType().getText().setValue("Hye");
			msg = MsgUtils.getParser().encode(ack);
			return msg;	
		}
		return "";
	}
	
	/**
	 * 退号反馈
	 * @param string
	 * @return
	 * @throws Exception 
	 */
	public String sendCancleRegister(String triggerEvent) throws Exception {
		SRR_S04 srr = new SRR_S04();
		//MSH
		MSH msh = srr.getMSH();
		String msgId=MsgUtils.getMsgId();
		MsgUtils.createMSHMsg(msh, msgId, "SRR", "S04");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		//MSA
		MSA msa = srr.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		String msg = null;
		if(triggerEvent.equals("S04")){
			msa.getTextMessage().setValue("成功");
			msg = MsgUtils.getParser().encode(srr);
			return msg;	
		}else{
			msa.getAcknowledgementCode().setValue("AR");
			msa.getTextMessage().setValue("失败");
			msg = MsgUtils.getParser().encode(srr);
			return msg;	
		}
	}
	
	/**
	 * 患者代缴费消息反馈
	 * @param string
	 * @param opNoSettleRs
	 * @param opPiMaster 
	 * @return
	 * @throws Exception 
	 */
	public String sendPatiNotSettle(String triggerEvent,List<BlPatiCgInfoNotSettleVO> opNoSettleRs, List<PibaseVo> opPiMaster) throws Exception {
		RAR_RAR rar = new RAR_RAR();
		//MSH
		MSH msh = rar.getMSH();
		String msgId=MsgUtils.getMsgId();
		MsgUtils.createMSHMsg(msh, msgId, "RAR", "RAR");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		//MSA
		MSA msa = rar.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		//QRD
		QRD qrd = rar.getDEFINITION(0).getQRD();
		qrd.getQueryDateTime().getTs1_TimeOfAnEvent().setValue(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));
		qrd.getQueryFormatCode().setValue("T");
		qrd.getQueryPriority().setValue("I");
		qrd.getQueryID().setValue(opPiMaster.get(0).getCodePv());
		qrd.getQuantityLimitedRequest().getCq1_Quantity().setValue("500");
		qrd.getQuantityLimitedRequest().getCq2_Units().getCe1_Identifier().setValue("RD");
		qrd.getWhatSubjectFilter(0).getCe1_Identifier().setValue("BILL");
		String msg = null;
		if(triggerEvent.equals("RAR")){
			msa.getTextMessage().setValue("成功");
			//PID
			PID pid = rar.getDEFINITION(0).getPATIENT().getPID();
		    pid.getPatientID().getID().setValue(opPiMaster.get(0).getCodePi());
		    if(opPiMaster.get(0).getDtIdtype().equals("01")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IdentifyNO");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("21")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("25")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("HealthCardNO");
		    }
			pid.getPatientIdentifierList(0).getID().setValue(opPiMaster.get(0).getIdNo());
			pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(opPiMaster.get(0).getNamePi());
			
			//NTE
			//遍历收费项目，得到总金额
			Double amount = 0.0;
			for (BlPatiCgInfoNotSettleVO settle : opNoSettleRs) {
				amount = MathUtils.add(amount, settle.getAmount().doubleValue());
			}
			
			NTE nte1 = rar.getDEFINITION(0).getPATIENT().getNTE(0);
			nte1.getComment(0).setValue(String.valueOf(amount));
			nte1.getCommentType().getText().setValue("Zfzjr");
			NTE nte2 = rar.getDEFINITION(0).getPATIENT().getNTE(1);
			nte2.getComment(0).setValue("0");
			nte2.getCommentType().getText().setValue("Sbzje");
			ORC orc;
			RXA rxa;
			Double cfJe =0.0;//单个处方的金额
			//为了检查检验拆分多个处方
			for (int i = 0; i < opNoSettleRs.size(); i++) {
				if(opNoSettleRs.get(i).getPresNo() == null || opNoSettleRs.get(i).getPresNo().equals("")){
					opNoSettleRs.get(i).setPresNo(opNoSettleRs.get(i).getPkCnord());
				}
			}
			for (int i = 0; i < opNoSettleRs.size(); i++) {
				//一样的说明是检查检验的处方
				if(opNoSettleRs.get(i).getPresNo().equals(opNoSettleRs.get(i).getPkCnord())){
					orc = rar.getDEFINITION(0).getORDER(i).getORC();
					orc.getPlacerOrderNumber().getEntityIdentifier().setValue(opNoSettleRs.get(i).getPresNo());//处方号
					orc.getPlacerOrderNumber().getEi2_NamespaceID().setValue(opNoSettleRs.get(i).getDtPrestype());//处方类型
					orc.getEnteredBy(0).getXcn1_IDNumber().setValue(opNoSettleRs.get(i).getCodeEmp());//医生编码
					orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(opNoSettleRs.get(i).getNameOrd());//医生姓名
					orc.getEntererSLocation().getPl1_PointOfCare().setValue(opNoSettleRs.get(i).getCodeDept());//科室编码
					orc.getEntererSLocation().getPl2_Room().setValue(opNoSettleRs.get(i).getNameDept());//科室名称
					orc.getAdvancedBeneficiaryNoticeCode().getIdentifier().setValue(opNoSettleRs.get(i).getAmountPi().toString());//处方金额
					rxa = rar.getDEFINITION(0).getORDER(i).getRXA();
					rxa.getGiveSubIDCounter().setValue(String.valueOf(i));//序号
					rxa.getAdministrationSubIDCounter().setValue(opNoSettleRs.get(i).getAmountPi().toString());//药品金额
					rxa.getAdministeredCode().getCe1_Identifier().setValue(opNoSettleRs.get(i).getItemCode());//药品编码
					rxa.getAdministeredCode().getCe2_Text().setValue(opNoSettleRs.get(i).getNameCg());//药品名称
					rxa.getAdministeredAmount().setValue(String.valueOf(opNoSettleRs.get(i).getQuan()));//药品数量
					rxa.getAdministeredUnits().getCe1_Identifier().setValue(opNoSettleRs.get(i).getUnit());//药品单位
					rxa.getAdministeredUnits().getCe2_Text().setValue(opNoSettleRs.get(i).getSpec());//药品规格
					rxa.getAdministeredStrength().setValue(opNoSettleRs.get(i).getAmountPi().toEngineeringString());//药品价格
				}
				for (int j = i+1; j < opNoSettleRs.size(); j++) {
					cfJe = cfJe + opNoSettleRs.get(i).getAmount().doubleValue();
					if(opNoSettleRs.get(i).getPresNo().equals(opNoSettleRs.get(j).getPresNo())){
						cfJe = cfJe + opNoSettleRs.get(j).getAmount().doubleValue();
						orc = rar.getDEFINITION(0).getORDER(i).getORC();
						orc.getPlacerOrderNumber().getEntityIdentifier().setValue(opNoSettleRs.get(i).getPresNo());//处方号
						orc.getPlacerOrderNumber().getEi2_NamespaceID().setValue(opNoSettleRs.get(i).getDtPrestype());//处方类型
						orc.getEnteredBy(0).getXcn1_IDNumber().setValue(opNoSettleRs.get(i).getCodeEmp());//医生编码
						orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(opNoSettleRs.get(i).getNameOrd());//医生姓名
						orc.getEntererSLocation().getPl1_PointOfCare().setValue(opNoSettleRs.get(i).getCodeDept());//科室编码
						orc.getEntererSLocation().getPl2_Room().setValue(opNoSettleRs.get(i).getNameDept());//科室名称
						orc.getAdvancedBeneficiaryNoticeCode().getIdentifier().setValue(String.valueOf(cfJe));//处方金额
						
					}
					rxa = rar.getDEFINITION(0).getORDER(i).getRXA();
					rxa.getGiveSubIDCounter().setValue(String.valueOf(1));//序号
					rxa.getAdministrationSubIDCounter().setValue(opNoSettleRs.get(i).getAmountPi().toString());//药品金额
					rxa.getAdministeredCode().getCe1_Identifier().setValue(opNoSettleRs.get(i).getItemCode());//药品编码
					rxa.getAdministeredCode().getCe2_Text().setValue(opNoSettleRs.get(i).getNameCg());//药品名称
					rxa.getAdministeredAmount().setValue(String.valueOf(opNoSettleRs.get(i).getQuan()));//药品数量
					rxa.getAdministeredUnits().getCe1_Identifier().setValue(opNoSettleRs.get(i).getUnit());//药品单位
					rxa.getAdministeredUnits().getCe2_Text().setValue(opNoSettleRs.get(i).getSpec());//药品规格
					rxa.getAdministeredStrength().setValue(opNoSettleRs.get(i).getAmountPi().toString());//药品价格
				}
			}
			msg = MsgUtils.getParser().encode(rar);
			return msg;	
		}else{
			msa.getAcknowledgementCode().setValue("AR");
			msa.getTextMessage().setValue("失败");
			msg = MsgUtils.getParser().encode(rar);
			return msg;	
		}
	}
	
	/**
	 * 门诊其他费用支付反馈
	 * @param string
	 * @param opPiMaster 
	 * @param map 
	 * @return
	 * @throws Exception 
	 */
	public String sendOpSettleInfo(String triggerEvent, Map<String, Object> map, List<PibaseVo> opPiMaster) throws Exception {
		ACK_P03 ack = new ACK_P03();
		//MSH
		MSH msh = ack.getMSH();
		String msgId=MsgUtils.getMsgId();
		MsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		//MSA
		MSA msa = ack.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		String msg = null;
		if(triggerEvent.equals("P03") && map!=null){
			msa.getTextMessage().setValue("成功");
			//PID
			PID pid = ack.getPID();
		    pid.getPatientID().getID().setValue(opPiMaster.get(0).getCodePi());
		    if(opPiMaster.get(0).getDtIdtype().equals("01")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("IdentifyNO");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("21")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
		    }
		    if(opPiMaster.get(0).getDtIdtype().equals("25")){
		    	pid.getPatientIdentifierList(0).getIdentifierTypeCode().setValue("HealthCardNO");
		    }
			pid.getPatientIdentifierList(0).getID().setValue(opPiMaster.get(0).getIdNo());
			pid.getPatientName(0).getFamilyName().getFn1_Surname().setValue(opPiMaster.get(0).getNamePi());
			//PV1
			PV1 pv1 = ack.getPV1();
			pv1.getPatientClass().setValue("O");
			pv1.getAssignedPatientLocation().getLocationStatus().setValue(opPiMaster.get(0).getNameDept());
			pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue(opPiMaster.get(0).getCodeDept());
			pv1.getVisitNumber().getID().setValue(opPiMaster.get(0).getCodePv());
			//ZPO
			ZPO zpo = ack.getZPO();
			//遍历map得到金额
			Double je = 0.0;
			for(Entry<String, Object> entry : map.entrySet()){
				if(entry.getKey().equals("amountPi")){
					BigDecimal amountPi = new BigDecimal(entry.getValue().toString());
					je += je + amountPi.doubleValue();
				}
			}
			zpo.getMzhm().setValue(opPiMaster.get(0).getCodePv());//门诊流水号
			zpo.getMzrq().setValue(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));//门诊日期
			zpo.getBrxm().setValue(opPiMaster.get(0).getNamePi());//病人姓名
			zpo.getSflb().setValue("0");//收费类别
			zpo.getJzje().getQuantity().setValue(String.valueOf(je));//记账金额
			zpo.getGrjf().getQuantity().setValue(String.valueOf(je));//个人缴费
			zpo.getJsfs().setValue("3");//结算方式
			zpo.getSr().getQuantity().setValue("0");//舍入金额
			createFy(zpo,map);
			
			//NTE
			NTE nte1 = ack.getNTE(0);			
			nte1.getComment(0).setValue(String.valueOf(je));
			nte1.getCommentType().getText().setValue("Qye");

			NTE nte2 = ack.getNTE(1);
			nte2.getComment(0).setValue("0");
			nte2.getCommentType().getText().setValue("Hye");
			msg = MsgUtils.getParser().encode(ack);
			return msg;	
		}else{
			msa.getAcknowledgementCode().setValue("AR");
			msa.getTextMessage().setValue("支付成功，但消息发送失败");
			msg = MsgUtils.getParser().encode(ack);
			return msg;
		}

	}
	
	//得到费用
	private void createFy(ZPO zpo, Map<String, Object> map) throws HL7Exception {
		for(Entry<String, Object> entry : map.entrySet()){
			String amount = null;
			if(entry.getKey().equals("amountPi")){
				amount = entry.getValue().toString();
			}
			if(entry.getKey().equals("code")){
				if(entry.getValue().toString().equals("01")){//西药费
					zpo.getXyf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("02")){//成药费
					zpo.getZcy().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("03")){//草药费
					zpo.getZcay().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("05")){//手术费
					zpo.getSsf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("09")){//输氧费
					zpo.getSyf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("14")){//护理费
					zpo.getHlf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("18")){//其他费
					zpo.getQtf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("19")){//检查费
					zpo.getJcf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("20")){//CT费
					zpo.getCt().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("24")){//放射费
					zpo.getFsf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("28")){//治疗费
					zpo.getZlf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("31")){//诊查费
					zpo.getZcy().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("41")){//输血费
					zpo.getSxf().getQuantity().setValue(amount);
				}
				if(entry.getValue().toString().equals("42")){//药事服务费
					zpo.getTxfwf().getQuantity().setValue(amount);
				}
			}
		}
	}

	/**
	 * 拼接数据
	 * 
	 * @param strs
	 *            包含数据的String数组
	 * @param zstr
	 *            拼接后存放的String
	 * @return 返回Stirng
	 */
	private String makeZxxString(String[] strs, String zstr) {

		zstr = "";
		for (int k = 0; k < strs.length; k++) {
			String tmp = strs[k] == null ? "" : strs[k];
			if (k == 0) {
				zstr = "|" + tmp;
			} else {
				zstr = zstr + "|" + tmp;
			}
		}

		return zstr;
	}

	

	
	
}
