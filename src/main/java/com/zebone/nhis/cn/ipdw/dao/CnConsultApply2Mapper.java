package com.zebone.nhis.cn.ipdw.dao;

import com.zebone.nhis.cn.ipdw.vo.OrdCaVo;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CnConsultApply2Mapper {
	
	/**
	 * 查询会诊列表信息
	 * @param pkPv 就诊主键
	 * @return
	 */
	public List<Map<String,Object>> qryOrderConsultList(String pkPv);
	
	/**
	 * 查询会诊申请信息
	 * @param pkCnord 医嘱主键
	 * @return
	 */
	public Map<String,Object> qryConsultApplyInfo(String pkCnord);
	
	/**
	 * 查询受邀会诊信息
	 * @param pkCons 会诊申请主键
	 * @return
	 */
	public List<Map<String,Object>> qryConsultResList(String pkCons);
	
	/**
	 * 作废会诊申请记录
	 * @param paramMap
	 */
	public void updateConsultInfo(Map<String, Object> paramMap);

	/**
	 * 查询会诊应答保存签名
	 * @param pkConsrep
	 * @return
	 */
	public OrdCaVo qryConRespEmp(String pkConsrep);

	/**
	 * 会诊项目费用查询
	 * @param pk_org
	 * @param pk_ord
	 * @return
	 */
	public List<Map<String,Object>> queryItemBySrv(Map<String, Object> paramMap);

	List<Map<String,Object>> qryConsultApplyAndPiInfo(Map<String, Object> paramMap);
}
