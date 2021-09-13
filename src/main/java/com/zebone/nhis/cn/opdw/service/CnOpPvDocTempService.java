package com.zebone.nhis.cn.opdw.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.dao.CnOpPvDocMapper;
import com.zebone.nhis.cn.opdw.vo.BdPvdocTempOrgVo;
import com.zebone.nhis.common.module.cn.opdw.BdPvDocTemp;
import com.zebone.nhis.common.module.cn.opdw.BdPvdocTempOrg;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class CnOpPvDocTempService {
	
	@Autowired
	private CnOpPvDocMapper cnOpPvDocMapper;
	 
	/**
	 * 查询当前机构下的所有模板
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	 public List<BdPvDocTemp> qryAllPvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 User u = (User)user;
		 String sql = "select t.pk_pvdoctemp,t.pk_org,t.code,t.name,t.spcode,t.d_code,t.flag_active,t.creator,t.create_time,t.modifier,t.modity_time,t.del_flag,t.ts from BD_PVDOC_TEMP t"
		 		+ " where t.pk_org='"+u.getPkOrg()+"'";
		 
		 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
		 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		 
		 List<BdPvDocTemp> ret = new ArrayList<BdPvDocTemp>();
		 
		 for(Map<String,Object> map : ps){
			 BdPvDocTemp bpd = new BdPvDocTemp();
			 BeanUtils.copyProperties(bpd, map);	
			 
			 ret.add(bpd);
		 }
		 
		 return ret;
	 }
	 
	 /**
	  * 
	  * @param param
	  * @param user
	  * @return 保存就诊模板
	  * @throws IllegalAccessException
	  * @throws InvocationTargetException
	  */
	 public BdPvDocTemp savePvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		 BdPvDocTemp pvd = JsonUtil.readValue(param,BdPvDocTemp.class);
		 User u = (User)user;
		 
		 if(StringUtils.isEmpty(pvd.getPkPvdoctemp())){
			 pvd.setCreator(u.getPkEmp());
			 pvd.setCreateTime(new Date());
			 pvd.setModityTime(null); //for sql server 
		 }else{
			 pvd.setModifier(u.getPkEmp());
			 pvd.setModityTime(new Date());
		 }
		 
		 pvd.setDelFlag("0");
		 
		 if(StringUtils.isEmpty(pvd.getPkPvdoctemp())) DataBaseHelper.insertBean(pvd);
		 else{
			 DataBaseHelper.updateBeanByPk(pvd, false);
			 if(StringUtils.isEmpty(pvd.getPkFather())){
				 //String sql = "update bd_pvdoc_temp t set t.pk_father = null where t.pk_pvdoctemp='"+pvd.getPkPvdoctemp()+"'"; for oracle
				 String sql = "update bd_pvdoc_temp set pk_father = null where pk_pvdoctemp='"+pvd.getPkPvdoctemp()+"'";
				 DataBaseHelper.update(sql, new HashMap<String,Object>());
			 }
		 }		 
		 
		 return pvd;
		 
	 }
	 
	     /**
		 * 查询当前模板的所有使用机构
		 * @param param
		 * @param user
		 * @return
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 */
		 public List<BdPvdocTempOrgVo> qryAllOrg(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pvdoctemp = JsonUtil.getFieldValue(param, "pvdoctemp");
			 String sql = "select temp.*,org.name_org from bd_pvdoc_temp_org temp"
				        + " inner join bd_ou_org org on temp.pk_org_use=org.pk_org " 
				        + " inner join bd_pvdoc_temp pvd on pvd.pk_pvdoctemp=temp.pk_pvdoctemp "
				        + " where pvd.pk_pvdoctemp='"+pvdoctemp+"'";
			 
			 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);	
			 ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
			 
			 List<BdPvdocTempOrgVo> ret = new ArrayList<BdPvdocTempOrgVo>();
			 
			 for(Map<String,Object> map : ps){
				 BdPvdocTempOrgVo bpo = new BdPvdocTempOrgVo();
				 BeanUtils.copyProperties(bpo, map);	
				 
				 ret.add(bpo);
			 }
			 
			 return ret;			 
		 }
		 
		 /**
		  * 
		  * @param param
		  * @param user
		  * @return 获取模板的二进制数据
		  * @throws IllegalAccessException
		  * @throws InvocationTargetException
		  */
		 public BdPvDocTemp getDocData(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pktemp = JsonUtil.getFieldValue(param, "pktemp");
			 List<BdPvDocTemp> bps = cnOpPvDocMapper.getPvDocTempData(pktemp);
			 
			 if(null == bps || bps.size() <= 0) return new BdPvDocTemp();
			 return bps.get(0);
		 }
		 
		 /**
		  * 
		  * @param param
		  * @param user
		  * @return 保存就诊模板的使用机构
		  * @throws IllegalAccessException
		  * @throws InvocationTargetException
		  */
		 public void savePvDocOrgs(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 List<BdPvdocTempOrg> orgs = JsonUtil.readValue(param,new TypeReference<List<BdPvdocTempOrg>>() {});
			 
			 for(BdPvdocTempOrg org : orgs){
				 if(StringUtils.isEmpty(org.getPkTemporg())) DataBaseHelper.insertBean(org);
				 else DataBaseHelper.updateBeanByPk(org, false);
			 }
			 
		 }
		 
		 /**
		  * 
		  * @param param
		  * @param user
		  * @return 删除就诊模板 0模板已经使用不能删除 1可以删除
		  * @throws IllegalAccessException
		  * @throws InvocationTargetException
		  */
		 public int delPvDoc(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pktemp = JsonUtil.getFieldValue(param, "pkTemp");
			 
			 String sql = "select count(1) as cont from pv_doc doc where doc.pk_pvdoctemp='"+pktemp+"'";
			 List<Map<String,Object>> ps = DataBaseHelper.queryForList(sql);
			 
			 if(null != ps && ps.size() > 0){
				 Map m = ps.get(0);
				 if(null != m && null != m.get("cont") && !StringUtils.isEmpty(m.get("cont").toString())){
					 int count = Integer.parseInt(m.get("cont").toString());
					 if(count > 0) return 0;
				 }
				 
				
			 }
			 			 
			 BdPvDocTemp bpt = new BdPvDocTemp();
			 bpt.setPkPvdoctemp(pktemp);
			 
			 DataBaseHelper.deleteBeanByPk(bpt);
			 
			 //String sql = "delete from BD_PVDOC_TEMP_ORG t where t.pk_pvdoctemp='"+pktemp+"'";
			 //DataBaseHelper.batchUpdate(sql);
			 DataBaseHelper.deleteBeanByWhere(new BdPvdocTempOrg(), "pk_pvdoctemp='"+pktemp+"'");
			 
			 return 1;
		 }
		 
		 /**
		  * 
		  * @param param
		  * @param user
		  * @return 删除就诊模板使用机构
		  * @throws IllegalAccessException
		  * @throws InvocationTargetException
		  */
		 public void delPvDocOrg(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
			 String pktemp = JsonUtil.getFieldValue(param, "pkOrg");
			 DataBaseHelper.deleteBeanByWhere(new BdPvdocTempOrg(), "pk_temporg='"+pktemp+"'");			 
		 }

}
