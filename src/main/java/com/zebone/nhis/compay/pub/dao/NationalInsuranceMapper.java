package com.zebone.nhis.compay.pub.dao;

import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybSt;
import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybVisit;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.compay.pub.vo.InsQgybSetlinfo;
import com.zebone.platform.modules.mybatis.Mapper;


import java.util.List;
import java.util.Map;

@Mapper
public interface NationalInsuranceMapper {

    /**更新医保计划*/
    void updatePv(Map<String, Object> praram);

    /** 查询医保挂号使用参数*/
    Map<String,Object> qryInsRegPre(String pkPv);

    /**查询患者就诊信息*/
    List<InsQgybVisit> qryVisitInfo(Map<String,Object> paramMap);

    /**查询医保登记详细信息*/
    List<Map<String,Object>> qryVisitDtls(Map<String,Object> paramMap);

    /** 查询患者主诊断信息 */
    List<Map<String,Object>> qryPvDiag(Map<String,Object> paramMap);

    /** 查询门诊挂号上传诊查费 */
    List<Map<String,Object>> qryRegChargeDetail(Map<String, Object> praram);

    /** 查询门诊待上传费用明细 */
    List<Map<String,Object>> qryChargeDetailNoUpload(Map<String, Object> praram);

    /** 查询住院待上传费用明细 */
    List<Map<String,Object>> qryIpChargeDetailNoUpload(Map<String, Object> praram);

    /**查询医保结算信息*/
    InsQgybSt qryYbStInfo(Map<String, Object> praram);

    /**查询排版人员科室信息*/
    Map<String,Object> qrySrvResInfo(Map<String, Object> praram);

    void updateOpdtFlagInsuByPk(Map<String,Object> paramMap);

    void updateIpdtFlagInsuByPk(Map<String,Object> paramMap);

    /**查询住院费用总额*/
    Double qryIpChargeTotalAmt(Map<String,Object> paramMap);

    /**查询未结算的医保就诊记录*/
    List<InsQgybPV> qryInsuPvNoStInfo(Map<String,Object> paramMap);

    /**查询全国医保结算信息*/
    InsQgybSt qryInsuStInfo(Map<String,Object> paramMap);

    /**
     *查询结算清单信息
     */
    List<InsQgybSetlinfo> qryInsuranceSettleDetail(Map<String,Object> paramMap);

    /**
     * 查询基金支付信息,住院和门诊慢特病
     */
    List<Map<String, Object>> qryInsQgybPayInfo(Map<String,Object> paramMap);

    /**
     * 查询诊断信息,门诊慢特病和住院
     */
    List<Map<String, String>> qryInsQgybDiagInfo(Map<String,Object> paramMap);

    /**
     * 查询收费项目信息
     */
    List<Map<String, String>> qryInsQgybItemInfo(Map<String,Object> paramMap);

    /**
     * 查询手术操作信息,门诊慢特病和住院
     */
    List<Map<String, Object>> qryInsQgybOprnInfo(Map<String,Object> paramMap);
}
