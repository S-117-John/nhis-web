package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.tpserv.vo.TpServUnionpayTrading;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServUnionpayTradingMapper{   
    /**
     * 根据主键查询
     */
    public TpServUnionpayTrading getTpServUnionpayTradingById(@Param("pkUnionpayTrading")String pkUnionpayTrading); 

    /**
     * 查询出所有记录
     */
    public List<TpServUnionpayTrading> findAllTpServUnionpayTrading();    
    
    /**
     * 保存
     */
    public int saveTpServUnionpayTrading(TpServUnionpayTrading tpServUnionpayTrading);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServUnionpayTrading(TpServUnionpayTrading tpServUnionpayTrading);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServUnionpayTrading(@Param("pkUnionpayTrading")String pkUnionpayTrading);
    
	public List<TpServUnionpayTrading> getServUnionpayTradingList(TpServUnionpayTrading master);
	
	//List<Map<String, Object>> getSUTMerchantidList(String transdate);
}

