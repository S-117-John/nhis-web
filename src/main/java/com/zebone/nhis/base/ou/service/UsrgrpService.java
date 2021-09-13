package com.zebone.nhis.base.ou.service;

import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;

import ca.uhn.hl7v2.util.StringUtil;
import com.zebone.nhis.cn.pub.dao.BdSerialnoMapper;
import com.zebone.nhis.cn.pub.vo.BdSerialno;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpPerm;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.zebone.nhis.base.ou.vo.UsrgrpAndDeptParam;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrp;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpDept;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpRole;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 用户组服务
 * @author Xulj
 *
 */
@Service
public class UsrgrpService {

	@Resource      
	private BdSerialnoMapper bdSnMapper;
	private boolean isInit = false;
	/**
	 * 新增 更新 用户组,用户组和科室关系,用户组和角色关系
	 * @param param
	 * @param user
	 * @return
	 */
	public void saveUsrgrpAndDept(String param , IUser user){
		UsrgrpAndDeptParam usrgrpAndDept = JsonUtil.readValue(param, UsrgrpAndDeptParam.class);
		BdOuUsrgrp usrgrp = usrgrpAndDept.getUsrgrp();
		List<BdOuUsrgrpDept> usrgrpDeptList = usrgrpAndDept.getDeptrelations();
		List<BdOuUsrgrpRole> usrgrpRoleList = usrgrpAndDept.getRolerelations();
		List<BdOuUsrgrpPerm> usergrpFormList= usrgrpAndDept.getBdreportrelations();
		/**验证用户组关联的科室,角色是否重复
		 * 至少2个，才验重*/
		if(usrgrpDeptList != null && usrgrpDeptList.size() > 1){
			Set pkDeptSet = new HashSet();
			for(int i = 0; i<usrgrpDeptList.size();i++){
				String pkdept = usrgrpDeptList.get(i).getPkDept();
				if(!pkDeptSet.contains(pkdept)){
					pkDeptSet.add(pkdept);
				}else{
					throw new BusException("用户组关联科室存在重复！");
				}
			}
		}
		if(usrgrpRoleList != null && usrgrpRoleList.size() > 1){
			Set pkRoleSet = new HashSet();
			for(int i = 0; i<usrgrpRoleList.size();i++){
				String pkrole = usrgrpRoleList.get(i).getPkRole();
				if(!pkRoleSet.contains(pkrole)){
					pkRoleSet.add(pkrole);
				}else{
					throw new BusException("用户组关联角色存在重复！");
				}
			}
		}
		if(usergrpFormList != null && usergrpFormList.size() > 1){
			Set pkRepoSet = new HashSet();
			for(int i = 0; i<usergrpFormList.size();i++){
				String codeDp = usergrpFormList.get(i).getCodeDp();
				if(!pkRepoSet.contains(codeDp)){
					pkRepoSet.add(codeDp);
				}else{
					throw new BusException("用户组关联报表存在重复！");
				}
			}
		}


		/**保存，更新用户组*/
		if(usrgrp.getPkUsrgrp() == null){
			//新增
			int count_usrgrp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp "
					+ "where code_usrgrp = ? and pk_org like ?||'%'", Integer.class, usrgrp.getCodeUsrgrp(),usrgrp.getPkOrg());
			if(count_usrgrp == 0){
				DataBaseHelper.insertBean(usrgrp);
			}else{
				throw new BusException("用户组编码重复！");
			}

		}else{
			//更新
			int count_usrgrp = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp "
					+ "where code_usrgrp = ? and pk_org like ?||'%' and pk_usrgrp != ?", Integer.class, usrgrp.getCodeUsrgrp(),usrgrp.getPkOrg(),usrgrp.getPkUsrgrp());
			if(count_usrgrp == 0){
				DataBaseHelper.updateBeanByPk(usrgrp,false);
				
			}else{
				throw new BusException("用户组编码重复！");
			}
		}

		/**保存更新用户组和科室关系*/
		//全删全插的方式
		DataBaseHelper.execute("delete from bd_ou_usrgrp_dept where pk_usrgrp = ?", new Object[]{usrgrp.getPkUsrgrp()});
		if(usrgrpDeptList != null && usrgrpDeptList.size() != 0){
			for(BdOuUsrgrpDept usrgrpDept : usrgrpDeptList){

				usrgrpDept.setPkUsrgrp(usrgrp.getPkUsrgrp());
				DataBaseHelper.insertBean(usrgrpDept);

			}
		}

		/**保存更新用户组和角色关系*/
		//全删全插的方式
		DataBaseHelper.execute("delete from bd_ou_usrgrp_role where pk_usrgrp = ?", new Object[]{usrgrp.getPkUsrgrp()});
		if(usrgrpRoleList != null && usrgrpRoleList.size() != 0){
			for(BdOuUsrgrpRole usrgrpRole : usrgrpRoleList){

				usrgrpRole.setPkUsrgrp(usrgrp.getPkUsrgrp());
				DataBaseHelper.insertBean(usrgrpRole);

			}
		}
		/**保存更新用户组和报表关系*/
		//全删全插的方式
		DataBaseHelper.execute("delete from bd_ou_usrgrp_perm where pk_usrgrp = ?", new Object[]{usrgrp.getPkUsrgrp()});
		if(usergrpFormList != null && usergrpFormList.size() != 0){
			for(BdOuUsrgrpPerm usrgrpPerm : usergrpFormList){
				ApplicationUtils.setDefaultValue(usrgrpPerm,true);
				usrgrpPerm.setPkOrg(usrgrp.getPkOrg());//机构设置为当前用户组所属机构
				if(usrgrpPerm.getDtDatapermtype()==null||"".equals(usrgrpPerm.getDtDatapermtype())){
					usrgrpPerm.setDtDatapermtype("01");
				}
				usrgrpPerm.setDelFlag("0");
				usrgrpPerm.setPkUsrgrp(usrgrp.getPkUsrgrp());
				DataBaseHelper.insertBean(usrgrpPerm);
			}
		}
	}
	
	/**
	 * 删除用户组
	 * @param param
	 * @param user
	 */
	public void deleteUsrgrp(String param , IUser user){
		
		BdOuUsrgrp usrgrp = JsonUtil.readValue(param, BdOuUsrgrp.class);

		if(StringUtil.isNotBlank(usrgrp.getPkUsrgrp())) {

			String queSql = "select * from bd_ou_user where pk_usrgrp = ?";
			List<BdOuUsrgrp> bdOuUsrgrpList = DataBaseHelper.queryForList(queSql,BdOuUsrgrp.class,usrgrp.getPkUsrgrp());
			if(bdOuUsrgrpList.size() > 0){
				throw new BusException("该用户组已被引用，不可删除！");
			}
			//清除关联科室默认标识
			DataBaseHelper.execute("UPDATE bd_ou_usrgrp_dept SET isdefualt = '0' where pk_usrgrp = ? ", usrgrp.getPkUsrgrp());
			//删除用户组
			DataBaseHelper.deleteBeanByPk(usrgrp);
		}
		//查询存在关联条件条目数
		//查询关联报表条目数
	/*	int count_usrgrp_role = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp_role "
				+ "where pk_usrgrp = ?", Integer.class, usrgrp.getPkUsrgrp());
		//查询关联科室条目数
		int count_usrgrp_dept = DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp_dept "
				+ "where pk_usrgrp = ?", Integer.class, usrgrp.getPkUsrgrp());
		//查询用户组数据权限
		int count_usrgrp_perm=DataBaseHelper.queryForScalar("select count(1) from bd_ou_usrgrp_perm "
				+ "where pk_usrgrp = ?", Integer.class, usrgrp.getPkUsrgrp());

		//判断是否存在关联报表、关联科室、用户组数据权限，并且删除用户组
		if((count_usrgrp_role + count_usrgrp_dept+count_usrgrp_perm) == 0){
			DataBaseHelper.deleteBeanByPk(usrgrp);
		}else{
			throw new BusException("若要删除请先解除被引用关系！");
		}*/
	}
	/**
	 * 获取返回的最大序号，位数不足时前面补0
	 * @param user
	 * @return
	 */
	public String getSerialNo(String param, IUser user){
		if(StringUtils.isEmpty(param)){
			throw new BusException("参数为空，请检查！");
		}
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String tableName=(String) paramMap.get("tableName");
		String fieldName=(String) paramMap.get("fieldName");
		int count=(int) paramMap.get("count");
		if(tableName==null) return "";
		if(!isInit){ 
			Double sn = bdSnMapper.selectSn(tableName.toUpperCase(), fieldName.toUpperCase()); 
			if(sn==null){
				BdSerialno initSn = new BdSerialno();
				initSn.setPkSerialno(NHISUUID.getKeyId());
				initSn.setPkOrg(CommonUtils.getGlobalOrg());
				initSn.setNameTb(tableName.toUpperCase());
				initSn.setNameFd(fieldName.toUpperCase());
				initSn.setValInit((short)1000);
				initSn.setVal((short)1000);
				bdSnMapper.initSn(initSn);
				isInit = true;
			}
		}
		int ret = ApplicationUtils.getSerialNo(tableName,fieldName,count);
		Integer length=bdSnMapper.selectLength(tableName.toUpperCase(), fieldName.toUpperCase());
		if(null==length){
			return String.valueOf(ret);
		}
		return getFourNumber(length,ret);
	}
	/**
	 * 前面补0
	 * @param length
	 * @param number
	 * @return
	 */
	public String getFourNumber(int length,int number){
		NumberFormat formatter = NumberFormat.getNumberInstance();   
		formatter.setMinimumIntegerDigits(length);   
		formatter.setGroupingUsed(false); 
		return formatter.format(number);
	}
}
