package com.zebone.nhis.ex.pivas.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdPivas;
import com.zebone.nhis.ex.pivas.vo.CnOrderCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PivasHandMapper {

	/** 根据请领单明细主键数组查询物品请领(退)明细列表 */
	List<ExPdApplyDetail> getExPdApplyDetailByPkPdapdts(Map<String, Object> mapParam);
	
	/** 根据静配记录主键数组查询检验信息 */
	List<CnOrderCheckVo> getCnOrderCheckVoList(String[] pkPdpivass);
	
	/** 根据静配记录主键数组查询静配管理信息列表 */
	List<ExPdPivas> getExPdPivasListByPkPdpivas(String[] pkPdpivass);
	
	/** 获取静配待计费信息 */
	List<BlPubParamVo> getBlPubParamVoList(Map<String, Object> mapParam);
} 
