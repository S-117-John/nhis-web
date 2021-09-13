package com.zebone.nhis.base.bd.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.vo.BdFilterItemDeptVO;
import com.zebone.nhis.base.bd.vo.BdFilterItemVO;
import com.zebone.nhis.base.bd.vo.DtFilterTypeParam;
import com.zebone.nhis.common.module.base.bd.code.BdFilterItem;
import com.zebone.nhis.common.module.base.bd.code.BdFilterItemDept;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 自定义过滤项目
 * @author gejianwen
 *
 */
@Service
public class FilterService {
	
	/**
	 * 加载过滤项目数据
	 * trans code 001002001006
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdFilterItemVO> loadFilterItem(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		//String dtFiltertype = JsonUtil.getFieldValue(param, "DtFiltertype");		
		DtFilterTypeParam DtFiltertype = JsonUtil.readValue(param, DtFilterTypeParam.class);
		String dtFiltertype = DtFiltertype.getDtFiltertype();
		String sql = "select item.pk_filteritem,item.code_item,item.name_item,item.spcode,ft.code dt_filtertype,ft.name dt_filtertype_name, "
				+ " item.desc_filter,item.flag_active,item.note from bd_filter_item item inner join bd_defdoc ft "
				+ " on item.dt_filtertype = ft.code and ft.code_defdoclist = '051002'";
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		
		List<BdFilterItemVO> ret = new ArrayList<BdFilterItemVO>();
		List<Map<String,Object>> ll = null;
		
		if(!StringUtils.isEmpty(dtFiltertype) && !"null".equalsIgnoreCase(dtFiltertype)){
			sql += " where item.dt_filtertype=?";
			ll = DataBaseHelper.queryForList(sql, dtFiltertype);			
		}else ll = DataBaseHelper.queryForList(sql); 
		
		if(null == ll || ll.size() <= 0) return null;
		
		for(Map<String,Object> map : ll){
			BdFilterItemVO bfi = new BdFilterItemVO();
			BeanUtils.copyProperties(bfi, map);	
			ret.add(bfi);
		}
		
		return ret;		
	}
	
	/**
	 * 保存过滤项目数据
	 * trans code 001002001007
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public 	BdFilterItemVO saveFilterItem(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		BdFilterItemVO vo = JsonUtil.readValue(param, BdFilterItemVO.class);
		if(null == vo)  throw new BusException("保存失败,没有获取到前台对象!");
		
		String sql = "select i.pk_filteritem from bd_filter_item i where i.name_item=? or i.code_item=?";
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql, new String[]{vo.getNameItem(),vo.getCodeItem()});
		
		if(null != ll && ll.size() > 0){
			if(StringUtils.isEmpty(vo.getPkFilteritem())) throw new BusException("保存失败,名称或者编码重复！");
			
			for(Map m : ll){
				if(null != m.get("PkFilteritem") && !vo.getPkFilteritem().equals(m.get("PkFilteritem").toString())) throw new BusException("保存失败,名称或者编码重复！");
			}
		}
		
		sql = "select * from bd_filter_item_dept d where d.pk_filteritem=?";
		
		ll = DataBaseHelper.queryForList(sql,vo.getPkFilteritem());
		List<BdFilterItemDept> db_deps = new ArrayList<BdFilterItemDept>();
		
		for(Map<String,Object> m : ll){
			BdFilterItemDept bfi = new BdFilterItemDept();
			BeanUtils.copyProperties(bfi, m);
			db_deps.add(bfi);
		}		
		
		User u = (User)user;		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		BdFilterItem bfi = new BdFilterItem();
		BeanUtils.copyProperties(bfi, vo);	
		
		if(StringUtils.isEmpty(bfi.getPkFilteritem())){
			bfi.setCreator(u.getPkEmp());
			bfi.setPkOrg(u.getPkOrg());
			bfi.setCreateTime(new Date());
			if(null == bfi.getModityTime()) bfi.setModityTime(new Date());
			if(null == bfi.getTs()) bfi.setTs(new Date());
			DataBaseHelper.insertBean(bfi);
		}
		else{
			bfi.setModifier(u.getPkEmp());
			bfi.setModityTime(new Date());
			if(null == bfi.getTs()) bfi.setTs(new Date());
			if(null == bfi.getCreateTime()) bfi.setCreateTime(new Date());
			DataBaseHelper.updateBeanByPk(bfi, false);
		}
		
		List<BdFilterItemDept> deps = vo.getDepts(); 
		if(null == deps || deps.size() <= 0){
			vo.setPkFilteritem(bfi.getPkFilteritem());
			return vo;
		}
	
		BdFilterItemVO ret = new BdFilterItemVO();
		ret.setDepts(new ArrayList<BdFilterItemDept>());
		BeanUtils.copyProperties(ret,bfi);	
		for(BdFilterItemDept dept : deps){
			if(checkIsContain(db_deps,dept)) continue; 
			dept.setPkFilteritem(bfi.getPkFilteritem());
			dept.setCreateTime(new Date());
			dept.setCreator(u.getPkEmp());
			if(null == dept.getCreateTime()) dept.setCreateTime(new Date());
			if(null == dept.getModityTime()) dept.setModityTime(new Date());
			if(null == dept.getTs()) dept.setTs(new Date());
			
			if(StringUtils.isEmpty(dept.getPkItemdept())) DataBaseHelper.insertBean(dept);
			else DataBaseHelper.updateBeanByPk(dept);
			
			ret.getDepts().add(dept);
		}			
				
		return ret;	
		
	}
	
	/**
	 * 查找现有的过滤项目中是否已经包含所选科室
	 * @param pk
	 * @return
	 */
	private boolean checkIsContain(List<BdFilterItemDept> deps,BdFilterItemDept dept){
		if(null == deps && deps.size() <= 0) return false;
		for(BdFilterItemDept de : deps){ 
			if(de.getPkDept().equals(dept.getPkDept()) && de.getPkOrg().equals(dept.getPkOrg())) return true;				
		}
		
		if(!StringUtils.isEmpty(dept.getPkItemdept())) dept.setPkItemdept(null);
		
		return false;
		
	}
	


	/**
	 * 获取过滤项目用到的科室
	 * trans code 001002001008
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdFilterItemDeptVO> getFilterDept(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		String src = JsonUtil.getFieldValue(param, "src");  //src为1取未选科室，0为选已选科室
		if("1".equals(src)) return getSourceDept(param,user);
		else return getDestDept(param,user);
		
	}

	private List<BdFilterItemDeptVO> getDestDept(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
		String sql = "select org.name_org,dept.pk_dept,dept.name_dept,fd.pk_itemdept,org.pk_org,dept.code_dept,fd.pk_filteritem "
				+ " from bd_filter_item_dept fd"
				+ " inner join bd_ou_dept dept on fd.pk_dept=dept.pk_dept"
				+ " inner join bd_ou_org org on dept.pk_org=org.pk_org where 1=1 ";
		
		String pk = JsonUtil.getFieldValue(param, "pk");
		String org = JsonUtil.getFieldValue(param, "org");
		
		List<String> par = new ArrayList<String>();
		if(!StringUtils.isEmpty(pk) && !"null".equalsIgnoreCase(pk)){
			sql += "and fd.pk_filteritem=?";
			par.add(pk);
		}
		
		if(!StringUtils.isEmpty(org) && !"null".equalsIgnoreCase(org)){
			sql += " and org.pk_org=?";
			par.add(org);
		}
		
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql, par.toArray());
		if(null == ll || ll.size() <= 0) return null;
		
		List<BdFilterItemDeptVO> ret = new ArrayList<BdFilterItemDeptVO>();
		for(Map m : ll){
			BdFilterItemDeptVO bfi = new BdFilterItemDeptVO();
			BeanUtils.copyProperties(bfi, m);
			ret.add(bfi);
		}
		
		return ret;
	}

	private List<BdFilterItemDeptVO> getSourceDept(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
		String sql = "select dept.pk_org,org.name_org,dept.pk_dept,dept.name_dept,dept.code_dept "
				+ " from bd_ou_dept dept inner join bd_ou_org org on org.pk_org=dept.pk_org "
				+ " where not exists (select 1 from bd_filter_item_dept fd "
				+ " where fd.pk_dept=dept.pk_dept ";
		
		String pk = JsonUtil.getFieldValue(param, "pk");
		String org = JsonUtil.getFieldValue(param, "org");
		
		List<String> par = new ArrayList<String>();
		if(!StringUtils.isEmpty(pk) && !"null".equalsIgnoreCase(pk)){
			par.add(pk);
			sql += " and fd.pk_filteritem=? ) ";
		}else sql += ")";
		
		
		if(!StringUtils.isEmpty(org) && !"null".equalsIgnoreCase(org)){
			sql += " and org.pk_org=?";
			par.add(org);
		}
		
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql, par.toArray()); 
		if(null == ll || ll.size() <= 0) return null;
		
		List<BdFilterItemDeptVO> ret = new ArrayList<BdFilterItemDeptVO>();
		for(Map m : ll){
			BdFilterItemDeptVO bfi = new BdFilterItemDeptVO();
			BeanUtils.copyProperties(bfi, m);
			ret.add(bfi);
		}
		
		return ret;
	}
	
	/**
	 * 删除过滤项目
	 * trans code 001002001010
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void deleteFilterDept(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		BdFilterItemVO vo = JsonUtil.readValue(param, BdFilterItemVO.class);
		if(null == vo)  throw new BusException("保存失败,没有获取到前台对象!");
		
		BdFilterItem item = new BdFilterItem();
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		BeanUtils.copyProperties(item, vo);
		
		List<BdFilterItemDept> deps = vo.getDepts(); 
		delAllDept(vo.getPkFilteritem());
		
		DataBaseHelper.deleteBeanByPk(item);		
	}
	
	private void delDept(List<BdFilterItemDept> deps, String pkFilteritem) {
		  if(null != deps && deps.size() > 0){
		    	for(BdFilterItemDept dept : deps){
		    		if(StringUtils.isEmpty(dept.getPkItemdept())) continue;		    		
		    		DataBaseHelper.deleteBeanByPk(dept);
		    	}	    	
		    	
		   }
		
	}

	/**
	 * 删除科室的公共方法
	 * @param deps
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private void delAllDept(String pkFilteritem) throws IllegalAccessException, InvocationTargetException{
		String sql = "select * from BD_FILTER_ITEM_dept it where it.pk_filteritem=?";
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql, pkFilteritem);
		if(null == ll || ll.size() <= 0) return;
			
		for(Map m : ll){
		    BdFilterItemDept dept = new BdFilterItemDept();
			BeanUtils.copyProperties(dept, m);
			DataBaseHelper.deleteBeanByPk(dept);
			
		}
	}
	
	/**
	 * 删除fiter项目对应的科室 
	 * trans code 001002001009
	 * @param param
	 * @param user
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void deleteDept(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		BdFilterItemVO vo = JsonUtil.readValue(param, BdFilterItemVO.class);
		if(null == vo)  throw new BusException("保存失败,没有获取到前台对象!");
		
		List<BdFilterItemDept> deps = vo.getDepts(); 
		delDept(deps,vo.getPkFilteritem());
		
	}
	
	
	
	
	
	
	

}
