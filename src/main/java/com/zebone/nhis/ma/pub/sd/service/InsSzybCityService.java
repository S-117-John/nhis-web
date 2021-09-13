package com.zebone.nhis.ma.pub.sd.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybOrder;
import com.zebone.nhis.common.module.compay.ins.shenzhen.XtGzrz;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.pub.syx.vo.CostForecastVo;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.ma.pub.syx.vo.ExOpSchVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;

/**
 * 深圳市医保业务处理服务
 * @author huanghaisheng
 *
 */
@Service
public class InsSzybCityService {
	//查询系统登录人的深圳市医保相关信息
	public void loginUserQry(Object[] args) {
		if (args != null) {
			IUser user =(IUser)args[0];
			User u = (User) user;
			Map<String,Object> rtnM =(Map<String,Object>)args[1];
			
			String sqlStr = "SELECT dict.code_insur FROM bd_ou_employee emp INNER JOIN ins_szyb_dictmap dict ON emp.pk_emp = dict.pk_his WHERE emp.pk_emp =? AND dict.code_type = 'YS001' AND dict.del_flag = 0 AND dict.FLAG_STOP = 0";
		    List<Map<String,Object>> codeInsurList = DataBaseHelper.queryForList(sqlStr, new Object[]{u.getPkEmp()});
		    rtnM.put("SughDocCodeInsur", (codeInsurList==null ||codeInsurList.size()==0)?"":codeInsurList.get(0).get("codeInsur"));
		}
	}
	
	/*
	 * 保存医保相关日志
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveSbLog(List<XtGzrz> logList) throws Exception {
		Date dateNow = new Date();
		for(XtGzrz log : logList){
			log.setRzDate(dateNow);
			log.setIntendingDate(dateNow);
			//发送平台消息
	    	if(!CommonUtils.isEmptyString(log.getKsms())){
	    		Map<String,Object> paramMap = new HashMap<>(16);
	    		paramMap.put("content", log.getKsms());
				paramMap.put("type", "ZDYJS");
				PlatFormSendUtils.sendCallPayMsg(paramMap);
	    	}
		}
		
		String sqlStr="insert into xt_gzrz(rz_date,type,demo,type1,opera,engineer,ks,ksms,unit_dept,user_group,intending_date,import_flag)"+
    			" values(:rzDate,:type,:demo,:type1,:opera,:engineer,:ks,:ksms,:unitDept,:userGroup,:intendingDate,:importFlag)";
		DataBaseHelper.batchUpdate(sqlStr ,logList);
	}
	
}
