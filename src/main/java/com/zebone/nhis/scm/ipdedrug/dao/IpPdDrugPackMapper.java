package com.zebone.nhis.scm.ipdedrug.dao;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ma.pub.zsba.vo.ExPrtAndPackVo;
import com.zebone.nhis.ma.pub.zsba.vo.OrderExVo;
import com.zebone.nhis.ma.pub.zsba.vo.PackPdVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IpPdDrugPackMapper {

	
	public List<ExPdApplyDetail> qryRetDrugPrice(List<String> pkPdapdts);

	public List<ExPdApplyDetail> qryDeptRetDrugPrice(List<String> pkPdapdts);
}
