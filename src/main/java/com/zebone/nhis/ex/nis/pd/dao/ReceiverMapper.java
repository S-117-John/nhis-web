package com.zebone.nhis.ex.nis.pd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.nis.pd.vo.AcceptVo;
import com.zebone.nhis.ex.nis.pd.vo.DetailedVo;
import com.zebone.nhis.ex.nis.pd.vo.EmpVo;
import com.zebone.nhis.ex.nis.pd.vo.SummaryVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ReceiverMapper {
	
	/**
	 * 查询配药汇总列表
	 * @param map
	 * @return
	 */
	public List<SummaryVo> QueryDispenseList(Map<String,Object> map);
	
	/**
	 * 查询配药明细列表
	 * @param map
	 * @return
	 */
	public List<DetailedVo> QueryDetailedList(Map<String,Object> map);
	
	/**
	 * 查询当前签收人
	 * @param map
	 * @return
	 */
	public EmpVo QueryEmp(Map<String,Object> map);
	
	/**
	 * 更新签收状态
	 * @param accept
	 */
	public void UpdateState(AcceptVo accept);
}
