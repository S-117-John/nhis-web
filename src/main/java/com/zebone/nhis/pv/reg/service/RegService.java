package com.zebone.nhis.pv.reg.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.syx.constant.IsAdd;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.service.PvInfoPubService;
import com.zebone.nhis.pv.pub.vo.PvOpAndSettleVo;
import com.zebone.nhis.pv.pub.vo.PvOpParam;
import com.zebone.nhis.pv.reg.dao.RegMapper;
import com.zebone.nhis.pv.reg.vo.PvApptAndEncounterVo;
import com.zebone.nhis.pv.reg.vo.PvOpVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 挂号相关服务
 * 
 * @author wangpeng
 * @date 2016年9月13日
 * 
 */
@Service
public class RegService {

	@Autowired
	private RegPubMapper regPubMapper;

	@Autowired
	private RegMapper regMapper;

	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	@Autowired
	private OpCgPubService opCgPubService;

	@Autowired
	private CommonService commonService;
	
	@Resource
    private PvInfoPubService pvInfoPubService;

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * 交易号：003002001004<br>
	 * 获取患者历史就诊记录、当前挂号信息、预约记录<br>
	 * 
	 * <pre>
	 * 1、获取患者预约记录
	 * 1.1 相关表：sch_appt（排班预约-预约）、sch_appt_pv（排班预约-预约就诊）、sch_resource（排班预约-排班资源）、bd_code_dateslot（日期分组）
	 * 1.2 sch_appt.pk_schres = sch_resource.pk_schres 查询预约资源 sch_resource.name as  resourceName 
	 * 1.3 午别：bd_code_dateslot。pk_dateslot = sch_appt.pk_dateslot 查询午别 bd_code_dateslot。name as dateslotName
	 * 2、获取患者历史就诊记录(最近五次)
	 * 2.1 相关表：pv_encounter（患者就诊-就诊记录）、pv_op（患者就诊-门诊属性）、sch_appt（排班预约-预约）、sch_appt_pv（排班预约-预约就诊）、sch_resource（排班预约-排班资源）
	 * 2.2 挂号资源：根据 pv_op.pk_res = sch_resource.pk_schres --> sch_resource.name as  resourceName
	 * 3、获取当前挂号信息
	 * 3.1 相关表：pv_encounter（患者就诊-就诊记录）、pv_op（患者就诊-门诊属性）、sch_appt（排班预约-预约）、sch_appt_pv（排班预约-预约就诊）、sch_resource（排班预约-排班资源）、bd_code_dateslot（日期分组）
	 * 3.2 挂号资源：根据就诊主键查询sch_appt_pv。pk_schappt --> sch_appt.pk_schres --> sch_resource.name as  resourceName
	 * 3.3 午别：根据就诊主键查询sch_appt_pv。pk_schappt --> sch_appt.pk_dateslot = bd_code_dateslot。pk_dateslo 查询午 别 bd_code_dateslot。name as dateslotName
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月19日
	 */
	public PvApptAndEncounterVo getPvApptAndEncounterVo(String param, IUser user) {

		PiMaster master = JsonUtil.readValue(param, PiMaster.class);
		String pkPi = master.getPkPi();
		String pkOrg = UserContext.getUser().getPkOrg();

		PvApptAndEncounterVo vo = new PvApptAndEncounterVo();
		List<SchAppt> apptList = regMapper.getSchApptListByPkPi(pkPi,pkOrg, DateUtils.dateToStr("yyyyMMdd", new Date()));
		List<PvOpVo> hisEncounterList = null;
		List<PvOpVo> todayEncounterList = null;
		//处理Oracle中不兼容问题
		if(Application.isSqlServer()){
			hisEncounterList = regMapper.getPvOpVoHistoryList(pkPi,pkOrg);
			todayEncounterList = regMapper.getPvOpVoTodayList(pkPi,pkOrg);
		}else{
			hisEncounterList = regMapper.getPvOpVoHistoryListOracle(pkPi,pkOrg);
			todayEncounterList = regMapper.getPvOpVoTodayListOracle(pkPi,pkOrg);
		}

		
		
		vo.setApptList(apptList);
		vo.setHisEncounterList(hisEncounterList);
		vo.setTodayEncounterList(todayEncounterList);

		return vo;
	}

	/**
	 * 交易号：003002001006<br>
	 * 保存挂号信息<br>
	 * 
	 * <pre>
	 * 根据患者主键查询患者信息（不包含照片）；
	 * 循环处理挂号信息：
	 * 1、根据排班主键 pkSch 查询 sch_ticket（排班预约-排班-号表） 获取   flag_used（已经使用标志）= 0（未使用） 、flag_stop（停用标志）= 0（未停用）的最小 ticketno （票号）
	 * 1.1 如果能查询到最小票号，根据排班主键 pkSch 更新 sch_sch（sch_sch）：cnt_used（数量_已使用）+1
	 * 1.1.1 根据 pkSch以及上述使用的ticketno（票号） 更新 sch_ticket： flag_used（已经使用标志）= 1（已使用）
	 * 1.2 如果没有号表，根据排班主键 pkSch查询sch_sch获取ticket_no 【cnt_used<=cnt_total and pk_org=:org】
	 * 1.2.1 根据主键更新 sch_sch：：cnt_used（数量_已使用）+1，ticket_no（当前就诊票号）+1
	 * 2、生成一条就诊信息  pv_encounter
	 * 3、如果是门诊，生成一条门诊附加信息  pv_op
	 * 4、保存医保计划 pv_insurance
	 * 4、调用接口保存结算、计费、发票等信息
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月19日
	 */
	public List<PvOpAndSettleVo> savePvEncounterAndOp(String param, IUser user) {
		List<PvOpAndSettleVo> voList = new ArrayList<PvOpAndSettleVo>();
		String isAdd = "0";
		// 返回值待定
		List<PvOpParam> paramList = JsonUtil.readValue(param, new TypeReference<List<PvOpParam>>() {
		});
		if (CollectionUtils.isEmpty(paramList)) {
			throw new BusException("参数错误，无法获取数据！");
		}

		String pkSchs = "";
		int i = 0;
		for (PvOpParam opParam : paramList) {
			if (i == 0) {
				pkSchs += "'" + opParam.getPkSch() + "'";
			} else {
				pkSchs += ",'" + opParam.getPkSch() + "'";
			}
			i++;
		}

		// 资源排班 sch_sch表 加锁，并获取排班相关数据
		// TODO 调试时不使用 for update,提交使用
		
		// 临时处理，去掉for update--挂号的时候会出现卡死的情况
		//String sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch where del_flag = '0' and pk_org = ? and pk_sch in (" + pkSchs + ") for update";
		
		String sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch where del_flag = '0' and pk_org = ? and pk_sch in (" + pkSchs + ") ";
		
		if (Application.isSqlServer()) {
			sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch with (rowlock,xlock) where del_flag = '0' and pk_org = ? and pk_sch in (" + pkSchs
					+ ")";
		}
		Map<String, Map<String, Object>> schMaps = DataBaseHelper.queryListToMap(sqlForUpdate, UserContext.getUser().getPkOrg());

		// 有多个挂号患者相同
		String pkPi = paramList.get(0).getPkPi();
		PiMaster master = regPubMapper.getPiMasterNoPhoto(pkPi);
		if (master == null) {
			throw new BusException("参数错误，无法获取患者信息！");
		}

		Integer opTimes = regPubMapper.getMaxOpTimes(pkPi);
		int j = 1;
		// 一次获取多个就诊流水号
		String[] codePvs = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ, paramList.size());// 门诊流水号
		// 循环处理挂号信息,预约的号（有预约主键pkAppo）使用数量不要再加1
		for (PvOpParam opParam : paramList) {
			String pkAppo = opParam.getPkAppo();
			Integer ticketno;
			String pkPv = NHISUUID.getKeyId();
			if (StringUtils.isNotEmpty(pkAppo)) {
				// 预约挂号的需要在挂号的时候是不是把sch_appt.eu_status 置为
				// 1（到达），然后把就诊主键写入sch_appt_pv.pk_pv
				ticketno = DataBaseHelper.queryForScalar(
						"select ticket_no from sch_appt where del_flag = '0' and (flag_cancel = '0' or flag_cancel is null) and pk_schappt = ?", Integer.class,
						pkAppo);
				Map<String, Object> apptMap = new HashMap<String, Object>();
				apptMap.put("pkPv", pkPv);
				apptMap.put("pkSchappt", pkAppo);
				apptMap.put("ts", new Date());
				apptMap.put("pkEmp", UserContext.getUser().getPkEmp());
				DataBaseHelper.update("update sch_appt set eu_status = '1', ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt", apptMap);
				DataBaseHelper.update("update sch_appt_pv set pk_pv =:pkPv, ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt", apptMap);
			} else {
				SchTicket ticket = new SchTicket();
				if (Application.isSqlServer()) {
					ticket = regPubMapper.getMinNoTicketnoByPkSchForSql(opParam.getPkSch(), UserContext.getUser().getPkOrg());
				} else {
					ticket = regPubMapper.getMinNoTicketnoByPkSch(opParam.getPkSch(), UserContext.getUser().getPkOrg());
				}

				if (ticket != null) {
					ticketno = Integer.parseInt(ticket.getTicketno());
					ticket.setFlagUsed("1"); // 已使用
					int result = DataBaseHelper.updateBeanByPk(ticket, true);
					if (result != 1) {
						throw new BusException("票号被重复获取，需重新操作");
					}
					DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { opParam.getPkSch() });
				} else {
					Map<String, Object> schMap = schMaps.get(opParam.getPkSch());
					if (Integer.parseInt(schMap.get("cntUsed").toString()) < Integer.parseInt(schMap.get("cntTotal").toString())) { // 已用数需要少于总数
						if (schMap.get("ticketNo") == null) {
							ticketno = 1;
							DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1,ticket_no = '2' where pk_sch = ?",
									new Object[] { opParam.getPkSch() });
						} else {
							ticketno = Integer.parseInt(schMap.get("ticketNo").toString());
							DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1,ticket_no = ? where pk_sch = ?", new Object[] {
									(ticketno + 1) + "", opParam.getPkSch() });
						}
					} else {
						throw new BusException("剩余票数不足");
					}
				}
			}

			// 保存就诊记录
			PvEncounter pvEncounter = new PvEncounter();
			pvEncounter.setPkPv(pkPv);
			pvEncounter.setPkPi(pkPi);
			pvEncounter.setPkDept(opParam.getPkDeptPv());
			pvEncounter.setCodePv(codePvs[j - 1]);
			pvEncounter.setEuPvtype("2".equals(opParam.getEuPvtype()) ? PvConstant.ENCOUNTER_EU_PVTYPE_2 : PvConstant.ENCOUNTER_EU_PVTYPE_1 ); // 急诊|门诊
			pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
			pvEncounter.setNamePi(master.getNamePi());
			pvEncounter.setDtSex(master.getDtSex());
			pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
			pvEncounter.setAddress(master.getAddress());
			pvEncounter.setDtMarry(master.getDtMarry());
			pvEncounter.setPkInsu(opParam.getPkInsu());
			pvEncounter.setPkPicate(opParam.getPkPicate());
			pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
			pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
			pvEncounter.setDateReg(new Date());
			pvEncounter.setDateBegin(pvEncounter.getDateReg());
			pvEncounter.setFlagCancel("0");
			
			String pkDeptArea = pvInfoPubService.getPkDeptArea(opParam.getPkRes());
	   		pvEncounter.setPkDeptArea(pkDeptArea);

			// 关闭事务自动提交
			DefaultTransactionDefinition def = new DefaultTransactionDefinition();
			def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			TransactionStatus status = platformTransactionManager.getTransaction(def); 
			List<CnOrder> signCnOrderList  =  new ArrayList<CnOrder>();
			List<CnOrder> qryOrderMsg = null;
		    try{
			   DataBaseHelper.insertBean(pvEncounter);
			   platformTransactionManager.commit(status); // 提交事务
		   } catch (Exception e) {
			   platformTransactionManager.rollback(status); // 添加失败 回滚事务；
		       e.printStackTrace();
			   throw new RuntimeException("保存挂号信息失败：" + e);
		   }
			// 保存门诊属性
			PvOp pvOp = new PvOp();
			pvOp.setPkPv(pvEncounter.getPkPv());
			pvOp.setOpTimes(new Long(opTimes + j));
			pvOp.setPkSchsrv(opParam.getPkSchsrv());
			pvOp.setPkRes(opParam.getPkRes());
			pvOp.setPkDateslot(opParam.getPkDateslot());
			// 挂号科室、医生从资源表里取sch_resource
			pvOp.setPkDeptPv(opParam.getPkDeptPv());
			pvOp.setPkEmpPv(opParam.getPkEmpPv());
			pvOp.setNameEmpPv(opParam.getNameEmpPv());
			pvOp.setTicketno(new Long(ticketno));
			pvOp.setPkSch(opParam.getPkSch());
			pvOp.setFlagFirst("1"); // 初诊
			pvOp.setPkAppo(opParam.getPkAppo()); // 对应预约
			// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
			// ( 参数-1) || '23:59:59'
			pvOp.setDateBegin(pvEncounter.getDateReg());
			if(!"9".equals(opParam.getEuSrvtype())){
				pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pvEncounter.getDateReg()));
			}else{
				pvOp.setDateEnd(DateUtils.strToDate(DateUtils.addDate(pvEncounter.getDateReg(), 24, 4, "yyyyMMddHHmmss")));
			}
			
			DataBaseHelper.insertBean(pvOp);
			j++;

			// 保存医保计划
			List<PvInsurance> insuranceList = opParam.getInsuList();
			List<PvInsurance> listForInsert = new ArrayList<PvInsurance>();
			if (CollectionUtils.isNotEmpty(insuranceList)) {
				for (PvInsurance insu : insuranceList) {
					insu.setPkPvhp(NHISUUID.getKeyId());
					insu.setPkOrg(UserContext.getUser().getPkOrg());
					insu.setCreator(UserContext.getUser().getPkEmp());
					insu.setCreateTime(new Date());
					insu.setModifier(UserContext.getUser().getPkEmp());
					insu.setTs(new Date());
					insu.setPkPv(pvEncounter.getPkPv());
					listForInsert.add(insu);
				}
				// 可以放循环外执行
				// DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvInsurance.class),
				// listForInsert);
			}
			// 保存医保计划
			// 调用结算时需要根据pk_pv查询pv_insurance表的信息所以将其提前插入---gxy
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PvInsurance.class), listForInsert);
			// 调用结算
			// 根据排班服务主键查询对应的收费项目主键
			Map<String, Object> mapParamTemp = new HashMap<String, Object>();
			User userObj = (User) user;
			mapParamTemp.put("pkOrg", userObj.getPkOrg());
			mapParamTemp.put("pkSchsrv", opParam.getPkSchsrv());
			List<String> pkItemsForPkSchSrv = cgQryMaintainService.qrySchSrvOrdsByPkSchsrv(mapParamTemp);
			Map<String, Double> mapItemAndQuan = new HashMap<String, Double>();
			for (String pkItem : pkItemsForPkSchSrv) {
				mapItemAndQuan.put(pkItem, 1d);// 根据排班服务关联的收费项目数量默认是1
			}
			List<BlOpDt> bods = opParam.getOpDtList();
			for (BlOpDt blOpDt : bods) {
				mapItemAndQuan.put(blOpDt.getPkItem(), blOpDt.getQuan());
			}
			mapParamTemp.put("pkPi", opParam.getPkPi());
			mapParamTemp.put("pkPv", pvEncounter.getPkPv());
			mapParamTemp.put("pkCurDept", userObj.getPkDept());
			mapParamTemp.put("pkUser", userObj.getPkEmp());// 当前用户主键
			mapParamTemp.put("userName", userObj.getUserName());
			mapParamTemp.put("depositList", opParam.getDepositList());// 支付方式
			mapParamTemp.put("pkEmpinv", opParam.getPkEmpinv());// 票据领用主键
			mapParamTemp.put("pkInvcate", opParam.getPkInvcate());// 票据分类主键
			mapParamTemp.put("codeInv", opParam.getCodeInv());// 发票号码
			mapParamTemp.put("amtInsuThird", opParam.getAmtInsuThird());//第三方医保支付金额
			List<BlInvoiceDt> blInvoiceDts = null;
			BlStInv blStInv = null;
			//含挂号费用的情况，进行结算，不含挂号费，不生成结算信息
			if(mapItemAndQuan!=null&&!mapItemAndQuan.isEmpty()){
				blInvoiceDts = opCgPubService.registerSettlement(mapParamTemp, mapItemAndQuan);
				//多加了一个判断条件，即使blInvoiceDts不为空，但是opParam.getPkEmpinv()可能为空，会报空指针异常
				if (blInvoiceDts != null && blInvoiceDts.size() > 0 && opParam.getPkEmpinv()!=null) {
					commonService.confirmUseEmpInv(opParam.getPkEmpinv(), 1L);
					// 取收费主键
					blStInv = DataBaseHelper.queryForBean("select * from bl_st_inv where pk_invoice=? ",
							BlStInv.class, new Object[]{blInvoiceDts.get(0).getPkInvoice()});
				}
				
			}
			
			// 返回值
			PvOpAndSettleVo vo = new PvOpAndSettleVo();
			vo.setOrderNo(opParam.getOrderNo());
			vo.setPkPv(pvEncounter.getPkPv());
			vo.setCodePv(pvEncounter.getCodePv());
			vo.setTicketno(ticketno.longValue());
			vo.setCodeInv(opParam.getCodeInv());
			// TODO 收费结算-发票明细
			vo.setBlInvoiceDtList(blInvoiceDts);
			vo.setPkSettle(blStInv != null ? blStInv.getPkSettle() : "");
			voList.add(vo);
			
			Map<String,Object> msgParam =  new HashMap<String,Object>();
			msgParam.put("pkEmp", UserContext.getUser().getPkEmp());
			msgParam.put("nameEmp", UserContext.getUser().getNameEmp());
			msgParam.put("codeEmp", UserContext.getUser().getCodeEmp());
			msgParam.put("pkPv", pvEncounter.getPkPv());
			msgParam.put("isAdd", isAdd);
			PlatFormSendUtils.sendPvOpRegMsg(msgParam);
			msgParam = null;
			
		}
		return voList;
	}

	/***
	 * 
	 * 保存预约签到挂号信息[弃用待测]<br>
	 * 交易号：
	 * 
	 * @author wangpeng
	 * @date 2017年3月10日
	 */
	public List<PvOpAndSettleVo> saveAppoPvEncounterAndOp(String param, IUser user) {

		List<PvOpAndSettleVo> voList = new ArrayList<PvOpAndSettleVo>();

		// 返回值待定
		List<PvOpParam> paramList = JsonUtil.readValue(param, new TypeReference<List<PvOpParam>>() {
		});
		if (CollectionUtils.isEmpty(paramList)) {
			throw new BusException("参数错误，无法获取数据！");
		}

		String pkSchs = "";
		int i = 0;
		for (PvOpParam opParam : paramList) {
			if (i == 0) {
				pkSchs += "'" + opParam.getPkSch() + "'";
			} else {
				pkSchs += ",'" + opParam.getPkSch() + "'";
			}
			i++;
		}

		// 资源排班 sch_sch表 加锁，并获取排班相关数据
		// TODO 调试时不使用 for update,提交使用
		String sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch where del_flag = '0' and pk_org = ? and pk_sch in (" + pkSchs + ") for update";
		if (Application.isSqlServer()) {
			sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch with (rowlock,xlock) where del_flag = '0' and pk_org = ? and pk_sch in (" + pkSchs
					+ ")";
		}
		Map<String, Map<String, Object>> schMaps = DataBaseHelper.queryListToMap(sqlForUpdate, UserContext.getUser().getPkOrg());

		// 有多个挂号患者相同
		String pkPi = paramList.get(0).getPkPi();
		PiMaster master = regPubMapper.getPiMasterNoPhoto(pkPi);
		if (master == null) {
			throw new BusException("参数错误，无法获取患者信息！");
		}

		Integer opTimes = regPubMapper.getMaxOpTimes(pkPi);
		int j = 1;
		// 一次获取多个就诊流水号
		String[] codePvs = ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZ, paramList.size());// 门诊流水号
		// 循环处理挂号信息,预约的号（有预约主键pkAppo）使用数量不要再加1
		for (PvOpParam opParam : paramList) {
			String pkAppo = opParam.getPkAppo();
			Integer ticketno;
			String pkPv = NHISUUID.getKeyId();
			if (StringUtils.isNotEmpty(pkAppo)) {
				// 预约挂号的需要在挂号的时候是不是把sch_appt.eu_status 置为
				// 1（到达），然后把就诊主键写入sch_appt_pv.pk_pv
				ticketno = DataBaseHelper.queryForScalar(
						"select ticket_no from sch_appt where del_flag = '0' and (flag_cancel = '0' or flag_cancel is null) and pk_schappt = ?", Integer.class,
						pkAppo);
				Map<String, Object> apptMap = new HashMap<String, Object>();
				apptMap.put("pkPv", pkPv);
				apptMap.put("pkSchappt", pkAppo);
				apptMap.put("ts", new Date());
				apptMap.put("pkEmp", UserContext.getUser().getPkEmp());
				DataBaseHelper.update("update sch_appt set eu_status = '1', ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt", apptMap);
				DataBaseHelper.update("update sch_appt_pv set pk_pv =:pkPv, ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt", apptMap);
			} else {
				SchTicket ticket = regPubMapper.getMinNoTicketnoByPkSch(opParam.getPkSch(), UserContext.getUser().getPkOrg());
				if (ticket != null) {
					ticketno = Integer.parseInt(ticket.getTicketno());
					ticket.setFlagUsed("1"); // 已使用
					int result = DataBaseHelper.updateBeanByPk(ticket, true);
					if (result != 1) {
						throw new BusException("票号被重复获取，需重新操作");
					}
					DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { opParam.getPkSch() });
				} else {
					Map<String, Object> schMap = schMaps.get(opParam.getPkSch());
					if (Integer.parseInt(schMap.get("cntUsed").toString()) < Integer.parseInt(schMap.get("cntTotal").toString())) { // 已用数需要少于总数
						if (schMap.get("ticketNo") == null) {
							ticketno = 1;
							DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1,ticket_no = '2' where pk_sch = ?",
									new Object[] { opParam.getPkSch() });
						} else {
							ticketno = Integer.parseInt(schMap.get("ticketNo").toString());
							DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1,ticket_no = ? where pk_sch = ?", new Object[] {
									(ticketno + 1) + "", opParam.getPkSch() });
						}
					} else {
						throw new BusException("剩余票数不足");
					}
				}
			}

			// 保存就诊记录
			PvEncounter pvEncounter = new PvEncounter();
			pvEncounter.setPkPv(pkPv);
			pvEncounter.setPkPi(pkPi);
			pvEncounter.setPkDept(opParam.getPkDeptPv());
			pvEncounter.setCodePv(codePvs[j - 1]);
			pvEncounter.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_1); // 门诊
			pvEncounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_0); // 登记
			pvEncounter.setNamePi(master.getNamePi());
			pvEncounter.setDtSex(master.getDtSex());
			pvEncounter.setAgePv(DateUtils.getAgeByBirthday(master.getBirthDate(),pvEncounter.getDateBegin()));
			pvEncounter.setAddress(master.getAddress());
			pvEncounter.setDtMarry(master.getDtMarry());
			pvEncounter.setPkInsu(opParam.getPkInsu());
			pvEncounter.setPkPicate(opParam.getPkPicate());
			pvEncounter.setPkEmpReg(UserContext.getUser().getPkEmp());
			pvEncounter.setNameEmpReg(UserContext.getUser().getNameEmp());
			pvEncounter.setDateReg(new Date());
			pvEncounter.setFlagCancel("0");
			
			String pkDeptArea = pvInfoPubService.getPkDeptArea(opParam.getPkRes());
	   		pvEncounter.setPkDeptArea(pkDeptArea);
	   		
			DataBaseHelper.insertBean(pvEncounter);

			// 保存门诊属性
			PvOp pvOp = new PvOp();
			pvOp.setPkPv(pvEncounter.getPkPv());
			pvOp.setOpTimes(new Long(opTimes + j));
			pvOp.setPkSchsrv(opParam.getPkSchsrv());
			pvOp.setPkRes(opParam.getPkRes());
			pvOp.setPkDateslot(opParam.getPkDateslot());
			// 挂号科室、医生从资源表里取sch_resource
			pvOp.setPkDeptPv(opParam.getPkDeptPv());
			pvOp.setPkEmpPv(opParam.getPkEmpPv());
			pvOp.setNameEmpPv(opParam.getNameEmpPv());
			pvOp.setTicketno(new Long(ticketno));
			pvOp.setPkSch(opParam.getPkSch());
			pvOp.setFlagFirst("1"); // 初诊
			pvOp.setPkAppo(opParam.getPkAppo()); // 对应预约
			// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
			// ( 参数-1) || '23:59:59'
			pvOp.setDateBegin(pvEncounter.getDateReg());
			pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pvEncounter.getDateReg()));
			DataBaseHelper.insertBean(pvOp);
			j++;

			// 返回值
			PvOpAndSettleVo vo = new PvOpAndSettleVo();
			vo.setOrderNo(opParam.getOrderNo());
			vo.setPkPv(pvEncounter.getPkPv());
			vo.setTicketno(ticketno.longValue());
			voList.add(vo);
		}

		return voList;
	}

	/**
	 * 交易号：003002001009<br>
	 * 获取门诊患者基本信息以及挂号费用信息<br>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月27日
	 */
	public PvOpAndSettleVo getPvOpAndSettleVo(String param, IUser user) {

		PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
		String pkPv = encounter.getPkPv();
		PvOpAndSettleVo vo = regMapper.getPvOpAndSettleVoByPkPv(pkPv);
		if (vo == null) {
			throw new BusException("无法获取就诊相关信息");
		}
		List<BlOpDt> opDtList = regMapper.getBlOpDtGroupListByPkPv(pkPv);
		List<BlDeposit> depositList = regMapper.getBlDepositGroupListByPkPv(pkPv);
		List<BlInvoiceDt> blInvoiceDtList = regMapper.getBlInvoiceDtList(pkPv);
		vo.setOpDtList(opDtList);
		vo.setDepositList(depositList);
		vo.setBlInvoiceDtList(blInvoiceDtList);

		return vo;
	}

	/**
	 * 
	 * 交易号：003002001010<br>
	 * 退号<br>
	 * 
	 * <pre>
	 * 1. 更新就诊记录表pv_encounter：eu_status=9 flag_cancel=1 pk_emp_cancel=user:id name_emp_cancel=user:name date_cancel=sysdate；
	 * 2. 更新排班表sch_sch：count_used = count_used -1；
	 * 3. 费用冲销，结算取消；
	 * 4. 如果有发票，作废发票；
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return void
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年10月10日
	 */
	public void cancelPv(String param, IUser user) {

		PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
		String pkPv = encounter.getPkPv();
		List<BlExtPayBankVo> blExtPayBank = encounter.getBlExtPayBank();
		
		// 判断就诊状态，只有为 0 的时候可以退号
		encounter = regPubMapper.getPvEncounterByPkPv(pkPv);
		if (!PvConstant.ENCOUNTER_EU_STATUS_0.equals(encounter.getEuStatus())) {
			throw new BusException("非登记状态无法退号！");
		}
		encounter.setEuStatus(PvConstant.ENCOUNTER_EU_STATUS_9); // 退诊
		encounter.setFlagCancel("1");
		encounter.setPkEmpCancel(UserContext.getUser().getPkEmp());
		encounter.setNameEmpCancel(UserContext.getUser().getNameEmp());
		encounter.setDateCancel(new Date());
		int count = DataBaseHelper.updateBeanByPk(encounter, true);
		if (count != 1) {
			throw new BusException("更新就诊记录失败!");
		}

		// 释放号源
		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = (select pk_sch from pv_op where pk_pv = ? )", new Object[] { pkPv });
		
		//查询门诊医生站退号是否产生退费记录
		String sql = "select count(1) from bl_settle where pk_pv=?";
		int count_settle = DataBaseHelper.queryForScalar(sql, Integer.class, pkPv);
		if(count_settle == 0){
			return;
		}
		
		// TODO 退号(003002001010) 费用冲销，结算取消;如果有发票，作废发票
		// 2016年10月26日15:45:56
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("pkOrg", ((User) user).getPkOrg());
		mapParam.put("pkPv", pkPv);
		opCgPubService.registrationRefound(mapParam,blExtPayBank);
		
		Map<String,Object>  paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("isAdd", IsAdd.UPDATE);
		paramMap.put("pkPv", pkPv);
		paramMap.put("pkEmp", ((User)user).getPkEmp());
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("codeEmp", ((User)user).getCodeEmp());
		PlatFormSendUtils.sendPvOpCancelRegMsg(paramMap);
		PlatFormSendUtils.sendPvOpRegMsg(paramMap);
		paramMap = null;
	}

	/**
	 * 
	 * 获取住院流水号
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年10月12日
	 */
	public String getPvEncounterCodeZy(String param, IUser user) {

		// 住院流水号 code-旧-003 新-0302"
		return ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZY);
	}

}
