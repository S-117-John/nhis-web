package com.zebone.nhis.base.bd.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.nhis.base.bd.vo.MaintainVo;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.DeptChargeItemMaintainMapper;
import com.zebone.nhis.base.bd.vo.DeptChargeItemMaintainVo;
import com.zebone.nhis.common.module.base.bd.price.BdHpFactor;
import com.zebone.nhis.common.module.bl.BlCgset;
import com.zebone.nhis.common.module.bl.BlCgsetDt;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DeptChargeItemMaintainService {
	
	@Resource
	private DeptChargeItemMaintainMapper deptChargeItemMaintainMapper;
	
	//查询：001002007075
	public List<MaintainVo> qryDeptChargeItemMaintain(String param, IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<MaintainVo> maintainVos = deptChargeItemMaintainMapper.qryDeptChargeItemMaintain(map);
		return maintainVos;
	}
	
	//新增保存：001002007076
	public void saveDeptChargeItemMaintain(String param,IUser user){
		
		List<DeptChargeItemMaintainVo> list = JsonUtil.readValue(param, new TypeReference<List<DeptChargeItemMaintainVo>>(){});
		if(list != null && list.size() > 0){
			for (DeptChargeItemMaintainVo vo : list) {
				BlCgset set = new BlCgset();
				set.setPkOrg(vo.getPkOrg());
				set.setPkDept(vo.getPkDept());
				set.setEuType("1");
				set.setFlagStop("0");
				int insert = DataBaseHelper.insertBean(set);
				if(insert > 0 && StringUtils.isNotBlank(set.getPkCgset())){
					BlCgsetDt dt = new BlCgsetDt();
					dt.setPkCgset(set.getPkCgset());
					dt.setPkOrg(vo.getPkOrg());
					dt.setSortno("0");
					dt.setEuItemtype(vo.getEuItemtype());
					dt.setPkItem(vo.getPkItem());
					dt.setQuan(new BigDecimal(vo.getQuan()));
					dt.setDosage(vo.getDosage());
					dt.setNameFreq(vo.getNameFreq());
					dt.setNameSupply(vo.getNameSupply());
					dt.setUnitDos(vo.getUnitDos());
					int success = DataBaseHelper.insertBean(dt);
					if(success <= 0){
						throw new BusException("保存失败！");
					}
				}else{
					throw new BusException("保存失败！");
				}
			}
		}
	}
	
	//删除：001002007077
	public void delDeptChargeItemMaintain(String param,IUser user){
		
		List<DeptChargeItemMaintainVo> list = JsonUtil.readValue(param, new TypeReference<List<DeptChargeItemMaintainVo>>(){});
		if(list != null && list.size() > 0){
			for (DeptChargeItemMaintainVo vo : list) {
				if(StringUtils.isBlank(vo.getPkCgset())){
					throw new BusException("删除失败，数据异常！");
				}
				String sqlDt = "delete from bl_cgset_dt where pk_cgset = ?";
				int del = DataBaseHelper.execute(sqlDt, vo.getPkCgset());
				if(del > 0 && StringUtils.isNotBlank(vo.getPkDept())){
					String sql = "delete from bl_cgset where bl_cgset.pk_dept = ? "
							+ "and not exists (select 1 from bl_cgset_dt "
							+ "where bl_cgset.pk_cgset=bl_cgset_dt.pk_cgset)";
					DataBaseHelper.execute(sql, vo.getPkDept());
				}else{
					System.out.println(vo.getPkCgset()+":"+vo.getPkItem());
					throw new BusException("删除失败！");
				}
			}
		}
	}
	
	//导入数据查询：001002007078
	public List<Map<String, Object>> qryImportDeptChargeItemMaintain(String param,IUser user){
		
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		
		return deptChargeItemMaintainMapper.qryImportDeptChargeItemMaintain(map);
	}
}
