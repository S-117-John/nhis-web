package com.zebone.nhis.ex.pub.dao;

import java.util.List;
import java.util.Map;


import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExPvIpdailyMapper {
	/**
	 * 根据机构查询所有病区
	 * 
	 * @param org
	 * @return
	 */
	public List<String> queryDeptByOrg(String[] pkOrgArr);

	/**
	 * 查询额定床位数及开放床位
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getBedNumByDept(Map<String, Object> map);

	/**
	 * 查询期初人数
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getQichuNumByDept(Map<String, Object> map);

	/**
	 * 查询今日入院
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getDayInNumByDept(Map<String, Object> map);

	/**
	 * 查询今日出院
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getDayOutNumByDept(Map<String, Object> map);

	/**
	 * 查询转往他科
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getDeptAdtOutNumByDept(Map<String, Object> map);

	/**
	 * 查询他科转入
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getDeptAdtInByDept(Map<String, Object> map);

	/**
	 * 查询病重人数
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getBzNumByDept(Map<String, Object> map);

	/**
	 * 查询死亡人数
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getDeathByDept(Map<String, Object> map);

	/**
	 * 查询病危人数
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getBwNumByDept(Map<String, Object> map);

	/**
	 * 查询某级别护理人数
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getHLNumByDept(Map<String, Object> map);

	/**
	 * 留陪人数
	 * 
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getAttendNumByDept(Map<String, Object> map);

}
