package com.zebone.nhis.cn.ipdw.dao;

import java.util.List;
import java.util.Map;




import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnChiefResidentMapper {
	public List<Map<String, Object>> queryChiefResidentList(Map<String, Object> paramMap);

}
