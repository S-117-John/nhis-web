package com.zebone.nhis.bl.ipcg.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.srv.BdOrd;
import com.zebone.nhis.common.module.base.bd.srv.BdOrdtype;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface MedTechDeptProMapper {

	/**
	 * 查询当前科室的项目
	 * @param map
	 * @return
	 */
	List<BdOrd> qryPro(Map<String,Object> map);
	
	/**
	 * 2.5.7.5.2.	删除当前科室项目
	 * @param map
	 * @return
	 */
	int delByOrdDept(Map<String,Object> map);
	
	/**
	 * 2.5.7.5.3.	查询医嘱类型
	 * @param map
	 * @return
	 */
	List<BdOrdtype> qryOrdtype();
	
	/**
	 * 2.5.7.5.4.	查询可导入的医嘱项目
	 * @param map
	 * @return
	 */
	List<BdOrd> qryOrd(Map<String,Object> map);
}
