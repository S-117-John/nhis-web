package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.DFT_P03;
import ca.uhn.hl7v2.model.v24.message.SRM_S01;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.*;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreate;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreateUtil;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.ACK_P03;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZPO;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 自助机服务对接业务处理
 * @author jd_em
 *
 */
@Service
@SuppressWarnings("unchecked")
public class SDSysMsgSelfAppService {

	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");

	@Resource
	private RegSyxService regSyxService;
	@Autowired
	private SDOpMsgMapper sDOpMsgMapper;
	@Resource
	private MsgCreate msgCreate;
	@Resource
	private MsgCreateUtil msgCreateUtil;
	@Resource
	private OpcgPubHelperService helperService;
	@Resource
	private TicketPubService ticketPubService;
	@Resource
    private PvInfoPubService pvInfoPubService;
	//电子发票
	@Autowired
	private ElectInvService electInvService;
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");



/*******************************预挂号********************************************/

	/**
	 * 预约挂号
	 * @param so1
	 * @return
	 * @throws HL7Exception
	 */
	public String appRegistPiInfo(SRM_S01 so1,Message mes) throws HL7Exception{
		Map<String, Object> appRegParam = new ConcurrentHashMap<String, Object>();
		String stu = so1.getARQ().getAppointmentReason().getIdentifier().getValue();
		String oldMsgId = so1.getMSH().getMessageControlID().getValue();
		appRegParam.put("oldmsgid", oldMsgId);
		appRegParam.put("isSur", "01");//01自费  02医保
		String sendApp = so1.getMSH().getSendingApplication().getNamespaceID().getValue();
		appRegParam.put("sendApp", sendApp);
		//预约渠道，微信写1，自助机写2
		appRegParam.put("orderSource", "2");
		//ARQ-1外部预约系统订单号
		String outsideOrderId = so1.getARQ().getPlacerAppointmentID().getEntityIdentifier().getValue();
		appRegParam.put("outsideOrderId", outsideOrderId);
		PID pid = so1.getPATIENT(0).getPID();
		int size = pid.getPatientIdentifierListReps();
		//PID-3.1患者卡号    PID-3.2患者卡类型
		for (int i = 0; i < size; i++) {
			String id = pid.getPatientIdentifierList(i).getID().getValue();
			String idType = pid.getPatientIdentifierList(i).getIdentifierTypeCode().getValue();
			if ("IDCard".equals(idType)||"CardNO".equals(idType)||"PatientNO".equals(idType)) {//患者编号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("patientno", id);
			}else if ("IdentifyNO".equals(idType)) {//身份证号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("identifyno", id);
			}else if("SocialSecurityCardNumber".equals(idType)){//社保卡号
				if(StringUtils.isNotBlank(id)){
					appRegParam.put("socialsecuritycardnumber", id);
					appRegParam.put("isSur", "02");
				}
			}else if("SBpcNO".equals(idType)){//电脑号
				if(StringUtils.isNotBlank(id)){
					appRegParam.put("SBpcNO", id);
					appRegParam.put("isSur", "02");
					String sql = "select pk_hp,name from bd_hp where del_flag = '0' and code = 'A31001' ";
					Map<String , Object> insurMap = DataBaseHelper.queryForMap(sql);
					appRegParam.put("pkHp", insurMap.get("pkHp"));
					appRegParam.put("name", insurMap.get("name"));
					appRegParam.put("insurTypeCode","A31001");
				}
			}else if("PassportNO".equals(idType)){//护照号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("PassportNO", id);
			}else if ("ReentryPermitNO".equals(idType)) {//回乡证号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("identifyno", id);
			}else if ("DrivingLicenseNO".equals(idType)) {//驾驶证号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("identifyno", id);
			}else if ("MilitaryIDNO".equals(idType)) {//军官证号
				if(StringUtils.isNotBlank(id))
					appRegParam.put("MilitaryIDNO", id);
			}
		}
		//PID-2 患者编号
		String patientNO = pid.getPatientID().getID().getValue();
		if(StringUtils.isNotBlank(patientNO))
			appRegParam.put("patientno", patientNO);
		//PID-5   患者姓名
		String piName =pid.getPatientName(0).getFamilyName().getSurname().getValue();
		if(StringUtils.isNotBlank(piName))
			appRegParam.put("piname", piName);
		//PID-7   患者出生日期
		String piDate = pid.getDateTimeOfBirth().getTimeOfAnEvent().getValue();
		if(StringUtils.isNotBlank(piDate))
			appRegParam.put("pidate", piDate);
		//PID-8   患者性别
		String piSex =pid.getAdministrativeSex().getValue();
		if(StringUtils.isNotBlank(piSex))
			appRegParam.put("pisex",SDMsgUtils.getParseSex(piSex));
		//PID-38 医保名称  医保类型
		String insurTypeCode = pid.getProductionClassCode().getIdentifier().getValue();
		//默认自费 (体检默认传1)
		insurTypeCode = insurTypeCode==null||"".equals(insurTypeCode)||"1".equals(insurTypeCode)?"01":insurTypeCode;
		//PID-39医保类型明细
		if("07".equals(insurTypeCode)){
			insurTypeCode = pid.getProductionClassCode().getNameOfCodingSystem().getValue();
			appRegParam.put("isSur", "02");
		}
		Map<String,Object> insurMap = DataBaseHelper.queryForMap("select pk_hp,name from bd_hp where del_flag = '0' and code = ? ", insurTypeCode);
		if(insurMap==null)
			throw new BusException("未查询到该("+insurTypeCode+")医保编号所对应的医保主建信息");
		appRegParam.put("pkHp", insurMap.get("pkHp"));
		appRegParam.put("name", insurMap.get("name"));
		appRegParam.put("insurTypeCode",insurTypeCode);
		//PID-13   患者手机号
		String piTel = pid.getPhoneNumberHome(0).getPhoneNumber().getValue();
		appRegParam.put("pitel",piTel==null?"":piTel);

		//AIL-3 预约科室
		String deptCode = so1.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getLocationResourceID().getPointOfCare().getValue();
		if(StringUtils.isNotBlank(deptCode))
			appRegParam.put("deptcode",deptCode);
		//AIP-1排版主键
		String pkSch = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getSetIDAIP().getValue();
		//String pk_sch = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getFillerStatusCode().getIdentifier().getValue();

		//PV1-2 门诊住院标识  （根据类型判断患者类型）  9是急诊
		String sqlDept = "select srv.EU_SRVTYPE,appt.PK_DEPT_EX,dept.NAME_DEPT,dept.dt_medicaltype,dept.CODE_DEPT from sch_appt appt inner join sch_srv srv on appt.PK_SCHSRV=srv.PK_SCHSRV inner join BD_OU_DEPT dept on dept.PK_DEPT=appt.PK_DEPT_EX where appt.PK_SCH=?";
		Map<String, Object> euPvtypeMap = DataBaseHelper.queryForMap(sqlDept, pkSch);
		String euSrvtype = SDMsgUtils.getPropValueStr(euPvtypeMap, "euSrvtype");
		String euPvtype = "9".equals(euSrvtype)?"2":"1";
		String euPvtypeMsg = "O";
		if("30".equals(SDMsgUtils.getPropValueStr(euPvtypeMap, "dtMedicaltype"))){
			euPvtype = "4";
			euPvtypeMsg = "T";
		}
		appRegParam.put("euPvtype", euPvtype);
		appRegParam.put("euPvtypeMsg",euPvtypeMsg);
		appRegParam.put("euSrvtype", euSrvtype);
		//AIP-3 医生工号
		String docCode = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getPersonnelResourceID(0).getIDNumber().getValue();
		if(StringUtils.isNotBlank(docCode))
			appRegParam.put("doccode",docCode);
		//AIP-4  预约挂号级别
		String level = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getResourceRole().getIdentifier().getValue();
		if(StringUtils.isNotBlank(level))
			appRegParam.put("level",level);
		//AIP-6 预约日期
		String day = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getStartDateTime().getTimeOfAnEvent().getValue();
		appRegParam.put("day",day);
		try {
			String zai [] =mes.toString().split("ZAI");
			String [] ele = zai[1].toString().split("\\|");
			//ZAI-4 日期
			//String begindate = ele[1];
			//appRegParam.put("begindate",begindate);
			//ZAI-4就诊开始时间
			String beginTime = ele[3];
			appRegParam.put("begintime",beginTime);
			//ZAI-5就诊结束时间
			String endTime =  ele[4];
			appRegParam.put("endtime",endTime);
		} catch (Exception e) {
			throw new BusException("消息SRM_S01中ZAI-5字段必须存值，则解析错报");
		}

		//校验数据
		if(StringUtils.isNotBlank(pkSch)){
			pkSch = pkSch.split("#")[0];
			appRegParam.put("pksch",pkSch);
			//获取资源主键
			String pkRes;
			try{
				String sql = "select sch.PK_SCHRES from SCH_SCH sch where sch.PK_SCH=? and sch.DEL_FLAG='0' and sch.FLAG_STOP='0' and sch.EU_STATUS='8'";
				pkRes = DataBaseHelper.queryForScalar(sql,String.class,pkSch);
			}catch(Exception e){
				throw new BusException("未查询到该排班的挂号资源信息！");
			}
			//判断是否重复预约
			String checkSql = "select to_char(DATE_APPT,'YYYYMMDD') DATE_APPT,appt.PK_SCH,appt.PK_PI,appt.EU_STATUS,appt.FLAG_PAY from SCH_APPT appt inner join PI_MASTER pi on pi.PK_PI=appt.PK_PI where appt.PK_SCHRES=? and pi.CODE_PI=? and appt.FLAG_CANCEL='0' and appt.DEL_FLAG='0' and appt.EU_STATUS in ('0','1') ";
			List<Map<String, Object>> checkList  = DataBaseHelper.queryForList(checkSql,pkRes,patientNO);
			if(checkList != null){
				for(Map<String, Object> checkMap : checkList){
					//如果存在资源 且为当天的 ，再判断是否支付
					if(SDMsgUtils.getPropValueStr(checkMap,"dateAppt").equalsIgnoreCase(day)){
						if("1".equalsIgnoreCase(SDMsgUtils.getPropValueStr(checkMap,"flagPay"))){
							throw new BusException("该患者编号("+patientNO+")已预约，已支付，请直接就诊！");
						}else{
							throw new BusException("该患者编号("+patientNO+")已预约，未支付，请缴费取号！");
						}
					}
					//如果资源相同且排班主键相同，也不允许挂号
					if(SDMsgUtils.getPropValueStr(checkMap,"pkSch").equalsIgnoreCase(pkSch)){
						throw new BusException("该患者编号("+patientNO+")已存在预约记录,请到窗口确认！");
					}
				}
			}
		}else {
			throw new BusException("未传入排班主建信息");
		}

		return addAppRegistPiInfo(appRegParam);
	}

	/**
	 *	微信预约登记消息:创建SRRS01消息返回
	 * @param appRegParam
	 * @return
	 * @throws HL7Exception
	 */
	private String createSRRS01(Map<String, Object> appRegParam) throws HL7Exception{

		SRR_S01 srr = new SRR_S01();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = srr.getMSH();
		MSA msa = srr.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
		msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(appRegParam, "sendApp"));//@todo
		String appCode =  SDMsgUtils.getPropValueStr(appRegParam,"appCode");
		//Map<String, Object>  priceMap = sDOpMsgMapper.querySDOpSchAllInfo(appRegParam).get(0);
		//查询费用	//医保号使用 0304
		if(!"01".equals(SDMsgUtils.getPropValueStr(appRegParam, "insurTypeCode"))){
			appRegParam.put("yb", ApplicationUtils.getCode("0304"));
		}
		String sqlPi = "select pi.pk_pi,pi.name_pi,pi.pk_org,pi.pk_picate,appt.pk_schsrv,piCate.flag_Spec,to_char(pi.birth_date,'yyyy-MM-dd') date_birth   from 	sch_appt appt left join  pi_master pi on pi.pk_pi = appt.pk_pi  left join  	pi_cate  piCate on piCate.Pk_Picate = pi.pk_picate  where appt.code = ?  and pi.del_flag = '0'";
		Map<String, Object>  bdPiMap = DataBaseHelper.queryForMap(sqlPi, SDMsgUtils.getPropValueStr(appRegParam,"appCode"));
		Map<String, Object> insurParam = new HashMap<>();
		insurParam.put("dateBirth", SDMsgUtils.getPropValueStr(bdPiMap,"dateBirth"));
		insurParam.put("flagSpec", SDMsgUtils.getPropValueStr(bdPiMap,"flagSpec"));
		insurParam.put("pkPicate", SDMsgUtils.getPropValueStr(bdPiMap,"pkPicate"));
		insurParam.put("pkSchsrv", SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"));
		insurParam.put("pkInsu", SDMsgUtils.getPropValueStr(appRegParam,"pkHp"));
		insurParam.put("nameInsu",SDMsgUtils.getPropValueStr(appRegParam,"name"));
		insurParam.put("euPvType",SDMsgUtils.getPropValueStr(appRegParam,"euPvtype"));
		insurParam.put("pkEmp","");
		String param = JsonUtil.writeValueAsString(insurParam);
		User user = new User();
		user.setPkEmp(SDMsgUtils.getPropValueStr(bdPiMap,"pkPi"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(bdPiMap,"namePi"));
		user.setPkOrg(SDMsgUtils.getPropValueStr(bdPiMap,"pkOrg"));
		List<ItemPriceVo> listItem  = regSyxService.getItemBySrv(param,user);
		//总金额
		BigDecimal price = BigDecimal.ZERO;
		if(listItem!=null && listItem.size()>0){
			for(ItemPriceVo item : listItem){
				price = price.add(new BigDecimal((item.getPrice()>0)?item.getPrice():0)).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}else{
			//查询0元收费项目
			String sql = "select item.code,item.name name_cg,item.price,item.pk_item from sch_srv_ord srvOrd left join bd_ord_item oi on oi.PK_ORD=srvOrd.PK_ORD left join BD_ITEM item on item.PK_ITEM=oi.PK_ITEM where srvOrd.eu_type='0' and srvOrd.PK_SCHSRV=?";
			listItem = DataBaseHelper.queryForList(sql, ItemPriceVo.class, new Object[]{SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv")});
		}
		msa.getAcknowledgementCode().setValue("AA");
		msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(appRegParam,"oldmsgid"));
		msa.getTextMessage().setValue("成功");
		msa.getExpectedSequenceNumber().setValue("100");
		msa.getDelayedAcknowledgmentType().setValue("F");
		SCH sch = srr.getSCHEDULE().getSCH();
		sch.getFillerAppointmentID().getEntityIdentifier().setValue(SDMsgUtils.getPropValueStr(appRegParam,"appCode"));//取号密码   随机生成
		sch.getFillerStatusCode().getIdentifier().setValue("0");//0不能转自费，1为可转自费
		NTE nte0 = srr.getSCHEDULE().getNTE(0);
		nte0.getComment(0).setValue(price.setScale(2, BigDecimal.ROUND_HALF_UP).toString());//自费扣费金额
		nte0.getCommentType().getText().setValue("Zfzje");
		NTE nte1 = srr.getSCHEDULE().getNTE(1);
		nte1.getComment(0).setValue("0");//社保扣费金额
		nte1.getCommentType().getText().setValue("Sbzje");
		NTE nte2 = srr.getSCHEDULE().getNTE(2);
		nte2.getComment(0).setValue("H8880"+SDMsgUtils.getPropValueStr(appRegParam,"appCode"));//预约回传预约号
		nte2.getCommentType().getText().setValue("Mzlsh");
		NTE nte3 = srr.getSCHEDULE().getNTE(3);
		nte3.getComment(0).setValue("0");//单据号
		nte3.getCommentType().getText().setValue("Djh");
		/*NTE nte4 = srr.getSCHEDULE().getNTE(1);
		nte4.getComment(0).setValue("0");//看诊流水号
		nte4.getCommentType().getText().setValue("Sbzje");*/
		srr.getSCHEDULE().getPATIENT().getPID().getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(appRegParam,"codePi"));//患者编号
		srr.getSCHEDULE().getPATIENT().getPID().getPatientName(0).getFamilyName().getSurname().setValue(SDMsgUtils.getPropValueStr(appRegParam,"namePi"));//患者名称

		PV1 pv1 = srr.getSCHEDULE().getPATIENT().getPV1();
		//PV1-2
		pv1.getPatientClass().setValue(SDMsgUtils.getPropValueStr(appRegParam,"euPvtypeMsg"));//O门诊  I住院
		//PV1-3  就诊科室编码   就诊科室名称
		Map<String, Object> mapDept =  DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where code_dept = ? and del_flag = '0' ", SDMsgUtils.getPropValueStr(appRegParam,"deptcode"));
		pv1.getAssignedPatientLocation().getFacility().getNamespaceID().setValue( SDMsgUtils.getPropValueStr(appRegParam,"deptcode"));
		pv1.getAssignedPatientLocation().getLocationStatus().setValue( SDMsgUtils.getPropValueStr(mapDept,"nameDept"));
		//PV1-18  患者类型  01：自费，02：医保
		pv1.getPatientType().setValue(SDMsgUtils.getPropValueStr(appRegParam,"isSur"));
		//PV1-19  唯一流水号 预约号
		pv1.getVisitNumber().getID().setValue(appCode);
		//PV1-50 自费传就诊流水号      医保传社保流水号
		pv1.getAlternateVisitID().getID().setValue(appCode);
		//医保数据段
		if("02".equals(SDMsgUtils.getPropValueStr(appRegParam,"isSur"))){
			String sqlsb = "select itemmap.ake001 from bd_item item  inner join ins_szyb_itemmap itemmap on itemmap.pk_item = item.pk_item  where item.del_flag = '0' and itemmap.del_flag = '0' and itemmap.EU_HPDICTTYPE = '01'  and item.pk_item  = ? ";
			Map<String, Object>  sbContextMap = DataBaseHelper.queryForMap(sqlsb,  listItem.get(0).getPkItem());

			String  ybStr = SDMsgUtils.getPropValueStr(appRegParam,"yb");
			//拼接z段消息
			String[] strZCT=new String[40];
			//ZCT-4终端号
			strZCT[3] = "000000";
			//ZCT-10 交易流水号      //改方法仅深大使用  故暂定写死医院编码     bke384(医药机构结算业务序列号)
			strZCT[9] ="H8880"+sdfDay.format(new Date())+SDMsgUtils.getPropValueStr(appRegParam,"yb").substring(ybStr.length()-7,ybStr.length());
			//ZCT-14 交易发起日期
			strZCT[13] =sdfDay.format(new Date());
			//ZCT-15 交易发起时间
			strZCT[14] =sdfTime.format(new Date())+"000";
			//ZCT-17 交易类型
			strZCT[16] ="MZ002";
			//ZCT-18 交易版本号
			strZCT[17] ="V0.2";
			//ZCT-19 发卡地行政区划代码
			strZCT[18] ="440300";
			//ZCT-20 协议机构编码
			strZCT[19] ="H8880";
			//ZCT-21 操作员
			strZCT[20] ="H888016H"+"^"+"微信";
			//ZCT-22 交易渠道
			strZCT[21] ="";
			//ZCT-23 交易验证码
			strZCT[22] ="";
			srr.addNonstandardSegment("ZCT");
			Segment zct = (Segment) srr.get("ZCT");
			zct.parse(msgCreateUtil.createZMsgStr(strZCT));

			//拼接z段消息
			String[] strZDS=new String[40];
			//ZDS-1顺序号
			strZDS[0] = "1";
			strZDS[1] = appCode;
			//ZDS-3 费用序列号
			strZDS[2] = appCode;
			//ZDS-4 社保目录编码
			strZDS[3] = SDMsgUtils.getPropValueStr(sbContextMap,"ake001");
			//ZDS-5  协议机构内部诊疗目录 编码^名称
			strZDS[4] =  listItem.get(0).getCode()+"^"+listItem.get(0).getNameCg();
			//ZDS-6 金额   aae019
			strZDS[5] = String.valueOf(price);

			srr.addNonstandardSegment("ZDS");
			Segment zds = (Segment) srr.get("ZDS");
			zds.parse(msgCreateUtil.createZMsgStr(strZDS));

			/*Double zfPrice = Double.valueOf(SDMsgUtils.getPropValueStr(priceMap,"price")) - listItem.get(0).getPrice();
			nte0.getComment(0).setValue(String.valueOf(zfPrice));//自费扣费金额*/
			nte0.getComment(0).setValue(String.valueOf(price));
			nte0.getCommentType().getText().setValue("Zfzje");
			/*nte1.getComment(0).setValue(String.valueOf(zfPrice));//社保扣费金额*/
			nte1.getComment(0).setValue("0");
			nte1.getCommentType().getText().setValue("Sbzje");
			//PID-3 社保号和社保密码
			srr.getSCHEDULE().getPATIENT().getPID().getPatientIdentifierList(0).getID().setValue(SDMsgUtils.getPropValueStr(appRegParam, "socialsecuritycardnumber"));
			srr.getSCHEDULE().getPATIENT().getPID().getPatientIdentifierList(0).getIdentifierTypeCode().setValue("SocialSecurityCardNumber");
			srr.getSCHEDULE().getPATIENT().getPID().getPatientIdentifierList(1).getID().setValue("000000");
			srr.getSCHEDULE().getPATIENT().getPID().getPatientIdentifierList(1).getIdentifierTypeCode().setValue("SocialSecurityCardPassword");
			// PID-36 普通门诊
			srr.getSCHEDULE().getPATIENT().getPID().getBreedCode().getAlternateIdentifier().setValue("11");
			String sqlRes = "select srv.code srvcode,res.pk_dept,dept.code_dept  from  sch_resource  res left join sch_sch sch on sch.pk_schres = res.pk_schres  left join sch_srv srv  on  sch.pk_schsrv  = srv.pk_schsrv  left join bd_ou_dept dept on (dept.pk_dept = res.pk_dept_belong or dept.pk_dept = res.pk_dept)  where sch.pk_sch = ? and sch.del_flag = '0' and dept.del_flag = '0' ";
			Map<String, Object>  resMap = DataBaseHelper.queryForMap(sqlRes, SDMsgUtils.getPropValueStr(appRegParam,"pkSch"));
			String sqlInsur =  "select code_insur from ins_szyb_dictmap where DEL_FLAG='0'  and eu_hpdicttype='01'  and flag_chd='1' and code_type= 'AKF001' and code_his= '"+SDMsgUtils.getPropValueStr(resMap,"codeDept") +"'" ;
			Map<String, Object>  qryMap = DataBaseHelper.queryForMap(sqlInsur);

			//PV1-4    bkc368  挂号类别
			srr.getSCHEDULE().getPATIENT().getPV1().getAdmissionType().setValue(SDMsgUtils.ghTypeTran(SDMsgUtils.getPropValueStr(resMap,"srvcode")));
			//PV1-11   akf001  科室编码
			srr.getSCHEDULE().getPATIENT().getPV1().getTemporaryLocation().getPointOfCare().setValue(SDMsgUtils.getPropValueStr(qryMap,"codeInsur"));
			//PV1-49   akc264   医疗费总额
			srr.getSCHEDULE().getPATIENT().getPV1().getTotalCharges().setValue(price.toString());
		}
		msg =  SDMsgUtils.getParser().encode(srr);
		return msg;
	}

	/**
	 * 微信预约登记消息
	 * 0.准备消息参数值；
	 * 1.处理消息业务逻辑
	 * 3.拼接回传第三方消息
	 * @param appRegParam
	 * @return
	 * @throws HL7Exception
	 */
	private String addAppRegistPiInfo(Map<String, Object> appRegParam) throws HL7Exception{
		//TODO 需要添加参数校验，否则无法排查错误困难

		// 0.准备消息参数值；
		PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);
		// 1.处理消息业务逻辑
		PiMasterRegVo piMasregvo = disposeAppRegistPi(appRegParam,regvo);
		appRegParam.put("appCode", piMasregvo.getApptCode());
		appRegParam.put("codePi", piMasregvo.getCodePi());
		appRegParam.put("namePi", piMasregvo.getNamePi());
		appRegParam.put("pkSch", piMasregvo.getPkSch());
		return createSRRS01(appRegParam);
	}

	/**
	 * 微信预约登记消息
	 * 1.判断是否还有可约号
	 * 2.占用号源
	 * 3.保存预约挂号信息（含保存患者信息）
	 * @param appRegParam
	 * @return
	 */
	private PiMasterRegVo disposeAppRegistPi(Map<String, Object> appRegParam,PiMasterRegVo regvo){
		String pkSch = SDMsgUtils.getPropValueStr(appRegParam,"pksch");
		//校验数据
		boolean haveTickNo = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ? and FLAG_USED='0' and FLAG_STOP='0' and FLAG_APPT='1'",Integer.class, new Object[]{pkSch})>0;
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",SchSch.class, pkSch);
		//Map<String, Object>  codeDateslotSecMap =   DataBaseHelper.queryForMap("select pk_dateslotsec from bd_code_dateslot_sec where  ? = to_number(to_char(to_date(time_begin,'hh24:mi:ss' ),'hh24miss'))   and  ? = to_number(to_char(to_date(time_end,'hh24:mi:ss' ),'hh24miss'))  ", SDMsgUtils.getPropValueStr(appRegParam,"begintime"),SDMsgUtils.getPropValueStr(appRegParam,"endtime"));
		//Map<String, Object>  codeDateslotSecMap =   DataBaseHelper.queryForMap("select pk_dateslotsec from bd_code_dateslot_sec where  ? = replace(time_begin,':') and  ? = replace(time_end,':')  ", SDMsgUtils.getPropValueStr(appRegParam,"begintime"),SDMsgUtils.getPropValueStr(appRegParam,"endtime"));
		Map<String, Object>  codeDateslotSecMap =   DataBaseHelper.queryForMap("select pk_dateslotsec from bd_code_dateslot_sec where  replace(time_begin,':')=? and replace(time_end,':')=? ", SDMsgUtils.getPropValueStr(appRegParam,"begintime"), SDMsgUtils.getPropValueStr(appRegParam,"endtime"));
		//1.判断是否还有可约号
		if(schSch==null||"1".equals(schSch.getFlagStop())){
			throw new BusException("您所选排班不存在或已停用！");
		}
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue()){
			throw new BusException("您所选排班已挂满！");
		}
		//修改原判断预约号源逻辑，查询-后期待优化
		List<SchTicket> ticketList = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0' ",
				SchTicket.class, pkSch);
		if(ticketList.size()<=0){
			throw new BusException("您所选排班已无可预约号！");
		}
		SchTicket ticket = null;
		//2.占用号源
		if(haveTickNo){
			// 处理号表 //日期分组时段主键
			ticket = ticketPubService.getUnusedAppTicket(pkSch,SDMsgUtils.getPropValueStr(codeDateslotSecMap,"pkDateslotsec"),regvo.getEuPvtype());
		}else{
			//无号表方式处理排班表可用号数
			ticket = ticketPubService.getUnusedAppTicketFromSch(pkSch,regvo.getEuPvtype());
		}
		PiMasterRegVo piMasregvo= null;
		//3.保存预约挂号信息（含保存患者信息）
		// 3.1.生成预约记录，写表sch_appt； 3.2.生成预约挂号记录，写表sch_appt_pv；3.3.更新排班表，更新sch_sch；
		try{
			User u = new User();
			u.setPkOrg(schSch.getPkOrg());
			u.setPkEmp(Constant.PKZZJ);
			u.setNameEmp(Constant.NAMEZZJ);
			u.setPkDept(Constant.PKDEPT);
			UserContext.setUser(u);
			piMasregvo= regSyxService.saveApptSchRegInfo(regvo, schSch, ticket, haveTickNo, u,false);
		}catch(Exception e){
			//还原占用的预约号
			if(haveTickNo){
				ticketPubService.setTicketUnused(ticket);
			}else{
				ticketPubService.setTicketUnusedFromSch(ticket);
			}
			throw new BusException(e.getMessage());
		}
		return piMasregvo;
	}


	/******************************  预挂号支付********************************/
	/**
	 * 自助机预挂号支付(自费、医保)
	 * 1.解析消息获取字段值
	 * 2.处理挂号收费逻辑
	 * 3.拼接消息回传
	 * @param dft
	 * @return
	 * @throws ParseException
	 * @throws HL7Exception
	 */
	public String appRegistCardPiInfoPay(DFT_P03 dft,Message hapiMsg) throws ParseException, HL7Exception{
		Map<String, Object> paramMap = new HashMap<>(64);
		//默认支付类型
		paramMap.put("dtPaymode", "99");
		//操作类型，医保或者自费（CARDRF）
		String mesType = dft.getFINANCIAL(0).getFT1().getTransactionType().getValue();
		paramMap.put("mesType", mesType);
		//医保金额
		String amountInsu = "0";

		//先获取第三方支付数据，单边账处理   ! ! ! !
		BlExtPay saveBlExtPay = saveBlExtPay(paramMap,dft);
		//自费金额
		paramMap.put("amountPi", saveBlExtPay.getAmount());
		//消息发送方
		String sendApp = dft.getMSH().getSendingApplication().getNamespaceID().getValue();
		paramMap.put("sendApp", sendApp);
		paramMap.put("receive", sendApp);
		String oldMsgId =  dft.getMSH().getMessageControlID().getValue();
		paramMap.put("oldmsgid", oldMsgId);
		//EVN-2 消息时间
		String mesTime = dft.getEVN().getRecordedDateTime().getTimeOfAnEvent().getValue();
		paramMap.put("mesTime", mesTime);
		//EVN-5.1
		String codeEmp= dft.getEVN().getOperatorID(0).getIDNumber().getValue();
		if(CommonUtils.isNull(codeEmp)){
			throw new BusException("EVN-5.1未传入操作人");
		}
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		if(empMap==null){
			throw new BusException("【"+codeEmp+"】未查询到有效的操作人数据");
		}
		paramMap.put("pkEmp",SDMsgUtils.getPropValueStr(empMap,"pkEmp"));
		paramMap.put("codeEmp",codeEmp);
		paramMap.put("nameEmp",SDMsgUtils.getPropValueStr(empMap,"nameEmp"));
		//EVN-5.2	支付方式
		String payWay = dft.getEVN().getOperatorID(0).getFamilyName().getSurname().getValue();
		paramMap.put("payWay", payWay);
		//PID-2 患者编号
		String patiId = dft.getPID().getPatientID().getID().getValue();
		paramMap.put("patiId", patiId);
		//预约渠道，微信写1，自助机写2
		paramMap.put("addrPosition", "2");
		int size = dft.getPID().getPatientIdentifierListReps();
		//PID-3.1患者卡号
		//PID-3.2患者卡类型
		for (int i = 0; i < size; i++) {
			String id = dft.getPID().getPatientIdentifierList(i).getID().getValue();
			String idType = dft.getPID().getPatientIdentifierList(i).getIdentifierTypeCode().getValue();
			if ("SBpcNO".equals(idType)) {//医保编号
				if(StringUtils.isNotBlank(id))
					paramMap.put("SBpcNO", id);
			}
			if ("CodeIdentifyingTheCheckDigitSchemeEmployed".equals(idType)) {//社保卡状态
				if(StringUtils.isNotBlank(id))
					paramMap.put("AAZ515", id);
			}
		}
		//PID 36 医疗类别
		String aka130 = dft.getPID().getPid36_BreedCode().getCe4_AlternateIdentifier().getValue();
		aka130 = aka130==null||"".equals(aka130)?"11":aka130;
		paramMap.put("aka130", aka130);
		//PID 36-1 获取险种类型  insSzybVisitCity.setAae140("Aae140");
		paramMap.put("Aae140", dft.getPID().getPid36_BreedCode().getCe1_Identifier().getValue());
		//PID-38-1医保类型
		String insurTypeCode = dft.getPID().getProductionClassCode().getIdentifier().getValue();
		String insurTypeName = dft.getPID().getProductionClassCode().getCe2_Text().getValue();
		String insurTypeDtCode = dft.getPID().getProductionClassCode().getCe3_NameOfCodingSystem().getValue();
		//默认自费
		insurTypeCode = insurTypeDtCode==null || "".equals(insurTypeDtCode)?insurTypeCode:insurTypeDtCode;
		insurTypeCode = insurTypeCode==null||"".equals(insurTypeCode)||"1".equals(insurTypeCode)?"01":insurTypeCode;
		String sql = "select code,name,pk_hp from bd_hp where del_flag = '0' and code = ? ";
		//PID-38-3医保类型明细
		if("07".equals(insurTypeCode)){
			paramMap.put("isSur", "02");
		}
		Map<String , Object> insurMap = DataBaseHelper.queryForMap(sql, insurTypeCode);
		if(insurMap==null)
			throw new BusException("未查询到该("+insurTypeCode+")医保编号所对应的医保主建信息");
		paramMap.put("pkHp", insurMap.get("pkHp"));
		paramMap.put("name", insurMap.get("name"));
		paramMap.put("code", insurMap.get("code"));
		paramMap.put("insurTypeCode",insurTypeCode);
		paramMap.put("insurTypeName",insurTypeName);
		//PV1-19    预约号
		//String code = dft.getPV1().getVisitNumber().getIdentifierTypeCode().getValue();
		String code = dft.getPV1().getVisitNumber().getID().getValue();
		paramMap.put("code", code);
		//PV1-2 门诊住院标识  （根据类型判断患者类型）  9是急诊
		String sqlDept = "select srv.EU_SRVTYPE,appt.PK_DEPT_EX,dept.NAME_DEPT,dept.dt_medicaltype,dept.CODE_DEPT,dept.NOTE,srv.CODE,srv.NAME from sch_appt appt inner join sch_srv srv on appt.PK_SCHSRV=srv.PK_SCHSRV inner join BD_OU_DEPT dept on dept.PK_DEPT=appt.PK_DEPT_EX where appt.CODE=?";
		Map<String, Object> euPvtypeMap = DataBaseHelper.queryForMap(sqlDept, code);
		String euSrvtype = SDMsgUtils.getPropValueStr(euPvtypeMap, "euSrvtype");
		String euPvtype = "9".equals(euSrvtype)?"2":"1";
		if("30".equals(SDMsgUtils.getPropValueStr(euPvtypeMap, "dtMedicaltype"))){
			euPvtype = "4";
		}
		paramMap.put("euPvtype", euPvtype);
		paramMap.put("codeSrv",SDMsgUtils.getPropValueStr(euPvtypeMap, "code"));
		paramMap.put("nameSrv",SDMsgUtils.getPropValueStr(euPvtypeMap, "name"));
		paramMap.put("deptAddr",SDMsgUtils.getPropValueStr(euPvtypeMap, "note"));
		//FT1-2交易流水号
		String tranId = dft.getFINANCIAL(0).getFT1().getTransactionID().getValue();
		paramMap.put("tranId", tranId);
		// 时间
		String dateTrans = dft.getFINANCIAL(0).getFT1().getTransactionDate().getTimeOfAnEvent().getValue();
		paramMap.put("dateTrans", dateTrans);
		//FT1-11.3 总金额
		String amountSt = dft.getFINANCIAL(0).getFT1().getTransactionAmountExtended().getPrice().getQuantity().getValue();

		//获取医保数据
		if(dft.toString().contains("ZPY")){
			Map<String, List<Map<String, Object>>> resolueMessage = SDMsgUtils.resolueMessage(dft.toString());
			List<Map<String, Object>> list = resolueMessage.get("ZPY");
			paramMap.put("list", list);
		}
		//医保支付数据
		if(dft.toString().contains("ZPR") && !"CARDRF".equals(mesType)){
			String zpr [] =hapiMsg.toString().split("ZPR");
			String [] ele = zpr[1].split("\\|");
			//ZPR-1（待定） Yljgbm	医疗机构编码 （目前为大病类别：CKA303）
			paramMap.put("cka303", ele[1]);
			//ZPR-2（待定） Yljgmc	医疗机构名称
			//ZPR-3（预约号） Mglsh 门诊流水号
			paramMap.put("Mglsh", ele[3]);
			//ZPR-4 Djh	单据号 (门诊登记号)
			paramMap.put("Djh", ele[4]);
			//ZPR-5 Ylzh	医疗证号
			paramMap.put("Ylzh", ele[5]);
			//校验医疗证号是否为密文
			if(ele[5]==null||ele[5].length()<11){
				throw new BusException("未获取到正确的医疗证号，缴费失败，请到窗口重新缴费！");
			}
			//ZPR-6 Dnh	电脑号
			paramMap.put("computerNo", ele[6]);
			//ZPR-7	Xm	姓名
			//prampMap.put("Xm", ele[7]);
			paramMap.put("namexm", ele[7]);
			//ZPR-8 Je	金额
			paramMap.put("Je", ele[8]);
			paramMap.put("priceje", ele[8]);
			amountSt = StringUtils.isBlank(ele[8])?"0":ele[8];
			//ZPR-9 Xjhj	现金合计
			//prampMap.put("Xjhj", ele[9]); 自费金额
			paramMap.put("cashSelf", ele[9]);
			//ZPR-10 Jzhj	记账合计
			//prampMap.put("Jzhj", ele[10]);
			paramMap.put("cashInsur", ele[10]);
			//ZPR-11 Jsywh	结算业务号
			paramMap.put("Jsywh", ele[11]);
			//ZPR-12 Tchj	统筹合计
			paramMap.put("Tchj", ele[12]);
			//ZPR-13 ywh
			if(ele[13]!=null && ele[13].length()>20){
				paramMap.put("ywh",ele[13].substring(0, 20));
			}else{
				paramMap.put("ywh",ele[13]);
			}
			//医保支付金额 = 医保金额10+统筹金额12
			amountInsu = (new BigDecimal(StringUtils.isBlank(ele[10])?"0":ele[10]).add(new BigDecimal(StringUtils.isBlank(ele[12])?"0":ele[12]))).toString();

			//医保支付校验金额
			//if(Double.valueOf(amountSt)<=0){
			//	throw new BusException("消息传入金额与实际不符，总金额为："+amountSt+"元，医保金额为："+amountInsu+"元！请到人工窗口挂号！");
			//}
		}
		//paramMap.put("amountPi", amountPi);
		paramMap.put("amountInsu", amountInsu);
		paramMap.put("amountSt", amountSt);

		return disposeAppRegPayInfo(paramMap, saveBlExtPay);
	}

	/**
	 * 执行预约确认(已预约登记信息)
	 * 1.效验挂号信息
	 * 2.准备消息参数值
	 * 3.执行预约确认(已预约登记信息)修改相关信息
	 * 4.拼接消息
	 * @param blExtPay
	 * @return
	 * @throws ParseException
	 * @throws HL7Exception
	 */
	private String disposeAppRegPayInfo(Map<String, Object> paramMap,BlExtPay blExtPay) throws ParseException, HL7Exception{
		String msg = "";
		User user = new User();
		user.setPkEmp(SDMsgUtils.getPropValueStr(paramMap,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(paramMap,"nameEmp"));
		user.setCodeEmp(SDMsgUtils.getPropValueStr(paramMap,"codeEmp"));
		user.setPkOrg(Constant.PKORG);
		user.setPkDept(Constant.PKDEPT);//门诊收费处
		UserContext.setUser(user);
		//1.效验挂号信息
		Map<String, Object> apptMap = DataBaseHelper.queryForMap("select appt.*,case when to_char(appt.date_reg,'yyyyMMdd') = to_char(appt.begin_time,'yyyyMMdd') then '1' else '0' end as timePosition ,to_char(appt.begin_time,'yyyyMMddHH24miss') beginStr,apptpv.pk_emp_phy from  sch_appt appt left join sch_appt_pv apptpv on apptpv.pk_schappt = appt.pk_schappt   where appt.code = ?  and appt.del_flag = '0' and appt.eu_status = '0' ", SDMsgUtils.getPropValueStr(paramMap,"code"));
		if(apptMap==null){
			if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '0' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(paramMap,"code"))==1)
				throw new BusException("预约号("+SDMsgUtils.getPropValueStr(paramMap,"code")+")预约数据异常,请到窗口确认！费用将3-5天自动退回");
			if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '1' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(paramMap,"code"))==1)
				throw new BusException("预约号("+SDMsgUtils.getPropValueStr(paramMap,"code")+")已成功预约,已支付,请勿重复推送");
			throw new BusException("未查询到该预约号("+SDMsgUtils.getPropValueStr(paramMap,"code")+")信息");
		}
		//校验排班是否停止 和 发布
		if(DataBaseHelper.queryForScalar("select COUNT(1) from  SCH_SCH sch where sch.PK_SCH=? and (sch.FLAG_STOP='1' or sch.EU_STATUS in ('0','1') or sch.del_flag = '1') ", Integer.class,SDMsgUtils.getPropValueStr(apptMap,"pkSch"))!=0)
			throw new BusException("您所预约的医生已停诊，无法完成支付，请到窗口确认！");
		//2.准备消息参数值；
		Map<String, Object> appRegParam = new HashMap<>();
		appRegParam.put("pksch", SDMsgUtils.getPropValueStr(apptMap,"pkSch"));
		appRegParam.put("patientno", SDMsgUtils.getPropValueStr(paramMap,"patiId"));
		appRegParam.put("euPvtype", SDMsgUtils.getPropValueStr(paramMap,"euPvtype"));
		appRegParam.put("pkSchappt", SDMsgUtils.getPropValueStr(apptMap,"pkSchappt"));
		PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);
		regvo.setTicketNo(SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
		regvo.setPkEmp(SDMsgUtils.getPropValueStr(paramMap,"pkEmp"));
		regvo.setNameEmpReg(SDMsgUtils.getPropValueStr(paramMap,"nameEmp"));
		//3.执行预约确认(已预约登记信息)
		//3.1.基于预约信息生成就诊记录，写表pv_encounter；
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		//regvo.setPkOrg(SDMsgUtils.getPropValueStr(apptMap,"pkOrg"));
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> queryMap = new HashMap<String,Object>();
		queryMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = sDOpMsgMapper.querySchResInfo(queryMap);
		//获取收费信息
		regvo = getPiMaterRegItemPriceVo(regvo,paramMap);
		regvo.setPkHp(SDMsgUtils.getPropValueStr(paramMap,"pkHp"));
		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);

		try{
			PvEncounter pv = savePvEncounter(regvo, pkPv,schres,SDMsgUtils.getPropValueStr(apptMap,"pkEmpPhy"));
			//3.1.1 关联 预约就诊记录（预约登记时不生成pv_encounter，确认时才生成）
			DataBaseHelper.update("update SCH_APPT_PV set pk_pv=? where pk_schappt=?",new Object[]{pkPv,SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			regvo.setCodePv(pv.getCodePv());
			//急诊
			if("2".equals(SDMsgUtils.getPropValueStr(paramMap, "euPvtype"))){
				//regSyxService.savePvEr(regvo,pv);
				savePvEr(regvo,pv,schres);
				//门诊
			}else{
				regSyxService.savePvOp(regvo,pv,schres);
			}
			//保存就诊保险信息
			savePvInsurance(regvo, pv);
			//3.3.生成记费信息，写表bl_op_dt； 3.4.生成结算信息，写表bl_settle；3.5.生成结算明细，写表bl_settle_detail；3.6.生成支付记录，写表bl_deposit；
			//如果挂号打印发票：bl_ext_pay 3.7.生成发票信息，写表bl_invoice；
			//3.8.生成发票和结算关系，写表bl_st_inv； 3.9.生成发票明细，bl_invoice_dt；3.10.更新发票登记表，更新bl_emp_invoice。
			//throw new BusException();  SDMsgUtils.getPropValueStr(map, "amountInsu")
			regvo.setAmtInsuThird(new BigDecimal(CommonUtils.getString(paramMap.get("amountInsu"), "0")));
			regvo =  regSyxService.saveSettle(regvo, pv);
			//校验结算总金额和传入挂号总金额是否一致
			if(CommonUtils.isNotNull(regvo.getPkSettle())){
				String sqlSettle="select amount_st from bl_settle where pk_settle =?";
				Map<String,Object> settleMap=DataBaseHelper.queryForMap(sqlSettle, new Object[]{regvo.getPkSettle()});
				if(settleMap==null || settleMap.get("amountSt")==null){
					throw new BusException("HIS业务处理失败，未生成结算信息！");
				}
				BigDecimal amoutStinp=new BigDecimal(CommonUtils.getString(paramMap.get("amountSt"),"0"));
				BigDecimal amoutStHis=new BigDecimal(CommonUtils.getString(settleMap.get("amountSt"),"0"));
				if(amoutStHis.compareTo(amoutStinp)!=0){
					throw new BusException("HIS业务处理失败，HIS结算金额【"+amoutStHis+"】与支付金额【"+amoutStinp+"】不符!");
				}
			}
			//3.12.更新预约登记信息
			DataBaseHelper.update("update sch_appt set  eu_status='1',flag_pay='1' where pk_schappt = ?  ", new Object[]{SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			//更新第三方支付数据
			if(!"".equals(SDMsgUtils.getPropValueStr(paramMap,"SerialNo"))){
				updateBlExtPay(regvo,paramMap);
			}
			//3.13.生成医保记录  ins_szyb_st  ins_szyb_st_city  ins_szyb_st_citydt  ins_szyb_visit  ins_szyb_visit_city
			if(!"CARDRF".equals(SDMsgUtils.getPropValueStr(paramMap,"mesType")) ){
				saveInsSzybInfo(regvo,paramMap);
			}
			paramMap.put("ticketNo",  SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
			paramMap.put("beginStr",  SDMsgUtils.getPropValueStr(apptMap,"beginStr"));
			paramMap.put("codePv", regvo.getCodePv());

			//提交事务
			platformTransactionManager.commit(status);
		}catch (Exception e){
			//出现错误，事务回滚，记录日志
			platformTransactionManager.rollback(status);
			//消息返回错误信息
			log.info("挂号收费方法（disposeAppRegPayInfo）出现错误："+e.toString());
			throw e;
		}
		//调用挂号电子发票接口
		//修改为手动事物 , 关闭事务自动提交  开票失败不影响结算
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		try {
			Map<String, Object> invMap = electInvService.registrationElectInv(pkPv, user, regvo.getPkSettle());
			paramMap.putAll(invMap);
			platformTransactionManager.commit(status2);
		} catch (Exception e ) {
			e.printStackTrace();
			platformTransactionManager.rollback(status2);
			log.info("SDSysMsgSelfAppService.disposeAppRegPayInfo 生成电子发票失败！"+e.getClass()+e.getMessage());
			paramMap.put("result",e.getClass()+e.getMessage());
		}

		//4.拼接返回消息
		msg = createAppRegPayHL7Info(paramMap);

		//发送挂号消息
		Map<String,Object> msgParam =  new HashMap<>(32);
		msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		msgParam.put("pkPv", regvo.getPkPv());
		msgParam.put("isAdd", "0");
		msgParam.put("timePosition", SDMsgUtils.getPropValueStr(apptMap,"timeposition"));//0：预约     1：当天
		//预约渠道，微信写1，自助机写2
		msgParam.put("addrPosition", SDMsgUtils.getPropValueStr(paramMap,"addrPosition"));
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);

		return msg;
	}

	/**
	 * 保存就诊保险信息
	 * @param master
	 * @param pv
	 */
	private void savePvInsurance(PiMasterRegVo master,PvEncounter pv){
		if (CommonUtils.isNotNull(master.getPkHp())) {
			PvInsurance insu= new PvInsurance();
			insu.setPkPvhp(NHISUUID.getKeyId());
			insu.setPkOrg(UserContext.getUser().getPkOrg());
			insu.setCreator(UserContext.getUser().getPkEmp());
			insu.setCreateTime(new Date());
			insu.setModifier(UserContext.getUser().getPkEmp());
			insu.setTs(new Date());
			insu.setPkPv(pv.getPkPv());
			insu.setPkHp(master.getPkHp());
			DataBaseHelper.insertBean(insu);
		}
	}

	/**
	 * 保存就诊记录急诊属性
	 * @return
	 */
	private PvEr savePvEr(PiMasterRegVo master,PvEncounter pv,Map<String,Object> schres){
		// 保存急诊属性
		PvEr pvEr = new PvEr();
		pvEr.setPkPv(pv.getPkPv());
		pvEr.setPkSchsrv(master.getPkSchsrv());
		pvEr.setPkRes(master.getPkSchres());
		pvEr.setPkDateslot(master.getPkDateslot());
		pvEr.setPkDeptPv(master.getPkDept());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		//Map<String,Object> paramMap = new HashMap<String,Object>();
		//paramMap.put("pkSchres", master.getPkSchres());
		//Map<String,Object> schres = sDOpMsgMapper.querySchResInfo(paramMap);
		if(schres!=null){
			pvEr.setPkEmpPv(CommonUtils.getString(schres.get("pkEmp")));
			pvEr.setNameEmpPv(CommonUtils.getString(schres.get("nameEmp")));
		}
		pvEr.setTicketno(CommonUtils.isEmptyString(master.getTicketNo())?0:Long.parseLong(master.getTicketNo()));
		pvEr.setPkSch(master.getPkSch());

		// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
		// ( 参数-1) || '23:59:59'
		pvEr.setDateBegin(pv.getDateBegin());
		pvEr.setDateEnd(ApplicationUtils.getPvDateEndEr(pv.getDateBegin()));
		//pvEr.setDateEnd(DateUtils.strToDate(DateUtils.addDate(pv.getDateBegin(), 24, 4, "yyyyMMddHHmmss")));
		pvEr.setDateArv(new Date());
		DataBaseHelper.insertBean(pvEr);
		return pvEr;
	}

	/**
	 * 生成PiMasterRegVo实体值
	 * @param appRegParam   必须包含排班主建pksch, 身份证号identifyno或者患者编号  patientno
	 * @return
	 */
	public PiMasterRegVo getPiMasterRegVo(Map<String, Object> appRegParam){
		PiMasterRegVo piMaster = new PiMasterRegVo();
		String pkSch =  SDMsgUtils.getPropValueStr(appRegParam,"pksch");

		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?", SchSch.class, pkSch);

		if(schSch==null){
			throw new BusException("未查询到排班信息，请检查");
		}
		Map<String, Object> schTimeMap =  DataBaseHelper.queryForMap("select to_char(to_date(time_begin,'hh24:mi:ss'),'hh24miss') time_begin from bd_code_dateslot  where pk_dateslot = ? ",schSch.getPkDateslot());
		String dateReg = sdfDay.format(schSch.getDateWork())+SDMsgUtils.getPropValueStr(schTimeMap,"timeBegin");
		try {
			piMaster.setDateReg(sdf.parse(dateReg));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new BusException("排班时间保存异常，请联系管理员查看");
		}
		piMaster.setPkDept(schSch.getPkDept());
		Map<String , Object> srvMap =  DataBaseHelper.queryForMap("select pk_srv from  pv_pe where pk_sch = ? and del_flag = '0' ", pkSch);
		piMaster.setPkSrv(SDMsgUtils.getPropValueStr(srvMap,"pkSrv"));//挂号类型，诊疗服务
		piMaster.setPkSchres(schSch.getPkSchres());
		piMaster.setPkSchsrv(schSch.getPkSchsrv());
		piMaster.setPkSch(pkSch);
		piMaster.setPkSchplan(schSch.getPkSchplan());
		piMaster.setPkDateslot(schSch.getPkDateslot());
		piMaster.setPkAppt(SDMsgUtils.getPropValueStr(appRegParam, "pkSchappt"));
		piMaster.setEuPvtype(SDMsgUtils.getPropValueStr(appRegParam, "euPvtype"));
		String cashInsur = SDMsgUtils.getPropValueStr(appRegParam, "cashInsur");
		piMaster.setAmtInsuThird(BigDecimal.ZERO.add(new BigDecimal("".equals(cashInsur)?"0":cashInsur)));//医保支付金额
		//sch_srv.eu_srvtype
		Map<String , Object> srvtypeMap =  DataBaseHelper.queryForMap("select eu_srvtype from  sch_srv where pk_schsrv = ? and del_flag = '0'", schSch.getPkSchsrv());
		piMaster.setEuSrvtype(SDMsgUtils.getPropValueStr(srvtypeMap,"euSrvtype"));
		piMaster.setEuSchclass(schSch.getEuSchclass());
		// outsideOrderId;外部预约系统订单号
		piMaster.setOutsideOrderId(SDMsgUtils.getPropValueStr(appRegParam,"outsideOrderId"));
		// orderSource;外部预约来源
		piMaster.setOrderSource(SDMsgUtils.getPropValueStr(appRegParam,"orderSource"));
		//患者基本信息   暂定先按身份证号查询编写
		String patientno = SDMsgUtils.getPropValueStr(appRegParam,"patientno");
		String identifyno = SDMsgUtils.getPropValueStr(appRegParam,"identifyno");
		PiMaster pi = null;
		//if(!CommonUtils.isEmptyString(identifyno)){//根据身份证查询患者信息
		//pi = DataBaseHelper.queryForBean("Select * from pi_master where  id_no = ?  and del_flag = '0'",PiMaster.class, identifyno);
		//}else
		if (!CommonUtils.isEmptyString(patientno)) {//根据患者编号查询患者信息
			pi = DataBaseHelper.queryForBean("Select * from pi_master where  code_pi = ? and del_flag = '0'",PiMaster.class,patientno);
		}
		if(pi==null){
			throw new BusException("未查询到该患者的个人信息，患者编码："+patientno);
		}
		piMaster.setNamePi(pi.getNamePi());
		piMaster.setPkPi(pi.getPkPi());
		piMaster.setPkOrg(StringUtils.isBlank(pi.getPkOrg())?schSch.getPkOrg():pi.getPkOrg());
		piMaster.setCodePi(pi.getCodePi());
		piMaster.setCodeOp(pi.getCodeOp());
		piMaster.setCodeIp(pi.getCodeIp());
		piMaster.setMobile(pi.getMobile());
		piMaster.setAddress(pi.getAddress());
		piMaster.setDtSex(pi.getDtSex());
		piMaster.setBirthDate(pi.getBirthDate());
		piMaster.setTelNo(pi.getTelNo());
		piMaster.setTelWork(pi.getTelWork());
		piMaster.setTelRel(pi.getTelRel());
		piMaster.setPkPicate(pi.getPkPicate());
		piMaster.setIdNo(pi.getIdNo());
		piMaster.setDtIdtype(pi.getDtIdtype());
		piMaster.setInsurNo(pi.getInsurNo());
		piMaster.setAgePv(ApplicationUtils.getAgeFormat(pi.getBirthDate(),null));
		return piMaster;
	}

	/**
	 * 多系统接口公用（微信自助机体检公用）
	 * 生成PiMasterRegVo中的计费实体消息值   必传pk_sch
	 * @param piMaster
	 * @param paramMap
	 * @return
	 */
	public PiMasterRegVo getPiMaterRegItemPriceVo(PiMasterRegVo piMaster,Map<String, Object> paramMap){
		//挂号记费项目参数
		String sqlPi = "select pi.pk_pi,pi.name_pi,pi.pk_org,pi.pk_picate,appt.pk_schsrv,piCate.flag_Spec,to_char(pi.birth_date,'yyyy-MM-dd') date_birth   from 	sch_appt appt left join  pi_master pi on pi.pk_pi = appt.pk_pi  left join  	pi_cate  piCate on piCate.Pk_Picate = pi.pk_picate  where appt.code = ?  and pi.del_flag = '0'";
		Map<String, Object>  bdPiMap = DataBaseHelper.queryForMap(sqlPi, SDMsgUtils.getPropValueStr(paramMap,"code"));
		Map<String, Object> insurParam = new HashMap<>();
		insurParam.put("dateBirth", SDMsgUtils.getPropValueStr(bdPiMap,"dateBirth"));
		insurParam.put("flagSpec", SDMsgUtils.getPropValueStr(bdPiMap,"flagSpec"));
		insurParam.put("pkPicate", SDMsgUtils.getPropValueStr(bdPiMap,"pkPicate"));
		insurParam.put("pkSchsrv", SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"));
		insurParam.put("pkInsu", SDMsgUtils.getPropValueStr(paramMap,"pkHp"));
		insurParam.put("nameInsu",SDMsgUtils.getPropValueStr(paramMap,"name"));
		insurParam.put("euPvType",piMaster.getEuPvtype());
		insurParam.put("pkEmp",piMaster.getPkPi());
		String param = JsonUtil.writeValueAsString(insurParam);
		User user = new User();
		user.setPkEmp(SDMsgUtils.getPropValueStr(bdPiMap,"pkPi"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(bdPiMap,"namePi"));
		user.setPkOrg(SDMsgUtils.getPropValueStr(bdPiMap,"pkOrg"));
		List<ItemPriceVo> listItem  = regSyxService.getItemBySrv(param,user);
		piMaster.setItemList(listItem);
		//throw new Exception();
		List<BlDeposit> blDepositList = new ArrayList<>();
		BlDeposit blDeposit = new BlDeposit();
		//金额处理
		//自费金额
		//String cashSelf = "".equals(SDMsgUtils.getPropValueStr(map, "cashSelf"))?"0":SDMsgUtils.getPropValueStr(map, "cashSelf");
		//医保金额
		//String cashInsur = "".equals(SDMsgUtils.getPropValueStr(map, "cashInsur"))?"0":SDMsgUtils.getPropValueStr(map, "cashInsur");
		//piMaster.setAmtInsuThird(new BigDecimal(cashInsur));
		//自费总金额  = 总金额  // 医保总金额  = 自费总金额+医保总金额
		String amountPi = SDMsgUtils.getPropValueStr(paramMap, "amountPi");
		amountPi = "".equals(amountPi)?"0":amountPi;
		//总金额
		BigDecimal price = new BigDecimal(StringUtils.isBlank(amountPi)?"0":amountPi);
		blDeposit.setAmount(price);
		blDeposit.setDtPaymode("99");//默认99
		blDeposit.setPkOrg(user.getPkOrg());
		//String  payway = SDMsgUtils.getPropValueStr(map,"payWay");
		//支付方式
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap,"SerialNo"))){
			blDeposit.setDtPaymode("7");
			//第三方支付金额
			blDeposit.setExtAmount(price);
			blDeposit.setPayInfo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//系统产生系统订单号
		}
		blDepositList.add(blDeposit);
		piMaster.setDepositList(blDepositList);
		piMaster.setInvStatus("-2");
		return piMaster;
	}

	/**
	 * 保存就诊记录
	 * @param pkEmpPhy
	 * @param pkPv
	 * @return
	 */
	public PvEncounter savePvEncounter(PiMasterRegVo piMasterRegVo,String pkPv,Map<String,Object> schres,String pkEmpPhy){
		//是否允许挂号
		String date = piMasterRegVo.getDateAppt()!=null?DateUtils.formatDate(piMasterRegVo.getDateAppt(), "yyyyMMdd"):
				(piMasterRegVo.getDateReg()!=null?DateUtils.formatDate(piMasterRegVo.getDateReg(), "yyyyMMdd"):null);
		if(StringUtils.isNotBlank(date)) {
			if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.pk_emp_phy=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{piMasterRegVo.getPkPi(),date,pkEmpPhy}) >0){
				throw new BusException("该患者已经存在当前日期和当前医生的挂号记录！");
			}
		}
		// 保存就诊记录
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		pvEncounter.setPkPi(piMasterRegVo.getPkPi());
		pvEncounter.setPkDept(piMasterRegVo.getPkDept());//就诊科室
		pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
		pvEncounter.setEuPvtype(piMasterRegVo.getEuPvtype());
		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
		pvEncounter.setNamePi(piMasterRegVo.getNamePi());
		pvEncounter.setDtSex(piMasterRegVo.getDtSex());
		pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(piMasterRegVo.getBirthDate(),null));
		pvEncounter.setAddress(piMasterRegVo.getAddress());
		pvEncounter.setFlagIn("0");
		pvEncounter.setFlagSettle("1");
		pvEncounter.setDtMarry(piMasterRegVo.getDtMarry());
		pvEncounter.setPkInsu(piMasterRegVo.getPkHp());
		pvEncounter.setPkPicate(piMasterRegVo.getPkPicate());
		pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
		pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
		pvEncounter.setDateReg(new Date());
		//如果是预约挂号，开始日期为预约日期
		if("2".equalsIgnoreCase(piMasterRegVo.getEuPvtype())){
			pvEncounter.setDateBegin(new Date());
		}else{
			if(piMasterRegVo.getDateAppt()!=null) {
				//预约开始时间应该为日期+预约号所在的开始时间（bug-20723）
				String sql = "select time_begin from bd_code_dateslot where pk_dateslot = ?";
				Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, piMasterRegVo.getPkDateslot());
				String dateStr = DateUtils.dateToStr("yyyyMMdd", piMasterRegVo.getDateAppt());
				if (DateUtils.dateToStr("yyyyMMdd", new Date()).equals(dateStr)) {//如果预约日期为当前日期，开始时间为挂号时间
					pvEncounter.setDateBegin(piMasterRegVo.getDateReg());
				} else {
					String dateAppt = dateStr + " " + queryForMap.get("timeBegin").toString();
					piMasterRegVo.setDateAppt(DateUtils.strToDate(dateAppt, "yyyyMMdd HH:mm:ss"));
					pvEncounter.setDateBegin(piMasterRegVo.getDateAppt());
				}
			}else{
				pvEncounter.setDateBegin(piMasterRegVo.getDateReg());//挂号的排班日期
			}
		}
		//只保存pv_op表
		pvEncounter.setFlagCancel("0");
		pvEncounter.setDtIdtypeRel("01");
		pvEncounter.setDtPvsource(piMasterRegVo.getDtSource());
		pvEncounter.setNameRel(piMasterRegVo.getNameRel());
		pvEncounter.setIdnoRel(piMasterRegVo.getIdnoRel());
		pvEncounter.setTelRel(piMasterRegVo.getTelRel());
		pvEncounter.setEuPvmode("0");
		//判断是否特诊
		boolean isSpec = false;
		if(StringUtils.isNotBlank(piMasterRegVo.getPkSrv())){
			SchSrv srv = DataBaseHelper.queryForBean("select eu_srvtype from sch_srv where pk_schsrv=?", SchSrv.class, piMasterRegVo.getPkSrv());
			isSpec = srv!=null && "2".equals(srv.getEuSrvtype());
		}
		pvEncounter.setFlagSpec(isSpec?"1":"0");
		pvEncounter.setEuStatusFp("0");
		pvEncounter.setEuLocked("0");
		pvEncounter.setEuDisetype("0");
		pvEncounter.setTs(new Date());
		long millis = System.currentTimeMillis();
		
		String pkDeptArea = pvInfoPubService.getPkDeptArea(piMasterRegVo.getPkSchres());
   		pvEncounter.setPkDeptArea(pkDeptArea);
		
		DataBaseHelper.insertBean(pvEncounter);
		log.info(Long.toString(millis)+"~~~自助机插入Pv_Encounter耗时~~~~~"+(System.currentTimeMillis() - millis));
		//regSyxService.addInsGzgyPv(piMasterRegVo, pvEncounter);

		return pvEncounter;
	}

	/**
	 * 多系统接口公用（微信自助机体检公用）
	 * 保存第三方支付数据
	 * @param dft
	 * @param paramMap
	 * @throws ParseException
	 */
	private BlExtPay saveBlExtPay(Map<String, Object> paramMap,DFT_P03 dft) throws ParseException{
		String amountPi = "0";
		//获取第三方支付数据(自费)
		if(dft.toString().contains("ZCT")){
			String[] zct =dft.toString().split("ZCT");
			String[] zctValue = zct[1].split("\\|");
			//ZCT 3  MerchantNo  商户号
			paramMap.put("shh",zctValue[3]);
			//ZCT 4 终端号 （必须）
			paramMap.put("zdh",zctValue[4]);
			//ZCT 8 订单号  SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			paramMap.put("ddh",zctValue[8]);
			//ZCT 9 自费金额
			//prampMap.put("zfje",zctValue[9]);
			amountPi = StringUtils.isBlank(zctValue[9])?"0":zctValue[9];
			amountPi = new BigDecimal(amountPi).divide(new BigDecimal(100)).toString();
			paramMap.put("amountPi", amountPi);
			//ZCT 10  流水号
			paramMap.put("lsh",zctValue[10]);
			//ZCT 11 票据号
			paramMap.put("pjh",zctValue[11]);
			//ZCT 11 授权号
			paramMap.put("sqh",zctValue[12]);
			//ZCT 13 参考号
			paramMap.put("ckh",zctValue[13]);
			//ZCT 14 交易发起日期 yyyyMMdd
			paramMap.put("jyrq",zctValue[14]);
			//ZCT 15 HHmmssSSS(时分秒毫秒)
			paramMap.put("jysj",zctValue[15]);
			//ZCT 系统订单号   TradeNo  系统订单号(微信退货交易时使用此域)
			//paramMap.put("xtddh",zctValue[25].trim());
			//TradeNo  系统订单号(微信退货交易时使用此域)
			paramMap.put("TradeNo",zctValue.length>24?zctValue[25].trim():"");
			//SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			paramMap.put("SerialNo",zctValue[8]);
			//Sysname  终端号
			paramMap.put("Sysname",zctValue[4]);
			//ResultPay 商户号#参考号 104480880621012#202135765632
			paramMap.put("ResultPay",zctValue[3]+"#"+zctValue[13]);
			// DescPay	流水号#票据号#授权号#日期  000890#000890##0731160650
			String monthDay = (zctValue[14].length()>4)?zctValue[14].substring(4):"";
			paramMap.put("DescPay",zctValue[10]+"#"+zctValue[11]+"#"+zctValue[12]+"#"+monthDay+zctValue[15]);
		}
		BlExtPay blExtPay = new BlExtPay();
		blExtPay.setAmount(BigDecimal.ZERO);
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"))){
			blExtPay.setPkExtpay(NHISUUID.getKeyId());
			blExtPay.setPkPi("0000000");//预存固定值
			blExtPay.setAmount(new BigDecimal(amountPi));
			blExtPay.setEuPaytype("7");
			blExtPay.setFlagPay("1");//支付标志
			blExtPay.setSerialNo(SDMsgUtils.getPropValueStr(paramMap,"SerialNo"));//订单号
			blExtPay.setTradeNo(SDMsgUtils.getPropValueStr(paramMap,"TradeNo"));//系统订单号
			blExtPay.setSysname(SDMsgUtils.getPropValueStr(paramMap,"Sysname"));//终端号
			blExtPay.setDescPay(SDMsgUtils.getPropValueStr(paramMap, "DescPay"));
			blExtPay.setResultPay(SDMsgUtils.getPropValueStr(paramMap, "ResultPay"));
			//如果系统订单号是空，说明银行卡支付
			if("".equals(SDMsgUtils.getPropValueStr(paramMap,"TradeNo"))){
				//订单号 = 卡号#流水号#日期#时间
				blExtPay.setSerialNo(SDMsgUtils.getPropValueStr(paramMap,"SerialNo")+"#"+SDMsgUtils.getPropValueStr(paramMap,"lsh")+"#"+SDMsgUtils.getPropValueStr(paramMap,"jyrq")+SDMsgUtils.getPropValueStr(paramMap,"jysj"));
				paramMap.put("SerialNo",blExtPay.getSerialNo());
			}
			blExtPay.setCreator(Constant.PKZZJ);
			blExtPay.setDatePay(new Date());
			blExtPay.setCreateTime(new Date());
			blExtPay.setTs(new Date());
			blExtPay.setModifier("");
			blExtPay.setDelFlag("0");
			//修改为手动事物 , 关闭事务自动提交
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = platformTransactionManager.getTransaction(def);
			DataBaseHelper.insertBean(blExtPay);
			platformTransactionManager.commit(status);
			paramMap.put("dtPaymode", "7");
		}
		return blExtPay;
	}
	/**
	 * 挂号收费 更新第三方收费表
	 * @param regvo
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	private BlExtPay updateBlExtPay(PiMasterRegVo regvo,Map<String, Object> map) throws ParseException{
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where del_flag = '0' and SERIAL_NO = ?", BlExtPay.class, SDMsgUtils.getPropValueStr(map,"SerialNo"));
		User user = UserContext.getUser();
		blExtPay.setPkOrg(user.getPkOrg());
		String dateTrans = SDMsgUtils.getPropValueStr(map,"dateTrans");
		Date date = "".equals(dateTrans)?new Date():sdf.parse(dateTrans);
		blExtPay.setDateAp(date);//请求时间
		blExtPay.setDatePay(date);//支付时间
		blExtPay.setCreator(user.getPkEmp());
		blExtPay.setPkPi(regvo.getPkPi());
		blExtPay.setPkPv(regvo.getPkPv());
		blExtPay.setPkSettle(regvo.getPkSettle());
		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
		Map<String,Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{regvo.getPkSettle()});
		blExtPay.setPkDepo(SDMsgUtils.getPropValueStr(depoMap, "pkDepo"));
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}

	/**
	 * 结算收费 更新第三方收费表
	 * @param opCgforVo
	 * @param map
	 * @return
	 * @throws ParseException
	 */
	private BlExtPay updateBlExtPay(OpCgTransforVo opCgforVo,Map<String, Object> map) throws ParseException{
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where del_flag = '0' and SERIAL_NO = ?", BlExtPay.class, SDMsgUtils.getPropValueStr(map,"SerialNo"));
		User user = UserContext.getUser();
		blExtPay.setPkOrg(user.getPkOrg());
		String dateTrans = SDMsgUtils.getPropValueStr(map,"dateTrans");
		Date date = "".equals(dateTrans)?new Date():sdf.parse(dateTrans);
		blExtPay.setDateAp(date);//请求时间
		blExtPay.setDatePay(date);//支付时间
		blExtPay.setCreator(user.getPkEmp());
		blExtPay.setPkPi(opCgforVo.getPkPi());
		blExtPay.setPkPv(opCgforVo.getPkPv());
		blExtPay.setPkSettle(opCgforVo.getPkSettle());
		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
		Map<String,Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
		blExtPay.setPkDepo(SDMsgUtils.getPropValueStr(depoMap, "pkDepo"));
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}

	/**
	 * 多系统接口公用（微信自助机体检公用）
	 * 生成医保记录
	 * ins_szyb_st
	 * ins_szyb_st_city
	 * ins_szyb_st_citydt
	 * ins_szyb_visit
	 * ins_szyb_visit_city
	 * @param regvo
	 * @param map
	 */
	private void saveInsSzybInfo(PiMasterRegVo regvo, Map<String,Object> map){
		User user = UserContext.getUser();
		//String pkCurDept = user.getPkDept();// 当前科室
		//String pkOpDoctor = user.getPkEmp();// 当前用户主键
		//String nameUser = user.getNameEmp();// 当前用户名

		InsSzybVisit insSzybVisit = new InsSzybVisit();
		String pkVisit = NHISUUID.getKeyId();
		insSzybVisit.setPkVisit(pkVisit);
		insSzybVisit.setPkOrg(user.getPkOrg());
		insSzybVisit.setPkHp(regvo.getPkHp());
		insSzybVisit.setPkPv(regvo.getPkPv());
		insSzybVisit.setPkPi(regvo.getPkPi());
		insSzybVisit.setCodeCenter("");
		insSzybVisit.setCodeOrg(Constant.CODEORG);
		insSzybVisit.setNameOrg(Constant.NAMEORG);
		insSzybVisit.setTransid(SDMsgUtils.getPropValueStr(map,"tranId"));//交易流水号
		//zpr-4
		insSzybVisit.setCodeInsst(SDMsgUtils.getPropValueStr(map,"Jsywh"));
		//zpr-3  医保登记号
		insSzybVisit.setPvcodeIns(SDMsgUtils.getPropValueStr(map,"Djh"));
		insSzybVisit.setNamePi(regvo.getNamePi());
		insSzybVisit.setPersontype(SDMsgUtils.getPropValueStr(map,"aka130"));//医疗类别(默认普通门诊)  SDMsgUtils.getPropValueStr(map,"code")
		insSzybVisit.setEuResctype("");//救助对象类型	0 非医疗救助类别；1；低保对象；2 特困供养人员；3 孤儿；4 建档立卡贫困人员；5 低收入家庭的老年人；6 低收入家庭的未成年人；7 低收入家庭的重度残疾人；8 低收入家庭的重病患者；9 其他
		insSzybVisit.setBirthDate(regvo.getBirthDate());
		insSzybVisit.setIdno(regvo.getIdNo());
		insSzybVisit.setCodeAreayd("440300");//深圳市行政区划代码
		insSzybVisit.setDtExthp("03");//深圳市医保
		insSzybVisit.setDateReg(new Date());
		insSzybVisit.setDtSttypeIns("");//结算类型
		insSzybVisit.setEuStatusSt("0");//门诊：0结算中（挂号登记开始） 1结算完成（挂号登记完成）
		insSzybVisit.setCodeMedino(SDMsgUtils.getPropValueStr(map,"Ylzh"));//医疗证号加密串
		log.info("自助机医疗证号："+SDMsgUtils.getPropValueStr(map,"Ylzh")+"保存的值："+insSzybVisit.getCodeMedino()+"电脑号："+SDMsgUtils.getPropValueStr(map,"computerNo"));
		insSzybVisit.setCreator(user.getPkEmp());
		insSzybVisit.setPassword("000000");// TODO
		insSzybVisit.setCreateTime(new Date());
		insSzybVisit.setTs(new Date());
		insSzybVisit.setDelFlag("0");
		DataBaseHelper.insertBean(insSzybVisit);

		InsSzybSt insSzybSt = new InsSzybSt();
		String pkInsst = NHISUUID.getKeyId();
		insSzybSt.setPkInsst(pkInsst);
		insSzybSt.setPkVisit(pkVisit);
		insSzybSt.setPkOrg(user.getPkOrg());
		insSzybSt.setCodeHpst(SDMsgUtils.getPropValueStr(map,"ywh").trim());
		insSzybSt.setPkHp(regvo.getPkHp());
		insSzybSt.setPkPv(regvo.getPkPv());
		insSzybSt.setPkPi(regvo.getPkPi());
		insSzybSt.setPkSettle(regvo.getPkSettle());
		insSzybSt.setPvcodeIns(SDMsgUtils.getPropValueStr(map,"Djh"));
		insSzybSt.setDateSt(new Date());
		insSzybSt.setCodeSerialno(SDMsgUtils.getPropValueStr(map, "Jsywh"));
		//insSzybSt.setAmount(Double.valueOf((String) (SDMsgUtils.getPropValueStr(map,"cashInsur")==""?0:SDMsgUtils.getPropValueStr(map,"cashInsur"))));
		insSzybSt.setAmount(Double.valueOf(StringUtils.isBlank(SDMsgUtils.getPropValueStr(map,"priceje"))?"0":SDMsgUtils.getPropValueStr(map,"priceje")));
		insSzybSt.setCodeCenter("");
		insSzybSt.setCodeOrg(Constant.CODEORG);
		insSzybSt.setNameOrg(Constant.CODEORG);
		insSzybSt.setCodeSerialno(SDMsgUtils.getPropValueStr(map,"Jsywh"));
		insSzybSt.setCreator(user.getPkEmp());
		insSzybSt.setCreateTime(new Date());
		insSzybSt.setTs(new Date());
		insSzybSt.setDelFlag("0");
		DataBaseHelper.insertBean(insSzybSt);

		InsSzybStCity insSzybStCity = new InsSzybStCity();
		String pkInsstCity = NHISUUID.getKeyId();
		insSzybStCity.setPkInsstcity(pkInsstCity);
		insSzybStCity.setPkInsst(pkInsst);
		insSzybStCity.setPkOrg(user.getPkOrg());
		insSzybStCity.setDtMedicate(SDMsgUtils.getPropValueStr(map,"AkA130Code"));//	医疗类别
		insSzybStCity.setAmtJjzf(Double.valueOf((String) (SDMsgUtils.getPropValueStr(map,"Tchj")==""?0:SDMsgUtils.getPropValueStr(map,"Tchj"))));//基金支付金额
		insSzybStCity.setAmtGrzhzf(Double.valueOf((String) (SDMsgUtils.getPropValueStr(map,"cashInsur")==""?0:SDMsgUtils.getPropValueStr(map,"cashInsur"))));//个人帐户支付金额
		insSzybStCity.setAmtGrzf(Double.valueOf((String) (SDMsgUtils.getPropValueStr(map,"cashSelf")==""?0:SDMsgUtils.getPropValueStr(map,"cashSelf"))));//个人支付金额
		//insSzybStCity.setAmtGrzh(0.0);//个人账户金额
		insSzybStCity.setCreator(user.getPkEmp());
		insSzybStCity.setCreateTime(new Date());
		insSzybStCity.setTs(new Date());
		insSzybStCity.setDelFlag("0");
		DataBaseHelper.insertBean(insSzybStCity);

		List<InsSzybStCitydt> InsSzybStCitydtList = new ArrayList<InsSzybStCitydt>();
		List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("list");
		for(Map<String,Object> m : mapList){
			InsSzybStCitydt insSzybStCitydt = new InsSzybStCitydt();
			insSzybStCitydt.setTypeOutput(SDMsgUtils.getPropValueStr(m, "1"));//结算返回信息类 --平台未返回数据
			insSzybStCitydt.setCategory(SDMsgUtils.getPropValueStr(m, "1"));//类别代码
			String amt = SDMsgUtils.getPropValueStr(m, "2");
			amt = "".equals(amt)?"0":amt;
			insSzybStCitydt.setAmtFee(Double.valueOf(amt));//费用金额
			insSzybStCitydt.setPkInsstcitydt(NHISUUID.getKeyId());
			insSzybStCitydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
			insSzybStCitydt.setCreator(user.getPkEmp());
			insSzybStCitydt.setCreateTime(new Date());
			insSzybStCitydt.setDelFlag("0");
			insSzybStCitydt.setTs(new Date());
			InsSzybStCitydtList.add(insSzybStCitydt);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class),InsSzybStCitydtList);

		InsSzybVisitCity  insSzybVisitCity = new InsSzybVisitCity();
		String pkVisitcity = NHISUUID.getKeyId();
		insSzybVisitCity.setPkVisitcity(pkVisitcity);
		insSzybVisitCity.setPkOrg(user.getPkOrg());
		insSzybVisitCity.setPkVisit(pkVisit);
		insSzybVisitCity.setPkPv(regvo.getPkPv());
		insSzybVisitCity.setAaz515(SDMsgUtils.getPropValueStr(map,"AAZ515"));//社保卡状态
		insSzybVisitCity.setAaz500(SDMsgUtils.getPropValueStr(map,"SBpcNO"));//社保卡号
		insSzybVisitCity.setAac001(1l);//人员ID
		insSzybVisitCity.setAac999(SDMsgUtils.getPropValueStr(map,"computerNo"));//人员电脑号
		insSzybVisitCity.setCka303(SDMsgUtils.getPropValueStr(map,"cka303"));//大病类别
		insSzybVisitCity.setAae140(SDMsgUtils.getPropValueStr(map,"Aae140"));//险种类型
		String idType = regvo.getDtIdtype();
		String aac058 = "";
		switch (idType) {
			case "01":aac058 = "01";break;
			case "02":aac058 = "02";break;
			case "03":aac058 = "04";break;
			case "06":aac058 = "04";break;
			case "07":aac058 = "05";break;
			case "08":aac058 = "06";break;
			case "09":aac058 = "07";break;
			case "10": aac058 = "08";break;
			case "99":aac058 = "99";break;
			default:break;
		}
		insSzybVisitCity.setAac058(aac058);
		insSzybVisitCity.setAac147(regvo.getIdNo());
		insSzybVisitCity.setAac002(regvo.getIdNo());//社会保障号码
		insSzybVisitCity.setAac003(regvo.getNamePi());
		String dtSex = regvo.getDtSex();
		String aac004 = "";
		switch (dtSex) {
			case "02":aac004 = "1";break;
			case "03":aac004 = "2";break;
			default:aac004 = "9";break;
		}
		insSzybVisitCity.setAac004(aac004);
		if(regvo.getBirthDate()!=null)
			insSzybVisitCity.setAac006(Integer.valueOf(sdfDay.format(regvo.getBirthDate())));
		//年纪处理
		String agePv = regvo.getAgePv();
		try{
			insSzybVisitCity.setBae093(Integer.valueOf(agePv.substring(0, agePv.indexOf("岁"))));
		}catch (Exception e){
			insSzybVisitCity.setBae093(1);
			e.printStackTrace();
			log.info("saveInsSzybInfo方法处理年龄异常！"+agePv);
		}
		insSzybVisitCity.setCac215(regvo.getAddrOrigin());
		insSzybVisitCity.setCreator(user.getPkEmp());
		insSzybVisitCity.setCreateTime(new Date());
		insSzybVisitCity.setTs(new Date());
		insSzybVisitCity.setDelFlag("0");
		DataBaseHelper.insertBean(insSzybVisitCity);

	}

	/**
	 * 创建预约执行确认ACK_P03的消息
	 * @param map
	 * @return
	 * @throws HL7Exception
	 */
	public String createAppRegPayHL7Info(Map<String, Object> map) throws HL7Exception{
		ACK_P03  ackp03 = new ACK_P03();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = ackp03.getMSH();
		MSA msa = ackp03.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
		msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map, "sendApp"));
		msa.getAcknowledgementCode().setValue("AA");
		msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(map,"oldmsgid"));
		msa.getTextMessage().setValue("成功");
		msa.getExpectedSequenceNumber().setValue("100");
		msa.getDelayedAcknowledgmentType().setValue("F");
		msa.getMsa6_ErrorCondition().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(map,"result"));

		NTE nte0 = ackp03.getNTE(0);
		nte0.getComment(0).setValue("0");//挂号前余额
		nte0.getCommentType().getText().setValue("Qye");
		NTE nte1 = ackp03.getNTE(1);
		nte1.getComment(0).setValue("0");//挂号后余额
		nte1.getCommentType().getText().setValue("Hye");
		NTE nte2 = ackp03.getNTE(2);
		nte2.getComment(0).setValue("");//发票号
		nte2.getCommentType().getText().setValue("Fph");

		//患者编号
		ackp03.getPID().getPatientID().getID().setValue(SDMsgUtils.getPropValueStr(map,"patiId"));
		String ticketNo = SDMsgUtils.getPropValueStr(map,"ticketNo");
		String beginStr = SDMsgUtils.getPropValueStr(map,"beginStr");
		StringBuffer timeNo = new StringBuffer();
		if (StringUtils.isNotBlank(beginStr)) {
			timeNo.append(beginStr.substring(10, 14));
		}else{
			timeNo.append("0000");
		}
		if(ticketNo.length()==1){
			timeNo.append("0").append(ticketNo);
		}else{
			timeNo.append(ticketNo);
		}
		//pv1-3.7 位置：挂号缴费提供科室位置信息 / “特需专家”“特需著名专家”时，指引地址设置为：四楼特需门诊
		String codeSrv = SDMsgUtils.getPropValueStr(map,"codeSrv");
		String addr = SDMsgUtils.getPropValueStr(map,"deptAddr");
		if("27".equals(codeSrv)||"28".equals(codeSrv)||"特需".equals(SDMsgUtils.getPropValueStr(map,"nameSrv"))){
			addr = "门诊四楼特需门诊";
		}
		ackp03.getPV1().getPv13_AssignedPatientLocation().getPl7_Building().setValue(addr);
		//PV1-3.9 当前时间就诊的顺序号
		ackp03.getPV1().getAssignedPatientLocation().getLocationDescription().setValue(timeNo.toString());
		//PV1-5  预约号
		ackp03.getPV1().getPreadmitNumber().getID().setValue(SDMsgUtils.getPropValueStr(map,"code"));
		//PV1-19 就诊流水号
		ackp03.getPV1().getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));

		//获取电子发票数据
		Map<String, Object> blInvoiceMsg = ElectInvService.getBlInvoiceMsg(map);
		if(blInvoiceMsg !=null && !blInvoiceMsg.containsKey("error")){
			//电子发票数据
			//EbillCode	电子票据代码 ebillbatchcode 电子票据代码
			ackp03.getZPO().getEbillCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillCode"));
			//EbillNo	电子票据号码 ebillno 电子票据号码
			ackp03.getZPO().getEbillNo().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillNo"));
			//CheckCode	电子校验码 checkcode 电子校验码
			ackp03.getZPO().getCheckCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"checkCode"));
			//EbillDate	电子票据生成时间 dateEbill 电子票据生成时间
			ackp03.getZPO().getEbillDate().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillDate"));
			//EbillQRCode	电子票据二维码效验数据 qrcode_ebill 电子票据二维码效验数据(byte[])
			ackp03.getZPO().getEbillQRCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillQRCode"));
		}else{
			msa.getMsa6_ErrorCondition().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"result"));
		}

		msg =  SDMsgUtils.getParser().encode(ackp03);
		return msg;
	}






	/******************************诊中支付 （自费）*************************************/



	/**
	 * 自助机门诊诊间支付方法-自费
	 * @param dft
	 * @return
	 * @throws ParseException
	 */
	public String receiveCardSettle(DFT_P03 dft)throws HL7Exception, ParseException  {
		String msg="";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> resAckMap=new HashMap<String,Object>();
		//默认扫码付 支付类型
		String dtPaymode = "7";
		paramMap.put("dtPaymode", dtPaymode);
		//获取第三方支付数据(自费) !!!!!
		BlExtPay saveBlExtPay = saveBlExtPay(paramMap,dft);

		//HIS未结算总金额 (需要结算部分)
		BigDecimal amountSt = BigDecimal.ZERO;
		//自费总金额
		BigDecimal amountPi = BigDecimal.ZERO.add(saveBlExtPay.getAmount());
		//医保总金额
		BigDecimal amountInsu = BigDecimal.ZERO;
		//获取消息数据
		MSH msh=dft.getMSH();
		resAckMap.put("msgOldId",msh.getMessageControlID().getValue());
		String receive = msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		resAckMap.put("receive", receive);
		paramMap.put("receive", receive);
		paramMap.put("sendApp", receive);
		PID pid=dft.getPID();
		String codeOp=pid.getPatientID().getID().getValue();
		PV1 pv1=dft.getPV1();
		String codePv=pv1.getVisitNumber().getID().getValue();//就诊流水号
		if(CommonUtils.isNull(codePv)){
			// 未传入就诊流水号，构建失败返回消息ACK
			return createAckMsgForDft("AE","PV1-19未传入就诊流水号",resAckMap);
		}
		FT1 ft1=dft.getFINANCIAL().getFT1();
		//Transaction Amount – Extended
		String amoutMsgStr = ft1.getTransactionAmountExtended().getCp1_Price().getQuantity().getValue();
		if(CommonUtils.isNull(amoutMsgStr)){
			// 未传入总金额 -返回异常消息
			return createAckMsgForDft("AE","FT1-11未传入总金额",resAckMap);
		}
		//医保通道类型
		String dtHpprop = ft1.getFt11_SetIDFT1().getValue();
		EVN evn=dft.getEVN();
		String codeEmp= evn.getOperatorID(0).getIDNumber().getValue();
		//String payType=evn.getOperatorID(0).getFamilyName().getSurname().getValue();
		if(CommonUtils.isNull(codeEmp)){
			// 未传入操作人-返回异常消息
			return createAckMsgForDft("AE","EVN-5.1未传入操作人",resAckMap);
		}
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap = DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		if(empMap==null){
			// 未查询到有效的操作人数据
			return createAckMsgForDft("AE","【"+codeEmp+"】未查询到有效的操作人数据",resAckMap);
		}
		String sqlPv = "select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		if(pvencounter==null){
			// 根据就诊流水号未查询到有效就诊数据，构建失败消息返回
			return createAckMsgForDft("AE","【"+codePv+"】根据就诊流水号未查询到有效就诊数据",resAckMap);
		}
		// 构建session中User信息
		User user = new User();
		user.setPkOrg(Constant.PKORG); //固定值-机构
		user.setPkDept(Constant.PKDEPT);//固定科室-门诊收费
		String pkEmp = SDMsgUtils.getPropValueStr(empMap, "pkEmp");
		String nameEmp = SDMsgUtils.getPropValueStr(empMap, "nameEmp");
		user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
		user.setNameEmp(nameEmp);// evn查询获取-nameEmp
		user.setCodeEmp(codeEmp);
		UserContext.setUser(user);

		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		OpCgTransforVo opCgforVo = new OpCgTransforVo();
		try {
			//医保患者自费结算，先转为自费结算后，结算完成后转为医保
			String pkInsu = new String(pvencounter.getPkInsu());//原来的类型
			updatePkInsu(Constant.PKINSUSELF,pvencounter);
			pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
			Map<String,Object> resMap=new HashMap<>();
			String curtime = DateUtils.getDateTimeStr(new Date());
			resMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
			resMap.put("notDisplayFlagPv", "0");
			resMap.put("isNotShowPv", "0");
			resMap.put("pkPv", pvencounter.getPkPv());
			resMap.put("pkPi", pvencounter.getPkPi());
			//未计算金额
			List<BlPatiCgInfoNotSettleVO> mapResult = helperService.queryPatiCgInfoNotSettle(resMap);
			if(mapResult==null || mapResult.size()==0){
				throw new BusException("未查询到未结算的数据信息");
			}
			Iterator<BlPatiCgInfoNotSettleVO> iterator = mapResult.iterator();
			while(iterator.hasNext()){
				BlPatiCgInfoNotSettleVO settleVO = iterator.next();
				//计算处方总金额（如果处方号为空默认本次就诊所有处方都结算）
				if(dtHpprop==null || "".equals(dtHpprop)||settleVO.getDtHpprop().equals(dtHpprop) ){
					amountSt = amountSt.add(settleVO.getAmount());
				}else{
					//不是该处方的，移除这个vo
					iterator.remove();
				}
			}
			BigDecimal amountMsg = new BigDecimal(amoutMsgStr);
			if(amountSt.compareTo(amountMsg)!=0){
				throw new BusException("支付总金额与结算金额不符：支付总金额：【"+amoutMsgStr+"】,实际结算金额：【"+amountSt+"】");
			}
			//组装预结算 数据
			String mapResultJson = JsonUtil.writeValueAsString(mapResult);
			List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
			ApplicationUtils apputil = new ApplicationUtils();
			opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
			opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
			opCgforVo.setBlOpDts(opDts);
			opCgforVo.setAggregateAmount(amountSt); //需支付总金额
			opCgforVo.setMedicarePayments(amountInsu);//医保支付
			opCgforVo.setPatientsPay(amountPi);//现金支付
			opCgforVo.setXjzf(saveBlExtPay.getAmount());//现金支付
			//调用预结算接口 countOpcgAccountingSettlement
			ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
			if(response.getStatus()!=0){
				throw new BusException("预结算异常："+response.getDesc()+response.getErrorMessage());
			}
			opCgforVo=(OpCgTransforVo) response.getData();
			opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
			opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
			opCgforVo.setBlOpDts(opDts);
			//TODO 构建结算接口vo数据：特别构建发票数据
			opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
			//付款数据构建
			List<BlDeposit> depositList=new ArrayList<BlDeposit>();
			BlDeposit deposit=new BlDeposit();
			deposit.setDtPaymode(dtPaymode);
			deposit.setAmount(amountPi);
			deposit.setFlagAcc("0");
			deposit.setDelFlag("0");
			deposit.setPayInfo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//第三方订单号
			depositList.add(deposit);
			opCgforVo.setBlDeposits(depositList);
			opCgforVo.setPatientsPay(amountMsg);//现金支付
			ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
			if(respSettle.getStatus()!=0){
				throw new BusException("结算异常："+response.getDesc()+response.getErrorMessage());
			}
			//医保患者自费结算，先转为自费结算后，转为医保
			updatePkInsu(pkInsu,pvencounter);

			//补更新新第三方支付表pkSettle
			opCgforVo = (OpCgTransforVo) respSettle.getData();
			//updateExtPayData(paramMap,opCgforVo);
			updateBlExtPay(opCgforVo,paramMap);

			//提交事务
			platformTransactionManager.commit(status);
		}catch(Exception e){
			// 添加失败 回滚事务；
			platformTransactionManager.rollback(status);
			e.printStackTrace();
			return createAckMsgForDft("AE",e.getMessage(),resAckMap);
		}

		//生成电子票据
		//手动提交事务不影响 结算业务
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		try {
			Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
			resAckMap.putAll(invMap);
			platformTransactionManager.commit(status2);
		} catch (Exception e) {
			platformTransactionManager.rollback(status2);
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败："+e.getClass()+e.getMessage());
		}
		//发送反馈消息
		msg = successDftAckParamCreate(opCgforVo,dft,ft1,resAckMap,codeOp,pvencounter);
		return msg;
	}

	/**
	 * 修改患者类型
	 * @param pkInsu
	 * @param pvencounter
	 */
	private void updatePkInsu(String pkInsu, PvEncounter pvencounter) {
		pvencounter.setPkInsu(pkInsu);
		DataBaseHelper.updateBeanByPk(pvencounter);
	}

	/**
	 * 更新第三方支付表pkSettle数据
	 * @param paramMap
	 */
//	private void updateExtPayData(Map<String,Object> paramMap,OpCgTransforVo opCgforVo){
//		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
//		Map<String,Object> depoMap=DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
//		String sql = "update BL_EXT_PAY set PK_SETTLE=?,pk_pi=?,pk_pv=?,pk_depo=? where SERIAL_NO=?";
//		DataBaseHelper.execute(sql, new Object[]{opCgforVo.getPkSettle(),opCgforVo.getPkPi(),opCgforVo.getPkPv(),SDMsgUtils.getPropValueStr(depoMap, "pkDepo"),SDMsgUtils.getPropValueStr(paramMap, "SerialNo")});
//	}
//
	/**
	 * 多系统接口公用（微信自助机体检公用）
	 * 保存第三方支付数据
	 * @param user
	 * @param amountPi
	 * @param paramMap
	 */
//	public void saveExtPayData(User user,BigDecimal amountPi,Map<String,Object> paramMap){
//		BlExtPay extPay=new BlExtPay();
//		extPay.setPkExtpay(NHISUUID.getKeyId());
//		extPay.setPkOrg(user.getPkOrg());
//		extPay.setAmount(amountPi);
//		extPay.setFlagPay("1");//支付标志
//		String date = SDMsgUtils.getPropValueStr(paramMap, "jyrq")+SDMsgUtils.getPropValueStr(paramMap, "jysj");
//		extPay.setDateAp(DateUtils.strToDate(date));//请求时间
//		extPay.setDatePay(DateUtils.strToDate(date));//支付时间
//		extPay.setSerialNo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//订单号
//		extPay.setTradeNo(SDMsgUtils.getPropValueStr(paramMap, "TradeNo"));//订单号
//		extPay.setSysname(SDMsgUtils.getPropValueStr(paramMap, "Sysname"));//系统名称
//		extPay.setDescPay(SDMsgUtils.getPropValueStr(paramMap, "DescPay"));
//		extPay.setResultPay(SDMsgUtils.getPropValueStr(paramMap, "ResultPay"));
//		extPay.setEuPaytype(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
//		extPay.setCreator(user.getPkEmp());
//		extPay.setCreateTime(new Date());
//		extPay.setTs(new Date());
//		extPay.setDelFlag("0");
//		DataBaseHelper.insertBean(extPay);
//	}

	/**
	 * 组建DFT_P03消息成功返回Ack所需要的数据 自助机
	 * @param opCgforVo
	 * @param dft
	 * @param ft1
	 * @param resAckMap
	 * @param codeOp
	 * @param pvencounter
	 * @return
	 */
	private String successDftAckParamCreate(OpCgTransforVo opCgforVo ,DFT_P03 dft,FT1 ft1,Map<String, Object> resAckMap,String codeOp,PvEncounter pvencounter){
		//获取费用分类信息
		StringBuffer ackSql=new StringBuffer();
		ackSql.append("select opdt.code_bill,sum(opdt.amount_pi) amount from bl_op_dt opdt");
		ackSql.append(" inner join bl_settle bst on bst.pk_settle=opdt.pk_settle");
		ackSql.append(" inner join bd_invcate_item cateitem on cateitem.code = opdt.code_bill and cateitem.del_flag = '0'");
		ackSql.append(" inner join bd_invcate inv on inv.pk_invcate = cateitem.pk_invcate and inv.eu_type = '0' and inv.del_flag = '0'");
		ackSql.append(" where bst.pk_settle=?");
		ackSql.append(" group by opdt.code_bill");
		List<Map<String,Object>> opFreeCateList=DataBaseHelper.queryForList(ackSql.toString(), new Object[]{opCgforVo.getPkSettle()});
		//获取发票号
		StringBuffer invSql=new StringBuffer();
		invSql.append(" select inv.code_inv from bl_invoice inv");
		invSql.append(" inner join bl_st_inv stinv on stinv.pk_invoice=inv.pk_invoice");
		invSql.append(" where stinv.PK_SETTLE=? and inv.FLAG_CANCEL='0'");
		invSql.append(" order by inv.ts desc");
		List<Map<String,Object>> invResList=DataBaseHelper.queryForList(invSql.toString(),new Object[]{opCgforVo.getPkSettle()});
		String codeInv="";
		if(invResList!=null && invResList.size()>0 && invResList.get(0)!=null && CommonUtils.isNotNull(invResList.get(0).get("codeInv"))){
			codeInv=invResList.get(0).get("codeInv").toString();
		}
		resAckMap.put("opFreeCate", opFreeCateList);
		resAckMap.put("codeInv", codeInv);
		// 获取窗口号
		String winnoConf="";
		StringBuffer winnoSql=new StringBuffer();
		winnoSql.append(" select un.name from ex_pres_occ pres inner join bd_dept_unit un on un.code=pres.winno_conf  where pres.pk_settle=? ");
		List<Map<String,Object>> winnoResList=DataBaseHelper.queryForList(winnoSql.toString(), opCgforVo.getPkSettle());
		if(winnoResList!=null){
			for (Map<String, Object> map : winnoResList) {
				if(map.get("name")!=null){
					winnoConf+=map.get("name").toString()+" ";
				}
			}
		}
		resAckMap.put("winnoConf", winnoConf);
		//获取结算数据
		String setSql="select * from bl_settle where pk_settle=?";
		BlSettle settle=DataBaseHelper.queryForBean(setSql, BlSettle.class, opCgforVo.getPkSettle());
		resAckMap.put("settle", settle);
		resAckMap.put("codePv", pvencounter.getCodePv());
		resAckMap.put("codeOp", codeOp);
		resAckMap.put("pvencounter", pvencounter);

		return createAckMsgForDft("AA", "成功", resAckMap);
	}

	/******************************诊中支付（医保）********************************************/

	/**
	 * 自助机门诊诊间支付方法-医保
	 * @param dft
	 * @return
	 * @throws ParseException
	 */
	public String receiveCardSettleYb(DFT_P03 dft)throws HL7Exception, ParseException  {
		String msg="";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> resAckMap=new HashMap<String,Object>();
		paramMap.put("dtPaymode", "99");
		//先获取第三方支付数据，单边账处理   ! ! ! !
		BlExtPay saveBlExtPay = saveBlExtPay(paramMap,dft);
		//总金额
		BigDecimal amountSt = BigDecimal.ZERO;
		//自费金额
		BigDecimal amountPi = saveBlExtPay.getAmount();

		MSH msh=dft.getMSH();
		resAckMap.put("msgOldId",msh.getMessageControlID().getValue());
		String receive = msh.getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		resAckMap.put("receive", receive);
		paramMap.put("receive", receive);
		paramMap.put("sendApp", receive);
		PID pid=dft.getPID();
		String codeOp=pid.getPatientID().getID().getValue();
		PV1 pv1=dft.getPV1();
		String codePv=pv1.getVisitNumber().getID().getValue();//就诊流水号
		if(CommonUtils.isNull(codePv)){
			// 未传入就诊流水号，构建失败返回消息ACK
			return createAckMsgForDft("AE","PV1-19未传入就诊流水号",resAckMap);
		}
		FT1 ft1 = dft.getFINANCIAL().getFT1();
		//Transaction Amount 总金额
		String amountMsgStr = ft1.getTransactionAmountExtended().getCp1_Price().getQuantity().getValue();
		if(CommonUtils.isNull(amountMsgStr)){
			// 未传入总金额 -返回异常消息
			return createAckMsgForDft("AE","FT1-11未传入总金额",resAckMap);
		}
		String codeEmp= dft.getEVN().getOperatorID(0).getIDNumber().getValue();
		if(CommonUtils.isNull(codeEmp)){
			// 未传入操作人-返回异常消息
			return createAckMsgForDft("AE","EVN-5.1未传入操作人",resAckMap);
		}
		String sqlEmp = "select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		if(empMap==null){
			// 未查询到有效的操作人数据
			return createAckMsgForDft("AE","【"+codeEmp+"】未查询到有效的操作人数据",resAckMap);
		}
		String sqlPv = "select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		if(pvencounter==null){
			// 根据就诊流水号未查询到有效就诊数据，构建失败消息返回
			return createAckMsgForDft("AE","【"+codePv+"】根据就诊流水号未查询到有效就诊数据",resAckMap);
		}
		String pkEmp= SDMsgUtils.getPropValueStr(empMap, "pkEmp");
		String nameEmp=SDMsgUtils.getPropValueStr(empMap, "nameEmp");
		// 构建session中User信息
		User user = new User();
		user.setPkOrg(Constant.PKORG); //固定值-机构
		user.setPkDept(Constant.PKDEPT);//固定科室-门诊收费
		user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
		user.setNameEmp(nameEmp);// evn查询获取-nameEmp
		user.setCodeEmp(codeEmp);
		UserContext.setUser(user);

		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		OpCgTransforVo opCgforVo = new OpCgTransforVo();

		try{
			Map<String,Object> doYbRetData = saveYbSettleData(dft,user,pvencounter,amountPi);
			if(!"1".equals(doYbRetData.get("status"))){
				throw new BusException("医保信息保存失败："+doYbRetData.get("data"));
			}
			//构造结算数据
			Map<String,Object> resMap = new HashMap<>();
			String curtime = DateUtils.getDateTimeStr(new Date());
			resMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
			resMap.put("notDisplayFlagPv", "0");
			resMap.put("isNotShowPv", "0");
			resMap.put("pkPv", pvencounter.getPkPv());
			resMap.put("pkPi", pvencounter.getPkPi());
			//查询未结算数据
			List<BlPatiCgInfoNotSettleVO> mapResult = helperService.queryPatiCgInfoNotSettle(resMap);
			if(mapResult==null || mapResult.size()==0){
				throw new BusException("未查询到未结算的数据信息");
			}
			//根据医保通道类型，过滤当前处方号的未记费数据
			String dtHpprop = ft1.getFt11_SetIDFT1().getValue();//医保通道类型
			Iterator<BlPatiCgInfoNotSettleVO> iterator = mapResult.iterator();
			while(iterator.hasNext()){
				BlPatiCgInfoNotSettleVO settleVO = iterator.next();
				//计算处方总金额（如果处方号为空默认本次就诊所有处方都结算）
				if(dtHpprop==null || "".equals(dtHpprop)||settleVO.getDtHpprop().equals(dtHpprop) ){
					amountSt = amountSt.add(settleVO.getAmount());
				}else{
					//不是该处方的，移除这个vo
					iterator.remove();
				}
			}
			//消息传入总金额
			BigDecimal amountMsg = new BigDecimal(amountMsgStr);
			if(amountSt.compareTo(amountMsg)!=0){
				throw new BusException("支付金额与实际金额不符：支付金额：【"+amountMsg+"】,需支付金额：【"+amountSt+"】");
			}
			//结算参数
			String mapResultJson = JsonUtil.writeValueAsString(mapResult);
			List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
			ApplicationUtils apputil = new ApplicationUtils();

			opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
			opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
			opCgforVo.setBlOpDts(opDts);
			opCgforVo.setAggregateAmount(amountSt); //需支付总金额
			opCgforVo.setMedicarePayments(amountSt.subtract(saveBlExtPay.getAmount()));//医保支付
			opCgforVo.setPatientsPay(saveBlExtPay.getAmount());//现金支付
			opCgforVo.setXjzf(saveBlExtPay.getAmount());//现金支付
			opCgforVo.setAmtInsuThird(amountMsg.subtract(saveBlExtPay.getAmount()));//医保支付
			//调用预结算接口 countOpcgAccountingSettlement
			ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
			if(response.getStatus()!=0){
				throw new BusException("预结算异常："+response.getDesc()+response.getErrorMessage());
			}
			//付款数据构建
			List<BlDeposit> depositList = new ArrayList<BlDeposit>();
			BlDeposit deposit=new BlDeposit();
			deposit.setDtPaymode(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
			deposit.setAmount(saveBlExtPay.getAmount());
			deposit.setFlagAcc("0");
			deposit.setDelFlag("0");
			deposit.setPayInfo(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"))?SDMsgUtils.getPropValueStr(paramMap, "SerialNo"):null);//第三方订单号
			depositList.add(deposit);
			opCgforVo = (OpCgTransforVo) response.getData();
			opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
			opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
			opCgforVo.setBlOpDts(opDts);
			opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
			opCgforVo.setBlDeposits(depositList);
			opCgforVo.setMedicarePayments(amountMsg.subtract(saveBlExtPay.getAmount()));//医保支付
			opCgforVo.setAmtInsuThird(amountMsg.subtract(saveBlExtPay.getAmount()));//医保支付
			//结算接口
			ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
			if(respSettle.getStatus()!=0){
				throw new BusException("结算异常："+response.getDesc()+respSettle.getErrorMessage());
			}
			//更新pkSettle
			opCgforVo = (OpCgTransforVo) respSettle.getData();
			//更新pkSettle医保相关表
			updatePkSettle(doYbRetData,opCgforVo);
			//更新pkSettle,第三方支付相关表
			if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"))){
				//updateExtPayData(paramMap,opCgforVo);
				updateBlExtPay(opCgforVo,paramMap);
			}
			//提交事务
			platformTransactionManager.commit(status);
		}catch(Exception e){
			// 添加失败 回滚事务；
			platformTransactionManager.rollback(status);
			e.printStackTrace();
			log.info("自助机医保结算异常："+e.getClass()+e.getMessage());
			return createAckMsgForDft("AE",e.getMessage(),resAckMap);
		}

		//生成电子票据
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		try {
			Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
			resAckMap.putAll(invMap);
			platformTransactionManager.commit(status2);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败：{}",e.getClass());
			platformTransactionManager.rollback(status2);
		}
		//发送反馈消息
		msg = successDftAckParamCreate(opCgforVo,dft,ft1,resAckMap,codeOp,pvencounter);
		log.info("诊中支付方法【 receiveCardSettleYb 】总金额：{ } 自费金额 {  }",saveBlExtPay.getAmount());
		return msg;
	}

	/**
	 * 处理医保结算数据
	 * @param dft 消息实例
	 * @param user 当前用户
	 * @param pvencounter 就诊信息
	 * @return resMap:{"status":"1:成功；0:失败" ,"data":"错误返回信息/成功需要返回数据"}
	 */
	private Map<String,Object> saveYbSettleData(DFT_P03 dft,User user,PvEncounter pvencounter,BigDecimal amountPi){
		Map<String,Object> resMap=new HashMap<String,Object>();
		Map<String, Object> map = SDMsgUtils.getStrBySplit("ZPR", "ZCT", "\\|", dft.toString());
		//ZPR-1 医疗机构编码
		//String yljgbm = SDMsgUtils.getPropValueStr(map, "2");
		//ZPR-2 医疗机构名称
		//String yljgmc = SDMsgUtils.getPropValueStr(map, "3");
		//ZPR-3 门诊流水号
		//String mzlsh = SDMsgUtils.getPropValueStr(map, "4");
		//ZPR-4单据号
		String Djh = SDMsgUtils.getPropValueStr(map, "5");
		//ZPR-5医疗证号
		//String Ylzh = SDMsgUtils.getPropValueStr(map, "6");
		//ZPR-6电脑号
		//String Dnh =  SDMsgUtils.getPropValueStr(map, "7");
		//ZPR-7姓名
		//String xm = SDMsgUtils.getPropValueStr(map, "8");
		//ZPR-8金额
		String je = SDMsgUtils.getPropValueStr(map, "9");
		//现金合计 9
		String Xjhj = SDMsgUtils.getPropValueStr(map, "10");
		//记账合计 10
		String Jzhj = SDMsgUtils.getPropValueStr(map, "11");
		//结算序列号 11
		String Jsywh = SDMsgUtils.getPropValueStr(map, "12");
		//统筹合计 12
		String Tchj = SDMsgUtils.getPropValueStr(map, "13");
		//业务号 ywh 13
		String ywh = SDMsgUtils.getPropValueStr(map, "14");
		if(ywh!=null && ywh.length()>20){
			ywh = ywh.substring(0, 20);
		}
		resMap.put("amountSt", StringUtils.isBlank(je)?"0":je);//合计金额
		resMap.put("amountPi", StringUtils.isBlank(Xjhj)?"0":Xjhj);//自费金额
		resMap.put("amountInsu", StringUtils.isBlank(Jzhj)?"0":Jzhj);//医保金额
		resMap.put("amountFund", StringUtils.isBlank(Tchj)?"0":Tchj);//基金支付金额
		//如果自费金额与医保自费金额不相等，返回错误
		if(!(new BigDecimal(StringUtils.isBlank(Xjhj)?"0":Xjhj).compareTo(amountPi)==0)){
			throw new BusException("自费金额:"+SDMsgUtils.getPropValueStr(resMap,"amountPi")+"，第三方订单支付金额："+amountPi.toString()+",请到窗口核实结算！");
		}
		InsSzybSt insSzybSt = new InsSzybSt();
		insSzybSt.setPkInsst(NHISUUID.getKeyId());
		insSzybSt.setPkOrg(user.getPkOrg());//机构
		String visitSql="select * from ins_szyb_visit where del_flag='0'  and pk_pv=? and pvcode_ins=? order by date_reg desc ";
		List<InsSzybVisit> visitList = DataBaseHelper.queryForList(visitSql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv(),Djh});
		if(visitList==null ||visitList.size()==0){
			resMap.put("status", "0");//1:成功；0：失败
			String message="HIS接口【诊中支付（医保）】调用失败:未查询到关联医保登记数据";
			resMap.put("data", message);
			return resMap;
		}
		InsSzybVisit insSzybVisit = visitList.get(0);
		insSzybSt.setPkVisit(insSzybVisit.getPkVisit());//医保登记主键
		insSzybSt.setPkHp(insSzybVisit.getPkHp());//医保主键
		insSzybSt.setPkPv(pvencounter.getPkPv());//就诊主键
		insSzybSt.setPkPi(pvencounter.getPkPi());//患者主键
		insSzybSt.setDateInp(pvencounter.getDateBegin());//入院日期
		insSzybSt.setDateOutp(null);//出院日期
		insSzybSt.setDays(1);//实际住院日期
		insSzybSt.setPkSettle("");//结算主键--暂时不写HIS结算业务通过后，补写pk_settle
		insSzybSt.setPvcodeIns(Djh);//就医登记号
		insSzybSt.setCodeSerialno(Jsywh);//医药机构结算业务
		insSzybSt.setDateSt(null);//结算日期
		insSzybSt.setAmount(Double.valueOf(je));//结算金额
		insSzybSt.setCodeCenter("");//中心编码
		insSzybSt.setCodeOrg(Constant.CODEORG);//医院编码
		insSzybSt.setNameOrg(Constant.NAMEORG);//医院名称
		insSzybSt.setTransid(Jsywh);//出院登记流水号
		insSzybSt.setBillno("");//发票号
		insSzybSt.setCodeHpst(ywh);////医保结算号   --取消医保结算号
		insSzybSt.setCreator(user.getPkEmp());//创建人
		insSzybSt.setCreateTime(new Date());//创建时间
		insSzybSt.setDelFlag("0");//删除标志
		insSzybSt.setTs(new Date());//时间戳
		DataBaseHelper.insertBean(insSzybSt);
		InsSzybStCity cityst=new InsSzybStCity();
		cityst.setPkInsstcity(NHISUUID.getKeyId());
		cityst.setPkOrg(user.getPkOrg());
		cityst.setPkInsst(insSzybSt.getPkInsst());
		cityst.setEuTreattype("3");
		cityst.setDtMedicate("11");//医疗类别
		cityst.setDiagcodeInp("");//入院诊断编码
		cityst.setDiagnameInp("");//入院诊断名称
		cityst.setReasonOutp("");//出院
		cityst.setDiagcodeOutp("");//出院诊断编码
		cityst.setDiagnameOutp("");//出院诊断名称
		cityst.setDiagcode2Outp("");//出院诊断编码
		cityst.setDiagname2Outp("");//出院诊断名称
		cityst.setStatusOutp("");//出院情况
		cityst.setAmtJjzf(Double.valueOf(Tchj));//基金支付
		cityst.setAmtGrzhzf(Double.valueOf(Jzhj));//个人账户支付
		cityst.setAmtGrzf(Double.valueOf(Xjhj));//个人支付
		//cityst.setAmtGrzh(0.00);//个人账户
		cityst.setCreateTime(new Date());
		cityst.setCreator(user.getPkEmp());
		cityst.setDelFlag("0");
		cityst.setTs(new Date());
		DataBaseHelper.insertBean(cityst);

		Map<String, List<Map<String, Object>>> resolueMessage = SDMsgUtils.resolueMessage(dft.toString());
		List<Map<String, Object>> list = resolueMessage.get("ZPY");
		int size = list.size();
		List<InsSzybStCitydt> citydtList=new ArrayList<InsSzybStCitydt>();
		for(int i=0;i<size;i++){
			Map<String, Object> m = list.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(SDMsgUtils.getPropValueStr(m, "1"));//结算返回信息类 --平台未返回数据
			citydt.setCategory(SDMsgUtils.getPropValueStr(m, "1"));//类别代码
			String amt=SDMsgUtils.getPropValueStr(m, "2");
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(cityst.getPkInsstcity());
			citydt.setCreator(user.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			citydtList.add(citydt);
		}

		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class),citydtList);
		resMap.put("status", "1");//1:成功；0：失败
		resMap.put("data", insSzybSt.getPkInsst());
		return resMap;
	}

	/**
	 * 医保补充更新pkSettle
	 * @param doYbRetData
	 * @param opCgforVo
	 */
	private void updatePkSettle(Map<String, Object> doYbRetData, OpCgTransforVo opCgforVo) {
		//HIS结算成功后回写医保结算记录中得pk_settle
		String upYbSql="update ins_szyb_st set pk_settle=? where pk_insst=?";
		DataBaseHelper.execute(upYbSql, new Object[]{opCgforVo.getPkSettle(),doYbRetData.get("data")});
	}


	/***********************返回消息*********************************/
	/**
	 * 诊中支付ACK消息返回构建 自助机
	 * @param status AE失败：AA成功
	 * @param message 特别信息提示
	 * @param paramMap 成功返回的数据
	 * @return
	 */
	private String createAckMsgForDft(String status,String message,Map<String,Object> paramMap){
		String msg = null;
		try {
			String msgId=SDMsgUtils.getMsgId();
			paramMap.put("msgid", msgId);
			paramMap.put("msgtype", "ACK");
			paramMap.put("triggerevent", "P03");
			paramMap.put("situation", status);
			paramMap.put("msaText", message);
			ACK_P03 ack = msgCreate.createAckMsgForOpDft(paramMap);
			if("AA".equals(status)){
				ZPO zpo=ack.getZPO();
				PvEncounter pven=(PvEncounter) paramMap.get("pvencounter");
				zpo.getMzhm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOp"));
				zpo.getFPh().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeInv"));
				zpo.getMzrq().setValue(DateUtils.formatDate(pven.getDateBegin(), "yyyyMMddHHmmss"));
				zpo.getBrxm().setValue(pven.getNamePi());
				if("1".equals(pven.getEuPvtype())){//门诊
					zpo.getSflb().setValue("0");
				}else if("2".equals(pven.getEuPvtype())){//急诊
					zpo.getSflb().setValue("1");
				}
				BlSettle settle=(BlSettle) paramMap.get("settle");
				zpo.getJzje().getMo1_Quantity().setValue("0");
				zpo.getGrjf().getMo1_Quantity().setValue(CommonUtils.getString(settle.getAmountPi(),"0"));
				zpo.getFyck().setValue(SDMsgUtils.getPropValueStr(paramMap, "winnoConf"));
				zpo.getSfy().setValue(UserContext.getUser().getCodeEmp());

				//获取电子发票数据
				Map<String, Object> blInvoiceMsg = ElectInvService.getBlInvoiceMsg(paramMap);
				if(blInvoiceMsg !=null && !blInvoiceMsg.containsKey("error")){
					//电子发票数据
					//EbillCode	电子票据代码 ebillbatchcode 电子票据代码
					zpo.getEbillCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillCode"));
					//EbillNo	电子票据号码 ebillno 电子票据号码
					zpo.getEbillNo().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillNo"));
					//CheckCode	电子校验码 checkcode 电子校验码
					zpo.getCheckCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"checkCode"));
					//EbillDate	电子票据生成时间 dateEbill 电子票据生成时间
					zpo.getEbillDate().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillDate"));
					//EbillQRCode	电子票据二维码效验数据 qrcode_ebill 电子票据二维码效验数据(byte[])
					zpo.getEbillQRCode().setValue(SDMsgUtils.getPropValueStr(blInvoiceMsg,"ebillQRCode"));
				}else{
					ack.getMSA().getMsa6_ErrorCondition().getCe2_Text().setValue(SDMsgUtils.getPropValueStr(paramMap,"result"));
				}

				NTE nte=ack.getNTE(0);
				nte.getSourceOfComment().setValue("0");
			}
			Message result = ack;
			msg = SDMsgUtils.getParser().encode(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}


}
