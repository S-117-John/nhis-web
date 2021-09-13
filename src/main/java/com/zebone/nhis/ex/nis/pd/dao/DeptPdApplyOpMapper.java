package com.zebone.nhis.ex.nis.pd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 科室领药—门诊
 * @author jd
 *
 */
@Mapper
public interface DeptPdApplyOpMapper {
	  /**
     * 查询已记费未退费医嘱执行单
     * @param paramMap
     * @return
     */
	public List<GeneratePdApExListVo> queryCgExlist(Map<String,Object> paramMap);
	/**
	 * 根据发药科室查询物品信息
	 * @param paramMap
	 * @return
	 */
	public  Map<String,Object>  getPdStoreInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询请领单列表
	 * @param map{pkDeptNs,dateBegin,dateEnd}
	 */
	public List<Map<String,Object>> queryPdApply(Map<String,Object> map);
	
	/**
	 * 查询请领单列表
	 * @param map{pkPdap,flagDe,flagPivas,pdname，euAlways}
	 */
	public List<Map<String,Object>> queryPdApDetail(Map<String,Object> map);
	
	/**
	 * 查询可生成请领的医嘱执行单信息
	 * @param map
	 * @return
	 */
	public List<GeneratePdApExListVo> qryUnPdApExList(Map<String,Object> map);
}
