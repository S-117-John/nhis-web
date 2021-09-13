package com.zebone.nhis.pv.reg.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.pay.service.ThirdPartyPaymentService;
import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpcgPubHelperService;
import com.zebone.nhis.bl.pub.service.PriceStrategyService;
import com.zebone.nhis.bl.pub.support.Constant;
import com.zebone.nhis.bl.pub.support.InvSettltService;
import com.zebone.nhis.bl.pub.syx.dao.CgStrategyPubMapper;
import com.zebone.nhis.bl.pub.syx.service.CgStrategyPubService;
import com.zebone.nhis.bl.pub.syx.vo.HpVo;
import com.zebone.nhis.bl.pub.util.BlcgUtil;
import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdPayer;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.BlSettleDetail;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzgyPv;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvEr;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.service.BalAccoutService;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.IDictCodeConst;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.reg.dao.RegSyxMapper;
import com.zebone.nhis.pv.reg.support.RegSyxProcessSupport;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 中山二院--门诊挂号服务
 * @author yangxue
 *
 */
@Service
public class RegSyxService {
	@Resource
    private PiPubService piPubService;
	@Autowired
	private RegPubMapper regPubMapper;
	@Resource
	private CgStrategyPubService cgStrategyPubService;
	@Resource
	private CgQryMaintainService cgQryMaintainService;
	@Resource
	private CgStrategyPubMapper cgStrategyPubMapper;
	@Resource
	private RegSyxProcessSupport regSyxProcessSupport;
	@Autowired
	private CommonService commonService;
	@Resource
	private RegSyxMapper regSyxMapper;
	@Autowired
	private ThirdPartyPaymentService thirdPartyPaymentService;
	@Autowired
	private BalAccoutService balAccoutService;
	@Autowired
	private InvSettltService invSettltService;
	@Autowired
	private OpcgPubHelperService opcgPubHelperService;
	@Resource
    private PvInfoPubService pvInfoPubService;
	@Resource
	private PriceStrategyService priceStrategyService;
	/**
	 * 保存挂号信息(有事物)
	 */
	public PiMasterRegVo savePvRegInfo(PiMasterRegVo regvo,boolean haveTickNo,User u){
		String isAdd = CommonUtils.isEmptyString(regvo.getPkPi()) ? "0": "1";
		this.savePiMasterinfo(regvo);
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(u.getPkOrg());
		regvo.setTicketNo(regvo.getTicketNo());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = regSyxMapper.querySchResInfo(paramMap);
		//2.保存就诊记录
		PvEncounter pv = this.savePvEncounter(regvo, pkPv,schres);
		regvo.setCodePv(pv.getCodePv());
		//3.生成门诊就诊记录，写表pv_op；
		if(EnumerateParameter.ONE.equals(pv.getEuPvtype()) || EnumerateParameter.FOUR.equals(pv.getEuPvtype())){
			this.savePvOp(regvo,pv,schres);//门诊就诊属性
		}else{
			//急诊就诊属性
			this.savePvEr(regvo, pv);
		}

		this.savePvInsurance(regvo, pv);
		//3.生成记费信息，写表bl_ip_dt；
		//4.生成结算信息，写表bl_settle；
		//5.生成结算明细，写表bl_settle_detail；
		//6.生成支付记录，写表bl_deposit；
		//如果挂号打印发票：
		//7.生成发票信息，写表bl_invoice；
		//8.生成发票和结算关系，写表bl_st_inv；
		//9.生成发票明细，bl_invoice_dt；
		//10.更新发票登记表，更新bl_emp_invoice。
		 regvo =  this.saveSettle(regvo, pv);

		//有号表方式，更新排班已使用号数；
		//限制了挂号时段的，也就是超过时间不能挂的，这里也不用处理,排班表上的触发点是挂号，不是按照时间实时的
	    if(haveTickNo){
		   DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
		}
		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		msgParam.put("pkPv", pv.getPkPv());
		msgParam.put("isAdd", "0");
		msgParam.put("isPiAdd", isAdd);//发送患者消息开关；0-新增 1-修改
		msgParam.put("pkPi", regvo.getPkPi());
		msgParam.put("timePosition", "1");//0：预约     1：当天
		msgParam.put("addrPosition", "0");//0：现场 1：自助机 2：电话 3：微信  4：支付宝
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		msgParam = null;
		
		return regvo;
	}

	/**
	 * 保存预约挂号信息（有事物）
	 *  1.生成预约记录，写表sch_appt；
        2.生成预约挂号记录，写表sch_appt_pv；
        3.更新排班表，更新sch_sch；
		4.生成就诊记录，写表pv_encounter；
		5.生成门诊就诊，写表pv_op；
		6.生成记费信息，写表bl_ip_dt；
		7.生成结算信息，写表bl_settle；
		8.生成结算明细，写表bl_settle_detail；
		9.生成支付记录，写表bl_deposit；
		如果挂号打印发票：
		10.生成发票信息，写表bl_invoice；
		11.生成发票和结算关系，写表bl_st_inv；
		12.生成发票明细，bl_invoice_dt；
	 */
	public  PiMasterRegVo saveApptPvRegInfo(PiMasterRegVo regvo,SchSch schSch,SchTicket ticket,boolean haveTickNo,User u){
		//1.保存预约登记信息，不含就诊记录
		String isAdd = CommonUtils.isEmptyString(regvo.getPkPi()) ? "0": "1";
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(u.getPkOrg());
		regvo.setTicketNo(ticket.getTicketno());
		regvo = this.saveApptSchRegInfo(regvo,schSch,ticket,haveTickNo,u,true);

		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = regSyxMapper.querySchResInfo(paramMap);
		//2.保存就诊记录
		PvEncounter pv = this.savePvEncounter(regvo, pkPv,schres);
		regvo.setCodePv(pv.getCodePv());
		//3.生成门诊就诊记录，写表pv_op；
		if(EnumerateParameter.ONE.equals(pv.getEuPvtype()) || EnumerateParameter.FOUR.equals(pv.getEuPvtype())) {
			this.savePvOp(regvo, pv, schres);//门诊就诊属性
		}else if(EnumerateParameter.TWO.equals(pv.getEuPvtype())){
			this.savePvEr(regvo,pv);
		}


		this.savePvInsurance(regvo, pv);
		//3.生成记费信息，写表bl_ip_dt；
		//4.生成结算信息，写表bl_settle；
		//5.生成结算明细，写表bl_settle_detail；
		//6.生成支付记录，写表bl_deposit；
		//如果挂号打印发票：
		//7.生成发票信息，写表bl_invoice；
		//8.生成发票和结算关系，写表bl_st_inv；
		//9.生成发票明细，bl_invoice_dt；
		//10.更新发票登记表，更新bl_emp_invoice。
		 regvo =  this.saveSettle(regvo, pv);

		//发送消息到平台
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		msgParam.put("pkPv", pv.getPkPv());
		msgParam.put("isAdd", "0");	
		msgParam.put("isPiAdd", isAdd);//发送患者消息开关；0-新增 1-修改
		msgParam.put("pkPi", regvo.getPkPi());
		msgParam.put("timePosition", "0");//0：预约     1：当天
		msgParam.put("addrPosition", "0");//0：现场 1：自助机 2：电话 3：微信  4：支付宝
		PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		msgParam = null;
		return regvo;
	}


	/**
	 * 保存预约登记信息(有事物部分)
	 * @param regvo
	 * @param schSch
	 * @param ticket
	 * @param haveTicketNo
	 * @param u
	 * @return
	 */
	public PiMasterRegVo saveApptSchRegInfo(PiMasterRegVo regvo,SchSch schSch,SchTicket ticket,boolean haveTicketNo,User u,boolean isGh){
		String isAdd = CommonUtils.isEmptyString(regvo.getPkPi()) ? "0": "1";
		regvo = this.savePiMasterinfo(regvo);
		boolean haveTicket = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
				Integer.class, new Object[]{schSch.getPkSch()})>0;
		SchAppt schAppt = new SchAppt();
		schAppt.setPkSchappt(NHISUUID.getKeyId());
		schAppt.setEuSchclass(regvo.getEuSchclass());
		schAppt.setPkSch(regvo.getPkSch());
		schAppt.setDateAppt(schSch.getDateWork());
		schAppt.setPkDateslot(schSch.getPkDateslot());
		schAppt.setPkSchres(schSch.getPkSchres());
		schAppt.setPkSchsrv(schSch.getPkSchsrv());
		schAppt.setOrderidExt(regvo.getOutsideOrderId());
		if(haveTicket){
		   schAppt.setTicketNo(ticket.getTicketno());
		}else{
		   schAppt.setTicketNo(String.valueOf(NumberUtils.toInt(schSch.getTicketNo(), 0) +1));
		}
		if (ticket != null) {
			schAppt.setBeginTime(ticket.getBeginTime());
			schAppt.setEndTime(ticket.getEndTime());
			regvo.setPkSchticket(ticket.getPkSchticket());
		} else {
			schAppt.setBeginTime(schSch.getDateWork());
			schAppt.setEndTime(schSch.getDateWork());
		}
		schAppt.setPkPi(regvo.getPkPi());
		if (StringUtils.isEmpty(regvo.getOrderSource())) {
			schAppt.setDtApptype(regSyxProcessSupport.getDefAppType());
		}else{
			schAppt.setDtApptype(regvo.getOrderSource());
		}
		schAppt.setPkDeptEx(schSch.getPkDept());
		schAppt.setPkOrgEx(schSch.getPkOrg());
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(u.getPkDept());
		schAppt.setPkEmpReg(u.getPkEmp());
		schAppt.setNameEmpReg(u.getNameEmp());
		schAppt.setEuStatus("0");//0:登记,1:到达
		if(isGh){
			schAppt.setFlagPay("1");
		}else{
			schAppt.setFlagPay("0");
		}
		//schAppt.setFlagPay("0");
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		schAppt.setCode(ApplicationUtils.getCode("0101"));//预约编码，不清楚,写主键//2019-07-19编码规则中预约单号为预约编码
		DataBaseHelper.insertBean(schAppt);

		//无论资源是科室还是人员都要写
		SchResource schRes = DataBaseHelper.queryForBean(
				"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ? and eu_restype=1", SchResource.class,schSch.getPkSchres());
		SchApptPv schApptPv = new SchApptPv();
		schApptPv.setPkSchappt(schAppt.getPkSchappt());
		schApptPv.setEuApptmode("0");
		if(schRes != null) {
			schApptPv.setPkEmpPhy(schRes.getPkEmp());
			Map<String,Object> nameEmp = DataBaseHelper.queryForMap("select name_emp from BD_OU_EMPLOYEE where pk_emp = ?", schRes.getPkEmp());
			schApptPv.setNameEmpPhy(nameEmp.get("nameEmp")==null?"":nameEmp.get("nameEmp").toString());
		}
		schApptPv.setFlagPv("0");
		schApptPv.setPkPv(regvo.getPkPv());
		schApptPv.setPkOrg(regvo.getPkOrg());
		DataBaseHelper.insertBean(schApptPv);

		if(haveTicket){
			DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { regvo.getPkSch() });
		}
		regvo.setPkAppt(schAppt.getPkSchappt());
		regvo.setApptCode(schAppt.getCode());
		regvo.setPkDeptunit(schSch.getPkDeptunit());
		regvo.setDateBegin(schAppt.getDateAppt());
		regvo.setDtApptype(schAppt.getDtApptype());
		//深圳口腔平台发送消息开关
		Map<String,Object> msgPiParam = new HashMap<String,Object>();
		msgPiParam.put("pkSchappt",schAppt.getPkSchappt());
		msgPiParam.put("pkPi", regvo.getPkPi());//是否发送患者信息给平台
		msgPiParam.put("isAdd", isAdd);//发送患者消息开关；0-新增 1-修改
		PlatFormSendUtils.sendSchApptReg(msgPiParam);
		return regvo;
	}

	/**
	 * 预约确认(已预约登记信息)
	 * 交易码：003002002007
	 * 1.基于预约信息生成就诊记录，写表pv_encounter；
     * 2.生成门诊就诊记录，写表pv_op；
     * 3.生成就诊保险记录，写pv_insurance;
     * 3.生成记费信息，写表bl_ip_dt；
     * 4.生成结算信息，写表bl_settle；
     * 5.生成结算明细，写表bl_settle_detail；
     * 6.生成支付记录，写表bl_deposit；
     * 如果挂号打印发票：
     * 7.生成发票信息，写表bl_invoice；
     * 8.生成发票和结算关系，写表bl_st_inv；
     * 9.生成发票明细，bl_invoice_dt；
     * 10.更新发票登记表，更新bl_emp_invoice。
	 * @param param
	 * @param user
	 */
	public PiMasterRegVo confirmApptRegInfo(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		if(regvo == null)
			throw new BusException("未获取到挂号信息！");
		if(StringUtils.isBlank(regvo.getPkAppt())){
			throw new BusException("预约确认未传入预约信息！");
		}
		if(StringUtils.isBlank(regvo.getTicketNo())){
			throw new BusException("预约确认未传入票号！");
		}
		//1.基于预约信息生成就诊记录，写表pv_encounter；
		String isAdd = CommonUtils.isEmptyString(regvo.getPkPi()) ? "0": "1";
		String pkPv = NHISUUID.getKeyId();
		regvo.setPkPv(pkPv);
		regvo.setPkOrg(((User)user).getPkOrg());
		// 挂号科室、医生从资源表里取sch_resource,当资源类型为人员时，填写，否则不填
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkSchres", regvo.getPkSchres());
		Map<String,Object> schres = regSyxMapper.querySchResInfo(paramMap);
		PvEncounter pv = this.savePvEncounter(regvo, pkPv,schres);
		//1.1 关联 预约就诊记录（预约登记时不生成pv_encounter，确认时才生成）
		DataBaseHelper.update("update SCH_APPT_PV set pk_pv=? where pk_schappt=?",new Object[]{pkPv,regvo.getPkAppt()});
		regvo.setCodePv(pv.getCodePv());
		//2.生成门诊就诊记录，写表pv_op；
		if(EnumerateParameter.TWO.equals(pv.getEuPvtype())){
			this.savePvEr(regvo, pv);//急诊就诊属性
		}else{
			this.savePvOp(regvo,pv,schres);//门诊就诊属性
		}
		this.savePvInsurance(regvo, pv);
		//3.生成记费信息，写表bl_ip_dt；
		//4.生成结算信息，写表bl_settle；
		//5.生成结算明细，写表bl_settle_detail；
		//6.生成支付记录，写表bl_deposit；
		//如果挂号打印发票：
		//7.生成发票信息，写表bl_invoice；
		//8.生成发票和结算关系，写表bl_st_inv；
		//9.生成发票明细，bl_invoice_dt；
		//10.更新发票登记表，更新bl_emp_invoice。
		 regvo =  this.saveSettle(regvo, pv);
		 //11.更新预约登记信息
		 DataBaseHelper.update("update sch_appt set  eu_status='1',flag_pay='1' where pk_schappt = ?  ", new Object[]{regvo.getPkAppt()});

		  //发送消息到平台
		  Map<String,Object> msgParam =  new HashMap<String,Object>();
		  msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
		  msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
		  msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
		  msgParam.put("pkPv", pv.getPkPv());
		  msgParam.put("isAdd", "0");
		  msgParam.put("isPiAdd", isAdd);//发送患者消息开关；0-新增 1-修改
		  msgParam.put("pkPi", regvo.getPkPi());
		  msgParam.put("timePosition", "0");//0：预约     1：当天
		  msgParam.put("addrPosition", "0");//0：现场 1：自助机 2：电话 3：微信  4：支付宝
		  PlatFormSendUtils.sendPvOpRegMsg(msgParam);
		  msgParam = null;
		  return regvo;
	}

	/**
	 * 根据诊疗服务，获取收费项目及价格信息
	 * 交易码：003002002003
	 * @param param{pkSchsrv,pkInsu,nameInsu,euPvType,pkPicate,flagSpec,dateBirth}
	 * @param user
	 * @return
	 */
	public List<ItemPriceVo>  getItemBySrv(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("flagSpec"))||CommonUtils.isNull(paramMap.get("pkSchsrv"))
				||CommonUtils.isNull(paramMap.get("pkPicate"))||CommonUtils.isNull(paramMap.get("euPvType")))
			throw new BusException("获取挂号收费信息时，必须选择医保及挂号类型！");
		//挂号全部按照全自费处理，，现在得设计是结算得时候退自费，然后按照医保类型在计算生成
		//boolean isCountStaff = "1".equals(CommonUtils.getPropValueStr(regSyxMapper.queryPhAttrVal(CommonUtils.getPropValueStr(paramMap, "pkInsu")),"valAttr", "0"));
		//判断当前患者是否为在职的本院职工（离职日期小于当前日期表示已离职）；(---上面的逻辑在产品文档中已经废弃2.3.1.5.9）
		boolean isCountStaff = StringUtils.isNotBlank(CommonUtils.getPropValueStr(paramMap,"pkEmp")) &&
				DataBaseHelper.queryForScalar("select count(1) from bd_ou_empjob job  where job.pk_emp=? and (job.date_left is null or job.date_left>to_date(?,'YYYYMMDDHH24MISS'))"
				,Integer.class,new Object[]{CommonUtils.getPropValueStr(paramMap,"pkEmp"), DateUtils.getDate("yyyyMMddHHmmss")})>0;
		if(StringUtils.isNotBlank(CommonUtils.getPropValueStr(paramMap,"staffEnhanceFlag")) && isCountStaff)	
		{
			String staffEnhanceFlag = CommonUtils.getPropValueStr(paramMap,"staffEnhanceFlag");
			if("1".equals(staffEnhanceFlag))
			{
				int nCount = DataBaseHelper.queryForScalar("select count(1) from bd_ou_employee boe  where boe.pk_emp=? and boe.flag_active='1'"
						,Integer.class,new Object[]{CommonUtils.getPropValueStr(paramMap,"pkEmp")});
				if(nCount == 0)
				{
					isCountStaff = false;
				}
			}
		}
		//是否走优惠计费策略，默认都走
		boolean blStrategy = MapUtils.getBooleanValue(paramMap,"blStrategy",true);
 		Map<String,Object> hpParam = new HashMap<String,Object>();
		hpParam.put("pkHp",paramMap.get("pkInsu"));
		hpParam.put("euPvType", paramMap.get("euPvType"));
		List<HpVo> hpList = cgStrategyPubMapper.queryHpList(hpParam);
		if(hpList==null||hpList.size()<=0)
			throw new BusException("所选医保类型【"+CommonUtils.getString(paramMap.get("nameInsu"))+"】不存在或已删除！");
		BdHp hp = new BdHp();
		ApplicationUtils.copyProperties(hp, hpList.get(0));
//		boolean  flagSpec = "1".equals(CommonUtils.getString(paramMap.get("flagSpec")))?true:false;
		//读取患者医保类型的扩展属性0311（本院职工），获取收费项目
		paramMap.put("euType", isCountStaff?"1":"0");
		List<ItemPriceVo> itemlist = regSyxProcessSupport.constructItemParam(paramMap);
		if(itemlist==null||itemlist.size()<=0)
			return null;
		PvEncounter pv = new PvEncounter();
		pv.setEuPvtype(paramMap.get("euPvType").toString());
	    pv.setFlagSpec(CommonUtils.getString(paramMap.get("flagSpec")));
	    String birthday = CommonUtils.getString(paramMap.get("dateBirth"));
	    BigDecimal  age = new BigDecimal(0);
	    if(CommonUtils.isNotNull(birthday)) {
	    	try {
				pv.setAgePv(""+DateUtils.getAge(DateUtils.parseDate(birthday,"YYYY-MM-DD")));
				age = new BigDecimal(pv.getAgePv());
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("转换出生日期异常！");
			}
	    }
		/* pkPi一直是空，不会走到此方法, age在上面取出生日期计算的年龄
		if(!CommonUtils.isEmptyString(pv.getPkPi())){
			age = CnPiPubService.getPvAge(pv.getPkPi());//计算患者年龄
		}
		*/
		
		List<ItemPriceVo> list = cgStrategyPubService.getItemAndOrdPrice(hp, ((User)user).getPkOrg(), new Date(), itemlist, pv,age);
	    if(blStrategy) {
			hpParam.put("pkPicate", paramMap.get("pkPicate"));
			hpParam.put("pkHp", null);
			List<HpVo> cateList = cgStrategyPubMapper.queryHpList(hpParam);//患者分类对应医保

			//调用医保记费策略
			list = cgStrategyPubService.getItemPriceByCgDiv(hp, pv, list);
			//调用优惠记费策略
			if(cateList!=null&&cateList.size()>0) {
				list = cgStrategyPubService.getItemDiscRatioByCgDiv(cateList.get(0), pv, list);
			}
			return list;
		} else {
			return priceStrategyService.priceZeroFilter(list);
		}

	}


	/**
	 * 保存或更新患者信息
	 * @param regvo
	 * @return
	 */
	public  PiMasterRegVo savePiMasterinfo(PiMasterRegVo regvo){
		PiMasterParam piParam = new PiMasterParam();
		PiMaster pi = new PiMaster();
		//中二的界面只有一个身份证号，默认证件类型就是身份证
		regvo.setDtIdtype((StringUtils.isNotBlank(regvo.getIdNo()) && StringUtils.isBlank(regvo.getDtIdtype()))?"01":regvo.getDtIdtype());
		ApplicationUtils.copyProperties(pi, regvo);
		if(CommonUtils.isNull(regvo.getPkPi())){//新增患者，先保存患者信息
			verifyMaster(regvo,true);
			/*if(CommonUtils.isNotNull(regvo.getCardNo())){
				piParam.setCardList(transformPiCard(regvo));
			}*/
			if(CommonUtils.isNotNull(regvo.getPkHp())){
				piParam.setInsuranceList(transformPiInsurance(regvo));
			}
			piParam.setMaster(pi);
			PiMaster piNew = piPubService.savePiMasterParam(piParam, new String[0]);
			//由于像深大那种会使用触发器修改,统一从库中取一次即可
			piNew.setCodeOp(MapUtils.getString(DataBaseHelper.queryForMap("select code_op from PI_MASTER where PK_PI=?",new Object[]{piNew.getPkPi()}),"codeOp"));
			//保存后的患者信息赋值给界面传过来的患者信息
			ApplicationUtils.copyProperties(regvo, piNew);
		}else{//只更新界面录入的几个字段
			verifyMaster(regvo,false);
			//健康卡是否需要读取外部接口 (老系统导入的患者，没有健康码，更新的时候需要注册健康码)
	        String extHealth = ApplicationUtils.getSysparam("PI0019", false);
	        if ("1".equals(extHealth)) {
	            //如果不存在健康码进行健康码注册
	            if (CommonUtils.isEmptyString(pi.getHicNo())) {
	                Map<String, Object> ehealthMap = new HashMap<>(16);
	                ehealthMap.put("piMaster", pi);
					//电子健康码注册
					Map<String, String> hicNo = (Map<String, String>) ExtSystemProcessUtils.processExtMethod("EHealthCode", "eHealthCodeEHC01", new Object[]{ehealthMap});
					if (hicNo != null) {
						regvo.setHicNo(hicNo.get("hicNo"));
						regvo.setNote(regvo.getNote()+hicNo.get("note"));
					}
	            }
	        }
			regSyxMapper.updatePiMasterByReg(regvo);
			//需求将codeOp门诊号作为卡号了
			/*if(CommonUtils.isNotNull(regvo.getCardNo())){
				piPubService.savePiCardList(transformPiCard(regvo), pi.getPkPi());
			}*/
			if(CommonUtils.isNotNull(regvo.getPkHp())){
				piPubService.savePiInsuranceList(transformPiInsurance(regvo), pi.getPkPi());
			}
		}
		return regvo;
	}

	/**
	 * 中二新加的两字段的唯一性校验
	 * @param regvo
	 */
	private void verifyMaster(PiMasterRegVo regvo,boolean isAdd){
		Object []objParam = isAdd?new Object[]{null}:new Object[]{null,regvo.getPkPi()};
		String pkPistr = isAdd?"":" and pk_pi != ? ";
		if(!isAdd){
			//新增的内部代码已经做了验证，这里只做修改时校验
			//1、校验编码是否重复
			objParam[0] = regvo.getCodePi();
			if (DataBaseHelper .queryForScalar( "select count(1) from pi_master "
					+ "where del_flag = '0' and code_pi = ?"+pkPistr, Integer.class, objParam) != 0)
				throw new BusException("患者编码重复，无法更新！");
			//3、校验住院号是否重复
			objParam[0] = regvo.getCodeIp();
			if(StringUtils.isNotBlank(regvo.getCodeIp())
					&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
							+ "where del_flag = '0' and code_ip = ?"+pkPistr,Integer.class, objParam))!=0){
				throw new BusException("住院号重复，无法更新！");
			}
			//4、校验身份证是否重复
			objParam[0] = regvo.getIdNo();
			if(StringUtils.isNotBlank(regvo.getIdNo())
					&& "01".equals(regvo.getDtIdtype())
					&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
							+ "where del_flag = '0' and dt_idtype = '01' and id_no = ?"+pkPistr,Integer.class, objParam))!= 0){
				throw new BusException("身份证号重复，无法更新！");
			}
		}
		objParam[0] = regvo.getSenNo();
		//5、校验老人证号是否重复
		if(StringUtils.isNotBlank(regvo.getSenNo())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and sen_no = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("老人优待证号重复！");
		}
		//6	、优抚证号是否重复
		objParam[0] = regvo.getSpcaNo();
		if(StringUtils.isNotBlank(regvo.getSpcaNo())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and spca_no = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("优抚证号重复！");
		}
		//6	、诊疗卡号是否重复
		objParam[0] = regvo.getCodeOp();
		if(StringUtils.isNotBlank(regvo.getCodeOp())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0' and  code_op=?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("诊疗卡号重复！");
		}
		//7,医疗证号
		objParam[0] = regvo.getMcno();
		if(StringUtils.isNotBlank(regvo.getMcno())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and mcno = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("医疗证号重复！");
		}
		//7,医保卡号
		objParam[0] = regvo.getInsurNo();
		if(StringUtils.isNotBlank(regvo.getInsurNo())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and insur_no = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("医保卡号重复！");
		}
		//8,健康卡号
		objParam[0] = regvo.getHicNo();
		if(StringUtils.isNotBlank(regvo.getHicNo())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and hic_no = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("健康卡号重复！");
		}
		//8,市民卡号
		objParam[0] = regvo.getCitizenNo();
		if(StringUtils.isNotBlank(regvo.getCitizenNo())
				&& (DataBaseHelper.queryForScalar("select count(1) from pi_master "
						+ "where del_flag = '0'and citizen_no = ?"+pkPistr,Integer.class, objParam))!= 0){
			throw new BusException("市民卡号重复！");
		}
	}

	private List<PiInsurance> transformPiInsurance(PiMasterRegVo regvo){
		List<PiInsurance> insulist = new ArrayList<PiInsurance>();
		PiInsurance insu = DataBaseHelper.queryForBean("select pi_insurance.* from pi_insurance where del_flag=0 and pk_pi=? and pk_hp=?", PiInsurance.class, new Object[]{regvo.getPkPi(),regvo.getPkHp()});
		//目前调用的方法是全部删了做插入的，那么这里直接设置为默认，序号为0
		if(insu == null) {
			insu = new PiInsurance();
			insu.setFlagDef("1");
			insu.setSortNo(0L);
			insu.setPkPi(regvo.getPkPi());
			insu.setPkHp(regvo.getPkHp());
		}
		insulist.add(insu);
		return insulist;
	}

	/**
	 * 保存就诊记录
	 * @param master
	 * @param pkPv
	 * @return
	 */
	public PvEncounter savePvEncounter(PiMasterRegVo master,String pkPv,Map<String,Object> schres){
		//是否允许挂号 二〇二〇年九月十七日 应产品要求直接注释
		allowReg(master);
		//科室的专业类型bd_ou_dept.dt_medicaltype='30'时，eu_pvtype=’4’
		if(StringUtils.isNotBlank(master.getPkDept()) &&
				"30".equals(CommonUtils.getPropValueStr(DataBaseHelper.queryForMap("select dt_medicaltype from bd_ou_dept where pk_dept=?",
						new Object[]{master.getPkDept()}),"dtMedicaltype"))){
			master.setEuPvtype(EnumerateParameter.FOUR);
		}
		// 保存就诊记录
		boolean jz = "2".equals(master.getEuPvtype());
		PvEncounter pvEncounter = new PvEncounter();
		pvEncounter.setPkPv(pkPv);
		pvEncounter.setPkPi(master.getPkPi());
		pvEncounter.setPkDept(master.getPkDept());//就诊科室
		pvEncounter.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ));
		pvEncounter.setEuPvtype(jz ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : (StringUtils.isBlank(master.getEuPvtype())?PvConstant.ENCOUNTER_EU_PVTYPE_1:master.getEuPvtype())); // 急诊|门诊
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
		if(master.getDateAppt()!=null){//如果是预约挂号，开始日期为预约日期
			//预约开始时间应该为日期+预约号所在的开始时间（bug-20723）
			String sql = "select time_begin from bd_code_dateslot where pk_dateslot = ?";
			Map<String, Object> queryForMap = DataBaseHelper.queryForMap(sql, master.getPkDateslot());
			String dateStr = DateUtils.dateToStr("yyyyMMdd", master.getDateAppt());
			String dateAppt = dateStr + " " + queryForMap.get("timeBegin").toString();
			master.setDateAppt(DateUtils.strToDate(dateAppt,"yyyyMMdd HH:mm:ss"));
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
		
		String pkDeptArea = pvInfoPubService.getPkDeptArea(master.getPkSchres());
   		pvEncounter.setPkDeptArea(pkDeptArea);
		DataBaseHelper.insertBean(pvEncounter);
		addInsGzgyPv(master, pvEncounter);

		return pvEncounter;
	}
	/**
	 * 保存就诊记录门诊属性
	 * @return
	 */
	public PvOp savePvOp(PiMasterRegVo master,PvEncounter pv,Map<String,Object> schres){
		// 保存门诊属性
		PvOp pvOp = new PvOp();
		pvOp.setPkPv(pv.getPkPv());
		Integer opTimes = regPubMapper.getMaxOpTimes(master.getPkPi());
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
		pvOp.setDtApptype(master.getDtApptype() == null?regSyxProcessSupport.getDefAppType():master.getDtApptype());
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
	private PvEr savePvEr(PiMasterRegVo master,PvEncounter pv){
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
		Map<String,Object> schres = regSyxMapper.querySchResInfo(paramMap);
		if(schres!=null){
			pvEr.setPkEmpPv(CommonUtils.getString(schres.get("pkEmp")));
			pvEr.setNameEmpPv(CommonUtils.getString(schres.get("nameEmp")));
		}
		pvEr.setTicketno(CommonUtils.isEmptyString(master.getTicketNo())?0:Long.parseLong(master.getTicketNo()));
		pvEr.setPkSch(master.getPkSch());

		// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
		// ( 参数-1) || '23:59:59'
		if(master.getDateAppt() != null && DateUtils.dateToStr("yyyyMMdd", master.getDateAppt()).equals(DateUtils.dateToStr("yyyyMMdd", new Date()))){
			pvEr.setDateBegin(new Date()); //如果为预约患者，预约日期为当前日期时开始时间为挂号时间
		}else{
			pvEr.setDateBegin(pv.getDateBegin());
		}
		pvEr.setDateEnd(ApplicationUtils.getPvDateEndEr(pv.getDateBegin()));
		pvEr.setDateArv(new Date());
		DataBaseHelper.insertBean(pvEr);
		return pvEr;
	}

	/**
	 * 保存就诊保险信息
	 * @param master
	 * @param pv
	 */
	public void savePvInsurance(PiMasterRegVo master,PvEncounter pv){
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
	 * 保存结算信息（包含门诊收费明细，门诊结算，门诊结算明细，交易记录，发票信息，发票明细）
	 * @return
	 * @throws BusException
	 */
	public PiMasterRegVo saveSettle(PiMasterRegVo master,PvEncounter pv) throws BusException {
		//没有收费项时不做收费处理
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return master;

		String pkOrg = master.getPkOrg();
		String pkPi = master.getPkPi();
		String pkPv = master.getPkPv();
		User user = UserContext.getUser();
		String pkCurDept = user.getPkDept();// 当前科室
		String pkOpDoctor = user.getPkEmp();// 当前用户主键
		String nameUser = user.getNameEmp();// 当前用户名

		//1.保存门诊收费明细
		List<BlOpDt> blOpDts = this.constructBlOpDt(master, pv);
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
			//amountInsu = amountInsu.add(new BigDecimal(((bpt.getPriceOrg() - bpt.getPrice()) + (bpt.getPriceOrg() * (1 - bpt.getRatioSelf()))) * bpt.getQuan()));
			Double amtInsu = MathUtils.mul(MathUtils.mul(bpt.getPrice(),1-bpt.getRatioSelf()),bpt.getQuan());
			amountInsu = amountInsu.add(BigDecimal.valueOf(amtInsu));
			//amountInsu = amountInsu.add(new BigDecimal(MathUtils.sub(bpt.getAmount(), bpt.getAmountPi())));
			pkDisc = bpt.getPkDisc();// 优惠类型
			if (pkDisc != null) {
				//患者优惠
				//discAmount = discAmount.add(new BigDecimal(MathUtils.mul(MathUtils.mul(bpt.getPriceOrg(), 1 - bpt.getRatioDisc()), bpt.getQuan())));
				discAmount = discAmount.add(BigDecimal.valueOf(MathUtils.sub(MathUtils.sub(bpt.getAmount(),bpt.getAmountPi()),amtInsu)));
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
		bs.setEuPvtype(master.getEuPvtype());
		bs.setPkInsurance(pv.getPkInsu());
		bs.setDtSttype("00");// 结算类型
		bs.setEuStresult(EnumerateParameter.ZERO);// 结算结果分类
		bs.setAmountSt(amountSt.setScale(2,BigDecimal.ROUND_HALF_UP));
		//此处患者支付还没有排除结算医保分摊的金额，同样医保支付还没有包含结算时医保分摊的金额
		bs.setAmountPi(amountPi.setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setAmountInsu(amountInsu.add(discAmount).add(amtInsuThird).setScale(2,BigDecimal.ROUND_HALF_UP));
		bs.setAmountRound(master.getAmountRound()!=null?master.getAmountRound():new BigDecimal(0D));

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
		bs.setDateReceipt(new Date());
		bs.setPkEmpReceipt(master.getPkEmpReceipt());
		bs.setNameEmpReceipt(master.getNameEmpReceipt());
		ApplicationUtils.setDefaultValue(bs, true);
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
		DataBaseHelper.batchUpdate("update bl_op_dt set pk_settle=:pkSettle,flag_settle=:flagSettle,ts=:ts where pk_cgop=:pkCgop ", blOpDtNews);
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
			bdHp = cgQryMaintainService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());
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
			bdHp = cgQryMaintainService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pv.getPkInsu());// 主医保计划
			blSettleDetail.setAmount(amountInsu.doubleValue());
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
			bdHp = cgQryMaintainService.qryBdHpInfo(mapParamTemp);
			blSettleDetail.setPkPayer(bdHp.getPkPayer());// 支付方
			blSettleDetail.setPkInsurance(pkDisc);// 优惠类型主键
			blSettleDetail.setAmount(discAmount.doubleValue());
			ApplicationUtils.setDefaultValue(blSettleDetail, true);// 设置默认字段
			blSettleDetails.add(blSettleDetail);
		}
		//不计算总额分摊的情况
        //3.4、患者支付金额
		if (amountPi.compareTo(BigDecimal.ZERO) == 1) {
			BlSettleDetail blSettleDetail = new BlSettleDetail();
			blSettleDetail.setPkSettle(bs.getPkSettle());
			mapParamTemp.put("pkOrg", "~                               ");
			BdPayer bdPayer = cgQryMaintainService.qryBdPayerByEuType(mapParamTemp);
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
			ApplicationUtils.setDefaultValue(blDepositDao, true);// 设置默认字段
			blDepositDao.setPkOrg(pkOrg);
			blDepositDao.setEuDptype(EnumerateParameter.ZERO);
			blDepositDao.setEuDirect(EnumerateParameter.ONE);
			blDepositDao.setPkPi(pkPi);
			blDepositDao.setPkPv(pkPv);
			blDepositDao.setEuPvtype(master.getEuPvtype());
			// 交易金额
			blDepositDao.setAmount(blDeposit.getAmount());
			blDepositDao.setDtPaymode(blDeposit.getDtPaymode());
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
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlDeposit.class), depositListDao);
		master.setDepositList(depositListDao);
		//判断是否对发票信息校验LB自助机使用, -2：不处理
		if(master.getInvStatus()==null || !("-2").equals(master.getInvStatus())){
			//5、写发票表
			String pkEmpinvoice = master.getPkEmpinv();// 票据领用主键
			String pkInvcate = master.getPkInvcate();// 票据分类主键
			String codeInv = master.getCodeInv();// 发票号码
		    if("1".equals(ApplicationUtils.getSysparam("BL0002", false))){//门诊挂号使用发票
		    	if (CommonUtils.isEmptyString(codeInv)||CommonUtils.isEmptyString(pkInvcate)||CommonUtils.isEmptyString(pkEmpinvoice)) {
					throw new BusException("挂号结算时未传入发票号或发票领用及发票分类信息！");
				}

		    	//获取BL0031（收费结算启用电子票据），参数值为1时打印电子票据
				//String eBillFlag = ApplicationUtils.getSysparam("BL0031", false,"0");
		    	String eBillFlag = invSettltService.getBL0031ByNameMachine(master.getNameMachine());
				if("1".equals(eBillFlag)){
					try{
						Map<String, Object> invMap = invSettltService.eBillRegistration(pkPv, user, pkSettle, master.getFlagPrint(),master.getNameMachine());
						master.setBlInvoiceList((List<BlInvoice>)invMap.get("invs"));
						// 更新发票领用表
						if (!CommonUtils.isEmptyString(codeInv)
								&& (!CommonUtils.isEmptyString(master.getFlagPrint()) && "1".equals(master.getFlagPrint())))
						{
							// 单张更新
							commonService.confirmUseEmpInv(pkEmpinvoice, 1L);
						}
					}catch(Exception e){
						e.printStackTrace();
						throw new BusException(e.getMessage());
					}

				}else{
					String sql = "select count(1) from BL_INVOICE where CODE_INV=? and del_flag='0'";
					int count = DataBaseHelper.queryForScalar(sql, Integer.class,
							codeInv);
					if (count > 0) {
						throw new BusException("当前票据号存在重复数据，请前往【票据管理】进行维护！");
					}

					// 挂号结算发票打印
				    // 单张发票 挂号用
					BlInvoice bi = new BlInvoice();
					bi.setPkOrg(pkOrg);
					bi.setPkInvcate(pkInvcate);// 票据分类主键
					bi.setCodeInv(codeInv);// 发票号码
					bi.setPkEmpinvoice(pkEmpinvoice);// 票据领用主键
					bi.setDateInv(new Date());// 发票日期
					bi.setAmountInv(amountSt.doubleValue());// 结算金额
					bi.setAmountPi(amountPi.doubleValue());// 患者自付
					bi.setPkEmpInv(pkOpDoctor);// 发票开立人员
					bi.setNameEmpInv(nameUser);
					bi.setPrintTimes(0);
					bi.setFlagCancel(EnumerateParameter.ZERO);
					bi.setFlagCc(EnumerateParameter.ZERO);
					bi.setFlagCcCancel(EnumerateParameter.ZERO);
					ApplicationUtils.setDefaultValue(bi, true);
					DataBaseHelper.insertBean(bi);
					// 目前发票明细是按收费项目的分类走
					// 1、写发票明细表
					mapParamTemp.clear();
					mapParamTemp.put("pkPv", pkPv);
					mapParamTemp.put("pkOrg", pkOrg);
					if(pkBlOpDts!=null&&pkBlOpDts.toString().length()>1){
						mapParamTemp.put("pkBlOpDtInSql", pkBlOpDts.toString().substring(0, pkBlOpDts.toString().length()-1));
					}
					List<BlInvoiceDt> blInvoiceDts = cgQryMaintainService.qryInfoForBlInvoiceDt(mapParamTemp);
					if (blInvoiceDts == null || blInvoiceDts.size() == 0) {
						throw new BusException("根据记费明细的收费项目分类查找对应的票据分类时出错，请在票据维护里维护门诊发票的使用");
					}
					List<BlInvoiceDt> blInvoiceDtList = new ArrayList<BlInvoiceDt>();
					for (BlInvoiceDt blInvoiceDt : blInvoiceDts) {
						blInvoiceDt.setPkInvoice(bi.getPkInvoice());
						ApplicationUtils.setDefaultValue(blInvoiceDt, true);// 设置默认字段
						blInvoiceDtList.add(blInvoiceDt);
					}
					DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BlInvoiceDt.class), blInvoiceDtList);
					master.setBlInvoiceDtList(blInvoiceDtList);
					// 2、写发票与结算的关系表
					BlStInv bsi = new BlStInv();
					bsi.setPkOrg(pkOrg);
					bsi.setPkSettle(bs.getPkSettle());
					bsi.setPkInvoice(bi.getPkInvoice());
					DataBaseHelper.insertBean(bsi);

					// 更新发票领用表
					if (!CommonUtils.isEmptyString(codeInv))
					{
						// 单张更新
						commonService.confirmUseEmpInv(pkEmpinvoice, 1L);
					}
				}
		    }
		}

	    //挂号凭条领用信息
		if (master.getPkEmpInvReceipt() != null && !"".equals(master.getPkEmpInvReceipt()))
		{
		  commonService.confirmUseEmpInv(master.getPkEmpInvReceipt(), 1L);
		}
		master.setPkSettle(pkSettle);
		return master;
	}
	/**
	 * 构建门诊记费参数
	 * @param master
	 * @param pv
	 * @return
	 */
	public List<BlOpDt> constructBlOpDt(PiMasterRegVo master,PvEncounter pv){
		if(master.getItemList()==null||master.getItemList().size()<=0)
			return null;
		String codeCg = ApplicationUtils.getCode("0601");
		List<BlOpDt> blopdts = new ArrayList<BlOpDt>();
		for(ItemPriceVo item:master.getItemList()){
			item.setFlagPd("0");
			BlOpDt vo = new BlOpDt();
			String pkCgop = NHISUUID.getKeyId();
			vo.setPkCgop(pkCgop);
			vo.setPkDeptAreaapp(pv.getPkDeptArea());
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
			Map<String, Object> resBill =  cgQryMaintainService
					.qryBillCodeByPkItem(param);
			Map<String, Object> resAccount = cgQryMaintainService
					.qryAccountCodeByPkItem(param);

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
	 * 校验患者是否在院
	 * 交易码：003002002013
	 * @param param{pkPi,pkHp}
	 * @param user
	 * @return
	 */
	public String verfyPatiInHospital(String param,IUser user){
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkPi")))
			return "0";
		if(CommonUtils.isNull(paramMap.get("pkHp")))
			throw new BusException("未传入医保主键pkHp!");
		List<PvEncounter> pvlist = regSyxMapper.queryPatiPvIpInfoByHp(paramMap);
		if(pvlist!=null&&pvlist.size()>0)
			return "1";
		return "0";
	}
	/**
	 * 查询患者信息{"pkPi","searchType","content"}
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPiInfo(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		verifyParmMap(paramMap);
		String dbType = MultiDataSource.getCurDbType();
		paramMap.put("dbType",dbType);
		List<Map<String, Object>> list = regSyxMapper.queryPiMaster(paramMap);
		if(CollectionUtils.isEmpty(list)){
			String sqlStr = null;
			if(StringUtils.isNotBlank(CommonUtils.getPropValueStr(paramMap, "pkEmp"))){
				sqlStr = "pk_emp =:pkEmp";
			}else if(StringUtils.equals(CommonUtils.getPropValueStr(paramMap, "searchType"),"5")){
				sqlStr = "code_emp =:content";
			}
			if(sqlStr != null)
				list = DataBaseHelper.queryForList("select name_emp name_pi,dt_sex,birthday birth_date,mobile,idno id_no,addr address,homephone tel_rel,pk_emp from bd_ou_employee WHERE flag_active = '1' and del_flag='0' and "+sqlStr, paramMap);
		}
		return list;
	}

	/**
	 * 查询患者当前日期的挂号记录
	 * @param param {"pkPi":"","date";"20190530"}
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPvEncounter(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		verifyParmMap(paramMap);
		if(StringUtils.isEmpty(CommonUtils.getPropValueStr(paramMap, "pkPi"))) {
			throw new BusException("请传入患者主键字段！");
		}
		if(StringUtils.isEmpty(CommonUtils.getPropValueStr(paramMap, "date"))) {
			throw new BusException("请传入日期字段！");
		}

		return regSyxMapper.queryPvEncounter(paramMap);
	}

	/**
	 * 查询患者预约待确认信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getConfirmReservations(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		verifyParmMap(paramMap);
		String pkPi = null;
		if(StringUtils.isEmpty(pkPi=CommonUtils.getPropValueStr(paramMap, "pkPi"))) {
			throw new BusException("请传入患者主键字段！");
		}
		return regSyxMapper.queryConfirmReservations(pkPi);
	}

	/**
	 * 依据排班查询第一个可预约号的开始时间（Hour）
	 * @param param
	 * @param user
	 * @return
	 */
	public String getFirstApptTicketHour(String param, IUser user){
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		Map<String, Object> mapResult = new  HashMap<String,Object>();
		if (Application.isSqlServer()) {
			mapResult = DataBaseHelper.queryForMap(
					"SELECT to_char(begin_time,'HH24:MI') appt_hour FROM(select top 1 percent begin_time from sch_ticket where pk_sch=? and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0' order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ) t ",
					CommonUtils.getPropValueStr(paramMap, "pkSch"));
		}else{
			mapResult = DataBaseHelper.queryForMap(
					"SELECT to_char(begin_time,'HH24:MI') appt_hour FROM(select begin_time from sch_ticket where pk_sch=? and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0' order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ) t WHERE rownum =1",
					CommonUtils.getPropValueStr(paramMap, "pkSch"));
		}


		return CommonUtils.getPropValueStr(mapResult, "apptHour");
	}

	private void verifyParmMap(Map<String, Object> paramMap) {
		if(MapUtils.isEmpty(paramMap)) {
			throw new BusException("未获取到入参！");
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

	public void allowReg(PiMasterRegVo master){
		if(master == null)
			return;
		
		String date = master.getDateAppt()!=null?DateUtils.formatDate(master.getDateAppt(), "yyyyMMdd"):
			(master.getDateReg()!=null?DateUtils.formatDate(master.getDateReg(), "yyyyMMdd"):null);
		
		if(StringUtils.isNotBlank(date)) {
			Integer regCount = DataBaseHelper.queryForScalar("select count(*) from PV_ENCOUNTER t WHERE t.PK_PI=? AND to_char(t.DATE_BEGIN,'yyyyMMdd') =? AND t.PK_DEPT=? AND t.EU_PVTYPE in('1','2') AND t.EU_STATUS in('0','1')",
					Integer.class, new Object[]{master.getPkPi(),date,master.getPkDept()});
			
			//允许患者同一日期重复挂号的科室编码
	        String withDateRepeatRegDept = ApplicationUtils.getSysparam("SCH0009", false);
	        //同一科室下允许患者重复挂号次数
	        String withDeptRepeatRegCount = ApplicationUtils.getSysparam("SCH0010", false);
	        //只有SCH0009配置了SCH0010才生效，SCH0009和SCH0010需联合使用
	        if(StringUtils.isNoneBlank(withDateRepeatRegDept,withDeptRepeatRegCount)) {
				int repeatRegInt = Integer.parseInt(withDeptRepeatRegCount);
	        	List<String> codeDeptList = Arrays.asList(withDateRepeatRegDept.split(",|，"));
				BdOuDept deptVo = DataBaseHelper.queryForBean("select code_dept,name_dept from bd_ou_dept t where pk_dept=? ", BdOuDept.class, new Object[]{master.getPkDept()});
				if(("ALL".equals(withDateRepeatRegDept.toUpperCase()) || codeDeptList.contains(deptVo.getCodeDept())) && regCount < repeatRegInt){
					return;
				}
				if(regCount >= repeatRegInt) {
					throw new BusException("患者【"+master.getNamePi()+"】在当前日期【"+deptVo.getNameDept()+"】科室下\n\r存在" + regCount + "次挂号记录，超过此科室最大挂号次数【"+ repeatRegInt + "】，\n\r不允许挂号！");
				}
	        }
			//默认规则一个患者在一个科室同一时段只能挂一次号
			if(regCount >0){
				throw new BusException("该患者已经存在当前日期和科室的挂号记录！");
			} 
		}
	}
	
	/**
	 * 公医患者挂号未写ins_gzgy_pv表
	 * @param master
	 * @param pvEncounter
	 */
	public void addInsGzgyPv(PiMasterRegVo master,PvEncounter pvEncounter){
		HpVo mainHp = null;
		if(!CommonUtils.isEmptyString(master.getPkHp())){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkHp", master.getPkHp());
			List<HpVo> hpList = cgStrategyPubMapper.queryHpList(paramMap);
			if(hpList!=null && hpList.size()>0){
				/**获取当前医保扩展属性值*/
				String valAttr = cgStrategyPubMapper.qryHpValAttr(master.getPkHp());
				mainHp = hpList.get(0);
				mainHp.setValAttr(valAttr);
			}
			if(mainHp == null)
				return;

			//查询患者主医保是否是公医患者，如果是则写ins_gzgy_pv表
			if ((!CommonUtils.isEmptyString(mainHp.getValAttr()) && ("1".equals(mainHp.getValAttr())))
					|| (!CommonUtils.isEmptyString(mainHp.getEuHptype()) && EnumerateParameter.FOUR.equals(mainHp.getEuHptype()))) { // 主医保是单位医保或是广州公医
				InsGzgyPv insGzgyPv = new InsGzgyPv();
				insGzgyPv.setPkPv(pvEncounter.getPkPv());
				insGzgyPv.setPkPi(master.getPkPi());
				insGzgyPv.setPkHp(pvEncounter.getPkInsu());
				insGzgyPv.setEuPvtype(master.getEuPvtype());
				insGzgyPv.setDelFlag("0");
				insGzgyPv.setMcno(master.getMcno());
				insGzgyPv.setDictSpecunit(master.getDtSpecunit());
				insGzgyPv.setEuPvmodeHp("0");
				DataBaseHelper.insertBean(insGzgyPv);
			}
		}
	}

	/**
	 * 交易号：003002001014
	 * 查询医保扩展属性
	 * @param param
	 * @param user
	 * @return
	 */
	public String qryHpAttrCode(String param, IUser user){
		String attrVal = null;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap!=null &&paramMap.size()>0
				&& paramMap.containsKey("pkHp") && paramMap.get("pkHp")!=null
				&& paramMap.containsKey("attrCode") && paramMap.get("attrCode")!=null){
			attrVal = regSyxMapper.qryHpAttrCodeVal(paramMap);
		}

		return attrVal;
	}

	/**
	 * 交易号：007002003043
	 * 保存挂号结算信息,自费转医保使用
	 * @param param
	 * @param user
	 */
	public PiMasterRegVo saveRegStInfo(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);

		//查询本次就诊信息
		PvEncounter pv = DataBaseHelper.queryForBean(
				"select * from PV_ENCOUNTER where pk_pv = ?",
				PvEncounter.class, new Object[]{regvo.getPkPv()});
		pv.setPkInsu(regvo.getPkHp());

		regvo.setPkPi(pv.getPkPi());
		regvo.setPkOrg(pv.getPkOrg());
		regvo.setCodePv(pv.getCodePv());

		/**原始挂号信息作废*/
		Map<String, Object> mapParam = new HashMap<String, Object>();
		User u = (User)user;
		mapParam.put("pkPv", regvo.getPkPv());
		mapParam.put("pkOrg", u.getPkOrg());

		mapParam.put("haveSettle", true);//要过滤掉未退号，先去给医保上报缴费时生成的一正一负的数据
		// 根据就诊主键查询挂号记费明细
		List<BlOpDt> blOpDts = DataBaseHelper.queryForList(
				"select * from bl_op_dt where PK_SETTLE = ?",
				BlOpDt.class,new Object[]{regvo.getPkSettle()});
		//有计费的才退费
		//退款记录
		List<BlDeposit> negaBlDeposits=null;
		if(CollectionUtils.isNotEmpty(blOpDts)
				&& (CommonUtils.isEmptyString(regvo.getFlagReback()) || "0".equals(regvo.getFlagReback()))){
			int size = blOpDts.size();
			String pkSettle = blOpDts.get(0).getPkSettle();
			String pkPi = blOpDts.get(0).getPkPi();
			mapParam.put("pkPi", pkPi);
			if (size > 1)
				for (int i = 1; i < size; i++) {
					if (!pkSettle.equals(blOpDts.get(i).getPkSettle())) {
						throw new BusException("此次挂号费用异常，形成了两笔结算信息。" + "pkPv:【" + blOpDts.get(i).getPkPv() + "】");
					}
				}
			// 根据结算主键查询结算信息
			mapParam.put("pkSettle", pkSettle);
			BlSettle blSettle = cgQryMaintainService.qryBlSettleInfoByPkpv(mapParam);
			// 生成退费结算信息
			if(blSettle == null) {
				throw new BusException("没有查询到患者的挂号结算信息");
			}
			String pkSettleCanc = opcgPubHelperService.generateRefoundSettle(blSettle,"20");
			// 生成退费明细
			BlOpDt regurnDef = new BlOpDt();
			regurnDef.setFlagRecharge("0");//重新生成待收费记录写1，否则写0
			opcgPubHelperService.generateRefoundRecord(blOpDts, pkSettleCanc,regurnDef);// 传入新的结算主键
			// 根据结算主键查询结算明细
			List<BlSettleDetail> blSettleDetail = cgQryMaintainService.qryBlSettleDetailInfoByBlSettle(mapParam);
			// 生成退费结算明细
			opcgPubHelperService.generateRefoundSettleDetail(blSettleDetail,pkSettleCanc);

			// 根据结算主键查询交款记录信息
			List<BlDeposit> blDeposits = cgQryMaintainService.qryRecordCashierByPkSettle(mapParam);
			// 生成退费的交款记录信息
			 negaBlDeposits = opcgPubHelperService.generateRefoundBlDeposits(blDeposits,pkSettleCanc);
			//加了非空判断
			if(negaBlDeposits != null){
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
						piAcc.setAmtAcc((piAcc.getAmtAcc() == null ? BigDecimal.ZERO : piAcc.getAmtAcc()).add(blDepositPi.getAmount()));
						balAccoutService.piAccDetailVal(piAcc, blDepositPi, regvo.getPkPv(), null);
					}
				}
			}

			String BL0002_code = ApplicationUtils.getSysparam("BL0002", false);
			// 如果挂号时打印的发票，要作废发票
			if (EnumerateParameter.ONE.equals(BL0002_code)) {
				// 根据结算主键查询作废结算时对应的发票
				List<BlInvoice> blInvoices = cgQryMaintainService.qryBlInvoiceInfoByBlSettle(mapParam);
				if(blInvoices!=null&&blInvoices.size()>0){
					for(BlInvoice inv:blInvoices){
						// 更新作废发票信息
						opcgPubHelperService.updateRefoundBlInvoice(inv);
					}
				}
			}

			//获取BL0031（收费结算启用电子票据），参数值为1时调用冲红电子票据接口
		     //String eBillFlag = ApplicationUtils.getSysparam("BL0031", false);
			String eBillFlag = invSettltService.getBL0031ByNameMachine(regvo.getNameMachine());
			if(!CommonUtils.isEmptyString(eBillFlag) && "1".equals(eBillFlag)){
				try{
					invSettltService.billCancelNoNewTrans(pkSettle, user);
				}catch (Exception e) {
					throw new BusException("退号失败：" + e.getMessage());
				}
			}

		}

		/**保存挂号信息*/
		regvo =  this.saveSettle(regvo, pv);


		/**修改患者主医保*/
		DataBaseHelper.execute("update PV_ENCOUNTER set pk_insu = ? where pk_pv = ?", new Object[]{regvo.getPkHp(),regvo.getPkPv()});

		return regvo;
	}

}
