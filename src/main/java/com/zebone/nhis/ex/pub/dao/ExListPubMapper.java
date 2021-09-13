package com.zebone.nhis.ex.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ExListPubMapper {
	/**
	 * 根据医嘱主键或父医嘱号，获取需要生成执行单的医嘱(含药品与非药品)
	 * @param map{}
	 * @return
	 */
	public List<GenerateExLisOrdVo> getGenExecPdOrdListByP(Map<String,Object> map);
	/**
	 * 根据指定医嘱和时间，组装生成执行单的医嘱信息（处理首次及末次使用）
	 * @param map{pkCnord}
	 * @return
	 */
	public GenerateExLisOrdVo getGenExecPdOrdListByTime(Map<String,Object> map);
	
}
