package com.zebone.nhis.base.bd.service;

import java.util.*;

import javax.annotation.Resource;

import com.zebone.nhis.ex.nis.ns.vo.FreqTimeVo;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.DeptOrderOpenSetMapper;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdAtd;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class DeptOrderOpenSetService {

	@Resource
	private DeptOrderOpenSetMapper deptOrderOpenSetMapper;
	//院区查询：001002007079
	public List<Map<String, Object>> qryCourt(String param,IUser user){

		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");

		return deptOrderOpenSetMapper.qryCourt(pkOrg);
	}

	//院区可开立医嘱查询：001002007080
	public List<Map<String, Object>> qryCourtDetail(String param,IUser user){

		String pkOrgarea = JsonUtil.getFieldValue(param, "pkOrgarea");

		return deptOrderOpenSetMapper.qryCourtDetail(pkOrgarea);
	}

	//科室开立医嘱保存：001002007081
	public void saveCourtDeptDetail(String param,IUser user){

		List<BdOrdAtd> list = JsonUtil.readValue(param,new TypeReference<List<BdOrdAtd>>() {});
		for (BdOrdAtd bdOrdAtd : list) {
			try{
				if(StringUtils.isNotBlank(bdOrdAtd.getPkOrdatd())){
					DataBaseHelper.updateBeanByPk(bdOrdAtd,false);
				}else{
					DataBaseHelper.insertBean(bdOrdAtd);
				}
			}catch(Exception e){
				throw new BusException("保存失败");
			}
		}
	}

	//科室开立医嘱删除：001002007082
	public void delCourtDeptDetail(String param,IUser user){

		List<BdOrdAtd> list = JsonUtil.readValue(param,new TypeReference<List<BdOrdAtd>>() {});
		for (BdOrdAtd bdOrdAtd : list) {
			if(StringUtils.isBlank(bdOrdAtd.getPkOrdatd())){
				throw new BusException("主键丢失，删除失败！");
			}
			String sql = "delete from bd_ord_atd where pk_ordatd = ? ";
			int del = DataBaseHelper.execute(sql, bdOrdAtd.getPkOrdatd());
			if(del == 0){
				throw new BusException("删除失败！");
			}
		}
	}

	//科室列表查询：001002007083
	public List<Map<String, Object>> qryDept(String param,IUser user){

		String pkOrg = JsonUtil.getFieldValue(param, "pkOrg");

		return deptOrderOpenSetMapper.qryDept(pkOrg);
	}

	//科室开立医嘱明细查询：001002007084
	public List<Map<String, Object>> qryDeptDetail(String param,IUser user){

		String pkDept = JsonUtil.getFieldValue(param, "pkDept");
		Map<String,Object> map = new HashMap<>(16);
		map.put("pkDept",pkDept);
		map.put("dbType",MultiDataSource.getCurDbType());
		List<Map<String, Object>> deptFather = deptOrderOpenSetMapper.qryDeptFather(map);
		return deptFather;
	}

	//科室开立医嘱类型查询：001002007085
	public List<Map<String, Object>> qryOrdtype(String param,IUser user){

		return deptOrderOpenSetMapper.qryOrdtype();
	}

	//科室开立医嘱导入查询：001002007086
	public List<Map<String, Object>> qryImportList(String param,IUser user){

		Map<String, Object> map = JsonUtil.readValue(param, Map.class);

		return deptOrderOpenSetMapper.qryImportList(map);
	}

	/**
	 *
	 * 001002007096
	 * 查询科室开立医嘱列表
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> findOrderList(String param,IUser user){
		Map<String, Object> map = JsonUtil.readValue(param, Map.class);
		List<Map<String,Object>> result = deptOrderOpenSetMapper.findOrderList(map);
		return result;
	}



}
