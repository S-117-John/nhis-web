package com.zebone.nhis.base.pub.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.base.pub.vo.CustomDeptVo;
import com.zebone.nhis.base.pub.vo.CustomPrintVo;
import com.zebone.nhis.common.module.base.bd.mk.BdTempOrdex;
import com.zebone.platform.modules.mybatis.Mapper;
@Mapper
public interface CustomPrintMapper {
	public List<BdTempOrdex> QueryCustomPrintList(String pkorg);
	public List<CustomPrintVo> QueryDept(String pktempordex);
	public List<CustomDeptVo> QueryCustomDept(@Param("organizationid")String organizationid,@Param("pktempordex") String pktempordex);
	public void DeleteCustomDept(@Param("pktempordex") String pktempordex);
	public List<BdTempOrdex> QueryCustomPrintByPy(Map<String,Object> map);
	public List<BdTempOrdex> QueryCustomPrintListG(Map<String,Object> map);
}
