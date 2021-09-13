package com.zebone.nhis.ma.pub.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.syx.vo.AtfYpPageNo;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxx;
import com.zebone.nhis.ma.pub.syx.vo.AtfYpxxSyx;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SendDrugsToMachineMapper {

	/**
	 * 查询组转包药机数据的参数
	 * @param string
	 * @return
	 */
	public Map<String, Object> qryParam(String pkPdde);

	/**
	 * 查询包药机主表
	 * @param param
	 * @return 
	 */
	public AtfYpPageNo generateRecord(Map<String, Object> param);

	/**
	 * 查询包药机子表
	 * @param param
	 * @return 
	 */
	public List<AtfYpxxSyx> generateRecordDetail(Map<String, Object> param);
	
}
