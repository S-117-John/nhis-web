package com.zebone.nhis.pv.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Lists;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
/**
 * 挂号号表公共服务类
 * @author yangxue
 *
 */
@Service
public class TicketPubService {
    /**
     * 获取可用预约号
     * @param pkSch
     * @return SchTicket
     */
	public SchTicket getUnusedAppTicket(String pkSch,String pkDateslotsec,String euPvtype){
		// 处理号表and eu_pvtype = ?,euPvtype
		List<SchTicket> tickets = new ArrayList<>();
		Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select time_begin,time_end from bd_code_dateslot_sec where pk_dateslotsec=?", pkDateslotsec);
		//判断时段编码是否为空
		if(MapUtils.isNotEmpty(slotSecMap)){
			tickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and FLAG_APPT = '1' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
				SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"));
		}else{
			Object [] ticketParam = {pkSch};
			String tkSql = "";
			if(limitSchTime()){
				ticketParam = new Object[]{pkSch, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")};
				tkSql = " and END_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss') ";
			}
			tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0'"+tkSql+" order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
					SchTicket.class, ticketParam);
		}	
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return ticket;
	}

	/**
	 * 获取可用预约号
	 * @param paraMap 必选参数：pkSch：排班主键、可选参数：startTime：开始时段（HH24:mi:ss）、endTime：结束时段（HH24:mi:ss）
	 * @return SchTicket
	 */
	public SchTicket getUnusedAppTicket(Map<String,Object> paraMap){
		if(MapUtils.isEmpty(paraMap)){
			throw new BusException("获取可预约号入参为空");
		}
		String startTime = MapUtils.getString(paraMap,"startTime");
		String endTime = MapUtils.getString(paraMap,"endTime");
		boolean lockTicket = MapUtils.getBooleanValue(paraMap,"lockTicket",true);
		String dtApptype = MapUtils.getString(paraMap, "dtApptype");
		paraMap.put("currentTime",DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		String tkSql = "";
		if(StringUtils.isNotEmpty(startTime)){
			tkSql += " and to_char(BEGIN_TIME,'HH24:mi:ss') >=:startTime ";
		}
		if(StringUtils.isNotEmpty(endTime)){
			tkSql += " and to_char(END_TIME,'HH24:mi:ss') <=:endTime " + (StringUtils.isNotEmpty(startTime)?" and :endTime>=:startTime ":"");
		}
		if(StringUtils.isNotEmpty(dtApptype)){
			tkSql += " and DT_APPTYPE=:dtApptype ";
		}else if(MapUtils.getBooleanValue(paraMap,"isExternal",false)){//外部接口查询
			tkSql += " and DT_APPTYPE >'1' ";
		}
		List<SchTicket> tickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = :pkSch  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0'" +
						" and END_TIME>=to_date(:currentTime,'yyyy-MM-dd HH24:mi:ss') " +
						tkSql +
						" order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
				SchTicket.class, paraMap);
		if(tickets==null||tickets.size()<=0){
			throw new BusException("所选的日期排班[时段]已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		if(lockTicket){
			//占用号表数据
			int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
			if(cnt<=0)
				throw new BusException("挂号号码已被占用，请重试！");
		}
		return ticket;
	}

	/**
     * 获取可用预约号
     * @param pkSch
     * @return SchTicket
     */
	public SchTicket getUnusedAppTicket(String pkSch,String pkDateslotsec,String euPvtype,String isAppointClinics){
		// 处理号表and eu_pvtype = ?,euPvtype
		List<SchTicket> tickets = new ArrayList<>();
		Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select time_begin,time_end from bd_code_dateslot_sec where pk_dateslotsec=?", pkDateslotsec);
		if(StringUtils.isBlank(isAppointClinics)){
			isAppointClinics="0";
		}
		//判断时段编码是否为空
		if(MapUtils.isNotEmpty(slotSecMap)){
			if("1".equals(isAppointClinics)){
				tickets = DataBaseHelper.queryForList(
						"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and FLAG_APPT = '1' and FLAG_USED = '0' and ((dt_apptype is not null and  dt_apptype!='0') or dt_apptype is null) order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
						SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"));
			}else{
				tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and FLAG_APPT = '1' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
					SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"));
			}
		}else{
			Object [] ticketParam = {pkSch};
			String tkSql = "";
			if(limitSchTime()){
				ticketParam = new Object[]{pkSch, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")};
				tkSql = " and END_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss') ";
			}
			if("1".equals(isAppointClinics)){
				tickets = DataBaseHelper.queryForList(
						"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and ((dt_apptype is not null and  dt_apptype!='0') or dt_apptype is null) and FLAG_USED = '0'"+tkSql+" order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
						SchTicket.class, ticketParam);
			}else{
				tickets = DataBaseHelper.queryForList(
						"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0'"+tkSql+" order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
						SchTicket.class, ticketParam);
			}
			
		}	
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return ticket;
	}
	/**
	 * 设置预约号可用
	 * @param ticket
	 */
	public void  setTicketUnused(SchTicket ticket){
		if(ticket==null)
			return;
		DataBaseHelper.update("update sch_ticket set FLAG_USED ='0' where pk_schticket = ?", new Object[]{ticket.getPkSchticket()});
	}
	 /**
     * 获取可用挂号
     * @param pkSch
     * @return SchTicket
     */
	public SchTicket getUnusedTicket(String pkSch,String euPvtype,String isAppointClinics){
		//参数值为0不限制，参数值为1限制只能挂当前时间之后的号源；
		Object [] ticketParam = {pkSch};
		String tkSql = "";
		if(limitSchTime()){
			ticketParam = new Object[]{pkSch, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")};
			tkSql = " and END_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss') ";
		}
		if(StringUtils.isBlank(isAppointClinics)){
			isAppointClinics="0";
		}
		// 处理号表and eu_pvtype = ?,euPvtype
		List<SchTicket> tickets = new ArrayList<SchTicket>();
		if("1".equals(isAppointClinics)){
			tickets=DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ? "+tkSql+" and DEL_FLAG = '0' and ((dt_apptype is not null and  dt_apptype!='0') or dt_apptype is null) and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
					SchTicket.class, ticketParam);
		}else{
			tickets=DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ? "+tkSql+" and DEL_FLAG = '0'  and flag_stop='0' and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
				SchTicket.class, ticketParam);
		}
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已挂满或无可挂号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return ticket;
	}
	/**
	 * 无号表方式下获取可用票号
	 * @param pkSch
	 * @param euPvtype
	 * @return
	 */
	public SchTicket getUnusedTicketFromSch(String pkSch,String euPvtype){
		//先更新号表，后查询更新的票号返回
		 int cnt =  DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_total", new Object[]{pkSch});
		 if(cnt<=0)
			 throw new BusException("您所选的排班资源已挂满！");
		 SchTicket  ticket =  new SchTicket();
		 SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
		 ticket.setTicketno(sch.getTicketNo());
		 ticket.setPkSch(pkSch);
		 return ticket;
	}
	/**
	 * 无号表方式下获取可用预约票号
	 * @param pkSch
	 * @param euPvtype
	 * @return
	 */
	public SchTicket getUnusedAppTicketFromSch(String pkSch,String euPvtype){
		//先更新号表，后查询更新的票号返回
		 int cnt =  DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? and cnt_used<cnt_appt", new Object[]{pkSch});
		 if(cnt<=0)
			 throw new BusException("您所选的排班资源已约满！");
		 SchTicket  ticket =  new SchTicket();
		 SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
		 ticket.setTicketno(sch.getTicketNo());
		 ticket.setPkSch(pkSch);
		 return ticket;
	}
	
	/**
	 * 无票号方式设置可用号数未使用
	 * @param ticket
	 */
	public void  setTicketUnusedFromSch(SchTicket ticket){
		if(ticket==null||CommonUtils.isNull(ticket.getPkSch()))
			return;
		DataBaseHelper.update("update sch_sch set cnt_used=cnt_used-1 where pk_sch = ?", new Object[]{ticket.getPkSch()});
	}

	/**
	 * 挂号是否限制时段
	 * @return
	 */
	public boolean limitSchTime(){
		return StringUtils.equals(ApplicationUtils.getSysparam("PV0044",false), EnumerateParameter.ONE);
	}
	
	/**
     * 获取可用号源（预约+当日）
     * @param pkSch
     * @param type (1-预约；0-当日)
     * @return SchTicket
     */
	public SchTicket getUnusedAppTicketExt(String pkSch,String pkDateslotsec,String type){
		List<SchTicket> tickets = new ArrayList<>();
		Map<String, Object> slotSecMap = DataBaseHelper.queryForMap("select time_begin,time_end from bd_code_dateslot_sec where pk_dateslotsec=?", pkDateslotsec);
		//判断时段编码是否为空
		if(MapUtils.isNotEmpty(slotSecMap)){
			tickets = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ? and TO_CHAR (begin_time, 'hh24:mi:ss') >=? and TO_CHAR (end_time, 'hh24:mi:ss') <=?  and DEL_FLAG = '0'  and FLAG_APPT = ? and FLAG_USED = '0' order by case  when (ticketno is null or TICKETNO='') then  0 else cast(TICKETNO as int) end ",
				SchTicket.class, pkSch,slotSecMap.get("timeBegin"),slotSecMap.get("timeEnd"),type);
		}else{
			Object [] ticketParam = {pkSch,type};
			String tkSql = "";
			if(limitSchTime()){
				ticketParam = new Object[]{pkSch,type, DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")};
				tkSql = " and END_TIME>=to_date(?,'yyyy-MM-dd HH24:mi:ss') ";
			}
			tickets = DataBaseHelper.queryForList(
					"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = ? and flag_stop='0' and FLAG_USED = '0'"+tkSql+" order by case   when (ticketno is null or ticketno='') then  0 else cast(ticketno as int) end ",
					SchTicket.class, ticketParam);
		}	
		if(tickets==null||tickets.size()<=0){
			throw new BusException("您所选的排班资源已无可预约号！");
		}
		SchTicket ticket = tickets.get(0);
		//占用号表数据
		int cnt = DataBaseHelper.update("update sch_ticket set FLAG_USED ='1' where pk_schticket =? and FLAG_USED='0'", new Object[]{ticket.getPkSchticket()});
		if(cnt<=0)
			throw new BusException("您所选的挂号号码已被占用，请重试！");
		return ticket;
	}
	
	/**
	 * 无号表方式下获取可用票号
	 * @param pkSch
	 * @param type (1-预约；0-当日)
	 * @return
	 */
	public SchTicket getUnusedAppTicketFromSchExt(String pkSch,String type){
		//先更新号表，后查询更新的票号返回
		int cnt =  DataBaseHelper.update("update sch_sch  set TICKET_NO = nvl(TICKET_NO,0)+1,cnt_used=cnt_used+1 where PK_SCH = ? "
	 			+ ("1".equals(type) ? " and cnt_used < cnt_appt " : " and cnt_used < cnt_total ") , new Object[]{pkSch});
	 	if(cnt<=0) {
	 		throw new BusException("您所选的排班资源已约满！");
	 	}
		 SchTicket  ticket =  new SchTicket();
		 SchSch sch= DataBaseHelper.queryForBean("select ticket_no,pk_sch from sch_sch where pk_sch = ?", SchSch.class, new Object[]{pkSch});
		 ticket.setTicketno(sch.getTicketNo());
		 ticket.setPkSch(pkSch);
		 return ticket;
	}
}
