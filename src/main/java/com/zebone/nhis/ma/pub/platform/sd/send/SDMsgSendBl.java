package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Adt消息处理(灵璧复制版本)
 * @author chengjia
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgSendBl {

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDMsgSendAdt sDMsgSendAdt;
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDQueryUtils sDQueryUtils;

	/**
	 * 发送撤销/取消出院结算
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendADTA13Msg(String triggerEvent,Map<String,Object> paramMap){
		try{
		if("A13".equals(triggerEvent)){
			ADT_A01 adt01 = new ADT_A01();
			String msgId=SDMsgUtils.getMsgId();
			//出院结算
			MSH msh = adt01.getMSH();
			SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A13");

			EVN evn=adt01.getEVN();
			//[1]A13
			evn.getEventTypeCode().setValue("A13");
			//2 记录日期/时间
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//3 计划事件的日期/时间
			evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//4"01: 患者请求 02: 医生/医疗从业者医嘱  03:人口普查管理"
			evn.getEventReasonCode().setValue("01");
			//5 操作员编码^操作员名称
			evn.getOperatorID(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "doCode"));
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "doName"));
			//6	事件发生时间/操作时间
			evn.getEventOccurred().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//7	事件机构
			evn.getEventFacility().getHd1_NamespaceID().setValue("NHIS");

			PID pid = adt01.getPID();
			PV1 pv1 = adt01.getPV1();
			sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);

			String msg = SDMsgUtils.getParser().encode(adt01);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);

		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("A13组装错误"+e);
		}
	}

	/**
	 * 发送出院结算信息ADT^A03
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendADTA03Msg(String triggerEvent,Map<String,Object> paramMap){
		try{
		if("A03".equals(triggerEvent)){
			String msgId=SDMsgUtils.getMsgId();
			ADT_A03 adt03 = new ADT_A03();
			//出院结算
			MSH msh = adt03.getMSH();
			SDMsgUtils.createMSHMsg(msh, msgId, "ADT","A03");

			EVN evn=adt03.getEVN();
			//[1]A03
			evn.getEventTypeCode().setValue("A03");
			//[2]记录日期/时间
			evn.getRecordedDateTime().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//[3]计划事件的日期/时间
			evn.getDateTimePlannedEvent().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//[4]"01: 患者请求 02: 医生/医疗从业者医嘱  03:人口普查管理"
			evn.getEventReasonCode().setValue("01");
			//[5]操作员编码^操作员名称
			evn.getOperatorID(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(paramMap, "doCode"));
			evn.getOperatorID(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(paramMap, "doName"));
			//[6]事件发生时间/操作时间
			evn.getEventOccurred().getTimeOfAnEvent().setValue(sdf.format(new Date()));
			//[7]事件机构
			evn.getEventFacility().getHd1_NamespaceID().setValue("NHIS");

			PID pid = adt03.getPID();
			PV1 pv1 = adt03.getPV1();

			sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, paramMap);
			String msg = SDMsgUtils.getParser().encode(adt03);
			//发送消息
			sDHl7MsgHander.sendMsg(msgId, msg);
		}
		} catch (Exception e) {
			// TODO: handle exception
			loger.info("A03组装错误"+e);
		}
	}


	/**
	 * 发送Hl7消息（ORL）检验收费(护士)
	 * @param triggerEvent
	 * @param map
	 * @param ipOpType
	 */
	public void LbsendORLMsg(String triggerEvent, Map<String, Object> map,String ipOpType){
		try{
			if(map==null) {
				loger.info("LbsendORLMsg方法map为空");
			}
			if ("O22".equals(triggerEvent)) {
				// 检验申请确认(护士)
				ORL_O22 orl = new ORL_O22();
				MSH msh = orl.getMSH();
				MSA msa = orl.getMSA();
				PID pid = orl.getRESPONSE().getPATIENT().getPID();
				ORC orc = orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getORC();
				OBR obr=orl.getRESPONSE().getPATIENT().getGENERAL_ORDER().getORDER().getOBSERVATION_REQUEST().getOBR();
				String msgId = SDMsgUtils.getMsgId();
				SDMsgUtils.createMSHMsg(msh, msgId, "ORL", "O22");

				SDMsgUtils.createMSAMsg(msa, msgId);
				//PID
				Map<String,Object> param = new HashMap<>(16);
				param.put("pkPv", SDMsgUtils.getPropValueStr(map,"pkPv"));
				SDMsgUtils.createPIDMsg(pid, sDMsgSendAdt.qryPID_PV1(param));
				//记费/退费标志
				map.put("control", SDMsgUtils.getPropValueStr(map,"control"));
				//ORC消息段
				getOrcForRisAndLis(orc, map);
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
					obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
				}
				sDHl7MsgHander.sendMsg(msgId, SDMsgUtils.getParser().encode(orl));
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("LbsendORLMsg方法O22组装错误"+e);
		}
	}

	/**
	 * 发送Hl7消息（ORG）检查收费(住院)
	 * @param triggerEvent
	 * @param map
	 * @param ipOpType
	 */
	public void sendOGRGMsg(String triggerEvent, Map<String, Object> map,String ipOpType){
		try{
			if("O20".equals(triggerEvent)){
				// 检查申请确认(护士)
				ORG_O20 org = new ORG_O20();
				MSH msh = org.getMSH();
				PID pid = org.getRESPONSE().getPATIENT().getPID();
				MSA msa = org.getMSA();
				ORC orc = org.getRESPONSE().getORDER(0).getORC();
				OBR obr = org.getRESPONSE().getORDER(0).getOBR();
				String msgId = SDMsgUtils.getMsgId();
				SDMsgUtils.createMSHMsg(msh, msgId, "ORG", "O20");
				SDMsgUtils.createMSAMsg(msa, msgId);
				 Map<String,Object> param=new HashMap<>();
				 param.put("pkPv", map.get("pkPv"));
				SDMsgUtils.createPIDMsg(pid, sDMsgSendAdt.qryPID_PV1(param));
				//ORC
				getOrcForRisAndLis(orc, map);
				// 项目编号^医嘱码"
				String code = SDMsgUtils.getPropValueStr(map, "codeOrd");
				obr.getUniversalServiceIdentifier().getIdentifier().setValue(code);
				obr.getUniversalServiceIdentifier().getText().setValue(SDMsgUtils.getPropValueStr(map, "nameOrd"));
				//obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(map, "codeOrdtype"));
				Map<String, Object> dtOrdcate = DataBaseHelper.queryForMap("select dt_ordcate from bd_ord where code=?", code);
				obr.getUniversalServiceIdentifier().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(dtOrdcate, "dtOrdcate"));
				//相关的临床信息 描述
				obr.getRelevantClinicalInfo().setValue(SDMsgUtils.getPropValueStr(map, "descOrd"));

				obr.getChargeToPractice().getDollarAmount().getMo1_Quantity().setValue(SDMsgUtils.getPropValueStr(map, "priceCg"));
				if(!"".equals(map.get("pkDeptExec"))){
					// 执行科室
					Map<String,Object> deptMap=sDQueryUtils.queryDeptByPk(map.get("pkDeptExec").toString());
					obr.getDiagnosticServSectID().setValue(SDMsgUtils.getPropValueStr(deptMap, "codeDept"));
				}
				sDHl7MsgHander.sendMsg(msgId, SDMsgUtils.getParser().encode(org));
			}
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("sendOGRGMsg01方法O20组装错误"+e);
		}
	}

	/**
	 * 创建住院通用检查检验ORC消息段
	 * @param orc
	 * @param map
	 * @throws DataTypeException
	 */
	private ORC getOrcForRisAndLis(ORC orc, Map<String, Object> map){
		try{
			//需判断查询的结果是否为空
			map.putAll(sDMsgSendAdt.qryPID_PV1(map));

			//[1] 申请控制
			orc.getOrderControl().setValue(SDMsgUtils.getPropValueStr(map, "control"));
			//1门诊，2急诊，3住院，4体检，5家庭病床
			String euPvtype="1".equals(map.get("euPvtype"))?"O":"I";
			//【2】申请编码，检查发送医嘱号
			String code = SDMsgUtils.getPropValueStr(map, "codeApply");
			if(("ris").equals(SDMsgUtils.getPropValueStr(map, "fenlei"))){
				code = SDMsgUtils.getPropValueStr(map, "codeApply")+"H"+SDMsgUtils.getPropValueStr(map, "ordsn");
			}
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(code);
			//【2】就诊流水号 1门诊，2急诊，3住院，4体检，5家庭病床
			//门诊流水号/住院流水号
			orc.getPlacerOrderNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(map,"codePv" ));
			//【2】患者来源
			orc.getPlacerOrderNumber().getUniversalIDType().setValue(euPvtype);
			//[3]收费单号
			orc.getFillerOrderNumber().getEi1_EntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "pkCgip"));
			//[4] 医嘱号  深大取 ordsn
			//orc.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeOrd"));
			orc.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "ordsn"));
			//[5]申请单状态:0、项目内不收费；1、项目外单独收费
			orc.getOrderStatus().setValue("0");
			//[9]收费时间
			if(null != SDMsgUtils.getPropValueDate(map, "dateHap")){
				orc.getDateTimeOfTransaction().getTs1_TimeOfAnEvent().setValue(sdf.format(SDMsgUtils.getPropValueDate(map, "dateHap")));
			}else{
				orc.getDateTimeOfTransaction().getTs1_TimeOfAnEvent().setValue(sdf.format(new Date()));
			}
			//[10]输入者
			orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
			orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
			//[12]下单方提供者
			if(null != map.get("pkEmpApp") && ("").equals(map.get("pkEmpApp"))){
				Map<String,Object> empMap=sDQueryUtils.getUserCodeByPkUser(map.get("pkEmpApp").toString());
				orc.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(empMap, "code"));
				orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(SDMsgUtils.getPropValueStr(empMap, "name"));
			}
			//[17]录入科室//@todo换成科室编码或名称
			Map<String,Object> inDeptMap=sDQueryUtils.queryDeptByPk(UserContext.getUser().getPkDept());
			orc.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
			orc.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "nameDept"));
			//[21]患者科室//@todo换成科室编码或名称
			orc.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(inDeptMap, "codeDept"));
		} catch (Exception e) {
			e.printStackTrace();
			loger.info("创建住院/门诊通用检查检验ORC消息段"+e);
		}
		return orc;
	}

	/**
	 * 发送床位费信息
	 * @param paramMap
	 */

	public void sendBedCgInfo(String msgType, Map<String,Object> paramMap){
		if(paramMap==null) {
			return;
		}
		List<Map<String,Object>> resList=new ArrayList<>();
		try {
			List<BlIpDt> blPubList=(List<BlIpDt>) paramMap.get("ipDtList");
			if(blPubList==null) {
				return;
			}
			Set<String> pkCgips= new HashSet<>(16);
			for (BlIpDt blIpDt : blPubList) {
				if ("41".equals(blIpDt.getCodeBill())) {
					pkCgips.add(blIpDt.getPkCgip());
				}
			}
            if(pkCgips.size() == 0) {
				return ;
			}

			String blSql = "select dt.pk_pi,dt.pk_pv,dt.code_cg codecg,item.code codeitem,item.name nameitem,dt.quan,unit.name unit,dt.price," +
					" cate.name namecate ,dept.code_dept codedept,to_char(dt.date_hap,'yyyyMMdd') datehap,emp.code_emp codeemp,org.code_org codeorg " +
					" from bl_ip_dt dt inner join bd_itemcate cate on dt.pk_itemcate = cate.pk_itemcate  inner join bd_item item on item.pk_item=dt.pk_item " +
					" inner join bd_unit unit on unit.pk_unit=dt.pk_unit inner join bd_ou_dept dept on dept.pk_dept=dt.pk_dept_cg " +
					" left join bd_ou_employee emp on emp.pk_emp=dt.pk_emp_cg left join bd_ou_org org on org.pk_org=dt.pk_org " +
					" where   dt.code_bill = '41' and dt.pk_cgip in (" + CommonUtils.convertSetToSqlInPart(pkCgips, "pk_cgip") + ")  ";
			resList=DataBaseHelper.queryForList(blSql);
			if(resList==null ||resList.size()==0) {
				return;
			}
			for (Map<String, Object> resMap : resList) {
				RAS_O17 ras = new RAS_O17();
				MSH msh = ras.getMSH();
				String msgId = SDMsgUtils.getMsgId();
				SDMsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");

				PID pid = ras.getPATIENT().getPID();
				PV1 pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
				Map<String, Object> param = new HashMap<>();
				param.put("pkPv", resMap.get("pkPv"));
				param.put("pkPi", resMap.get("pkPi"));
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);

				ORC orc = ras.getORDER(0).getORC();
				//申请控制
				orc.getOrderControl().setValue("OK");
				//明细编码
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeitem"));
				//申请单号
				orc.getFillerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codecg"));
				orc.getPlacerGroupNumber().getUniversalID().setValue(SDMsgUtils.getPropValueStr(resMap, "codecg"));
				//事务发生时间
				orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));
				//下单方提供者
				orc.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(resMap, "codeemp"));
				//输入者所属组织
				orc.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeorg"));
				//申请机构名称
				orc.getOrderingFacilityName(0).getOrganizationName().setValue(SDMsgUtils.getPropValueStr(resMap, "codeorg"));


				RXO rxo = ras.getORDER(0).getORDER_DETAIL().getRXO();
				rxo.getRequestedGiveCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codecg"));
				rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(resMap, "codeitem"));
				rxo.getRequestedGiveAmountMinimum().setValue(SDMsgUtils.getPropValueStr(resMap, "quan"));
				rxo.getRequestedGiveUnits().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "unit"));
				rxo.getDeliverToLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(resMap, "codedept"));
				rxo.getSupplementaryCode(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "namecate"));
				rxo.getSupplementaryCode(0).getText().setValue(SDMsgUtils.getPropValueStr(resMap, "codeitem"));

				RXA rxa = ras.getORDER(0).getRXA();
				rxa.getGiveSubIDCounter().setValue("0");
				String dateHap = SDMsgUtils.getPropValueStr(resMap, "datehap");

				rxa.getDateTimeStartOfAdministration().getTimeOfAnEvent().setValue(dateHap + "000000");
				rxa.getDateTimeEndOfAdministration().getTimeOfAnEvent().setValue(dateHap + "235959");
				rxa.getAdministeredAtLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(resMap, "codeorg"));
				rxa.getCompletionStatus().setValue("2");
				rxa.getActionCodeRXA().setValue("A");
				rxa.getSystemEntryDateTime().getTimeOfAnEvent().setValue(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));

				NTE nte = ras.getORDER(0).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
				//单价
				nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(resMap, "price"));
				String msg = SDMsgUtils.getParser().encode(ras);
				sDHl7MsgHander.sendMsg(msgId, msg);
			}
		} catch (Exception e) {
			loger.info("构建床位费信息时发生异常：\n"+e.getMessage()+"\n请求入口解析参数为：\n"+JsonUtil.writeValueAsString(resList));
		}

	}

	/**
	 * 高值耗材
	 * @param msgType
	 * @param paramMap
	 */
	public void sendHighValueConSumIp(String msgType,Map<String,Object> paramMap){
		List<Map<String,Object>> resList;
		if("OP".equals(paramMap.get("type"))){
			List<String> pkcgOpList=(List<String>) paramMap.get("pkcgOpList");
			resList=sDMsgMapper.queryHighvaluOpcg(pkcgOpList);
		}else{
			List<String> pkcgIpList=(List<String>) paramMap.get("pkcgIpList");
			resList=sDMsgMapper.queryHighvaluIpcg(pkcgIpList);
		}
		if(resList==null ||resList.size()==0) {
			return;
		}
		try {
			for (Map<String, Object> resMap : resList) {
				RAS_O17 ras = new RAS_O17();
				MSH msh = ras.getMSH();
				String msgId = SDMsgUtils.getMsgId();
				SDMsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");

				PID pid = ras.getPATIENT().getPID();
				PV1 pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
				Map<String, Object> param = new HashMap<>();
				param.put("pkPv", resMap.get("pkPv"));
				param.put("pkPi", resMap.get("pkPi"));
				sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);

				ORC orc = ras.getORDER(0).getORC();
				//申请控制
				orc.getOrderControl().setValue(msgType);
				//明细编码
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "pkCgip"));
				//申请单号
				orc.getFillerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeCg"));
				//申请单号
				orc.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeCg"));
				//事务发生时间
				orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(DateUtils.dateToStr("yyyyMMddHHmmss", new Date()));
				//当前操作人工号
				orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
				//下单方提供者-计费人工号
				orc.getOrderingProvider(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(resMap, "codeEmpCg"));
				//计费科室
				orc.getEnteringOrganization().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeDeptCg"));
				orc.getEnteringOrganization().getText().setValue(SDMsgUtils.getPropValueStr(resMap, "nameDeptCg"));
				orc.getAdvancedBeneficiaryNoticeCode().getAlternateIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "pkCgipBack"));


				RXO rxo = ras.getORDER(0).getORDER_DETAIL().getRXO();
				rxo.getRequestedGiveCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "codeItem"));
				rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(resMap, "barcode"));
				rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(resMap, "nameItem"));
				rxo.getRequestedGiveCode().getAlternateIdentifier().setValue("m");
				rxo.getRequestedGiveAmountMinimum().setValue(SDMsgUtils.getPropValueStr(resMap, "quanCg"));
				rxo.getRequestedGiveUnits().getIdentifier().setValue(SDMsgUtils.getPropValueStr(resMap, "unitCg"));


				NTE nte = ras.getORDER(0).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
				//单价
				nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(resMap, "price"));
				String msg = SDMsgUtils.getParser().encode(ras);

				sDHl7MsgHander.sendMsg(msgId, msg);
			}
		} catch (Exception e) {
			loger.info("构建高值耗材费用信息时发生异常：\n"+e.getMessage()+"\n请求入口解析参数为：\n"+JsonUtil.writeValueAsString(resList));
		}
	}


}
