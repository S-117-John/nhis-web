package com.zebone.nhis.webservice.lbzy.dao;

import com.zebone.nhis.bl.pub.vo.ItemPriceVo;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.pi.acc.PiAccDetail;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.pv.reg.vo.PiMasterRegVo;
import com.zebone.nhis.webservice.lbzy.model.*;
import com.zebone.nhis.webservice.lbzy.model.ipin.*;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryPayRecordDetail;
import com.zebone.nhis.webservice.lbzy.model.paydt.QueryToPayPres;
import com.zebone.nhis.webservice.lbzy.model.reg.ApptTimeItem;
import com.zebone.nhis.webservice.lbzy.model.reg.RegApptVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SelfServiceMapper {

    List<Map<String,Object>> getRegisterDeptList(Map<String,Object> paramMap);

    List<DoctorItem> getDoctorList(Map<String,Object> paramMap);

    /**
     * 预约挂号查询
     * @param paramMap
     * @return
     */
    List<PreRegRecordItem> queryPreRegRecord(Map<String,Object> paramMap);

    /**
     * 待缴费列表
     * @param paramMap
     * @return
     */
    List<QueryToPayDetailItem> qryPayPresDetail(Map<String,Object> paramMap);

    /**
     * 查询缴费记录
     * @return
     */
    List<QueryPayRecordItem> findBlDeposit(Map<String,Object> paramMap);

    /**
     * 查询药品
     * @param paramMap
     * @return
     */
    List<QueryDrugItem> findDrug(Map<String,Object> paramMap);

    List<QueryDrugItem> findItem(Map<String,Object> paramMap);

    List<PiAccDetail> findPiAccDetail(Map<String,Object> paramMap);

    /**
     * 查询挂号记录
     * @return
     */
    List<QueryRegRecordItem> findRegRecord(Map<String,Object> paramMap);

    /**
     * 查询待支付处方列表
     * @param codePi
     * @return
     */
    List<QueryToPayItem> qryPayPresRegInfo(@Param("codePi") String codePi);

    /**
     * 查询待支付处方列表
     * @param codePi
     * @return
     */
    List<QueryToPayPres> qryPayPresInfo(@Param("codePi") String codePi);
    /***
     * 查询待支付总金额
     * @param codePi
     * @param codePvs
     * @return
     */
    List<BlOpDtFeeVo> qryPayBlOpDt(@Param("codePi") String codePi, @Param("codePvs") List<String> codePvs);

    /**
     * 6.8.查询缴费记录详细
     * @return
     */
    List<QueryPayRecordDetail> qryPayRecordDetail(Map<String,Object> paramMap);

    /**
     * 查询患者在院信息
     * @param paramMap
     * @return
     */
    List<QueryInHospital> queryInHospital(Map<String,Object> paramMap);

    /**
     * 查询可预约时段
     * @param paramMap
     * @return
     */
    List<ApptTimeItem> qryAptTime(Map<String,Object> paramMap);

    RegApptVo qryApptInfo(@Param("pkAppt") String pkAppt, @Param("pkPi") String pkPi);

    /**
     * 构造预约确认患者信息
     * @param paramMap
     * @return
     */
    PiMasterRegVo qryConfirmReg(Map<String,Object> paramMap);

    /**
     * 构造预约确认患者--诊查费
     * @param paramMap
     * @return
     */
    List<ItemPriceVo> qryCofirmRegFee(Map<String,Object> paramMap);

    PiMasterRegVo qryRegSch(Map<String,Object> paramMap);

    /**
     * 查询就诊信息
     * @param pkPv
     * @return
     */
    RegApptVo qryRegInfo(@Param("pkPv") String pkPv);

    /**
     * 查询住院预交金记录
     * @return
     */
    List<QueryInPay> qryPrePay(Map<String,Object> paramMap);

    /**
     * 查询住院每天消费金额
     * @param paramMap
     * @return
     */
    List<QueryInHospitalDay> queryFeeInDay(Map<String,Object> paramMap);

    /**
     * 住院费用信息详情
     * @param paramMap
     * @return
     */
    List<QueryInHospitalDetail> queryInHospitalDetail(Map<String,Object> paramMap);
}
