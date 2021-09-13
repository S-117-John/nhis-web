package com.zebone.nhis.ma.lb.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.RSP_K21;
import ca.uhn.hl7v2.model.v24.segment.MSH;
import ca.uhn.hl7v2.model.v24.segment.QAK;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.OrderParam;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.cn.pub.service.OrderPubService;
import com.zebone.nhis.common.module.cn.cp.CpRec;
import com.zebone.nhis.common.module.cn.ipdw.*;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.lb.dao.CpMsgMapper;
import com.zebone.nhis.ma.pub.platform.zb.comm.MsgSenderHander;
import com.zebone.nhis.ma.pub.platform.zb.send.MsgSendCn;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 临床路径对接服务
 * @author IBM
 *
 */
@Service
public class CpMsgService {
	private static Logger log = org.slf4j.LoggerFactory.getLogger(SelfMsgService.class);

	@Resource
	private OrderPubService orderPubService;

	@Resource
	private CommonService commonService;

	@Resource
	private MsgSenderHander msgSender;

	@Autowired
	private MsgSendCn msgSendCn;

	@Autowired
	private CpMsgMapper cpMsgMapper;

	@Autowired
	private CnPubService cnPubService;

	private SysMsgRec msgrecParam = new SysMsgRec();

	//用于接收解析查询患者状态相应的消息--是否纳入路径
	private String isExtCp = null;


	/**
	 * 灵璧临床路径-平台-his交互信息处理
	 * @param msgRec
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String handleSysMsgRecExtCp(SysMsgRec msgRec)throws HL7Exception, ParseException{
		Parser parser = new GenericParser();
		if(msgrecParam == null){
			msgrecParam = msgRec;
			//msgrecParam.setPkMsg(msgRec.getPkMsg());
		}
		String msg=msgRec.getMsgContent();

		Message hapiMsg=null;
		try {
			hapiMsg = parser.parse(msg);

		} catch (Exception e) {
			// TODO: handle exception
			log.info("parse error");
			e.printStackTrace();
			return "";
		}

		if(hapiMsg==null){
			log.info("hapiMsg is null");
			return "";
		}

		Segment segment = (Segment) hapiMsg.get("MSH");
		MSH msh=(MSH)segment;

		String msgType = msh.getMessageType().encode();

		log.info("msgType:"+msgType);

		//接受信息消息
		if("RSP^K21".equals(msgType)){
			isExtCp = receiveOrderInfo((RSP_K21)hapiMsg);
			return isExtCp;
		}
		return "";
	}

	/**
	 * 接收保存诊断之后的反馈消息
	 * @param hapiMsg
	 * @return
	 */
	private String receiveOrderInfo(RSP_K21 rsp) {
		QAK qak = rsp.getQAK();
		String value = qak.getQueryTag().getValue();
		return value;
	}

	/**
	 * 根据解析到的消息，判断患者是否纳入路径
	 * @param param
	 * @param user
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public List<String> receivePatientStage(String param,IUser user) throws HL7Exception, ParseException{
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<String> arrayList = new ArrayList<>();
		MSH msh = msgSender.RSPmsh;
		String msgId = msh.getMessageControlID().getValue().replace("-", "");
		SysMsgRec queryForBean = DataBaseHelper.queryForBean("select err_txt from sys_msg_rec where msg_id=?", SysMsgRec.class, new Object[]{msgId});
		arrayList.add(queryForBean.getErrTxt());
		return arrayList;
	}

	/**
	 * 发送患者状态信息
	 * @param paramMap
	 */
	private void sendPiStage(Map<String, Object> paramMap) {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch!=null){
			try {
				msgSendCn.sendPatientInfo("Q21", paramMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！");
			}
		}
	}

	/**
	 * 保存临床路径医嘱信息
	 * @param orders
	 * @return
	 */
	public void saveExtCpCnOrder(String param , IUser user){
		OrderParam list = JsonUtil.readValue(param,OrderParam.class);
		List<CnOrder> addList = list.getNewOrdList();
		List<CnRisApply> addRisList = new ArrayList<CnRisApply>();
		List<CnLabApply> addLisList = new ArrayList<CnLabApply>();
		List<CnOrdHerb> addHerbList = new ArrayList<CnOrdHerb>();//草药医嘱
		User loginUser = (User)user ;
		Date d = new Date();
		Map<String, Object> mapParam = new HashMap<String,Object>();
		String jsonParam = null;
		CnPrescription presVo = null;//草药处方
		for(CnOrder addCnOrder :addList){
			//科室编码转换
			jsonParam = createParam(loginUser,"bd_ou_dept","pk_dept","code_dept",addCnOrder.getPkDeptExec());
			String pkDeptExec = commonService.getDictPkByCode(jsonParam, loginUser);
			mapParam.clear();
			addCnOrder.setPkOrd(pkDeptExec);
			//剂量单位转换
			jsonParam = createParam(loginUser,"bd_unit","pk_unit","code",addCnOrder.getPkUnitDos());
			String pkUnitDos = commonService.getDictPkByCode(jsonParam, loginUser);
			mapParam.clear();
			addCnOrder.setPkOrd(pkUnitDos);
			//用量单位转换
			jsonParam = createParam(loginUser,"bd_unit","pk_unit","code",addCnOrder.getPkUnit());
			String pkUnit = commonService.getDictPkByCode(jsonParam, loginUser);
			mapParam.clear();
			addCnOrder.setPkOrd(pkUnit);

			if(addCnOrder.getCodeOrdtype().startsWith("01")){//根据codeOrdtype医嘱类型判断，01开头的是药品，然后医嘱项目存药品主键
				addCnOrder.setFlagDurg("1");
				jsonParam = createParam(loginUser,"bd_pd","pk_pd","code",addCnOrder.getCodeOrd());
				String pkOrd = commonService.getDictPkByCode(jsonParam, loginUser);
				mapParam.clear();
				addCnOrder.setPkOrd(pkOrd);
			}
			else{//不是01开头的是非药品，然后医嘱项目存医嘱项目主键
				addCnOrder.setFlagDurg("0");
				jsonParam = createParam(loginUser,"bd_ord","pk_ord","code",addCnOrder.getCodeOrd());
				String pkOrd = commonService.getDictPkByCode(jsonParam, loginUser);
				mapParam.clear();
				addCnOrder.setPkOrd(pkOrd);
			}
			addNewCnOrder(addRisList, addLisList,addHerbList, loginUser, d, addCnOrder);
		}

		List<CnOrder> herbList = new ArrayList<CnOrder>();//存放的所有草药
		if(addList!=null && addList.size()>0){
			for (CnOrder cn : addList) {
				if(cn.getCodeOrdtype().startsWith("0103")){//如果是草药的话，先不保存cn_order表
					herbList.add(cn);
				}
			}
			if(herbList.size()>0){
				addList.removeAll(herbList);//这个集合中就是除去草药的所有医嘱，然后保存
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), addList);
		}
		//保存检验
		if(addRisList!=null && addRisList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnRisApply.class), addRisList);
		}
		//保存检查
		if(addLisList!=null && addLisList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnLabApply.class), addLisList);
		}

		Date dt = new Date();
		Date dd = cnPubService.getOutOrdDate(addList.get(0).getPkPv());
		if (dd != null && dd.compareTo(dt) < 0) {
			dt = dd;
		}
		//拼接医嘱中显示的草药名称
		String OrdName = "";
		int i=0;
		for (CnOrder item :herbList)
		{
			if (i <= 2)
			{
				if (OrdName != null && !OrdName.equals("")) {
					OrdName += "," + item.getNameOrd();
				}
				else OrdName = item.getNameOrd();
				i++;
			}
		}
		CnOrder order = null;
		if(addHerbList != null && addHerbList.size() > 0){
			//保存处方
			presVo = new CnPrescription();
			presVo.setPkPres(NHISUUID.getKeyId());
			presVo.setPkOrg(loginUser.getPkOrg());
			presVo.setPkPv(addList.get(0).getPkPv());
			presVo.setPkPi(addList.get(0).getPkPi());
			presVo.setDtPrestype("02");//02 草药处方
			presVo.setPresNo(ApplicationUtils.getCode("0407"));//处方号
			presVo.setDatePres(dt);
			presVo.setPkDept(loginUser.getPkDept());
			presVo.setPkDeptNs(herbList.get(0).getPkDeptNs());//开立病区
			presVo.setPkEmpOrd(loginUser.getPkEmp());
			presVo.setNameEmpOrd(loginUser.getNameEmp());
			presVo.setFlagPrt(Constants.FALSE);
			presVo.setEuBoil(herbList.get(0).getEuBoil());
			presVo.setTs(dt);
			DataBaseHelper.insertBean(presVo);
			//保存医嘱
			order.setTs(dt);
			order.setPkCnord(NHISUUID.getKeyId());
			order.setCodeApply(null);
			order.setPkPres(presVo.getPkPres());
			order.setCodeOrdtype("0103");					//草药类型，bd_ordtype
			order.setEuAlways("1");
			order.setOrdsn(0);	//医嘱号				
			order.setDescOrd(order.getNameOrd());
			order.setCodeFreq("once");//频次
			order.setQuan(1.0);
			order.setQuanCg(1.0);
			order.setEuStatusOrd("0");		//签署
			order.setDateEnter(dt);			//暂用服务器时间，db时间后期处理
			order.setDateStart(dt);
			order.setPkDept(loginUser.getPkDept());
			order.setPkDeptNs(order.getPkDeptNs());
			order.setPkEmpInput(loginUser.getPkEmp());
			order.setNameEmpInput(loginUser.getUserName());
			order.setEuIntern(order.getEuIntern());
			order.setEuPvtype("3");			//住院患者
			order.setOrdsnParent(order.getOrdsn());
			order.setNameOrd(order.getNameOrd()==null?"草药医嘱":OrdName);
			order.setDescOrd(order.getNameOrd()==null?"草药医嘱":OrdName);
			order.setCodeOrd("DEF99999");
			order.setFlagDoctor("1");
			order.setFlagDurg("1");
			DataBaseHelper.insertBean(order);

			for(CnOrdHerb herb : addHerbList){
				if(herb.getPkPd()!=null && !"".equals(herb.getPkPd()) ){
					herb.setPkCnord(order.getPkCnord());
					herb.setDelFlag(Constants.FALSE);
					herb.setTs(dt);
					DataBaseHelper.insertBean(herb);
				}else{
					if(herb.getPkOrdherb()!=null && !"".equals(herb.getPkOrdherb())) {
						DataBaseHelper.deleteBeanByPk(herb);
					}
				}
			}
		}
	}

	/**
	 * 组建参数
	 * @param loginUser
	 * @param tableName
	 * @param pkColName
	 * @param codeColName
	 * @param code
	 * @return
	 */
	private String createParam(User loginUser, String tableName, String pkColName,String codeColName, String code) {
		Map<String, Object> mapParam = new HashMap<String,Object>();
		mapParam.put("pkOrg", loginUser.getPkOrg());
		mapParam.put("tableName", tableName);
		mapParam.put("pkColName", pkColName);
		mapParam.put("codeColName", codeColName);
		mapParam.put("code", code);
		return JsonUtil.writeValueAsString(mapParam);
	}

	private void addNewCnOrder(List<CnRisApply> addRisList,List<CnLabApply> addLisList, List<CnOrdHerb> addHerbList, User loginUser, Date d,CnOrder addCnOrder) {
		String pkCnord = NHISUUID.getKeyId();
		addCnOrder.setPkCnord(pkCnord);
		addCnOrder.setEuStatusOrd("0");
		addCnOrder.setTs(d);
		addCnOrder.setDateEnter(d);
		addCnOrder.setPackSize(1.00); //pkUnit取基本单位，故取1
		String codeOrdtype = addCnOrder.getCodeOrdtype();
		if(codeOrdtype.startsWith("03")){//检验
			String CodeApply =  ApplicationUtils.getCode("0402");
			addCnOrder.setCodeApply(CodeApply);
			CnRisApply cnRisApply = new CnRisApply();
			String pkOrdris = NHISUUID.getKeyId();
			cnRisApply.setPkOrdris(pkOrdris);
			cnRisApply.setPkCnord(addCnOrder.getPkCnord());
			cnRisApply.setPkOrg(loginUser.getPkOrg());
			cnRisApply.setEuStatus("0");//检查过程状态0 申请
			cnRisApply.setFlagBed(Constants.FALSE);
			cnRisApply.setFlagPrint(Constants.FALSE);
			cnRisApply.setTs(d);
			cnRisApply.setDescBody(addCnOrder.getDescBody());
			cnRisApply.setDtRistype(addCnOrder.getDtType());
			addRisList.add(cnRisApply);
		}else if(codeOrdtype.startsWith("02")){//检查
			String CodeApply = ApplicationUtils.getCode("0401");
			addCnOrder.setCodeApply(CodeApply);
			CnLabApply cnLabApply = new CnLabApply();
			String pkOrdlis = NHISUUID.getKeyId();
			cnLabApply.setPkOrdlis(pkOrdlis);
			cnLabApply.setPkCnord(addCnOrder.getPkCnord());
			cnLabApply.setEuStatus("0");//检查过程状态0申请
			cnLabApply.setFlagPrt(Constants.FALSE);
			cnLabApply.setTs(d);
			cnLabApply.setDtColtype(addCnOrder.getDtColltype());
			cnLabApply.setDtSamptype(addCnOrder.getDtSamptype());
			cnLabApply.setDtTubetype(addCnOrder.getDtContype());
			addLisList.add(cnLabApply);
		}else if(codeOrdtype.startsWith("0103")){//草药类型
			CnOrdHerb cnOrdHerb = new CnOrdHerb();
			cnOrdHerb.setPkOrdherb(null);
			//cnOrdHerb.setPkCnord(addCnOrder.getPkCnord());
			cnOrdHerb.setPkPd(addCnOrder.getPkOrd());
			cnOrdHerb.setSortNo(null);//序号
			cnOrdHerb.setPrice(addCnOrder.getPriceCg());
			cnOrdHerb.setPkUnit(addCnOrder.getPkUnit());
			cnOrdHerb.setQuan(addCnOrder.getQuan());
			cnOrdHerb.setNoteUse(addCnOrder.getCodeSupply());
			addHerbList.add(cnOrdHerb);
		}
	}


	/**
	 * 更新出入路径信息（含入路径，退路径）
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 * @throws HL7Exception
	 */
	public void updateCpInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		CpRec cpRec = new CpRec();
		if(paramMap.get("pkPv")!=null && !paramMap.equals("")){
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select * from cp_rec where pk_pv = ?", paramMap.get("pkPv"));
			if(queryForMap==null || queryForMap.get("pkCprec")==null || queryForMap.get("pkCprec").equals("")){  //进入路径
				//路径应用记录主键
				cpRec.setPkCprec(NHISUUID.getKeyId());
				//所属机构
				cpRec.setPkOrg(paramMap.get("pkOrg").toString());
				//患者就诊
				cpRec.setPkPv(paramMap.get("pkPv").toString());
//	    	    //记录类型
				cpRec.setEuRectype("1");
//	    	    //记录状态
				cpRec.setEuStatus(paramMap.get("euStatus").toString());
				//启用时间
				cpRec.setDateStart(new Date());
				//启用人
				cpRec.setPkEmpStart(paramMap.get("pkEmp").toString());
				//启用人姓名
				cpRec.setNameEmpStart(paramMap.get("pkNameEmp").toString());
				//是否转路径
				cpRec.setFlagTransfer("0");
				//创建人
				cpRec.setCreator(paramMap.get("pkEmp").toString());
				//创建时间
				cpRec.setCreateTime(new Date());
				//更改时间
				cpRec.setTs(new Date());
				DataBaseHelper.insertBean(cpRec);
				DataBaseHelper.execute("UPDATE PV_ENCOUNTER SET EU_PVMODE = ? WHERE PK_PV = ?",paramMap.get("euPvmode"), paramMap.get("pkPv") );
			}
			else { //退出路径
//	    	    cpRec.setPkCprec(queryForMap.get("pkCprec").toString());
//	    	    cpRec.setPkOrg(paramMap.get("pkOrg").toString());
//	    	    cpRec.setPkPv(paramMap.get("pkPv").toString());
//	    	    cpRec.setEuRectype("1");
//	    	    cpRec.setEuStatus(paramMap.get("euStatus").toString());
//	    	    cpRec.setDateStart(new Date());
//	    	    cpRec.setPkEmpStart(paramMap.get("pkEmp").toString());
//	    	    cpRec.setNameEmpStart(paramMap.get("pkNameEmp").toString());
//	    	    cpRec.setFlagTransfer("0");
//	    	    cpRec.setCreator(paramMap.get("pkEmp").toString());
//	    	    cpRec.setCreateTime(new Date());
//	    	    cpRec.setTs(new Date());
				DataBaseHelper.execute("update cp_rec set eu_status=? ,ts=? where pk_pv =?" ,paramMap.get("euStatus"),new Date(),paramMap.get("pkPv"));
				DataBaseHelper.execute("UPDATE PV_ENCOUNTER SET EU_PVMODE = ? WHERE PK_PV = ?",paramMap.get("euPvmode"),paramMap.get("pkPv") );
			}
		}
	}

	/**
	 * 发送-获取患者状态的消息
	 * @param param
	 * @param user
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public void sendStageMessage(String param,IUser user) throws Exception{
		//接收前台参数--就诊号，科室编码
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//根据患者就诊号和科室获取具体信息
		List<Map<String, Object>> piMasterInfo = cpMsgMapper.getPiMasterInfo(paramMap);
		//姓名
		paramMap.put("namePi", piMasterInfo.get(0).get("namePi"));
		//患者编码
		paramMap.put("codePi", piMasterInfo.get(0).get("codePi"));
		//住院号
		paramMap.put("codeIp", piMasterInfo.get(0).get("codeIp"));
		//就诊号
		paramMap.put("codePv", piMasterInfo.get(0).get("codePv"));
		//科室编码
		paramMap.put("codeDept", piMasterInfo.get(0).get("codeDept"));

		//发送获取患者状态的消息
		sendPiStage(paramMap);
	}

	/**
	 * 获取医嘱编码主键
	 */
	public Map<String, Object> getPkOrd(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String, Object> resMap = new HashMap<>();
		if(paramMap.get("flagDurg").equals("0")){ //非药品医嘱
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select * from bd_ord where code=?", paramMap.get("code"));
			if(queryForMap==null){
				throw new BusException("无法获取医嘱编码主键，检查路径医嘱编码或医嘱类型");
			}
			resMap.put("pkOrd", queryForMap.get("pkOrd").toString());
			resMap.put("labSam", "");
			if(paramMap.get("labSam")!=null && paramMap.get("labSam").equals("03")){
				Map<String, Object> forMap = DataBaseHelper.queryForMap("select * from bd_ord_lab where pk_ord = ?", queryForMap.get("pkOrd").toString());
				resMap.put("labSam", forMap.get("dtSamptype").toString());
			}
			return resMap;
		}else  {                                  //药品医嘱
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select * from bd_pd where code=?", paramMap.get("code"));
			if(queryForMap==null){
				throw new BusException("无法获取医嘱编码主键，检查医嘱编码或医嘱类型");
			}
			resMap.put("pkOrd", queryForMap.get("pkPd").toString());
			resMap.put("labSam", "");
			return resMap;
		}
	}
	/* *
	 * @Description  更新患者临床路径状态 022002001006
	 * @auther wuqiang
	 * @Date 2019-09-30
	 * @Param [param, user]
	 * @return void
	 */
	public void updeCpEuStatus(String param,IUser user){
		String pkPv=JsonUtil.getFieldValue(param,"pkPv");
		String sql=" update  CP_REC set EU_STATUS ='2' where PK_PV=? ";
		DataBaseHelper.update(sql,new Object[]{pkPv});
	}
}
