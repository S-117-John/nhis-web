package com.zebone.nhis.ma.pub.platform.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 重发消息
 * @author JesusM
 *
 */
@Mapper
public interface SDMsgResendMapper {
	
	 /**
     * 查询保存HL7消息记录
     * @param map
     * @return
     */
    public List<SysMsgRec> queryMsgList(Map<String,Object> paramMap);
    
	/**
	 * 查询医嘱信息 （医嘱号）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOrder(Map<String,Object> paramMap);
	
	/**
     * 查询患者信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPatient(Map<String,Object> paramMap);
	
	/**
	 * 根据医嘱号查询住院医嘱信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOrderByOrdsn(Map<String, Object> paramMap);
	
	/**
	 * 根据查询手术信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryOperationByOrdsn(Map<String,Object> paramMap);
	
	/**
	 * 根据查询检验信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryLisByOrdsn(Map<String,Object> paramMap);
	
	/**
	 * 根据查询检查信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryRisByOrdsn(Map<String,Object> paramMap);
	
    
	

}
