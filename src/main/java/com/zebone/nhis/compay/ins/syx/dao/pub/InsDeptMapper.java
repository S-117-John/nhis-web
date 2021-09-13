package com.zebone.nhis.compay.ins.syx.dao.pub;

import java.util.List;

import com.zebone.nhis.compay.ins.syx.vo.gzyb.InsDeptMapDataVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsDeptMapper {

	public List<InsDeptMapDataVo> getList();
}
