package com.zebone.nhis.emr.rec.rec.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.rec.EmrChargeList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrDeptList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrEmpList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageBr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOrDt;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageTrans;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageZL;
import com.zebone.nhis.common.module.emr.rec.rec.EmrLisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpHandleList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOpsList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrOrdList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatRec;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.module.emr.rec.rec.EmrRisResult;
import com.zebone.nhis.common.module.emr.rec.rec.EmrTransList;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.emr.rec.rec.vo.*;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * 病历书写-病历书写-mapper
 * @author chengjia
 */
@Mapper
public interface EmrRecMapper{   
    /**
     * 根据主键查询
     */
    public EmrMedRec getEmrMedRecById(@Param("pkRec")String pkRec); 

    /**
     * 查询出所有记录
     */
    public List<EmrMedRec> findAllEmrMedRec();    
    
    /**
     * 保存
     */
    public int saveEmrMedRec(EmrMedRec emrMedRec);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrMedRec(EmrMedRec emrMedRec);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrMedRec(@Param("pkRec")String pkRec);
    
    /**
     * 根据主键查询
     */
    public EmrMedDoc getEmrMedDocById(@Param("pkRec")String pkRec); 

    /**
     * 查询出所有记录
     */
    public List<EmrMedDoc> findAllEmrMedDoc();    
    
    /**
     * 保存
     */
    public int saveEmrMedDoc(EmrMedDoc emrMedDoc);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrMedDoc(EmrMedDoc emrMedDoc);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrMedDoc(@Param("pkRec")String pkRec);

    /**
     * 保存(病历存储库)
     */
    public int saveEmrMedDocEmr(EmrMedDoc emrMedDoc);
    
    /**
     * 根据主键更新(病历存储库)
     */
    public int updateEmrMedDocEmr(EmrMedDoc emrMedDoc);
    
    /**
     * 根据主键删除(病历存储库)
     */
    public int deleteEmrMedDocEmr(@Param("pkRec")String pkRec,@Param("dbName")String dbName);
    
    /**
     * 查询病历文档列表
     * @param pkPv
     * @return
     */
    public List<EmrMedRec> queryMedRecListByPvs(List<PvEncounter> pvens); 
    public List<EmrMedRec> queryMedRecList(@Param("pkPv")String pkPv); 
    
    public List<EmrMedRec> queryMedRecListReview(@Param("pkPv")String pkPv,@Param("euType")String euType); 
    public List<EmrMedRec> queryMedRecListByFlagRecover(@Param("pkPv")String pkPv); 
    /**
     * 查询当前病人所有病历数据
     * @param pkPv
     * @return
     */
    public List<EmrMedRec> queryPatMedRecList(@Param("pkPv")String pkPv); 
    
    /**
     * 查询病历文档列表(模板/分类)
     * @param pkPv
     * @return
     */
    public List<EmrMedRec> queryMedRecListAll(@Param("pkPv")String pkPv); 
    
    /**
     * 根据主键查询病历文档记录
     * @param pkRec
     * @return
     */
    public EmrMedRec queryEmrMedRecById(@Param("pkRec")String pkRec);
    
    /**
     * 根据主键查询病历文档内容
     * @param pkRec
     * @return
     */
    public EmrMedDoc queryEmrMedDocById(@Param("pkDoc")String pkDoc);
 
    /**
     * 根据主键查询病历文档内容(病历存储库)
     * @param pkRec
     * @return
     */
    public EmrMedDoc queryEmrMedDocByIdEmr(@Param("pkDoc")String pkDoc,@Param("dbName")String dbName);
    
    /**
     * 病历通用查询
     * @param sqlText
     * @return
     */
    public List<HashMap<String,String>> queryDictList(@Param("sqlText")String sqlText); 
    
    /**
     * 根据主键查询
     */
    public EmrPatRec getEmrPatRecById(@Param("pkPatrec")String pkPatrec); 

    /**
     * 查询出所有记录
     */
    public List<EmrPatRec> findAllEmrPatRec();    
    
    /**
     * 保存
     */
    public int saveEmrPatRec(EmrPatRec emrPatRec);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrPatRec(EmrPatRec emrPatRec);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrPatRec(@Param("pkPatrec")String pkPatrec);
    
    /**
     * 根据科室编码查询病人列表
     * @param pkDept/flagIn/orderBy
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List<EmrPatList> queryPatListByDept(Map paramMap);   
    
    /**
     * 根据pkpv查询病人列表
     * @param pkPv
     * @return
     */
	public EmrPatList queryPatListByPkpv(Map paramMap);   
    
    /**
     * 根据科室编码查询病人列表
     * @param pkDept/flagIn/orderBy
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List<EmrPatList> queryPatListByDeptDc(Map paramMap);

    public List<EmrPatListReview> queryPatListByDeptDcReview(Map paramMap);

    /**
     * 查询病历患者列表
     * @param pkDept/flagIn/orderBy
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List<EmrPatList> queryPatListAudit(Map paramMap);   
    
    /**
     * 查询病历患者列表
     * @param pkDept/flagIn/orderBy
     * @return
     */
    @SuppressWarnings("rawtypes")
	public List<EmrPatList> queryPatListAuditOracle(Map paramMap);  
    
    /**
     * 根据查询条件查询患者就诊记录
     * @param pkPatrec/pkPv
     * @return
     */
    public List<EmrPatList> queryPatListByConds(Map paramMap);    
    
    /**
     * 根据查询条件查询患者病历记录
     * @param pkPatrec/pkPv
     * @return
     */
    public List<EmrPatRec> queryEmrPatRecByConds(Map paramMap);    
    
    /**
     * 根据编码查询病历人员信息
     * @param pks
     * @return
     */
    public List<EmrEmpList> queryEmpListByPks(List<String> codes);
 
    /**
     * 根据pk查询病历人员信息
     * @param pkEmp
     * @return
     */
    public EmrEmpList queryEmrEmpListByPk(@Param("pkEmp")String pkEmp);    
    
    
    /**
     * 病历通用更新
     * @param sqlText
     * @return
     */
    public int updateDictData(@Param("sqlText")String sqlText);
    
    /**
     * 科室质控点击完成时调用省病案接口存储过程
     * @param paramMap
     */
    public int updateProc(Map param);
    /**
     * 病案编码完成时调用存储过程
     * @param paramMap
     */
    public int updateProcbaSub(Map param);
    
    public int updateProcbaSubIcd(Map param);
    
    /**
     * 病历通用更新
     * @param sqlText
     * @return
     */
    public int updateDictDataDC(@Param("sqlText")String sqlText);
    
    /**
     * 保存病历诊断记录
     */
    public int saveEmrPatDiags(EmrPatDiags emrPatDiags);
    
    /**
     * 更新病历诊断记录
     */
    public int updateEmrPatDiags(EmrPatDiags emrPatDiags);
    
    /**
     * 根据主键删除病历诊断记录
     */
    public int deleteEmrPatDiags(@Param("pkDiag")String pkDiag);
        
    /**
     * 查询患者病历诊断记录
     * @param pkPv
     * @return
     */
    public List<EmrPatDiags> queryPatDiagsList(Map paramMap);  
    
    
    /**
     * 查询医师常用诊断
     * @param pkPv
     * @return
     */
    public List<EmrPatDiags> queryEmpDiagsList(@Param("pkEmp")String pkEmp); 
    
    /**
     * 查询医嘱信息
     * @param pkPv
     * @return
     */
    public List<EmrOrdList> queryPatOrdList(@Param("pkPv")String pkPv); 
    
    /**
     * 查询检验结果信息
     * @param pkPv
     * @return
     */
    public List<EmrLisResult> queryPatLisResult(@Param("pkPv")String pkPv,@Param("beginDateStr")String beginDateStr,@Param("endDateStr")String endDateStr,@Param("pkPi")String pkPi);
    /**
     * 查询检验结果信息
     * @param pkPv
     * @return
     */
    public List<EmrLisResult> queryPatLisResultSdtj(@Param("pkPv")String pkPv,@Param("beginDateStr")String beginDateStr,@Param("endDateStr")String endDateStr,@Param("pkPi")String pkPi);
    /**
     * 查询检查结果信息
     * @param pkPv
     * @return
     */
    public List<EmrRisResult> queryPatRisResult(@Param("pkPv")String pkPv,@Param("beginDateStr")String beginDateStr,@Param("endDateStr")String endDateStr,@Param("pkPi")String pkPi);    
    
    /**
     * 根据文档分类查询患者病历记录
     * @param pkPv
     * @param typeCode
     * @return
     */
    public List<EmrMedRec> queryMedRecListByType(@Param("pkPv")String pkPv,@Param("typeCode")String typeCode);
    
    /**
     * 查询病历科室列表
     * @param paramMap
     * @return
     */
    public List<EmrDeptList> queryEmrDeptList(Map paramMap); 
    
    
    /**
     * 根据pk_pv查询病案首页
     */
    public EmrHomePage queryHomePageByPv(Map map);
    
    public List<EmrHomePageDiags> queryHomePageDiags(Map map); 
    
    public List<EmrHomePageOps> queryHomePageOps(Map map); 

    public List<EmrHomePageCharges> queryHomePageCharges(Map map);
    
    public EmrHomePageZL queryHomePageZls(Map map);
    /**
     * 保存病案首页
     */
    public int saveEmrHomePage(EmrHomePage emrHomePage);
    
    /**
     * 根据主键更新病案首页
     */
    public int updateEmrHomePage(EmrHomePage emrHomePage);
    
    /**
     * 根据主键删除病案首页
     */
    public int deleteEmrHomePage(@Param("pkPage")String pkPage);
    
    /**
     * 根据pk_pv查询HIS病案首页相关数据
     */
    public EmrHomePage queryHisHomePageData(Map paramMap); 

    /**
     * 查询患者费用信息(HIS)
     * @param pkPv
     * @return
     */
    public List<EmrChargeList> queryChargeList(Map map);  
    
    /**
     * 查询患者费用信息(HIS 病案)
     * @param pkPv
     * @return
     */
    public List<EmrChargeList> queryChargeListPg(Map map);  
    
    
    /**
     * 查询患者手术记录
     * @param pkPv
     * @return
     */
    public List<EmrOpsList> queryOpsList(Map map);  
    
    /**
     * 查询患者手术操作记录
     * @param pkPv
     * @return
     */
    public List<EmrOpHandleList> queryOphandleList(Map map);  
    
    /**
     * 保存
     */
    public int saveEmrHomePageOps(EmrHomePageOps emrHomePageOps);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageOps(EmrHomePageOps emrHomePageOps);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageOps(@Param("pkOps")String pkOps);
    
    /**
     * 保存
     */
    public int saveEmrHomePageDiags(EmrHomePageDiags emrHomePageDiags);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageDiags(EmrHomePageDiags emrHomePageDiags);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageDiags(@Param("pkPagediag")String pkPagediag);
    
    /**
     * 保存
     */
    public int saveEmrHomePageCharges(EmrHomePageCharges emrHomePageChages);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageCharges(EmrHomePageCharges emrHomePageCharges);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageCharges(@Param("pkCharge")String pkCharge);
    
    /**
     * 查询病历文档信息(pk_pv list)
     * @param pks
     * @return
     */
    public List<EmrMedRec> queryEmrDocListByPvs(List<String> pks);
    
    /**
     * 查询HIS诊断
     * @param pkPv
     * @return Map
     */
    public List<Map> queryPatHisDiags(Map map); 
    
    /**
     * 查询HIS诊断sqlserver
     * @param pkPv
     * @return Map
     */
    public List<Map> queryPatHisDiagsSql(Map map); 
    
    
    /**
     * 查询HIS医护人员信息
     * @param pkPv/pkDept/pkDeptNs
     * @return Map
     */
    public List<Map> queryPatHisStaff(Map map); 
    
    public List<Map> queryPatHisStaffSql(Map map);
    
    //查询当前诊断
	public List<Map<String,Object>> queryPvDiags(String pkPv);
	//查询历史诊断
	public List<Map<String,Object>> queryCnDiag(String pkPv);    
    
    /**
     * 根据主键查询病历医师
     * @param pkEmp
     * @return
     */
    public List<EmrDoctor> queryEmrDoctorByPk(Map map);
    
    public List<PvDiagVo> queryPvDiagVos(@Param("pkPv")String pkPv);
    
    public List<PvDiagVo> queryPvDiagVosPage(@Param("pkPv")String pkPv);
    
    /**
     * 根据主键查询
     */
    public EmrHomePageOr getEmrHomePageOrById(@Param("pkPageor")String pkPageor); 

    /**
     * 查询出所有记录
     */
    public List<EmrHomePageOr> findAllEmrHomePageOr();    
    
    /**
     * 保存
     */
    public int saveEmrHomePageOr(EmrHomePageOr emrHomePageOr);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageOr(EmrHomePageOr emrHomePageOr);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageOr(@Param("pkPageor")String pkPageor);
    
    /**
     * 根据主键查询
     */
    public EmrHomePageOrDt getEmrHomePageOrDtById(@Param("pkOrdt")String pkOrdt); 

    /**
     * 查询出所有记录
     */
    public List<EmrHomePageOrDt> findAllEmrHomePageOrDt();    
    
    /**
     * 保存
     */
    public int saveEmrHomePageOrDt(EmrHomePageOrDt emrHomePageOrDt);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageOrDt(EmrHomePageOrDt emrHomePageOrDt);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageOrDt(@Param("pkOrdt")String pkOrdt);
    
    /**
     * 根据主键查询
     */
    public EmrHomePageBr getEmrHomePageBrById(@Param("pkBr")String pkBr); 

    /**
     * 查询出所有记录
     */
    public List<EmrHomePageBr> findAllEmrHomePageBr();    
    
    /**
     * 保存
     */
    public int saveEmrHomePageBr(EmrHomePageBr emrHomePageBr);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageBr(EmrHomePageBr emrHomePageBr);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageBr(@Param("pkBr")String pkBr);
    
    /**
     * 查询患者转科记录
     * @param pkPv
     * @return
     */
    public List<EmrTransList> queryTransList(Map map);  
    
    /**
     * 保存
     */
    public int saveEmrHomePageTrans(EmrHomePageTrans emrHomePageTrans);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageTrans(EmrHomePageTrans emrHomePageTrans);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageTrans(@Param("pkTrans")String pkTrans);
    
    /**
     * 保存
     */
    public int saveEmrHomePageZL(EmrHomePageZL emrHomePageZL);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrHomePageZL(EmrHomePageZL emrHomePageZL);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrHomePageZL(@Param("pkZl")String pkZL);
    
	/**
	 * 查询分娩记录
	 * @param paramMap{pkPv}
	 * @return
	 */
	public List<EmrInfantRecVo> queryPvLaborRecDt(@Param("pkPv")String pkPv);
	
	public void deleteEmrHomePageOpsByPage(@Param("pkPage")String pkPage);
	
	public void deleteEmrHomePageDiagsByPage(@Param("pkPage")String pkPage);
	
	public void deleteEmrHomePageChargesByPage(@Param("pkPage")String pkPage);
	
	public void deleteEmrHomePageBrByPage(@Param("pkPage")String pkPage);
	
	public void deleteEmrHomePageOrByPage(@Param("pkPage")String pkPage);
	
	public void deleteEmrHomePageOrDtByPage(@Param("pkPage")String pkPage);
	
    public List<EmrHomePageBr> queryEmrHomePageBrsByPage(@Param("pkPage")String pkPage); 
    
    public EmrHomePageOr queryEmrHomePageOrByPage(@Param("pkPage")String pkPage); 
    
    public EmrHomePageOr queryEmrHomePageOrByPageSql(@Param("pkPage")String pkPage); 

    public List<EmrHomePageOrDt> queryEmrHomePageOrDtsByPage(@Param("pkPage")String pkPage);
    
	/**
	 * 查询HIS肿瘤记录
	 * @param paramMap{pkPv}
	 * @return
	 */
	public List<EmrHomePageOrDt> queryHisHomePageOrDt(@Param("pkPv")String pkPv);
	
	/**
	 * 查询HIS肿瘤记录
	 * @param paramMap{pkPv}
	 * @return
	 */
	public List<EmrHomePageOrDt> queryHisHomePageOrDtSql(@Param("pkPv")String pkPv);
    
    /**
     * 查询患者生命体征数据
     * @param pkPv
     * @return
     */
    public List<EmrVitalSigns> queryVitalSigns(Map map);    
    
    /**
     * 查询患者生命体征数据--血糖(从护理记录单取)
     * @param pkPv
     * @return
     */
    public List<Map<String, Object>> queryVitalSignsBlood(Map map);
    /**
     * 设置医师
     * @param map
     */
    public void setPhysician(Map map);
    
    /**
     * 更新病历文档上级医师设置
     * @param map
     */
    public void updateMedRecEmpAct(Map map);
    
    /**
     * 更新病历记录
     * @param map
     */
    public void renewRec(Map map);

    /**
     * 完成病历
     * @param map
     */
	public void finishEmr(Map map);
	
    /**
     * 查询病历文档列表（目录与文档一一对应)
     * @param pkPv
     * @return
     */
    public List<EmrMedDocVo> queryMedDocList(@Param("pkPv")String pkPv);
    
    /**
     * 查询当前病人所有病历列表+数据
     * @param pkPv
     * @return
     */
    public List<EmrMedRec> queryMedRecDocList(Map map); 
    
    /**
     * 查询当前病人所有病历列表+数据(数据存储库)
     * @param pkPv
     * @return
     */
    public List<EmrMedRec> queryMedRecDocListEmr(Map map); 
    
    /**
     * 根据主键查询病历数据(数据存储库)
     * @param pks
     * @return
     */
    public List<EmrMedDoc> queryDocListEmrByPks(@Param("dbName")String dbName,@Param("list")List<String> pkDocs);
    /**
	 * 根据pkpv查询患者出院小结的出院时间
	 * @param pkPv
	 * @param user
	 * @return
	 */
    public String queryDisTimeBySummaryByPk(@Param("pkPv")String pkPv);
    /**
     * 根据首页的pkpv去更新his的pi信息
     * @param emrHomePage
     * @return
     */
    public int updHisPiMaster(EmrHomePage emrHomePage);
    /**
     * 根据编码查询地址信息
     * @param emrHomePage
     * @return
     */
    public String queryCurrAddrByCode(String codeDiv);
    /**
     * 根据就诊主键查询患者会诊记录
     * @param pkPv
     * @return
     */
    public List<EmrConsult> queryConsultByPk(String pkPv);
    /**
     * 根据患者住院号查询历史病历(博爱)
     * @param pkPv
     * @return
     */
    public List<EmrHistory> queryHistorytByCodeIp(String codeIp);
    /**
     * 根据第三方同步患者信息(门诊)
     * @param param
     * @return
     */
    public int syncPatInfoOpByThird(Map param);
    
    /**
     * 根据第三方同步患者信息(住院)
     * @param param
     * @return
     */
    public int syncPatInfoIpByThird(Map param);

    /**
     * 同江医院查询HIS中病人病历记录
     * @param param
     * @return
     */
	public List<Map<String,Object>> queryHisPatRecBac(Map<String,Object> paramMap);
    /**
     * 同江医院同步诊断到HIS
     * @param param
     * @return
     */
    public int syncToHisTjDiag(Map param);
    /**
     * 查询患者--门诊质控使用
     * @param param
     * @return
     */
    List<Map<String,Object>> queryOpPatInfo(Map<String,Object> param);
}
