package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PskqPlatFormSendCnMapper {

    /**
     * 获取手术申请单信息
     * @param map
     * @return
     */
     SurgeryApplicationOrder getOpApplyInfoById(Map<String, Object> map);


     /**
 	 * 检查申请
 	 * @param paramMap{pkPv:"就诊主键","type":"ip/op"}
 	 * @return
 	 */ExamApply qryRisInfo(Map<String,Object> paramMap);
 	
 	/**
     * 获取入院登记信息
     * @param map
     * @return
     */
 	EncounterInpatient getEncounterIpAdtById(Map<String, Object> map);


	/**
	 * 查询转科信息
	 * @param map
	 * @return
	 */
	AdtChangeInfo getEncounterIpAdtChangeDeptById(Map<String, Object> map);


	/**
	 * 查询换床信息
	 * @param map
	 * @return
	 */
	AdtChangeInfo getEncounterIpAdtChangeBedById(Map<String, Object> map);


	/**
     * 住院计费信息
     * @param map
     * @return
     */
 	List<CostDetailInpat> queryIpCostDetailInfo(Map<String, Object> map);
 	
 	/**
     * 获取门诊住院检验申请单信息
     * @param list
     * @return
     */
    List<LabApply> queryLisApplyInfoById(List<String> list);

	/**
	 * 获取门诊住院检验申请单信息
	 * @param list
	 * @return
	 */
	List<ExamApply> queryPacsApplyInfoById(List<String> list);

	/**
	 * 查询门诊医嘱
	 * 单条查询、包含药品非药品医嘱
	 * @return pkCnord
	 */
	//OrderOutpat queryOpCnorder(String pkCnord);

	/**
	 * 门诊医嘱查询
	 * 查询非药品类型医嘱项目
	 * @param list
	 * @return
	 */
	List<OrderOutpat> queryOpCnorderProject(List<String> list);



	/**
	 * 门诊医嘱查询
	 * 查询非草药的药品医嘱信息
	 * @param list
	 * @return
	 */
	List<OrderOutpat> queryOpCnorderDrugs(List<String> list);
	
	/**
	 * 住院医嘱查询
	 ** 查询非药品类型医嘱项目
	 * @param list
	 * @return
	 */
	List<OrderExecRecord> queryIpCnorderProject(List<String> list);
	
	/**
	 * 住院医嘱查询
	 * 查询非草药的药品医嘱信息
	 * @param list
	 * @return
	 */
	List<OrderExecRecord> queryIpCnorderDrugs(List<String> list);
}
