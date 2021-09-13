package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.DFT_P03;
import ca.uhn.hl7v2.model.v24.message.SRM_S01;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import org.apache.commons.collections.CollectionUtils;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.*;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.*;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.common.SDHl7MsgHander;
import com.zebone.nhis.ma.pub.platform.sd.create.CreateOpMsg;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreateUtil;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.*;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.pv.reg.dao.RegSyxMapper;
import com.zebone.nhis.pv.reg.service.RefundSyxService;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 微信对接服务类
 * @author jd_em
 *
 */
@Service
public class SDSysMsgWeChatService {

	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
	

	private static final String WeXinPayType="15";
	
	@Resource
	private SDSysMsgPubService msgPubService;
	@Resource
	private RegSyxMapper regSyxMapper;
	@Resource
	private RegSyxService regSyxService;
	@Resource
	private RefundSyxService refundSyxService;
	@Resource
	private CgQryMaintainService cgQryMaintainService;
	@Resource
	private OpcgPubHelperService opcgPubHelperService;
	@Resource
	private BalAccoutService balAccoutService;
	@Resource
	private InvSettltService invSettltService;
	@Resource
	private MsgCreateUtil msgCreateUtil;
	@Resource
	private TicketPubService ticketPubService;
	@Resource
	private SDMsgMapper msgMapper;
	@Resource
	private CreateOpMsg createOpMsg;
	@Resource
	private	SDHl7MsgHander sDHl7MsgHander;
	@Autowired
	private ElectInvService electInvService;
	@Resource
    private PvInfoPubService pvInfoPubService;
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	
	
	/**
	 * 160预挂号支付(自费、医保)
	 * 1.解析消息获取字段值
	 * 2.处理挂号收费逻辑
	 * 3.拼接消息回传
	 * @param dft
	 * @return
	 * @throws ParseException
	 * @throws DataTypeException 
	 */
	public String  appRegist160PiInfoPay(DFT_P03 dft,Message hapiMsg, String logPrefix) throws ParseException, DataTypeException{
		log.info(logPrefix + " begin createRegPayParam");
		Map<String, Object> prampMap = createRegPayParam(dft,hapiMsg,logPrefix);
		log.info(logPrefix + " end createRegPayParam");
		
		return disposeAppRegPayInfo(prampMap, logPrefix);
	}
	
	/**
	 * 解析挂号支付消息各节点内容，构建门诊挂号支付所需参数数据
	 * @param dft
	 * @param hapiMsg
	 * @return
	 * @throws ParseException
	 * @throws DataTypeException
	 */
	private Map<String,Object> createRegPayParam(DFT_P03 dft,Message hapiMsg,String logPrefix)throws ParseException, DataTypeException{
		log.info(logPrefix + " enter createRegPayParam");
		Map<String, Object> prampMap = new HashMap<>();
		String sendApp = dft.getMSH().getSendingApplication().getNamespaceID().getValue();
		prampMap.put("sendApp", sendApp);
		String oldMsgId =  dft.getMSH().getMessageControlID().getValue();
		prampMap.put("oldmsgid", oldMsgId);
		//EVN-2 消息时间
		String mesTime = dft.getEVN().getRecordedDateTime().getTimeOfAnEvent().getValue();
		prampMap.put("mesTime", mesTime);
		//EVN-5.1
		String codeEmp= dft.getEVN().getOperatorID(0).getIDNumber().getValue();
		if(CommonUtils.isNull(codeEmp)){
			throw new BusException("EVN-5.1未传入操作人");
		}
		log.info(logPrefix + " begin searchEmp");
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		log.info(logPrefix + " end searchEmp");
		if(empMap==null){
			throw new BusException("【"+codeEmp+"】未查询到有效的操作人数据");
		}
		prampMap.put("pkEmp",SDMsgUtils.getPropValueStr(empMap,"pkEmp"));
		prampMap.put("nameEmp",SDMsgUtils.getPropValueStr(empMap,"nameEmp"));
		prampMap.put("codeEmp",codeEmp);
		//EVN-5.2	支付方式
		String payWay = dft.getEVN().getOperatorID(0).getFamilyName().getSurname().getValue();
		prampMap.put("payWay", payWay);
		//PID-2 患者编号
		String patiId = dft.getPID().getPatientID().getID().getValue();
		prampMap.put("patiId", patiId);

		if("PEIS".equals(sendApp)){//体检
			prampMap.put("addrPosition", "4");
			prampMap.put("pkHp","eb2baf4e9848486984516fe6a84d6136");//默认自费
		}else{
			prampMap.put("addrPosition", "3");
			String ylzh="";
			int idcount= dft.getPID().getPatientIdentifierListReps();
			if(idcount>2&& dft.getPID().getPatientIdentifierList(2)!=null){
				ylzh=dft.getPID().getPatientIdentifierList(2).getID().getValue();
			}
			prampMap.put("Ylzh",CommonUtils.isEmptyString(ylzh)?"000":ylzh);
		}
		int size = dft.getPID().getPatientIdentifierListReps();
		//PID-3.1患者卡号
		//PID-3.2患者卡类型
		log.info(logPrefix + " begin create PatientIdentifierList");
		for (int i = 0; i < size; i++) {
			String id = dft.getPID().getPatientIdentifierList(i).getID().getValue();
			String idType = dft.getPID().getPatientIdentifierList(i).getIdentifierTypeCode().getValue();
			if ("IDCard".equals(idType)||"CardNO".equals(idType)||"PatientNO".equals(idType)) {//患者编号
				if(StringUtils.isNotBlank(id))
				prampMap.put("patientno", id);
			}else if ("IdentifyNO".equals(idType)) {//身份证号
				if(StringUtils.isNotBlank(id))
				prampMap.put("identifyno", id);
			}else if("SocialSecurityCardNumber".equals(idType)){//社保卡号
				if(StringUtils.isNotBlank(id)){
					prampMap.put("socialsecuritycardnumber", id);
					prampMap.put("isSur", "02");
				}
			}else if("SBpcNO".equals(idType)){//电脑号
				if(StringUtils.isNotBlank(id)){
					prampMap.put("SBpcNO", id);
					prampMap.put("isSur", "02");
				}
			}else if("PassportNO".equals(idType)){//护照号
				if(StringUtils.isNotBlank(id))
					prampMap.put("PassportNO", id);
			}else if ("ReentryPermitNO".equals(idType)) {//回乡证号
				if(StringUtils.isNotBlank(id))
				prampMap.put("identifyno", id);
			}else if ("DrivingLicenseNO".equals(idType)) {//驾驶证号
				if(StringUtils.isNotBlank(id))
				prampMap.put("identifyno", id);
			}else if ("MilitaryIDNO".equals(idType)) {//军官证号
				if(StringUtils.isNotBlank(id))
				prampMap.put("MilitaryIDNO", id);
			}
		}
		log.info(logPrefix + " end create PatientIdentifierList");
		//获取险种类型 insSzybVisitCity.setAae140("");//险种类型
		prampMap.put("Aae140", dft.getPID().getPid36_BreedCode().getCe1_Identifier().getValue());
		//PV1-2 门诊住院标识
		String OorI = dft.getPV1().getPatientClass().getValue();
		prampMap.put("OorI", OorI);
		//PV1-19    预约号
		String code = dft.getPV1().getVisitNumber().getID().getValue();
		prampMap.put("code", code);
		//FT1-2交易流水号
		String tranId = dft.getFINANCIAL(0).getFT1().getTransactionID().getValue();
		prampMap.put("tranId", tranId);
		prampMap.put("SerialNo", tranId);
		// 时间
		String dateTrans = dft.getFINANCIAL(0).getFT1().getTransactionDate().getTimeOfAnEvent().getValue();
		prampMap.put("dateTrans", dateTrans);
		//FT1-11.3 金额(总金额)
		String amountSt = dft.getFINANCIAL(0).getFT1().getTransactionAmountExtended().getPrice().getQuantity().getValue();
		// 自费金额
		String amountPi = "0";
		// 医保金额
		String amountInsu = "0";
		//获取医保数据
		if(dft.toString().contains("ZPY")){
			Map<String, List<Map<String, Object>>> resolueMessage = SDMsgUtils.resolueMessage(dft.toString());
			List<Map<String, Object>> list = resolueMessage.get("ZPY");
			prampMap.put("list",list);
		}
		String insurTypeCode = "";
		
		//获取第三方支付数据(自费)
		if(dft.toString().contains("ZCT")){
			String[] zct =hapiMsg.toString().split("ZCT");
			String[] zctValue = zct[1].split("\\|");
			//ZCT 3  MerchantNo  商户号
		    prampMap.put("shh",zctValue[3]);
			//ZCT 4 终端号 （必须）
			prampMap.put("zdh",zctValue[4]);
			//ZCT 8 订单号  SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			prampMap.put("ddh",zctValue[8]);
			//ZCT 9 自费金额
			//prampMap.put("zfje",zctValue[9]);
			amountPi = StringUtils.isBlank(zctValue[9])?"0":zctValue[9];
			amountPi = new BigDecimal(amountPi).divide(new BigDecimal(100)).toString();
			//ZCT 10  流水号
			prampMap.put("lsh",zctValue[10]);
			//ZCT 11 票据号
			prampMap.put("pjh",zctValue[11]);
			//ZCT 11 授权号
			prampMap.put("sqh",zctValue[12]);
			//ZCT 13 参考号
			prampMap.put("ckh",zctValue[13]);
			//ZCT 14 交易发起日期 yyyyMMdd
			prampMap.put("jyrq",zctValue[14]);
			//ZCT 15 HHmmssSSS(时分秒毫秒)
			prampMap.put("jysj",zctValue[15]);
			//ZCT 系统订单号   TradeNo  系统订单号(微信退货交易时使用此域)
			prampMap.put("xtddh",zctValue.length>24?zctValue[25]:"");
			//TradeNo  系统订单号(微信退货交易时使用此域)
			prampMap.put("TradeNo",zctValue.length>24?zctValue[25]:"");
			//SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			prampMap.put("SerialNo",zctValue[8]);
			//Sysname  终端号
			prampMap.put("Sysname",zctValue[4]);
			//ResultPay 商户号#参考号 104480880621012#202135765632
			prampMap.put("ResultPay",zctValue[3]+"#"+zctValue[13]);
			// DescPay	流水号#票据号#授权号#日期  000890#000890##0731160650
			String monthDay = (zctValue[14].length()>4)?zctValue[14].substring(4):"";
			prampMap.put("DescPay",zctValue[10]+"#"+zctValue[11]+"#"+zctValue[12]+"#"+monthDay+zctValue[15]);
			prampMap.put("dtBank", zctValue[22]!=null ?zctValue[22].replace("\r", ""):"");
		}
		
		//获取医保类型
		if(dft.toString().contains("ZPL")){
			String zpl [] =hapiMsg.toString().split("ZPL");
			String [] zplele1 = zpl[1].split("\\|");
			insurTypeCode = zplele1[1];
			if(insurTypeCode.contains("ZPR"))
				insurTypeCode =  insurTypeCode.substring(0, insurTypeCode.indexOf("\rZPR"));
		}
		//默认自费
		insurTypeCode = insurTypeCode==null||"".equals(insurTypeCode)||"1".equals(insurTypeCode)?"01":insurTypeCode;

		log.info(logPrefix + " begin searchHp");
		String sql = "select name,pk_hp from bd_hp where del_flag = '0' and code = ? ";
		prampMap.put("insurTypeCode",insurTypeCode);
		Map<String , Object> insurMap = DataBaseHelper.queryForMap(sql, insurTypeCode);
		log.info(logPrefix + " end searchHp");
		if(insurMap==null)
			throw new BusException("未查询到该("+insurTypeCode+")医保编号所对应的医保主建信息");
		prampMap.put("pkHp", insurMap.get("pkHp"));
		prampMap.put("name", insurMap.get("name"));

		//支付数据
		if(dft.toString().contains("ZPR") && !"PEIS".endsWith(sendApp)){
			
			Map<String, List<Map<String, Object>>> resolueMessags = SDMsgUtils.resolueMessage(dft.toString());
			Map<String, Object> map = resolueMessags.get("ZPR").get(0);
			// ZPR-1 医疗机构编码
			// String yljgbm = SDMsgUtils.getPropValueStr(map, "1");
			// ZPR-2 医疗机构名称
			// String yljgmc = SDMsgUtils.getPropValueStr(map, "2");
			// ZPR-3 门诊流水号
			String mzlsh = SDMsgUtils.getPropValueStr(map, "3");
			// ZPR-4单据号
			String Djh = SDMsgUtils.getPropValueStr(map, "4");
			// ZPR-5医疗证号
			 String Ylzh = SDMsgUtils.getPropValueStr(map, "5");
			// ZPR-6电脑号
			 String Dnh = SDMsgUtils.getPropValueStr(map, "6");
			// ZPR-7姓名
			 String xm = SDMsgUtils.getPropValueStr(map, "7");
			// ZPR-8金额
			String je = SDMsgUtils.getPropValueStr(map, "8");
			// 现金合计 9
			String Xjhj = SDMsgUtils.getPropValueStr(map, "9");
			// 记账合计 10
			String Jzhj = SDMsgUtils.getPropValueStr(map, "10");
			// 结算序列号 11
			String Jsywh = SDMsgUtils.getPropValueStr(map, "11");
			// 统筹合计 12
			String Tchj = SDMsgUtils.getPropValueStr(map, "12");
			// 业务号 ywh 13
			String ywh = SDMsgUtils.getPropValueStr(map, "13");
			if (ywh != null && ywh.length() > 20) {
				ywh = ywh.substring(0, 20);
			}
			
			prampMap.put("Mglsh", mzlsh);
			//ZPR-4 Djh	单据号 (门诊登记号)
			prampMap.put("Djh", Djh);
			//ZPR-5 Ylzh	医疗证号
			prampMap.put("insurNo", Ylzh);
			//ZPR-6 Dnh	电脑号
			prampMap.put("computerNo", Dnh);
			//ZPR-7	Xm	姓名
			//prampMap.put("Xm", ele[7]);
			prampMap.put("namexm", xm);
			//ZPR-8 Je	金额
			prampMap.put("Je", je);
			prampMap.put("priceje", je);
			//ZPR-9 Xjhj	现金合计
			//prampMap.put("Xjhj", ele[9]);
			prampMap.put("cashSelf", Xjhj);
			amountPi = StringUtils.isBlank(Xjhj)?"0":Xjhj;
			//ZPR-10 Jzhj	记账合计
			//prampMap.put("Jzhj", ele[10]);
			prampMap.put("cashInsur",Jzhj);
			amountInsu = StringUtils.isBlank(Jzhj)?"0":Jzhj;
			//ZPR-11 Jsywh	序列号  code_serialno bke384
			prampMap.put("Jsywh", Jsywh);
			//ZPR-12 Tchj	统筹合计
			prampMap.put("Tchj", CommonUtils.isNotNull(Tchj)?Tchj:"0");
			if ("YYT".equals(sendApp)) {//自助机
				//ZPR-13 ywh 结算业务号 code_hpst ckc618
				prampMap.put("ywh", ywh);
			}
		}
		//默认普通门诊
		prampMap.put("aka130", "11");
		prampMap.put("AkA130Name", "普通门诊");
		prampMap.put("amountPi", amountPi);
		prampMap.put("amountInsu", amountInsu);
		prampMap.put("amountSt", amountSt);
		
		User user=new User();
		// 构建session中User信息
		user.setPkOrg(Constant.PKORG); //固定值-机构
		user.setPkDept(Constant.PKDEPT);//固定科室-门诊收费
		String pkEmp= SDMsgUtils.getPropValueStr(empMap, "pkEmp");
		String nameEmp=SDMsgUtils.getPropValueStr(empMap, "nameEmp");
		user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
		user.setNameEmp(nameEmp);// evn查询获取-nameEmp
		user.setCodeEmp(codeEmp);
		UserContext.setUser(user);
		
		return prampMap;
	}
	
	/**
     * 执行预约确认(已预约登记信息)
     * 1.效验挂号信息
     * 2.准备消息参数值
     * 3.执行预约确认(已预约登记信息)修改相关信息
     * 4.拼接消息
     * @param map
     * @return
     * @throws ParseException
     * @throws DataTypeException
     */
    public String disposeAppRegPayInfo(Map<String, Object> map, String logPrefix) throws ParseException, DataTypeException{
    	log.info(logPrefix + " enter disposeAppRegPayInfo");
    	//1.效验挂号信息

    	log.info(logPrefix + " begin search 效验挂号信息");
    	Map<String, Object> apptMap = DataBaseHelper.queryForMap("select appt.*,case when to_char(appt.date_reg,'yyyyMMdd') = to_char(appt.begin_time,'yyyyMMdd') then '1' else '0' end as timePosition ,to_char(appt.begin_time,'yyyyMMddHH24miss') beginStr,apptpv.pk_emp_phy from  sch_appt appt left join sch_appt_pv apptpv on apptpv.pk_schappt = appt.pk_schappt   where appt.code = ?  and appt.del_flag = '0' and appt.eu_status = '0' ", SDMsgUtils.getPropValueStr(map,"code"));
    	log.info(logPrefix + " end search 效验挂号信息");
    	if(apptMap==null){
	    	if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '0' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(map,"code"))==1)
	    		throw new BusException("预约号("+SDMsgUtils.getPropValueStr(map,"code")+")预约数据异常,请到窗口确认！费用将3-5天自动退回");
	    	if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '1' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(map,"code"))==1)
	    		throw new BusException("预约号("+SDMsgUtils.getPropValueStr(map,"code")+")已成功预约,已支付,请勿重复推送");
			throw new BusException("未查询到该预约号("+SDMsgUtils.getPropValueStr(map,"code")+")信息");
		}
		//校验排班是否停止 和 发布
    	log.info(logPrefix + " begin search 校验排班是否停止 和 发布");
		if(DataBaseHelper.queryForScalar("select COUNT(1) from  SCH_SCH sch where sch.PK_SCH=? and (sch.FLAG_STOP='1' or sch.EU_STATUS in ('0','1') or sch.del_flag = '1') ", Integer.class,SDMsgUtils.getPropValueStr(apptMap,"pkSch"))!=0)
    		throw new BusException("您所预约的医生已停诊，无法完成支付，请到窗口确认！");
		log.info(logPrefix + " end search 校验排班是否停止 和 发布");
		
		User user = new User();
	    user.setPkEmp(SDMsgUtils.getPropValueStr(map,"pkEmp"));
	    user.setNameEmp(SDMsgUtils.getPropValueStr(map,"nameEmp"));
	    user.setPkOrg(Constant.PKORG);
	    user.setPkDept(Constant.PKDEPT);//门诊收费处
	    user.setCodeEmp(SDMsgUtils.getPropValueStr(map,"codeEmp"));
	    UserContext.setUser(user);
	    
	    String message="";
		//2.准备消息参数值；
		Map<String, Object> appRegParam = new HashMap<>();
		appRegParam.put("pksch", SDMsgUtils.getPropValueStr(apptMap,"pkSch"));
		String resSql="select eu_srvtype from sch_srv where pk_schsrv=?";
		log.info(logPrefix + " begin search sch_srv");
		Map<String,Object> srvMap = DataBaseHelper.queryForMap(resSql, new Object[]{apptMap.get("pkSchsrv")});
		log.info(logPrefix + " end search sch_srv");
		String euSrvType=CommonUtils.getString(srvMap.get("euSrvtype"),"1");
		if("9".equals(euSrvType)){
			appRegParam.put("euPvType", "2");
		}else{
			appRegParam.put("euPvType", "1");
		}
		appRegParam.put("patientno", SDMsgUtils.getPropValueStr(map,"patiId"));
		appRegParam.put("pkSchappt", SDMsgUtils.getPropValueStr(apptMap,"pkSchappt"));
		PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);
		regvo.setTicketNo(SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
		regvo.setPkEmp(SDMsgUtils.getPropValueStr(map,"pkEmp"));
		regvo.setNameEmpReg(SDMsgUtils.getPropValueStr(map,"nameEmp"));
		//3.执行预约确认(已预约登记信息)
		//3.1.基于预约信息生成就诊记录，写表pv_encounter；
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(Constant.PKORG);
	    //修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
	    try {
			//3.11.新增外部接口支付明细,Bl_Ext_Pay
			if(!"".equals(SDMsgUtils.getPropValueStr(map, "SerialNo"))&&!"4".equals(SDMsgUtils.getPropValueStr(map, "euPvType"))){
				log.info(logPrefix + " begin saveExtPayDataRegPi");
				saveExtPayDataRegPi(regvo,map);
				log.info(logPrefix + " end saveExtPayDataRegPi");
			}
			// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkSchres", regvo.getPkSchres());

			log.info(logPrefix + " begin querySchResInfo");
			Map<String,Object> schres = regSyxMapper.querySchResInfo(paramMap);
			log.info(logPrefix + " end querySchResInfo");
			regvo = getPiMaterRegItemPriceVo(regvo,map);
			regvo.setPkHp(SDMsgUtils.getPropValueStr(map,"pkHp"));
			regvo.setAmtInsuThird(new BigDecimal(CommonUtils.getString(map.get("amountInsu"), "0")));

			log.info(logPrefix + " begin savePvEncounter");
			PvEncounter pv = savePvEncounter(regvo, pkPv,schres,SDMsgUtils.getPropValueStr(apptMap,"pkEmpPhy"));
			log.info(logPrefix + " end savePvEncounter");
			
			//3.1.1 关联 预约就诊记录（预约登记时不生成pv_encounter，确认时才生成）
			log.info(logPrefix + " begin update SCH_APPT_PV");
			DataBaseHelper.update("update SCH_APPT_PV set pk_pv=? where pk_schappt=?",new Object[]{pkPv,SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			log.info(logPrefix + " end update SCH_APPT_PV");
			regvo.setCodePv(pv.getCodePv());
			//3.2.生成门诊就诊记录，写表pv_op；Pv_Insurance
			if("1".equals(pv.getEuPvtype())){
				log.info(logPrefix + " begin A01");
				regSyxService.savePvOp(regvo,pv,schres);
				log.info(logPrefix + " end A01");
			}else{
				log.info(logPrefix + " begin A02");
				savePvEr(regvo,pv,schres);
				log.info(logPrefix + " end A02");
			}
			log.info(logPrefix + " begin A03");
			regSyxService.savePvInsurance(regvo, pv);
			log.info(logPrefix + " end A03");
			regvo =  regSyxService.saveSettle(regvo, pv);
			log.info(logPrefix + " end A04");
			
			if(CommonUtils.isNotNull(regvo.getPkSettle())){
				//校验结算总金额和传入挂号总金额是否一致
				log.info(logPrefix + " begin A05");
				String sqlSettle="select amount_st from bl_settle where pk_settle =?";
				Map<String,Object> settleMap=DataBaseHelper.queryForMap(sqlSettle, new Object[]{regvo.getPkSettle()});
				log.info(logPrefix + " end A05");
				if(settleMap==null || settleMap.get("amountSt")==null){
					throw new BusException("HIS业务处理失败，未生成结算信息！");
				}
				BigDecimal amoutStinp=new BigDecimal(CommonUtils.getString(map.get("amountSt"),"0"));
				BigDecimal amoutStHis=new BigDecimal(CommonUtils.getString(settleMap.get("amountSt"),"0"));
				if(amoutStHis.compareTo(amoutStinp)!=0){
					throw new BusException("HIS业务处理失败，HIS结算金额【"+amoutStHis+"】与支付金额【"+amoutStinp+"】不符!");
				}
				String setSql="select amount_pi from bl_settle where pk_settle=?";
				log.info(logPrefix + " begin A06");
				Map<String,Object> setMap=DataBaseHelper.queryForMap(setSql, regvo.getPkSettle());
				log.info(logPrefix + " end A06");
			
				String depsql="update bl_deposit set amount=? where pk_settle=?";
				DataBaseHelper.execute(depsql, new Object[]{CommonUtils.getString(setMap.get("amountPi"), "0.00"),regvo.getPkSettle()});
				log.info(logPrefix + " end A07");
			}
			
			//3.12.更新预约登记信息
			log.info(logPrefix + " begin A08");
			DataBaseHelper.update("update sch_appt set  eu_status='1',flag_pay='1' where pk_schappt = ?  ", new Object[]{SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			log.info(logPrefix + " end A08");
			//更新第三方支付数据
			String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
	 		Map<String,Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{regvo.getPkSettle()});
			log.info(logPrefix + " end A09");
			DataBaseHelper.update("update BL_EXT_PAY set PK_SETTLE=?,PK_DEPO=? where SERIAL_NO=?",new Object[]{regvo.getPkSettle(),SDMsgUtils.getPropValueStr(depoMap, "pkDepo"),SDMsgUtils.getPropValueStr(map,"SerialNo")});
			log.info(logPrefix + " end A10");
			//3.13.生成医保记录  ins_szyb_st  ins_szyb_st_city  ins_szyb_st_citydt  ins_szyb_visit  ins_szyb_visit_city
			if(!"01".equals(SDMsgUtils.getPropValueStr(map,"insurTypeCode")) && !"PEIS".equals(SDMsgUtils.getPropValueStr(map, "sendApp"))){
				saveInsSzybInfo(regvo,map);
				log.info(logPrefix + " end A11");
			}
			map.put("ticketNo",  SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
			map.put("beginStr",  SDMsgUtils.getPropValueStr(apptMap,"beginStr"));
			map.put("codePv", pv.getCodePv());
	    	platformTransactionManager.commit(status);
			log.info(logPrefix + " end commit");
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			// 事务处理失败发送消息
			e.printStackTrace();
			return createAppRegPayHL7ErroInfo(map,e.getMessage());
		}

		//生成电子票据
		log.info(logPrefix + " begin 生成电子票据 getTransaction");
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		log.info(logPrefix + " end 生成电子票据 getTransaction");
		try {
			log.info(logPrefix + " begin registrationElectInv");
			Map<String, Object> invMap = electInvService.registrationElectInv(regvo.getPkPv(), user, regvo.getPkSettle());
			log.info(logPrefix + " end registrationElectInv");
			map.putAll(invMap);
			platformTransactionManager.commit(status2);
			log.info(logPrefix + " end commit status2");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败：{}",e.getClass());
			map.put("result",e.getClass()+e.getMessage());
			platformTransactionManager.rollback(status2);
		}

		//4.拼接反馈成功消息
		log.info(logPrefix + " begin createAppRegPayHL7Info");
		message = createAppRegPayHL7Info(map);
		log.info(logPrefix + " end createAppRegPayHL7Info");

		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		msgParam.put("pkPv", pkPv);
		msgParam.put("isAdd", "0");
		msgParam.put("timePosition", SDMsgUtils.getPropValueStr(apptMap,"timeposition"));//0：预约     1：当天
		msgParam.put("addrPosition", SDMsgUtils.getPropValueStr(map,"addrPosition"));//0：现场 1：自助机 2：电话 3：微信  4：支付宝
		log.info(logPrefix + " begin sendPvOpRegMsg");
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		log.info(logPrefix + " end sendPvOpRegMsg");

    	return message;
    }
    
    
    /**
	 * 保存就诊记录急诊属性
	 * @return
	 */
	private PvEr savePvEr(PiMasterRegVo master,PvEncounter pv,Map<String,Object> schres ){
		// 保存急诊属性
		PvEr pvEr = new PvEr();
		pvEr.setPkPv(pv.getPkPv());
		pvEr.setPkSchsrv(master.getPkSchsrv());
		pvEr.setPkRes(master.getPkSchres());
		pvEr.setPkDateslot(master.getPkDateslot());
		pvEr.setPkDeptPv(master.getPkDept());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", master.getPkSchres());
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
	 * 预挂号支付退费(自费、医保)
	 * 1.解析消息获取字段值
	 * 2.处理挂号收费逻辑
	 * 3.拼接消息回传
	 * @param dft
	 * @return
	 */
	public String  appRegistPiInfoPayExit(DFT_P03 dft,Message hapiMsg){
		Map<String, Object> prampMap = new HashMap<>();
		prampMap.put("oldmsgid", dft.getMSH().getMessageControlID().getValue());
		String patiId = dft.getPID().getPatientID().getID().getValue();
		prampMap.put("patientno", patiId);
		String code = dft.getPV1().getVisitNumber().getID().getValue();
		//String code = dft.getPV1().getPreadmitNumber().getID().getValue();
		prampMap.put("code", code);
		return  cancelPiPayReg(prampMap);
	}
    
	/**
	 * 第三方支付成功未就诊 取消挂号 1.生成负的结算信息，写表bl_settle； 2.生成负的记费信息，写表bl_op_dt；
	 * 3.生成负的支付记录，写表bl_deposit； 4.取消就诊记录，更新pv_encounter； 5.作废发票信息，更新表bl_invoice
	 * @param regvo
	 * @return
	 * @throws
	 * @author fuhao
	 */
	public void cancelAppPayRegistPi(PiMasterRegVo regvo) {
		if (regvo == null || CommonUtils.isNull(regvo.getPkPv()))
			throw new BusException("退号时未获取到就诊信息！");
		if (CommonUtils.isNull(regvo.getPkSch()))
			throw new BusException("退号时未获取到排班主键pkSch！");

		String pkPv = regvo.getPkPv();
		
		refundSyxService.validCancelRegByConf(pkPv);
		User u = new User();
		u.setPkEmp(Constant.PKWECHAT);// 暂定写死为医保备案的人员主建
		u.setNameEmp(Constant.NAMEWECHAT);
		u.setPkOrg(regvo.getPkOrg());
		u.setPkDept(Constant.PKDEPT);// 门诊收费处
		// 更新取消预约相关表信息
		/* refundSyxService.cancelReg(param,user); */
		Map<String, Object> mapParam = new HashMap<String, Object>();

		mapParam.put("pkPv", pkPv);
		mapParam.put("pkOrg", u.getPkOrg());
		mapParam.put("haveSettle", true);// 要过滤掉未退号，先去给医保上报缴费时生成的一正一负的数据
		// 根据就诊主键查询挂号记费明细
		List<BlOpDt> blOpDts = cgQryMaintainService.qryRegCostInfoByPkpv(mapParam);
		// 有计费的才退费
		// 退款记录
		List<BlDeposit> negaBlDeposits = null;
		if (CollectionUtils.isNotEmpty(blOpDts)) {
			int size = blOpDts.size();
			String pkSettle = blOpDts.get(0).getPkSettle();
			String pkPi = blOpDts.get(0).getPkPi();
			mapParam.put("pkPi", pkPi);
			if (size > 1)
				for (int i = 1; i < size; i++) {
					if (!pkSettle.equals(blOpDts.get(i).getPkSettle())) {
						throw new BusException("此次挂号费用异常，形成了两笔结算信息。" + "pkPv:【"+ blOpDts.get(i).getPkPv() + "】");
					}
				}
			// 根据结算主键查询结算信息
			mapParam.put("pkSettle", pkSettle);
			BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
			// 生成退费结算信息
			if (blSettle == null) {
				throw new BusException("没有查询到患者的挂号结算信息");
			}
			String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle, "20");
			// 生成退费明细
			BlOpDt regurnDef = new BlOpDt();
			regurnDef.setFlagRecharge("0");// 重新生成待收费记录写1，否则写0
			opcgPubHelperService.generateRefoundRecord(blOpDts, pkSettleCanc,regurnDef);// 传入新的结算主键
			// 根据结算主键查询结算明细
			List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
			// 生成退费结算明细

			opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

			// 根据结算主键查询交款记录信息
			List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
			// 生成退费的交款记录信息
			negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits, pkSettleCanc);
			// 加了非空判断
			if (negaBlDeposits != null) {
				for (BlDeposit negaBlDeposit : negaBlDeposits) {
					if (negaBlDeposit.getDtPaymode().equals(IDictCodeConst.PATIACCOUNT)) {
						// 更新患者账户，调用患者账户消费服务
						BlDepositPi blDepositPi = new BlDepositPi();
						ApplicationUtils.setDefaultValue(blDepositPi, true);
						blDepositPi.setEuDirect(EnumerateParameter.ONE);
						blDepositPi.setPkPi(pkPi);
						blDepositPi.setAmount(negaBlDeposit.getAmount().abs());
						blDepositPi.setDtPaymode(EnumerateParameter.FOUR);
						blDepositPi.setPkEmpPay(u.getPkEmp());
						blDepositPi.setNameEmpPay(u.getNameEmp());
						PiAcc piAcc = cgQryMaintainService.qryPiAccByPkPi(pkPi);// 患者账户信息
						ApplicationUtils.setDefaultValue(piAcc, false);
						piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO: piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
						balAccoutService.piAccDetailVal(piAcc, blDepositPi,pkPv, null);
					}
				}
			}

			String BL0002_code = ApplicationUtils.getSysparam("BL0002", false);
			// 如果挂号时打印的发票，要作废发票
			if (EnumerateParameter.ONE.equals(BL0002_code)) {
				// 根据结算主键查询作废结算时对应的发票
				List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
				if (blInvoices != null && blInvoices.size() > 0) {
					for (BlInvoice inv : blInvoices) {
						// 更新作废发票信息
						opcgPubHelperService.updateRefoundBlInvoice(inv);
					}
				}
			}

			// 获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
			// String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
			String eBillFlag = invSettltService.getBL0031ByNameMachine(regvo
					.getNameMachine());
			if (!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)) {
				try {
					invSettltService.billCancel(pkSettle, u);
				} catch (Exception e) {
					throw new BusException("取消结算失败：" + e.getMessage());
				}
			}

		}

		Map<String, Object> updateMap = new HashMap<String, Object>();
		updateMap.put("flagCancel", "1");
		updateMap.put("euStatus", "9");
		updateMap.put("pkEmpCancel", u.getPkEmp());
		updateMap.put("nameEmpCancel", u.getNameEmp());
		updateMap.put("dateCancel", new Date());
		updateMap.put("ts", new Date());
		updateMap.put("pkPv", regvo.getPkPv());
		// 更新就诊记录
		regSyxMapper.updatePvEncounter(updateMap);
		
	}

	/**
	 * 门诊诊间支付方法-自费
	 * @param dft
	 * @return
	 */
	public String receiveP03OpPvSettle(DFT_P03 dft, String logPrefix)throws HL7Exception  {
		log.info(logPrefix + " enter receiveP03OpPvSettle");
		String msg="";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		Map<String,Object> resAckMap=new HashMap<String,Object>();
		MSH msh=dft.getMSH();
		resAckMap.put("msgOldId",msh.getMessageControlID().getValue());
		PID pid=dft.getPID();
		String codeOp=pid.getPatientID().getID().getValue();
		PV1 pv1=dft.getPV1();
		String codePv=pv1.getVisitNumber().getID().getValue();//就诊流水号
		if(CommonUtils.isNull(codePv)){
			// 未传入就诊流水号，构建失败返回消息ACK
			return msgPubService.createAckMsgForDft("AE","PV1-19未传入就诊流水号",resAckMap);
		}
		FT1 ft1=dft.getFINANCIAL().getFT1();
		//Transaction Amount – Extended
		String amoutExt=ft1.getTransactionAmountExtended().getCp1_Price().getQuantity().getValue();
		if(CommonUtils.isNull(amoutExt)){
			// 未传入总金额 -返回异常消息
			return msgPubService.createAckMsgForDft("AE","FT1-11未传入总金额",resAckMap);
		}
		EVN evn=dft.getEVN();
		String codeEmp= evn.getOperatorID(0).getIDNumber().getValue();
		String payType=evn.getOperatorID(0).getFamilyName().getSurname().getValue();
		if(CommonUtils.isNull(codeEmp)){
			// 未传入操作人-返回异常消息
			return msgPubService.createAckMsgForDft("AE","EVN-5.1未传入操作人",resAckMap);
		}

		log.info(logPrefix + " begin A01");
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		log.info(logPrefix + " end A01");
		if(empMap==null){
			// 未查询到有效的操作人数据
			return msgPubService.createAckMsgForDft("AE","【"+codeEmp+"】未查询到有效的操作人数据",resAckMap);
		}

		String sqlPv="select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter=DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		log.info(logPrefix + " end A02");
		if(pvencounter==null){
			// 根据就诊流水号未查询到有效就诊数据，构建失败消息返回
			return msgPubService.createAckMsgForDft("AE","【"+codePv+"】根据就诊流水号未查询到有效就诊数据",resAckMap);
		}
		Map<String,Object> resMap=new HashMap<>();
		// 构建session中User信息
		User user = new User();
        user.setPkOrg(Constant.PKORG); //固定值-机构
        user.setPkDept(Constant.PKDEPT);//固定科室-门诊收费
        String pkEmp= SDMsgUtils.getPropValueStr(empMap, "pkEmp");
        String nameEmp=SDMsgUtils.getPropValueStr(empMap, "nameEmp");
        user.setPkEmp(pkEmp);// evn中查询获取  pkEmp
        user.setNameEmp(nameEmp);// evn查询获取-nameEmp
        user.setCodeEmp(codeEmp);
        UserContext.setUser(user);
		String curtime = DateUtils.getDateTimeStr(new Date());
		resMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
		resMap.put("notDisplayFlagPv", "0");
		resMap.put("isNotShowPv", "0");
		resMap.put("pkPv", pvencounter.getPkPv());
		resMap.put("pkPi", pvencounter.getPkPi());
		List<BlPatiCgInfoNotSettleVO> mapResult= opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
		log.info(logPrefix + " end A03");
		if(mapResult==null || mapResult.size()==0){
			// 未查询到未结算的数据信息-返回异常信息
			return msgPubService.createAckMsgForDft("AE","未查询到未结算的数据信息",resAckMap);
		}

		String mapResultJson=JsonUtil.writeValueAsString(mapResult);
		List<BlOpDt> opDts=JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
		BigDecimal amountSt=new BigDecimal(0.00);//HIS未结算总金额
		for (BlPatiCgInfoNotSettleVO noSetvo : mapResult) {
			amountSt=amountSt.add(noSetvo.getAmount());
		}

		//自费结算默认所有结算明细按自费结算
		for (BlOpDt opdt :opDts) {
			opdt.setPkInsu(Constant.PKINSUSELF);
		}
		BigDecimal amountMsg=new BigDecimal(amoutExt);
		if(amountSt.compareTo(amountMsg)!=0){
			//传入总金额与HIS计算结果不符
			return msgPubService.createAckMsgForDft("AE","传入总金额与HIS计算结果不符,传入金额【"+amountMsg+"】,HIS计算金额【"+amountSt+"】",resAckMap);
		}
		//支付类型
		String dtPaymode = "99";
		if("WX".equals(payType)){//微信
			dtPaymode = "15";
		}else if("ZFB".equals(payType)){//支付宝
			dtPaymode = "16";
		}else if("CD".equals(payType)){//银联
			dtPaymode = "3";
		}else if("SMF".equals(payType)){//扫码付
			dtPaymode = "7";
 		}else{
 			dtPaymode = "99";
		}
		paramMap.put("payType", dtPaymode);
		paramMap.put("dtPaymode", dtPaymode);

		//TODO 构建第三方支付数据并保存  预结算接口回查询第三方支付数据
		//获取第三方支付数据(自费)
		if(dft.toString().contains("ZCT")){
			String[] zct = dft.toString().split("ZCT");
			String[] zctValue = zct[1].split("\\|");
			//ZCT 3  MerchantNo  商户号
			paramMap.put("shh",zctValue[3]);
			//ZCT 4 终端号 （必须）
			paramMap.put("zdh",zctValue[4]);
			//ZCT 8 订单号  SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			paramMap.put("ddh",zctValue[8]);
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
			paramMap.put("xtddh",zctValue.length>24?zctValue[25]:"");

			//TradeNo  系统订单号(微信退货交易时使用此域)
			paramMap.put("TradeNo",zctValue.length>24?zctValue[25]:"");
			//SerialNo	卡号(微信支付宝订单号(目前固定长度30))
			paramMap.put("SerialNo",zctValue[8]);
			//Sysname  终端号
			paramMap.put("Sysname",zctValue[4]);
			//ResultPay 商户号#参考号 104480880621012#202135765632
			paramMap.put("ResultPay",zctValue[3]+"#"+zctValue[13]);
			// DescPay	流水号#票据号#授权号#日期  000890#000890##0731160650
			String monthDay = (zctValue[14].length()>4)?zctValue[14].substring(4):"";
			paramMap.put("DescPay",zctValue[10]+"#"+zctValue[11]+"#"+zctValue[12]+"#"+monthDay+zctValue[15]);
			paramMap.put("dtBank", zctValue[22]!=null ?zctValue[22].replace("\r", ""):"");
			//大于0写表

			log.info(logPrefix + " begin saveExtPayData");
			saveExtPayData(user,amountMsg,paramMap);
			log.info(logPrefix + " end saveExtPayData");			
		}
		/**
		 * 如果患者医保挂号进行自费支付，先将患者本次就诊身份转为自费，结算后 将身份变更为初始身份
		 **/
		String pkInsuOld = pvencounter.getPkInsu();
		ApplicationUtils apputil = new ApplicationUtils();
		OpCgTransforVo opCgforVo = new OpCgTransforVo();
		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		log.info(logPrefix + " end status");
		try {
			if(pvencounter.getPkInsu()!=null && !pvencounter.getPkInsu().equals(Constant.PKINSUSELF)){
				String sql="update pv_encounter set pk_insu=? where pk_pv=?";
				DataBaseHelper.execute(sql, new Object[]{Constant.PKINSUSELF,pvencounter.getPkPv()});
				log.info(logPrefix + " end A04");
			}
			//组装预结算 数据
			//ApplicationUtils apputil = new ApplicationUtils();
			//OpCgTransforVo opCgforVo = new OpCgTransforVo();
			opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
			opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
			opCgforVo.setBlOpDts(opDts);
			//调用预结算接口 countOpcgAccountingSettlement
			log.info(logPrefix + " begin 调用预结算接口");
			ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
			log.info(logPrefix + " end 调用预结算接口");
			if(response.getStatus()!=0){
				// 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
				platformTransactionManager.rollback(status);
				return msgPubService.createAckMsgForDft("AE","HIS接口【YjSettle】调用失败:"+response.getDesc()+response.getErrorMessage()+"",resAckMap);
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
			deposit.setAmount(amountMsg);
			deposit.setFlagAcc("0");
			deposit.setDelFlag("0");
			deposit.setPayInfo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//第三方订单号
			depositList.add(deposit);
			opCgforVo.setBlDeposits(depositList);

			log.info(logPrefix + " begin HIS接口【Settle】");
			ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
			log.info(logPrefix + " end HIS接口【Settle】");
			if(respSettle.getStatus()!=0){
				platformTransactionManager.rollback(status);
				return msgPubService.createAckMsgForDft("AE","HIS接口【Settle】调用失败:"+respSettle.getDesc()+respSettle.getErrorMessage(),resAckMap);
			}
			
			
			opCgforVo = (OpCgTransforVo) respSettle.getData();

			//补更新新第三方支付表pkSettle
			log.info(logPrefix + " begin updateExtPayData");
			msgPubService.updateExtPayData(paramMap,opCgforVo);
			log.info(logPrefix + " end updateExtPayData");

			platformTransactionManager.commit(status);
			log.info(logPrefix + " end commit status");
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return msgPubService.createAckMsgForDft("AE","HIS结算逻辑处理",resAckMap);
		}finally{
			String sql="update pv_encounter set pk_insu=? where pk_pv=?";
			DataBaseHelper.execute(sql, new Object[]{pkInsuOld,pvencounter.getPkPv()});
			log.info(logPrefix + " end update pv_encounter");
		}


		//生成电子票据
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		try {
			log.info(logPrefix + " begin mzOutElectInv");
			Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
			log.info(logPrefix + " end mzOutElectInv");
			resAckMap.putAll(invMap);
			platformTransactionManager.commit(status2);
			log.info(logPrefix + " end commit status2");
		} catch (Exception e) {
			platformTransactionManager.rollback(status2);
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败：{}",e.getClass());
			resAckMap.put("result",e.getClass()+e.getMessage());
		}
		//成功反馈消息
		log.info(logPrefix + " begin successDftAckParamCreate");
		msg = msgPubService.successDftAckParamCreate(opCgforVo,dft,ft1,resAckMap,codeOp,pvencounter);
		log.info(logPrefix + " end successDftAckParamCreate");
		return msg;
	}

	/**
	 * 预约挂号（微信自助机体检公用）
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
		//预约渠道，体检4 ，微信写3，自助机写2
		appRegParam.put("orderSource", "PEIS".equals(sendApp)?"4":"3");
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
		//PID-13   患者手机号
		String piTel ="";
		if("MIH".equals(sendApp)){//微信
			piTel =pid.getPhoneNumberHome(0).getPhoneNumber().getValue();
			appRegParam.put("euPvTypeMsg","O");
			appRegParam.put("euPvType", 1);
		}else if ("PEIS".equals(sendApp)) {//体检
			piTel =pid.getPhoneNumberHome(0).getAnyText().getValue();
			appRegParam.put("euPvType", 4);
		}
		appRegParam.put("pitel",piTel==null?"":piTel);

		//AIL-3 预约科室
		String deptCode = so1.getRESOURCES(0).getLOCATION_RESOURCE(0).getAIL().getLocationResourceID().getPointOfCare().getValue();
		
		if(StringUtils.isNotBlank(deptCode))
			appRegParam.put("deptcode",deptCode);
		
		//AIP-1排版主键
		String pk_sch = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getSetIDAIP().getValue();
		//String pk_sch = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getFillerStatusCode().getIdentifier().getValue();

		//AIP-3 医生工号
		String docCode = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getPersonnelResourceID(0).getIDNumber().getValue();
		if(StringUtils.isNotBlank(docCode)){
			appRegParam.put("doccode",docCode);
		}

			
		//AIP-4  预约挂号级别
		String level = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getResourceRole().getIdentifier().getValue();
		if(StringUtils.isNotBlank(level)){
			appRegParam.put("doccode",docCode);
		}

		//AIP-6 预约日期
		String day = so1.getRESOURCES(0).getPERSONNEL_RESOURCE(0).getAIP().getStartDateTime().getTimeOfAnEvent().getValue();
		appRegParam.put("day",day);
		try {
			String zai [] =mes.toString().split("ZAI");
			String [] ele = zai[1].toString().split("\\|");
			//ZAI-4就诊开始时间
			String beginTime = ele[3].toString();
			appRegParam.put("begintime",beginTime);
			//ZAI-5就诊结束时间
			String endTime =  ele[4].toString();
			appRegParam.put("endtime",endTime);
		} catch (Exception e) {
			throw new BusException("消息SRM_S01中ZAI-5字段必须存值，则解析错报");
		}

		//校验数据
		if(StringUtils.isNotBlank(pk_sch)){
			pk_sch = pk_sch.split("#")[0];
			appRegParam.put("pksch",pk_sch);
			//获取资源主键
			String pkRes;
			try{
				String sql = "select sch.PK_SCHRES from SCH_SCH sch where sch.PK_SCH=? and sch.DEL_FLAG='0' and sch.FLAG_STOP='0' and sch.EU_STATUS='8'";
				pkRes = DataBaseHelper.queryForScalar(sql,String.class,pk_sch);
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
					if(SDMsgUtils.getPropValueStr(checkMap,"pkSch").equalsIgnoreCase(pk_sch)){
						throw new BusException("该患者编号("+patientNO+")已存在预约记录,请到窗口确认！");
					}
				}
			}
		}else {
			throw new BusException("未传入排班主建信息");
		}

		User user=new User();
		user.setPkEmp(Constant.PKWECHAT);
		user.setNameEmp(Constant.NAMEWECHAT);
		user.setPkOrg(Constant.PKORG);
		UserContext.setUser(user);
		return addAppRegistPiInfo(appRegParam);
	}
	
	/**
	 * 门诊诊间支付方法-医保
	 * @param dft
	 * @return
	 */
	public String receiveP03OpPvSettleYb(DFT_P03 dft, String logPrefix)throws HL7Exception  {
		log.info(logPrefix + " enter receiveP03OpPvSettleYb");
		Map<String,Object> paramMap = new HashMap<String,Object>();
		String msg="";
		Map<String,Object> resAckMap=new HashMap<String,Object>();
		MSH msh=dft.getMSH();
		resAckMap.put("msgOldId",msh.getMessageControlID().getValue());
		PID pid=dft.getPID();
		String codeOp=pid.getPatientID().getID().getValue();
		PV1 pv1=dft.getPV1();
		String codePv=pv1.getVisitNumber().getID().getValue();//就诊流水号
		if(CommonUtils.isNull(codePv)){
			// 未传入就诊流水号，构建失败返回消息ACK
			return msgPubService.createAckMsgForDft("AE","PV1-19未传入就诊流水号",resAckMap);
		}
		FT1 ft1=dft.getFINANCIAL().getFT1();
		//Transaction Amount 总金额
		String amoutExt=ft1.getTransactionAmountExtended().getCp1_Price().getQuantity().getValue();
		if(CommonUtils.isNull(amoutExt)){
			// 未传入总金额 -返回异常消息
			return msgPubService.createAckMsgForDft("AE","FT1-11未传入总金额",resAckMap);
		}
		EVN evn=dft.getEVN();
		String codeEmp= evn.getOperatorID(0).getIDNumber().getValue();
		String payType=evn.getOperatorID(0).getFamilyName().getSurname().getValue();
		if(CommonUtils.isNull(codeEmp)){
			// 未传入操作人-返回异常消息
			return msgPubService.createAckMsgForDft("AE","EVN-5.1未传入操作人",resAckMap);
		}
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
		log.info(logPrefix + " end A01");
		if(empMap==null){
			// 未查询到有效的操作人数据
			return msgPubService.createAckMsgForDft("AE","【"+codeEmp+"】未查询到有效的操作人数据",resAckMap);
		}

		String sqlPv="select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter=DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		log.info(logPrefix + " end A02");
		if(pvencounter==null){
			// 根据就诊流水号未查询到有效就诊数据，构建失败消息返回
			return msgPubService.createAckMsgForDft("AE","【"+codePv+"】根据就诊流水号未查询到有效就诊数据",resAckMap);
		}
		Map<String,Object> resMap=new HashMap<>();

		// 构建session中User信息
		User user = new User();
        user.setPkOrg(Constant.PKORG); //固定值-机构
        user.setPkDept(Constant.PKDEPT);//固定科室-门诊收费
        String pkEmp= SDMsgUtils.getPropValueStr(empMap, "pkEmp");
        String nameEmp=SDMsgUtils.getPropValueStr(empMap, "nameEmp");
        user.setPkEmp(pkEmp);
        user.setNameEmp(nameEmp);
        user.setCodeEmp(codeEmp);
        UserContext.setUser(user);
		String curtime = DateUtils.getDateTimeStr(new Date());
		resMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
		resMap.put("notDisplayFlagPv", "0");
		resMap.put("isNotShowPv", "0");
		resMap.put("pkPv", pvencounter.getPkPv());
		resMap.put("pkPi", pvencounter.getPkPi());
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
		log.info(logPrefix + " end A03");
		if(mapResult==null || mapResult.size()==0){
			// 未查询到未结算的数据信息-返回异常信息
			return msgPubService.createAckMsgForDft("AE","未查询到未结算的数据信息",resAckMap);
		}

		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		log.info(logPrefix + " end getTransaction");

		//构建结算接口vo数据：医保结算vo
		Map<String,Object> doYbRetData=new HashMap<>();
		try {
			doYbRetData = saveYbSettleData(dft,user,pvencounter);
			log.info(logPrefix + " end saveYbSettleData");
			if(!"1".equals(doYbRetData.get("status"))){
				platformTransactionManager.rollback(status);
				return msgPubService.createAckMsgForDft("AE","HIS接口【YbSettle】调用失败:"+doYbRetData.get("data"),resAckMap);
			}
		} catch (Exception e) {
			platformTransactionManager.rollback(status);
			return msgPubService.createAckMsgForDft("AE","HIS接口【YbSettle】调用失败:"+e.getMessage(),resAckMap);
		}

		//构造结算数据
		String mapResultJson=JsonUtil.writeValueAsString(mapResult);
		List<BlOpDt> opDts=JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
		BigDecimal amountSt=new BigDecimal(0.00);//HIS未结算总金额
		for (BlPatiCgInfoNotSettleVO noSetvo : mapResult) {
			amountSt=amountSt.add(noSetvo.getAmount());
		}
		//消息传入总金额
		BigDecimal amountMsg=new BigDecimal(amoutExt);
		if(amountSt.compareTo(amountMsg)!=0){
			platformTransactionManager.rollback(status);
			//传入总金额与HIS计算结果不符
			return msgPubService.createAckMsgForDft("AE","传入总金额与HIS计算结果不符,传入金额【"+amountMsg+"】,HIS计算金额【"+amountSt+"】",resAckMap);
		}
		//自付金额
		String amountPi = SDMsgUtils.getPropValueStr(doYbRetData, "amountPi");
		amountPi = "".equals(amountPi)?"0":amountPi;
		// 付款方式
		//支付类型
		String dtPaymode = "99";
		if("WX".equals(payType)){//微信
			dtPaymode = "15";
		}else if("ZFB".equals(payType)){//支付宝
			dtPaymode = "16";
		}else if("CD".equals(payType)){//银联
			dtPaymode = "3";
		}else if("SMF".equals(payType)){//扫码付
			dtPaymode = "7";
		}else{
			dtPaymode = "99";
		}
		paramMap.put("payType", dtPaymode);
		paramMap.put("dtPaymode", dtPaymode);
		
		//自费支付数据(第三方支付)
		if(dft.toString().contains("ZPR") && dft.toString().contains("ZCT")){
			String zpr [] =dft.toString().split("ZPR");
			String [] ele = zpr[1].split("\\|");
			BigDecimal amoutSelf = new BigDecimal(ele[9]);

			//获取第三方支付数据(自费)
			if(dft.toString().contains("ZCT")){
				String[] zct = dft.toString().split("ZCT");
				String[] zctValue = zct[1].split("\\|");
				//ZCT 3  MerchantNo  商户号
				paramMap.put("shh",zctValue[3]);
				//ZCT 4 终端号 （必须）
				paramMap.put("zdh",zctValue[4]);
				//ZCT 8 订单号  SerialNo	卡号(微信支付宝订单号(目前固定长度30))
				paramMap.put("ddh",zctValue[8]);
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
				paramMap.put("xtddh",zctValue.length>24?zctValue[25]:"");

				//TradeNo  系统订单号(微信退货交易时使用此域)
				paramMap.put("TradeNo",zctValue.length>24?zctValue[25]:"");
				//SerialNo	卡号(微信支付宝订单号(目前固定长度30))
				paramMap.put("SerialNo",zctValue[8]);
				//Sysname  终端号
				paramMap.put("Sysname",zctValue[4]);
				//ResultPay 商户号#参考号 104480880621012#202135765632
				paramMap.put("ResultPay",zctValue[3]+"#"+zctValue[13]);
				// DescPay	流水号#票据号#授权号#日期  000890#000890##0731160650
				String monthDay = (zctValue[14].length()>4)?zctValue[14].substring(4):"";
				paramMap.put("DescPay",zctValue[10]+"#"+zctValue[11]+"#"+zctValue[12]+"#"+monthDay+zctValue[15]);
				paramMap.put("dtBank", zctValue[22]!=null ?zctValue[22].replace("\r", ""):"");
				saveExtPayData(user,amoutSelf,paramMap);
				log.info(logPrefix + " end saveExtPayData");
			}
		}

		ApplicationUtils apputil = new ApplicationUtils();
		OpCgTransforVo opCgforVo = new OpCgTransforVo();
		opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		opCgforVo.setAggregateAmount(amountSt); //需支付总金额
		BigDecimal amtPi=new BigDecimal(amountPi);
		opCgforVo.setMedicarePayments(amountSt.subtract(amtPi));//医保支付
		opCgforVo.setPatientsPay(amtPi);//现金支付
		opCgforVo.setXjzf(amtPi);//现金支付
		//调用预结算接口 countOpcgAccountingSettlement
		log.info(logPrefix + " begin 调用预结算接口 countOpcgAccountingSettlement");
		ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
		log.info(logPrefix + " end 调用预结算接口 countOpcgAccountingSettlement");
		if(response.getStatus()!=0){
			platformTransactionManager.rollback(status);
			// 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
			return msgPubService.createAckMsgForDft("AE","HIS接口【YjSettle】调用失败:"+response.getDesc()+response.getErrorMessage()+"",resAckMap);
		}
		opCgforVo = (OpCgTransforVo) response.getData();
		opCgforVo.setPkPv(SDMsgUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(SDMsgUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入

		//付款数据构建
		List<BlDeposit> depositList = new ArrayList<BlDeposit>();
		BlDeposit deposit=new BlDeposit();
		deposit.setDtPaymode(dtPaymode);
		deposit.setAmount(amtPi);
		deposit.setFlagAcc("0");
		deposit.setDelFlag("0");
		deposit.setPayInfo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//第三方订单号
		depositList.add(deposit);
		opCgforVo.setBlDeposits(depositList);
		opCgforVo.setAggregateAmount(amountSt); //需支付总金额
		opCgforVo.setMedicarePayments(amountSt.subtract(amtPi));//医保支付
		opCgforVo.setPatientsPay(amtPi);//患者支付
		opCgforVo.setAmtInsuThird(amountSt.subtract(amtPi));

		log.info(logPrefix + " begin mergeOpcgAccountedSettlement");
		ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
		log.info(logPrefix + " end mergeOpcgAccountedSettlement");
		if(respSettle.getStatus()!=0){
			platformTransactionManager.rollback(status);
			return msgPubService.createAckMsgForDft("AE","HIS接口【Settle】调用失败:"+response.getDesc()+respSettle.getErrorMessage()+"",resAckMap);
		}
		//更新pkSettle
		opCgforVo = (OpCgTransforVo) respSettle.getData();
		//更新pkSettle医保相关表
		msgPubService.updatePkSettle(doYbRetData,opCgforVo);
		log.info(logPrefix + " end updatePkSettle");
		//更新pkSettle,第三方支付相关表
		if(!"".equals(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"))){
			msgPubService.updateExtPayData(paramMap,opCgforVo);
			log.info(logPrefix + " end updateExtPayData");
		}
		//提交事务
		platformTransactionManager.commit(status);
		log.info(logPrefix + " end commit status");

		//生成电子票据
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		log.info(logPrefix + " end getTransaction");
		try {
			log.info(logPrefix + " begin mzOutElectInv");
			Map<String, Object> invMap = electInvService.mzOutElectInv(opCgforVo.getPkPv(), user, opCgforVo.getPkSettle());
			log.info(logPrefix + " end mzOutElectInv");
			resAckMap.putAll(invMap);
			platformTransactionManager.commit(status2);
			log.info(logPrefix + " end commit status2");
		} catch (Exception e) {
			platformTransactionManager.rollback(status2);
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败：{}",e.getClass());
			resAckMap.put("result",e.getClass()+e.getMessage());
		}
		//反馈消息
		log.info(logPrefix + " begin successDftAckParamCreate");
		msg = msgPubService.successDftAckParamCreate(opCgforVo,dft,ft1,resAckMap,codeOp,pvencounter);
		log.info(logPrefix + " end successDftAckParamCreate");
		return msg;
	}
	
	
	/**
     * 多系统接口公用（微信自助机体检公用）
     * 保存第三方支付数据
     * @param user
     * @param amountPi
     * @param paramMap
     */
	private void saveExtPayData(User user,BigDecimal amountPi,Map<String,Object> paramMap){
		BlExtPay extPay=new BlExtPay();
		extPay.setPkExtpay(NHISUUID.getKeyId());
		extPay.setPkOrg(user.getPkOrg());
		extPay.setAmount(amountPi);
		extPay.setFlagPay("1");//支付标志
		String date = SDMsgUtils.getPropValueStr(paramMap, "jyrq")+SDMsgUtils.getPropValueStr(paramMap, "jysj");
		extPay.setDateAp(DateUtils.strToDate(date));//请求时间
		extPay.setDatePay(DateUtils.strToDate(date));//支付时间
		extPay.setSerialNo(SDMsgUtils.getPropValueStr(paramMap, "SerialNo"));//订单号
		extPay.setTradeNo(SDMsgUtils.getPropValueStr(paramMap, "TradeNo"));//订单号
		extPay.setSysname("Y160");//系统名称
		extPay.setDescPay(SDMsgUtils.getPropValueStr(paramMap, "DescPay"));
		extPay.setResultPay(SDMsgUtils.getPropValueStr(paramMap, "ResultPay"));
		extPay.setEuPaytype(SDMsgUtils.getPropValueStr(paramMap, "dtPaymode"));
		extPay.setCreator(user.getPkEmp());
		extPay.setCreateTime(new Date());
		extPay.setTs(new Date());
		extPay.setDelFlag("0");
		extPay.setDtBank(SDMsgUtils.getPropValueStr(paramMap, "dtBank"));
		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		DataBaseHelper.insertBean(extPay);
		platformTransactionManager.commit(status);
	}
	
	/**
	 * 发送微信退费申请消息
	 * @param pkSettle
	 */
	public WeChatCancelResultVo sendCancelApplyOp(String pkSettle,String type) {
		Map<String,Object> resMapvo = msgMapper.querySettleDataByPkSettle(pkSettle);
		if(resMapvo==null){
			throw new RuntimeException("未查询到第三方关联的订单记录！");
		}
		WeChatCancelResultVo resultvo=new WeChatCancelResultVo();
		ZAM_ZRB zam_zrb = new ZAM_ZRB();
		String msgId = SDMsgUtils.getMsgId();
		resMapvo.put("msgid",msgId);
		resMapvo.put("msgtype", "ZAM");
		resMapvo.put("triggerevent", "ZRB");
		Map<String, Object> pidMap = createOpMsg.qrySdOpPID_PV1(resMapvo);
		try {
			createOpMsg.createMSHMsg(zam_zrb.getMSH(), resMapvo);
			createOpMsg.createPIDMsg(zam_zrb.getPID(), pidMap);
			createOpMsg.createPV1Msg(zam_zrb.getPV1(), pidMap);
			createZPMMsg(zam_zrb.getZPM(0),resMapvo,type);
		} catch (DataTypeException e) {
			throw new RuntimeException("数据构建发生错误！");
		}
		try {
			Object resAck= sDHl7MsgHander.sendMsgFor(msgId, SDMsgUtils.getParser().encode(zam_zrb));
			if(CommonUtils.isNotNull(resAck)){
				Map<String,List<Map<String,Object>>> resMap=SDMsgUtils.resolueMessage(resAck.toString());
				if(resMap.containsKey("MSA")){
					List<Map<String,Object>> msaList=resMap.get("MSA");
					
					if("AA".equals(msaList.get(0).get("1"))){//成功
						resultvo.setStatuscode("1");
					}else{//失败
						String messageTxt=msaList.get(0).get("3")!=null ?msaList.get(0).get("1").toString():"";
						resultvo.setStatuscode("0");
						resultvo.setMessageTxt(messageTxt);
					}
				}
				
				if(resMap.containsKey("ZPD")){
					List<Map<String,Object>> zpdList=resMap.get("ZPD");
					
					if("0".equals(zpdList.get(0).get("2"))){//成功
						WeChatCancelResDataVo datavo=new WeChatCancelResDataVo();
						Map<String,Object> zpdData=zpdList.get(0);
						datavo.setApprtNo(SDMsgUtils.getPropValueStr(zpdData, "3"));
						datavo.setOrderNo(SDMsgUtils.getPropValueStr(zpdData, "4"));
						datavo.setTrandIdNo(SDMsgUtils.getPropValueStr(zpdData, "5"));
						datavo.setDatePay(SDMsgUtils.getPropValueStr(zpdData, "6"));
						String amountIns=SDMsgUtils.getPropValueStr(zpdData, "7");
						if(CommonUtils.isNotNull(amountIns)){
							datavo.setAmountInsu(new BigDecimal(amountIns));
						}
						String amountPi=SDMsgUtils.getPropValueStr(zpdData, "8");
						if(CommonUtils.isNotNull(amountPi)){
							datavo.setAmountPi(new BigDecimal(amountPi));
						}
						
						resultvo.setStatuscode("1");
						resultvo.setData(datavo);
					}else{//失败
						Map<String,Object> zpdData=zpdList.get(0);
						resultvo.setStatuscode("0");
						String msgStrSplit=SDMsgUtils.getPropValueStr(zpdData, "1");
						if(CommonUtils.isNotNull(msgStrSplit)){
							String [] msgStrs=msgStrSplit.split("^");
							if(msgStrs.length>1){
								resultvo.setMessageTxt(msgStrs[1]);
							}
						}
						
					}
				}
			}else{
				resultvo.setStatuscode("0");
				resultvo.setMessageTxt("未获取到消息反馈内容！");
			}
		} catch (HL7Exception e) {
			throw new RuntimeException("消息发送通道失败！");
		}
		return resultvo;
	}
	
	/**
	 * 构建ZPM 消息节点
	 * @param zpm
	 * @param paramMap
	 * @param type
	 * @throws DataTypeException
	 */
	private void createZPMMsg(ZPM zpm, Map<String, Object> paramMap,String type) throws DataTypeException {
		//1	Mzhm	门诊号码	ST	O	50
		zpm.getMzhm().setValue(SDMsgUtils.getPropValueStr(paramMap, "codeOp"));
		//2	Fph		发票号	ST	R	50
		zpm.getFph().setValue(SDMsgUtils.getPropValueStr(paramMap, "reptNo"));
		//3	YjsId	预结算ID	ST	R	75
		zpm.getYjsId().setValue(SDMsgUtils.getPropValueStr(paramMap, ""));
		//4	Ybkh	医保卡号	ST	O	50
		String insuNo=SDMsgUtils.getPropValueStr(paramMap, "codeMedino");
		zpm.getYbkh().setValue(insuNo);
		//5	Mzrq	门诊日期	DT	O	8
		zpm.getMzrq().setValue(SDMsgUtils.getPropValueStr(paramMap, "dateBegin"));
		//6	OrderId	微信（第三方）订单号	ST	R	75
		zpm.getOrderId().setValue(SDMsgUtils.getPropValueStr(paramMap, "serialNo"));
		//7	Brxm	姓名	ST	O	50
		zpm.getBrxm().setValue(SDMsgUtils.getPropValueStr(paramMap, "namePi"));
		//8	Sflb	收费类别	ST	O	1		0代表门诊 1 代表急诊
		String euPvtype=SDMsgUtils.getPropValueStr(paramMap, "euPvtype");
		if("1".equals(euPvtype)){
			zpm.getSflb().setValue("0");
		}else if ("2".equals(euPvtype)){
			zpm.getSflb().setValue("1");
		}
		//9	Ybje	医保金额	MO	O	12
		zpm.getYbje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountInsu"));
		//10 Zfje	自费金额	MO	O	12
		zpm.getZfje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountPi"));
		//11 Jsfs	结算方式	ST	O	50
		if(CommonUtils.isEmptyString(insuNo)){
			zpm.getJsfs().setValue("1");//自费
		}else{
			zpm.getJsfs().setValue("7");//医保
		}
		//12 sqdlx	申请单类型	ST	R	10		1.挂号退费    2、诊间支付退费  3、住院预交金退费-待定
		zpm.getsqdlx().setValue(type);
		//34 Zje	总金额	MO	O	12
		zpm.getZje().setValue(SDMsgUtils.getPropValueStr(paramMap, "amountSt"));
		//38 Paymethod	支付方式	e.g. WX
		zpm.getPaymethod().setValue(SDMsgUtils.getPropValueStr(paramMap, "dtBank"));
		//39 PayChannel	渠道		e.g.JY160
		zpm.getPayChannel().setValue("JY160");
		}
	
	
    /**
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
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		//总金额
		String priceje = CommonUtils.getString(SDMsgUtils.getPropValueStr(map,"priceje"),"0");
		//基金支付金额
		String Tchj = CommonUtils.getString(SDMsgUtils.getPropValueStr(map,"Tchj"),"0");
		//医保支付总金额
		String cashInsur = CommonUtils.getString(SDMsgUtils.getPropValueStr(map,"cashInsur"),"0");
		//自费金额
		String cashSelf = CommonUtils.getString(SDMsgUtils.getPropValueStr(map,"cashSelf"),"0");

    	InsSzybVisit insSzybVisit = new InsSzybVisit();
    	String pkVisit = NHISUUID.getKeyId();
    	insSzybVisit.setPkVisit(pkVisit);
    	insSzybVisit.setPkOrg(Constant.PKORG);
    	insSzybVisit.setPkOrg(regvo.getPkOrg());
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
    	insSzybVisit.setCreator(pkOpDoctor);
		insSzybVisit.setPassword("000000");
    	insSzybVisit.setCreateTime(new Date());
    	insSzybVisit.setTs(new Date());
    	insSzybVisit.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybVisit);


    	InsSzybSt insSzybSt = new InsSzybSt();
    	String pkInsst = NHISUUID.getKeyId();
    	insSzybSt.setPkInsst(pkInsst);
    	insSzybSt.setPkOrg(Constant.PKORG);
    	insSzybSt.setPkVisit(pkVisit);
    	insSzybSt.setPkOrg(regvo.getPkOrg());
    	insSzybSt.setCodeHpst(SDMsgUtils.getPropValueStr(map,"ywh"));
    	insSzybSt.setPkHp(regvo.getPkHp());
    	insSzybSt.setPkPv(regvo.getPkPv());
    	insSzybSt.setPkPi(regvo.getPkPi());
    	insSzybSt.setPkSettle(regvo.getPkSettle());
    	insSzybSt.setPvcodeIns(SDMsgUtils.getPropValueStr(map,"Djh"));
    	insSzybSt.setDateSt(new Date());
    	insSzybSt.setCodeSerialno(SDMsgUtils.getPropValueStr(map, "Jsywh"));
		//总金额
    	insSzybSt.setAmount(Double.valueOf(priceje));
    	insSzybSt.setCodeCenter("");
    	insSzybSt.setCodeOrg(Constant.CODEORG);
    	insSzybSt.setNameOrg(Constant.CODEORG);
    	insSzybSt.setCodeSerialno(SDMsgUtils.getPropValueStr(map,"Jsywh"));
    	insSzybSt.setCreator(pkOpDoctor);
    	insSzybSt.setCreateTime(new Date());
    	insSzybSt.setTs(new Date());
    	insSzybSt.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybSt);

    	InsSzybStCity insSzybStCity = new InsSzybStCity();
    	String pkInsstCity = NHISUUID.getKeyId();
    	insSzybStCity.setPkOrg(Constant.PKORG);
    	insSzybStCity.setPkInsstcity(pkInsstCity);
    	insSzybStCity.setPkInsst(pkInsst);
    	insSzybStCity.setDtMedicate(SDMsgUtils.getPropValueStr(map,"AkA130Code"));//	医疗类别
    	insSzybStCity.setAmtJjzf(Double.valueOf(Tchj));//基金支付金额
    	insSzybStCity.setAmtGrzhzf(Double.valueOf(cashInsur)-Double.valueOf(Tchj));//个人帐户支付金额
    	insSzybStCity.setAmtGrzf(Double.valueOf(cashSelf));//个人支付金额
    	//insSzybStCity.setAmtGrzh(0.0);//个人账户金额
    	insSzybStCity.setCreator(pkOpDoctor);
    	insSzybStCity.setCreateTime(new Date());
    	insSzybStCity.setTs(new Date());
    	insSzybStCity.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybStCity);

    	List<InsSzybStCitydt> InsSzybStCitydtList = new ArrayList<InsSzybStCitydt>();
    	List<Map<String,Object>> mapList = (List<Map<String, Object>>) map.get("list");
    	if(mapList!=null && mapList.size()>0){
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
    	}
    	InsSzybVisitCity  insSzybVisitCity = new InsSzybVisitCity();
    	String pkVisitcity = NHISUUID.getKeyId();
    	insSzybVisitCity.setPkVisitcity(pkVisitcity);
    	insSzybVisitCity.setPkOrg(regvo.getPkOrg());
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
		case "01":
			 aac058 = "01";
			break;
		case "02":
			 aac058 = "02";
			break;
		case "03":
			 aac058 = "04";
			break;
		case "06":
			 aac058 = "04";
			break;
		case "07":
			 aac058 = "05";
			break;
		case "08":
			 aac058 = "06";
			break;
		case "09":
			 aac058 = "07";
			break;
		case "10":
			 aac058 = "08";
			break;
		case "99":
			 aac058 = "99";
			break;
		default:
			break;
		}
    	insSzybVisitCity.setAac058(aac058);
    	insSzybVisitCity.setAac147(regvo.getIdNo());
    	insSzybVisitCity.setAac002(regvo.getIdNo());//社会保障号码
    	insSzybVisitCity.setAac003(regvo.getNamePi());
    	String dtSex = regvo.getDtSex();
    	String aac004 = "";
    	switch (dtSex) {
		case "02":
			aac004 = "1";
			break;
		case "03":
			aac004 = "2";
			break;
		default:
			aac004 = "9";
			break;
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
    	insSzybVisitCity.setCreator(pkOpDoctor);
    	insSzybVisitCity.setCreateTime(new Date());
    	insSzybVisitCity.setTs(new Date());
    	insSzybVisitCity.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybVisitCity);

    }

    /**
     * 创建预约执行确认成功ACK_P03的消息
     * @param map
     * @return
     * @throws DataTypeException
     */
    private String createAppRegPayHL7Info(Map<String, Object> map) throws DataTypeException{
    	ACK_P03  ackp03 = new ACK_P03();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = ackp03.getMSH();
		MSA msa = ackp03.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
		msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map, "sendApp"));//@todo
		try {

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
				timeNo.append(beginStr.toString().substring(10, 14));
			}else{
				timeNo.append("0000");
			}
			if(ticketNo.length()==1){
				timeNo.append("0").append(ticketNo);
			}else{
				timeNo.append(ticketNo);
			}
			//PV1-3.9 当前时间就诊的顺序号
			ackp03.getPV1().getAssignedPatientLocation().getLocationDescription().setValue(timeNo.toString());
			//PV1-5  预约号
			ackp03.getPV1().getPreadmitNumber().getID().setValue(SDMsgUtils.getPropValueStr(map,"code"));
			//PV1-19 就诊流水号
			ackp03.getPV1().getVisitNumber().getID().setValue(SDMsgUtils.getPropValueStr(map,"codePv"));

			//微信 不返回发票信息 电子发票数据
			//EbillCode	电子票据代码
			ackp03.getZPO().getEbillCode().setValue("");
			//EbillNo	电子票据号码
			ackp03.getZPO().getEbillNo().setValue("");
			//CheckCode	电子校验码
			ackp03.getZPO().getCheckCode().setValue("");
			//EbillDate	电子票据生成时间
			ackp03.getZPO().getEbillDate().setValue("");
			//EbillQRCode	电子票据二维码效验数据
			ackp03.getZPO().getEbillQRCode().setValue("");

			msg =  SDMsgUtils.getParser().encode(ackp03);
		} catch (Exception e) {
			 log.info("消息编码为"+SDMsgUtils.getPropValueStr(map,"oldmsgid")+"的ACK_P03消息拼接异常");
		}

    	return msg;
    }
    /**
     * 创建预约执行确认失败ACK_P03的消息
     * @param map
     * @return
     * @throws DataTypeException
     */
    private String createAppRegPayHL7ErroInfo(Map<String, Object> map,String errorMsg) throws DataTypeException{
    	ACK_P03  ackp03 = new ACK_P03();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = ackp03.getMSH();
		MSA msa = ackp03.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
		msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map, "sendApp"));
		try {

			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(map,"oldmsgid"));
			msa.getTextMessage().setValue(errorMsg);
			msa.getExpectedSequenceNumber().setValue("100");
			msa.getDelayedAcknowledgmentType().setValue("F");
			msg =  SDMsgUtils.getParser().encode(ackp03);
		} catch (Exception e) {
			 log.info("消息编码为"+SDMsgUtils.getPropValueStr(map,"oldmsgid")+"的ACK_P03消息拼接异常");
		}

    	return msg;
    }

    /**
     * 生成PiMasterRegVo中的计费实体消息值   必传pk_sch
     * @param appRegParam
     * @return
     */
    private PiMasterRegVo getPiMaterRegItemPriceVo(PiMasterRegVo piMaster,Map<String, Object> map){
	   //挂号记费项目参数
	    String sqlPi = "select pi.pk_pi,pi.name_pi,pi.pk_org,pi.pk_picate,appt.pk_schsrv,piCate.flag_Spec,to_char(pi.birth_date,'yyyy-MM-dd') date_birth   from 	sch_appt appt left join  pi_master pi on pi.pk_pi = appt.pk_pi  left join  	pi_cate  piCate on piCate.Pk_Picate = pi.pk_picate  where appt.code = ?  and pi.del_flag = '0'";
		Map<String, Object>  bdPiMap = DataBaseHelper.queryForMap(sqlPi, SDMsgUtils.getPropValueStr(map,"code"));
		Map<String, Object> insurParam = new HashMap<>();
		insurParam.put("dateBirth", SDMsgUtils.getPropValueStr(bdPiMap,"dateBirth"));
		insurParam.put("flagSpec", SDMsgUtils.getPropValueStr(bdPiMap,"flagSpec"));
		insurParam.put("pkPicate", SDMsgUtils.getPropValueStr(bdPiMap,"pkPicate"));
		insurParam.put("pkSchsrv", SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"));
		insurParam.put("pkInsu", SDMsgUtils.getPropValueStr(map,"pkHp"));
		insurParam.put("nameInsu",SDMsgUtils.getPropValueStr(map,"name"));
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
	   String amountPi = SDMsgUtils.getPropValueStr(map, "amountPi");
	   amountPi = "".equals(amountPi)?"0":amountPi;
	   //总金额
	   BigDecimal price = new BigDecimal(StringUtils.isBlank(amountPi)?"0":amountPi);
	   blDeposit.setAmount(price);
	   blDeposit.setDtPaymode("15");//默认99
	   blDeposit.setPkOrg(Constant.PKORG);
	   //String  payway = SDMsgUtils.getPropValueStr(map,"payWay");
	   //支付方式
	   if("01".equals(SDMsgUtils.getPropValueStr(map, "insurTypeCode"))){
		 blDeposit.setDtPaymode(WeXinPayType);
		 //第三方支付金额
		 blDeposit.setExtAmount(price);
		 blDeposit.setPayInfo(SDMsgUtils.getPropValueStr(map, "SerialNo"));//系统产生系统订单号
	   }
	   blDepositList.add(blDeposit);
	   piMaster.setDepositList(blDepositList);
	   piMaster.setInvStatus("-2");
	   return piMaster;
    }

   /**
	 * 保存就诊记录
	 * @param master
	 * @param pkPv
	 * @return
	 */
	private PvEncounter savePvEncounter(PiMasterRegVo master,String pkPv,Map<String,Object> schres,String pkEmpPhy){
		//是否允许挂号
		allowReg(master,pkEmpPhy);
		// 保存就诊记录
		//boolean jz = "2".equals(master.getEuPvtype());
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		pvEncounter.setPkPi(master.getPkPi());
		pvEncounter.setPkDept(master.getPkDept());//就诊科室
		pvEncounter.setCodePv(ApplicationUtils.getCode("0301"));
		//pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : PvConstant.ENCOUNTER_EU_PVTYPE_1 ); // 急诊|门诊
		pvEncounter.setEuPvtype(master.getEuPvtype());
		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
		pvEncounter.setNamePi(master.getNamePi());
		pvEncounter.setDtSex(master.getDtSex());
		pvEncounter.setAgePv(ApplicationUtils.getAgeFormat(master.getBirthDate(),null));
		pvEncounter.setAddress(master.getAddress());
		pvEncounter.setFlagIn("0");
		pvEncounter.setFlagSettle("1");
		pvEncounter.setDtMarry(master.getDtMarry());
		pvEncounter.setPkInsu(master.getPkHp());
		pvEncounter.setPkPicate(master.getPkPicate());
		pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
		pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
		pvEncounter.setDateReg(new Date());
		//如果是急诊 为当前时间
		if("2".equalsIgnoreCase(master.getEuPvtype())){
			pvEncounter.setDateBegin(new Date());
		}else{
			//如果是预约挂号，开始日期为预约日期
			if(master.getDateAppt()!=null) {
				//预约开始时间应该为日期+预约号所在的开始时间（bug-20723）
				String sql = "select time_begin from bd_code_dateslot where pk_dateslot = ?";
				Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, master.getPkDateslot());
				String dateStr = DateUtils.dateToStr("yyyyMMdd", master.getDateAppt());
				if (DateUtils.dateToStr("yyyyMMdd", new Date()).equals(dateStr)) {//如果预约日期为当前日期，开始时间为挂号时间
					pvEncounter.setDateBegin(master.getDateReg());
				} else {
					String dateAppt = dateStr + " " + queryForMap.get("timeBegin").toString();
					master.setDateAppt(DateUtils.strToDate(dateAppt, "yyyyMMdd HH:mm:ss"));
					pvEncounter.setDateBegin(master.getDateAppt());
				}
			}else{
				pvEncounter.setDateBegin(master.getDateReg());//挂号的排班日期
			}
		}
		pvEncounter.setFlagCancel("0");
		pvEncounter.setDtIdtypeRel("01");
		pvEncounter.setDtPvsource(master.getDtSource());
		pvEncounter.setNameRel(master.getNameRel());
		pvEncounter.setIdnoRel(master.getIdnoRel());
		pvEncounter.setTelRel(master.getTelRel());
		pvEncounter.setEuPvmode("0");
		pvEncounter.setFlagSpec(isSpec(master.getPkSrv())?"1":"0");
		pvEncounter.setEuStatusFp("0");
		pvEncounter.setEuLocked("0");
		pvEncounter.setEuDisetype("0");
		long millis = System.currentTimeMillis();
		
		String pkDeptArea = pvInfoPubService.getPkDeptArea(master.getPkSchres());
   		pvEncounter.setPkDeptArea(pkDeptArea);

		DataBaseHelper.insertBean(pvEncounter);
		log.info(Long.toString(millis)+"~~~微信插入Pv_Encounter耗时~~~~~"+(System.currentTimeMillis() - millis));
		//regSyxService.addInsGzgyPv(master, pvEncounter);

		return pvEncounter;
	}
	
	private void allowReg(PiMasterRegVo master,String pkEmpPhy){
		if(master == null)
			return;

		String date = master.getDateAppt()!=null?DateUtils.formatDate(master.getDateAppt(), "yyyyMMdd"):
			(master.getDateReg()!=null?DateUtils.formatDate(master.getDateReg(), "yyyyMMdd"):null);
		if(StringUtils.isNotBlank(date)) {
			if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.pk_emp_phy=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{master.getPkPi(),date,pkEmpPhy}) >0){
				throw new BusException("该患者已经存在当前日期和当前医生的挂号记录！");
			}
		}
	}

	/**
	 * 依据服务主键，判断当前服务的服务类型 是否为 特诊
	 * @param pkSrv
	 * @return
	 */
	private boolean isSpec(String pkSrv){
		if(StringUtils.isNotBlank(pkSrv)){
			SchSrv srv = DataBaseHelper.queryForBean("select eu_srvtype from sch_srv where pk_schsrv=?", SchSrv.class, pkSrv);
			return srv!=null && "2".equals(srv.getEuSrvtype());
		}
		return false;
	}
	
	  /**
	    *	处理预挂号退费消息
	    *	1.验证是否存在该支付信息，且未就诊
	    *	2.处理费用信息明细表
	    *	3.发送P03反馈消息
	    *
	    */
	private String cancelPiPayReg(Map<String, Object> appRegParam){

			    String code =SDMsgUtils.getPropValueStr(appRegParam,"code");
			    String sql = " select appt.pk_sch,appt.ticket_no,appt.pk_schappt,app.pk_pv,pi.pk_pi,pi.name_pi,pi.pk_org,pv.flag_cancel,pv.eu_status   from sch_appt_pv app left join sch_appt appt on   appt.pk_schappt = app.pk_schappt left join pi_master pi on pi.pk_pi = appt.pk_pi left join pv_encounter  pv on pv.pk_pv = app.pk_pv"+
			    			 "  where appt.code = ?  ";
			    Map<String, Object> pkSchMap =  DataBaseHelper.queryForMap(sql, code);
			    if(pkSchMap==null)
			    	throw new BusException("未查询到该预约号【"+code+"】对应的预约信息或患者已被接诊");
			    if("1".equals(SDMsgUtils.getPropValueStr(pkSchMap,"flagCancel")))
				    throw new BusException("已取消就诊，请勿重复推送");
			    if(!"0".equals(SDMsgUtils.getPropValueStr(pkSchMap,"euStatus")))
			    	 throw new BusException("已就诊，无法取消登记退费");

			    String pkSch = SDMsgUtils.getPropValueStr(pkSchMap,"pkSch");
			    appRegParam.put("pksch", pkSch);
			    appRegParam.put("ticketno", SDMsgUtils.getPropValueStr(pkSchMap,"ticketNo"));
			    appRegParam.put("pkschappt", SDMsgUtils.getPropValueStr(pkSchMap,"pkSchappt"));
				PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);
				regvo.setPkPv(SDMsgUtils.getPropValueStr(pkSchMap,"pkPv"));
				regvo.setPkSch(pkSch);
				regvo.setPkAppt(SDMsgUtils.getPropValueStr(pkSchMap,"pkSchappt"));
				regvo.setPkDept(Constant.PKDEPT);//门诊收费处
				User user=new User();
				user.setPkDept(Constant.PKDEPT);
				user.setPkEmp(Constant.PKWECHAT);
				user.setNameEmp(Constant.NAMEWECHAT);
				user.setPkOrg(Constant.PKORG);
				UserContext.setUser(user);
				cancelAppPayRegistPi(regvo);
				
				
				StringBuffer sqlapppv=new StringBuffer();
				sqlapppv.append("update sch_appt set eu_status=?,flag_cancel=? ,pk_emp_cancel=?,name_emp_cancel=? ,date_cancel=?");
				sqlapppv.append(" where exists(select 1 from sch_appt_pv inner join pv_encounter on pv_encounter.pk_pv=sch_appt_pv.pk_pv ");
				sqlapppv.append(" where sch_appt_pv.pk_schappt=sch_appt.pk_schappt and pv_encounter.pk_pv=? )");
				DataBaseHelper.execute(sqlapppv.toString(), new Object[]{"9","1",user.getPkEmp(),user.getNameEmp(),new Date(),regvo.getPkPv()});
				
				//更新可用号数
		   		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[] { pkSch });
		   		//还原号表
		   		DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where ticketno = ? and pk_sch = ?", new Object[] {SDMsgUtils.getPropValueStr(appRegParam,"ticketno") ,regvo.getPkSch()});


				//拼接消息实例
				SRR_S01 srr = new SRR_S01();
				String msgId = SDMsgUtils.getMsgId();
				String msg = "";
				MSH msh = srr.getMSH();
				MSA msa = srr.getMSA();
				SDMsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
				try {
					msa.getAcknowledgementCode().setValue("AA");
					msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(appRegParam,"oldmsgid"));
					msa.getTextMessage().setValue("成功");
					msa.getExpectedSequenceNumber().setValue("100");//???????
					msa.getDelayedAcknowledgmentType().setValue("F");
					msg =  SDMsgUtils.getParser().encode(srr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			return msg;
	   }

	 /**
     *	创建SRRS01消息返回
     * @param appRegParam
     * @return
	 * @throws HL7Exception 
     */
    private  String createSRRS01(Map<String, Object> appRegParam) throws HL7Exception{

    	SRR_S01 srr = new SRR_S01();
		String msgId = SDMsgUtils.getMsgId();
		String msg = "";
		MSH msh = srr.getMSH();
		MSA msa = srr.getMSA();
		SDMsgUtils.createMSHMsg(msh, msgId, "SRR", "S01");
		msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(appRegParam, "sendApp"));//@todo
		String appCode =  SDMsgUtils.getPropValueStr(appRegParam,"appCode");
		//Map<String, Object>  priceMap = sDOpMsgMapper.querySDOpSchAllInfo(appRegParam).get(0);
		//查询费用
//		StringBuffer sql = new StringBuffer()
//				.append("select sum(price) price from SCH_APPT appt ")
//				.append("inner join sch_srv_ord sord on appt.PK_SCHSRV=sord.PK_SCHSRV ")
//				.append("inner join bd_ord_item oi on oi.PK_ORD=sord.PK_ORD ")
//				.append("inner join BD_ITEM item on item.PK_ITEM=oi.PK_ITEM ")
//				.append("where appt.code=? and sord.eu_type = '0' group by appt.CODE "); // sord.eu_type = '0' 患者  1为本院职工
//		Map<String, Object>  priceMap = DataBaseHelper.queryForMap(sql.toString(), appCode );
//		//医保号使用 0304
    	if(!"01".equals(SDMsgUtils.getPropValueStr(appRegParam, "insurTypeCode"))){
    		appRegParam.put("yb", ApplicationUtils.getCode("0304"));
    	}
		String sqlPi = "select pi.id_no, pi.pk_pi,pi.name_pi,pi.pk_org,pi.pk_picate,appt.pk_schsrv,piCate.flag_Spec,to_char(pi.birth_date,'yyyy-MM-dd') date_birth   from 	sch_appt appt left join  pi_master pi on pi.pk_pi = appt.pk_pi  left join  	pi_cate  piCate on piCate.Pk_Picate = pi.pk_picate  where appt.code = ?  and pi.del_flag = '0'";
		Map<String, Object>  bdPiMap = DataBaseHelper.queryForMap(sqlPi, SDMsgUtils.getPropValueStr(appRegParam,"appCode"));
		Map<String, Object> insurParam = new HashMap<>();
		insurParam.put("dateBirth", SDMsgUtils.getPropValueStr(bdPiMap,"dateBirth"));
		insurParam.put("flagSpec", SDMsgUtils.getPropValueStr(bdPiMap,"flagSpec"));
		insurParam.put("pkPicate", SDMsgUtils.getPropValueStr(bdPiMap,"pkPicate"));
		insurParam.put("pkSchsrv", SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"));
		insurParam.put("pkInsu", SDMsgUtils.getPropValueStr(appRegParam,"pkHp"));
		insurParam.put("nameInsu",SDMsgUtils.getPropValueStr(appRegParam,"name"));
		insurParam.put("euPvType","1");
		insurParam.put("pkEmp","");
		String idNo=SDMsgUtils.getPropValueStr(appRegParam, "identifyno");
		String param = JsonUtil.writeValueAsString(insurParam);
		User user = new User();
	    user.setPkEmp(SDMsgUtils.getPropValueStr(bdPiMap,"pkPi"));
	    user.setNameEmp(SDMsgUtils.getPropValueStr(bdPiMap,"namePi"));
	    user.setPkOrg(SDMsgUtils.getPropValueStr(bdPiMap,"pkOrg"));
	    List<ItemPriceVo> listItem  = regSyxService.getItemBySrv(param,user);
	    //总金额
	    double price = 0.0;
	    if(listItem!=null && listItem.size()>0){
	    	for(ItemPriceVo item : listItem){
		    	price += item.getPrice();
		    }
	    }else{
	    	listItem=getItemPriceVo(idNo,bdPiMap);
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
		nte0.getComment(0).setValue(String.valueOf(price));//自费扣费金额
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
		pv1.getPatientClass().setValue(SDMsgUtils.getPropValueStr(appRegParam,"euPvTypeMsg"));//O门诊  I住院
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
//			if(bdPiMap==null){
//				throw new BusException("未查询到该预约号的医保信息:"+SDMsgUtils.getPropValueStr(appRegParam,"appCode")+"");
//			}
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
			srr.getSCHEDULE().getPATIENT().getPV1().getTotalCharges().setValue(String.valueOf(listItem.get(0).getPrice()));
		}
		msg =  SDMsgUtils.getParser().encode(srr);

		return msg;
    }
    
    /**
     * 获取资源价格
     * @param idNo
     * @param bdPiMap
     * @return
     */
    private List<ItemPriceVo> getItemPriceVo(String idNo,Map<String,Object> bdPiMap){
    	List<ItemPriceVo> listItem=new ArrayList<>();
    	String euPitype ="0";
    	if(CommonUtils.isNotNull(idNo)){
    		StringBuffer sqljob=new StringBuffer();
    		sqljob.append("select count(1) from bd_ou_empjob job");
    		sqljob.append(" inner join bd_ou_employee emp on emp.pk_emp=job.pk_emp");
    		sqljob.append(" where emp.idno=? and (job.date_left is null or job.date_left<to_date(?,'YYYYMMDDHH24MISS'))");
    		Integer jobCount=DataBaseHelper.queryForScalar(sqljob.toString(), Integer.class, new Object[]{idNo, DateUtils.getDate("yyyMMddHHmmss")});
    		if(jobCount>0){
    			euPitype="1";
    		}
    	}
    	
    	StringBuffer sqlItem=new StringBuffer();
    	sqlItem.append("select item.code,item.name name_cg,item.price,item.pk_item from  sch_srv srv");
    	sqlItem.append(" left join sch_srv_ord ord on ord.pk_schsrv = srv.pk_schsrv  and ord.del_flag='0'");
    	sqlItem.append(" left join BD_ORD orddic on orddic.PK_ORD=ord.PK_ORD");
    	sqlItem.append(" left join BD_ITEM item on item.CODE=orddic.CODE");
    	sqlItem.append(" where srv.del_flag='0' and srv.flag_stop='0' and srv.pk_schsrv = ?");
    	sqlItem.append(" and (case when ord.EU_TYPE is null then '0' else ord.EU_TYPE end ) =?");
    	
    	if("1".equals(euPitype)){
    		listItem=DataBaseHelper.queryForList(sqlItem.toString(),ItemPriceVo.class , new Object[]{SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"),euPitype});
    	}
    	if("0".equals(euPitype) ||listItem==null ||listItem.size()==0){
    		listItem=DataBaseHelper.queryForList(sqlItem.toString(),ItemPriceVo.class , new Object[]{SDMsgUtils.getPropValueStr(bdPiMap,"pkSchsrv"),"0"});
    	}
    	return listItem;
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
		Map<String, Object>  codeDateslotSecMap =   DataBaseHelper.queryForMap("select pk_dateslotsec from bd_code_dateslot_sec where  to_char(to_date(time_begin,'hh24:mi:ss'),'hh24mi')= ?  and  to_char(to_date(time_end,'hh24:mi:ss'),'hh24mi') =?  ", SDMsgUtils.getPropValueStr(appRegParam,"begintime"),SDMsgUtils.getPropValueStr(appRegParam,"endtime"));
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
			 String begintime=SDMsgUtils.getPropValueStr(appRegParam,"begintime");
			 String endTime=SDMsgUtils.getPropValueStr(appRegParam,"endtime");
			 ticket=getUnusedAppTicketByTime(pkSch, SDMsgUtils.getPropValueStr(codeDateslotSecMap,"pkDateslotsec"), begintime, endTime);
		}else{
			//无号表方式处理排班表可用号数
			 ticket = ticketPubService.getUnusedAppTicketFromSch(pkSch,"");
		}
		PiMasterRegVo piMasregvo= null;
		//3.保存预约挂号信息（含保存患者信息）
		// 3.1.生成预约记录，写表sch_appt； 3.2.生成预约挂号记录，写表sch_appt_pv；3.3.更新排班表，更新sch_sch；
		try{
			User user=UserContext.getUser();
			piMasregvo= regSyxService.saveApptSchRegInfo(regvo, schSch, ticket, haveTickNo, user,false);
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
    
    /**
     * 获取可用预约号
     * @param pkSch
     * @return SchTicket
     */
	private SchTicket getUnusedAppTicketByTime(String pkSch,String pkDateslotsec,String timeStart,String timeEnd){
		// 处理号表and eu_pvtype = ?,euPvtype
		List<SchTicket> tickets = new ArrayList<>();
		Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select time_begin,time_end from bd_code_dateslot_sec where pk_dateslotsec=? ", pkDateslotsec);
		//判断时段编码是否为空
		if(MapUtils.isNotEmpty(slotSecMap)){
			StringBuffer sql=new StringBuffer();
			sql.append("select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24mi') >=? and TO_CHAR (end_time, 'hh24mi') <=? ");
			sql.append(" and DEL_FLAG = '0'  and FLAG_USED='0' and FLAG_STOP='0' and FLAG_APPT='1' ");
			sql.append(" order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ");
			tickets = DataBaseHelper.queryForList(sql.toString() , SchTicket.class, pkSch,timeStart,timeEnd);
		}else{
			tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0' order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
					SchTicket.class, pkSch);
		}	
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0' and flag_stop='0' and DEL_FLAG = '0' and FLAG_APPT = '1' ", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return ticket;
	}
    
    /**
	 * 处理医保结算数据
	 * @param dft 消息实例
	 * @param user 当前用户
	 * @param pvencounter 就诊信息
	 * @return resMap:{"status":"1:成功；0:失败" ,"data":"错误返回信息/成功需要返回数据"}
	 */
    private Map<String,Object> saveYbSettleData(DFT_P03 dft,User user,PvEncounter pvencounter){
    	Map<String,Object> resMap=new HashMap<String,Object>();
    	Map<String, List<Map<String, Object>>> resolueMessags = SDMsgUtils.resolueMessage(dft.toString());
    	Map<String, Object> map = resolueMessags.get("ZPR").get(0);
    	//ZPR-1 医疗机构编码
		//String yljgbm = SDMsgUtils.getPropValueStr(map, "1");
		//ZPR-2 医疗机构名称
		//String yljgmc = SDMsgUtils.getPropValueStr(map, "2");
		//ZPR-3 门诊流水号
		String mzlsh = SDMsgUtils.getPropValueStr(map, "3");
		//ZPR-4单据号
		String Djh = SDMsgUtils.getPropValueStr(map, "4");
		//ZPR-5医疗证号
		//String Ylzh = SDMsgUtils.getPropValueStr(map, "5");
		//ZPR-6电脑号
		//String Dnh =  SDMsgUtils.getPropValueStr(map, "6");
		//ZPR-7姓名
		//String xm = SDMsgUtils.getPropValueStr(map, "7");
		//ZPR-8金额
		String je = SDMsgUtils.getPropValueStr(map, "8");
		//现金合计 9
		String Xjhj = SDMsgUtils.getPropValueStr(map, "9");
		//记账合计 10
		String Jzhj = SDMsgUtils.getPropValueStr(map, "10");
		//结算序列号 11
		String Jsywh = SDMsgUtils.getPropValueStr(map, "11");
		//统筹合计 12
		String Tchj = SDMsgUtils.getPropValueStr(map, "12");
		//业务号 ywh 13
		String ywh = SDMsgUtils.getPropValueStr(map, "13");
		if(ywh!=null && ywh.length()>20){
			ywh = ywh.substring(0, 20);
		}
		resMap.put("amountPi", StringUtils.isBlank(Xjhj)?"0":Xjhj);//自费金额
		resMap.put("amountSt", StringUtils.isBlank(je)?"0":je);//合计金额
		resMap.put("amountInsu", StringUtils.isBlank(Jzhj)?"0":Jzhj);//医保金额
		resMap.put("amountFund", StringUtils.isBlank(Tchj)?"0":Tchj);//基金支付金额
		InsSzybSt insSzybSt=new InsSzybSt();
		insSzybSt.setPkInsst(NHISUUID.getKeyId());
		insSzybSt.setPkOrg(user.getPkOrg());//机构
		String visitSql="select * from ins_szyb_visit where del_flag='0'  and pk_pv=? and pvcode_ins=? order by date_reg desc ";
		List<InsSzybVisit> visitList = DataBaseHelper.queryForList(visitSql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv(),mzlsh});
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
		insSzybSt.setPvcodeIns(mzlsh);//就医登记号
		insSzybSt.setDateSt(new Date());//结算日期
		insSzybSt.setAmount(Double.valueOf(je));//结算金额
		insSzybSt.setCodeCenter("");//中心编码
		insSzybSt.setCodeOrg(Constant.CODEORG);//医院编码
		insSzybSt.setNameOrg(Constant.NAMEORG);//医院名称
		insSzybSt.setTransid("");//出院登记流水号
		insSzybSt.setBillno("");//发票号
		insSzybSt.setCodeHpst(ywh);////医保结算号
		insSzybSt.setCodeSerialno(Jsywh);//医药机构结算业务
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
 	 * 保存第三方支付数据
 	 * @param user
 	 * @param amountMsg
 	 * @param payType
 	 * @param ft1
 	 * @param dft
 	 * @param opCgforVo
     * @throws ParseException
 	 */
 	private void saveExtPayDataRegPi(PiMasterRegVo regvo,Map<String, Object> map) throws ParseException{
 		BlExtPay extPay=new BlExtPay();
 		User user = UserContext.getUser();
 		extPay.setPkExtpay(NHISUUID.getKeyId());
 		extPay.setPkOrg(user.getPkOrg());
 		extPay.setAmount(new BigDecimal(SDMsgUtils.getPropValueStr(map, "amountSt")));
 		extPay.setEuPaytype("15");
 		String dateTrans = SDMsgUtils.getPropValueStr(map,"dateTrans");
 		Date date = "".equals(dateTrans)?new Date():sdf.parse(dateTrans);
 		extPay.setDateAp(date);//请求时间
 		extPay.setFlagPay("1");//支付标志
 		extPay.setDatePay(date);//支付时间
 		extPay.setSerialNo(SDMsgUtils.getPropValueStr(map,"SerialNo"));//订单号
 		extPay.setTradeNo(SDMsgUtils.getPropValueStr(map,"TradeNo"));//系统订单号
 		extPay.setSysname("Y160");//终端号
 		extPay.setPkPi(regvo.getPkPi());
 		extPay.setPkPv(regvo.getPkPv());
 		extPay.setDescPay(SDMsgUtils.getPropValueStr(map, "DescPay"));
 		extPay.setResultPay(SDMsgUtils.getPropValueStr(map, "ResultPay"));
 		extPay.setPkSettle(regvo.getPkSettle());
 		extPay.setCreator(user.getPkEmp());
 		extPay.setCreateTime(new Date());
 		extPay.setTs(new Date());
 		extPay.setDelFlag("0");
 		extPay.setDtBank(SDMsgUtils.getPropValueStr(map, "dtBank"));
 		DataBaseHelper.insertBean(extPay);
 	}
 	
 	/**
     * 生成PiMasterRegVo实体值
     * @param appRegParam   必须包含排班主建pksch, 身份证号identifyno或者患者编号  patientno
     * @return
     */
   private PiMasterRegVo getPiMasterRegVo(Map<String, Object> appRegParam){
	   PiMasterRegVo piMaster = new PiMasterRegVo();
	   String pkSch =  SDMsgUtils.getPropValueStr(appRegParam,"pksch");

	   SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?", SchSch.class, pkSch);

	   if(schSch==null){
		   throw new BusException("未查询到排班信息，请检查");
	   }
	   //Map<String, Object> schTimeMap =  DataBaseHelper.queryForMap("select to_char(to_date(time_begin,'hh24:mi:ss'),'hh24miss') time_begin from bd_code_dateslot  where pk_dateslot = ? ",schSch.getPkDateslot());
	   piMaster.setDateReg(new Date());

	   piMaster.setPkDept(schSch.getPkDept());
	   Map<String , Object> srvMap =  DataBaseHelper.queryForMap("select pk_srv from  pv_pe where pk_sch = ? and del_flag = '0' ", pkSch);
	   piMaster.setPkSrv(SDMsgUtils.getPropValueStr(srvMap,"pkSrv"));//挂号类型，诊疗服务
	   piMaster.setPkSchres(schSch.getPkSchres());
	   piMaster.setPkSchsrv(schSch.getPkSchsrv());
	   piMaster.setPkSch(pkSch);
	   piMaster.setPkSchplan(schSch.getPkSchplan());
	   piMaster.setPkDateslot(schSch.getPkDateslot());
	   piMaster.setPkAppt(SDMsgUtils.getPropValueStr(appRegParam, "pkSchappt"));
	   piMaster.setEuPvtype(SDMsgUtils.getPropValueStr(appRegParam, "euPvType"));
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
		  // pi = DataBaseHelper.queryForBean("Select * from pi_master where  id_no = ?  and del_flag = '0'",PiMaster.class, identifyno);
	   //}else
	   if (!CommonUtils.isEmptyString(patientno)) {//根据患者编号查询患者信息
		   pi = DataBaseHelper.queryForBean("Select * from pi_master where  code_pi = ? and del_flag = '0'",PiMaster.class,patientno);
	   }
	   if(pi==null){
		   throw new BusException("未查询到该患者的个人信息，患者编码："+patientno);
	   }
	   // 01 医院所在区(县)  02  医院所在市的外区  ...
	   //piMaster.setDtSource("");//患者来源
	   piMaster.setNamePi(pi.getNamePi());
	   piMaster.setPkPi(pi.getPkPi());
	   piMaster.setPkOrg(pi.getPkOrg());
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
 	
}
