package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.nhis.cn.ipdw.vo.*;
import com.zebone.nhis.common.module.emr.rec.rec.*;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 病案首页
 */
@Mapper
public interface EmrHomePageMapper {

    List<EmrHomePageVO> getEmrHomePageByPkPv(String pkPv);

    /**
     * 获取就诊信息
     * @param pkPv
     * @return
     */
    List<EmrVisitInformationVO> getVisitInformationById(String pkPv);

    /**
     * 获取转科信息
     * @param pkPv
     * @return
     */
    List<EmrTransferInformationVO> getTransferInformationById(String pkPv);

    /**
     * 获取诊断信息
     * @param pkPv
     * @return
     */
    List<EmrDiagnosticInformationVO> getDiagnosticInformationById(String pkPv);

    /**
     * 获取手术记录
     * @param pkPv
     * @return
     */
    List<EmrSurgeryRecordVO> getSurgeryRecordById(String pkPv);

    /**
     * 获取费用信息
     * @param pkPv
     * @return
     */
    List<EmrCostInformationVO> getCostInformationById(String pkPv);


    /**
     * 获取婴儿记录
     * @param pkPv
     * @return
     */
    List<EmrBabyRecordVO> getBabyRecordById(String pkPv);

    /**
     * 获取重症监护信息
     * @param pkPage
     * @return
     */
    List<EmrHomePageTransVO> getIntensiveCareById(String pkPage);

    /**
     * 获取cchi信息
     * @param pkPv
     * @return
     */
    List<EmrCchiVO> getCchiById(String pkPv);

    /**
     * 提交
     * @param pkPv
     * @return
     */
    int submit(Map<String, Object> paramMap);

    /**
     * 撤销
     * @param pkPv
     * @return
     */
    int revoke(String pkPv);

    /**
     * 物理删除转科信息
     * @param pkPage
     * @return
     */
    int deleteHomePageTrans(String pkPage);

    /**
     * 物理删除诊断表
     * @param pkPage
     * @return
     */
    int deleteHomePageDiags(String pkPage);

    /**
     * 物理删除手术信息
     * @param pkPage
     * @return
     */
    int deleteHomePageOps(String pkPage);

    int deleteHomePageCharges(String pkPage);

    List<EmrHomePageTransVO> findTransByPkPage(String pkPage);

    int updateEmrHomePage(@Param("emrHomePage") EmrHomePage emrHomePage);

    int updateEmrHomePageTrans(@Param("emrHomePageTrans") EmrHomePageTrans emrHomePageTrans);

    int updateEmrHomePageDiags(@Param("emrHomePageDiags") EmrHomePageDiags emrHomePageDiags);

    int updateEmrHomePageOps(@Param("emrHomePageOps") EmrHomePageOps emrHomePageOps);

    int updateEmrHomePageCharges(@Param("emrHomePageCharges") EmrHomePageCharges emrHomePageCharges);

    int updateEmrHomePageBr(@Param("emrHomePageBr") EmrHomePageBr emrHomePageBr);

    int updateEmrHomePageOr(@Param("emrHomePageOr") EmrHomePageOr emrHomePageOr);

    int updateEmrHomePageOrDt(@Param("emrHomePageOrDt") EmrHomePageOrDt emrHomePageOrDt);


    List<EmrHomePageOps> findOps(String pkPage);

    List<EmrHomePageCharges> findCharges(String pkPage);

    List<EmrHomePageBr> findBrs(String pkPage);

    List<EmrHomePageOr> findOr(String pkPage);

    List<EmrHomePageOrDt> findOrDt(String pkPageOr);

    List<EmrHomePageTrans> findTrans(String pkPage);

    List<EmrHomePageDiags> findDiags(String pkPage);

    int insert(EmrHomePage record);

    int insertSelective(EmrHomePage record);

    List<EmrPageNurseLevelVo> getNurseDays(String pkPv);

    Double getQuanHours(String pkPv);

    /**
     * 物理删除出生记录
     * @param pkPage
     * @return
     */
    int deleteHomePageBr(String pkPage);

    /**
     * 物理删除EMR_HOME_PAGE_OR_DT
     * @param pkOr
     * @return
     */
    int deleteHomePageOrDt(String pkOr);

    /**
     * 物理删除EMR_HOME_PAGE_OR
     * @param pkPage
     * @return
     */
    int deleteHomePageOr(String pkPage);

    /**
     * 物理删除EMR_HOME_PAGE_OPS
     * @param pkOps
     * @return
     */
    int deleteHomePageOpsByPk(String pkOps);

    /**
     * 物理删除EMR_HOME_PAGE_Br
     * @param pkBr
     * @return
     */
    int deleteHomePageBrByPk(String pkBr);

    /**
     * 物理删除EMR_HOME_PAGE_OR_DT
     * @param pkOrDt
     * @return
     */
    int deleteHomePageOrDtByPk(String pkOrDt);

    List<Map<String,Object>> getQcEhpList(String pkPv);

    List<Map<String,Object>> getQcEhpDetail(String pkPv);

}
