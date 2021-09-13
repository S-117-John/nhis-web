package com.zebone.nhis.base.ou.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.ou.dao.EmpMapper;
import com.zebone.nhis.base.ou.vo.EmpAndJobParam;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 人员服务
 * @author Xulj
 *
 */
@Service
public class EmpService {
	
	/**
	 * 添加状态
	 **/
	public static final String AddState = "_ADD";

	/**
	 * 更新状态
	 */
	public static final String UpdateState = "_UPDATE";

	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";

	@Resource
	private EmpMapper empMapper;
	
	
	//增删改标志
	private String rleCode = "";
	

	
	
	
	/**
	 * 根据表单获取人员信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map> getEmpInfos(String param , IUser user){
		
		Map paramMap = JsonUtil.readValue(param,Map.class);
		
		return empMapper.getEmpInfos(paramMap); 
	}
	
	/**
	 * 获取部门下所有人员
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map> getDeptEmpInfos(String param , IUser user){
		Map paramMap = JsonUtil.readValue(param,Map.class);
		if("9".equals(paramMap.get("delFlag"))){
			paramMap.put("delFlag",null);
		}
		return empMapper.getDeptEmpInfos(paramMap);
	}
	
	/**
	 * 新增 更新 人员
	 * @param param
	 * @param user
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BdOuEmployee saveEmpAndJobInfos(String param , IUser user){
		
		EmpAndJobParam empAndJob = JsonUtil.readValue(param, EmpAndJobParam.class);
		//2020—10-29，中山人民医院消息推送需要修改前数据
		EmpAndJobParam updatEmpAndJobParam =new EmpAndJobParam();
		if(empAndJob.getEmp().getPkEmp()!=null){
			BdOuEmployee bdOuEmployee = DataBaseHelper.queryForBean("select * from bd_ou_employee where pk_emp = ? ", BdOuEmployee.class,empAndJob.getEmp().getPkEmp());
			List<BdOuEmpjob> bdOuEmpjobList = DataBaseHelper.queryForList("select * from bd_ou_empjob where pk_emp=?", BdOuEmpjob.class,empAndJob.getEmp().getPkEmp());
			updatEmpAndJobParam.setEmp(bdOuEmployee);
			updatEmpAndJobParam.setEmpJobs(bdOuEmpjobList);
		}
		BdOuEmployee bdEmp = insertEmpAndJobInfos(empAndJob,user);

		//该判断为了防止发送和接收死循环，当平台推过来的消息不发送消息
		if(StringUtils.isBlank(empAndJob.getFlagSendMsg())||"1".equals(empAndJob.getFlagSendMsg())){
			Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
			paramMap.put("beforeModification",updatEmpAndJobParam);//2020—10-29，中山人民医院消息推送需要修改前数据
			paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
			paramMap.put("STATUS", this.rleCode);

			PlatFormSendUtils.sendBdOuEmpMsg(paramMap);
		}
		return bdEmp;
	}
	
	/**
	 * 新增人员信息
	 * @param empAndJob
	 * @return
	 */
	public BdOuEmployee insertEmpAndJobInfos(EmpAndJobParam empAndJob, IUser user){
		BdOuEmployee emp = empAndJob.getEmp();
		
		List<BdOuEmpjob> empjoblist = empAndJob.getEmpJobs();
		if(StringUtils.isEmpty(emp.getIdno())) emp.setIdno(""); //中山要求不输入身份证，数据库身份证字段为非空字段
		
		if(emp.getPkEmp() == null){
			//新增：校验编码是否机构内唯一
			int count_emp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_employee "
					+ "where pk_org = ? and code_emp = ? and DEL_FLAG = '0'",Integer.class,emp.getPkOrg(),emp.getCodeEmp());
			if(count_emp == 0){
				DataBaseHelper.insertBean(emp);
				//先全删再后插的方式
				saveEmpjobInfos(empjoblist, emp, user);
				//'新增'标志
				rleCode=this.AddState;
			}else{
				throw new BusException("人员编码重复！");
			}
			
		}else{
			//更新：校验编码是否机构内唯一
			int count_emp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_employee "
					+ "where pk_org = ? and code_emp = ? and pk_emp != ? and DEL_FLAG = '0'",Integer.class,emp.getPkOrg(),emp.getCodeEmp(),emp.getPkEmp());
			if(count_emp == 0){
				DataBaseHelper.updateBeanByPk(emp, false);
				//先全删再恢复的方式（软删除）
				saveEmpjobInfos(empjoblist, emp, user);
				//'修改'标志
				rleCode=this.UpdateState;
			}else{
				throw new BusException("人员编码重复！");
			}
			
		}
		return emp;
	}
	
	/**
	 * 新增 更新 人员工作关系
	 * @param
	 * @param user
	 */
	private void saveEmpjobInfos(List<BdOuEmpjob> empjoblist, BdOuEmployee emp, IUser user){
		
		if(empjoblist != null && empjoblist.size() != 0){
			//先全删再恢复的方式（软删除）
			String pkEmp = emp.getPkEmp();
			DataBaseHelper.update("update bd_ou_empjob set del_flag = '1' where pk_emp = ?", new Object[]{pkEmp});
			for(BdOuEmpjob empjob : empjoblist){
				if(empjob.getPkEmpjob() != null){
					empjob.setDelFlag("0");//恢复
					empjob.setPkEmp(pkEmp);
					DataBaseHelper.updateBeanByPk(empjob,false);
				}else{
					empjob.setPkEmp(pkEmp);
					DataBaseHelper.insertBean(empjob);
				}
			}
		}else{
			throw new BusException("请为该人员设置至少一条工作关系！");
		}
		
	}
	
	/**
	 * 获取人员详细信息
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getEmpAndJobInfos(String param , IUser user){
		
		BdOuEmployee employee = JsonUtil.readValue(param,BdOuEmployee.class);
		BdOuEmployee emp = DataBaseHelper.queryForBean("select * from bd_ou_employee where pk_emp = ? ", BdOuEmployee.class, employee.getPkEmp()) ;
		if(emp == null){
			throw new BusException("没有获取该人员信息！");
		}
		
		List<Map<String, Object>> empjobList = DataBaseHelper.queryForList("select * from bd_ou_empjob where pk_emp = ? and del_flag = '0'", employee.getPkEmp());
		Map<String, Object> rmap = new HashMap<String, Object>();
		rmap.put("emp", emp);
		rmap.put("empJobs", empjobList);
		return rmap;
	}
	
	/**
	 * 删除人员信息
	 * @param param
	 * @param user
	 */
	public void deleteEmpInfos(String param , IUser user){
		
		BdOuEmployee emp = JsonUtil.readValue(param,BdOuEmployee.class);
		if ("0".equals(emp.delFlag)){
			int count_user = DataBaseHelper.queryForScalar("select count(1) from bd_ou_user "
					+ "where pk_emp = ?", Integer.class, emp.getPkEmp());

			if(count_user == 0){
				//'删除'标志
				rleCode=this.DelState;
				//同时删除该人员所有关系
				//DataBaseHelper.update("delete from bd_ou_empjob  where pk_emp = ?", new Object[]{emp.getPkEmp()});
				//删除人员
				DataBaseHelper.update("update bd_ou_employee set del_flag='1'  where pk_emp = ?  and del_flag='0'", new Object[]{emp.getPkEmp()});
				//发送消息至平台
				Map<String,Object> paramMap = new HashMap<String,Object>();
				paramMap.put("codeEmp", UserContext.getUser().getCodeEmp());
				paramMap.put("STATUS", this.rleCode);
				paramMap.put("emp", JsonUtil.readValue(param,Map.class));
				PlatFormSendUtils.sendBdOuEmpMsg(paramMap);
			}else{
				throw new BusException("人员关联了用户！");
			}
		}else{
			DataBaseHelper.update("update bd_ou_employee set del_flag='0' where pk_emp = ? and del_flag='1'",new Object[]{emp.getPkEmp()});
		}

	}
	

}

