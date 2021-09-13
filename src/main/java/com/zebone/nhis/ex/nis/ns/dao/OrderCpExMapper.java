package com.zebone.nhis.ex.nis.ns.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;


/**
 * 临床路径医嘱确认业务操作接口
 * @author yangxue
 *
 */
@Mapper
public interface OrderCpExMapper {
	
	/**
	 * 获取病区已入路径患者
	 * @param pkDeptNs
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	public List<Map<String,Object>> queryPatiList(String pkDeptNs);
	
	/**
	 * 获取所选患者工作记录
	 * @param pkPvs
	 * @return
	 */
	public List<Map<String,Object>> queryOrderCpList(Map<String,Object> paramMap);
}
