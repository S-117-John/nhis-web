package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageCharges;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageDiags;
import com.zebone.nhis.common.module.emr.rec.rec.EmrHomePageOps;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.webservice.syx.vo.EmrEncInfoVo;
import com.zebone.nhis.webservice.syx.vo.EmrPatInfoResult;
import com.zebone.nhis.webservice.syx.vo.EmrPvDiagList;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfAdmitRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfCourseRec;
import com.zebone.nhis.webservice.syx.vo.emr.EmrAfHomePage;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmCourseRslt;
import com.zebone.nhis.webservice.syx.vo.emr.EmrHmFirstCourseRslt;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 病历书写-syx-mapper
 * @author chengjia
 */
@Mapper
public interface EmrSyxRecMapper{   
 
    /**
	 * 查询住院病人基本信息
	 * @param 
	 * @param 
	 * @return
	 */
	public List<EmrEncInfoVo> queryPatEncList(Map map);
	
    /**
	 * 查询住院病人基本诊断信息
	 * @param 
	 * @param 
	 * @return
	 */
	public List<EmrPvDiagList> queryPvDiagList(Map map);
	/**
	 * 查询住院患者基本信息(EMR12)
	 * @param map
	 * @return
	 */
	public List<EmrPatInfoResult> queryPatInfoList(Map map);
	
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
	 * 查询住院病人病历信息
	 * @param 
	 * @param 
	 * @return
	 */
	public List<EmrEncInfoVo> queryPatEmrEncList(Map map);
	
	/**
	 * 查询患者首次病程记录信息
	 * @param map
	 * @return
	 */
	public List<EmrHmFirstCourseRslt> queryHmFirstCourse(Map map);
	
	/**
	 * 查询患者病程记录信息
	 * @param map
	 * @return
	 */
	public List<EmrHmCourseRslt> queryHmCourse(Map map);
	
	/**
	 * 查询患者入院记录信息
	 * @param map
	 * @return
	 */
	public List<EmrHmCourseRslt> queryAdmitRec(Map map); 
	
	
	/**
	 * 查询患者病案首页记录信息
	 * @param map
	 * @return
	 */
	public List<EmrAfHomePage> queryAfHomePage(Map map);
	
	/**
	 * 查询患者病案首页诊断
	 * @param pks
	 * @return
	 */
	public List<EmrHomePageDiags> queryHomePageDiagsByPks(List<String> pks);
	
	/**
	 * 查询患者病案首页手术
	 * @param pks
	 * @return
	 */
	public List<EmrHomePageOps> queryHomePageOpsByPks(List<String> pks);
	
	/**
	 * 查询患者病案首页费用
	 * @param pks
	 * @return
	 */
	public List<EmrHomePageCharges> queryHomePageChargesByPks(List<String> pks);
	
	/**
	 * 查询患者入院记录
	 * @param map
	 * @return
	 */
	public List<EmrAfAdmitRec> queryAfAdmitRec(Map map);
	
	/**
	 * 查询患者病程记录
	 * @param map
	 * @return
	 */
	public List<EmrAfCourseRec> queryAfCourseRec(Map map);
}
