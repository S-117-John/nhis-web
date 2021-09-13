package com.zebone.nhis.webservice.syx.dao;

import java.util.List;

import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDetailReq;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoDtVo;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoReq;
import com.zebone.nhis.webservice.syx.vo.selfmac.SettleInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SelfmacMapper {

	public List<SettleInfoVo> qrySettleInfo(SettleInfoReq req);
	
	public List<SettleInfoDtVo> qrySettleInfoDetail(SettleInfoDetailReq req);
}
