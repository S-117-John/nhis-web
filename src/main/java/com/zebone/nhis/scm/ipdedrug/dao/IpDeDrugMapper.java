package com.zebone.nhis.scm.ipdedrug.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.scm.ipdedrug.vo.DrugApDeptVo;
import com.zebone.nhis.scm.ipdedrug.vo.IpDeDrugDto;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface IpDeDrugMapper {

	/**
	 * 查询医嘱请领单明细明细
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> qryAppListDetail(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询处方请领单明细发药明细
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> qryIpDePresDrugDetailByCDT(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询发药打印记录
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> qryDeDrugPrintRecord(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询退药请领明细
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> qryIpReBackDrugDetailByCDT(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询发药记录
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> queryDeDrugDetail(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询发药单
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> queryDeDrugList(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询请领单明细
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> queryApDrugDetail(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 查询请领单明细(科室领药)
	 * @param ipDeDrugDto
	 * @return
	 * @throws BusException
	 */
	List<Map<String, Object>> queryApDrugDetailDept(IpDeDrugDto ipDeDrugDto) throws BusException;

	/**
	 * 医嘱停发处理
	 * @param paramMap
	 * {"pkPdapDts":"领药明细主键集合(List<String>)",
	 * "dtExdeptpdstop":"停发原因",
	 * "reasonStop":"原因描述",
	 * "pkEmpStop":"停发人Id",
	 * "nameEmpStop":"停发人姓名"}
	 */
	public int saveStopApplyReason(Map<String,Object> paramMap);
	
	/**
	 * 查询停发医嘱
	 * @param paramMap{"pkPdaps":"领药申请单号（List）","codePv":"就诊号"}
	 * @return
	 */
	public List<Map<String,Object>> qryStopApply(Map<String,Object> paramMap);
	
	/**
	 * 取消停发
	 * @param pkPdapDts{"pkPdapDts":"领药明细主键list"}
	 */
	public int cancelStopApply(List<String> pkPdapDts);
	
	/**
	 * 查询处方汇总
	 * @param paramMap{"pkStore":"仓库主键","pkPreses":"处方集合"}
	 * @return
	 */
	public List<Map<String,Object>> qrySumPres(Map<String,Object> paramMap);
	
	/**
	 * 住院医嘱发药查询请领单
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryExPdApplyList(Map<String,Object> paramMap);
	
	/**
	 * 发药单查询双击明细记录调出本条发药单的执行记录
	 * @param paramMap{"pkPv":"就诊主键","codeDe":"发药单号"}
	 * @return
	 */
	public List<Map<String,Object>> qryExOrderOccInfo(Map<String,Object> paramMap);
	
	/**
	 * 发退单查询双击明细记录调出本条药品的执行记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryPdExOrderInfo(Map<String,Object> paramMap);
	
	/**
	 * 查询退药申请单
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryIpReBackAppListByCDT(Map<String,Object> paramMap);
	
	/**
	 * 住院静配发药-查询明细数据（包含用药时间）
	 * @param deDrugDto
	 * @return
	 */
	public List<Map<String,Object>> qryPdApdtRelExInfo(IpDeDrugDto deDrugDto);
	
	/**
	 * 住院静配发药-查询请领明细的执行数据
	 * @param pkPdapdts
	 * @return
	 */
	public List<Map<String,Object>> qryPivasPdExInfo(List<String> pkPdapdts);
	
	/**
	 * 住院静配退药-查询退药申请明细执行数据
	 * @param deDrugDto
	 * @return
	 */
	public List<Map<String,Object>> qryBackDrugPivasInfo(IpDeDrugDto deDrugDto);
	
	/**
	 * 查询住院药房请领单数，按科室汇总展示
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryExPdApplyListBySum(Map<String,Object> paramMap);

	/**
	 * 查询医嘱请领明细数据（根据选择的请领科室数据）
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryDrugDtApList(Map<String,Object> paramMap);
	
	/**
	 * 查询处方请领明细 （根据选择的请领科室数据）
	 * @param ipDeDrugDto
	 * @return
	 */
	public List<Map<String,Object>> qryPresDrugApList(IpDeDrugDto ipDeDrugDto);
	
	/**
	 * 查询停发申请明细
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> qryStopApDtByDeptAp(Map<String,Object> paramMap);
	
	/**
	 * 住院医嘱发退药-查询发/退申请单信息
	 * @param paramMap
	 * @return
	 */
	public List<DrugApDeptVo> queryAllApplyDrugList(Map<String,Object> paramMap);
	
	/**
	 * 住院医嘱发退药-查询退药记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryApDtretDrugList(Map<String,Object> paramMap);
	
	/**
	 * 住院医嘱发退药-查询发药记录
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryApDtDrugList(Map<String,Object> paramMap);
	
	/**
	 * 发药单查询，医嘱发药单查询-汇总查询
	 * @param ipDeDrugDto
	 * @return
	 */
	public List<Map<String,Object>> queryDeDrugSumList(IpDeDrugDto ipDeDrugDto);
	
	/**
	 * 查询科室待发药数据量
	 * @param paramMap
	 * @return
	 */
	public int getUnfinishDrugAp(Map<String,Object> paramMap);
	
	/**
	 * 查询申请单数据按科室
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryExApplyForDept(Map<String,Object> paramMap);
	
	/**
	 * 查询申请单明细数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryExApplyDtForDept(Map<String,Object> paramMap);

	/**
	 * 根据请领单查询发药明细
	 * @param ipDeDrugDto
	 * @return
	 */
	List<Map<String, Object>> queryDeDetailByAp(IpDeDrugDto ipDeDrugDto);
}
