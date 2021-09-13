package com.zebone.nhis.ma.pub.platform.sd.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.bl.pub.vo.BlPatiCgInfoNotSettleVO;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 门诊查询
 * @author JesusM
 *
 */
@Mapper
public interface SDOpMsgMapper {

	/**
	 * 查询门诊患者信息以及就诊信息，主要构建PID，PV1消息，其他节点请自行拓展不要在此接口追加
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> queryPatListOp(Map<String,Object> paramMap);

	/**
	 * 查询排班字典根据时间分组信息
	 * @param pkSchs
	 * @return
	 */
	public List<Map<String,Object>> qryArrGroup(Map<String,Object> paramMap);

	/**
	 * 查询排班字典信息--根据单个主建查询
	 * @param pkSchs
	 * @return
	 */
	public List<Map<String,Object>> qrySchDictByOneKey(Map<String,Object> pkSchs);

	/**
	 * 查询排班字典信息--根据多个主建查询
	 * @param pkSchs
	 * @return
	 */
	public List<Map<String,Object>> qrySchDict(List<String> pkSchs);

	/**
	 * 查询门诊结算发票数据
	 * @param pkSettle
	 * @return
	 */
	public Map<String,Object> qrySettleForInvData(String pkSettle);

	//体检获得费用Vo-检验容器
	public List<BlPubParamVo> qryLabContCgVo(Map<String,Object>map);

	//体检获得费用Vo-检验标本
	public List<BlPubParamVo> qryLabSampCgVo(Map<String,Object>map);
	//生成医嘱号
	//Double selectSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName);
    //int updateSn(@Param("tableName") String tableName, @Param("fieldName") String fieldName, @Param("count") int count);
	/**
	 * 查询门诊发票明细数据
	 * @param pkSettle
	 * @return
	 */
	public List<Map<String,Object>> qryInvDtData(String pkSettle);

	/**
	 * 查询门诊收费明细数据
	 * @param pkSettle
	 * @return
	 */
	public List<Map<String,Object>> qryOpDtData(String pkSettle);
    /**
	 * 查询患者基本信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>> querySDOpPiMaster(Map<String,Object>map);


	 /**
	 * 查询门诊排班相关信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String,Object>>  querySDOpSchAllInfo(Map<String,Object>map);

	/**
	 * 查询挂号客户在时间段内的就诊先后顺序
	 * @param map
	 * @return
	 */
	public  List<Map<String, Object>> qryTimeSegmentSort(Map<String,Object> map);
	/**
	 * 待支付列表查询
	 * @param paramMap
	 * @return
	 */
	public List<BlPatiCgInfoNotSettleVO> queryNoSettleInfoForCg(Map<String,Object> paramMap);

	/**
	 * 查询手术申请数据
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> queryOperation(Map<String, Object> paramMap);
	
	/**
	 * 更新预约信息
	 * @param paramMap
	 */
	public void updateSchAppt(Map<String,Object> paramMap);
	
	
	/**
	 * 查询草药全名
	 * @param pkCnord
	 */
	public String queryBdPd(String pkCnord);

	
	/**
	 * 查询排班资源信息
	 * @param paramMap{pkSchres}
	 * @return
	 */
	public Map<String,Object> querySchResInfo(Map<String,Object> paramMap);


}
