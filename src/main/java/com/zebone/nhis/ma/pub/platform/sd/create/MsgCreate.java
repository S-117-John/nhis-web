package com.zebone.nhis.ma.pub.platform.sd.create;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendAdt;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
public class MsgCreate {
	@Resource
	private SDMsgMapper sDMsgMapper;
	@Resource
	private MsgCreateUtil msgCreateUtil;
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private SDMsgSendAdt sDMsgSendAdt;
	
	
	/**
	 * 创建OBR消息
	 * @param obr
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public OBR createOBRMsg(OBR obr,Map<String, Object> paramMap) throws DataTypeException{
		return msgCreateUtil.createOBRMsg(obr, paramMap);
	}
	
	/**
	 * 创建orc消息
	 * @param orc
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ORC createORCMsg(ORC orc,Map<String, Object> paramMap) throws DataTypeException{
		return msgCreateUtil.createORCMsg(orc, paramMap);
	}
	
	/**
	 * 创建rxo消息
	 * @param rxo
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public RXO createRXOMsg(RXO rxo,Map<String, Object> paramMap) throws DataTypeException{
		return msgCreateUtil.createRXOMsg(rxo, paramMap);
	}
	
	/**
	 * 创建rxa消息
	 * @param rxa
	 * @param paramMap
	 * @throws DataTypeException 
	 */
	public RXA createRXAMsg(RXA rxa, Map<String, Object> paramMap) throws DataTypeException{
		return msgCreateUtil.createRXAMsg(rxa,paramMap);
	}
	/**
	 * 入院预交金消息生成（创建DFT_P03消息）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createDFT_P03Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		DFT_P03 dft_p03 = new DFT_P03();
		//查询所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "DFT");
		paramMap.put("triggerevent", "P03");
		//创建msh消息
		msgCreateUtil.createMSHMsg(dft_p03.getMSH(), paramMap);
		//创建EVN消息
		msgCreateUtil.createEVNMsg(dft_p03.getEVN(), paramMap);
		//创建pid消息
		msgCreateUtil.createPIDMsg(dft_p03.getPID(), paramMap);
		//创建pv1消息
		msgCreateUtil.createPV1Msg(dft_p03.getPV1(), paramMap);
		//创建ft1消息
		msgCreateUtil.createFT1Msg(dft_p03.getFINANCIAL().getFT1(), paramMap);
		return dft_p03;
	}
	
	/**
	 * 手术申请消息生成（创建ORM_O01消息）
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public ORM_O01 createORM_O01Msg(String msgId,Map<String, Object> paramMap) throws Exception{
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ORM");
		paramMap.put("triggerevent", "O01");
		ORM_O01 orm_o01 = new ORM_O01();
		MSH msh = orm_o01.getMSH();
		msh = msgCreateUtil.createMSHMsg(msh, paramMap);
		Map<String, Object> map = (Map<String, Object>) paramMap.get("map");
		PID pid = orm_o01.getPATIENT().getPID();
		pid = msgCreateUtil.createPIDMsg(pid, map);
		PV1 pv1 = orm_o01.getPATIENT().getPATIENT_VISIT().getPV1();
		pv1 = msgCreateUtil.createPV1Msg(pv1, map);
		//pv1=msgCreateUtil.createNolPV1Msg(pv1, paramMap);
		ORC orc = orm_o01.getORDER().getORC();
		orc = msgCreateUtil.createORCMsg(orc, paramMap);
		ODT odt = orm_o01.getORDER().getORDER_DETAIL().getODT();
		odt = msgCreateUtil.createODTMsg(odt, paramMap);
		DG1 dg1 = orm_o01.getORDER().getORDER_DETAIL().getDG1();
		dg1 = msgCreateUtil.createDG1Msg(dg1, paramMap);
		//拼接z段消息
		String[] strs=new String[40];
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
		strs[7] = SDMsgUtils.getPropValueStr(paramMap,"euOptype").equals("3")? "1" : "0";
		//10 手术人员
		StringBuffer strs9 = new StringBuffer();
		//codePhyOp 主刀
		strs9.append(SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap,"pkEmpPhyOp")),"code"));
		strs9.append("#");
		//codeAsis 一助
		strs9.append(SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap,"pkEmpAsis")),"code"));
		strs9.append("#");
		//codeAsis2  二助
		strs9.append(SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap,"pkEmpAsis2")),"code"));
		strs9.append("#");
		//codeAsis3 三助
		strs9.append(SDMsgUtils.getPropValueStr(sDQueryUtils.getUserCodeByPkUser(SDMsgUtils.getPropValueStr(paramMap,"pkEmpAsis3")),"code"));
		strs[9] = strs9.toString();
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
		strs[15] = new StringBuffer().append(codeOp).append("#").append(nameOp).append("#").append(descOp).toString();
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
		zop.parse(msgCreateUtil.createZMsgStr(strs));	
		return orm_o01;
	}
	
	
	/**
	 * 手术医嘱确认（（创建ORR_O02消息））
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public ORR_O02 createORR_O02Msg(String msgId,Map<String, Object> paramMap) throws HL7Exception{
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ORR");
		paramMap.put("triggerevent", "O02");
		ORR_O02 orr_o02 = new ORR_O02();
		Map<String,Object> patmap = (Map<String, Object>) paramMap.get("patmap");
		MSH msh = orr_o02.getMSH();
		msgCreateUtil.createMSHMsg(msh, paramMap);
		PID pid = orr_o02.getRESPONSE().getPATIENT().getPID();
		msgCreateUtil.createPIDMsg(pid, patmap);
		ORC orc = orr_o02.getRESPONSE().getORDER().getORC();
		msgCreateUtil.createORCMsg(orc, paramMap);
		OBR obr = orr_o02.getRESPONSE().getORDER().getOBR();
		msgCreateUtil.createOBRMsg(obr,paramMap);
		return orr_o02;
	}

	/**
	 * 住院发票（创建ZPI_PMI）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public ZPI_PMI createZPI_PMIMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception  {
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ZPI");
		paramMap.put("triggerevent", "PMI");
		ZPI_PMI zpi_pmi = new ZPI_PMI();
		MSH msh = zpi_pmi.getMSH();
		msgCreateUtil.createMSHMsg(msh, paramMap);
		PID pid = zpi_pmi.getPID();
 		msgCreateUtil.createPIDMsg(pid, paramMap);
		PV1 pv1 = zpi_pmi.getPV1();
		msgCreateUtil.createPV1Msg(pv1, paramMap);
		FT1 ft1 = zpi_pmi.getFT1();
		msgCreateUtil.createFT1Msg(ft1, paramMap);
		ZFP zfp = zpi_pmi.getZFP();
		msgCreateUtil.createZFPMsg(zfp, paramMap);
		//一对多
		List<Map<String,Object>> amountList= (List<Map<String, Object>>) paramMap.get("amountlist");
		int i=0;
		if(amountList!=null){
			for (Map<String, Object> map : amountList) {
				PMI pmi = zpi_pmi.getPMI(i);
				map.put("fpbz", SDMsgUtils.getPropValueStr(paramMap, "fpbz"));
				msgCreateUtil.createPMIMsg(pmi, map);
				i++;
			}
		}
		return zpi_pmi;
	}
	/**
	 * 催缴预交金（深大项目）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public ZDL_TXL createZDL_TXLMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		if("ZYYJ".equals(SDMsgUtils.getPropValueStr(paramMap, "type"))){
			//查询所需数据
			List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
			if(null != queryPatList && queryPatList.size() > 0 ){
				paramMap.putAll(queryPatList.get(0));
			}
		}
		
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ZDL");
		paramMap.put("triggerevent", "TXL");
		ZDL_TXL zdl_txl = new ZDL_TXL();
		MSH msh = zdl_txl.getMSH();
		msgCreateUtil.createMSHMsg(msh, paramMap);
		
		if("ZYYJ".equals(SDMsgUtils.getPropValueStr(paramMap, "type"))){
			PID pid = zdl_txl.getPID();
	 		msgCreateUtil.createPIDMsg(pid, paramMap);
			PV1 pv1 = zdl_txl.getPV1();
			msgCreateUtil.createPV1Msg(pv1, paramMap);
		}
		
		TXD txd = zdl_txl.getTXD();
		msgCreateUtil.createTXDMsg(txd, paramMap);
		return zdl_txl;
	}
	
	/**
	 * 反馈住院消息（RSP^ZDI）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public RSP_ZDI createRSP_ZDIMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		RSP_ZDI rsp_zdi = new RSP_ZDI();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZDI");
		//paramMap.put("receive", "MIH");
		msgCreateUtil.createMSHMsg(rsp_zdi.getMSH(), paramMap);
		String type = SDMsgUtils.getPropValueStr(paramMap, "type");
		//主要信息
		if("Theme".equals(type)){
			List<Map<String, Object>> paramMapList = (List<Map<String, Object>>) paramMap.get("paramMapList");
			if(paramMapList!=null && paramMapList.size()>0){
				paramMap.put("situation", "AA");
				paramMap.put("msaText", "成功");
				msgCreateUtil.createMSAMsg(rsp_zdi.getMSA(), paramMap);
				//PID 数据
				Map<String, Object> map = paramMapList.get(0);
				msgCreateUtil.createPIDMsg(rsp_zdi.getPID(), map);
				int size = paramMapList.size();
				for(int i=0;i<size;i++){
					msgCreateUtil.createPV1Msg(rsp_zdi.getPV1(i), paramMapList.get(i));
					//查询诊断信息
					List<Map<String, Object>> diags = sDQueryUtils.queryDiagByPkpv(SDMsgUtils.getPropValueStr(paramMapList.get(i), "pkPv"));
					if(diags!=null && diags.size()>0){
						//rsp_zdi.insertDG1(i);
						diags.get(0).put("sort",i);
						msgCreateUtil.createDG1MsgWechat(rsp_zdi.getDG1(i), diags.get(0));
					}else{
						//退诊或者诊断为空处理
						Map<String,Object> sortMap = new HashMap<>(4);
						sortMap.put("sort",i);
						sortMap.put("nameDiag","未录入");
						msgCreateUtil.createDG1MsgWechat(rsp_zdi.getDG1(i), sortMap);
					}
				}
			}else {
				paramMap.put("situation", "AR");
				paramMap.put("msaText", "处理完成：未找到相关患者信息。请确认。。。");
				msgCreateUtil.createMSAMsg(rsp_zdi.getMSA(), paramMap);
			}
		}else if("Details".equals(type)){
			//详细信息
			paramMap.put("situation", "AA");
			paramMap.put("msaText", "成功");
			msgCreateUtil.createMSAMsg(rsp_zdi.getMSA(), paramMap);
			msgCreateUtil.createPIDMsg(rsp_zdi.getPID(), paramMap);
			msgCreateUtil.createPV1Msg(rsp_zdi.getPV1(), paramMap);
			//预交金集合
			List<Map<String,Object>> zivList = (List<Map<String, Object>>) paramMap.get("zivList");
			if(zivList!=null && zivList.size()>0){
				int size = zivList.size();
				for(int i=0;i<size;i++){
					msgCreateUtil.createZIVMsg(rsp_zdi.getZIV(i), zivList.get(i));
				}
			}
			//诊断集合
			List<Map<String,Object>> diags = (List<Map<String, Object>>) paramMap.get("diags");
			if(diags!=null && diags.size()>0){
				int size = diags.size();
				for(int i=0;i<size;i++){
					msgCreateUtil.createDG1MsgWechat(rsp_zdi.getDG1(i), diags.get(i));
				}
			}
		}else {
			paramMap.put("situation", "AR");
			paramMap.put("msaText", "失败：消息数据有误");
			msgCreateUtil.createMSAMsg(rsp_zdi.getMSA(), paramMap);
		}
		return rsp_zdi;
	}

	/**
	 * 不启用
	 * 	发送RAS_O17消息，医嘱执行
	 * @param msgId
	 * @param paramMap
	 * @param typestatus
	 * @return
	 * @throws HL7Exception
	 */
	public Message createRAS_O17Msg(String msgId,Map<String, Object> paramMap, String typestatus) throws HL7Exception {
		//查询所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RAS");
		paramMap.put("triggerevent", "O17");
		paramMap.put("typestatus", typestatus);
		switch (typestatus){
			case "ADD":paramMap.put("control", "NW");break;
			case "DEL":paramMap.put("control", "CA");break;
			case "UPDATE":paramMap.put("control", "RU");break;
			default:paramMap.put("control", "NW");break;
		}
		RAS_O17 ras = new RAS_O17();
		MSH msh = ras.getMSH();
		msgCreateUtil.createMSHMsg(msh, paramMap);
		PID pid = ras.getPATIENT().getPID();
		PV1 pv1 = ras.getPATIENT().getPATIENT_VISIT().getPV1();
		
		Map<String,Object> param=new HashMap<>();
		param.put("pkPv", paramMap.get("pkPv"));
		param.put("pkPi", paramMap.get("pkPi"));
		sDMsgSendAdt.qryAndSetPID_PV1(pid, pv1, param);
		ORC orc = ras.getORDER(0).getORC();
		msgCreateUtil.createORCMsg(orc, paramMap);
		RXO rxo = ras.getORDER(0).getORDER_DETAIL().getRXO();
		msgCreateUtil.createRXOMsg(rxo, paramMap);
		NTE nte = ras.getORDER(0).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
		msgCreateUtil.createNTEMsg(nte,paramMap);
		RXR rxr = ras.getORDER(0).getRXR();
		msgCreateUtil.createRXRMsg(rxr,paramMap);
		RXA rxa = ras.getORDER(0).getRXA();
		msgCreateUtil.createRXAMsg(rxa,paramMap);
		return ras;
	}

	/**
	 * （不启用）
	 * 创建NTE消息
	 * @param nte
	 * @param paramMap
	 * @throws DataTypeException 
	 */
	public NTE createNTEMsg(NTE nte, Map<String, Object> paramMap) throws DataTypeException {
		msgCreateUtil.createNTEMsg(nte,paramMap);
		return nte;
	}
	
	/**
	 * 创建RXR消息
	 * @param rxr
	 * @param paramMap
	 * @throws DataTypeException 
	 */
	public RXR createRXRMsg(RXR rxr, Map<String, Object> paramMap) throws DataTypeException {
		msgCreateUtil.createRXRMsg(rxr,paramMap);
		return rxr;
	}

	/**
	 * 查询患者信息(住院)QBP^Q21
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createQBP_Q21Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "K21");
		//paramMap.put("receive", "MIH");
		RSP_K21 rsp_k21 = new RSP_K21();
		msgCreateUtil.createMSHMsg(rsp_k21.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(rsp_k21.getMSA(), paramMap);
		if("AA".equals(SDMsgUtils.getPropValueStr(paramMap, "situation"))){
			msgCreateUtil.createPIDMsg(rsp_k21.getQUERY_RESPONSE().getPID(), paramMap);
		}
		return rsp_k21;
	}
	
	/**
	 * 查询费用类别统计(TotalExpenses)
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createRSP_ZPIMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		//查询所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZPI");
		//paramMap.put("receive", "MIH");
		paramMap.put("situation", "AA");
		paramMap.put("msaText", "成功");
		RSP_ZPI rsp_zpi = new RSP_ZPI();
		msgCreateUtil.createMSHMsg(rsp_zpi.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(rsp_zpi.getMSA(), paramMap);
		msgCreateUtil.createPIDMsg(rsp_zpi.getPID(), paramMap);
		msgCreateUtil.createPV1Msg(rsp_zpi.getPV1(), paramMap);
		List<Map<String, Object>> queryCost = sDQueryUtils.queryCost(SDMsgUtils.getPropValueStr(paramMap, "pkPv"));
		if(queryCost!=null && queryCost.size()>0){
			for(int i=0;i<queryCost.size();i++){
				msgCreateUtil.createZPOMsg(rsp_zpi.getZPO(i), queryCost.get(i));
			}
		}
		return rsp_zpi;
	}

	/**
	 *  押金缴入(住院ACK_P03) 接收 DFT^P03  WeChat（深大项目）（深大项目）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createACK_P03Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		//查询所需数据
//		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
//		if(null != queryPatList && queryPatList.size() > 0 ){
//			paramMap.putAll(queryPatList.get(0));
//		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ACK");
		paramMap.put("triggerevent", "P03");
		paramMap.put("receive", "MIH");
		//paramMap.put("situation", "AA");
		//paramMap.put("msaText", "成功");
		ACK_P03 ack_p03 = new ACK_P03();
		msgCreateUtil.createMSHMsg(ack_p03.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(ack_p03.getMSA(), paramMap);
		msgCreateUtil.createPIDMsg(ack_p03.getPID(), paramMap);
		msgCreateUtil.createPV1Msg(ack_p03.getPV1(), paramMap);
		msgCreateUtil.createZPOMsg(ack_p03.getZPO(), paramMap);
		return ack_p03;
	}
	/**
	 * 查询押金缴入状态(RSP_ZYJ) 接收 QBP^ZYJ WeChat（深大项目）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createRSP_ZYJMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		//查询所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZYJ");
		//paramMap.put("receive", "MIH");
		RSP_ZYJ rsp_zyj = new RSP_ZYJ();
		msgCreateUtil.createMSHMsg(rsp_zyj.getMSH(), paramMap);
		//msgCreateUtil.createPIDMsg(rsp_zyj.getPID(), paramMap);
		//msgCreateUtil.createPV1Msg(rsp_zyj.getPV1(), paramMap);
		msgCreateUtil.createQPDMsg(rsp_zyj.getQPD(), paramMap);
		String codePv = SDMsgUtils.getPropValueStr(paramMap, "codePv");
		String bankNo = SDMsgUtils.getPropValueStr(paramMap, "bankNo");
		//根据bankNo
		List<Map<String, Object>> queryDeposit = sDQueryUtils.queryDeposit(bankNo,codePv);
		if(queryDeposit!=null && queryDeposit.size()>0){
			paramMap.put("situation", "AA");
			paramMap.put("msaText", "成功");
			msgCreateUtil.createMSAMsg(rsp_zyj.getMSA(), paramMap);
			msgCreateUtil.createZRIMsg(rsp_zyj.getZRI(), queryDeposit.get(0));
		}else{
			//throw new BusException("未查询到相关记录！");
			paramMap.put("situation", "AR");
			paramMap.put("msaText", "失败");
			msgCreateUtil.createMSAMsg(rsp_zyj.getMSA(), paramMap);
		}
//		List<Map<String, Object>> queryDeposit = sDQueryUtils.queryDeposit(codePv);
//		if(queryDeposit!=null && queryDeposit.size()>0){
//			for(int i=0;i<queryDeposit.size();i++){
//				msgCreateUtil.createZRIMsg(rsp_zyj.getZRI(i), queryDeposit.get(0));
//			}
//		}
		return rsp_zyj;
	}
	/**
	 * 住院费每日账单详情：（深大项目）
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws DataTypeException 
	 */
	public Message createRSP_ZLLMsg(String msgId, Map<String, Object> paramMap) throws DataTypeException {
		//查询所需数据
		List<Map<String, Object>> queryPatList = sDMsgMapper.queryPatList(paramMap);
		if(null != queryPatList && queryPatList.size() > 0 ){
			paramMap.putAll(queryPatList.get(0));
		}
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZYJ");
		RSP_ZZL rsp_zzl = new RSP_ZZL();
		msgCreateUtil.createMSHMsg(rsp_zzl.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(rsp_zzl.getMSA(), paramMap);
		ZDL zdl = rsp_zzl.getZDL();
		zdl.getDj().getMo1_Quantity().setValue("1");
		zdl.getDw().setValue("12312");;
		return rsp_zzl;
	}

	/**
	 * 医嘱消息OMP_O09
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createOMP_O09Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		OMP_O09 omp_o09= new OMP_O09();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "OMP");
		paramMap.put("triggerevent", "O09");
		msgCreateUtil.createMSHMsg(omp_o09.getMSH(), paramMap);
		msgCreateUtil.createPIDMsg(omp_o09.getPATIENT().getPID(), paramMap);
		msgCreateUtil.createPV1Msg(omp_o09.getPATIENT().getPATIENT_VISIT().getPV1(), paramMap);
		List<Map<String,Object>> orderList = (List<Map<String, Object>>) paramMap.get("orderList");
		if(orderList!=null && orderList.size()>0){
			for(int i=0;i<orderList.size(); i++){
				Map<String, Object> map = orderList.get(i);
				map.put("triggerevent", "O09");
				map.put("control", SDMsgUtils.getPropValueStr(paramMap, "control"));//申请控制（深圳项目需要）
				map.put("ordStatus", SDMsgUtils.getPropValueStr(paramMap, "ordStatus"));//医嘱状态
				msgCreateUtil.createORCMsg(omp_o09.getORDER(i).getORC(), map);
				msgCreateUtil.createRXOMsg(omp_o09.getORDER(i).getRXO(), map);
				msgCreateUtil.createNTEMsgForOmpO09(omp_o09.getORDER(i).getNTE(), map);
				msgCreateUtil.createRXRMsg(omp_o09.getORDER(i).getRXR(), map);
			}
		}
		return omp_o09;
	}
	/**
	 * 检验确认消息
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createORL_O22Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		ORL_O22 orl_o22 = new ORL_O22();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ORL");
		paramMap.put("triggerevent", "O22");
		msgCreateUtil.createMSHMsg(orl_o22.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(orl_o22.getMSA(), paramMap);
		msgCreateUtil.createPIDMsg(orl_o22.getRESPONSE().getPATIENT().getPID(), paramMap);
		msgCreateUtil.createORCMsg(orl_o22.getRESPONSE().getPATIENT().getGENERAL_ORDER(0).getORDER(0).getORC(), paramMap);
		msgCreateUtil.createOBRMsg(orl_o22.getRESPONSE().getPATIENT().getGENERAL_ORDER(0).getORDER(0).getOBSERVATION_REQUEST().getOBR(), paramMap);
		return orl_o22;
	}
	/**
	 * 检验申请
	 * @param msgId
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public Message createOML_O21Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		OML_O21 oml_o21= new OML_O21();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "OML");
		paramMap.put("triggerevent", "O21");
		msgCreateUtil.createMSHMsg(oml_o21.getMSH(), paramMap);
		msgCreateUtil.createPIDMsg(oml_o21.getPATIENT().getPID(), paramMap);
		msgCreateUtil.createPV1Msg(oml_o21.getPATIENT().getPATIENT_VISIT().getPV1(), paramMap);
		msgCreateUtil.createORCMsg(oml_o21.getORDER_GENERAL(0).getORDER(0).getORC(), paramMap);
		msgCreateUtil.createOBRMsg(oml_o21.getORDER_GENERAL(0).getORDER(0).getOBSERVATION_REQUEST().getOBR(), paramMap);
		msgCreateUtil.createDG1Msg(oml_o21.getORDER_GENERAL(0).getORDER(0).getOBSERVATION_REQUEST().getDG1(), paramMap);
		return oml_o21;
	}
	/**
	 * 检查确认
	 * @param msgId
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public Message createORG_O20Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		ORG_O20 org_o20= new ORG_O20();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "ORG");
		paramMap.put("triggerevent", "O20");
		msgCreateUtil.createMSHMsg(org_o20.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(org_o20.getMSA(), paramMap);
		msgCreateUtil.createPIDMsg(org_o20.getRESPONSE().getPATIENT().getPID(), paramMap);
		msgCreateUtil.createORCMsg(org_o20.getRESPONSE().getORDER(0).getORC(), paramMap);
		msgCreateUtil.createOBRMsg(org_o20.getRESPONSE().getORDER(0).getOBR(), paramMap);
		return org_o20;
		
	}
	/**
	 * 检查申请(OMG_O19)
	 * @param msgId
	 * @param paramMap
	 * @throws HL7Exception 
	 */
	public Message createOMG_O19Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		OMG_O19 omg_o19= new OMG_O19();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "OMG");
		paramMap.put("triggerevent", "O19");
		msgCreateUtil.createMSHMsg(omg_o19.getMSH(), paramMap);
		msgCreateUtil.createPIDMsg(omg_o19.getPATIENT().getPID(), paramMap);
		msgCreateUtil.createPV1Msg(omg_o19.getPATIENT().getPATIENT_VISIT().getPV1(), paramMap);
		msgCreateUtil.createORCMsg(omg_o19.getORDER(0).getORC(), paramMap);
		msgCreateUtil.createOBRMsg(omg_o19.getORDER(0).getOBR(), paramMap);
		/**
		 * identifier: SIGN:临床症状及体征 HISTORY:既往病史  RIS:检查结果 LIS:检验结果 DRUG:用药情况 REMARK:备注 DIAG:诊断  SITE：部位描述
		 */
		String [] obxStr={"SIGN","DIAG","SITE"};
		for (int j = 0; j < obxStr.length; j++) {
			OBX obx = omg_o19.getORDER().getOBSERVATION(j).getOBX();
			obx.getValueType().setValue("ST");
			obx.getObservationIdentifier().getIdentifier().setValue(obxStr[j]);
			String obxNote="";
			if(obxStr[j].equals("SIGN")) obxNote=SDMsgUtils.getPropValueStr(paramMap, "noteDise");
			else if(obxStr[j].equals("DIAG"))obxNote=SDMsgUtils.getPropValueStr(paramMap, "descDiag");
			else if(obxStr[j].equals("SITE"))obxNote=SDMsgUtils.getPropValueStr(paramMap, "nameOrd");
			obx.getObservationValue(0).parse(obxNote);
		}

		return omg_o19;
	}
	/**
	 * 会诊申请消息(REF_I12)
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public REF_I12 createREF_I12Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		REF_I12 ref_i12 = new REF_I12();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "REF");
		paramMap.put("triggerevent", "I12");
		paramMap.put("receive", "CDR");
		msgCreateUtil.createMSHMsg(ref_i12.getMSH(), paramMap);
		Map<String,Object> pati = (Map<String, Object>) paramMap.get("pati");
		msgCreateUtil.createPIDMsg(ref_i12.getPID(), pati);
		msgCreateUtil.createPV1Msg(ref_i12.getPATIENT_VISIT().getPV1(), pati);
		msgCreateUtil.createDG1Msg(ref_i12.getDG1(), paramMap);
		msgCreateUtil.createRF1Msg(ref_i12.getRF1(), paramMap);
		msgCreateUtil.createOBRMsg(ref_i12.getOBSERVATION(0).getOBR(), paramMap);
		List<Map<String,Object>> consultResponse = (List<Map<String, Object>>) paramMap.get("consultResponse");
		for(int i=0;i<consultResponse.size();i++){
			msgCreateUtil.createPRDMsg(ref_i12.getPROVIDER_CONTACT(i).getPRD(), consultResponse.get(i));
		}
		return ref_i12;
	}
	/**
	 * 会诊应答消息(RRI_I12)
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception
	 */
	public RRI_I12 createRRI_I12Msg(String msgId, Map<String, Object> paramMap) throws HL7Exception{
		RRI_I12 rri_i12 = new RRI_I12();
		paramMap.put("msgid", msgId);
		paramMap.put("msgtype", "RRI");
		paramMap.put("triggerevent", "I12");
		paramMap.put("receive", "CDR");
		msgCreateUtil.createMSHMsg(rri_i12.getMSH(), paramMap);
		Map<String,Object> pati = (Map<String, Object>) paramMap.get("pati");
		msgCreateUtil.createPIDMsg(rri_i12.getPID(), pati);
		msgCreateUtil.createPV1Msg(rri_i12.getPATIENT_VISIT().getPV1(), pati);
		msgCreateUtil.createDG1Msg(rri_i12.getDG1(), paramMap);
		msgCreateUtil.createRF1Msg(rri_i12.getRF1(), paramMap);
		msgCreateUtil.createOBRMsg(rri_i12.getOBSERVATION(0).getOBR(), paramMap);
		List<Map<String,Object>> consultResponse = (List<Map<String, Object>>) paramMap.get("consultResponse");
		for(int i=0;i<consultResponse.size();i++){
			msgCreateUtil.createPRDMsg(rri_i12.getPROVIDER_CONTACT(i).getPRD(), consultResponse.get(i));
			msgCreateUtil.createOBXMsg(rri_i12.getOBSERVATION().getRESULTS_NOTES(i).getOBX(), consultResponse.get(i));
		}
		return rri_i12;
	}
	/**
	 * 默认消息定义
	 * @return
	 * @throws DataTypeException 
	 */
	public ACK createAckMsg(Map<String, Object> paramMap) throws DataTypeException{
		ACK ack = new ACK();
		msgCreateUtil.createMSHMsg(ack.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(ack.getMSA(), paramMap);
		return ack;
	}

	/**
	 * 门诊诊间支付ACK^P03消息构建
	 * @param paramMap
	 * @return
	 * @throws DataTypeException
	 */
	public ACK_P03 createAckMsgForOpDft(Map<String,Object> paramMap) throws DataTypeException{
		ACK_P03 ack = new ACK_P03();
		msgCreateUtil.createMSHMsg(ack.getMSH(), paramMap);
		msgCreateUtil.createMSAMsg(ack.getMSA(), paramMap);
		PID pid=ack.getPID();
		pid.getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOp"));
		PV1 pv1=ack.getPV1();
		pv1.getPatientClass().setValue("O");
		pv1.getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(paramMap, "codePv"));
		return ack;
	}
	/**
	 * 已缴费记录明细查询 RSP^ZDL
	 * @param msgId
	 * @param paramMap
	 * @return
	 * @throws HL7Exception 
	 */
	public Message createRSP_ZDLMsg(String msgId, Map<String, Object> paramMap) throws HL7Exception {
		RSP_ZDL rsp_zdl = new RSP_ZDL();
		paramMap.put("msgid", msgId);
		msgCreateUtil.createMSHMsg(rsp_zdl.getMSH(),paramMap);
		msgCreateUtil.createMSAMsg(rsp_zdl.getMSA(),paramMap);
		//msgCreateUtil.createERRMsg(rsp_zdl.getERR(),paramMap);
		msgCreateUtil.createQAKMsg(rsp_zdl.getQAK(),paramMap);
		msgCreateUtil.createQPDMsg(rsp_zdl.getQPD(),paramMap);
		msgCreateUtil.createPIDMsg(rsp_zdl.getPID(),paramMap);
		msgCreateUtil.createPV1Msg(rsp_zdl.getPV1(),paramMap);
		msgCreateUtil.createZIVMsg(rsp_zdl.getZIV(),paramMap);
		msgCreateUtil.createZDLMsg(rsp_zdl.getZDL(),paramMap);
		return rsp_zdl;
	}

}
