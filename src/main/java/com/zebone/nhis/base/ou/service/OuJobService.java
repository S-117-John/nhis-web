package com.zebone.nhis.base.ou.service;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuJob;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 岗位服务
 * @author Xulj
 *
 */
@Service
public class OuJobService {

	/**
	 * 保存 更新 岗位
	 * @param param
	 * @param user
	 * @return
	 */
	public BdOuJob saveJob(String param, IUser user){
		
		BdOuJob job = JsonUtil.readValue(param, BdOuJob.class);
		
		if(job.getPkJob() == null){
			int count_job = DataBaseHelper.queryForScalar("select count(1) from bd_ou_job "
					+ "where del_flag = '0' and code_job = ? and pk_org like ?||'%'", Integer.class, job.getCodeJob(),job.getPkOrg());
			if(count_job == 0){
				DataBaseHelper.insertBean(job);
			}else{
				throw new BusException("岗位编码重复！");
			}
		}else{
			int count_job = DataBaseHelper.queryForScalar("select count(1) from bd_ou_job "
					+ "where del_flag = '0' and code_job = ? and pk_org like ?||'%' and pk_job != ?", Integer.class, job.getCodeJob(),job.getPkOrg(),job.getPkJob());
			if(count_job == 0){
				DataBaseHelper.updateBeanByPk(job,false);
			}else{
				throw new BusException("岗位编码重复！");
			}
		}
		return job;
	}
	
	/**
	 * 删除岗位
	 * @param param
	 * @param user
	 */
	public void delJob(String param, IUser user){
		
		String pkJob = JsonUtil.getFieldValue(param, "pkJob");
		
		int job_count = DataBaseHelper.queryForScalar("select count(*) from bd_ou_job where del_flag = '0' and pk_father = ?", Integer.class, pkJob);
		int empjob_count = DataBaseHelper.queryForScalar("select count(*) from bd_ou_empjob where del_flag = '0' and pk_job = ?", Integer.class, pkJob);
		
		if(job_count == 0 && empjob_count == 0){
			DataBaseHelper.update("update bd_ou_job set del_flag = '1' where pk_job = ?", new Object[]{pkJob});
		}else{
			if(job_count != 0){
				throw new BusException("该岗位被下级岗位引用！");
			}
			if(empjob_count != 0){
				throw new BusException("该岗位被人员工作关系引用！");
			}
		}
		
	}
}
