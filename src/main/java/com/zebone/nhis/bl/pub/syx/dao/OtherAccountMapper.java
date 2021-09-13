package com.zebone.nhis.bl.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.bl.pub.syx.vo.OtherChargeRecordVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherReferencePdAndItemVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherRelatedOrdersVo;
import com.zebone.nhis.bl.pub.syx.vo.OtherVo;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface OtherAccountMapper {

	/**
	 * 查询关联医嘱
	 * 
	 * @param pkpv
	 * @param pkdept
	 * @return
	 */
	public List<OtherRelatedOrdersVo> qryRelatedOrdersList(
			@Param(value = "pkpv") String pkpv,
			@Param(value = "pkdept") String pkdept);

	/**
	 * 获取药品和收费项目参照
	 * 
	 * @param pkhp
	 * @param pkOrg
	 * @param keyword
	 * @return
	 */
	public List<OtherReferencePdAndItemVo> qryReferencePdAndItemList(
			@Param(value = "pkhp") String pkhp,
			@Param(value = "pkOrg") String pkOrg,
			@Param(value = "keyword") String keyword);

	/**
	 * 查询收费记录
	 * @param pkdeptcg
	 * @param codeip
	 * @param keyword
	 * @param pkitemcate
	 * @param datecg
	 * @return
	 */
	public List<OtherChargeRecordVo> qryOtherChargeRecordList(OtherVo otherVo);
	
	/**查询科室常用项目*/
	public List<Map<String,Object>> qryComItemList(Map<String,Object> paramMap);

	/**
	 * 门诊收费记录按需查询
	 * @param otherVo
	 * @return
	 */
	List<OtherChargeRecordVo> qryOpOtherChargeRecordList(OtherVo otherVo);
}
