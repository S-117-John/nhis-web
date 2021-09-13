package com.zebone.nhis.ma.pub.platform.send.impl.syx.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ConsisPrescDtlvw;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.ConsisPrescMstvw;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DataPrescription;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DataPrescriptionDetail;
import com.zebone.nhis.ma.pub.platform.syx.vo.scmhr.DcDictDrug;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 处理scm下的服务接口
 * @author jd
 *
 */
@Mapper
public interface SyxPlatFormSendScmMapper {
	/**
	 * 查询门诊签到明细处方信息
	 * @param pkPreses
	 * @return
	 */
	public List<ConsisPrescMstvw> qryPresInfo(List<String> pkPreses);
	
	/**
	 * 查询门诊处方明细数据
	 * @param pkPreses
	 * @return
	 */
	public List<ConsisPrescDtlvw> qryPresDtInfo(List<String> pkPreses);
	
	/**
	 * 查询以开始发药/和可以结束发药的处方信息
	 * @param paramMap
	 * @return
	 */
	public List<ConsisPrescMstvw> qryStartPresInfo(Map<String, Object> paramMap);
	
	/**
	 * 上传处方信息至平台(HIP)
	 * @param pkPreses
	 * @return
	 */
	public List<DataPrescription> upPresInfoToHip(List<String> pkPresocces);
	
	/**
	 * 上传处方明细信息至平台（HIP）
	 * @param pkPresocces
	 * @return
	 */
	public List<DataPrescriptionDetail> upPresDtInfoToHip(List<String> pkPresocces);
	
	/**
	 * 上传退费的处方信息（全退）
	 * @param paramMap
	 * @return
	 */
	public List<ConsisPrescMstvw> qryReturnPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询药品信息
	 * @param paramMap
	 * @return
	 */
	public List<DcDictDrug> qryPdDictInfo(Map<String,Object> paramMap);
}
