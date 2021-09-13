package com.zebone.nhis.ma.pub.platform.send.impl.pskq.dao;

import com.zebone.nhis.ma.pub.platform.pskq.model.CostDetailOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementDetailOutpat;
import com.zebone.nhis.ma.pub.platform.pskq.model.SettlementMasterOutpat;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.StExInfoVo;
import com.zebone.nhis.ma.pub.platform.send.impl.pskq.model.StExItemInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 费用信息
 */
@Mapper
public interface PskqPlatFormSendBlMapper {
    //根据费用明细主键查询费用明细信息  Map<String,Object> pkCgops
   List<CostDetailOutpat>  queBlOpDtRefund(List<String> list);
   //根据费用明细查询门诊退费结算信息
   List<SettlementMasterOutpat> queRefundSelectSettleMasterInfo(List<String> list);
    //根据费用明细查询发票信息
   List<SettlementDetailOutpat> queRefundSelectSettleDetailInfo(List<String> list);

   /**根据结算主键查询本次结算下的检查医嘱信息*/
   List<Map<String,Object>> qryRisOrdInfoByPkSettle(Map<String,Object> paramMap);

   /**根据医嘱主键查询收费项目信息*/
   List<StExItemInfoVo> qryOpdtByPkCnord(@Param("pkCnord") String pkCnord);
}
