package com.zebone.nhis.pv.reg.dao;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.pv.reg.vo.PvQueVO;
import com.zebone.nhis.pv.reg.vo.TransParamVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface QueTriageMapper {

	/**
	 * 查询当前诊台所属的队列
	 * 
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryQueByplatForm(Map<String, Object> qryParam);

	/**
	 * 查询挂号信息
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryRegisterInfo(Map<String, Object> qryParam);

	/**
	 * 根据病情评估等级生成“排序号”和“调整排队序号”
	 * @param pvQueVO
	 * @return
	 */
	public Map<String, Object> qryNo(PvQueVO pvQueVO);
	
	/**
	 * 查询科室下出诊医生和待诊人数
	 * @param pkSch
	 * @param dateWork 
	 * @param pkDateslot
	 * @return
	 */
	public List<Map<String, Object>> qryEmpToAutoDistribute(@Param("pkSchres")String pkSchres, @Param("pkDateslot")String pkDateslot, @Param("dateWork")String dateWork);

	/**
	 * 查询分诊台下的排班
	 * @param pkQcplatform
	 * @return
	 */
	public List<Map<String, Object>> qrySchByPlatForm(Map<String, Object> pkQcplatform);

	/**
	 * 更新门诊就诊pv_op
	 * @param transParam
	 * @param bdOuEmployee
	 */
	public void updatePvOp(@Param("transParam")TransParamVo transParam, @Param("bdOuEmployee")BdOuEmployee bdOuEmployee);

	/**
	 * 查询排队信息
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryQueinfo(Map<String, Object> qryParam);

	/**
	 * 查询转发队列
	 * @param changeParam
	 * @return
	 */
	public List<Map<String, Object>> qryChangeQue(Map<String, Object> changeParam);

	/**
	 * 转发队列
	 * @param qryNo
	 */
	public void changeQue(Map<String, Object> updateParam);

	/**
	 * 查询患者就诊信息
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryPvInfo(Map<String, Object> qryParam);

	/**
	 * 查询过号信息
	 * @param qryParam
	 * @return
	 */
	public List<Map<String, Object>> qryOverPvInfo(Map<String, Object> qryParam);

	/**
	 * 查询转科列表
	 * @param pkQcplatform
	 * @return
	 */
	public List<Map<String, Object>> qryTransDept(Map<String, Object> pkQcplatform);

	/**
	 * 查询患者是否就诊
	 * @param pkPv
	 * @param dateNow
	 * @return
	 */
	public Integer isSeek(@Param("pkPv") String pkPv, @Param("dateNow") Date dateNow);
	
}
