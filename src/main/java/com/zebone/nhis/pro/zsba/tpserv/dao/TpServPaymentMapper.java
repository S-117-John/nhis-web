package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServPayment;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServPaymentMapper{   
    /**
     * 根据主键查询
     */
    public TpServPayment getTpServPaymentById(@Param("pkPayment")String pkPayment); 

    /**
     * 查询出所有记录
     */
    public List<TpServPayment> findAllTpServPayment();    
    
    /**
     * 保存
     */
    public int saveTpServPayment(TpServPayment tpServPayment);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServPayment(TpServPayment tpServPayment);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServPayment(@Param("pkPayment")String pkPayment);
}

