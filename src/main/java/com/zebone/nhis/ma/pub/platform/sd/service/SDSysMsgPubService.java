package com.zebone.nhis.ma.pub.platform.sd.service;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v24.message.DFT_P03;
import ca.uhn.hl7v2.model.v24.message.SRM_S01;
import ca.uhn.hl7v2.model.v24.message.SRR_S01;
import ca.uhn.hl7v2.model.v24.segment.*;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreate;
import com.zebone.nhis.ma.pub.platform.sd.create.MsgCreateUtil;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.platform.sd.vo.ACK_P03;
import com.zebone.nhis.ma.pub.platform.sd.vo.ZPO;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集成消息公共业务处理
 * @author jd_em
 *
 */
@Service
public class SDSysMsgPubService {

	private static Logger log = LoggerFactory.getLogger("nhis.lbHl7Log");
	
	//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");
	
	@Resource
	private MsgCreateUtil msgCreateUtil;
	@Resource
	private RegSyxService regSyxService;
	@Resource
	private MsgCreate msgCreate;
	
	/**
	 * 更新第三方支付表pkSettle数据
	 * @param paramMap
	 */
	public void updateExtPayData(Map<String,Object> paramMap,OpCgTransforVo opCgforVo){
		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
		Map<String,Object> depoMap=DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
		String sql = "update BL_EXT_PAY set PK_SETTLE=?,pk_pi=?,pk_pv=?,pk_depo=? where SERIAL_NO=?";
		DataBaseHelper.execute(sql, new Object[]{opCgforVo.getPkSettle(), opCgforVo.getPkPi(), opCgforVo.getPkPv(), SDMsgUtils.getPropValueStr(depoMap, "pkDepo"), SDMsgUtils.getPropValueStr(paramMap, "SerialNo")});
	}
	
	/**
	 * 微信
	 * 组建DFT_P03消息成功返回Ack所需要的数据
	 * @param opCgforVo
	 * @param dft
	 * @param ft1
	 * @param resAckMap
	 * @param codeOp
	 * @param pvencounter
	 * @return
	 */
	public String successDftAckParamCreate(OpCgTransforVo opCgforVo ,DFT_P03 dft,FT1 ft1,Map<String, Object> resAckMap,String codeOp,PvEncounter pvencounter){
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
		List<Map<String,Object>> winnoResList=DataBaseHelper.queryForList(winnoSql.toString(), new Object[]{opCgforVo.getPkSettle()});
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
		BlSettle settle=DataBaseHelper.queryForBean(setSql, BlSettle.class, new Object[]{opCgforVo.getPkSettle()});
		resAckMap.put("settle", settle);
		resAckMap.put("codePv", pvencounter.getCodePv());
		resAckMap.put("codeOp", codeOp);
		resAckMap.put("pvencounter", pvencounter);

		return createAckMsgForDft("AA", "成功", resAckMap);
	}

	
	/**
     * 医保补充更新pkSettle
     * @param object
     * @param data
     */
	public void updatePkSettle(Map<String, Object> doYbRetData, OpCgTransforVo opCgforVo) {

		//HIS结算成功后回写医保结算记录中得pk_settle
		String upYbSql="update ins_szyb_st set pk_settle=? where pk_insst=?";
		DataBaseHelper.execute(upYbSql, new Object[]{opCgforVo.getPkSettle(),doYbRetData.get("data")});
	}
	
	

   
   /**************取号预算接口（自助机 160公用）****************************************/
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
		//预约渠道，微信写1，自助机写2
		appRegParam.put("orderSource", "YYT".equals(sendApp)?"2":"1");
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
		}else if ("YYT".equals(sendApp)) {//自助机
			piTel =pid.getPhoneNumberHome(0).getPhoneNumber().getValue();
			appRegParam.put("euPvTypeMsg","O");
			appRegParam.put("euPvType", 1);
			//PID-38 医保名称
			//PID-38医保类型
			String insurTypeCode = pid.getProductionClassCode().getIdentifier().getValue();
			//默认自费 (体检默认传1)
			insurTypeCode = insurTypeCode==null||"".equals(insurTypeCode)||"1".equals(insurTypeCode)?"01":insurTypeCode;
			String sql = "select pk_hp,name from bd_hp where del_flag = '0' and code = ? ";
			//PID-39医保类型明细
			if("07".equals(insurTypeCode)){
				 	insurTypeCode = pid.getProductionClassCode().getNameOfCodingSystem().getValue();
					appRegParam.put("isSur", "02");
			}
			Map<String , Object> insurMap = DataBaseHelper.queryForMap(sql, insurTypeCode);
			if(insurMap==null)
				throw new BusException("未查询到该("+insurTypeCode+")医保编号所对应的医保主建信息");
			appRegParam.put("pkHp", insurMap.get("pkHp"));
			appRegParam.put("name", insurMap.get("name"));
			appRegParam.put("insurTypeCode",insurTypeCode);
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
			//ZAI-4就诊开始时间
			String beginTime = ele[3].toString();
			appRegParam.put("begintime",beginTime);
			//ZAI-5就诊结束时间
			String endTime =  ele[4].toString();
			appRegParam.put("endtime",endTime);
		} catch (Exception e) {
			throw new BusException("消息SRM_S01中ZAI-5字段必须存值，则解析错报");
		}

		String message = "";
		//PV1-19
		String appCode =so1.getPATIENT(0).getPV1().getVisitNumber().getID().getValue();

   	    appRegParam.put("appCode", appCode);
   	    appRegParam.put("codePi", patientNO);
   	    appRegParam.put("namePi", piName);
   	    message = createSRRS01(appRegParam);
		return message;
	}

   /**
    *	创建SRRS01消息返回
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
		insurParam.put("euPvType","1");
		insurParam.put("pkEmp","");
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
   
   
   /*********************************返回的消息***************************/
	
   /**
	 * 诊中支付ACK消息返回构建 微信使用
	 * @param status AE失败：AA成功
	 * @param message 特别信息提示
	 * @param paramMap 成功返回的数据
	 * @return
	 */
	public String createAckMsgForDft(String status,String message,Map<String,Object> paramMap){
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
				//zpo.getSfy().setValue(UserContext.getUser().getCodeEmp());
				//电子发票数据
				//EbillCode	电子票据代码
				zpo.getEbillCode().setValue("");
				//EbillNo	电子票据号码
				zpo.getEbillNo().setValue("");
				//CheckCode	电子校验码
				zpo.getCheckCode().setValue("");
				//EbillDate	电子票据生成时间
				zpo.getEbillDate().setValue("");
				//EbillQRCode	电子票据二维码效验数据
				zpo.getEbillQRCode().setValue("");

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
