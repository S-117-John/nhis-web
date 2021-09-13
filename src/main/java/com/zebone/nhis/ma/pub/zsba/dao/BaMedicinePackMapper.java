package com.zebone.nhis.ma.pub.zsba.dao;

import com.zebone.nhis.ma.pub.zsba.vo.OrderExVo;
import com.zebone.nhis.ma.pub.zsba.vo.PackPdMedVo;
import com.zebone.nhis.scm.pub.vo.PdDeDrugVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaMedicinePackMapper {

	List<PackPdMedVo> qryPackPdVoList(@Param("pdDeDrugVos") List<PdDeDrugVo> pdDeDrugVos);

	/**
	 * 查询未发送包药机记录
	 *
	 * @param qryParam
	 * @return
	 */
	 List<PdDeDrugVo> queryPdDeDrugVo(Map<String, Object> qryParam);

	List<OrderExVo> queryExMedBag(Map<String, Object> qryParam);

	public  List<OrderExVo> queryExMedBagDetial(String codeBag);

    List<Map<String, Object>> querySendDrugAgainData(Map<String, Object> qryParam);
}
