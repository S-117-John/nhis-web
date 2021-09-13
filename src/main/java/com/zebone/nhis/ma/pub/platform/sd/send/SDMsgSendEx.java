package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.v24.message.OMP_O09;
import ca.uhn.hl7v2.model.v24.message.RGV_O15;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreate;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Adt消息处理(灵璧复制版本)
 *
 * @author chengjia
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDMsgSendEx {

	public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	private static Logger loger = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Resource
	private SDHl7MsgHander sDHl7MsgHander;
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private SDMsgSendAdt sDMsgSendAdt;
	@Resource
	private MsgCreate msgCreate;
	@Resource
	private SDMsgSendIp sDMsgSendIp;

	public int splitNum = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.split.num", "10"));

	/**
	 * 发送Hl7消息（OMP^O09）住院医嘱
	 * @param triggerEvent
	 * @param listMap
	 * @param control
	 */
	public void sendOMPMsg(String triggerEvent,List<Map<String, Object>> listMap,String control){
		try{
			if (listMap == null || listMap.size() <= 0) {
				return;
			}
			int index=0;
			String msg;
			// 医嘱信息
			OMP_O09 omp = new OMP_O09();
			MSH msh = omp.getMSH();
			PID pid = omp.getPATIENT().getPID();
			PV1 pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
			List<Map<String, Object>> resList = HL7SendExOrder(listMap);
			Map<String, Object> patMap = new HashMap<>(16);
			patMap.put("pkPi", SDMsgUtils.getPropValueStr(resList.get(0), "pkPi"));
			patMap.put("pkPv", SDMsgUtils.getPropValueStr(resList.get(0), "pkPv"));
			String msgId = SDMsgUtils.getMsgId();
			SDMsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");
			sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, patMap);
			for (Map<String, Object> map : resList) {
				if (control != null && control.length() > 2) {
					//操作标志
					map.put("control", control.substring(0, 2));
					//医嘱类型
					map.put("ordStatus", control.substring(2));
				} else {
					map.put("control", control);
				}
				//签署时候检查检验，手术不发O09消息
				String ordType = SDMsgUtils.getPropValueStr(map, "codeOrdtype");
				if ("2".equals(SDMsgUtils.getPropValueStr(map, "ordstatus")) && (ordType.startsWith("02") || ordType.startsWith("03") || ordType.startsWith("04") || ordType.startsWith("0903"))) {
					continue;
				}
				//如果患者不一样，发当前消息，新建pv1
				if (!SDMsgUtils.getPropValueStr(map, "pkPv").equals(SDMsgUtils.getPropValueStr(patMap, "pkPv"))) {
					msg = SDMsgUtils.getParser().encode(omp);
					sDHl7MsgHander.sendMsg(msgId, msg);
					//新消息
					omp = new OMP_O09();
					msgId = SDMsgUtils.getMsgId();
					msh = omp.getMSH();
					pid = omp.getPATIENT().getPID();
					pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
					patMap.clear();
					patMap.put("pkPi", SDMsgUtils.getPropValueStr(map, "pkPi"));
					patMap.put("pkPv", SDMsgUtils.getPropValueStr(map, "pkPv"));
					SDMsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");
					sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, patMap);
					//mod = 0;
					index = 0;
				}
				ORC orc = omp.getORDER(index).getORC();
				msgCreate.createORCMsg(orc, map);
				RXO rxo = omp.getORDER(index).getRXO();
				NTE nte = omp.getORDER(index).getNTE();
				//医嘱备注
				nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(map, "noteOrd"));
				//O09消息的RXO段特俗处理
				map.put("msgtype", "OMP");
				map.put("triggerevent", "O09");
				msgCreate.createRXOMsg(rxo, map);
				RXR rxr = omp.getORDER(index).getRXR();
				// 给药方式
				rxr.getRoute().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeSupply"));
				// 草药用法
				String supplyName = qrySupplyCode(SDMsgUtils.getPropValueStr(map, "codeSupply"));
				rxr.getAdministrationMethod().getIdentifier().setValue(supplyName);
				if (index != 0 && index % splitNum == 0) {
					// 发送消息
					msg = SDMsgUtils.getParser().encode(omp);
					sDHl7MsgHander.sendMsg(msgId, msg);
					//msg = "";//发送完之后把原来消息清空
					//新消息生成
					msgId = SDMsgUtils.getMsgId();
					omp = new OMP_O09();
					msh = omp.getMSH();
					SDMsgUtils.createMSHMsg(msh, msgId, "OMP", "O09");
					//Map<String, Object> param = new HashMap<>(16);
					//param.put("pkPi", map.get("pkPi"));
					//param.put("pkPv", map.get("pkPv"));
					pid = omp.getPATIENT().getPID();
					pv1 = omp.getPATIENT().getPATIENT_VISIT().getPV1();
					sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, patMap);
					index = 0;
				} else {
					index++;
				}
			}
			//ORC段不存在  或者 检查检验手术消息不发时判断使用，ORC消息段申请控制为空直接返回
			String orc2 = omp.getORDER(0).getORC().getOrderControl().getValue();
			if(orc2 == null || "".equals(orc2)){
				return;
			}
			msg = SDMsgUtils.getParser().encode(omp);
			sDHl7MsgHander.sendMsg(msgId, msg);
		} catch (Exception e) {
			loger.info("方法：SDMsgSendEx.sendOMPMsg 发送Hl7消息（OMP^O09）:异常捕获："+e.toString());
		}
	}


	/**
	 * 住院药房发药  RGV_O15
	 * @param triggerEvent
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendRGVMsg(String triggerEvent,Map<String, Object> paramMap) throws HL7Exception{

		RGV_O15 rgv_o15 = null;
		String msg ;
		String msgId =  SDMsgUtils.getMsgId();
		paramMap.put("msgType", "RGV");
		paramMap.put("triggerEvent", "O15");
		Map<String, Object> mapDept = qryDeptInfoByPK(UserContext.getUser().getPkDept());
		List<ExPdDe> exPdDes =  (List<ExPdDe>) paramMap.get("exPdDes");
		List<ExPdApplyDetail> exPdAppDetails =  (List<ExPdApplyDetail>) paramMap.get("exPdAppDetails");
		String orderControl = (String)paramMap.get("orc");
		Map<String, Object> parm = new HashMap<>(16);
		Set<String> pkCnords = new HashSet<>();
		Set<String> codeDes = new HashSet<>();
		for (ExPdDe exPdDe : exPdDes) {
			codeDes.add(exPdDe.getCodeDe());
		}
		for (ExPdApplyDetail exPdAppDetail : exPdAppDetails) {
			pkCnords.add(exPdAppDetail.getPkCnord());
		}
		if (pkCnords.size()!=0) {
			parm.put("pkCnords", pkCnords);
		}
		parm.put("codeDes", codeDes);
		if("OK".equals(orderControl)){
			parm.put("euDirect", "1");
		}else {
			parm.put("euDirect", "-1");
		}
		List<Map<String, Object>> resExpdDeList  = sDMsgMapper.queryOrderPKPI(parm);

		int expedeLength = resExpdDeList.size();
		//控制申请单号循环的主体消息部分生成个数
		int z = 0;
		int y = 1 ;
		if(expedeLength==0){
			throw new BusException("未查询到相关患者的发药信息，请核对!");
		}
		String pkPi = resExpdDeList.get(0).get("pkPi").toString();
		rgv_o15 = RGVO15_HEAD(msgId,rgv_o15,resExpdDeList,0);
		for (int j = 0; j < expedeLength; j++) {
			if( y % splitNum == 0){
				msg = SDMsgUtils.getParser().encode(rgv_o15);
				sDHl7MsgHander.sendMsg(msgId, msg);
				y = 1 ;
				z = 0;
				msgId =  SDMsgUtils.getMsgId();
				rgv_o15 = RGVO15_HEAD(msgId,rgv_o15,resExpdDeList,j);
				pkPi = resExpdDeList.get(j).get("pkPi").toString();
			}
			String pkPiStr = resExpdDeList.get(j).get("pkPi").toString();
			y++;
			if(!pkPi.equals(pkPiStr)){
				msg = SDMsgUtils.getParser().encode(rgv_o15);
				sDHl7MsgHander.sendMsg(msgId, msg);
				y = 1 ;
				z = 0 ;
				msgId =  SDMsgUtils.getMsgId();
				rgv_o15 = RGVO15_HEAD(msgId,rgv_o15,resExpdDeList,j);
				pkPi = resExpdDeList.get(j).get("pkPi").toString();
			}
			RGVO15_MAIN(orderControl,rgv_o15,resExpdDeList,mapDept,j,z);
			z++;
		}
		msg = SDMsgUtils.getParser().encode(rgv_o15);
		sDHl7MsgHander.sendMsg(msgId, msg);

	}


	/**
	 * 拼接RGV_O15的头部信息
	 * @param msgId
	 * @param rgv_o15
	 * @param resExpdDeList
	 * @param j
	 * @return
	 */
	private RGV_O15 RGVO15_HEAD(String msgId,RGV_O15 rgv_o15,List<Map<String, Object>> resExpdDeList,int j){
		rgv_o15 = new RGV_O15();
		Map<String, Object> patMap = resExpdDeList.get(j);
		MSH msh = rgv_o15.getMSH();
		SDMsgUtils.createMSHMsg(msh, msgId, "RGV", "O15");
		Map<String, Object> param = new HashMap<>(16);
		param.put("pkPi", patMap.get("pkPi"));
		param.put("pkPv", patMap.get("pkPv"));
		PID pid = rgv_o15.getPATIENT().getPID();
		PV1 pv1 = rgv_o15.getPATIENT().getPATIENT_VISIT().getPV1();
		sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
		return rgv_o15;
	}


	/**
	 * 拼接RGV_O15的主体信息
	 * @param orderControl
	 * @param rgv_o15
	 * @param resExpdDeList
	 * @param mapDept
	 * @param j
	 * @param z
	 * @throws DataTypeException
	 */
	private void RGVO15_MAIN(String orderControl,RGV_O15 rgv_o15,List<Map<String, Object>> resExpdDeList,Map<String, Object> mapDept ,int j,int z) throws DataTypeException{
		Map<String, Object> map = resExpdDeList.get(j);
		ORC orc = rgv_o15.getORDER(z).getORC();
		// 申请控制
		orc.getOrderControl().setValue(orderControl);
		// 医嘱详细编码
		//科室领药领取基数药
		if("1".equals(SDMsgUtils.getPropValueStr(map, "pkExocc"))){
			Map<String, Object> Exocc = DataBaseHelper.queryForMap("select occ.Pk_exocc from EX_PD_DE de left join EX_PD_APPLY_DETAIL dt on dt.PK_PDAPDT=de.PK_PDAPDT left join BL_IP_DT ipdt on ipdt.PK_PDAPDT=dt.PK_PDAPDT left join EX_ORDER_OCC occ on occ.PK_CG=ipdt.PK_CGIP where de.pk_cnord =  '"+SDMsgUtils.getPropValueStr(map, "pkCnord")+"'");
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(Exocc, "pkExocc"));
		}else{//非科室领药
			orc.getPlacerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "pkExocc"));
		}
		// 申请单号
		orc.getFillerOrderNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeApply"));
		// 医嘱组号
		orc.getPlacerGroupNumber().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "ordsn"));
		// 医嘱状态
		//orc.getOrderStatus().setValue(SDMsgUtils.getPropValueStr(map, "euStatusOrd"));
		// 发药时间
		orc.getDateTimeOfTransaction().getTimeOfAnEvent().setValue(sdf.format(new Date()));
		// 输入者
		orc.getEnteredBy(0).getIDNumber().setValue(UserContext.getUser().getCodeEmp());
		//orc.getEnteredBy(0).getFamilyName().getFn1_Surname().setValue(UserContext.getUser().getNameEmp());
		// 发药药房
		orc.getEntererSLocation().getPersonLocationType().setValue(SDMsgUtils.getPropValueStr(mapDept, "codeDept"));

		if("CR".equals(orderControl)){
			orc.getAdvancedBeneficiaryNoticeCode().getAlternateIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "pkExocc"));
		}


		RXO rxo = rgv_o15.getORDER(z).getORDER_DETAIL().getRXO();
		// 医嘱编码^收费项目编码^医嘱名称
		rxo.getRequestedGiveCode().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeOrd"));
		if("".equals(SDMsgUtils.getPropValueStr(map, "pkItem"))){
			rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(map, "codeOrd"));
		}else{
			rxo.getRequestedGiveCode().getText().setValue(SDMsgUtils.getPropValueStr(map, "pkItem"));
		}
		rxo.getRequestedGiveCode().getNameOfCodingSystem().setValue(SDMsgUtils.getPropValueStr(map, "nameOrd"));
		// 数量
		rxo.getRequestedGiveAmountMinimum().setValue(SDMsgUtils.getPropValueStr(map, "quanOcc"));
		// identifier 数量单位
		rxo.getRequestedGiveUnits().getIdentifier().setValue(SDMsgUtils.getPropValueStr(map,"unitOcc"));
		//药房编码
		rxo.getDeliverToLocation().getFacility().getUniversalID().setValue(SDMsgUtils.getPropValueStr(mapDept, "codeDept"));
		// 药品单类型^药品单号
		//String codeDecate = qryDrugClassByPKCnord(SDMsgUtils.getPropValueStr(map, "pkCnord"));
		rxo.getSupplementaryCode(0).getIdentifier().setValue(SDMsgUtils.getPropValueStr(map, "codeDecate"));
		rxo.getSupplementaryCode(0).getText().setValue(SDMsgUtils.getPropValueStr(map, "codeDe"));

		// NTE-说明
		NTE nte = rgv_o15.getORDER(z).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
		nte.getComment(0).setValue(SDMsgUtils.getPropValueStr(map, "priceDe"));
	}




	/**
	 * 根据医嘱主键查询医嘱信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> HL7SendExOrder(List<Map<String, Object>> paramMap) {
		Map<String,Object> map = new HashMap<>(16);
		List<String> pkCnords = new ArrayList<>();
		List<String> ordsns = new ArrayList<>();
		for (Map<String, Object> m : paramMap) {
			pkCnords.add(SDMsgUtils.getPropValueStr(m, "pkCnord"));
			ordsns.add( SDMsgUtils.getPropValueStr(m, "ordsn"));
		}
		map.put("ordsns", ordsns);
		map.put("pkCnords", pkCnords);
		return sDMsgMapper.queryOrder(map);
	}

	/**
	 * 根据医嘱用法名称查询用法编码
	 *
	 * @param name
	 * @return
	 */
	private String qrySupplyCode(String name) {
		if (name.length() <= 0) {
			return null;
		}
		String sql = "select name  from bd_supply where code=? and del_flag='0'";
		Map<String,Object> resMap=DataBaseHelper.queryForMap(sql, name);
		return SDMsgUtils.getPropValueStr(resMap,"name");
	}


	/**
	 * 根据部门主键查询部门编码和部门名称
	 * @param pkDept
	 * @return
	 */
	private Map<String, Object> qryDeptInfoByPK(String pkDept) {
		if (pkDept.length() <= 0) {
			return null;
		}
		String sql = "select code_dept,name_dept from bd_ou_dept where pk_dept=?";
		return DataBaseHelper.queryForMap(sql,pkDept);
	}


	/**
	 * 发药时发送药品执行信息
	 * @param paramMap
	 * @throws HL7Exception
	 */
	public void sendDeExorderMsg(Map<String,Object> paramMap) throws HL7Exception{
		if(CommonUtils.isNotNull(paramMap.get("codeDe"))){
			String codeDe= paramMap.get("codeDe").toString();
			List<Map<String,Object>> resList=sDMsgMapper.queryPddeExorderInfo(codeDe);
			if(resList!=null && resList.size()>0){
				sDMsgSendIp.sendExConfirmMsg(resList,"ADD");
			}

		}
	}


}
