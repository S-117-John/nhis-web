package com.zebone.nhis.ma.pub.platform.sd.dao;

import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 	接收sql
 * @author maijiaxing
 *
 */
@Mapper
public interface SDReceiveMapper {
	
	/**
	 * 通过执行单主键查询患者，医嘱，执行单信息
	 * @param pkExoccs
	 * @return
	 */
	List<ExlistPubVo> queryExByPk(List<String> pkExoccs);
	
	/**
	 * 通过医嘱主键查询检查明细信息
	 * @param pkCnords
	 * @return
	 */
	List<Map<String, Object>> queryDtList(List<String> pkCnords);
	/**
	 * 通过申请单号查询医嘱
	 * @param codeApplys
	 * @return
	 */
	List<Map<String,Object>> queryOrderByCodeApply(List<String> codeApplys);
	
	/**
	 * 查询患者基本信息
	 * @param paramMap
	 * @return
	 */
	List<Map<String,Object>> queryPiMaster(Map<String,Object> paramMap);
	
	/**
	 * 查询检查申请单信息
	 * @return
	 */
	List<CnRisApply> queryRisApply(@Param("set") Set<String> ordsnSet);
	
	 /**
     * 查询多个条件医嘱信息
     * @param map
     * @return
     */
    List<Map<String,Object>> queryReqOrdList(Map<String,Object> map);

    /**
     * 查询微信患者信息
     * @param map
     * @return
     */
    List<Map<String,Object>> queryWechatPatList(Map<String,Object> map);


	/**
	 * 查询停用 医嘱
	 * @param map
	 */
	List<Map<String,Object>> queryBdOrd(Map<String,Object> map);
}
