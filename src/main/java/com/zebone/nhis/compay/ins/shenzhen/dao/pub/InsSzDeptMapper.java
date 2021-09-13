package com.zebone.nhis.compay.ins.shenzhen.dao.pub;

import java.util.List;

import com.zebone.nhis.compay.ins.shenzhen.vo.szxnh.InsDeptMapDataVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsSzDeptMapper {

	public List<InsDeptMapDataVo> getList();
}
