package com.zebone.nhis.pv.reg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.common.support.CommonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.sch.plan.SchSch;
import com.zebone.nhis.common.module.sch.pub.SchResource;
import com.zebone.nhis.common.module.sch.pub.SchSrv;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pv.reg.support.RegSyxHandler;
import com.zebone.nhis.pv.reg.support.RegSyxProcessSupport;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.sch.plan.service.SchService;
import com.zebone.nhis.sch.plan.vo.SchSaveParam;
import com.zebone.nhis.sch.pub.support.SchEuStatus;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 排班-挂号快捷生成
 * @author Alvin
 *
 */
@Service
public class RegSyxShortcutService {

	@Autowired
	SchService schService;
	@Autowired
	RegSyxHandler regSyxHandler;
	@Resource
	private RegSyxProcessSupport regSyxProcessSupport;

	@Resource
	private RegSyxService regSyxService;

	/**
	 * 一键排班+挂号<br>
	 * 目前大多数值后台来查询封装处理，减少了前端传入的数据，坏处就是不太灵活..Do what u do
	 * @param param
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void genSchAndCreateReg(String param, IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String,Object>>() {});
		if(MapUtils.isEmpty(paramMap)) {
			throw new BusException("未传入参数");
		}
		String pkSchres = MapUtils.getString(paramMap, "pkSchres");
		String pkSchsrv = MapUtils.getString(paramMap, "pkSchsrv");
		int cntTotal = MapUtils.getIntValue(paramMap, "cntTotal",1);
		
		String pkPi = MapUtils.getString(paramMap, "pkPi");
		String pkInsu = MapUtils.getString(paramMap, "pkHp");
		
		User u = (User)user;
		checkParamOfSch(pkSchres, pkSchsrv);
		
		SchSrv schSrv =  DataBaseHelper.queryForBean("select * from sch_srv where pk_schsrv=?", SchSrv.class, new Object[]{pkSchsrv});
		if(schSrv == null) {
			throw new BusException("依据排班服务主键未获取到对应服务信息");
		}
		SchSch schSch = saveSch(pkSchres, pkSchsrv ,cntTotal, u);
		
		PiMasterRegVo piMaster = DataBaseHelper.queryForBean("select * from PI_MASTER where pk_pi=?", PiMasterRegVo.class, new Object[]{pkPi});
		piMaster.setPkSch(schSch.getPkSch());
		piMaster.setEuSchclass(schSch.getEuSchclass());
		piMaster.setDateReg(new Date());
		piMaster.setPkDept(schSch.getPkDept());
		piMaster.setPkSchsrv(schSrv.getPkSchsrv());
		piMaster.setPkSchres(pkSchres);
		piMaster.setPkDateslot(schSch.getPkDateslot());
		piMaster.setEuSrvtype(schSrv.getEuSrvtype());
		piMaster.setPkSchplan(null);
		piMaster.setEuPvtype(StringUtils.equals(schSrv.getEuSrvtype(), "9")?"2":"1");
		//获取医保信息
		if(StringUtils.isBlank(pkInsu)) {
			pkInsu = regSyxProcessSupport.getQcfHp();
		}
		piMaster.setPkHp(pkInsu);
		regSyxHandler.savePvRegInfo(JsonUtil.writeValueAsString(piMaster), user);
	}


	private SchSch saveSch(String pkSchres, String pkSchsrv,int cntTotal, User u) {
		SchResource schResource =  DataBaseHelper.queryForBean("select * from sch_resource where pk_schres=?", SchResource.class, new Object[]{pkSchres});
		if(schResource == null) {
			throw new BusException("依据排班资源主键未获取到对应资源信息");
		}
		Map<String,Object> mapDateslot = DataBaseHelper.queryForMap("select ds.pk_dateslot from sch_resource res inner join bd_code_dateslot ds on res.dt_dateslottype=ds.dt_dateslottype"
				+ " where res.pk_schres=? and to_date(?,'HH24:MI:SS') between to_date(ds.TIME_BEGIN,'HH24:MI:SS') and to_date(ds.TIME_END,'HH24:MI:SS')"
				,new Object[]{pkSchres,DateUtils.getDate("HH:mm:ss")}); 
		if(MapUtils.isEmpty(mapDateslot)) {
			throw new BusException("未获取到午别信息");
		}
		String pkDateslot = MapUtils.getString(mapDateslot, "pkDateslot");
		List<SchSaveParam> listSch = new ArrayList<>();
		SchSaveParam schSaveParam = new SchSaveParam();
		SchSch schSch = new SchSch();
		schSch.setDateWork(new Date());
		schSch.setPkDateslot(pkDateslot);
		schSch.setPkPlanweek(null);
		schSch.setPkSchplan(null);
		schSch.setPkSchres(pkSchres);
		schSch.setPkSchsrv(pkSchsrv);
		schSch.setCntTotal(cntTotal);
		schSch.setCntAdd(0);
		schSch.setCntAppt(0);
		schSch.setCntOver(0);
		schSch.setCntUsed(0);
		schSch.setFlagStop("0");
		schSch.setMinutePer(schResource.getMinutePer().intValue());
		schSch.setPkOrg(u.getPkOrg());
		schSch.setPkDept(schResource.getPkDeptBelong());
		schSch.setEuSchclass(schResource.getEuSchclass());
		schSch.setFlagModi("1");
		schSch.setEuStatus(SchEuStatus.AUDIT.getStatus());
		schSch.setEuAppttype("0");
		schSch.setPkDeptunit(null);
		schSch.setPkEmpChk(u.getPkEmp());
		schSch.setNameEmpChk(u.getNameEmp());
		schSch.setNote(u.getUserName() + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "生成排班！");
		schSaveParam.setSchSch(schSch);
		listSch.add(schSaveParam);
		List<SchSaveParam> list = schService.saveSch(JsonUtil.writeValueAsString(listSch), (IUser)u);
		if(CollectionUtils.isEmpty(list)) {
			throw new BusException("未得到排班信息");
		}
		return list.get(0).getSchSch();
	}
	
	private void checkParamOfSch(String pkSchres,String pkSchsrv){
		if(StringUtils.isBlank(pkSchres)) {
			throw new BusException("未传入排班资源主键");
		}
		if(StringUtils.isBlank(pkSchsrv)) {
			throw new BusException("未传入排班服务主键");
		}
	}

	/**
	 * 保存挂号信息,新临时挂号（无排班、号表、也不生成）<br>
	 * @param param
	 * @param user
	 * @return
	 */
	public PiMasterRegVo savePvRegInfo(String param,IUser user) {
		PiMasterRegVo regvo = JsonUtil.readValue(param, PiMasterRegVo.class);
		if(regvo == null)
			throw new BusException("未获取到挂号信息！");
		if(CommonUtils.isNull(regvo.getEuPvtype()))
			throw new BusException("未设置就诊类型euPvtype！");
		if(CommonUtils.isNull(regvo.getPkSchres()))
			throw new BusException("未设置排班资源主键pkSchres！");
		if(CommonUtils.isNull(regvo.getPkSchsrv()))
			throw new BusException("未设置排班服务主键pkSchsrv！");
		if(CommonUtils.isNull(regvo.getPkDateslot()))
			throw new BusException("未设置日期分组主键pkDateslot！");
		User u = (User) user;
		regvo.setTicketNo("-999");
		return regSyxService.savePvRegInfo(regvo, false, u);
	}
}
