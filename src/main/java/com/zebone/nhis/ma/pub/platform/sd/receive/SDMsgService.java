package com.zebone.nhis.ma.pub.platform.sd.receive;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v24.datatype.TX;
import ca.uhn.hl7v2.model.v24.message.*;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.bl.pub.vo.BlIpCgVo;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.cn.ipdw.ExOpSch;
import com.zebone.nhis.common.module.ex.nis.ns.*;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgParseUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDReceiveMapper;
import com.zebone.nhis.ma.pub.platform.sd.entity.MidBlOp;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendIp;
import com.zebone.nhis.ma.pub.platform.sd.send.SDMsgSendOp;
import com.zebone.nhis.ma.pub.platform.sd.service.*;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.IDCardUtil;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.ACK_ZRN;
import com.zebone.nhis.ma.pub.platform.sd.vo.Z11;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.IdCardUtils;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息数据解析和业务逻辑处理
 * @author maijiaxing
 *
 */
@Service
public class SDMsgService {
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
	
	@Resource
	private SDReceiveMapper sDReceiveMapper;
	@Resource
	private SDMsgDataUpdateService sDMsgDataUpdate;//公用
	@Resource 
	private SDSysMsgSelfAppService sDSysMsgSelfAppService;//自助机
	@Resource
	private SDSysMsgPubService sDSysMsgPubService;//公用模块
	@Resource
	private SDSysMsgBodyCheckService sDSysMsgBodyCheckService;//体检
	@Resource
	private SDBaseDataService sDBaseDataService;//基础数据
	@Resource
	private SDQueryUtils sDQueryUtils;
	@Resource
	private SDMsgSendIp sDMsgSendIp;
	@Resource
	private SDMsgSendOp sDMsgSendOp;
	@Resource
	private SDOpMsgMapper sDOpMsgMapper;
	@Resource
	private SDSysMsgWeChatService msgWeChatService;




	/**
	 * 待支付列表查询
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 */
	public String receiveQRYQ27(Message hapiMsg) throws HL7Exception {
		QRY_Q01 q27= (QRY_Q01)hapiMsg;
		Map<String, Object> map = new HashMap<>();
		String msg = "";
		String oldmsgid = q27.getMSH().getMessageControlID().getValue();
		String sendApp = q27.getMSH().getSendingApplication().getNamespaceID().getValue();
		try{
			map.put("oldmsgid",oldmsgid);
			map.put("time",q27.getMSH().getDateTimeOfMessage().getTimeOfAnEvent().getValue());
			map.put("sendApp",sendApp);
			map.put("paitId", q27.getQRD().getWhoSubjectFilter(0).getSecondAndFurtherGivenNamesOrInitialsThereof().getValue());
			msg = sDMsgDataUpdate.disposeUnpaidInfo(map);
		}catch (Exception e){
			//异常消息反馈
			return CreateOpMsg.createRAR_RAR(oldmsgid,sendApp,"AE","失败"+e.toString());
		}
		return msg;
	}

	/**
     * 号源排班
	 * 1.消息解析
	 * 2.消息逻辑查询拼接消息回传
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String receiveZHYZRN(Message hapiMsg) throws HL7Exception {
		log.info("接收ZHY^ZRN");
		Map<String, Object> mshMap = MsgParseUtils.getMSH(hapiMsg);
		String msgOldId = SDMsgUtils.getPropValueStr(mshMap, "msgOldId");
		Map<String, Object> paramMap = new HashMap<>();
		Segment qrd = (Segment)hapiMsg.get("ZRN");
		//ZRN-1  号源开始时间
		String dateStart = qrd.getField(1, 0).encode();
		if(StringUtils.isBlank(dateStart)) {
			throw new BusException("未传入号源查询【开始】时间，请检查");
		}
		String start = dateStart.substring(0, 8);
		paramMap.put("start", start);
		//ZRN-2  号源结束时间
		String dateEnd = qrd.getField(2, 0).encode();
		if(StringUtils.isBlank(dateEnd)) {
			throw new BusException("未传入号源查询【结束】时间，请检查");
		}
		String end = dateEnd.substring(0, 8);
		paramMap.put("end", end);
		//ZRN-4  科室编码
		String deptId = qrd.getField(4, 0).encode();
		paramMap.put("deptid", deptId);
		//ZRN-5  医生编码
		String drId = qrd.getField(5, 0).encode();
		paramMap.put("drId", drId);
		paramMap.put("euStatus", "8");//审核状态
		paramMap.put("flagStop", "0");//未取消
		ACK_ZRN ack = new ACK_ZRN();
		String msg = "";
		SDMsgUtils.createMSHMsg(ack.getMSH(), SDMsgUtils.getMsgId(), "ACK", "ZRN");
		//消息发送方——>消息接收方
		ack.getMSH().getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(mshMap, "receive"));
		List<Map<String, Object>> schInfoList   = sDOpMsgMapper.qryArrGroup(paramMap);
		int size = schInfoList.size();
		if(size==0){
			StringBuffer excStr = new StringBuffer();
			if(deptId!="")excStr.append("未查询到该科室编号（"+deptId+"）,");
			if(drId!="")excStr.append("未查询到该医生编号（"+drId+"）,");
				throw new BusException(excStr.toString()+"在"+start+"-"+end+"时间段内排班信息，请检查");
		}
		try {
			ack.getMSA().getAcknowledgementCode().setValue("AA");
			ack.getMSA().getMessageControlID().setValue(msgOldId);
			ack.getMSA().getTextMessage().setValue("成功");
			ack.getMSA().getExpectedSequenceNumber().setValue("100");
			ack.getMSA().getDelayedAcknowledgmentType().setValue("F");
			Map<String, Object>  map = new HashMap<>();
			for (int i = 0; i < size; i++) {
				Map<String, Object>  schInfoMap = schInfoList.get(i);
				Z11 z11 = ack.getZ11Loop(i).getZ11();
				if( i ==0)
					map = sDOpMsgMapper.qrySchDictByOneKey(schInfoMap).get(0);
				else{
					String pk0= SDMsgUtils.getPropValueStr(schInfoList.get(i-1),"pkSch");
					String pk1 = SDMsgUtils.getPropValueStr(schInfoList.get(i),"pkSch");
					if (!pk0.equals(pk1)){
						map = sDOpMsgMapper.qrySchDictByOneKey(schInfoMap).get(0);
					}
				}
				//z11-1 序号 //唯一号
				z11.getID().setValue(SDMsgUtils.getPropValueStr(map,"pkSch")+"#"+SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0].substring(8,14));//唯一号
				//z11-2 排班类型 0科室/1医生
				z11.getSCHEMA_TYPE().setValue(SDMsgUtils.getPropValueStr(map, "euRestype"));
				//z11-3 看诊日期
				z11.getSEE_DATE().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0].toString().substring(0, 8));
				//z11-4 星期
				z11.getWEEK().setValue(SDMsgUtils.getPropValueStr(map, "weekNo"));
				//z11-5 开始日期
				z11.getBEGIN_TIME().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[0]);
				//z11-6 结束日期
				z11.getEND_TIME().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"timegroup").split("-")[1]);
				//z11-7 科室代号
		    	z11.getDEPT_CODE().setValue(SDMsgUtils.getPropValueStr(map,"codeDept"));
				//z11-8 科室名称
				z11.getDEPT_NAME().setValue(SDMsgUtils.getPropValueStr(map,"nameDept"));
				//z11-9 医师代号
				z11.getDOCT_CODE().setValue(SDMsgUtils.getPropValueStr(map,"codeEmp"));
				//z11-10 医生姓名
				z11.getDOCT_NAME().setValue(SDMsgUtils.getPropValueStr(map,"nameEmp"));
				//z11-11 医生类型
				String dtEmptype=SDMsgUtils.getPropValueStr(map, "dtEmptype");
				if("1".equals(dtEmptype)){
					z11.getDOCT_TYPE().setValue("1");
				}else if("5".equals(dtEmptype) || "6".equals(dtEmptype)){
					z11.getDOCT_TYPE().setValue("2");
				}
				//z11-12  来人挂号限额
				z11.getREG_LMT().setValue("");
				//z11-13  挂号已挂
				z11.getREGED().setValue("");
				//z11-14  当前时间分组限额 （实际：总号数 countnum）
				z11.getTEL_LMT().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"countnum"));
				//z11-15  当前时间已挂号 （实际：可预约数 rmngnum）
				z11.getTEL_REGED().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"rmngnum"));
				//z11-16  当前时间已预约 （实际：已使用 usenum）
				z11.getTEL_REGING().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"usenum"));
				//z11-17 特诊挂号限额
				z11.getSPE_LMT().setValue("");
				//z11-18 特诊已挂
				z11.getSPE_REGED().setValue("");
				//z11-19 1正常/0停诊
				String status=SDMsgUtils.getPropValueStr(map, "euStatus");
				String flagStop=SDMsgUtils.getPropValueStr(map, "flagStop");
				String delFlag=SDMsgUtils.getPropValueStr(map, "delFlag");
				if("8".equals(status)&&"0".equals(flagStop)&&"0".equals(delFlag) ){
					//未停止，未删除，已发布的为正常 其他的为停诊
					z11.getVALID_FLAG().setValue("1");
				}else {
					//恢复操作  ，取消发布  1正常/0停诊
					z11.getVALID_FLAG().setValue("0");
				}
				//z11-20 1加号/0否
				String cntadd = SDMsgUtils.getPropValueStr(map, "cntAdd");
				if(cntadd.equals("")){
					cntadd = "0";
				}
				z11.getAPPEND_FLAG().setValue( Integer.parseInt(cntadd)>0?"1":"0");//1加号/0否
				//z11-21	REASON_NO	停诊原因
				//z11-22	REASON_NAME	停诊原因名称
				//z11-23  停止人 23
				//z11-24 STOP_DATE	停止时间
				//z11-25 ORDER_NO	顺序号
				//z11-26 挂号级别代码
				z11.getREGLEVL_CODE().setValue(SDMsgUtils.getPropValueStr(map, "codeSrv"));
				//z11-27 REGLEVL_NAME	挂号级别名称
				z11.getREGLEVL_NAME().setValue(SDMsgUtils.getPropValueStr(map, "nameSrv"));
				//z11-28 REMARK	备注 是否专病
				z11.getREMARK().setValue(SDMsgUtils.getPropValueStr(map, "flagSpecdise"));
				//z11-29 OPER_CODE	操作员
				z11.getOPER_CODE().setValue(SDMsgUtils.getPropValueStr(map, "nameEmpChk"));
				//z11-30 OPER_DATE	最近改动日期
				z11.getOPER_DATE().setValue(SDMsgUtils.getPropValueStr(map, "ts"));
				//z11-31 	NOON_ID 午别
				String codeDateslot=SDMsgUtils.getPropValueStr(map, "codeDateslot");
				String namDateSlot="";
				if("0101".equals(codeDateslot)||"005".equals(codeDateslot)||"0601".equals(codeDateslot)||"0402".equals(codeDateslot)){
					namDateSlot="1";
				}else if("0102".equals(codeDateslot)||"006".equals(codeDateslot)||"0602".equals(codeDateslot)||"0401".equals(codeDateslot)){
					namDateSlot="2";
				}else if("0201".equals(codeDateslot)){
					namDateSlot="早班";
				}else if("0202".equals(codeDateslot)){
					namDateSlot="中班";
				}else if("0203".equals(codeDateslot)){
					namDateSlot="晚班";
				}else if("0301".equals(codeDateslot)){
					namDateSlot="连续";
				}else if("0603".equals(codeDateslot)){
					namDateSlot="3";
				}else{
					namDateSlot = codeDateslot;
				}
				//z11-31 	NOON_ID 午别
				z11.getNOON_ID().setValue(namDateSlot);
				//z11-32	INTERVAL	排班时间间隔
				z11.getINTERVAL().setValue(SDMsgUtils.getPropValueStr(map, "minutePer"));
				//z11-33	WEB_LMT	网站挂号限额
				z11.getWEB_LMT().setValue("");
				//z11-34	WEIXIN_LMT	微信挂号限额
				z11.getWEIXIN_LMT().setValue(SDMsgUtils.getPropValueStr(schInfoMap,"countnum"));
				//z11-35	OTHER_LMT	预留挂号限额
				z11.getOTHER_LMT().setValue("");
				//新字段文档未定定义
				//36 可预约号
				String rmngnum = SDMsgUtils.getPropValueStr(schInfoMap,"rmngnum");
				z11.getRmngNum().setValue(Integer.valueOf(rmngnum)<0?"0":rmngnum);
				//37 科室编码
				z11.getCODE_RES().setValue(SDMsgUtils.getPropValueStr(map, "codeRes"));
				//38 科室名称
				z11.getNAME_RES().setValue(SDMsgUtils.getPropValueStr(map, "nameRes"));
			}
			msg =  SDMsgUtils.getParser().encode(ack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 预挂号支付     APPRF：APP自费(160微信支付宝) APPRFPTYB：APP医保(160微信支付宝)  CARDRF：卡自费(自助机)  CARDRFPTYB：卡医保(自助机)
	 * 住院预交金     INPAY
	 * 住院押金缴入  APPPRE
	 * 诊中支付          APPPY：APP自费(160微信支付宝)  APPPYPTYB：APP医保(160微信支付宝) CARDPY：卡自费(自助机)  CARDPYPTYB：卡医保(自助机)
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String receiveDFTP03(Message hapiMsg, String logPrefix) throws HL7Exception, ParseException{
		String msgMsg = null;
		DFT_P03 dft= (DFT_P03)hapiMsg;
		String mesType = dft.getFINANCIAL(0).getFT1().getTransactionType().getValue();
		//消息发送方
		String sendApp = dft.getMSH().getSendingApplication().getNamespaceID().getValue();
		log.info(logPrefix + "enter DFT^P03 process type:" + mesType + " sendApp:" + sendApp);
		switch (mesType) {
			case "APPRF":{
				//// 预挂号支付 体检系统
				if("PEIS".equals(sendApp))
					msgMsg = sDSysMsgBodyCheckService.appRegistPeisPiInfoPay(dft,hapiMsg);
				// 预挂号支付(160自费)
				else msgMsg = msgWeChatService.appRegist160PiInfoPay(dft,hapiMsg,logPrefix);
			}break;
			case "APPRFPTYB":msgMsg = msgWeChatService.appRegist160PiInfoPay(dft,hapiMsg,logPrefix);break;// 预挂号支付(160医保)
			case "APPRFT":msgMsg = msgWeChatService.appRegistPiInfoPayExit(dft,hapiMsg);break;// 预挂号退费(160自费)
			case "APPRFPTYBT":msgMsg = msgWeChatService.appRegistPiInfoPayExit(dft,hapiMsg);break;// 预挂号退费(160医保)
			case "APPPY":msgMsg=msgWeChatService.receiveP03OpPvSettle(dft,logPrefix);break;// 诊中支付(160自费)
			case "APPPYPTYB":msgMsg = msgWeChatService.receiveP03OpPvSettleYb(dft,logPrefix);break;// 诊中支付(160医保)
			case "INPAY":break;// 住院预交金
			case "APPPRE":break;// 住院押金缴入
			case "CARDRF":msgMsg = sDSysMsgSelfAppService.appRegistCardPiInfoPay(dft,hapiMsg);break;// 预挂号支付(自助机自费)
			case "CARDRFPTYB":msgMsg = sDSysMsgSelfAppService.appRegistCardPiInfoPay(dft,hapiMsg);break;//预挂号支付(自助机医保)
			//case "CARDRFT":msgMsg = appRegistPiInfoPayExit(dft,hapiMsg);break;// 预挂号退费(自助机自费)
			//case "CARDRFPTYBT":msgMsg = appRegistPiInfoPayExit(dft,hapiMsg);break;//预挂号支付退费(自助机医保)
			case "CARDPY":msgMsg = sDSysMsgSelfAppService.receiveCardSettle(dft);break;// 诊中支付(自助机自费)
			case "CARDPYPTYB":msgMsg = sDSysMsgSelfAppService.receiveCardSettleYb(dft);break;// 诊中支付(自助机医保)
			default:break;
		}
		return msgMsg;
	}
		

	/**
	 * APP_REG：预挂号(微信) APPT_REG：预约挂号 APPRA：患者建档QHYS：取号预算  CARD_REG：预挂号(自助机)
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String receiveSRMS01(Message hapiMsg) throws HL7Exception{
		String msgMsg = null;
		SRM_S01 so1 = (SRM_S01)hapiMsg;
		ARQ arq = so1.getARQ();
		//消息id
		String oldMsgId = so1.getMSH().getMessageControlID().getValue();
		String sendApp = so1.getMSH().getSendingApplication().getNamespaceID().getValue();
		//ARQ-7 预约原因
		String stu = arq.getAppointmentReason().getIdentifier().getValue();
		try{
			switch (stu) {
				case "APP_REG":msgMsg = msgWeChatService.appRegistPiInfo(so1,hapiMsg);break;//预挂号(微信)
				case "APPRA":msgMsg = piInfoCreated(so1);break;//患者建档
				case "QHYS":msgMsg = sDSysMsgPubService.appRegistPiInfo(so1,hapiMsg);break;//取号预算
				case "CARD_REG":msgMsg = sDSysMsgSelfAppService.appRegistPiInfo(so1,hapiMsg);break;//预挂号(自助机)
				default:break;
			}
		}catch (Exception e){
			//异常反馈消息
			return CreateOpMsg.createSRR_S01(oldMsgId,sendApp,"AE","失败:" + e.getMessage()+" 或者到窗口核实!");
		}
		return msgMsg;
	}

	

	/**
	 * 患者建档信息
	 * @param so1
	 * @return
	 * @throws ParseException 
	 * @throws HL7Exception 
	 */
	public String piInfoCreated(SRM_S01 so1) throws ParseException, HL7Exception{
		String oldMsgId = so1.getMSH().getMessageControlID().getValue();
		String sendApp = so1.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		PiMaster master  = new PiMaster();
		//ARQ arq = so1.getARQ();
		PID pid = so1.getPATIENT(0).getPID();
		//PID-5   患者姓名
		String piName =pid.getPatientName(0).getFamilyName().getSurname().getValue();
		//PID-7   患者出生日期
		String piTime =pid.getDateTimeOfBirth().getTimeOfAnEvent().getValue();
		//PID-8   患者性别
		String piSex =pid.getAdministrativeSex().getValue();
		//PID-13   患者手机号
		String piTel =pid.getPhoneNumberHome(0).getPhoneNumber().getValue();
		//PID-38   患者医保类型-id
		String piInsurTypeId =pid.getProductionClassCode().getIdentifier().getValue();
		//PID-38   患者医保类型-name
		String piInsurTypeName =pid.getProductionClassCode().getText().getValue();
		if(piTime==null){
			throw new BusException("请传入患者出生日期");
		}
		//ARQ-1 流水号
		//String runWaterCode = arq.getPlacerAppointmentID().getEntityIdentifier().getValue();
		int size = pid.getPatientIdentifierListReps();
		for (int i = 0; i < size; i++) {
			String id = pid.getPatientIdentifierList(i).getID().getValue();
			String idType = pid.getPatientIdentifierList(i).getIdentifierTypeCode().getValue();
			if ("IdentifyNO".equals(idType)) {
				//身份证号
				if(StringUtils.isNotBlank(id)){
					//校验身份证号(只对18位的身份证做校验)
					if(id.length() == 18 && IdCardUtils.validate(id)){
						Map<String, Object> birAgeSex = IDCardUtil.getBirthAgeSex(id);
						//对比患者性别是否一致
						if(StringUtils.isBlank(piSex) || !piSex.equalsIgnoreCase(SDMsgUtils.getPropValueStr(birAgeSex,"sex"))){
							piSex = SDMsgUtils.getPropValueStr(birAgeSex,"sex");
						}
						//对比患者出生日期是否一致
						if(StringUtils.isBlank(piTime) || !piTime.equalsIgnoreCase(SDMsgUtils.getPropValueStr(birAgeSex,"birthday"))){
							piTime = SDMsgUtils.getPropValueStr(birAgeSex,"birthday");
						}
					}
					master.setDtIdtype("01");
					master.setIdNo(id.toUpperCase());
				}
			}else if("PassportNO".equals(idType)){
				//护照号
					if(StringUtils.isNotBlank(id)){
						master.setDtIdtype("10");
						master.setIdNo(id.toUpperCase());
					}
			}else if("PermanentIdentityCard".equals(idType)){
				//港澳居民身份证
				if(StringUtils.isNotBlank(id)){
					master.setDtIdtype("03");
					master.setIdNo(id.toUpperCase());
				}
			}else if ("ReentryPermitNO".equals(idType)) {
				//港澳居民来往内地通行证（回乡证）
				if(StringUtils.isNotBlank(id)){
					master.setDtIdtype("05");
					master.setIdNo(id.toUpperCase());
				}
			}else if ("MainlandTravelPermit".equals(idType)) {
				//台湾居民来往大陆通行证（台胞证）
				if(StringUtils.isNotBlank(id)){
					master.setDtIdtype("08");
					master.setIdNo(id.toUpperCase());
				}
			}else if ("OfficerID".equals(idType)) {
				//军官证号
				if(StringUtils.isNotBlank(id)){
					master.setDtIdtype("02");
					master.setIdNo(id.toUpperCase());
				}
			}else if("BirthCertificate".equals(idType)){
				//出生医学证明
				if(StringUtils.isNotBlank(id)){
					master.setDtIdtype("09");
					master.setIdNo(id.toUpperCase());
				}
			}
		}
		master.setDtSex(SDMsgUtils.getParseSex(piSex));
		master.setBirthDate(sd.parse(piTime.substring(0,8)));
		master.setNamePi(piName);
		//master.setTelNo(piTel);
		master.setMobile(piTel);
		master.setPkOrg(Constant.PKORG);//深大的机构编码  暂定写死
		master.setCreator(sendApp);

		User user = new User();
		user.setPkOrg(Constant.PKORG);
		UserContext.setUser(user);
		return sDMsgDataUpdate.addPiInfo(master,oldMsgId,piInsurTypeId,piInsurTypeName,sendApp);
	}

	/**
	 * 取消预约挂号
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String receiveSRMS04(Message hapiMsg) throws HL7Exception {
		SRM_S01 s04 =  (SRM_S01)hapiMsg;
		String oldMsgId =  s04.getMSH().getMessageControlID().getValue();
		String piId = s04.getPATIENT(0).getPID().getPatientID().getID().getValue();
		String pvcode = s04.getPATIENT(0).getPV1().getVisitNumber().getID().getValue();
		String sendApp = s04.getMSH().getSendingApplication().getNamespaceID().getValue();

		Map<String, Object> appRegParam = new ConcurrentHashMap<String, Object>();
		appRegParam.put("sendApp", sendApp);
		appRegParam.put("oldmsgid", oldMsgId);
		appRegParam.put("patientno", piId);
		appRegParam.put("code", pvcode);
		//根据系统名字，写操作人
		if("YYT".equals(sendApp)){
			appRegParam.put("pkEmp", Constant.PKZZJ);
			appRegParam.put("nameEmp", Constant.NAMEZZJ);
		}else{
			appRegParam.put("pkEmp", Constant.PKWECHAT);
			appRegParam.put("nameEmp", Constant.NAMEWECHAT);
		}
		return sDMsgDataUpdate.cancelPiReg(appRegParam);
	}

	/**
	 * 住院费每日账单列表(Theme)   住院费每日账单详情明细(Details)
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String receiveQBPZZL(Message hapiMsg) throws HL7Exception{
		log.info("QBP^ZZL");
		Map<String, Object> paramMap = new HashMap<>();
		String msg = "";
		Segment zzl = (Segment)hapiMsg.get("QPD");
		String qryName = zzl.getField(1, 0).encode();//消息查询名称
		String ipCode = zzl.getField(3, 0).encode();//患者就诊流水号
		Map<String, Object> mshMap = MsgParseUtils.getMSH(hapiMsg);
		paramMap.putAll(mshMap);
		String msgCntrlID = SDMsgUtils.getPropValueStr(mshMap, "msgOldId");
		paramMap.put("msgCntrlID", msgCntrlID);
		//1900014831^^^^PatientSerialNO^^20200312
		char[] ch = ipCode.toCharArray();
		int chLen = ch.length;
		char s = '^' ;
		String code = "";
		StringBuffer strBuf = new StringBuffer();
		for (int j = 0; j < chLen; j++) {
			if(ch[j]==s){
				break;
			}
			strBuf.append(ch[j]);
		}
		code = strBuf.toString();
		paramMap.put("ipCode", code);
		StringBuffer strBuf2 = new StringBuffer();
		if("Details".equals(qryName)){//详情Details
			String time = "";
			for (int j = chLen-1; j > 0; j--) {
				if(ch[j]==s){
					break;
				}
				strBuf2.append(ch[j]);
			}
		    time = strBuf2.reverse().toString();
			paramMap.put("time", time);
			//PlatFormSendUtils.sendBlWeiXinQBPZZLMsgDetails(paramMap);
			msg = sDMsgSendIp.sendQBPZZLMsgDetails(paramMap);
		}else{//列表Theme
			//PlatFormSendUtils.sendBlWeiXinQBPZZLMsgTheme(paramMap);
			msg = sDMsgSendIp.sendQBPZZLMsgTheme(paramMap);
		}
		return msg ;
	}

	/**
	 * 查询住院缴费记录
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public String  receiveSQMZQ1(Message hapiMsg) throws HL7Exception{
		log.info("SQM^ZQ1");
		String msg = "";
		Map<String, Object> paramMap = new HashMap<>();
		Map<String, Object> mshMap = MsgParseUtils.getMSH(hapiMsg);
		paramMap.putAll(mshMap);
		String msgOldId = SDMsgUtils.getPropValueStr(mshMap, "msgOldId");
		paramMap.put("msgOldId", msgOldId);
		Segment qrd = (Segment)hapiMsg.get("QRD");
		String ipCode = qrd.getField(4, 0).encode();
		paramMap.put("ipCode", ipCode);
		String outsideOrderId = qrd.getField(8, 0).encode();
		paramMap.put("outsideOrderId", outsideOrderId);
		String condition = qrd.getField(9, 0).encode();
		try{
			if("RES".equals(condition)){
				msg = sDMsgSendOp.sendSQRZQ1Msg(paramMap);//查询门诊预约结果
			}else{
				msg = sDMsgSendIp.sendSQRZQ1Msg(paramMap);//查询住院缴费记录
			}
		}catch(Exception e){
			//失败反馈消息
			return CreateOpMsg.createSQR_ZQ1(msgOldId,SDMsgUtils.getPropValueStr(mshMap, "receive"),"AE","失败"+e.toString());
		}
		return msg;
	}

	/**
	 * 手术排班
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public void  receiveSIUS12(Message hapiMsg) throws HL7Exception{
		 log.info("SIU^S12");
		 Map<String, Object> orderMap = new HashMap<>();
		 ExOpSch exOpSch = new ExOpSch();
		 SIU_S12 siu = (SIU_S12)hapiMsg;
		 String meString = siu.getMSH().getMessageControlID().getValue();
		 //sch-26 手术申请号
		 String appId = siu.getSCH().getPlacerOrderNumber(0).getEntityIdentifier().getValue();
		 orderMap.put("appId", appId);
		 int exist = DataBaseHelper.execute("select * from cn_op_apply where pk_cnord = (select pk_cnord from cn_order where  code_apply = ? and code_ordtype = '04' and del_flag = '0') and del_flag = '0'", appId);
		 if(exist == 0){
			 throw new BusException("未查询到编号为:"+appId+"手术申请单信息");
		 }
		 if(StringUtils.isNotBlank(appId)){
			 Map<String, Object> patiInfo = new HashMap<>();
			 patiInfo = DataBaseHelper.queryForMap("select ord.pk_org,ord.pk_pv,ord.pk_cnord,ord.pk_dept,dept.name_dept,ord.date_sign,pv.dt_sex,pv.age_pv,pi.code_ip,pv.name_pi,bed.bedno from cn_order ord left join pv_encounter pv on pv.pk_pv =ord.pk_pv left join pv_bed bed on pv.pk_pv =bed.pk_pv left join bd_ou_dept dept on dept.pk_dept = ord.pk_dept left join pi_master pi on pi.pk_pi = pv.pk_pi   where ord.code_apply = ? and ord.code_ordtype = '04'",appId);
			 exOpSch.setPkOpsch(SDMsgUtils.getPk());
			 exOpSch.setPkOrg(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
			 exOpSch.setPkPv(SDMsgUtils.getPropValueStr(patiInfo,"pkPv"));
			 exOpSch.setPkCnord(SDMsgUtils.getPropValueStr(patiInfo,"pkCnord"));
			 exOpSch.setPkDept(SDMsgUtils.getPropValueStr(patiInfo,"pkDept"));
			 exOpSch.setNameDept(SDMsgUtils.getPropValueStr(patiInfo,"nameDept"));
			 String dateApply = (SDMsgUtils.getPropValueStr(patiInfo,"dateSign"));
			 if(!"".equals(dateApply)){
				 try {
					exOpSch.setDateApply(sdfDate.parse(dateApply));
				} catch (ParseException e) {
					e.printStackTrace();
				}//SHIJIAN
			 }
			 String sexCode = SDMsgUtils.getPropValueStr(patiInfo,"dtSex");
			 if("02".equals(sexCode)){
				 sexCode ="男";
			 }else if ("03".equals(sexCode)) {
				 sexCode ="女";
			 }else if ("04".equals(sexCode)) {
				 sexCode ="未说明的性别";
			 }
			 exOpSch.setDtSex(sexCode);
			 exOpSch.setAgePv(SDMsgUtils.getPropValueStr(patiInfo,"agePv"));
			 exOpSch.setCodeIp(SDMsgUtils.getPropValueStr(patiInfo,"codeIp"));
			 exOpSch.setNamePi(SDMsgUtils.getPropValueStr(patiInfo,"namePi"));
			 exOpSch.setBedNo(SDMsgUtils.getPropValueStr(patiInfo,"bedno"));
			 exOpSch.setCodeApply(appId);

			 //能否取到录入者
			 String creatorname = siu.getSCH().getEnteredByPerson(0).getIDNumber().getValue();
			 exOpSch.setCreator(creatorname);
			 exOpSch.setCreateTime(new Date());
			 Segment zopSegment =(Segment)siu.get("ZOP");
			 //String zop2  = zopSegment.getField(2, 0).encode();//手术室  zop-2
			 String zop3  = zopSegment.getField(3, 0).encode();//手术室^手术间  zop-3
			 String [] opRoom = zop3.split("#");
			 String opBigRoom = opRoom[0].toString();
			 String opSmallRoom = opRoom[1].toString();
			 Map<String, Object> empMap1 = new HashMap<>();
			 empMap1 = DataBaseHelper.queryForMap("select pk_dept,name_dept from bd_ou_dept where code_dept = ?",opBigRoom);
			 Map<String, Object> empMap2 = new HashMap<>();
			 empMap2 = DataBaseHelper.queryForMap("SELECT name,name_place FROM bd_res_opt WHERE code= ?",opSmallRoom);
			 exOpSch.setPkDeptOp(SDMsgUtils.getPropValueStr(empMap1,"pkDept"));//手术室
			 exOpSch.setNameDeptOp(SDMsgUtils.getPropValueStr(empMap1,"nameDept"));//手术室名称
			 exOpSch.setRoomNo(SDMsgUtils.getPropValueStr(empMap2,"name"));//手术室间
			 exOpSch.setRoomPlace(SDMsgUtils.getPropValueStr(empMap2,"namePlace"));//手术位置
			 //基于深大项目手术入参及实际前台展示需求--暂定写死院区
			 exOpSch.setNameOrgarea("院本部");
			 exOpSch.setPkOrgarea("5c1acc1a4b1a48e0be2b0f7cec8cafbd");

			 //台次      手术排班表和手术申请表中的台次和场次是一个意思吗??
			 String zop4  = zopSegment.getField(4, 0).encode();
			 exOpSch.setTicketno(Integer.valueOf(zop4));

			 //紧急标志
			 String zop8  = zopSegment.getField(8, 0).encode();
			 if ("1".equals(zop8)) {
				 zop8 = "3";
			 }else if("0".equals(zop8)){
				 zop8 = "1";
			 }else {
				 zop8 = "2";
			 }
			 exOpSch.setEuOptype(zop8);

			//手术人员   -- 需循环
			 String zop10  = zopSegment.getField(10, 0).encode();
			 List<String> listStrZop10 =  splitString(zop10);
			 int zopLen=listStrZop10.size();
			 List<Map<String, Object>> empListMap = new ArrayList<>();
			 for (int i = 0; i < zopLen; i++) {
				 Map<String, Object> empMap = new HashMap<>();
				 empMap = DataBaseHelper.queryForMap("SELECT pk_emp,name_emp FROM bd_ou_employee WHERE code_emp= ?",listStrZop10.get(i));
				 empListMap.add(empMap);
			 }
			 if (empListMap.size()>1) {
				 exOpSch.setNameEmp(SDMsgUtils.getPropValueStr(empListMap.get(0),"nameEmp"));
				 exOpSch.setPkEmp(SDMsgUtils.getPropValueStr(empListMap.get(0),"pkEmp"));
				 exOpSch.setNameEmpAsst(SDMsgUtils.getPropValueStr(empListMap.get(1),"nameEmp"));
				 exOpSch.setPkEmpAsst(SDMsgUtils.getPropValueStr(empListMap.get(1),"pkEmp"));
			 }else if (empListMap.size()>0){
				 exOpSch.setNameEmp(SDMsgUtils.getPropValueStr(empListMap.get(0),"nameEmp"));
				 exOpSch.setPkEmp(SDMsgUtils.getPropValueStr(empListMap.get(0),"pkEmp"));
			 }
			 //String zop11  = zopSegment.getField(11, 0).encode();//麻醉人员
			 //String zop12  = zopSegment.getField(12, 0).encode();//灌注医生
			 //String zop13  = zopSegment.getField(13, 0).encode();//输血者
			 //String zop14  = zopSegment.getField(14, 0).encode();//台上护士
			 //String zop15  = zopSegment.getField(15, 0).encode();//供应护士
			 //String zop16  = zopSegment.getField(15, 0).encode();//16手术申请项目(手术代码^手术名称^手术规模)待定
			 //特殊准备（体位、用物、仪器）
			 String zop21  = zopSegment.getField(21, 0).encode();
			 exOpSch.setSpecEquipment(zop21);
			 //备注
			 String zop22  = zopSegment.getField(22, 0).encode();
			 exOpSch.setNote(zop22);
			 //手术名称
			 String zop25  = zopSegment.getField(25, 0).encode();
			 exOpSch.setNameOpt(zop25);
			 //手术开始时间
			 String zop36  = zopSegment.getField(36, 0).encode();
			 /*orderMap.put("dateplan", zop36);*/
			 String dateSch =  siu.getSCH().getAppointmentTimingQuantity(0).getStartDateTime().getTimeOfAnEvent().getValue();
			 if(!"".equals(zop36)){
				 try {
					 exOpSch.setDatePlan(sdf.parse(zop36));//计划时间
					 exOpSch.setDateSch(sdf.parse(dateSch));//排班日期
				 } catch (ParseException e) {e.printStackTrace();}
			 }
			 Map<String, Object> empMapSch = new HashMap<>();
			 empMapSch = DataBaseHelper.queryForMap("select job.pk_emp,job.pk_dept from bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp   where emp.name_emp = ? ",creatorname);
			 exOpSch.setPkEmpSch(SDMsgUtils.getPropValueStr(empMapSch,"pkEmp"));//排班人
			 exOpSch.setPkDeptSch(SDMsgUtils.getPropValueStr(empMapSch,"pkDept"));//排班部门
			 exOpSch.setNameEmpSch(creatorname);
			 exOpSch.setEuStatus("1");
			 exOpSch.setDelFlag("0");

			 sDMsgDataUpdate.updateCnOpApplyList(exOpSch,orderMap);
		 }else{
			 log.info("消息编码为"+meString+"的SIUS12(手术排班--住院)无申请单号，请传申请单号");
		 }

	}

	/**
	 * 手术室撤销手术(住院)
	 * @param hapiMsg
	 * @throws HL7Exception
	 */
	public void receiveORMO01(Message hapiMsg) throws HL7Exception{
		 ORM_O01 orm = (ORM_O01)hapiMsg;
		 String meString = orm.getMSH().getMessageControlID().getValue();
		 Map<String,Object> queryMap = new HashMap<String,Object>();
		 int len=orm.getORDERReps();
		 for(int i=0;i<len;i++){
			 ORC orc=orm.getORDER(i).getORC();
			 String control=orc.getOrderControl().getValue();
			 String pkEmp = "";
			 String nameEmp = "";
			 if("OC".equals(control)){
				 String appId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();//ORC-2  申请单号
				 String exCodeDoc=orc.getEnteredBy(0).getIDNumber().getValue();//ORC-10操作人员
				 if(StringUtils.isNotBlank(exCodeDoc)){//根据lis返回操作员工号查询操作员信息
					 Map<String, Object> bdOuMap = DataBaseHelper.queryForMap("SELECT PK_EMP,NAME_EMP FROM bd_ou_employee WHERE CODE_EMP= ? and del_flag = '0' ",exCodeDoc);
					 if(bdOuMap!=null){
						 pkEmp=SDMsgUtils.getPropValueStr(bdOuMap,"pkEmp");
						 nameEmp=SDMsgUtils.getPropValueStr(bdOuMap,"nameEmp");
					 }
				 }
				List<Map<String,Object>> orderList= null;
				if(StringUtils.isNotBlank(appId)){
					queryMap.put("codeApply", appId);
					queryMap.put("codeOrdtype", "04%");
					orderList=sDReceiveMapper.queryReqOrdList(queryMap);
				}
				if(orderList!=null&&orderList.size()>0){
					 for(Map<String,Object> orderMap :orderList){
						 if(StringUtils.isNotBlank(pkEmp)){
							 orderMap.put("pkemp",pkEmp);//操作医生主键
							 orderMap.put("nameEmp",nameEmp);//操作医生主键
						 }
						orderMap.put("control", control);
						sDMsgDataUpdate.updateOpApplyList(orderMap);
					 }
				 }else{
					 log.info("消息编码为"+meString+"的ORMO01(手术室撤销手术--住院)无申请单号，请传申请单号");
				 }
			 }
		 }


	}

	/**
	 * 手术执行确认(住院)
	 * @param hapiMsg
	 * @throws HL7Exception
	 */
	public void receiveORRO02(Message hapiMsg) throws HL7Exception{
		 ORR_O02 orr = (ORR_O02)hapiMsg;
		 String meString = orr.getMSH().getMessageControlID().getValue();
		 Map<String,Object> queryMap = new HashMap<String,Object>();
		 int len=orr.getRESPONSE().getORDERReps();
		 for(int i=0;i<len;i++){
			 ORC orc = orr.getRESPONSE().getORDER(i).getORC();
			 String control=orc.getOrderControl().getValue();
			 String pkEmp = "";
			 if("OR".equals(control)){
				 String appId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue();//ORC-2  申请单号
				 String exCodeDoc=orc.getEnteredBy(0).getIDNumber().getValue();//ORC-10操作人员
				 if(StringUtils.isNotBlank(exCodeDoc)){//根据lis返回操作员工号查询操作员信息
					 Map<String, Object> bdOuMap = DataBaseHelper.queryForMap("SELECT PK_EMP FROM bd_ou_employee WHERE CODE_EMP= ?",exCodeDoc);
					 if(bdOuMap!=null){
						 pkEmp=SDMsgUtils.getPropValueStr(bdOuMap,"pkEmp");
					 }
				 }
				List<Map<String,Object>> orderList= null;
				if(StringUtils.isNotBlank(appId)){
					queryMap.put("codeApply", appId);
					queryMap.put("codeOrdtype", "04%");
					orderList=sDReceiveMapper.queryReqOrdList(queryMap);
				}
				if(orderList!=null&&orderList.size()>0){
					 for(Map<String,Object> orderMap :orderList){
						 if(StringUtils.isNotBlank(pkEmp)){
							 orderMap.put("pkemp",pkEmp);//操作医生主键
						 }
						orderMap.put("control", control);
						orderMap.put("ordtype", "04");
						sDMsgDataUpdate.updateOpApplyList(orderMap);
					 }
				 }else{
					 log.info("消息编码为"+meString+"的ORRO02(手术执行确认--住院)无申请单号，请传申请单号");
				 }
			 }
		 }

	}

	/**
	 * 检查退费(执行确认取消)
	 * @param hapiMsg
	 * @throws Exception
	 */
	public void receiveOMGO19(Message hapiMsg) throws Exception{
		//获取患者标识
		OMG_O19 omg = (OMG_O19)hapiMsg;
		String patientType = omg.getPATIENT().getPATIENT_VISIT().getPV1().getPv12_PatientClass().getValue();
		//消息发送方
		String send = omg.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		if("T".equals(patientType) && "PEIS".equals(send)){
			//门诊体检
			sDSysMsgBodyCheckService.bodyCheckOMGO19(hapiMsg);
		}else{
			//住院
			noRefundOMGO19(hapiMsg);
		}
	}
	
	
	
	/**
	 * 检查不退费只更新状态服务
	 * @param hapiMsg
	 * @throws Exception
	 */
	private void noRefundOMGO19(Message hapiMsg) throws Exception{
		OMG_O19 omg = (OMG_O19)hapiMsg;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sysName", omg.getMSH().getSendingApplication().getNamespaceID().getValue());
		//医嘱号集合
		Set<String> ordsnSet = new HashSet<String>();
		//申请单号集合
		Set<String> codeApplySet = new HashSet<String>();
		int len = omg.getORDERReps();
		for(int i=0;i<len;i++){
			ORC orc = omg.getORDER(i).getORC();
			//医嘱号
			 String ordsn = orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
			 if(ordsn == null || "".equals(ordsn)){
				 log.info("检查退费失败;申请单号为空！\r\n"+hapiMsg.toString());
				 throw new BusException("申请单号为空！");
			 }else{
				String[] split = ordsn.split("H");
				codeApplySet.add(split[0]);
				ordsnSet.add(split[1]);
			 }
			//日期
			String dateOcc = orc.getOrc9_DateTimeOfTransaction().getTs1_TimeOfAnEvent().getValue();
			paramMap.put("dateOcc", dateOcc);
			//获取操作医生工号 ORC-10
			String codeEmp = orc.getEnteredBy(0).getIDNumber().getValue();
			paramMap.put("codeEmp", codeEmp);
			//获取部门号 ORC17
			String codeDept = orc.getEnteringOrganization().getIdentifier().getValue();
			paramMap.put("codeDept", codeDept);
		}
		//查询执行人数据
		 Map<String, Object> queryEmpByCode = sDQueryUtils.queryEmpByCode(SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
		if(queryEmpByCode != null && queryEmpByCode.size()>0){
			paramMap.putAll(queryEmpByCode);
		}else{
			log.info("检查计费未找到计费人信息！"+hapiMsg.toString());
			paramMap.put("pkEmp", paramMap.get("sysName"));
			paramMap.put("nameEmp", paramMap.get("sysName"));
			 //throw new BusException("检查计费未找到计费人信息");
		}
		//查询科室相关信息
		Map<String, Object> queryDeptByCode = sDQueryUtils.queryDeptByCode(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
		if(queryDeptByCode != null && queryDeptByCode.size()>0){
			paramMap.putAll(queryDeptByCode);
		}else{
			log.info("检查计费部门信息为空！"+hapiMsg.toString());
			paramMap.put("pkDept", paramMap.get("sysName"));
			paramMap.put("nameDept", paramMap.get("sysName"));
			//throw new BusException("消息中未传入计费部门信息");
		}
		List<CnRisApply> risApply = sDReceiveMapper.queryRisApply(ordsnSet);
		//判断是门诊还是住院患者
		Map<String, Object> map = DataBaseHelper.queryForMap("select pv.EU_PVTYPE from PV_ENCOUNTER pv inner join  CN_ORDER o on pv.PK_PV=o.PK_PV where o.PK_CNORD=?", risApply.get(0).getPkCnord());
		for(CnRisApply cnRisApply : risApply){
			 //更新申请单表  住院接口改为 -1 其他为1
			cnRisApply.setEuStatus("1");
			if("3".equals(SDMsgUtils.getPropValueStr(map,"euPvtype"))){
				cnRisApply.setEuStatus("-1");
			}
			//查询医辅执行表数据
			ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, cnRisApply.getPkCnord());
			if(exAssistOcc!=null){
				//取消时间
				try{
					exAssistOcc.setDateCanc(sdf.parse(SDMsgUtils.getPropValueStr(paramMap, "dateOcc")));
				}catch(Exception e){
					exAssistOcc.setDateOcc(new Date());
					log.info("检查取消登记，取消时间错误！");
				}
				exAssistOcc.setPkEmpCanc(SDMsgUtils.getPropValueStr(paramMap, "pkEmp"));
				exAssistOcc.setNameEmpCanc(SDMsgUtils.getPropValueStr(paramMap, "nameEmp"));
				//exAssistOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(paramMap, "pkDept"));
				//exAssistOcc.setPkOrgOcc(cnRisApply.getPkOrg());
				exAssistOcc.setEuStatus("0");
				exAssistOcc.setFlagOcc("0");
			}
			sDMsgDataUpdate.updateRisApply(cnRisApply,exAssistOcc);
		 }
	}
	
	
	/**
	 * 检查退费服务（暂未启用）
	 * @param hapiMsg
	 */
	private void refundOMGO19(Message hapiMsg){
		 OMG_O19 omg = (OMG_O19)hapiMsg;
		 Map<String,Object> userMap = new HashMap<String,Object>();
		 Map<String,Object> paramMap = new HashMap<String,Object>();
		 //发送系统信息
		 userMap.put("sysName", omg.getMSH().getSendingApplication().getNamespaceID().getValue());
		 int len = omg.getORDERReps();
		 String ordsns = "";
		 for(int i=0;i<len;i++){
			 ORC orc = omg.getORDER(i).getORC();
			 //String control=orc.getOrderControl().getValue();
			 //执行医生编码 orc - 12
			 userMap.put("codeEmp", orc.getOrderingProvider(0).getIDNumber().getValue());
			 //执行科室编码 orc -17
			 userMap.put("codeDept", orc.getEnteringOrganization().getIdentifier().getValue());
			 //申请单号
			 String codeApply = orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
			 String ordsn;
			 if(codeApply == null || "".equals(codeApply)){
				 log.info("检查退费失败;申请单号为空！\r\n"+hapiMsg.toString());
				 throw new BusException("申请单号为空！");
			 }else{
				String[] split = codeApply.split("H");
				codeApply = split[0];
				ordsn = split[1];
				if(i==len-1){
					ordsns += "'"+ordsn+"'";
				}else{
					ordsns += "'"+ordsn+"',";
				}
			 }
		 }
		//查询退费人
		String codeEmp = SDMsgUtils.getPropValueStr(userMap, "codeEmp");
		Map<String, Object> queryEmpByCode = sDQueryUtils.queryEmpByCode(codeEmp);
		if(queryEmpByCode != null && queryEmpByCode.size()>0){
			userMap.putAll(queryEmpByCode);
		}else{
			userMap.put("pkEmp", userMap.get("sysName"));
			userMap.put("nameEmp", userMap.get("sysName"));
			log.info("退费人为空，未找到退费人信息！"+hapiMsg.toString());
			//throw new BusException("消息中未传入退费人信息信息！");
		}
		//查询退费部门
		String codeDept = SDMsgUtils.getPropValueStr(userMap, "codeDept");
		Map<String, Object> queryDeptByCode = sDQueryUtils.queryDeptByCode(codeDept);
		if(queryDeptByCode != null && queryDeptByCode.size()>0){
			userMap.putAll(queryDeptByCode);
		}else{
			log.info("退费部门为空，未找到退费部门信息！"+hapiMsg.toString());
			userMap.put("pkDept", userMap.get("sysName"));
			userMap.put("nameDept", userMap.get("sysName"));
			//throw new BusException("消息中未传入退费部门信息！");
		}
		String sql = "select * from ex_order_occ ex left join cn_order o on ex.pk_cnord=o.pk_cnord where ex.eu_status ='1' and o.ordsn in ("+ordsns+")";
		List<Map<String, Object>> orderList = DataBaseHelper.queryForList(sql);
		String pkCnords = "";
		if(orderList != null && orderList.size()>0){
			for(int i=0;i<orderList.size();i++){
				if(i == orderList.size()-1){
					pkCnords += "'"+SDMsgUtils.getPropValueStr(orderList.get(i), "pkCnord")+"'";
				}else{
					pkCnords += "'"+SDMsgUtils.getPropValueStr(orderList.get(i), "pkCnord")+"',";
				}
			}
		}else{
			log.info("未找到需要退费的记录！\r\n"+hapiMsg.toString());
			throw new BusException("未找到需要退费的记录！");
		}
		//判断是已经记费并且未退费
		//查询收费主键
		//String sqlBl = "select pk_cgip,quan from BL_IP_DT where del_flag = '0' and pk_cgip_back is null and flag_settle='0' and pk_cnord in ("+pkCnords+")";
		//String sqlBl = "select * from BL_IP_DT where del_flag = '0' and pk_cgip_back is null and flag_settle='0' and pk_cnord in ("+pkCnords+")";
		String sqlBl = "select * from (select blip.pk_org,blip.pk_cgip,(blip.quan + nvl( back.quan,0)) as quan  from BL_IP_DT blip left outer join  (select sum(quan) quan,sum(amount) amt,pk_cgip_back from bl_ip_dt  where flag_settle = 0 and flag_pd = 0 and  quan < 0  and pk_cnord in ("+pkCnords+") group by pk_cgip_back ) back on blip.pk_cgip=back.pk_cgip_back where blip.del_flag = '0' and blip.pk_cgip_back is null and blip.flag_settle='0' and blip.quan >0  and blip.pk_cnord in ("+pkCnords+")) dt where dt.quan>0";
		List<Map<String, Object>> BlList = DataBaseHelper.queryForList(sqlBl);
		paramMap.put("BlList", BlList);
		paramMap.put("pkCnords", pkCnords);
		paramMap.put("ordsns", ordsns);
		paramMap.put("msg", hapiMsg.toString());
		sDMsgDataUpdate.updateRisRefund(paramMap,userMap);
	}


	/**
	 * 检查收费(执行确认)
	 * @param hapiMsg
	 * @throws Exception
	 */
	public void receiveORGO20(Message hapiMsg) throws Exception{
		//不计费服务
		noCgORGO20(hapiMsg);
		//计费服务
		//cgORGO20(hapiMsg);
	}
	
	/**
	 * 检查消息不计费，只更新状态服务
	 * @param hapiMsg
	 * @throws Exception
	 */
	private void noCgORGO20(Message hapiMsg) throws Exception{
		ORG_O20 org = (ORG_O20)hapiMsg;
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("sysName", org.getMSH().getSendingApplication().getNamespaceID().getValue());
		//医嘱号集合
		Set<String> ordsnSet = new HashSet<String>();
		int len = org.getRESPONSE().getORDERReps();
		for(int i=0;i<len;i++){
			ORC orc = org.getRESPONSE().getORDER(i).getORC();
			 //检验申请单号（:医嘱序号）
			 String ordsn = orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
			 //获取医嘱号
			 if(ordsn!=null || !"".equals(ordsn)){
				 String[] split = ordsn.split("H");
				 ordsnSet.add(split[1]);
			 }else{
				 log.info("检查执行确认更新状态失败；申请单号为空！\r\n"+hapiMsg.toString());
				 throw new BusException("检查执行确认更新状态失败；申请单号为空！");
			 }
			//日期
			String dateOcc = orc.getOrc9_DateTimeOfTransaction().getTs1_TimeOfAnEvent().getValue();
			paramMap.put("dateOcc", dateOcc);
			//获取操作医生工号 ORC-10
			String codeEmp = orc.getEnteredBy(0).getIDNumber().getValue();
			paramMap.put("codeEmp", codeEmp);
			//获取部门号 ORC17
			String codeDept = orc.getEnteringOrganization().getIdentifier().getValue();
			paramMap.put("codeDept", codeDept);
		}
		//查询执行人数据
		Map<String, Object> queryEmpByCode = sDQueryUtils.queryEmpByCode(SDMsgUtils.getPropValueStr(paramMap, "codeEmp"));
		if(queryEmpByCode != null && queryEmpByCode.size()>0){
			paramMap.putAll(queryEmpByCode);
		}else{
			log.info("检查计费未找到计费人信息！"+hapiMsg.toString());
			paramMap.put("pkEmp", paramMap.get("sysName"));
			paramMap.put("nameEmp", paramMap.get("sysName"));
			 //throw new BusException("检查计费未找到计费人信息");
		}
		//查询科室相关信息
		Map<String, Object> queryDeptByCode = sDQueryUtils.queryDeptByCode(SDMsgUtils.getPropValueStr(paramMap, "codeDept"));
		if(queryDeptByCode != null && queryDeptByCode.size()>0){
			paramMap.putAll(queryDeptByCode);
		}else{
			log.info("检查计费部门信息为空！"+hapiMsg.toString());
			paramMap.put("pkDept", paramMap.get("sysName"));
			paramMap.put("nameDept", paramMap.get("sysName"));
			//throw new BusException("消息中未传入计费部门信息");
		}
		List<CnRisApply> risApply = sDReceiveMapper.queryRisApply(ordsnSet);
		for(CnRisApply cnRisApply : risApply){
			//查询医辅执行表数据
			ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from EX_ASSIST_OCC where PK_CNORD=?",ExAssistOcc.class, cnRisApply.getPkCnord());
			if(exAssistOcc!=null){
				try{
					exAssistOcc.setDateOcc(sdf.parse(SDMsgUtils.getPropValueStr(paramMap, "dateOcc")));
				}catch(Exception e){
					exAssistOcc.setDateOcc(new Date());
					log.info("检查登记，执行时间错误！");
				}
				exAssistOcc.setPkOrgOcc(cnRisApply.getPkOrg());
				exAssistOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(paramMap, "pkEmp"));
				exAssistOcc.setNameEmpOcc(SDMsgUtils.getPropValueStr(paramMap, "nameEmp"));
				exAssistOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(paramMap, "pkDept"));
				exAssistOcc.setEuStatus("1");
				exAssistOcc.setFlagOcc("1");
			}
			//更新数据
			cnRisApply.setEuStatus("3");
			sDMsgDataUpdate.updateRisApply(cnRisApply,exAssistOcc);
		}
	}
	
	/**
	 * 检查消息计费服务（暂不启用）
	 * @param hapiMsg
	 */
	private void cgORGO20(Message hapiMsg){
		 ORG_O20 org = (ORG_O20)hapiMsg;
		 Map<String,Object> userMap = new HashMap<String,Object>();
		 //发送系统信息
		 userMap.put("sysName", org.getMSH().getSendingApplication().getNamespaceID().getValue());
		 int len = org.getRESPONSE().getORDERReps();
		 String ordsns = "";
		 for(int i=0;i<len;i++){
			 ORC orc = org.getRESPONSE().getORDER(i).getORC();
			 //检验申请单号（lb:医嘱序号）
			 String codeApply = orc.getPlacerOrderNumber().getEntityIdentifier().getValue();
			 String ordsn = "";
			 //获取操作医生工号 ORC-10
			 String codeEmp = orc.getEnteredBy(0).getIDNumber().getValue();
			 userMap.put("codeEmp", codeEmp);
			 //获取部门号 ORC17
			 String codeDept = orc.getEnteringOrganization().getIdentifier().getValue();
			 userMap.put("codeDept", codeDept);
			 //获取医嘱号
			 if(codeApply!=null || !"".equals(codeApply)){
				 String[] split = codeApply.split("H");
				 codeApply = split[0];
				 ordsn = split[1];
			 }else{
				 log.info("检查执行确认计费失败；申请单号为空！\r\n"+hapiMsg.toString());
				 throw new BusException("检查执行确认计费失败；申请单号为空！");
			 }
			 if(i == len-1){
				 ordsns +="'"+ ordsn +"'";
			   }else{
				   ordsns +="'"+ ordsn +"',";
			}
		 }
		//查询执行人数据
		 Map<String, Object> queryEmpByCode = sDQueryUtils.queryEmpByCode(SDMsgUtils.getPropValueStr(userMap, "codeEmp"));
		if(queryEmpByCode != null && queryEmpByCode.size()>0){
			userMap.putAll(queryEmpByCode);
		}else{
			log.info("检查计费未找到计费人信息！"+hapiMsg.toString());
			 userMap.put("pkEmp", userMap.get("sysName"));
			 userMap.put("nameEmp", userMap.get("sysName"));
			 //throw new BusException("检查计费未找到计费人信息");
		}
		//查询科室相关信息
		Map<String, Object> queryDeptByCode = sDQueryUtils.queryDeptByCode(SDMsgUtils.getPropValueStr(userMap, "codeDept"));
		if(queryDeptByCode != null && queryDeptByCode.size()>0){
			userMap.putAll(queryDeptByCode);
		}else{
			log.info("检查计费部门信息为空！"+hapiMsg.toString());
			 userMap.put("pkDept", userMap.get("sysName"));
			 userMap.put("nameDept", userMap.get("sysName"));
			 //throw new BusException("消息中未传入计费部门信息");
		}
		//执行单数据
		String sql = "select * from ex_order_occ ex left join cn_order o on ex.pk_cnord=o.pk_cnord where ex.eu_status='0' and o.ordsn in ("+ordsns+")";
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql);
		//判断是否需要计费
		if(list!=null && list.size()>0){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("dtlist", list);
			sDMsgDataUpdate.updateRisEx(paramMap,userMap);
			//sDMsgDataUpdate.updateRisCharge(paramMap);
		}else{
			//写日志
			log.info("计费失败：未找到需要计费的项目或该项目已经计费！\r\n" + hapiMsg.toString());
			throw new BusException("计费失败：未找到需要计费的项目或该项目已经计费！");
		}
	}

	/**
	 * ORLO22  LIS检验消息
	 * CM:打印条码
	 * F:采血扫码
	 * A：签收
	 * CA：撤销
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException 
	 */
	public void receiveORLO22(Message hapiMsg) throws HL7Exception, ParseException{
		 ORL_O22 org = (ORL_O22)hapiMsg;
		 String meString = org.getMSH().getMessageControlID().getValue();//MSH-10
		 Map<String,Object> queryMap = new HashMap<String,Object>();
		 int len=org.getRESPONSE().getPATIENT().getGENERAL_ORDERReps();
		 String send = org.getMSH().getSendingApplication().getUniversalID().getValue();//MSH-3
		 queryMap.put("send", send);
		 String pkEmp = "";
		 String nameEmp = "";
		 for(int i=0;i<len;i++){
			 int len2=org.getRESPONSE().getPATIENT().getGENERAL_ORDER(i).getORDERReps();
			 for(int j=0;j<len2;j++){
				 ORC  orc = org.getRESPONSE().getPATIENT().getGENERAL_ORDER(i).getORDER(j).getORC();
				 String orderId=orc.getPlacerOrderNumber().getEntityIdentifier().getValue(); //检验申请单号 orc-2.1
				 String codeEmp=orc.getEnteredBy(0).getIDNumber().getValue();//获取操作医生工号  orc-10
				 String ordControl = orc.getOrderStatus().getValue();  //orc-5
				 String codeCg = orc.getEnteringOrganization().getIdentifier().getValue(); //orc-17
				 String date = orc.getOrc15_OrderEffectiveDateTime().getTs1_TimeOfAnEvent().getValue();
				 if(StringUtils.isNotBlank(codeEmp)){//根据lis返回操作员工号查询操作员信息
					 Map<String, Object> bdOuMap = DataBaseHelper.queryForMap("SELECT name_emp,PK_EMP FROM bd_ou_employee WHERE CODE_EMP= ?",codeEmp);
					 if(bdOuMap!=null){
						 pkEmp=SDMsgUtils.getPropValueStr(bdOuMap,"pkEmp");
						 nameEmp=SDMsgUtils.getPropValueStr(bdOuMap,"nameEmp");
					 }
				 }
				List<Map<String,Object>> orderList= null;
				if(StringUtils.isNotBlank(orderId)){
					 queryMap.put("codeApply", orderId);
					 queryMap.put("codeOrdtype", "03%");
					 orderList = sDReceiveMapper.queryReqOrdList(queryMap);
				}
				if(orderList!=null&&orderList.size()>0){
					for(Map<String,Object> orderMap :orderList){
						 if(StringUtils.isNotBlank(pkEmp)){
							 orderMap.put("pkempqry",pkEmp);//操作医生主键
							 orderMap.put("nameemp",nameEmp);//操作医生主键
						 }
						 if(StringUtils.isNotBlank(codeCg)){
							 orderMap.put("codedeptcg", codeCg);//计费科室
						 }
						 orderMap.put("ordcontrol", ordControl);
						 orderMap.put("date", date);
						 sDMsgDataUpdate.updateLisRptList(orderMap);
					}
				}else {
					log.info("消息编码为"+meString+"ORLO22无申请单号，请传申请单号");
					throw new BusException("消息编码为"+meString+"无申请单号");
				}
			 }
		 }
	}

	/**
	 * 科室字典   人员字典  人员简历字典  物资字典
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public void  receiveMFNM01(Message hapiMsg) throws HL7Exception, ParseException {
		MFN_M01 mfn = (MFN_M01)hapiMsg;
		MFI mfi = mfn.getMFI();
		MFE mfe;
		String mfnType =mfi.getMasterFileIdentifier().getIdentifier().getValue();
		int len = mfn.getMFReps();
		if ("Department".equals(mfnType)) {
			//保存科室
			sDBaseDataService.saveDept(mfn);

		}else if ("Employee".equals(mfnType)) {
			//保存人员
			sDBaseDataService.saveEmployee(mfn);

		}else if ("StaffProfile".equals(mfnType)) {
			//个人简历
			sDBaseDataService.saveStaffProfile(mfn);

		}else if ("mate".equals(mfnType)) {
			//保存材料
			sDBaseDataService.saveMate(mfn);

		}


	}

	/**
	 * 住院用血计费  包药袋计费  对应的医嘱执行
	 * @param hapiMsg
	 */
	public void receiveRASO17(Message hapiMsg) {
		RAS_O17 ras = (RAS_O17)hapiMsg;
		ApplicationUtils apputil = new ApplicationUtils();
		 //String meString = ras.getMSH().getMessageControlID().getValue();
		 String sendApp =ras.getMSH().getSendingApplication().toString();
		 String sendAppStr = "";
		 if(sendApp.contains("TMIS")){
			 sendAppStr = "91";
		 }else if(sendApp.contains("APPS")){
			 sendAppStr = "92";
		 }
		 List<BlPubParamVo> blCgVos = new ArrayList<>();
		 List<RefundVo> reFund = new ArrayList<RefundVo>();//退费集合
		 Double charge = 0.0;
		 int len=ras.getORDERReps();
		 //String pvCode = ras.getPATIENT().getPATIENT_VISIT().getPV1().getVisitNumber().getIdentifierTypeCode().getValue();
		 String pvCode = ras.getPATIENT().getPATIENT_VISIT().getPV1().getVisitNumber().getID().getValue();
		 for(int i=0;i<len;i++){
			 ORC orc=ras.getORDER(i).getORC();
			 RXO rxo = ras.getORDER(i).getORDER_DETAIL().getRXO();
			 RXA rxa = ras.getORDER(i).getRXA();
			 //NTE nte = ras.getORDER(i).getORDER_DETAIL().getORDER_DETAIL_SUPPLEMENT().getNTE();
			 //orc
			 String control=orc.getOrderControl().getValue();//NW新增   ORC-1
			 String detailID = orc.getPlacerOrderNumber().getEntityIdentifier().getValue();//细目编号  ORC-2
			 //String ordID=orc.getPlacerGroupNumber().getEntityIdentifier().getValue();//医嘱号 ORC-4
			 //String sysTime = orc.getDateTimeOfTransaction().getTimeOfAnEvent().getValue();//系统时间 orc-9
			 String operation = orc.getEnteredBy(0).getIDNumber().getValue();//操作员 orc-10
			 //String nurse = orc.getVerifiedBy(0).getIDNumber().getValue();	//护士 orc-11
			 String doctor = orc.getOrderingProvider(0).getIDNumber().getValue();	//医生 orc-12
			 //String ordStartDate = orc.getOrderEffectiveDateTime().getTimeOfAnEvent().getValue();//医嘱开始时间  orc-15
			 //String causeCancel = orc.getOrderControlCodeReason().getText().getValue();//取消医嘱原因  orc-16
			 //String inDeptCode = orc.getEnteringOrganization().getIdentifier().getValue();//入录科室编码  orc-17
			 String oldDetailID = orc.getAdvancedBeneficiaryNoticeCode().getAlternateIdentifier().getValue();//停止医嘱退药的时候  记录对应所退项目的原计费细目编号 orc-20.4
			 //String oldDetailID = orc.getAdvancedBeneficiaryNoticeCode().getIdentifier().getValue();

			 //String applyDept = orc.getOrderingFacilityName(0).getOrganizationName().getValue();//orc-21 申请科室
			 //rxo
			 //String ordCode = rxo.getRequestedGiveCode().getIdentifier().getValue();//rxo-1.1 医嘱编码
			 String blItemCode = rxo.getRequestedGiveCode().getText().getValue();//rxo-1.2 收费项目编码
			 String ordName = rxo.getRequestedGiveCode().getNameOfCodingSystem().getValue();//rxo-1.3 医嘱名称  医嘱分类(n：非药品，d：药品)：bl_ip_dt.flag_pd(0：非药品，1：药品)
			 String numStr = rxo.getRequestedGiveAmountMinimum().getValue();//rxo-2 数量
			 //String units = rxo.getRequestedGiveUnits().getIdentifier().getValue();//rxo-4 数量单位
			 //net
			 //String priceStr = nte.getComment(0).getValue();//单价
			 //RXA
			 //String startTime = rxa.getDateTimeStartOfAdministration().getTimeOfAnEvent().getValue();//医嘱开始时间
			 //String endTime = rxa.getDateTimeEndOfAdministration().getTimeOfAnEvent().getValue();//医嘱结束时间
			 //String lenFlag = rxa.getAdministeredCode().getIdentifier().getValue();//长短期标志
			 //String ordClass = rxa.getAdministeredCode().getText().getValue();//医嘱类别
			 //String costFlag = rxa.getAdministeredCode().getNameOfCodingSystem().getValue();//费用标志
			 String exeDept = rxa.getAdministeredAtLocation().getPointOfCare().getValue();//执行科室
			 //String status = rxa.getCompletionStatus().getValue();//rxa-20  完成情况
			 //String actionCode = rxa.getActionCodeRXA().getValue();// rxa-21 行动代码
			 String exeTime = rxa.getSystemEntryDateTime().getTimeOfAnEvent().getValue();//rxa-22 执行时间

			 if(!"".equals(numStr)){
				 charge = Double.valueOf(numStr);
			 }
			 if("NW".equals(control)){
				 if(0<charge){//计费
					 List<Map<String, Object>> bdItem = new ArrayList<Map<String, Object>>();
					 Map<String, Object> patiInfo = new HashMap<>();

					 Map<String, Object> appDoc = new HashMap<>();
					 Map<String, Object> exDoc = new HashMap<>();
					 Map<String, Object> cgDoc = new HashMap<>();
					 if( detailID== null || "".equals(detailID)){
						 log.info("细目编号（ORC-2）未传入");
						 throw new BusException("细目编号（ORC-2）未传入");
					 }else{
						 Map<String, Object> BlIpDtMap = DataBaseHelper.queryForMap("select * from bl_ip_dt where pk_ordexdt = '"+detailID+"' and del_flag = '0' ");
						 if(BlIpDtMap != null){
							 log.info("该项目已计费");
							 throw new BusException("该项目已计费");
						 }
					 }

//					 BlIpDt blIpDt =new BlIpDt();
					 if(StringUtils.isNotBlank(pvCode)){
						 String sql ="select * from pv_encounter where code_pv = ? and del_flag = '0'";
						 //String sql ="select pv.*,pi.code_ip from pv_encounter pv left join pi_master pi on pi.pk_pi = pv.pk_pi  where pi.code_ip = ? and pv.del_flag = '0' and pi.del_flag = '0'";
						 patiInfo = DataBaseHelper.queryForMap(sql,pvCode);
						 if(patiInfo == null){
							 log.info("未查询到该患者信息");
							 throw new BusException("未查询到该患者信息");
						 }
					 }
					 if(StringUtils.isNotBlank(blItemCode)){
					 	//药品
						 if ("d".equals(ordName)) {
							//bdItem = DataBaseHelper.queryForMap("select * from bd_pd where code = ? and del_flag ='0'",blItemCode);
							 bdItem = DataBaseHelper.queryForList("select bd.*,st.batch_no from bd_pd bd left join pd_inv_init st on st.pk_pd = bd.pk_pd  where bd.code = ? and st.del_flag ='0' and bd.del_flag ='0'",blItemCode);
						 }else{
						 	//非药品
							//bdItem = DataBaseHelper.queryForList("select pk_item from bd_ord_item where pk_ord = (select pk_ord from bd_ord where code = ? and del_flag = '0') and del_flag = '0'",blItemCode);
							 bdItem = DataBaseHelper.queryForList("select pk_ord from bd_ord where code = ? and del_flag = '0'",blItemCode);
						 }
						 if(bdItem.size()<1){
							 log.info("未查询到项目编码主建");
							 throw new BusException("未查询到项目编码主建");
						 }
					 }
					 if(StringUtils.isNotBlank(doctor)){
						 String sql = "SELECT emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.code_emp =?";
						 appDoc = DataBaseHelper.queryForMap(sql,doctor);
					 }

					 //执行科室
					 if(StringUtils.isNotBlank(exeDept)){
						 String sql = "select pk_dept from bd_ou_dept where code_dept =?";
						 exDoc = DataBaseHelper.queryForMap(sql,exeDept);
					 }
					 if(StringUtils.isNotBlank(operation)){
						 String sql = "SELECT emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.code_emp =?";
						 cgDoc= DataBaseHelper.queryForMap(sql,operation);
					 }
					 BlPubParamVo vo = null;
					 for (int j = 0; j < bdItem.size(); j++) {
					 	// blIpDt.setQuan(charge);//数量
						vo = new BlPubParamVo();
						vo.setPkOrg(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
						vo.setPkOrgApp(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
						vo.setPkOrgEx(SDMsgUtils.getPropValueStr(patiInfo,"pkOrg"));
						vo.setPkPi(SDMsgUtils.getPropValueStr(patiInfo,"pkPi"));
						vo.setPkPv(SDMsgUtils.getPropValueStr(patiInfo,"pkPv"));
						vo.setEuPvType(SDMsgUtils.getPropValueStr(patiInfo,"euPvtype"));
						vo.setPkDeptApp(SDMsgUtils.getPropValueStr(patiInfo,"pkDept"));
						vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(patiInfo,"pkDeptNs"));//开立科室
						vo.setPkEmpApp(SDMsgUtils.getPropValueStr(appDoc,"pkEmp"));
						vo.setNameEmpApp(SDMsgUtils.getPropValueStr(appDoc,"nameEmp"));//申请人
						vo.setPkDeptEx(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));//执行科室
						vo.setDateHap(DateUtils.strToDate(exeTime, "yyyyMMddHHmmss"));
						vo.setPkDeptCg(SDMsgUtils.getPropValueStr(cgDoc,"pkDept"));//计费科室
						vo.setPkEmpCg(SDMsgUtils.getPropValueStr(cgDoc,"pkEmp"));
						//vo.setNameEmpCg(SDMsgUtils.getPropValueStr(cgDoc,"nameEmp"));
						vo.setNameEmpCg("平台接口计费");
						vo.setPkOrdexdt(detailID);//细目编号  唯一号
						vo.setEuBltype(sendAppStr);//区分是包药机还是输血
						if("d".equals(ordName)){
							vo.setFlagPd("1");//1为药品
							//vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkPd"));
							vo.setPkOrd(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkPd"));
							vo.setSpec(SDMsgUtils.getPropValueStr(bdItem.get(j),"spec"));
							vo.setBatchNo(SDMsgUtils.getPropValueStr(bdItem.get(j),"batchNo"));
							//vo.setDateExpire(DateUtils.strToDate(SDMsgUtils.getPropValueStr(bdItem.get(j),"spec"), "yyyyMMddHHmmss"));
							vo.setPkUnitPd(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkUnitPack"));
							vo.setPackSize(Integer.parseInt(SDMsgUtils.getPropValueStr(bdItem.get(j),"packSize")));
							vo.setPrice(Double.parseDouble((SDMsgUtils.getPropValueStr(bdItem.get(j),"price"))));
							vo.setPriceCost(Double.parseDouble((SDMsgUtils.getPropValueStr(bdItem.get(j),"price"))));
						}else {
							vo.setFlagPd("0");//0为非药品
							//vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkItem"));
							vo.setPkOrd(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkOrd"));
						}
						vo.setQuanCg(charge);
						blCgVos.add(vo);
					}
				 	}else{//退费
						Map<String, Object> cgDoc = new HashMap<>();
						List<Map<String, Object>> pkMap = new ArrayList<Map<String, Object>>();
						if(StringUtils.isNotBlank(operation)){
							String sql = "SELECT emp.pk_org,emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.code_emp = ?  ";
							cgDoc= DataBaseHelper.queryForMap(sql,operation);
						}
						if(StringUtils.isNotBlank(oldDetailID)){
							pkMap =DataBaseHelper.queryForList("select pk_cgip from  BL_IP_DT where flag_settle='0' and  pk_ordexdt = '"+oldDetailID+"' and del_flag = '0' ");
							if(pkMap.size()<1){
								log.info("未查询到项目编码主建");
								throw new BusException("未查询到项目编码主建");
							}
							for (int j = 0; j < pkMap.size(); j++) {
								List<Map<String, Object>> pkCgIpBackMap =DataBaseHelper.queryForList("select pk_cgip from  BL_IP_DT where flag_settle='0' and  pk_cgip_back = '"+SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCgip")+"' and del_flag = '0' ");
								if(pkCgIpBackMap.size()>0){
									log.info("该项目编码已退费");
									throw new BusException("该项目编码已退费");
								}
							}

						}
						RefundVo refundVo = null;
						for (int j = 0; j < pkMap.size(); j++) {
							refundVo = new RefundVo();
							refundVo.setPkOrg(SDMsgUtils.getPropValueStr(cgDoc,"pkOrg"));
							refundVo.setPkCgip(SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCgip"));//计费主建
							//refundVo.setNameEmp(SDMsgUtils.getPropValueStr(cgDoc,"nameEmp"));
							refundVo.setNameEmp("平台接口退费");
							refundVo.setPkEmp(SDMsgUtils.getPropValueStr(cgDoc,"pkEmp"));
							refundVo.setPkDept(SDMsgUtils.getPropValueStr(cgDoc,"pkDept"));
							refundVo.setQuanRe(MathUtils.mul(charge, -1.0));
							reFund.add(refundVo);
						}
				 	}
				 }else{
					 log.info("请检查ORC-1是否为NW");
					 throw new BusException("请检查ORC-1是否为NW");
				 }

		 }
		//组装用户信息
		 User u  = new User();
		 if(0<charge){//计费
			u.setPkOrg(blCgVos.get(0).getPkOrg());
			u.setPkEmp(blCgVos.get(0).getPkEmpEx());
			//u.setNameEmp(blCgVos.get(0).getNameEmpEx());
			u.setNameEmp("平台接口计费");
			u.setPkDept(blCgVos.get(0).getPkDeptEx());
			UserContext.setUser(u);
			//组装记费参数
			BlIpCgVo cgVo = new BlIpCgVo();
			cgVo.setAllowQF(true);
			cgVo.setBlCgPubParamVos(blCgVos);
			ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,u);
			if(rs.getStatus()<0){
				 log.info(rs.getDesc());
				throw new BusException(rs.getDesc());
			}

		 }else{
			u.setPkOrg(reFund.get(0).getPkOrg());
			u.setPkEmp(reFund.get(0).getPkEmp());
			//u.setNameEmp(reFund.get(0).getNameEmp());
			u.setNameEmp("平台接口退费");
			u.setPkDept(reFund.get(0).getPkDept());
			UserContext.setUser(u);
			//退费
			if(reFund!=null && reFund.size()>0){
				ResponseJson prePayInfo = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",reFund,u);
				if(prePayInfo.getStatus()<0){
					log.info(prePayInfo.getDesc());
					throw new BusException(prePayInfo.getDesc());
				}
			}

		 }


	}


	/**
	 * 自定义医嘱执行
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public boolean receiveZASO17(Message hapiMsg) throws HL7Exception, ParseException {
		boolean rs = false;
		//获取执行人相关数据 //组装用户信息
		Map<String, Object> userMap = new HashMap<String,Object>();
		userMap.put("pkOrg", Constant.PKORG);
		userMap.put("pkEmp", "~");
		userMap.put("pkDept", "~");
		String msg = hapiMsg.encode();
		//判断是否有RXA消息段
		if(msg.indexOf("RXA|")!=-1){
			Segment rxa = (Segment) hapiMsg.get("RXA");
			//获取用户数据
			String user = rxa.getField(10, 0).encode();
			//判断数据格式是否正确；
			if(user !=null && user.contains("^")){
				String[] codeEmp = user.split("\\^");//0是编码，1是名字
				Map<String, Object> empMap = sDQueryUtils.queryEmpByCode(codeEmp[0]);
				userMap.put("codeEmp", codeEmp[0]);
				userMap.put("nameEmp", codeEmp[1]);
				if(empMap!=null){
					userMap.putAll(empMap);
				}
			}else{
				userMap.put("nameEmp", user);
				log.info("自定义医嘱执行：传入数据执行人数据格式有误或数据为空!"+hapiMsg.toString());
				//throw new BusException("自定义医嘱执行：传入数据执行人数据格式有误或数据为空!");
			}
			//获取执行科室数据
			String dept = rxa.getField(11, 0).encode();
			if(dept !=null && !"".equals(dept)){
				Map<String, Object> deptMap = sDQueryUtils.queryDeptByCode(dept);
				if(deptMap != null){
					userMap.putAll(deptMap);
				}else{
					userMap.put("codeDept", dept);
					userMap.put("nameDept", "~");
					log.info("自定义医嘱执行：未找到相关执行科室!"+hapiMsg.toString());
					//throw new BusException("自定义医嘱执行：传入数据执行人数据格式有误或数据为空!");
				}
			}else{
				log.info("自定义医嘱执行：未找到相关执行科室!"+hapiMsg.toString());
				//throw new BusException("自定义医嘱执行：传入数据执行人数据格式有误或数据为空!");
			}
			//获取时间 执行时间
			userMap.put("date", rxa.getField(22, 0).encode());
		}
		//通过执行单号查询所需数据
		Segment orc = (Segment) hapiMsg.get("ORC");
		String pkExocc = orc.getField(2, 0).encode();
		//组装皮试结果数据
		Map<String,Object> stMap=new HashMap<String,Object>();
		//判断是否有rxo段
		if(msg.indexOf("RXO|")!=-1){
			Segment rxo = (Segment) hapiMsg.get("RXO");
			String stResStr=rxo.getField(20, 0).encode();
			String[] stRes=stResStr.split("\\^");
			if(stRes!=null && stRes.length>0){
				stMap.put("result", stRes[0]);
				stMap.put("batchNo", stRes.length>1?stRes[1]:"");
				//stMap.put("dateStart", orc.getField(15,0).encode());
			}
		}
		//判断是否有rxr段，获取用法数据
		if(msg.indexOf("RXR|")!=-1){
			Segment rxr=(Segment) hapiMsg.get("RXR");
			String codeSupply=rxr.getField(1,0).encode();
			stMap.put("codeSupply", codeSupply);
		}

		List<Map<String, Object>> exList = new ArrayList<Map<String,Object>>();
		if(pkExocc != null && !"".equals(pkExocc)){
			String sql = "select * from ex_order_occ ex left join cn_order o on ex.pk_cnord=o.pk_cnord where ex.eu_status='0' and ex.pk_exocc=?";
			exList = DataBaseHelper.queryForList(sql,pkExocc);
		}else{
			log.info("自定义医嘱执行失败：该项目已执行记费或未生成执行单数据!"+hapiMsg.toString());
			throw new BusException("自定义医嘱执行失败：该项目已执行计费或未生成执行单数据!");
		}
		//计费并执行
		if(exList!=null && exList.size()>0){
			sDMsgDataUpdate.saveExBlOrd(exList,userMap,stMap);
		}else {
			log.info("自定义医嘱执行失败：未找到对应执行单记录或该项目已被执行记费!"+hapiMsg.toString());
			throw new BusException("自定义医嘱执行失败：未找到对应执行单记录或该项目已被执行记费!");
		}

		return rs;
	}
	
	/**
	 * 毒麻药柜计费录入接口消息
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public void receiveQRYP04(Message hapiMsg) throws HL7Exception, ParseException {
		Map<String, Object> userMap = new HashMap<String,Object>();
		//药品或项目列表
		List<Map<String, Object>> lstMapItem = new ArrayList<Map<String, Object>>();
		//userMap.put("pkOrg", Constant.PKORG);
		//userMap.put("pkEmp", "~");
		//userMap.put("pkDept", "~");
		String msg = hapiMsg.encode();
		Segment mshSeg = (Segment) hapiMsg.get("MSH");
		MSH msh=(MSH)mshSeg;
		String msgId = msh.getMessageControlID().getValue();
		//解析QRD段
		Segment qrd = (Segment)hapiMsg.get("QRD");
		//手术唯一ID
		String queryId = qrd.getField(4, 0).encode();
		userMap.put("opId", queryId);
		//患者信息
		String patientInfo = qrd.getField(8, 0).encode();//患者ID^住院号^就诊流水号
		if(patientInfo !=null){
			String[] patientInfoArray = patientInfo.split("\\^");
			if(patientInfoArray.length > 0)
			{
				userMap.put("patientId", patientInfoArray[0]);				
			}
			if(patientInfoArray.length > 1)
			{
				userMap.put("inPatientNo", patientInfoArray[1]);		
			}
			if(patientInfoArray.length > 2)
			{
				userMap.put("codePv", patientInfoArray[2]);		
			}			
		}
		//手术信息
		String opInfo = qrd.getField(10, 0).encode();//手术编码^手术名称
		if(opInfo !=null){
			String[] opInfoArray = opInfo.split("\\^");
			if(opInfoArray.length > 0)
			{
				userMap.put("opCode", opInfoArray[0]);				
			}
			if(opInfoArray.length > 1)
			{
				userMap.put("opName", opInfoArray[1]);
			}		
		}
		//患者姓名
		String patientName = qrd.getField(11, 0).encode();
		userMap.put("namePi", patientName);	
		//解析RXO段，可能有多个
		String splitChar = "" + (char)0x0d;
		String[] msgArray = msg.split(splitChar);
		for(int i=0; i<msgArray.length; i++)
	    {
		    String msgSeg = msgArray[i];
		    if(msgSeg.indexOf("RXO") == 0)
		    {
		    	Map<String, Object> itemMap = new HashMap<String,Object>();
			    String[] msgArray2 = msgSeg.split("\\|");
			    //计价项目编码^名称
			    if(msgArray2.length > 1)
			    {
			    	String itemInfo = msgArray2[1];
			    	if(itemInfo !=null){
						String[] itemInfoArray = itemInfo.split("\\^");
						if(itemInfoArray.length > 0)
						{
							itemMap.put("itemCode", itemInfoArray[0]);				
						}
						if(itemInfoArray.length > 1)
						{
							itemMap.put("itemName", itemInfoArray[1]);
						}		
					}
			    }
			    //数量
			    if(msgArray2.length > 2)
			    {
			    	String quan = msgArray2[2];
			    	itemMap.put("quan", quan);
			    }
			    //单位
			    if(msgArray2.length > 4)
			    {
			    	String quanUnit = msgArray2[4];
			    	itemMap.put("quanUnit", quanUnit);
			    }
			    lstMapItem.add(itemMap);
		    }
	    }
		userMap.put("itemArray", lstMapItem);
		//校验就诊数据		
		String sqlPv = "select * from pv_encounter where code_pv=?";
		PvEncounter pvEncounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{userMap.get("codePv")});
		if(pvEncounter==null)
		{
			throw new HL7Exception("根据就诊流水号[" + userMap.get("codePv") + "]在pv_encounter表未查询到就诊数据");
		}
		String pkPv = pvEncounter.getPkPv();
		//校验手术信息		
		String sqlOp = "select pk_cnord from cn_order where CODE_APPLY = ? and code_ordtype = '04' and pk_pv = ?";
		Map<String,Object> mapOrder = DataBaseHelper.queryForMap(sqlOp, new Object[]{userMap.get("opId"), pkPv});
		if(mapOrder == null)
		{
			throw new HL7Exception("根据手术申请单号[" + userMap.get("opId") + "]在cn_order表未查询到数据");
		}
		String pkCnord = SDMsgUtils.getPropValueStr(mapOrder, "pkCnord");
		List<MidBlOp> addList=new ArrayList<MidBlOp>();
		Date curDate = new Date();
		for(Map<String, Object> item : lstMapItem)
		{
			MidBlOp midBlOp = new MidBlOp();
			midBlOp.setPkMidBlOp(SDMsgUtils.getPk());
			midBlOp.setPkPv(pkPv);
			midBlOp.setCodeApply((String)userMap.get("opId"));
			midBlOp.setPkCnord(pkCnord);
			midBlOp.setCodeItem((String)item.get("itemCode"));
			midBlOp.setNameItem((String)item.get("itemName"));
			midBlOp.setQuan(Double.valueOf((String)item.get("quan")));
			midBlOp.setQuanUnit((String)item.get("quanUnit"));
			midBlOp.setFlagItem("1");//药品
			midBlOp.setMsgId(msgId);
			midBlOp.setDateAppt(curDate);
			midBlOp.setFeeFlag("0");//未计费
			midBlOp.setDelFlag("0");//未删除
			addList.add(midBlOp);
		}
		if(addList.size() > 0)
		{			
			sDMsgDataUpdate.saveQRYP04(addList);
		}		
	}

	/**
	 * 接收检查报告信息
	 * @param hapiMsg
	 * @throws HL7Exception
	 */
	public void receiveORUR01(Message hapiMsg) throws HL7Exception{

		 ORU_R01 oru=(ORU_R01)hapiMsg;
		 MSH msh=oru.getMSH();
		 List<ExRisOcc> list=new ArrayList<ExRisOcc>();
		 //int len = oru.getPATIENT_RESULT().getORDER_OBSERVATIONReps();
		 OBR obr;
		 OBX obx;
		 List<Map<String,Object>> patList = new ArrayList<>();
		 obr = oru.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBR();
		 String codeApply=obr.getPlacerOrderNumber().getEntityIdentifier().getValue();
		 String [] str = codeApply.split("#");
		 for (int j = 0; j < str.length; j++) {
			 String[] qry = str[j].split("H");
			 Map<String,Object> queryMap = DataBaseHelper.queryForMap(" select ord.*,to_char(ord.DATE_START,'YYYYMMDDHH24MISS') as DATE_START from cn_order ord  where ord.del_flag = '0' and  ord.code_apply = ?  and  ord.ordsn = ?", new Object[]{qry[0],qry[1]});
			 if(queryMap == null){
				 throw new BusException("未查询到该"+str[j]+"申请信息");
			 }
			 queryMap.put("applycode", str[j]);
			 patList.add(queryMap);
		 }

		 int applyLength = patList.size();
		 if(applyLength == 0){
			 throw new BusException("未查询到申请单信息，请核对");
		 }
		 for (int m = 0; m < applyLength; m++) {
			 ExRisOcc exRisOcc = new ExRisOcc();
			 Map<String,Object> patientMap = patList.get(m);
			 exRisOcc.setPkRisocc(SDMsgUtils.getPk());
			 exRisOcc.setPkOrg(SDMsgUtils.getPropValueStr(patientMap,"pkOrg"));
			 exRisOcc.setPkPi(SDMsgUtils.getPropValueStr(patientMap,"pkPi"));
			 exRisOcc.setPkPv(SDMsgUtils.getPropValueStr(patientMap,"pkPv"));
			 exRisOcc.setCodeApply(SDMsgUtils.getPropValueStr(patientMap,"codeApply"));
			 exRisOcc.setPkCnord(SDMsgUtils.getPropValueStr(patientMap,"pkCnord"));
			 exRisOcc.setNameOrd(SDMsgUtils.getPropValueStr(patientMap,"nameOrd"));
			 exRisOcc.setPkOrgOcc(SDMsgUtils.getPropValueStr(patientMap,"pkOrg"));//执行机构
			 exRisOcc.setPkDeptOcc(obr.getDiagnosticServSectID().getValue());//执行科室
			 //执行人  obr - 35 --待需求确认 --编码||名字
			 String codeEmp = obr.getObr35_Transcriptionist(0).getNdl1_OPName().getCnn1_IDNumber().getValue();
			 String nameEmp = 	obr.getObr35_Transcriptionist(0).getOPName().getCnn2_FamilyName().getValue();
			 nameEmp = nameEmp==null?codeEmp:nameEmp;
			 if(codeEmp!=null && codeEmp.contains("&")){
				 String[] split = codeEmp.split("&");
				 codeEmp = split[0];
				 nameEmp = split[1];
			 }
			 Map<String, Object> empOcc = sDQueryUtils.queryEmpByCode(codeEmp);
			 exRisOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(empOcc, "pkEmp"));//执行人pk
			 exRisOcc.setNameEmpOcc(nameEmp);

			 exRisOcc.setDateRpt(obr.getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValueAsDate());//检查报告时间 obr -22
			 exRisOcc.setDateOcc(obr.getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValueAsDate());//执行日期待需求确认
			 exRisOcc.setDateChk(obr.getResultsRptStatusChngDateTime().getTimeOfAnEvent().getValueAsDate());//审核日期待需求确认
			 exRisOcc.setFlagChk("1");
			 exRisOcc.setDelFlag("0");
			 //报告系统名称:检查项目编码:名称暂时记到备注里
			 exRisOcc.setNote(msh.getSendingApplication().getNamespaceID().getValue()+":"+obr.getUniversalServiceIdentifier().getIdentifier().getValue()+":"+obr.getUniversalServiceIdentifier().getText().getValue());
			 int lenObx=oru.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBSERVATIONReps();

			 obx = oru.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBSERVATION(0).getOBX();
			 //审核医生   obx-16
			 String codeEmpChk = obx.getResponsibleObserver().getIDNumber().getValue();
			 String nameEmpChk = obx.getResponsibleObserver().getFamilyName().getSurname().getValue();
			 Map<String, Object> empChk = sDQueryUtils.queryEmpByCode(codeEmpChk);
			 exRisOcc.setPkEmpChk(SDMsgUtils.getPropValueStr(empChk, "pkEmp"));
			 exRisOcc.setNameEmpChk(nameEmpChk);
			 for(int j=0;j<lenObx;j++){
				 obx = oru.getPATIENT_RESULT().getORDER_OBSERVATION(0).getOBSERVATION(j).getOBX();

				 log.info("obx:"+j+"-"+obx.getObservationIdentifier().getIdentifier().getValue()+"-"+obx.getObservationIdentifier().getText());
				 TX content=null;
				 if(obx.getObservationValue().length>0){
					 Varies v = obx.getObservationValue()[0];
					 try {
						 content =(TX)v.getData();
						 log.info(content.getValue());
					 } catch (Exception e) {
							e.printStackTrace();
							continue;
					 }
				 }
				 String reportType=obx.getObservationIdentifier().getText().getValue();
				 if(null !=content){
					 if(reportType.equals("检查所见")||reportType.equals("肉眼所见")){
						 exRisOcc.setResultObj(content.getValue().replace("\\X0D\\","\n").replace("\\X0A\\", "").replace("\\X09\\", "").replace("\\X0C\\", "").replace("\\X0B\\", ""));
					 }else if(reportType.equals("检查提示")||reportType.equals("诊断意见")){
						 exRisOcc.setResultSub(content.getValue().replace("\\X0D\\","\n").replace("\\X0A\\", "").replace("\\X09\\", "").replace("\\X0C\\", "").replace("\\X0B\\", ""));
					 }else if(reportType.equals("影像URL")){
						 exRisOcc.setAddrImg(content.getValue());

					 }
				 }
			 }
			 String send = msh.getSendingApplication().getNamespaceID().getValue();
			 if("LWRIS".equals(send)||"LWUS".equals(send)||"LWEIS".equals(send)||"LWPIMS".equals(send)){
					String pidId = oru.getPATIENT_RESULT().getPATIENT().getPID().getPatientID().getID().getValue();//患者ID
					exRisOcc.setAddrImg("http://10.0.3.51:82/xds/Index.php?appuser=1&healthcarefacility=XXX&type=HISCODE1&value="+pidId+"&type1=HISCODE2&value1="+SDMsgUtils.getPropValueStr(patientMap,"applycode"));
			 }
			 exRisOcc.setDateOcc(new Date());
			 list.add(exRisOcc);
		 }

		 if(list.size()>0){
			 sDMsgDataUpdate.saveRisRptList(codeApply,list);
		 }
	 }

	/**
	 *
	 * 发布检验结果（住院）
	 * @param hapiMsg
	 * @throws HL7Exception
	 * @throws ParseException
	 */
	public void receiveOULR21(Message hapiMsg) throws HL7Exception, ParseException{
		//接收检验结果信息
		 OUL_R21 oul_r21=(OUL_R21)hapiMsg;
		 Map<String, Object> pidMap = MsgParseUtils.getPID(oul_r21.getPATIENT().getPID());
		 //	住院号
		 if("".equals(SDMsgUtils.getPropValueStr(pidMap, "codePi"))){
			 log.info("codePi is null");
			 //throw new BusException("检验结果:传入的患者编号为空！");
		 }
		 //获取病人信息 codePi
//		 List<Map<String, Object>> queryPiByCodePi = sDQueryUtils.queryPiByCodePi(SDMsgUtils.getPropValueStr(pidMap, "codePi"));
//		 if(queryPiByCodePi==null || queryPiByCodePi.size()==0){
//			 log.info("没有查到该病人信息:"+SDMsgUtils.getPropValueStr(pidMap, "codePi"));
//		 }
		 //获取检验信息（多条执行单）
		 int size = oul_r21.getORDER_OBSERVATIONReps();
		 for(int i=0;i<size;i++){
			 //删除标志
			 String delStatus = "";
			 //申请单号 2.1
			 String codeApply = oul_r21.getORDER_OBSERVATION(i).getOBR().getPlacerOrderNumber().getEntityIdentifier().getValue();
			 Map<String, Object> obrMap = MsgParseUtils.getOBR(oul_r21.getORDER_OBSERVATION(i).getOBR());
			 //判断是否已经记费
			 String sqlChk = "select * from CN_ORDER ord inner join EX_ORDER_OCC occ on ord.PK_CNORD=occ.PK_CNORD where ord.CODE_APPLY=? and occ.eu_status='0' and ord.code_ordtype like '03%' and ord.eu_status_ord<4 and ord.DEL_FLAG=0 and occ.DEL_FLAG=0";
			 Map<String, Object> orderMap = DataBaseHelper.queryForMap(sqlChk, codeApply);
			 if(orderMap!=null && orderMap.size()>0){
				//记费
				//组装用户参数
				Map<String,Object> userMap = new HashMap<String,Object>();
				userMap.put("pkOrg", SDMsgUtils.getPropValueStr(orderMap,"pkOrg"));
				userMap.put("pkDept", SDMsgUtils.getPropValueStr(obrMap,"pkDeptNs"));
				userMap.put("pkEmp", SDMsgUtils.getPropValueStr(orderMap,"pkEmpEx"));
				userMap.put("nameEmp", "接收检验报告记费");
				userMap.put("date", SDMsgUtils.getPropValueStr(obrMap,"dateChk"));
				sDMsgDataUpdate.saveReportBl(orderMap,userMap);
			 }else{
				//查询医嘱信息
				 String sql = " select o.* from cn_order o where o.code_ordtype like '03%' and o.code_apply=?";
				 orderMap = DataBaseHelper.queryForMap(sql, codeApply);
			 }
			 //未找到对应检验医嘱
			 if(orderMap==null){
				 throw new BusException("未找到有效的检验医嘱,申请单号为："+codeApply);
			 }
			 //获取检验结果信息(多个检验结果)
			 List<ExLabOcc> exLabOccList = new ArrayList<ExLabOcc>();
			 //药敏结果
			 List<ExLabOccBact> exLabOccBactList = new ArrayList<ExLabOccBact>();
			 List<ExLabOccBactAl> exLabOccBactAlList = new ArrayList<ExLabOccBactAl>();
			 int len = oul_r21.getORDER_OBSERVATION(i).getOBSERVATIONReps();
			 for(int j=0;j<len;j++){
				 OBX obx = oul_r21.getORDER_OBSERVATION(i).getOBSERVATION(j).getOBX();
				 //box11
				 delStatus= obx.getObservationResultStatus().getValue();
				//如果是删除标志；直接删除所有报告信息；退出本次循环
				 if("D".equals(delStatus)){
					 sDMsgDataUpdate.delLisRptList(codeApply,SDMsgUtils.getPropValueStr(obrMap, "codeRpt"));
					 exLabOccList.clear();
					 exLabOccBactList.clear();
					 break;
				 }
				 Map<String, Object> obxMap = MsgParseUtils.getOBX(obx);
				 //普通检验
				 ExLabOcc exLabOcc = new ExLabOcc();
				 String pkLabocc = SDMsgUtils.getPk();
				 exLabOcc.setPkLabocc(pkLabocc);
				 exLabOcc.setPkOrg(SDMsgUtils.getPropValueStr(orderMap, "pkOrg"));
				 exLabOcc.setPkPi(SDMsgUtils.getPropValueStr(orderMap, "pkPi"));
				 exLabOcc.setPkPv(SDMsgUtils.getPropValueStr(orderMap,"pkPv"));
				 exLabOcc.setPkCnord(SDMsgUtils.getPropValueStr(orderMap,"pkCnord"));
				 exLabOcc.setCodeApply(SDMsgUtils.getPropValueStr(orderMap,"codeApply"));
				 exLabOcc.setPkOrgOcc(SDMsgUtils.getPropValueStr(orderMap,"pkOrgExec"));
				 exLabOcc.setPkDeptOcc(SDMsgUtils.getPropValueStr(orderMap,"pkDeptExec"));
				 exLabOcc.setPkOrd(SDMsgUtils.getPropValueStr(orderMap, "pkOrd"));
				 exLabOcc.setNameOrd(SDMsgUtils.getPropValueStr(orderMap,"nameOrd"));
				 //Date dateChk = sdf.parse(SDMsgUtils.getPropValueStr(obrMap, "dateChk"));//送检时间
				 if("MIC".equals(SDMsgUtils.getPropValueStr(obrMap, "rptType"))){
					 exLabOcc.setEuType("1");
				 }else{
					 exLabOcc.setEuType("0");
				 }
				 exLabOcc.setCodeRpt(SDMsgUtils.getPropValueStr(obrMap, "codeRpt"));//报告唯一号
				 exLabOcc.setCodeSamp(SDMsgUtils.getPropValueStr(obrMap, "codeApply"));//样本号
				 exLabOcc.setUnit(SDMsgUtils.getPropValueStr(obxMap, "unit"));
				 Date dateOcc = sdf.parse(SDMsgUtils.getPropValueStr(obxMap, "dateOcc"));//执行时间
				 exLabOcc.setDateOcc(dateOcc);
				 Date dateRpt = sdf.parse(SDMsgUtils.getPropValueStr(obrMap, "dateRpt"));//报告时间
				 exLabOcc.setDateRpt(dateRpt);
				 exLabOcc.setDateChk(dateRpt);//报告核查时间
				 exLabOcc.setPkEmpOcc(SDMsgUtils.getPropValueStr(obrMap, "pkEmpOcc"));//执行人
				 exLabOcc.setNameEmpOcc(SDMsgUtils.getPropValueStr(obrMap, "nameEmpOcc"));
				 exLabOcc.setFlagChk("1");
				 exLabOcc.setSortNo(Integer.parseInt(SDMsgUtils.getPropValueStr(obxMap, "id")));
				 exLabOcc.setPkEmpChk(SDMsgUtils.getPropValueStr(obrMap, "pkChk"));//审核人
				 exLabOcc.setNameEmpChk(SDMsgUtils.getPropValueStr(obrMap, "nameChk"));
				 exLabOcc.setCodeIndex(SDMsgUtils.getPropValueStr(obxMap, "codeIndex"));
				 exLabOcc.setNameIndex(SDMsgUtils.getPropValueStr(obxMap, "nameIndex"));
				 exLabOcc.setVal(SDMsgUtils.getPropValueStr(obxMap, "val"));
				 //参考范围
				 String references = SDMsgUtils.getPropValueStr(obxMap, "references");
				 if(references!=null && !"".equals(references)){
					 //判断是否数值范围 是则拆分
					 if(references.contains("-")){
						 String[] split = references.split("-");
						 exLabOcc.setValMin(split[0]);
						 exLabOcc.setValMax(split[1]);
					 }else{
						 //汉字参考范围
						 exLabOcc.setValMin(references);
					 }
				 }
				 //result.setEuResult(SDMsgUtils.getPropValueStr(obxMap, "euResult"));
				 exLabOcc.setEuResult(SDMsgUtils.getPropValueStr(obxMap, "euResult"));//检验结果
				 exLabOcc.setDelFlag("0");
				 exLabOccList.add(exLabOcc);
				 //如果是药敏
				 if("MIC".equals(SDMsgUtils.getPropValueStr(obrMap, "rptType"))){
					//药敏报告，写ex_lab_occ_bact和ex_lab_occ_bact_al
					 ExLabOccBact bact = new ExLabOccBact();  // 赋值待确定
					 String pkBact = SDMsgUtils.getPk();
					 bact.setPkBact(pkBact);
					 bact.setPkOrg(SDMsgUtils.getPropValueStr(orderMap, "pkOrg"));
					 bact.setPkLabocc(pkLabocc);
					 bact.setSortNo(Integer.parseInt(SDMsgUtils.getPropValueStr(obxMap, "id")));
					 //bact.setBacttype("");
					 bact.setNamePd(SDMsgUtils.getPropValueStr(obxMap, "nameIndex"));
					 //bact.setMic(mic);
					 bact.setValLab(SDMsgUtils.getPropValueStr(obxMap, "val"));//检验值
					 bact.setEuAllevel(SDMsgUtils.getPropValueStr(obxMap, "euResult"));
					 bact.setCodeBact(SDMsgUtils.getPropValueStr(obxMap, "codeBact"));
					 bact.setNameBact(SDMsgUtils.getPropValueStr(obxMap, "nameBact"));
					 //bact.setBactcol(Double.parseDouble(SDMsgUtils.getPropValueStr(obxMap, "bactcol")));
					 bact.setNote(oul_r21.getORDER_OBSERVATION(i).getNTE().getField(3, 0).encode());
					 bact.setDelFlag("0");
					 bact.setTs(new Date());
					 exLabOccBactList.add(bact);
					 ExLabOccBactAl bactAl = new ExLabOccBactAl();
					 bactAl.setPkBact(pkBact);
					 bactAl.setPkBactal(SDMsgUtils.getPk());
					 bactAl.setPkOrg(SDMsgUtils.getPropValueStr(orderMap, "pkOrg"));
					 bactAl.setSortNo(Integer.parseInt(SDMsgUtils.getPropValueStr(obxMap, "id")));
					 bactAl.setCodePd(SDMsgUtils.getPropValueStr(obxMap, "codeIndex"));
					 bactAl.setNamePd(SDMsgUtils.getPropValueStr(obxMap, "nameIndex"));
					 //bactAl.setMic(0.00);最强抑菌浓度
					 bactAl.setValLab(SDMsgUtils.getPropValueStr(obxMap, "references"));
					 bactAl.setEuAllevel("");
					 bactAl.setNote(oul_r21.getORDER_OBSERVATION(i).getNTE().getField(3, 0).encode());
					 bactAl.setDelFlag("0");
					 bactAl.setTs(new Date());
					 exLabOccBactAlList.add(bactAl);
				 }
			 }
			 //保存结果
			 sDMsgDataUpdate.saveLisRptList(SDMsgUtils.getPropValueStr(orderMap, "pkCnord"),codeApply,exLabOccList);
			 if(exLabOccBactAlList!=null && exLabOccBactList!=null){
				 sDMsgDataUpdate.saveBactRptList(codeApply,exLabOccBactList,exLabOccBactAlList);
			 }
		 }
	}


	/**
	 * 以#切割存入集合中
	 * @param obj
	 * @return
	 */
	private List<String> splitString(String obj){
		List<String> listStr = new ArrayList<>();
		String [] str= obj.split("#");
		int strInt = str.length;
		for (int i = 0; i < strInt; i++) {
			listStr.add(str[i]);
		}
		return listStr;
	}

	/**
	 * QBP^ZDI查询住院信息 （包括列表：Theme和详细：Details）
	 * @param hapiMsg
	 * @throws HL7Exception
	 */
	public String receiveQBPZDI(Message hapiMsg) throws HL7Exception{
		String msg = "";
		Map<String, Object> paramMap = new HashMap<String,Object>();
		Map<String, Object> mshMap = MsgParseUtils.getMSH(hapiMsg);
		String msgOldId = SDMsgUtils.getPropValueStr(mshMap, "msgOldId");
		String receive = SDMsgUtils.getPropValueStr(mshMap, "receive");
		Segment qpd = (Segment) hapiMsg.get("QPD");
		String type = qpd.getField(1,0).encode();//消息类型：Theme和Details
		paramMap.put("receive", receive);
		paramMap.put("msgOldId", msgOldId);
		paramMap.put("type", type);
		//获取消息患者数据
		String qpdValue = qpd.getField(3,0).encode();
		Map<String, Object> msgValue = MsgParseUtils.getMsgValue(qpdValue);
		String code = SDMsgUtils.getPropValueStr(msgValue, "value1");//住院号code_ip
		if("Theme".equals(type)){
			paramMap.put("codeIp",code );//住院号code_ip
			//查询患者信息
			List<Map<String, Object>> paramMapList = sDReceiveMapper.queryWechatPatList(paramMap);
			if(paramMapList != null && paramMapList.size()>0){
				paramMap.put("paramMapList", paramMapList);
				paramMap.put("situation", "AA");
				paramMap.put("text", "成功");
				//发送反馈消息
				msg = sDMsgSendIp.sendQueryIpMsg(paramMap);
			}else {
				paramMap.put("situation", "AA");
				paramMap.put("text", "处理完成：未查找到住院号为："+code+"病人的相关信息");
				msg = sDMsgSendIp.sendQueryIpMsg(paramMap);
			}
		}else if("Details".equals(type)){
			paramMap.put("codePv", code);//就诊号code_pv
			//查询患者信息
			List<Map<String, Object>> paramMapList = sDReceiveMapper.queryWechatPatList(paramMap);
			if(paramMapList != null && paramMapList.size()>0){
				String pkPv = SDMsgUtils.getPropValueStr(paramMapList.get(0), "pkPv");
				//查询预交金
				List<Map<String, Object>> zivList = sDQueryUtils.queryBlDeposit(pkPv);
				paramMap.put("zivList", zivList);
				paramMap.putAll(paramMapList.get(0));
				//查询诊断信息
				List<Map<String, Object>> diags = sDQueryUtils.queryDiagByPkpv(pkPv);
				paramMap.put("diags", diags);
				paramMap.put("situation", "AA");
				paramMap.put("text", "成功");
				//发送反馈消息
				msg = sDMsgSendIp.sendQueryIpMsg(paramMap);
			}
		}else {
			paramMap.put("codePv", code);//就诊号code_pv
			paramMap.put("situation", "AR");
			paramMap.put("text", "接收成功;处理完成:消息类型不明确；无法查找患者相关信息。请确认。。。");
			//发送反馈消息
			msg = sDMsgSendIp.sendQueryIpMsg(paramMap);
		}
		return msg;
	}


	/**
	 *查询患者信息(住院,门诊)	QBP^Q21
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 */
	public String receiveQBPQ21(Message hapiMsg) throws HL7Exception {
		String msg = "";
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.putAll(MsgParseUtils.getMSH(hapiMsg));
		//String msgOldId = SDMsgUtils.getPropValueStr(MsgParseUtils.getMSH(hapiMsg), "msgOldId");
		//String type = qpd.getField(1,0).encode();//消息类型：Theme和Details
		//paramMap.put("msgOldId", msgOldId);
		Segment qpd = (Segment) hapiMsg.get("QPD");
		String qpdValue = qpd.getField(3,0).encode();
		Map<String, Object> msgValue = MsgParseUtils.getMsgValue(qpdValue);
		String type = SDMsgUtils.getPropValueStr(msgValue, "value5");
		String code = SDMsgUtils.getPropValueStr(msgValue, "value1");//患者号（id_no或code_ip）
		String namePi=qpd.getField(5,0).encode();
		switch (type){
			case "IdentifyNO":				paramMap.put("dtIdtype","01" );paramMap.put("idNo",code );break;//居民身份证
			case "PassportNO":				paramMap.put("dtIdtype","10" );paramMap.put("idNo",code );break;//护照
			case "PermanentIdentityCard":	paramMap.put("dtIdtype","03" );paramMap.put("idNo",code );break;//港澳居民身份证
			case "ReentryPermitNO":			paramMap.put("dtIdtype","05" );paramMap.put("idNo",code );break;//港澳居民来往内地通行证（回乡证）
			case "MainlandTravelPermit":	paramMap.put("dtIdtype","08" );paramMap.put("idNo",code );break;//台湾居民来往大陆通行证（台胞证）
			case "OfficerID":				paramMap.put("dtIdtype","02" );paramMap.put("idNo",code );break;//军官证
			case "BirthCertificate":		paramMap.put("dtIdtype","09" );paramMap.put("idNo",code );break;//出生医学证明
			case "PatientNO":				paramMap.put("codeIp",code );break;
			default:paramMap.put("idNo",code );paramMap.put("codeIp",code );break;
		}
		//查询患者信息
		paramMap.put("namePi", namePi);
		if(CommonUtils.isNotNull(code) && CommonUtils.isNotNull(namePi)){
			//需求，姓名和身份证号相同则认为是同一个人
			List<Map<String, Object>> paramMapList = sDOpMsgMapper.querySDOpPiMaster(paramMap);
			// 查询出患者信息 等于1条
			if(paramMapList!=null && paramMapList.size()==1){
				// 判断名字是否一致
				if(namePi.equalsIgnoreCase(SDMsgUtils.getPropValueStr(paramMapList.get(0),"namePi"))){
					paramMap.putAll(paramMapList.get(0));
					paramMap.put("situation", "AA");
					paramMap.put("msaText", "成功");
				}else{
					paramMap.put("codePv", code);//就诊号code_pv
					paramMap.put("situation", "AR");
					paramMap.put("msaText", "患者证件号与姓名不一致：证件号："+SDMsgUtils.getPropValueStr(paramMapList.get(0),"idNo")+"；姓名："+SDMsgUtils.getPropValueStr(paramMapList.get(0),"namePi")+"；门诊号："+SDMsgUtils.getPropValueStr(paramMapList.get(0),"codePi"));
				}
			}else if(paramMapList!=null && paramMapList.size()>1){
				// 查询出患者信息 大于1条
				String msaText = "";
				for(Map<String, Object> map:paramMapList){
					//循环获取信息，如果名字相等，则返回
					if(namePi.equalsIgnoreCase(SDMsgUtils.getPropValueStr(map,"namePi"))){
						paramMap.putAll(map);
						paramMap.put("situation", "AA");
						paramMap.put("msaText", "成功");
						break;
					}else{
						msaText = "患者证件号与姓名不一致：证件号："+SDMsgUtils.getPropValueStr(map,"idNo")
								+"；姓名："+SDMsgUtils.getPropValueStr(map,"namePi")
								+"；门诊号："+SDMsgUtils.getPropValueStr(map,"codePi")+"；";
					}
				}
				//如果没有满足的患者信息，返回所有患者信息
				if(!"AA".equals(SDMsgUtils.getPropValueStr(paramMap,"situation"))){
					paramMap.put("codePv", code);//就诊号code_pv
					paramMap.put("situation", "AR");
					paramMap.put("msaText", msaText);
				}
			}else {
				paramMap.put("codePv", code);//就诊号code_pv
				paramMap.put("situation", "AR");
				paramMap.put("msaText", "接收成功;处理完成:未查找到该患者相关信息。");
			}
		}else{
			paramMap.put("codePv", code);//就诊号code_pv
			paramMap.put("situation", "AR");
			paramMap.put("msaText", "接收成功;处理失败:未按要求传入有效参数。");
		}
		//发送反馈消息
		msg = sDMsgSendIp.sendQueryPiMsg(paramMap);
		return msg;
	}

	/**
	 * 查询费用类别统计(TotalExpenses)QBP——ZPI
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 */
	public String receiveQBPZPI(Message hapiMsg) throws HL7Exception {
		String msg = "";
		Segment qpd = (Segment) hapiMsg.get("QPD");
		Map<String, Object> paramMap = new HashMap<String,Object>();
		Map<String, Object> mshMap = MsgParseUtils.getMSH(hapiMsg);
		paramMap.putAll(mshMap);
		String msgOldId = SDMsgUtils.getPropValueStr(mshMap, "msgOldId");
		paramMap.put("msgOldId",msgOldId );
		//获取消息类型   TotalExpenses   APPT  APPR
		String type = qpd.getField(2,0).encode();
		//获取需要的数据
		String qpdValue = qpd.getField(3,0).encode();
		Map<String, Object> msgValue = MsgParseUtils.getMsgValue(qpdValue);
		switch (type){
			//查询费用类别统计
			case "TotalExpenses":{
				String code = SDMsgUtils.getPropValueStr(msgValue, "value1");//住院号code_ip
				paramMap.put("codePv",code );//就诊流水号

				//发送反馈消息
				msg = sDMsgSendIp.sendTotalExpensesMsg(paramMap);
			}break;
			//已缴费发票列表查询(门诊 APPT)
			case "APPT":{
				String serialNo = SDMsgUtils.getPropValueStr(msgValue, "value1");
				//第三方流水号
				paramMap.put("serialNo", serialNo);
				msg = sDMsgSendOp.sendAPPTMsg(paramMap);
			}break;
			//已缴费列表查询(挂号 APPR)
			case "APPR":{
				String serialNo = SDMsgUtils.getPropValueStr(msgValue, "value1");
				//第三方流水号
				paramMap.put("serialNo", serialNo);
				msg = sDMsgSendOp.sendAPPRMsg(paramMap);
			}break;
			default:break;
		}
		return msg;
	}

	/**
	 * 押金缴入(住院) 接收 DFT^P03  WeChat
	 * @param dft_p03
	 * @return
	 * @throws HL7Exception
	 */
	public String ipDeposit(DFT_P03 dft_p03) throws HL7Exception{

		String msg = "";
		String msgOldId = dft_p03.getMSH().getMessageControlID().getValue();
		Map<String, Object> paramMap = new HashMap<String,Object>();
		paramMap.put("msgOldId",msgOldId );

		FT1 ft1 = dft_p03.getFINANCIAL().getFT1();
		//4. PV1-19：住院就诊流水号code_pv
		paramMap.put("codePv", dft_p03.getPV1().getVisitNumber().getID().getValue());
		//查询患者所需数据
		List<Map<String, Object>> queryPvByCodePv = sDQueryUtils.queryPvByCodePv(SDMsgUtils.getPropValueStr(paramMap, "codePv"));
		if(queryPvByCodePv != null && queryPvByCodePv.size()>0){
			paramMap.putAll(queryPvByCodePv.get(0));
		}
		//收费类型
		paramMap.put("euDptype", "9");//9:预交金
		//evn-5.1 收费员工号
		paramMap.put("nameEmpPay", dft_p03.getEVN().getOperatorID(0).getIDNumber().getValue());
		//EVN-5.2(FamilyName.Surname) ：支付方式(WX：微信，ZFB：支付宝)  码表：bd_defdoc  110100
		paramMap.put("dtPaymode", "7");
		paramMap.put("paymodeName", dft_p03.getEVN().getOperatorID(0).getFamilyName().getSurname().getValue());
		//3. EVN-5.3(GivenName) ：渠道
		paramMap.put("way", dft_p03.getEVN().getOperatorID(0).getFamilyName().getSurname().getValue());
		//5. FT1-3：交易流水号
		paramMap.put("payInfo", ft1.getTransactionID().getValue());
		paramMap.put("bankNo", ft1.getTransactionID().getValue());//页面上的凭证号码
		//6. FT1-4：交易时间
		paramMap.put("datePay", ft1.getTransactionDate().getTimeOfAnEvent().getValue());
		//7. FT1-6：APPPRE：押金缴入
		paramMap.put("nameEmpPay", ft1.getTransactionType().getValue());
		//8. FT1-11.3(FromValue)：总金额
		paramMap.put("acoumt", ft1.getTransactionAmountExtended().getFromValue().getValue());
		paramMap.put("flagCc", "0");//flagCc 操作员结账标志
		paramMap.put("euDirect", "1");// 收退方向 1收 -1退
		paramMap.put("flagReptBack", "0");//flagReptBack 表示预交金收据收回的标志
		paramMap.put("flagCc", "0");//flagCc 操作员结账标志
		paramMap.put("pkDept", "7f8c9d3cf2ae4f41aa0912a05ecbf731");//pkDept 收付款部门
		boolean rs = sDMsgDataUpdate.WechatDepositUpdete(paramMap);
		if(rs){
			paramMap.put("situation", "AA");
			paramMap.put("msaText", "成功");
			msg = sDMsgSendIp.sendFeedbackDepositMsg(paramMap);
		}else{
			paramMap.put("situation", "AR");
			paramMap.put("msaText", "失败");
			msg = sDMsgSendIp.sendFeedbackDepositMsg(paramMap);
		}

		return msg;
	}
	
	/**
	 * 查询押金缴入状态 接收 QBP^ZYJ
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 */
	public String receiveQBPZYJ(Message hapiMsg) throws HL7Exception {
		String msg = "";
		Map<String, Object> paramMap = new HashMap<String,Object>();
		//String msgOldId = SDMsgUtils.getPropValueStr(MsgParseUtils.getMSH(hapiMsg), "msgOldId");
		paramMap.putAll(MsgParseUtils.getMSH(hapiMsg));
		Segment qpd = (Segment) hapiMsg.get("QPD");
		String encode = qpd.getField(3, 0).encode();
		String[] split = encode.split("\\^");
		paramMap.put("bankNo",qpd.getField(2, 0).encode());
		//paramMap.put("msgOldId",msgOldId );
		paramMap.put("codePv",split[0]);
		msg = sDMsgSendIp.sendDepositStatusMsg(paramMap);
		return msg;
	}

	/**
	 * 住院病人护理评估
	 * @param hapiMsg
	 * @throws HL7Exception
	 */
	public String receiveZPIPSI(Message hapiMsg) throws HL7Exception {
		String msg = "";
		Segment pid = (Segment) hapiMsg.get("PID");
		Segment pv1 = (Segment) hapiMsg.get("PV1");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//pid-3.1 住院标志  pid-3.5 住院号 code_ip
		 String codeIp = pid.getField(3, 0).encode();
		 if(codeIp!=null && !"".equals(codeIp)){
			 String[] split = codeIp.split("\\^");
			 paramMap.put("codeIp", split[0]);
		 }
		//pv1-19 流水号code_PV
		String codePv = pv1.getField(19,0).encode();
		if(codePv!=null && !"".equals(codePv)){
			paramMap.put("codePv", codePv);
		}else {
			log.info("更新患者体重数据失败！患者住院流水号为空！"+hapiMsg.encode());
			throw new BusException("更新患者体重数据失败！患者住院流水号为空！");
		}
		//获取体重数据
		int indexOf = hapiMsg.encode().indexOf("|W^体重|");
		int indexOf2 = hapiMsg.encode().indexOf("|kg|");
		if(indexOf<0 || indexOf2<0){
			log.info("消息格式错误！未找到体重标识!"+hapiMsg.encode());
			throw new BusException("消息格式错误！未找到体重标识!");
		}else {
			String weightStr = hapiMsg.encode().substring(indexOf+6,indexOf2);
			try{
				double weight = Double.parseDouble(weightStr);
				paramMap.put("weight", weight);
			}catch(Exception e){
				throw new BusException("体重数据("+weightStr+")转换错误！"+e.getMessage());
			}
		}
		//更新数据
		sDMsgDataUpdate.updateWeightInfo(paramMap);
		return msg;
	}
	
	/**
	 * 已缴费记录明细查询
	 * @param hapiMsg
	 * @return
	 * @throws HL7Exception
	 */
	public String receiveQBPZDL(Message hapiMsg) throws HL7Exception {
		String msg = "";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("msgtype", "RSP");
		paramMap.put("triggerevent", "ZDL");
		paramMap.putAll(MsgParseUtils.getMSH(hapiMsg));
		//获取消息数据
		Segment qpd = (Segment) hapiMsg.get("QPD");
		String qpd3 = qpd.getField(3, 0).encode();
		//入参
		Map<String, Object> msgValue = MsgParseUtils.getMsgValue(qpd3);
		if(msgValue!=null && msgValue.size()>1){

		}else{
			throw new BusException("入参为空或入参格式有误");
		}
		msg = sDMsgSendIp.sendQueryBlMsg(paramMap);
		return msg;
	}
	
	/**
	 * 诊断消息
	 * @param hapiMsg
	 * @return
	 */
	public String receiveADTA31(Message hapiMsg) {
		String msg = "";
		//ADT_A05 adt_a31 = (ADT_A05) hapiMsg;
		return msg;
	}

	/**
	 * 体检检验申请
	 * @param hapiMsg
	 * @throws ParseException
	 */
	public void receiveOMLO21(Message hapiMsg) throws ParseException {
		//获取消息数据
		OML_O21 oml = (OML_O21)hapiMsg;
		Map<String,Object> result = new HashMap<>();
		//获取患者信息
		PID pid = oml.getPATIENT().getPID();
		PV1 pv1 = oml.getPATIENT().getPATIENT_VISIT().getPV1();
		String codePi = pid.getPid2_PatientID().getCx1_ID().getValue();
		String namePi = pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().getValue();
		String codePv = pv1.getPv119_VisitNumber().getCx1_ID().getValue();
		String send = oml.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		String type = pv1.getPv12_PatientClass().getValue();
		if("PEIS".equals(send) && "T".equals(type)){
			//体检
			result.put("euPvtype","4");
		}else{
			//门诊
			result.put("euPvtype","1");
		}
		result.put("codePi",codePi);
		result.put("namePi",namePi);
		boolean isDel = "CA".equals(oml.getORDER_GENERAL(0).getORDER(0).getORC().getOrc1_OrderControl().getValue());
		List<Map<String, Object>> queryPiPvOpbyPv = sDQueryUtils.queryPiPvOpbyPv(codePv,isDel);
		if(queryPiPvOpbyPv==null || queryPiPvOpbyPv.size()!=1){
			throw new BusException("\n未找到就诊流水号为"+codePv+"的就诊记录，或同一就诊流水号存在多条数据记录");
		}
		result.putAll(queryPiPvOpbyPv.get(0));
		//获取机构信息
		result.put("pkOrg", Constant.PKORG);
		//用户信息
		String codeEmp = pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().getValue();
		Map<String, Object> queryEmpByCode = sDQueryUtils.queryEmpByCode(codeEmp);
		result.put("codeEmp", codeEmp);
		result.put("pkEmp", SDMsgUtils.getPropValueStr(queryEmpByCode, "pkEmp"));
		result.put("nameEmp", SDMsgUtils.getPropValueStr(queryEmpByCode, "nameEmp"));
		//科室信息
		String dept = pv1.getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().getValue();
		Map<String, Object> queryDeptByCode = sDQueryUtils.queryDeptByCode(dept);
		result.put("codeDept", dept);
		result.put("nameDept", pv1.getPv13_AssignedPatientLocation().getPl5_LocationStatus().getValue());
		result.put("pkDept", SDMsgUtils.getPropValueStr(queryDeptByCode, "pkDept"));
		//result.put("codeEmp", pv1.getPv17_AttendingDoctor(0).getXcn1_IDNumber().getValue());
		//试管类型
		SAC sac = oml.getORDER_GENERAL(0).getCONTAINER_1().getSAC();
		//容器
		result.put("codeContainer", sac.getSac3_ContainerIdentifier().getEi1_EntityIdentifier().getValue());
		result.put("nameContainer", sac.getSac3_ContainerIdentifier().getEi2_NamespaceID().getValue());
		//标本
		result.put("codeLisType", sac.getSac4_PrimaryParentContainerIdentifier().getEi1_EntityIdentifier().getValue());
		result.put("nameLisType", sac.getSac4_PrimaryParentContainerIdentifier().getEi2_NamespaceID().getValue());
		String sample = sac.getSac6_SpecimenSource().getSps1_SpecimenSourceNameOrCode().getCe1_Identifier().getValue();
		if(sample!=null && sample.contains("&")){
			String[] split = sample.split("&");
			if(split.length>1){
				result.put("codeSample", split[0]);
				result.put("nameSample", split[1]);
			}
		}else{
			result.put("nameSample", sample);
		}
		//医嘱信息
		List<Map<String,Object>> cnOrderList = new ArrayList<>();
		int len = oml.getORDER_GENERAL(0).getORDERReps();
		for(int i=0;i<len;i++){
			Map<String,Object> cnOrder = new HashMap<>();
			//获取医嘱信息
			ORC orc = oml.getORDER_GENERAL(0).getORDER(i).getORC();
			OBR obr = oml.getORDER_GENERAL(0).getORDER(i).getOBSERVATION_REQUEST().getOBR();
			cnOrder.put("control", orc.getOrc1_OrderControl().getValue());
			//申请单
			StringBuffer codeApply = new StringBuffer();
			codeApply.append(orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue());
			cnOrder.put("codeApply", codeApply.toString());
			cnOrder.put("date", orc.getOrc9_DateTimeOfTransaction().getTs1_TimeOfAnEvent().getValue());
			cnOrder.put("codeEmp",orc.getOrc10_EnteredBy(0).getXcn1_IDNumber().getValue());
			String codeOrd = obr.getObr4_UniversalServiceIdentifier().getCe1_Identifier().getValue();
			cnOrder.put("codeOrd", codeOrd);
			String nameOrd = obr.getObr4_UniversalServiceIdentifier().getCe2_Text().getValue();
			cnOrder.put("nameOrd", nameOrd);
			Map<String, Object> queryBdOrdByCode = sDQueryUtils.queryBdOrdByCode(codeOrd);
			if(queryBdOrdByCode!=null){
				cnOrder.putAll(queryBdOrdByCode);
			}
			String codeDeptExec = obr.getObr24_DiagnosticServSectID().getValue();
			if(StringUtils.isNotBlank(codeDeptExec) ){
				if(codeDeptExec.contains("#")){
					String[] split = codeDeptExec.split("#");
					cnOrder.put("codeDeptExec", split[0]);
					cnOrder.put("nameDeptExec", split[1]);
					Map<String, Object> queryDeptByCodeEx = sDQueryUtils.queryDeptByCode(split[0]);
					cnOrder.put("pkDeptExec", SDMsgUtils.getPropValueStr(queryDeptByCodeEx, "pkDept"));
				}else{
					Map<String, Object> queryDeptByCodeEx = sDQueryUtils.queryDeptByCode(codeDeptExec);
					cnOrder.put("pkDeptExec", SDMsgUtils.getPropValueStr(queryDeptByCodeEx, "pkDept"));
					cnOrder.put("nameDeptExec", SDMsgUtils.getPropValueStr(queryDeptByCodeEx, "nameDept"));
				}
			}
			cnOrder.put("codeDeptExec", codeDeptExec);
			//获取收费信息
			FT1 ft1 = oml.getORDER_GENERAL(0).getORDER(i).getFT1();
			String value = ft1.getFt16_TransactionType().getValue();
			String value2 = ft1.getFt118_PatientType().getValue();
			StringBuffer note = new StringBuffer(StringUtils.isBlank(value)?"":value)
					.append("-").append(StringUtils.isBlank(value2)?"":value2);
			cnOrder.put("note", note.toString());
			String quan = ft1.getFt110_TransactionQuantity().getValue();
			cnOrder.put("quan", StringUtils.isBlank(quan)?"1":quan);
			cnOrderList.add(cnOrder);
			//团检标志
			if("J".equals(ft1.getFt118_PatientType().getValue())){
				result.put("J", "");
			}
			//门诊写入医嘱，一天只能开一次核酸逻辑  根据 患者就诊号和医嘱编码 和时间判断（医嘱表创建时间即ORC-9 ）
			if("1".equals(SDMsgUtils.getPropValueStr(result,"euPvtype"))){
				String sql = "select count(1) from PV_ENCOUNTER pv left join CN_ORDER cn on cn.PK_PV=pv.PK_PV where pv.CODE_PV=? and cn.CODE_ORD=? and to_char(cn.CREATE_TIME,'YYYYMMDD')=?";
				Integer check = DataBaseHelper.queryForScalar(sql, Integer.class, codePv, codeOrd, SDMsgUtils.getPropValueStr(cnOrder, "date").substring(0, 8));
				if(check >= 1){
					throw new BusException("当天已存在相同的医嘱:日期（ORC-9=cn_order.create_time）："+SDMsgUtils.getPropValueStr(cnOrder, "date")+",医嘱项目编码："+codeOrd);
				}
			}
		}
		result.put("cnOrderList", cnOrderList);

		//校验code_ord 是否停用
		List<Map<String, Object>> mapList = sDReceiveMapper.queryBdOrd(result);
		if(mapList != null && mapList.size()>0){
			throw new BusException("\n项目已经停用："+mapList.toString());
		}
		//1、保存cn_order
		//2、保存cn_lis_apply
		//3、保存bl_op_dt(调用记费接口)(标本费等相关费用)
		sDSysMsgBodyCheckService.bodyCheckLis(result);

	}

	/**
	 * 取消挂号逻辑
	 * @param hapiMsg
	 * @throws ParseException
	 */
	public void receiveADTA11(Message hapiMsg) throws ParseException {
		//1、获取消息数据
		ADT_A09 adt_a11 = (ADT_A09)hapiMsg;
		PV1 pv1 = adt_a11.getPV1();
		//不是体检不做处理，直接退出
		if(!"T".equals(pv1.getPv12_PatientClass().getValue()))return;
		//获取数据
		Map<String,Object> paramMap = new HashMap<String,Object>();
		//evn 5-1 退号医生id
		paramMap.put("codeEmp", adt_a11.getEVN().getEvn5_OperatorID(0).getXcn1_IDNumber().getValue());
		//evn 5-2 退号医生名字
		paramMap.put("nameEmp", adt_a11.getEVN().getEvn5_OperatorID(0).getXcn2_FamilyName().getFn1_Surname().getValue());
		// evn 6 退号时间
		paramMap.put("date", adt_a11.getEVN().getEvn6_EventOccurred().getTs1_TimeOfAnEvent().getValue());
		//pv1 19 就诊流水号
		paramMap.put("codePv", pv1.getPv119_VisitNumber().getCx1_ID().getValue());
		//2、退号 + 退费
		sDSysMsgBodyCheckService.updateADTA11(paramMap);
	}

	/**
	 * 保存入院通知单
	 * @param hapiMsg
	 * @return
	 * @throws ParseException
	 */
	public String receiveADTA06(Message hapiMsg) throws ParseException {
		String msg = "";
		ADT_A06 adt_a06 = (ADT_A06)hapiMsg;
		//1、获取消息数据
		/**
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String codePi = adt_a06.getPID().getPid2_PatientID().getCx1_ID().getValue();
		paramMap.put("codePi", codePi);
		//codePv
		paramMap.put("codePv", adt_a06.getPV1().getPv19_ConsultingDoctor(0).getXcn1_IDNumber().getValue());
		//科室信息
		paramMap.put("codeDept", adt_a06.getPV1().getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().getValue());
		paramMap.put("nameDept", adt_a06.getPV1().getPv13_AssignedPatientLocation().getPl5_LocationStatus().getValue());
		//医生信息
		paramMap.put("codeEmp", adt_a06.getPV1().getPv17_AttendingDoctor(0).getXcn1_IDNumber().getValue());
		paramMap.put("nameEmp", adt_a06.getPV1().getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().getValue());
		//时间
		paramMap.put("date", adt_a06.getPV1().getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue());
		//诊断信息
		paramMap.put("diag", adt_a06.getDG1(0).getDg14_DiagnosisDescription().getValue());
		*/
		//判断是否已有入院通知单 codePi
		boolean insert = true;
		String codePi = adt_a06.getPID().getPid2_PatientID().getCx1_ID().getValue();
		String codePv = adt_a06.getPV1().getPv119_VisitNumber().getCx1_ID().getValue();
		//门诊科室
		String codeDeptOp = adt_a06.getPV1().getPv13_AssignedPatientLocation().getPl4_Facility().getHd1_NamespaceID().getValue();
		//String nameDept = adt_a06.getPV1().getPv13_AssignedPatientLocation().getPl5_LocationStatus().getValue();
		Map<String, Object> deptOp = sDQueryUtils.queryDeptByCode(codeDeptOp);
		//入院科室
		String codeDeptIp = adt_a06.getPV1().getPv142_PendingLocation().getPl4_Facility().getHd2_UniversalID().getValue();
		Map<String, Object> deptIp = sDQueryUtils.queryDeptByCode(codeDeptIp);
		String codeEmp = adt_a06.getPV1().getPv17_AttendingDoctor(0).getXcn1_IDNumber().getValue();
		String nameEmp = adt_a06.getPV1().getPv17_AttendingDoctor(0).getXcn2_FamilyName().getFn1_Surname().getValue();
		Map<String, Object> emp = sDQueryUtils.queryEmpByCode(codeEmp);
		String date = adt_a06.getPV1().getPv144_AdmitDateTime().getTs1_TimeOfAnEvent().getValue();
		String diag = adt_a06.getDG1(0).getDg14_DiagnosisDescription().getValue();
		PvIpNotice pvIpNotice = sDQueryUtils.queryPvIpNotice(codePi);
		if(pvIpNotice == null){
			PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where  code_pi = ?",PiMaster.class, codePi);
			PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where CODE_PV = ?",PvEncounter.class, codePv);
			pvIpNotice = new PvIpNotice();
			pvIpNotice.setPkInNotice(NHISUUID.getKeyId());
			pvIpNotice.setPkOrg(Constant.PKORG);
			pvIpNotice.setPkPi(piMaster.getPkPi());
			pvIpNotice.setPkHp(pvEncounter.getPkInsu());
			pvIpNotice.setPkPvOp(pvEncounter.getPkPv());
			//pvIpNotice.setPkPvIp(null);
			pvIpNotice.setPkDeptOp(SDMsgUtils.getPropValueStr(deptOp, "pkDept"));
			pvIpNotice.setPkEmpOp(SDMsgUtils.getPropValueStr(emp, "pkEmp"));
			pvIpNotice.setNameEmpOp(nameEmp);
			//pvIpNotice.setPkDiagMaj(null);
			pvIpNotice.setDescDiagMaj(diag);
			//pvIpNotice.setPkDiagTcm(null);
			//pvIpNotice.setDescDiagMajTcm(null);
			//pvIpNotice.setDescDiagElsTcm(null);
			//pvIpNotice.setDescDiagEls(null);
			//pvIpNotice.setDescDiagIp(null);
			pvIpNotice.setDescDiagOp(diag);
			//pvIpNotice.setDtLevelDise(null);
			//pvIpNotice.setPkDeptIp(null);
			pvIpNotice.setPkDeptNsIp(SDMsgUtils.getPropValueStr(deptIp, "pkDept"));
			//pvIpNotice.setDtBedtype(null);
			//pvIpNotice.setBedNo(null);
			pvIpNotice.setDateAdmit(sdf.parse(date));
			//pvIpNotice.setAmtDepo(null);//预交金金额
			pvIpNotice.setEuStatus("0");
			//pvIpNotice.setContactDept(null);
			//有效期默认一个月
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, 1);
			pvIpNotice.setDateValid(calendar.getTime());
			//pvIpNotice.setNote(null);
			pvIpNotice.setCreator(Constant.NOTE);
			pvIpNotice.setCreateTime(new Date());
			//pvIpNotice.setModifier(null);
			pvIpNotice.setDelFlag("0");
			pvIpNotice.setTs(new Date());
		}else{
			insert = false;
			pvIpNotice.setPkDeptOp(SDMsgUtils.getPropValueStr(deptOp, "pkDept"));
			pvIpNotice.setPkEmpOp(SDMsgUtils.getPropValueStr(emp, "pkEmp"));
			pvIpNotice.setNameEmpOp(nameEmp);
			pvIpNotice.setDescDiagMaj(diag);
			pvIpNotice.setDescDiagOp(diag);
			pvIpNotice.setDateAdmit(sdf.parse(date));
			pvIpNotice.setModifier(Constant.NOTE);
			//pvIpNotice.setTs(new Date());
			//有效期
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(date));
			calendar.add(Calendar.MONTH, 1);
			pvIpNotice.setDateValid(calendar.getTime());
		}
		//2、判断是否需要保存 写表PV_IP_NOTICE
		sDMsgDataUpdate.updateADTA06(pvIpNotice,insert);
		return msg;
	}
}
