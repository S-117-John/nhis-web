package com.zebone.nhis.pi.fix.dao;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.pi.fix.vo.PageQryPiParam;
import com.zebone.nhis.pi.fix.vo.PiParamsDto;
import com.zebone.nhis.pi.pub.vo.PiMasterVo;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pi.acc.PiCardIss;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.pi.pub.vo.PiMasterAndAddr;
import com.zebone.nhis.pi.pub.vo.PibaseVo;
import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 患者基本信息（包含患者信息：基本、地址、家庭关系、保险计划）
 * 
 * @author wangpeng
 * @date 2016年9月10日
 *
 */
@Mapper
public interface PibaseMapper {
	
	/** 获取患者基本信息列表(不含照片) */
	List<PiMasterVo> getPiMasterListNoPhoto(PiMaster piMaster);
	
	/** 根据保险计划主键获取保险计划信息 */
	PiInsurance getPiInsuranceById(String pkInsurance);
	
	/** 根据患者就诊信息查询诊断信息vo列表 */
	List<PibaseVo> getPibaseVoList(PvEncounter pvEncounter);
	
	/** 查询当前机构下的患者分类 */
	List<PiCate> findAllCates();
	
	/**可用的卡登记记录*/
	List<PiCardIss> getPiCardIssList(PiCardIss piCardIss);
	
	/**查询领卡记录*/
	List<PiCardIss> queryPiCardIssList(Map map);  
	//查询数据库中所有已用卡号
	List<PiCardIss> getPiCardIssLists(@Param(value="pkCardIss")String pkCardIss);
	
	/**查询患者基本信息(照片 + 地址)*/
	List<PiMasterAndAddr> getPiMasterAndAddr(Map map);

    List<PiMasterVo> queryPiMasterList(@Param("params") PiParamsDto params);

}
















