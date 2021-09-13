package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.webservice.syx.vo.scmhr.DataPrescription;
import com.zebone.nhis.webservice.syx.vo.scmhr.DataPrescriptionDetail;
import com.zebone.nhis.webservice.syx.vo.scmhr.DcDictDrug;
import com.zebone.nhis.webservice.syx.vo.scmhr.ResSubject;
import com.zebone.nhis.webservice.syx.vo.scmhr.Root;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ScmHrMapper {
	/**
	 * 药房药品货位数据查询服务
	 * @param paramMap
	 * @return
	 */
	public List<ResSubject> qryDrugSpacePosi(Map<String,Object> paramMap);
	
	/**
	 * 药房窗口数据查询
	 * @param paramMap
	 * @return
	 */
	public List<ResSubject> qryDrugWindows(Map<String,Object> paramMap);
	
	/**
	 * 查询处方数据
	 * @param paramMap
	 * @return
	 */
	public List<DataPrescription> qryDataPresInfo(Map<String,Object> paramMap);
	/**
	 * 药品字典查询服务
	 * 
	 */
	public List<DcDictDrug> qryDrugDict(Map<String,Object> paramMap);
	
	/**
	 * 查询处方明细数据
	 * @param pkPresocces
	 * @return
	 */
	public List<DataPrescriptionDetail> qryDataPrescriptionDetails(List<String> pkPresocces);

	/**
	 * 根据处方更新窗口信息
	 * @param root
	 */
	public int updateWinnoByPresNo(Root root);
	
}
