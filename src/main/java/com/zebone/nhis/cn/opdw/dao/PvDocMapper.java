package com.zebone.nhis.cn.opdw.dao;

import java.util.List;


import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.cn.opdw.PvDoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PvDocMapper {
  
    /**
    * 保存(病历存储库)
    */
   public int savePvDocEmr(PvDoc pvDoc);

   /**
    * 根据主键更新(病历存储库)
    */
   public int updatePvDocEmr(PvDoc pvDoc);
   
    /**
     * 根据主键查询病历文档内容(病历存储库)
     * @param pkRec
     * @return
     */
    public PvDoc queryPvDocByIdEmr(@Param("pkPvDoc")String pkPvDoc,@Param("dbName")String dbName);
    
    /**
     * 根据主键删除(病历库)
     */
    public int deletePvDocEmr(@Param("pkPvDoc")String pkRec,@Param("dbName")String dbName);
    
    /**
     * 根据主键查询病历数据(数据存储库)
     * @param pks
     * @return
     */
    public List<PvDoc> queryDocListEmrByPks(@Param("dbName")String dbName,@Param("list")List<String> pkPvDocs);
    
}