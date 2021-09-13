package com.zebone.nhis.scm.st.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.scm.pub.vo.PdStDtVo;
import com.zebone.nhis.scm.st.vo.PdPayVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface PdPayMapper {
   /***
	 * 供应商列表
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> querySupplyerList(@Param("pkOrg") String pkOrg);
	/***
	* 查询待付款记录
	* @param map
	* @return
	*/
	public List<PdStDtVo> queryToPayList(@Param("pkOrg") String pkOrg,@Param("pkSupplyer") String pkSupplyer);
    /**
     * 查询已付款记录
     * @param map
     * @return
     */
    public List<PdPayVo> queryPayList(Map<String,Object> map);
    /**
     * 查询付款明细
     * @param pkPdpay
     * @return
     */
    public List<PdStDtVo> queryStDtList(@Param("pkPdpay") String pkPdpay);
}
