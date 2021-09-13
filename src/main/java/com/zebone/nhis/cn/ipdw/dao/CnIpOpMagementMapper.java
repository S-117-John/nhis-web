package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.CnOpSureParam;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnIpOpMagementMapper {
	/**
	 * 手术管理-查询手术申请单列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryOpApplyByParam(Map<String,Object> map);
	
	/**
	 * 查询手术医嘱录入的费用
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryOpApplyFeeByParam(Map<String,Object> map);
	

	/**
	 * 查询当前病人医嘱中检查的执行状态
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> GetRisStatus(Map<String,Object> map);
	
	/**
	 * 麻醉管理-查询手术申请单列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryAnaeApplyByParam(Map<String,Object> map);
	/**
	 * 手术管理-查询已确认/未确认医嘱
	 * @param map
	 * @return
	 */
	public List<CnOpSureParam> qrySureOrd(Map<String,Object> map);
	/**
	 * 查询费用明细
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryChargeDetail(Map<String, Object> map);
	/**
	 * 查询费用汇总
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryChargeSum(Map<String, Object> map);
	/**
	 * 查询手术科室的病人医嘱
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryPvCnOrder(Map<String, Object> map);
	
	/**
	 * 统计查询医技科室收费项目
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryItemSum(Map<String, Object> map);
	//手麻医嘱已确认的医嘱
	public List<Map<String, Object>> qryConfirmPvCnOrder(Map<String, Object> mo);

	/**
	 * 获取手术医嘱执行确认列表
	 * @param m
	 * @return
	 */
    List<Map<String, Object>> medicalAdviceList(Map<String, Object> m);

	/**
	 * 根据执行单号，查询执行单信息
	 * @param m
	 * @return
	 */
	public List<ExlistPubVo> qryExecList(Map<String, Object> m);


	/**
	 * 根据执行单号及住院号查询执行单列表
	 * @param map
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByPkAndCodeIp(Map<String,Object> map);

	void updateExOrderOcc(Map<String, Object> map);
	
	/**
	 * 手术管理-查询出院手术申请单列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryOpOutApply(Map<String, Object> map);
}
