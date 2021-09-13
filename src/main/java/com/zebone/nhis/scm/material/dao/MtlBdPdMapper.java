package com.zebone.nhis.scm.material.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.scm.pub.BdPdAs;
import com.zebone.nhis.scm.material.vo.BdPdAttVo;
import com.zebone.nhis.scm.material.vo.BdPdBaseVo;
import com.zebone.nhis.scm.material.vo.BdPdStoreInfo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MtlBdPdMapper {
	/**
	 * 查询物品分类列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryBdPdcateList(Map<String,Object> map);
	/**
	 * 查询物品基本信息
	 * @param map
	 * @return
	 */
	public List<BdPdBaseVo> queryPdBaseList(Map<String,Object> map);
	/**
	 * 查询物品 - 别名列表
	 * @param map
	 * @return
	 */
	public List<BdPdAs> queryBdPdAsList(Map<String,Object> map);
	/**
	 * 查询物品 - 附加属性【新增】
	 * @param map
	 * @return
	 */
	public List<BdPdAttVo> queryBdPdDefNew(Map<String,Object> map);
	/**
	 * 查询物品 - 附加属性【新增 + 已存在】
	 * @param map
	 * @return
	 */
	public List<BdPdAttVo> queryBdPdDef(Map<String,Object> map);
	/**
	 * 查询物品 - 仓库物品【新增】
	 * @param map
	 * @return
	 */
	public List<BdPdStoreInfo> queryBdPdStoreNew(Map<String,Object> map);
	
	/**
	 * 查询物品 - 仓库物品【新增 + 已存在】
	 * @param map
	 * @return
	 */
	public List<BdPdStoreInfo> queryBdPdStore(Map<String,Object> map);
	
	/**
	 * 获取物品包装数据
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> getPdConvert(String pkPd);
	
	/**
	 * 获取仓库物品数据
	 * @param pkPd
	 * @return
	 */
	public List<Map<String,Object>> getPdStore(String pkPd);
}
