package com.zebone.nhis.pro.zsba.ex.dao;

import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.pro.zsba.ex.vo.ExCgBaVo;
import com.zebone.nhis.pro.zsba.ex.vo.FreqTimeBaVo;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 执行单处理类
 *
 * @author yangxue
 */
@Mapper
public interface OrderExecListBaMapper {
	/**
	 * 查询医嘱频次时刻列表
	 *
	 * @return
	 */
	public List<FreqTimeBaVo> queryFreqTimeList(@Param("code") String code);

	/**
	 * 查询医嘱执行单（根据注入sql的方式）
	 *
	 * @return
	 */
	public List<ExCgBaVo> queryExListByWhereSql(@Param("sql") String sql);


	/**
	 * 查询已核对未生成执行单医嘱
	 *
	 * @param map
	 * @return
	 */
	public List<OrderCheckVO> getCheckedOrdList(Map<String, Object> map);


	/**
	 * 根据添加的任何条件查询医嘱执行单及计费信息
	 *
	 * @param paramMap
	 * @return
	 */
	public List<ExCgBaVo> queryExAndCglist(@Param("pkExocc") String pkExocc);

	/**
	 * 查询指定时间区间的执行单
	 *
	 * @param paramMap
	 * @return
	 */
	public List<ExOrderOcc> queryExListByDate(Map<String, Object> paramMap);

	/**
	 * 根据医嘱主键，作废医嘱执行单
	 *
	 * @param map{pkOrds}
	 * @return
	 */
	public void cancelExecListByPkOrd(Map<String, Object> map);


	/**
	 * 根据医嘱主键，查询医嘱对应的已执行执行单列表
	 *
	 * @param map{pkOrds}
	 * @return
	 */
	public List<Map<String, Object>> queryOrderExecListByOrd(Map<String, Object> map);

	/**
	 * 根据不同的条件查询符合生成执行单条件的医嘱
	 *
	 * @return
	 */
	public List<GenerateExLisOrdVo> queryOrderPlanList(Map<String, Object> map);


	/**
	 * 根据添加的任何条件查询医嘱执行单及计费信息
	 *
	 * @return
	 */
	public List<ExOrderOcc> queryExlist(@Param("pkCnord") String pkCnord);
}
