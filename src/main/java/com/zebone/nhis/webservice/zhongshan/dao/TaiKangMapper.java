package com.zebone.nhis.webservice.zhongshan.dao;

import com.zebone.nhis.webservice.zhongshan.vo.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wq
 * @Classname TaiKangMapper
 * @Description 泰康接口数据库交互接口
 * @Date 2020-11-24 14:22
 */
@Mapper
public interface TaiKangMapper {
    TaiKangPatientAdmInf getPatientAdmInf(TaiKangRequestAdmOutVo requestAdmOutVo);

    List<TaiKangPatientCostInf>  getPatientCostInfList(TaiKangRequestCostVo requestCostVo);

    List<TaiKangMedicalDirectory>  getTaiKangMedicalDirectoryList(TaiKangRequestMedVo taiKangRequestMedVo);

    List<TaiKangPatientOutInf> getPatientOutInf(TaiKangRequestAdmOutVo requestAdmOutVo);

    List<TaiKangInvoiceCareStr>  getInvoiceCareStr(String pkSettle);
    /*
    * 查询患者住院信息以及结算信息
    * */
    List<Map<String, Object>>  getPatientSettleInfor(TaiKangRequestSettleWriteVo requestSettleWriteVo);
    /*
     * 查询理赔记录
     * */
    List<Map<String, Object>>  getPatientSettleCnacInfor(TaiKangRequestSettleWriteVo requestSettleWriteVo);
}
