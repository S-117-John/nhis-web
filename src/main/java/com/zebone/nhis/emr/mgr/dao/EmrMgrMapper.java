package com.zebone.nhis.emr.mgr.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.mgr.EmrBorrowDoc;
import com.zebone.nhis.common.module.emr.mgr.EmrBorrowRec;
import com.zebone.nhis.common.module.emr.mgr.EmrOpenEditDoc;
import com.zebone.nhis.common.module.emr.qc.EmrAmendRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.dict.ViewEmrPatList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiagsIcd;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOpsIcd;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.emr.mgr.vo.EmrOpenEditRecParam;
import com.zebone.nhis.emr.rec.dict.vo.EmrPatListPrarm;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * @author 
 */
@Mapper
public interface EmrMgrMapper{   
    /**
     * 根据主键查询
     */
    public EmrBorrowRec getEmrBorrowRecById(@Param("pkBorrow")String pkBorrow); 

    /**
     * 查询出所有记录
     */
    public List<EmrBorrowRec> findAllEmrBorrowRec();    
    
    /**
     * 保存
     */
    public int saveEmrBorrowRec(EmrBorrowRec emrBorrowRec);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrBorrowRec(EmrBorrowRec emrBorrowRec);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrBorrowRec(@Param("pkBorrow")String pkBorrow);
    
    
    /**
     * 根据主键查询
     */
    public EmrBorrowDoc getEmrBorrowDocById(@Param("pkBorrowDoc")String pkBorrowDoc); 

    /**
     * 查询出所有记录
     */
    public List<EmrBorrowDoc> findAllEmrBorrowDoc();    
    
    /**
     * 保存
     */
    public int saveEmrBorrowDoc(EmrBorrowDoc emrBorrowDoc);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrBorrowDoc(EmrBorrowDoc emrBorrowDoc);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrBorrowDoc(@Param("pkBorrowDoc")String pkBorrowDoc);
    
    /**
     * 查找病历借阅记录
     * @param map
     * @return
     */
    public List<EmrBorrowRec> queryBorrowRecList(Map map);
    
    
    public List<EmrBorrowDoc> queryBorrowDocList(Map map);
    
    /**
     * 根据参数查询病历签收患者信息
     * @param map
     * @return
     */
    public List<EmrPatListPrarm> querySignForInfo(Map map);
    /**
     * 根据参数查询病历召回申请的患者信息
     * @param map
     * @return
     */
    public List<EmrOpenEditRecParam> queryApplyInfo(Map map);
    
    public EmrMedRec queryOpenRecList(@Param("pkRec")String pkRec); 
    
    /**
     * 根据查询条件查询患者病案列表
     * @param map
     * @return
     */
    public List<EmrPatListPrarm> queryIcdByPrame(Map map);
    /**
     * 根据pkpv查询病案首页-诊断
     * @param pkPv
     * @return
     */
    public List<EmrHomePageDiagsIcd> queryPageDiagsByPk(@Param("pkPv")String pkPv);
    /**
     * 根据pkpv查询病案首页-手术
     * @param pkPv
     * @return
     */
    public List<EmrHomePageOpsIcd> queryPageOpsByPk(@Param("pkPv")String pkPv);
    /**
     * 查询开放的文档分类
     * @param pkPv
     * @return
     */
    public List<String> queryOpenTypeCode(@Param("pkPv")String pkPv);
    /**
     * 查询开放的文档
     * @param pkPv
     * @return
     */
    public List<EmrOpenEditDoc> queryOpenEditDoc(@Param("pkPv")String pkPv);
    /**
     * 查询病案编码中首页的诊断
     * @param pkPv
     * @return
     */
    public EmrHomePage queryHomePage(@Param("pkPv")String pkPv);
    
    /**
     * 查询患者病历记录列表
     * @param map
     * @return
     */
    public List<EmrPatList> queryPatExtList(Map map);
    
    /**
     * 导出检查查询医生列表
     * @param map
     * @return
     */
    public List<EmrDoctor> queryDoctor(Map map);
    /**
     * 打印医嘱时查询医嘱
     * @param map
     * @return
     */
    List<Map<String, Object>> printQryCnOrderBoai(Map<String, Object> map);
    /**
      * 查询整改反馈列表
     * @param map
     * @return
     */
    List<EmrAmendRec> queryEmrAmendRecList(Map<String, Object> map);
}

