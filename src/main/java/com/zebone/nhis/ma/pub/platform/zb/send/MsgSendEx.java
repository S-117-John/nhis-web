package com.zebone.nhis.ma.pub.platform.zb.send;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.message.RAS_O17;
import ca.uhn.hl7v2.model.v24.segment.DG1;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.NTE;
import ca.uhn.hl7v2.model.v24.segment.ODT;
import ca.uhn.hl7v2.model.v24.segment.ORC;
import ca.uhn.hl7v2.model.v24.segment.PID;
import ca.uhn.hl7v2.model.v24.segment.PV1;
import ca.uhn.hl7v2.model.v24.segment.RXA;
import ca.uhn.hl7v2.model.v24.segment.RXO;
import ca.uhn.hl7v2.model.v24.segment.RXR;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.zb.comm.Hl7MsgHander;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecEXMapper;
import com.zebone.nhis.ma.pub.platform.zb.dao.MsgRecMapper;
import com.zebone.nhis.ma.pub.platform.zb.service.SysMsgService;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * Adt消息处理
 * 
 * @author chengjia
 * 
 */
@Service
public class MsgSendEx {
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private Hl7MsgHander msgHander;
	@Resource
	private MsgRecMapper msgRecMapper;
	@Resource
	private MsgRecEXMapper msgRecEXMapper;
	@Resource
	private MsgSendAdt msgSendAdt;
	@Resource
	private SysMsgService msgService;
	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue(
			"msg.split.num", "10"));

	/**
	 * 发送Hl7消息（OMP^O09）住院医嘱
	 * 
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendOMPMsg(String triggerEvent,List<List<Map<String, Object>>> newListsMap){
		try{
		if (triggerEvent.equals("O09")) {
			for(List<Map<String, Object>> listMap:newListsMap){
				if (listMap != null && listMap.size() > 0) {
					String msgId = "", msg = "";
					// 医嘱信息
					OMP_O09 omp = new OMP_O09();
					MSH msh;
					PID pid;
					PV1 pv1 = null;

					ORC orc;
					RXO rxo;
					RXR rxr;
					int mod = 0;
					int index=0;
					
					Map<String, Object> patMap = new HashMap<String, Object>();
					msh = omp.getMSH();
					patMap = listMap.get(0);
					msgId = MsgUtils.getMsgId();
					MsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");

					pid = omp.getPATIENT().getPID();
					pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
					msgSendAdt.qryAndSetPID_PV1(pid, pv1, patMap);
					
					List<Map<String, Object>> resList = HL7SendExOrder(listMap);
					for (int i = 0; i < resList.size(); i++) { // list.size()
						Map<String, Object> map = listMap.get(i);
						
						//如果患者不一样，发当前消息，新建pv1
						if(!MsgUtils.getPropValueStr(map, "pkPv").equals(MsgUtils.getPropValueStr(patMap, "pkPv"))){
							msg = MsgUtils.getParser().encode(omp);
							msgHander.sendMsg(msgId, msg);

							msgId = MsgUtils.getMsgId();
							omp = new OMP_O09();

							msh = omp.getMSH();
							MsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");

							pid = omp.getPATIENT().getPID();
							pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
							msgSendAdt.qryAndSetPID_PV1(pid, pv1, map);

							mod = 0;
							index=0;
						}
						
						orc = omp.getORDER(index).getORC();
						
						// 申请控制control
						orc.getOrderControl().setValue("NW");
						// 处方明细编码
						orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
						// 申请单号
						orc.getFillerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
						// 医嘱号ordsnParent
						orc.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
						
						//平台：医嘱状态 ：1录入 2提交 3首次执行（确认） 4执行 5停止 6撤销
						//his：医嘱状态 ：0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
						Map<String,Object> statusMap=DataBaseHelper.queryForMap("SELECT eu_status_ord FROM cn_order WHERE pk_cnord = ?", MsgUtils.getPropValueStr(map, "pkCnord"));
						String euStatusOrd=MsgUtils.getPropValueStr(statusMap, "euStatusOrd");
						// 医嘱状态
						if ("O".equalsIgnoreCase(pv1.getPatientClass().getValue())) {
							orc.getOrderStatus().setValue("1");// 门诊：【0：手工单；1、电脑录入】
						} else {
							orc.getOrderStatus().setValue(MsgUtils.getEuStatusOrdText(euStatusOrd));
						}
						/*
						 * 剂量(quantity-quantity)&单位(quantity-units-identifier)^
						 * 频率(interval-explicitTimeInterval)^ 优先权-priority(紧急A)^
						 * 天数(occurrenceDuration-identifier)^ 次数(totalOccurences)
						 */
						orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(MsgUtils.getPropValueStr(map, "dosage"));// 剂量
						String unitName = qryUnitByPK(MsgUtils.getPropValueStr(map,"pkUnitDos"));
						orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);// 单位
						String cnt = qryItemFreq(MsgUtils.getPropValueStr(map,"codeFreq"));
						orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(MsgUtils.getPropValueStr(map, "codeFreq"));// 牝鹿
						if (MsgUtils.getPropValueStr(map, "flagEmer").trim().equals("1"))
							orc.getQuantityTiming(0).getDuration().setValue("A");// 优先权
						orc.getQuantityTiming(0).getPriority().setValue(MsgUtils.getPropValueStr(map, "days"));// 天数
						orc.getQuantityTiming(0).getCondition().setValue(cnt);// 次数

						// parent
						orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
						// 事务日期/时间
						orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
						// 输入者
						Map<String, Object> empCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpOrd"));
						if(empCode!=null){
							orc.getEnteredBy(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "CODE"));
							orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "NAME"));
							// 医生编码
							orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "codeUser"));
							orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "nameUser"));
						}
						
						// 护士
						orc.getVerifiedBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
						orc.getVerifiedBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
						// 开始时间  2020-07-29 添加停止、撤销时间
						String dateStop=MsgUtils.getPropValueDateSting(map,"dateStop");//停止时间
						String dateErase=MsgUtils.getPropValueDateSting(map,"dateErase");//作废时间
						if( "4".equals(euStatusOrd) && StringUtils.isNotBlank(dateStop)){
							orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(dateStop);
						}else if("9".equals(euStatusOrd)){
							if(StringUtils.isBlank(dateErase)){
								orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
							}else{
								orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(dateErase);
							}
						}else{
							orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map,"dateStart"));
						}
						
						Map<String, Object> deptMap = msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,"pkDept"));
						if(deptMap!=null){
							// 录入科室
							orc.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
							orc.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(deptMap, "nameDept"));
						}
						
						// 患者科室
						Map<String, Object> ipDeptMap = msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept());
						if(ipDeptMap!=null){
							orc.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(ipDeptMap, "codeDept"));
							orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(MsgUtils.getPropValueStr(ipDeptMap, "nameDept"));
						}
						// 区分首次末次
						if (!"4".equals(euStatusOrd) && "0".equals(MsgUtils.getPropValueStr(map, "flagFirst"))) {
							orc.getEnteringDevice().getIdentifier().setValue("F");
							orc.getEnteringDevice().getText().setValue(MsgUtils.getPropValueStr(map, "firstNum"));
						} else {
							orc.getEnteringDevice().getIdentifier().setValue("L");
							orc.getEnteringDevice().getText().setValue(MsgUtils.getPropValueStr(map, "lastNum"));
						}

						// "a,长短期标识：0、临时       1、长期 b.LB-医嘱类别：01:药品，02:检查，03:检验，04:手术，
						//05:治疗，06：护理，07：卫材，08：嘱托，09：诊疗，10：公卫，11：患者管理，12：输血，13：饮食，14：陪护，99：其他"
						String codeOrdtype = MsgUtils.getPropValueStr(map,"codeOrdtype");
						if (MsgUtils.getPropValueStr(map, "euAlways").equals("0")) {
							orc.getOrderStatusModifier().getIdentifier().setValue("1");
						} else {
							orc.getOrderStatusModifier().getIdentifier().setValue("0");
						}
						orc.getOrderStatusModifier().getText().setValue(codeOrdtype.substring(0, 2));

						rxo = omp.getORDER(index).getRXO();
						// 医嘱编码
						rxo.getRequestedGiveCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
						rxo.getRequestedGiveCode().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
						if (MsgUtils.getPropValueStr(map, "flagDurg").trim().equals("0"))
							rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("n");
						else {
							rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue("d");
						}
						// 数量
						rxo.getRequestedGiveAmountMinimum().setValue(MsgUtils.getPropValueStr(map, "quan"));
						// 单位
						rxo.getRequestedGiveUnits().getIdentifier().setValue(unitName);
						// 药房编码*/
						Map<String, Object> itemMap = msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,"pkDeptExec"));
						if(itemMap!=null){
						rxo.getDeliverToLocation().getPointOfCare().setValue(MsgUtils.getPropValueStr(itemMap, "codeDept"));
						}
						// 草药付数
						rxo.getNumberOfRefills().setValue(MsgUtils.getPropValueStr(map, "ords"));
						// 预防治疗标志 皮试
						rxo.getIndication(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "euSt"));// TODO

						rxr = omp.getORDER(index).getRXR();
						
						// 给药方式
						rxr.getRoute().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeSupply"));
						// 草药用法
						String supplyName = qrySupplyCode(MsgUtils.getPropValueStr(map,"codeSupply"));
					
						rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);
						//logers.info("住院医嘱3-->"+MsgUtils.getParser().encode(omp));
						if (i != 0 && i % splitNum == 0) {
							msg = MsgUtils.getParser().encode(omp);
							msgHander.sendMsg(msgId, msg);

							msgId = MsgUtils.getMsgId();
							omp = new OMP_O09();

							msh = omp.getMSH();
							MsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");

							Map<String, Object> param = new HashMap<String, Object>();
							param.put("pkPi", map.get("pkPi"));
							param.put("pkPv", map.get("pkPv"));

							pid = omp.getPATIENT().getPID();
							MsgUtils.createPIDMsg(pid, msgSendAdt.qryPID_PV1(param));

							pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
							MsgUtils.createPV1Msg(pv1, msgSendAdt.qryPID_PV1(param));

							mod = 0;
							index=0;
						} else {
							index++;
							mod = i % splitNum;
						}
					}
		            
					if (mod != 0 || listMap.size() == 1) {
						msg = MsgUtils.getParser().encode(omp);
						msgHander.sendMsg(msgId, msg);
					}
					}
			}
		}
		} catch (Exception e) {
			loger.error("发送Hl7消息（OMP^O09）住院医嘱{},{}",newListsMap,e.getMessage());
		}
	}

	/**
	 * 医嘱费用
	 * 
	 * @param triggerEvent
	 * @param listMap
	 * @throws HL7Exception
	 */
	public void sendRASMsg(String triggerEvent,List<Map<String, Object>> listMap, String typeStatus) {
		try{
		if (triggerEvent.equals("O17")) {
			String msgId = "", msg = "";
			// 医嘱执行信息
			RAS_O17 ras = new RAS_O17();
			MSH msh;
			PID pid;
			PV1 pv1;

			ORC orc;
			RXO rxo;
			RXR rxr;
			NTE nte;
			RXA rxa;
			int mod = 0;
			int index=0;
			List<Map<String, Object>> resList = getSendExOrderItem(listMap);
			if (resList != null && resList.size() > 0) {
				Map<String, Object> patMap = new HashMap<String, Object>();
				msgId = MsgUtils.getMsgId();
				patMap = resList.get(0);
				msh = ras.getMSH();
				MsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");

				Map<String, Object> param = new HashMap<String, Object>();
				param.put("pkPi", patMap.get("pkPi"));
				param.put("pkPv", patMap.get("pkPv"));
				pid = ras.getPATIENT().getPID();
				pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
				msgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
			}
			for (int i = 0; i < resList.size(); i++) {
				Map<String, Object> map = resList.get(i);
				orc = ras.getORDER(index).getORC();
				// 申请控制
				orc.getOrderControl().setValue("NW");
				// 医嘱详细编码
				orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
				// 申请单号
				orc.getFillerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
				// 医嘱组号
				orc.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
				// 医嘱状态
				orc.getOrderStatus().setValue(MsgUtils.getPropValueStr(map, "euStatusOrd"));
				/*
				 * 剂量(quantity-quantity)&单位(quantity-units-identifier)^
				 * 频率(interval-explicitTimeInterval)^ 优先权-priority(紧急A)^
				 * 天数(occurrenceDuration-identifier)^ 次数(totalOccurences)
				 */
				orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(MsgUtils.getPropValueStr(map, "dosage"));// 计量
				String unitName = qryUnitByPK(MsgUtils.getPropValueStr(map,"pkUnitDos"));
				orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);// 单位
				String cnt = qryItemFreq(MsgUtils.getPropValueStr(map,"codeFreq"));
				orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(MsgUtils.getPropValueStr(map, "codeFreq"));// 频率
				if (MsgUtils.getPropValueStr(map, "flagEmer").trim().equals("1"))
					orc.getQuantityTiming(0).getDuration().setValue("A");// 优先权
				orc.getQuantityTiming(0).getPriority().setValue(MsgUtils.getPropValueStr(map, "days"));// 天数
				orc.getQuantityTiming(0).getCondition().setValue(cnt);

				// parent
				orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
				// 系统时间Date/Time of Transaction
				orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
				// 停止医嘱退药的时候记录对应所退项目的原费用ID Advanced Beneficiary Notice Code
				if ("1".equals(MsgUtils.getPropValueStr(map, "flagCanc"))) {
					orc.getAdvancedBeneficiaryNoticeCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "pkPdback"));
				}
				// 输入者
				orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
				orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
				// 医生编码
				Map<String, Object> empCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpOrd"));
				orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "CODE"));
				orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "NAME"));

				// 护士
				Map<String, Object> chkCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpChk"));
				orc.getVerifiedBy(0).getIDNumber().setValue(MsgUtils.getPropValueStr(chkCode, "CODE"));
				orc.getVerifiedBy(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(chkCode, "NAME"));
				// 开始时间
				orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map,"dateStart"));
				// 如取消医嘱可录入原因Order Control Code Reason
				orc.getOrderControlCodeReason().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "noteOrd"));
				// 录入科室
				Map<String, Object> mapDept = msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept());
				orc.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(mapDept, "codeDept"));
				orc.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(mapDept, "nameDept"));
				// 患者科室
				Map<String, Object> occDept = msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,"pkDept"));
				orc.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(occDept, "codeDept"));
				orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(MsgUtils.getPropValueStr(occDept, "nameDept"));

				rxo = ras.getORDER(index).getORDER_DETAIL().getRXO();
				// 医嘱编码^收费项目编码^医嘱名称
				rxo.getRequestedGiveCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				rxo.getRequestedGiveCode().getText().setValue(MsgUtils.getPropValueStr(map, "pkItem"));
				rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				// 数量
				rxo.getRequestedGiveAmountMinimum().setValue(MsgUtils.getPropValueStr(map, "quanOcc"));
				// identifier 数量单位
				String unitPdName = qryUnitByPK(MsgUtils.getPropValueStr(map,
						"pkUnitPd"));
				rxo.getRequestedGiveUnits().getIdentifier().setValue(unitPdName);
				// 医嘱描述
				rxo.getProviderSPharmacyTreatmentInstructions(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "descOrd"));
				// 嘱托
				rxo.getProviderSAdministrationInstructions(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "flagNote"));
				// facility-niversalID 药房编码
				Map<String, Object> nivDepts = msgService
						.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,
								"pkDeptExec"));
				rxo.getDeliverToLocation().getPointOfCare().setValue(MsgUtils.getPropValueStr(nivDepts, "codeDept"));
				rxo.getDeliverToLocation().getRoom().setValue(MsgUtils.getPropValueStr(nivDepts, "nameDept"));
				// 草药付数Number Of Refills
				rxo.getNumberOfRefills().setValue(MsgUtils.getPropValueStr(map, "ords"));
				// 预防治疗标志
				rxo.getIndication(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "flagPrev"));
				/* // Allow Substitutions 允许替代 拆分标识：0可拆分；1不可拆分 */// TODO 拆分标志
				rxo.getAllowSubstitutions().setValue("1");
				// 药品单类型^药品单号^A打印D取消
				rxo.getSupplementaryCode(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "nameDecate"));
				rxo.getSupplementaryCode(0).getText().setValue(MsgUtils.getPropValueStr(map, "codeDe"));
				String print = "";
				if ("1".equals(MsgUtils.getPropValueStr(map, "flagPrt"))) {
					print = "A";
				} else {
					print = "D";
				}
				rxo.getSupplementaryCode(0).getNameOfCodingSystem().setValue(print);

				// NTE-说明
				nte = ras.getORDER(index).getORDER_DETAIL()
						.getORDER_DETAIL_SUPPLEMENT().getNTE();
				nte.getComment(0).setValue(MsgUtils.getPropValueStr(map, "price"));
				//护理等级和护理名称
				nte.getCommentType().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeOrd"));
				nte.getCommentType().getText().setValue(MsgUtils.getPropValueStr(map, "nameOrd"));
				rxr = ras.getORDER(index).getRXR();
				// 给药方式
				rxr.getRoute().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeSupply"));
				// 草药用法
				String supplyName = qrySupplyCode(MsgUtils.getPropValueStr(map,"codeSupply"));
				rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);

				rxa = ras.getORDER(index).getRXA();
				// 开始执行的日期／时间 医嘱开始时间
				rxa.getDateTimeStartOfAdministration().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map,"dateStart"));
				// 结束执行的日期／时间 医嘱结束时间
				if(StringUtils.isNotBlank(MsgUtils.getPropValueStr(map, "dateStop"))){
					rxa.getDateTimeEndOfAdministration().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(map, "dateStop"));
				}
				// "长短期标志identifier ^医嘱类别^费用标志
				rxa.getAdministeredCode().getIdentifier().setValue(MsgUtils.getPropValueStr(map, "euAlways"));
				rxa.getAdministeredCode().getText().setValue(MsgUtils.getPropValueStr(map, "codeOrdtype"));
				String flagBl = "";
				if ("1".equals(MsgUtils.getPropValueStr(map, "flagSelf"))) {
					flagBl = "1";
				} else if ("1"
						.equals(MsgUtils.getPropValueStr(map, "flagNote"))) {
					flagBl = "2";
				} else if ("1"
						.equals(MsgUtils.getPropValueStr(map, "flagBase"))) {
					flagBl = "3";
				} else if ("1".equals(MsgUtils.getPropValueStr(map, "flagBl"))) {
					flagBl = "5";
				}
				rxa.getAdministeredCode().getNameOfCodingSystem()
						.setValue(flagBl);
				// 执行者 执行护士
				rxa.getAdministeringProvider(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
				rxa.getAdministeringProvider(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
				// 执行定位 执行科室
				rxa.getAdministeredAtLocation().getPointOfCare().setValue(MsgUtils.getPropValueStr(mapDept, "codeDept"));
				rxa.getAdministeredAtLocation().getRoom().setValue(MsgUtils.getPropValueStr(mapDept, "nameDept"));

				// Substance/Treatment Refusal Reason拒绝物品/治疗原因 未作原因
				rxa.getSubstanceTreatmentRefusalReason(0).getIdentifier().setValue(MsgUtils.getPropValueStr(map, "noteOrd"));
				// Completion Status完成情况 录入状态1 上账状态2
				rxa.getCompletionStatus().setValue("2");
				// Action Code-RXA　RXA行动代码 A增加 D删除 U更新(撤销D）
				if (typeStatus.equals("ADD")) {
					rxa.getActionCodeRXA().setValue("A");
				} else if (typeStatus.equals("DEL")) {
					rxa.getActionCodeRXA().setValue("D");
				} else if (typeStatus.equals("UPDATE")) {
					rxa.getActionCodeRXA().setValue("U");
				}
				// System Entry Date/Time系统录入的日期/时间 执行时间
				rxa.getSystemEntryDateTime().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));

				if (i != 0 && i % splitNum == 0) {
					// i=20
					// 发送消息
					msg = MsgUtils.getParser().encode(ras);
					msgHander.sendMsg(msgId, msg);

					ras = new RAS_O17();

					msgId = MsgUtils.getMsgId();

					msh = ras.getMSH();
					MsgUtils.createMSHMsg(msh, msgId, "RAS", "O17");

					Map<String, Object> param = new HashMap<String, Object>();
					param.put("pkPi", map.get("pkPi"));
					param.put("pkPv", map.get("pkPv"));

					pid = ras.getPATIENT().getPID();
					MsgUtils.createPIDMsg(pid, msgSendAdt.qryPID_PV1(param));

					pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
					MsgUtils.createPV1Msg(pv1, msgSendAdt.qryPID_PV1(param));

					mod = 0;
					index = 0;
				} else {
					index++;
					mod = i % splitNum;
				}
			}
			if (mod != 0 || listMap.size() == 1) {
				msg = MsgUtils.getParser().encode(ras);
				msgHander.sendMsg(msgId, msg);
			}

		} else {
			// 其他
		}
		} catch (Exception e) {
			loger.error("O17医嘱费用{}",e.getMessage());
		}
	}


	/**
	 * 手术申请消息生成（创建ORM_O01消息）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public void createORM_O01Msg(Map<String, Object> paramMap) throws Exception{
		List<Map<String,Object>> listMap = (List<Map<String,Object>>)paramMap.get("ordlist");
        for(Map<String, Object> map:listMap){
		
        Map<String,Object> cnOpApplyMap=DataBaseHelper.queryForMap("SELECT * FROM cn_op_apply WHERE pk_cnord = ?", MsgUtils.getPropValueStr(map, "pkCnord"));
        
        if(MapUtils.isNotEmpty(cnOpApplyMap)){ 
		ORM_O01 orm = new ORM_O01();
		MSH msh = orm.getMSH();
		String msgId = MsgUtils.getMsgId();
		MsgUtils.createMSHMsg(msh, msgId, "ORM", "O01");
		
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("pkPv", map.get("pkPv"));
		PID pid = orm.getPATIENT().getPID();
		PV1 pv1 = orm.getPATIENT().getPATIENT_VISIT().getPV1();
		msgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
		
		
		ORC orc = orm.getORDER().getORC();
		// 申请控制
		orc.getOrderControl().setValue("NW");
		// 处方明细编码
		orc.getPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsn"));
		// 申请单号
		orc.getFillerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "codeApply"));
		// 医嘱号ordsnParent
		orc.getPlacerGroupNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
		// 医嘱状态
		if ("O".equalsIgnoreCase(pv1.getPatientClass().getValue())) {
			orc.getOrderStatus().setValue("1");// 门诊：【0：手工单；1、电脑录入】
		} else {
			//平台：医嘱状态 ：1录入 2提交 3首次执行（确认） 4执行 5停止 6撤销
			//his：医嘱状态 ：0 开立；1 签署；2 核对；3 执行；4 停止；9 作废
			Map<String,Object> statusMap=DataBaseHelper.queryForMap("SELECT eu_status_ord FROM cn_order WHERE pk_cnord = ?", MsgUtils.getPropValueStr(map, "pkCnord"));
			orc.getOrderStatus().setValue(MsgUtils.getEuStatusOrdText(MsgUtils.getPropValueStr(statusMap, "euStatusOrd")));
		}
		//logers.info("Msgorc:{},{}", i,MsgUtils.getParser().encode(omp));
		/*
		 * 剂量(quantity-quantity)&单位(quantity-units-identifier)^
		 * 频率(interval-explicitTimeInterval)^ 优先权-priority(紧急A)^
		 * 天数(occurrenceDuration-identifier)^ 次数(totalOccurences)
		 */
		orc.getQuantityTiming(0).getQuantity().getQuantity().setValue(MsgUtils.getPropValueStr(map, "dosage"));// 剂量
		String unitName = qryUnitByPK(MsgUtils.getPropValueStr(map,"pkUnitDos"));
		orc.getQuantityTiming(0).getQuantity().getUnits().getIdentifier().setValue(unitName);// 单位
		String cnt = qryItemFreq(MsgUtils.getPropValueStr(map,"codeFreq"));
		orc.getQuantityTiming(0).getInterval().getRi1_RepeatPattern().setValue(MsgUtils.getPropValueStr(map, "codeFreq"));// 牝鹿
		if (MsgUtils.getPropValueStr(map, "flagEmer").trim().equals("1"))
			orc.getQuantityTiming(0).getDuration().setValue("A");// 优先权
		orc.getQuantityTiming(0).getPriority().setValue(MsgUtils.getPropValueStr(map, "days"));// 天数
		orc.getQuantityTiming(0).getCondition().setValue(cnt);// 次数
		
		// parent
		orc.getParentOrder().getParentSPlacerOrderNumber().getEntityIdentifier().setValue(MsgUtils.getPropValueStr(map, "ordsnParent"));
		// 停止时间
		orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(MsgUtils.PropDateSting(new Date()));
		// 输入者
		Map<String, Object> empCode = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(map,"pkEmpOrd"));
		if(empCode!=null){
			orc.getEnteredBy(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "CODE"));
			orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "NAME"));
			// 医生编码
			orc.getOrderingProvider(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "codeUser"));
			orc.getOrderingProvider(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "nameUser"));
		}
		
		// 护士
		orc.getVerifiedBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		orc.getVerifiedBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		// 开始时间-手术时间
		orc.getOrderEffectiveDateTime().getTimeOfAnEvent().setValue(MsgUtils.getPropValueDateSting(cnOpApplyMap,"datePlan"));
		Map<String, Object> deptMap = msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,"pkDept"));
		if(deptMap!=null){
			// 录入科室
			orc.getEnteringOrganization().getIdentifier().setValue(MsgUtils.getPropValueStr(deptMap, "codeDept"));
			orc.getEnteringOrganization().getText().setValue(MsgUtils.getPropValueStr(deptMap, "nameDept"));
		}
		
		// 患者科室
		Map<String, Object> ipDeptMap = msgService.getDeptInfoByPkDept(UserContext.getUser().getPkDept());
		if(ipDeptMap!=null){
			orc.getOrderingFacilityName(0).getOrganizationName().setValue(MsgUtils.getPropValueStr(ipDeptMap, "codeDept"));
			orc.getOrderingFacilityName(0).getOrganizationNameTypeCode().setValue(MsgUtils.getPropValueStr(ipDeptMap, "nameDept"));
		}
		

		// 区分首次末次
		if ("0".equals(MsgUtils.getPropValueStr(map, "flagFirst"))) {
			orc.getEnteringDevice().getIdentifier().setValue("F");
		} else {
			orc.getEnteringDevice().getIdentifier().setValue("L");
			orc.getEnteringDevice().getText().setValue(MsgUtils.getPropValueStr(map, "lastNum"));
		}

		// "a,长短期标识：0、临时       1、长期 b.LB-医嘱类别：01:药品，02:检查，03:检验，04:手术，
		//05:治疗，06：护理，07：卫材，08：嘱托，09：诊疗，10：公卫，11：患者管理，12：输血，13：饮食，14：陪护，99：其他"
		String codeOrdtype = MsgUtils.getPropValueStr(map,"codeOrdtype");
		if (MsgUtils.getPropValueStr(map, "euAlways").equals("0")) {
			orc.getOrderStatusModifier().getIdentifier().setValue("1");
		} else {
			orc.getOrderStatusModifier().getIdentifier().setValue("0");
		}
		orc.getOrderStatusModifier().getText().setValue(codeOrdtype.substring(0, 2));

		ODT odt = orm.getORDER().getORDER_DETAIL().getODT();
		odt.getTrayType().getIdentifier().setValue("1");
		
		DG1 dg1 = orm.getORDER().getORDER_DETAIL().getDG1();
		//根据患者PKPV查询患者诊断
		Map<String,Object> termDiagMap=DataBaseHelper.queryForMap("SELECT * FROM PV_DIAG WHERE PK_DIAG = ? AND PK_PV=?", MsgUtils.getPropValueStr(cnOpApplyMap, "pkDiagPre"),map.get("pkPv"));
		//组装DG1诊断消息体
		if(null != termDiagMap && !("").equals(termDiagMap)){
			//Diagnosis Code - DG1 编码	
			dg1.getDiagnosisCodeDG1().getCe1_Identifier().setValue(MsgUtils.getPropValueStr(termDiagMap, "codeIcd"));
			//Diagnosis Description	诊断描述	名称	
			dg1.getDiagnosisDescription().setValue(MsgUtils.getPropValueStr(termDiagMap, "descDiag"));
			
			//临床诊断医师	IDNumber
			if(empCode != null){
				dg1.getDiagnosingClinician(0).getIDNumber().setValue(MsgUtils.getPropValueStr(empCode, "CODE"));
				dg1.getDiagnosingClinician(0).getFamilyName().getFn1_Surname().setValue(MsgUtils.getPropValueStr(empCode, "NAME"));				//证明日期/时间
			}
			
		}
		
		//拼接z段消息
		String[] strs=new String[20];
		strs[1] = "";
		//2手术科室
		Map<String, Object> deptMaps = msgService.getDeptInfoByPkDept(MsgUtils.getPropValueStr(map,"pkDeptExec"));
		if(MapUtils.isNotEmpty(deptMaps)){
			strs[2] = MsgUtils.getPropValueStr(deptMaps,"codeDept")+"#"+MsgUtils.getPropValueStr(deptMaps,"nameDept");
		}else{
			strs[2] = "";
		}
		
		strs[3] = "";
		strs[4] = "";
		//5 手术等级
		Map<String, Object> queryOplevel = msgService.queryOplevel(MsgUtils.getPropValueStr(cnOpApplyMap,"dtOplevel"));
		if(MapUtils.isNotEmpty(queryOplevel)){
			strs[5] = MsgUtils.getPropValueStr(queryOplevel,"baCode");	
		}else{
			strs[5] = "";
		}
		//手术时长
		if(CommonUtils.isNotNull(MsgUtils.getPropValueStr(cnOpApplyMap,"duration"))){
			strs[6] = MsgUtils.getPropValueStr(cnOpApplyMap,"duration");	
		}else{
			strs[6] = "";
		}
		Map<String, Object> querydtAnae = msgService.querydtAnae(MsgUtils.getPropValueStr(cnOpApplyMap,"dtAnae"));
		//7 麻醉方法
		if(MapUtils.isNotEmpty(querydtAnae)){
			strs[7] = MsgUtils.getPropValueStr(querydtAnae,"code")+"#"+MsgUtils.getPropValueStr(querydtAnae,"name");	
		}else{
			strs[7] = "";
		}
		//8 急诊标志 急诊标志(0：择期，1：急诊) 数据库	1择期 2限期 3急诊
		String euOptype = MsgUtils.getPropValueStr(cnOpApplyMap,"euOptype");
		if(StringUtils.isNotBlank(euOptype)){
			strs[8] =euOptype;
		}else{
			strs[8] ="1";
		}
		strs[9] ="";
		//10 手术人员
		String codePhyOp="",codePhyOpName="",codeAsis="",codeAsisName="",codeAsiss="",codeAsissName="",codeAnae="",codeAnaeName="";
        if(CommonUtils.isNotNull(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpPhyOp"))){
        	Map<String,Object> codePhyOpMap = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpPhyOp"));
        	codePhyOp = MsgUtils.getPropValueStr(codePhyOpMap,"code");
        	codePhyOpName = MsgUtils.getPropValueStr(codePhyOpMap,"name");
        }
        if(CommonUtils.isNotNull(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAsis"))){
        	Map<String,Object> codeAsisMap = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAsis"));
        	codeAsis = MsgUtils.getPropValueStr(codeAsisMap,"code");
        	codeAsisName = MsgUtils.getPropValueStr(codeAsisMap,"name");
        }
        if(CommonUtils.isNotNull(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAsis2"))){
        	Map<String,Object> codeAsissMap = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAsis2"));
        	codeAsiss = MsgUtils.getPropValueStr(codeAsissMap,"code");
        	codeAsissName = MsgUtils.getPropValueStr(codeAsissMap,"name");
        }
		strs[10] = codePhyOp+"#"+codePhyOpName+"#"+codeAsis+"#"+codeAsisName+"#"+codeAsiss+"#"+codeAsissName;
		//11 麻醉人员
		if(CommonUtils.isNotNull(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAnae"))){
			Map<String,Object> codeAnaeMap = msgService.getUserCodeByPkUser(MsgUtils.getPropValueStr(cnOpApplyMap,"pkEmpAnae"));
			codeAnae = MsgUtils.getPropValueStr(codeAnaeMap,"code");
			codeAnaeName = MsgUtils.getPropValueStr(codeAnaeMap,"name");
        }
		strs[11] = codeAnae+"#"+codeAnaeName;
		strs[12] ="";
		strs[13] ="";
		strs[14] ="";
		strs[15] ="";
		
		String codeOp="",nameOp="";
		//判断是否存在Key
		if(cnOpApplyMap.containsKey("pkOp")){
			//判断Value是否有效
			if(CommonUtils.isEmptyString(MsgUtils.getPropValueStr(cnOpApplyMap,"pkOp"))){
				nameOp=MsgUtils.getPropValueStr(cnOpApplyMap,"nameOp");
			}else{
				//手术代码
				codeOp = MsgUtils.getPropValueStr(msgService.queryBdTermDiag(MsgUtils.getPropValueStr(cnOpApplyMap,"pkOp")),"diagcode");
				//名称
				nameOp = MsgUtils.getPropValueStr(msgService.queryBdTermDiag(MsgUtils.getPropValueStr(cnOpApplyMap,"pkOp")),"diagname");
			}
		}
		//描述
		String descOp = MsgUtils.getPropValueStr(cnOpApplyMap,"descOp");
		if(descOp==null||"".equals(descOp)){
			descOp = nameOp;
		}
		StringBuffer dateApp = new StringBuffer(codeOp+"#"+nameOp+"#"+descOp);
		//查询附加手术
		List<Map<String,Object>> cnOpApplyList=DataBaseHelper.queryForList("SELECT diag.diagcode,diag.diagname,sub.note FROM cn_op_subjoin sub INNER JOIN  BD_TERM_DIAG diag on sub.pk_diag_sub=diag.pk_diag WHERE pk_ordop =? ", MsgUtils.getPropValueStr(cnOpApplyMap, "pkOrdop"));
		if(cnOpApplyList.size()>0){
			for (int i = 0; i < cnOpApplyList.size(); i++) {
				 if(i>=3){
				    	break;
				    }
				dateApp.append("#"+MsgUtils.getPropValueStr(cnOpApplyList.get(i),"diagcode")+"#"+MsgUtils.getPropValueStr(cnOpApplyList.get(i),"diagname")+"#"+MsgUtils.getPropValueStr(cnOpApplyList.get(i),"note"));
			   
			}
		}
		//16 手术申请项目(手术代码#名称#描述)
		strs[16] = dateApp.toString();
		//17 无菌程度
		strs[17] = MsgUtils.getPropValueStr(cnOpApplyMap,"dtAsepsis");
		strs[18] ="";
		strs[19] ="";
		orm.addNonstandardSegment("ZOP");
		Segment zop = (Segment) orm.get("ZOP");
		zop.parse(MsgUtils.arrayStrHl7Msg(strs));	
		
		String msg = MsgUtils.getParser().encode(orm);
		msgHander.sendMsg(msgId, msg);
		
        }
		}
	}

	
	// 根据医嘱主键查询医嘱信息
	public List<Map<String, Object>> HL7SendExOrder(
			List<Map<String, Object>> paramMap) {
		List<String> pkCnords = new ArrayList<String>();
		for (Map<String, Object> map : paramMap) {
			pkCnords.add(map.get("pkCnord").toString());
		}
		return msgRecEXMapper.queryOrder(pkCnords);
	}

	// 根据执行主键查询医嘱费用相关信息
	public List<Map<String, Object>> getSendExOrderItem(
			List<Map<String, Object>> paramMap) {
		List<String> occList = new ArrayList<String>();
		for (Map<String, Object> map : paramMap) {
			occList.add(map.get("pkExocc").toString());
		}
		return msgRecEXMapper.queryOrderItem(occList);
	}

	/**
	 * 根据医嘱用法名称查询用法编码
	 * 
	 * @param name
	 * @return
	 */
	private String qrySupplyCode(String name) {
		if (name.length() <= 0)
			return null;
		String sql = "select name  from bd_supply where code=?";
		return DataBaseHelper.queryForScalar(sql, String.class, name);
	}

	/**
	 * 根据医嘱频次编码查询频数
	 * 
	 * @param code
	 * @return
	 */
	private String qryItemFreq(String code) {
		if (code.length() <= 0)
			return null;
		String sql = "select cnt from BD_TERM_FREQ where CODE=?";
		return DataBaseHelper.queryForScalar(sql, String.class, code);
	}

	/**
	 * 根据单位主键查询单位名称
	 * 
	 * @param pkUnit
	 * @return
	 */
	private String qryUnitByPK(String pkUnit) {
		if (pkUnit.length() <= 0)
			return null;
		String sql = "select name from bd_unit where pk_unit=?";
		return DataBaseHelper.queryForScalar(sql, String.class, pkUnit);
	}
	
}
