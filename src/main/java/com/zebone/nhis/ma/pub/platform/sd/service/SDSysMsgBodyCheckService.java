package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v24.message.DFT_P03;
import ca.uhn.hl7v2.model.v24.message.OMG_O19;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDOpMsgMapper;
import com.zebone.nhis.ma.pub.platform.sd.dao.SDQueryUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.Constant;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.ACK_P03;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
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

/**
 * 体检接口
 * @author JesusM
 *
 */
@Service
public class SDSysMsgBodyCheckService {
	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	@Autowired
	private SDQueryUtils sDQueryUtils;
	@Autowired
	private  BdSnService bdSnService;//获取新医嘱号服务
	@Autowired
	private SDOpMsgMapper sDOpMsgMapper;
	@Autowired
	private RegSyxService regSyxService;
	@Autowired
	private OpCgPubService opCgPubService;
	//电子发票
	@Autowired
	private ElectInvService electInvService;
	
	@Resource
    private PvInfoPubService pvInfoPubService;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
	
	
	/*****************体检挂号*********************************************/
	/**
	 * 体检挂号
	 * @param dft
	 * @param hapiMsg
	 * @return
	 * @throws ParseException
	 * @throws HL7Exception 
	 */
	public String  appRegistPeisPiInfoPay(DFT_P03 dft,Message hapiMsg) throws ParseException, HL7Exception{
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
		String sqlEmp="select pk_emp,name_emp from bd_ou_employee where code_emp=?";
		Map<String,Object> empMap=DataBaseHelper.queryForMap(sqlEmp, new Object[]{codeEmp});
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
		prampMap.put("addrPosition", "4");
		prampMap.put("pkHp","eb2baf4e9848486984516fe6a84d6136");//默认自费
		int size = dft.getPID().getPatientIdentifierListReps();
		//PID-3.1患者卡号
		//PID-3.2患者卡类型
		for (int i = 0; i < size; i++) {
			String id = dft.getPID().getPatientIdentifierList(i).getID().getValue();
			String idType = dft.getPID().getPatientIdentifierList(i).getIdentifierTypeCode().getValue();
			if ("IDCard".equals(idType)||"CardNO".equals(idType)||"PatientNO".equals(idType)) {//患者编号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("patientno", id);
				}
			}else if ("IdentifyNO".equals(idType)) {//身份证号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("identifyno", id);
				}
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
			}else if("PassportNO".equals(idType)){
				//护照号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("PassportNO", id);
				}
			}else if ("ReentryPermitNO".equals(idType)) {
				//回乡证号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("identifyno", id);
				}
			}else if ("DrivingLicenseNO".equals(idType)) {
				//驾驶证号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("identifyno", id);
				}
			}else if ("MilitaryIDNO".equals(idType)) {
				//军官证号
				if(StringUtils.isNotBlank(id)) {
					prampMap.put("MilitaryIDNO", id);
				}
			}
		}
		//PV1-2 门诊住院标识
		String OorI = dft.getPV1().getPatientClass().getValue();
		prampMap.put("OorI", OorI);
		prampMap.put("euPvType", "PEIS".equals(sendApp)?4:1);
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
		String insurTypeCode = "";
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
		String sql = "select name,pk_hp from bd_hp where del_flag = '0' and code = ? ";
		prampMap.put("insurTypeCode",insurTypeCode);
		Map<String , Object> insurMap = DataBaseHelper.queryForMap(sql, insurTypeCode);
		if(insurMap==null)
			throw new BusException("未查询到该("+insurTypeCode+")医保编号所对应的医保主建信息");
		prampMap.put("pkHp", insurMap.get("pkHp"));
		prampMap.put("name", insurMap.get("name"));
		//默认普通门诊
		prampMap.put("aka130", "11");
		prampMap.put("AkA130Name", "普通门诊");
		prampMap.put("amountPi", amountPi);
		prampMap.put("amountInsu", amountInsu);
		prampMap.put("amountSt", amountSt);
		
		return disposeAppRegPayInfo(prampMap);
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
	 * @throws HL7Exception 
     */
    public String disposeAppRegPayInfo(Map<String, Object> map) throws ParseException, HL7Exception{
    	//1.效验挂号信息
    	Map<String, Object> apptMap = DataBaseHelper.queryForMap("select appt.*,case when to_char(appt.date_reg,'yyyyMMdd') = to_char(appt.begin_time,'yyyyMMdd') then '1' else '0' end as timePosition ,to_char(appt.begin_time,'yyyyMMddHH24miss') beginStr,apptpv.pk_emp_phy from  sch_appt appt left join sch_appt_pv apptpv on apptpv.pk_schappt = appt.pk_schappt   where appt.code = ?  and appt.del_flag = '0' and appt.eu_status = '0' ", SDMsgUtils.getPropValueStr(map,"code"));
		if(apptMap==null){
	    	if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '0' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(map,"code"))==1) {
				throw new BusException("预约号("+SDMsgUtils.getPropValueStr(map,"code")+")已成功预约,未支付,请勿重复推送");
			}
	    	if(DataBaseHelper.queryForScalar("select COUNT(1) from  sch_appt appt where appt.code = ? and appt.eu_status = '1' and appt.flag_pay = '1' and  appt.del_flag = '0' ", Integer.class,SDMsgUtils.getPropValueStr(map,"code"))==1) {
				throw new BusException("预约号("+SDMsgUtils.getPropValueStr(map,"code")+")已成功预约,已支付,请勿重复推送");
			}
			throw new BusException("未查询到该预约号("+SDMsgUtils.getPropValueStr(map,"code")+")信息");
		}
		User user = new User();
	    user.setPkEmp(SDMsgUtils.getPropValueStr(map,"pkEmp"));
	    user.setNameEmp(SDMsgUtils.getPropValueStr(map,"nameEmp"));
	    user.setPkOrg(Constant.PKORG);
	    user.setPkDept(Constant.PKDEPT);//门诊收费处
	    user.setCodeEmp(SDMsgUtils.getPropValueStr(map,"codeEmp"));
	    UserContext.setUser(user);
	  //2.准备消息参数值；
		Map<String, Object> appRegParam = new HashMap<>();
		appRegParam.put("pksch", SDMsgUtils.getPropValueStr(apptMap,"pkSch"));
		appRegParam.put("patientno", SDMsgUtils.getPropValueStr(map,"patiId"));
		appRegParam.put("euPvType", SDMsgUtils.getPropValueStr(map,"euPvType"));
		appRegParam.put("pkSchappt", SDMsgUtils.getPropValueStr(apptMap,"pkSchappt"));
		PiMasterRegVo regvo = getPiMasterRegVo(appRegParam);
		regvo.setTicketNo(SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
		regvo.setPkEmp(SDMsgUtils.getPropValueStr(map,"pkEmp"));
		regvo.setNameEmpReg(SDMsgUtils.getPropValueStr(map,"nameEmp"));
		//3.执行预约确认(已预约登记信息)
		//3.1.基于预约信息生成就诊记录，写表pv_encounter；
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(SDMsgUtils.getPropValueStr(apptMap,"pkOrg"));
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = sDOpMsgMapper.querySchResInfo(paramMap);
		regvo = getPiMaterRegItemPriceVo(regvo,map);
		regvo.setPkHp(SDMsgUtils.getPropValueStr(map,"pkHp"));
	    //修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
		try {
			PvEncounter pv = savePvEncounter(regvo, pkPv,schres,SDMsgUtils.getPropValueStr(apptMap,"pkEmpPhy"));
			//3.1.1 关联 预约就诊记录（预约登记时不生成pv_encounter，确认时才生成）
			DataBaseHelper.update("update SCH_APPT_PV set pk_pv=? where pk_schappt=?",new Object[]{pkPv,SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			regvo.setCodePv(pv.getCodePv());
			//3.2.生成门诊就诊记录，写表pv_op；Pv_Insurance
			regSyxService.savePvOp(regvo,pv,schres);
			regSyxService.savePvInsurance(regvo, pv);
			//3.3.生成记费信息，写表bl_op_dt；
			//3.4.生成结算信息，写表bl_settle；
			//3.5.生成结算明细，写表bl_settle_detail；
			//3.6.生成支付记录，写表bl_deposit；
			//如果挂号打印发票：bl_ext_pay
			//3.7.生成发票信息，写表bl_invoice；
			//3.8.生成发票和结算关系，写表bl_st_inv；
			//3.9.生成发票明细，bl_invoice_dt；
			//3.10.更新发票登记表，更新bl_emp_invoice。
			//体检自费
			regvo =  regSyxService.saveSettle(regvo, pv);
			//3.12.更新预约登记信息
			DataBaseHelper.update("update sch_appt set  eu_status='1',flag_pay='1' where pk_schappt = ?  ", new Object[]{SDMsgUtils.getPropValueStr(apptMap,"pkSchappt")});
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			// 事务处理失败发送消息
			ACK_P03  ackp03 = new ACK_P03();
			String msgId = SDMsgUtils.getMsgId();
			MSH msh = ackp03.getMSH();
			MSA msa = ackp03.getMSA();
			SDMsgUtils.createMSHMsg(msh, msgId, "ACK", "P03");
			msh.getReceivingApplication().getNamespaceID().setValue(SDMsgUtils.getPropValueStr(map, "sendApp"));
			msa.getAcknowledgementCode().setValue("AE");
			msa.getMessageControlID().setValue(SDMsgUtils.getPropValueStr(map,"oldmsgid"));
			msa.getTextMessage().setValue(e.getMessage());
			msa.getExpectedSequenceNumber().setValue("100");
			msa.getDelayedAcknowledgmentType().setValue("F");
			return SDMsgUtils.getParser().encode(ackp03);
		}
		map.put("ticketNo",  SDMsgUtils.getPropValueStr(apptMap,"ticketNo"));
		map.put("beginStr",  SDMsgUtils.getPropValueStr(apptMap,"beginStr"));
		map.put("codePv", regvo.getCodePv());
		//生成电子票据
		TransactionStatus status2 = platformTransactionManager.getTransaction(def);
		try {
			Map<String, Object> invMap = electInvService.registrationElectInv(regvo.getPkPv(), user, regvo.getPkSettle());
			map.putAll(invMap);
			platformTransactionManager.commit(status2);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("SDSysMsgBodyCheckService.disposeAppRegPayInfo 生成电子票据失败：{}",e.getClass());
			platformTransactionManager.rollback(status2);
		}
		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		msgParam.put("pkPv", regvo.getPkPv());
		msgParam.put("isAdd", "0");
		msgParam.put("timePosition", SDMsgUtils.getPropValueStr(apptMap,"timeposition"));//0：预约     1：当天
		msgParam.put("addrPosition", SDMsgUtils.getPropValueStr(map,"addrPosition"));//0：现场 1：自助机 2：电话 3：微信  4：支付宝
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		//4.拼接消息
    	return createAppRegPayHL7Info(map);
    }
    
    /**
   	 * 保存就诊记录
   	 * @param master
   	 * @param pkPv
   	 * @return
   	 */
   	private PvEncounter savePvEncounter(PiMasterRegVo master,String pkPv,Map<String,Object> schres,String pkEmpPhy){
   		//是否允许挂号
   		//allowReg(master,pkEmpPhy);
   		String date = master.getDateAppt()!=null?DateUtils.formatDate(master.getDateAppt(), "yyyyMMdd"):
			(master.getDateReg()!=null?DateUtils.formatDate(master.getDateReg(), "yyyyMMdd"):null);
		if(StringUtils.isNotBlank(date)) {
			if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.pk_emp_phy=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{master.getPkPi(),date,pkEmpPhy}) >0){
				throw new BusException("该患者已经存在当前日期和当前医生的挂号记录！");
			}
		}
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
   		pvEncounter.setDateBegin(new Date());//挂号收费日期
   		if(master.getDateAppt()!=null){//如果是预约挂号，开始日期为预约日期
   			//预约开始时间应该为日期+预约号所在的开始时间（bug-20723）
   			String sql = "select time_begin from bd_code_dateslot where pk_dateslot = ?";
   			Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, master.getPkDateslot());
   			String dateStr = DateUtils.dateToStr("yyyyMMdd", master.getDateAppt());
   		    String dateAppt = dateStr + " " + queryForMap.get("timeBegin").toString();
   		    master.setDateAppt(DateUtils.strToDate(dateAppt,"yyyyMMdd HH:mm:ss"));
   			pvEncounter.setDateReg(master.getDateAppt());
   		}else{
   			pvEncounter.setDateReg(master.getDateReg());//挂号的排班日期
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
   		pvEncounter.setTs(new Date());
   		
   		String pkDeptArea = pvInfoPubService.getPkDeptArea(master.getPkSchres());
   		pvEncounter.setPkDeptArea(pkDeptArea);
   		DataBaseHelper.insertBean(pvEncounter);

   		return pvEncounter;
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
     * 
     * @param appRegParam
     * @return
     */
    private PiMasterRegVo getPiMasterRegVo(Map<String, Object> appRegParam){
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
 	   if(!CommonUtils.isEmptyString(identifyno)){//根据身份证查询患者信息
 		   pi = DataBaseHelper.queryForBean("Select * from pi_master where  id_no = ?  and del_flag = '0'",PiMaster.class, identifyno);
 	   }else if (!CommonUtils.isEmptyString(patientno)) {//根据患者编号查询患者信息
 		   pi = DataBaseHelper.queryForBean("Select * from pi_master where  code_pi = ? and del_flag = '0'",PiMaster.class,patientno);
 	   }
 	   if(pi==null){
 		   throw new BusException("请传入正确的患者身份证号或患者编号");
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

	/**
	 * 生成PiMasterRegVo中的计费实体消息值   必传pk_sch
	 * @param piMaster
	 * @param map
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
	   blDeposit.setDtPaymode("99");//默认99
	   //String  payway = SDMsgUtils.getPropValueStr(map,"payWay");
	   //支付方式
	   if("01".equals(SDMsgUtils.getPropValueStr(map, "insurTypeCode"))){
		 blDeposit.setDtPaymode("7");
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
     * 挂号支付反馈信息
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

			//电子发票数据 体检不返回电子票据信息
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
	
	
	
	/***********************体检检查***********************************************/
	
	/**
	 * 体检检查申请保存
	 * @param hapiMsg
	 * @throws ParseException
	 */
	public void bodyCheckOMGO19(Message hapiMsg) throws ParseException{
		//获取消息数据
		OMG_O19 omg = (OMG_O19)hapiMsg;
		Map<String,Object> result = new HashMap<String,Object>();
		//获取患者信息
		PID pid = omg.getPATIENT().getPID();
		PV1 pv1 = omg.getPATIENT().getPATIENT_VISIT().getPV1();
		String codePi = pid.getPid2_PatientID().getCx1_ID().getValue();
		String codePv = pv1.getPv119_VisitNumber().getCx1_ID().getValue();
		String send = omg.getMSH().getMsh3_SendingApplication().getHd1_NamespaceID().getValue();
		String type = pv1.getPv12_PatientClass().getValue();
		if("PEIS".equals(send) && "T".equals(type)){
			result.put("euPvtype","4");//体检
		}else{
			result.put("euPvtype","1");//门诊
		}
		result.put("codePi",codePi);
		result.put("namePi",pid.getPid5_PatientName(0).getXpn1_FamilyName().getFn1_Surname().getValue());
		result.put("codePv", codePv);
		boolean isDel = "CA".equals(omg.getORDER(0).getORC().getOrc1_OrderControl().getValue());
		List<Map<String, Object>> queryPiPvOpbyPv = sDQueryUtils.queryPiPvOpbyPv(codePv,isDel);
		if(queryPiPvOpbyPv==null || queryPiPvOpbyPv.size()!=1){
			throw new BusException("未找到就诊流水号为"+codePv+"的就诊记录，或同一就诊流水号存在多条数据记录");
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
		//医嘱信息
		List<Map<String,Object>> cnOrderList = new ArrayList<Map<String,Object>>();
		int len = omg.getORDERReps();
		for(int i=0;i<len;i++){
			Map<String,Object> cnOrder = new HashMap<String,Object>();
			//获取医嘱信息
			ORC orc = omg.getORDER(0).getORC();
			OBR obr = omg.getORDER(0).getOBR();
			cnOrder.put("control", orc.getOrc1_OrderControl().getValue());
			StringBuffer codeApply = new StringBuffer(orc.getOrc2_PlacerOrderNumber().getEi1_EntityIdentifier().getValue());
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
			FT1 ft1 = omg.getORDER(0).getFT1(0);
			StringBuffer note = new StringBuffer(ft1.getFt16_TransactionType().getValue())
					.append("-").append(ft1.getFt118_PatientType().getValue());
			cnOrder.put("note", note.toString());
			cnOrder.put("quan", ft1.getFt110_TransactionQuantity().getValue());
			cnOrderList.add(cnOrder);
			//团检标志
			if("J".equals(ft1.getFt118_PatientType().getValue())){
				result.put("J", "");
			}
		}
		result.put("cnOrderList", cnOrderList);

		//1、保存cn_order
		//2、保存cn_ris_apply
		//3、保存bl_op_dt(调用记费接口)
		bodyCheckRis(result);
	}
	
	/**
	 * 体检保存检查
	 * @param result
	 * @throws ParseException
	 */
	private void bodyCheckRis(Map<String, Object> result) throws ParseException {
		//构造用户参数
		User user  = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(result,"pkOrg"));
		user.setPkDept(SDMsgUtils.getPropValueStr(result,"pkDept"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(result,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(result,"nameEmp"));
		user.setCodeEmp(SDMsgUtils.getPropValueStr(result,"codeEmp"));
		UserContext.setUser(user);
		List<Map<String,Object>> cnOrderList = (List<Map<String, Object>>) result.get("cnOrderList");
		for(Map<String,Object> order:cnOrderList){
			switch (SDMsgUtils.getPropValueStr(order, "control")){
				case "NW":addBodyCheckRis(result,order,user);break;
				case "RU":updateBodyCheckRis(result,order,user);break;
				case "CA":delBodyCheckRis(result,order,user);break;
				default:throw new BusException("消息错误，未知操作类型！");
			}
		}
		//更新pv表接诊医生 以及 就诊类型状态  团检J(1) 或非团检 
		PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where PK_PV=?", PvEncounter.class, SDMsgUtils.getPropValueStr(result, "pkPv"));
		pvEncounter.setNameEmpPhy(SDMsgUtils.getPropValueStr(result, "nameEmp"));
		pvEncounter.setPkEmpPhy(SDMsgUtils.getPropValueStr(result, "pkEmp"));
		if(result.containsKey("J")){
			pvEncounter.setDtSpcdtype("1");
		}
		DataBaseHelper.updateBeanByPk(pvEncounter);

	}
	//修改体检检查记录
	private void updateBodyCheckRis(Map<String, Object> result, Map<String, Object> order, User user) throws ParseException {
		//先删除原来
		delBodyCheckRis(result,order,user);
		//删除医嘱表
		//String sqlCn = "delete CN_ORDER where CODE_APPLY=?";
		//删除计费表
		//String sqlDt = "delete bl_op_dt d where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
		//DataBaseHelper.execute(sqlDt, SDMsgUtils.getPropValueStr(order, "codeApply"));
		//DataBaseHelper.execute(sqlCn, SDMsgUtils.getPropValueStr(order, "codeApply"));
		//插入新数据
		addBodyCheckRis(result,order,user);

	}
	//删除体检检查记录
	private void delBodyCheckRis(Map<String, Object> result, Map<String, Object> order, User user) {
		//1、查询是否已经记费
		//String sqlBl = "select * from ( select blop.pk_org,blop.pk_cgop,(blop.quan + nvl( back.quan,0)) as quan from BL_OP_DT blop left outer join (select sum(quan) quan,sum(amount) amt,pk_cgop_back from BL_OP_DT  where flag_settle = 0 and flag_pd = 0 and  quan < 0  and pk_cnord in (select PK_CNORD from CN_ORDER where CODE_APPLY=?) group by pk_cgop_back ) back on blop.pk_cgop=back.pk_cgop_back where blop.del_flag = '0' and blop.pk_cgop_back is null and blop.flag_settle='0' and blop.quan >0 and blop.pk_cnord in (select PK_CNORD from CN_ORDER where CODE_APPLY=?)) dt where dt.quan>0;";
		String sqlBl = "select PK_CNORD from BL_OP_DT where FLAG_SETTLE='1' and  PK_CNORD in (select PK_CNORD from CN_ORDER where CODE_APPLY=? )";
		Map<String, Object> blMap = DataBaseHelper.queryForMap(sqlBl, SDMsgUtils.getPropValueStr(order, "codeApply"));
		//如果存在结算信息，不允许删除
		if(blMap!=null && blMap.size()>0){
			throw new BusException("该项目已经结算，不允许撤销！");
		}
		//删除医嘱表
		String sqlCn = "delete CN_ORDER where CODE_APPLY=?";
		//删除计费表
		String sqlDt = "delete bl_op_dt d where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
		//删除申请表
		String sqlApply = "delete CN_RIS_APPLY a where a.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
		DataBaseHelper.execute(sqlDt, SDMsgUtils.getPropValueStr(order, "codeApply"));
		DataBaseHelper.execute(sqlApply, SDMsgUtils.getPropValueStr(order, "codeApply"));
		DataBaseHelper.execute(sqlCn, SDMsgUtils.getPropValueStr(order, "codeApply"));
		//优化写法，待测试
		//String sql = "update bl_op_dt d set FLAG_SETTLE='9',MODIFIER='接口作废',ts=sysdate where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
		//DataBaseHelper.execute(sql, SDMsgUtils.getPropValueStr(order, "codeApply"));

	}

	//新增体检检查记录
	private void addBodyCheckRis(Map<String, Object> result,Map<String, Object> order,User user) throws ParseException{
		//1、优化写法：未测试；如果已经存在医嘱则更新状态
//		String codeApply = SDMsgUtils.getPropValueStr(order, "codeApply");
//		String sql = "select count(1) from CN_ORDER o where o.CODE_APPLY=?";
//		Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{codeApply});
//		if(count>0){
//			String update = "update bl_op_dt d set FLAG_SETTLE='0',MODIFIER='接口恢复',ts=sysdate where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
//			DataBaseHelper.execute(update,codeApply);
//			return;
//		}
		//2、否则插入新医嘱
		//组装医嘱数据
		CnOrder cnOrder = new CnOrder();
		String pkCnord = NHISUUID.getKeyId();
		cnOrder.setPkOrg(user.getPkOrg());
		cnOrder.setPkCnord(pkCnord);
		cnOrder.setPkPi(SDMsgUtils.getPropValueStr(result, "pkPi"));
		cnOrder.setPkPv(SDMsgUtils.getPropValueStr(result, "pkPv"));
		cnOrder.setEuPvtype(SDMsgUtils.getPropValueStr(result, "euPvtype"));//	1门诊，2急诊，3住院，4体检，5家庭病床
		Date date = sdf.parse(SDMsgUtils.getPropValueStr(order, "date"));
		cnOrder.setCreateTime(date);
		cnOrder.setDateEnter(date);//录入时间
		cnOrder.setDateStart(date);
		//参数
		String addDay = ApplicationUtils.getSysparam("CN0004", false);
		if(CommonUtils.isEmptyString(addDay)){
			addDay = "30";//默认30天
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, CommonUtils.getInt(addDay));
		cnOrder.setDateEffe(calendar.getTime());//有效期
		cnOrder.setDays(1L);
		String codeFreq = SDMsgUtils.getPropValueStr(order, "codeFreq");
		cnOrder.setCodeFreq(StringUtils.isBlank(codeFreq)?"1012":codeFreq);//频次
		//cnOrder.setCodeOrdtype(SDMsgUtils.getPropValueStr(order, "codeOrdtype"));//医嘱类型
		cnOrder.setCodeOrdtype("02");//医嘱类型
		cnOrder.setPkOrd(SDMsgUtils.getPropValueStr(order, "pkOrd"));
		cnOrder.setCodeOrd(SDMsgUtils.getPropValueStr(order, "codeOrd"));
		cnOrder.setNameOrd(SDMsgUtils.getPropValueStr(order, "nameOrd"));
		cnOrder.setDescOrd(SDMsgUtils.getPropValueStr(order, "descOrd"));
		cnOrder.setCodeApply(SDMsgUtils.getPropValueStr(order, "codeApply"));
		cnOrder.setDtHpprop("01");//自费
		if(!"01".equals(SDMsgUtils.getPropValueStr(result, "code"))){
			cnOrder.setDtHpprop("11");//普通医保
			if("4".equals(SDMsgUtils.getPropValueStr(result, "euPvtype"))){
				cnOrder.setDtHpprop("17");//体检
			}
		}
		//生成医嘱号  param ： {"tableName":"CN_ORDER","fieldName":"ORDSN","count":1}
		int ordsn = bdSnService.getSerialNo("CN_ORDER","ORDSN",1,user);
		cnOrder.setOrdsn(ordsn);
		cnOrder.setOrdsnParent(ordsn);
		cnOrder.setQuan(Double.valueOf(SDMsgUtils.getPropValueStr(order, "quan")));
		cnOrder.setQuanCg(Double.valueOf(SDMsgUtils.getPropValueStr(order, "quan")));
		//cnOrder.setPriceCg(0.0);
		cnOrder.setPkOrgExec(user.getPkOrg());
		cnOrder.setFlagDurg("0");
		cnOrder.setFlagSelf("0");
		cnOrder.setFlagNote("0");
		cnOrder.setFlagBase("0");
		cnOrder.setEuAlways("1");
		cnOrder.setPkEmpInput(user.getPkEmp());//录入人
		cnOrder.setNameEmpInput(user.getNameEmp());
		cnOrder.setFlagItc("1");//录入完成标志
		cnOrder.setPkDeptNs("");//开立病区
		cnOrder.setPkDept(user.getPkDept());//开立科室
		cnOrder.setPkEmpOrd(user.getPkEmp());//开立医生
		cnOrder.setNameEmpOrd(user.getNameEmp());//开立医生名字
		cnOrder.setPkDeptExec(SDMsgUtils.getPropValueStr(order, "pkDeptExec"));//执行科室
		cnOrder.setDtSamptype(SDMsgUtils.getPropValueStr(order, "dtSamptype"));//标本类型
		cnOrder.setDtColltype(SDMsgUtils.getPropValueStr(order, "dtColltype"));//采集方法
		cnOrder.setCreator(Constant.NOTE);
		cnOrder.setTs(new Date());
		cnOrder.setDelFlag("0");
		cnOrder.setFlagErase("0");
		DataBaseHelper.insertBean(cnOrder);
		//组装申请单数据
		CnRisApply cnRisApply = new CnRisApply();
		cnRisApply.setPkOrdris(NHISUUID.getKeyId());
		cnRisApply.setPkOrg(user.getPkOrg());
		cnRisApply.setPkCnord(pkCnord);
		cnRisApply.setCodeApply(SDMsgUtils.getPropValueStr(order, "codeApply"));
		cnRisApply.setNoteDise("");//病情描述
		cnRisApply.setDtRistype(SDMsgUtils.getPropValueStr(order, "dtType"));//检查类型
		cnRisApply.setDescBody(SDMsgUtils.getPropValueStr(order, "dtBody"));//检查部位描述
		cnRisApply.setPurpose("");//检查目的
		cnRisApply.setPkMsp("");//医技资源
		//cnRisApply.setDateAppo("");//预约操作时间
		//cnRisApply.setDateExam();//预约确定时间
		cnRisApply.setFlagFasting("");//是否空腹
		cnRisApply.setDtPatitrans("");//患者传输方式
		cnRisApply.setRisNotice(SDMsgUtils.getPropValueStr(order, "descAtt"));//检查注意事项
		//cnRisApply.setTicketno();//排队号
		cnRisApply.setNote("");
		cnRisApply.setCreateTime(new Date());
		cnRisApply.setCreator(Constant.NOTE);
		cnRisApply.setTs(new Date());
		DataBaseHelper.insertBean(cnRisApply);
		//组装计费数据
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		BlPubParamVo vo = new BlPubParamVo();
		vo.setPkOrg(user.getPkOrg());
		vo.setPkPi(cnOrder.getPkPi());
		vo.setPkPv(cnOrder.getPkPv());
		vo.setEuPvType(cnOrder.getEuPvtype());
		vo.setFlagPd("0");//0为非药品
		vo.setPkOrd(cnOrder.getPkOrd());
		vo.setPkCnord(cnOrder.getPkCnord());
		vo.setQuanCg(cnOrder.getQuan());
		vo.setPkOrgApp(user.getPkOrg());
		vo.setPkDeptApp(user.getPkDept());
		vo.setPkDeptNsApp(cnOrder.getPkDept());
		vo.setPkEmpApp(user.getPkEmp());
		vo.setNameEmpApp(user.getNameEmp());
		vo.setFlagPv("0");
		vo.setSpec("检查费");
		//vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(result,"pkExocc"));
		vo.setEuBltype("99");//收费类型
		vo.setDateHap(new Date());
		//执行人。执行部门 (计费部门)
		vo.setPkOrgEx(user.getPkOrg());
		vo.setPkDeptEx(cnOrder.getPkDeptExec());
		vo.setPkEmpEx(user.getPkEmp());
		vo.setNameEmpEx(user.getNameEmp());
		//计费部门，计费人
		vo.setPkDeptCg(user.getPkDept());
		vo.setPkEmpCg(user.getPkEmp());
		vo.setNameEmpCg(user.getNameEmp());
		vo.setDateCg(new Date());
		blCgVos.add(vo);
		//组装记费参数
		//OpCgPubService opCgPubService = new OpCgPubService();
		opCgPubService.blOpCgBatch(blCgVos);
	}

	
	
	
	/**************************体检检验************************************************/
	
	/**
	 * 体检检验记费
	 * @param result
	 * @throws ParseException
	 */
	public void bodyCheckLis(Map<String, Object> result) throws ParseException {
		//构造用户参数
		User user  = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(result,"pkOrg"));
		user.setPkDept(SDMsgUtils.getPropValueStr(result,"pkDept"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(result,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(result,"nameEmp"));
		user.setCodeEmp(SDMsgUtils.getPropValueStr(result,"codeEmp"));
		UserContext.setUser(user);
		//记费参数集合
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		//更新医嘱和检验记费
		List<Map<String,Object>> cnOrderList = (List<Map<String, Object>>) result.get("cnOrderList");
		for(Map<String,Object> order:cnOrderList){
			switch (SDMsgUtils.getPropValueStr(order, "control")){
				case "NW":blCgVos.add(addBodyCheckLis(result,order,user));break;
				case "RU":blCgVos.add(updateBodyCheckLis(result,order,user));break;
				case "CA":delBodyCheckLis(result,order,user);break;
				default:throw new BusException("消息错误，未知操作类型！");
			}
		}
		//如果是删除，不进行计费
		if("CA".equals(SDMsgUtils.getPropValueStr(cnOrderList.get(0), "control"))) {
			return;
		}
		//组装标本和容器记费参数
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(result,"codeSample"))){
			List<BlPubParamVo> labSampCgVos = sDOpMsgMapper.qryLabSampCgVo(result);
			for (BlPubParamVo bldt : labSampCgVos) {
				bldt = setCgVoValue(bldt,result,user,"标本收费");
				blCgVos.add(bldt);
			}
		}
		if(StringUtils.isNotBlank(SDMsgUtils.getPropValueStr(result,"codeContainer"))){
			List<BlPubParamVo> labContCgVos = sDOpMsgMapper.qryLabContCgVo(result);
			for (BlPubParamVo bldt : labContCgVos) {
				bldt = setCgVoValue(bldt,result,user,"容器收费");
				blCgVos.add(bldt);
			}
		}
		//记费
		//OpCgPubService opCgPubService = new OpCgPubService();
		opCgPubService.blOpCgBatch(blCgVos);

		//更新pv表接诊医生 以及 就诊类型状态  团检J(1) 或非团检
		PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where PK_PV=?", PvEncounter.class, SDMsgUtils.getPropValueStr(result, "pkPv"));
		pvEncounter.setNameEmpPhy(SDMsgUtils.getPropValueStr(result, "nameEmp"));
		pvEncounter.setPkEmpPhy(SDMsgUtils.getPropValueStr(result, "pkEmp"));
		if(result.containsKey("J")){
			pvEncounter.setDtSpcdtype("1");
		}
		DataBaseHelper.updateBeanByPk(pvEncounter);
	}
	//标本容器、vo字段赋值--cg
	private BlPubParamVo setCgVoValue(BlPubParamVo blPubVo,Map<String,Object> result,User user,String spec) throws ParseException{
		BlPubParamVo blPubParamVo = new BlPubParamVo();
		ApplicationUtils.copyProperties(blPubParamVo, blPubVo);
		blPubParamVo.setPkPv(SDMsgUtils.getPropValueStr(result, "pkPv"));
		blPubParamVo.setPkPi(SDMsgUtils.getPropValueStr(result, "pkPi"));
		//blPubParamVo.setPkCnord(SDMsgUtils.getPropValueStr(result, "pkCnord"));//设置医嘱主键，用于绑定相关记费项目
		blPubParamVo.setSpec(spec);//设置规格，注明是标本 | 容器费用
		blPubParamVo.setDateExpire(new Date());//标本合费时借该字段用于缓存计划执行时间 sdf.parse(SDMsgUtils.getPropValueStr(result, "date"))
		blPubParamVo.setPkOrg(user.getPkOrg());
		blPubParamVo.setEuPvType(SDMsgUtils.getPropValueStr(result, "euPvtype"));
		blPubParamVo.setFlagPd("0");
		blPubParamVo.setFlagPv("0");
		blPubParamVo.setEuBltype("0");
		blPubParamVo.setPkOrgApp(user.getPkOrg());
		blPubParamVo.setPkDeptApp(SDMsgUtils.getPropValueStr(result, "pkDept"));
		blPubParamVo.setPkOrgEx(user.getPkOrg());
		blPubParamVo.setPkDeptEx(SDMsgUtils.getPropValueStr(result, "pkDeptExec"));//标本|容器的记费科室为检验执行科室
		blPubParamVo.setDateHap(new Date());//费用发生时间
		blPubParamVo.setPkDeptCg(user.getPkDept());
		blPubParamVo.setPkEmpCg(user.getPkEmp());
		blPubParamVo.setNameEmpCg(user.getNameEmp());
		blPubParamVo.setPkEmpApp(SDMsgUtils.getPropValueStr(result, "pkEmp"));
		blPubParamVo.setNameEmpApp(SDMsgUtils.getPropValueStr(result, "nameEmp"));
		blPubParamVo.setEuAdditem("1");//附加费用
		return blPubParamVo;
	}

	//体检检验新增
	private BlPubParamVo addBodyCheckLis(Map<String, Object> result, Map<String, Object> order, User user) throws ParseException {
		//1、如果已经存在医嘱则更新状态
//		String codeApply = SDMsgUtils.getPropValueStr(order, "codeApply");
//		String sql = "select count(1) from CN_ORDER o where o.CODE_APPLY=?";
//		Integer count=DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{codeApply});
//		if(count>0){
//			String update = "update bl_op_dt d set FLAG_SETTLE='0',MODIFIER='接口恢复',ts=sysdate where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
//			DataBaseHelper.execute(update,codeApply);
//			return;
//		}
		//组装医嘱数据
		CnOrder cnOrder = new CnOrder();
		String pkCnord = NHISUUID.getKeyId();
		cnOrder.setPkOrg(user.getPkOrg());
		cnOrder.setPkCnord(pkCnord);
		cnOrder.setPkPi(SDMsgUtils.getPropValueStr(result, "pkPi"));
		cnOrder.setPkPv(SDMsgUtils.getPropValueStr(result, "pkPv"));
		//1门诊，2急诊，3住院，4体检，5家庭病床
		cnOrder.setEuPvtype(SDMsgUtils.getPropValueStr(result, "euPvtype"));
		Date date = sdf.parse(SDMsgUtils.getPropValueStr(order, "date"));
		cnOrder.setCreateTime(date);
		cnOrder.setDateEnter(date);//录入时间
		cnOrder.setDateStart(date);
		//参数
		String addDay = ApplicationUtils.getSysparam("CN0004", false);
		if(CommonUtils.isEmptyString(addDay)){
			addDay = "30";//默认30天
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, CommonUtils.getInt(addDay));
		cnOrder.setDateEffe(calendar.getTime());//有效期
		cnOrder.setDays(1L);
		String codeFreq = SDMsgUtils.getPropValueStr(order, "codeFreq");
		cnOrder.setCodeFreq(StringUtils.isBlank(codeFreq)?"1012":codeFreq);//频次
		//cnOrder.setCodeOrdtype(SDMsgUtils.getPropValueStr(order, "codeOrdtype"));//医嘱类型
		cnOrder.setCodeOrdtype("03");//医嘱类型
		cnOrder.setPkOrd(SDMsgUtils.getPropValueStr(order, "pkOrd"));
		cnOrder.setCodeOrd(SDMsgUtils.getPropValueStr(order, "codeOrd"));
		cnOrder.setNameOrd(SDMsgUtils.getPropValueStr(order, "nameOrd"));
		cnOrder.setDescOrd(SDMsgUtils.getPropValueStr(order, "descOrd"));
		cnOrder.setCodeApply(SDMsgUtils.getPropValueStr(order, "codeApply"));
		//生成医嘱号  param ： {"tableName":"CN_ORDER","fieldName":"ORDSN","count":1}
		int ordsn = bdSnService.getSerialNo("CN_ORDER","ORDSN",1,user);
		cnOrder.setOrdsn(ordsn);
		cnOrder.setOrdsnParent(ordsn);
		cnOrder.setQuan(Double.valueOf(SDMsgUtils.getPropValueStr(order, "quan")));
		cnOrder.setQuanCg(Double.valueOf(SDMsgUtils.getPropValueStr(order, "quan")));
		//cnOrder.setPriceCg(Double.valueOf(SDMsgUtils.getPropValueStr(order, "")));
		//cnOrder.setPriceCg(0.0);
		//cnOrder.setDescDiag("");
		cnOrder.setPkOrgExec(user.getPkOrg());
		cnOrder.setFlagDurg("0");
		cnOrder.setFlagSelf("0");
		cnOrder.setFlagNote("0");
		cnOrder.setFlagBase("0");
		cnOrder.setEuAlways("1");
		cnOrder.setPkEmpInput(user.getPkEmp());//录入人
		cnOrder.setNameEmpInput(user.getNameEmp());
		cnOrder.setFlagItc("1");//录入完成标志
		cnOrder.setPkDeptNs("");//开立病区
		cnOrder.setDtHpprop("01");//自费
		cnOrder.setFlagErase("0");
		cnOrder.setDtSamptype(SDMsgUtils.getPropValueStr(order, "dtSamptype"));//标本类型
		cnOrder.setDtColltype(SDMsgUtils.getPropValueStr(order, "dtColltype"));//采集方法
		if(!"01".equals(SDMsgUtils.getPropValueStr(result, "code"))){
			cnOrder.setDtHpprop("11");//普通医保
			if("4".equals(SDMsgUtils.getPropValueStr(result, "euPvtype"))){
				cnOrder.setDtHpprop("17");//体检
			}
		}
		cnOrder.setPkDept(user.getPkDept());//开立科室
		cnOrder.setPkEmpOrd(user.getPkEmp());//开立医生
		cnOrder.setNameEmpOrd(user.getNameEmp());//开立医生名字
		cnOrder.setPkDeptExec(SDMsgUtils.getPropValueStr(order, "pkDeptExec"));//执行科室
		cnOrder.setCreator(Constant.NOTE);
		cnOrder.setTs(new Date());
		cnOrder.setDelFlag("0");
		DataBaseHelper.insertBean(cnOrder);
		//检验申请
		CnLabApply cnLabApply = new CnLabApply();
		cnLabApply.setPkOrdlis(NHISUUID.getKeyId());
		cnLabApply.setPkOrg(user.getPkOrg());
		cnLabApply.setPkCnord(pkCnord);
		cnLabApply.setCodeApply(SDMsgUtils.getPropValueStr(order, "codeApply"));
		//cnLabApply.setDescDiag(cnOrder.getDescDiag());//诊断描述
		//cnLabApply.setPurpose(cnOrder.getPurpose());//检验目的
		cnLabApply.setDtSamptype(cnOrder.getDtSamptype());//标本类型
		cnLabApply.setDtTubetype(SDMsgUtils.getPropValueStr(order, "dtColltype"));//试管类型
		cnLabApply.setDtColtype(cnOrder.getDtColltype());//采集方法
		//cnLabApply.setSampNo(cnOrder.getDtSamptype());//标本编号
		cnLabApply.setPkDeptCol("");//样本采集科室
		cnLabApply.setEuStatus("0");//检验过程状态
		cnLabApply.setDateCol(null);//采集时间
		cnLabApply.setDateAcpt(null);//标本接收时间	
		cnLabApply.setDateRpt(null);//报告时间
		cnLabApply.setNote("");//备注
		cnLabApply.setFlagBl("0");
		cnLabApply.setPkEmpCol("");//采集人
		cnLabApply.setNameEmpCol("");//采集人姓名
		cnLabApply.setPkEmpInput(cnOrder.getPkEmpInput());//录入人
		cnLabApply.setPkDeptExec(cnOrder.getPkDeptExec());//执行科室
		cnLabApply.setPkOrgExec(cnOrder.getPkOrdExc());
		cnLabApply.setCreateTime(new Date());//创建时间
		cnLabApply.setCreator(Constant.NOTE);//创建人
		cnLabApply.setTs(new Date());
		DataBaseHelper.insertBean(cnLabApply);
		//组装检验计费数据
		//List<BlPubParamVo> blCgVos = new ArrayList<>();
		BlPubParamVo vo = new BlPubParamVo();
		vo.setPkOrg(user.getPkOrg());
		vo.setPkPi(cnOrder.getPkPi());
		vo.setPkPv(cnOrder.getPkPv());
		vo.setEuPvType(cnOrder.getEuPvtype());
		vo.setFlagPd("0");//0为非药品
		vo.setPkOrd(cnOrder.getPkOrd());
		vo.setPkCnord(cnOrder.getPkCnord());
		vo.setQuanCg(cnOrder.getQuan());
		vo.setPkOrgApp(user.getPkOrg());
		vo.setPkDeptApp(user.getPkDept());
		vo.setPkDeptNsApp(user.getPkDept());
		vo.setPkEmpApp(user.getPkEmp());
		vo.setNameEmpApp(user.getNameEmp());
		vo.setFlagPv("0");
		//vo.setPkOrdexdt(SDMsgUtils.getPropValueStr(result,"pkExocc"));
		vo.setEuBltype("99");//收费类型
		vo.setDateHap(new Date());
		vo.setSpec("检验费");
		//执行人。执行部门 (计费部门)
		vo.setPkOrgEx(user.getPkOrg());
		vo.setPkDeptEx(cnOrder.getPkDeptExec());
		vo.setPkEmpEx(cnOrder.getPkEmpEx());
		vo.setNameEmpEx(cnOrder.getNameEmpEx());
		//计费部门，计费人
		vo.setPkDeptCg(user.getPkDept());
		vo.setPkEmpCg(user.getPkEmp());
		vo.setNameEmpCg(user.getNameEmp());
		vo.setDateCg(new Date());

		//blCgVos.add(vo);
		//OpCgPubService opCgPubService = new OpCgPubService();
		//opCgPubService.blOpCgBatch(blCgVos);
		//apputil.execService("BL", "OpCgPubService", "blOpCgBatch", blCgVos,user);
		return vo;

	}
	
	//体检检验修改
		private BlPubParamVo updateBodyCheckLis(Map<String, Object> result, Map<String, Object> order, User user) throws ParseException {
			//先删除原来
			delBodyCheckLis(result,order,user);
			//删除医嘱表
			//String sqlCn = "delete CN_ORDER where CODE_APPLY=?";
			//删除计费表
			//String sqlDt = "delete bl_op_dt d where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
			//DataBaseHelper.execute(sqlDt, SDMsgUtils.getPropValueStr(order, "codeApply"));
			//DataBaseHelper.execute(sqlCn, SDMsgUtils.getPropValueStr(order, "codeApply"));
			//插入新数据
			return addBodyCheckLis(result,order,user);

		}

		//体检检验删除
		private void delBodyCheckLis(Map<String, Object> result, Map<String, Object> order, User user) {
			//1、查询是否已经记费
			//String sqlBl = "select * from ( select blop.pk_org,blop.pk_cgop,(blop.quan + nvl( back.quan,0)) as quan from BL_OP_DT blop left outer join (select sum(quan) quan,sum(amount) amt,pk_cgop_back from BL_OP_DT  where flag_settle = 0 and flag_pd = 0 and  quan < 0  and pk_cnord in (select PK_CNORD from CN_ORDER where CODE_APPLY=?) group by pk_cgop_back ) back on blop.pk_cgop=back.pk_cgop_back where blop.del_flag = '0' and blop.pk_cgop_back is null and blop.flag_settle='0' and blop.quan >0 and blop.pk_cnord in (select PK_CNORD from CN_ORDER where CODE_APPLY=?)) dt where dt.quan>0;";
			String sqlBl = "select PK_CNORD from BL_OP_DT where FLAG_SETTLE='1' and  PK_CNORD in (select PK_CNORD from CN_ORDER where CODE_APPLY=? )";
			Map<String, Object> blMap = DataBaseHelper.queryForMap(sqlBl, SDMsgUtils.getPropValueStr(order, "codeApply"));
			//如果存在结算信息，不允许删除
			if(blMap!=null && blMap.size()>0){
				throw new BusException("该项目已经结算，不允许撤销！");
			}
			//2、原来逻辑，直接删除
			//删除医嘱表
			String sqlCn = "delete CN_ORDER where CODE_APPLY=?";
			//删除计费表
			String sqlDt = "delete bl_op_dt d where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
			//删除申请表
			String sqlApply = "delete CN_LAB_APPLY a where a.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
			DataBaseHelper.execute(sqlDt, SDMsgUtils.getPropValueStr(order, "codeApply"));
			DataBaseHelper.execute(sqlApply, SDMsgUtils.getPropValueStr(order, "codeApply"));
			DataBaseHelper.execute(sqlCn, SDMsgUtils.getPropValueStr(order, "codeApply"));
			//3、优化写法：修改bl_op_dt（待测试）
			//String sql = "update bl_op_dt d set FLAG_SETTLE='9',MODIFIER='接口作废',ts=sysdate where d.PK_CNORD in (select o.PK_CNORD from CN_ORDER o where o.CODE_APPLY=?) ";
			//DataBaseHelper.execute(sql, SDMsgUtils.getPropValueStr(order, "codeApply"));
		}
		
		
		
/***********************体检退号*******************************************/
		
	/**
	 * 体检专用退号+退费
	 * @param paramMap
	 */
	public void updateADTA11(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		//Map<String, Object> pv = DataBaseHelper.queryForMap("select pk_pv from pv_encounter where eu_pvtype!='3' and flag_cancel!='1' and eu_locked='0' and CODE_PV=?", );
		String codePv = SDMsgUtils.getPropValueStr(paramMap, "codePv");
		String sql = "select dt.PK_PV from BL_OP_DT dt inner join PV_ENCOUNTER pv on dt.PK_PV=pv.PK_PV where dt.FLAG_PV!='1' and dt.FLAG_SETTLE='1' and pv.CODE_PV=? ";
		List<Map<String, Object>> pvList = DataBaseHelper.queryForList(sql.toString(), SDMsgUtils.getPropValueStr(paramMap, "codePv"));
		//如果不存在就诊记录，抛出异常
		if(pvList!=null && pvList.size()>0){
			throw new BusException(SDMsgUtils.getPropValueStr(paramMap, "codePv")+"该患者存在已缴费且未退费的信息，不可退号！");
		}
		
		//1、更新pv_encounter
		StringBuffer pv_encounter = new StringBuffer("update pv_encounter set eu_status='9',flag_cancel='1', ")
				.append("name_emp_cancel='").append(SDMsgUtils.getPropValueStr(paramMap, "nameEmp")).append("',")
				.append("modifier='体检退号接口',ts=sysdate, ")
				.append("date_cancel=to_date('").append(SDMsgUtils.getPropValueStr(paramMap, "date")).append("','YYYYMMDDHH24MiSS') ")
				.append(" where code_pv=?");
		int countPv = DataBaseHelper.execute(pv_encounter.toString(),codePv);
		//2、更新pv_op
		StringBuffer pv_op = new StringBuffer("update SCH_APPT set EU_STATUS='9',FLAG_CANCEL='1' where PK_SCHAPPT=(select PK_SCHAPPT from PV_OP where PK_PV =(select PK_PV from PV_ENCOUNTER where CODE_PV=?))");
		DataBaseHelper.execute(pv_op.toString(),codePv);
		//3、更新pv_Insurance
		//StringBuffer pv_insurance = new StringBuffer("");
		//4、更新bl_settle
		//StringBuffer bl_settle = new StringBuffer("");
		//5、更新排班表 sch_sch
		//StringBuffer sch_sch = new StringBuffer("");
		//6、退挂号费，体检不需要退费
		if(countPv !=1){
			throw new BusException("体检退号失败！");
		}
	}

}
