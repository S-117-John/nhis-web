package com.zebone.nhis.compay.pub.syx.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.compay.pub.syx.vo.CostForecastAmtVo;
import com.zebone.nhis.compay.pub.syx.vo.CostForecastItemVo;
import com.zebone.nhis.compay.pub.syx.vo.CostHpDictAttrVo;
import com.zebone.nhis.compay.pub.syx.vo.CostZtFeeVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface QryCostForecastMapper {

	/**
	 * 查询各项费用金额
	 * @param pkpv
	 * @return
	 */
	public CostForecastAmtVo qryAmtVo(String pkpv);
	
	/**
	 * 取医保目录及项目分类
	 * @param pkpv
	 * @return
	 */
	public List<CostForecastItemVo> qryCostItemVo(String pkpv);
	
	/**
	 * 查询医保计划拓展属性
	 * @param pkOrgUse
	 * @param pkDict
	 * @return
	 */
	List<CostHpDictAttrVo> queryHpDictAttr(@Param("pkOrgUse")String pkOrgUse,@Param("pkDict")String pkDict);
	
	/**
	 * 查询预交金系数
	 * @param pkpv
	 * @return
	 */
	public List<Map<String,Object>> getYjFactor(String pkpv);
	
	/**
	 * 查询在途费用药品
	 * @param pkpv
	 * @return
	 */
	public Double getZtPdFee(String pkpv);
	/**
	 * 查询在途费用非药品
	 * @param pkpv
	 * @return
	 */
	public Double getZtNPdFee(String pkpv);
	
	/**
	 * 取取在途费用明细
	 * @param pkpv
	 * @return
	 */
	public List<CostZtFeeVo> qryZtFeeVo(String pkpv);
	
}

