package com.zebone.nhis.ex.nis.qry.dao;

import java.math.BigDecimal;
import java.util.Map;

import com.zebone.nhis.common.module.pv.PvIpDaily;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface DeptDayReportMapper {
   /**
    * 查询额定床位数及开放床位
    * @param map
    * @return
    */
	public Map<String,Object> getBedNumByDept(Map<String,Object> map);
	/**
    * 查询期初人数
    * @param map
    * @return
    */
	public BigDecimal getQichuNumByDept(Map<String,Object> map);
	/**
    * 查询今日入院
    * @param map
    * @return
    */
	public BigDecimal getDayInNumByDept(Map<String,Object> map);
	/**
    * 查询今日出院
    * @param map
    * @return
    */
	public BigDecimal getDayOutNumByDept(Map<String,Object> map);
	/**
    * 查询转往他科
    * @param map
    * @return
    */
	public BigDecimal getDeptAdtOutNumByDept(Map<String,Object> map);
	/**
    * 查询他科转入
    * @param map
    * @return
    */
	public BigDecimal getDeptAdtInByDept(Map<String,Object> map);
	/**
    * 查询病重人数
    * @param map
    * @return
    */
	public BigDecimal getBzNumByDept(Map<String,Object> map);
	/**
	 * 查询死亡人数
	 * @param map
	 * @return
	 */
	public BigDecimal getDeathByDept(Map<String,Object> map);
	
	/**
    * 查询病危人数
    * @param map
    * @return
    */
	public BigDecimal getBwNumByDept(Map<String,Object> map);
	/**
	 * 查询某级别护理人数
	 * @param map
	 * @return
	 */
	public BigDecimal getHLNumByDept(Map<String,Object> map);
	
	/**
	 * 留陪人数
	 * @param map
	 * @return
	 */
	public BigDecimal getAttendNumByDept(Map<String,Object> map);
	
	/**
	 * 获取病区日报
	 * @param map
	 * @return
	 */
	public PvIpDaily getDeptDayReportByPkAndDate(Map<String,Object> map);
	/**
	 * 修改病区日报
	 * @param map
	 * @return
	 */
	public Integer modifyDeptDayReport(PvIpDaily pvIpDaily);

}
