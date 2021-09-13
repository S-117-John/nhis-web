package com.zebone.nhis.pro.zsba.compay.ins.pub.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.compay.ins.pub.vo.InsZsPubCertOut;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
 @Mapper
public interface InsPubCertOutMapper{   
    /**
     * 根据主键查询
     */
    public InsZsPubCertOut getInsCertOutById(@Param("pkCertout")String pkCertout); 

    /**
     * 查询出所有记录
     */
    public List<InsZsPubCertOut> findAllInsCertOut();    
    
    /**
     * 保存
     */
    public int saveInsCertOut(InsZsPubCertOut insCertOut);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateInsCertOut(InsZsPubCertOut insCertOut);
    
    /**
     * 根据主键删除
     */
    public int deleteInsCertOut(@Param("pkCertout")String pkCertout);
}

