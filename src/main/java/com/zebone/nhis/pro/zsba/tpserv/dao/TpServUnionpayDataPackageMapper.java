package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServUnionpayDataPackage;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServUnionpayDataPackageMapper{   
    /**
     * 根据主键查询
     */
    public TpServUnionpayDataPackage getTpServUnionpayDataPackageById(@Param("pkDataBillP")String pkDataBillP); 

    /**
     * 查询出所有记录
     */
    public List<TpServUnionpayDataPackage> findAllTpServUnionpayDataPackage();    
    
    /**
     * 保存
     */
    public int saveTpServUnionpayDataPackage(TpServUnionpayDataPackage tpServUnionpayDataPackage);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServUnionpayDataPackage(TpServUnionpayDataPackage tpServUnionpayDataPackage);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServUnionpayDataPackage(@Param("pkDataBillP")String pkDataBillP);
}

