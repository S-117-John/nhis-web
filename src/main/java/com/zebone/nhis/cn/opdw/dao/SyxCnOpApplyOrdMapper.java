package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxCnOpApplyOrdMapper {

	//获取当前用户的常用检治项目
	public List<Map<String,Object>> qryPreferOrders(Map<String,Object>map);
	//查询患者检查治疗医嘱
	public List<Map<String,Object>> qryReqOrders(Map<String,Object>map);
	//查询患者检查治疗医嘱
	public List<Map<String,Object>> qryReqOrdersEx(Map<String,Object>map);
	/**
	 * 字典项目收费构造查询
	 * @param map
	 * @return
	 */
	List<BlPubParamVo> qryDocItemCgvo(Map<String,Object>map);

	List<BlPubParamVo> qryDiagItem(Map<String,Object>map);


	//查询门诊全院检查项目树
	public List<Map<String, Object>> queryRisLabTreeList(Map<String,Object>map);
	
	//查询门诊全院检验项目树
	public List<Map<String, Object>> queryLisLabTreeList(Map<String,Object>map);
	
	//获取当前就诊已经缴费的检验检查列表
    public List<Map<String, Object>> getLisLabCount(Map<String,Object>map);
	
	//查询检验附加费用
	public List<Map<String, Object>> queryLabAddItems(Map<String, Object> paramMap);

	//根据执行科室查询对应的诊区
	public List<Map<String, Object>> qeryDeptArea(String pkDeptExec);
}
