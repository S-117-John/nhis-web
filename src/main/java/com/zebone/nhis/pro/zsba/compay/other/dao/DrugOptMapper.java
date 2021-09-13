package com.zebone.nhis.pro.zsba.compay.other.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptResult;
import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptVote;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 投票 - 数据库操作
 * @author 
 */
 @Mapper
public interface DrugOptMapper{   

	 /**
	  * 查询单人/多人的投票明细
	  * @param 
	  * @return
	  */
	 public List<Map<String,Object>> qryOptVoteS(Map<String,Object> paramMap);
	 
	 /**
	  * 查询票选结果
	  * @param 
	  * @return
	  */
	 public List<Map<String,Object>> qryOptResult(Map<String,Object> paramMap);
	 
	 /**
	  * 查询二次票选结果
	  * @param 
	  * @return
	  */
	 public List<DrugOptResult> qryOptResult2(Map<String,Object> paramMap);
	 
	 /**
	  * 查询票选最终结果
	  * @param 
	  * @return
	  */
	 public List<Map<String,Object>> qryOptResultEnd();

	 /**
	  * 查询二次的投票明细
	  * @param 
	  * @return
	  */
	 public List<Map<String,Object>> qryOptVoteSecond(Map<String,Object> paramMap);
	 
}

