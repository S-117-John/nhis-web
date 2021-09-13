package com.zebone.nhis.cn.opdw.service;

import com.zebone.nhis.cn.opdw.dao.SyxCnOpClinicAppointmentMapper;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 诊间预约
 * @author cuijiansheng 2019-8-19
 *
 */
@Service
public class SyxCnOpClinicAppointmentService {

	@Autowired
	private SyxCnOpClinicAppointmentMapper mapper;
	
	public static SimpleDateFormat sdfl = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static SimpleDateFormat sdl = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 查询当前医生排班日历：004003011042
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryClinicSch(String param, IUser user){
		
		User u = (User)user;
		String beginTime = JsonUtil.getFieldValue(param, "beginTime");
		String endTime = JsonUtil.getFieldValue(param, "endTime");
		String pkSchres = JsonUtil.getFieldValue(param,"pkSchres");
		String pkDept= JsonUtil.getFieldValue(param,"pkDept");
		if(StringUtils.isEmpty(pkDept)) pkDept=  u.getPkDept();
		String pkEmp= JsonUtil.getFieldValue(param,"pkEmp");
		if(StringUtils.isEmpty(pkEmp)) pkEmp=  u.getPkEmp();
		if(StringUtils.isEmpty(pkSchres)) {
			SchResource res = DataBaseHelper.queryForBean("SELECT * FROM sch_resource WHERE pk_dept_belong=? and pk_emp = ?", SchResource.class, pkDept,pkEmp);
			if(res == null || StringUtils.isBlank(res.getPkSchres())){
				throw new BusException("当前医生排班资源为空！");
			}
			pkSchres = res.getPkSchres();
		}
		if(StringUtils.isEmpty(pkSchres)){
			throw new BusException("当前医生排班资源为空！");
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("beginTime", beginTime);
		paramMap.put("endTime", endTime);
		paramMap.put("pkSchres", pkSchres);
		paramMap.put("isSqlServer", Application.isSqlServer()?1:0);
		String ifFilter=ApplicationUtils.getSysparam("SCH0016", false);
		List<Map<String, Object>> resultList=null;
		if("1".equals(ifFilter)){
			resultList = mapper.qryClinicSchTic(paramMap);
		}else{
			resultList = mapper.qryClinicSch(paramMap);
		}
		
		//把数据都放在一行上面
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", resultList);
		
		//把一行的数据放进dataSource
		List<Map<String, Object>> result = new ArrayList<>();
		result.add(map);
		
		return result;
	}
	
	/**
	 * 查询医生出诊信息：004003011043
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryClinicSchList(String param, IUser user){
		
		User u = (User)user;
		String dateWork = JsonUtil.getFieldValue(param, "dateWork");
		String pkSchres = JsonUtil.getFieldValue(param,"pkSchres");
		String pkDept= JsonUtil.getFieldValue(param,"pkDept");
		if(StringUtils.isEmpty(pkDept)) pkDept=  u.getPkDept();
		String pkEmp= JsonUtil.getFieldValue(param,"pkEmp");
		if(StringUtils.isEmpty(pkEmp)) pkEmp=  u.getPkEmp();
		if(StringUtils.isEmpty(pkSchres)){
			SchResource res = DataBaseHelper.queryForBean("SELECT * FROM sch_resource WHERE pk_dept_belong=? and  pk_emp = ?", SchResource.class,pkDept,pkEmp);
			if(res == null || StringUtils.isBlank(res.getPkSchres())){
				throw new BusException("当前医生排班资源为空！");
			}
			pkSchres = res.getPkSchres();
		}
		if(StringUtils.isEmpty(pkSchres)){
			throw new BusException("当前医生排班资源为空！");
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("dateWork", dateWork);
		paramMap.put("pkSchres", pkSchres);
		String ifFilter=ApplicationUtils.getSysparam("SCH0016", false);
		List<Map<String, Object>> list=null;
		if("1".equals(ifFilter)){
			list = mapper.qryClinicSchListTic(paramMap);
		}else{
			list = mapper.qryClinicSchList(paramMap);
		}
		return list;
	}
	
	/**
	 * 预约处理：004003011044
	 * @param param
	 * @param user
	 * @throws ParseException 
	 */
	public void clinicMakeApp(String param, IUser user) throws ParseException{
		
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		User u = (User)user;
		//排班主键
		String pkSch = CommonUtils.getString(paramMap.get("pkSch"));
		if(StringUtils.isBlank(pkSch)) throw new BusException("诊间预约传排班主键为空！");
		//日期分组
		String pkDateslot = CommonUtils.getString(paramMap.get("pkDateslot"));
		//就诊日期
		String dateWork = CommonUtils.getString(paramMap.get("dateWork"));
		//预约开始时间
		String beginTime = CommonUtils.getString(paramMap.get("beginTime"));
		//预约结束时间
		String endTime = CommonUtils.getString(paramMap.get("endTime"));
		//患者主键
		String pkPi = CommonUtils.getString(paramMap.get("pkPi"));
		//医生主键
		String pkEmp= MapUtils.getString(paramMap,"pkEmp");
		//医生名称
		String nameEmp= MapUtils.getString(paramMap,"nameEmp");
		//科室
		String pkDept= MapUtils.getString(paramMap,"pkDept");
		
		//排班记录
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		if(schSch==null) throw new BusException("当前医生排班为空！");
		//排班资源
		String pkSchres = schSch.getPkSchres();
		//排班服务
		String pkSchsrv = schSch.getPkSchsrv();

		//就诊时间
		
		String sql = "select count(1) from sch_appt where to_char(date_appt,'yyyy-MM-dd')=? and pk_schres=? and pk_dept_ex=? and flag_cancel='0' and pk_pi=? ";
		Integer count = DataBaseHelper.queryForScalar(sql, Integer.class, dateWork,pkSchres,schSch.getPkDept(),pkPi);
		if(count > 0){
			throw new BusException("预约记录已存在!");
		}
		Date dateBegin = sdf.parse(beginTime);
		Date dateEnd = sdf.parse(endTime);
		//排班-号表
		List<SchTicket> tickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ?  and del_flag = '0' and flag_appt = '1' and flag_stop='0' and flag_used = '0' and  begin_time = ? and end_time =?   order by  ticketno  ",
				SchTicket.class, new Object[]{pkSch,dateBegin,dateEnd});
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set flag_used ='1' where pk_schticket =? and flag_used='0'", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		//预约科室
		pkDept = StringUtils.isEmpty(pkDept)?u.getPkDept():pkDept;
		pkEmp=StringUtils.isEmpty(pkEmp)?u.getPkEmp():pkEmp;
		nameEmp=StringUtils.isEmpty(nameEmp)?u.getNameEmp():nameEmp;
		//排班预约
		SchAppt schAppt = new SchAppt();
		schAppt.setEuSchclass("0");
		schAppt.setPkSch(pkSch);
		schAppt.setDateAppt(sdl.parse(dateWork));
		schAppt.setPkDateslot(pkDateslot);
		schAppt.setPkSchres(pkSchres);
		schAppt.setPkSchsrv(pkSchsrv);
		schAppt.setTicketNo(ticket.getTicketno());
		schAppt.setBeginTime(dateBegin);
		schAppt.setEndTime(dateEnd);
		schAppt.setPkPi(pkPi);
		schAppt.setDtApptype("0");
		schAppt.setPkDeptEx(schSch.getPkDept());
		schAppt.setPkOrgEx(schSch.getPkOrg());
		schAppt.setDateReg(new Date());
		schAppt.setPkDeptReg(u.getPkDept());
		schAppt.setPkEmpReg(u.getPkEmp());
		schAppt.setNameEmpReg(u.getNameEmp());
		schAppt.setEuStatus("0");//0:登记,1:到达
		schAppt.setFlagPay("0");
		schAppt.setFlagNotice("0");
		schAppt.setFlagCancel("0");
		schAppt.setFlagNoticeCanc("0");
		schAppt.setCode(ApplicationUtils.getCode("0101"));//预约编码，不清楚,写主键//2019-07-19编码规则中预约单号为预约编码
		DataBaseHelper.insertBean(schAppt);
		
		//预约就诊
		SchApptPv schApptPv = new SchApptPv();
		schApptPv.setPkSchappt(schAppt.getPkSchappt());
		schApptPv.setEuApptmode("0");
		schApptPv.setPkEmpPhy(pkEmp);
		schApptPv.setNameEmpPhy(nameEmp);
		schApptPv.setFlagPv("0");
		DataBaseHelper.insertBean(schApptPv);
		
		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?", new Object[] {pkSch});


		Map<String,Object> map = new HashMap<>(16);
		map.put("pkPi",pkPi);
		map.put("pkSch",pkSch);
		PlatFormSendUtils.sendReserveOutpatient(map);
	}
}
