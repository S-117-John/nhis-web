package com.zebone.nhis.ma.pub.platform.sd.send;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.ORM_O01;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.RSP_ZPI;
import com.zebone.nhis.ma.pub.platform.sd.vo.SQR_ZQ1;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 门诊接收消息处理
 * @author maijiaxing
 *
 */
@Service
public class SDMsgSendOp {

	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private SDOpMsgMapper sDOpMsgMapper;
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;


	/**
	 * 已缴费发票列表查询(门诊 APPT)
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public String sendAPPTMsg(Map<String, Object> paramMap) throws HL7Exception {
		RSP_ZPI rsp_zpi = new RSP_ZPI();
		paramMap.put("msgid", SDMsgUtils.getMsgId());
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZPI");
		createOpMsg.createMSHMsg(rsp_zpi.getMSH(), paramMap);
		//根据第三方交易号查询his发票数据
		Map<String, Object> queryBlExtPay = sDQueryUtils.queryBlExtPay(SDMsgUtils.getPropValueStr(paramMap, "serialNo"));
		if(queryBlExtPay!=null && queryBlExtPay.size()>0){
			paramMap.put("msaText", "成功");
			paramMap.put("situation", "AA");
			createOpMsg.createZPOMsg(rsp_zpi.getZPO(), queryBlExtPay);
		}else{
			paramMap.put("situation", "AE");
			paramMap.put("msaText", "成功!未查询到缴费记录！");
		}
		createOpMsg.createMSAMsg(rsp_zpi.getMSA(), paramMap);
		return SDMsgUtils.getParser().encode(rsp_zpi);
	}

	/**
	 * 已缴费发票列表查询(挂号 APPR)
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public String sendAPPRMsg(Map<String, Object> paramMap) throws HL7Exception {
		RSP_ZPI rsp_zpi = new RSP_ZPI();
		paramMap.put("msgid", SDMsgUtils.getMsgId());
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZPI");
		createOpMsg.createMSHMsg(rsp_zpi.getMSH(), paramMap);
		//根据第三方交易号查询his发票数据
		Map<String, Object> queryBlExtPay = sDQueryUtils.queryBlExtPay(SDMsgUtils.getPropValueStr(paramMap, "serialNo"));
		if(queryBlExtPay!=null && queryBlExtPay.size()>0){
			paramMap.put("msaText", "成功");
			paramMap.put("situation", "AA");
			createOpMsg.createZPOMsg(rsp_zpi.getZPO(), queryBlExtPay);
		}else{
			paramMap.put("situation", "AE");
			paramMap.put("msaText", "成功!未查询到缴费记录！");
		}
		createOpMsg.createMSAMsg(rsp_zpi.getMSA(), paramMap);
		return SDMsgUtils.getParser().encode(rsp_zpi);
	}


	/**
	 * 生成门诊查询预约结果消息实例
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public String  sendSQRZQ1Msg(Map<String, Object> paramMap) throws HL7Exception {
		SQR_ZQ1 sqr = new SQR_ZQ1();

		paramMap.put("msgid", SDMsgUtils.getMsgId());
		paramMap.put("msgtype", "SQR");
		paramMap.put("triggerevent", "ZQ1");
		paramMap.put("situation", "AA");
		paramMap.put("msaText", "成功");
		createOpMsg.createMSHMsg(sqr.getMSH(), paramMap);
		createOpMsg.createMSAMsg(sqr.getMSA(), paramMap);

		List<Map<String, Object>>  schMapList = sDOpMsgMapper.querySDOpSchAllInfo(paramMap);
		if(schMapList==null || schMapList.size()==0){
			throw new BusException("未查询到该患者成功的预约记录");
		}

		Map<String, Object> schMap = schMapList.get(0);
		//ARQ-2 取号密码
		sqr.getARQ(0).getFillerAppointmentID().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(schMap, "apptcode"));
		//ARQ-7
		sqr.getARQ(0).getAppointmentReason().getIdentifier().setValue("APPT_REG");
		//ARQ-18  预约号
		sqr.getARQ(0).getPlacerContactLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(schMap, "apptcode"));
		//ARQ-19 编号
		sqr.getARQ(0).getEnteredByPerson(0).getIDNumber().setValue(SDMsgUtils.getPropValueStr(schMap, "codePi"));
		//ARQ-19 名称
		sqr.getARQ(0).getEnteredByPerson(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(schMap, "namePi"));
		//zai-7 0未看诊，1看诊
		sqr.getZAI(0).getYuYueTtate().setValue(SDMsgUtils.getPropValueStr(schMap, "ispkpv"));
		//zai-11 自费金额
		sqr.getZAI(0).getZfzje().setValue(SDMsgUtils.getPropValueStr(schMap, "price"));
		//zai-12 医保金额
		sqr.getZAI(0).getSbzje().setValue("0.0");
		//zai-12 Mzlsh
		sqr.getZAI(0).getMzlsh().setValue("H8880"+SDMsgUtils.getPropValueStr(schMap, "apptcode"));
		return SDMsgUtils.getParser().encode(sqr);
	}
	/**
	 * 门诊手术消息
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public void sendOpApplyMsg(Map<String, Object> paramMap) throws HL7Exception {
		//查询相关数据
		//查询手术申请信息(control如果是CR，则不查询，因为数据已被删除，会报错) 申请单号或医嘱主键
		List<Map<String, Object>> queryOp = sDOpMsgMapper.queryOperation(paramMap);
		if(queryOp != null && queryOp.size()>0){
			paramMap.putAll(queryOp.get(0));
		}
		//查询患者信息所需数据
		List<Map<String, Object>> queryPatList = sDOpMsgMapper.queryPatListOp(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		String msgId=SDMsgUtils.getMsgId();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ORM");
		paramMap.put("triggerevent", "O01");
		ORM_O01 orm_o01 = new ORM_O01();
		MSH msh = orm_o01.getMSH();
		createOpMsg.createMSHMsg(msh, paramMap);
		Map<String, Object> map = queryPatList.get(0);
		PID pid = orm_o01.getPATIENT().getPID();
		createOpMsg.createPIDMsg(pid, map);
		PV1 pv1 = orm_o01.getPATIENT().getPATIENT_VISIT().getPV1();
		createOpMsg.createPV1Msg(pv1, map);
		//pv1=msgCreateUtil.createNolPV1Msg(pv1, paramMap);
		ORC orc = orm_o01.getORDER().getORC();
		createOpMsg.createORCMsg(orc, paramMap);
		ODT odt = orm_o01.getORDER().getORDER_DETAIL().getODT();
		createOpMsg.createODTMsg(odt, paramMap);
		DG1 dg1 = orm_o01.getORDER().getORDER_DETAIL().getDG1();
		createOpMsg.createDG1Msg(dg1, paramMap);
		//拼接z段消息
		String[] strs = new String[40];
		//2手术科室
		Map<String, Object> deptMap = sDQueryUtils.queryDeptByPk(SDMsgUtils.getPropValueStr(paramMap,"pkDeptExec"));
		strs[1] = SDMsgUtils.getPropValueStr(deptMap,"codeDept");
		//5 手术等级
		Map<String, Object> queryOplevel = sDQueryUtils.queryOplevel(SDMsgUtils.getPropValueStr(paramMap,"dtOplevel"));
		strs[4] = SDMsgUtils.getPropValueStr(queryOplevel,"oldId");
		//6 手术时长
		strs[5] = SDMsgUtils.getPropValueStr(paramMap,"duration");
		//7 麻醉方法
		strs[6] = SDMsgUtils.getPropValueStr(paramMap,"anaeName");
		//8 急诊标志 急诊标志(0：择期，1：急诊) 数据库	1择期 2限期 3急诊
		//String euOptype = SDMsgUtils.getPropValueStr(paramMap,"euOptype");
		strs[7] = "3".equals(SDMsgUtils.getPropValueStr(paramMap,"euOptype"))? "1" : "0";
		//10 手术人员
		//codePhyOp 主刀
		strs[9] = SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap, "pkEmpPhyOp")), "code") +
				//codeAsis 一助
				"#" + SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap, "pkEmpAsis")), "code") +
				//codeAsis2  二助
				"#" + SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap, "pkEmpAsis2")), "code") +
				//codeAsis3 三助
				"#" + SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap, "pkEmpAsis3")), "code");
		//11 麻醉人员
		String codeAnae = SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap,"pkEmpAnae")),"code");
		strs[10] = codeAnae;
		//16 手术申请项目(手术代码#名称#描述)
		//手术代码
		String codeOp = SDMsgUtils.getPropValueStr(sDQueryUtils.queryBdTermDiag(SDMsgUtils.getPropValueStr(paramMap,"pkOp")),"diagcode");
		//名称
		String nameOp = SDMsgUtils.getPropValueStr(paramMap,"nameOp");
		//描述
		String descOp = SDMsgUtils.getPropValueStr(paramMap,"descOp");
		descOp = "".equals(descOp)?nameOp:descOp;
		nameOp = "".equals(nameOp)?descOp:nameOp;
		strs[15] = codeOp + "#" + nameOp + "#" + descOp;
		//17 无菌程度
		strs[16] = SDMsgUtils.getPropValueStr(paramMap,"dtAsepsis");
		//18 ZOP-18：乙肝-甲肝-丙肝-梅毒-艾滋按顺序对应结果
		strs[17] = SDMsgUtils.getPropValueStr(paramMap,"dtInfresult");
		//21 ZOP-21：备注(格式：体位名称#手术用物名称#仪器名称#备注)
		strs[20] = SDMsgUtils.getPropValueStr(paramMap,"remark");
		//22 ZOP-22：麻醉分级 dt_asalevel
		strs[21] = SDMsgUtils.getPropValueStr(paramMap,"dtAsalevel");
		//26 C形臂标志
		strs[25] = SDMsgUtils.getPropValueStr(paramMap,"flagCarm");
		//37 冰冻标志
		strs[36] = SDMsgUtils.getPropValueStr(paramMap,"flagFrozen");
		orm_o01.addNonstandardSegment("ZOP");
		Segment zop = (Segment) orm_o01.get("ZOP");
		zop.parse(createOpMsg.createZMsgStr(strs));
		String msg = SDMsgUtils.getParser().encode(orm_o01);
		//发送消息
		sDHl7MsgHander.sendMsg(msgId, msg);
	}

}
