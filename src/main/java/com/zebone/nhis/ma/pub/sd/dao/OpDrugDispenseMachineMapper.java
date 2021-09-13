package com.zebone.nhis.ma.pub.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.ma.pub.sd.vo.OpDrugDispensePresInfo;
import com.zebone.nhis.ma.pub.sd.vo.OpPdBaseStoreInfo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊发药机接口
 * @author jd_em
 *
 */
@Mapper
public interface OpDrugDispenseMachineMapper {

	/**
	 * 查询待上传的处方信息
	 * @param pkPresoccs
	 * @return
	 */
	public List<OpDrugDispensePresInfo> queryPresInfoUpload(Map<String,Object> paramMap);
	
	
	/**
	 * 上传仓库药品数据
	 * @param pkPdStore
	 * @return
	 */
	public List<OpPdBaseStoreInfo> queryPdStoreInfo(Map<String,Object> paramMap);
}
