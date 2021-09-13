package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.srv.BdQcPlatform;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface QcPlatFormMapper {

	/**
	 * 查询分诊台信息
	 * 
	 * @param pkOrgarea
	 * @return
	 */
	public List<BdQcPlatform> qryQcPlatFormInfo(String pkOrgarea);

	/**
	 * 查询分诊台队列
	 * 
	 * @param pkQcplatform
	 * @return
	 */
	public List<Map<String, Object>> qryQcPlatFormQue(String pkQcplatform);

	/**
	 * 查询可导入队列
	 * 
	 * @param pkDept
	 * @return
	 */
	public List<Map<String, Object>> qryCanImpPlatForm(String pkOrgarea);

	/**
	 * 查询分诊权限信息
	 * 
	 * @param pkUser
	 * @param pkOrgarea
	 * @return
	 */
	public List<Map<String, Object>> qryAuthPlatFormInfo(@Param("pkUser")String pkUser, @Param("pkOrgarea")String pkOrgarea);

	/**
	 * 查询院区下的科室信息
	 * 
	 * @param pkOrgarea
	 * @return
	 */
	public List<BdOuDept> qryDeptInfo(String pkOrgarea);

	/**
	 * 查询机构下是否有权限的用户
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryUserByArea(Map<String, Object> qryParam);

	/**
	 * 查询序号是否重复
	 * @param qryParam
	 * @return
	 */
	public Integer isRepeatNo(Map<String, Object> qryParam);

}
