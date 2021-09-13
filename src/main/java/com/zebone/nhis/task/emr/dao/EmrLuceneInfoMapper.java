package com.zebone.nhis.task.emr.dao;

import com.zebone.nhis.task.emr.vo.EmrLuceneInfo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * @author 
 */
@Mapper
public interface EmrLuceneInfoMapper {
    /**
     * 根据主键查询
     */
    EmrLuceneInfo getEmrLuceneInfoById(@Param("pkLucene") String pkLucene);

    /**
     * 查询出所有记录
     */
    public List<EmrLuceneInfo> findAllEmrLuceneInfo();    
    
    /**
     * 保存
     */
    public int saveEmrLuceneInfo(EmrLuceneInfo emrLuceneInfo);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateEmrLuceneInfo(EmrLuceneInfo emrLuceneInfo);
    
    /**
     * 根据主键删除
     */
    public int deleteEmrLuceneInfo(@Param("pkLucene") String pkLucene);
}

