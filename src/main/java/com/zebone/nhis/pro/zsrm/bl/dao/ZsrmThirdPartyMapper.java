package com.zebone.nhis.pro.zsrm.bl.dao;

import com.zebone.nhis.common.module.pay.BlExtPay;
import com.zebone.nhis.pro.zsrm.bl.vo.PaymentCheckVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ZsrmThirdPartyMapper {
    //查询his第三方支付信息
    public List<PaymentCheckVo> getHisTripartite(Map<String,Object> paramMap);

    //一代社保交易总笔数金额查询
    public Map<String, Object> queryOneInsu(Map<String,Object> paramMap);
    //三代代社保交易总笔数金额查询
    public Map<String, Object> queryThreInsu(Map<String,Object> paramMap);
    //交易总笔数金额查询
    public Map<String, Object> querytotalAmt(Map<String,Object> paramMap);
    //查询支付信息
    public BlExtPay queryThirdOrder(Map<String,Object> paramMap);

    //使用订单号查询支付信息
    public BlExtPay queryThirdOrders(Map<String,Object> paramMap);
}
