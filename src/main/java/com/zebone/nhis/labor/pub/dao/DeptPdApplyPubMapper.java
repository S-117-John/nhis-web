package com.zebone.nhis.labor.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeptPdApplyPubMapper {
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

	/**
	 * 查询可生成请领的医嘱执行单信息(退费的)
	 * @param map
	 * @return
	 */
	public List<GeneratePdApExListVo> qryUnPdApExBackList(Map<String,Object> map);

    Integer queryCgipCount(@Param("exPdapdts") List<ExPdApplyDetail> exPdapdts);

    void updatePkPdApdt(@Param("exPdapdts") List<ExPdApplyDetail> exPdapdts);

}
