package com.zebone.nhis.cn.opdw.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.opdw.vo.PdZeroVo;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.cn.opdw.vo.SyxCnOrderVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SyxCnOpOrderMapper {

	//获取当前就诊记录的药品医嘱
	public List<SyxCnOrderVo> qryPdOrders(Map<String,Object>map);
	
	//获取处方附加收费列表 old:004003007006 
	public List<Map<String,Object>> qryPresAddItems(Map<String,Object>map);
	
	//获得患者处方明细列表
	public List<Map<String,Object>> getPrescriptionDetail(@Param(value="pkPres")String pkPres,@Param(value="pkDeptExec")String pkDeptExec,@Param(value="pkDeptExChineseDrug")String pkDeptExChineseDrug);

	/**
	 * 查询处方限制天数
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryDays(Map<String, Object> map);

	/**
	 * 得到限制的数量和单位
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryDaysAndQuan(Map<String, Object> map);
	//药品零元购属性
	List<Map<String,Object>> qryPdZero(SyxCnOrderVo vo);
	//药品零元购属性s
	List<Map<String,Object>> qryPdsZero(Map<String,Object> param);

	//零元购药品可以开立的最大数量--批量
	List<PdZeroVo> qryPdsMaxSize(Map<String,Object> param);
	//零元购药品可以开立的最大数量
	List<Map<String,Object>> qryPdMaxSize(Map<String,Object> param);
	//查询患者已开立的数量
	List<Map<String,Object>> qryPiSize(Map<String,Object> param);

	//查询患者已开立的零元购药品
	List<Map<String,Object>> qryPiZeroOrd(Map<String,Object> param);

    List<Map<String, String>> getFlagSettle(@Param("presNo") String presNo);

}
