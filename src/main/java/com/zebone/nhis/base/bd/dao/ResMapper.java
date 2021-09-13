package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdSysparamTemp;
import com.zebone.nhis.common.module.base.bd.res.BdDeptUnit;
import com.zebone.nhis.common.module.base.bd.res.BdDeptUnitObj;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ResMapper {

	/**
	 * 根据所属机构，所属上级部门查询科室
	 * @param pkOrg
	 * @param pkDept
	 * @return
	 */
	List<Map<String, Object>> getBdOuDeptsByOrgDept(@Param("pkOrg") String pkOrg, @Param("pkDept") String pkDept);
	
	/**
	 * 根据所属机构，所属部门查询人员
	 * @param pkOrg
	 * @param pkDept
	 * @return
	 */
	List<Map<String, Object>> getBdOuEmployeesByOrgDept(@Param("pkOrg") String pkOrg, @Param("pkDept") String pkDept);
	
	/**
	 * 根据所属机构，所属部门查询手术台
	 * @param pkOrg
	 * @param pkDept
	 * @return
	 */
	List<Map<String, Object>> getBdResOptsByOrgDept(@Param("pkOrg") String pkOrg, @Param("pkDept") String pkDept);
	
	/**
	 * 根据所属机构，所属部门查询床位
	 * @param pkOrg
	 * @param pkDept
	 * @return
	 */
	List<Map<String, Object>> getBdResBedsByOrgDept(@Param("pkOrg") String pkOrg, @Param("pkDept") String pkDept);
	
	/**
	 * 根据所属机构，所属部门查询医技
	 * @param pkOrg
	 * @param pkDept
	 * @return
	 */
	List<Map<String, Object>> getBdResMspsByOrgDept(@Param("pkOrg") String pkOrg, @Param("pkDept") String pkDept);
	
	/**
	 * 查询当前机构下所有床位
	 * @param pkOrg
	 * @return
	 */
	public List<BdResBed> findAllBeds(@Param("pkOrg") String pkOrg); 
	
	/**
	 * 查询当前机构下所有床位
	 * @param pkOrg
	 * @return
	 */
	public List<BdResBed> findDeptBeds(@Param("pkOrg") String pkOrg,@Param("pkWard") String pkWard); 
	
	
	
	/**
	 * 根据当前机构下所有业务单元
	 * @param pkOrg
	 * @return
	 */
	public List<BdDeptUnit> findAllDeptUnits(@Param("pkOrg") String pkOrg);
	
	
	/**
	 * 查询待选参数
	 * @param name
	 * @return
	 */
	public List<BdSysparamTemp> getWaitParam(@Param("pkPc") String pkPc,@Param("name") String name);
	
	/**
	 * 查询已选参数
	 * @param pkPc
	 * @return
	 */
	public List<BdSysparamTemp> getSelectedParam(@Param("pkPc") String pkPc,@Param("name") String name);
	
	/**
	 * 查询药房窗口
	 * @param pkdept
	 * @return
	 */
	public List<BdDeptUnit> getDeptUnit(Map<String, Object> map);

	/**
	 * 查询窗口关联科室
	 * @param pkDeptunit
	 * @param deptName 
	 * @return
	 */
	public List<BdDeptUnitObj> getDeptUnitObj(@Param("pkDeptunit")String pkDeptunit, @Param("deptName")String deptName);

	/**
	 * 查询窗口关联的备选科室<!-- 废弃，不再使用 -->
	 * @param pkOrg
	 * @return
	 */
	public List<BdOuDept> qryOptDept(@Param("pkOrg")String pkOrg);
	
	//查询挂号分配列表
	List<Map<String, Object>> getPcDeptByPc(@Param("pkPc")String pkPc);

	//查询未分配的分诊台列表
	List<Map<String, Object>> qryNotUsePcDept(@Param("pkPc")String pkPc,@Param("pkOrg")String pkOrg);

	//查询已分配的分诊台列表
	List<Map<String, Object>> qryAlUsePcDept(@Param("pkPc")String pkPc);

	//查询本机构工作站编码最大值
	String qryMaxCode(@Param("pkOrg")String pkOrg);

	//门诊诊室设置查询科室列表
	List<Map<String, Object>> qryClinicSetDept(@Param("pkOrg")String pkOrg);
	
	//门诊诊室设置查询查询科室下的诊室信息
	List<Map<String, Object>> qryClinicSetDeptUnit(@Param("pkDept")String pkDept);
	
}
