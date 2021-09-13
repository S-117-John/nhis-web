package com.zebone.nhis.bl.ipcg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.ipcg.dao.QueryMedTechResMapper;
import com.zebone.nhis.bl.ipcg.vo.ApptOrdVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExAssistOcc;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;



/**
 * 医技预约查询
 * @author user
 *
 */

@Service
public class QueryMedTechResService {
	
	@Resource
	private QueryMedTechResMapper queryMedOrdMapper;
	
	/**
	 * 医技预约查询
	 */
	public List<Map<String, Object>> queryMedOrd(String param,IUser user){
		ApptOrdVo aov = JsonUtil.readValue(param, ApptOrdVo.class);
		User u = UserContext.getUser();
		String currentPkDept = u.getPkDept();
		if(aov.getDateBegin() == null || aov.getDateEnd() == null){
			throw new BusException("请输入预约时间");
		}else{
		aov.setStrDateBegin(DateUtils.getDateTimeStr(aov.getDateBegin()));   
		aov.setStrDateEnd(DateUtils.getDateTimeStr(aov.getDateEnd()));
		
		aov.setPkDeptEx(currentPkDept);
		List<Map<String, Object>> laov = queryMedOrdMapper.queryMedOrd(aov);
		return laov;
		}
	}
	
	/**
	 * 取消预约
	 */
	public void cancelMedOrd(String param,IUser user){
		String pkSchappt= JsonUtil.getFieldValue(param, "pkSchappt");
		String pkAssocc= JsonUtil.getFieldValue(param, "pkAssocc");
		User u = UserContext.getUser();
		String nameEmp = u.getNameEmp();
		String pkEmp = u.getPkEmp();
		if(pkSchappt!=null){
			DataBaseHelper.execute("update sch_appt set flag_cancel='1',eu_status='9',pk_emp_cancel=?,name_emp_cancel=?, date_cancel=? where pk_schappt=? and flag_cancel='0'", new Object[]{pkEmp,nameEmp,new Date(),pkSchappt});
			DataBaseHelper.execute("update ex_assist_occ set date_appt='' where pk_assocc=?", pkAssocc);
		}
	}
	
	/**
	 * 跳转医技业务页面时查询
	 * @param param
	 * @param user
	 * @return
	 */
	public ExAssistOcc qryMedOrd(String param,IUser user){
		String pkAssocc = JsonUtil.getFieldValue(param, "pkAssocc");
		ExAssistOcc exAssistOcc = DataBaseHelper.queryForBean("select * from ex_assist_occ where pk_assocc=?", ExAssistOcc.class, pkAssocc);
		return exAssistOcc;
	}
}
