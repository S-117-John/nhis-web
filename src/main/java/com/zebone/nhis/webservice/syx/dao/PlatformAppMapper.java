package com.zebone.nhis.webservice.syx.dao;

import com.zebone.nhis.webservice.syx.vo.platForm.*;
import com.zebone.platform.modules.mybatis.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PlatformAppMapper {
	
	/**
	 * 住院患者信息查询接口
	 * @param piInfo
	 * @return
	 */
	public List<ResIpUserInfo> qryPiInfoForIn(ReqPiInfo piInfo);
	
	/**
	 * 住院费用查询接口
	 * @param reqvo
	 * @return
	 */
	public QryPiCostResVo getInPatientFeeInfo(QryPiCostReq reqvo);
	
	/**
	 * 按金缴纳记录查询接口
	 * @param reqvo
	 * @return
	 */
	public List<QryPiDeposOrderInfo> getInPatientForegiftInfo(QryPiDeposReqVo reqvo);
	
	/**
	 * 一日清单基本信息查询接口:基本信息
	 * @param reqvo
	 * @return
	 */
	public QryPiDeposResVo getInPiListOfDayInfo(QryPiDeposReqVo reqvo);
	
	/**
	 * 一日清单基本信息查询接口:项目信息
	 * @param paramMap
	 * @return
	 */
	public List<QryPiDeposOrderInfo> getInPiItemOfDayInfo(Map<String, Object> paramMap);

	/**
	 * 一日清单项目明细查询接口
	 * @param reqVo
	 * @return
	 */
	public List<QryPiItemInfo> getInPiListOfDayItemInfo(QryPiDeposReqVo reqVo);
	
	/**
	 * 日清单项目分类查询接口
	 * @param reqVo
	 * @return
	 */
	public List<QryPiDeposOrderInfo> getInPiListOfDayItemKind(QryPiDeposReqVo reqVo);
	
	public List<QryPatiInfoVo> getPiMasterListNoPhoto(Map<String,Object> piMaster);

	/**
	 * 获取HIS系统对帐信息接口
	 * @param chkInfo
	 * @return
	 */
	public List<QryChecklistInfoResData> getChkInfoList(QryCheckListInfo chkInfo);

	/**
	 * 查询出院结算信息接口（新增）
	 * @param reqVo
	 * @return
	 */
    public QryAppSettleResVo getSettleAccounts(QryAppSettleReqVo reqVo);

	/**
	 * 根据科室主键查询对应病区
	 * @param pkDept
	 * @return
	 */
	public List<Map<String, Object>> getPkDeptNs(String pkDept);

    /**
     * 获取科室信息
     * @return
     */
    public List<QryAppItem> getDeptInfos();
}
