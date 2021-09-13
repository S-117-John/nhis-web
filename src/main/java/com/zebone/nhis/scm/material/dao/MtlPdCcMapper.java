package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.scm.st.PdCc;
import com.zebone.nhis.common.module.scm.st.PdCcDetail;
import com.zebone.nhis.scm.material.vo.MtlPdBaseParam;
import com.zebone.platform.modules.mybatis.Mapper;

/***
 * 库存结账mapper
 *  
 * @author wj
 *
 */
@Mapper
public interface MtlPdCcMapper {
	
	//根据库存结账主键查询
	public List<Map<String,Object>> getPdCcList(Map<String,Object> map);
	
	//根据库存结账主键查询
	public PdCc getPdCcById(String pkPdcc);
	
	//根据库存结账主键查询库存结账明细列表
	public List<PdCcDetail> getPdCcDetailListById(String pkPdcc);
	
	//获取统计时间段内物品基本信息
	public List<MtlPdBaseParam> getPdBaseParamList(Map<String, Object> mapParam);
	
}
