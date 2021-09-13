package com.zebone.nhis.emr.rec.tmp.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElement;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTemplate;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTmpOrg;
import com.zebone.nhis.common.module.emr.rec.tmp.EmrTmpSet;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;


/**
 * 病历书写-模板管理-mapper
 * @author chengjia
 *
 */
@Mapper
public interface EmrTmpMapper{   
    /**
     * 根据主键查询 病历模板
     */
    public EmrTemplate getEmrTemplateById(@Param("pkTmp")String pkTmp); 

    /**
    * 根据模板类型、科室、code查询科室会诊病历模板
    */
   public List<EmrTemplate> queryConsultTmpByCode(Map paramMap); 
   
   
   /**
    * 根据主键查询 病历模板数据
    */
   public EmrTemplate queryTmpDataById(@Param("pkTmp")String pkTmp); 
   
   /**
    * 根据主键查询病历模板记录（含分类）
	 * @param pkTmp
	 * @return
	 */
   public EmrTemplate queryTmpById(@Param("pkTmp")String pkTmp); 
   
   /**
    * 根据主键查询病历模板记录（含分类、无数据）
	 * @param pkTmp
	 * @return
	 */
   public EmrTemplate queryTmpByIdNoData(@Param("pkTmp")String pkTmp); 
   
   /**
    *
    * 查询所有模板记录
	 * @return
	 */
   public List<EmrTemplate> queryAllTmpList(); 
   
   /**
    * 根据编码查询病历模板记录（含分类）
	 * @param code
	 * @return
	 */
   public EmrTemplate queryTmpByCode(Map paramMap);    
    /**
     * 保存病历模板
     */
    public int saveEmrTemplate(EmrTemplate emrTemplate);
    
    /**
     * 根据主键更新病历模板（参数对象中的主键将作为更新条件）
     */
    public int updateEmrTemplate(EmrTemplate emrTemplate);
    
    /**
     * 根据主键删除病历模板
     */
    public int deleteEmrTemplate(@Param("pkTmp")String pkTmp);
    
    /**
     * 查询科室模板列表
     */
    public List<EmrTemplate> queryTmpList(@Param("pkDept")String pkDept);    
    
    /**
     * 查询科室模板列表(带文档分类)/@Param("pkDept")String pkDept,@Param("flagOpen")String flagOpen
     */
    public List<EmrTemplate> queryTypeTmpList(Map paramMap);    

    /**
     * 查询科室模板列表(pkDept)
     * @param paramMap
     * @return
     */
    public List<EmrTemplate> queryTypeTmpListDept(Map paramMap);    

    /**
     * 查询全局模板列表
     * @param paramMap:@Param("pkOrg")String pkOrg
     * @return
     */
    public List<EmrTemplate> queryTmpListOrg(Map paramMap);    
    
    /**
     * 根据查询条件查询模板
     * @param pk/name
     * @return
     */
    public List<EmrTemplate> queryTmpByConds(Map paramMap);
            
    /**
     * 查询科室用模板
     * @param pk/name
     * @return
     */
    public List<EmrTemplate> queryDeptTmpList(Map paramMap);
    
    /**
     * 根据主键查询
     */
    public EmrTmpSet getEmrTmpSetById(@Param("pkSet")String pkSet); 

    /**
     * 查询出所有记录
     */
    public List<EmrTmpSet> findAllEmrTmpSet();    
    
    /**
     * 保存
     */
    public int saveEmrTmpSet(EmrTmpSet emrTmpSet);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrTmpSet(EmrTmpSet emrTmpSet);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrTmpSet(@Param("pkSet")String pkSet); 
    
    /**
     * 查询模板设置列表(pk_tmp,code)
     */
    public List<EmrTmpSet> queryTmpSetByCode(@Param("pkTmp")String pkTmp,@Param("code")String code); 
    
    /**
     * 查询模板设置列表(pk_tmp)
     * @param pkTmp
     * @return
     */
    public List<EmrTmpSet> queryTmpSetByTmp(@Param("pkTmp")String pkTmp);       
    
    /**
     * 根据主键查询
     */
    public EmrTmpOrg getEmrTmpOrgById(@Param("pkTmporg")String pkTmporg); 

    
    public List<EmrTmpOrg> queryTmpOrgByTmpId(@Param("pkTmp")String pkTmp);
    
    /**
     * 查询出所有记录
     */
    public List<EmrTmpOrg> findAllEmrTmpOrg();    
    
    /**
     * 保存
     */
    public int saveEmrTmpOrg(EmrTmpOrg emrTmpOrg);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrTmpOrg(EmrTmpOrg emrTmpOrg);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrTmpOrg(@Param("pkTmporg")String pkTmporg);
    
    
    /**
     * 根据查询条件查询模板科室
     * @param pk/name
     * @return
     */
    public List<EmrTmpOrg> queryTmpOrgByConds(Map paramMap);
}

