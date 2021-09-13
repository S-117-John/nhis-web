package com.zebone.nhis.ma.kangMei.dao;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.ma.kangMei.vo.DataReq;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SendPrescriptionMapper {


	public List<Map<String, Object>> qrySendDispMap(@Param("presNo") String presNo);

	public List<DataReq> cancelOrder(@Param("presNo") String presNo, @Param("pkPv") String pkPv);

	public List<Map<String, Object>> qryOpPresInfo(Map<String, Object> paramMap);

	public List<Map<String, Object>> qrySendDispMapSql(@Param("presNo") String presNo);

	List<Map<String, Object>> qryPresListData(Map<String, Object> paramMap);

	/**
	 * @return java.util.List<com.zebone.nhis.common.module.bl.BlIpDt>
	 * @Description 根据处方查询草药代煎已记费项目
	 * @auther wuqiang
	 * @Date 2020-11-06
	 * @Param [msgParam]
	 */

	List<BlIpDt> qryBlipDtByPkPres(String pkPres, String pkPv, String pkItem);
}
