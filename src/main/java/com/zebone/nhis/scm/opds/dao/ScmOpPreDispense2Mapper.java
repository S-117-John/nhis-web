package com.zebone.nhis.scm.opds.dao;

import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScmOpPreDispense2Mapper {
	/**
	 * 查询本机关联的窗口号
	 * @param paramMap {"namePc":"计算机名"} 
	 * @return {"winno":"当前窗口号" }
	 */
	public Map<String, Object> qryLocalWinNo(Map<String,Object> paramMap);
	
	/**
	 * 查询待配药处方信息 
	 * @param paramMap { "pkDept":"发药药房","winnoPrep":"配药窗口"}
	 * @return 待配药处方信息
	 */
	public List<Map<String,Object>> qryUnMatchPres(Map<String,Object> paramMap);
	
	/**
	 * 查询处方明细
	 * @param paramMap{"pk_presocc":"处方主键 ","pkDept":"当前科室"}
	 * @return 处方明细
	 */
	public List<Map<String,Object>> qryPresDetail(Map<String,Object> paramMap);
	
	/**
	 * 查询已完成配药处方信息
	 * @param paramMap {"pkDept":"发药药房","winnoPrep":"配药窗口"}
	 * @return 已配药未发药处方信息
	 */
	public List<Map<String,Object>> qryMatchPres(Map<String,Object> paramMap);
	
	/**
	 * 查询未配药暂挂的处方信息
	 * @param paramMap  {"pkDept":"发药药房","winnoPrep":"配药窗口"}
	 * @return 配药暂挂的处方信息
	 */
	public List<Map<String,Object>> qryPresPending(Map<String,Object> paramMap);
	
	/**
	 * 修改处方状态为打印
	 * @param paramMap {"pkPresoccs":"处方"}
	 */
	public void updatePresPrintInfo(List<String> pkPresoccs);
	
	/**
	 * 确认配药
	 * @param paramMap {"pkPresocc":"配药处方","datePrep":"配药时间"," pkEmp":"配药人","nameEmp":"配药人姓名"}
	 */
	public void saveConfirmDosageInfo(Map<String,Object> paramMap);
	
	/**
	 * 取消配药
	 * @param paramMap {"pkPresocc":"配药处方","pkEmp":"取消配药人" }
	 */
	public Integer cancelPresDosageInfo(Map<String,Object> paramMap);
	
	/**
	 * 处理处方暂挂信息
	 * @param paramMap {"pkPresoccs":"配药处方"}
	 */
	public void doPresPending(List<String> pkPresoccs);
	
	/**
	 * 查询可选择配药窗口
	 * @param paramMap {"pkDept":"药房","euButype":"业务类型  （0配药1发药）","pkDeptunit":"当前窗口"}
	 * @return 可选择窗口
	 */
	public List<Map<String,Object>> qryDosageFormInfo(Map<String,Object> paramMap);

	void updateCodeBasketNull(Map<String, Object> paramMap);

	/**
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresPivasCount(Map<String,Object> paramMap);

	/**
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryUserDepartment(Map<String,Object> paramMap);

}
