package com.zebone.nhis.ex.nis.ns.dao;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

	/**
	 * 更新护嘱信息（含核对、作废、停止操作）
	 *
	 * @param map
	 */
	public void updateOrdNsInfo(Map<String, Object> map);

	/**
	 * 更新医嘱信息（核对）
	 *
	 * @param map{parentNos:list}
	 */
	public int updateOrdInfo(Map<String, Object> map);

	/**
	 * 根据患者获取待核对医嘱列表
	 *
	 * @param map
	 * @return
	 */
	public List<OrderCheckVo> queryOrderCheckList(Map<String, Object> map);

	/**
	 * 根据条件查询已核对医嘱列表
	 *
	 * @param map
	 * @return
	 */
	public List<OrderCheckVo> queryOrderEcList(Map<String, Object> map);

	/**
	 * 根据不同条件获取护嘱列表
	 *
	 * @param map
	 * @return
	 */
	public List<OrderCheckVo> queryOrderNsList(Map<String, Object> map);

	/**
	 * 根据不同条件获取医嘱费用列表
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryOrdFeeByPk(Map<String, Object> map);

	/**
	 * 根据父医嘱号查询符合条件的医嘱信息
	 *
	 * @param paramMap{parentList,flagDoctor,euStatusOrd}
	 * @return
	 */
	public List<CnOrder> queryOrdByParent(Map<String, Object> paramMap);

	/**
	 * 2019-03-02 查询医嘱对应的草药明细
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryPresDtByOrd(Map<String, Object> map);

	/**
	 * 根据医嘱用法编码，查询附加收费项目
	 *
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> querySupplyItemByCode(Map<String, Object> map);

	/**
	 * 根据医嘱主键获取检验项目的附加收费项目
	 */
	public List<Map<String, Object>> queryLisItemByPkCnorder(Map<String, Object> map);

	/**
	 * 根据医嘱主键医技医嘱记费所需数据
	 */
	List<OrderCheckVo> getOrder(@Param("pklist") List<String> pklist);
}
