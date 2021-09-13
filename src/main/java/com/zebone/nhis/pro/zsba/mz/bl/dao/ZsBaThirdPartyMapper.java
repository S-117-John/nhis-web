package com.zebone.nhis.pro.zsba.mz.bl.dao;

import com.zebone.nhis.pro.zsba.mz.bl.vo.PaymentCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsBaThirdPartyMapper {
    //查询his第三方支付信息
    public List<PaymentCheckVo> getHisTripartite(Map<String,Object> paramMap);
}
