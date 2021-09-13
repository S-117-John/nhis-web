package com.zebone.nhis.pv.hd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.pv.hd.vo.PiHdVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 透析登记服务
 * @author yangxue
 *
 */
@Mapper
public interface HdPvMapper {
	
    //查询透析档案
	public List<Map<String,Object>> queryHdPiMasterPkPi(Map<String,String> paramap);

	//查询就诊记录
	public List<Map<String,Object>> queryAcographyPkPi(Map<String,Object> paramap);

	 /**
	  * 查询透析排班信息
	  * @param param{pkOrg}
	  * @return
	  */
	 public List<Map<String,Object>> queryHdSch(Map<String,Object> param);
	  /**
	   * 查询透析患者信息
	   * @param param{}
	   * @return
	   */
	 public List<PiHdVo>  queryHdPiMaster(Map<String,Object> param);
	 
	//查询当前患者的透析治疗记录
	public List<Map<String,Object>> queyrPiRecord(Map<String,Object> map);

	//查询患者就诊信息
	public List<Map<String,Object>> queryPiVis(Map<String,Object> map);
	
	//查询患者就诊信息（含结束治疗）
	public List<Map<String,Object>> queryPiVisOrEnd(Map<String,Object> map);
	
	//更新患者信息
	public void updatePiMaster(PiMaster pi);

	//查询最近一次透析治疗记录
	public Map<String, Object> queryLatelyTreatmentRecord(Map<String, Object> paramMap);

}
