package com.zebone.nhis.pro.sd.emr.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.pro.sd.emr.vo.EmrDataQueryVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 深大项目病案数据上传接口
 * @author jd_em
 *
 */
@Mapper
public interface SughEmrDataUploadMapper {
	/**
	 * 查询病案数据
	 * @param queryVo
	 * @return
	 */
	public List<Map<String,Object>> queryEmrDataUpList(EmrDataQueryVo queryVo);
	
	/**
     * 科室质控点击完成时调用省病案接口存储过程
     * @param paramMap
     */
    public int updateProc(Map<String,Object> param);
}
