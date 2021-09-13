package com.zebone.nhis.sch.appt.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.*;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotSec;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.mybatis.MyBatisPage;
import com.zebone.nhis.common.module.pi.PiLock;
import com.zebone.nhis.common.module.pi.PiLockDt;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptApply;
import com.zebone.nhis.common.module.sch.appt.SchApptOrd;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.service.CnPiPubService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsrm.sch.vo.PiBlackVo;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.sch.appt.dao.DepartmentInformationMapper;
import com.zebone.nhis.sch.appt.dao.InquireChargesVoMapper;
import com.zebone.nhis.sch.appt.dao.SchApptMapper;
import com.zebone.nhis.sch.appt.dao.SchedulingResourcesMapper;
import com.zebone.nhis.sch.appt.vo.*;
import com.zebone.nhis.sch.plan.dao.SchMapper;
import com.zebone.nhis.sch.plan.vo.SchInfoVo;
import com.zebone.nhis.sch.plan.vo.SchSchVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 排班预约
 * 
 * @author
 *
 */
@Service
public class SchApptService {

	@Autowired
	private SchApptMapper schApptMapper;

	@Autowired
	private SchMapper schMapper;

	@Autowired
	private BaseCodeMapper codeMapper;
	
	@Autowired
	private InquireChargesVoMapper iChargesVoMapper;
	
	@Autowired
	private DepartmentInformationMapper dInformationMapper;
	
	@Autowired
	private SchedulingResourcesMapper sResourcesMapper;

	@Autowired
    private TicketPubService ticketPubService;
	
	public Page<SchSch> getSch(String param, IUser user){
	    MyBatisPage.startPage(1, 10);
	    List<SchSch> list = schMapper.listAll();
	    Page<SchSch> page = MyBatisPage.getPage();
	    page.setRows(list);
	    return page;
	}
	/**
	 * 查询收费项目（分页）
	 * @param param
	 * @param user
	 * @return
	 */
	public Page<InquireChargesVo> inquireCharges(String param, IUser user){
		InquireChargesVo inquireChargesVo = JsonUtil.readValue(param, InquireChargesVo.class);
		MyBatisPage.startPage(inquireChargesVo.getPageNum(), inquireChargesVo.getPageSize());		
		String spcode =inquireChargesVo.getSpcode();
		System.out.println(spcode);
		List<InquireChargesVo> list = iChargesVoMapper.getInquireChargesVo(spcode);
		Page<InquireChargesVo> page = MyBatisPage.getPage();
		page.setRows(list);
		return page;
		
		
	}
	

	/**
	 * 保存预约登记信息
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public SchApptApply saveSchApptApply(String param, IUser user) {
		User u = (User) user;
		SchApptApply schApptApply = JsonUtil.readValue(param, SchApptApply.class);
		if(StringUtils.isBlank(schApptApply.getPkSchapply())){ //只对新增校验，对修改不校验
			
			List<SchApptApply> applyList = DataBaseHelper.queryForList("select * from SCH_APPT_APPLY where pk_pi = ? "
					+ "and date_begin = ? and date_end = ? and pk_schres = ? and pk_schsrv = ? and pk_dateslot = ?",SchApptApply.class, schApptApply.getPkPi(),schApptApply.getDateBegin(),
					schApptApply.getDateEnd(),schApptApply.getPkSchres(),schApptApply.getPkSchsrv(),schApptApply.getPkDateslot());
			if(applyList.size() > 0){
				throw new BusException("该患者在同时段内已对相同资源服务进行预约登记");
			}
		}
		schApptApply.setNameEmpReg(u.getNameEmp());
		schApptApply.setPkEmpReg(u.getPkEmp());
		schApptApply.setDateReg(new Date());
		int count_pkApply = DataBaseHelper.queryForScalar(
				"select count(1) from SCH_APPT_APPLY where pk_schapply=?", Integer.class,
				schApptApply.getPkSchapply());
		if (count_pkApply == 0){
			DataBaseHelper.insertBean(schApptApply);
		}else {
			DataBaseHelper.updateBeanByPk(schApptApply, false);
		}
		return schApptApply;
	}

	/**
	 * 查询预约登记记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<SchApptApplyVo> searchSchApptApply(String param, IUser user) {
		Map params = JsonUtil.readValue(param, Map.class);
		return schApptMapper.searchSchApptApply(params);
	}

	/**
	 * 取消预约登记
	 * 
	 * @param param
	 * @param user
	 */
	public void cancelApply(String param, IUser user) {
		String pkSchapply = JSON.parseObject(param).getString("pkSchapply");
		User u = (User) user;
		SchApptApply schApptApply = new SchApptApply();
		schApptApply.setPkSchapply(pkSchapply);
		schApptApply.setFlagCancel("1");
		schApptApply.setEuStatus("9");
		schApptApply.setPkEmpCancel(u.getPkEmp());
		schApptApply.setNameEmpCancel(u.getNameEmp());
		schApptApply.setDateCancel(new Date());
		DataBaseHelper.updateBeanByPk(schApptApply, false);
	}

	/**
	 * 预约挂号查询-取消预约
	 * @param param
	 * @param user
	 */
	public void cancelAppt(String param, IUser user) {

		String pkSchappt = JSON.parseObject(param).getString("pkSchappt");
		if(pkSchappt == null){
			throw new BusException("请在挂号处退号！");
		}
		User u = (User) user;
		SchAppt schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and pk_schappt = ?",
				SchAppt.class, pkSchappt);
		if("9".equals(schAppt.getEuStatus())){
			throw new BusException("该患者已经取消预约");
		}
		if("1".equals(schAppt.getEuStatus())){
			throw new BusException("该患者已经就诊过了");
		}
		DateTime dateappt = new DateTime(schAppt.getDateAppt());
		DateTime now = DateTime.parse(DateTime.now().toString("yyyy-MM-dd"));
		if(Days.daysBetween(dateappt, now).getDays() > 0){
			throw new BusException("该时间不能取消预约");
		}
		DataBaseHelper.update(
				"update sch_appt set flag_cancel = '1',eu_status = '9',pk_emp_cancel = ?,name_emp_cancel = ?,date_cancel = sysdate "
						+ "where pk_schappt = ? and eu_status = '0'",
				new Object[] { u.getPkEmp(), u.getNameEmp(), pkSchappt });
		modSchAndTicket(schAppt.getPkSch(), schAppt.getTicketNo());

		List<String> pkCnords = DataBaseHelper.queryForList("select pk_cnord from sch_appt_ord where pk_schappt = ?",
				String.class, new Object[] { pkSchappt });
		if (pkCnords.size() > 0) {
			String pkCnord = pkCnords.get(0);
			// 更新检查申请单
			DataBaseHelper.update("update cn_ris_apply set eu_status = '1' where pk_cnord = ?",
					new Object[] { pkCnord });
			// 更新病理申请单
			DataBaseHelper.update("update cn_pa_apply set eu_status = '1' where pk_cnord = ?",
					new Object[] { pkCnord });
		}
		Map<String,Object> msgParam =  new HashMap<String,Object>();
		msgParam.put("pkSchappt", pkSchappt);
		msgParam.put("note","HIS-手动取消");
        msgParam.put("isWeChat", "1");
		msgParam.put("isAdd", "0");
        PlatFormSendUtils.sendSchApptReg(msgParam);
	}

	private void modSchAndTicket(String pkSch, String ticketNo){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("pkSch", pkSch);
		paramMap.put("ticketno", ticketNo);
		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - " +
						" nvl((select count(1) from SCH_TICKET t1 left join SCH_TICKET t2 on t1.PK_SCHTICKET=t2.PK_SCHTICKET_MAIN and t2.PK_SCH=:pkSch where t1.PK_SCH=:pkSch and t1.TICKETNO=:ticketno and t1.FLAG_USED='1'),0) where pk_sch = :pkSch"
				, paramMap);
		DataBaseHelper.update("update SCH_TICKET  set FLAG_USED='0',PK_SCHTICKET_MAIN=null " +
				"where (PK_SCHTICKET_MAIN in(select PK_SCHTICKET from SCH_TICKET where TICKETNO=:ticketno and PK_SCH=:pkSch) " +
				"  and PK_SCHTICKET_MAIN is not null) " +
				"      or (TICKETNO=:ticketno and PK_SCH=:pkSch)",paramMap);
	}
	/**
	 * 预约挂号查询-查询预约挂号记录
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Page<Map<String, Object>> searchSchAppt(String param, IUser user) {
		User u =(User)user;
		String pkOrg = u.getPkOrg();
		Map params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		MyBatisPage.startPage(MapUtils.getIntValue(params,"pageIndex",1), MapUtils.getIntValue(params,"pageSize",20));
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		params.put("ftByUser", schAuthority);
		params.put("pkDeptbus",u.getPkDept());
		if(CommonUtils.isNull(params.get("flagDept"))){
			params.put("flagDept","0");
		}
		List<Map<String, Object>> schApptlist = schApptMapper.searchSchAppt(params);
		Page<Map<String, Object>> page = MyBatisPage.getPage();
		page.setRows(schApptlist);
		return page;
	}

	public List<CheckApplyVo> checkApply(String param, IUser user) {
		User u = ((User) user);
		
		User u1=UserContext.getUser();
		String pkOrg=u1.getPkOrg();
		String pkDept = u.getPkDept();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkDept", pkDept);
		paramMap.put("pkOrg", pkOrg); 
		paramMap.get(pkOrg);
		
		//paramMap.put("pkOrg", u.getPkOrg());
		List<CheckApplyVo> list = schApptMapper.checkApply(paramMap);
		for (int i = list.size() - 1; i >= 0; i--) {
			CheckApplyVo vo = list.get(i);
			if (vo.getPkOrdris() == null && vo.getPkOrdpe() == null) {
				list.remove(i);
			}
		}
		return list;
	}

	public List<ApplyCheckInfo> getApplyCheckInfo(String param, IUser user) {
		String pkOrg = ((User) user).getPkOrg();
		Map paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", pkOrg);
		return schApptMapper.getApplyCheckInfo(paramMap);
	}

	public void updateSchApptStatus(String param, IUser user) {
		User u = (User) user;
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkSchappt = (String) paramMap.get("pkSchappt");
		String euStatus = (String) paramMap.get("euStatus");

		SchAppt schAppt = new SchAppt();
		schAppt.setPkSchappt(pkSchappt);
		schAppt.setEuStatus(euStatus);
		if ("9".equals(euStatus)) {
			schAppt.setFlagCancel("1");
			schAppt.setDateCancel(new Date());
			schAppt.setPkEmpCancel(u.getPkEmp());
			schAppt.setNameEmpCancel(u.getNameEmp());
		}
		DataBaseHelper.updateBeanByPk(schAppt, false);

		if ("9".equals(euStatus)) {
			schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and pk_schappt = ?",
					SchAppt.class, pkSchappt);
			DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?",
					new Object[] { schAppt.getPkSch() });
			DataBaseHelper.update("update sch_ticket set flag_used = '0' where pk_sch = ? and ticketno = ?",
					new Object[] { schAppt.getPkSch(), schAppt.getTicketNo() });
			Map<String,Object> pkCnord = DataBaseHelper.queryForMap("select pk_cnord from sch_appt_ord where pk_schappt = ?",
					schAppt.getPkSchappt());
			// 更新检查申请单
			DataBaseHelper.update("update cn_ris_apply set eu_status = '1' where pk_cnord = ?",
					new Object[] { pkCnord.get("pkCnord")==null?"":pkCnord.get("pkCnord").toString() });
			// 更新病理申请单
			DataBaseHelper.update("update cn_pa_apply set eu_status = '1' where pk_cnord = ?",
					new Object[] { pkCnord.get("pkCnord")==null?"":pkCnord.get("pkCnord").toString() });
		}

	}

	/**
	 * 获取排班信息
	 * 
	 * @return
	 */
	public Page<SchInfoVo> getSchInfo(String param, IUser user) {
		String pkOrg = ((User) user).getPkOrg();
		Map<String, Object> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		params.put("flagStop","0");
		params.put("euStatus","8");
		params.put("isSqlServer",String.valueOf(Application.isSqlServer()));

		//由于有分页，先按照下面分组条件查询分页结果》》再次依据主键查询结果集，保证分页正确
		MyBatisPage.startPage(MapUtils.getIntValue(params, "pageIndex"), MapUtils.getIntValue(params, "pageSize"));
		
		//传递过来的是诊区,按诊区查询（只有菜单参数flagRole=1 SCH0018才起作用）
		String flagRole = CommonUtils.getPropValueStr(params,"flagRole");
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		if(!EnumerateParameter.ONE.equals(flagRole) && !EnumerateParameter.THREE.equals(schAuthority)) {
			params.remove("pkDeptArea");
		}
		List<String> listGroup = schMapper.getSchOfApptGroup(params);

		Page<SchInfoVo> page = MyBatisPage.getPage();
		List<SchInfoVo> infoList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(listGroup)) {
			Set<String> pkSchs = new HashSet<>();
			listGroup.stream().forEach(s -> {
				pkSchs.addAll(Arrays.asList(s.split(",")));
			});
			listGroup.clear();
			params.clear();
			params.put("pkSchs", pkSchs);
			List<SchSchVo> list = schMapper.getSchOfApptInfo(params);

			/** list分组 */
			// 根据pkSchres:schresName:pkSchsrv:schsrvName 对list进行分组
			ImmutableList<SchSchVo> digits = ImmutableList.copyOf(list);
			// 分组方法，pkSchres:schresName:pkSchsrv:schsrvName一致的为一组
			Function<SchSchVo, String> group = schSchVo -> schSchVo.getPkSchres() + ":" + schSchVo.getPkSchsrv() + ":" + schSchVo.getPkEmp();
			// 执行分组方法
			ImmutableListMultimap<String, SchSchVo> groupMap = Multimaps.index(digits, group);

			//查询日期分组
			List<BdCodeDateslot> dateslotList = DataBaseHelper.queryForList("select * from BD_CODE_DATESLOT where flag_active='0'", BdCodeDateslot.class);
			Map<String, String> pkTotype = dateslotList.parallelStream().collect(Collectors.toMap(BdCodeDateslot::getPkDateslot, BdCodeDateslot::getDtDateslottype));
			Map<String, List<BdCodeDateslot>> typeToMap = dateslotList.parallelStream().collect(Collectors.groupingBy(BdCodeDateslot::getDtDateslottype));

			//查询实时可预约号数量
			Map<String, SchApptCntVo> apptCount = getCntAppt(pkSchs);
			for (String key : groupMap.keySet()) {
				String[] arr = key.split(":");
				SchInfoVo infoVo = new SchInfoVo(arr[0], arr[1]);
				List<SchSchVo> schVos = groupMap.get(key);
				Set<String> pkDateslots = Sets.newHashSet();
				Set<String> soltTypes = new HashSet<String>();
				List<BdCodeDateslot> bdCodeDateslotList = new ArrayList<BdCodeDateslot>();
				for (SchSchVo schSchVo : schVos) {
					SchApptCntVo cntVo = apptCount.get(schSchVo.getPkSch());
					schSchVo.setFlagTicket(cntVo!=null ? "1" : "0");
					schSchVo.setCntAppt(cntVo!=null ? cntVo.getCntAppt():0);
					schSchVo.setApptType(cntVo!=null ? cntVo.getCntApptOut():0);
					schSchVo.setCntApptIn(cntVo!=null ? cntVo.getCntApptIn():0);
					String soltType = pkTotype.get(schSchVo.getPkDateslot());
					if(!soltTypes.contains(soltType)) {
						bdCodeDateslotList.addAll(soltType==null?null:typeToMap.get(soltType));
						soltTypes.add(soltType);
					}
				}
				SchSchVo schVoFirst = schVos.get(0);
				infoVo.setSchresName(schVoFirst.getSchresName());
				infoVo.setSchsrvName(schVoFirst.getSchsrvName());
				infoVo.setDoctorName(schVoFirst.getDoctorName());
				//String soltType = pkTotype.get(schVoFirst.getPkDateslot());
				//infoVo.setBdCodeDateslots(soltType==null?null:typeToMap.get(soltType));
				infoVo.setBdCodeDateslots(bdCodeDateslotList);
				infoVo.setSchschs(schVos);
				infoVo.setPkDateslots(pkDateslots);
				infoList.add(infoVo);
			}
		}
		page.setRows(infoList);
		return page;
	}

	public Map<String, SchApptCntVo> getCntAppt(Set<String> pkSchs){
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("date", DateUtils.getDateTime());
		paramMap.put("pkSchs", pkSchs);
		List<SchApptCntVo> apptCntVoList = schApptMapper.getApptStatCount(paramMap);
		return apptCntVoList.stream().collect(Collectors.toMap(SchApptCntVo::getPkSch, vo -> vo));
	}

	/**
	 * 获取排班号表信息
	 *
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getSchTicketInfo(String param, IUser user) {
		Map params = JsonUtil.readValue(param, Map.class);
		String pkSch = (String) params.get("pkSch");
		String pkSchsrv = (String) params.get("pkSchsrv");
		String dateWork = (String) params.get("dateWork");
		String pkSchres = (String) params.get("pkSchres");
		String pkDateslot = (String) params.get("pkDateslot");
		String dtApptype = MapUtils.getString(params,"dtApptype");
		SchSch schSch = null;
		if (pkSch != null) {
			schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? ",
					SchSch.class, new Object[] { pkSch });
			pkDateslot = schSch.getPkDateslot();
		} else {
			try {  //查询未排班的日期会报错
				schSch = DataBaseHelper.queryForBean(
						"select * from sch_sch where del_flag = '0' and pk_schsrv = ? and date_work = to_date(?,'yyyy-mm-dd')  and pk_schres = ? and pk_dateslot = ?",
						SchSch.class, new Object[] { pkSchsrv, dateWork, pkSchres, pkDateslot });
				pkSch = schSch.getPkSch();
			} catch (Exception e) {
				throw new BusException("未查询到该日期的排班！");
			}
		}
//		List<BdCodeDateslotSec> dateslotSecs = DataBaseHelper.queryForList(
//				"select * from BD_CODE_DATESLOT_SEC where pk_dateslot = ? order by sortno", BdCodeDateslotSec.class,
//				new Object[] { pkDateslot });
		List<SchTicket> schTickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where del_flag = '0' and pk_sch = ? "+(StringUtils.isNotBlank(dtApptype)?" and DT_APPTYPE = ?":""+" order by begin_time,end_time"), SchTicket.class,
				(StringUtils.isNotBlank(dtApptype)?new Object[] { pkSch,dtApptype }:new Object[] { pkSch}));
		DateTime begin = getSchBeginTime(pkDateslot, schSch.getDateWork());
		/** list分组 */
		// 根据beginTime,endTime 对list进行分组
		ImmutableList<SchTicket> digits = ImmutableList.copyOf(schTickets);
		// 分组方法，beginTime-endTime一致的为一组
		Function<SchTicket, String> group = tk -> tk.getBeginTime() + "-" + tk.getEndTime();
		// 执行分组方法
		ImmutableListMultimap<String, SchTicket> groupMap = Multimaps.index(digits, group);
		Integer sortno = 1;
		List<BdCodeDateslotSec> bdCodeDateslotSecList = Lists.newArrayList();
		for (String key : groupMap.keySet()) {
			BdCodeDateslotSec dateslotSec = new BdCodeDateslotSec();
			dateslotSec.setTimeBegin(DateUtils.dateToStr("HH:mm:ss", groupMap.get(key).get(0).getBeginTime()));
			dateslotSec.setTimeEnd(DateUtils.dateToStr("HH:mm:ss", groupMap.get(key).get(0).getEndTime()));
			dateslotSec.setSortno(sortno++);
			dateslotSec.setPkDateslot(pkDateslot);
			dateslotSec.setSecmin(DateUtils.getMinsBetween(groupMap.get(key).get(0).getBeginTime(), groupMap.get(key).get(0).getEndTime()));
			bdCodeDateslotSecList.add(dateslotSec);
		}

		Map<String, List<SchTicket>> ticketMap = Maps.newTreeMap();
		for (BdCodeDateslotSec dateslotSec : bdCodeDateslotSecList) {
			String sec = dateslotSec.getTimeBegin().substring(0, 5) + "-" + dateslotSec.getTimeEnd().substring(0, 5);

			List<SchTicket> list = Lists.newArrayList();
			for (int i = schTickets.size() - 1; i >= 0; i--) {
				SchTicket ticket = schTickets.get(i);

				String beginTime = DateUtils.dateToStr("HH:mm:ss", ticket.getBeginTime());
				String endTime = DateUtils.dateToStr("HH:mm:ss", ticket.getEndTime());
				String timeBegin = dateslotSec.getTimeBegin();
				String timeEnd = dateslotSec.getTimeEnd();

				if (timeBegin.equals(beginTime) && timeEnd.equals(endTime)) {
					list.add(ticket);
					schTickets.remove(i);
				}
			}
			ticketMap.put(sec, list);
		}

		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("schSch", schSch);
		resultMap.put("schTicketSecs", ticketMap);
		return resultMap;
	}

	/**
	 * 生成预约信息
	 * 
	 * @param param
	 * @param user
	 */
	public SchSch generateSchAppt(String param, IUser user) {
		//TODO 此方法有问题，1.取号未限制时段有效性 2.未关注新加入的渠道 3.未发送预约信息。暂不使用？？
		User u = (User) user;
		Map params = JsonUtil.readValue(param, Map.class);
		String pkSch = (String) params.get("pkSch");
		// String dateslotsec = (String) params.get("dateslotsec");
		String ticketno = (String) params.get("ticketno");
		String flagOccupy = (String) params.get("flagOccupy");
		String descOccupy = (String) params.get("descOccupy");
		String pkCnord = (String) params.get("pkCnord");
		// String type = (String) params.get("type");// 0:排班,1:时间分段,2:号表

		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		CnOrder cnOrder = DataBaseHelper.queryForBean("select * from cn_order where del_flag = '0' and pk_cnord = ?",
				CnOrder.class, pkCnord);
		ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from ex_assist_occ WHERE flag_canc='0' and (flag_refund ='0' or flag_refund is null)  and pk_cnord = ?",
				ExAssistOcc.class, pkCnord);
		SchResource schRes = DataBaseHelper.queryForBean(
				"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ?", SchResource.class,
				schSch.getPkSchres());
		if(exAssistOcc == null){
			throw new BusException("未查询到医技执行单，请确认该申请单是否计费！");
		}

		// 处理号表
		SchTicket schTicket = null;
		// if ("0".equals(type)) {
		// schTicket = DataBaseHelper.queryForBean(
		// "select * from (select * from SCH_TICKET where PK_SCH = ? and
		// DEL_FLAG = '0' and FLAG_APPT = '0' and FLAG_USED = '0') where ROWNUM
		// < 2",
		// SchTicket.class, pkSch);
		// }else if("1".equals(type)){
		// String[] time = dateslotsec.split("~");
		// String date = new
		// DateTime(schSch.getDateWork()).toString("yyyy-MM-dd") + " ";
		// DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd
		// HH:mm");
		// Date begin = DateTime.parse(date + time[0],dtf).toDate();
		// Date end = DateTime.parse(date + time[1],dtf).toDate();
		// schTicket = DataBaseHelper.queryForBean(
		// "select * from (select * from SCH_TICKET where PK_SCH = ? and
		// DEL_FLAG = '0' and FLAG_APPT = '0' and FLAG_USED = '0' and begin_time
		// >= ? and end_time <= ? ) where ROWNUM < 2",
		// SchTicket.class, pkSch, begin,end);
		// }else{
		schTicket = DataBaseHelper.queryForBean(
				"select * from sch_ticket where pk_sch = ? and ticketno = ? and DEL_FLAG = '0' and FLAG_APPT = '1' and FLAG_USED = '0'",
				SchTicket.class, pkSch, ticketno);
		// }
//		if(schTicket==null){
//			throw new BusException("未查询到您选择的编号，请检查该号是否可预约！");
//		}

		SchAppt schAppt = new SchAppt();
		schAppt.setPkSchappt(NHISUUID.getKeyId());
		schAppt.setEuSchclass("1");
		schAppt.setPkSch(pkSch);
		schAppt.setCode(schAppt.getPkSchappt());
		schAppt.setDateAppt(schSch.getDateWork());
		schAppt.setPkDateslot(schSch.getPkDateslot());
		schAppt.setPkSchres(schSch.getPkSchres());
		schAppt.setPkSchsrv(schSch.getPkSchsrv());
		schAppt.setTicketNo(ticketno);
		if (schTicket != null) {
			schAppt.setBeginTime(schTicket.getBeginTime());
			schAppt.setEndTime(schTicket.getEndTime());
		} else {
			schAppt.setBeginTime(schSch.getDateWork());
			schAppt.setEndTime(schSch.getDateWork());
		}
		schAppt.setPkPi(cnOrder.getPkPi());
		schAppt.setDtApptype("00");
		schAppt.setPkDeptEx(schSch.getPkDept());
		schAppt.setPkOrgEx(schSch.getPkOrg());
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(u.getPkDept());
		schAppt.setPkEmpReg(u.getPkEmp());
		schAppt.setNameEmpReg(u.getNameEmp());
		schAppt.setEuStatus("0");
		schAppt.setFlagPay("0");
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		DataBaseHelper.insertBean(schAppt);

		SchApptOrd schApptOrd = new SchApptOrd();
		schApptOrd.setPkSchappt(schAppt.getPkSchappt());
		schApptOrd.setEuPvtype(cnOrder.getEuPvtype());
		schApptOrd.setPkMsp(schRes.getPkMsp());
		schApptOrd.setPkCnord(pkCnord);
		schApptOrd.setFlagNotice("0");
		schApptOrd.setFlagExec("0");
		schApptOrd.setFlagOccupy(flagOccupy);
		schApptOrd.setDescOccupy(descOccupy);
		schApptOrd.setPkAssocc(exAssistOcc.getPkAssocc());
		DataBaseHelper.insertBean(schApptOrd);

		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] { pkSch });
		if (schTicket != null) {
			DataBaseHelper.update("update sch_ticket set flag_used = '1' where pk_schticket = ?",
					new Object[] { schTicket.getPkSchticket() });
		} else {
			DataBaseHelper.update("update sch_sch set ticket_no = nvl(ticket_no, '0') + 1 where pk_sch = ?",
					new Object[] { pkSch });
		}
		// 更新检查申请单
		DataBaseHelper.update("update cn_ris_apply set eu_status = '2' where pk_cnord = ?", new Object[] { pkCnord });
		// 更新病理申请单
		DataBaseHelper.update("update cn_pa_apply set eu_status = '2' where pk_cnord = ?", new Object[] { pkCnord });
		
		return schSch;
	}

	/**
	 * 获取排班号表开始时间
	 * 
	 * @param pkDateslot
	 *            时间分组主键
	 * @param dateWork
	 *            排班的工作日期
	 * @return
	 */
	private DateTime getSchBeginTime(String pkDateslot, Date dateWork) {
		DateTime dt = new DateTime(dateWork);
		String dateWorkStr = dt.toString("MM-dd");
		String yeardate = dt.toString("yyyy-MM-dd");
		
		BdCodeDateslotTime bdCodeDateslotTime = DataBaseHelper.queryForBean(
				"select *  from bd_code_dateslot_time where del_flag = '0' " + "and pk_dateslot = ? "
						+ "and lpad(valid_month_begin,2,'0') || '-' || lpad(valid_day_begin,2,'0') <= ? "
						+ "and lpad(valid_month_end,2,'0') || '-' || lpad(valid_day_end,2,'0') >= ? ",
				BdCodeDateslotTime.class, pkDateslot, dateWorkStr, dateWorkStr);
		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime begin;
		if (bdCodeDateslotTime == null) {
			BdCodeDateslot bdCodeDateslot = DataBaseHelper.queryForBean(
					"select * from bd_code_dateslot where pk_dateslot = ?", BdCodeDateslot.class, pkDateslot);
			if (bdCodeDateslot == null)
				throw new BusException("日期分组不存在");
			begin = DateTime.parse(yeardate + " " + bdCodeDateslot.getTimeBegin(), dtf);
		} else {
			begin = DateTime.parse(yeardate + " " + bdCodeDateslotTime.getTimeBegin(), dtf);
		}
		return begin;
	}
	
	

	public Map<String, Object> getTicketno(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		String pkSch = (String) paramMap.get("pkSch");
		String dateslotSec = (String) paramMap.get("dateslotSec");
		SchTicket  ticket =  new SchTicket();
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? ",
				SchSch.class, new Object[] { pkSch });
		if (schSch == null) {
			throw new BusException("不存在的排班！");
		}
        boolean haveTicketNo = DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
                Integer.class, new Object[]{pkSch})>0;
		if(haveTicketNo){
			Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select time_begin,time_end from bd_code_dateslot_sec where pk_dateslotsec=?", dateslotSec);
            Map<String,Object> ticketParam = new HashMap<>();
            ticketParam.put("pkSch", pkSch);
            ticketParam.put("startTime", MapUtils.getString(slotSecMap,"timeBegin"));
            ticketParam.put("endTime", MapUtils.getString(slotSecMap,"timeEnd"));
			ticketParam.put("lockTicket",false);
			ticketParam.put("dtApptype", MapUtils.getString(paramMap, "dtApptype"));
			ticket = ticketPubService.getUnusedAppTicket(ticketParam);
        } else {
            SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
            ticket.setTicketno(sch.getTicketNo());
            ticket.setPkSch(pkSch);
        }
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("apptTime", ticket.getBeginTime());
		resultMap.put("euPvtype", ticket.getEuPvtype());
		resultMap.put("ticketno", ticket.getTicketno());
		resultMap.put("dtApptype", ticket.getDtApptype());
		return resultMap;
	}
	
	
	public SchSchVo applyRegister(String param, IUser user) {
		User u = (User) user;
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPi = paramMap.get("pkPi");
		String pkSch = paramMap.get("pkSch");
		String ticketNo = paramMap.get("ticketNo");
		String dtApptype = MapUtils.getString(paramMap,"dtApptype");
		String pkApptpv = null;//paramMap.get("pkApptpv");

		SchSchVo schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ? and flag_stop='0'",
				SchSchVo.class, pkSch);
		if(schSch == null){
			throw new BusException("对应排班不存在或者已经停用！");
		}

		int count = DataBaseHelper.queryForScalar("select count(1) from sch_appt "
				+ "where pk_pi = ? and "
				+ "date_appt >= ? and "
				+ "date_appt <= ? and "
				+ "pk_sch = ? and "
				+ "flag_cancel = '0'", Integer.class, new Object[]{pkPi,schSch.getDateWork(),schSch.getDateWork(),pkSch});
		if(count > 0){
			throw new BusException("已经预约过了，不能再次预约；只能预约次日及以后日期的挂号！");
		}
		SchResource schRes = DataBaseHelper.queryForBean(
				"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ?", SchResource.class,
				schSch.getPkSchres());
		PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?",
				PiMaster.class, pkPi);

		//校验如果患者年龄大于儿童年龄限制，不允许挂儿科
		String codeDept = ApplicationUtils.getSysparam("CN0001", false);//儿科科室编码
		String ageMax = ApplicationUtils.getSysparam("PV0019",false);//儿童年龄上限
		//计算患者年龄
		BigDecimal age = CnPiPubService.getPvAge(piMaster.getPkPi());//计算患者年龄
		if(!CommonUtils.isEmptyString(codeDept)&&age.compareTo(new BigDecimal(Integer.parseInt(ageMax==null?"999":ageMax)))>0){
			  BdOuDept childDept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept = ? ", BdOuDept.class,new Object[]{schSch.getPkDept()});
			  if(childDept!=null&&(","+codeDept+",").indexOf(","+childDept.getCodeDept()+",")>=0)
				throw new BusException("儿科不允许大于年龄"+ageMax+"的患者进行预约!");
		}


		SchTicket schTicket = DataBaseHelper.queryForBean(
				"select * from sch_ticket where pk_sch = ? and ticketno = ? and DEL_FLAG = '0' and FLAG_APPT = '1' and FLAG_USED = '0'",
				SchTicket.class, pkSch, ticketNo);
		if (schTicket == null) {
			throw new BusException("该号已被占用，请刷新后重新预约！");
		}

		SchAppt schAppt = new SchAppt();
		if(pkApptpv==null||pkApptpv.equals("")){
			pkApptpv = NHISUUID.getKeyId();
		}
		schAppt.setNoteAppt(MapUtils.getString(paramMap,"noteAppt"));
		schAppt.setPkSchappt(pkApptpv);
		schAppt.setEuSchclass("0");
		schAppt.setPkSch(pkSch);
		schAppt.setCode(ApplicationUtils.getCode("0101"));
		schAppt.setDateAppt(schSch.getDateWork());
		schAppt.setPkDateslot(schSch.getPkDateslot());
		schAppt.setPkSchres(schSch.getPkSchres());
		schAppt.setPkSchsrv(schSch.getPkSchsrv());
		schAppt.setTicketNo(ticketNo);
//		if (schTicket != null) {
		schAppt.setBeginTime(schTicket.getBeginTime());
		schAppt.setEndTime(schTicket.getEndTime());
//		} else {
//			schAppt.setBeginTime(schSch.getDateWork());
//			schAppt.setEndTime(schSch.getDateWork());
//		}
		schAppt.setPkPi(piMaster.getPkPi());
		schAppt.setDtApptype(dtApptype);
		schAppt.setPkDeptEx(schSch.getPkDept());
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(u.getPkDept());
		schAppt.setPkEmpReg(u.getPkEmp());
		schAppt.setNameEmpReg(u.getNameEmp());
		schAppt.setEuStatus("0");
		schAppt.setFlagPay("0");
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		schAppt.setPkOrgEx(u.getPkOrg());
		DataBaseHelper.insertBean(schAppt);

		SchApptPv schApptPv = new SchApptPv();
		schApptPv.setPkSchappt(schAppt.getPkSchappt());
		schApptPv.setEuApptmode("0");
		if("1".equals(schRes.getEuRestype())){//资源类型为人员
			schApptPv.setPkEmpPhy(schRes.getPkEmp());
			Map<String,Object> nameEmp = DataBaseHelper.queryForMap("select name_emp from BD_OU_EMPLOYEE where pk_emp = ?", schRes.getPkEmp());
			schApptPv.setNameEmpPhy(nameEmp.get("nameEmp")==null?"":nameEmp.get("nameEmp").toString());
		}
		schApptPv.setFlagPv("0");
		DataBaseHelper.insertBean(schApptPv);

		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",
				new Object[] { pkSch });
		int countSch = 0;
		if (schTicket != null) {
			countSch = DataBaseHelper.update("update sch_ticket set flag_used = '1' where pk_schticket = ? and flag_stop='0'",
					new Object[] { schTicket.getPkSchticket() });
		} else {
			countSch = DataBaseHelper.update("update sch_sch set ticket_no = nvl(ticket_no, '0') + 1 where pk_sch = ? and flag_stop='0'",
					new Object[] { pkSch });
		}
		if(countSch==0){
			throw new BusException("号已经被占用，重新操作！");
		}
		Map<String, SchApptCntVo> cntVoMap = getCntAppt(Sets.newHashSet(schSch.getPkSch()));
		SchApptCntVo cntVo = cntVoMap.get(schSch.getPkSch());
		schSch.setCntUsed(schSch.getCntUsed() + 1);
		schSch.setCntAppt(cntVo==null?0:cntVo.getCntAppt());
		schSch.setApptType(cntVo==null?0:cntVo.getCntApptOut());
		schSch.setCntApptIn(cntVo!=null ? cntVo.getCntApptIn():0);
		Map<String,Object> msgPiParam = new HashMap<String,Object>();
		msgPiParam.put("pkSchappt",schAppt.getPkSchappt());
		msgPiParam.put("isWeChat","1");//是否给平台发送微信预约消息
		msgPiParam.put("isAdd","1");
		PlatFormSendUtils.sendSchApptReg(msgPiParam);
		return schSch;
	}

	public void cancelApplyRegister(String param, IUser user) {
		String pkSchappt = JSON.parseObject(param).getString("pkSchappt");
		User u = (User) user;
		SchAppt schAppt = new SchAppt();
		schAppt.setPkSchappt(pkSchappt);
		schAppt.setFlagCancel("1");
		schAppt.setEuStatus("9");
		schAppt.setPkEmpCancel(u.getPkEmp());
		schAppt.setNameEmpCancel(u.getNameEmp());
		schAppt.setDateCancel(new Date());
		DataBaseHelper.updateBeanByPk(schAppt, false);

		schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and pk_schappt = ?",
				SchAppt.class, pkSchappt);
		if(schAppt == null){
			throw new BusException("该登记不存在");
		}

		modSchAndTicket(schAppt.getPkSch(), schAppt.getTicketNo());
		Map<String,Object> msgPiParam = new HashMap<String,Object>();
		msgPiParam.put("pkSchappt",schAppt.getPkSchappt());
		msgPiParam.put("note","HIS-手动取消2");
		PlatFormSendUtils.sendSchApptReg(msgPiParam);
	}

	public List<ApplyBlacklist> genApplyBlacklist(String param, IUser user) {
		// 允许锁定的爽约次数
		int breakCnt = Integer.valueOf(ApplicationUtils.getSysparam("SCH0002", true));
		
		// 允许锁定的就诊迟到次数
		int lateTimer = Integer.valueOf(ApplicationUtils.getSysparam("SCH0006", false));
		
		// 查询爽约的天数范围
		int breakDays = Integer.valueOf(ApplicationUtils.getSysparam("SCH0003", true));
		DateTime now = DateTime.now();
		DateTime begin = now.plusDays(0 - breakDays);
		String dateBegin = begin.toString("yyyyMMdd") + "000000";
		String dateEnd = now.toString("yyyyMMdd") + "235959";	
		Map<String, Object> params = Maps.newHashMap();
		params.put("breakCnt", breakCnt);
		params.put("dateBegin", dateBegin);
		
		params.put("dateEnd", dateEnd);
		params.put("lateTimer", lateTimer);
		List<ApplyBlacklist> list = schApptMapper.genApplyBlacklist(params);
		List<ApplyBlacklist> Newlist = schApptMapper.genApplyBlackLatelist(params);
		for (ApplyBlacklist applyBlacklist : Newlist) {
			list.add(applyBlacklist);
		}
		return list;
	}

	public Map<String, Object> getBlacklistDT(String param, IUser user) {
		String pkPi = JSON.parseObject(param).getString("pkPi");
		// 查询爽约的天数范围
		int breakDays = Integer.valueOf(ApplicationUtils.getSysparam("SCH0003", true));
		DateTime now = DateTime.now();
		DateTime begin = now.plusDays(0 - breakDays);
		String dateBegin = begin.toString("yyyyMMdd") + "000000";
		String dateEnd = now.toString("yyyyMMdd") + "235959";
		/*文档：有锁记录的时候则大于锁定开始时间*/
		String sql = "select date_begin from pi_lock where PK_PI=? order by CREATE_TIME desc ";
		List<Map<String, Object>> NewdateBegin = DataBaseHelper.queryForList(sql, new Object[]{pkPi});
		Map<String, Object> params = Maps.newHashMap();
		params.put("pkPi", pkPi);
		params.put("dateBegin", dateBegin);
        if(NewdateBegin.size()>0){
        	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        	String dateString = formatter.format(NewdateBegin.get(0).get("dateBegin"));
        	params.put("dateBegin", dateString);
		}
		params.put("dateEnd", dateEnd);
		List<Map<String, Object>> schAppts = schApptMapper.getSchApptBlacklist(params);
		List<Map<String, Object>> schApptLates = schApptMapper.getSchApptBlackLLatelist(params);
		for (Map<String, Object> map : schApptLates) {
			schAppts.add(map);
		}
		
		/*
		 * List<Map<String, Object>> piLockDts = DataBaseHelper.queryForList(
		 * "select pi.date_begin,pi.date_end, pi.eu_locktype,dt.eu_direct,dt.name_emp_opera,dt.note,case when dt.flag_effect='1' then '已审核' else '未审核' end flag_effect,boe.name_emp pk_emp_effect,dt.pk_pilockdt From pi_lock_dt dt"
		 * +
		 * " Left Join pi_lock pi On pi.pk_pi=dt.pk_pi left join BD_OU_EMPLOYEE boe on boe.pk_emp=dt.pk_emp_effect where dt.pk_pi = ?"
		 * ,pkPi);
		 */
		//获取患者的黑名单处理记录
		List<Map<String, Object>> piLockDts = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotEmpty(pkPi)) {
			piLockDts = schApptMapper.getLockDtlist(pkPi);
		}
		
		Map<String, Object> resultMap = Maps.newHashMap();
		resultMap.put("schAppts", schAppts);
		resultMap.put("piLockDts", piLockDts);
		return resultMap;
	}

	public void updateLock(String param, IUser user) {
		//【SCH0014】判断添加黑名单是否需要审核0:否（自动审核），1：是，
		String isAudit=ApplicationUtils.getSysparam("SCH0014", false);
		if(StringUtils.isEmpty(isAudit)){
			isAudit="0";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		User u = (User) user;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);	
		//List<Map<String,Object>> paramMap = JsonUtil.readValue(param, new TypeReference<List<Map<String,Object>>>() {});
		List<String> pkPis = (List<String>) paramMap.get("pkPis");				
		String type = (String) paramMap.get("type");
		String lockType = (String) paramMap.get("lockType");
		String note = (String) paramMap.get("note");
		String dateEnd = (String) paramMap.get("dateEnd");
		Date newDateEndDate = null;
		try {
			if(dateEnd != null){				
				newDateEndDate = df.parse(dateEnd);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		if ("0".equals(type)) {// 锁定
			List<PiLock> piLocks = Lists.newArrayList();
			List<PiLockDt> piLockDts = Lists.newArrayList();
			for (String pkPi : pkPis) {
				PiLock piLock = new PiLock();
				piLock.setPkPilock(NHISUUID.getKeyId());
				piLock.setEuLocktype(lockType);
				piLock.setNote(note);
				piLock.setDateBegin(new Date());							
				piLock.setDateEnd(newDateEndDate);
				piLock.setPkPi(pkPi);
				piLock.setPkOrg(u.getPkOrg());
				piLock.setCreateTime(new Date());
				piLock.setTs(new Date());
				piLock.setCreator(u.getPkEmp());
				piLocks.add(piLock);
				PiLockDt piLockDt = new PiLockDt();
				piLockDt.setPkPilockdt(NHISUUID.getKeyId());
				piLockDt.setPkOrg(u.getPkOrg());
				piLockDt.setPkPi(pkPi);
				piLockDt.setEuLocktype(lockType);
				piLockDt.setEuDirect("1");
				piLockDt.setDateLock(new Date());
				piLockDt.setPkEmpOpera(u.getPkEmp());
				piLockDt.setNameEmpOpera(u.getNameEmp());
				piLockDt.setNote(note);
				piLockDt.setCreateTime(new Date());
				piLockDt.setTs(new Date());
				piLockDt.setCreator(u.getPkEmp());
				if("0".equals(isAudit)){
					piLockDt.setFlagEffect("1");
					piLockDt.setPkEmpEffect(u.getPkEmp());
					piLockDt.setDateEffect(new Date());
				}else{
					piLockDt.setFlagEffect("0");
				}
				piLockDts.add(piLockDt);
			}
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLock.class), piLocks);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDts);			
		} else if ("1".equals(type)) {// 解锁
			StringBuilder whereCase = new StringBuilder();
			whereCase.append("where pk_pi in (");
			List<PiLockDt> piLockDts = Lists.newArrayList();
			for (int i = 0; i < pkPis.size(); i++) {
				String pkPi = pkPis.get(i);
				if (i == 0) {
					whereCase.append("'").append(pkPi).append("'");
				} else {
					whereCase.append(",'").append(pkPi).append("'");
				}
				PiLockDt piLockDt = new PiLockDt();
				piLockDt.setPkPilockdt(NHISUUID.getKeyId());
				piLockDt.setPkOrg(u.getPkOrg());
				piLockDt.setPkPi(pkPi);
				piLockDt.setEuLocktype("0");
				piLockDt.setEuDirect("-1");
				piLockDt.setDateLock(new Date());
				piLockDt.setPkEmpOpera(u.getPkEmp());
				piLockDt.setNameEmpOpera(u.getNameEmp());
				piLockDts.add(piLockDt);
			}
			whereCase.append(")");
			DataBaseHelper.update("update PI_LOCK_RECORD set EU_STATUS='0' where PK_PILOCK in(select PK_PILOCK from pi_lock "+whereCase+")",paramMap);

			DataBaseHelper.execute("delete from pi_lock " + whereCase);
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(PiLockDt.class), piLockDts);
		}
	}

	public void noticeApplyPi(String param, IUser user) {
		User u = (User) user;
		Map paramMap = JsonUtil.readValue(param, Map.class);
		List<String> pkSchappts = (List<String>) paramMap.get("pkSchappts");
		for (String pkSchappt : pkSchappts) {
			DataBaseHelper.update("update sch_appt set flag_notice_canc = '1' where pk_schappt = ?",
					new Object[] { pkSchappt });

			SchAppt schAppt = new SchAppt();
			schAppt.setPkSchappt(pkSchappt);
			schAppt.setEuStatus("9");
			schAppt.setFlagCancel("1");
			schAppt.setDateCancel(new Date());
			schAppt.setPkEmpCancel(u.getPkEmp());
			schAppt.setNameEmpCancel(u.getNameEmp());
			DataBaseHelper.updateBeanByPk(schAppt, false);

			schAppt = DataBaseHelper.queryForBean("select * from sch_appt where del_flag = '0' and pk_schappt = ?",
					SchAppt.class, pkSchappt);
			DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?",
					new Object[] { schAppt.getPkSch() });
			DataBaseHelper.update("update sch_ticket set flag_used = '0' where pk_sch = ? and ticketno = ?",
					new Object[] { schAppt.getPkSch(), schAppt.getTicketNo() });
			List<String> pkCnords = DataBaseHelper.queryForList(
					"select pk_cnord from sch_appt_ord where pk_schappt = ?", String.class, new Object[] { pkSchappt });
			if (pkCnords.size() > 0) {
				String pkCnord = pkCnords.get(0);
				// 更新检查申请单
				DataBaseHelper.update("update cn_ris_apply set eu_status = '1' where pk_cnord = ?",
						new Object[] { pkCnord });
				// 更新病理申请单
				DataBaseHelper.update("update cn_pa_apply set eu_status = '1' where pk_cnord = ?",
						new Object[] { pkCnord });
			}
		}

	}
	
	
	public ApplyBlacklistVo getPiList(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = MapUtils.getIntValue(paramMap, "pageIndex");
		int pageSize = MapUtils.getIntValue(paramMap, "pageSize");
		ApplyBlacklistVo vo = new ApplyBlacklistVo();
		// 分页操作
		MyBatisPage.startPage(pageIndex, pageSize);
		List<ApplyBlacklist> list = schApptMapper.getPiList(paramMap);
		Page<List<ApplyBlacklist>> page = MyBatisPage.getPage();
		vo.setPatiList(list);
		vo.setTotalCount(page.getTotalCount());
		return vo;
	}
	/**
	 * 自助服务-预约挂号
	 * @param param
	 * @return
	 * 
	 */
	public Page<DepartmentInformation> getDepartmentInformation(String param, IUser user){
		User userorg = (User) user;
		DepartmentInformation dInformation = JsonUtil.readValue(param, DepartmentInformation.class);
		MyBatisPage.startPage(dInformation.getPageNum(), dInformation.getPageSize());
		String pkOrg = userorg.getPkOrg();
		List<DepartmentInformation> list;
		if (Application.isSqlServer()) {
		    list = dInformationMapper.getDepartmentInformation_sql(pkOrg);
		} else {
		    list = dInformationMapper.getDepartmentInformation_oracle(pkOrg);
		}
		Page<DepartmentInformation> page = MyBatisPage.getPage();
		page.setRows(list);		
		return page;		
	}
	/**
	 * 根据科室获取有排班的资源
	 * @param param
	 * @return
	 */
	public Page<SchedulingResources> getSchedulingResources(String param, IUser user){
		SchedulingResources resources = JsonUtil.readValue(param, SchedulingResources.class);
		MyBatisPage.startPage(resources.getPageNum(), resources.getPageSize());
		String pkDept = resources.getPkDept();
		List<SchedulingResources> list;
		if (Application.isSqlServer()) {
		    list = sResourcesMapper.getSchedulingResources_sql(pkDept);
		} else {
		    list = sResourcesMapper.getSchedulingResources_oracle(pkDept);
		}
		Page<SchedulingResources> page = MyBatisPage.getPage();
		page.setRows(list);				
		return page;		
	}
	
	/**
     * 查询预约等待
     * @param param
     * @return
     */
	public List<Map<String, Object>> searchApply(String param, IUser user){
	    List<Map<String, Object>> list = null;
	    if (Application.isSqlServer()) {
	        list = schApptMapper.searchApply_sql();
	    } else {
	        list = schApptMapper.searchApply_oracle();
	    }
	    return list;
	}
	
	/**
     * 查询已预约
     * @param param
     * @return
     */
	public List<Map<String, Object>> searchAlreadyApply(String param, IUser user){
	    Map params = JsonUtil.readValue(param, Map.class);
	    String pkPi = (String) params.get("pkPi");
	    List<Map<String, Object>> list = null;
	    Map<String,Object> pataMap = new HashMap<>();
		pataMap.put("pkPi",pkPi);
		pataMap.put("dateAppt",DateUtils.getDateMorning(new Date(),0));
		list = schApptMapper.searchAlreadyApply(pataMap);
        return list;
	}
	
	/**
     * 获取可预约时间内的挂号科室信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCanApplyDept(String param, IUser user){
        String pkOrg = ((User)user).getPkOrg();
        List<Map<String, Object>> list = null;
        if (Application.isSqlServer()) {
            list = schApptMapper.getCanApplyDept_sql(pkOrg);
        } else {
            list = schApptMapper.getCanApplyDept_oracle(pkOrg);
        }
        return list;
    }
    
    /**
     * 获取可预约科室的详细预约信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCanApplyDeptDetail(String param, IUser user){
        Map params = JsonUtil.readValue(param, Map.class);
        String pkDept = (String) params.get("pkDept");
        List<Map<String, Object>> list = null;
        if (Application.isSqlServer()) {
            list = schApptMapper.getCanApplyDeptDetail_sql(pkDept);
        } else {
            list = schApptMapper.getCanApplyDeptDetail_oracle(pkDept);
        }
        return list;
    }
    /**
     * 获取黑名单审核记录
     * @param param
     * @param user
     * @return
     */
    public ApplyBlacklistVo getPiAuditList(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		int pageIndex = MapUtils.getIntValue(paramMap, "pageIndex");
		int pageSize = MapUtils.getIntValue(paramMap, "pageSize");
		ApplyBlacklistVo vo = new ApplyBlacklistVo();
		// 分页操作
		MyBatisPage.startPage(pageIndex, pageSize);
		List<ApplyBlacklist> list = schApptMapper.getPiAuditList(paramMap);
		Page<List<ApplyBlacklist>> page = MyBatisPage.getPage();
		vo.setPatiList(list);
		vo.setTotalCount(page.getTotalCount());
		return vo;
	}
    /**
     * 黑名单审核
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
	public void updateLockAudit(String param, IUser user) {
		User u = (User) user;
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);	
		List<String> pkPis = (List<String>) paramMap.get("pkPis");	
		String type = (String) paramMap.get("type");
		PiLockDt piLockDt=new PiLockDt();
		piLockDt.setFlagEffect(type);
		if(EnumerateParameter.ZERO.equals(type)){
			piLockDt.setPkEmpEffect(null);
			piLockDt.setDateEffect(null);
		} else {
			piLockDt.setPkEmpEffect(u.getPkEmp());
			piLockDt.setDateEffect(new Date());
		}
		piLockDt.setTs(new Date());
		if(pkPis.size()>0){
			DataBaseHelper.update("update PI_LOCK_DT set ts=:ts,flag_effect=:flagEffect,pk_emp_effect=:pkEmpEffect,DATE_EFFECT=:dateEffect where PK_PI in ("+CommonUtils.convertListToSqlInPart(pkPis)+")",piLockDt);
		}
	}
    /**
     * 交易号 ：009003002012
     * 查询科室树
     * @param param
     * @param user
     */
    @SuppressWarnings("unchecked")
	public List<Map<String,Object>> getDeptTree(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
		User u = (User) user;
		paramMap.put("pkOrg", u.getPkOrg());		
		List<Map<String,Object>>  deptTreeList = null;
		//增加菜单参数，菜单参数为0时，诊区列表默认加载所有诊区，菜单参数为1时，根据登陆人员所属诊区显示；
		String flagRole = CommonUtils.getPropValueStr(paramMap, "flagRole");
		//读取参数【SCH0018】诊疗资源维护和诊疗排班计划是否控制权限
		String schAuthority = ApplicationUtils.getSysparam("SCH0018", false);
		if(EnumerateParameter.ONE.equals(flagRole) && EnumerateParameter.THREE.equals(schAuthority)) {
			paramMap.put("pkDept", u.getPkDept());	
			deptTreeList = schApptMapper.getAreaDeptTree(paramMap);
		}else {
			deptTreeList = schApptMapper.getDeptTree(paramMap);
		}
		return deptTreeList;
	}
    
}
