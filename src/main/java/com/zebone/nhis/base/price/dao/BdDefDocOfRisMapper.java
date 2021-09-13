package com.zebone.nhis.base.price.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BdDefDocOfRisMapper {

	/**
	 * 查询检验容器|样本 - 码表数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryDefdocOfRis(Map<String,Object> paramMap);

	/**
	 * 查询检验容器|样本收费项目数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryItemDefdocOfRis(Map<String,Object> paramMap);

	/**
	 * 判断检验容器|样本的编码是否唯一
	 * @param map{codeDefdoclist:分类主键,code:编码,name:名称,pkDefdoc:主键}
	 * @return
	 */
	public Integer queryDefdocCntByCon(Map<String,Object> map);
	
	/**
	 * 删除检验容器|样本 - 码表数据
	 * @param map{codeDefdoclist:码表类型,pkDefdoc：码表记录主键}
	 * @return
	 */
	public Integer deleteDefdocOfRis(Map<String,Object> map);

	/**
	 * 删除检验容器|样本收费项目数据
	 * @param map{codeDefdoc:容器|样本编码,codeDefdoclist:码表类型,pkItemdefdoc：码表对应收费记录主键}
	 * @return
	 */
	public Integer deleteItemDefdocOfRis(Map<String,Object> map);
	
}
