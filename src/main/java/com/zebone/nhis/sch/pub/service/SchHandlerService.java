package com.zebone.nhis.sch.pub.service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.sch.pub.SchModlog;
import com.zebone.nhis.common.support.ApplicationUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotSec;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;
import com.zebone.nhis.common.module.sch.pub.SchTicketrules;
import com.zebone.nhis.sch.pub.support.TypeUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SchHandlerService {

	@Autowired
	private BaseCodeMapper codeMapper;

	
	/**
	 * 如果为true,计划时长则是该日期分组类型的日期分组(也称作午别)中所有工作时间的总和
	 * 如果为false,计划时长则是该日期分组类型选中的日期分组(也称作午别)中的工作时间(单一)
	 */
	private static final boolean USEALLJHSC = false;

	/** 标志传入的是计划主键 */
	private static final int PK_TYPE_SCH_PLAN = 1;
	/** 标志传入的是排班的主键 */
	private static final int PK_TYPE_SCH_SCH = 2;

	/**
	 * 查询计划计算出的实际时长等信息
	 * 交易号：009001001015
	 * @param param
	 *            {"dtDateslottype":"分组类型","pkPlanweek":"周计划主键","pkSch":"主键",
	 *            "pkType":主键类型1:计划的，2：排班的}
	 * @param user
	 * @return {"actualTime":实际时长,"planTime":计划时长,"cntStd":指导号数}
	 */
	public Map<String, Object> getOperInfo(String param, IUser user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String dtDateslottype = MapUtils.getString(paramMap, "dtDateslottype");
		String pkPlanweek = MapUtils.getString(paramMap, "pkPlanweek");
		if(StringUtils.isBlank(pkPlanweek))
			pkPlanweek = MapUtils.getString(paramMap, "pkPlanWeek");
		String pkSch = MapUtils.getString(paramMap, "pkSch");
		Integer pkType = MapUtils.getInteger(paramMap, "pkType");

		if (StringUtils.isBlank(pkSch) || pkType == null || StringUtils.isBlank(dtDateslottype) || StringUtils.isBlank(pkPlanweek)
				|| !Arrays.asList(PK_TYPE_SCH_PLAN, PK_TYPE_SCH_SCH).contains(pkType)) {
			throw new BusException("传入的参数不正确！");
		}

		StringBuilder sql = new StringBuilder();
		if (pkType == PK_TYPE_SCH_PLAN) {
			sql.append(" SELECT plan.PK_SCHRES,pw.CNT_TOTAL,plan.MINUTE_PER,pw.PK_DATESLOT FROM SCH_PLAN plan LEFT JOIN SCH_PLAN_WEEK pw ON plan.PK_SCHPLAN = pw.PK_SCHPLAN WHERE plan.PK_SCHPLAN = ?");
		} else {
			sql.append(" SELECT plan.PK_SCHRES,sch.CNT_TOTAL,sch.MINUTE_PER,pw.PK_DATESLOT FROM  SCH_SCH sch LEFT JOIN SCH_PLAN plan on sch.PK_SCHPLAN = plan.PK_SCHPLAN LEFT JOIN SCH_PLAN_WEEK pw ON plan.PK_SCHPLAN = pw.PK_SCHPLAN  WHERE sch.PK_SCH = ?");
		}
		sql.append(" AND plan.DT_DATESLOTTYPE = ? AND pw.PK_PLANWEEK = ? ");
		Map<String, Object> rsMap = DataBaseHelper.queryForMap(sql.toString(), new Object[] { pkSch, dtDateslottype, pkPlanweek });
		Map<String, Object> result = new HashMap<String, Object>();
		if (MapUtils.isNotEmpty(rsMap)) {
			String pkDateslot = MapUtils.getString(rsMap, "pkDateslot");
			result.put("actualTime", MapUtils.getInteger(rsMap, "cntTotal", 0) * MapUtils.getInteger(rsMap, "minutePer", 0));
			Map<String, String> params = new HashMap<String, String>();
			params.put("pkDateslot", pkDateslot);
			params.put("pkSchres", MapUtils.getString(rsMap, "pkSchres"));
			result.put("cntStd",  codeMapper.findCntStd(params));
			if (StringUtils.isNotBlank(dtDateslottype) && StringUtils.isNotBlank(pkDateslot)) {
				result.put("planTime", getPlanTime(dtDateslottype, pkDateslot));
			}
		}
		
		return result;
	}

	public int getPlanTime(String dtDateslottype, String pkDateslot) {
		int jhsc = 0;
		Date now = new Date();
		if (USEALLJHSC) {
			// 通过dtDateslottype查询出该日期分组类型的日期分组
			List<BdCodeDateslot> bdCodeDateslots = codeMapper.listByDtDateslottype(dtDateslottype);

			for (BdCodeDateslot bdCodeDateslot : bdCodeDateslots) {
				jhsc += getEffectivePlanDuration(bdCodeDateslot.getPkDateslot(), now);
			}
		} else {
			jhsc += getEffectivePlanDuration(pkDateslot, now);
		}
		return jhsc;
	}

	/**
	 * 获取日期分组有效时间内的计划时长
	 * 
	 * @param pkDateslot
	 *            时间分组主键
	 * @param dateWork
	 *            工作日期
	 * @return
	 */
	public int getEffectivePlanDuration(String pkDateslot, Date dateWork) {
		DateTime dt = new DateTime(dateWork);
		String dateWorkStr = dt.toString("MM-dd");
		BdCodeDateslotTime bdCodeDateslotTime = DataBaseHelper.queryForBean(
				"select *  from bd_code_dateslot_time where del_flag = '0' " + "and pk_dateslot = ? "
						+ "and lpad(valid_month_begin,2,'0') || '-' || lpad(valid_day_begin,2,'0') <= ? "
						+ "and lpad(valid_month_end,2,'0') || '-' || lpad(valid_day_end,2,'0') >= ? ",
				BdCodeDateslotTime.class, pkDateslot, dateWorkStr, dateWorkStr);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("HH:mm:ss");
		int jhsc = 0;
		if (bdCodeDateslotTime != null) {
			DateTime dt_begin = DateTime.parse(bdCodeDateslotTime.getTimeBegin(), dtf);
			DateTime dt_end = DateTime.parse(bdCodeDateslotTime.getTimeEnd(), dtf);
			jhsc = Minutes.minutesBetween(dt_begin, dt_end).getMinutes();
		}
		return jhsc;
	}
	
	public String getCurrentOrgAreaPk(User user){
		if(user == null || StringUtils.isBlank(user.getPkOrg()) || StringUtils.isBlank(user.getPkDept()) )
			return null;
		String sql = "select pk_orgarea from bd_ou_dept dept where dept.pk_org = ? and dept.pk_dept =?  and dept.del_flag = '0' ";
		Map<String, Object> result = DataBaseHelper.queryForMap(sql, new Object[]{user.getPkOrg(), user.getPkDept()});
		return MapUtils.getString(result, "pkOrgarea");
	}

	/**
	 * 获取指定日期分组下的时间段
	 * @param pkDateslot
	 * @return
	 */
	public List<BdCodeDateslotSec> getDateSlotTime(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkDateslot = MapUtils.getString(paramMap, "pkDateslot");
		if(StringUtils.isBlank(pkDateslot)) {
			throw new BusException("未传入日期分组主键！");
		}
		return DataBaseHelper.queryForList("select t.* from bd_code_dateslot_sec t where t.PK_DATESLOT=? order by t.sortno",
				BdCodeDateslotSec.class,pkDateslot);
	}
	
	/***
	 * 依据日期分组和资源，查询指导号数
	 * @param param
	 * @param user
	 * @return
	 */
	public Integer getCntStd(String param, IUser user) {
		Map<String, String> params = JsonUtil.readValue(param, new TypeReference<Map<String,String>>() {});
		if(MapUtils.isEmpty(params)
				||StringUtils.isBlank(params.get("pkDateslot")) ||StringUtils.isBlank(params.get("pkSchres"))) {
			throw new BusException("未传入日期分组主键或者排班资源主键！");
		}
		return codeMapper.findCntStd(params);
	}
	
	/***
	 * 获得可预约号
	 * @param cntTotal
	 * @param pkTicketrules
	 * @return
	 */
	public List<Integer> getAppointmentNos(int cntTotal,String pkTicketrules) {
		SchTicketrules schTicketrules = DataBaseHelper.queryForBean("select * from sch_ticketrules where pk_ticketrules = ?", 
				SchTicketrules.class, new Object[]{pkTicketrules});
		int euRuletype;
		if(schTicketrules == null){
			euRuletype = TypeUtils.euRuleType_all;
		}else{
			euRuletype = Integer.valueOf(schTicketrules.getEuRuletype());
		}
		List<Integer> ticketNos = Lists.newArrayList();
		switch (euRuletype) {
		case TypeUtils.euRuleType_all:
			for (int i = 1; i <= cntTotal; i++) {
				ticketNos.add(i);
			}
			break;
		case TypeUtils.euRuleType_allnot:
			break;
		case TypeUtils.euRuleType_even:
			for (int i = 1; i <= cntTotal; i++) {
				if(i % 2 == 0){
					ticketNos.add(i);
				}
			}
			break;
		case TypeUtils.euRuleType_odd:
			for (int i = 1; i <= cntTotal; i++) {
				if(i % 2 != 0){
					ticketNos.add(i);
				}
			}
			break;
		case TypeUtils.euRuleType_segment:
			int begin = schTicketrules.getBeginNo();
			int end = schTicketrules.getEndNo();
			for (int i = 1; i <= cntTotal; i++) {
				if(i >= begin && i <= end){
					ticketNos.add(i);
				}
			}
			break;
		case TypeUtils.euRuleType_enumerate:
			List<String> strTickets = Splitter.on(",").trimResults().splitToList(schTicketrules.getTickets()); 
			Function<String, Integer> strToIntFun = new Function<String, Integer>() {
				@Override
				public Integer apply(String arg0) {
		
					return Integer.parseInt(arg0);
				}
			};
			List<Integer> intTickets = Lists.transform(strTickets, strToIntFun);
			Integer min = Ordering.natural().min(intTickets);
			Integer max = Ordering.natural().max(intTickets);
			for (int i = 1; i <= cntTotal; i++) {
				if(i >= min && i <= max){
					ticketNos.add(i);
				}
			}
			break;
		case TypeUtils.euRuleType_func:
			break;
		default:
			break;
		}
		return ticketNos;
	}
	
	/*
	 * 查询资源是否存在树形结构,从子节点查询
	 * 修改中二的诊疗资源维护 兼容SqlServer语法
	 */
	public List<Map<String, Integer>> getCount(String param, IUser user) {
		String params = JSON.parseObject(param).getString("pkSchres");
		List<Map<String, Integer>> count = new TreeList();
		Map<String, Integer> map = new HashMap<>();
		if(Application.isSqlServer()){
			int schCount =  DataBaseHelper.queryForScalar("select ct count from fn_stat_schresource(?)", Integer.class, new Object[]{params});
			map.put("count", schCount);
			count.add(map);
		}else{
			int schCount=  DataBaseHelper.queryForScalar("select count(*) count from SCH_RESOURCE start with PK_SCHRES =? connect by prior PK_SCHRES = PK_FATHER", Integer.class, new Object[]{params});
			map.put("count", schCount);
			count.add(map);
		}
	    return count;
	}

	/**
	 * 保存排班修改记录
	 *
	 * @param schModlogs
	 * @return
	 */
	public void saveSchModLogBatch(List<SchModlog> schModlogs) {
		if (schModlogs == null ||schModlogs.size()==0) {
			return;
		}
		for(SchModlog schModlog:schModlogs){
			ApplicationUtils.setDefaultValue(schModlog, true);
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(SchModlog.class),schModlogs);

	}
	
	
}
