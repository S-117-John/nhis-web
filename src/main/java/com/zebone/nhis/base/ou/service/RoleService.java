package com.zebone.nhis.base.ou.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.base.ou.dao.RoleMapper;
import com.zebone.nhis.base.ou.vo.RoleAndOperParam;
import com.zebone.nhis.base.ou.vo.RoleAndUsrgrpParam;
import com.zebone.nhis.common.module.base.ou.BdOuRole;
import com.zebone.nhis.common.module.base.ou.BdOuRoleOper;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpRole;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 角色服务
 * @author Xulj
 *
 */
@Service
public class RoleService {

	@Resource
	private RoleMapper roleMapper;
	
	/**
	 * 根据表单参数查询角色列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> getRolesByForm(String param , IUser user){
		Map paramMap = JsonUtil.readValue(param,Map.class);
		return roleMapper.getRolesByForm(paramMap);
	}
	
	/**
	 * 新增 更新 角色
	 * @param param
	 * @param user
	 * @return
	 */
	public BdOuRole saveRole(String param , IUser user){
		
		BdOuRole role = JsonUtil.readValue(param, BdOuRole.class);
		
		if(role.getPkRole() == null){
			//新增
			int count_role = DataBaseHelper.queryForScalar("select count(1) from bd_ou_role "
					+ "where code_role = ?", Integer.class, role.getCodeRole());
			if(count_role == 0){
				DataBaseHelper.insertBean(role);
			}else{
				throw new BusException("角色编码重复！");
			}
			
		}else{
			//更新
			int count_role = DataBaseHelper.queryForScalar("select count(1) from bd_ou_role "
					+ "where code_role = ? and pk_role != ?", Integer.class, role.getCodeRole(), role.getPkRole());
			if(count_role == 0){
				DataBaseHelper.updateBeanByPk(role,false);
			}else{
				throw new BusException("角色编码重复！");
			}
		}
		
		return role;
	}
	
	/**
	 * 删除角色
	 * @param param
	 * @param user
	 */
	public void deleteRole(String param , IUser user){
		BdOuRole role = JsonUtil.readValue(param, BdOuRole.class);
		
		int count_roleOper = DataBaseHelper.queryForScalar("select count(1) from bd_ou_role_oper where pk_role = ?", Integer.class, role.getPkRole());
		int count_usrgrpRole = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp_role where pk_role = ?", Integer.class, role.getPkRole());
		
		if(count_roleOper == 0 && count_usrgrpRole == 0){
			DataBaseHelper.deleteBeanByPk(role);
		}else{
			if(count_roleOper != 0){
				throw new BusException("该角色拥有功能授权，不能删除！");
			}
			if(count_usrgrpRole != 0){
				throw new BusException("该角色被用户组分配，不能删除！");
			}
		}
	}
	
	/**
	 * 新增 更新 角色用户组关系<br>
	 * 交易号：001001005004
	 * 
	 * @param param
	 * @param user
	 */       
	public void saveUsrgrpRoles(String param , IUser user){
		RoleAndUsrgrpParam roleAndUsrgrp = JsonUtil.readValue(param,RoleAndUsrgrpParam.class);
		BdOuRole role = roleAndUsrgrp.getBdOuRole();
		
		List<BdOuUsrgrpRole> usrgrpRolelist = roleAndUsrgrp.getUsrgrpRoles();
		
		if(usrgrpRolelist != null && usrgrpRolelist.size() != 0){
			//全删全插的方式
			String pkRole = role.getPkRole();
			DataBaseHelper.execute("delete from bd_ou_usrgrp_role where pk_role = ?", new Object[]{pkRole});
			for(BdOuUsrgrpRole usrgrpRole : usrgrpRolelist){
				if(usrgrpRole.getPkOrg() != null){
					DataBaseHelper.insertBean(usrgrpRole);
				}else{
					throw new BusException("机构不能为空！");
				}
			}
		}else{
			String pkRole = role.getPkRole();
			DataBaseHelper.execute("delete from bd_ou_usrgrp_role where pk_role = ?", new Object[]{pkRole});
		}
		
	}

	/**
	 * 新增 更新 角色功能关系
	 * @param param
	 * @param user
	 */
	public void saveRoleOpers(String param , IUser user){
		
		RoleAndOperParam roleAndOper = JsonUtil.readValue(param,RoleAndOperParam.class);
		
		BdOuRole role = roleAndOper.getBdOuRole();
		List<BdOuRoleOper> roleOperlist = roleAndOper.getRoleOpers();
		
		if(roleOperlist != null && roleOperlist.size() != 0){
			//全删全插的方式
			String pkRole = role.getPkRole();
			DataBaseHelper.execute("delete from bd_ou_role_oper where pk_role = ?", new Object[]{pkRole});
			for(BdOuRoleOper roleOper : roleOperlist){
				DataBaseHelper.insertBean(roleOper);
			}
		}else{
			//全删全插的方式
			String pkRole = role.getPkRole();
			DataBaseHelper.execute("delete from bd_ou_role_oper where pk_role = ?", new Object[]{pkRole});
		}
	}
	
}
