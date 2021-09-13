package com.zebone.nhis.arch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.zebone.nhis.arch.vo.PvRecordVo;
import com.zebone.nhis.common.module.base.bd.code.BdDefdoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ArchiveCollectMapper {
	
	List<PvRecordVo> qryPatisByCon(PvRecordVo param);
	
	@Select("select systype.code,systype.name from bd_defdoc systype where systype.code_defdoclist='051001'")
	List<BdDefdoc> qrySystem();

}
