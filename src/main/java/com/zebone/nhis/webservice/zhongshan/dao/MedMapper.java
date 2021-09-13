package com.zebone.nhis.webservice.zhongshan.dao;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.webservice.zhongshan.vo.BlCgVo;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecord;
import com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecordRisVo;
import com.zebone.nhis.webservice.zhongshan.vo.PvEncounterVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MedMapper {

	/**
	 * 分情况查询 【心电】申请单列表 - CIS / HIS -- 【ECG接口】
	 *
	 * @param paramMap()
	 * @return
	 */
	public List<PacsApplyRecord> queryAppList(Map<String, Object> paramMap);

	/**
	 * 分情况查询 【心电】申请单列表 - NHIS -- 【ECG接口】
	 *
	 * @param paramMap()
	 * @return
	 */
	public List<PacsApplyRecord> queryIpEcgAppListFromNHIS(Map<String, Object> paramMap);

	/**
	 * 查询NHIS系统检验申请单-根据申请开始时间/申请结束时间/住院号/申请科室
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryLabApplyInfo(Map<String, Object> paramMap);

	/**
	 * 查询【全部Ris】申请单列表 - NHIS（心电除外）--【Ris接口】
	 *
	 * @param paramMap()
	 * @return
	 */
	public List<PacsApplyRecordRisVo> queryRisAppFromNHIS(Map<String, Object> paramMap);

	/**
	 * 根据条形码查询记费所需数据 -- 【Lis接口】
	 *
	 * @param paramMap
	 * @return
	 */
	public List<BlCgVo> querylisForCg(Map<String, Object> paramMap);

	/**
	 * 根据条形码查询医嘱记录 -- 【Lis接口】
	 *
	 * @param paramMap
	 * @return
	 */
	public List<CnOrder> querylisForOrd(Map<String, Object> paramMap);

	/**
	 * 根据医嘱主键查询检查线相关信息
	 *
	 * @param paramMap 第三方传来信息
	 * @return
	 */
	public List<Map<String, Object>> queryOrdRis(Map<String, Object> paramMap);

	/**
	 * @return java.util.List<java.lang.String>
	 * @Description 查询患者是否有门诊有效就诊记录
	 * @auther wuqiang
	 * @Date 2021-02-25
	 * @Param [paramMap]
	 */
	public List<String> getPkPvByOp(Map<String, Object> paramMap);

	/**
	 * @return java.util.List<java.lang.String>
	 * @Description 查询患者是否有住院有效就诊记录
	 * @auther wuqiang
	 * @Date 2021-02-25
	 * @Param [paramMap]
	 */
	public List<String> getPkPvByIp(Map<String, Object> paramMap);

    /**
	 * @return java.util.List<com.zebone.nhis.webservice.zhongshan.vo.PacsApplyRecord>
	 * @Description 查询门诊心电申请单列表
	 * @auther wuqiang
	 * @Date 2021-02-25
	 * @Param [param]
	 */
	public List<PacsApplyRecord> queryOpEcgAppListFromNHIS(Map<String, Object> param);

	public void updateExAssOccE(Map<String, Object> param);

	public void cancelOpExtem(Map<String, Object> param);

	List<PacsApplyRecordRisVo> queryOpRisAppFromNHIS(Map<String, Object> param);

	PvEncounterVo getPvLab(@Param("codeApply") String codeApply,@Param("patient_id") String patient_id, @Param("admiss_times") String admiss_times);

}
