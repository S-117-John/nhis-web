package com.zebone.nhis.base.bd.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.base.bd.vo.BdHpStdivVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface HpStdivMapper {

	public List<BdHpStdivVo> qryBdHpStdiv(@Param(value = "nameDiv") String nameDiv, @Param(value = "spcodeDiv")String spcodeDiv);
}
