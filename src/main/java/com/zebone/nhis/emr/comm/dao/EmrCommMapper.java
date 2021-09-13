package com.zebone.nhis.emr.comm.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.base.bd.code.BdWorkcalendardate;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.emr.comm.vo.BdSerialnoVo;
import com.zebone.nhis.emr.comm.vo.EmrMedRecPatVo;
import com.zebone.nhis.emr.comm.vo.EmrMedRecTaskVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 电子病历-公共服务-mapper
 * @author chengjia
 */
@Mapper
public interface EmrCommMapper{   
   

    /**
     * 查询医师病历书写任务
     * @param 
     * @return Map
     */
    public List<Map> queryEmpTaskList(Map map); 
    
    public List<EmrMedRecTaskVo> queryEmpTaskListSql(Map map); 
    
    /**
     * 根据查询条件查询患者就诊记录
     * @param pkPatrec/pkPv
     * @return
     */
    public List<EmrPatList> queryEmrPatList(Map paramMap);  
    
    Double selectSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName);

    int initSn(BdSerialnoVo initSn);

    /**
     * 查询病历用工作日
     * @param params
     * @return
     */
    public List<BdWorkcalendardate> queryEmrWeekdays(Map<String, String> params);
    
    
    /**获取医生患者任务列表
     * @param paramMap
     * @return
     */
    public List<EmrPatList> queryEmpPatTaskList(Map paramMap);  
    
    /**获取医生患者任务列表sqlsever
     * @param paramMap
     * @return
     */
    public List<EmrPatList> queryEmpPatTaskListSql(Map paramMap); 
    
    
    /**
     * 查询患者病历记录
     * @param codeIp/dateBegin/pkPv/typeCode/orderBy
     * @return
     */
    public List<EmrMedRecPatVo> queryPatMedRecDoc(Map paramMap); 
    
    
    /**
     * 根据主键查询病历文档内容
     */
    public EmrMedDoc getEmrMedDocById(@Param("pkDoc")String pkDoc); 
    
    
}
