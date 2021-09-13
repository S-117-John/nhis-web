package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.base.bd.dao.DeptFeeSetMapper;
import com.zebone.nhis.common.module.base.bd.price.BdHpFactor;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DeptFeeSetService {
	@Resource
	private DeptFeeSetMapper deptFeeSetMapper;

	/**
	 * 新增和更新 科室收费参数
	 * @param param
	 * @param user
	 * @return
	 */
	@Transactional
	public List<BdHpFactor> saveHpFactor(String param, IUser user){
		List<BdHpFactor> dtList = JsonUtil.readValue(param, new TypeReference<List<BdHpFactor>>(){});
		if(dtList == null || dtList.size() < 1){
			throw new BusException("未获取到待保存的科室控费记录！");
		}
		for(BdHpFactor factor : dtList){
			if(factor.getPkHpfactor() == null){
				DataBaseHelper.insertBean(factor);
			}else{
				DataBaseHelper.updateBeanByPk(factor,false);
			}
		}
		List<BdHpFactor> resList = DataBaseHelper.queryForList("select dept.name_dept , fac.* from bd_hp_factor fac "
				+ " left join bd_ou_dept dept on fac.pk_dept = dept.pk_dept"
				+ " where fac.del_flag = '0' and fac.pk_dept = ? and fac.pk_org = ?", BdHpFactor.class,
				new Object[] {dtList.get(0).getPkDept() , ((User) user).getPkOrg()});
		return resList;
	}
	
	/**
	 * 删除 科室控费参数
	 * @param param
	 * @param user
	 */
	public void delHpFactor(String param, IUser user){
		String pKHpFactor = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pKHpFactor)){
			throw new BusException("未获取到待删除的科室控费主键！");
		}
		DataBaseHelper.execute("delete from bd_hp_factor where pk_hpfactor = ? ", new Object[]{pKHpFactor});
	}
	/**
	 * 查询科室列表
	 * @param param{pkOrg,dtDepttype,dtDepttypes(有多个科室类型的情况)}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryDeptList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkOrg")))
			throw new BusException("未获取当前机构主键！");
		String deptParam = ApplicationUtils.getSysparam("BD0003", false);
		if("1".equals(deptParam))
			return deptFeeSetMapper.findDeptAndTypeByPkOrg(paramMap);
		else 
			return deptFeeSetMapper.findDeptByPkOrg(paramMap);
	}

}
