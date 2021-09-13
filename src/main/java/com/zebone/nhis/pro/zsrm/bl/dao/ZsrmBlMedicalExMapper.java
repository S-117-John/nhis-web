package com.zebone.nhis.pro.zsrm.bl.dao;

import com.zebone.nhis.pro.zsrm.bl.vo.CnOrderVo;
import com.zebone.nhis.pro.zsrm.bl.vo.ExtCostOccVo;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 医技执行接口服务
 * 
 */
@Mapper
public interface ZsrmBlMedicalExMapper {

	/**
	 * 门诊医技申请单查询
	 * 
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> qryOpMedAppInfo(Map<String, Object> paramMap) throws BusException;

	/**
	 * 医技执行
	 * @param paramMap
	 */
	public void medExeOcc(Map<String, Object> paramMap);

	/**
	 * 查询医技申请单执行记录(门诊、住院及医疗记录查询公用)
	 *
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String, Object>> queryExAssistOccList(Map<String, Object> paramMap) throws BusException;

	/**
	 * 门诊数据校验
	 *
	 * @param string
	 */
	public int opDataCheck(String string);

	//根据执行科室查询对应的诊区
	public List<String> qeryDeptAreaZsrm(String pkDeptExec);

	//查询自定义扩展属性0601日间手术室科室
	public String qeryDeptOperationZsrm(String pkDept);

	/**
	 * 药品附加费用执行--查询患者医嘱信息
	 * @param pkPv
	 * @return
	 */
	public List<CnOrderVo> getPvOrdInfo(Map<String, Object> pkPv);

	/**
	 * 药品附加费用执行--根据医嘱查询收费项目
	 * @param pkCnords
	 * @return
	 */
	public List<ExtCostOccVo> getExtCostByOrd(@Param("pkCnords") List<String> pkCnords);
}
