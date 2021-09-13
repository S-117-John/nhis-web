package com.zebone.nhis.ma.pub.platform.send.impl.yh.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd.BdPd;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd.ExPdApply;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface SendMsgMapper {

	/**
	 * 得到作废的医嘱号
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<CnOrder> getCnoderNo(Map<String, Object> paramMap);

	public List<Map<String, Object>> getBlInfo(String pkCnord);

	/**
	 * 根据就诊主键获取床位信息
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> queryPiInfo(String string);

	/**
	 * 根据就诊主键查询出院信息
	 * @param string
	 */
	public List<Map<String, Object>> queryOutInfo(String string);
	
	/**
	 * 得到作废医嘱的费用信息
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<BlIpDt> getCnoderBl(Map<String, Object> paramMap);

	/**
	 * 药品请领单
	 * @param pk
	 * @return
	 */
	public ExPdApply getExPdApply(String pk);

	/**
	 * 获取药品信息
	 * @param pk
	 * @return
	 */
	public BdPd getBdPd(String pk);

	/**
	 * 获取科室编码
	 * @param pk
	 * @return
	 */
	public String getCodeDept(String pk);

	/**
	 * 根据医嘱主键查询具体信息
	 * @param pk
	 * @return
	 */
	public CnOrder queryCnOrder(String pk);
}
