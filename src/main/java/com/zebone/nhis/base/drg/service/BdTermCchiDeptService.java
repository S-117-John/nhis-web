package com.zebone.nhis.base.drg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.drg.dao.BdTermCchiDeptMapper;
import com.zebone.nhis.base.drg.vo.BdTermCchiDeptSaveParam;
import com.zebone.nhis.common.module.base.bd.drg.BdTermCchiDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * CCHI科室模板 
 * @author dell
 *
 */
@Service
public class BdTermCchiDeptService {
	
	@Resource
	public BdTermCchiDeptMapper bdTermCchiDeptMapper;
	/**
	 * 保存CCHI科室模板
	 * @param param
	 * @param user
	 */
	public void saveTermCchiDept(String param, IUser user){
		BdTermCchiDeptSaveParam saveParam = JsonUtil.readValue(param,BdTermCchiDeptSaveParam.class);
		BdTermCchiDept bdTermCchiDept=saveParam.getBdTermCchiDept();
		List<BdTermCchiDept> addList = saveParam.getAddTermCchiDeptList();
        List<BdTermCchiDept> delCcdts = saveParam.getDelTermCchiDeptList();
		if(null==bdTermCchiDept.getEuChild() || "".equals(bdTermCchiDept.getEuChild())){
			throw new BusException("类别为空，请检查！");
		}
		//数据校验
		if(bdTermCchiDept.getEuChild().equals("0")){
			checkSort(bdTermCchiDept);
		}else{
			//checkCchiDept(bdTermCchiDept);
		}
		//先删除list中的数据
		this.delTermCchiDeptList(delCcdts,bdTermCchiDept);
		if(StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
			ApplicationUtils.setDefaultValue(bdTermCchiDept, true);
			bdTermCchiDept.setPkDept(UserContext.getUser().getPkDept());
			bdTermCchiDept.setFlagDel("0");
			DataBaseHelper.insertBean(bdTermCchiDept);
			for (BdTermCchiDept b : addList) {
				b.setPkOrg(bdTermCchiDept.getPkOrg());
				b.setPkDept(bdTermCchiDept.getPkDept());
				b.setFlagDel("0");
				b.setSortno(bdTermCchiDept.getSortno());
				b.setEuChild("1");
				
				checkCchiDept(b);
				ApplicationUtils.setDefaultValue(b, true);
				
				DataBaseHelper.insertBean(b);
				
            }
			//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdTermCchiDept.class), addList);
		}else{
			DataBaseHelper.updateBeanByPk(bdTermCchiDept);
			List<BdTermCchiDept> addListSave =new ArrayList<BdTermCchiDept>();
			List<BdTermCchiDept> updateListSave =new ArrayList<BdTermCchiDept>();
			for (BdTermCchiDept b : addList) {
				if(StringUtils.isEmpty(b.getPkCchidept())){
					b.setPkOrg(bdTermCchiDept.getPkOrg());
					b.setPkDept(bdTermCchiDept.getPkDept());
					b.setFlagDel("0");
					b.setSortno(bdTermCchiDept.getSortno());
					b.setEuChild("1");
					
					checkCchiDept(b);
					
					ApplicationUtils.setDefaultValue(b, true);
					
					DataBaseHelper.insertBean(b);
					addListSave.add(b);
				}else{
					checkCchiDept(b);
					updateListSave.add(b);
				}
            }
			if(null!=addListSave && addListSave.size()>0){
				//DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdTermCchiDept.class), addListSave);
			}
			if(null!=updateListSave && updateListSave.size()>0){
				DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(BdTermCchiDept.class), updateListSave);
			}
			
		}
	}
	/**
	 * 删除CCHI科室模板
	 * @param param
	 * @param user
	 */
	@SuppressWarnings("null")
	public void delTermCchiDept(String param, IUser user) {
		BdTermCchiDept bdTermCchiDept = JsonUtil.readValue(param,BdTermCchiDept.class);
		if(StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
			throw new BusException("主键为空，请检查参数!");
		}
		if(null==bdTermCchiDept.getEuChild() || "".equals(bdTermCchiDept.getEuChild())){
			throw new BusException("类别为空，请检查！");
		}
		if(null==bdTermCchiDept.getPkDept() || "".equals(bdTermCchiDept.getPkDept())){
			throw new BusException("科室为空，请检查！");
		}
		List<Map<String,Object>> list=DataBaseHelper.queryForList("select * from BD_TERM_CCHI_DEPT where PK_CCHIDEPT=?", bdTermCchiDept.getPkCchidept());
		if(null==list && list.size()==0){
			if(null==bdTermCchiDept.getPkDept() || "".equals(bdTermCchiDept.getPkDept())){
				throw new BusException("要删除的数据不存在，请检查！");
			}
		}
		DataBaseHelper.deleteBeanByPk(bdTermCchiDept);
		if(bdTermCchiDept.getEuChild().equals("0")){
			Map<String,Object> map=list.get(0);
			DataBaseHelper.deleteBeanByWhere(bdTermCchiDept, "pk_dept='"+map.get("pkDept")+"' and sortno='"+bdTermCchiDept.getSortno()+"'");
		}
	}
	/**
	 * 查询左侧树列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Map<String,Object>> querySortnoList(String param,IUser user){
		BdTermCchiDept qryparam = JsonUtil.readValue(param,BdTermCchiDept.class);
		qryparam.setPkDept(UserContext.getUser().getPkDept());
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		if(StringUtils.isEmpty(qryparam.getPkDept())){
			throw new BusException("科室参数为空，请检查！");
		}
		List<Map<String,Object>> list=DataBaseHelper.queryForList("select * from bd_term_cchi_dept where pk_dept=? and eu_child='0' and flag_del='0' order by sortno", qryparam.getPkDept());
		return list;
	}
	/**
	 * 查询CCHI列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryTermCchiDeptList(String param,IUser user){
		BdTermCchiDept qryparam = JsonUtil.readValue(param,BdTermCchiDept.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		if(StringUtils.isEmpty(qryparam.getPkDept())){
			throw new BusException("科室参数为空，请检查！");
		}
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> list=bdTermCchiDeptMapper.queryTermCchiDeptList(paramMap);
		return list;
	}
	/**
	 * 校验类别数据
	 * @param bdTermCchiDept
	 */
	public void checkSort(BdTermCchiDept bdTermCchiDept){
		String sql="select count(*) from BD_TERM_CCHI_DEPT where SORTNO=? and PK_DEPT=? and FLAG_DEL='0'  and EU_CHILD='0' ";
		int count=0;
		if(StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
			count=DataBaseHelper.queryForScalar(sql, Integer.class, bdTermCchiDept.getSortno(),UserContext.getUser().getPkDept());
		}else{
			count=DataBaseHelper.queryForScalar(sql+" and PK_CCHIDEPT!=?", Integer.class,  bdTermCchiDept.getSortno(),bdTermCchiDept.getPkDept(),bdTermCchiDept.getPkCchidept());
		}
		if(count>0){
			throw new BusException("当前科室下分类序号必须唯一，请检查！");
		}
		
		String sqlName="select count(*) from BD_TERM_CCHI_DEPT where NAME_CATE=? and PK_DEPT=? and FLAG_DEL='0'  and EU_CHILD='0' ";
		int countName=0;
		if(StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
			countName=DataBaseHelper.queryForScalar(sqlName, Integer.class, bdTermCchiDept.getNameCate(),UserContext.getUser().getPkDept());
		}else{
			countName=DataBaseHelper.queryForScalar(sqlName+" and PK_CCHIDEPT!=?", Integer.class,  bdTermCchiDept.getNameCate(),bdTermCchiDept.getPkDept(),bdTermCchiDept.getPkCchidept());
		}
		if(countName>0){
			throw new BusException("当前科室下分类名称必须唯一，请检查！");
		}
	}
	/**
	 * 校验列表数据
	 * @param bdTermCchiDept
	 */
	public void checkCchiDept(BdTermCchiDept bdTermCchiDept){
		String sql="select count(*) from BD_TERM_CCHI_DEPT where SORTNO=? and PK_DEPT=? and PK_CCHI=? and PK_ITEM=? and FLAG_DEL='0'  and EU_CHILD='1' ";
		int count=0;
		if(StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
			count=DataBaseHelper.queryForScalar(sql, Integer.class, bdTermCchiDept.getSortno(),UserContext.getUser().getPkDept(),bdTermCchiDept.getPkCchi(),bdTermCchiDept.getPkItem());
		}else{
			count=DataBaseHelper.queryForScalar(sql+" and PK_CCHIDEPT!=?", Integer.class,  bdTermCchiDept.getSortno(),bdTermCchiDept.getPkDept(),bdTermCchiDept.getPkCchidept(),bdTermCchiDept.getPkCchi(),bdTermCchiDept.getPkItem());
		}
		if(count>0){
			throw new BusException("当前分类序号下CCHI项目+收费项目必须唯一，请检查！");
		}
		
	}
	/**
	 * 获取最大序号
	 * @param param
	 * @param user
	 * @return
	 */
	public String getSortno(String param, IUser user) {
		String pkDept=UserContext.getUser().getPkDept();
		if(StringUtils.isEmpty(pkDept)){
			throw new BusException("科室参数为空，请检查！");
		}
		String sql="select Max(Sortno) from BD_TERM_CCHI_DEPT where PK_DEPT='"+pkDept+"' and FLAG_DEL='0'";
		Integer sortno=DataBaseHelper.queryForScalar(sql, Integer.class,null);
		if(sortno== null){
			sortno=0;
		}
        String groupno = String.valueOf(sortno + 1);
        return groupno;
    }
	/**
	 * 删除列表
	 * @param delCcdts
	 * @param bdTermCchiDept
	 */
	@SuppressWarnings("null")
	private void delTermCchiDeptList(List<BdTermCchiDept> delCcdts,BdTermCchiDept bdTermCchiDept){
		if(null!=delCcdts && delCcdts.size()>0){
//			if(!StringUtils.isEmpty(bdTermCchiDept.getPkCchidept())){
//				List<Map<String,Object>> list=DataBaseHelper.queryForList("select * from BD_TERM_CCHI_DEPT where PK_CCHIDEPT=?", bdTermCchiDept.getPkCchidept());
//				if(null==list && list.size()==0){
//					if(null==bdTermCchiDept.getPkDept() || "".equals(bdTermCchiDept.getPkDept())){
//						throw new BusException("要删除的数据不存在，请检查！");
//					}
//				}
//				if(bdTermCchiDept.getEuChild().equals("0")){
//					Map<String,Object> map=list.get(0);
//					DataBaseHelper.deleteBeanByWhere(bdTermCchiDept, "pk_dept='"+map.get("pkDept")+"' and sortno=' and EU_CHILD='1' "+bdTermCchiDept.getSortno()+"'");
//				}
//			}
			for(BdTermCchiDept b:delCcdts){
				if(!StringUtils.isEmpty(b.getPkCchidept())){
					DataBaseHelper.deleteBeanByPk(b);
				}
			}
		}
	}
}
