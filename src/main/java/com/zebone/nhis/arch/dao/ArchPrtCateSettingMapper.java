package com.zebone.nhis.arch.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.arch.vo.ArchDoctypeVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ArchPrtCateSettingMapper {
	
	/**
	 * 查询已选打印文档类型 
	 * @param pk
	 * @return
	 */
	List<ArchDoctypeVo> qryArchDocType_chosen(@Param("pkPrtcate") String pk);
	
	/**
	 * 查询备选打印文档类型 
	 * @param pk
	 * @return
	 */
	List<ArchDoctypeVo> qryArchDocType_alter(@Param("pkOrg") String pk);

}
