package com.zebone.nhis.ex.nis.emr.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOccDt;
import com.zebone.nhis.ex.nis.emr.vo.ExVtsOccByPv;
import com.zebone.nhis.ex.nis.emr.vo.ExVtsOccVo;
import com.zebone.nhis.ex.nis.emr.vo.NorNurseVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface VtsMapper {
   /**
    * 查询生命体征
    * @param map{pkPv,dateBegin,dateEnd}
    * @return
    */
	public List<ExVtsOccVo> queryVtsByPV(Map<String,Object> map) ;
   /**
    * 查询生命体征明细
    * @param map{pkVtsocc}
    * @return
    */
	public List<ExVtsOccDt> queryVtsDetailsByPV(Map<String,Object> map) ;
   /**
    * 查询根据具体时间，患者查询生命体征
    * @param map{pkPv,dateCur}
    * @return
    */
	public ExVtsOccVo queryVtsByPvAndDate(Map<String,Object> map) ;
  /**
    * 根据主键查询生命体征明细
    * @param map{pkVtsocc,euDateslot,hourVts}
    * @return
    */
	public List<ExVtsOccDt> queryVtsDetailsByPk(Map<String,Object> map) ;
	/**
	 * 根据日期查询病区所有患者生命体征
	 * @param map{pkDeptNs,dateCur,euDateslot,hourVts}
	 * @return
	 */
	public List<ExVtsOccVo> queryVtsByDate(Map<String,Object> map);
	/**
	 * 根据日期查询产房所有患者生命体征
	 * @param map{pkDeptNs,dateCur,euDateslot,hourVts}
	 * @return
	 */
	public List<ExVtsOccVo> queryLaborVtsByDate(Map<String,Object> map);
	
	public List<NorNurseVo> queryNorNurse(Map<String,String> map);
	
	/**
	 * 查询生命体征 - 某患者，某日期的基本体征信息
	 * @param map{pkPv,dateOcc}
	 * @return
	 */
	public List<ExVtsOccByPv> queryVtsByPvAndDateByOne(Map<String,Object> map) ;
	
}
