package com.zebone.nhis.emr.mgr.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.mgr.EmrBorrowDoc;
import com.zebone.nhis.common.module.emr.mgr.EmrBorrowRec;
import com.zebone.nhis.common.module.emr.qc.EmrEventRec;
import com.zebone.nhis.emr.mgr.vo.EmrSealParam;
import com.zebone.nhis.emr.mgr.vo.EmrSealVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * @author 
 */
@Mapper
public interface NemsEmrMapper{   

    /**
     * 查询病人召回申请病人列表(病历状态为归档)
     * @param map
     * @return
     */
	public List<Map> listEmrPatArch(Map map);
	/**
	 * 查询病人召回申请病人列表
	 * @param map
	 * @return
	 */
	public List<Map> listEmrPat(Map map);

	public void agreeRecall(Map map);

	/**
	 * 查询封存病人
	 * @param map
	 */
	public List<EmrSealVo> searchMothPat(Map map);
	
	
	public void updateEmrSealRec(EmrSealParam emrSealParam);
	
	/**
	 * 查询需要解封的病人
	 * @param map
	 */
	public List<Map> listArchPat(Map map);
	
	/**
	 * 病历解封
	 * @param map
	 */
	public void unlockRec(Map map);
	
	
    
}

