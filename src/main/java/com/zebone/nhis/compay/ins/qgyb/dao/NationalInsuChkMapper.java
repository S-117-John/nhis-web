package com.zebone.nhis.compay.ins.qgyb.dao;

import com.zebone.nhis.compay.ins.qgyb.vo.CheckAccDetailVo;
import com.zebone.nhis.compay.pub.vo.InsChkAccSumVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NationalInsuChkMapper {

    /**
     * 查询全国医保结算总账
     * @param pramMap
     * @return
     */
    List<InsChkAccSumVo> qryAccSum(Map<String,Object> pramMap);

    /**
     * 查询全国医保结算费用明细
     * @param paramMap
     * @return
     */
    List<CheckAccDetailVo> qryAccDetail(Map<String,Object> paramMap);

    /**
     * 查询全国医保住院系统门诊订单明细
     * @param paramMap
     * @return
     */
    List<CheckAccDetailVo> getExtSysDetailInfoZY(Map<String,Object> paramMap);

    /**
     * 查询全国医保旧系统门诊订单明细
     * @param paramMap
     * @return
     */
    List<CheckAccDetailVo> getExtSysDetailInfoMZ(Map<String,Object> paramMap);

}
