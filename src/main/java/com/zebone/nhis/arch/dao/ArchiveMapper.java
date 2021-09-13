package com.zebone.nhis.arch.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.arch.vo.ArchOpDocInfo;
import com.zebone.nhis.arch.vo.ArchiveDto;
import com.zebone.nhis.arch.vo.PvArchPrt;
import com.zebone.nhis.common.module.arch.ArchPv;
import com.zebone.nhis.common.module.arch.PvArchDoc;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface ArchiveMapper {

	/**
	 * 归档管理_查询患者信息
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryArchManagerPatiInfo(ArchiveDto archiveDto);

	/**
	 * 归档管理_检查归档情况
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryArchManagerCheck(ArchiveDto archiveDto);

	/**
	 * 病历管理_查询病历归档计记录
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryMRArchRecord(ArchiveDto archiveDto);
	/**
	 * 病历管理_查询病历归档计记录
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryArchPatInfo(ArchiveDto archiveDto);

	/**
	 * 病历管理_查询病历归档计记录(病历检索功能使用)
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryMRArchRecordFlagIn(ArchiveDto archiveDto);
	/**
	 * 查询病历点评文件
	 * @param archiveDto
	 * @return
	 */
	List<Map<String, Object>> queryMRReview(ArchiveDto archiveDto);
	
	/**
	 * 查看归档的纸质分类文件
	 * @param pkPv
	 * @param pkDocType
	 * @return
	 */
	public List<PvArchDoc> qryDocByPv(@Param("pkPv")String pkPv, @Param("pkDocType")String pkDocType);
	/**
	 * 根据系统分类查询已上传的文件类型
	 * @param sysType
	 * @param pkArchive
	 * @return
	 */
	public List<String> queryArchDocList(@Param("sysType")String sysType,@Param("pkArchive")String pkArchive);
	/**
	 * 根据系统分类查询已上传的文件类型
	 * @param sysType
	 * @param pkArchive
	 * @return
	 */
	public List<String> queryRecTypeList(@Param("pkPv")String pkPv);
	/**
	 * 查询科室患者列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryArchPatList(Map<String, Object> map);
	/**
	 * 查询科室患者列表--产房
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryArchPatListByLabor(Map<String, Object> map);
	/**
	 * 查询出院患者列表
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryArchLeavePatList(Map<String, Object> map);
	/**
	 * 查询转科患者列表
	 * @param map
	 * @return
	 */	
	public List<Map<String, Object>> qryArchChangePatList(Map<String, Object> map);
	/**
	 * 查询会诊患者列表
	 * @param map
	 * @return
	 */	
	public List<Map<String, Object>> qryArchConsulPatList(Map<String, Object> map);
	
	/**
	 * 根据检查申请单号查询报告单号
	 * @param codeApply
	 * @return
	 */
	public String queryRisCodeRpt(@Param("codeApply")String codeApply);
	
	/**
	 * 根据检验申请单号查询报告单号
	 * @param codeApply
	 * @return
	 */
	public String queryLisCodeRpt(@Param("codeApply")String codeApply);
	/**
	 * 根据CodePv查询打印条码的数据
	 * @param codePv
	 * @return
	 */
	public Map<String, Object> queryBarCodeByCodePv(@Param("codePv")String codePv);
	/**
	 * 打印登记查询患者
	 * @param map
	 * @return
	 */
	public List<Map<String ,Object>> queryPrintNewPatInfo(Map<String, Object> map);
	/**
	 * 打印登记查询患者
	 * @param map
	 * @return
	 */
	public List<Map<String ,Object>> queryPrintUpdPatInfo(Map<String, Object> map);
	/**
	 * 打印登记查询患者
	 * @param map
	 * @return
	 */
	public List<PvArchPrt> queryContentByPk(Map<String, Object> map);
	/**
	 * 查询归档文件
	 * @param archiveDto
	 * @return
	 */
	public List<Map<String, Object>> queryArchManagerDoc(Map<String,Object> map);
	
	public List<Map<String, Object>> queryArchManagerDocOp(Map<String,Object> map);
	/**
	 * 查询归档文件(归档管理使用,排序特殊)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryArchMgrDoc(Map<String,Object> map);
	/**
	 * 查询门诊的归档报告
	 * @param map
	 * @return
	 */
	public List<ArchOpDocInfo> queryArchLogOpDoc(Map<String,Object> map);
	public List<ArchOpDocInfo> queryArchLogOpDocOld(Map<String,Object> map);
	/**
	 * 查询旧系统患者报告(博爱)
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> qryArchOldIpDocInfo(Map<String,Object> map);
	/**
	 * 查询患者的检查未归档列表--博爱
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> queryArchPacsListBoai(@Param("codeIp")String codeIp,@Param("times")String times);
	public List<String> queryArchCodeRptBoai(Map<String,Object> map);
	public List<ArchPv> queryArchPv(Map<String,Object> map);
	public List<String> queryCodeArchByDate(String codeArch);
}
