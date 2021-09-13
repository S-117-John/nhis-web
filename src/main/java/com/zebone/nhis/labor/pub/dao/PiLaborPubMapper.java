package com.zebone.nhis.labor.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.labor.nis.PiLabor;
import com.zebone.nhis.labor.pub.vo.LabPatiCardVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PiLaborPubMapper {
	/**
	 * 根据不同条件查询孕妇档案信息,查询条件可扩展
	 * @param paramMap{pkOrg....}
	 * @return
	 */
	public List<PiLabor> queryPiLabor(Map<String,Object> paramMap);
	
	/**
	 * 根据患者主键或档案主键查询档案信息
	 * @param paramMap{pkPilabor,pkPi}
	 * @return
	 */
	public List<PiLabor> queryPiLaborByPk(Map<String,Object> paramMap);
	/**
	 * 根据患者就诊主键查询档案信息
	 * @param paramMap{pkPv}
	 * @return
	 */
	public List<PiLabor> queryPiInfo(Map<String,Object> paramMap);
	
	/**
	 * 根据患者或者就诊主键，查询患者信息
	 * @param paramMap{pkPi，pkPv}
	 * @return
	 */
	public List<Map<String,Object>> queryPatiInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询病区床位列表及患者相关信息
	 * @param paramMap
	 * @return
	 */
	public List<LabPatiCardVo> getBedInfo(Map<String,Object> paramMap);
	
	/**
	 * 根据病区获取在院患者列表
	 * @param param
	 * @param user
	 * @return{name_pi,name_bed,code_bed,pv_code,pk_pv}
	 */
	public List<Map<String,Object>> getLabPatiList(Map<String,Object> paramMap);
	
	/**
	 * 校验预产期
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> verfyPreBirthOracle(Map<String,Object> param);
	/**
	 * 校验预产期
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> verfyPreBirthSqlServer(Map<String,Object> param);
	/**
	 * 修改校验预产期
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> modifyverfyPreBirthOracle(Map<String,Object> param);
	
	/**
	 * 修改校验预产期
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> modifyverfyPreBirthSqlServer(Map<String,Object> param);
	/**
	 * 根据传入的科室判断是否在业务线中配置的属于临床科室
	 * @param param{pkDeptNs}
	 * @return{pk_deptbu}
	 */
	public List<Map<String,Object>> queryDeptBuByDept(Map<String,Object> paramMap);
	/**
	 * 根据业务线查询对应临床科室设置的护理单元
	 * @param param{pkDeptbu}
	 * @return{pk_dept}
	 */
	public List<Map<String,Object>> queryDeptByDeptBu(Map<String,Object> paramMap);
}
