package com.zebone.nhis.ma.pub.lb.dao;

import com.zebone.nhis.ma.pub.lb.vo.AdviceSaveCancelVo;
import com.zebone.nhis.ma.pub.lb.vo.MedicalAdviceVo;
import com.zebone.nhis.ma.pub.lb.vo.OpPrescriptionVo;
import com.zebone.nhis.ma.pub.lb.vo.PatientInfoVo;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MedicalInsuranceCostsMapper {
    /**
     * 住院上传审核接口
     * @return
     */
    List<PatientInfoVo> adviceAudit(Map<String, Object> param);

    List<PatientInfoVo> opPrescription(Map<String, Object> param);
     /**
     * 上传审核接口InputData数据
     * @return
     */
    List<MedicalAdviceVo> adviceInputData(Map<String, Object> param);

    List<MedicalAdviceVo> opInsuReviewInputData(Map<String, Object> param);
//    门诊结果上报接口
    List<OpPrescriptionVo> opInputData(Map<String, Object> param);

    /**
     * 保存取消数据
     * @return
     */
    List<AdviceSaveCancelVo> adviceSaveCancel(Map<String, Object> param);
    /**
     *
     * 住院费用审核类接口
     */
    List<MedicalAdviceVo> getPatientInfoVo(Map<String, Object> param);


    /**
     * 撤销住院费用明细接口
     */
    List<PatientInfoVo> getInpCancelFee(Map<String, Object> param);

    MedicalAdviceVo adviceAuditInputData(Map<String, Object> param);

    List<Map<String,Object>> opInterfaceInfo(Map<String, Object> param);
}
