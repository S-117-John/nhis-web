package com.zebone.nhis.pv.reg.support;

import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.sch.plan.SchPlan;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pv.pub.service.TicketPubService;
import com.zebone.nhis.pv.reg.dao.RegSyxMapper;
import com.zebone.nhis.pv.reg.service.RegSyxService;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.pv.reg.vo.RegApptExtVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 处理中二挂号服务无事物处理类（基于考虑挂号时取号并发问题）
 * @author yangxue
 *
 */
@Service
public class RegSyxHandler {
	
	@Resource
	private RegSyxMapper regSyxMapper;
	
	@Resource
	private TicketPubService ticketPubService;
	
	@Resource
	private RegSyxService regSyxService;
	
	
	/**
	 * 保存预约登记信息<br>
	 * 交易号：003002002006
	 * @param param
	 * @param user
	 */
	public PiMasterRegVo saveApptSchRegInfo(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		if(regvo == null)
			throw new BusException("未获取到挂号信息！");
		if(CommonUtils.isNull(regvo.getEuPvtype()))
			throw new BusException("未设置就诊类型euPvtype！");
		if(CommonUtils.isNull(regvo.getPkSch()))
			throw new BusException("未设置排班主键pkSch！");
		if(CommonUtils.isNull(regvo.getPkSchres()))
			throw new BusException("未设置排班资源主键pkSchres！");
		if(CommonUtils.isNull(regvo.getPkSchsrv()))
			throw new BusException("未设置排班服务主键pkSchsrv！");
		if(CommonUtils.isNull(regvo.getPkDateslot()))
			throw new BusException("未设置日期分组主键pkDateslot！");
		if(regvo.getDateAppt()==null)
			throw new BusException("未设置预约日期dateAppt！");
		
		User u = (User) user;
		String pkSch = regvo.getPkSch();
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		//1.判断是否还有可约号
		if(schSch==null||"1".equals(schSch.getFlagStop()))
			throw new BusException("您所选排班不存在或已停用！");
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue()){
			throw new BusException("您所选排班已挂满！");
		}
		SchTicket ticket = null;
		boolean haveTicketNo = haveTicket(schSch.getPkSch());
		//2.占用号源
		if(haveTicketNo){
			// 处理号表
			 ticket = ticketPubService.getUnusedAppTicket(pkSch,regvo.getPkDateslotsec(),regvo.getEuPvtype());
		}else{
			 ticket = ticketPubService.getUnusedAppTicketFromSch(pkSch, regvo.getEuPvtype());
		}
		//3.保存预约登记信息（含保存患者信息）
		try{
			regvo = regSyxService.saveApptSchRegInfo(regvo, schSch, ticket, haveTicketNo, u,false);
		}catch(Exception e){
			e.printStackTrace();
			//还原占用的预约号
			if(haveTicketNo){
			   ticketPubService.setTicketUnused(ticket);
			}else{
			   ticketPubService.setTicketUnusedFromSch(ticket);
			}
			throw new BusException(e.getMessage());
		}
		return regvo;
	}

	public boolean haveTicket(String pkSch){
		return DataBaseHelper.queryForScalar("select count(*) from SCH_TICKET where pk_sch = ?",
				Integer.class, new Object[]{pkSch})>0;
	}
	/**
	 * 保存预约登记信息,webService调用，用于已锁定号源的情况,无交易号
	 * @param param
	 * @param user
	 * @return
	 */
	public String saveApptSchRegInfoExt(String param, IUser user) {
		RegApptExtVo regvo = JsonUtil.readValue(param, RegApptExtVo.class);
		boolean haveTicketNo = haveTicket(regvo.getSchSch().getPkSch());
		//3.保存预约登记信息（含保存患者信息）
		try{
			PiMasterRegVo piMasterRegVo = regSyxService.saveApptSchRegInfo(regvo.getRegvo(), regvo.getSchSch(), regvo.getTicket(), haveTicketNo, regvo.getU(),false);
		}catch(Exception e){
			//还原占用的预约号
			if(haveTicketNo){
				ticketPubService.setTicketUnused(regvo.getTicket());
			}else{
				ticketPubService.setTicketUnusedFromSch(regvo.getTicket());
			}
			throw new BusException(e.getMessage());
		}
		return JsonUtil.writeValueAsString(regvo);
	}

	/**
	 * 保存预约挂号信息(无事物)<br>
	 * 交易号：003002002005
	 * <pre>
	 *  0.获取票号
	 *  1.生成预约记录，写表sch_appt；
        2.生成预约挂号记录，写表sch_appt_pv；
        3.更新排班表，更新sch_sch；
		4.生成就诊记录，写表pv_encounter；
		5.生成门诊就诊，写表pv_op；
		6.生成记费信息，写表bl_op_dt；
		7.生成结算信息，写表bl_settle；
		8.生成结算明细，写表bl_settle_detail；
		9.生成支付记录，写表bl_deposit；
		如果挂号打印发票：
		10.生成发票信息，写表bl_invoice；
		11.生成发票和结算关系，写表bl_st_inv；
		12.生成发票明细，bl_invoice_dt；
		</pre>
	 * @param param
	 * @param user
	 */
	public  PiMasterRegVo saveApptPvRegInfo(String param,IUser user){
		
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		//校验入参是否正确
		this.checkRegInfo(regvo);
		this.checkPiInBlack(regvo.getPkPi());

		User u = (User) user;
		String pkSch = regvo.getPkSch();
		boolean haveTickNo = haveTicket(pkSch);
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		//1.判断是否还有可约号
		if(schSch==null||"1".equals(schSch.getFlagStop())){
			throw new BusException("您所选排班不存在或已停用！");
		}
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue()){
			throw new BusException("您所选排班已挂满！");
		}
		//修改原判断预约号源逻辑，查询-后期待优化
		List<SchTicket> ticketList = DataBaseHelper.queryForList(
				"select * from sch_ticket where pk_sch = ?  and DEL_FLAG = '0' and FLAG_APPT = '1' and flag_stop='0' and FLAG_USED = '0' ",
				SchTicket.class, pkSch);
		if(ticketList.size()<=0){
			throw new BusException("您所选排班已无可预约号！");
		}
		SchTicket ticket = null;
		//2.占用号源
		if(haveTickNo){
			// 处理号表
			 ticket = ticketPubService.getUnusedAppTicket(pkSch,regvo.getPkDateslotsec(),regvo.getEuPvtype(),regvo.getIsAppointClinics());
		}else{
			//无号表方式处理排班表可用号数
			 ticket = ticketPubService.getUnusedAppTicketFromSch(pkSch, regvo.getEuPvtype());
		}
		//3.保存预约挂号信息（含保存患者信息）
		try{
			regvo = regSyxService.saveApptPvRegInfo(regvo, schSch, ticket, haveTickNo, u);
		}catch(Exception e){
			//还原占用的预约号
			if(haveTickNo){
			   ticketPubService.setTicketUnused(ticket);
			}else{
			   ticketPubService.setTicketUnusedFromSch(ticket);
			}
			throw new BusException(e.getMessage());
		}
		return regvo;
	}
	/**
	 * 保存挂号信息(无事物)<br>
	 * 交易号：003002002004 
	 * @param param
	 * @param user
	 * @return
	 */
	public PiMasterRegVo savePvRegInfo(String param,IUser user){
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		//校验入参是否正确
		this.checkRegInfo(regvo);

		User u = (User) user;
		String pkSch = regvo.getPkSch();
		boolean haveTickNo = haveTicket(pkSch);
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		//1.判断是否还有可挂号
		if(schSch==null||"1".equals(schSch.getFlagStop()))
			throw new BusException("您所选排班不存在或已停用！");
		if(schSch.getCntTotal().intValue()<=schSch.getCntUsed().intValue())
			throw new BusException("您所选排班已约满！");
		SchTicket ticket = null;
		//2.占用号源
		if(haveTickNo){
			// 处理号表
			 ticket = ticketPubService.getUnusedTicket(pkSch, regvo.getEuPvtype(),regvo.getIsAppointClinics());
		}else{//无号表方式 ，锁定排班表
			 ticket = ticketPubService.getUnusedTicketFromSch(pkSch, regvo.getEuPvtype());
		}
		//3.保存挂号信息（含保存患者信息）
		try{
			regvo.setTicketNo(ticket.getTicketno());
			regvo = regSyxService.savePvRegInfo(regvo, haveTickNo, u);
		} catch(Exception e){
			e.printStackTrace();
			//还原占用的挂号资源
			if(haveTickNo){
			   ticketPubService.setTicketUnused(ticket);
			}else{
			   ticketPubService.setTicketUnusedFromSch(ticket);
			}
			throw new BusException(e.getMessage());
		}
		return regvo;
	}
	
	/**
	 * 校验挂号信息入参是否正确
	 * @param regvo
	 */
	private void checkRegInfo(PiMasterRegVo regvo){
		if(regvo == null)
			throw new BusException("未获取到挂号信息！");
		if(CommonUtils.isNull(regvo.getEuPvtype()))
			throw new BusException("未设置就诊类型euPvtype！");
		if(CommonUtils.isNull(regvo.getPkSch()))
			throw new BusException("未设置排班主键pkSch！");
		if(CommonUtils.isNull(regvo.getPkSchres()))
			throw new BusException("未设置排班资源主键pkSchres！");
		if(CommonUtils.isNull(regvo.getPkSchsrv()))
			throw new BusException("未设置排班服务主键pkSchsrv！");
		if(CommonUtils.isNull(regvo.getPkDateslot()))
			throw new BusException("未设置日期分组主键pkDateslot！");
	}
	
	/**
	 * 校验已存在的患者是否在黑名单
	 * @param pkPi
	 */
	private void checkPiInBlack(String pkPi){
		if(StringUtils.isNotBlank(pkPi)) {
			Map<String,Object> map = DataBaseHelper.queryForMap("select * from pi_lock lk where lk.pk_pi=? and to_date(?,'yyyy-MM-dd HH24:mi:ss') between DATE_BEGIN and DATE_END", new Object[]{pkPi,DateUtils.getDate("yyyy-MM-dd HH:mm:ss")});
			if(MapUtils.isNotEmpty(map)) {
				throw new BusException("当前患者在预约黑名单中，不允许预约挂号" + 
						(StringUtils.isNotBlank(MapUtils.getString(map, "note"))?":"+MapUtils.getString(map, "note"):""));
			}
		}
	}
	

	/**
	 * 查询患者医保计划<br>
	 * 先获取默认得，如果没有就获取第一条
	 * <br><b>order by hp.sort_no</b>
	 * @param param
	 * @param user
	 * @return
	 */
	public PiInsurance getPiInsurance(String param, IUser user){
		Map<String,Object> mapParam = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {});
		List<PiInsurance> list = DataBaseHelper.queryForList("select hp.* from pi_insurance hp where hp.pk_pi=? and hp.del_flag='0' order by hp.sort_no", PiInsurance.class, MapUtils.getString(mapParam, "pkPi"));
		PiInsurance piInsurance = null;
		if(CollectionUtils.isNotEmpty(list)) {
			piInsurance = (PiInsurance) CollectionUtils.find(list, new BeanPropertyValueEqualsPredicate("flagDef", "1"));
			piInsurance = (piInsurance==null)?list.get(0):piInsurance;
		}
		
		return piInsurance;
	}
}
