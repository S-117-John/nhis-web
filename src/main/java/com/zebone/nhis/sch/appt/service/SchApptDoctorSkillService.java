package com.zebone.nhis.sch.appt.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptOrd;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.sch.appt.dao.SchApptDoctorSkillMapper;
import com.zebone.nhis.sch.appt.vo.CheckApplyVo;
import com.zebone.nhis.sch.appt.vo.OrderSuccessParam;
import com.zebone.nhis.sch.plan.vo.SchInfoVo;
import com.zebone.nhis.sch.plan.vo.SchSchVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class SchApptDoctorSkillService {

	@Autowired
	SchApptDoctorSkillMapper apptDoctorSkillMapper;
	
	@Autowired
	private BaseCodeMapper codeMapper;
	
	/**
	 * 根据医嘱主键查询申请单信息
	 * @param param
	 * @param user
	 * @return
	 */
	public CheckApplyVo checkApply(String param, IUser user){
		Map<String, String> readValue = JsonUtil.readValue(param, new TypeReference<Map<String, String>>(){});
		String pk = readValue.get("param");
		CheckApplyVo checkApplyByPk = apptDoctorSkillMapper.getCheckApplyByPk(pk);
		return checkApplyByPk;
	}
	
	/**
	 * 获取医技预约排班信息
	 * 
	 * @return
	 */
	public List<SchInfoVo> getSkillSchInfo(String param, IUser user) {
		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		List<SchSchVo> list = apptDoctorSkillMapper.getSkillSchInfo(params);

		/** list分组 */
		// 根据pkSchres:schresName:pkSchsrv:schsrvName 对list进行分组
		ImmutableList<SchSchVo> digits = ImmutableList.copyOf(list);
		// 分组方法，pkSchres:schresName:pkSchsrv:schsrvName一致的为一组
		Function<SchSchVo, String> group = new Function<SchSchVo, String>() {
			@Override
			public String apply(SchSchVo schSchVo) {
				return schSchVo.getPkSchres() + ":" + schSchVo.getSchresName() + ":" + schSchVo.getPkSchsrv() + ":"
						+ schSchVo.getSchsrvName();
			}
		};
		// 执行分组方法
		ImmutableListMultimap<String, SchSchVo> groupMap = Multimaps.index(digits, group);

		List<SchInfoVo> infoList = Lists.newArrayList();
		for (String key : groupMap.keySet()) {
			String[] arr = key.split(":");
			String schsrvName = "";
			if (arr.length == 4) {
				schsrvName = arr[3];
			}
			SchInfoVo infoVo = new SchInfoVo(arr[0], arr[1], arr[2], schsrvName);
			List<SchSchVo> schVos = groupMap.get(key);
			Set<String> pkDateslots = Sets.newHashSet();
			for (SchSchVo schSchVo : schVos) {
				pkDateslots.add(schSchVo.getPkDateslot());
				//将该sql合并在了apptDoctorSkillMapper.getSkillSchInfo的sql中
//				int count = DataBaseHelper.queryForScalar("select count(1) from sch_ticket where DEL_FLAG = '0' and pk_sch = ?", Integer.class,schSchVo.getPkSch());
//				schSchVo.setFlagTicket(count > 0 ? "1" : "0");
			}
			infoVo.setBdCodeDateslots(codeMapper.getBdCodeDateslotByPkForType(pkDateslots));
			infoVo.setSchschs(schVos);
			infoVo.setPkDateslots(pkDateslots);
			infoList.add(infoVo);
		}
		return infoList;
	}
	
	/**
	 * 生成医技预约信息
	 * 
	 * @param param
	 * @param user
	 */
	public OrderSuccessParam generateSchAppt(String param, IUser user) {
		User u = (User) user;
		Map params = JsonUtil.readValue(param, Map.class);
		String pkSch = (String) params.get("pkSch");
		// String dateslotsec = (String) params.get("dateslotsec");
		String ticketno = (String) params.get("ticketno");
		String flagOccupy = (String) params.get("flagOccupy");
		String descOccupy = (String) params.get("descOccupy");
		String pkCnord = (String) params.get("pkCnord");
		String pkAssocc = (String) params.get("pkAssocc");
		// String type = (String) params.get("type");// 0:排班,1:时间分段,2:号表
		
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		CnOrder cnOrder = DataBaseHelper.queryForBean("select * from cn_order where del_flag = '0' and pk_cnord = ?",
				CnOrder.class, pkCnord);
		ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from ex_assist_occ WHERE flag_canc='0' and (flag_refund ='0' or flag_refund is null)  and pk_cnord = ? and pk_assocc=?",
				ExAssistOcc.class, new Object[]{pkCnord,pkAssocc});
		SchResource schRes = DataBaseHelper.queryForBean(
				"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ?", SchResource.class,
				schSch.getPkSchres());
		
		if(exAssistOcc.getDateAppt() != null && !StringUtils.isEmpty(exAssistOcc.getDateAppt().toString())){
			throw new BusException("该执行单已预约，您可以取消该预约之后再次预约！");
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
			DataBaseHelper.update("update sch_ticket set flag_used = '1' where pk_schticket = ?", new Object[] { schTicket.getPkSchticket() });
		} else {
			DataBaseHelper.update("update sch_sch set ticket_no = nvl(ticket_no, '0') + 1 where pk_sch = ?",new Object[] { pkSch });
		}
		// 更新检查申请单
		DataBaseHelper.update("update cn_ris_apply set eu_status = '2' where pk_cnord = ?", new Object[] { pkCnord });
		// 更新病理申请单
		DataBaseHelper.update("update cn_pa_apply set eu_status = '2' where pk_cnord = ?", new Object[] { pkCnord });
		
		if(schTicket == null){
			throw new BusException("未查询到该号，请确认该号是否“可预约”！");
		}
		
		//更新医技执行单预约时间和设备
		DataBaseHelper.update("update ex_assist_occ set date_appt=?, pk_msp=? where pk_assocc=? ", new Object[] {schTicket.getBeginTime(),schRes.getPkMsp(),exAssistOcc.getPkAssocc()});
		
		OrderSuccessParam successParam = new OrderSuccessParam();
		successParam.setExAssistOcc(exAssistOcc);
		successParam.setSchSch(schSch);
		return successParam;
	}
}
