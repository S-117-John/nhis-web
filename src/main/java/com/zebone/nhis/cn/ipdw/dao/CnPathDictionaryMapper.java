package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.cp.BdCpExp;
import com.zebone.nhis.common.module.cn.ipdw.BdCpReason;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnPathDictionaryMapper {
	/*路径原因*/
	public List<Map<String, Object>> qryPathCr();
	
	public List<String> countCrUse(List<BdCpReason> list);
	
	public Integer getMaxCrSortno();
	
	public void delPathCr(List<BdCpReason> list);
	
	/*变异数据*/
	public List<Map<String, Object>> qryPathCe();
	
	public Integer getMaxCeSortno();

	public List<String> countCeUse(List<BdCpExp> list);

	public void delPathCe(List<BdCpExp> list);
	
	/*工作数据*/
	public List<Map<String, Object>> qryPathCt();

	public Integer getMaxCtSortno();

	public List<String> countCtUse(List list);

	public void delPathCt(List list);

}
