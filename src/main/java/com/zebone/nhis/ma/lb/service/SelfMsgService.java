package com.zebone.nhis.ma.lb.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.DFT_P03;
import ca.uhn.hl7v2.model.v24.message.QBP_Q21;
import ca.uhn.hl7v2.model.v24.message.SRM_S01;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import ca.uhn.hl7v2.parser.GenericParser;
import ca.uhn.hl7v2.parser.Parser;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.lb.dao.SelfMsgMapper;
import com.zebone.nhis.ma.lb.send.MsgSendSch;
import com.zebone.nhis.ma.lb.send.MsgSendZzj;
import com.zebone.nhis.ma.lb.vo.PvOpVo;
import com.zebone.nhis.ma.pub.platform.zb.utils.MsgUtils;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pv.pub.vo.PvOpAndSettleVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
/**
 * 自助机对接服务
 * @author IBM
 *
 */
@Service
public class SelfMsgService {
	private static Logger log = org.slf4j.LoggerFactory.getLogger(SelfMsgService.class);
	
	@Resource
	private PiPubService piPubService;
	
	@Resource
	private SelfMsgMapper selfMsgMapper;
	
	@Resource
	private MsgSendZzj msgSendZzj;
	
	@Autowired
	private MsgSendSch msgSendSch;		

	/***
	 * 灵璧自助机-平台-his交互信息处理
	 * @param msgRec
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException 
	 */
	public String handleSysMsgRecZZJ(SysMsgRec msgRec)throws Exception{
		Parser parser = new GenericParser();
		
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
		 
		 //机构主键
		 String pkOrg = msh.getContinuationPointer().getValue();
		 if(pkOrg==null || pkOrg.equals("")){
			 return sendError();
		 }
		 		 
		 String msgType = msh.getMessageType().encode();
		 
		 log.info("msgType:"+msgType);
		//接受SRM_S01消息
		if("SRM^S01".equals(msgType)){
			return creatPatiAndSaveRegister((SRM_S01)hapiMsg,msh);
		 }
		//患者信息查询
		if("QBP^Q21".equals(msgType)){
			return queryPiInfo((QBP_Q21)hapiMsg);
		}
		//排班资源信息查询
		if("QBP^Z11".equals(msgType)){
			 //得到QPD
			 Segment qpdSegment  = (Segment) hapiMsg.get("QPD");
			 QPD qpd = (QPD)qpdSegment;
			 
			 String schInfo = qpd.getMessageQueryName().getIdentifier().getValue();//查询类型：当天/预约
			 String queryTag=qpd.getQueryTag().getValue();//排班资源类型：科室/人员
			 
			 //得到RCP
			 Segment rcpSegment  = (Segment) hapiMsg.get("RCP");
			 RCP rcp = (RCP)rcpSegment;
			 
			 String prior = rcp.getQueryPriority().getValue();//查询优先级:紧急/一般
			 Date queryDate = DateUtils.strToDate(rcp.getExecutionAndDeliveryTime().getTimeOfAnEvent().getValue(), "yyyyMMdd");
			 Map<String, Object> hashMap = new HashMap<>();
			 hashMap.put("schInfo", schInfo);
			 hashMap.put("queryTag", queryTag);
			 hashMap.put("prior", prior);
			 hashMap.put("queryDate", queryDate);
			 hashMap.put("pkOrg", pkOrg);
			 return sendSchResource(hashMap);
		}
		//自助机支付接口
		if("DFT^P03".equals(msgType)){
			return registeredPayment((DFT_P03)hapiMsg,pkOrg);
		}
		//自助机退号
		if("SRM^S04".equals(msgType)){
			//得到ARQ
			Segment arqSegment  = (Segment) hapiMsg.get("ARQ");
			ARQ arq = (ARQ)arqSegment;
			//得到PID
			String srm = hapiMsg.toString();
			//得到PID
			String pidMsg = srm.substring(srm.indexOf("PID"),srm.indexOf("PV1")); 
			String[] pidSplit = pidMsg.split("\\|");
			String codePi = pidSplit[2];
			String[] split2 = pidSplit[3].trim().split("\\^");
			String idCard = split2[0];
			String idType = split2[4];
			//得到PV1
			String pv1Msg = srm.trim().substring(srm.indexOf("PV1"));
			String[] pv1Split = pv1Msg.split("\\|");
			String codePv = pv1Split[19];
			Map<String, String> map = new HashMap<String,String>();
			map.put("codePi", codePi);
			map.put("idCard", idCard);
			map.put("idType", idType);
			map.put("pkOrg", pkOrg);
			map.put("codePv", codePv);
			return cancleRegister(map);
		}
		//查询待缴费记录
		if("QRY^Q27".equals(msgType)){
			//得到QPD
			Segment qrdSegment  = (Segment) hapiMsg.get("QRD");
			QRD qrd = (QRD)qrdSegment;
			//查询时间
			String queryTime = qrd.getQueryDateTime().getTs1_TimeOfAnEvent().getValue();
			String patiId = qrd.getWhoSubjectFilter(0).getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().getValue();
			Map<String, String> map = new HashMap<String,String>();
			map.put("queryTime", queryTime);
			map.put("patiId", patiId);
			map.put("pkOrg", pkOrg);
			return queryPatiCgInfoNotSettle(map);
		}
		return "";
	}

	/**
	 * 查询患者待缴费
	 * @param map
	 * @return
	 * @throws HL7Exception
	 */
	private String queryPatiCgInfoNotSettle(Map<String, String> map) throws HL7Exception {
		//根据患者证件号查询患者就诊记录详细信息
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setCodePi(map.get("patiId"));
		String codePi=map.get("patiId");
		//得到患者pk_pi
		List<PibaseVo> opPiMaster = selfMsgMapper.getOpPiMaster(pvEncounter);
		PiMaster pimaster = DataBaseHelper.queryForBean("select * from pi_master where code_pi= ? and del_flag='0'", PiMaster.class, new Object[]{codePi});
		if(pimaster == null){
			return sendErrorPid("暂无患者记录！");
		}
		//获取患者就诊记录
	    //List<PvOpVo> pvOpVo = selfMsgMapper.getPvOpVoTodayListOracle(pimaster.getPkPi(),map.get("pkOrg"));
		
		ResponseJson  opNoSettleRs = null;

		//门诊查询患者未收费项目
		User user = new User();
		user.setPkOrg(map.get("pkOrg"));
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		OpCgTransforVo opNoSettleReglist = null;
		try {
			//调用查询患者未收费项目接口：组建入参
			opNoSettleReglist = creatPiNotSettle(opPiMaster);
		} catch (Exception e2) {
			e2.printStackTrace();
			return sendErrorPid("患者查询未收费项目失败！");
		}
		ApplicationUtils apputil = new ApplicationUtils();
		opNoSettleRs =  apputil.execService("PV", "OpCgSettlementService", "queryPatiCgInfoNotSettleBypkPv",opNoSettleReglist,user);
		if(opNoSettleRs.getStatus()<0){
			return sendErrorPid(opNoSettleRs.getDesc());
		}
		//获得到的参数类型转换
		String objectToJson = ApplicationUtils.objectToJson(opNoSettleRs.getData());
		List<BlPatiCgInfoNotSettleVO> settleList = JsonUtil.readValue(objectToJson, new TypeReference<List<BlPatiCgInfoNotSettleVO>>() {});
		return sendPatiNotSettle(settleList,opPiMaster);
	}

	/**
	 * 查询待缴费消息反馈
	 * @param opNoSettleRs
	 * @param opPiMaster
	 * @return
	 */
	private String sendPatiNotSettle(List<BlPatiCgInfoNotSettleVO> opNoSettleRs, List<PibaseVo> opPiMaster) {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		String message="";
		if(sendMsgSwitch !=null){
			try {
				message=msgSendZzj.sendPatiNotSettle("RAR", opNoSettleRs,opPiMaster);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
		return message;
	}

	/**
	 * 患者建档和挂号
	 * 根据ARQ来判断是患者建档，还是挂号
	 * APP_REG预挂号,APPT_REG挂号结算，APPRA患者建档
	 * @param srm
	 * @param msh 
	 * @param pkOrg 
	 * @throws HL7Exception 
	 */
	public String creatPatiAndSaveRegister(SRM_S01 srm, MSH msh) throws HL7Exception{
		ARQ arq=srm.getARQ();
		PID pid=srm.getPATIENT(0).getPID();
		RGS rgs=srm.getRESOURCES(0).getRGS();
		//获得-排班资源id
		String pkSch = rgs.getResourceGroupID().getIdentifier().getValue();
		//得到AIL
		AIL ail = srm.getRESOURCES(0).getLOCATION_RESOURCE().getAIL();
		//得到机构
		String pkOrg = msh.getContinuationPointer().getValue();
		int count_card =0;//证件号码
		//创建患者个人信息
		PiMaster pimaster=null;
		try {
			pimaster = createPiByPID(pid,pkOrg);
		} catch (Exception e) {
			e.printStackTrace();
			return sendErrorPid("患者信息有误！");
		}
		PiMasterParam pimast=new PiMasterParam();
		List<PiInsurance> insuranceList=new ArrayList<PiInsurance>();
		//创建患者医保计划
		insuranceList.add(createInsur(pid));
		pimast.setInsuranceList(insuranceList);
		if(arq.getAppointmentReason().getIdentifier().getValue().equals("APPRA")){//等于APPRA--患者建档
			String birth = null;
			if(pid.getDateTimeOfBirth().getTimeOfAnEvent().getValue() != null){
				birth = pid.getDateTimeOfBirth().getTimeOfAnEvent().getValue();
			}
			birth = birth.substring(0,4) + "/" + birth.substring(4, 6) + "/" + birth.substring(6, 8);
			pimaster.setBirthDate(DateUtils.strToDate(birth,"yyyy/MM/dd"));//出生日期
			pimast.setMaster(pimaster);
			//处理错误信息
			String msgError = null;//用于接收所有错误提示信息
			//其他信息
			if(pimaster.getNamePi()==null || pimaster.getNamePi().equals("") || pimaster.getMobile()==null || pimaster.getMobile().equals("") || pimaster.getBirthDate()==null || pimaster.getBirthDate().equals("")){
				msgError = "请完善患者信息！";
				return sendErrorPid(msgError);
			}
			//判断身份证
		    count_card = DataBaseHelper .queryForScalar( "select count(1) from pi_master "
						+ "where del_flag = '0' and dt_idtype = ? and id_no = ?", Integer.class,pimaster.getDtIdtype() ,pimaster.getIdNo());
			if(StringUtils.isNotBlank(pimaster.getIdNo())){
				if (count_card != 0)
				{
					msgError = "证件号重复！";
				    return sendErrorPid(msgError);
				}
				else 
					return sendCreatePati(pimast, "O");
			}
		}
		if(arq.getAppointmentReason().getIdentifier().getValue().equals("APP_REG")){//等于APP_REG--挂号
			String msg = MsgUtils.getParser().encode(srm);
			//得到ZAI
			String msgZai = msg.substring(msg.indexOf("ZAI")+1);
			String[] zais = msgZai.split("\\|");
			String toDate = zais[0];
			String begin = zais[2];
			String end = zais[3];
			pimast.setMaster(pimaster);
			return sendPreRegister(pimast,pkSch);
		}else{
			return "";
		}
	}

	/**
	 * 患者建档信息判断
	 * @param msgError
	 * @return
	 * @throws HL7Exception
	 */
	private String sendErrorPid(String msgError) throws HL7Exception {
		SRR_S01 srr=new SRR_S01();
		String msgId=MsgUtils.getMsgId();
		MSH msh=srr.getMSH();		
		MsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		MSA msa=srr.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		msa.getAcknowledgementCode().setValue("AR");
	    msa.getTextMessage().setValue(msgError);
	    String msg = MsgUtils.getParser().encode(srr);
        return msg;
	}

	/**
	 * 患者信息查询
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception 
	 */
	private String queryPiInfo(QBP_Q21 qbp) throws HL7Exception {
		String msg = MsgUtils.getParser().encode(qbp);
		String msgQpd = msg.substring(msg.indexOf("QPD")+1);
		String[] split = msgQpd.split("\\|");
		String[] split2 = split[3].split("\\^");
		String idCard = split2[0];
		String identifyNO = split2[4];
		PiMaster pimaster = new PiMaster();
		pimaster.setIdNo(idCard);
        if("SocialSecurityCardEncryption".equals(identifyNO)){//社保卡加密串
			
		}else if("SocialSecurityCardPassword".equals(identifyNO)){//社保卡密码
			
		}else if("SocialSecurityCardNumber".equals(identifyNO)){//社保卡号
			pimaster.setDtIdtype("21");
		}
		else if("SocialSecurtiyComputerNumber".equals(identifyNO)){//社保电脑号
			
		}else if("HealthCardNO".equals(identifyNO)){//个人健康卡
			pimaster.setDtIdtype("25");
		}else if("BankCardNO".equals(identifyNO)){//银行卡（对应HCS中的CardType = 1）
			
		}else if("MultiHealthCardNO".equals(identifyNO)){//多功能健康卡（对应HCS中的CardType = 3）
			
		}else if("IdentifyNO".equals(identifyNO)){//身份证号
			pimaster.setDtIdtype("01");
		}else if("TreatmentCardNO".equals(identifyNO)){//就诊卡
			pimaster.setDtIdtype("");
		}
		return sendPatientInfo(pimaster);
	}
	
	/**
	 * 自主机首次建档反馈
	 * @param piMaster 患者信息
	 * @param type 门诊（I）/住院(O)/体检（P）
	 */
	private String sendCreatePati(PiMasterParam piMast,String type){
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		PiMaster newPi=null;
		boolean success=true;
		try {
			newPi = piPubService.savePiMaster(piMast.getMaster());
			piPubService.savePiInsuranceList(piMast.getInsuranceList(), newPi.getPkPi());
		} catch (Exception e1) {
			success=false;
		}
		Map<String,Object> paramMap=(Map<String,Object>)ApplicationUtils.beanToMap(newPi);
		String message="";
		if(sendMsgSwitch !=null){
			try {
				message=msgSendZzj.sendPati("S01", paramMap, type,success);
			} catch (HL7Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
		return message;
	}
	
	/**
	 * 患者信息查询反馈
	 * @param pimaster
	 * @return
	 */
	private String sendPatientInfo(PiMaster pimaster){
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		String message="";
		if(sendMsgSwitch !=null){
			List<Map<String, Object>> piMasterList = selfMsgMapper.getPiMasterListNoPhoto(pimaster);
			try {
				message=msgSendZzj.sendPatientInfo("K21", piMasterList);
			} catch (HL7Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}

		return message;
	}

	/**
	 * 排班资源信息查询
	 * @param hashMap
	 * @return
	 */
	private String sendSchResource(Map<String, Object> hashMap) {
		
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		String message =null ;
		if(sendMsgSwitch !=null){
			try {
				message=msgSendSch.sendSchResource("Z11",hashMap);
			} catch (HL7Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
       return message;
	}	
	
	/**
	 * 挂号结算反馈消息
	 * @param pimast
	 * @param pkSch 
	 * @return
	 * @throws HL7Exception 
	 */
	@SuppressWarnings("unchecked")
	private String sendPreRegister(PiMasterParam pimast, String pkSch) throws HL7Exception {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		ResponseJson  rs = null;
		ResponseJson result = null;
		//根据pkSch得到pkSchsrv
		Map<String, Object> param = DataBaseHelper.queryForMap("select pk_schsrv from sch_sch where pk_sch=?", new Object[]{pkSch});
		//组装预结算参数
		
		List<Map<String, Object>> reglist;
		try {
			reglist = creatPreRegister(pimast,param.get("pkSchsrv").toString());
		} catch (Exception e2) {
			e2.printStackTrace();
			return sendErrorPid("挂号失败！");
		}
		
		boolean success=true;
		if(sendMsgSwitch !=null){
			try {
				//自定义一个User用户,只需要机构就可以
				User user = new User();
				user.setPkOrg(pimast.getMaster().getPkOrg());
				UserContext userContext = new UserContext();
				userContext.setUser(user);
				//调用挂号预结算
				ApplicationUtils apputil = new ApplicationUtils();
				rs =  apputil.execService("PV", "OpCgSettlementService", "countRegisteredAccountingSettlement",reglist,user);
				if(rs.getStatus()<0){//挂号预结算失败
					return sendErrorPid(rs.getDesc());
				}
				//组装结算成功数据
				List<Map<String, Object>> settle = createSettleInfo((List<Map<String,Object>>)rs.getData(),pimast,pkSch);

				result = apputil.execService("PV", "RegService", "savePvEncounterAndOp",settle,user);
				if(result.getStatus()<0){//挂号结算失败
					return sendErrorPid(result.getDesc());
				}
				
			} catch (Exception e1) {
				success=false;
			}
		}
		String message="";
		if(rs != null){
			try {
				message=msgSendZzj.sendRegister("S01", (List<Map<String,Object>>)rs.getData(),(List<PvOpAndSettleVo>)result.getData(),pimast,pkSch,success);
			} catch (HL7Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
		else{
			return "";
		}
		return message;
	}	
	
	/**
	 * 支付(暂时还不考虑和医保相关的)
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception 
	 */
	@SuppressWarnings("unused")
	private String registeredPayment(DFT_P03 dft,String pkOrg) throws HL7Exception {
		String msg = MsgUtils.getParser().encode(dft);
		//EVN
		EVN evn = dft.getEVN();
		//PID
		PID pid = dft.getPID();
		//PV1
		PV1 pv1 = dft.getPV1();
		//FT1
		FT1 ft1 = dft.getFINANCIAL(0).getFT1();
		//得到ZPR(通过解析字符串的方式)
		String msgZpr = msg.substring(msg.indexOf("ZPR"));
		String[] split = msgZpr.trim().split("\\|");
		String yljgbm = split[1];//医疗机构编码
		String mzlsh = split[3];//门诊流水号
		String je = split[8];//金额
		//是挂号收费还是其他费用收费
		String settleType = ft1.getTransactionCode().getIdentifier().getValue();
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setCodePi(pid.getPatientID().getID().getValue());//患者编码
		pvEncounter.setIdNo(pid.getPatientIdentifierList(0).getID().getValue());
		pvEncounter.setDateEnd(new Date());
		pvEncounter.setCodePv(mzlsh);
		//得到患者的就诊类型（门诊收费/住院收费）
		if(settleType.equals("1")){//挂号收费
			//挂号收费反馈：
			pvEncounter.setEuPvtype("1");//就诊类型
			return sendRegisterSettle(je,pvEncounter);
		}else{//其他费用--门诊和住院支付反馈消息是分开组建的，
			String type = pv1.getPatientClass().getValue();
			if(type.equalsIgnoreCase("O")){
				//组装门诊收费数据
				pvEncounter.setEuPvtype("1");//就诊类型
				return sendOpSettle(pvEncounter,pkOrg);
			}else if(type.equalsIgnoreCase("I")){
				//组装住院收费数据
				//根据业务实际进展，住院暂时放下
				System.out.println("住院");
			}
		}
		return null;
	}	

	/**
	 * 自助机退号反馈
	 * @return
	 */
	private String sendCancleRegister() {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		String message="";
		if(sendMsgSwitch !=null){
			try {
				message=msgSendZzj.sendCancleRegister("S04");
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
		return message;
	}
	
	/**
	 * 自助机退号
	 * @param hapiMsg
	 * @param pkOrg
	 * @return
	 * @throws HL7Exception 
	 * @throws JSONException 
	 */
	private String cancleRegister(Map<String,String> map) throws Exception {
		//根据患者证件号等信息得到患者主键pk_pi 
		String codePi = map.get("codePi");//患者编号
		String idType = map.get("idType");
		if(idType.equals("IdentifyNO")){
			idType="01";
		}
		if(idType.equals("SocialSecurityCardNumber")){
			idType="21";
		}
		if(idType.equals("HealthCardNO")){
			idType="25";
		}
		
		String idNo = map.get("idCard");//证件号
		PiMaster pimaster = DataBaseHelper.queryForBean("select * from pi_master where code_pi= ? and dt_idtype=? and id_no=?", PiMaster.class, new Object[]{codePi,idType,idNo});
		if(pimaster == null){
			return sendErrorPid("暂无患者记录！");
		}
		ResponseJson rsPv = null;
		ResponseJson rsPreSettle = null;
		ResponseJson rsSettle = null;
		ApplicationUtils apputil = new ApplicationUtils();
		User user = new User();
		user.setPkOrg(map.get("pkOrg"));
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		//获取患者就诊记录
	    List<PvOpVo> pvOpVo = selfMsgMapper.getPvOpVoTodayListOracle(pimaster.getPkPi(),map.get("pkOrg"));
		//退号预结算
	    Map<String,Object> param = new HashMap<String, Object>();
	    String pkPv = null;
	    for (int i = 0; i < pvOpVo.size(); i++) {
			if(pvOpVo.get(i).getCodePv().equals(map.get("codePv"))){
				pkPv = pvOpVo.get(i).getPkPv();
			}
		}
	    if(pkPv == null){
	    	return sendErrorPid("该患者无就诊记录");
	    }
	    param.put("pkPv", pkPv);
		rsPreSettle = apputil.execService("PV", "opCgSettlementService", "countRefoundRegisteredSettlement", param, user);
		if(rsPreSettle.getStatus() < 0){
			return sendErrorPid(rsPreSettle.getDesc());
		}
		//退号结算
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		rsSettle = apputil.execService("PV", "regService", "cancelPv", pvEncounter, user);
		if(rsSettle.getStatus() < 0){
			return sendErrorPid(rsSettle.getDesc());
		}
		return sendCancleRegister();
	}

	/**
	 * 门诊支付消息反馈
	 * @param pimast
	 * @return
	 * @throws HL7Exception 
	 */
	private String sendOpSettle(PvEncounter pvEncounter,String pkOrg) throws HL7Exception {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		//根据患者证件号查询患者就诊记录详细信息
		List<PibaseVo> opPiMaster = selfMsgMapper.getOpPiMaster(pvEncounter);
		//得到最新一次的就诊记录
	    //List<PvOpVo> pvOpVo = selfMsgMapper.getPvOpVoTodayListOracle(opPiMaster.get(0).getPkPi(),pkOrg);
		ResponseJson  opNoSettleRs = null;
		ResponseJson opPreSettleRs = null;
		ResponseJson opSettleRs = null;

		//门诊查询患者未收费项目
		User user = new User();
		user.setPkOrg(pkOrg);
		UserContext userContext = new UserContext();
		userContext.setUser(user);
		OpCgTransforVo opNoSettleReglist = null;
		try {
			//调用查询患者未收费项目接口：组建入参
			opNoSettleReglist = creatPiNotSettle(opPiMaster);
		} catch (Exception e2) {
			e2.printStackTrace();
			return sendErrorPid("患者查询未收费项目失败！");
		}
		ApplicationUtils apputil = new ApplicationUtils();
		opNoSettleRs =  apputil.execService("PV", "OpCgSettlementService", "queryPatiCgInfoNotSettle",opNoSettleReglist,user);
		if(opNoSettleRs.getStatus()<0){
			return sendErrorPid(opNoSettleRs.getDesc());
		}
		//组装门诊预结算信息
		OpCgTransforVo preSettleReglist = null;
		preSettleReglist = createOpPreSettle((List<BlPatiCgInfoNotSettleVO>)opNoSettleRs.getData(),opPiMaster);
		
		//主要包含了费用金额信息
		opPreSettleRs =  apputil.execService("PV", "OpCgSettlementService", "countOpcgAccountingSettlement",preSettleReglist,user);
		if(opPreSettleRs.getStatus()<0){
			return sendErrorPid(opPreSettleRs.getDesc());
		}
		//组装门诊结算信息
		//获得到的参数类型转换
		String objectToJson = ApplicationUtils.objectToJson(opPreSettleRs.getData());
		OpCgTransforVo opCgVo = JsonUtil.readValue(objectToJson, OpCgTransforVo.class);
		preSettleReglist.setAggregateAmount(opCgVo.getAggregateAmount());
		preSettleReglist.setPatientsPay(opCgVo.getPatientsPay());
		preSettleReglist.setAccountReceivable(opCgVo.getAccountReceivable());
		OpCgTransforVo opSettle = createOpSettle(preSettleReglist,opPiMaster);
		opSettleRs =  apputil.execService("PV", "OpCgSettlementService", "mergeOpcgAccountedSettlement",opSettle,user);
		if(opSettleRs.getStatus()<0){
			return sendErrorPid(opSettleRs.getDesc());
		}
		return sendOpSettleInfo(opPiMaster);
	}

	/**
	 * 门诊其他费用反馈
	 * @param opSettleRs
	 * @param opPiMaster
	 * @param pvOpVo 
	 * @return
	 */
	private String sendOpSettleInfo(List<PibaseVo> opPiMaster) {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		Map<String, Object> map = DataBaseHelper.queryForMap("select bod.amount_pi,bi.code,bi.name from bl_op_dt bod left join bd_itemcate bi on bod.pk_itemcate = bi.pk_itemcate "
				+ "where bod.pk_pv=? and bod.flag_settle='1' ", new Object[]{opPiMaster.get(0).getPkPv()});
		String message="";
		if(sendMsgSwitch !=null){
			try {
				message=msgSendZzj.sendOpSettleInfo("P03",map,opPiMaster);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
		return message;
	}

	/**
	 * 组建门诊收费结算参数
	 * @param preSettleReglist --患者收费项目信息
	 * @param opCgVo --患者待缴费用信息
	 * @param opPiMaster--患者信息
	 */
	private OpCgTransforVo createOpSettle(OpCgTransforVo preSettleReglist, List<PibaseVo> opPiMaster) {
        OpCgTransforVo opCgTransforVo = new OpCgTransforVo();
        for (int i = 0; i < preSettleReglist.getBlOpDts().size(); i++) {
			if(preSettleReglist.getBlOpDts().get(i).getPresNo() == null || preSettleReglist.getBlOpDts().get(i).getPresNo().equals("")){
				preSettleReglist.getBlOpDts().get(i).setPresNo(preSettleReglist.getBlOpDts().get(i).getPkCnord());
			}
		}
		//支付方式--blDeposits
		List<BlDeposit> blDepositList = new ArrayList<>();
		//本次患者账户支付
		if (preSettleReglist.getAccountPay().compareTo(BigDecimal.ZERO)==1 || preSettleReglist.getAccountPay().compareTo(BigDecimal.ZERO)==0)//=0是等于,=1是大于
        {
            BlDeposit blDeposit = new BlDeposit();
            blDeposit.setDtPaymode("4");
            blDeposit.setAmount(preSettleReglist.getAccountPay());
            blDeposit.setFlagAcc("0");
            blDeposit.setDelFlag("0");
            blDepositList.add(blDeposit);
        }
		if(preSettleReglist.getAccountReceivable().compareTo(BigDecimal.ZERO)==1){
			BlDeposit blDeposit = new BlDeposit();
            blDeposit.setDtPaymode("3");
            blDeposit.setAmount(preSettleReglist.getAccountReceivable());
            blDeposit.setFlagAcc("0");
            blDeposit.setDelFlag("0");
            blDepositList.add(blDeposit);
		}
		opCgTransforVo.setBlDeposits(blDepositList);
		
		//发票相关的信息--invoiceInfo--自助机不打发票，不组建该信息
		List<InvoiceInfo> invoiceInfoList = new ArrayList<>();
		opCgTransforVo.setInvoiceInfo(invoiceInfoList);
		
		//票据领用消息--billInfo
		List<BlEmpInvoice> billInfoList = new ArrayList<>();
		opCgTransforVo.setBillInfo(billInfoList);
		
		//费用信息--blOpDts
		BlOpDt blOpDt = new BlOpDt();
		List<BlOpDt> blOpDtList = new ArrayList<>();
		for (BlOpDt vo : preSettleReglist.getBlOpDts()) {
			blOpDt.setItemCode(vo.getItemCode());
			blOpDt.setPkInsu(vo.getPkInsu());
			blOpDt.setPkPres(vo.getPkPres());
			//dtPrestye
			blOpDt.setPresNo(vo.getPresNo());
			//dateEnter
			//nameDept
			blOpDt.setNameEmpApp(vo.getNameEmpApp());
			blOpDt.setNameEmpCg(vo.getNameEmpCg());
			blOpDt.setPkCnord(vo.getPkCnord());
			blOpDt.setPkCgop(vo.getPkCgop());
			blOpDt.setNameCg(vo.getNameCg());
			blOpDt.setSpec(vo.getSpec());
			blOpDt.setQuan(vo.getQuan());
			blOpDt.setQuanCg(vo.getQuanCg());
			blOpDt.setPriceOrg(vo.getPriceOrg());
			blOpDt.setAmount(vo.getAmount());
			blOpDt.setCodeOrdtype(vo.getCodeOrdtype());
			blOpDt.setCodeOrdType(vo.getCodeOrdtype());
			blOpDt.setPkPv(vo.getPkPv());
			blOpDt.setCodeBill(vo.getCodeBill());
			blOpDt.setPkDeptApp(vo.getPkDeptApp());
			blOpDt.setPkEmpApp(vo.getPkEmpApp());
			blOpDt.setPkDeptEx(vo.getPkDeptEx());
			blOpDt.setPkItem(vo.getPkItem());
			blOpDt.setPkPd(vo.getPkPd());
			blOpDt.setFlagPd(vo.getFlagPd());			
			blOpDtList.add(blOpDt);
		}
		opCgTransforVo.setBlOpDts(blOpDtList);
		
		opCgTransforVo.setCardNo(null);
		opCgTransforVo.setPkPi(opPiMaster.get(0).getPkPi());
		opCgTransforVo.setPkInsurance(opPiMaster.get(0).getPkInsu());
		opCgTransforVo.setAggregateAmount(preSettleReglist.getAggregateAmount());
		opCgTransforVo.setPatientsPay(preSettleReglist.getPatientsPay());
		opCgTransforVo.setAccountReceivable(preSettleReglist.getAccountReceivable());
		opCgTransforVo.setMedicarePayments(preSettleReglist.getMedicarePayments());
		opCgTransforVo.setAccountBalance(preSettleReglist.getAccountBalance());
		opCgTransforVo.setAccountPay(preSettleReglist.getAccountPay());
		opCgTransforVo.setInVoiceNo(null);//发票号
		opCgTransforVo.setPkEmpinvoice(null);//票据领用主键
		opCgTransforVo.setBlInvoiceDts(null);//发票明细
		opCgTransforVo.setBlInVoiceOtherInfo(null);//
		opCgTransforVo.setYbzf(new BigDecimal("0"));
		opCgTransforVo.setXjzf(new BigDecimal("0"));
		opCgTransforVo.setPkPv(opPiMaster.get(0).getPkPv());
		opCgTransforVo.setPkSettle(null);
		return opCgTransforVo;
		
	}

	/**
	 * 挂号支付消息反馈
	 * @param mglsh
	 * @param pid
	 * @param pvEncounter 
	 * @return
	 */
	private String sendRegisterSettle (String je, PvEncounter pvEncounter) {
		String sendMsgSwitch = ApplicationUtils.getPropertyValue("msg.send.switch","0");
		if(sendMsgSwitch ==null || sendMsgSwitch.equals("0")){
			return "";
		}
		String message =null ;
		List<PibaseVo> opPiMaster = selfMsgMapper.getOpPiMaster(pvEncounter);
		Map<String, Object> hashMap = new HashMap<String,Object>();
		if(sendMsgSwitch !=null && "1".equals(sendMsgSwitch)){
			try {
				message=msgSendZzj.sendRegisterSettle("P03",je,opPiMaster);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new BusException("发送消息失败！"); 
			}
		}
       return message;
	}
	
	/**
	 * 根据pid消息创建患者信息
	 * @param pid
	 * @param pkOrg 
	 * @return
	 */
	private PiMaster createPiByPID(PID pid, String pkOrg){
		String str=pid.toString();
		PiMaster pimaster=new PiMaster();
		pimaster.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));//住院号
		pimaster.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));//门诊号
		pimaster.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));//患者编码
		pimaster.setBarcode("");//条形码
		//患者挂号的时候会传入
		pimaster.setPkPicate("58f82f7162c24140a00d5c2e3cb0d7d9");//患者分类
		pimaster.setNamePi(pid.getPatientName(0).getFamilyName().getFn1_Surname().getValue());//患者姓名
		pimaster.setPhotoPi(null);//患者照片
		String idType;
		//String ny = null;//定义出生日期变量
		log.info("==============【"+pimaster.getNamePi()+"】SelfMsgService.createPiByPID获取住院号:"+pimaster.getCodeIp()+"============");
		
	    idType=pid.getPatientIdentifierList(0).getIdentifierTypeCode().getValue();//证件类型
		pimaster.setIdNo(pid.getPatientIdentifierList(0).getID().getValue());//证件号
		
		if("SocialSecurityCardEncryption".equals(idType)){//社保卡加密串
			
		}else if("SocialSecurityCardPassword".equals(idType)){//社保卡密码
			
		}else if("SocialSecurityCardNumber".equals(idType)){//社保卡号
			idType="21";		
		}
		else if("SocialSecurtiyComputerNumber".equals(idType)){//社保电脑号
			
		}else if("HealthCardNO".equals(idType)){//个人健康卡
			idType="25";			
		}else if("BankCardNO".equals(idType)){//银行卡（对应HCS中的CardType = 1）
			
		}else if("MultiHealthCardNO".equals(idType)){//多功能健康卡（对应HCS中的CardType = 3）
			
		}else if("IdentifyNO".equals(idType)){//身份证号
			//如果有身份证号，就设置出生日期
			//ny = pid.getPatientIdentifierList(0).getID().getValue().substring(6, 14);
			//ny = ny.substring(0,4) + "/" + ny.substring(4, 6) + "/" + ny.substring(6, 8);
			//pimaster.setBirthDate(DateUtils.strToDate(ny, "yyyy/MM/dd"));//出生日期
			idType="01";
		}else if("TreatmentCardNO".equals(idType)){//就诊卡
			idType="";
		}	
		pimaster.setDtIdtype(idType);//证件类型
		pimaster.setHicNo("");//健康卡号
		pimaster.setInsurNo("");//医保卡号
		pimaster.setMpi("");//区域主索引
		pimaster.setFlagEhr("");//健康档案建立标志
		String sex=pid.getAdministrativeSex().getValue();
		if("F".equals(sex)){//女
			sex="03";
		}else if("M".equals(sex)){//男
			sex="02";
		}else if("O".equals(sex)){//其他
			sex="01";
		}else if("U".equals(sex)){//未说明
			sex="04";
		}
		pimaster.setDtSex(sex);//性别
		
		
		pimaster.setDtMarry("");//婚姻编码
		pimaster.setDtOccu("");//职业编码
		pimaster.setDtEdu("");//学历编码
		pimaster.setDtCountry("");//国籍编码
		pimaster.setDtNation("");//民族编码
		pimaster.setTelNo("");//电话号码
		pimaster.setMobile(pid.getPhoneNumberHome(0).getPhoneNumber().getValue());//手机号码
		pimaster.setWechatNo("");//微信号码
		pimaster.setEmail("");//邮箱
		pimaster.setAddrcodeBirth("");//出生地区域编码
		pimaster.setAddrBirth("");//出生地描述
		pimaster.setAddrcodeOrigin("");//籍贯地区编码
		pimaster.setAddrOrigin("");//籍贯描述
		pimaster.setAddrcodeRegi("");//户口地址区域编码
		pimaster.setAddrRegi("");//户口地址描述
		pimaster.setAddrRegiDt("");//户口详细地址
		pimaster.setPostcodeRegi("");//户口编码
		pimaster.setAddrcodeCur("");//现住址区域编码
		pimaster.setAddrCur("");//现住址描述
		pimaster.setAddrCurDt("");//现住址详细地址
		pimaster.setPostcodeCur("");//现住址编码
		pimaster.setUnitWork("");//工作单位
		pimaster.setTelWork("");//工作单位电话
		pimaster.setPostcodeWork("");//工作单位地址邮箱
		pimaster.setNameRel(pimaster.getNamePi());//联系人
		//pimaster.setDtIdtypeRel(pimaster.getDtIdtype());//联系人证件类型
		//pimaster.setIdnoRel(pimaster.getIdNo());//联系人证件号
		pimaster.setDtRalation("01");//联系人关系:本人
		pimaster.setTelRel(pimaster.getMobile());//联系人电话
		pimaster.setAddrRel("");//联系人地址
		pimaster.setDtBloodAbo("");//ABO型血
		pimaster.setDtBloodRh("");//RH型血
		pimaster.setPkOrg(pkOrg);//机构
		return pimaster;
	}
	
	/**
	 * 组装门诊预结算所需参数
	 * @param data
	 * @param opPiMaster
	 * @return
	 */
	private OpCgTransforVo createOpPreSettle(List<BlPatiCgInfoNotSettleVO> data, List<PibaseVo> opPiMaster) {
		OpCgTransforVo opCgTransforVo = new OpCgTransforVo();
		
		List<BlDeposit> blDepositList = new ArrayList<>();
		opCgTransforVo.setBlDeposits(blDepositList);
		
		List<InvoiceInfo> invoiceInfoList = new ArrayList<>();
		opCgTransforVo.setInvoiceInfo(invoiceInfoList);
		
		List<BlOpDt> blOpDtList = new ArrayList<>();
		for (BlPatiCgInfoNotSettleVO vo : data) {
			BlOpDt blOpDt = new BlOpDt();
			blOpDt.setItemCode(vo.getItemCode());
			blOpDt.setPkInsu(vo.getPkInsu());
			blOpDt.setPkPres(vo.getPkPres());
			//dtPrestye
			blOpDt.setPresNo(vo.getPresNo());
			//dateEnter
			//nameDept
			blOpDt.setNameEmpApp(vo.getNameEmpOrd());
			blOpDt.setNameEmpCg(vo.getNameEmpInput());
			blOpDt.setPkCnord(vo.getPkCnord());
			blOpDt.setPkCgop(vo.getPkCgop());
			blOpDt.setNameCg(vo.getNameCg());
			blOpDt.setSpec(vo.getSpec());
			blOpDt.setQuan(vo.getQuan());
			blOpDt.setQuanCg(vo.getQuanCg());
			blOpDt.setPriceOrg(vo.getPriceOrg().doubleValue());
			blOpDt.setAmount(vo.getAmount().doubleValue());
			blOpDt.setCodeOrdtype(vo.getCodeOrdtype());
			blOpDt.setPkPv(vo.getPkPv());
			blOpDt.setCodeBill(vo.getCodeBill());
			blOpDt.setPkDeptApp(vo.getPkDeptApp());
			blOpDt.setPkEmpApp(vo.getPkEmpApp());
			blOpDt.setPkDeptEx(vo.getPkDeptEx());
			blOpDt.setPkItem(vo.getPkItem());
			blOpDt.setPkPd(vo.getPkPd());
			blOpDt.setFlagPd(vo.getFlagPd());			
			blOpDtList.add(blOpDt);
		}
		opCgTransforVo.setBlOpDts(blOpDtList);
		opCgTransforVo.setPkInsurance(opPiMaster.get(0).getPkInsu());
		opCgTransforVo.setAggregateAmount(new BigDecimal("0"));
		opCgTransforVo.setPatientsPay(new BigDecimal("0"));
		opCgTransforVo.setAccountReceivable(new BigDecimal("0"));
		opCgTransforVo.setMedicarePayments(new BigDecimal("0"));
		opCgTransforVo.setAccountBalance(new BigDecimal("0"));
		opCgTransforVo.setAccountPay(new BigDecimal("0"));
		opCgTransforVo.setInVoiceNo(null);
		opCgTransforVo.setPkEmpinvoice(null);
		opCgTransforVo.setBlInvoiceDts(null);
		opCgTransforVo.setBlInVoiceOtherInfo(null);
		opCgTransforVo.setYbzf(new BigDecimal("0"));
		opCgTransforVo.setXjzf(new BigDecimal("0"));
		opCgTransforVo.setPkPv(opPiMaster.get(0).getPkPv());
		opCgTransforVo.setPkSettle(null);
		return opCgTransforVo;
	}

	/**
	 * 组装门诊患者未收费项目参数
	 * @param opPiMaster
	 * @return
	 */
	private OpCgTransforVo creatPiNotSettle(List<PibaseVo> opPiMaster) {
		OpCgTransforVo opCgTransforVo = new OpCgTransforVo();
		
		//BlDeposit blDeposits = new BlDeposit();
		List<BlDeposit> blDepositList = new ArrayList<>();
		opCgTransforVo.setBlDeposits(blDepositList);
		
		//InvoiceInfo invoiceInfo = new InvoiceInfo();
		List<InvoiceInfo> invoiceInfoList = new ArrayList<>();
		opCgTransforVo.setInvoiceInfo(invoiceInfoList);

		//BlEmpInvoice blEmpInvoice = new BlEmpInvoice();
		List<BlEmpInvoice> blEmpInvoiceList = new ArrayList<>();
		opCgTransforVo.setBillInfo(blEmpInvoiceList);
		
		opCgTransforVo.setCardNo(null);
		opCgTransforVo.setPkPi(opPiMaster.get(0).getPkPi());
		opCgTransforVo.setBlOpDts(null);
		opCgTransforVo.setPkInsurance(null);
		opCgTransforVo.setAggregateAmount(new BigDecimal("0"));
		opCgTransforVo.setPatientsPay(new BigDecimal("0"));
		opCgTransforVo.setAccountReceivable(new BigDecimal("0"));
		opCgTransforVo.setMedicarePayments(new BigDecimal("0"));
		opCgTransforVo.setAccountBalance(new BigDecimal("0"));
		opCgTransforVo.setAccountPrepaid(new BigDecimal("0"));
		opCgTransforVo.setAccountPay(new BigDecimal("0"));
		opCgTransforVo.setInVoiceNo(null);
		opCgTransforVo.setPkEmpinvoice(null);
		opCgTransforVo.setPkInvcate(null);
		opCgTransforVo.setBlInvoiceDts(null);
		opCgTransforVo.setBlInVoiceOtherInfo(null);
		opCgTransforVo.setYbzf(new BigDecimal("0"));
		opCgTransforVo.setXjzf(new BigDecimal("0"));
		opCgTransforVo.setPkPv(opPiMaster.get(0).getPkPv());
		opCgTransforVo.setPkSettle(null);
		
		return opCgTransforVo;
	}

	/**
	 * 组建挂号预结算信息
	 * @param pimast
	 * @param pkSch
	 * @return
	 */
	private List<Map<String, Object>> creatPreRegister(PiMasterParam pimast,String pkSchSrv) {
		List<Map<String, Object>> piMasterList = selfMsgMapper.getPiMasterListNoPhoto(pimast.getMaster());
		List<Map<String,Object>> reglist = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> insuList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> opDtList = new ArrayList<Map<String,Object>>();
		//医保信息
		Map<String,Object> insu = new HashMap<String,Object>();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("cardNo", "");
		paramMap.put("orderNo", 0);
		paramMap.put("pkPi", piMasterList.get(0).get("pkPi"));
		paramMap.put("pkPicate", piMasterList.get(0).get("pkPicate"));
		paramMap.put("pkSchsrv", pkSchSrv);
		paramMap.put("pkAppo", null);
		paramMap.put("pkInsu", pimast.getInsuranceList().get(0).getPkHp());
		insu.put("sortNo", "0");
		insu.put("pkHp", pimast.getInsuranceList().get(0).getPkHp());
		insu.put("flagMaj", "1");
		insuList.add(insu);
		paramMap.put("ybzje", 0);
		paramMap.put("ybzf", 0);
		paramMap.put("xjzf", 0);
		paramMap.put("insuList", insuList);
		paramMap.put("opDtList", opDtList);
		
		reglist.add(paramMap);
		return reglist;
	}

	/**
	 * 组建挂号结算信息
	 * @param piMaster
	 * @param string
	 * @return
	 */
	private List<Map<String, Object>> createSettleInfo(List<Map<String,Object>> registParam,PiMasterParam pimast,String pkSch) {
		//根据排班主键得到排班的详细信息
		Map<String,Object> planInfo = selfMsgMapper.getPlanInfo(pkSch);
	    if(planInfo==null){
	    	return null;
	    }
	    //查询患者pkpi
	    PiMaster pi = DataBaseHelper.queryForBean("select * from pi_master where dt_idtype=? and id_no=?", PiMaster.class, new Object[]{pimast.getMaster().getDtIdtype(),pimast.getMaster().getIdNo()});
		if(pi==null){
			return null;
		}
	    List<Map<String,Object>> reglist = new ArrayList<Map<String,Object>>();
		Map<String, Object> paramMap = new HashMap<>();
		PiMaster piMaster = pimast.getMaster();
		paramMap.put("cardNo",null);
		paramMap.put("orderNo",new BigDecimal("0"));
		paramMap.put("pkPi",pi.getPkPi());
		paramMap.put("pkPicate",piMaster.getPkPicate());
		paramMap.put("pkSchsrv",planInfo.get("pkSchsrv"));//排班服务主键
		paramMap.put("pkRes",planInfo.get("pkSchres"));//排班资源主键 
		paramMap.put("pkDateslot",planInfo.get("pkDateslot"));//日期分组主键
		paramMap.put("pkSch",pkSch);
		paramMap.put("pkInsu",pimast.getInsuranceList().get(0).getPkHp());
		paramMap.put("euPvtype", "1");
		if(planInfo.get("pkDept") !=null || planInfo.get("pkDept").equals("")){
			paramMap.put("pkDeptPv",planInfo.get("pkDept"));//挂号科室主键
		}
		if(planInfo.get("pkEmp") !=null){
			paramMap.put("pkEmpPv", planInfo.get("pkEmp"));//挂号医生主键
			paramMap.put("nameEmpPv" ,planInfo.get("name"));//挂号医生姓名
		}
		//自助机挂号：不传发票信息
		//paramMap.put("codeInv" ,"");//发票号
		//paramMap.put("pkEmpinv" ,"");//发票领用主键
		//paramMap.put("pkInvcate" ,"");//票据分类主键
		
		//医保计划列表
		PvInsurance pvInsurance = new PvInsurance();
		pvInsurance.setSortNo(0);
		pvInsurance.setPkHp(pimast.getInsuranceList().get(0).getPkHp());
		pvInsurance.setFlagMaj("0");
		List<PvInsurance> pvInsuranceList = new ArrayList<>();
		pvInsuranceList.add(pvInsurance);
		paramMap.put("insuList",pvInsuranceList);
		
		//收费明细列表
		BlOpDt blOpDt = new BlOpDt();
		List<BlOpDt> blOpDtList = new ArrayList<>();
		paramMap.put("opDtList",blOpDtList);
		
		//交款记录列表
		BlDeposit blDeposit = new BlDeposit();
		List<BlDeposit> blDepositList = new ArrayList<>();
		blDeposit.setPkDepo(null);
		blDeposit.setAmount(new BigDecimal(registParam.get(0).get("patientsPay").toString()));
		blDeposit.setDtPaymode("0");//支付方式
		blDeposit.setExtAmount(new BigDecimal("0"));
		blDeposit.setFlagAcc("0");
		blDeposit.setDelFlag("0");		
		blDepositList.add(blDeposit);
		paramMap.put("depositList",blDepositList);
		
		paramMap.put("registerResult" ,null);
		paramMap.put("ybChargeResult" ,null);
		paramMap.put("ybzje" ,new BigDecimal("0"));
		paramMap.put("ybzf" ,new BigDecimal("0"));
		paramMap.put("xjzf" ,new BigDecimal("0"));
		paramMap.put("amtInsuThird" ,new BigDecimal("0"));
		paramMap.put("dtExthp" ,null);
		paramMap.put("isRegSuccess" ,"0");
		reglist.add(paramMap);
		
		return reglist;
	}

	/**
	 * 解析pid:创建医保计划信息
	 * @param pid
	 * @return
	 */
	private PiInsurance createInsur(PID pid){
		PiInsurance insur=new PiInsurance();
		insur.setSortNo(1l);//序号
		insur.setDateBegin(new Date());//开始时间
		insur.setDateEnd(new Date());//结束时间
		
		//自助机目前还没涉及医保相关的，暂时数据写死
		//String code=pid.getProductionClassCode().getIdentifier().getValue();
		String code = "0001";
		String sql="select pk_hp from bd_hp where code=?";
		String pkHp=DataBaseHelper.queryForScalar(sql, String.class, code);//查询医保主键
		insur.setPkHp(pkHp);
		return insur;
	}
	
	/**
     * 如果没有传入机构，进行处理
     * @return
     * @throws HL7Exception 
     */
	private String sendError() throws HL7Exception {
		SRR_S01 srr=new SRR_S01();
		String msgId=MsgUtils.getMsgId();
		MSH msh=srr.getMSH();		
		MsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
		msh.getReceivingApplication().getNamespaceID().setValue("YTZZJ");
		MSA msa=srr.getMSA();
		MsgUtils.createMSAMsg(msa, msgId);
		msa.getAcknowledgementCode().setValue("AR");
	    msa.getTextMessage().setValue("没有传入机构主键");
	    String msg = MsgUtils.getParser().encode(srr);
        return msg;
	}
}
