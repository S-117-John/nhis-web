package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.opdw.vo.PiPresInfo;
import com.zebone.nhis.cn.opdw.vo.PiPresInfoDt;
import com.zebone.nhis.cn.opdw.vo.SyxCnOpPresVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxCnOpHerbMapper {

	List<SyxCnOpPresVo> qryHerbOrders(Map<String,Object> map);

	List<SyxCnOpPresVo> qryHerbOrdersSqlServer(Map<String,Object> map);

	List<PiPresInfo> getCopyPres(PiPresInfo para);
	
	List<PiPresInfo> getCopyPresOracle(PiPresInfo para);
	
	List<PiPresInfoDt> getCopyPresDt(@Param("pkPres") String pkPres,@Param("pkDept") String pkDept,@Param("pkHp") String pkHp);

	List<PiPresInfoDt> getCopyPresDtSqlServer(@Param("pkPres") String pkPres,@Param("pkDept") String pkDept);
	List<Map<String,Object>> queryOpHerbPresList(Map<String,Object> paramMap);
	
	List<Map<String,Object>> queryPiHistoryHerbPres(Map<String,Object> paramMap);
}
