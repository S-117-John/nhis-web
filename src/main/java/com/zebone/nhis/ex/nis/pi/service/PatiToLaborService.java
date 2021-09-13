package com.zebone.nhis.ex.nis.pi.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.labor.nis.PvLabor;
import com.zebone.nhis.labor.pub.service.PiLaborPubService;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 病区入产房业务类
 * @author yangxue
 *
 */
@Service
public class PatiToLaborService {
	@Resource
	private PiLaborPubService piLaborPubService;//注入产妇档案信息
	/**
	 * 查询孕妇档案信息
	 * @param param{pkPi}
	 * @return
	 */
	public PvLabor queryLabor(String param,IUser user){
		String pk_pi = JsonUtil.readValue(param, String.class);
		return DataBaseHelper.queryForBean("select pk_pilabor,date_prebirth from pi_labor where pk_pi = ? ", PvLabor.class, new Object[]{pk_pi});
	}
	
	 /**
     * 保存病区入产房信息
     * @param param{pkPv,pkDeptNsSrc,pkDeptSrc,pkDept,pkDeptNs,labWeek,pkPilabor,datePrebirth}
     * @param user
     */
    public void saveLaborInfo(String param,IUser user){
    	PvLabor lab = JsonUtil.readValue(param, PvLabor.class);
    	if(lab == null ){
    		throw new BusException("未获取到入产房的相关信息！");
    	}
    	lab.setEuStatus("0");
    	lab.setFlagIn("0");
    	DataBaseHelper.insertBean(lab);
    }
}
