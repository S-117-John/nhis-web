package com.zebone.nhis.pv.adt.service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pv.PvBed;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.ex.pub.service.AdtPubService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.pv.adt.dao.AdtRegMapper;
import com.zebone.nhis.pv.pub.dao.RegPubMapper;
import com.zebone.nhis.pv.pub.vo.AdtOutParam;
import com.zebone.nhis.pv.reg.dao.RegMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * ADT基础服务,包含：入院登记、取消入院、出院、取消出院相关接口
 * 
 * @author wangpeng
 * @date 2016年9月21日
 * 
 */
@Service
public class AdtRegService {

	@Resource
	private AdtRegMapper adtRegMapper;

	@Resource
	private RegMapper regMapper;

	@Autowired
	private RegPubMapper regPubMapper;

	@Resource
	private AdtPubService adtPubService;

	@Resource
	private BdResPubService bdResPubService;//处理婴儿床位公共类

	// 用来控制手动事物
	@Resource(name = "transactionManager")
	private PlatformTransactionManager platformTransactionManager;

	/**
	 * 交易号：003004001004<br>
	 * 取消入院<br>
	 * 
	 * <pre>
	 * 1、检查是否可以取消入院：
	 * 1.1 存在未作废医嘱，存在病历不允许取消入院;
	 * 1.2 累计费用不为0，不允许取消入院；
	 * 1.3 累计押金不为0，不允许取消入院；
	 * 1.4 外放接口，用于后续添加是否允许取消入院的校验条件；
	 * 2、取消后更新表
	 * 2.1 更新就诊表pv_encounter：eu_status=9 flag_in=0 flag_cancel=1 pk_emp_cancel=user:id name_emp_cancel=user:name date_cancel=sysdate
	 * 2.2 更新入院通知单pv_ip_notice：eu_status=1 pk_pv_ip=null (pk_dept_ip=null pk_dept_ns_ip=null dt_bedtype=null)--2018-03-23 设置不清空，并添加备注
	 * 2.3 更新医护人员表pv_staff：date_end=new Date()
	 * 2.4更新医疗组表pv_clinic_group：date_end=new Date()
	 * 2.5 更新床位记录表pv_bed：date_end=new Date() pk_emp_end=user:id name_emp_end=user:name
	 * 2.6 更新转科表pv_adt：date_end=new Date() pk_emp_end=user:id name_emp_end=user:name
	 * 2.7 更新床位表bd_res_bed：dt_sex=？ flag_active=？ eu_status=？ flag_ocupy=？ pk_dept_used=？ pk_pi=？ 
	 * 3、取消入院后的住院号不可以再次使用，取消入院后就诊记录作废并非删除
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月29日
	 */
	public void deleteAdtReg(String param, IUser user) {
		User u = UserContext.getUser();

		PvEncounter encounter = JsonUtil.readValue(param, PvEncounter.class);
		String pkPv = encounter.getPkPv();// 从前台获取患者就诊主键

		AdtOutParam outParam = JsonUtil.readValue(param, AdtOutParam.class);
		String pkDeptNs = outParam.getPkDeptNs();// 从前台获取患者就诊病区

		// 取消入院校验
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkPv", pkPv);

		// 查询患者未处理完的数据时，需要传入患者的就诊病区
		// if(!CommonUtils.isEmptyString(pkDeptNs))
		// map.put("pkDeptNs", pkDeptNs);

		Map<String, Object> result = adtPubService.getDeptOutVerfyData(map,
				user);
		if (result != null) {
			if (CommonUtils.isNotNull(result.get("ordData"))
					&& ((List) result.get("ordData")).size() > 0) {
				throw new BusException("存在未停未作废医嘱，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("risData"))
					&& ((List) result.get("risData")).size() > 0) {
				throw new BusException("存在未执行执行单，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("apData"))
					&& ((List) result.get("apData")).size() > 0) {
				throw new BusException("存在未完成请领单，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("packBedData"))
					&& ((List) result.get("packBedData")).size() > 0) {
				throw new BusException("存在包床记录，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("exListData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未执行执行单，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("opData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未完成的手术申请单，不允许取消出院！");
			}
			if (CommonUtils.isNotNull(result.get("cpData"))
					&& ((List) result.get("exListData")).size() > 0) {
				throw new BusException("存在未完成的在径医嘱，不允许取消出院！");
			}
		}
		// 累计费用
		Long amountDt = DataBaseHelper
				.queryForScalar(
						"select sum(amount) from bl_ip_dt where del_flag = '0' and flag_settle='0' and pk_pv= ? ",
						Long.class, pkPv);
		if (amountDt != null && amountDt > 0) {
			throw new BusException("累计费用不为0,不允许取消入院！");
		}
		// 累计押金
		Long amountSettle = DataBaseHelper
				.queryForScalar(
						"select sum(amount) from bl_deposit where del_flag = '0' and eu_dptype = '9' and flag_settle='0' and pk_pv= ? ",
						Long.class, pkPv);
		if (amountSettle != null && amountSettle > 0) {
			throw new BusException("累计押金不为0,不允许取消入院！");
		}
		// 存在病历
		Integer countbl = DataBaseHelper
				.queryForScalar(
						"select count(1) from EMR_MED_REC where del_flag = '0' and pk_pv=?",
						Integer.class, new Object[] { pkPv });
		if (countbl > 0) {
			throw new BusException("存在病历,不允许取消入院！");
		}

		// 更新pv_encounter
		encounter = regPubMapper.getPvEncounterByPkPv(pkPv);
		encounter.setEuStatus("9");
		encounter.setFlagIn("0");
		encounter.setFlagCancel("1");
		encounter.setPkEmpCancel(UserContext.getUser().getPkEmp());
		encounter.setNameEmpCancel(UserContext.getUser().getNameEmp());
		encounter.setDateCancel(new Date());
		encounter.setModifier(UserContext.getUser().getPkEmp());
		int count = DataBaseHelper.updateBeanByPk(encounter, true);
		if (count != 1) {
			throw new BusException("就诊记录更新失败！");
		}

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkPv", pkPv);
		mapParam.put("pkPi", encounter.getPkPi());
		mapParam.put("nameEmp", u.getNameEmp());
		mapParam.put("noticeNote", DateUtils.getDate("yyyy-MM-dd HH:mm:ss")
				+ "办理取消入院！");
		// 更新入院通知单表入院通知单表
		// DataBaseHelper.update("update pv_ip_notice set eu_status = '1',pk_pv_ip = null, pk_dept_ip = null, pk_dept_ns_ip = null, dt_bedtype = null, modifier =:modifier, ts =:ts where pk_pv_ip =:pkPv",
		// mapParam);
		// 2018-03-23修改为不清空待入院病区，科室，以及床位类型，添加备注
		DataBaseHelper
				.update("update pv_ip_notice set eu_status = '1',pk_pv_ip = null,note= :noticeNote, modifier =:modifier, ts =:ts where pk_pv_ip =:pkPv",
						mapParam);

		// 更新医护人员表pv_staff
		DataBaseHelper
				.update("update pv_staff set date_end =:ts, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新医疗组pv_clinic_group
		DataBaseHelper
				.update("update pv_clinic_group set date_end =:ts, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新就诊床位记录表pv_bed
		DataBaseHelper
				.update("update pv_bed set date_end =:ts, pk_emp_end =:modifier, name_emp_end =:nameEmp, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新转科表pv_adt
		DataBaseHelper
				.update("update pv_adt set date_end =:ts, pk_emp_end =:modifier, name_emp_end =:nameEmp, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新床位表bd_res_bed
		DataBaseHelper
				.update("update bd_res_bed set dt_sex = null, eu_status = '01', flag_ocupy = '0', pk_pi = null, pk_dept_used = null ,modifier =:modifier, ts =:ts where pk_pi =:pkPi",
						mapParam);

		// 2018-12-26 中二需求，取消入院同步更新旧系统患者信息
		ExtSystemProcessUtils.processExtMethod("PiInfo", "cancelInhospital",
				pkPv);
		// 发送取消入院信息
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		PlatFormSendUtils.sendPvCancelInMsg(paramMap);
		paramMap = null;

		// 中山二院--调用平安APP接口推送消息
		ExtSystemProcessUtils.processExtMethod("PAAPP", "noticeCancelInHosp",
				param);
	}

	/**
	 * 交易号：003004001005 -- 已作废，未使用该方法<br>
	 * 出院<br>
	 * 
	 * <pre>
	 * 1、判断是否可以办理出院
	 * 1.1 存在未停医嘱或未做废(新开医嘱)不允许办理出院；
	 * 1.2 医嘱有效，检验申请单处在申请、预约、采集时不允许出院；
	 * 1.3 医嘱有效，检查申请单处在申请、预约时不允许出院；
	 * 1.4 医嘱有效，手术申请单未完成时不允许出院；
	 * 1.5 医嘱有效，输血申请单未完成不允许出院；
	 * 1.6 医嘱有效，医技辅申请单未完成不允许出院；
	 * 1.7 物品请领单已经发放完成或者停发，否则不允许出院；
	 * 1.8 存在包床信息，未退包床不允许出院；
	 * 2、办理出院后更新的表结构
	 * 2.1 更新就诊表pv_encounter：eu_status=2  flag_in=0 date_end=param:dateEnd；
	 * 2.2 更新住院属性表pv_ip：dt_outcomes=param:dtOutcomes  pk_dept_dis=pv_encounter.pk_dept   pk_dept_ns_dis=pv_encounter.pk_dept_ns  dt_outtype=param:dtOuttype；
	 * 2.3 更新医护人员表pv_staff：date_end=param:dateEnd；
	 * 2.4 更新医疗组表date_end：date_end=param:dateEnd；
	 * 2.5 更新转科表pv_adt:date_end=param:dateEnd   pk_emp_end=user:id  name_emp_end=user:name  flag_dis=1
	 * 2.6 更新床位记录表pv_bed：date_end=param:dateEnd  pk_emp_end=user:id  name_emp_end=user:name
	 * 2.7 更新床位表bd_res_bed：dt_sex=？ flag_active=？ eu_status=？ flag_ocupy=？ pk_dept_used=？ pk_pi=？
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月29日
	 */
	public void outHospital(String param, IUser user) {
		User u = UserContext.getUser();

		AdtOutParam outParam = JsonUtil.readValue(param, AdtOutParam.class);
		String pkPv = outParam.getPkPv();

		PvEncounter encounter = regPubMapper.getPvEncounterByPkPv(pkPv);

		// 出院校验
		String flagCheck = outParam.getFlagCheck();
		if ("1".equals(flagCheck)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pkPv", pkPv);

			// 查询患者未处理完的数据时，需要传入患者的就诊病区
			if (!CommonUtils.isEmptyString(outParam.getPkDeptNs()))
				map.put("pkDeptNs", outParam.getPkDeptNs());

			Map<String, Object> result = adtPubService.getDeptOutVerfyData(map,
					user);
			if (result != null) {
				if (CommonUtils.isNotNull(result.get("ordData"))
						&& ((List) result.get("ordData")).size() > 0) {
					throw new BusException("存在未停未作废医嘱，不允许出院！");
				}
				if (CommonUtils.isNotNull(result.get("risData"))
						&& ((List) result.get("risData")).size() > 0) {
					throw new BusException("存在未执行执行单，不允许出院！");
				}
				if (CommonUtils.isNotNull(result.get("apData"))
						&& ((List) result.get("apData")).size() > 0) {
					throw new BusException("存在未完成请领单，不允许出院！");
				}
				if (CommonUtils.isNotNull(result.get("packBedData"))
						&& ((List) result.get("packBedData")).size() > 0) {
					throw new BusException("存在包床记录，不允许出院！");
				}
				if (CommonUtils.isNotNull(result.get("exListData"))
						&& ((List) result.get("exListData")).size() > 0) {
					throw new BusException("存在未执行执行单，不允许出院！");
				}
				if (CommonUtils.isNotNull(result.get("opData"))
						&& ((List) result.get("exListData")).size() > 0) {
					throw new BusException("存在未完成的手术申请单，不允许取消出院！");
				}
				if (CommonUtils.isNotNull(result.get("cpData"))
						&& ((List) result.get("exListData")).size() > 0) {
					throw new BusException("存在未完成的在径医嘱，不允许取消出院！");
				}
			}
		}

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkPv", pkPv);
		mapParam.put("dateEnd", outParam.getDateEnd());
		mapParam.put("pkPi", encounter.getPkPi());
		mapParam.put("dateNotice", outParam.getDateEnd());
		mapParam.put("dtOutcomes", outParam.getDtOutcomes());
		mapParam.put("pkDeptDis", encounter.getPkDept());
		mapParam.put("pkDeptNsDis", encounter.getPkDeptNs());
		mapParam.put("dtOuttype", outParam.getDtOuttype());
		mapParam.put("nameEmp", u.getNameEmp());

		// 更新表结构
		// 更新就诊记录表pv_encounter:就诊状态改为‘2’护士站出院
		DataBaseHelper
				.update("update pv_encounter set eu_status = '2', flag_in = '0', date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv",
						mapParam);

		// 更新住院属性表pv_ip
		DataBaseHelper
				.update("update pv_ip set date_notice =:dateNotice, dt_outcomes =:dtOutcomes, "
						+ " pk_dept_dis =:pkDeptDis, pk_dept_ns_dis =:pkDeptNsDis, dt_outtype =:dtOuttype,"
						+ " date_notice =:ts, pk_emp_notice =:modifier, name_emp_notice =:nameEmp, "
						+ "modifier =:modifier, ts =:ts where pk_pv =:pkPv",
						mapParam);

		// 更新医护人员表pv_staff
		DataBaseHelper
				.update("update pv_staff set date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新医疗组pv_clinic_group
		DataBaseHelper
				.update("update pv_clinic_group set date_end =:dateEnd, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新转科表pv_adt
		DataBaseHelper
				.update("update pv_adt set date_end =:dateEnd, pk_emp_end =:modifier, name_emp_end =:nameEmp, flag_dis = '1', modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新就诊床位记录表pv_bed
		DataBaseHelper
				.update("update pv_bed set date_end =:dateEnd, pk_emp_end =:modifier, name_emp_end =:nameEmp, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end is null",
						mapParam);

		// 更新床位表bd_res_bed
		DataBaseHelper
				.update("update bd_res_bed set eu_status = '01', flag_ocupy = '0', pk_pi = null, pk_dept_used = null, modifier =:modifier, ts =:ts where pk_pi =:pkPi",
						mapParam);

	}

	/**
	 * 交易号：<> 取消出院<br>
	 * 
	 * <pre>
	 * 校验：1、患者是否被冻结 pv_ip.flag_frozen = 1 已冻结，不可取消
	 * 1、取消出院后，科室、病区、主管医师、主管护士恢复到出院前状态，患者恢复到待入科接收状态;
	 * 2、更新表结构
	 * 2.1 更新就诊记录表pv_encounter:eu_status=0  flag_in=null  date_end=null;
	 * 2.2 更新住院属性表pv_ip: pk_dept_dis=null  pk_dept_ns_dis=null  dt_outtype=null  dt_outcomes=null;
	 * 2.3 更新医护人员表pv_staff:date_end=null;
	 * 2.4 更新医疗组pv_clinic_group:date_end=null;
	 * 2.5 更新转科表pv_adt:date_end=null  pk_emp_end=null  name_emp_end=null  flag_dis=null;
	 * 2.6 更新就诊床位记录表pv_bed:date_end=nul  pk_emp_end=null  name_emp_end=null;
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月30日
	 */
	public void cancelOut(String param, IUser user) {
		User u = UserContext.getUser();

		AdtOutParam outParam = JsonUtil.readValue(param, AdtOutParam.class);
		String pkPv = outParam.getPkPv();
		//婴儿母亲信息
		PvEncounter pvMomInfo = outParam.getPvMomInfo();
		String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
		// 2019-02-19 取消出院前校验就诊状态是否为冻结
		// int cntFrozen =
		// DataBaseHelper.queryForScalar("select count(1) from pv_ip where pk_pv = ? and flag_frozen = '1'"
		// + " and del_flag = '0'",Integer.class, new Object[]{pkPv});
		// if(cntFrozen > 0)
		// throw new BusException("所选患者就诊为冻结状态，不能取消出院！");
		//2020-05-09 取消出院前校验是否有就诊中的记录
		List<Map<String,Object >> mapList= DataBaseHelper.queryForList(
						"select * from pv_encounter where eu_status='1' and eu_pvtype='3' and pk_pi=?",new Object[] { outParam.getPkPi() });
		if (mapList.size() > 0) {
			String sql = "SELECT NAME_DEPT FROM BD_OU_DEPT WHERE PK_DEPT = ?";
			Map<String, Object> mapNs = DataBaseHelper.queryForMap(sql,
					new Object[] { mapList.get(0).get("pkDeptNs") });
			throw new BusException("当前患者存在" + mapNs != null ? CommonUtils.getString(mapNs.get("nameDept")) : "" + "未出院记录，不允许重复入院！");
		}
		PvEncounter encounter = adtRegMapper.getPvEncounterByPkPv(pkPv);

		String queSql = "select * from pv_infant where pk_pv_infant = ?";
		PvInfant pvInfant = DataBaseHelper.queryForBean(queSql,PvInfant.class,pkPv);
		String bedNo = "";//婴儿重新生成的床位号
		List<BdResBed> bedList = new ArrayList<>();
		BdResBed bedMa = new BdResBed();

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkPv", pkPv);
		mapParam.put("bedNo", outParam.getBedNo());
		mapParam.put("pkWard", outParam.getPkDeptNs());
		mapParam.put("pkPi", outParam.getPkPi());

			// 取消出院时将冻结状态设置为0
			DataBaseHelper
					.update("update pv_ip set flag_frozen = '0', modifier =:modifier where pk_pv =:pkPv",
							mapParam);
			// 相关表只能恢复最后一条记录
			// 更新就诊记录表pv_encounter:就诊状态改为'1'(直接入科)，在院标志改为'1'(在院),置空结束时间
			int cnt_pv = DataBaseHelper
					.update("update pv_encounter set eu_status = '1', bed_no =:bedNo, flag_in = '1', date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and eu_status = '2' ",
							mapParam);
			if (cnt_pv < 1)
				throw new BusException("患者的当前就诊记录已经结算处理，不允许取消出院！");

			// 更新住院属性表pv_ip:置空pk_dept_dis、pk_dept_ns_dis、dt_outtype、dt_outcomes、date_notice、dt_sttype_ins[结算类型-中二新增]
			DataBaseHelper
					.update("update pv_ip set dt_sttype_ins = null, date_notice = null, dt_outcomes = null, pk_dept_dis = null, pk_dept_ns_dis = null, dt_outtype = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv",
							mapParam);

			// 更新医护人员表pv_staff: 置空date_end
			DataBaseHelper
					.update("update pv_staff set date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end = (select max(date_end) from pv_staff where pk_pv =:pkPv)",
							mapParam);

			// 更新医疗组pv_clinic_group: 置空date_end
			DataBaseHelper
					.update("update pv_clinic_group set date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end = (select max(date_end) from pv_clinic_group where pk_pv =:pkPv)",
							mapParam);

			// 更新转科表pv_adt: 置空date_end pk_emp_end name_emp_end flag_dis
			DataBaseHelper
					.update("update pv_adt set date_end = null, pk_emp_end = null, name_emp_end = null, flag_dis = '0', modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end  = (select max(date_end) from pv_adt where pk_pv =:pkPv)",
							mapParam);


			if(pvInfant != null && !outParam.getBedNo().equals(encounter.getBedNo()) && "1".equals(BD0013)){
				//婴儿床位分隔符
				String bedSpc = ExSysParamUtil.getSpcOfCodeBed();//获取婴儿床位分隔符
				if(CommonUtils.isEmptyString(bedSpc))
					throw new BusException("请维护系统参数【BD0007】-婴儿床位编码分隔符！");

				//获取婴儿全局排序
				Integer sortNo = pvInfant.getSortNo();
				if(pvMomInfo != null && pvMomInfo.getPkDeptNs().equals(outParam.getPkDeptNs()) && pvMomInfo.getBedNo().equals(outParam.getBedNo())){

					bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_pi = ? and pk_ward = ? and code = ? "
							, BdResBed.class, new Object[]{pvMomInfo.getPkPi(),outParam.getPkDeptNs(),outParam.getBedNo()});
					bedNo = pvMomInfo.getBedNo()+bedSpc+sortNo;
				}else{
					bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where  pk_ward = ? and code = ? "
							, BdResBed.class, new Object[]{outParam.getPkDeptNs(),outParam.getBedNo()});
					bedNo = outParam.getBedNo()+bedSpc+sortNo;
				}
				bedList = bdResPubService.isHaveBedAndAdd(outParam.getPkDeptNs(), bedMa, bedNo, null, u);
				//修改婴儿占用床位主键
				String updSql = "update pv_ip set pk_bed_an = ? where pk_pv = ?";
				DataBaseHelper.update(updSql, new Object[]{outParam.getPkBed(),pkPv});
				//修改婴儿就诊信息床位号
				updSql = "update pv_encounter set bed_no = ? where pk_pv = ? ";
				DataBaseHelper.update(updSql, new Object[]{bedNo,pkPv});
				mapParam.put("bedNo",bedList.get(0).getCode());
			}



			if (outParam.getBedNo().equals(encounter.getBedNo())) {// 如果还是原来的床位，置为出院前状态；如果是新的床位，需要插入新的就诊床位记录
				// 更新就诊床位记录表pv_bed: 置空date_end pk_emp_end name_emp_end
				DataBaseHelper
						.update("update pv_bed set date_end = null, pk_emp_end = null, name_emp_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end  = (select max(date_end) from pv_bed where pk_pv =:pkPv)",
								mapParam);
			} else {
				PvBed bed = new PvBed();
				bed.setPkPv(pkPv);
				bed.setPkDept(encounter.getPkDept());
				bed.setPkDeptNs(encounter.getPkDeptNs());
				if(pvInfant != null && !outParam.getBedNo().equals(encounter.getBedNo()) && "1".equals(BD0013)){
					bed.setBedno(bedList.get(0).getCode());
				}else{
					bed.setBedno(outParam.getBedNo());
				}
				bed.setDateBegin(new Date());
				bed.setPkBedWard(outParam.getPkDeptNs());
				bed.setEuHold("0");
				bed.setPkEmpBegin(u.getPkEmp());
				bed.setNameEmpBegin(u.getNameEmp());
				bed.setFlagMaj("1"); // 主床位
				DataBaseHelper.insertBean(bed);
			}

			// 处理床位表bd_res_bed
			int cntBed = DataBaseHelper
					.update("update bd_res_bed set eu_status = '02', flag_ocupy = '1', pk_pi =:pkPi, modifier =:modifier, ts =:ts where pk_ward =:pkWard and code =:bedNo and pk_pi is null",
							mapParam);
			if (cntBed < 1)
				throw new BusException("当前床位【" + mapParam.get("bedNo")
						+ "】已经被占用，请重新选择！");

			// 处理婴儿床位表
			DataBaseHelper
					.update("update bd_res_bed set del_flag = '0', flag_active = '1' where pk_ward =:pkWard and code =:bedNo and pk_pi=:pkPi",
							mapParam);


		// 发送取消出院信息至平台
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		paramMap.put("pkPv", paramMap.get("pkPv"));
		paramMap.put("status", "update");
		PlatFormSendUtils.sendPvCancelOutMsg(paramMap);
		paramMap = null;

		// 中山二院--调用平安APP接口推送消息
		ExtSystemProcessUtils.processExtMethod("PAAPP", "noticeCancelOutHosp",
				param);
	}

	/**
	 * 交易号：<> 取消出院<br>
	 * 
	 * <pre>
	 * 校验：1、患者是否被冻结 pv_ip.flag_frozen = 1 已冻结，不可取消
	 * 1、取消出院后，科室、病区、主管医师、主管护士恢复到出院前状态，患者恢复到待入科接收状态;
	 * 2、更新表结构
	 * 2.1 更新就诊记录表pv_encounter:eu_status=0  flag_in=null  date_end=null;
	 * 2.2 更新住院属性表pv_ip: pk_dept_dis=null  pk_dept_ns_dis=null  dt_outtype=null  dt_outcomes=null;
	 * 2.3 更新医护人员表pv_staff:date_end=null;
	 * 2.4 更新医疗组pv_clinic_group:date_end=null;
	 * 2.5 更新转科表pv_adt:date_end=null  pk_emp_end=null  name_emp_end=null  flag_dis=null;
	 * 2.6 更新就诊床位记录表pv_bed:date_end=nul  pk_emp_end=null  name_emp_end=null;
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 * @throws
	 * 
	 * @author wangpeng
	 * @date 2016年9月30日
	 */
	public void cancelOutAutoCommint(String param, IUser user) {
		User u = UserContext.getUser();

		AdtOutParam outParam = JsonUtil.readValue(param, AdtOutParam.class);
		String pkPv = outParam.getPkPv();

		// 2019-02-19 取消出院前校验就诊状态是否为冻结
		// int cntFrozen =
		// DataBaseHelper.queryForScalar("select count(1) from pv_ip where pk_pv = ? and flag_frozen = '1'"
		// + " and del_flag = '0'",Integer.class, new Object[]{pkPv});
		// if(cntFrozen > 0)
		// throw new BusException("所选患者就诊为冻结状态，不能取消出院！");

		PvEncounter encounter = adtRegMapper.getPvEncounterByPkPv(pkPv);

		Map<String, Object> mapParam = new HashMap<String, Object>();
		mapParam.put("modifier", u.getPkEmp());
		mapParam.put("ts", new Date());
		mapParam.put("pkPv", pkPv);
		mapParam.put("bedNo", outParam.getBedNo());
		mapParam.put("pkWard", outParam.getPkDeptNs());
		mapParam.put("pkPi", outParam.getPkPi());
		// yangxue 修改为手动事物
		// 关闭事务自动提交
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		TransactionStatus status = platformTransactionManager
				.getTransaction(def);
		try {
			// 取消出院时将冻结状态设置为0
			DataBaseHelper
					.update("update pv_ip set flag_frozen = '0', modifier =:modifier where pk_pv =:pkPv",
							mapParam);
			// 相关表只能恢复最后一条记录
			// 更新就诊记录表pv_encounter:就诊状态改为'1'(直接入科)，在院标志改为'1'(在院),置空结束时间
			int cnt_pv = DataBaseHelper
					.update("update pv_encounter set eu_status = '1', bed_no =:bedNo, flag_in = '1', date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and eu_status = '2' ",
							mapParam);
			if (cnt_pv < 1)
				throw new BusException("患者的当前就诊记录已经结算处理，不允许取消出院！");

			// 更新住院属性表pv_ip:置空pk_dept_dis、pk_dept_ns_dis、dt_outtype、dt_outcomes、date_notice、dt_sttype_ins[结算类型-中二新增]
			DataBaseHelper
					.update("update pv_ip set dt_sttype_ins = null, date_notice = null, dt_outcomes = null, pk_dept_dis = null, pk_dept_ns_dis = null, dt_outtype = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv",
							mapParam);

			// 更新医护人员表pv_staff: 置空date_end
			DataBaseHelper
					.update("update pv_staff set date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end = (select max(date_end) from pv_staff where pk_pv =:pkPv)",
							mapParam);

			// 更新医疗组pv_clinic_group: 置空date_end
			DataBaseHelper
					.update("update pv_clinic_group set date_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end = (select max(date_end) from pv_clinic_group where pk_pv =:pkPv)",
							mapParam);

			// 更新转科表pv_adt: 置空date_end pk_emp_end name_emp_end flag_dis
			DataBaseHelper
					.update("update pv_adt set date_end = null, pk_emp_end = null, name_emp_end = null, flag_dis = '0', modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end  = (select max(date_end) from pv_adt where pk_pv =:pkPv)",
							mapParam);

			if (outParam.getBedNo().equals(encounter.getBedNo())) {// 如果还是原来的床位，置为出院前状态；如果是新的床位，需要插入新的就诊床位记录
				// 更新就诊床位记录表pv_bed: 置空date_end pk_emp_end name_emp_end
				DataBaseHelper
						.update("update pv_bed set date_end = null, pk_emp_end = null, name_emp_end = null, modifier =:modifier, ts =:ts where pk_pv =:pkPv and date_end  = (select max(date_end) from pv_bed where pk_pv =:pkPv)",
								mapParam);
			} else {
				PvBed bed = new PvBed();
				bed.setPkPv(pkPv);
				bed.setPkDept(encounter.getPkDept());
				bed.setPkDeptNs(encounter.getPkDeptNs());
				bed.setBedno(outParam.getBedNo());
				bed.setDateBegin(new Date());
				bed.setPkBedWard(outParam.getPkDeptNs());
				bed.setEuHold("0");
				bed.setPkEmpBegin(u.getPkEmp());
				bed.setNameEmpBegin(u.getNameEmp());
				bed.setFlagMaj("1"); // 主床位
				DataBaseHelper.insertBean(bed);
			}

			// 处理床位表bd_res_bed
			DataBaseHelper
					.update("update bd_res_bed set eu_status = '02', flag_ocupy = '1', pk_pi =:pkPi, modifier =:modifier, ts =:ts where pk_ward =:pkWard and code =:bedNo",
							mapParam);
			platformTransactionManager.commit(status); // 提交事务
		} catch (Exception e) {
			platformTransactionManager.rollback(status); // 添加失败 回滚事务；
			e.printStackTrace();
			throw new RuntimeException();
			// throw new BusException("保存患者信息失败：" + e.getMessage());
		}
		// 发送取消出院信息至平台
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkEmp", UserContext.getUser().getPkEmp());
		paramMap.put("nameEmp", UserContext.getUser().getNameEmp());
		paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
		paramMap.put("pkPv", paramMap.get("pkPv"));
		paramMap.put("status", "update");
		PlatFormSendUtils.sendPvCancelOutMsg(paramMap);
		paramMap = null;

	}

	/***
	 * 交易号：003004001011<br>
	 * 查询入院登记时是否收取预交金
	 *
	 * @param param
	 * @param user
	 * @return Map<String,String>
	 * @throws
	 *
	 * @author wangpeng
	 * @date 2016年12月28日
	 */
	public Map<String, String> getIsPrepaid(String param, IUser user) {
		Map<String, String> map = new HashMap<String, String>();
		String val = ApplicationUtils.getSysparam(SysConstant.SYS_PARAM_PV0002,
				true);// 入院登记时是否收取预交金
		map.put("isPrepaid", val);
		return map;
	}

	public String getBedsByWardnum(String param, IUser user) {
		String pkWard = JSON.parseObject(param).getString("pkWard");
		Integer count = 0;
		count = DataBaseHelper
				.queryForScalar(
						"select count(1) from bd_res_bed bed where bed.del_flag = '0' and bed.flag_active = '1' and bed.flag_ocupy = '0' and bed.eu_status = '01' and bed.dt_bedtype <> '9' and bed.pk_ward = ?",
						Integer.class, pkWard);

		String deptParam = ApplicationUtils.getDeptSysparam("PV0021", pkWard);
		String num = "0";
		String wardnum = "0";
		if (deptParam != null && deptParam != "") {
			num = deptParam;
		}
		if (count < 1 && "1".equals(num)) {
			wardnum = "1";
		} else {
			wardnum = count.toString();
		}
		return wardnum;
	}

	/**
	 * 获取患者欠费金额
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Double getAmountOwed(String param, IUser user) {
		Double remain = 0.00;
		String pkPi = JsonUtil.getFieldValue(param, "pkPi");
		remain = getRemain(pkPi);
		return remain;
	}

	/**
	 * 获取当前的余额
	 * 
	 * @param pk_pv
	 * @param pk_hp
	 * @return
	 * @throws BusinessException
	 */
	public Double getRemain(String pkPi) throws BusException {
		if (pkPi == null || "".equals(pkPi))
			throw new BusException("患者就诊主键为空，无法获取患者余额！");
		Double fee = getRemainFee(pkPi);

		if (fee.doubleValue() > 0)
			return fee;
		else
			return 0D;
	}

	/**
	 * 获取担保金
	 * 
	 * @param pk_pv
	 * @return
	 */
	public Double getAccFee(String pkPi) {
		if (pkPi == null || "".equals(pkPi))
			throw new BusException("患者主键为空，无法获取患者余额！");
		String sql = "select sum(amt_credit) as num from pv_ip_acc where pk_pi = ? and flag_canc = '0'";
		Map<String, Object> map = DataBaseHelper.queryForMap(sql,
				new Object[] { pkPi });
		if (map != null) {
			return CommonUtils.getDouble(map.get("num"));
		}
		return 0.00;
	}

	public Double getRemainFee(String pkPi) {
		BigDecimal yjj = BigDecimal.ZERO;
		BigDecimal zfy = BigDecimal.ZERO;
		BigDecimal ye = BigDecimal.ZERO;
		zfy = processNull(adtRegMapper.getTotalFee(pkPi));
		yjj = processNull(adtRegMapper.getYjFee(pkPi));
		if (zfy.doubleValue() > 0) {
			ye = yjj.subtract(zfy);
			if (ye.doubleValue() < 0) {
				ye = ye.multiply(new BigDecimal(-1));
			}else
			{
				ye = BigDecimal.ZERO;
			}
		}
		return ye.doubleValue();
	}

	/**
	 * 处理空值
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unused")
	private BigDecimal processNull(BigDecimal args) {
		if (args == null) {
			args = new BigDecimal(0);
		}
		return args;
	}
	
	/**
	 * 获取指定code的患者类别
	 * 交易码：003004001041
	 * @param param
	 * @param user
	 * @return
	 */
	public PiCate searchPicateByCode(String param, IUser user){
		String code = JsonUtil.getFieldValue(param, "code");
		PiCate cate = null;
		if(!CommonUtils.isEmptyString(code))
		{
			
			cate = DataBaseHelper.queryForBean(
					"select * from pi_cate where DEL_FLAG = '0' and CODE = ?",
					PiCate.class, new Object[]{code});
		}
		return cate;
	}


	/**
	 * 获取婴儿母亲的就诊信息
	 * 交易码：003004001044
	 * @param param
	 * @param user
	 * @return
	 */
	public PvEncounter getMomPvInfo(String param, IUser user){
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		PvEncounter pvEncounter = new PvEncounter();
		if(!CommonUtils.isEmptyString(pkPv))
		{
			String queSql = "select * from PV_ENCOUNTER where pk_pv = (select pk_pv from PV_INFANT where PK_PV_INFANT = ?)";
			pvEncounter = DataBaseHelper.queryForBean(queSql,PvEncounter.class,pkPv);
		}
		return pvEncounter;
	}
}
