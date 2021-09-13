package com.zebone.nhis.labor.nis.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvLaborExListMapper {
	/**
	 * 根据不同条件查询执行单列表（主要用于查询执行确认与取消执行的执行单列表查询）
	 * @param map{dateBegin,dateEnd,confirmFlag,ordtype,euStatus,cancelFlag,nameOrd,pkPvs,pkDeptNs}
	 * @return
	 */
	public List<ExlistPubVo> queryExecListByCon(Map<String,Object> map);
	
	/**
	 * 更新关联皮试医嘱结果
	 * @param ordList,euSt,result
	 */
	public void updateStOrd(Map<String,Object> map);
	
}
