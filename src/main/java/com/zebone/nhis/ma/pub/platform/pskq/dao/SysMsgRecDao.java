package com.zebone.nhis.ma.pub.platform.pskq.dao;


import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.nhis.ma.pub.platform.pskq.model.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysMsgRecDao {

    /**
     * 查询第三方接收发送的日志记录
     * @param param
     * @return
     */
    List<SysMsgRec> selectMessage(Map<String,Object> param);

    /**
     * 查询诊断信息
     * @param pkpv
     * @return
     */
    OrdDiagInfo selectDiagInfo(String pkpv);

    /**
     * 查询门急诊结算主记录
     * @param param
     * @return
     */
    List<SettlementMasterOutpat> selectSettleMasterInfo(Map<String,Object> param);

    /**
     * 查询门急诊结算发票明细
     * @param param
     * @return
     */
    List<SettlementDetailOutpat> selectSettleDetailInfo(Map<String,Object> param);


    /**
     * 查询门急诊费用明细
     * @param param
     * @return
     */
    List<CostDetailOutpat> selectCostDetailInfo(Map<String,Object> param);


    /**
     * 查询新增住院预交金
     * @param param
     * @return
     */
    AdvancePayment selectIpAdvancePayment(Map<String,Object> param);


}