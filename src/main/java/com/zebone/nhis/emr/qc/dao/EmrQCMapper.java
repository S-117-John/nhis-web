package com.zebone.nhis.emr.qc.dao;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.emr.qc.EmrAmendDoc;
import com.zebone.nhis.common.module.emr.qc.EmrAmendRec;
import com.zebone.nhis.common.module.emr.qc.EmrAuditDoc;
import com.zebone.nhis.common.module.emr.qc.EmrAuditRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeDept;
import com.zebone.nhis.common.module.emr.qc.EmrGradeItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgItem;
import com.zebone.nhis.common.module.emr.qc.EmrGradeMsgRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRec;
import com.zebone.nhis.common.module.emr.qc.EmrGradeRule;
import com.zebone.nhis.common.module.emr.qc.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.qc.EmrGradeType;
import com.zebone.nhis.common.module.emr.qc.EmrMedRecTask;
import com.zebone.nhis.common.module.emr.qc.ViewEmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.emr.qc.vo.EmrDefectVo;
import com.zebone.nhis.emr.qc.vo.EmrGradeMsgItemVo;
import com.zebone.nhis.emr.qc.vo.EmrPatRecListVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface EmrQCMapper{   

    /**
     * 查询病历评分标准分类
     * @param 
     * @return
     */
    public List<EmrGradeType> queryGradeTypeList(); 
    
    
    /**
     * 根据分类查询评分标准
     * @param typeCodeList
     * @return
     */
    public List<EmrGradeStandard> queryGradeStdByTypes(@Param("typeCodeList")List<String> typeCodeList,@Param("pkDept")String pkDept,@Param("cateCode")String cateCode);
    /**
	 * 根据分类查询评分标准(质控中心标准or专家教授标准)
	 * @param typeCodeList
	 * @return
	 */
    public List<EmrGradeStandard> qryGradeStdByCenter(@Param("typeCodeList")List<String> typeCodeList,@Param("cateCode")String cateCode);
    /**
     * 根据条件查询评分标准
     * stdTypes/typeCodes/scoreXml/orderBy
     * @param map
     * @return
     */
    public List<EmrGradeStandard> queryGradeStdByConds(Map map);
    
    /**
     * 查询病历评分记录
     * @param pkPv/euGradeType//@Param("pkPv")String pkPv,@Param("euGradeType")String euGradeType
     * @return
     */
    public List<EmrGradeRec> queryGradeRecByConds(Map map);

    /**
     * 查询病历评分记录明细
     * @param pkGraderec//@Param("pkGraderec")String pkGraderec
     * @return
     */
    public List<EmrGradeItem> queryGradeItemByConds(Map map);

    /**
     * 保存
     */
    public int saveEmrGradeType(EmrGradeType emrGradeType);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeType(EmrGradeType emrGradeType);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrGradeType(@Param("pkGradetype")String pkGradetype);
    
    
    /**
     * 保存评分记录
     */
    public int saveEmrGradeRec(EmrGradeRec emrGradeRec);
    
    /**
     * 根据主键更新评分记录（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeRec(EmrGradeRec emrGradeRec);
    
    /**
     * 根据主键删除评分记录
     */
    public int deleteEmrGradeRec(@Param("pkGraderec")String pkGraderec);
    
    /**
     * 保存评分记录明细
     */
    public int saveEmrGradeItem(EmrGradeItem emrGradeItem);
    
    /**
     * 根据主键更新评分记录明细（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeItem(EmrGradeItem emrGradeItem);
    
    /**
     * 根据主键删除评分记录明细
     */
    public int deleteEmrGradeItem(@Param("pkGradeitem")String pkGradeitem);
    
    
    /**
     * 更新病历事件记录
     * @param map
     */
    public void updteEventRec(Map map);
    
    /**
     * 根据条件查询病历评分规则
     * @param map 
     * @return
     */
    public List<EmrGradeRule> queryGradeRuleByConds(Map map);
    
    /**
     * 根据条件查询病历事件记录
     * @param map 
     * @return
     */
    public List<EmrEventRec> queryEventRecByConds(Map map);    
    
    /**
     * 根据条件查询病历事件记录DC
     * @param map 
     * @return
     */
    public List<EmrEventRec> queryEventRecByCondsDC(Map map); 
    
    
    /**
     * 根据文档分类查询患者病历记录
     * @param pkPv
     * @param typeCode
     * @return
     */
    public List<EmrMedRec> queryMedRecListByType(Map map); 
    
    
    /**
     * 根据文档分类查询患者病历记录DC
     * @param pkPv
     * @param typeCode
     * @return
     */
    public List<EmrMedRec> queryMedRecListByTypeDC(Map map); 
    
    /**
     * 根据条件查询病历书写任务
     * @param pkPv
     * @param ruleCode
     * @param kEvtrec
     * @return
     */
    public List<EmrMedRecTask> queryMedRecTaskByConds(Map map); 
    
    /**
     * 保存
     */
    public int saveEmrMedRecTask(EmrMedRecTask emrMedRecTask);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrMedRecTask(EmrMedRecTask emrMedRecTask);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrMedRecTask(@Param("pkTask")String pkTask);
    
    /**
     * 查询病历质控评分
     * @param pkPv
     * @return Map
     */
    public List<Map> queryPatGradeScore(Map map); 
    
    public List<Map> queryPatGradeScoreSql(Map map); 
    /**
     * 保存
     */
    public int saveEmrGradeMsgRec(EmrGradeMsgRec emrGradeMsgRec);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeMsgRec(EmrGradeMsgRec emrGradeMsgRec);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrGradeMsgRec(@Param("pkGradeMsgrec")String pkGradeMsgrec);
    
    /**
     * 保存
     */
    public int saveEmrGradeMsgItem(EmrGradeMsgItem emrGradeMsgItem);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeMsgItem(EmrGradeMsgItem emrGradeMsgItem);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrGradeMsgItem(@Param("pkGradeMsgitem")String pkGradeMsgitem);
    
    /**
     * 查询病历评分消息记录
     * @param pkPv/euMsgType
     * @return
     */
    public List<EmrGradeMsgRec> queryGradeMsgRecByConds(Map map);

    /**
     * 查询病历评分消息明细
     * @param pkGradeMsgrec
     * @return
     */
    public List<EmrGradeMsgItem> queryGradeMsgItemByConds(Map map);
    
    /**
     * 查询病历评分结果
     * @param map
     * @return
     */
    public List<EmrGradeItem> queryGradeScoreByConds(Map map);
    
    /**
     * 查询医师病历书写任务
     * @param 
     * @return Map
     */
    public List<Map> queryEmpTaskList(Map map); 
    
    /**
     * 保存审签记录
     */
    public int saveEmrAuditRec(EmrAuditRec emrAuditRec);
    
    /**
     * 根据主键更新审签记录
     */
    public int updateEmrAuditRec(EmrAuditRec emrAuditRec);
    
    /**
     * 根据主键删除审签记录
     */
    public int deleteEmrAuditRec(@Param("pkAudit")String pkAudit);

    /**
     *保存审签文档
     */
    public int saveEmrAuditDoc(EmrAuditDoc emrAuditDoc);
    
    /**
     *保存审签文档(病历存储库)
     */
    public int saveEmrAuditDocEmr(EmrAuditDoc emrAuditDoc);
    
    /**
     * 根据主键更审签文档
     */
    public int updateEmrAuditDoc(EmrAuditDoc emrAuditDoc);
    
    /**
     * 根据主键更审签文档（病历存储库）
     */
    public int updateEmrAuditDocEmr(EmrAuditDoc emrAuditDoc);
    
    /**
     * 根据主键删除评分记录
     */
    public int deleteEmrAuditDoc(@Param("pkAuditDoc")String pkAuditDoc);

    /**
     * 根据主键删除评分记录（病历存储库）
     */
    public int deleteEmrAuditDocEmr(@Param("pkAuditDoc")String pkAuditDoc,@Param("dbName")String dbName);
    
    /**
     * 保存整改记录
     */
    public int saveEmrAmendRec(EmrAmendRec emrAmendRec);
    
    /**
     * 根据主键更新整改记录
     */
    public int updateEmrAmendRec(EmrAmendRec emrAmendRec);
    
    /**
     * 根据主键删除整改记录
     */
    public int deleteEmrAmendRec(@Param("pkAmend")String pkAmend);

    
    /**
     * 保存整改文档
     */
    public int saveEmrAmendDoc(EmrAmendDoc emrAmendDoc);
    
    /**
     * 保存整改文档（病历存储库）
     */
    public int saveEmrAmendDocEmr(EmrAmendDoc emrAmendDoc);
    
    /**
     * 根据主键更新整改文档
     */
    public int updateEmrAmendDoc(EmrAmendDoc emrAmendDoc);
    
    /**
     * 根据主键更新整改文档（病历存储库）
     */
    public int updateEmrAmendDocEmr(EmrAmendDoc emrAmendDoc);
    
    /**
     * 根据主键删除整改文档
     */
    public int deleteEmrAmendDoc(@Param("pkAmendDoc")String pkAmendDoc);

    /**
     * 根据主键删除整改文档（病历存储库）
     */
    public int deleteEmrAmendDocEmr(@Param("pkAmendDoc")String pkAmendDoc,@Param("dbName")String dbName);
    
    /**
     * 查询病历整改记录
     * @param map
     * @return
     */
    public List<EmrAmendRec> queryAmendRecList(Map map);    
    
    /**
     * 根据主键查询病历整改文档
     * @param pkAmend
     * @return
     */
    public EmrAmendRec getEmrAmendRec(@Param("pkAmend")String pkAmend);
    
    /**
     * 根据主键查询病历整改文档(病历存储库)
     * @param pkAmend
     * @return
     */
    public EmrAmendRec getEmrAmendRecEmr(@Param("pkAmend")String pkAmend,@Param("dbName")String dbName);
    
    /**
     * 查询病历审签记录
     * @param pkPv/pk_rec/euLevel
     * @return
     */
    public List<EmrAuditRec> queryEmrAuditRec(Map map);
    
    /**
     * 查询病历审签文档
     * @param pkAudit
     * @return
     */
    public EmrAuditDoc queryEmrAuditDoc(Map map); 

    /**
     * 查询病历评分消息记录
     * @param map
     * @return
     */
    public List<EmrGradeMsgItemVo> queryGradeMsgItem(Map map);
    /**
     * 查询规则已维护科室
     * @param cateCode
     * @return
     */
    public List<Map> findGradeHaveDept();
    
    /**
     * 查询系统分类下已维护科室
     * @param cateCode
     * @return
     */
    public List<EmrGradeDept> findHaveDept(String cateCode);
    /**
     * 查询除系统分类已维护科室的其他科室
     * @param viewEmrDeptList
     * @return
     */
    public List<ViewEmrDeptList> findElseDept(Map map);
    /**
     * 根据条件查询质控缺陷
     * @param map
     * @return
     */
    public List<EmrDefectVo> findDefectByParam(Map map);
    
    /**
     * 根据pkpv查询患者是否有手术
     * @param map
     * @return
     */
    public int findIsOpByPkPv(@Param("pkPv")String pkPv,@Param("isFourOp")String isFourOp);
    /**
     * 根据pkpv查询患者质控扣分项
     * @param map
     * @return
     */
    public List<EmrDefectVo> findDefectByPkPv(Map map);
    
    /**
     * 根据主键查询病历评分记录
     * @param pkGraderec
     * @return
     */
    public EmrGradeRec queryGradeRecByPk(@Param("pkGraderec")String pkGraderec);
    
    /**
     * 根据pkpv查询患者质控扣分项
     * @param map
     * @return
     */
    public List<EmrDefectVo> findChangedDefect(Map map);
    
    /**
     * 根据pkpv查询患者质控扣分项
     * @param map
     * @return
     */
    public List<EmrDeptList> findDept(Map map);
    
    /**
     * 批量更新患者病历记录
     * @param pkPvs/pkEmp/qcDate
     * @return
     */
    public void updateEmrPatRecList(@Param("pkPvs")List<String> pkPvs,@Param("pkEmp")String pkEmp,@Param("qcDate")Date qcDate);
    
    /**
     * 根据条件查询病历归档列表
     * @param map
     * @return
     */
    public List<EmrPatRecListVo> queryPatArchList(Map map);
    /**
     * 更新病历事件记录
     * @param map
     */
    public void updteEventRecTimeLimit(Map map);
}

