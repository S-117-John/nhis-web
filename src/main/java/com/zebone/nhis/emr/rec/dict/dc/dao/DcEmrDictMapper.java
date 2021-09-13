package com.zebone.nhis.emr.rec.dict.dc.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.emr.rec.dict.EmrCommonWords;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElement;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDataElementDc;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictClass;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictCode;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictOpt;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRange;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDictRangeCode;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDocType;
import com.zebone.nhis.common.module.emr.rec.dict.EmrDoctor;
import com.zebone.nhis.common.module.emr.rec.dict.EmrGradeRule;
import com.zebone.nhis.common.module.emr.rec.dict.EmrGradeStandard;
import com.zebone.nhis.common.module.emr.rec.dict.EmrKnowledgeBase;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParagraph;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParagraphDc;
import com.zebone.nhis.common.module.emr.rec.dict.EmrParameter;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSnGenerator;
import com.zebone.nhis.common.module.emr.rec.dict.EmrSwitch;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 
 * 病历书写-字典管理-mapper
 * @author chengjia
 */
@Mapper
public interface DcEmrDictMapper{   
	
	//EmrDocType
    /**
     * 根据主键查询
     */
    public EmrDocType getEmrDocTypeById(@Param("pkDoctype")String pkDoctype); 

    /**
     * 查询出所有记录
     */
    public List<EmrDocType> findAllEmrDocType(EmrDocType emrDocType);    
    
    /**
     * 保存
     */
    public int saveEmrDocType(EmrDocType emrDocType);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDocType(EmrDocType emrDocType);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDocType(@Param("pkDoctype")String pkDoctype);
    
    /**
     * 查询出所有有效记录(根据eu_visit,eu_use)
     */
    public List<EmrDocType> queryDocTypeListVstUsFlg(Map paramMap);  

    /**
     * 查询出所有医师用二级文档分类
     */
    public List<EmrDocType> queryDocTypeLTDoc();  

    //EmrParagraphDc
    /**
     * 根据主键查询
     */
    public EmrParagraphDc getEmrParagraphDcById(@Param("pkPara")String pkPara); 

    /**
     * 查询出所有记录
     */
    public List<EmrParagraphDc> findAllEmrParagraphDc(EmrParagraphDc para);    
    
    /**
     * 保存
     */
    public int saveEmrParagraphDc(EmrParagraphDc emrParagraphDc);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrParagraphDc(EmrParagraphDc emrParagraphDc);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrParagraphDc(@Param("pkPara")String pkPara);
    
    /**
     * 查询病历文档段落（有效）
     * @return
     */
    public List<EmrParagraph> queryParaList();    
    
    //EmrDataElementDc
    /**
     * 根据主键查询
     */
    public EmrDataElementDc getEmrDataElementDcById(@Param("pkElement")String pkElement); 

    /**
     * 查询出所有记录
     */
    public List<EmrDataElementDc> findAllEmrDataElementDc();    
    
  
    /**
     * 保存
     */
    public int saveEmrDataElementDc(EmrDataElement emrDataElementDc);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDataElementDc(EmrDataElement emrDataElementDc);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDataElementDc(@Param("pkElement")String pkElement);
    
    /**
     * 查询数据元素列表
     */
    public List<EmrDataElement> queryDataEleList();       
    
    
	//EmrDictClass
    /**
     * 根据主键查询
     */
    public EmrDictClass getEmrDictClassById(@Param("pkDictcls")String pkDictcls); 

    /**
     * 查询出所有记录
     */
    public List<EmrDictClass> findAllEmrDictClass();    
    
    
    /**
     * 查询出所有记录(未删/排序)
     */
    public List<EmrDictClass> queryDictClassList();    
    
    /**
     * 保存
     */
    public int saveEmrDictClass(EmrDictClass emrDictClass);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDictClass(EmrDictClass emrDictClass);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDictClass(@Param("pkDictcls")String pkDictcls);
    
    //EmrDictCode
    /**
     * 根据主键查询
     */
    public EmrDictCode getEmrDictCodeById(@Param("pkDictcode")String pkDictcode); 

    /**
     * 查询出所有记录
     */
    public List<EmrDictCode> findAllEmrDictCode();    
    
    /**
     * 根据字典分类编码查询出所有记录(未删/排序)
     */
    public List<EmrDictCode> queryDictCodeList(@Param("classCode")String classCode); 
    
    
    /**
     * 保存
     */
    public int saveEmrDictCode(EmrDictCode emrDictCode);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDictCode(EmrDictCode emrDictCode);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDictCode(@Param("pkDictcode")String pkDictcode);
    
    //EmrDictOpt
    /**
     * 根据主键查询
     */
    public EmrDictOpt getEmrDictOptById(@Param("pkDictopt")String pkDictopt); 

    /**
     * 查询出所有记录
     */
    public List<EmrDictOpt> findAllEmrDictOpt();    

    /**
     * 根据编码查询病历选择字典
     */
    public List<EmrDictOpt> getDictOptByCode(@Param("code")String code);    
    
    /**
     * 保存
     */
    public int saveEmrDictOpt(EmrDictOpt emrDictOpt);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDictOpt(EmrDictOpt emrDictOpt);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDictOpt(@Param("pkDictopt")String pkDictopt);
    
    //EmrDictRange
    /**
     * 根据主键查询
     */
    public EmrDictRange getEmrDictRangeById(@Param("pkRange")String pkRange); 

    /**
     * 查询出所有记录
     */
    public List<EmrDictRange> findAllEmrDictRange();    
    
    /**
     * 查询出所有记录(未删除/排序)
     */
    public List<EmrDictRange> queryDictRangeList();
        
    /**
     * 根据查询条件查询记录
     * @param code
     * @return
     */
    public List<EmrDictRange> queryDictRangeByConds(@Param("code")String code);
    
    /**
     * 根据名称查询病历值域
     * @param map
     * @return
     */
    public List<EmrDictRange> queryDictRangeByName(Map paramMap);
    
    /**
     * 根据名称查询病历值域编码
     * @param map
     * @return
     */
    public List<EmrDictRangeCode> queryDictRangeCodeByConds(Map paramMap);    
    
    /**
     * 保存
     */
    public int saveEmrDictRange(EmrDictRange emrDictRange);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDictRange(EmrDictRange emrDictRange);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDictRange(@Param("pkRange")String pkRange);
    
    //EmrRangeCode
    /**
     * 根据主键查询
     */
    public EmrDictRangeCode getEmrDictRangeCodeById(@Param("pkRangeCode")String pkRangeCode); 

    /**
     * 查询出所有记录
     */
    public List<EmrDictRangeCode> findAllEmrDictRangeCode();    
    
    
    /**
     * 查询出所有记录(根据分类编码)
     */
    public List<EmrDictRangeCode> queryDictRangeCodeList(@Param("pkRange")String pkRange);    
    
    /**
     * 保存
     */
    public int saveEmrDictRangeCode(EmrDictRangeCode emrDictRangeCode);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDictRangeCode(EmrDictRangeCode emrDictRangeCode);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrDictRangeCode(@Param("pkRangeCode")String pkRangeCode);  
    
    //EmrParameter
    /**
     * 根据主键查询
     */
    public EmrParameter getEmrParameterById(@Param("pkParam")String pkParam); 

    /**
     * 查询出所有记录
     */
    public List<EmrParameter> findAllEmrParameter();    

    /**
     * 查询出所有记录(排序)
     */
    public List<EmrParameter> queryParameterList(Map paramMap);    
    
    /**
     * 根据编码查询病历参数
     * @param code
     * @return
     */
    public EmrParameter queryParameterByCode(Map map);   
    
    /**
     * 保存
     */
    public int saveEmrParameter(EmrParameter emrParameter);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrParameter(EmrParameter emrParameter);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrParameter(@Param("pkParam")String pkParam);
    
    //EmrSwitch
    /**
     * 根据主键查询
     */
    public EmrSwitch getEmrSwitchById(@Param("pkSwitch")String pkSwitch); 

    /**
     * 查询出所有记录
     */
    public List<EmrSwitch> findAllEmrSwitch();    
    
    /**
     * 查询病历开关列表
     */
    public List<EmrSwitch> querySwitchList(Map paramMap);    
        
    public EmrSwitch querySwitchByCode(Map map);
    
    
    /**
     * 保存
     */
    public int saveEmrSwitch(EmrSwitch emrSwitch);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrSwitch(EmrSwitch emrSwitch);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrSwitch(@Param("pkSwitch")String pkSwitch);    
    
    //EmrCommonWords
    /**
     * 根据主键查询
     */
    public EmrCommonWords getEmrCommonWordsById(@Param("pkCword")String pkCword); 

    /**
     * 查询出所有记录
     */
    public List<EmrCommonWords> findAllEmrCommonWords();    
    
    /**
     * 保存
     */
    public int saveEmrCommonWords(EmrCommonWords emrCommonWords);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrCommonWords(EmrCommonWords emrCommonWords);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrCommonWords(@Param("pkCword")String pkCword);
    
    //EmrKnowledgeBase
    /**
     * 根据主键查询
     */
    public EmrKnowledgeBase getEmrKnowledgeBaseById(@Param("pkKnowBase")String pkKnowBase); 

    /**
     * 查询出所有记录
     */
    public List<EmrKnowledgeBase> findAllEmrKnowledgeBase();    
    
    /**
     * 保存
     */
    public int saveEmrKnowledgeBase(EmrKnowledgeBase emrKnowledgeBase);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrKnowledgeBase(EmrKnowledgeBase emrKnowledgeBase);

    
    /**
     * 更新流水号+1
     * @param code
     * @return
     */
    public int updateSysSn(@Param("code")String code);
    
    /**
     * 保存记录
     * @param emrSnGenerator
     * @return
     */
    public int saveSysSn(EmrSnGenerator emrSnGenerator);    
    
    /**
     * 取流水号
     * @param code
     * @return
     */
    public int getSysSnByCode(@Param("code")String code);
    
    
    /**
     * 根据查询条件查询数据元素
     * @param code/name
     * @return
     */
    public List<EmrDataElement> queryDataElementByConds(Map paramMap);
    
    /**
     * 查询标准诊断编码
     */
    public List<BdTermDiag> queryTermDiagList(BdTermDiag termDiag);
    
    /**
     * 查询知识库中填写过的诊断
     */
    public List<BdTermDiag> queryTermDiagTextList(BdTermDiag termDiag);
    
    /**
     * 查询病历字典代码表
     */
    public List<EmrDictCode> queryDictCodeAllList();
    
    
    /**
     * 查询临床知识库
     * @param param
	 * @param user
     */
    public List<EmrKnowledgeBase> queryKnowledgeBase(EmrKnowledgeBase emrKnowledgeBase);
    
    /**
     * 更新常用词汇记录
     */
    public List<EmrCommonWords> saveEmrCommonWordsBase(List<EmrCommonWords> list);
    
    /**
     * 更新病历文档分类
     */
    public List<EmrDocType> saveEmrDocTypesBase(List<EmrDocType> list);
    
    /**
     * 更新文档段落
     */
    public List<EmrParagraphDc> saveEmrParagraphDcBase(List<EmrParagraphDc> list);
    
    /**
     * 查询病历元素
     */
    public List<EmrDataElementDc> findAllEmrDataElementDcBase(EmrDataElementDc dataElement);  
    
    public List<EmrDataElementDc> findAllEmrDataElementDcBaseSql(EmrDataElementDc dataElement);  
    
    /**
     * 更新病历元素
     */
    public List<EmrDataElementDc> saveEmrDataElementDcBase(List<EmrDataElementDc> list);
    
    /**
     * 查询病历系统参数
     */
    public List<EmrParameter> findAllEmrParameterBase(Map paramMap);
    
    /**
     * 更新病历系统参数
     */
    public List<EmrParameter> updateEmrParameterBase();
    
    /**
     * 查询病历系统开关
     */
    public List<EmrSwitch> findAllEmrSwitchBase(Map paramMap);
    
    /**
     * 更新病历系统开关
     */
    public List<EmrSwitch> updateEmrSwitchBase();
    
    /**
     * 查询病历字典分类
     */
    public List<EmrDictClass> findAllEmrDictClassBase(EmrDictClass emrDictClass); 
    
    /**
     * 更新病历字典分类
     */
    public List<EmrDictClass> saveEmrDictClassBase(List<EmrDictClass> list);
    
    /**
     * 查询病历字典代码表
     */
    public List<EmrDictCode> findAllEmrDictCodeByCls(@Param("pkDictcls")String pkDictcls);
    
    /**
     * 更新病历字典代码表
     */
    public void saveEmrDictCodeBase(List<EmrDictCode> list);
    
    /**
     * 查询病历值域
     */
    public List<EmrDictRange> findAllEmrDictRangeBase(EmrDictRange emrDictRange);  
    
    /**
     * 更新病历值域
     */
    public List<EmrDictRange> saveEmrDictRangeBase(List<EmrDictRange> list);
    
    /**
     * 查询病历值域代码
     */
    public List<EmrDictRangeCode> findAllEmrDictRangeCodeByRa(@Param("pkRange")String pkRange);    
    
    /**
     * 病历值域代码
     */
    public void saveEmrDictRangeCodeBase(List<EmrDictRangeCode> list);
    
    /**
     * 根据主键查询
     */
    public List<EmrDoctor> getEmrDoctorById(Map paramMap); 
    
    /**
     * 根据主键查询病历医师
     * @param pkEmp
     * @return
     */
    public EmrDoctor queryEmrDoctorById(@Param("pkEmp")String pkEmp);
    
    /**
     * 查询出所有记录
     */
    public List<EmrDoctor> findAllEmrDoctor();    
    
    /**
     * 保存
     */
    public int saveEmrDoctor(EmrDoctor emrDoctor);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrDoctor(EmrDoctor emrDoctor);
    
    /**
     * 根据主键删除病历医师设置
     */
    public int deleteEmrDoctor(@Param("pkEmp")String pkEmp);
    
    /**
     * 查询病历医师设置
     */
    public List<EmrDoctor> findAllEmrDoctorBase(EmrDoctor emrDoctor);
    
    /**
     * 保存病历医师设置
     */
    public List<EmrDoctor> saveEmrDoctorBase(List<EmrDoctor> list);
    
    /**
     * 重置病历医师设置中的密码
     */
    public int updateEmrDoctorByPwd(@Param("pkEmp")String pkEmp);
    
    /**
     * 根据主键删除病历医师设置
     */
    public List<EmrDoctor> deleteEmrDoctorBase(@Param("pkEmp")String pkEmp);
    
    /**
     * 根据主键病历评分标准规则
     */
    public EmrGradeRule getEmrGradeRuleById(@Param("pkRule")String pkRule); 

    /**
     * 查询出所有记录
     */
    public List<EmrGradeRule> findAllEmrGradeRule();    
    
    /**
     * 保存
     */
    public int saveEmrGradeRule(EmrGradeRule emrGradeRule);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeRule(EmrGradeRule emrGradeRule);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrGradeRule(@Param("pkRule")String pkRule);
    
    /**
     * 查询出所有病历评分标准规则
     */
    public List<EmrGradeRule> findAllEmrGradeRuleBase(EmrGradeRule emrGradeRule);
    
    /**
     * 更新病历评分标准规则
     */
    public List<EmrGradeRule> saveEmrGradeRuleBase(List<EmrGradeRule> list);
    
    /**
     * 查询临床知识库诊断列表
     */
    public List<EmrKnowledgeBase> queryKBIcdList();
    
    /**
     * 根据条件查询临床知识库
     * @param typeCode/code
     * @return
     */
    public List<EmrKnowledgeBase> queryKnowledgeBaseByConds(@Param("typeCode")String typeCode,@Param("code")String code);
    
    
    /**
     * 查询医生常用词汇
     * @param 
     * @return
     */
    public List<EmrCommonWords> queryCommonWordsList(@Param("pkDept")String pkDept,@Param("pkEmp")String pkEmp);
    
    /**
     * 根据主键查询
     */
    public EmrGradeStandard getEmrGradeStandardById(@Param("pkStand")String pkStand); 

    /**
     * 查询出所有记录
     */
    public List<EmrGradeStandard> findAllEmrGradeStandard();    
    
    /**
     * 保存
     */
    public int saveEmrGradeStandard(EmrGradeStandard emrGradeStandard);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrGradeStandard(EmrGradeStandard emrGradeStandard);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrGradeStandard(@Param("pkStand")String pkStand);
    
    /**
     * 查询出所有病历评分标准
     */
    public List<EmrGradeStandard> findAllEmrGradeStandardBase(EmrGradeStandard emrGradeStandard);  
    
}

