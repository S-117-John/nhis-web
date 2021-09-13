package com.zebone.nhis.ma.tpi.rhip.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.tpi.rhip.vo.*;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.cn.ipdw.CnOpSubjoin;
import com.zebone.nhis.common.module.cn.opdw.CnOpEmrRecord;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePage;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageBr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOr;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOrDt;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.scm.st.PdSt;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 第三方接口-区域卫生平台-mapper
 * @author chengjia
 */
@Mapper
public interface RhipMapper{   
   

    /**
     * 根据查询条件查询患者就诊记录
     * @param pkPatrec/pkPv
     * @return
     */
    public List<PatListVo> queryPatList(Map paramMap);  
    
    
    /**
     * 根据查询条件查询患者就诊记录(门诊)
     * @param pkPatrec/pkPv
     * @return
     */
    public List<PatListVo> queryPatListOp(Map paramMap);  
    
    /**
     * 查询患者病历记录
     * @param pkPv/typeCode/orderBy
     * @return
     */
    public List<MedRecVo> queryPatMedRecDoc(Map paramMap); 
    
    /**
     * 查询患者医嘱记录
     * @param pkPv/typeCode/orderBy
     * @return
     */
    public List<TOrderVo> queryCnOrder(Map paramMap); 
    
    
    //病案首页
    public EmrHomePage queryHomePageByPv(Map map);
    
    public List<EmrHomePageDiags> queryHomePageDiags(Map map); 
    
    public List<EmrHomePageOps> queryHomePageOps(Map map); 

    public List<EmrHomePageCharges> queryHomePageCharges(Map map);

    public List<EmrHomePageBr> queryEmrHomePageBrsByPage(@Param("pkPage")String pkPage); 
    
    public EmrHomePageOr

    queryEmrHomePageOrByPage(@Param("pkPage")String pkPage);

    public List<EmrHomePageOrDt> queryEmrHomePageOrDtsByPage(@Param("pkPage")String pkPage);    
    
    /**
     * 获取单个患者的手术申请列表
     * @param pkPv
     * @return
     */
    List<RhipCnOpApply> getOpApplyList(@Param("pkPv")String pkPv, @Param("pkOrg")String pkOrg);
    
    /**
     * 获取附加申请列表
     * @param pkOpList
     * @return
     */
    List<CnOpSubjoin> getChildApplyList(@Param("pkOpList") List<String> pkOpList);
    
    /**
     * 查询生命体征数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryVtsList(Map map); 
    
	/**
	 * 查询患者结算发票信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> querySettleInvList(Map map);
    
    
	/**
	 * 查询患者结算信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> querySettleList(Map map);
    
	/**
	 * 查询患者住院收费明细信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> queryBlIpDtList(Map map);
    
	/**
	 * 查询患者结算信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> queryOpSettleList(Map map);
    
	/**
	 * 查询患者门诊收费明细信息
	 * @param pkPv
	 * @return
	 */
    public List<Map<String,Object>> queryBlOpDtList(Map map);
    
    
    /**
	 * 查询物品-入出库单
	 * @param pkPv
	 * @return
	 */
    public List<PdstDetail> queryPdstList(Map map);
    public List<PvDiagVo> queryPvDiagVos(Map map);
    
    
    /**
     * 查询患者就诊记录（住院）
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> queryPatEncInfo(Map paramMap); 
    
    /**
     * 查询患者就诊记录（门诊）
     * @param pkPv
     * @return
     */
    public List<Map<String,Object>> queryPatEncInfoOp(Map paramMap); 
    
    /**
     * 查询患者门诊病历记录
     * @param map
     * @return
     */
    public CnOpEmrRecord queryPatEmrOp(Map map);
    
    
	/**
	 * 获得患者处方列表(含明细)
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> queryPatPresList(String pkPv);
	
	
    /**
     * 根据主键查询病历文档内容
     */
    public EmrMedDoc getEmrMedDocById(@Param("pkDoc")String pkDoc); 
	
    /**
     * 根据查询条件查询出院患者就诊记录
     * @param pkPatrec/pkPv
     * @return
     */
    public List<OutPvPatiVo> queryOutPatiList(Map paramMap);

    /**
     * 根据pkpv查询诊断信息
     * @param pkPv
     * @return
     */
	public List<Map<String,String>> queryDiagInfo(String pkPv);

    /**
     * 根据pkpv查询费用费用分类 金额和代码 发票名称
     * @param map
     * @return
     */
    public  List<IptFeeCostVo> queryIptFeeCostList(Map map);
    
    public  List<String> queryPvList();
    
    public  List<CardInfo> queryCardInfo(String pkPi);
}


