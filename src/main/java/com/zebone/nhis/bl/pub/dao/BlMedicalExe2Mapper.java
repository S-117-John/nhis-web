package com.zebone.nhis.bl.pub.dao;

import com.zebone.nhis.bl.pub.vo.RisPrintInfoVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医技执行接口服务(第二版)
 *
 * @author yangxue
 */
@Mapper
public interface BlMedicalExe2Mapper {
	/**
	 * 住院医技申请单查询
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpMedAppInfo(Map<String, Object> paramMap) throws BusException;

	/**
	 * 门诊医技申请单查询
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryOpMedAppInfo(Map<String, Object> paramMap) throws BusException;

	/**
	 * 住院：已记费下--申请单费用
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpMedBlDtInfo(Map<String, Object> paramMap) throws BusException;

	/**
	 * 住院：未记费下--申请单费用
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryIpMedBlDtCharge(Map<String, Object> paramMap) throws BusException;

	/**
	 * 查询门诊医技申请对应费用明细
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryOpMedBlDtInfo(Map<String, Object> paramMap) throws BusException;

	/**
	 * 查询医技申请单执行记录(门诊、住院及医疗记录查询公用)
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> queryExAssistOccList(Map<String, Object> paramMap) throws BusException;

	/**
	 * 医疗记录：医技申请单查询(住院)
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryMedAppInfoIp(Map<String, Object> paramMap);

	/**
	 * 医疗记录：医技申请单查询(门诊)
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryMedAppInfoOp(Map<String, Object> paramMap);

	/**
	 * 取消执行
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public void cancleExocc(Map<String, Object> paramMap);

	/**
	 * 住院退费
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public String queryCharge(String pkExocc);

	/**
	 * 查询记费详细数据
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> queryBlIpDt(String pkCg);

	/**
	 * 门诊数据校验
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public int opDataCheck(String string);

	/**
	 * 住院数据校验
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> ipDataCheck(String string);

	/**
	 * 医技执行
	 *
	 * @param pkAssOcc
	 * @param nameEmpOcc
	 * @param pkEmpOcc
	 * @param dataOcc
	 */
	public void medExeOcc(Map<String, Object> paramMap);

	/**
	 * 查询多条申请单费用合计
	 *
	 * @param pkExoccList
	 * @return
	 */
	public Double getTotalAmount(List<String> pkExoccList);

	/**
	 * 查询费用记录（未记费）重构
	 *
	 * @param pkCnordList
	 * @return
	 */
	public List<Map<String, Object>> qryIpMedBlDtCharge_refactor(List<String> pkCnordList);

	/**
	 * 查询费用记录（已记费） 重构
	 *
	 * @param pkListMap
	 * @return
	 */
	public List<Map<String, Object>> qryIpMedBlDtInfo_refactor(Map<String, Object> pkListMap);

	/**
	 * 查询执行单记录（重构）
	 *
	 * @param paramMaps
	 * @return
	 */
	public List<Map<String, Object>> queryExAssistOccList_refactor(@Param("pkCnordFather") String pkCnordFather, @Param("pkExoccFather") String pkExoccFather);

	/**
	 * 查询收费组套
	 *
	 * @param pkBlCgset
	 * @return
	 */
	public List<Map<String, Object>> qrycgSet(String pkBlCgset);

	/**
	 * 查询是否可退费，返回值大于0时可退费
	 *
	 * @param pkExoccList
	 * @return
	 */
	public Integer isRefund(List<String> pkExoccList);

	/**
	 * 查询对应的记费记录主键（重构）
	 *
	 * @param pkExoccList
	 * @return
	 */
	public List<String> queryCharge_refactor(List<String> pkExoccList);

	/**
	 * 退费功能：更新医嘱执行单
	 *
	 * @param pkExoccList
	 */
	public void updateExOrdOcc(List<String> pkExoccList);

	/**
	 * 如果关联的医技执行单未执行，做删除处理
	 *
	 * @param pkExoccList
	 */
	public void deleteExOrdOcc(List<String> pkExoccList);

	/**
	 * 计费功能：更新医嘱执行单
	 *
	 * @param updateOccParam
	 */
	public void updateOrdOcc(Map<String, Object> updateOccParam);
	
	/**
	 * 医技执行-F2计费功能：更新医嘱执行单
	 *
	 * @param updateOccParam
	 */
	public void updateOrdOccSpec(Map<String, Object> updateOccParam);

	/**
	 * 计费功能：更新医嘱表 cn_order
	 *
	 * @param updateOrderParam
	 */
	public void updateCnOrder(Map<String, Object> updateOrderParam);

	/**
	 * 判断是否有未执行的医嘱执行单，返回值大于0时返回，返回值为0时生成医技执行单
	 *
	 * @param pkExoccList
	 * @return
	 */
	public int isCharging(List<String> pkExoccList);

	/**
	 * 根据记费主键，查询到具体的记费数据
	 *
	 * @param pkCgList
	 * @return
	 */
	public List<BlIpDt> queryBlIpDt_refactor(List<String> pkCgList);

	/**
	 * 查询检查申请单打印信息
	 *
	 * @param pkPv
	 * @param pkDeptExec
	 * @return
	 */
	public List<RisPrintInfoVo> qryRisPrintInfo(@Param("pkOrdris") List<String> pkOrdris, @Param("pkDeptExec") String pkDeptExec);

	/**
	 * 退费功能：删除出医技执行单明细表
	 *
	 * @param pkExoccList
	 */
	public void deleteExOrdOccDt(List<String> pkExoccList);

	/**
	 * 批量更新申请单（检查、检验、输血）
	 *
	 * @param qryMap
	 */
	public void updateApply(List<Map<String, Object>> qryMap);

	/**
	 * 查询常用的收费项目
	 *
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryUsuItem(Map<String, Object> qryParam);

	/**
	 * 查询科室收费项目
	 *
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryDeptItem(Map<String, Object> qryParam);

	/**
	 * 退回医技申请
	 *
	 * @param pkOrdries
	 */
	public void cancelApply(String pkOrdries);

	/**
	 * 查询存在医技申请的住院患者
	 *
	 * @param qryPam
	 * @return
	 */
    public List<Map<String, Object>> qryPiInfo(Map qryPam);

	/**
	 * 根据pkpv查询该患者在该科室下的所有申请单
	 *
	 * @param pv
	 * @param pkPv
	 * @param pkDeptExec
	 * @return
	 */
	public List<Map<String, Object>> qryApplyByPkPv(@Param("pkPv") String pkPv, @Param("pkDeptExec") String pkDeptExec, @Param("euCharge") String euCharge);

    /**
	 * 根据主键查询患者在该科室下的可打印申请单
	 *
	 * @param qryPam
	 * @return
	 */
    public List<RisPrintInfoVo> qryAppPrtByPkPv(Map<String, Object> qryPam);

	/**
	 * 查询费用记录（已记费)可部分退
	 *
	 * @param pkListMap
	 * @return
	 */
	public List<Map<String, Object>> qryIpMedBlDtPartialRefundInfo(Map<String, Object> pkListMap);

	/**
	 * 查询费用记录
	 *
	 * @param pkListMap
	 * @return
	 */
	public List<Map<String, Object>> qryIpMedBlDtInfoRefactors(Map<String, Object> pkListMap);

	/**
	 * 门诊医技申请单查询
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> queryOpBaMedicalApp(Map<String, Object> paramMap) throws BusException;

}
