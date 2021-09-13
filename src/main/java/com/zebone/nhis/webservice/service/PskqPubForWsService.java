package com.zebone.nhis.webservice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.vo.BlIpCgVo;
import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.bl.pub.vo.InvoiceInfo;
import com.zebone.nhis.bl.pub.vo.OpCgTransforVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybSt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCity;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybStCitydt;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisit;
import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybVisitCity;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOccDt;
import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.sd.util.SDMsgUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.webservice.dao.BdPubForWsMapper;
import com.zebone.nhis.webservice.dao.BlPubForWsMapper;
import com.zebone.nhis.webservice.dao.PvPubForWsMapper;
import com.zebone.nhis.webservice.dao.SchPubForWsMapper;
import com.zebone.nhis.webservice.pskq.model.CostDetailInpat;
import com.zebone.nhis.webservice.pskq.model.ExamApply;
import com.zebone.nhis.webservice.pskq.model.LabApply;
import com.zebone.nhis.webservice.pskq.repository.PvEncounterRepository;
import com.zebone.nhis.webservice.support.Constant;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.nhis.webservice.vo.EnoteInvInfo;
import com.zebone.nhis.webservice.vo.ItemPriceVo;
import com.zebone.nhis.webservice.vo.LbPiMasterRegVo;
import com.zebone.nhis.webservice.vo.LbSHRequestVo;
import com.zebone.nhis.webservice.vo.PskqSchApptVo;
import com.zebone.nhis.webservice.vo.wechatvo.ItemsVO;
import com.zebone.nhis.webservice.vo.wechatvo.SumItemsVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 *坪山口腔域webservcie专用公共服务
 *
 */
@Service
public class PskqPubForWsService {
	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;
	
	@Resource
	private OpcgPubHelperService opcgPubHelperService;	
	@Resource
	private BlPubForWsService blPubForWsService;
	@Resource
	private SchPubForWsMapper schPubForWsMapper;
	@Resource
	private BdPubForWsMapper bdPubForWsMapper;
	@Resource
	private PvPubForWsMapper pvPubForWsMapper;
	@Resource
	private BlPubForWsMapper blPubForWsMapper;

	ApplicationUtils apputil = new ApplicationUtils();
	private String CODE_ORG = ApplicationUtils.getPropertyValue("yb.codeOrg", "");
	private String NAME_ORG = ApplicationUtils.getPropertyValue("yb.nameOrg", "");
	private String PKINSUSELF = ApplicationUtils.getPropertyValue("msg.constant.pkInsu", "eb2baf4e9848486984516fe6a84d6136");


	/**
	 * 挂号操作
	 * @param
	 * @param
	 * @throws ParseException
	 */
	public Map<String,Object> register(String param,Map<String,Object> requ,User user,LbPiMasterRegVo regvo){
		//响应结果
		Map<String,Object> result = new HashMap<>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pkSchticket",requ.get("pkSchticket"));
		//根据号源主键查新排班信息
		List<Map<String, Object>> schPlanlist = schPubForWsMapper.LbgetSchPlanInfo(paramMap);
		if(schPlanlist.size()<=0){
			result.put("result", "false");
			result.put("message", "号源信息有误，请重新获取");
			return result;
		}
		if(schPlanlist.get(0)==null){
			result.put("result", "false");
			result.put("message", "号源信息有误，请重新获取");
			return result;
		}
		boolean Iffeel = false;
		String payType = CommonUtils.getPropValueStr(requ, "payType");

		regvo.setDateReg(new Date());//挂号日期--排班日期
		regvo.setPkDept(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkDept"));//挂号科室
		if(CommonUtils.getPropValueStr(schPlanlist.get(0),"euSrvtype").equals("9")){
			regvo.setEuPvtype("2");//就诊类型
		}
		regvo.setDtPaymode(payType);//支付类型
		regvo.setPkSchres(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkSchres"));
		regvo.setPkDateslot(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkDateslot"));//日期分组
		regvo.setTicketNo(CommonUtils.getPropValueStr(schPlanlist.get(0),"ticketno"));//预约票号
		regvo.setPkSch(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkSch"));//排班主键
		regvo.setPkSchsrv(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));//排班服务主键


		String payAmt = CommonUtils.getPropValueStr(requ,"payAmt");
		BigDecimal amt = BigDecimal.ZERO.add(new BigDecimal("".equals(payAmt)?"0":payAmt));
		//if(amt.compareTo(BigDecimal.ZERO) != 0){
			BlDeposit dep =new BlDeposit();
			//账户支付可用余额校验
			if("4".equals(payType)){
				result.put("result", "false");
				result.put("message", "挂号暂不支持账户支付");
				return result;
			}else if(!"1".equals(payType) && !"4".equals(payType)){
				dep.setPayInfo(CommonUtils.getPropValueStr(requ, "orderno"));
			}

			dep.setAmount(amt);
			dep.setDtPaymode(payType);//支付方式
			List<BlDeposit> depList =new ArrayList<>();
			depList.add(dep);
			regvo.setDepositList(depList);
			Iffeel = true;
			//门诊诊察费明细
			/*List<ItemPriceVo> itemList = bdPubForWsMapper.LbgetBdItemInfo(CommonUtils.getPropValueStr(schPlanlist.get(0),"pkSchsrv"));
			regvo.setItemList(itemList);*/
		//}

		//判断是否允许挂号
		String date = regvo.getDateAppt()!=null?DateUtils.formatDate(regvo.getDateAppt(), "yyyyMMdd"): (regvo.getDateReg()!=null?DateUtils.formatDate(regvo.getDateReg(), "yyyyMMdd"):null);
		if(StringUtils.isNotBlank(date)) {
			if(DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{regvo.getPkPi(),date,regvo.getPkDept()}) >0){
				result.put("result", "false");
				result.put("message", "该患者已经存在当前日期和科室的挂号记录！");
				return result;
			}
		}

		//保存挂号信息（含保存患者信息）
		regvo = savePvRegInfo(Iffeel, regvo, user);
		Map<String, Object> PatMap = new HashMap<String, Object>();
		PatMap.put("pkPi", regvo.getPkPi());
		PatMap.put("pkPv", regvo.getPkPv());
		PatMap.put("pkEmp", regvo.getPkEmp());
		List<BlInvoice> blInvoiceList = regvo.getBlInvoiceList();
		if(blInvoiceList != null && blInvoiceList.size() > 0) {
			result.put("ebillno", blInvoiceList.get(0).getEbillno());
			result.put("ebillbatchcode", blInvoiceList.get(0).getEbillbatchcode());
			result.put("checkcode", blInvoiceList.get(0).getCheckcode());
			result.put("dateEbill", blInvoiceList.get(0).getDateEbill());
			result.put("qrcodeEbill", blInvoiceList.get(0).getQrcodeEbill());
			result.put("urlEbill", blInvoiceList.get(0).getUrlEbill());
			result.put("urlNetebill", blInvoiceList.get(0).getUrlNetebill());
		}

		Map<String, Object> piCardMap = DataBaseHelper.queryForMap("select CARD_NO from PI_CARD where FLAG_ACTIVE = '1' AND EU_STATUS ='0' and DT_CARDTYPE ='01' and pk_pi=? ORDER BY create_time DESC", new Object[]{regvo.getPkPi()});
		if(piCardMap != null){
			result.put("cardNo", CommonUtils.getPropValueStr(piCardMap,"cardNo"));
		}else{
			result.put("cardNo",null);
		}
		//如果是预约挂号更新预约信息
		if(!CommonUtils.isEmptyString(CommonUtils.getPropValueStr(requ, "apptCode"))){
			DataBaseHelper.update("update sch_appt set eu_status='1',flag_pay='1' where code = ?  ", new Object[]{CommonUtils.getPropValueStr(requ,"apptCode")});
		}


		//更新第三方支付数据
		if(!"".equals(CommonUtils.getPropValueStr(requ,"orderno"))){
			updateBlExtPay(regvo,requ);
		}

		//生成医保记录  ins_szyb_st  ins_szyb_st_city  ins_szyb_st_citydt  ins_szyb_visit  ins_szyb_visit_city
		if("1".equals(CommonUtils.getPropValueStr(requ,"medicalType"))){
			saveInsSzybInfo(regvo,requ,user);
		}
		
		result.put("price", payAmt);//挂号费
		result.put("nameDept", CommonUtils.getPropValueStr(schPlanlist.get(0),"name"));//科室名称
		result.put("codeDept", CommonUtils.getPropValueStr(schPlanlist.get(0),"codeDept"));//科室编码
		result.put("namePlace", CommonUtils.getPropValueStr(schPlanlist.get(0),"namePlace"));//候诊地点
		result.put("ticketno", CommonUtils.getPropValueStr(schPlanlist.get(0),"ticketno"));//票号
		result.put("nameEmp", CommonUtils.getPropValueStr(schPlanlist.get(0),"nameEmp"));//医生名称
		result.put("codeEmp", CommonUtils.getPropValueStr(schPlanlist.get(0),"codeEmp"));//医生编码
		result.put("stvName", CommonUtils.getPropValueStr(schPlanlist.get(0),"stvName"));//排班服务名称
		result.put("codeOp", regvo.getCodeOp());//就诊流水号
		result.put("codePv", regvo.getCodePv());//门诊流水号
		result.put("pkPv", regvo.getPkPv());//就诊唯一主键
		result.put("pkSch", regvo.getPkSch());//排班唯一主键
		result.put("codeAppt", CommonUtils.getPropValueStr(requ, "codeAppt"));//预约单号

		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", user.getPkEmp());
		msgParam.put("nameEmp", user.getNameEmp());
		msgParam.put("codeEmp",user.getCodeEmp());
		msgParam.put("pkPv", regvo.getPkPv());
		msgParam.put("isAdd", "0");
		msgParam.put("timePosition", CommonUtils.isEmptyString(CommonUtils.getPropValueStr(requ, "codeAppt")) ? "1" : "0" );//0：预约     1：当天
		msgParam.put("addrPosition", CommonUtils.getPropValueStr(requ,"addrPosition"));//0：现场 1：自助机 2：电话 3：微信  4：支付宝 5：健康网
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		msgParam = null;

		return result;
	}

	/**
	 * 保存挂号信息(有事物)
	 * @param
	 * @param
	 */
	public LbPiMasterRegVo savePvRegInfo(boolean Iffeel,LbPiMasterRegVo regvo,User u){
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(u.getPkOrg());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = schPubForWsMapper.querySchResInfo(paramMap);
		//2.保存就诊记录
		PvEncounter pv = savePvEncounter(regvo, pkPv,u);
		regvo.setCodePv(pv.getCodePv());
		regvo.setAgePv(pv.getAgePv());
		//3.生成门诊就诊记录，写表pv_op；
		if("1".equals(pv.getEuPvtype())){
			savePvOp(regvo,pv,schres);//门诊就诊属性
		}else{
			//急诊就诊属性
			savePvEr(regvo, pv);
		}
		//判断是否有挂号费用
		if(Iffeel){
			/**
			 * 3.生成记费信息，写表bl_ip_dt；
			 * 4.生成结算信息，写表bl_settle；
			 * 5.生成结算明细，写表bl_settle_detail；
			 * 6.生成支付记录，写表bl_deposit；
			 * 如果挂号打印发票：
			 * 7.生成发票信息，写表bl_invoice；
			 * 8.生成发票和结算关系，写表bl_st_inv；
			 * 9.生成发票明细，bl_invoice_dt；
			 * 10.更新发票登记表，更新bl_emp_invoice。
			 */
			regvo = saveSettle(regvo, pv,u);
		}
		//有号表方式，更新排班已使用号数
		//DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
		schPubForWsMapper.updateSchCntUsed(regvo.getPkSch());

		return regvo;
	}


	/**
	 * 保存就诊记录门诊属性
	 * @return
	 */
	private PvOp savePvOp(LbPiMasterRegVo master,PvEncounter pv,Map<String,Object> schres){
		// 保存门诊属性
		PvOp pvOp = new PvOp();
		pvOp.setPkPv(pv.getPkPv());
		Integer opTimes = pvPubForWsMapper.LbgetMaxOpTimes(master.getPkPi());
		pvOp.setOpTimes(new Long(opTimes+1));
		pvOp.setPkSchsrv(master.getPkSchsrv());
		pvOp.setPkRes(master.getPkSchres());
		pvOp.setPkDateslot(master.getPkDateslot());
		pvOp.setPkDeptPv(master.getPkDept());
		if(schres!=null){
			pvOp.setPkEmpPv(CommonUtils.getString(schres.get("pkEmp")));
			pvOp.setNameEmpPv(CommonUtils.getString(schres.get("nameEmp")));
		}
		pvOp.setTicketno(CommonUtils.isEmptyString(master.getTicketNo())?0:Long.parseLong(master.getTicketNo()));
		pvOp.setPkSch(master.getPkSch());
		pvOp.setFlagFirst("1"); // 初诊
		pvOp.setPkAppo(master.getPkAppt()); // 字段重复
		pvOp.setPkSchappt(master.getPkAppt());// 对应预约
		if(CommonUtils.isNull(master.getPkAppt())){//挂号方式
			pvOp.setEuRegtype("0");
		}else{
			pvOp.setEuRegtype("1");
		}
		pvOp.setDtApptype("0");
		// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
		// ( 参数-1) || '23:59:59'
		pvOp.setDateBegin(pv.getDateBegin());

		if(!"9".equals(master.getEuSrvtype())){
			pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pv.getDateBegin()));
		}else{//急诊号24小时有效
			pvOp.setDateEnd(DateUtils.strToDate(DateUtils.addDate(pv.getDateBegin(), 24, 4, "yyyyMMddHHmmss")));
		}
		pvOp.setFlagNorelease("0");
		DataBaseHelper.insertBean(pvOp);
		return pvOp;
	}

	/**
	 * 保存就诊记录急诊属性
	 * @return
	 */
	private PvEr savePvEr(LbPiMasterRegVo master,PvEncounter pv){
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
		Map<String,Object> schres =schPubForWsMapper.querySchResInfo(paramMap);
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
		pvEr.setDateArv(new Date());
		DataBaseHelper.insertBean(pvEr);
		return pvEr;
	}

	/**
	 * 保存就诊记录
	 * @param master
	 * @param pkPv
	 * @return
	 */
	private PvEncounter savePvEncounter(LbPiMasterRegVo master,String pkPv,User user){

		// 保存就诊记录
		boolean jz = "2".equals(master.getEuPvtype());
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		pvEncounter.setPkOrg(user.getPkOrg());
		pvEncounter.setPkPi(master.getPkPi());
		pvEncounter.setPkDept(master.getPkDept());//就诊科室
		pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
		pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : PvConstant.ENCOUNTER_EU_PVTYPE_1 ); // 急诊|门诊
		pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
		pvEncounter.setNamePi(master.getNamePi());
		pvEncounter.setDtSex(master.getDtSex());
		pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
		pvEncounter.setAddress(master.getAddress());
		pvEncounter.setFlagIn("0");
		pvEncounter.setFlagSettle("1");
		pvEncounter.setDtMarry(master.getDtMarry());
		pvEncounter.setPkInsu(master.getPkHp());
		pvEncounter.setPkPicate(master.getPkPicate());
		pvEncounter.setPkEmpReg(user.getPkEmp());
		pvEncounter.setNameEmpReg(user.getNameEmp());
		pvEncounter.setDateReg(new Date());
		if(master.getDateAppt()!=null){//如果是预约挂号，开始日期为预约日期
			pvEncounter.setDateBegin(master.getDateAppt());
		}else{
			pvEncounter.setDateBegin(master.getDateReg());//挂号的排班日期
		}
		//只保存pv_op表
		//if(schres!=null){
		//	pvEncounter.setPkEmpPhy(CommonUtils.getString(schres.get("pkEmp")));
		//	pvEncounter.setNameEmpPhy(CommonUtils.getString(schres.get("nameEmp")));
		//}
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

		pvEncounter.setCreator(user.getPkEmp());
		pvEncounter.setCreateTime(new Date());
		pvEncounter.setDelFlag("0");
		pvEncounter.setTs(new Date());
		DataBaseHelper.insertBean(pvEncounter);

		return pvEncounter;
	}

	/**
	 * 保存结算信息（包含门诊收费明细，门诊结算，门诊结算明细，交易记录，发票信息，发票明细）
	 * @return
	 * @throws BusException
	 */
	private LbPiMasterRegVo saveSettle(LbPiMasterRegVo master,PvEncounter pv,User user) throws BusException {
		//没有收费项时不做收费处理
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return master;

		String pkOrg = master.getPkOrg();
		String pkPi = master.getPkPi();
		String pkPv = master.getPkPv();

		String pkCurDept = user.getPkDept();// 当前科室
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		String nameUser = user.getNameEmp();// 当前用户名

		//1.保存门诊收费明细
		List<BlOpDt> blOpDts = constructBlOpDt(master, pv,user);
		if(blOpDts!=null&&blOpDts.size()>0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlOpDt.class), blOpDts);
		//2.生成结算
		BigDecimal amountSt = BigDecimal.ZERO;// 结算金额
		BigDecimal amountPi = BigDecimal.ZERO;// 患者自付金额
		BigDecimal amountInsu = BigDecimal.ZERO;// 医保支付金额
		BigDecimal discAmount = BigDecimal.ZERO;// 患者优惠金额
		BigDecimal accountPrepaid = BigDecimal.ZERO;// 账户已付
		String pkDisc = null;
		for (BlOpDt bpt : blOpDts) {
			amountSt = amountSt.add(new BigDecimal(bpt.getAmount()));
			amountPi = amountPi.add(new BigDecimal(bpt.getAmountPi()));
			//医保优惠计费部分
			amountInsu = amountInsu.add(new BigDecimal(0.00));
			pkDisc = bpt.getPkDisc();// 优惠类型
			if (pkDisc != null) {
				//患者优惠
				discAmount = discAmount.add(new BigDecimal(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan())));
			}
			if ("1".equals(bpt.getFlagAcc())) {
				accountPrepaid = accountPrepaid.add(new BigDecimal(bpt.getAmountPi()));
			}
		}
		//第三方医保支付金额
		BigDecimal amtInsuThird = master.getAmtInsuThird()==null?BigDecimal.ZERO:master.getAmtInsuThird();
		if(amtInsuThird.compareTo(BigDecimal.valueOf(0D))!=0){
			amountPi = amountSt.subtract(amtInsuThird);
		}
		BlSettle bs = new BlSettle();
		bs.setPkOrg(pkOrg);
		bs.setPkPi(pkPi);
		bs.setPkPv(pkPv);
		bs.setPkInsurance(pv.getPkInsu());
		bs.setDtSttype("00");// 结算类型
		bs.setEuStresult(EnumerateParameter.ZERO);// 结算结果分类
		bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
		//此处患者支付还没有排除结算医保分摊的金额，同样医保支付还没有包含结算时医保分摊的金额
		bs.setAmountPi(amountPi.setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setAmountInsu(amountInsu.add(discAmount).add(amtInsuThird).setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setDateSt(new Date());
		bs.setPkOrgSt(pkOrg);
		bs.setPkDeptSt(pkCurDept);
		bs.setPkEmpSt(pkOpDoctor);
		bs.setNameEmpSt(nameUser);
		bs.setFlagCc(EnumerateParameter.ZERO);
		bs.setFlagCanc(EnumerateParameter.ZERO);
		bs.setFlagArclare(EnumerateParameter.ZERO);
		bs.setAmountPrep(BigDecimal.ZERO);
		bs.setAmountAdd(0d);
		bs.setAmountDisc(0d);
		bs.setCodeSt(ApplicationUtils.getCode("0604"));
		bs.setReceiptNo(master.getReceiptNo());
		bs.setDateReceipt(master.getDateReceipt());
		bs.setPkEmpReceipt(master.getPkEmpReceipt());
		bs.setNameEmpReceipt(master.getNameEmpReceipt());
		bs.setEuPvtype(pv.getEuPvtype());

		bs.setPkOrg(pkOrg);
		bs.setCreator(pkOpDoctor);
		bs.setCreateTime(new Date());
		bs.setDelFlag("0");
		bs.setTs(new Date());
		bs.setModifier(pkOpDoctor);

		DataBaseHelper.insertBean(bs);
		String pkSettle = bs.getPkSettle();
		// 将结算主键反写到记费细目表
		List<BlOpDt> blOpDtNews = new ArrayList<BlOpDt>();
		StringBuilder pkBlOpDts = new StringBuilder("");
		for (BlOpDt bpt : blOpDts) {
			BlOpDt bodNew = new BlOpDt();
			bodNew.setPkSettle(pkSettle);
			bodNew.setFlagSettle(EnumerateParameter.ONE);
			bodNew.setTs(new Date());
			bodNew.setPkCgop(bpt.getPkCgop());
			blOpDtNews.add(bodNew);
			pkBlOpDts = pkBlOpDts.append("'").append(bpt.getPkCgop()).append("',");
		}
		//批量更新blOpDtNews
		blPubForWsMapper.updateBlOpDtList(blOpDtNews);
		//DataBaseHelper.batchUpdate("update bl_op_dt set amount_hppi=:amountHppi,amount_pi=:amountPi,pk_settle=:pkSettle,flag_settle=:flagSettle,ts=:ts where pk_cgop=:pkCgop ", blOpDtNews);

		//3、生成结算明细
		/*
		 * 结算明细组成说明：
		 * （1）记费表bl_op_dt内部医保支付金额;
		 * （2）记费表bl_op_dt优惠比例金额；
		 * （3）结算时第三方医保支付金额；
		 * （4）结算时每次调动医保返回的报销金额；
		 * （5）患者自付金额。
		 */
		Map<String,Object> mapParamTemp = new HashMap<String,Object>();
		List<BlSettleDetail> blSettleDetails = new ArrayList<BlSettleDetail>();
		BdHp bdHp = null;
		// 3.1、内部主医保项目和种类支付金额
		if (amountInsu.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pv.getPkInsu());
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		//3.2 外部医保支付金额
		if(amtInsuThird.compareTo(BigDecimal.ZERO)==1){
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pv.getPkInsu());
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		// 3.3、患者的优惠比例金额
		if (discAmount.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.clear();
			mapParamTemp.put("pkHp", pkDisc);
			mapParamTemp.put("pkOrg", pkOrg);
			bdHp = blPubForWsService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pkDisc);// 优惠类型主键
			blSettleDetail.setAmount(discAmount.doubleValue());

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		//不计算总额分摊的情况
		//3.4、患者支付金额
		if (amountPi.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkStdt(NHISUUID.getKeyId());
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.put("pkOrg", "~                               ");
			BdPayer bdPayer = blPubForWsService.qryBdPayerByEuType(mapParamTemp);
			if (bdPayer == null) {
				throw new BusException("未维护支付方为本人的医保计划");
			}
			//查询付款方式为本人的医保主键
			BdHp hp = DataBaseHelper.queryForBean(
					"select * from bd_hp where PK_PAYER = ? and EU_HPTYPE = '0'",
					BdHp.class, new Object[]{bdPayer.getPkPayer()});
			blSettleDetail.setPkPayer(bdPayer.getPkPayer());// 支付方(本人)
			blSettleDetail.setPkInsurance(hp.getPkHp());//自费部分，全自费主键
			blSettleDetail.setAmount(amountPi.doubleValue());// 患者自付金额

			blSettleDetail.setCreator(pkOpDoctor);
			blSettleDetail.setCreateTime(new Date());
			blSettleDetail.setDelFlag("0");
			blSettleDetail.setTs(new Date());
			blSettleDetail.setModifier(pkOpDoctor);
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		// 批量统一插入结算明细表
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlSettleDetail.class), blSettleDetails);

		// 4、写结算交款记录表
		List<BlDeposit> depositListDao = new ArrayList<BlDeposit>();
		List<BlDeposit> depositList = master.getDepositList();// 支付方式

		for (BlDeposit blDeposit : depositList) {
			BlDeposit blDepositDao = new BlDeposit();
			blDepositDao.setPkDepo(NHISUUID.getKeyId());
			blDepositDao.setCreator(pkOpDoctor);
			blDepositDao.setCreateTime(new Date());
			blDepositDao.setDelFlag("0");
			blDepositDao.setTs(new Date());
			blDepositDao.setModifier(pkOpDoctor);

			blDepositDao.setPkOrg(pkOrg);
			blDepositDao.setEuDptype(EnumerateParameter.ZERO);
			blDepositDao.setEuDirect(EnumerateParameter.ONE);
			blDepositDao.setPkPi(pkPi);
			blDepositDao.setPkPv(pkPv);
			blDepositDao.setEuPvtype(pv.getEuPvtype());
			// 交易金额
			blDepositDao.setAmount(blDeposit.getAmount());
			blDepositDao.setDtPaymode(master.getDtPaymode());
			blDepositDao.setPayInfo(blDeposit.getPayInfo()); //收付款方式信息 对应支票号，银行交易号码
			blDepositDao.setDatePay(new Date());
			blDepositDao.setPkDept(pkCurDept);
			blDepositDao.setPkEmpPay(pkOpDoctor);
			blDepositDao.setNameEmpPay(nameUser);
			blDepositDao.setFlagAcc(blDepositDao.getFlagAcc() == null ? EnumerateParameter.ZERO : EnumerateParameter.ONE);
			blDepositDao.setFlagSettle(EnumerateParameter.ONE);
			blDepositDao.setPkSettle(bs.getPkSettle());
			blDepositDao.setFlagCc(EnumerateParameter.ZERO);// 操作员结账标志
			depositListDao.add(blDepositDao);
		}
		master.setDepositList(depositListDao);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depositListDao);
		//判断是否对发票信息校验LB自助机使用, -2：不处理 （第三方调用，走电子票据，不走纸质票据）
		if(master.getInvStatus() == null || !("-2").equals(master.getInvStatus())){
		    if("1".equals(ApplicationUtils.getSysparam("BL0002", false))){//门诊挂号使用发票
		    	//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
		    	String eBillFlag = getBL0031ByNameMachine(master.getNameMachine());
				if("1".equals(eBillFlag)){
					try{
						Map<String, Object> retInv = new HashMap<>(16);						
						//调用开立票据接口生成票据信息
						retInv = eBillRegistration(pkPv,user,pkSettle);
						master.setBlInvoiceList((List<BlInvoice>)retInv.get("invs"));
					}catch(Exception e){
						e.printStackTrace();
						throw new BusException(e.getMessage());
					}
				}
		    }
		}
		master.setPkSettle(pkSettle);
		return master;
	}

	/**
	 * 构建门诊记费参数
	 * @param master
	 * @param pv
	 * @param
	 * @return
	 */
	private List<BlOpDt> constructBlOpDt(LbPiMasterRegVo master,PvEncounter pv,User user){
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return null;
		String codeCg = ApplicationUtils.getCode("0601");
		List<BlOpDt> blopdts = new ArrayList<BlOpDt>();
		List<ItemPriceVo> itemList = new ArrayList<>();
		itemList = (List<ItemPriceVo>)master.getItemList();
		String jsonBean =JsonUtil.writeValueAsString(itemList);
		List<ItemPriceVo> items =new ArrayList<>();
		items = (List<ItemPriceVo>)JsonUtil.readValue(jsonBean, new TypeReference<List<ItemPriceVo>>(){});
		for(ItemPriceVo item:items){
			item.setFlagPd("0");
			BlOpDt vo = new BlOpDt();
			String pkCgop = NHISUUID.getKeyId();
			vo.setPkCgop(pkCgop);
			vo.setSpec(item.getSpec());
			vo.setPkOrg(UserContext.getUser().getPkOrg());
			vo.setPkPv(master.getPkPv());
			vo.setPkPi(master.getPkPi());
			vo.setPkOrgApp(UserContext.getUser().getPkOrg());
			vo.setPkDeptApp(pv.getPkDept());
			vo.setPkOrgEx(UserContext.getUser().getPkOrg());
			vo.setPkSchAppt(master.getPkAppt());
			//如果序号为空，从1开始递增
			vo.setSortno(1);
			vo.setPkEmpApp(CommonUtils.isEmptyString(item.getPkEmpApp())?pv.getPkEmpPhy():item.getPkEmpApp());
			vo.setNameEmpApp(CommonUtils.isEmptyString(item.getNameEmpApp())?pv.getNameEmpPhy():item.getNameEmpApp());
			vo.setPkDeptEx(CommonUtils.isEmptyString(item.getPkDeptEx())?pv.getPkDept():item.getPkDeptEx());
			//医嘱自动记费不写记费部门、记费人员、记费人员姓名字段。
			if(!BlcgUtil.converToTrueOrFalse(item.getFlagSign())){
				vo.setPkDeptCg(CommonUtils.isEmptyString(item.getPkDeptCg())?UserContext.getUser().getPkDept():item.getPkDeptCg());
				vo.setPkEmpCg(CommonUtils.isEmptyString(item.getPkEmpCg())?UserContext.getUser().getPkEmp() : item.getPkEmpCg());
				vo.setNameEmpCg(CommonUtils.isEmptyString(item.getNameEmpCg())?UserContext.getUser().getNameEmp() : item.getNameEmpCg());//当前用户姓名；
			}
			if(CommonUtils.isEmptyString(vo.getPkEmpApp()))
				vo.setPkEmpApp(UserContext.getUser().getPkEmp());
			if(CommonUtils.isEmptyString(vo.getNameCg()))
				vo.setNameEmpApp(UserContext.getUser().getNameEmp());
			//校验dateCg是否为null
			if(item.getDateCg()!=null)
				vo.setDateCg(item.getDateCg());
			else
				vo.setDateCg(new Date());
			//获取当前时间yyyy-MM-dd
			Date dateHap = null;
			if(item.getDateHap()!=null)
				dateHap = DateUtils.strToDate(DateUtils.formatDate(item.getDateHap(), "yyyy-MM-dd"),"yyyy-MM-dd");
			else
				dateHap = DateUtils.strToDate(DateUtils.getDateTime(),"yyyy-MM-dd");
			vo.setDateHap(dateHap);
			vo.setPkUnit(item.getPkUnit());
			vo.setPkUnitPd(item.getPkUnitPd());
			vo.setSpec(item.getSpec());
			vo.setPackSize(1);
			//设置药品相关属性
			if("1".equals(item.getFlagPd())){//药品
				vo.setPkPd(item.getPkOrdOld());
				vo.setPkUnit(item.getPkUnitPd());
				vo.setNameCg(item.getName());
			}
			vo.setBatchNo(item.getBatchNo());
			vo.setPrice(item.getPrice());
			vo.setFlagPv("1");//挂号结算标志
			vo.setPriceCost(item.getPriceCost());
			vo.setDateExpire(item.getDateExpire());
			vo.setPkItem(item.getPkItem());
			vo.setNameCg(item.getNameCg());
			vo.setPkItemcate(item.getPkItemcate());
			vo.setFlagInsu("0");
			vo.setFlagPd(item.getFlagPd());
			vo.setPkPres(item.getPkPres());
			vo.setPkCnord(item.getPkCnord());
			vo.setPkCgopBack(null);
			vo.setFlagSettle("0");
			vo.setCodeCg(codeCg);
			vo.setFlagAcc("0");

			// 设置单价，数量，金额，比例等信息
			// price_org,price,quan,amount,pk_disc,ratio_disc,ratio_self,amount_pi
			vo.setQuan(item.getQuan());
			vo.setPriceOrg(item.getPriceOrg());
			vo.setPkDisc(item.getPkDisc());
			vo.setRatioDisc(item.getRatioDisc()==null?1D:item.getRatioDisc());
			vo.setRatioSelf(item.getRatioSelf());

			vo.setRatioAdd(item.getRatioSpec()!=null && BlcgUtil.converToTrueOrFalse(pv.getFlagSpec())?item.getRatioSpec():0D);	//特诊加收比例
			//如果特诊加收比例为0，判断患者年龄是否小于6岁
			BigDecimal age = CnPiPubService.getPvAge(pv.getPkPi());//计算患者年龄
			if((vo.getRatioAdd()==null || vo.getRatioAdd()==0D) && new BigDecimal(6).compareTo(age)>0)
				vo.setRatioAdd(item.getRatioChildren()!=null?item.getRatioChildren():0D);	//特诊加收比例

			//计算项目原始单价
			Double priceOld = MathUtils.div(vo.getPriceOrg(), MathUtils.add(1D, vo.getRatioAdd()), 6);
			//特诊加收金额 price_org*quan*ratio_add
			vo.setAmountAdd(MathUtils.mul(MathUtils.mul(priceOld,vo.getQuan()), vo.getRatioAdd()));

			vo.setFlagRecharge("0");
		   //amount，金额，price_org*quan+amount_add
			vo.setAmount(MathUtils.mul(vo.getQuan(), vo.getPrice()));
			//amount_hppi，患者支付的医保金额，price*quan*ratio_self；
			if("1".equals(item.getFlagHppi())){
				vo.setAmountHppi(item.getAmtHppi().doubleValue());
			}else{
			  vo.setAmountHppi(MathUtils.mul(MathUtils.mul(vo.getPrice(), vo.getQuan()),vo.getRatioSelf()));
			}
			//amount_pi，amount_hppi-[price_org*(1-ratio_disc)*quan]+amount_add，计算结果小于0时为0；
            //Double amt = MathUtils.add(MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan())), vo.getAmountAdd());
			Double amt = MathUtils.sub(vo.getAmountHppi(), MathUtils.mul(MathUtils.mul(vo.getPriceOrg(), MathUtils.sub(1D, vo.getRatioDisc())), vo.getQuan()));
			//因静配自动记费可能会传数量<0的计费项目，所以判断amt_pi字段赋值时增加校验条件。
			if(MathUtils.compareTo(amt, 0D)<0 && vo.getQuan()>0){
				vo.setAmountPi(0D);
			}else{
				vo.setAmountPi(amt);
			}

			Map<String, Object> param = new HashMap<String, Object>();
			param.put("pkItem", vo.getPkItem());
			param.put("pkOrg", vo.getPkOrg());
			param.put("euType", Constant.OPINV);
			param.put("flagPd", vo.getFlagPd());
			Map<String, Object> resBill =  blPubForWsService.qryBillCodeByPkItem(param);
			Map<String, Object> resAccount = blPubForWsService.qryAccountCodeByPkItem(param);

			if(resBill==null || resBill.get("code")==null)
				throw new BusException("调用门诊挂号记费时，【"+vo.getNameCg()+"】未获取到对应账单码，请检查！");

			if(resBill!=null){
				vo.setCodeBill(resBill.get("code")==null?"":(String)resBill.get("code"));
			}
			if(resAccount!=null){
				vo.setCodeAudit(resAccount.get("code")==null?"":(String) resAccount.get("code"));
			}
            ApplicationUtils.setDefaultValue(vo, true);
            //组装最终集合之前过滤amount=0的记费信息，保存amount=0的信息在结算时上传记费明细到医保端可能会出问题，所以记费时过滤费用为0的项目。
            if(!MathUtils.equ(vo.getAmount(), 0.0)
            		&& !MathUtils.equ(vo.getQuan(), 0.0))
            {
            	blopdts.add(vo);
            }
		}
		return blopdts;
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
	 * 挂号收费 更新第三方收费表
	 * @param
	 * @param
	 * @return
	 * @throws ParseException
	 */
	private BlExtPay updateBlExtPay(LbPiMasterRegVo regvo,Map<String, Object> map){
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where del_flag = '0' and SERIAL_NO = ?", BlExtPay.class, CommonUtils.getPropValueStr(map,"orderno"));
		String dateTrans = CommonUtils.getPropValueStr(map,"dateTrans");
		Date date = "".equals(dateTrans)?new Date():DateUtils.strToDate(dateTrans, "yyyy-MM-dd HH:mm:ss");
		blExtPay.setDateAp(date);//请求时间
		blExtPay.setDatePay(date);//支付时间
		blExtPay.setPkPi(regvo.getPkPi());
		blExtPay.setPkPv(regvo.getPkPv());
		blExtPay.setPkSettle(regvo.getPkSettle());
		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
 		Map<String,Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{regvo.getPkSettle()});
		blExtPay.setPkDepo(CommonUtils.getPropValueStr(depoMap, "pkDepo"));
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}

	/**
     * 深圳医保
     * 生成医保记录
     * ins_szyb_st
     * ins_szyb_st_city
     * ins_szyb_st_citydt
     * ins_szyb_visit
     * ins_szyb_visit_city
     * @param regvo
     * @param map
     */
    private void saveInsSzybInfo(LbPiMasterRegVo regvo, Map<String,Object> map,User u){
		String pkOpDoctor = u.getPkEmp();// 当前用户主键

    	InsSzybVisit insSzybVisit = new InsSzybVisit();
    	String pkVisit = NHISUUID.getKeyId();
    	insSzybVisit.setPkVisit(pkVisit);
    	insSzybVisit.setPkOrg(regvo.getPkOrg());
    	insSzybVisit.setPkHp(regvo.getPkHp());
    	insSzybVisit.setPkPv(regvo.getPkPv());
    	insSzybVisit.setPkPi(regvo.getPkPi());
    	insSzybVisit.setCodeCenter("");
    	insSzybVisit.setCodeOrg(CODE_ORG);//医院编码
    	insSzybVisit.setNameOrg(NAME_ORG);//医院名称
    	insSzybVisit.setTransid(CommonUtils.getPropValueStr(map,"serialNumber"));//交易流水号
    	//zpr-4
    	insSzybVisit.setCodeInsst(CommonUtils.getPropValueStr(map,"bke384"));//机构结算业务号
    	//zpr-3  医保登记号
    	insSzybVisit.setPvcodeIns(CommonUtils.getPropValueStr(map,"akc190"));//就医登记号
    	insSzybVisit.setNamePi(regvo.getNamePi());
    	insSzybVisit.setPersontype(CommonUtils.getPropValueStr(map,"aka130"));//医疗类别(默认普通门诊)  CommonUtils.getPropValueStr(map,"code")
    	insSzybVisit.setEuResctype("");//救助对象类型	0 非医疗救助类别；1；低保对象；2 特困供养人员；3 孤儿；4 建档立卡贫困人员；5 低收入家庭的老年人；6 低收入家庭的未成年人；7 低收入家庭的重度残疾人；8 低收入家庭的重病患者；9 其他
    	insSzybVisit.setBirthDate(regvo.getBirthDate());
    	insSzybVisit.setIdno(regvo.getIdNo());
    	insSzybVisit.setCodeAreayd("440300");//深圳市行政区划代码
    	insSzybVisit.setDtExthp("03");//深圳市医保
    	insSzybVisit.setDateReg(new Date());
    	insSzybVisit.setDtSttypeIns("");//结算类型
    	insSzybVisit.setEuStatusSt("0");//门诊：0结算中（挂号登记开始） 1结算完成（挂号登记完成）
    	insSzybVisit.setCodeMedino(CommonUtils.getPropValueStr(map,"ylzh"));//医疗证号加密串
    	insSzybVisit.setCreator(pkOpDoctor);
    	insSzybVisit.setCreateTime(new Date());
    	insSzybVisit.setTs(new Date());
    	insSzybVisit.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybVisit);

    	InsSzybSt insSzybSt = new InsSzybSt();
    	String pkInsst = NHISUUID.getKeyId();
    	insSzybSt.setPkInsst(pkInsst);
    	insSzybSt.setPkVisit(pkVisit);
    	insSzybSt.setPkOrg(regvo.getPkOrg());
    	insSzybSt.setCodeHpst(CommonUtils.getPropValueStr(map,"ckc618"));//医保业务号
    	insSzybSt.setPkHp(regvo.getPkHp());
    	insSzybSt.setPkPv(regvo.getPkPv());
    	insSzybSt.setPkPi(regvo.getPkPi());
    	insSzybSt.setPkSettle(regvo.getPkSettle());
    	insSzybSt.setPvcodeIns(CommonUtils.getPropValueStr(map,"akc190"));//就医登记号
    	insSzybSt.setDateSt(new Date());
    	insSzybSt.setCodeSerialno(CommonUtils.getPropValueStr(map, "bke384"));//机构结算业务号
    	insSzybSt.setAmount(Double.valueOf((String) (CommonUtils.getPropValueStr(map,"akc264")==""?0:CommonUtils.getPropValueStr(map,"akc264"))));//结算金额
    	insSzybSt.setCodeCenter("");
    	insSzybVisit.setCodeOrg(CODE_ORG);//医院编码
    	insSzybVisit.setNameOrg(NAME_ORG);//医院名称
    	insSzybSt.setCodeSerialno(CommonUtils.getPropValueStr(map,"bke384"));//机构结算业务号
    	insSzybSt.setCreator(pkOpDoctor);
    	insSzybSt.setCreateTime(new Date());
    	insSzybSt.setTs(new Date());
    	insSzybSt.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybSt);

    	InsSzybStCity insSzybStCity = new InsSzybStCity();
    	String pkInsstCity = NHISUUID.getKeyId();
    	insSzybStCity.setPkInsstcity(pkInsstCity);
    	insSzybStCity.setPkInsst(pkInsst);
    	insSzybStCity.setDtMedicate(CommonUtils.getPropValueStr(map,"aka130"));//医疗类别
    	insSzybStCity.setAmtJjzf(Double.valueOf((String) (CommonUtils.getPropValueStr(map,"akb068")==""?0:CommonUtils.getPropValueStr(map,"akb068"))));//基金支付金额
    	insSzybStCity.setAmtGrzhzf(Double.valueOf((String) (CommonUtils.getPropValueStr(map,"akb066")==""?0:CommonUtils.getPropValueStr(map,"akb066"))));//个人帐户支付金额
    	insSzybStCity.setAmtGrzf(Double.valueOf((String) (CommonUtils.getPropValueStr(map,"akb067")==""?0:CommonUtils.getPropValueStr(map,"akb067"))));//个人支付金额
    	//insSzybStCity.setAmtGrzh(0.0);//个人账户金额
    	insSzybStCity.setCreator(pkOpDoctor);
    	insSzybStCity.setCreateTime(new Date());
    	insSzybStCity.setTs(new Date());
    	insSzybStCity.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybStCity);


    	List<Map<String, Object>> outputlist1 = (List<Map<String, Object>>)map.get("outputlist1");
		List<Map<String, Object>> outputlist2 = (List<Map<String, Object>>)map.get("outputlist2");
		List<Map<String, Object>> outputlist3 = (List<Map<String, Object>>)map.get("outputlist3");
    	List<InsSzybStCitydt> InsSzybStCitydtList = new ArrayList<InsSzybStCitydt>();
		for(int i=0;i < outputlist1.size();i++){
			Map<String, Object> m = outputlist1.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aka111"));//大类代码
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aka111"));//大类代码
			String amt=CommonUtils.getPropValueStr(m, "bka058");//费用金额
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
			citydt.setCreator(u.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			InsSzybStCitydtList.add(citydt);
		}
		for(int i=0;i < outputlist2.size();i++){
			Map<String, Object> m = outputlist2.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aaa036"));//支付项目代码
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aaa036"));//支付项目代码
			String amt=CommonUtils.getPropValueStr(m, "aae019");//金额
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
			citydt.setCreator(u.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			InsSzybStCitydtList.add(citydt);
		}
		for(int i=0;i < outputlist3.size();i++){
			Map<String, Object> m = outputlist3.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aka037"));//个人累计信息类别
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aka037"));//个人累计信息类别
			String amt=CommonUtils.getPropValueStr(m, "bke264");//可用值
			amt = "".equals(amt)?"0":amt;
			citydt.setAmtFee(Double.valueOf(amt));//费用金额
			citydt.setPkInsstcitydt(NHISUUID.getKeyId());
			citydt.setPkInsstcity(insSzybStCity.getPkInsstcity());
			citydt.setCreator(u.getPkEmp());
			citydt.setCreateTime(new Date());
			citydt.setDelFlag("0");
			citydt.setTs(new Date());
			InsSzybStCitydtList.add(citydt);
		}

    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(InsSzybStCitydt.class),InsSzybStCitydtList);

    	InsSzybVisitCity  insSzybVisitCity = new InsSzybVisitCity();
    	String pkVisitcity = NHISUUID.getKeyId();
    	insSzybVisitCity.setPkVisitcity(pkVisitcity);
    	insSzybVisitCity.setPkOrg(regvo.getPkOrg());
    	insSzybVisitCity.setPkVisit(pkVisit);
    	insSzybVisitCity.setPkPv(regvo.getPkPv());
    	insSzybVisitCity.setAaz515(CommonUtils.getPropValueStr(map,"aaz515"));//社保卡状态
    	insSzybVisitCity.setAaz500(CommonUtils.getPropValueStr(map,"aaz500"));//社保卡号
    	insSzybVisitCity.setAac001(1l);//人员ID
    	insSzybVisitCity.setAac999(CommonUtils.getPropValueStr(map,"aac999"));//人员电脑号
    	insSzybVisitCity.setCka303(CommonUtils.getPropValueStr(map,"cka303"));//大病类别
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
    	insSzybVisitCity.setAac006(Integer.valueOf(DateUtils.dateToStr("yyyyMMdd", regvo.getBirthDate())));
    	//年纪处理
    	String agePv = regvo.getAgePv();
    	if(agePv!=null){
    		insSzybVisitCity.setBae093(Integer.valueOf(agePv.substring(0, agePv.indexOf("岁"))));
    	}else{
    		throw new BusException("年龄数据错误！"+agePv);
    	}
    	insSzybVisitCity.setCac215(regvo.getAddrOrigin());
    	insSzybVisitCity.setCreator(pkOpDoctor);
    	insSzybVisitCity.setCreateTime(new Date());
    	insSzybVisitCity.setTs(new Date());
    	insSzybVisitCity.setDelFlag("0");
    	DataBaseHelper.insertBean(insSzybVisitCity);

    }

    /**
     * 【门诊】划价【退费】处理逻辑   未完成
     * @param costList
     * @return

    public String disposeOpReBlMessage(List<CostDetail> costList){

		List<Map<String, Object>> pkMap = new ArrayList<Map<String, Object>>();
		Set<String> cnOrdSet = new HashSet<>();
		Set<String> costDetOrdSet = new HashSet<>();

		for (int i = 0; i < costList.size(); i++) {
			costDetOrdSet.add(costList.get(i).getCostDetailId());
		}
		String addCost="";
		for (int i = 0; i < costDetOrdSet.size(); i++) {
			if(i==costDetOrdSet.size()-1){
				addCost+="'"+costDetOrdSet.iterator().next()+"')";
			}else{
				addCost+="'"+costDetOrdSet.iterator().next()+"',";
			}
		}

		pkMap =DataBaseHelper.queryForList("select pk_cgip,pk_cnord from  BL_IP_DT where flag_settle='0' and  pk_ordexdt in ("+addCost+" and del_flag = '0' ");
		if(pkMap.size()<1){
			return "未查询该费用明细ID("+addCost;
		}
		for (int j = 0; j < pkMap.size(); j++) {
			List<Map<String, Object>> pkCgIpBackMap =DataBaseHelper.queryForList("select pk_cgip from  BL_IP_DT where flag_settle='0' and  pk_cgip_back = '"+SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCgip")+"' and del_flag = '0' ");
			if(pkCgIpBackMap.size()>0){
				return "未查询该费用明细ID("+addCost+"已退费,请勿重复退费";
			}
			cnOrdSet.add(SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCnord"));
		}



		if(cnOrdSet.size()==0)
			return "未查询到所有的医嘱ID信息";

		String sql = "SELECT emp.pk_org,emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.code_EMP =?";
		Map<String, Object> exDoc = DataBaseHelper.queryForMap(sql,costList.get(0).getChargeOperaId());
		if(exDoc==null){
			throw new BusException("根据记账人ID("+costList.get(0).getChargeOperaId()+"),未查询到人员信息");
		}

	    List<RefundVo> reFund = new ArrayList<RefundVo>();//退费集合

		//判断是已经记费并且未退费
		//查询收费主键
		String addPkCnords="";
		for (int i = 0; i < cnOrdSet.size(); i++) {
			if(i==cnOrdSet.size()-1){
				addPkCnords+="'"+cnOrdSet.iterator().next()+"')";
			   }else{
				addPkCnords+="'"+cnOrdSet.iterator().next()+"',";
			   }
		}
		String sqlBl = "select * from (select blip.pk_org,blip.pk_cgip,blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan  from BL_IP_DT blip  left outer join  (select sum(quan) quan,sum(amount) amt,pk_cgip_back from bl_ip_dt  where flag_settle = 0 and flag_pd = 0 and  quan < 0 and pk_ordexdt in ("+addCost+"  and PK_CNORD in ( "+addPkCnords+" group by pk_cgip_back ) back on blip.pk_cgip=back.pk_cgip_back where blip.del_flag = '0' and blip.pk_cgip_back is null and blip.flag_settle='0' and blip.quan >0 and blip.pk_ordexdt in ("+addCost+" and blip.pk_cnord in ( "+addPkCnords+" ) dt where dt.quan>0 ";
		List<Map<String, Object>> BlList = DataBaseHelper.queryForList(sqlBl);
		Double num = 0.0;
		for (int i = 0; i < BlList.size(); i++) {
			Map<String, Object> blMap = BlList.get(i);
			if(!"".equals(SDMsgUtils.getPropValueStr(blMap,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(blMap,"quan"));
			}
			RefundVo refundVo = new RefundVo();
			refundVo.setPkOrg(SDMsgUtils.getPropValueStr(blMap,"pkOrg"));
			refundVo.setPkCgip(SDMsgUtils.getPropValueStr(blMap,"pkCgip"));//计费主建
			refundVo.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
			//refundVo.setNameEmp(Constant.NOTE);
			refundVo.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
			refundVo.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
			refundVo.setQuanRe(num);
			reFund.add(refundVo);
		}
		User user = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(BlList.get(0),"pkOrg"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
		//user.setNameEmp(Constant.NOTE);
		user.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
		UserContext.setUser(user);

		//退费
		if(reFund!=null && reFund.size()>0){
			ApplicationUtils apputil = new ApplicationUtils();
			ResponseJson prePayInfo = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",reFund,user);
			if(prePayInfo.getStatus()<0)
				return "退费失败:"+prePayInfo.getDesc();
			return "succ";
		}else
			return "退费失败:没有可退费用";

    }

	 */
	/**
	 * 【门诊】划价【计费】处理逻辑   未完成
	 * @param surgeryCost
	 * @return

    public String disposeOpBlMessage(CostDetail surgeryCost){
    	List<BlPubParamVo> blCgVos = new ArrayList<>();
		//查询转换所需要的参数
		Map<String, Object> empOccMap =  DataBaseHelper.queryForMap("select pk_emp,name_emp,code_emp from bd_ou_employee where code_emp = '"+surgeryCost.getExecOperaId()+"' and name_emp = '"+surgeryCost.getExecOperaName()+"'");
		if (empOccMap==null) {
			return	"根据执行人ID("+surgeryCost.getExecOperaId()+"),未查询到执行人主建信息";
		}
		Map<String, Object> deptExecMap =  DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = '"+surgeryCost.getExecDeptId()+"' and del_flag = '0' ");
		if (deptExecMap==null) {
			return	"根据执行科室ID("+surgeryCost.getExecDeptId()+"),未查询到执行科室主建信息";
		}

		Map<String, Object> cnOrdMap =  DataBaseHelper.queryForMap("select * from cn_order where ordsn = '"+surgeryCost.getOrderId()+"' and code_ordtype = '04' and del_flag = '0' ");
		if (cnOrdMap==null) {
			return	"根据医嘱ID("+surgeryCost.getOrderId()+"),未查询到医嘱主建信息";
		}

		Map<String, Object> opMap =  DataBaseHelper.queryForMap("select dept.pk_dept,emp.pk_emp,emp.name_emp from bd_ou_employee emp  left join bd_ou_empjob empjob on empjob.pk_emp = emp.pk_emp  left join bd_ou_dept dept  on dept.pk_dept = empjob.pk_dept where empjob.is_main = '1' and emp.code_emp = '"+surgeryCost.getEnterDoctorId()+"'");
		if (opMap==null) {
			return	"根据开立医生ID("+surgeryCost.getEnterDoctorId()+"),未查询到开立医生主建信息";
		}
		Map<String, Object> CgMap =  DataBaseHelper.queryForMap("select emp.pk_emp,dept.pk_dept from bd_ou_employee emp  left join bd_ou_empjob empjob on empjob.pk_emp = emp.pk_emp  left join bd_ou_dept dept  on dept.pk_dept = empjob.pk_dept where empjob.is_main = '1' and emp.code_emp = '"+surgeryCost.getChargeOperaId()+"'");
		if (CgMap==null) {
			return	"根据记账人ID("+surgeryCost.getChargeOperaId()+"),未查询到计费科室主建信息";
		}
		String flagPd = "";
		if("05".equals(surgeryCost.getChargeItemClassCode())||"04".equals(surgeryCost.getChargeItemClassCode())||"03".equals(surgeryCost.getChargeItemClassCode()))
			flagPd= "1";
		else
			flagPd= "0";
		List<Map<String, Object>> bdItem = new ArrayList<Map<String, Object>>();
		if(flagPd.equals("1")){
			//批次还存在问题   batch_no  待处理
			bdItem = DataBaseHelper.queryForList("select bd.*,st.batch_no from bd_pd bd left join pd_inv_init st on st.pk_pd = bd.pk_pd  where bd.code = ? and st.del_flag ='0' and bd.del_flag ='0'",surgeryCost.getChargeItemCode());
		}else{
			bdItem = DataBaseHelper.queryForList("select pk_item from bd_ord_item where pk_ord = (select pk_ord from bd_ord where code = ? and del_flag = '0') and del_flag = '0'",surgeryCost.getChargeItemCode());
		}
		if (bdItem.size()==0) {
			return	"根据收费项目代码("+surgeryCost.getChargeItemCode()+"),未查询到收费项目信息";
		}

		//根据执行单状态判断检验是否计费
		BlPubParamVo vo = new BlPubParamVo();
		 for (int j = 0; j < bdItem.size(); j++) {
			 	// blIpDt.setQuan(charge);//数量
				vo = new BlPubParamVo();
				vo.setPkOrg(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
				vo.setPkOrgApp(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
				vo.setPkOrgEx(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
				vo.setPkPi(SDMsgUtils.getPropValueStr(cnOrdMap,"pkPi"));
				vo.setPkPv(SDMsgUtils.getPropValueStr(cnOrdMap,"pkPv"));
				vo.setEuPvType(SDMsgUtils.getPropValueStr(cnOrdMap,"euPvtype"));
				vo.setPkCnord(SDMsgUtils.getPropValueStr(cnOrdMap,"pkCnord"));
				vo.setPkDeptApp(SDMsgUtils.getPropValueStr(opMap,"pkDept"));//开立科室
				vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(opMap,"pkDept"));//开立病区
				vo.setPkEmpApp(SDMsgUtils.getPropValueStr(opMap,"pkEmp"));//申请主建
				vo.setNameEmpApp(SDMsgUtils.getPropValueStr(opMap,"nameEmp"));//申请人姓名
				vo.setPkDeptEx(SDMsgUtils.getPropValueStr(deptExecMap,"pkDept"));//执行科室
				vo.setDateHap(DateUtils.strToDate(surgeryCost.getExecDateTime(), "yyyyMMddHHmmss"));
				vo.setPkDeptCg(SDMsgUtils.getPropValueStr(CgMap,"pkDept"));//计费科室
				vo.setPkEmpCg(SDMsgUtils.getPropValueStr(CgMap,"pkEmp"));
				//vo.setNameEmpCg(SDMsgUtils.getPropValueStr(cgDoc,"nameEmp"));
				vo.setNameEmpCg("平台接口计费");
				vo.setPkOrdexdt(surgeryCost.getCostDetailId());
				vo.setEuBltype("3");//手麻

				if("1".equals(flagPd)){
					vo.setFlagPd("1");//1为药品
					vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkPd"));
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
					vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkItem"));
				}
				vo.setQuanCg(Double.valueOf(surgeryCost.getChargeQuantity()));
				blCgVos.add(vo);
			}



		//组装用户信息
		User user = new User();
		user.setPkOrg(blCgVos.get(0).getPkOrg());
		user.setPkEmp(blCgVos.get(0).getPkEmpEx());
		user.setNameEmp(blCgVos.get(0).getNameEmpEx());
		//user.setNameEmp(Constant.NOTE);
		user.setPkDept(blCgVos.get(0).getPkDeptEx());
		UserContext.setUser(user);
		//组装记费参数
		BlIpCgVo cgVo = new BlIpCgVo();
		cgVo.setAllowQF(true);
		cgVo.setBlCgPubParamVos(blCgVos);
		ApplicationUtils apputil = new ApplicationUtils();
		ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
		if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
			return rs.getErrorMessage();

    	return "succ";
    }
	*/

	/**
	 * 【住院】划价【计费】处理逻辑
	 *
	 * 收费项目计费
	 * @param
	 * @return
	 */
	public String disposeIpBlMessage(CostDetailInpat costDetailInpat){
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		//查询转换所需要的参数
		Map<String, Object> empOccMap =  DataBaseHelper.queryForMap("select pk_emp,name_emp,code_emp from bd_ou_employee where code_emp = '"+costDetailInpat.getExecOperaId()+"' and name_emp = '"+costDetailInpat.getExecOperaName()+"'");
		if (empOccMap==null) {
			return	"根据执行人ID("+costDetailInpat.getExecOperaId()+"),未查询到执行人主建信息";
		}
		Map<String, Object> deptExecMap =  DataBaseHelper.queryForMap("select pk_dept from bd_ou_dept where code_dept = '"+costDetailInpat.getExecDeptId()+"' and del_flag = '0' ");
		if (deptExecMap==null) {
			return	"根据执行科室ID("+costDetailInpat.getExecDeptId()+"),未查询到执行科室主建信息";
		}
		costDetailInpat.setEncounterId(costDetailInpat.getEncounterId().split("_")[3]);
		//根据就诊流水
		PvEncounter pvEncounter = PvEncounterRepository.findByCode(costDetailInpat.getEncounterId());

		if(pvEncounter == null){
			return	"根据就诊号("+costDetailInpat.getEncounterId()+"),未查询到就诊信息";
		}


		Map<String, Object> cnOrdMap =  DataBaseHelper.queryForMap("select * from cn_order where PK_PV = '"+pvEncounter.getPkPv()+"' and code_ordtype = '04' and del_flag = '0' and CODE_APPLY = '"+costDetailInpat.getApplySingleNo()+"'");
		if (cnOrdMap==null) {
			return	"根据就诊ID和申请单号("+costDetailInpat.getEncounterId()+"+"+costDetailInpat.getApplySingleNo()+"),未查询到医嘱信息";
		}

		Map<String, Object> opMap =  DataBaseHelper.queryForMap("select dept.pk_dept,emp.pk_emp,emp.name_emp from bd_ou_employee emp  left join bd_ou_empjob empjob on empjob.pk_emp = emp.pk_emp  left join bd_ou_dept dept  on dept.pk_dept = empjob.pk_dept where empjob.is_main = '1' and emp.code_emp = '"+costDetailInpat.getEnterDoctorId()+"'");
		if (opMap==null) {
			return	"根据开立医生ID("+costDetailInpat.getEnterDoctorId()+"),未查询到开立医生主建信息";
		}
		Map<String, Object> CgMap =  DataBaseHelper.queryForMap("select emp.pk_emp,emp.name_emp,dept.pk_dept from bd_ou_employee emp  left join bd_ou_empjob empjob on empjob.pk_emp = emp.pk_emp  left join bd_ou_dept dept  on dept.pk_dept = empjob.pk_dept where empjob.is_main = '1' and emp.code_emp = '"+costDetailInpat.getChargeOperaId()+"'");
		if (CgMap==null) {
			return	"根据记账人ID("+costDetailInpat.getChargeOperaId()+"),未查询到计费科室主建信息";
		}
		String flagPd = "";
		if("05".equals(costDetailInpat.getChargeItemClassCode())||"04".equals(costDetailInpat.getChargeItemClassCode())||"03".equals(costDetailInpat.getChargeItemClassCode())) {
			flagPd= "1";
		} else {
			flagPd= "0";
		}
		List<Map<String, Object>> bdItem = new ArrayList<Map<String, Object>>();

//		String s = costDetailInpat.getChargeItemCode();
//		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
//		String start = "";
//		if (s != null && s.length() > 0) {
//			start = s.trim().substring(0, 1);
//		}
//		if(pattern.matcher(start).matches()){
//			flagPd = "0";
//		}else {
//			flagPd = "1";
//		}
		if(flagPd.equals("1")){
			//批次还存在问题   batch_no  待处理
			bdItem = DataBaseHelper.queryForList("select bd.* from bd_pd bd where bd.code = ? and bd.del_flag = '0'",costDetailInpat.getChargeItemCode());
		}else{
			bdItem = DataBaseHelper.queryForList("select pk_item from bd_item where  code = ? and del_flag = '0' ",costDetailInpat.getChargeItemCode());
		}
		if (bdItem.size()==0) {
			return	"根据收费项目代码("+costDetailInpat.getChargeItemCode()+"),未查询到收费项目信息";
		}

		//根据执行单状态判断检验是否计费
		BlPubParamVo vo = new BlPubParamVo();
		for (int j = 0; j < bdItem.size(); j++) {
			// blIpDt.setQuan(charge);//数量
			vo = new BlPubParamVo();
			vo.setPkOrg(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
			vo.setPkOrgApp(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
			vo.setPkOrgEx(SDMsgUtils.getPropValueStr(cnOrdMap,"pkOrg"));
			vo.setPkPi(SDMsgUtils.getPropValueStr(cnOrdMap,"pkPi"));
			vo.setPkPv(SDMsgUtils.getPropValueStr(cnOrdMap,"pkPv"));
			vo.setEuPvType(SDMsgUtils.getPropValueStr(cnOrdMap,"euPvtype"));
			vo.setPkCnord(SDMsgUtils.getPropValueStr(cnOrdMap,"pkCnord"));
			vo.setPkDeptApp(SDMsgUtils.getPropValueStr(opMap,"pkDept"));//开立科室
			vo.setPkDeptNsApp(SDMsgUtils.getPropValueStr(opMap,"pkDept"));//开立病区
			vo.setPkEmpApp(SDMsgUtils.getPropValueStr(opMap,"pkEmp"));//申请主建
			vo.setNameEmpApp(SDMsgUtils.getPropValueStr(opMap,"nameEmp"));//申请人姓名
			vo.setPkDeptEx(SDMsgUtils.getPropValueStr(deptExecMap,"pkDept"));//执行科室
			if(StringUtils.isNotEmpty(costDetailInpat.getExecDateTime())){
				vo.setDateHap(DateUtils.strToDate(costDetailInpat.getExecDateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			vo.setPkDeptCg(SDMsgUtils.getPropValueStr(CgMap,"pkDept"));//计费科室
			vo.setPkEmpCg(SDMsgUtils.getPropValueStr(CgMap,"pkEmp"));
			vo.setNameEmpCg(SDMsgUtils.getPropValueStr(CgMap,"nameEmp"));
			//vo.setNameEmpCg("平台接口计费");
			vo.setPkOrdexdt(costDetailInpat.getApplySingleNo());
			vo.setEuBltype("3");//手麻

			if("1".equals(flagPd)){
				vo.setFlagPd("1");//1为药品
				vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkPd"));
				vo.setPkOrd(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkPd"));
				vo.setSpec(SDMsgUtils.getPropValueStr(bdItem.get(j),"spec"));
				vo.setBatchNo(SDMsgUtils.getPropValueStr(bdItem.get(j),"batchNo"));
				//vo.setDateExpire(DateUtils.strToDate(SDMsgUtils.getPropValueStr(bdItem.get(j),"spec"), "yyyyMMddHHmmss"));
				vo.setPkUnitPd(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkUnitMin"));
				vo.setPackSize(1);//SDMsgUtils.getPropValueStr(bdItem.get(j),"packSize")
				BigDecimal price=new BigDecimal(SDMsgUtils.getPropValueStr(bdItem.get(j),"price")).divide(new BigDecimal(SDMsgUtils.getPropValueStr(bdItem.get(j),"packSize"))).setScale(4,   BigDecimal.ROUND_HALF_UP);
				vo.setPrice(price.doubleValue());
				vo.setPriceCost(price.doubleValue());
			}else {
				vo.setFlagPd("0");//0为非药品
				vo.setPkItem(SDMsgUtils.getPropValueStr(bdItem.get(j),"pkItem"));
			}
			vo.setQuanCg(Double.valueOf(costDetailInpat.getChargeQuantity()));
			blCgVos.add(vo);
		}



		//组装用户信息
		User user = new User();
		user.setPkOrg(blCgVos.get(0).getPkOrg());
		user.setPkEmp(blCgVos.get(0).getPkEmpEx());
		user.setNameEmp(blCgVos.get(0).getNameEmpEx());
		//user.setNameEmp(Constant.NOTE);
		user.setPkDept(blCgVos.get(0).getPkDeptEx());
		UserContext.setUser(user);
		//组装记费参数
		BlIpCgVo cgVo = new BlIpCgVo();
		cgVo.setAllowQF(true);
		cgVo.setBlCgPubParamVos(blCgVos);
		ApplicationUtils apputil = new ApplicationUtils();

		ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,user);
		if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
			return rs.getErrorMessage();

		return "succ";
	}



	/**
	 * 【住院】划价【退费】处理逻辑
	 * @param costList
	 * @return
	 */
	public String disposeIpReBlMessage(List<CostDetailInpat> costList){

		List<Map<String, Object>> pkMap = new ArrayList<Map<String, Object>>();
		Set<String> cnOrdSet = new HashSet<>();
		Set<String> costDetOrdSet = new HashSet<>();

		for (int i = 0; i < costList.size(); i++) {
			costDetOrdSet.add(costList.get(i).getCostDetailId());
		}
		String addCost="";
		for (int i = 0; i < costDetOrdSet.size(); i++) {
			if(i==costDetOrdSet.size()-1){
				addCost+="'"+costDetOrdSet.iterator().next()+"')";
			}else{
				addCost+="'"+costDetOrdSet.iterator().next()+"',";
			}
		}

		pkMap =DataBaseHelper.queryForList("select pk_cgip,pk_cnord from  BL_IP_DT where flag_settle='0' and  pk_ordexdt in ("+addCost+" and del_flag = '0' ");
		if(pkMap.size()<1){
			return "未查询该费用明细ID("+addCost;
		}
		for (int j = 0; j < pkMap.size(); j++) {
			List<Map<String, Object>> pkCgIpBackMap =DataBaseHelper.queryForList("select pk_cgip from  BL_IP_DT where flag_settle='0' and  pk_cgip_back = '"+SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCgip")+"' and del_flag = '0' ");
			if(pkCgIpBackMap.size()>0){
				return "未查询该费用明细ID("+addCost+"已退费,请勿重复退费";
			}
			cnOrdSet.add(SDMsgUtils.getPropValueStr(pkMap.get(j),"pkCnord"));
		}



		if(cnOrdSet.size()==0)
			return "未查询到所有的医嘱ID信息";

		String sql = "SELECT emp.pk_org,emp.PK_EMP,emp.name_emp,job.pk_dept FROM bd_ou_employee emp left join bd_ou_empjob job on emp.pk_emp = job.pk_emp where emp.code_EMP =?";
		Map<String, Object> exDoc = DataBaseHelper.queryForMap(sql,costList.get(0).getChargeOperaId());
		if(exDoc==null){
			throw new BusException("根据记账人ID("+costList.get(0).getChargeOperaId()+"),未查询到人员信息");
		}

		List<RefundVo> reFund = new ArrayList<RefundVo>();//退费集合

		//判断是已经记费并且未退费
		//查询收费主键
		String addPkCnords="";
		for (int i = 0; i < cnOrdSet.size(); i++) {
			if(i==cnOrdSet.size()-1){
				addPkCnords+="'"+cnOrdSet.iterator().next()+"')";
			}else{
				addPkCnords+="'"+cnOrdSet.iterator().next()+"',";
			}
		}
		String sqlBl = "select * from (select blip.pk_org,blip.pk_cgip,blip.quan + (CASE WHEN back.quan is null THEN 0 ELSE back.quan END) as quan  from BL_IP_DT blip  left outer join  (select sum(quan) quan,sum(amount) amt,pk_cgip_back from bl_ip_dt  where flag_settle = 0 and flag_pd = 0 and  quan < 0 and pk_ordexdt in ("+addCost+"  and PK_CNORD in ( "+addPkCnords+" group by pk_cgip_back ) back on blip.pk_cgip=back.pk_cgip_back where blip.del_flag = '0' and blip.pk_cgip_back is null and blip.flag_settle='0' and blip.quan >0 and blip.pk_ordexdt in ("+addCost+" and blip.pk_cnord in ( "+addPkCnords+" ) dt where dt.quan>0 ";
		List<Map<String, Object>> BlList = DataBaseHelper.queryForList(sqlBl);
		Double num = 0.0;
		for (int i = 0; i < BlList.size(); i++) {
			Map<String, Object> blMap = BlList.get(i);
			if(!"".equals(SDMsgUtils.getPropValueStr(blMap,"quan"))){
				num = Double.valueOf(SDMsgUtils.getPropValueStr(blMap,"quan"));
			}
			RefundVo refundVo = new RefundVo();
			refundVo.setPkOrg(SDMsgUtils.getPropValueStr(blMap,"pkOrg"));
			refundVo.setPkCgip(SDMsgUtils.getPropValueStr(blMap,"pkCgip"));//计费主建
			refundVo.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
			//refundVo.setNameEmp(Constant.NOTE);
			refundVo.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
			refundVo.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
			refundVo.setQuanRe(num);
			reFund.add(refundVo);
		}
		User user = new User();
		user.setPkOrg(SDMsgUtils.getPropValueStr(BlList.get(0),"pkOrg"));
		user.setPkEmp(SDMsgUtils.getPropValueStr(exDoc,"pkEmp"));
		user.setNameEmp(SDMsgUtils.getPropValueStr(exDoc,"nameEmp"));
		//user.setNameEmp(Constant.NOTE);
		user.setPkDept(SDMsgUtils.getPropValueStr(exDoc,"pkDept"));
		UserContext.setUser(user);

		//退费
		if(reFund!=null && reFund.size()>0){
			ApplicationUtils apputil = new ApplicationUtils();
			ResponseJson prePayInfo = apputil.execService("PV", "ipCgPubService", "savePatiRefundInfo",reFund,user);
			if(prePayInfo.getStatus()<0)
				return "退费失败:"+prePayInfo.getDesc();
			return "succ";
		}else
			return "退费失败:没有可退费用";

	}


	/**
	 * 根据患者查询有效就诊记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getEffectPvInfo(Map<String,Object> paramMap){
		paramMap.put("curDate", DateUtils.getDateStr(new Date())+"000000");//当天23点
		return blPubForWsMapper.queryEffectPvList(paramMap);
	}
	/**
     * 检验采集
     * @param
     */
    public void lisCollection(CnOrder cnOrder,PvEncounter pvEncounter,LabApply labApply,CnLabApply cnLabApply,User user){
    	int update = DataBaseHelper.execute("update cn_lab_apply set eu_status ='2',pk_emp_col=?,name_emp_col = ?,modifier=?,date_col=to_date(?,'yyyyMMddHH24miss') ,modity_time=sysdate where pk_cnord=?",user.getPkEmp(),user.getNameEmp(),user.getPkEmp(),DateUtils.getDateTimeStr(new Date()),cnOrder.getPkCnord());
		int execute = DataBaseHelper.execute("update CN_ORDER set DATE_PLAN_EX=to_date(?,'yyyyMMddHH24miss'),PK_EMP_EX=?,NAME_EMP_EX=?,ts=sysdate where pk_cnord=?",DateUtils.getDateTimeStr(new Date()),user.getPkEmp(),user.getNameEmp(),cnOrder.getPkCnord());
		int executeOcc = DataBaseHelper.execute("update ex_assist_occ set flag_occ='1',eu_status='1',date_occ = to_date(?,'yyyyMMddHH24miss'),pk_emp_occ=?,name_emp_occ=?,ts=sysdate where pk_cnord=?",DateUtils.getDateTimeStr(new Date()),user.getPkEmp(),user.getNameEmp(),cnOrder.getPkCnord());
		if(update<1||execute<1 || executeOcc<1){
			throw new BusException("更改申请单状态失败");
		}
		//saveLisItem(cnOrder, pvEncounter, labApply, cnLabApply, user);
    }
    /**
     * 检验接收
     * @param
     */
    public void lisReceive(CnOrder cnOrder,LabApply labApply,CnLabApply cnLabApply,User user){
    	int update =DataBaseHelper.execute("update cn_lab_apply set eu_status ='3',modifier=?,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=?",user.getPkEmp(),cnOrder.getPkCnord());
    	String pkDeptCg="";
		if(CommonUtils.isNotNull(labApply.getCollectDeptId())){
			Map<String,Object> deptMap=DataBaseHelper.queryForMap("select pk_dept,name_dept from bd_ou_dept where code_dept=?", new Object[]{labApply.getCollectDeptId()});
			pkDeptCg=CommonUtils.getString(deptMap.get("pkDept"),"");
		}else{
			 throw new BusException("采样科室不能为空");
		}
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE ex_assist_occ SET TS= sysdate,");
		sql.append("FLAG_OCC = '1', DATE_OCC = to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'), PK_EMP_OCC = ?, NAME_EMP_OCC = ?,");
		sql.append(" EU_STATUS = '1', PK_ORG_OCC = ?, PK_DEPT_OCC = ?");
		sql.append(" WHERE flag_occ = '0' AND flag_canc = '0' AND flag_refund = '0' AND PK_CNORD = ? ");
		int execute=DataBaseHelper.execute(sql.toString(), new Object[]{user.getPkEmp(),user.getNameEmp(),cnLabApply.getPkOrg(),pkDeptCg,cnOrder.getPkCnord()});
		if(update<1||execute<1){
			throw new BusException("更改申请单状态失败");
		}
    }
    /**
     * 样本退回
     * @param
     */
    public void lisReturn(CnOrder cnOrder,LabApply labApply,CnLabApply cnLabApply,User user){
    	int execute =DataBaseHelper.execute("update ex_assist_occ set eu_status ='0',DATE_CANC=to_date(?,'yyyyMMddHH24miss'),PK_EMP_CANC=?,NAME_EMP_CANC=? where pk_cnord=?",DateUtils.getDateTimeStr(DateUtils.strToDate(labApply.getCancelDateTime(), "yyyyMMddHHmmss")),user.getPkEmp(),user.getNameEmp(),cnOrder.getPkCnord());
        int update =DataBaseHelper.execute("update cn_lab_apply set eu_status ='1',modifier=?,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss') where pk_cnord=?",user.getPkEmp(),cnOrder.getPkCnord());
        if(update<1||execute<1){
			throw new BusException("更改申请单状态失败");
		}	
    }
    /**
     * 检验报告
     * @param
     */
    public void lisReport(CnOrder cnOrder,LabApply labApply,CnLabApply cnLabApply,User user){
    	int update =DataBaseHelper.execute("update cn_lab_apply set eu_status ='4',modifier=?,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'),DATE_RPT=to_date(?,'yyyyMMddHH24miss') where pk_cnord=?",user.getPkEmp(),DateUtils.getDateTimeStr(DateUtils.strToDate(labApply.getExecDateTime(), "yyyyMMddHHmmss")),cnOrder.getPkCnord());
        if(update<1){
        	 throw new BusException("更改申请单状态失败");
		}
    }
    /**
     * 检查状态
     * @param
     */
    public void risStatus(CnOrder cnOrder,ExamApply examApply,CnRisApply cnRisApply){
    	int update =DataBaseHelper.execute("update cn_ris_apply set eu_status ='3' where pk_cnord=?",cnOrder.getPkCnord());
        if(update<1){
        	 throw new BusException("更改申请单状态失败");
		}
    }
    /**
     * 检查报告
     * @param
     */
    public void risReport(CnOrder cnOrder,ExamApply examApply,CnRisApply cnRisApply,User user){
    	int update =DataBaseHelper.execute("update cn_ris_apply set eu_status ='4',modifier=?,modity_time=to_date(to_char(sysdate,'yyyyMMddHH24miss'),'yyyy/mm/dd HH24:mi:ss'),DATE_RPT=to_date(?,'yyyyMMddHH24miss') where pk_cnord=?",user.getPkEmp(),DateUtils.getDateTimeStr(DateUtils.strToDate(examApply.getExecDateTime(), "yyyyMMddHHmmss")),cnOrder.getPkCnord());
        if(update<1){
        	 throw new BusException("更改申请单状态失败");
		}
    }
	/**
     * 医辅执行表
     * @param
     */
    private void saveLisItem(CnOrder cnOrder,PvEncounter pvEncounter,LabApply labApply,CnLabApply cnLabApply,User user){
    	//写医疗执行表EX_ASSIST_OCC   EX_ASSIST_OCC_DT    退费时要记得删掉这两个表
		ExAssistOcc exAssistOcc = new ExAssistOcc();
		String pkAssocc =UUID.randomUUID().toString().replace("-", "");
		exAssistOcc.setPkAssocc(pkAssocc);
		exAssistOcc.setPkOrg(cnOrder.getPkOrg());
		exAssistOcc.setPkCnord(cnOrder.getPkCnord());
		exAssistOcc.setPkPv(cnOrder.getPkPv());
		exAssistOcc.setPkPi(cnOrder.getPkPi());
		exAssistOcc.setEuPvtype(pvEncounter.getEuPvtype());
		exAssistOcc.setCodeOcc(ApplicationUtils.getCode("0503"));
		exAssistOcc.setPkDept(cnOrder.getPkDept());
		exAssistOcc.setPkEmpOrd(cnOrder.getPkEmpOrd());
		exAssistOcc.setNameEmpOrd(cnOrder.getNameEmpOrd());
		exAssistOcc.setDateOrd(cnOrder.getCreateTime());//开单日期
		exAssistOcc.setQuanOcc(Double.valueOf(labApply.getChargeQuantity()));
		exAssistOcc.setTimesOcc(1);//当前执行次数
		//exAssistOcc.setTimesTotal(timesTotal);//总执行次数
		exAssistOcc.setPkOrgOcc(cnOrder.getPkOrg());
		exAssistOcc.setPkDeptOcc(cnLabApply.getPkDeptCol());
		
		exAssistOcc.setPkEmpOcc(user.getPkEmp());
		exAssistOcc.setNameEmpOcc(user.getNameEmp());
		exAssistOcc.setFlagCanc("0");
		exAssistOcc.setPkExocc(user.getPkEmp());
		exAssistOcc.setEuStatus("1");
		exAssistOcc.setFlagPrt("0");
		exAssistOcc.setCreator(user.getPkEmp());
		exAssistOcc.setCreateTime(new Date());
		exAssistOcc.setTs(new Date());
		exAssistOcc.setDelFlag("0");
		exAssistOcc.setFlagRefund("0");
		exAssistOcc.setFlagOcc("0");
		exAssistOcc.setPkEmpOcc(user.getPkEmp());
		exAssistOcc.setNameEmpOcc(user.getNameEmp());
		int insertBean = DataBaseHelper.insertBean(exAssistOcc);
		ExAssistOccDt exAssistOccDt = new ExAssistOccDt();
		exAssistOccDt.setPkAssocc(pkAssocc);
		exAssistOccDt.setPkAssoccdt(SDMsgUtils.getPk());
		exAssistOccDt.setPkOrg(cnOrder.getPkOrg());
		exAssistOccDt.setFlagMaj("1");
		exAssistOccDt.setPkCnord(cnOrder.getPkCnord());
		exAssistOccDt.setPkExocc(user.getPkEmp());
		exAssistOccDt.setDelFlag("0");
		exAssistOccDt.setCreateTime(new Date());
		exAssistOccDt.setTs(new Date());
		exAssistOccDt.setPkOrd(cnOrder.getPkOrd());
		int insertBeanDt = DataBaseHelper.insertBean(exAssistOccDt);
		if(insertBeanDt<1||insertBean<1){
			throw new BusException("写入医辅执行表失败");
		}
    }
    
    /**
	 * 门诊挂号开立票据对外接口（第三方调用，不走纸质票据）
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @param
	 * @param
	 * @return
	 */
	public Map<String, Object> eBillRegistration(String pkPv,IUser iuser,String pkSettle){
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", "0");//不打印纸质票据
		//调用开立票据接口生成票据信息
		Map<String, Object> retInv = new HashMap<>(16);
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillRegistration", new Object[]{paramMap,iuser});	
		return retInv;
	}

	/**
	 * 根据电脑名称获取参数BL0031值
	 * 是否开启电子票据接口(计算机级别参数)
	 * @param nameMachine
	 * @return
	 */
	public String getBL0031ByNameMachine(String nameMachine){
		String sql = "select argu.arguval from bd_res_pc pc "
				+" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
				+" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0031' and pc.addr = ?";
	
		Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, nameMachine);
		
		String rtnName = null;
		
		if(qryMap!=null && qryMap.size()>0){
			rtnName = CommonUtils.getString(qryMap.get("arguval"));
		}
			
		if(CommonUtils.isEmptyString(rtnName)){
			rtnName = "0";
		}
		
		return rtnName;
	}

	
	/**
	 * 根据票据id查询电子票据信息
	 * @param paramMap
	 * @return
	 */
	public List<EnoteInvInfo> qryEnoteInvInfo(Map<String,Object> paramMap){
		return blPubForWsMapper.qryEnoteInvInfo(paramMap);
	}

	/**
	 * 获取医院微信公众号每日账单
	 * @param paramMap
	 * @return
	 */
	public List<SumItemsVO> getHisMerchantSum(Map<String, Object> paramMap) {
		return blPubForWsMapper.getHisMerchantSum(paramMap);
	}

	/**
	 * 获取医院微信公众号每日账单明细
	 * @param paramMap
	 * @return
	 */
	public List<ItemsVO> getHisMerchantDetail(Map<String, Object> paramMap) {
		return blPubForWsMapper.getHisMerchantDetail(paramMap);
	}
	

	/**
	 * 诊间支付方法-自费
	 * @param dft
	 * @return
     * @throws ParseException 
	 */
	public Map<String, Object> clinicPaymentZF(String param, IUser user)  {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Map<String,Object> result = new HashMap<String,Object>();
		//HIS未结算总金额 (需要结算部分)
		BigDecimal amountSt = BigDecimal.ZERO;
		//自费总金额
		BigDecimal amountPi = BigDecimal.ZERO.add(new BigDecimal(MapUtils.getString(map,"payAmt")));
		//医保总金额
		BigDecimal amountInsu = BigDecimal.ZERO;	
		String codePv=CommonUtils.getPropValueStr(map, "codePv");//就诊号
		if(CommonUtils.isNull(codePv)){
			result.put("result", "false");
			result.put("message", "未传入就诊号");
			return result;
		}
		//Transaction Amount – Extended
		String amountMsgStr = CommonUtils.getPropValueStr(map, "amountMsgStr");
		if(CommonUtils.isNull(amountMsgStr)){
			result.put("result", "false");
			result.put("message", "未传入总金额");
			return result;
		}

		String sqlPv = "select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		if(pvencounter==null){
			result.put("result", "false");
			result.put("message", "【"+codePv+"】根据就诊主键未查询到有效就诊数据");
			return result;
		}
		// 构建session中User信息
        UserContext.setUser(user);		
		
		//修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
	
		//获取第三方支付数据(自费) !!!!!
		BlExtPay saveBlExtPay = saveBlExtPay(map);	
		
		//医保患者自费结算，先转为自费结算后，结算完成后转为医保
		String pkInsu = new String(pvencounter.getPkInsu());//原来的类型
		updatePkInsu(PKINSUSELF,pvencounter);
		pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		Map<String,Object> resMap=new HashMap<>();
        String curtime = DateUtils.getDateTimeStr(new Date());
		resMap.put("curDate", curtime.substring(0, curtime.length()-4)+"0000"); //有效时间匹配到小时
		resMap.put("notDisplayFlagPv", "0");
		resMap.put("isNotShowPv", "0");
		resMap.put("pkPv", pvencounter.getPkPv());
		resMap.put("pkPi", pvencounter.getPkPi());
		//未计算金额
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
		if(mapResult==null || mapResult.size()==0){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			// 未查询到未结算的数据信息-返回异常信息
			result.put("result", "false");
			result.put("message", "未查询到未结算的数据信息");
			return result;
		}
		//处方类型
		String dtHpprop = CommonUtils.getPropValueStr(map, "dtHpprop");
		Iterator<BlPatiCgInfoNotSettleVO> iterator = mapResult.iterator();
		while(iterator.hasNext()){
			BlPatiCgInfoNotSettleVO settleVO = iterator.next();
			//计算处方总金额（如果处方号为空默认本次就诊所有处方都结算）
			if(dtHpprop==null || "".equals(dtHpprop)||settleVO.getDtHpprop().equals(dtHpprop)){
				amountSt = amountSt.add(settleVO.getAmount());
			}else{
				//不是该处方的，移除这个vo
				iterator.remove();
			}
		}
		BigDecimal amountMsg = new BigDecimal(amountMsgStr);
		if(amountSt.compareTo(amountMsg)!=0){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			//传入总金额与HIS计算结果不符
			result.put("result", "false");
			result.put("message", "传入总金额与HIS计算结果不符,传入金额【"+amountMsgStr+"】,HIS计算金额【"+amountSt+"】");
			return result;
		}
		
		//组装预结算 数据
		String mapResultJson = JsonUtil.writeValueAsString(mapResult);
		List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
		ApplicationUtils apputil = new ApplicationUtils();
		OpCgTransforVo opCgforVo = new OpCgTransforVo();
		opCgforVo.setPkPv(CommonUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(CommonUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		opCgforVo.setAggregateAmount(amountSt); //需支付总金额
		opCgforVo.setMedicarePayments(amountInsu);//医保支付
		opCgforVo.setPatientsPay(amountPi);//现金支付
		//调用预结算接口 countOpcgAccountingSettlement
		ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
		if(response.getStatus()!=0){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			// 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
			result.put("result", "false");
			result.put("message", "HIS预结算接口【YjSettle】调用失败:"+response.getDesc()+"");
			return result;
		}
		opCgforVo=(OpCgTransforVo) response.getData();
		opCgforVo.setPkPv(CommonUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(CommonUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		//TODO 构建结算接口vo数据：特别构建发票数据
		opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
		List<InvoiceInfo> invoiceInfo = new ArrayList<InvoiceInfo>();
		opCgforVo.setInvoiceInfo(invoiceInfo);

		//付款数据构建
		List<BlDeposit> depositList=new ArrayList<BlDeposit>();
		BlDeposit deposit=new BlDeposit();
		deposit.setDtPaymode(MapUtils.getString(map,"payType"));
		deposit.setAmount(amountPi);
		deposit.setFlagAcc("0");
		deposit.setDelFlag("0");
		deposit.setPayInfo(CommonUtils.getPropValueStr(map, "orderno"));//第三方订单号
		depositList.add(deposit);
		opCgforVo.setBlDeposits(depositList);
		opCgforVo.setPatientsPay(amountMsg);//现金支付
		ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
		if(respSettle.getStatus()!=0){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "HIS结算接口【Settle】调用失败:"+respSettle.getDesc()+respSettle.getErrorMessage());
			return result;
		}

		//更新pkSettle
		opCgforVo = (OpCgTransforVo) respSettle.getData();
		
		//是否启用电子票据服务(第三方调用,不走纸质票据)
		if(!"-2".equals(CommonUtils.getPropValueStr(map, "invStatus"))) {
			//收费结算,校验BL0008参数是否是1
			if("1".equals(ApplicationUtils.getSysparam("BL0008", false))){
				String machineName = CommonUtils.getPropValueStr(map, "deviceid").toUpperCase();
				String eBillFlag = getBL0031ByNameMachine(machineName);
				if ("1".equals(eBillFlag)) {
					try{
						Map<String, Object> inv = eBillMzOutpatient(opCgforVo.getPkPv(),UserContext.getUser(),opCgforVo.getPkSettle());
						//组装电子票据返回信息
						if(inv != null) {
							List<BlInvoice> invoList = (List<BlInvoice>)inv.get("inv");
							if(invoList != null && invoList.size() > 0) {
								result.put("ebillno",invoList.get(0).getEbillno());
								result.put("ebillbatchcode",invoList.get(0).getEbillbatchcode());
								result.put("checkcode",invoList.get(0).getCheckcode());
								result.put("dateEbill",invoList.get(0).getDateEbill());
								result.put("qrcodeEbill",invoList.get(0).getQrcodeEbill());
								result.put("urlEbill",invoList.get(0).getUrlEbill());
								result.put("urlNetebill",invoList.get(0).getUrlNetebill());
							}
						}		
					}catch(Exception e){
						platformTransactionManager.rollback(status); // 添加失败 回滚事务；
						result.put("result", "false");
						result.put("message", "HIS结算电子票据接口【inv】调用失败 : "+ e.getMessage());
						return result;
					}
				}
			}
		}		
		
		//医保患者自费结算，先转为自费结算后，转为医保
		updatePkInsu(pkInsu,pvencounter);

		//更新第三方支付表
		updateBlExtPay(opCgforVo,map);
				
		//提交事务
		platformTransactionManager.commit(status);
		result.put("pkPv", opCgforVo.getPkPv());
		result.put("orderno", CommonUtils.getPropValueStr(map, "orderno"));
		
		Map<String, Object> mapData=new HashMap<String, Object>();
		mapData.put("data", result);
		mapData.put("status", Constant.RESSUC);
		return mapData;
	}
	
	/**
	 * 诊间支付方法-医保
	 * @param dft
	 * @return
     * @throws ParseException 
	 */
	public Map<String, Object> clinicPaymentYB(String param, IUser user)  {
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		Map<String,Object> result = new HashMap<String,Object>();

		//总金额
		BigDecimal amountSt = BigDecimal.ZERO;
		//自费金额
		BigDecimal amountPi = BigDecimal.ZERO.add(new BigDecimal(MapUtils.getString(map,"payAmt")));
		
		BigDecimal amountInsu = BigDecimal.ZERO;	
		amountInsu=new BigDecimal(CommonUtils.getPropValueStr(map,"amtInsuThird")==""?0:Double.valueOf(CommonUtils.getPropValueStr(map,"amtInsuThird")));
		String codePv=CommonUtils.getPropValueStr(map, "codePv");//就诊流水号
		if(CommonUtils.isNull(codePv)){
			result.put("result", "false");
			result.put("message", "未传入就诊号");
			return result;
		}
		
		String amountMsgStr = CommonUtils.getPropValueStr(map, "amountMsgStr");
		if(CommonUtils.isNull(amountMsgStr)){
			result.put("result", "false");
			result.put("message", "未传入总金额");
			return result;
		}
		
		String sqlPv = "select * from pv_encounter where code_pv=?";
		PvEncounter pvencounter = DataBaseHelper.queryForBean(sqlPv, PvEncounter.class,new Object[]{codePv});
		if(pvencounter==null){
			result.put("result", "false");
			result.put("message", "【"+codePv+"】根据就诊唯一主键未查询到有效就诊数据");
			return result;
		}
		
        UserContext.setUser(user);
        
        //修改为手动事物 , 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager.getTransaction(def);
        
		//先获取第三方支付数据
		BlExtPay saveBlExtPay = saveBlExtPay(map);
		
		//TODO 构建结算接口vo数据：医保结算vo；保存医保数据
		Map<String,Object> doYbRetData = saveYbSettleData(map,pvencounter);
		if(!"1".equals(doYbRetData.get("status"))){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "HIS方法【saveYbSettleData】保存医保数据失败"+doYbRetData.get("data"));
			return result;
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
		List<BlPatiCgInfoNotSettleVO> mapResult = opcgPubHelperService.queryPatiCgInfoNotSettle(resMap);
		if(mapResult==null || mapResult.size()==0){
			// 未查询到未结算的数据信息-返回异常信息
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "未查询到未结算的数据信息");
			return result;
		}
		//处方类型
		String dtHpprop = CommonUtils.getPropValueStr(map, "dtHpprop");
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
			//传入总金额与HIS计算结果不符
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "传入总金额与HIS计算结果不符,传入金额【"+amountMsg+"】,HIS计算金额【"+amountSt+"】");
			return result;
		}
		
		//结算参数
		String mapResultJson = JsonUtil.writeValueAsString(mapResult);
		List<BlOpDt> opDts = JsonUtil.readValue(mapResultJson, new TypeReference<List<BlOpDt>>() {});
		ApplicationUtils apputil = new ApplicationUtils();
		OpCgTransforVo opCgforVo = new OpCgTransforVo();
		opCgforVo.setPkPv(CommonUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(CommonUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		opCgforVo.setAggregateAmount(amountSt); //需支付总金额
		opCgforVo.setMedicarePayments(amountInsu);//医保支付
		opCgforVo.setPatientsPay(saveBlExtPay.getAmount());//现金支付
		//调用预结算接口 countOpcgAccountingSettlement
		ResponseJson  response = apputil.execService("BL", "OpCgSettlementService", "countOpcgAccountingSettlement", opCgforVo,user);
		if(response.getStatus()!=0){
			// 预结算接口调用失败 -处理异常消息：注意此接口调用要避免异常
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "HIS接口【YjSettle】调用失败:"+response.getDesc()+response.getErrorMessage());
			return result;
		}
		opCgforVo = (OpCgTransforVo) response.getData();
		opCgforVo.setPkPv(CommonUtils.getPropValueStr(resMap, "pkPv"));
		opCgforVo.setPkPi(CommonUtils.getPropValueStr(resMap, "pkPi"));
		opCgforVo.setBlOpDts(opDts);
		opCgforVo.setInvStatus("-2");//自费-不限制发票信息传入
		//opCgforVo.setInvStatus(CommonUtils.getPropValueStr(map, "invStatus"));//-2 不走票据服务
		opCgforVo.setMachineName(CommonUtils.getPropValueStr(map, "deviceid").toUpperCase());
		List<InvoiceInfo> invoiceInfos = new ArrayList<InvoiceInfo>();
		InvoiceInfo invoiceInfo = new InvoiceInfo();
		invoiceInfo.setFlagPrint("0");
		invoiceInfo.setMachineName(UserContext.getUser().getCodeEmp().toUpperCase());
        invoiceInfos.add(invoiceInfo);
		opCgforVo.setInvoiceInfo(invoiceInfos);
		
		//付款数据构建
		List<BlDeposit> depositList = new ArrayList<BlDeposit>();
		BlDeposit deposit=new BlDeposit();
		deposit.setDtPaymode(CommonUtils.getPropValueStr(map, "payType"));
		deposit.setAmount(saveBlExtPay.getAmount());
		deposit.setFlagAcc("0");
		deposit.setDelFlag("0");
		deposit.setPayInfo(CommonUtils.getPropValueStr(map, "orderno"));//第三方订单号
		depositList.add(deposit);
		opCgforVo.setBlDeposits(depositList);
		opCgforVo.setMedicarePayments(amountInsu);//医保支付
		opCgforVo.setAmtInsuThird(amountInsu);
		//结算接口
		ResponseJson  respSettle = apputil.execService("BL", "OpCgSettlementService", "mergeOpcgAccountedSettlement", opCgforVo,user);
		if(respSettle.getStatus()!=0){
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			result.put("result", "false");
			result.put("message", "HIS接口【Settle】调用失败:"+response.getDesc()+respSettle.getErrorMessage());
			return result;
		}	
		
		//更新pkSettle
		opCgforVo = (OpCgTransforVo) respSettle.getData();
		
		//是否启用电子票据服务(第三方调用,不走纸质票据)
		if(!"-2".equals(CommonUtils.getPropValueStr(map, "invStatus"))) {
			//收费结算,校验BL0008参数是否是1
			if("1".equals(ApplicationUtils.getSysparam("BL0008", false))){
				String machineName = CommonUtils.getPropValueStr(map, "deviceid").toUpperCase();
				String eBillFlag = getBL0031ByNameMachine(machineName);
				if ("1".equals(eBillFlag)) {
					try{
						Map<String, Object> inv = eBillMzOutpatient(opCgforVo.getPkPv(),UserContext.getUser(),opCgforVo.getPkSettle());
						//组装电子票据返回信息
						if(inv != null) {
							List<BlInvoice> invoList = (List<BlInvoice>)inv.get("inv");
							if(invoList != null && invoList.size() > 0) {
								result.put("ebillno",invoList.get(0).getEbillno());
								result.put("ebillbatchcode",invoList.get(0).getEbillbatchcode());
								result.put("checkcode",invoList.get(0).getCheckcode());
								result.put("dateEbill",invoList.get(0).getDateEbill());
								result.put("qrcodeEbill",invoList.get(0).getQrcodeEbill());
								result.put("urlEbill",invoList.get(0).getUrlEbill());
								result.put("urlNetebill",invoList.get(0).getUrlNetebill());
							}
						}	
					}catch(Exception e){
						platformTransactionManager.rollback(status); // 添加失败 回滚事务；
						result.put("result", "false");
						result.put("message", "HIS结算电子票据接口【inv】调用失败 : "+ e.getMessage());
						return result;
					}
				}
			}
		}
		
		//更新医保相关表
		updatePkSettle(doYbRetData,opCgforVo);
		//更新第三方支付相关表
		if(!"".equals(CommonUtils.getPropValueStr(map, "orderno"))){
			//updateExtPayData(paramMap,opCgforVo);
			updateBlExtPay(opCgforVo,map);
		}		
		//提交事务
		platformTransactionManager.commit(status);	
		/*ResponseJson json=new ResponseJson();
		json.setData(result);
		json.setStatus(Constant.SUC);*/
		Map<String, Object> mapData=new HashMap<String, Object>();
		mapData.put("data", result);
		mapData.put("status", Constant.RESSUC);
		return mapData;
	}
	
	/**
	 * 处理医保结算数据
	 * @param dft 消息实例
	 * @param user 当前用户
	 * @param pvencounter 就诊信息
	 * @return resMap:{"status":"1:成功；0:失败" ,"data":"错误返回信息/成功需要返回数据"}
	 */
    private Map<String,Object> saveYbSettleData(Map<String,Object> map,PvEncounter pvencounter){
    	User user = UserContext.getUser();
    	Map<String,Object> resMap=new HashMap<String,Object>();
    	//ZPR-1 医疗机构编码
		//String yljgbm = CommonUtils.getPropValueStr(map, "2");
		//ZPR-2 医疗机构名称
		//String yljgmc = CommonUtils.getPropValueStr(map, "3");
		//ZPR-3 门诊流水号
		//String mzlsh = CommonUtils.getPropValueStr(map, "4");
		//ZPR-4单据号
		String Djh = CommonUtils.getPropValueStr(map, "akc190");//就医登记号
		//ZPR-5医疗证号
		//String Ylzh = CommonUtils.getPropValueStr(map, "6");
		//ZPR-6电脑号
		//String Dnh =  CommonUtils.getPropValueStr(map, "7");
		//ZPR-7姓名
		//String xm = CommonUtils.getPropValueStr(map, "8");
		//ZPR-8金额
		String je = CommonUtils.getPropValueStr(map, "akc264");//结算金额
		//现金合计 9
		String Xjhj = CommonUtils.getPropValueStr(map, "akb067");//个人支付
		//记账合计 10
		String Jzhj = CommonUtils.getPropValueStr(map, "akb066");//个人账户支付
		//结算序列号 11
		String Jsywh = CommonUtils.getPropValueStr(map, "bke384");//机构结算业务号
		//统筹合计 12
		String Tchj = CommonUtils.getPropValueStr(map, "akb068");//基金支付
		//业务号 ywh 13
		String ywh = CommonUtils.getPropValueStr(map, "ckc618");//医保结算号
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
		List<InsSzybVisit> visitList = DataBaseHelper.queryForList(visitSql, InsSzybVisit.class,new Object[]{pvencounter.getPkPv(),Djh});
		if(visitList==null ||visitList.size()==0){
			resMap.put("status", "false");//1:成功；0：失败
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
		insSzybSt.setCodeOrg(ApplicationUtils.getPropertyValue("yb.codeOrg", ""));//医院编码
		insSzybSt.setNameOrg(ApplicationUtils.getPropertyValue("yb.nameOrg", ""));//医院名称
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
		
		List<Map<String, Object>> outputlist1 = (List<Map<String, Object>>)map.get("outputlist1");
		List<Map<String, Object>> outputlist2 = (List<Map<String, Object>>)map.get("outputlist2");
		List<Map<String, Object>> outputlist3 = (List<Map<String, Object>>)map.get("outputlist3");	
		List<InsSzybStCitydt> citydtList=new ArrayList<InsSzybStCitydt>();
		for(int i=0;i < outputlist1.size();i++){
			Map<String, Object> m = outputlist1.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aka111"));//大类代码
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aka111"));//大类代码
			String amt=CommonUtils.getPropValueStr(m, "bka058");//费用金额
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
		for(int i=0;i < outputlist2.size();i++){
			Map<String, Object> m = outputlist2.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aaa036"));//支付项目代码
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aaa036"));//支付项目代码
			String amt=CommonUtils.getPropValueStr(m, "aae019");//金额
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
		for(int i=0;i < outputlist3.size();i++){
			Map<String, Object> m = outputlist3.get(i);
			InsSzybStCitydt citydt = new InsSzybStCitydt();
			citydt.setTypeOutput(CommonUtils.getPropValueStr(m, "aka037"));//个人累计信息类别
			citydt.setCategory(CommonUtils.getPropValueStr(m, "aka037"));//个人累计信息类别
			String amt=CommonUtils.getPropValueStr(m, "bke264");//可用值
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
     * @param object
     * @param data
     */
	private void updatePkSettle(Map<String, Object> doYbRetData, OpCgTransforVo opCgforVo) {
		//HIS结算成功后回写医保结算记录中得pk_settle
		String upYbSql="update ins_szyb_st set pk_settle=? where pk_insst=?";
		DataBaseHelper.execute(upYbSql, new Object[]{opCgforVo.getPkSettle(),doYbRetData.get("data")});
	}
	    
	/**
     * 保存第三方支付数据
	 * @param regvo
	 * @param map
	 */
	public BlExtPay saveBlExtPay(Map<String, Object> map){
		BlExtPay extPay=new BlExtPay();
		User user = UserContext.getUser();
		extPay.setPkExtpay(NHISUUID.getKeyId());
		extPay.setPkOrg(user.getPkOrg());
		extPay.setAmount(new BigDecimal(MapUtils.getString(map,"payAmt")));
		extPay.setEuPaytype(MapUtils.getString(map,"payType"));
		extPay.setFlagPay("1");//支付标志
		extPay.setSerialNo(MapUtils.getString(map,"orderno"));//订单号
		extPay.setTradeNo(MapUtils.getString(map,"flowno"));//系统订单号
		extPay.setSysname(MapUtils.getString(map,"sysname"));//终端号
		extPay.setPkPi(MapUtils.getString(map,"codePi"));//先保存就诊编码
		extPay.setDescPay(MapUtils.getString(map,"descPay"));
		extPay.setResultPay(MapUtils.getString(map,"resultPay"));
		extPay.setCreator(user.getPkEmp());
		extPay.setCreateTime(new Date());
		extPay.setTs(new Date());
		extPay.setDelFlag("0");		
		DataBaseHelper.insertBean(extPay);
		return extPay;
	}
	
	/**
	 * 挂号收费 更新第三方收费表
	 * @param prampMap
	 * @param dft
	 * @return
	 * @throws ParseException
	 */
	private BlExtPay updateBlExtPay(OpCgTransforVo opCgforVo,Map<String,Object> map) {
		BlExtPay blExtPay = DataBaseHelper.queryForBean("select * from BL_EXT_PAY where del_flag = '0' and SERIAL_NO = ?", BlExtPay.class, CommonUtils.getPropValueStr(map,"orderno"));
		String dateTrans = CommonUtils.getPropValueStr(map,"paytime");
		Date date = "".equals(dateTrans)?new Date():DateUtils.strToDate(dateTrans,"yyyy-MM-dd HH:mm:ss");
		blExtPay.setDateAp(date);//请求时间
		blExtPay.setDatePay(date);//支付时间
		blExtPay.setPkPi(opCgforVo.getPkPi());
		blExtPay.setPkPv(opCgforVo.getPkPv());
		blExtPay.setPkSettle(opCgforVo.getPkSettle());
		String sqlDepo="select pk_depo from bl_deposit where pk_settle=?";
 		Map<String,Object> depoMap = DataBaseHelper.queryForMap(sqlDepo, new Object[]{opCgforVo.getPkSettle()});
		blExtPay.setPkDepo(CommonUtils.getPropValueStr(depoMap, "pkDepo"));
		DataBaseHelper.updateBeanByPk(blExtPay);
		return blExtPay;
	}
	
	/**
     * 修改患者类型
     * @param object
     * @param data
     */
	private void updatePkInsu(String pkInsu, PvEncounter pvencounter) {
		pvencounter.setPkInsu(pkInsu);
		DataBaseHelper.updateBeanByPk(pvencounter);
	}
	
	/**
	 * 门诊结算开立票据对外接口（第三方调用，不走纸质票据）
	 * @param pkPv
	 * @param iuser
	 * @param pkSettle
	 * @param flagPrint
	 * @param nameMachine
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> eBillMzOutpatient(String pkPv,IUser iuser,String pkSettle){
		Map<String, Object> retInv = new HashMap<>(16);
		Map<String,Object> paramMap = new HashMap<>(16);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkSettle", pkSettle);
		paramMap.put("flagPrint", "0");//不打印纸质票据
		//调用开立票据接口生成票据信息
		retInv = (Map<String, Object>) ExtSystemProcessUtils.processExtMethod("EBillInfo", "eBillMzOutpatient", new Object[]{paramMap,iuser});
		List<BlInvoice> blInvoices = (List<BlInvoice>)retInv.get("inv");
		List<BlInvoiceDt> blInvoiceDts = (List<BlInvoiceDt>)retInv.get("invDt");
		List<BlStInv> blStInvs = (List<BlStInv>)retInv.get("stInv");
		
		if(blInvoices!=null && blInvoices.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoice.class), blInvoices); // 批量插入发票
		}
		if(blInvoiceDts!=null && blInvoiceDts.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDts); // 批量插入发票明细
		}
		if(blStInvs!=null && blStInvs.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlStInv.class), blStInvs); // 批量写发票与结算的关系
		}
		return retInv;
	}
	/**
	 * 根据医生工号查询排班
	 * @param params
	 * @return
	 */
	public List<PskqSchApptVo> searchSchAppt(LbSHRequestVo requ){
		return schPubForWsMapper.searchSchAppt(requ);
	}

	
	
}
