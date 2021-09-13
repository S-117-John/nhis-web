package com.zebone.nhis.ex.nis.ns.dao;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.ex.nis.ns.vo.*;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
/**
 * 执行单处理类
 * @author yangxue
 *
 */
@Mapper
public interface OrderExecListMapper {
	/**
	 * 根据医嘱主键，查询医嘱对应的已执行执行单列表
	 * @param map{pkOrds}
	 * @return
	 */
	public List<Map<String,Object>> queryOrderExecListByOrd(Map<String,Object> map);
	
	/**
	 * 根据医嘱主键，作废医嘱执行单
	 * @param map{pkOrds}
	 * @return
	 */
	public void cancelExecListByPkOrd(Map<String,Object> map);
	/**
	 * 根据不同的条件查询符合生成执行单条件的医嘱
	 * @param 目前支持，map{pkPvs,dateEnd,flagDrug,pkDeptNs}
	 * @return
	 */
	public List<GenerateExLisOrdVo>  queryOrderPlanList(Map<String,Object> map);
	
	/**
	 * 根据医嘱的pkCnord查询医嘱执行单列表
	 * @map
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByPkCnord(Map<String,Object> map);
	
	/**
	 * 根据不同条件查询执行单列表（主要用于查询执行确认与取消执行的执行单列表查询）
	 * @param map{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,cancelFlag,nameOrd,pkPvs,pkDeptNs}
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByCon(Map<String,Object> map);
	/**
	 * 根据不同条件查询执行单列表（主要用于查询执行确认与取消执行的执行单列表查询,其中执行确认含已请领未发药的执行单）
	 * @param map{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,cancelFlag,nameOrd,pkPvs,pkDeptNs}
	 * @return
	 */
	public List<ExlistPubVo> queryExecListContainApDrug(Map<String,Object> map);
	
	/**
	 * 根据患者查询未执行试敏医嘱
	 * @param map{dateBegin,dateEnd,pkPvs}
	 * @return
	 */
	public List<Map<String,Object>> queryStUnExecListByPv(Map<String,Object> map);
	/**
	 * 根据患者查询已执行试敏医嘱
	 * @param map{dateBegin,dateEnd,pkPvs}
	 * @return
	 */
	public List<Map<String,Object>> queryStExecListByPv(Map<String,Object> map);
	/**
	 * 根据患者查询全部试敏医嘱
	 * @param map{dateBegin,dateEnd,pkPvs}
	 * @return
	 */
	public List<Map<String,Object>> queryAllStExecListByPv(Map<String,Object> map);
	
	/**
	 * 查询血糖执行单列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryBloodList(Map<String,Object> map);
	
	/**
	 * 更新医嘱执行单
	 * @param exOrderOcc
	 */
	public void updateExOrderOcc(ExOrderOcc exOrderOcc);
	/**
	 * 更新关联皮试医嘱结果
	 * @param ordList,euSt,result
	 */
	public void updateStOrd(Map<String,Object> map);
	/**
	 * 查询需要关联的试敏医嘱
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryStOrdList(Map<String,Object> map);
	/**
	 * 查询医嘱频次时刻列表
	 * @param map
	 * @return
	 */
	public List<FreqTimeVo> queryFreqTimeList(@Param("code") String code);
	/**
	 * 查询医嘱执行单（根据注入sql的方式）
	 * @param map
	 * @return
	 */
	public List<ExCgVo> queryExListByWhereSql(@Param("sql") String sql);
	/**
	 * 根据执行单号及住院号查询执行单列表
	 * @param map
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByPkAndCodeIp(Map<String,Object> map);
	/**
	 * 查询已核对未生成执行单医嘱
	 * @param map
	 * @return
	 */
	public List<OrderCheckVo> getCheckedOrdList(Map<String,Object> map);
	
	/**
	 * 检验标本采集查询
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryLabExlist(Map<String,Object> map);
	public List<Map<String,Object>> qryLabExlistSsr(Map<String,Object> map);
	/**
	 * 检验标本采集查询-为了并单
	 * @param map
	 * @return
	 */
	public List<LabColVo> qryLabExlistForDif(Map<String,Object> map);
	public List<LabColVo> qryLabExlistForDifSsr(Map<String,Object> map);
	
	
	/**
	 * 根据患者及容器编码组装收费VO
	 */
	public List<BlPubParamVo> qryLabContCgVo(List<String> pkCgList);
	public List<BlPubParamVo> qryLabContCgVoSsr(List<String> pkCgList);
	/**
	 * 根据患者及标本编码组装收费VO
	 */
	public List<BlPubParamVo> qryLabSpecCgVo(List<String> pkSpecList);
	public List<BlPubParamVo> qryLabSpecCgVoSsr(List<String> pkSpecList);
	/**
	 * 查询指定时间区间的执行单
	 * @param paramMap
	 * @return
	 */
	public List<ExOrderOcc> queryExListByDate(Map<String,Object> paramMap);
	/**
	 * 删除指定条件的执行单
	 * @param paramMap
	 */
	public void deleteExList(Map<String,Object> paramMap);
	/**
	 * 更新输血申请单
	 * @param codeApplys
	 */
	public void updateTransApply(List<ExlistPubVo> list);
	/**
	 * 更新临时医嘱的执行护士
	 * @param list
	 */
	public void updateOrdEmpNsEx(Map<String,Object> paramMap);
	/**
	 * 根据添加的任何条件查询医嘱执行单及计费信息
	 * @param paramMap
	 * @return
	 */
	public List<ExCgVo> queryExAndCglist(@Param("pkExocc")String pkExocc);
	/**
	 * 根据添加的任何条件查询医嘱执行单及计费信息
	 * @param paramMap
	 * @return
	 */
	public List<ExOrderOcc> queryExlist(@Param("pkCnord")String pkCnord);
	/**
	 * 查询带有患者信息的皮试结果
	 * @param paramMap
	 * @return
	 */
	public List<ExStOccPv> queryExStOccPv(@Param("pkCnord")String pkCnord,@Param("pkPv")String pkPv);

	List<Map<String, Object>> queryAdtByPkpv(Map<String,Object> paramMap);

	List<ExOrderOcc> queryOrderOcc(Map<String, Object> map);

	/**
	 * 查询代理执行单列表
	 * @param map{dateBegin,dateEnd,euStatus,pkPvs,pkDeptNs,pkDeptEx}
	 * @return
	 */
	public List<ExlistPubVo> queryAgentExecList(Map<String, Object> map);

	/**
	 * 根据父医嘱查询对应非药品子医嘱执行单
	 * @param
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByParent(Map<String,Object> map);
}
