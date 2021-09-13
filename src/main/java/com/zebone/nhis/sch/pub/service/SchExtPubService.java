package com.zebone.nhis.sch.pub.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.zebone.nhis.common.dao.BaseCodeMapper;
import com.zebone.nhis.common.module.pi.PiAcc;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.support.PiConstant;
import com.zebone.nhis.common.module.sch.appt.SchAppt;
import com.zebone.nhis.common.module.sch.appt.SchApptPv;
import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.plan.SchTicket;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.SysConstant;
import com.zebone.nhis.sch.pub.dao.SchExtPubMapper;
import com.zebone.nhis.sch.pub.vo.SchForExtVo;
import com.zebone.nhis.webservice.support.LbSelfUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 排班预约对外接口（可供微信、支付宝等网上预约挂号使用）
 * @author yangxue
 *
 */
@Service
public class SchExtPubService {
	@Resource
	private SchExtPubMapper schExtPubMapper;
	@Autowired
	private BaseCodeMapper codeMapper;
	
	/**
	 * 获取临床科室信息列表
	 * @param param{pkOrg:机构主键}
	 * @param user 
	 * @return[{"pkDept":部门主键,"codeDept":"科室编码", "nameDept":"科室名称", "sortno":"排序号","deptDesc":"科室简介"},...]
	 */
   public List<Map<String,Object>> getDeptList(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap==null||paramMap.get("pkOrg")==null||CommonUtils.isEmptyString(paramMap.get("pkOrg").toString()))
		   throw new BusException("未获取到机构主键！");
	   return schExtPubMapper.queryDeptList(paramMap);
   }
   
   /**
	 * 获取临床医生信息列表
	 * @param param{codeDept:科室编码}
	 * @param user 
	 * @return[{pkDept:所属科室主键,codeEmp:医生编码,pkEmp:医生主键,nameEmp:医生名称,nameSex：性别,docTitle：职称,dtSex：性别编码,idno：身份证号,spec：专长},...]
	 */
  public List<Map<String,Object>> getDoctorList(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap==null||paramMap.get("codeDept")==null||CommonUtils.isEmptyString(paramMap.get("codeDept").toString()))
		   throw new BusException("未获取到科室编码！");
	   return schExtPubMapper.queryDoctorList(paramMap);
  }
   
  /**
   * 保存患者信息
   * @param param{namePi，idNo，mobile，dtSex，birth_date}
   * @param user
   * @return
   */
  public Map<String,Object> savePiMaster(String param,IUser user){
	  PiMaster pi =  JsonUtil.readValue(param, PiMaster.class);
	  Map<String,Object> result = new HashMap<String,Object>();
	  if(pi==null){
		  result.put("message", "未获取到患者信息!");
		  result.put("result", "false");
		  return result;
	  }
	  if(CommonUtils.isEmptyString(pi.getNamePi())){
		  result.put("message", "未获取到患者姓名!");
		  result.put("result", "false");
		  return result;
	  }
	  if(CommonUtils.isEmptyString(pi.getIdNo())){
		  result.put("message", "未获取到患者身份证号!");
		  result.put("result", "false");
		  return result;
	  } 
	  if(CommonUtils.isEmptyString(pi.getMobile())){
		  result.put("message", "未获取到患者手机号!");
		  result.put("result", "false");
		  return result;
	  } 
	  if(CommonUtils.isEmptyString(pi.getPkPicate())){
		  result.put("message", "未获取到患者分类!");
		  result.put("result", "false");
		  return result;
	  }
	  //校验是否已经注册
	  PiMaster temp_pi  = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where id_no = ? and dt_idtype='01' ", PiMaster.class, pi.getIdNo());
	  if(temp_pi!=null){
		  result.put("message", "该身份证号的患者已经注册!");
		  result.put("result", "false");
		  result.put("codePi", temp_pi.getCodePi());
		  result.put("namePi", temp_pi.getNamePi());
		  result.put("pkPi", temp_pi.getPkPi());
		  result.put("codeOp", temp_pi.getCodeOp());
		  return result;
	  }
	  pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));
	  pi.setCodeOp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_MZBL));
	  pi.setCodeIp(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZYBL));
	  pi.setBirthDate(DateUtils.strToDate(pi.getIdNo().substring(6,14)+"000000"));
	  ApplicationUtils.setDefaultValue(pi, true);
	  DataBaseHelper.insertBean(pi);
	  savePiAccByNew(pi);//新增时，插入一条PiAcc记录

	   //设置默认的医保计划
	   //查询自费医保计划主键
		Map<String, Object> bdhpMap = DataBaseHelper.queryForMap("SELECT * FROM bd_hp WHERE eu_hptype=? and del_flag='0' ", "0");
		//保存患者医保计划 ---自助机默认自费
		PiInsurance insu = new PiInsurance();
		insu.setPkHp(LbSelfUtil.getPropValueStr(bdhpMap,"pkHp"));
		insu.setPkPi(pi.getPkPi());
		insu.setSortNo(Long.valueOf("1"));
	
		insu.setDateBegin(new Date());//生效日期
		insu.setDateEnd(DateUtils.getTimeForOneYear(10));//失效日期
	
		insu.setFlagDef("1");//设置默认
		insu.setDelFlag("0");
		insu.setCreator(pi.getPkEmp());//创建人
		insu.setCreateTime(new Date());
		insu.setTs(new Date());
		DataBaseHelper.insertBean(insu);
	  result.put("message", "保存成功!");
	  result.put("result", "true");
	  result.put("codePi", pi.getCodePi());
	  result.put("namePi", pi.getNamePi());
	  result.put("pkPi", pi.getPkPi());
	  result.put("codeOp", pi.getCodeOp());
	  return result;
  }
  /**
   * 更新患者信息
   * @param param{namePi，idNo，mobile,codePi}
   * @param user
   * @return
   */
  public Map<String,Object> updatePiMaster(String param,IUser user){
	  PiMaster pi =  JsonUtil.readValue(param, PiMaster.class);
	  Map<String,Object> result = new HashMap<String,Object>();
	  if(pi==null){
		  result.put("message", "未获取到患者信息!");
		  result.put("result", "false");
		  return result;
	  }
	  if(CommonUtils.isEmptyString(pi.getNamePi())){
		  result.put("message", "未获取到患者姓名!");
		  result.put("result", "false");
		  return result;
	  }
	  if(CommonUtils.isEmptyString(pi.getIdNo())){
		  result.put("message", "未获取到患者身份证号!");
		  result.put("result", "false");
		  return result;
	  } 
	  if(CommonUtils.isEmptyString(pi.getMobile())){
		  result.put("message", "未获取到患者手机号!");
		  result.put("result", "false");
		  return result;
	  } 
	  //校验是否已经注册
	  PiMaster temp_pi  = DataBaseHelper.queryForBean("select code_pi,name_pi,pk_pi from pi_master where code_pi = ?", PiMaster.class, pi.getCodePi());
	  if(temp_pi==null){
		  result.put("message", "未查询到该患者编码对应的患者!");
		  result.put("result", "false");
		  return result;
	  }
	  temp_pi.setNamePi(pi.getNamePi());
	  temp_pi.setIdNo(pi.getIdNo());
	  temp_pi.setMobile(pi.getMobile());
	  ApplicationUtils.setDefaultValue(temp_pi, false);
	  DataBaseHelper.updateBeanByPk(temp_pi, false);
	  result.put("message", "更新成功!");
	  result.put("result", "true");
	  return result;
  }
  /**
	 * 获取排班信息
	 * 
	 * @return
	 */
	public List<SchForExtVo> getSchInfo(String param, IUser user) {
		String pkOrg = ((User) user).getPkOrg();
		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		params.put("euSchclass", "0");
		List<SchForExtVo> list = schExtPubMapper.getSchInfo(params);
		return list;
	}
	/**
	 * 保存预约挂号信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> applyRegister(String param, IUser user) {
		User u = (User) user;
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		String pkPi = paramMap.get("pkPi");
		String pkSch = paramMap.get("pkSch");
		String ticketNo = paramMap.get("ticketNo");
		String dtApptype=paramMap.get("dtApptype");
		String pkApptpv = null;//paramMap.get("pkApptpv");
		Map<String,Object> result = new HashMap<String,Object>();
		
		SchSch schSch = DataBaseHelper.queryForBean("select * from sch_sch where del_flag = '0' and pk_sch = ?",
				SchSch.class, pkSch);
		/**原有方法**
		int count = DataBaseHelper.queryForScalar("select count(1) from sch_appt "
				+ "where pk_pi = ? and "
				+ "date_appt >= ? and "
				+ "date_appt <= ? and "
				+ "pk_sch = ? and "
				+ "flag_cancel = '0'", Integer.class, new Object[]{pkPi,schSch.getDateWork(),schSch.getDateWork(),pkSch});*/
		
		StringBuilder sql = new StringBuilder();
		sql.append("select count(1) from  sch_appt appt INNER JOIN sch_sch sch  on appt.pk_sch = sch.pk_sch  ");
		sql.append("and sch.del_flag='0' and sch.pk_dept=? ");
		sql.append("where appt.pk_pi =? and appt.date_appt >=? and appt.date_appt <=? and appt.flag_cancel = '0' ");
		int count = DataBaseHelper.queryForScalar(sql.toString(), Integer.class, new Object[] { schSch.getPkDept(),
				pkPi, schSch.getDateWork(), schSch.getDateWork() });
		if (count > 0) {
			result.put("message", "已经预约过了，不能再次预约；只能预约次日及以后日期的挂号！");
			result.put("result", "false");
			return result;
		}

		SchResource schRes = DataBaseHelper.queryForBean(
				"select * from SCH_RESOURCE where del_flag = '0' and pk_schres = ?", SchResource.class,
				schSch.getPkSchres());
		PiMaster piMaster = DataBaseHelper.queryForBean("select * from pi_master where del_flag = '0' and pk_pi = ?",
				PiMaster.class, pkPi);

		SchTicket schTicket = DataBaseHelper.queryForBean(
				"select * from sch_ticket where pk_sch = ? and ticketno = ? and DEL_FLAG = '0' and FLAG_APPT = '1' and FLAG_USED = '0'",
				SchTicket.class, pkSch, ticketNo);

		SchAppt schAppt = new SchAppt();
		if(pkApptpv==null||pkApptpv.equals("")){
			pkApptpv = NHISUUID.getKeyId();
		}
		schAppt.setPkSchappt(pkApptpv);
		schAppt.setEuSchclass("0");
		schAppt.setPkSch(pkSch);
		schAppt.setCode(schAppt.getPkSchappt());
		schAppt.setDateAppt(schSch.getDateWork());
		schAppt.setPkDateslot(schSch.getPkDateslot());
		schAppt.setPkSchres(schSch.getPkSchres());
		schAppt.setPkSchsrv(schSch.getPkSchsrv());
		schAppt.setTicketNo(ticketNo);
		if (schTicket != null) {
			schAppt.setBeginTime(schTicket.getBeginTime());
			schAppt.setEndTime(schTicket.getEndTime());
		} else {
			schAppt.setBeginTime(schSch.getDateWork());
			schAppt.setEndTime(schSch.getDateWork());
		}
		schAppt.setPkPi(piMaster.getPkPi());
		
		//处理预约渠道
		if(CommonUtils.isEmptyString(dtApptype)){
			schAppt.setDtApptype("00");//预约渠道
		}else{
			schAppt.setDtApptype(dtApptype);//预约渠道
		}
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
			Map<String,Object> nameMap = DataBaseHelper.queryForMap("select name_emp from BD_OU_EMPLOYEE where pk_emp = ?", schRes.getPkEmp());
			if(nameMap!=null&&nameMap.get("nameEmp")!=null){
				schApptPv.setNameEmpPhy(nameMap.get("nameEmp").toString());
			}
			
		}
		schApptPv.setFlagPv("0");
		DataBaseHelper.insertBean(schApptPv);

//		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1,cnt_appt = cnt_appt - 1 where pk_sch = ?",
//				new Object[] { pkSch });
		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used + 1 where pk_sch = ?",
				new Object[] { pkSch });
		if (schTicket != null) {
			DataBaseHelper.update("update sch_ticket set flag_used = '1' where pk_schticket = ?",
					new Object[] { schTicket.getPkSchticket() });
		} else {
			DataBaseHelper.update("update sch_sch set ticket_no = nvl(ticket_no, '0') + 1 where pk_sch = ?",
					new Object[] { pkSch });
		}

		schSch.setCntUsed(schSch.getCntUsed() + 1);
		result.put("message", "预约成功！");
		result.put("result", "true");
		result.put("pkSchappt", pkApptpv);
		result.put("code", ApplicationUtils.getCode("0101"));
		return result;
	}
	
	/**
	 * 取消预约挂号信息
	 * @param param
	 * @param user
	 */
	public Map<String,Object> cancelApplyRegister(String param, IUser user) {
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
		Map<String,Object> result = new HashMap<String,Object>();
		if(schAppt == null){
			result.put("message", "该登记不存在！");
			result.put("result", "false");
			return result;
		}
		//DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1,cnt_appt = cnt_appt + 1 where pk_sch = ?", new Object[] { schAppt.getPkSch() });
		DataBaseHelper.update("update sch_sch set cnt_used = cnt_used - 1 where pk_sch = ?", new Object[] { schAppt.getPkSch() });
		int count = DataBaseHelper.queryForScalar(
				"select count(1) from sch_ticket " + "where DEL_FLAG = '0' and pk_sch = ?", Integer.class,
				new Object[]{schAppt.getPkSch()});
		if (count > 0) {
			DataBaseHelper.update("update sch_ticket set flag_used = '0' where pk_sch = ? and ticketno = ?",
					new Object[]{schAppt.getPkSch(),schAppt.getTicketNo()});
		}
		result.put("message", "取消预约成功！");
		result.put("result", "true");
		return result;
	}
	
	/**
	 * 获取排班信息(微信)
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getSchInfoForSelf(String param, IUser user) {
		String pkOrg = ((User) user).getPkOrg();
		Map<String, String> params = JsonUtil.readValue(param, Map.class);
		params.put("pkOrg", pkOrg);
		params.put("euSchclass", "0");
		List<Map<String, Object>> list = schExtPubMapper.getSchInfoForSelf(params);
		return list;
	}
	
	/**
	 * 获取患者预约未就诊挂号信息 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getPiAppointment(String param, IUser user) {
		Map<String, String> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String, Object>> list = schExtPubMapper.getPiAppointment(paramMap);
		return list;
	}

	/** 新增的时候保存一条账户信息  */
	public void savePiAccByNew(PiMaster master) {
		PiAcc acc = new PiAcc();
		acc.setPkPi(master.getPkPi());
		acc.setCodeAcc(master.getCodeIp());
		acc.setAmtAcc(BigDecimal.ZERO);
		acc.setCreditAcc(BigDecimal.ZERO);
		acc.setEuStatus(PiConstant.ACC_EU_STATUS_1);
		DataBaseHelper.insertBean(acc);
	}
	
}
