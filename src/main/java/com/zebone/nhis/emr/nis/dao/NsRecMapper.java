package com.zebone.nhis.emr.nis.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.emr.rec.rec.EmrMedDoc;
import com.zebone.nhis.common.module.emr.rec.rec.EmrPatList;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.emr.nis.vo.NdRecordRowsVo;
import com.zebone.nhis.emr.nis.vo.RecordDtAndColTextVo;
import com.zebone.nhis.emr.nis.vo.RegisInfoParam;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.platform.modules.mybatis.Mapper;

import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * @author 
 */
@Mapper
public interface NsRecMapper{   
	//查询医嘱
	public List<Map<String,Object>> qryCnOrder(Map<String,Object> map);
	//查询带有患者信息的医嘱
	public List<Map<String,Object>> qryCnPatiOrder(String pkPv);
	/**
	 * 查询出量总和
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOutValOracle(Map<String,Object> paramMap);
	/**
	 * 查询出量总和
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryOutValSqlServer(Map<String,Object> paramMap);
	/**
	 * 查询入量总和--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryInValOracle(Map<String,Object> paramMap);
	
	/**
	 * 查询出量总和--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryToValOracle(Map<String,Object> paramMap);
	
	
	
	/**
	 * 查询体温最大最小值--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryTwValOracle(Map<String,Object> paramMap);
	
	/**
	 * 查询呼吸最大最小值--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryHxValOracle(Map<String,Object> paramMap);
	
	/**
	 * 查询心率最大最小值--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryXlValOracle(Map<String,Object> paramMap);
	
	/**
	 * 查询Spo最大最小值--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> querySpoValOracle(Map<String,Object> paramMap);
	/**
	 * 查询入量总和--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryInValSqlServer(Map<String,Object> paramMap);
	
	/**
	 * 查询病历行记录
	 * @param pkRecord dateBegin dateEnd
	 * @return
	 */
	public List<NdRecordRowsVo> queryRecordRowList(Map<String,String> param);
	
	/**
	 * 查询病历列记录
	 * @param pkRecord dateBegin dateEnd
	 * @return
	 */
	public List<NdRecordDt> queryRecordColDtList(Map<String,String> paramMap);
	
	/**
	 * 查询病历列记录
	 * @param pkRecord dateBegin dateEnd
	 * @return
	 */
	public List<NdRecordDt> queryRecordColDtListNew(Map<String,String> paramMap);
	/**
	 * 查询病历列记录
	 * @param
	 * @return
	 */
	public List<RegisInfoParam> queryRegisInfo(String pkPv);
	
	 /**
     * 根据主键查询病历文档内容
     * @param pkRec
     * @return
     */
    public EmrMedDoc queryEmrMedDoc(@Param("pkDoc")String pkDoc);
    
	public List<RecordDtAndColTextVo> queryNdRecordAndColText(@Param("pkRecord")String pkRecord, @Param("pkRecordrow")String pkRecordrow);
	
	/**
	 * 根据产房查询患者列表
	 * @param paramMap
	 * @return
	 */
	public List<EmrPatList> queryPatListByLaborDept(Map<String,String> paramMap);
	
	/**
	 * 查询分娩记录
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryPvLaborRec(String pkPv);
	
	/**
	 * 查询新生儿记录
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryPvInfant(Map<String,Object> param);
	
	/**
	 * 查询新生儿记录--评分
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPvInfantGrade(String pkInfant);
}

