package com.zebone.nhis.compay.pub.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.compay.pub.vo.InsCommonMedical;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedicalInsuranceMapper {

	/**
	 * 查询通用医保表数据,分页查询
	 * @return
	 */
	public List<Map<String, Object>> selectCommonMedical(InsCommonMedical insCommonMedical);
	
	public String getCodeiP();
	
	int updatrPiMaster(PiMaster master);
}
