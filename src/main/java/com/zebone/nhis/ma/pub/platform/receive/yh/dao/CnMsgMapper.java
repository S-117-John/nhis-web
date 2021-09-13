package com.zebone.nhis.ma.pub.platform.receive.yh.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnMsgMapper {
	
	//根据收费项目主键查询具体信息
	public BdItem getBdItemsByCon(@Param(value = "pkItem") String pkItem);
}
