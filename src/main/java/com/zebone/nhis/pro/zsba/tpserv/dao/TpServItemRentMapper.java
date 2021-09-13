package com.zebone.nhis.pro.zsba.tpserv.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.nhis.pro.zsba.tpserv.vo.TpServItemRent;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface TpServItemRentMapper{   
    /**
     * 根据主键查询
     */
    public TpServItemRent getTpServItemRentById(@Param("pkRent")String pkRent); 

    /**
     * 查询出所有记录
     */
    public List<TpServItemRent> findAllTpServItemRent();    
    
    /**
     * 保存
     */
    public int saveTpServItemRent(TpServItemRent tpServItemRent);
    
    /**
     * 根据主键更新（参数对象中的主键将作为更新条件）
     */
    public int updateTpServItemRent(TpServItemRent tpServItemRent);
    
    /**
     * 根据主键删除
     */
    public int deleteTpServItemRent(@Param("pkRent")String pkRent);
    
	public List<TpServItemRent> getServItemRentList(TpServItemRent master);
	
	public  List<Map<String,Object>> getHgfInfo(Map<String,Object> map);
	
	public  Map<String,Object> getHgfNum(Map<String,Object> map);
	
	public  Map<String,Object> getPatientInfo(Map<String,Object> map);
	
	/** 根据患者就诊信息查询诊断信息vo列表 */
	List<PibaseVo> getPibaseVoList(PvEncounter pvEncounter);
	
	/**
	 * 获取新生儿科未结算费用明细
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getNewbornPediatricsData(Map<String,Object> map);
	
	/**
	 * 获取新生儿科未结算总费用
	 * @param map
	 * @return
	 */
	public  List<Map<String,Object>> getNewbornPediatricsTotalCost(Map<String,Object> map);
}

