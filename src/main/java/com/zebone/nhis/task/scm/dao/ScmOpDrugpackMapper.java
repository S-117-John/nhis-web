package com.zebone.nhis.task.scm.dao;

import java.util.List;




import java.util.Map;

import com.zebone.nhis.task.scm.vo.OpDrugDispensePresInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmOpDrugpackMapper {
	
	public List<OpDrugDispensePresInfo> queryPresInfoUpload( Map<String,Object> paramMap);
}
