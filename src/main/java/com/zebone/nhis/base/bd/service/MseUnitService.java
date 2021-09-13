package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.MseUnitMapper;
import com.zebone.nhis.base.bd.dao.PdMapper;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
@Service
public class MseUnitService {
	
	@Resource
	private MseUnitMapper mseUnitMapper;
	
	@Resource
	private PdMapper pdMapper;
	
	/**
	 * 查询计量单位(条件查询)
	 * @param param {code,name,spcode,dCode}
	 * @param user
	 * @return
	 */
	public List<BdUnit> queryUnitByCondition(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, HashMap.class);
		List<BdUnit> unitList = mseUnitMapper.queryUnitsByCondition(params);
		return unitList;
	}
	
	/**
	 * 保存计量单位(添加/修改)
	 * @param param
	 * @param user
	 */
	public void saveBdUnitList(String param, IUser user){
		List<BdUnit> bdUnitList = JsonUtil.readValue(param, new TypeReference<List<BdUnit>>() {
		});
		
		User loginUser = (User)user;
		
		Map<String, String> codemap = new HashMap<String, String>();
		
		/**校验---1.校验前台所传的list的每一条编码的唯一性*/
		if(bdUnitList != null && bdUnitList.size() > 0){
			int len = bdUnitList.size();
			for(int i = 0; i<len; i++){
				String code = bdUnitList.get(i).getCode();
				if(codemap.containsKey(code)){
					throw new BusException("计量单位编码重复！");
				}
			    codemap.put(code, bdUnitList.get(i).getPkUnit());
			}
					
			/**查询数据库中所有*/
			List<BdUnit> allist = mseUnitMapper.queryAllUnits();
			
			/**校验---2.校验前台所传的list的每一条编码和名称是否和数据库重复*/
			for(BdUnit bdUnit : allist){
				if(codemap.containsKey(bdUnit.getCode())){
					String pkUnit = codemap.get(bdUnit.getCode());
					if(pkUnit == null){  //新增情况
						throw new BusException("计量单位编码在数据库中已存在！");
					}else{                //修改情况
						if(!bdUnit.getPkUnit().equals(pkUnit)){
							throw new BusException("计量单位编码在数据库中已存在！");
						}
					}
				}
			}

			List<String> insertPk=new ArrayList<String>();//用於存儲新增后的主鍵，調用平臺傳入
			
			/**新增或更新到数据库*/
			for(BdUnit bdUnit : bdUnitList){
				if(bdUnit.getPkUnit() == null){
					bdUnit.setCreator(loginUser.getId());
					bdUnit.setPkOrg(loginUser.getPkOrg());
					bdUnit.setCreateTime(new Date());
					DataBaseHelper.insertBean(bdUnit);
					insertPk.add(bdUnit.getPkUnit());
				}else{
					bdUnit.setModifier(loginUser.getId());
					DataBaseHelper.updateBeanByPk(bdUnit,false);
				}
			}
		}
	}

	
	/**
	 * 删除计量单位
	 * @param param
	 * @param user
	 */
	public void delUnit(String param,IUser user){
		
		String pkUnit = JsonUtil.getFieldValue(param,"pkUnit");
		/**获取计量单位信息**/
		BdUnit unit = mseUnitMapper.queryUnitBypkUnit(pkUnit);
		/**获取计量单位是否在药品表中存在**/
		int count = pdMapper.queryUnitIsCite(pkUnit);
		if(count<=0){
			/**判断计量单位是否已作废,如果作废则不能删除**/
			if(unit.getDelFlag().equals("1")){
				throw new BusException("单位["+unit.getName()+"]已作废，不能删除！");
			}
			/**删除未作废的单位**/
			mseUnitMapper.delUnitByPk(unit.getPkUnit());
		} else {
			throw new BusException("单位["+unit.getName()+"]已被引用，不能删除！");
		}
	}
}
