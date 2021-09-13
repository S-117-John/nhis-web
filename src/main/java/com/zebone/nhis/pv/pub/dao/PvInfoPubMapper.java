package com.zebone.nhis.pv.pub.dao;

import com.zebone.nhis.pv.pub.vo.PageQryAuditParam;
import com.zebone.nhis.pv.pub.vo.PageQryPvParam;
import com.zebone.nhis.pv.pub.vo.PvEnCounterAuditVo;
import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 获取就诊信息
 * @author yangxue
 *
 */
@Mapper
public interface PvInfoPubMapper {
	/**
	 * 查询患者就诊信息(医保需要)
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> getPvOutInfoForIns(Map<String,Object> paramMap);
	/**
	 * 查询患者分类
	 * @param pkPv
	 * @return
	 */
	public List<Map<String,Object>> getPiCateCodeByPv(@Param("pkPv")String pkPv);
	
	/** 获取住院患者信息列表-分页*/
	public List<PvEncounterListVo> getPvEncounterVoList(PageQryPvParam qryparam);
	
	public List<PvEnCounterAuditVo> getSearchPatiAuditInfoPaging(PageQryAuditParam qryparam);

    public String getCodeiP();

	/**
	 * 获取科室拓展属性
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryDepartmentDictAttr(Map<String,Object> map);

}
