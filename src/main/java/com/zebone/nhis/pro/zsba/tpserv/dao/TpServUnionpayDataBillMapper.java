package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServUnionpayDataBill;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServUnionpayDataBillMapper{   
    /**
     * 根据主键查询
     */
    public TpServUnionpayDataBill getTpServUnionpayDataBillById(@Param("pkDataBill")String pkDataBill); 

    /**
     * 查询出所有记录
     */
    public List<TpServUnionpayDataBill> findAllTpServUnionpayDataBill();    
    
    /**
     * 保存
     */
    public int saveTpServUnionpayDataBill(TpServUnionpayDataBill tpServUnionpayDataBill);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServUnionpayDataBill(TpServUnionpayDataBill tpServUnionpayDataBill);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServUnionpayDataBill(@Param("pkDataBill")String pkDataBill);
}

