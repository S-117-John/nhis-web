package com.zebone.nhis.webservice.zhongshan.dao;

import java.util.List;
import java.util.Map;

import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * @Classname ThiIpCnWebMapper
 * @Description 三方接口医嘱相关查询
 * @Date 2021-04-13 15:42
 * @Created by wuqiang
 */
@Mapper
public interface ZsbaOutPatientOrderMapper {
    //查询住院检验医嘱申请单信息
	public List<Map<String, Object>> queryIpLabOrderApply(Map<String, Object> paramMap);
	//查询门诊检验医嘱申请单信息
	public List<Map<String, Object>> queryOpLabOrderApply(Map<String, Object> paramMap);
	//根据医嘱序号集合或标本编号(条码编号)查询医嘱相关信息
	public List<Map<String, Object>> queryOrdsnOrderInfo(Map<String, Object> paramMap);
	/**
	 * 门诊数据校验
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public int opDataCheck(Map<String, Object> paramMap);

	/**
	 * 住院数据校验
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public int ipDataCheck(Map<String, Object> paramMap);
	
	public int updateCnLabApply(Map<String, Object> paramMap);
	
	/**
	 * 医技执行
	 *
	 * @param pkAssOcc
	 * @param nameEmpOcc
	 * @param pkEmpOcc
	 * @param dataOcc
	 */
	public void medExeOcc(Map<String, Object> paramMap);
	
	/**
	 * 取消执行
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public void cancleExocc(Map<String, Object> paramMap);
	
	/**
	 * 查询检验医嘱信息
	 * @param paramMap{"pkOrd":医嘱姓名主键}
	 * @return
	 */
	public Map<String, Object> queryBdOrdLabInfo(Map<String, Object> paramMap);
	
	/**
	 * 查询处方主键和名称
	 * @param list {codeApply}
	 * @return
	 */
	public List<Map<String, Object>> queryPkCnordCodeInfo(List<String> list);
	
}
