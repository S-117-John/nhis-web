package com.zebone.nhis.pv.arr.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface QcBigScreenSetMapper {

	/**
	 * 查询屏幕信息列表
	 * @param pkOrg
	 * @return
	 */
	List<Map<String, Object>> qryScreenList(String pkOrg);

	/**
	 * 查询诊室信息列表
	 * @param pkQcscreen
	 * @return
	 */
	List<Map<String, Object>> qryScreenDuList(String pkQcscreen);

	/**
	 * 查询导入信息列表
	 * @param pkOrg
	 * @return
	 */
	List<Map<String, Object>> qryExportList(String pkOrg);

	/**
	 * 删除诊室
	 * @param listPk PK_QCSCREENDU集合
	 * @return
	 */
	int delScreenDuByPks(List<String> listPk);

	/**
	 * 保存时查询其他屏幕已存在的诊室
	 * @param listPk PK_DEPTUNIT集合
	 * @param pkQcscreen PK_QCSCREEN
	 * @return 诊室名称
	 */
	List<String> qryExists(@Param("list") List<String> listPk, @Param("pkQcscreen") String pkQcscreen);

}
