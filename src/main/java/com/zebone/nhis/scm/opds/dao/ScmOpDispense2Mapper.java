package com.zebone.nhis.scm.opds.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmOpDispense2Mapper {
	/**
	 *查询本机对应的发药窗口
	 * @param paramMap{"namePc":"计算机名称（地址）"}
	 * @return{"pkDeptunit":"","code":"","name":"","flagOpen":"常开标志"}
	 */
	public Map<String,Object> qryLocalPdUpForm(Map<String,Object> paramMap);
	
	/**
	 * 查询未完成患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryUnFinishedPiInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询完成患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryFinishedPiInfo(Map<String,Object> paramMap);
	/**
	 * 查询暂挂患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryPendingPiInfo(Map<String,Object> paramMap);
	/**
	 * 查询未完成处方信息
	 * @param paramMap{"pkPv":"就诊主键","pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryUnFinishedPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询已完成处方信息
	 * @param paramMap{"pkPv":"就诊主键","pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryFinishedPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询暂挂处方信息
	 * @param paramMap{"pkPv":"就诊主键","pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryPendingPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询处方明细信息
	 * @param paramMap{"pkPresocc":"处方执行主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryPresDetialInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询可选择的发药窗口
	 * @param paramMap{"pkDept":"当前药房","pkDeptunit":"当前窗口主键"}
	 * @return
	 */
	public List<Map<String,Object>> selectPdUpForm(Map<String,Object> paramMap);
	
	/**
	 * 发药信息暂挂处理
	 * @param paramMap{"pkPresocc":"处方","winno":"发药窗口"}
	 */
	public void updatePendingPd(Map<String,Object> paramMap);

	/**
	 * 开始发药
	 * @param paramMap
	 */
	public void startPdForm(Map<String,Object> paramMap);
	
	/**
	 * 停止发药
	 * @param paramMap
	 */
	public void stopPdForm(Map<String,Object> paramMap); 
	
	/**
	 * 查询已经发送外部接口的草药订单信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryHerbOrder(Map<String,Object> paramMap);
	
	/**
	 * 获取门诊号
	 * @param paramMap
	 * @return
	 */
	public List<String> getCodeOpByPiInfo(Map<String,Object> paramMap);
}
