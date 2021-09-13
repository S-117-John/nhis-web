package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.ORG_O20;
import ca.uhn.hl7v2.model.v24.message.ORL_O22;
import ca.uhn.hl7v2.model.v24.message.ORP_O10;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.PMI;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZAM_ZRB;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZFP;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZPI_PMI;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 深大门诊收费结算消息整合
 * 此类消息构建内容包括：结算，收费消息构建
 * @author jd_em
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgSendOpBl {

	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private SDOpMsgMapper opMsgMapper;

	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.split.num", "10"));


	/**
	 * 发送Hl7消息（ORG）检查收费(针对门诊方法重写)
	 * @param triggerEvent
	 * @param map
	 * @param ipOpType
	 */
		public void sendOGRGMsgOut(String triggerEvent, Map<String, Object> map,String ipOpType){
			try{
				if("O20".equals(triggerEvent)){
					// 检查申请确认(护士)
					String msgId = "";
					String msg = "";
					msgId = SDMsgUtils.getMsgId();
					ORG_O20 org = new ORG_O20();
					MSH msh = org.getMSH();
					PID pid = org.getRESPONSE().getPATIENT().getPID();
					MSA msa = org.getMSA();
					ORC orc = org.getRESPONSE().getORDER(0).getORC();
					OBR obr = org.getRESPONSE().getORDER(0).getOBR();
					map.put("msgid",msgId);
					map.put("msgtype", "ORG");
					map.put("triggerevent", "O20");
					createOpMsg.createMSHMsg(msh, map);
					createOpMsg.createMSAMsg(msa, map);
					Map<String, Object> pidMap=createOpMsg.qrySdOpPID_PV1(map);
					createOpMsg.createPIDMsg(pid, pidMap);
					createOpMsg.createORCMsg(orc, map);
					// 项目编号^医嘱码"
					String code = SDMsgUtils.getPropValueStr(map, "codeOrd");
					//Map<String, Object> dtOrdcate = DataBaseHelper.queryForMap("select dt_ordcate from bd_ord where code=?", code);
					//obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(map, "codeOrdtype"));
					obr.getUniversalServiceIdentifier().getIdentifier().setValue(code);
					obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(map, "nameOrd"));
					obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(map, "dtOrdcate"));
					//相关的临床信息 描述
					obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "descOrd"));
					// 金额 23
					obr.getObr23_ChargeToPractice().getMoc1_DollarAmount().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(map, "priceCg"));
					if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(map,"pkDeptExec"))){
						// 执行科室
						Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(map.get("pkDeptExec").toString());
						obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
					}
					msg = SDMsgUtils.getParser().encode(org);
					sDHl7MsgHander.sendMsg(msgId, msg);

				}
			} catch (Exception e) {
				// TODO: handle exception
				loger.info("sendOGRGMsg01方法O20组装错误"+e);
			}
		}


		/**
		 * 门诊处方收费
		 * @param triggerEvent 区分消息信息
		 * @param listMap 拼装的数据
		 * @param typeStatus 当前的状态【ADD,UPDATE,DEL】
		 * @throws HL7Exception
		 */
		public void sendORPMsg(String triggerEvent,List<Map<String, Object>> listMap,String typeStatus){
			try{
			if("O10".equals(triggerEvent)){
				if(listMap!=null){
				int i;
				String msg = "";
				int index = 0 ;
				int mode = 0;
				String msgId=SDMsgUtils.getMsgId();
				ORP_O10 orp=new ORP_O10();
				MSH msh = orp.getMSH();
				MSA msa	= orp.getMSA();
				PID pid = orp.getRESPONSE().getPATIENT().getPID();
				ORC orc;
				Map<String, Object> map =listMap.get(0);
				map.put("msgid",msgId);
				map.put("msgtype", "ORP");
				map.put("triggerevent", triggerEvent);
				createOpMsg.createMSHMsg(msh, map);
				createOpMsg.createMSAMsg(msa, map);
				Map<String, Object> pidMap=createOpMsg.qrySdOpPID_PV1(map);
				createOpMsg.createPIDMsg(pid, pidMap);

				for (i = 0; i < listMap.size(); i++) {
					Map<String, Object> mapOrc=listMap.get(i);
					String codeOrd = SDMsgUtils.getPropValueStr(mapOrc, "codeOrdType");
					if(!codeOrd.startsWith("02")&&!codeOrd.startsWith("03")){
						orc=orp.getRESPONSE().getORDER(index).getORC();
						mapOrc.put("control", typeStatus);
						mapOrc.put("codeOrdtype", codeOrd);
						createOpMsg.createORCMsg(orc, mapOrc);
						String sql="select flag_durg from cn_order where pk_cnord = ?";
				    	Map<String,Object> flagDurgMap=DataBaseHelper.queryForMap(sql, SDMsgUtils.getPropValueStr(mapOrc, "pkCnord"));
				    	if("0".equals(SDMsgUtils.getPropValueStr(flagDurgMap, "flagDurg").trim())) {
							orp.getRESPONSE().getORDER(index).getORDER_DETAIL().getRXO().getRequestedGiveCode().getNameOfCodingSystem().setValue("n");
						} else{
							orp.getRESPONSE().getORDER(index).getORDER_DETAIL().getRXO().getRequestedGiveCode().getNameOfCodingSystem().setValue("d");
						}

					}else{
						continue;
					}
					if(mode!=0 && i%splitNum==0){
						msg=SDMsgUtils.getParser().encode(orp);
						sDHl7MsgHander.sendMsg(msgId, msg);
						orp=new ORP_O10();
						msh=orp.getMSH();
						msa=orp.getMSA();
						pid=orp.getRESPONSE().getPATIENT().getPID();
						createOpMsg.createMSHMsg(msh, map);
						createOpMsg.createMSAMsg(msa, map);
						createOpMsg.createPIDMsg(pid, pidMap);
						mode = 0;
						index = 0;
					}else{
						index ++;
						mode=i%splitNum;
					}
				}
				if (mode != 0 || listMap.size() == 1) {
					msg = SDMsgUtils.getParser().encode(orp);
					sDHl7MsgHander.sendMsg(msgId, msg);
				}
			}}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				loger.info("sendORPMsg方法O10组装错误"+e);
			}
		}


	/**
	 * 发送Hl7消息（ORL）检验收费(护士)
	 * @param triggerEvent
	 * @param map
	 * @param ipOpType
	 */
		public void sendORLMsgOut(String triggerEvent, Map<String, Object> map,String ipOpType){
			try{
				if(map==null) {
					loger.info("sendORLMsgOut方法map为空");
				}
				String msg = "";
				if (triggerEvent.equals("O22")) {

					String msgId = SDMsgUtils.getMsgId();
					// 检验申请确认(护士)
					ORL_O22 orl = new ORL_O22();
					MSH msh = orl.getMSH();
					MSA msa = orl.getMSA();
					PID pid = orl.getRESPONSE().getPATIENT().getPID();
					ORC orc = orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getORC();
					OBR obr = orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getOBSERVATION_REQUEST().getOBR();
					Map<String, Object> pidMap=createOpMsg.qrySdOpPID_PV1(map);
					map.put("msgid",msgId);
					map.put("msgtype", "ORL");
					map.put("triggerevent", triggerEvent);
					createOpMsg.createMSHMsg(msh, map);
					createOpMsg.createMSAMsg(msa, map);
					createOpMsg.createPIDMsg(pid, pidMap);
					//记费/退费标志
					map.put("control", SDMsgUtils.getPropValueStr(map,"control"));
					createOpMsg.createORCMsg(orc, map);

					// 项目编号^医嘱名称"
					obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeOrd"));
					obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(map, "nameOrd"));
					//相关的临床信息 描述
					obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "descOrd"));
					//样本类型
					obr.getSpecimenSource().getSpecimenSourceNameOrCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeOrdtype"));
					//费用信息
					obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(map, "priceCg"));
					// 执行科室
					Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(map.get("pkDeptExec")==null? "0306":map.get("pkDeptExec").toString());
					if(deptMap==null){
						obr.getDiagnosticServSectID().setValue("0306");
					}else{
					obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));}

					msg = SDMsgUtils.getParser().encode(orl);
					sDHl7MsgHander.sendMsg(msgId, msg);
				}
			} catch (Exception e) {
				// TODO: handle exception
				loger.info("LbsendORLMsg方法O22组装错误"+e);
			}
		}

		/**
		 * 退费消息(ZAM^ZRB) (入口已经注释)
		 * @param paramMap
		 * @throws HL7Exception
		 */
		public void sendRefundMsg(Map<String, Object> paramMap) throws HL7Exception {
			ZAM_ZRB zam_zrb = new ZAM_ZRB();
			String msgId = SDMsgUtils.getMsgId();
			paramMap.put("msgid",msgId);
			paramMap.put("msgtype", "ZAM");
			paramMap.put("triggerevent", "ZRB");
			Map<String, Object> pidMap = createOpMsg.qrySdOpPID_PV1(paramMap);
			createOpMsg.createMSHMsg(zam_zrb.getMSH(), paramMap);
			createOpMsg.createPIDMsg(zam_zrb.getPID(), pidMap);
			createOpMsg.createPV1Msg(zam_zrb.getPV1(), pidMap);
			//退费信息
			List<Map<String, Object>> list = (List<Map<String, Object>>) paramMap.get("list");
			if(list!=null && list.size()>0){
				int i=0;
				for(Map<String, Object> map:list){
					createOpMsg.createZPMMsg(zam_zrb.getZPM(i++),map);
				}
			}
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, SDMsgUtils.getParser().encode(zam_zrb));
		}

		/**
		 * 发票信息字典
		 * @param paramMap
		 * @throws HL7Exception
		 */
		public void sendBlInvioceDict(Map<String,Object> paramMap)throws HL7Exception{
			String pkSettle=SDMsgUtils.getPropValueStr(paramMap, "pkSettle");
			if(CommonUtils.isNull(pkSettle)) {
				return ;
			}

			String msgId=SDMsgUtils.getMsgId();
			paramMap.put("msgid", msgId);
			paramMap.put("msgtype", "ZPI");
			paramMap.put("triggerevent", "PMI");
			ZPI_PMI zpi = new ZPI_PMI();
			MSH msh = zpi.getMSH();
			createOpMsg.createMSHMsg(msh, paramMap);
			Map<String, Object> pMap=createOpMsg.qrySdOpPID_PV1(paramMap);
			PID pid = zpi.getPID();
			PV1 pv1 = zpi.getPV1();
			createOpMsg.createMSHMsg(msh, paramMap);
			createOpMsg.createPIDMsg(pid, pMap);
			createOpMsg.createPV1Msg(pv1, pMap);

			Map<String,Object> settleMap=opMsgMapper.qrySettleForInvData(pkSettle);
			if(settleMap==null){
				loger.info("门诊发票消息ZPI_PMI：未获取结算发票记录，无法构建！");
				return;
			}
			FT1 ft1 = zpi.getFT1();
			createFT1Msg(ft1, settleMap);
			ZFP zfp = zpi.getZFP();
			createZFPMsg(zfp, settleMap);

			List<Map<String,Object>> invDtDataList=opMsgMapper.qryInvDtData(pkSettle);
			if(invDtDataList==null ||invDtDataList.size()==0){
				loger.info("门诊发票消息ZPI_PMI：未获取结算发票明细记录，无法构建PMI数据！");
				return;
			}
			for (int i = 0; i < invDtDataList.size(); i++) {
				PMI pmi=zpi.getPMI(i);
				Map<String,Object> invDtData=invDtDataList.get(i);
				createPMIMsg(pmi, invDtData);
			}

			List<Map<String,Object>> opdtDataList=opMsgMapper.qryOpDtData(pkSettle);
			if(opdtDataList==null ||opdtDataList.size()==0){
				loger.info("门诊发票消息ZPI_PMI：未获取结算费用明细记录，无法构建OBR数据！");
				return;
			}

			for (int i = 0; i < opdtDataList.size(); i++) {
				OBR obr=zpi.getOBR(i);
				Map<String,Object> opdtData=opdtDataList.get(i);
				createOBRMsg(obr, opdtData);
			}
			String msgTxt= SDMsgUtils.getParser().encode(zpi);
			sDHl7MsgHander.sendMsg(msgId,msgTxt);
		}


		/**
		 * 创建门诊发票 -FT1消息
		 * @param ft1
		 * @param paramMap
		 * @return
		 * @throws DataTypeException
		 */
		private FT1 createFT1Msg(FT1 ft1,Map<String, Object> paramMap) throws DataTypeException{
			//ft1-1  //发生序号
			ft1.getSetIDFT1().setValue("1");
			//ft1-2
			ft1.getTransactionID().setValue(SDMsgUtils.getPropValueStr(paramMap,"codeInv"));
			//ft1-4 有问题		//收取时间（事务时间）
			ft1.getTransactionDate().getTimeOfAnEvent().setValue(DateUtils.getDate("yyyyMMddHHmmss"));
			//ft1-6	 结算类别（请参照患者结算类别） 01：自费 02：医保 03：公费"
			String euHptype=SDMsgUtils.getPropValueStr(paramMap, "euHptype");
			String transType="01";
			if("3".equals(euHptype)){//公费
				transType="03";
			}else if("0".equals(euHptype)){//自费
				transType="01";
			}else{//医保
				transType="02";
			}
			ft1.getTransactionType().setValue(transType);
			//FT1-8：结算方式(1：门诊结算，2：住院中途结算，3：出院结算，4：门诊挂号)：EU_DPTYPE
			ft1.getTransactionDescription().setValue("1");
			//ft1-11.3 总金额
			ft1.getTransactionAmountExtended().getFromValue().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountSt"));

			//15.3 保险总金额
			ft1.getInsuranceAmount().getFromValue().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountInsu"));
			//22.3自费金额
			ft1.getUnitCost().getFromValue().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPi"));
			return ft1;
		}

		/**
		 * 创建ZFP消息
		 * @param zfp
		 * @param paramMap
		 * @throws DataTypeException
		 */
		private ZFP createZFPMsg(ZFP zfp, Map<String, Object> paramMap) throws DataTypeException {
			//ZFP-1	Fph	  发票号
			//1.1发票号
			zfp.getFph().getCe1_Identifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeInv"));
			//ZFP-2	Ybje 医保金额 amtInsu
			zfp.getYbje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountInsu"));
			//ZFP-3	Grjf 个人缴费  amountSelf
			zfp.getGrjf().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPi"));
			//ZFP-5	Sr	  舍入金额
			zfp.getSr().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountRound"));
			//ZFP-6	Zje	 总金额  amountTotal
			zfp.getZje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountSt"));
			//ZFP-7	sfybm 收费员编码  doCode
			zfp.getSfybm().setValue(UserContext.getUser().getCodeEmp());
			//ZFP-8	Sfy	 收费员姓名  doName
			zfp.getSfy().setValue(UserContext.getUser().getNameEmp());
			//ZFP-9	Jssj 结算时间
			zfp.getJssj().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateSt"));
			//ZFP-10 Fpbz 发票标识  发票标识(1：正常，2：重打，3：退费)
			zfp.getFpbz().setValue("1");
			return zfp;
		}

		/**
		 * 创建PMI消息
		 * @param pmi
		 * @param paramMap
		 * @throws DataTypeException
		 */
		private PMI createPMIMsg(PMI pmi, Map<String, Object> paramMap) throws DataTypeException {
			//PMI-1  Fpxbm 发票项编码
			pmi.getFpxbm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeBill"));
			//PMI-2 Fpx 发票项目
			pmi.getFpx().setValue(SDMsgUtils.getPropValueStr(paramMap, "name"));
			//pmi-3单项金额
			pmi.getJe().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
			return pmi;
		}

		/**
		 * 门诊发票OBR数据构建
		 * @param obr
		 * @param paramMap
		 * @return
		 * @throws DataTypeException
		 */
		private OBR createOBRMsg(OBR obr,Map<String,Object> paramMap)throws DataTypeException{
			//obr-1 项目编码
			obr.getSetIDOBR().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeCg"));
			//obr-3 单价
			obr.getFillerOrderNumber().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "price"));
			//OBR-4.1项目编号
			obr.getUniversalServiceIdentifier().getIdentifier().setValue(SDMsgUtils.getPropValueStr(paramMap, "code"));
			//OBR-4.2 项目名称
			obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(paramMap, "name"));
			//obr-9 数量
			obr.getCollectionVolume().getCq1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "quan"));
			//obr-11 单位
			obr.getSpecimenActionCode().setValue(SDMsgUtils.getPropValueStr(paramMap, "unit"));
			//obr-23 金额
			obr.getChargeToPractice().getMoc1_DollarAmount().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(paramMap, "amount"));
			//obr-24 执行科室
			obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
			return obr;
		}
}
