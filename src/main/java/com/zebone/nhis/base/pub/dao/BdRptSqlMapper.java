package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.pub.vo.RptPrintDataDto;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdRptSqlMapper {

	List<Map<String, Object>> queryDeDrugDetailRpt(RptPrintDataDto rptPrintDataDto) throws BusException;

	List<Map<String, Object>> queryDeDrugSumRpt(RptPrintDataDto rptPrintDataDto) throws BusException;
}
