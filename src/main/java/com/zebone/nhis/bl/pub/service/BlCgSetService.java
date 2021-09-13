package com.zebone.nhis.bl.pub.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zebone.nhis.bl.pub.dao.BlCgSetMapper;
import com.zebone.nhis.bl.pub.vo.BlCgSetDtVo;
import com.zebone.nhis.bl.pub.vo.BlCgSetVo;
import com.zebone.nhis.common.module.bl.BlCgset;
import com.zebone.nhis.common.module.bl.BlCgsetDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 设置记费套餐服务（即：记费项目模板）
 * @author yangxue
 *
 */
@Service
public class BlCgSetService {
	@Autowired
	private BlCgSetMapper blCgSetMapper;
	
   /**
    * 保存套餐模板
    */
	public void saveCgSet(String param,IUser user){
		BlCgset set = JsonUtil.readValue(param, BlCgset.class);
		if (set == null)
			throw new BusException("未获取到要保存的数据！");
		// 校验模板编码和名称是否重复
		String sql = "select * from bl_cgset where (code = ?  or name = ?)";
		List<String> paramlist = new ArrayList<String>();
		paramlist.add(set.getCode());
		paramlist.add(set.getName());
		if (!CommonUtils.isEmptyString(set.getPkDept())) {
			sql = sql + " and pk_dept = ?";
			paramlist.add(set.getPkDept());
		}
		if (!CommonUtils.isEmptyString(set.getPkCgset())) {
			sql = sql + " and pk_cgset != ?";
			paramlist.add(set.getPkCgset());
		}
		List<BlCgset> list = DataBaseHelper.queryForList(sql, BlCgset.class, paramlist.toArray());
		if (list != null && list.size() > 0)
			throw new BusException("编码或名称已被占用，请更换！");
		
		set.setEuType("0");
		if (CommonUtils.isEmptyString(set.getPkCgset())) {
			DataBaseHelper.insertBean(set);
		} else {
			DataBaseHelper.updateBeanByPk(set, false);
		}
	}
	/**
	 * 查询套餐模板
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlCgSetVo> queryCgSetList(String param,IUser user){
		String pk_dept = JsonUtil.readValue(param, String.class);
		String sql = "select * from bl_cgset where eu_type='0' and pk_org = ? and del_flag='0'";
		List<String> list = new ArrayList<String>();
		list.add(((User)user).getPkOrg());
		if(!CommonUtils.isEmptyString(pk_dept)){
			list.add(pk_dept);
			sql = sql + " and pk_dept = ?";
		}
			
		return DataBaseHelper.queryForList(sql, BlCgSetVo.class,list.toArray());
	}
	/**
	 * 删除套餐
	 * @param param
	 * @param user
	 */
	public void deleteCgSet(String param,IUser user){
		String pk_cgset = JsonUtil.readValue(param, String.class);
		List<Map<String,Object>> list = DataBaseHelper.queryForList("select pk_cgset from bl_cgset_dt where pk_cgset = ? and del_flag='0'", new Object[]{pk_cgset});
		if(list!=null&&list.size()>0){
			throw new BusException("该模板下存在明细内容，不允许被删除！");
		}
		//DataBaseHelper.execute("delete from bl_cgset where pk_cgset = ? ", new Object[]{pk_cgset});
		BlCgset blCgsetDt=new BlCgset();
		blCgsetDt.setPkCgset(pk_cgset);
		blCgsetDt.setModifier(UserContext.getUser().getPkEmp());
		blCgsetDt.setModifyTime(new Date());
		blCgsetDt.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(blCgsetDt,false);
	}
	/**
	 * 保存模板明细
	 * @param param
	 * @param user
	 */
	public void saveCgSetItem(String param,IUser user){
		List<BlCgSetDtVo> dtList = JsonUtil.readValue(param,new TypeReference<List<BlCgSetDtVo>>(){});
    	if(dtList==null||dtList.size()<=0)
    		throw new BusException("未获取到要保存的信息！");
    	for(BlCgSetDtVo vo:dtList){
    		BlCgsetDt dt = new BlCgsetDt();
    		ApplicationUtils.copyProperties(dt, vo);
    		if(CommonUtils.isEmptyString(dt.getPkCgsetdt())){
    			DataBaseHelper.insertBean(dt);
    		}else{
    			DataBaseHelper.updateBeanByPk(dt,false);
    		}
    	}
	}
	/**
	 * 删除模板明细
	 * @param param
	 * @param user
	 */
	public void deleteCgSetItem(String param,IUser user){
		List<String> list = JsonUtil.readValue(param,ArrayList.class);
		if(list==null||list.size()<=0)
    		throw new BusException("未获取到要删除的信息！");
		for(String pk_dt:list){
			//DataBaseHelper.execute("delete from bl_cgset_dt where pk_cgsetdt = ?", new Object[]{pk_dt});
			BlCgsetDt blCgsetDt=new BlCgsetDt();
			blCgsetDt.setPkCgsetdt(pk_dt);
			blCgsetDt.setModifier(UserContext.getUser().getPkEmp());
			blCgsetDt.setModifyTime(new Date());
			blCgsetDt.setDelFlag("1");
			DataBaseHelper.updateBeanByPk(blCgsetDt,false);
		}
	}
	/**
	 * 查询模板明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlCgSetDtVo> queryCgSetItemList(String param,IUser user){
		String pk_cgset = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_cgset))throw new BusException("未获取到模板主键!");
		String sql=" SELECT dt.*,item.name as name_item,item.spec,item.price,item.pk_unit unit FROM bl_cgset_dt dt inner join bd_item item ON item.pk_item = dt.pk_item WHERE dt.pk_cgset =?  and dt.del_flag='0' and item.del_flag='0'  "
				+ "UNION ALL SELECT dt.*, bp.name as name_item,bp.spec,bp.price / bp.pack_size as price,bp.pk_unit_min unit FROM bl_cgset_dt dt inner join bd_pd bp on bp.pk_pd=dt.pk_item WHERE dt.pk_cgset =?  and dt.del_flag='0' and bp.del_flag='0'   ORDER BY 13";
		List<BlCgSetDtVo> list = DataBaseHelper.queryForList(sql,BlCgSetDtVo.class, new Object[]{pk_cgset,pk_cgset});
		return list;
	}  
	/**
	 * 查询当前科室所有模板及明细
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlCgSetVo> queryCgSetAndDtList(String param,IUser user){
		String pk_dept = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pk_dept))throw new BusException("未获取到科室主键!");
		String sql = " select * from bl_cgset where pk_org = ? and flag_stop !='1' and del_flag='0' and (pk_dept = ? or pk_dept is null) order by code ";
		List<BlCgSetVo> list = DataBaseHelper.queryForList(sql, BlCgSetVo.class, new Object[]{((User)user).getPkOrg(),pk_dept});
	    if(list == null || list.size()<=0) return null;
	    for(BlCgSetVo vo:list){
	    	List<BlCgSetDtVo> dtlist = 
	    			DataBaseHelper.queryForList("select * from (select dt.*,item.name as name_item ,"
	    					+ "item.SPCODE as spcode_item,item.spec,item.price,item.pk_unit unit,"
	    					+ "'0' as flag_pd,null as pk_unit_pd,1 as pack_size "
	    					+ "from bl_cgset_dt dt inner join bd_item item on item.pk_item = dt.pk_item "
	    					+ "where dt.del_flag='0' and item.flag_active='1' and item.del_flag='0' and dt.pk_cgset = ?  "
	    					+ "union all "
	    					+ " select dt.*,pd.name as name_item ,pd.SPCODE as spcode_item,pd.spec,pd.price/pd.pack_size price,pd.PK_UNIT_MIN unit,"
	    					+ "'1' as flag_pd,pd.pk_unit_min as pk_unit_pd,1 as pack_size "
	    					+ "from bl_cgset_dt dt inner join bd_pd pd  on pd.pk_pd = dt.pk_item  "
	    					+ " where dt.del_flag='0' and pd.flag_stop='0' and dt.pk_cgset = ? ) table1 order by sortno",BlCgSetDtVo.class, new Object[]{vo.getPkCgset(),vo.getPkCgset()});
	        vo.setDtlist(dtlist);
	    }
	    return list;
	}
	
	/**
	 * 查询当前科室所有模板及明细  （重构）
	 * 007004002045
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlCgSetVo> queryCgSetAndDtList_refactoring(String param, IUser user) {
		String pk_dept = JsonUtil.readValue(param, String.class);

		if (CommonUtils.isEmptyString(pk_dept))
			throw new BusException("未获取到科室主键!");

		String sql = " select * from bl_cgset where pk_org = ? and flag_stop !='1' and (pk_dept = ? or pk_dept is null) and eu_type='0' order by code ";
		List<BlCgSetVo> list = DataBaseHelper.queryForList(sql, BlCgSetVo.class, new Object[] { ((User) user).getPkOrg(), pk_dept });

		if (list == null || list.size() <= 0)
			return null;

		Set<String> pkCgsets = Sets.newHashSet();
		for (BlCgSetVo vo : list) {
			pkCgsets.add(vo.getPkCgset());
		}

//		String dtSql = "select * from (SELECT dt.*,item.name as name_item,item.spec,item.price,item.pk_unit unit,'0' flag_pd,null price_cost,item.PK_UNIT pk_unit_pd,1 pack_size FROM bl_cgset_dt dt inner join bd_item item ON item.pk_item = dt.pk_item " 
//		+ "UNION ALL SELECT dt.*,bp.name as name_item, bp.spec,bp.price / bp.pack_size as price,bp.pk_unit_min unit,'1' flag_pd,bp.price price_cost,bp.pk_unit_min pk_unit_pd,1 pack_size FROM bl_cgset_dt dt inner join bd_pd bp on bp.pk_pd=dt.pk_item ) "
//		+ "cgdt where cgdt.PK_CGSET in (" + CommonUtils.convertSetToSqlInPart(pkCgsets, "pk_cgset") + ") ORDER BY sortno";
//		List<BlCgSetDtVo> cgDts = DataBaseHelper.queryForList(dtSql, BlCgSetDtVo.class);
		
		List<BlCgSetDtVo> cgDts = blCgSetMapper.qryCgSetDts(pkCgsets);

		for (BlCgSetVo vo : list) {
			List<BlCgSetDtVo> cgSetDts = Lists.newArrayList();
			for (BlCgSetDtVo dtVo : cgDts) {
				if (dtVo.getPkCgset().equals(vo.getPkCgset()))
					cgSetDts.add(dtVo);
			}
			vo.setDtlist(cgSetDts);
		}

		return list;
	}
}
