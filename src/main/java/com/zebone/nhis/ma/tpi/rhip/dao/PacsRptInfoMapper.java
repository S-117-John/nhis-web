package com.zebone.nhis.ma.tpi.rhip.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.common.module.ma.tpi.rhip.PacsRptInfo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * @author 
 */
/**
 * 检查报告mapper
 * @author chengjia
 *
 */
@Mapper
public interface PacsRptInfoMapper{   
    /**
     * 根据主键查询
     */
    public PacsRptInfo getPacsRptInfoById(); 

    /**
     * 查询出所有记录
     */
    public List<PacsRptInfo> findAllPacsRptInfo();    
    
    /**
     * 保存
     */
    public int savePacsRptInfo(PacsRptInfo pacsRptInfo);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updatePacsRptInfo(PacsRptInfo pacsRptInfo);
    
    /**
     * 根据主键删除
     */
    public int deletePacsRptInfo();
    
    /**
     * 查询检查报告信息
     * @param map(hospitalCardid病人id/clinicHospitalno住院号)
     * @return
     */
    public List<PacsRptInfo> queryPacRptInfo(Map map);
    

}

