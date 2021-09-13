package com.zebone.nhis.base.bd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.scm.pub.BdIndtype;
import com.zebone.nhis.common.module.scm.pub.BdPdIndhp;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdindMapper {
	
	public List<BdIndtype> qryCatalog();

	/**
	 * 查询机构下面的适应症用药
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryPdInds(Map<String,Object> paramMap);
	
	/**
	 * 根据适应症用药主键查询相关药品
	 * @param pkPdind
	 * @return
	 */
	public List<Map<String,Object>> qryPds(String pkPdind);
	
	/**
	 * 查询适应症用药关联的可使用医保
	 * @param pkPdind
	 * @return
	 */
	public List<BdPdIndhp> qryHp(String pkIndtype);
	
	/**
	 *删除关联的医保 
	 */
	public void delHp(String pkIndtype);
	
	/**
	 * 删除目录类别下的适应症关联药品 
	 * @param map
	 */
	public void delPdByType(String codeIndtype);
	
	/**
	 * 根据主键删除适应症用药
	 * @param map
	 */
	public void delPdInd(String pkPdind);
	/**
	 * 删除适应症用药明细
	 * @param map
	 */
	public void delPd(String pkPdind);
	
	/**
	 * 删除目录类别下的适应症信息
	 * @param map
	 */
	public void delPdIndByType(String codeIndtype);
	
	/**
	 * 删除目录类别 
	 * @param pkIndtype
	 */
	public void delIndtype(String pkIndtype);
	
	/**
	 * 适应症用药关联医嘱 
	 * @param map
	 */
	public void qryDesc(Map map);

	public List<Map<String, Object>> qryHps();
	
	public Integer countName(String nameType);
	
	public Integer countCode(String codeType);
}
