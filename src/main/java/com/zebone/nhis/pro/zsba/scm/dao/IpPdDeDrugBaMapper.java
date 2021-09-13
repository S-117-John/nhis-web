package com.zebone.nhis.pro.zsba.scm.dao;

import com.zebone.nhis.common.module.scm.pub.BdPdDecate;
import com.zebone.nhis.pro.zsba.scm.vo.IpDeDrugBaDto;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface IpPdDeDrugBaMapper {
	/**
	 * 查询住院药房请领单数，按科室汇总展示
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryPdApplyListByDept(Map<String, Object> paramMap);

	/**
	 * 查询草药处方列表
	 *
	 * @param ipDeDrugBaDto
	 * @return
	 */
    public List<Map<String, Object>> queryPressList(IpDeDrugBaDto ipDeDrugBaDto);

	/**
	 * 住院医嘱发药查询请领单
	 *
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> qryExPdApplyList(Map<String, Object> paramMap);

	/**
	 * 查询停发医嘱
	 *
	 * @param paramMap{"pkPdaps":"领药申请单号（List）","codePv":"就诊号"}
	 * @return
	 */

	public List<Map<String, Object>> qryStopApply(Map<String, Object> paramMap);

	public int queyOrderApplyNumber(String pkDept);

	public int queyPresApplyNumber(String pkDept);

	public int queyDeptApplyNumber(String pkDept);

	public int queyEmerApplyNumber(String pkDept);


	public List<Map<String, Object>> qryPdApplyListEmerByDept(Map<String, Object> paramMap);

	/**
	 * 医嘱停发处理
	 *
	 * @param paramMap {"pkPdapDts":"领药明细主键集合(List<String>)",
	 *                 "dtExdeptpdstop":"停发原因",
	 *                 "reasonStop":"原因描述",
	 *                 "pkEmpStop":"停发人Id",
	 *                 "nameEmpStop":"停发人姓名"}
	 */
	public int saveStopApplyReason(Map<String, Object> paramMap);

	/**
	 * 取消停发
	 *
	 * @param pkPdapDts{"pkPdapDts":"领药明细主键list"}
	 */
	public int cancelStopApply(List<String> pkPdapDts);

	/**
	 * @return java.util.List<com.zebone.nhis.common.module.scm.pub.BdPdcate>
	 * @Description 根据业务和使用科室查询发放分类
	 * @auther wuqiang
	 * @Date 2020-07-29
	 * @Param [ipDeDrugDto]
	 */
	List<BdPdDecate> getBdPdCates(IpDeDrugBaDto ipDeDrugDto);

	/**
	 * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
	 * @Description 查询医嘱发药业务中发送分类是否有发药明细
	 * @auther wuqiang
	 * @Date 2020-07-30
	 * @Param [bdPdcates, ipDeDrugDto]
	 */
	List<Map<String, Object>> getCnOrderNotEmpty(@Param("bdPdcates") List<BdPdDecate> bdPdcates, @Param("ipDeDrugDto") IpDeDrugBaDto ipDeDrugDto);

	List<Map<String, Object>> getCnOrderDeptNotEmpty(@Param("bdPdcates") List<BdPdDecate> bdPdcates, @Param("ipDeDrugDto") IpDeDrugBaDto ipDeDrugDto);

	int  queyDeptApplyBackNumber(String pkDept);
	
	//ba-根本pkSettle查询已配药药蓝信息
	List<Map<String, Object>> queyPresCodeBasket(Map<String, Object> paramMap);
	//ba-根本pkPresocc查询未发药处方数量
	public int queyPresConfNumber(Map<String, Object> paramMap);
	
	int qryPresPending(Map<String, Object> paramMap);
	/**
	 * 查询发药打印记录
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> qryDeDrugPrintRecord(IpDeDrugDto ipDeDrugDto) throws BusException;
	
	//查询处方信息-根据presNo
	List<Map<String, Object>> qryPresPresNo(Map<String, Object> paramMap);
	
	/**
	 * 查询处方明细
	 * @param paramMap{"pk_presocc":"处方主键 ","pkDept":"当前科室"}
	 * @return 处方明细
	 */
	public List<Map<String,Object>> qryPresDetail(Map<String,Object> paramMap);
	
	
	/**
	 * 查询未完成处方信息
	 * @param paramMap{"pkPv":"就诊主键","pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryUnFinishedPresInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询处方明细信息
	 * @param paramMap{"pkPresocc":"处方执行主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryPresDetialInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询处方药篮统计信息
	 * @param paramMap{"pkPv":"就诊主键"}
	 * @return
	 */
	public List<Map<String,Object>> qryPresBasketPkPvInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询统计当前药房科室未发处方数
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresConf(Map<String,Object> paramMap);
	
	/**
	 * 查询统计所有药房科室未发处方数
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPresConfAll(Map<String,Object> paramMap);
	
	
	public List<Map<String,Object>> qryPvDiag(List<String> pkPvs);
	
	/**
	 * 退药处方查询
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPresOccList(Map<String,Object> paramMap);

	/**
	 * 退药处方执行明细
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> qryPresDt(Map<String,Object> map);
	
	/**
	 * 查询未完成患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryUnFinishedPiInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询完成患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryFinishedPiInfo(Map<String,Object> paramMap);
	/**
	 * 查询暂挂患者信息
	 * @param paramMap{"pkDept":"发药药房","winno":"药房窗口"}
	 * @return
	 */
	public List<Map<String,Object>> qryPendingPiInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询过敏信息
	 * @param paramMap{"pkPiS":"pkpi集合"}
	 * @return
	 */
	public List<Map<String,Object>> qryPiAllergicNameAllInfo(Map<String,Object> paramMap);
}
