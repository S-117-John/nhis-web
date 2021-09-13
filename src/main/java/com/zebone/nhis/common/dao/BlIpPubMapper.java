package com.zebone.nhis.common.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlDepositPi;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.BlOpFee;
import com.zebone.nhis.common.module.bl.BlSettle;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface BlIpPubMapper {
	
	
	/**
	 * 根据退费参数查询记费记录
	 * @param vos
	 * @return
	 */
	public List<BlIpDt> QryBlDtIpByRefunds(List<RefundVo> vos);
	
	public BlSettle QryBlSettleByPk(@Param(value = "pkSettle") String pkSettle);
	
	public List<BlIpDt> QryBlIpDtBySt(@Param(value = "pkSettle") String pkSettle);
	
	public List<BlDeposit> QryBlDepositBySt(@Param(value = "pkSettle") String pkSettle);
	
	public List<BlDepositPi> QryBlDepositPiBySt(@Param(value = "pkSettle") String pkSettle);
	
	public List<BdItem> getBdItemsByCon(List<BlIpDt> list);

	public List<Map<String, Object>> qryStrikePatis(Map<String, Object> paraMap);
	
	public List<BlIpDt> getRefundData(Map<String,Object> map);
	
	public List<BlOpFee> qryOrdFees(List<String> pks);
	
	public void  medExeOp(Map<String,Object> map);

	 @Select("SELECT dt.* FROM Bl_ip_dt dt WHERE (dt.PK_CGIP_BACK is null or dt.PK_CGIP_BACK='' ) and dt.pk_ordexdt = #{pkExocc} and not exists(select 1 from Bl_ip_dt dtBack  where dt.pk_cgip = dtBack.pk_cgip_back)")
	public List<BlIpDt> qryByPkExocc(@Param(value = "pkExocc")String pkExocc);

	 @Select("SELECT * FROM bd_pd WHERE pk_pd = #{pkPd} ")
	public BdPd getPdInfoByPk(@Param(value = "pkPd")String pkPd);

	public List<CnOrder> qryOrdByPkAss(List<String> pkAssOccs);

	public List<CnOrder> qryOrdByPkAssOcc(List<String> pkAssOccs);
	
	public void updOrder(List<CnOrder> cnOrds);
	/**
	 * 更新医嘱为执行状态
	 * @param params
	 */
	public void updateOrderToExec(Map<String,Object> params);

	public void updOrderApply(List<String> pks);

	public void updOrderPa(List<String> pks);

	public List<BlIpDt> qryCgIps(Map<String,Object> params);
	//public List<CnRisApply> QryApplyByOrd(List<CnOrder> cnOrds);
	@Select("SELECT * FROM bl_deposit WHERE pk_st_mid =  #{pkStMid}")
	public List<BlDeposit> QryBlDepositByMidSt(@Param(value = "pkStMid")String pkStMid);
	
	/**
	 * 查询已提交申请单数量
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public Integer queryMedAppSubmitCount(Map<String,Object> paramMap)throws BusException;
	/**
	 * 查询申请单对应未签署医嘱
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public Integer queryMedAppOrdCount(Map<String,Object> paramMap)throws BusException;
	/**
	 * 查询医技申请的对应的医技医嘱
	 * @param ordParents
	 * @return
	 * @throws BusException
	 */
	public List<CnOrder> queryMedOrdList(List<String> ordParents)throws BusException;
	
	/**
	 * 查询待处理医疗执行记录
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String,Object>> queryMedToDoExList(Map<String,Object> paramMap)throws BusException;
    /**
     * 根据医嘱项目主键查询对应收费项目
     * @param pkCnord
     * @return
     * @throws BusException
     */
	public List<Map<String,Object>> queryItemListByOrd(Map<String,Object> paramMap) throws BusException;
	/**
	 * 查询医技申请单
	 * @param paramMap
	 * @return
	 * @throws BusException
	 */
	public List<Map<String,Object>> queryMedAppList(Map<String,Object> paramMap) throws BusException;

	
	/**
	 * 查询患者特诊加收金额
	 * @param pkCgips
	 * @param pkPv
	 * @param dateBegin
	 * @param dateEnd
	 * @return
	 */
	public Double qryAmountAddByPv(@Param("pkCgips") List<String> pkCgips,@Param("pkPv") String pkPv,@Param("dateBegin")Date dateBegin,@Param("dateEnd")Date dateEnd);

	/**查询待退费项目可退数量*/
	public List<BlIpDt> qryItemAllowQuanRe(List<RefundVo> vos);

}
