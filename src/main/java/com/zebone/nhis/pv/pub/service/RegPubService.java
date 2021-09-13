package com.zebone.nhis.pv.pub.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.service.CgQryMaintainService;
import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.BlExtPayBankVo;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.vo.PvOpAndSettleVo;
import com.zebone.nhis.pv.pub.vo.PvOpParam;
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
public class RegPubService {

	@Autowired
	private RegPubMapper regPubMapper;

	@Autowired
	private CgQryMaintainService cgQryMaintainService;

	@Autowired
	private OpCgPubService opCgPubService;
	
	@Resource
    private PvInfoPubService pvInfoPubService;
	/***
	 * 
	 * 保存预约签到挂号信息<br>
	 * 交易号：003002001011
	 *
	 * @author wangpeng
	 * @date 2017年3月10日
	 */
	@SuppressWarnings("unused")
	public List<PvOpAndSettleVo> saveAppoPvEncounterAndOp(List<PvOpParam> paramList, IUser user) {

		List<PvOpAndSettleVo> voList = new ArrayList<PvOpAndSettleVo>();

		// // 返回值待定
		// List<PvOpParam> paramList = JsonUtil.readValue(param, new
		// TypeReference<List<PvOpParam>>() {
		// });
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
		String sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch where del_flag = '0' and pk_org = ? and pk_sch in ("
				+ pkSchs + ") for update";
		if (Application.isSqlServer()) {
			sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch with (rowlock,xlock) where del_flag = '0' and pk_org = ? and pk_sch in ("
					+ pkSchs + ")";
		}
		Map<String, Map<String, Object>> schMaps = DataBaseHelper.queryListToMap(sqlForUpdate,
				UserContext.getUser().getPkOrg());

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
						"select ticket_no from sch_appt where del_flag = '0' and (flag_cancel = '0' or flag_cancel is null) and pk_schappt = ?",
						Integer.class, pkAppo);
				if (ticketno == null) {
					throw new BusException("本次预约挂号未设置票号！");
				}
				Map<String, Object> apptMap = new HashMap<String, Object>();
				apptMap.put("pkPv", pkPv);
				apptMap.put("pkSchappt", pkAppo);
				apptMap.put("ts", new Date());
				apptMap.put("pkEmp", UserContext.getUser().getPkEmp());
				DataBaseHelper.update(
						"update sch_appt set eu_status = '1', ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt",
						apptMap);
				DataBaseHelper.update(
						"update sch_appt_pv set pk_pv =:pkPv, ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt",
						apptMap);
			} else {
				SchTicket ticket = new SchTicket();
				if (Application.isSqlServer()) {
					ticket = regPubMapper.getMinNoTicketnoByPkSchForSql(opParam.getPkSch(),
							UserContext.getUser().getPkOrg());
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
					DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",
							new Object[] { opParam.getPkSch() });
				} else {
					Map<String, Object> schMap = schMaps.get(opParam.getPkSch());
					if (Integer.parseInt(schMap.get("cntUsed").toString()) < Integer
							.parseInt(schMap.get("cntTotal").toString())) { // 已用数需要少于总数
						if (schMap.get("ticketNo") == null) {
							ticketno = 1;
							DataBaseHelper.update(
									"update sch_sch set cnt_used = cnt_used + 1,ticket_no = '2' where pk_sch = ?",
									new Object[] { opParam.getPkSch() });
						} else {
							ticketno = Integer.parseInt(schMap.get("ticketNo").toString());
							DataBaseHelper.update(
									"update sch_sch set cnt_used = cnt_used + 1,ticket_no = ? where pk_sch = ?",
									new Object[] { (ticketno + 1) + "", opParam.getPkSch() });
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
			pvEncounter.setDateBegin(pvEncounter.getDateReg());
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

			// 保存医保计划(预约签到没有辅助医保)
			PvInsurance insu = new PvInsurance();
			insu.setPkPvhp(NHISUUID.getKeyId());
			insu.setPkPv(pvEncounter.getPkPv());
			insu.setSortNo(1);
			insu.setPkHp(opParam.getPkInsu());
			insu.setFlagMaj("1");
			// 调用结算时需要根据pk_pv查询pv_insurance表的信息所以将其提前插入
			DataBaseHelper.insertBean(insu);

			// 根据排班服务主键查询对应的收费项目主键
			Map<String, Object> mapParamTemp = new HashMap<String, Object>();
			User userObj = UserContext.getUser();
			mapParamTemp.put("pkOrg", userObj.getPkOrg());
			mapParamTemp.put("pkSchsrv", opParam.getPkSchsrv());
			List<String> pkItemsForPkSchSrv = cgQryMaintainService.qrySchSrvOrdsByPkSchsrv(mapParamTemp);
			Map<String, Double> mapItemAndQuan = new HashMap<String, Double>();
			for (String pkItem : pkItemsForPkSchSrv) {
				mapItemAndQuan.put(pkItem, 1d);// 根据排班服务关联的收费项目数量默认是1
			}
			mapParamTemp.put("pkPi", opParam.getPkPi());
			mapParamTemp.put("pkPv", pvEncounter.getPkPv());
			mapParamTemp.put("pkCurDept", userObj.getPkDept());
			mapParamTemp.put("pkUser", userObj.getPkEmp());// 当前用户主键
			mapParamTemp.put("userName", userObj.getUserName());
			// mapParamTemp.put("depositList", opParam.getDepositList());// 支付方式
			// mapParamTemp.put("pkEmpinv", opParam.getPkEmpinv());// 票据领用主键
			// mapParamTemp.put("pkInvcate", opParam.getPkInvcate());// 票据分类主键
			// mapParamTemp.put("codeInv", opParam.getCodeInv());// 发票号码
			// List<BlInvoiceDt> blInvoiceDts =
			// opcgPubService.registerSettlement(mapParamTemp, mapItemAndQuan);
			mapParamTemp.put("isFlagClinic", "isFlagClinic");
			List<BlInvoiceDt> blInvoiceDts = opCgPubService.registerSettlement(mapParamTemp, mapItemAndQuan);
			String pkCurDept = userObj.getPkDept();// 当前科室
			String pkOpDoctor = userObj.getPkEmp();// 当前用户主键
			String nameUser = user.getUserName();// 当前用户名
			List<BlPubParamVo> blOpCgPubParamVos = new ArrayList<BlPubParamVo>();
			for (Map.Entry<String, Double> entryTemp : mapItemAndQuan.entrySet()) {// 循环收费项目MAP
				String pkItem = entryTemp.getKey();// 收费项目主键
				double quan = entryTemp.getValue();// 收费项目数量
				BlPubParamVo bppv = new BlPubParamVo();
				bppv.setPkOrg(userObj.getPkOrg());
				bppv.setPkPv(pkPv);
				bppv.setPkPi(pkPi);
				bppv.setPkItem(pkItem);
				bppv.setQuanCg(quan);// 收费项目数量
				bppv.setPkOrgEx(userObj.getPkOrg());// 执行机构
				bppv.setPkOrgApp(userObj.getPkOrg());// 开立机构
				bppv.setPkDeptEx(pkCurDept);// 执行科室
				bppv.setPkDeptApp(pkCurDept);// 开立科室
				bppv.setPkEmpApp(pkOpDoctor);// 开立医生
				bppv.setNameEmpApp(nameUser);// 开立医生姓名
				bppv.setFlagPd(EnumerateParameter.ZERO);// 物品标志
				bppv.setFlagPv(EnumerateParameter.ONE);// 挂号费用标志
				bppv.setDateHap(new Date());// 费用发生日期
				bppv.setPkDeptCg(pkCurDept);// 记费科室
				bppv.setPkEmpCg(pkOpDoctor);// 记费人员
				bppv.setNameEmpCg(nameUser);// 记费人员名称
				blOpCgPubParamVos.add(bppv);
			}
			
			//如果预约签到已经结算则不需要调用计费方法
			int count = DataBaseHelper.queryForScalar("select count(1) from bl_settle where pk_pv=?", Integer.class, pvEncounter.getPkPv());
			if(count == 0){
			// 调用通用的计费方法
				opCgPubService.blOpCg(blOpCgPubParamVos);
			}
			
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
	 * 保存门诊医生站挂号信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<PvOpAndSettleVo> saveClinicPvEncounterAndOp(String param, IUser user) {
		List<PvOpAndSettleVo> voList = new ArrayList<PvOpAndSettleVo>();
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
		String sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch where del_flag = '0' and pk_org = ? and pk_sch in ("
				+ pkSchs + ") for update";
		if (Application.isSqlServer()) {
			sqlForUpdate = "select pk_sch as key_ ,sch_sch.* from sch_sch with (rowlock,xlock) where del_flag = '0' and pk_org = ? and pk_sch in ("
					+ pkSchs + ")";
		}
		Map<String, Map<String, Object>> schMaps = DataBaseHelper.queryListToMap(sqlForUpdate,
				UserContext.getUser().getPkOrg());

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
						"select ticket_no from sch_appt where del_flag = '0' and (flag_cancel = '0' or flag_cancel is null) and pk_schappt = ?",
						Integer.class, pkAppo);
				Map<String, Object> apptMap = new HashMap<String, Object>();
				apptMap.put("pkPv", pkPv);
				apptMap.put("pkSchappt", pkAppo);
				apptMap.put("ts", new Date());
				apptMap.put("pkEmp", UserContext.getUser().getPkEmp());
				DataBaseHelper.update(
						"update sch_appt set eu_status = '1', ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt",
						apptMap);
				DataBaseHelper.update(
						"update sch_appt_pv set pk_pv =:pkPv, ts =:ts, modifier =:pkEmp where pk_schappt =:pkSchappt",
						apptMap);
			} else {
				SchTicket ticket = new SchTicket();
				if (Application.isSqlServer()) {
					ticket = regPubMapper.getMinNoTicketnoByPkSchForSql(opParam.getPkSch(),
							UserContext.getUser().getPkOrg());
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
					DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",
							new Object[] { opParam.getPkSch() });
				} else {
					Map<String, Object> schMap = schMaps.get(opParam.getPkSch());
					if (Integer.parseInt(schMap.get("cntUsed").toString()) < Integer
							.parseInt(schMap.get("cntTotal").toString())) { // 已用数需要少于总数
						if (schMap.get("ticketNo") == null) {
							ticketno = 1;
							DataBaseHelper.update(
									"update sch_sch set cnt_used = cnt_used + 1,ticket_no = '2' where pk_sch = ?",
									new Object[] { opParam.getPkSch() });
						} else {
							ticketno = Integer.parseInt(schMap.get("ticketNo").toString());
							DataBaseHelper.update(
									"update sch_sch set cnt_used = cnt_used + 1,ticket_no = ? where pk_sch = ?",
									new Object[] { (ticketno + 1) + "", opParam.getPkSch() });
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
			pvEncounter.setDateBegin(pvEncounter.getDateReg());
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
			pvOp.setFlagFirst(opParam.getFlagFirst()); // 初诊标志
			pvOp.setPkAppo(opParam.getPkAppo()); // 对应预约
			// 有效开始时间为登记时间，结束时间根据设置来计算:date_end=to_date(date_begin,'yyyymmdd') +
			// ( 参数-1) || '23:59:59'
			pvOp.setDateBegin(pvEncounter.getDateReg());
			pvOp.setDateEnd(ApplicationUtils.getPvDateEnd(pvEncounter.getDateReg()));
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

			// 根据排班服务主键查询对应的收费项目主键
			Map<String, Object> mapParamTemp = new HashMap<String, Object>();
			User userObj = UserContext.getUser();
			mapParamTemp.put("pkOrg", userObj.getPkOrg());
			mapParamTemp.put("pkSchsrv", opParam.getPkSchsrv());
			List<String> pkItemsForPkSchSrv = cgQryMaintainService.qrySchSrvOrdsByPkSchsrv(mapParamTemp);
			Map<String, Double> mapItemAndQuan = new HashMap<String, Double>();
			for (String pkItem : pkItemsForPkSchSrv) {
				mapItemAndQuan.put(pkItem, 1d);// 根据排班服务关联的收费项目数量默认是1
			}
			List<BlOpDt> bods = opParam.getOpDtList();
			BigDecimal price = BigDecimal.ZERO;
			for (BlOpDt blOpDt : bods) {
				mapItemAndQuan.put(blOpDt.getPkItem(), blOpDt.getQuan());
			}
			//查询挂号费
			String sql = "select sum(t.price) as price from sch_srv_ord so "
					+ "inner join bd_ord_item ot on so.pk_ord = ot.pk_ord "
					+ "inner join bd_item t on ot.pk_item=t.pk_item where so.del_flag = '0' and ot.del_flag = '0' and so.pk_schsrv=?";
			price = DataBaseHelper.queryForScalar(sql, BigDecimal.class, opParam.getPkSchsrv());
			BlDeposit bldeposit = new BlDeposit();
			bldeposit.setAmount(price);
			bldeposit.setDtPaymode(EnumerateParameter.FOUR);
			bldeposit.setDatePay(new Date());
			List<BlDeposit> depositList = new ArrayList<BlDeposit>();
			depositList.add(bldeposit);
			opParam.setDepositList(depositList);
			mapParamTemp.put("pkPi", opParam.getPkPi());
			mapParamTemp.put("pkPv", pvEncounter.getPkPv());
			mapParamTemp.put("pkCurDept", userObj.getPkDept());
			mapParamTemp.put("pkUser", userObj.getPkEmp());// 当前用户主键
			mapParamTemp.put("userName", userObj.getUserName());
			mapParamTemp.put("depositList", opParam.getDepositList());// 支付方式
//			mapParamTemp.put("pkEmpinv", opParam.getPkEmpinv());// 票据领用主键
//			mapParamTemp.put("pkInvcate", opParam.getPkInvcate());// 票据分类主键
//			mapParamTemp.put("codeInv", opParam.getCodeInv());// 发票号码
//			mapParamTemp.put("pkEmpinv", null);// 票据领用主键
//			mapParamTemp.put("pkInvcate", null);// 票据分类主键
//			mapParamTemp.put("codeInv", null);// 发票号码
			mapParamTemp.put("isFlagClinic", "isFlagClinic");
			List<BlInvoiceDt> blInvoiceDts = opCgPubService.registerSettlement(mapParamTemp, mapItemAndQuan);
//			BlStInv blStInv = null;
//			if (blInvoiceDts != null && blInvoiceDts.size() > 0) {
//				commonService.confirmUseEmpInv(opParam.getPkEmpinv(), 1L);
//				// 取收费主键
//				blStInv = DataBaseHelper.queryForBean("select * from bl_st_inv where pk_invoice=? ",
//						BlStInv.class, new Object[]{blInvoiceDts.get(0).getPkInvoice()});
//			}
			String pkCurDept = userObj.getPkDept();// 当前科室
			String pkOpDoctor = userObj.getPkEmp();// 当前用户主键
			String nameUser = user.getUserName();// 当前用户名
			List<BlPubParamVo> blOpCgPubParamVos = new ArrayList<BlPubParamVo>();
			for (Map.Entry<String, Double> entryTemp : mapItemAndQuan.entrySet()) {// 循环收费项目MAP
				String pkItem = entryTemp.getKey();// 收费项目主键
				double quan = entryTemp.getValue();// 收费项目数量
				BlPubParamVo bppv = new BlPubParamVo();
				bppv.setPkOrg(userObj.getPkOrg());
				bppv.setPkPv(pkPv);
				bppv.setPkPi(pkPi);
				bppv.setPkItem(pkItem);
				bppv.setQuanCg(quan);// 收费项目数量
				bppv.setPkOrgEx(userObj.getPkOrg());// 执行机构
				bppv.setPkOrgApp(userObj.getPkOrg());// 开立机构
				bppv.setPkDeptEx(pkCurDept);// 执行科室
				bppv.setPkDeptApp(pkCurDept);// 开立科室
				bppv.setPkEmpApp(pkOpDoctor);// 开立医生
				bppv.setNameEmpApp(nameUser);// 开立医生姓名
				bppv.setFlagPd(EnumerateParameter.ZERO);// 物品标志
				bppv.setFlagPv(EnumerateParameter.ONE);// 挂号费用标志
				bppv.setDateHap(new Date());// 费用发生日期
				bppv.setPkDeptCg(pkCurDept);// 记费科室
				bppv.setPkEmpCg(pkOpDoctor);// 记费人员
				bppv.setNameEmpCg(nameUser);// 记费人员名称
				blOpCgPubParamVos.add(bppv);
			}
			// 调用通用的计费方法
//			opcgPubService.blOpCg(blOpCgPubParamVos);

			
			
			// 获取排班资源
			String sql_sch = "select sr.name as srname,sv.name as svname from sch_sch ss inner join sch_resource sr on ss.pk_schres=sr.pk_schres "
					+ "  inner join sch_srv sv on sv.pk_schsrv=ss.pk_schsrv where ss.pk_sch=?";
			Map<String, Object> map_sch = DataBaseHelper.queryForMap(sql_sch, opParam.getPkSch());
			// 获取科室名称
			Map<String,Object> deptMap = DataBaseHelper.queryForMap("select name_dept from bd_ou_dept where pk_dept=?",opParam.getPkDeptPv());
			String nameDept = "";
			if(deptMap!=null&&deptMap.get("nameDept")!=null)
				 nameDept = deptMap.get("nameDept").toString();
			// 返回值
			PvOpAndSettleVo vo = new PvOpAndSettleVo();
			vo.setOrderNo(opParam.getOrderNo());
			vo.setPkPv(pvEncounter.getPkPv());
			vo.setTicketno(ticketno.longValue());
			vo.setCodePv(pvEncounter.getCodePv());
			vo.setSchResName(CommonUtils.getString(map_sch.get("srname")));
			vo.setSchsrvName(CommonUtils.getString(map_sch.get("srname")));
			vo.setDeptPvName(nameDept);
			voList.add(vo);
		}
		return voList;
	}

	/**
	 * 门诊医生站退号
	 * 
	 * @param param
	 * @param user
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	public void cancelPvClinic(String param, IUser user) {

		Map<String, String> mapParam = JsonUtil.readValue(param, HashMap.class);
		String pkPv = mapParam.get("pkPv");
		String pkEmp = UserContext.getUser().getPkEmp();
		
		int count_order = DataBaseHelper.queryForScalar("select count(1) from cn_order where pk_pv=?", Integer.class,
				pkPv);
		int count_emr = DataBaseHelper.queryForScalar("select count(1) from cn_emr_op where pk_pv=?", Integer.class,
				pkPv);
		int count_en = DataBaseHelper.queryForScalar("select count(1) from pv_encounter where pk_pv=? and pk_emp_reg=?",
				Integer.class, pkPv, pkEmp);

		// 以下四种情况下都满足才可退号
		if (count_order != 0) {
			throw new BusException("已开立医嘱不可退号");
		}else if(count_emr !=0){
			throw new BusException("已书写病历不可退号");
		}else if (count_en ==0) {
			throw new BusException("非本医生挂出不可退号");
		} else {
			String sql = "select name_emp from bd_ou_employee where pk_emp=?";
			Map<String,Object> regMap = DataBaseHelper.queryForMap(sql, pkEmp);
			String name_emp_reg = null;
			if(regMap!=null&&regMap.get("nameEmp")!=null)
				name_emp_reg = regMap.get("nameEmp").toString();
			Date dateCancel = new Date();
			// 更新就诊状态
			String sql_up = "update pv_encounter set eu_status='9',flag_cancel='1',pk_emp_cancel=?,name_emp_cancel=?,date_cancel=? where pk_pv=?";
			DataBaseHelper.update(sql_up, new Object[] { pkEmp, name_emp_reg, dateCancel, pkPv });
			// 释放资源
			DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = (select pk_sch from pv_op where pk_pv = ? )",
					new Object[] { pkPv });
			
			//退号(003002001010) 费用冲销，结算取消;如果有发票，作废发票
			Map<String, String> map = new HashMap<String, String>();
			mapParam.put("pkOrg", ((User) user).getPkOrg());
			mapParam.put("pkPv", pkPv);
			List<BlExtPayBankVo> blExtPayBank = new ArrayList<BlExtPayBankVo>();
			opCgPubService.registrationRefound(mapParam,blExtPayBank);
		}
	}
}
