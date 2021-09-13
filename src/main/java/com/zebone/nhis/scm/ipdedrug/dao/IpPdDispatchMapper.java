package com.zebone.nhis.scm.ipdedrug.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.ipdedrug.vo.DispatchVo;
import com.zebone.nhis.scm.ipdedrug.vo.EmpVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface IpPdDispatchMapper {
	public List<DispatchVo> QueryDispatchList(Map<String,Object> map);
	public void UpdateState(Map<String,Object> map);
}
