package com.zebone.nhis.base.price.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.bd.price.BdHpDiagdivItemcate;
import com.zebone.nhis.common.module.base.bd.price.BdHpDiagdiv;
import com.zebone.nhis.common.module.base.bd.srv.BdItemcate;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdHpDiagDivMapper {
	/**
	 * 根据诊断和患者信息查询是否有符合的单病种规则
	 * @param map{
	 * 			pk_hp：医保计划 ；
				}
	 * @return
	 */
	public List<Map<String,Object>> queryBdHpDiagDiv(Map<String,Object> map);
	
	/**
	 * 判断单病种费用分类中是否存在某单病种数据
	 * @param map{pkTotaldiv:单病种定义外键}
	 * @return
	 */
	public Integer isExistBdIteamcate(Map<String,Object> map);
	
	/**
	 * 删除单病种定义
	 * @param map{pkTotaldiv:单病种定义外键}
	 * @return
	 */
	public Integer deleteHpDiagDiv(Map<String,Object> map);
	
	/**
	 * 删除单病种费用控制
	 * @param map
	 * @return
	 */
	public Integer deleteDiagItemcate(Map<String,Object> map);
	
	/**
	 * 根据诊断和患者信息查询是否有符合的单病种规则
	 * @param map{
	 * 			pk_hp：医保计划 ；
 				diagcode：疾病编码
				}
	 * @return
	 */
	public List<Map<String,Object>> getBdHpDiagdiv(Map<String,Object> map);
	
	/**
	 * 查询医保计划下的单病种设置 
	 * @param 
	 * @param 
	 * @return
	 */
	public List<BdHpDiagdiv> queryHpDiagDivList(String pkHp);
	
	/**
	 * 查询单病种下的  费用分类列表 
	 * @param 
	 * @param 
	 * @return
	 */
	public List<BdHpDiagdivItemcate> queryHpDiagdivItemcate(String pkTotaldiv);

	
}
