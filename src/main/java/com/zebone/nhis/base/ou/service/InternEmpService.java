package com.zebone.nhis.base.ou.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.base.ou.dao.InternEmpMapper;
import com.zebone.nhis.base.ou.vo.EmpAndInternParam;
import com.zebone.nhis.common.module.base.ou.BdOuEmpIntern;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InternEmpService {
	@Resource
	private InternEmpMapper internEmpMapper;
	
	/**
	 * 查询实习人员信息
	 * 交易号001001003007
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map> seleInternEmpL(String param , IUser user){
		Map paramMap = JsonUtil.readValue(param,Map.class);
		
		return internEmpMapper.seleInternEmpL(paramMap);
		
	}
	

	/**
	 * 保存实习医生信息
	 * 交易号	001001003008
	 * @param param
	 * @param user
	 * @return
	 */
	public BdOuEmployee saveInternEmp(String param,IUser user){
		EmpAndInternParam empAndIntern = JsonUtil.readValue(param, EmpAndInternParam.class);
		BdOuEmployee emp = empAndIntern.getEmp();
		BdOuEmpIntern internEmp = empAndIntern.getInternEmp();
	    BdOuEmpjob empJobVo = empAndIntern.getEmpJobVo();
	  
		if(emp.getPkEmp() == null){
			//新增：校验编码是否机构内唯一
			int count_emp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_employee bd_ou_emp_intern "
					+ "where pk_org = ? and code_emp = ? and DEL_FLAG = '0'",Integer.class,emp.getPkOrg(),emp.getCodeEmp());
			
			if(count_emp == 0){
				String pkEmp=NHISUUID.getKeyId();
				emp.setPkEmp(pkEmp);
				internEmp.setPkEmp(pkEmp);
				empJobVo.setPkEmp(pkEmp);
				emp.setDelFlag("0");
				internEmp.setDelFlag("0");
				empJobVo.setDelFlag("0");
				
				DataBaseHelper.insertBean(emp);
				DataBaseHelper.insertBean(internEmp);
				DataBaseHelper.insertBean(empJobVo);
				//先全删再后插的方式
				//saveIntEmp(empIntern, emp, user);
			}else{
				throw new BusException("人员编码重复！");
			}
		}else {
			//更新：校验编码是否机构内唯一
			int count_emp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_employee "
					+ "where pk_org = ? and code_emp = ? and pk_emp != ? and DEL_FLAG = '0'",Integer.class,emp.getPkOrg(),emp.getCodeEmp(),emp.getPkEmp());
			if(count_emp == 0){
				emp.setDelFlag("0");
				internEmp.setDelFlag("0");
				empJobVo.setDelFlag("0");
				DataBaseHelper.updateBeanByPk(emp, false);
				DataBaseHelper.updateBeanByPk(internEmp,false);
				DataBaseHelper.updateBeanByPk(empJobVo, false);
				//先全删再恢复的方式（软删除）
				//saveEmpjobInfos(empjoblist, emp, user);
			}else{
				throw new BusException("人员编码重复！");
			}
		}
		
		return emp;
		
		
	}
	
	/**
	 * 删除实习医生信息
	 * 交易号001001003009
	 * @param param
	 * @param user
	 */
	public void delEmpIntern(String param , IUser user){
		BdOuEmployee emp = JsonUtil.readValue(param,BdOuEmployee.class);
		int count_user = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
				+ "where pk_emp = ?", Integer.class, emp.getPkEmp());
		if(count_user == 0){
			//--删除人员基本信息
			DataBaseHelper.execute("delete from bd_ou_employee where pk_emp=?", new Object[]{emp.getPkEmp()});
			//--删除人员实习属性
			DataBaseHelper.execute("delete from bd_ou_emp_intern where pk_emp=?", new Object[]{emp.getPkEmp()});
			//--删除人员工作信息
			DataBaseHelper.execute("delete from bd_ou_empjob where pk_emp=?", new Object[]{emp.getPkEmp()});
			}else{
				throw new BusException("人员关联了用户！");
			}
		}
	
	/**
	 * 获取人员详细信息
	 * 交易号001001003010
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getEmpInternInfo(String param , IUser user ){
		BdOuEmployee employee = JsonUtil.readValue(param,BdOuEmployee.class);
		BdOuEmployee emp = DataBaseHelper.queryForBean("select * from bd_ou_employee where pk_emp = ? ", BdOuEmployee.class, employee.getPkEmp()) ;
		if(emp == null){
			throw new BusException("没有获取该人员信息！");
		}
		BdOuEmpIntern empIntern = DataBaseHelper.queryForBean("select * from bd_ou_emp_intern where pk_emp = ?", BdOuEmpIntern.class, employee.getPkEmp());
		BdOuEmpjob empJobVo = DataBaseHelper.queryForBean("select * from bd_ou_empjob where pk_emp = ?", BdOuEmpjob.class, new Object[]{employee.getPkEmp()});
		Map<String, Object> rmap = new HashMap<String, Object>();
		rmap.put("emp", emp);
		rmap.put("internEmp", empIntern);
		rmap.put("empJobVo", empJobVo);
		return rmap;
		
	}
	
	

}
