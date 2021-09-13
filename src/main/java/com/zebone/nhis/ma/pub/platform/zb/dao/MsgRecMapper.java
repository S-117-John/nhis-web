package com.zebone.nhis.ma.pub.platform.zb.dao;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ma.msg.SysMsgRec;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * TODO
 * @author 
 */
@Mapper
public interface MsgRecMapper{   

    /**
     * 查询HL7消息记录
     * @param map
     * @return
     */
    public List<SysMsgRec> queryMsgList(Map<String,Object> map);
    
    /**
     * 查询患者信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPatListIp(Map<String,Object> map);  
    
    /**
     * 查询患者信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryPatListOp(Map<String,Object> map);
    
    
    /**
     * 查询申请医嘱信息
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryReqOrdList(Map<String,Object> map);

    
    /**
     *根据pk_pi查询患者信息
     * pi_master
     * @param map
     * @return
     */
    public Map<String,Object> queryPiMaster(Map<String,Object> map);
    /**
     * 
     * @param map
     * @return
     */
	public List<Map<String, Object>> queryPatList(Map<String, Object> map);    
}

