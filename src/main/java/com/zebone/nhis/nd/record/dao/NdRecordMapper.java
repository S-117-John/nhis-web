package com.zebone.nhis.nd.record.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.nd.record.NdRecord;
import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.common.module.nd.record.NdRecordFd;
import com.zebone.nhis.common.module.nd.record.NdRecordTitle;
import com.zebone.nhis.nd.pub.vo.NdRecordRowVo;
import com.zebone.nhis.nd.record.vo.NdRecordVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface NdRecordMapper {
	/**
	 * 保存护理文书流文件
	 * @param fd
	 * @return
	 */
	public int saveRecordFd(NdRecordFd fd);
	/**
	 * 更新护理文书流文件
	 * @param fd
	 * @return
	 */
	public int updateRecordFd(NdRecordFd fd);
	/**
	 * 保存护理文书
	 * @param record
	 * @return
	 */
	public int saveRecord(NdRecord record);
	/**
	 * 更新护理文书
	 * @param record
	 * @return
	 */
	public int updateRecord(NdRecord record);
   /**
	 * 查询病历格式
	 * @param pkRecord
	 * @return
	 */
	public NdRecordVo queryRecordContent(@Param("pkRecord")String pkRecord);
	
	public NdRecordVo queryRecordContentNoDocData(@Param("pkRecord")String pkRecord);
	
	/**
	 * 查询病历表头数据
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordTitle> queryRecordTitleData(@Param("pkRecord")String pkRecord);
	
	/**
	 * 查询病历行记录
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordRowListOracle(Map<String,Object> param);
	
	/**
	 * 查询病历行记录
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordRowVo> queryRecordRowListSqlServer(Map<String,Object> param);
	/**
	 * 查询生成产程图的行记录
	 * @param pkRecord
	 * @return
	 */
	//public List<NdRecordRowVo> queryCheckRecordRowList(@Param("pkRecord")String pkRecord);
	/**
	 * 查询病历流文件
	 * @param pkRecord
	 * @return
	 */
	public List<NdRecordFd> queryRecordFdList(@Param("pkRecord")String pkRecord);
	
	/**
	 * 查询出量总和
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryOutValOracle(Map<String,Object> paramMap);
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
	 * 查询入量总和--oracle
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object> queryInValSqlServer(Map<String,Object> paramMap);
}
