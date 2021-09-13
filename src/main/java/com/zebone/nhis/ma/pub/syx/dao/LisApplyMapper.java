package com.zebone.nhis.ma.pub.syx.dao;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.syx.vo.TExamineItemSetListForIP;
import com.zebone.nhis.ma.pub.syx.vo.TExamineRequestForIP;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface LisApplyMapper {
	
	/**
	 * 查询待同步申请单列表 - nhis
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<TExamineRequestForIP> queryLisAppList(Map<String,Object> map);
	
	/**
	 * 查询门诊待同步申请单列表 - nhis
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<TExamineRequestForIP> queryOpLisAppList(Map<String,Object> map);
	
	/**
	 * 查询待同步申请 明细列表 - nhis
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<TExamineItemSetListForIP> queryLisFeeList(Map<String,Object> map);

	/**
	 * 查询已同步申请单列表 - lis
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<TExamineRequestForIP> queryLisAppListFromLis(List<TExamineRequestForIP> list);
	
	/**
	 * 查询已同步申请明细 列表 - lis
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public List<TExamineItemSetListForIP> queryLisFeeListFromLis(List<TExamineItemSetListForIP> list);
	
	/**
	 * 核收检验申请单【eu_status '2' => '3'】
	 * @param codeApplys
	 */
	public void updateLabApplyToChk(List<String> appList);

	/**
	 * 删除检验申请单
	 * @param codeApplys
	 */
	public void delExamineRequestForIP(List<Long> appList);

	/**
	 * 删除检验申请单对应明细
	 * @param codeApplys
	 */
	public void delExamineItemSetListForIP(List<Long> appList);
	
	/**
	 * 更新 LIS 库 检验申请单信息【作废 - 已读取未处理】
	 * @param map{pkPvs,apptype,codeApply}
	 * @return
	 */
	public void updateExamineRequestForIP(List<TExamineRequestForIP> list);
	
	/**
	 * 查询 当前操作人旧ID
	 * @param map
	 * @return
	 */
	public String queryOldIdByPkEmp(Map<String,Object> map);
}
