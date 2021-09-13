package com.zebone.nhis.base.ou.service;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.zebone.nhis.base.ou.dao.TraineeSetMapper;
import com.zebone.nhis.common.module.base.ou.BdOuEmpIntern;
import com.zebone.nhis.common.module.sch.schta.SchTa;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class TraineeSetService {
	
	@Resource
	private TraineeSetMapper traineeSetMapper;
	
	/**
	 * 查询实习生信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getTraineeList(String param, IUser user){
		String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
		List<Map<String, Object>> list = traineeSetMapper.qryTraineeSet(pkEmp);	
		return list;					
	}
	
	/**
	 * 删除实习生的信息
	 * @param param
	 * @param user
	 */
	public void delTrainee(String param, IUser user) {
		String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");			
		User user1 = (User)user;
		Map<String, Object> map = new TreeMap<>();
		map.put("pkEmpTeach", user1.getPkEmp());
		map.put("pkEmp", pkEmp);
		traineeSetMapper.delTrainee(map);		
	}
	
	//保存实习生信息
	public void saveTraineeSetList(String param, IUser user) {
		
		BdOuEmpIntern intern = JsonUtil.readValue(param,BdOuEmpIntern.class);			
		String name = UserContext.getUser().getNameEmp();
		User user1 = (User)user;						
		List<Map<String, Object>> traineeSets = traineeSetMapper.qryTraineeSetByPkEmp(intern.getPkEmp());				
		if(traineeSets.size()>0)
		{						
			Map<String,Object> map = new TreeMap<>();
		    map.put("pkEmpTeach", user1.getPkEmp());
		    map.put("nameEmpTeach", name);
			map.put("dateBegin", intern.getDateBegin());
			map.put("dateEnd", intern.getDateEnd());
			map.put("cycle", intern.getCycle());
			map.put("note", intern.getNote());
			map.put("delFlag","0");
			map.put("pkEmp", intern.getPkEmp());
			traineeSetMapper.updateTraineeByPkEmp(map);
			
			SchTa schTa = new SchTa();			
			schTa.setPkOrg(user1.getPkOrg());
			schTa.setDtTaschtype("01");
			schTa.setPkEmp(intern.getPkEmp());
			schTa.setPkDept(user1.getPkDept());
			schTa.setPkEmpTeacher(user1.getPkEmp());
			schTa.setNameEmpTeacher(user1.getNameEmp());
			schTa.setDateBegin(intern.getDateBegin());
			schTa.setDateEnd(intern.getDateEnd());
			schTa.setDtTatype(null);
			schTa.setNote(intern.getNote());
			schTa.setDateSch(new Date());
			schTa.setPkEmpSch(user1.getPkEmp());
			schTa.setNameEmpSch(user1.getNameEmp());
			schTa.setDelFlag("0");
			DataBaseHelper.insertBean(schTa);
		}else {
			intern.setDtInternsrc(null);
			intern.setPkOrg(user1.getPkOrg());
			intern.setEuTeachtype("1");
			intern.setEuWorktype("0");
			intern.setNameEmpTeach(name);			
			intern.setPkEmpTeach(user1.getPkEmp());
			DataBaseHelper.insertBean(intern);
			
			SchTa schTa = new SchTa();			
			schTa.setPkOrg(user1.getPkOrg());
			schTa.setDtTaschtype("01");
			schTa.setPkEmp(intern.getPkEmp());
			schTa.setPkDept(user1.getPkDept());
			schTa.setPkEmpTeacher(user1.getPkEmp());
			schTa.setNameEmpTeacher(user1.getNameEmp());
			schTa.setDateBegin(intern.getDateBegin());
			schTa.setDateEnd(intern.getDateEnd());
			schTa.setDtTatype(null);
			schTa.setNote(intern.getNote());
			schTa.setDateSch(new Date());
			schTa.setPkEmpSch(user1.getPkEmp());
			schTa.setNameEmpSch(user1.getNameEmp());
			schTa.setDelFlag("0");
			DataBaseHelper.insertBean(schTa);
		}
	}
	
	/*
	 * 得到导师姓名
	 */
	public String getTeachName(String param , IUser user) {
		String pkEmp = JsonUtil.getFieldValue(param, "pkEmp");
		String teacherName = traineeSetMapper.getTeacherName(pkEmp);
		if(teacherName!= null && teacherName.equals(UserContext.getUser().getNameEmp())) {
			teacherName = null;
		}
		return teacherName;
	}	
}
