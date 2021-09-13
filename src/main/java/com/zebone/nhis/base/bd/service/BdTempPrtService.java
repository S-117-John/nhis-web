package com.zebone.nhis.base.bd.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.BdTempPrtMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdTempPrtDept;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 科室自定义执行单
 * @author ds
 *
 */
@Service
public class BdTempPrtService {
	
	@Autowired
	private BdTempPrtMapper bdTempPrtMapper;
	/**
	 * 查询右侧列表
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryTempPrtList(String param,IUser user){
		BdTempPrtDept qryparam = JsonUtil.readValue(param,BdTempPrtDept.class);
		if (qryparam == null )
			throw new BusException("查询参数有问题！");
		if(StringUtils.isEmpty(qryparam.getPkDept())){
			throw new BusException("科室参数为空，请检查！");
		}
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if (paramMap == null || CommonUtils.isNull(paramMap.get("pkOrg"))) {
			paramMap.put("pkOrg", UserContext.getUser().getPkOrg());
		}
		List<Map<String,Object>> list=bdTempPrtMapper.queryTempPrtList(paramMap);
		return list;
	}
	/**
	 * 保存
	 * @param param
	 * @param user
	 */
	public void saveTempPrt(String param, IUser user){
		List<BdTempPrtDept> list = JsonUtil.readValue(param, new TypeReference<List<BdTempPrtDept>>(){});
		for (BdTempPrtDept bdTempPrtDept : list) {
			if(StringUtils.isEmpty(bdTempPrtDept.getPkTempprtdept())){
				int count=DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_PRT_DEPT where DT_ORDEXTYPE=? and del_flag='0' and PK_DEPT=?", Integer.class, bdTempPrtDept.getDtOrdextype(),bdTempPrtDept.getPkDept());
				if(count>0){
					throw new BusException("同一个科室，执行单类型不能重复，请检查！");
				}
				ApplicationUtils.setDefaultValue(bdTempPrtDept, true);
				bdTempPrtDept.setDelFlag("0");
				bdTempPrtDept.setPkOrg( UserContext.getUser().getPkOrg());
				DataBaseHelper.insertBean(bdTempPrtDept);
			}else{
				int count=DataBaseHelper.queryForScalar("select count(*) from BD_TEMP_PRT_DEPT where PK_TEMPPRTDEPT!=? and DT_ORDEXTYPE=? and del_flag='0' and PK_DEPT=? ", Integer.class, bdTempPrtDept.getPkTempprtdept(), bdTempPrtDept.getDtOrdextype(),bdTempPrtDept.getPkDept());
				if(count>0){
					throw new BusException("同一个科室，执行单类型不能重复，请检查！");
				}
				bdTempPrtDept.setPkOrg( UserContext.getUser().getPkOrg());
				DataBaseHelper.updateBeanByPk(bdTempPrtDept,false);
			}
		}
	}
	/**
	 * 删除
	 * @param param
	 * @param user
	 */
	public void delTempPrt(String param, IUser user) {
		BdTempPrtDept bdTermCcdt = JsonUtil.readValue(param,BdTempPrtDept.class);
		if(StringUtils.isEmpty(bdTermCcdt.getPkTempprtdept())){
			throw new BusException("主键为空，请检查参数!");
		}
		bdTermCcdt.setDelFlag("1");
		DataBaseHelper.updateBeanByPk(bdTermCcdt,false);
	}
}
