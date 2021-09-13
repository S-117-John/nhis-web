package com.zebone.nhis.webservice.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.webservice.vo.EmrOrdListVo;
import com.zebone.nhis.webservice.vo.EmrPatListVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 病历书写-公共-mapper
 * @author chengjia
 */
@Mapper
public interface EmrPubRecMapper{   
    /**
     * 根据条件查询病历文档列表
     * @param map codeIp住院号/codePi就诊ID/codePv就诊流水号/ipTimes住院次数
     * @return
     */
    public List<EmrMedRec> queryPatMedRecList(Map map); 
    
    /**
	 * 查询住院病人列表
	 * @param deptCode科室编码/deptId科室ID
	 * @param 
	 * @return
	 */
	public List<EmrPatListVo> queryPatList(Map map);
	
    /**
	 * 查询住院病人列表
	 * @param codeIp住院号/codePi就诊ID/codePv就诊流水号/ipTimes住院次数
	 * @param 
	 * @return
	 */
	public List<EmrOrdListVo> queryOrdList(Map map);

}
